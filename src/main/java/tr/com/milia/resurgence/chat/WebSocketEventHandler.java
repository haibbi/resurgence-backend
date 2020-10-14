package tr.com.milia.resurgence.chat;

import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.socket.messaging.SessionSubscribeEvent;
import org.springframework.web.socket.messaging.SessionUnsubscribeEvent;
import tr.com.milia.resurgence.security.TokenAuthentication;

import java.security.Principal;
import java.util.Map;

public class WebSocketEventHandler {

	private final SimpMessagingTemplate template;

	public WebSocketEventHandler(SimpMessagingTemplate template) {
		this.template = template;
	}

	@EventListener(SessionSubscribeEvent.class)
	public void onSessionSubscribeEvent(SessionSubscribeEvent event) {
		send(event.getUser(), event.getMessage(), Type.SUBSCRIBE);
	}

	@EventListener(SessionUnsubscribeEvent.class)
	public void onSessionUnsubscribeEvent(SessionUnsubscribeEvent event) {
		send(event.getUser(), event.getMessage(), Type.UNSUBSCRIBE);
	}

	private void send(Principal user, org.springframework.messaging.Message<byte[]> message, Type type) {
		if (!(user instanceof TokenAuthentication)) throw new AccessDeniedException("invalid user");
		final TokenAuthentication authentication = (TokenAuthentication) user;
		final String playerName = authentication.getPlayerName();

		final Object destinationHeader = message.getHeaders().get(SimpMessageHeaderAccessor.DESTINATION_HEADER);
		if (!(destinationHeader instanceof String)) return;
		final String destination = (String) destinationHeader;

		Map<String, Object> headers = Map.of("player", playerName);
		template.convertAndSend(destination, new Message(type), headers);
	}

}
