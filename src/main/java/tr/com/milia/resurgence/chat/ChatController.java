package tr.com.milia.resurgence.chat;

import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import tr.com.milia.resurgence.security.TokenAuthentication;

import java.util.LinkedHashMap;
import java.util.Map;

public class ChatController {

	private final SimpMessagingTemplate template;

	public ChatController(SimpMessagingTemplate template) {
		this.template = template;
	}

	@MessageMapping("/{topic}")
	public void greeting(Message message,
						 TokenAuthentication authentication,
						 @DestinationVariable("topic") String topic) {
		Map<String, Object> headers = new LinkedHashMap<>();
		headers.put("player", authentication.getPlayerName());

		message.setType(Type.MESSAGE);

		template.convertAndSend("/topic/" + topic, message, headers);
	}

}
