package tr.com.milia.resurgence.account;

import javax.persistence.*;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

@Entity
public class Account {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(unique = true, nullable = false)
	private String email;

	@Column(nullable = false)
	private String password;

	@Column(nullable = false)
	@Enumerated(value = EnumType.STRING)
	private Status status;

	@ElementCollection
	@CollectionTable(name = "account_push_notification_tokens")
	@Column(name = "token")
	private Set<String> pushNotificationTokens;

	public Account() {
	}

	public Account(String email, String password, Status status) {
		this.email = email;
		this.password = password;
		this.status = status;
		pushNotificationTokens = new HashSet<>();
	}

	public Long getId() {
		return id;
	}

	public String getEmail() {
		return email;
	}

	public String getPassword() {
		return password;
	}

	public Status getStatus() {
		return status;
	}

	public Set<String> getPushNotificationTokens() {
		return Collections.unmodifiableSet(pushNotificationTokens);
	}

	public void addPushNotificationToken(String token) {
		this.pushNotificationTokens.add(token);
	}

	public void removePushNotificationToken(String token) {
		this.pushNotificationTokens.remove(token);
	}
}
