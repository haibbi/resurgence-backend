package tr.com.milia.resurgence.estate;

import org.quartz.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tr.com.milia.resurgence.item.Item;
import tr.com.milia.resurgence.item.PlayerItemService;
import tr.com.milia.resurgence.player.Player;
import tr.com.milia.resurgence.player.PlayerService;
import tr.com.milia.resurgence.task.PlayerNotFound;

import java.time.Duration;
import java.time.Instant;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.quartz.SimpleScheduleBuilder.simpleSchedule;

@Service
public class BuildingService {

	private static final Logger log = LoggerFactory.getLogger(BuildingService.class);

	private final DeedRepository repository;
	private final PlayerService playerService;
	private final Scheduler scheduler;
	private final PlayerItemService playerItemService;

	public BuildingService(DeedRepository repository,
						   PlayerService playerService,
						   Scheduler scheduler,
						   PlayerItemService playerItemService) {
		this.repository = repository;
		this.playerService = playerService;
		this.scheduler = scheduler;
		this.playerItemService = playerItemService;
	}

	@Transactional
	public void buy(String playerName, Building building) {
		if (repository.findById(building).isPresent()) throw new BuildingOwnerException();

		Player player = findPlayer(playerName);
		player.decreaseBalance(building.getPrice());

		repository.save(new Deed(building, player));
		schedule(playerName, building);
	}

	@Transactional
	public void sell(String playerName, Building building) {
		Optional<Deed> optionalDeed = repository.findById(building);
		if (optionalDeed.isEmpty()) throw new BuildingOwnerException();

		Deed deed = optionalDeed.get();
		Player player = deed.getPlayer();

		if (!player.getName().equals(playerName)) throw new BuildingOwnerException();

		player.increaseBalance(building.getPrice());

		repository.deleteById(building);
		cancel(playerName, building);
	}

	public List<Deed> owners() {
		return repository.findAll();
	}

	private void cancel(String playerName, Building building) {
		JobKey jobKey = generateJobKey(playerName, building);
		try {
			scheduler.deleteJob(jobKey);
		} catch (SchedulerException e) {
			log.error("An error occurred while canceling building job {}", jobKey, e);
			throw new RuntimeException(e);
		}
	}

	private void schedule(String playerName, Building building) {
		JobKey jobKey = generateJobKey(playerName, building);
		JobDetail jobDetail = JobBuilder.newJob(BuildingJob.class)
			.withIdentity(jobKey)
			.usingJobData("buildingName", building.name())
			.usingJobData("playerName", playerName)
			.build();

		Duration duration = building.getDuration();

		Trigger trigger = TriggerBuilder.newTrigger()
			.forJob(jobDetail)
			.startAt(Date.from(Instant.now().plus(duration)))
			.withSchedule(simpleSchedule()
				.withIntervalInMilliseconds(duration.toMillis())
				.repeatForever()
				.withMisfireHandlingInstructionNowWithExistingCount())
			.build();

		try {
			scheduler.scheduleJob(jobDetail, trigger);
		} catch (SchedulerException e) {
			log.error("An error occurred while scheduling building job {}", jobKey, e);
			throw new RuntimeException(e);
		}
	}

	@EventListener(BuildingPeriodCompletedEvent.class)
	@Transactional
	public void onBuildingPeriodCompleted(BuildingPeriodCompletedEvent event) {
		Building building = event.building;
		Player player = findPlayer(event.playerName);

		Map<Item, Long> produces = new HashMap<>(building.getProduces());
		log.debug("Building period completed. {} now have {}", player.getName(), produces.toString());

		Long money = produces.remove(Item.MONEY);
		if (money != null) {
			player.increaseBalance(money);
		}
		produces.forEach((item, quantity) -> playerItemService.addItem(player, item, quantity));

	}

	private Player findPlayer(String name) {
		return playerService.findByName(name).orElseThrow(PlayerNotFound::new);
	}

	private JobKey generateJobKey(String playerName, Building building) {
		return JobKey.jobKey(building.name() + "-" + playerName, "BUILDING");
	}

}
