package tr.com.milia.resurgence.family;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import tr.com.milia.resurgence.security.TokenAuthentication;
import tr.com.milia.resurgence.task.PlayerNotFound;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/family")
public class FamilyController {

	private final FamilyService service;

	public FamilyController(FamilyService service) {
		this.service = service;
	}

	@PostMapping("/found/{name}")
	public FamilyResponse found(TokenAuthentication authentication, @PathVariable("name") String familyName) {
		String playerName = authentication.getPlayerName().orElseThrow(PlayerNotFound::new);
		Family family = service.found(playerName, familyName);

		return FamilyResponse.exposed(family);
	}

	@GetMapping
	public ResponseEntity<FamilyResponse> family(TokenAuthentication authentication) {
		String playerName = authentication.getPlayerName().orElseThrow(PlayerNotFound::new);
		return service.findPlayerFamilyFamily(playerName)
			.map(FamilyResponse::exposed)
			.map(ResponseEntity::ok)
			.orElse(ResponseEntity.notFound().build());
	}

	@GetMapping("/families")
	public List<FamilyResponse> families() {
		return service.findAllWithMember().stream()
			.map(FamilyResponse::new)
			.collect(Collectors.toList());
	}

}
