package tr.com.milia.resurgence.gambling;

import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import tr.com.milia.resurgence.security.TokenAuthentication;
import tr.com.milia.resurgence.task.PlayerNotFound;

import javax.validation.constraints.Min;

@RestController
@RequestMapping("/gambling")
public class GamblingController {

	private final NumberGame numberGame;
	private final LotteryService lotteryService;

	public GamblingController(NumberGame numberGame, LotteryService lotteryService) {
		this.numberGame = numberGame;
		this.lotteryService = lotteryService;
	}

	@PostMapping("/number/{number}/{bet}")
	public GamblingResponse number(TokenAuthentication authentication,
								   @PathVariable("number") int number,
								   @PathVariable("bet") int bet) {
		String playerName = authentication.getPlayerName().orElseThrow(PlayerNotFound::new);
		boolean win = numberGame.play(playerName, number, bet);

		return new GamblingResponse(win);
	}

	@PostMapping("/even/{even}/{bet}")
	public GamblingResponse even(TokenAuthentication authentication,
								 @PathVariable("even") boolean even,
								 @Validated @PathVariable("bet") @Min(1) int bet) {
		String playerName = authentication.getPlayerName().orElseThrow(PlayerNotFound::new);
		boolean win = numberGame.play(playerName, even, bet);

		return new GamblingResponse(win);
	}

	@PostMapping("/lottery")
	public void purchaseLotteryTicket(TokenAuthentication authentication) {
		String player = authentication.getPlayerName().orElseThrow(PlayerNotFound::new);
		lotteryService.purchase(player);
	}

}
