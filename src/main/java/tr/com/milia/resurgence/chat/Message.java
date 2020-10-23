package tr.com.milia.resurgence.chat;

import java.time.Instant;
import java.util.Objects;

public class Message implements Comparable<Message> {

	private final long sequence;
	private final String content;
	private final String from;
	private final Instant time;

	public Message(long sequence, String content, String from) {
		this(sequence, content, from, Instant.now());
	}

	public Message(long sequence, String content, String from, Instant time) {
		this.sequence = sequence;
		this.content = content;
		this.from = from;
		this.time = time;
	}

	public long getSequence() {
		return sequence;
	}

	public String getContent() {
		return content;
	}

	public String getFrom() {
		return from;
	}

	public Instant getTime() {
		return time;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		Message message = (Message) o;
		return sequence == message.sequence &&
			Objects.equals(content, message.content) &&
			Objects.equals(from, message.from) &&
			time.equals(message.time);
	}

	@Override
	public int hashCode() {
		return Objects.hash(sequence, content, from, time);
	}

	@Override
	public int compareTo(Message o) {
		return Long.compare(sequence, o.sequence);
	}

	@Override
	public String toString() {
		return "Message{" +
			"sequence=" + sequence +
			", content='" + content + '\'' +
			", from='" + from + '\'' +
			'}';
	}
}
