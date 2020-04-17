package tr.com.milia.resurgence.player;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import java.security.Principal;

@RestController
@RequestMapping("/player")
public class PlayerController {

	private final PlayerService service;

	public PlayerController(PlayerService service) {
		this.service = service;
	}

	@GetMapping
	public Mono<ResponseEntity<PlayerInfoResponse>> info(Principal principal) {
		return service.findByUsername(principal.getName())
			.map(PlayerInfoResponse::new)
			.map(ResponseEntity::ok)
			.map(Mono::just)
			.orElseGet(() -> Mono.just(ResponseEntity.notFound().build()));
	}

	@PostMapping
	public Mono<PlayerInfoResponse> create(@RequestBody @Validated CreatePlayerRequest request,
										   Principal principal) {
		var player = service.create(request.name, principal.getName());
		return Mono.just(new PlayerInfoResponse(player));
	}
}
