package tr.com.milia.resurgence.chat;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionSubscribeEvent;

@Component
public class WebSocketEventHandler {

	private static final Logger log = LoggerFactory.getLogger(WebSocketEventHandler.class);

	private final ApplicationEventPublisher publisher;

	public WebSocketEventHandler(ApplicationEventPublisher publisher) {
		this.publisher = publisher;
	}

	@EventListener(value = SessionSubscribeEvent.class)
	public void on(SessionSubscribeEvent event) {
		publisher.publishEvent(new TopicSubscribeEvent(event.getMessage(), event.getUser()));
	}

}
