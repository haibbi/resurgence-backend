package tr.com.milia.resurgence.account;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tr.com.milia.resurgence.security.TokenAuthentication;

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
		Account account = accountService.create(request.email, request.password);
		return ResponseEntity.ok().body(new RegistrationResponse(account));
	}

	@PatchMapping("/push-token")
	public void addToken(TokenAuthentication authentication, @RequestBody @Valid PushNotificationTokenRequest request) {
		accountService.addToken(authentication.getName(), request.token);
	}
}
