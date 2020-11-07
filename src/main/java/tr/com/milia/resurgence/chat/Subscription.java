package tr.com.milia.resurgence.chat;

import java.util.Objects;

public class Subscription {
	private final String name;
	private long read;

	public Subscription(String name) {
		this(name, 0);
	}

	public Subscription(String name, long read) {
		if (read < 0) throw new IllegalArgumentException("Read sequence must be greater than zero");

		this.name = Objects.requireNonNull(name);
		this.read = read;
	}

	public String getName() {
		return name;
	}

	public long getRead() {
		return read;
	}

	public void setRead(long read) {
		if (read < 0) throw new IllegalArgumentException("Read sequence must be greater than zero");
		this.read = read;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		Subscription that = (Subscription) o;
		return name.equals(that.name);
	}

	@Override
	public int hashCode() {
		return Objects.hash(name);
	}
}
