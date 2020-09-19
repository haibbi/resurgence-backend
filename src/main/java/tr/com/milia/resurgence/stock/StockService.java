package tr.com.milia.resurgence.stock;

import org.springframework.context.ApplicationEventPublisher;
import tr.com.milia.resurgence.RandomUtils;
import tr.com.milia.resurgence.item.Item;

import java.time.Duration;
import java.util.Arrays;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

public class StockService {

	private final ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();
	private final ApplicationEventPublisher eventPublisher;
	private final Map<Item, Long> initialPrices;

	public StockService(ApplicationEventPublisher eventPublisher) {
		this.eventPublisher = eventPublisher;
		initialPrices = Arrays.stream(Item.values()).collect(Collectors.toMap(i -> i, Item::getPrice));
	}

	public void schedule(Duration duration, Item item, double change) {
		long period = duration.toMillis();
		executor.scheduleAtFixedRate(() -> update(item, change), period, period, TimeUnit.MILLISECONDS);
	}

	void update(Item item, double change) {
		double ratio = RandomUtils.random(0, change);
		if (RandomUtils.random() <= .50) ratio *= -1;

		long initialPrice = initialPrices.get(item);
		long amount = (long) (initialPrice * ratio);

		eventPublisher.publishEvent(new StockEvent(item, amount));
	}


}
