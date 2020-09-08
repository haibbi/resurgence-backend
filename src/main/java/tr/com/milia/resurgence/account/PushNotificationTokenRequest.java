package tr.com.milia.resurgence.account;

import javax.validation.constraints.NotNull;

class PushNotificationTokenRequest {
	@NotNull
	String token;

	public void setToken(String token) {
		this.token = token;
	}
}
