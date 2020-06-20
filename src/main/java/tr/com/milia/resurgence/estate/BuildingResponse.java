package tr.com.milia.resurgence.estate;

import tr.com.milia.resurgence.i18n.LocaleEnum;
import tr.com.milia.resurgence.item.Item;

import java.time.Duration;
import java.util.List;
import java.util.stream.Collectors;

class BuildingResponse implements LocaleEnum {

	private final Building building;

	private final long price;
	private final Duration duration;
	private final List<Produce> produces;

	public BuildingResponse(Building building) {
		this.building = building;
		this.price = building.getPrice();
		this.duration = building.getDuration();
		this.produces = building.getProduces().entrySet().stream()
			.map(e -> new Produce(e.getKey(), e.getValue()))
			.collect(Collectors.toList());
	}

	@Override
	public String[] getCodes() {
		return building.getCodes();
	}

	@Override
	public Object[] getArguments() {
		return building.getArguments();
	}

	@Override
	public String getDefaultMessage() {
		return building.getDefaultMessage();
	}

	@Override
	public String name() {
		return building.name();
	}

	public long getPrice() {
		return price;
	}

	public Duration getDuration() {
		return duration;
	}

	public List<Produce> getProduces() {
		return produces;
	}
}

class Produce {
	private final Item item;
	private final Long value;

	public Produce(Item item, Long value) {
		this.item = item;
		this.value = value;
	}

	public Item getItem() {
		return item;
	}

	public Long getValue() {
		return value;
	}
}
