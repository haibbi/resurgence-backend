package tr.com.milia.resurgence.chat;

import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionSubscribeEvent;
import tr.com.milia.resurgence.security.TokenAuthentication;
import tr.com.milia.resurgence.task.PlayerNotFound;

import java.security.Principal;
import java.util.Map;

@Component
public class WebSocketEventHandler {

	private final SimpMessagingTemplate template;

	public WebSocketEventHandler(SimpMessagingTemplate template) {
		this.template = template;
	}

	@EventListener(SessionSubscribeEvent.class)
	public void onSessionSubscribeEvent(SessionSubscribeEvent event) {
		final Principal user = event.getUser();
		if (!(user instanceof TokenAuthentication)) throw new AccessDeniedException("invalid user");
		final TokenAuthentication authentication = (TokenAuthentication) user;
		final String playerName = authentication.getPlayerName().orElseThrow(PlayerNotFound::new);

		final Object destinationHeader = event.getMessage().getHeaders().get(SimpMessageHeaderAccessor.DESTINATION_HEADER);
		if (!(destinationHeader instanceof String)) return;
		final String destination = (String) destinationHeader;

		template.convertAndSend(destination, new Message(Type.SUBSCRIBE), Map.of("player", playerName));
	}

}
