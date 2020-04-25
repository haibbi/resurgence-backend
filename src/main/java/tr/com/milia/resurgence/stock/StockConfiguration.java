package tr.com.milia.resurgence.stock;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import tr.com.milia.resurgence.item.Item;

import java.time.Duration;

@Configuration
public class StockConfiguration {

	@Bean
	StockService stockService(ApplicationEventPublisher eventPublisher) {
		StockService stockService = new StockService(eventPublisher);

		stockService.schedule(Duration.ofSeconds(10), Item.BEER, 0.05);
		stockService.schedule(Duration.ofSeconds(30), Item.GLOCK, 0.05);
		stockService.schedule(Duration.ofMinutes(1), Item.FORD_FIESTA, 0.05);

		return stockService;
	}

}
