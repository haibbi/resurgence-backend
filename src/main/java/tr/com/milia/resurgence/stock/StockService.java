package tr.com.milia.resurgence.stock;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationEventPublisher;
import tr.com.milia.resurgence.RandomUtils;
import tr.com.milia.resurgence.item.Item;

import java.time.Duration;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class StockService {

	private static final Logger log = LoggerFactory.getLogger(StockService.class);

	private final ScheduledExecutorService executor = Executors.newScheduledThreadPool(4);
	private final ApplicationEventPublisher eventPublisher;

	public StockService(ApplicationEventPublisher eventPublisher) {
		this.eventPublisher = eventPublisher;
	}

	public void schedule(Duration duration, Item item, double change) {
		long period = duration.toMillis();
		executor.scheduleAtFixedRate(() -> update(item, change), period, period, TimeUnit.MILLISECONDS);
	}

	void update(Item item, double change) {
		double ratio = RandomUtils.random(0, change);
		if (RandomUtils.random() <= .50) ratio *= -1;

		long amount = (long) (item.getPrice() * ratio);

		long newPrice = item.getPrice() + amount;

		log.debug(item.name() + " price has {}. old/new/ratio: {}/{}/{}",
			ratio < 0 ? "down" : "up", item.getPrice(), newPrice, amount);

		eventPublisher.publishEvent(new StockEvent(item, amount));
	}


}
