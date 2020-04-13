package tr.com.milia.resurgence.account;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

class RegistrationRequest {
	@NotNull
	@Email
	String email;

	@NotNull
	@Size(min = 6)
	String password;

	public void setEmail(String email) {
		this.email = email;
	}

	public void setPassword(String password) {
		this.password = password;
	}
}
