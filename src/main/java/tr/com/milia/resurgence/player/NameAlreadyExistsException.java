package tr.com.milia.resurgence.player;

import tr.com.milia.resurgence.i18n.LocalizedException;

public class NameAlreadyExistsException extends LocalizedException {
	public NameAlreadyExistsException(String name) {
		super("player.name.already.exists", name);
	}
}
