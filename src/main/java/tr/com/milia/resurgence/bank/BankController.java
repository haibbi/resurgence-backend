package tr.com.milia.resurgence.bank;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import tr.com.milia.resurgence.security.TokenAuthentication;
import tr.com.milia.resurgence.task.PlayerNotFound;

@RestController
@RequestMapping("/bank")
public class BankController {

	private final BankService bankService;

	public BankController(BankService bankService) {
		this.bankService = bankService;
	}

	@PostMapping("/interest/{amount}")
	public InterestResponse interest(TokenAuthentication authentication, @PathVariable("amount") long amount) {
		String playerName = authentication.getPlayerName().orElseThrow(PlayerNotFound::new);
		long interest = bankService.interest(playerName, amount);

		return new InterestResponse(interest);
	}

}
