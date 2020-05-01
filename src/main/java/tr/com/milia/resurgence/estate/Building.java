package tr.com.milia.resurgence.estate;

import tr.com.milia.resurgence.item.Item;

import java.time.Duration;
import java.util.Map;

import static java.util.Map.of;

public enum Building {

	HOTEL(5_000_000, Duration.ofDays(1), of(Item.MONEY, 10_000L)),
	BEER_FACTORY(5_000_000, Duration.ofDays(1), of(Item.BEER, 1_000L));

	private final long price;
	private final Duration duration;
	private final Map<Item, Long> produces;

	Building(long price, Duration duration, Map<Item, Long> produces) {
		this.price = price;
		this.duration = duration;
		this.produces = produces;
	}

	public long getPrice() {
		return price;
	}

	public Duration getDuration() {
		return duration;
	}

	public Map<Item, Long> getProduces() {
		return produces;
	}
}
