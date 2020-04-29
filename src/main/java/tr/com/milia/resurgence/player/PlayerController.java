package tr.com.milia.resurgence.player;

import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@RestController
@RequestMapping("/player")
public class PlayerController {

	private final PlayerService service;

	public PlayerController(PlayerService service) {
		this.service = service;
	}

	@GetMapping
	@Transactional
	public ResponseEntity<PlayerInfoResponse> info(Principal principal) {
		return service.findByUsername(principal.getName())
			.map(PlayerInfoResponse::new)
			.map(ResponseEntity::ok)
			.orElseGet(() -> ResponseEntity.notFound().build());
	}

	@PostMapping
	public PlayerInfoResponse create(@RequestBody @Validated CreatePlayerRequest request,
									 Principal principal) {
		var player = service.create(request.name, principal.getName(), request.race);
		return new PlayerInfoResponse(player);
	}
}
