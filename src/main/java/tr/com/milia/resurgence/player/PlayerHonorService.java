package tr.com.milia.resurgence.player;

import org.quartz.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.Period;
import java.util.Date;

import static org.quartz.SimpleScheduleBuilder.simpleSchedule;

@Service
public class PlayerHonorService {

	private static final Logger log = LoggerFactory.getLogger(PlayerHonorService.class);

	private final PlayerService playerService;
	private final Scheduler scheduler;

	public PlayerHonorService(PlayerService playerService, Scheduler scheduler) {
		this.playerService = playerService;
		this.scheduler = scheduler;
	}

	@EventListener(PlayerCreatedEvent.class)
	public void onPlayerCreatedEvent(PlayerCreatedEvent event) {
		Player player = event.getSource();
		JobKey jobId = new JobKey(player.getName(), "honor-weekly");

		JobDetail jobDetail = JobBuilder.newJob(HonorJob.class)
			.withIdentity(jobId)
			.usingJobData("playerName", player.getName())
			.build();

		Period weekPeriod = Period.ofWeeks(1);

		Trigger trigger = TriggerBuilder.newTrigger()
			.forJob(jobDetail)
			.startAt(Date.from(Instant.now().plus(weekPeriod)))
			.withSchedule(simpleSchedule()
				.withIntervalInHours(weekPeriod.getDays() * 24)
				.repeatForever()
				.withMisfireHandlingInstructionNowWithExistingCount())
			.build();

		try {
			scheduler.scheduleJob(jobDetail, trigger);
		} catch (SchedulerException e) {
			log.error("An error occurred while scheduling player honor job {}", jobId, e);
		}
	}

	@EventListener(HonorPeriodCompletedEvent.class)
	@Transactional
	public void onHonorPeriodCompletedEvent(HonorPeriodCompletedEvent event) {
		playerService.findByName(event.getPlayerName()).ifPresent(player -> {
			player.increaseUsableHonor(10); // todo level'e göre değişken alması lazım
		});
	}

}
