package tr.com.milia.resurgence.player;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import tr.com.milia.resurgence.security.TokenAuthentication;

import java.io.IOException;
import java.security.Principal;
import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/player")
public class PlayerController {

	private static final Logger log = LoggerFactory.getLogger(PlayerController.class);

	private final PlayerService service;

	public PlayerController(PlayerService service) {
		this.service = service;
	}

	@GetMapping
	@Transactional(readOnly = true)
	public ResponseEntity<PlayerInfoResponse> info(Principal principal) {
		return service.findByUsername(principal.getName())
			.map(PlayerInfoResponse::new)
			.map(ResponseEntity::ok)
			.orElseGet(() -> ResponseEntity.notFound().build());
	}

	@GetMapping("/{name}")
	@Transactional(readOnly = true)
	public ResponseEntity<PlayerInfoResponse> info(@PathVariable("name") String name) {
		return service.findByName(name)
			.map(p -> new PlayerInfoResponse(p, true))
			.map(ResponseEntity::ok)
			.orElseGet(() -> ResponseEntity.notFound().build());
	}

	@GetMapping("/races")
	public List<Race> races() {
		return Arrays.asList(Race.values());
	}

	@PostMapping
	public PlayerInfoResponse create(@RequestBody @Validated CreatePlayerRequest request, Principal principal) {
		var player = service.create(request.name, principal.getName(), request.race);
		return new PlayerInfoResponse(player);
	}

	@PostMapping("/image")
	public ResponseEntity<Void> editImage(TokenAuthentication authentication,
										  @RequestParam(value = "file") MultipartFile file) {
		if (file.isEmpty()) return ResponseEntity.badRequest().build();

		try {
			service.editImage(authentication.getPlayerName(), file);
			return ResponseEntity.ok().build();
		} catch (IOException e) {
			log.error("Player image file cannot read", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
	}
}
