package tr.com.milia.resurgence.bank;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tr.com.milia.resurgence.security.TokenAuthentication;
import tr.com.milia.resurgence.task.PlayerNotFound;

@RestController
@RequestMapping("/bank/account")
public class BankAccountController {

	private final BankService service;

	public BankAccountController(BankService service) {
		this.service = service;
	}

	@GetMapping
	public ResponseEntity<BankAccountResponse> account(TokenAuthentication authentication) {
		String player = authentication.getPlayerName().orElseThrow(PlayerNotFound::new);
		return service.findAccount(player).map(BankAccountResponse::new)
			.map(ResponseEntity::ok)
			.orElse(ResponseEntity.notFound().build());
	}

	@PostMapping("/{amount}")
	public void deposit(TokenAuthentication authentication, @PathVariable("amount") long amount) {
		String player = authentication.getPlayerName().orElseThrow(PlayerNotFound::new);
		service.deposit(player, amount);
	}

	@DeleteMapping("/{amount}")
	public void withdraw(TokenAuthentication authentication, @PathVariable("amount") long amount) {
		String player = authentication.getPlayerName().orElseThrow(PlayerNotFound::new);
		service.withdraw(player, amount);
	}

}
