package tr.com.milia.resurgence.notification;

import org.springframework.web.bind.annotation.*;
import tr.com.milia.resurgence.security.TokenAuthentication;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/message")
public class MessageController {

	private final MessageService service;

	public MessageController(MessageService service) {
		this.service = service;
	}

	@GetMapping
	public List<MessageResponse> messages(TokenAuthentication authentication) {
		return service.messages(authentication.getPlayerName()).stream()
			.map(MessageResponse::new)
			.collect(Collectors.toList());
	}

	@DeleteMapping(params = "id")
	public void delete(TokenAuthentication authentication, @RequestParam("id") Long id) {
		service.delete(authentication.getPlayerName(), id);
	}

}
