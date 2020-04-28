package tr.com.milia.resurgence.bank;

import org.quartz.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tr.com.milia.resurgence.player.PlayerService;
import tr.com.milia.resurgence.task.PlayerNotFound;

import java.time.Duration;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;

@Service
// todo https://milia.atlassian.net/browse/RSRGNC-143 bittiğinde güncelle
public class BankService {

	public static final String INTEREST_JOB_GROUP_NAME = "interest";
	private static final Logger log = LoggerFactory.getLogger(BankService.class);

	private final PlayerService playerService;
	private final Scheduler scheduler;

	public BankService(PlayerService playerService, Scheduler scheduler) {
		this.playerService = playerService;
		this.scheduler = scheduler;
	}

	@Transactional
	public long interest(String playerName, long amount) {
		try {
			JobDetail interest = scheduler.getJobDetail(JobKey.jobKey(playerName, INTEREST_JOB_GROUP_NAME));
			if (interest != null) throw new DualInterestException();
		} catch (SchedulerException e) {
			throw new RuntimeException(e);
		}

		var player = playerService.findByName(playerName).orElseThrow(PlayerNotFound::new);

		double ratio = InterestRates.find(amount).orElseThrow(InterestRateNotFound::new).ratio;

		player.decreaseBalance(amount);

		long total = amount + (long) (amount * ratio);

		scheduleInterest(playerName, total);

		return total;
	}

	@Transactional
	public void transfer(String fromPlayerName, String toPlayerName, long amount) {
		var fromPlayer = playerService.findByName(fromPlayerName).orElseThrow(PlayerNotFound::new);
		var toPlayer = playerService.findByName(toPlayerName).orElseThrow(PlayerNotFound::new);

		fromPlayer.decreaseBalance(amount);
		toPlayer.increaseBalance(amount);
	}

	@EventListener(InterestCompletedEvent.class)
	@Transactional
	public void onInterestCompletedEvent(InterestCompletedEvent event) {
		playerService.findByName(event.getPlayerName()).ifPresent(player -> {
			player.increaseBalance(event.getAmount());
		});
	}

	private void scheduleInterest(String playerName, long amount) {
		JobKey jobId = new JobKey(playerName, INTEREST_JOB_GROUP_NAME);

		JobDetail jobDetail = JobBuilder.newJob(InterestJob.class)
			.withIdentity(jobId)
			.usingJobData("playerName", playerName)
			.usingJobData("amount", amount)
			.build();

		Duration duration = Duration.of(1, ChronoUnit.DAYS);

		Trigger trigger = TriggerBuilder.newTrigger()
			.forJob(jobDetail)
			.startAt(Date.from(Instant.now().plus(duration)))
			.build();

		try {
			scheduler.scheduleJob(jobDetail, trigger);
		} catch (SchedulerException e) {
			log.error("An error occurred while scheduling interest job {}", jobId, e);
		}
	}

}
