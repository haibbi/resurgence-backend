package tr.com.milia.resurgence.quest;

import java.util.Set;

public enum QuestStatus {
	PENDING,
	IN_PROGRESS,
	DONE,
	CANCELED,
	;

	public static final Set<QuestStatus> COMPLETED_STATUS = Set.of(DONE, CANCELED);

}
