package tr.com.milia.resurgence.quest;

import tr.com.milia.resurgence.i18n.LocalizedException;

public class QuestCanNotCompleteYetException extends LocalizedException {
	public QuestCanNotCompleteYetException() {
		super("quest.can.not.complete.yet");
	}
}
