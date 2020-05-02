package tr.com.milia.resurgence.chat;

import org.springframework.web.util.HtmlUtils;

public class Message {

	private String content;
	private Type type;

	public Message() {
	}

	public Message(String content) {
		this.content = content;
		type = Type.MESSAGE;
	}

	public Message(Type type) {
		this.type = type;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = HtmlUtils.htmlEscape(content); // todo gerekli mi? smileyler gözükmeyebilir.
	}

	public Type getType() {
		return type;
	}

	public void setType(Type type) {
		this.type = type;
	}
}
