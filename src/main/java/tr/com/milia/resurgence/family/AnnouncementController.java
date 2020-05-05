package tr.com.milia.resurgence.family;

import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import tr.com.milia.resurgence.security.TokenAuthentication;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/family/announcement")
public class AnnouncementController {

	private final AnnouncementService service;

	public AnnouncementController(AnnouncementService service) {
		this.service = service;
	}

	@GetMapping
	public List<AnnouncementResponse> all(TokenAuthentication authentication) {
		String player = authentication.getPlayerName();
		return service.findAll(player).stream()
			.map(AnnouncementResponse::new)
			.collect(Collectors.toList());
	}

	@PostMapping
	public void add(TokenAuthentication authentication, @RequestBody @Validated AnnouncementRequest request) {
		String player = authentication.getPlayerName();
		service.add(player, request.title, request.content, request.general);
	}

	@PatchMapping("/{id}")
	public void edit(TokenAuthentication authentication,
					 @PathVariable("id") Long id,
					 @RequestBody @Validated AnnouncementRequest request) {
		String player = authentication.getPlayerName();
		service.edit(id, player, request.title, request.content, request.general);
	}

	@DeleteMapping("/{id}")
	public void delete(TokenAuthentication authentication, @PathVariable("id") Long id) {
		String player = authentication.getPlayerName();
		service.remove(id, player);
	}

}
