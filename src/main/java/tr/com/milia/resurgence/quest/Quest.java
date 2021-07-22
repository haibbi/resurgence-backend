package tr.com.milia.resurgence.quest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tr.com.milia.resurgence.item.Item;
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

public record Quest(
	Title titleRequirement,
	Set<SkillRequirement> skillRequirements,
	Set<Quests> questRequirements,
	Set<TimeRangeRequirement> timeRangeRequirements,

	Set<ItemCompleteRequirement> itemCompleteRequirements,
	Set<ConsumeItemCompleteRequirement> consumeItemCompleteRequirements,
	Set<TaskCompleteRequirement> taskCompleteRequirements,
	Set<QuestCompleteRequirement> questCompleteRequirements,

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


		Map<Skill, BigDecimal> playerSkills = player.getSkills().stream()
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

		return true;
	}

}

// Requirements
record TitleRequirement(Title title) { }
record SkillRequirement(Skill skill, BigDecimal expertise) { }
record QuestRequirement(Quests quest) { }
record TimeRangeRequirement(Instant start, Instant end) { }

// Complete Requirements
record ItemCompleteRequirement(Item item, int quantity) { }
record ConsumeItemCompleteRequirement(Item item, int quantity) { }
record TaskCompleteRequirement(Task task, int times) { }
record QuestCompleteRequirement(Quests quests) { }

// Rewards
record ExperienceReward(int experience) { }
record ItemReward(Item item, int quantity) { }

enum Status {PENDING, UNLOCKED, IN_PROGRESS, COMPLETED, DONE, CANCELED}
