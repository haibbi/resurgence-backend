package tr.com.milia.resurgence.quest;

import tr.com.milia.resurgence.i18n.LocalizedException;

public class QuestAlreadyCompletedException extends LocalizedException {
	public QuestAlreadyCompletedException() {
		super("quest.already.completed");
	}
}
