package tr.com.milia.resurgence.item;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import tr.com.milia.resurgence.player.PlayerService;
import tr.com.milia.resurgence.security.TokenAuthentication;

@RestController
@RequestMapping("/player-item")
public class PlayerItemController {

	private final PlayerService playerService;
	private final PlayerItemService itemService;

	public PlayerItemController(PlayerService playerService, PlayerItemService itemService) {
		this.playerService = playerService;
		this.itemService = itemService;
	}

	@PostMapping("/use")
	@Transactional
	public ResponseEntity<PlayerItemUseResponse> use(TokenAuthentication authentication, @RequestParam Item item) {
		return playerService.findByName(authentication.getPlayerName())
			.map(p -> ResponseEntity.ok(new PlayerItemUseResponse(itemService.use(p, item))))
			.orElse(ResponseEntity.status(HttpStatus.BAD_REQUEST).build());
	}

}

record PlayerItemUseResponse(boolean used) {
}
