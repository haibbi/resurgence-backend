package tr.com.milia.resurgence.chat.persistence;

import tr.com.milia.resurgence.chat.Presence;

import javax.persistence.Embeddable;
import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;

@Embeddable
public class OnlineStateId implements Serializable {

	private String name;
	private boolean online;
	private Instant time;

	public OnlineStateId() {
	}

	public OnlineStateId(Presence presence) {
		this.name = presence.getName();
		this.online = presence.isOnline();
		this.time = presence.getTime();
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public boolean isOnline() {
		return online;
	}

	public void setOnline(boolean online) {
		this.online = online;
	}

	public Instant getTime() {
		return time;
	}

	public void setTime(Instant time) {
		this.time = time;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		OnlineStateId that = (OnlineStateId) o;
		return online == that.online && name.equals(that.name) && time.equals(that.time);
	}

	@Override
	public int hashCode() {
		return Objects.hash(name, online, time);
	}
}
