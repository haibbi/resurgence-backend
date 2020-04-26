package tr.com.milia.resurgence.bank;

import org.quartz.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tr.com.milia.resurgence.player.PlayerService;
import tr.com.milia.resurgence.smuggling.NotEnoughMoneyException;
import tr.com.milia.resurgence.task.PlayerNotFound;

import java.time.Duration;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;

@Service
public class BankService {

	private static final Logger log = LoggerFactory.getLogger(BankService.class);

	private final PlayerService playerService;
	private final Scheduler scheduler;

	public BankService(PlayerService playerService, Scheduler scheduler) {
		this.playerService = playerService;
		this.scheduler = scheduler;
	}

	@Transactional
	public long interest(String playerName, long amount) {
		var player = playerService.findByName(playerName).orElseThrow(PlayerNotFound::new);

		if (player.getBalance() < amount) throw new NotEnoughMoneyException();

		double ratio = InterestRates.find(amount).orElseThrow(InterestRateNotFound::new).ratio;

		player.decreaseBalance((int) amount); // todo https://milia.atlassian.net/browse/RSRGNC-143

		long total = amount + (long) (amount * ratio);

		scheduleInterest(playerName, total);

		return total;
	}

	@EventListener(InterestCompletedEvent.class)
	@Transactional
	public void onInterestCompletedEvent(InterestCompletedEvent event) {
		playerService.findByName(event.getPlayerName()).ifPresent(player -> {
			player.increaseBalance((int) event.getAmount());  // todo https://milia.atlassian.net/browse/RSRGNC-143
		});
	}

	private void scheduleInterest(String playerName, long amount) {
		JobKey jobId = new JobKey(playerName, "interest");

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
