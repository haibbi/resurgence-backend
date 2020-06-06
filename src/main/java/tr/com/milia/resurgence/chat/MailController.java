package tr.com.milia.resurgence.chat;

import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import tr.com.milia.resurgence.security.TokenAuthentication;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/mail")
public class MailController {

	private final MailService service;

	public MailController(MailService service) {
		this.service = service;
	}

	@GetMapping("/incoming")
	public List<MailResponse> incoming(TokenAuthentication authentication) {
		String player = authentication.getPlayerName();
		return service.incoming(player).stream().map(MailResponse::new).collect(Collectors.toList());
	}

	@GetMapping("/outgoing")
	public List<MailResponse> outgoing(TokenAuthentication authentication) {
		String player = authentication.getPlayerName();
		return service.outgoing(player).stream().map(MailResponse::new).collect(Collectors.toList());
	}

	@PostMapping("/{to}")
	public void send(TokenAuthentication authentication,
					 @PathVariable("to") String to,
					 @Validated @RequestBody MailRequest request) {
		String from = authentication.getPlayerName();
		service.send(from, to, request.content);
	}

	@PostMapping("/report/{id}")
	public void report(TokenAuthentication authentication, @PathVariable("id") Long id) {
		String player = authentication.getPlayerName();
		service.report(player, id);
	}

	@PostMapping("/read/{id}")
	public void read(TokenAuthentication authentication, @PathVariable("id") Long id) {
		String to = authentication.getPlayerName();
		service.read(to, id);
	}

	@DeleteMapping("/{id}")
	public void delete(TokenAuthentication authentication, @PathVariable("id") Long id) {
		String player = authentication.getPlayerName();
		service.delete(player, id);
	}

}
