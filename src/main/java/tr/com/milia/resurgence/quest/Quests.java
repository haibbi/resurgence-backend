package tr.com.milia.resurgence.quest;

import tr.com.milia.resurgence.i18n.LocaleEnum;
import tr.com.milia.resurgence.item.Item;
import tr.com.milia.resurgence.player.Title;
import tr.com.milia.resurgence.task.Task;

import java.util.Collections;
import java.util.Set;

public enum Quests implements LocaleEnum {
	FIRST(
		new Quest(
			Title.EMPTY_SUIT,
			Collections.emptySet(),
			Collections.emptySet(),
			Collections.emptySet(),
			Set.of(
				new ItemCompleteRequirement(Item.KNIFE, 1)
			),
			Set.of(
				new TaskCompleteRequirement(Task.SHOPLIFTING, 10)
			),
			Collections.emptySet(),
			new ExperienceReward(1_000),
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
			Set.of(
				new ItemCompleteRequirement(Item.GLOCK, 5)
			),
			Set.of(
				new TaskCompleteRequirement(Task.HOUSE_ROBBERY, 10)
			),
			Collections.emptySet(),
			new ExperienceReward(2500),
			Set.of(
				new ItemReward(Item.SHOTGUN, 1)
			)
		)
	),
	THIRD(
		new Quest(
			Title.EMPTY_SUIT,
			Collections.emptySet(),
			Set.of(SECOND),
			Collections.emptySet(),
			Set.of(
				new ItemCompleteRequirement(Item.SHOTGUN, 10)
			),
			Set.of(
				new TaskCompleteRequirement(Task.PARKING_LOT_ROBBERY, 10)
			),
			Collections.emptySet(),
			new ExperienceReward(2500),
			Set.of(
				new ItemReward(Item.FORD_FIESTA, 1),
				new ItemReward(Item.BULLET, 10)
			)
		)
	),
	FOURTH(
		new Quest(
			Title.EMPTY_SUIT,
			Collections.emptySet(),
			Set.of(SECOND),
			Collections.emptySet(),
			Set.of(
				new ItemCompleteRequirement(Item.FORD_FIESTA, 1)
			),
			Set.of(
				new TaskCompleteRequirement(Task.BANK_RUBBERY, 10)
			),
			Set.of(THIRD),
			new ExperienceReward(2500),
			Set.of(
				new ItemReward(Item.MUSTANG, 1),
				new ItemReward(Item.BULLET, 100)
			)
		)
	),
	FIFTY(
		new Quest(
			Title.EMPTY_SUIT,
			Collections.emptySet(),
			Set.of(THIRD),
			Collections.emptySet(),
			Set.of(
				new ItemCompleteRequirement(Item.MUSTANG, 1)
			),
			Set.of(
				new TaskCompleteRequirement(Task.FEDERAL_RESERVE, 10)
			),
			Set.of(FOURTH),
			new ExperienceReward(2500),
			Set.of(
				new ItemReward(Item.ASPIRIN, 20),
				new ItemReward(Item.BULLET, 200)
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
