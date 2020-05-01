package tr.com.milia.resurgence.family;

import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
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
	@Transactional
	public FamilyResponse found(TokenAuthentication authentication, @PathVariable("name") String familyName) {
		String playerName = authentication.getPlayerName().orElseThrow(PlayerNotFound::new);
		Family family = service.found(playerName, familyName);

		return FamilyResponse.exposed(family);
	}

	@GetMapping
	@Transactional
	public ResponseEntity<FamilyResponse> family(TokenAuthentication authentication) {
		String playerName = authentication.getPlayerName().orElseThrow(PlayerNotFound::new);
		return service.findFamilyByPlayerName(playerName)
			.map(FamilyResponse::exposed)
			.map(ResponseEntity::ok)
			.orElse(ResponseEntity.notFound().build());
	}

	@GetMapping("/all")
	public List<FamilyResponse> allFamilies() {
		return service.findAll().stream()
			.map(FamilyResponse::new)
			.collect(Collectors.toList());
	}

	@PostMapping("assign/consultant/{name}")
	public void assignConsultant(TokenAuthentication authentication, @PathVariable("name") String consultant) {
		String player = authentication.getPlayerName().orElseThrow(PlayerNotFound::new);
		service.assignConsultant(player, consultant);
	}

	@DeleteMapping("fire/consultant")
	public void fireConsultant(TokenAuthentication authentication) {
		String player = authentication.getPlayerName().orElseThrow(PlayerNotFound::new);
		service.fireConsultant(player);
	}

	@PostMapping("assign/chief/{name}")
	public void assignChief(TokenAuthentication authentication, @PathVariable("name") String chief) {
		String player = authentication.getPlayerName().orElseThrow(PlayerNotFound::new);
		service.assignChief(player, chief);
	}

	@DeleteMapping("fire/chief/{name}")
	public void fireChief(TokenAuthentication authentication, @PathVariable("name") String chief) {
		String player = authentication.getPlayerName().orElseThrow(PlayerNotFound::new);
		service.fireChief(player, chief);
	}


	@PostMapping("assign/chief/member/{chief}/{member}")
	public void assignMemberToChief(TokenAuthentication authentication,
									@PathVariable("chief") String chief,
									@PathVariable("member") String member) {
		String player = authentication.getPlayerName().orElseThrow(PlayerNotFound::new);
		service.assignMemberToChief(player, chief, member);
	}

	@DeleteMapping("fire/chief/member/{chief}/{member}")
	public void removeMemberFromChief(TokenAuthentication authentication,
									  @PathVariable("chief") String chief,
									  @PathVariable("member") String member) {
		String player = authentication.getPlayerName().orElseThrow(PlayerNotFound::new);
		service.removeMemberFromChief(player, chief, member);
	}

}
