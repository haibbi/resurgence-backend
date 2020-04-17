package tr.com.milia.resurgence.account;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

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

	public Account() {
	}

	public Account(String email) {
		this.email = email;
	}

	public Account(String email, String password, Status status) {
		this.email = email;
		this.password = password;
		this.status = status;
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
}
