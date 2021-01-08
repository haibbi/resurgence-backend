package tr.com.milia.resurgence.player;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.util.UriComponentsBuilder;
import tr.com.milia.resurgence.security.TokenAuthentication;

import java.io.IOException;
import java.security.Principal;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/player")
public class PlayerController {

	private static final Logger log = LoggerFactory.getLogger(PlayerController.class);

	private final PlayerService service;

	public PlayerController(PlayerService service) {
		this.service = service;
	}

	@GetMapping({"", "/", "/{name}"})
	@Transactional(readOnly = true)
	public ResponseEntity<PlayerInfoResponse> info(
		@PathVariable(value = "name", required = false) Optional<String> name,
		TokenAuthentication authentication
	) {
		final String currentUser;
		try {
			currentUser = authentication.getPlayerName();
		} catch (PlayerNotFound e) {
			return ResponseEntity.notFound().build();
		}
		final String requested = name.orElse(currentUser);
		return service.findByName(requested)
			.map(p -> new PlayerInfoResponse(p, !requested.equals(currentUser)))
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

	@GetMapping("/image/{player}")
	public ResponseEntity<Void> image(@PathVariable("player") String player) {
		final String playerImageURL = service.findByName(player).map(p -> {
			if (p.getImage() == null)
				return raceImageLocation(p.getRace());
			return p.getImage();
		}).orElseGet(() -> raceImageLocation(Race.COSA_NOSTRA)); // default cosa nostra image

		return ResponseEntity.status(HttpStatus.MOVED_PERMANENTLY).header(HttpHeaders.LOCATION, playerImageURL).build();
	}

	private String raceImageLocation(Race race) {
		return UriComponentsBuilder.fromPath(String.format("/static/player/%s-256.png", race.name())).toUriString();
	}
}
