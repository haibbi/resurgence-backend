package tr.com.milia.resurgence.security;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import tr.com.milia.resurgence.account.Account;
import tr.com.milia.resurgence.account.AccountService;
import tr.com.milia.resurgence.player.Player;
import tr.com.milia.resurgence.player.PlayerService;

import java.util.Collections;

public class AccountDetailsService implements UserDetailsService {

	private final AccountService accountService;
	private final PlayerService playerService;

	public AccountDetailsService(AccountService accountService, PlayerService playerService) {
		this.accountService = accountService;
		this.playerService = playerService;
	}

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		Account account = accountService.findByEmail(username).orElseThrow(() -> {
			throw new UsernameNotFoundException(username);
		});

		String playerName = playerService.findByUsername(username).map(Player::getName).orElse(null);

		return new ResurgenceUser(username,
			account.getPassword(),
			playerName,
			account.getStatus().isEnabled(),
			Collections.emptyList());
	}
}
