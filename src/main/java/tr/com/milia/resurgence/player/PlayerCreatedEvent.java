package tr.com.milia.resurgence.player;

import org.springframework.context.ApplicationEvent;

public class PlayerCreatedEvent extends ApplicationEvent {

	public PlayerCreatedEvent(Player source) {
		super(source);
	}

	@Override
	public Player getSource() {
		return (Player) super.getSource();
	}
}
