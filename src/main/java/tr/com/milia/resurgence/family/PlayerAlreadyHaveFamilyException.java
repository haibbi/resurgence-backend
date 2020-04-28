package tr.com.milia.resurgence.family;

import tr.com.milia.resurgence.i18n.LocalizedException;

public class PlayerAlreadyHaveFamilyException extends LocalizedException {
	public PlayerAlreadyHaveFamilyException() {
		super("player.have.a.family");
	}
}
