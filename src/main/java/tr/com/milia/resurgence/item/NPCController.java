package tr.com.milia.resurgence.item;

import org.springframework.web.bind.annotation.*;
import tr.com.milia.resurgence.security.TokenAuthentication;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/npc")
public class NPCController {

	private final PlayerItemService service;

	public NPCController(PlayerItemService service) {
		this.service = service;
	}

	@GetMapping
	public List<PlayerItemResponse> items(
		TokenAuthentication authentication,
		@RequestParam(required = false, defaultValue = "") List<Item.Category> categories
	) {
		String player = authentication.getPlayerName();
		return service.findAllPlayerItem(player).stream()
			.filter(item -> item.getQuantity() > 0)
			.filter(item -> !Collections.disjoint(item.getItem().getCategory(), categories))
			.map(PlayerItemResponse::new)
			.collect(Collectors.toList());
	}

	@DeleteMapping("/{item}/{quantity}")
	public void sell(TokenAuthentication authentication,
					 @PathVariable("item") Item item,
					 @PathVariable("quantity") long quantity) {
		String player = authentication.getPlayerName();
		service.sellItem(player, item, quantity);
	}

	@PostMapping("/{item}/{quantity}")
	public void buy(TokenAuthentication authentication,
					@PathVariable("item") Item item,
					@PathVariable("quantity") long quantity) {
		String player = authentication.getPlayerName();
		service.buyItem(player, item, quantity);
	}

}
