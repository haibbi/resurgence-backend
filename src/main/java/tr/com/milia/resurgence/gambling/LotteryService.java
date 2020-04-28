package tr.com.milia.resurgence.gambling;

import org.quartz.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tr.com.milia.resurgence.RandomUtils;
import tr.com.milia.resurgence.player.Player;
import tr.com.milia.resurgence.player.PlayerService;
import tr.com.milia.resurgence.task.PlayerNotFound;

import java.time.Instant;
import java.util.Date;
import java.util.List;

@Service
public class LotteryService {

	public final static long LOTTERY_TICKET_PRICE = 50_000;
	private static final Logger log = LoggerFactory.getLogger(LotteryService.class);

	private final PlayerService playerService;
	private final LotteryRepository repository;

	public LotteryService(PlayerService playerService, LotteryRepository repository, Scheduler scheduler) {
		this.playerService = playerService;
		this.repository = repository;
		schedule(scheduler);
	}

	private void schedule(Scheduler scheduler) {
		JobKey jobKey = JobKey.jobKey("lottery", "GAMBLING");

		try {
			JobDetail jobDetail = scheduler.getJobDetail(jobKey);
			if (jobDetail != null) return; // lottery job already exist
		} catch (SchedulerException e) {
			throw new RuntimeException(e);
		}

		JobDetail jobDetail = JobBuilder.newJob(LotteryJob.class).withIdentity(jobKey).build();

		Trigger trigger = TriggerBuilder.newTrigger()
			.forJob(jobDetail)
			.startAt(Date.from(Instant.now()))
			// At 00:00 on Sunday. https://www.freeformatter.com/cron-expression-generator-quartz.html
			.withSchedule(CronScheduleBuilder.cronSchedule("0 0 0 ? * 1")
				.withMisfireHandlingInstructionFireAndProceed())
			.build();

		try {
			scheduler.scheduleJob(jobDetail, trigger);
		} catch (SchedulerException e) {
			log.error("An error occurred while scheduling lottery job {}", jobKey, e);
		}
	}

	@Transactional
	public void purchase(String playerName) {
		Player player = playerService.findByName(playerName).orElseThrow(PlayerNotFound::new);

		repository.save(new LotteryTicket(player));
		player.decreaseBalance(LOTTERY_TICKET_PRICE);
	}

	@Transactional
	public void finish() {
		List<LotteryTicket> allPurchasedTicket = repository.findAllByPurchaseTimeBefore(Instant.now());

		long min = allPurchasedTicket.stream().mapToLong(LotteryTicket::getId).min().orElseThrow();
		long max = allPurchasedTicket.stream().mapToLong(LotteryTicket::getId).max().orElseThrow();

		long selected = (long) RandomUtils.random(min, max);

		LotteryTicket luckTicket = repository.findById(selected).orElseThrow();

		long prize = allPurchasedTicket.size() * LOTTERY_TICKET_PRICE;

		luckTicket.getPlayer().increaseBalance(prize);

		repository.deleteAllInBatch();
	}

	@EventListener(LotteryPeriodCompletedEvent.class)
	public void onLotteryPeriodCompletedEvent() {
		finish();
	}

}
