package tr.com.milia.resurgence.chat;

import org.springframework.messaging.Message;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import tr.com.milia.resurgence.security.TokenAuthentication;

import java.security.Principal;
import java.util.Objects;
import java.util.regex.Pattern;

public class TopicSubscribeEvent {

	private final TokenAuthentication authentication;
	private final String player;
	private final String topic;

	public TopicSubscribeEvent(Message<byte[]> message, Principal user) {
		if (!(user instanceof TokenAuthentication))
			throw new IllegalStateException("Principal class must be " + TokenAuthentication.class);

		this.authentication = (TokenAuthentication) user;
		this.player = authentication.getPlayerName();

		this.topic = Objects.requireNonNull(
			message.getHeaders().get(SimpMessageHeaderAccessor.DESTINATION_HEADER),
			"Destination header must not be null"
		).toString();
	}

	public TokenAuthentication getAuthentication() {
		return authentication;
	}

	public String getPlayer() {
		return player;
	}

	public String getTopic() {
		return topic;
	}

	public boolean isUserStat() {
		return Pattern.matches("/user/[a-zA-Z0-9]+" + Topics.USER_STATS, this.topic);
	}
}
