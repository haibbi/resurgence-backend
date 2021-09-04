package tr.com.milia.resurgence.quest;

import tr.com.milia.resurgence.i18n.LocaleEnum;

import java.util.Set;

public enum QuestStatus implements LocaleEnum {
	PENDING,
	IN_PROGRESS,
	DONE,
	CANCELED,
	;

	public static final Set<QuestStatus> COMPLETED_STATUS = Set.of(DONE, CANCELED);

}
