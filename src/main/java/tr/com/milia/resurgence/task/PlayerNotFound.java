package tr.com.milia.resurgence.task;

import tr.com.milia.resurgence.i18n.LocalizedException;

public class PlayerNotFound extends LocalizedException {
	public PlayerNotFound() {
		super("player.not.found");
	}
}
