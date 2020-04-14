package tr.com.milia.resurgence.account;

import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("/account")
public class AccountController {

	private final AccountService accountService;
	private final PasswordEncoder passwordEncoder;

	public AccountController(AccountService accountService, PasswordEncoder passwordEncoder) {
		this.accountService = accountService;
		this.passwordEncoder = passwordEncoder;
	}

	@PostMapping
	public ResponseEntity<RegistrationResponse> register(@RequestBody @Valid RegistrationRequest request) {

		request.password = passwordEncoder.encode(request.password);
		Account account = new Account(request.email, request.password, Status.UNVERIFIED);
		accountService.create(account);

		return ResponseEntity.ok().body(new RegistrationResponse(account));
	}

}
