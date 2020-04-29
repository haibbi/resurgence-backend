package tr.com.milia.resurgence.bank;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import tr.com.milia.resurgence.security.TokenAuthentication;
import tr.com.milia.resurgence.task.PlayerNotFound;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/bank")
public class BankController {

	private final BankService service;

	public BankController(BankService service) {
		this.service = service;
	}

	@PostMapping("/interest/{amount}")
	public InterestResponse interest(TokenAuthentication authentication, @PathVariable("amount") long amount) {
		String playerName = authentication.getPlayerName().orElseThrow(PlayerNotFound::new);
		long interest = service.interest(playerName, amount);

		return new InterestResponse(interest);
	}

	@GetMapping("/interest")
	public ResponseEntity<InterestAccount> currentInterest(TokenAuthentication authentication) {
		String playerName = authentication.getPlayerName().orElseThrow(PlayerNotFound::new);
		return service.currentInterest(playerName)
			.map(ResponseEntity::ok)
			.orElse(ResponseEntity.notFound().build());
	}

	@GetMapping("/interest-rates")
	public List<InterestRateResponse> interestRates() {
		return Arrays.stream(InterestRates.values())
			.map(InterestRates::getRate)
			.map(InterestRateResponse::new)
			.collect(Collectors.toList());
	}

	@PostMapping("/transfer/{player}/{amount}")
	public void transfer(TokenAuthentication authentication,
						 @PathVariable("player") String toPlayer,
						 @PathVariable("amount") long amount) {
		String fromPlayer = authentication.getPlayerName().orElseThrow(PlayerNotFound::new);
		service.transfer(fromPlayer, toPlayer, amount);
	}

}
