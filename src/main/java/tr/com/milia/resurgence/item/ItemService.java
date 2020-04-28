package tr.com.milia.resurgence.item;

import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;
import tr.com.milia.resurgence.stock.StockEvent;

@Service
public class ItemService {

	void updateItemPrice(Item item, long price) {
		item.setPrice(price);
	}

	@EventListener(StockEvent.class)
	public void onStockEvent(StockEvent event) {
		Item item = event.getItem();
		long newPrice = item.getPrice() + event.getChange();
		updateItemPrice(item, newPrice);
	}

}
