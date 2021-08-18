package tr.com.milia.resurgence.quest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tr.com.milia.resurgence.item.Item;
import tr.com.milia.resurgence.item.PlayerItem;
import tr.com.milia.resurgence.player.Player;
import tr.com.milia.resurgence.player.Title;
import tr.com.milia.resurgence.skill.PlayerSkill;
import tr.com.milia.resurgence.skill.Skill;
import tr.com.milia.resurgence.task.Task;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public record Quest(
	Title titleRequirement,
	Set<SkillRequirement> skillRequirements,
	Set<Quests> questRequirements,
	Set<TimeRangeRequirement> timeRangeRequirements,

	Set<ItemCompleteRequirement> itemCompleteRequirements,
	Set<ItemCompleteRequirement> consumeItemCompleteRequirements,
	Set<TaskCompleteRequirement> taskCompleteRequirements,
	Set<Quests> questCompleteRequirements,

	ExperienceReward experienceReward,
	Set<ItemReward> itemRewards
) {

	private static final Logger log = LoggerFactory.getLogger(Quest.class);

	boolean isUnlocked(Player player, Set<Quests> completedQuests) {
		if (log.isDebugEnabled())
			log.debug("Is quest unlocked? Player[{}], completedQuests[{}]",
				player.getName(), completedQuests);

		if (log.isDebugEnabled())
			log.debug("Title requirement is checking. Requirement[{}], player[{}] title[{}]",
				titleRequirement, player.getName(), player.getTitle());

		if (titleRequirement.compareTo(player.getTitle()) > 0) return false;


		var playerSkills = player.getSkills().stream()
			.collect(Collectors.toMap(PlayerSkill::getSkill, PlayerSkill::getExpertise));

		if (log.isDebugEnabled())
			log.debug("Skill requirements are checking. Requirement[{}], player[{}] skills[{}]",
				skillRequirements, player.getName(), playerSkills);

		for (SkillRequirement sr : skillRequirements) {
			BigDecimal ps = playerSkills.get(sr.skill());
			if (ps == null || sr.expertise().compareTo(ps) > 0) return false;
		}

		if (log.isDebugEnabled())
			log.debug("Completed Quest requirements are checking." +
					" Requirement[{}], player[{}] completedSkills[{}]",
				questRequirements, player.getName(), completedQuests);

		if (!completedQuests.containsAll(questRequirements)) return false;

		Instant now = Instant.now();

		if (log.isDebugEnabled())
			log.debug("Time Range requirements are checking." +
					" Requirement[{}], player[{}] currentTime[{}]",
				timeRangeRequirements, player.getName(), now);

		for (TimeRangeRequirement trr : timeRangeRequirements) {
			if (!trr.start().isBefore(now) || !trr.end().isAfter(now)) return false;
		}

		if (log.isDebugEnabled())
			log.debug("Quest is locked for player[{}].", player.getName());

		return true;
	}

	boolean canComplete(Player player, Set<Quests> completedQuests, Map<Task, Long> completedTasks) {
		var itemRequirement = Stream.concat(
			itemCompleteRequirements.stream(),
			consumeItemCompleteRequirements.stream()
		).collect(Collectors.groupingBy(
			ItemCompleteRequirement::item,
			Collectors.summingInt(ItemCompleteRequirement::quantity)
		));

		var playerItems = player.getItems()
			.stream()
			.collect(Collectors.toMap(PlayerItem::getItem, PlayerItem::getQuantity));

		for (var entry : itemRequirement.entrySet()) {
			var item = entry.getKey();
			var requiredQuantity = entry.getValue();

			Long exist = playerItems.get(item);
			if (exist == null || exist < requiredQuantity) return false;
		}

		if (!completedQuests.containsAll(questCompleteRequirements)) return false;

		for (var tcr : taskCompleteRequirements) {
			var completedCount = completedTasks.get(tcr.task());
			if (completedCount == null || completedCount < tcr.times()) return false;
		}

		return true;
	}

}

// Requirements
record SkillRequirement(Skill skill, BigDecimal expertise) { }
record TimeRangeRequirement(Instant start, Instant end) { }

// Complete Requirements
record ItemCompleteRequirement(Item item, int quantity) { }
record TaskCompleteRequirement(Task task, int times) { }

// Rewards
record ExperienceReward(int experience) { }
record ItemReward(Item item, int quantity) { }
