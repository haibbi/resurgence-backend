package tr.com.milia.resurgence.family;

import org.springframework.web.bind.annotation.*;
import tr.com.milia.resurgence.security.TokenAuthentication;

import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/family/hr")
public class HumanResourceController {

	private final HumanResourceService service;

	public HumanResourceController(HumanResourceService service) {
		this.service = service;
	}

	@PostMapping("/invite/{player}")
	public void invite(TokenAuthentication authentication, @PathVariable("player") String playerName) {
		String member = authentication.getPlayerName();
		service.invite(playerName, member);
	}

	@PostMapping("/application/{family}")
	public void application(TokenAuthentication authentication, @PathVariable("family") String familyName) {
		String player = authentication.getPlayerName();
		service.application(player, familyName);
	}

	@GetMapping
	public List<InvitationResponse> allInvitations(TokenAuthentication authentication) {
		String player = authentication.getPlayerName();
		final List<Invitation> invitations = new LinkedList<>();

		invitations.addAll(service.findAllByPlayer(player));
		invitations.addAll(service.findAllByFamily(player));

		return invitations.stream()
			.map(InvitationResponse::new)
			.collect(Collectors.toList());
	}

	@PostMapping("accept/{invitationId}")
	public void accept(TokenAuthentication authentication,
					   @PathVariable("invitationId") Long id) {
		String player = authentication.getPlayerName();
		service.accept(player, id);
	}

	@DeleteMapping("/leave")
	public void leave(TokenAuthentication authentication) {
		String player = authentication.getPlayerName();
		service.leave(player);
	}

	@DeleteMapping("/fire/{member}")
	public void fire(TokenAuthentication authentication, @PathVariable("member") String member) {
		String player = authentication.getPlayerName();
		service.fire(player, member);
	}

	@DeleteMapping("/destroy")
	public void destroy(TokenAuthentication authentication) {
		String player = authentication.getPlayerName();
		service.destroy(player);
	}

}
