package tr.com.milia.resurgence.security;

import org.springframework.lang.Nullable;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.Collection;

public class ResurgenceUser extends User {

	private final String playerName;

	public ResurgenceUser(String username,
						  String password,
						  @Nullable String playerName,
						  boolean enabled,
						  Collection<? extends GrantedAuthority> authorities) {
		super(username,
			password,
			enabled,
			true,
			true,
			true,
			authorities);
		this.playerName = playerName;
	}

	public String getPlayerName() {
		return playerName;
	}
}
