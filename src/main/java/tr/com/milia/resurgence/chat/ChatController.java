package tr.com.milia.resurgence.chat;

import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import tr.com.milia.resurgence.security.TokenAuthentication;
import tr.com.milia.resurgence.task.PlayerNotFound;

import java.util.LinkedHashMap;
import java.util.Map;

@Controller
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
		headers.put("player", authentication.getPlayerName().orElseThrow(PlayerNotFound::new));

		message.setType(Type.MESSAGE);

		template.convertAndSend("/topic/" + topic, message, headers);
	}

}
