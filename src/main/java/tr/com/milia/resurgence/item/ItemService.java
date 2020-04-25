package tr.com.milia.resurgence.item;

import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;
import tr.com.milia.resurgence.stock.StockEvent;

@Service
public class ItemService {

	void updateItemPrice(Item item, int price) {
		item.setPrice(price);
	}

	@EventListener(StockEvent.class)
	public void onStockEvent(StockEvent event) {
		Item item = event.getItem();
		int newPrice = item.getPrice() + event.getChange();
		updateItemPrice(item, newPrice);
	}

}
