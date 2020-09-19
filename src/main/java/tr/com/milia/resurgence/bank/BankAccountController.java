package tr.com.milia.resurgence.bank;

import org.springframework.web.bind.annotation.*;
import tr.com.milia.resurgence.security.TokenAuthentication;

@RestController
@RequestMapping("/bank/account")
public class BankAccountController {

	private final BankService service;

	public BankAccountController(BankService service) {
		this.service = service;
	}

	@GetMapping
	public BankAccountResponse account(TokenAuthentication authentication) {
		String player = authentication.getPlayerName();
		return service.findAccount(player)
			.map(BankAccountResponse::new)
			.orElseGet(() -> new BankAccountResponse(0L));
	}

	@PostMapping("/{amount}")
	public void deposit(TokenAuthentication authentication, @PathVariable("amount") long amount) {
		String player = authentication.getPlayerName();
		service.deposit(player, amount);
	}

	@DeleteMapping("/{amount}")
	public void withdraw(TokenAuthentication authentication, @PathVariable("amount") long amount) {
		String player = authentication.getPlayerName();
		service.withdraw(player, amount);
	}

}
