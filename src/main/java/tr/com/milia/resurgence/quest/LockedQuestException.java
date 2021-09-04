package tr.com.milia.resurgence.quest;

import tr.com.milia.resurgence.i18n.LocalizedException;

public class LockedQuestException extends LocalizedException {
	public LockedQuestException() {
		super("quest.locked");
	}
}
