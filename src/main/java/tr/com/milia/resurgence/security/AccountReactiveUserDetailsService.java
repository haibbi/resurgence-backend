package tr.com.milia.resurgence.security;

import org.springframework.security.core.userdetails.ReactiveUserDetailsService;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import reactor.core.publisher.Mono;
import tr.com.milia.resurgence.account.AccountService;

import java.util.Collections;

public class AccountReactiveUserDetailsService implements ReactiveUserDetailsService {
	private final AccountService accountService;

	public AccountReactiveUserDetailsService(AccountService accountService) {
		this.accountService = accountService;
	}

	@Override
	public Mono<UserDetails> findByUsername(String username) {
		return accountService.findByEmail(username).map(account -> new User(account.getEmail(), account.getPassword(),
			// todo account.getStatus().equals(Status.VERIFIED)
			true,
			true,
			true,
			true,
			Collections.emptyList()
		)).<Mono<UserDetails>>map(Mono::just).orElseGet(Mono::empty);
	}
}
