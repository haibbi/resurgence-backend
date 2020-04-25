package tr.com.milia.resurgence.stock;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.context.ApplicationEventPublisher;
import tr.com.milia.resurgence.item.Item;

import java.time.Duration;

import static org.mockito.Mockito.*;

class StockServiceTest {

	StockService service;
	ApplicationEventPublisher eventPublisher;

	@BeforeEach
	void setUp() {
		eventPublisher = mock(ApplicationEventPublisher.class);
		service = new StockService(eventPublisher);
	}

	@Test
	void scheduleTest() throws InterruptedException {
		service.schedule(Duration.ofMillis(100), Item.BEER, .05);
		Thread.sleep(1000);

		verify(eventPublisher, atLeast(10)).publishEvent(any(StockEvent.class));
	}
}
