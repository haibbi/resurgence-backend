package tr.com.milia.resurgence.chat;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessageType;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.stereotype.Component;

@Component
public class MessageLogger implements ChannelInterceptor {

	private static final Logger log = LoggerFactory.getLogger(MessageLogger.class);

	private final MessageLogRepository repository;
	private final ObjectMapper objectMapper;

	public MessageLogger(MessageLogRepository repository, ObjectMapper objectMapper) {
		this.repository = repository;
		this.objectMapper = objectMapper;
	}

	@Override
	public void afterSendCompletion(Message<?> message, MessageChannel channel, boolean sent, Exception ex) {
		final StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(message);

		if (headerAccessor.getMessageType() != SimpMessageType.MESSAGE) return;

		Object payload = message.getPayload();
		if (payload instanceof byte[]) {
			try {
				final String topic = (String) message.getHeaders().get(SimpMessageHeaderAccessor.DESTINATION_HEADER);
				final String player = headerAccessor.getFirstNativeHeader("player");
				var sentMessage = objectMapper.readValue((byte[]) payload, tr.com.milia.resurgence.chat.Message.class);

				if (sentMessage.getType() != Type.MESSAGE) return;

				repository.save(new MessageLog(sentMessage, topic, player));
			} catch (Exception e) {
				// do nothing
				log.warn("An error occurred while persistent message log", e);
			}
		}
	}

}
