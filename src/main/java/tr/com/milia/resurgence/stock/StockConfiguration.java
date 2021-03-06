package tr.com.milia.resurgence.stock;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import tr.com.milia.resurgence.item.Item;

import java.time.Duration;

@Configuration
public class StockConfiguration {

	@Bean
	@Lazy(value = false)
	StockService stockService(ApplicationEventPublisher eventPublisher) {
		StockService stockService = new StockService(eventPublisher);

		stockService.schedule(Duration.ofSeconds(10), Item.BEER, .5);
		stockService.schedule(Duration.ofMinutes(5), Item.HOUSE, .1);
		Item.sellable().forEach(item -> stockService.schedule(Duration.ofMinutes(5), item, .25));

		return stockService;
	}

}
