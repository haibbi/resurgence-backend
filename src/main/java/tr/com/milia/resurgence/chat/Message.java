package tr.com.milia.resurgence.chat;

public class Message implements Comparable<Message> {

	private final long sequence;
	private final String content;
	private final String from;

	public Message(long sequence, String content, String from) {
		this.sequence = sequence;
		this.content = content;
		this.from = from;
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
