package tr.com.milia.resurgence.account;

class RegistrationResponse {
	private final String email;
	private final Status status;

	public RegistrationResponse(Account account) {
		email = account.getEmail();
		status = account.getStatus();
	}

	public String getEmail() {
		return email;
	}

	public Status getStatus() {
		return status;
	}
}
