package tr.com.milia.resurgence.player;

import tr.com.milia.resurgence.i18n.LocalizedException;

public class PlayerNotFound extends LocalizedException {
	public PlayerNotFound() {
		super("player.not.found");
	}
}
