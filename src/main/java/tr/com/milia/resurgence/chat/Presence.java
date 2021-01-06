package tr.com.milia.resurgence.chat;

import tr.com.milia.resurgence.chat.persistence.OnlineState;
import tr.com.milia.resurgence.chat.persistence.OnlineStateId;

import java.time.Instant;
import java.util.Objects;

public class Presence {

	/**
	 * Player name
	 */
	private final String name;

	/**
	 * Eger aktif ise {@code true}
	 */
	private final boolean online;

	/**
	 * Time to occur
	 */
	private final Instant time;

	/**
	 * Duration in millisecond
	 */
	private final long durationMillis;

	private Presence(String name, boolean online, Instant time) {
		this.name = Objects.requireNonNull(name);
		this.online = online;
		this.time = Objects.requireNonNull(time);
		this.durationMillis = System.currentTimeMillis() - time.toEpochMilli();
	}

	static Presence online(String name) {
		return new Presence(name, true, Instant.now());
	}

	static Presence offline(String name) {
		return new Presence(name, false, Instant.now());
	}

	static Presence from(OnlineState state) {
		OnlineStateId statId = state.getId();
		return new Presence(statId.getName(), statId.isOnline(), statId.getTime());
	}

	public String getName() {
		return name;
	}

	public boolean isOnline() {
		return online;
	}

	public Instant getTime() {
		return time;
	}

	public long getDurationMillis() {
		return durationMillis;
	}

	@Override
	public String toString() {
		return "OnlineStat{" +
			"name='" + name + '\'' +
			", online=" + online +
			", time=" + time +
			'}';
	}
}
