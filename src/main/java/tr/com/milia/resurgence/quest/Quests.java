package tr.com.milia.resurgence.quest;

import tr.com.milia.resurgence.item.Item;
import tr.com.milia.resurgence.player.Title;
import tr.com.milia.resurgence.task.Task;

import java.util.Collections;
import java.util.Set;

public enum Quests {
	FIRST(
		new Quest(
			Title.EMPTY_SUIT,
			Collections.emptySet(),
			Collections.emptySet(),
			Collections.emptySet(),
			Collections.emptySet(),
			Collections.emptySet(),
			Set.of(
				new TaskCompleteRequirement(Task.SHOPLIFTING, 5)
			),
			Collections.emptySet(),
			new ExperienceReward(1000),
			Set.of(
				new ItemReward(Item.GLOCK, 1)
			)
		)
	),
	SECOND(
		new Quest(
			Title.EMPTY_SUIT,
			Collections.emptySet(),
			Set.of(FIRST),
			Collections.emptySet(),
			Collections.emptySet(),
			Set.of(
				new ItemCompleteRequirement(Item.KNIFE, 10)
			),
			Collections.emptySet(),
			Collections.emptySet(),
			new ExperienceReward(2500),
			Set.of(
				new ItemReward(Item.SHOTGUN, 1)
			)
		)
	),
	;

	private final Quest quest;

	Quests(Quest quest) {
		this.quest = quest;
	}

	Quest getQuest() {
		return quest;
	}

}
