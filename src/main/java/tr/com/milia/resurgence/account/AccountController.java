package tr.com.milia.resurgence.account;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("/account")
public class AccountController {

	private final AccountService accountService;

	public AccountController(AccountService accountService) {
		this.accountService = accountService;
	}

	@PostMapping
	public ResponseEntity<RegistrationResponse> register(@RequestBody @Valid RegistrationRequest request) {
		// TODO: 13.04.2020 add password encoder
		Account account = new Account(request.email, request.password, Status.UNVERIFIED);
		accountService.create(account);

		return ResponseEntity.ok().body(new RegistrationResponse(account));
	}

}
