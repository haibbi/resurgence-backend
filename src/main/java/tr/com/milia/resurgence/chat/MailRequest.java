package tr.com.milia.resurgence.chat;

import javax.validation.constraints.NotEmpty;

class MailRequest {

	@NotEmpty
	String content;

	public void setContent(String content) {
		this.content = content;
	}
}
