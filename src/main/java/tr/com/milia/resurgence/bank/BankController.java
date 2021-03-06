package tr.com.milia.resurgence.bank;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tr.com.milia.resurgence.security.TokenAuthentication;

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
		String playerName = authentication.getPlayerName();
		long interest = service.interest(playerName, amount);

		return new InterestResponse(interest);
	}

	@GetMapping("/interest")
	public ResponseEntity<InterestAccountResponse> currentInterest(TokenAuthentication authentication) {
		String playerName = authentication.getPlayerName();
		return service.currentInterest(playerName)
			.map(InterestAccountResponse::new)
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
						 @PathVariable("amount") long amount,
						 @RequestParam(value = "d", required = false) String description) {
		String fromPlayer = authentication.getPlayerName();

		if (fromPlayer.equals(toPlayer)) throw new SelfTransferException();

		service.transfer(fromPlayer, toPlayer, amount, description);
	}

}
