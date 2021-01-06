package tr.com.milia.resurgence.chat.persistence;

import tr.com.milia.resurgence.chat.Presence;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "online_state")
public class OnlineState {

	@EmbeddedId
	private OnlineStateId id;

	public OnlineState() {
	}

	public OnlineState(Presence presence) {
		this.id = new OnlineStateId(presence);
	}

	public OnlineStateId getId() {
		return id;
	}
}
