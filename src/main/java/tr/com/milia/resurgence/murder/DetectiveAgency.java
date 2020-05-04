package tr.com.milia.resurgence.murder;

import org.quartz.*;
import org.quartz.impl.matchers.GroupMatcher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tr.com.milia.resurgence.RandomUtils;
import tr.com.milia.resurgence.item.Item;
import tr.com.milia.resurgence.player.Player;
import tr.com.milia.resurgence.player.PlayerNotFound;
import tr.com.milia.resurgence.player.PlayerService;
import tr.com.milia.resurgence.skill.PlayerSkill;
import tr.com.milia.resurgence.skill.Skill;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Duration;
import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class DetectiveAgency {

	private static final Logger log = LoggerFactory.getLogger(DetectiveAgency.class);
	private static final String SCHEDULER_GROUP_NAME = "DETECTIVE_AGENCY";

	private final PlayerService playerService;
	private final Scheduler scheduler;
	private final ResearchResultRepository repository;

	public DetectiveAgency(PlayerService playerService, Scheduler scheduler, ResearchResultRepository repository) {
		this.playerService = playerService;
		this.scheduler = scheduler;
		this.repository = repository;
	}

	@Transactional
	public void hireAgent(String seekerName, String wantedName, int agentQuantity) {
		if (agentQuantity < 0) throw new IllegalArgumentException();

		try {
			long totalDetectiveCount = findRunningAgentKey().stream()
				.filter(jobKey -> jobKey.getName().startsWith(seekerName + "-"))
				.mapToInt(jobKey -> {
					final int activeAgent;
					try {
						JobDetail jobDetail = scheduler.getJobDetail(jobKey);
						JobDataMap data = jobDetail.getJobDataMap();
						activeAgent = data.getInt("agent");
					} catch (SchedulerException e) {
						log.warn("An error occurred while retrieving agent job details", e);
						return 0;
					}
					return activeAgent;
				})
				.sum();
			if (totalDetectiveCount + agentQuantity > 25) throw new AgentLimitExceededException();
		} catch (SchedulerException e) {
			log.warn("An error occurred while fetching agent job keys", e);
		}

		Player seeker = findPlayer(seekerName);
		findPlayer(wantedName); // just check if player exists

		long detectivePrice = Item.AGENT.getPrice();
		seeker.decreaseBalance(detectivePrice * agentQuantity);

		schedule(seekerName, wantedName, agentQuantity);
	}

	@Transactional
	public List<AgencyStatus> status(String seekerName) {
		Player seeker = findPlayer(seekerName);

		List<AgencyStatus> statuses =
			repository.findAllBySeekerAndExpireTimeGreaterThanEqualOrderByExpireTime(seeker, Instant.now()).stream()
				.map(AgencyStatus::report)
				.collect(Collectors.toCollection(LinkedList::new));

		try {
			statuses.addAll(searchRunningAgent(seeker));
		} catch (SchedulerException e) {
			log.warn("An error occurred while fetching agent from scheduler", e);
		}

		return statuses;
	}

	@EventListener(AgencyJobCompletedEvent.class)
	@Transactional
	public void onAgencyJobCompletedEvent(AgencyJobCompletedEvent event) {
		Player seeker = findPlayer(event.getSeeker());
		Player wanted = findPlayer(event.getWanted());
		int agentQuantity = event.getAgent();

		BigDecimal seekerSkillContribution = seeker.getSkills().stream()
			.filter(s -> s.getSkill() == Skill.TRACING)
			.findFirst()
			.map(PlayerSkill::skillContribution)
			.orElse(BigDecimal.ZERO);

		int agentSkillContribution = Item.AGENT.getSkillsContribution(Set.of(Skill.TRACING)) * agentQuantity;

		BigDecimal wantedSkillContribution = wanted.getSkills().stream()
			.filter(s -> s.getSkill() == Skill.SNEAK)
			.findFirst()
			.map(PlayerSkill::skillContribution)
			.orElse(BigDecimal.ZERO);

		// Power = Seeker Skills + Agent Skill / Wanted Skill
		BigDecimal power = seekerSkillContribution.add(BigDecimal.valueOf(agentSkillContribution))
			.divide(wantedSkillContribution, 2, RoundingMode.CEILING);

		// successRatio = Power / Power + 1
		double successRatio = power.divide(power.add(BigDecimal.ONE), RoundingMode.CEILING).doubleValue();

		double random = RandomUtils.random();

		boolean succeed = random < successRatio;

		repository.save(new ResearchResult(seeker, wanted, agentQuantity, succeed));
	}

	void clean(String attacker, String victim) {
		try {
			for (JobKey jobKey : findRunningAgentKey()) {
				if (jobKey.getName().startsWith(attacker + "-" + victim)) {
					scheduler.deleteJob(jobKey);
				}
			}
		} catch (SchedulerException e) {
			log.warn("An error occurred while removing active agent {} {}", attacker, victim, e);
		}
		repository.deleteAllBySeeker_NameAndWanted_Name(attacker, victim);
	}

	private List<AgencyStatus> searchRunningAgent(Player seeker) throws SchedulerException {
		List<AgencyStatus> statuses = new LinkedList<>();
		Set<JobKey> detectiveAgencyJobs = findRunningAgentKey();
		for (JobKey jobKey : detectiveAgencyJobs) {
			if (!jobKey.getName().startsWith(seeker.getName() + "-")) continue;

			JobDetail jobDetail = scheduler.getJobDetail(jobKey);
			JobDataMap data = jobDetail.getJobDataMap();
			String wantedName = data.getString("wanted");
			int agent = data.getInt("agent");
			Player wanted = findPlayer(wantedName);

			List<? extends Trigger> triggers = scheduler.getTriggersOfJob(jobKey);

			if (triggers == null || triggers.isEmpty()) {
				continue;
			}
			Trigger trigger = triggers.get(0);
			Date fireTime = trigger.getFinalFireTime();

			statuses.add(AgencyStatus.searching(seeker, wanted, agent, fireTime.toInstant()));
		}
		statuses.sort(Comparator.comparing(o -> o.left));
		return statuses;
	}

	private Set<JobKey> findRunningAgentKey() throws SchedulerException {
		return scheduler.getJobKeys(GroupMatcher.groupEquals(SCHEDULER_GROUP_NAME));
	}

	private void schedule(String seeker, String wanted, int agentQuantity) {
		JobKey jobKey = generateJobKey(seeker, wanted);
		JobDetail jobDetail = JobBuilder.newJob(AgencyJob.class)
			.withIdentity(jobKey)
			.usingJobData("seeker", seeker)
			.usingJobData("wanted", wanted)
			.usingJobData("agent", agentQuantity)
			.build();

		Duration duration = Duration.ofDays(1);

		Trigger trigger = TriggerBuilder.newTrigger()
			.forJob(jobDetail)
			.startAt(Date.from(Instant.now().plus(duration)))
			.build();

		try {
			scheduler.scheduleJob(jobDetail, trigger);
		} catch (SchedulerException e) {
			log.error("An error occurred while scheduling agency job {}", jobKey, e);
			throw new RuntimeException(e);
		}
	}

	private JobKey generateJobKey(String seeker, String wanted) {
		String jobName = String.join("-", seeker, wanted, UUID.randomUUID().toString());
		return JobKey.jobKey(jobName, SCHEDULER_GROUP_NAME);
	}

	private Player findPlayer(String name) {
		return playerService.findByName(name).orElseThrow(PlayerNotFound::new);
	}

}
