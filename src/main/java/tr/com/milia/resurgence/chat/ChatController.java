package tr.com.milia.resurgence.chat;

import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;
import tr.com.milia.resurgence.security.TokenAuthentication;

@Controller
public class ChatController {


	private final TopicService service;

	public ChatController(TopicService service) {
		this.service = service;
	}

	@MessageMapping("/subscriptions")
	public void subscriptions(TokenAuthentication tokenAuthentication) {
		String playerName = tokenAuthentication.getPlayerName();
		service.sendSubscriptions(playerName);
	}

	@MessageMapping("/players/{player}")
	public void filterPlayers(TokenAuthentication tokenAuthentication, @DestinationVariable("player") String player) {
		service.filterAndSendPlayer(tokenAuthentication.getPlayerName(), player);
	}

	@MessageMapping("/send/{topic}")
	public void send(
		String message,
		TokenAuthentication authentication,
		@DestinationVariable("topic") String topic
	) {
		service.sendMessage(authentication.getPlayerName(), topic, message);
	}

	@MessageMapping("/p2p/{peer}")
	public void p2p(TokenAuthentication authentication, @DestinationVariable("peer") String peer) {
		String currentPlayer = authentication.getPlayerName();
		service.createP2PTopic(currentPlayer, peer);
	}

	@MessageMapping("/read/{topic}")
	public void read(TokenAuthentication authentication, @DestinationVariable("topic") String topic) {
		service.read(authentication.getPlayerName(), topic);
	}
}
