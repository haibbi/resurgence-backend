package tr.com.milia.resurgence.account;

import tr.com.milia.resurgence.i18n.LocaleEnum;

public enum Status implements LocaleEnum {
	UNVERIFIED(false),
	VERIFIED(true),
	BANNED(false),
	PERMANENT_BANNED(false);

	private final boolean enabled;

	Status(boolean enabled) {
		this.enabled = enabled;
	}

	public boolean isEnabled() {
		return enabled;
	}
}
