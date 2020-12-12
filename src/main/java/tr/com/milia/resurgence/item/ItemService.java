package tr.com.milia.resurgence.item;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;
import tr.com.milia.resurgence.stock.StockEvent;

import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class ItemService {

	private static final Logger log = LoggerFactory.getLogger(ItemService.class);
	private final Map<Item, Long> initialPrices;

	public ItemService() {
		initialPrices = Arrays.stream(Item.values()).collect(Collectors.toMap(i -> i, Item::getPrice));
	}

	void updateItemPrice(Item item, long price) {
		item.setPrice(price);
	}

	@EventListener(StockEvent.class)
	public void onStockEvent(StockEvent event) {
		Item item = event.getItem();
		long change = event.getChange();
		long newPrice = initialPrices.get(item) + change;
		log.trace(item.name() + " price change. old/new/change {}/{}/{}", item.getPrice(), newPrice, change);
		updateItemPrice(item, newPrice);
	}

}
