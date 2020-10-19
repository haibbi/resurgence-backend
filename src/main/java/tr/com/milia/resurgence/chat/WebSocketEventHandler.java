package tr.com.milia.resurgence.chat;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.AbstractSubProtocolEvent;

@Component
public class WebSocketEventHandler {

	private static final Logger log = LoggerFactory.getLogger(WebSocketEventHandler.class);

	@EventListener(AbstractSubProtocolEvent.class)
	public void on(AbstractSubProtocolEvent event) {
		log.trace("AbstractSubProtocolEvent {}", event);
	}

}
