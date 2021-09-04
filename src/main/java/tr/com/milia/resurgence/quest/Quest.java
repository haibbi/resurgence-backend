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
import java.util.*;
import java.util.stream.Collectors;

public record Quest(
	Title titleRequirement,
	Set<SkillRequirement> skillRequirements,
	Set<Quests> questRequirements,
	Set<TimeRangeRequirement> timeRangeRequirements,

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
		return this.completeNeeds(player, completedQuests, completedTasks).isMeet();
	}

	CompleteNeeds completeNeeds(Player player, Set<Quests> completedQuests, Map<Task, Long> completedTasks) {
		CompleteNeeds needs = new CompleteNeeds(this);
		var playerItems = player.getItems()
			.stream()
			.collect(Collectors.toMap(PlayerItem::getItem, PlayerItem::getQuantity));

		for (var entry : consumeItemCompleteRequirements) {
			var item = entry.item();
			var requiredQuantity = entry.quantity();

			long exist = playerItems.getOrDefault(item, 0L);
			if (exist < requiredQuantity) {
				needs.addItemNeed(item, requiredQuantity - exist);
			}
		}

		final var quests = new HashSet<>(questCompleteRequirements);
		quests.removeAll(completedQuests);
		if (!quests.isEmpty()) {
			quests.forEach(needs::addQuestNeed);
		}

		for (var tcr : taskCompleteRequirements) {
			long completedCount = completedTasks.getOrDefault(tcr.task(), 0L);
			if (completedCount < tcr.times()) {
				needs.addTaskNeed(tcr.task(), tcr.times() - completedCount);
			}
		}

		return needs;
	}

}

// Requirements
record SkillRequirement(Skill skill, BigDecimal expertise) { }
record TimeRangeRequirement(Instant start, Instant end) { }

// Complete Requirements
record ItemCompleteRequirement(Item item, long quantity) { }
record TaskCompleteRequirement(Task task, long times) { }

// Rewards
record ExperienceReward(int experience) { }
record ItemReward(Item item, int quantity) { }

class CompleteNeeds {

	public static final CompleteNeeds EMPTY = new CompleteNeeds(null);

	private final Map<Item, Long> itemNeeds = new EnumMap<>(Item.class);
	private final Map<Task, Long> taskNeeds = new EnumMap<>(Task.class);
	private final List<Quests> questNeeds = new ArrayList<>();
	private final Quest quest;

	public CompleteNeeds(Quest quest) {
		this.quest = quest;
	}

	void addItemNeed(Item item, long quantity) {
		this.itemNeeds.put(item, quantity);
	}

	void addTaskNeed(Task task, long quantity) {
		this.taskNeeds.put(task, quantity);
	}

	void addQuestNeed(Quests quest) {
		this.questNeeds.add(quest);
	}

	Map<Item, Long> itemNeeds() {
		return Collections.unmodifiableMap(itemNeeds);
	}

	public Map<Task, Long> taskNeeds() {
		return Collections.unmodifiableMap(taskNeeds);
	}

	public List<Quests> questNeeds() {
		return Collections.unmodifiableList(questNeeds);
	}

	double percent() {
		if (quest == null) return 0;

		long itemTotal = quest.consumeItemCompleteRequirements()
			.stream()
			.mapToLong(ItemCompleteRequirement::quantity)
			.sum();
		long taskTotal = quest.taskCompleteRequirements()
			.stream()
			.mapToLong(TaskCompleteRequirement::times)
			.sum();
		long questTotal = quest.questCompleteRequirements().size();

		long itemNeedCount = itemNeeds.values().stream().reduce(0L, Long::sum);
		long taskNeedCount = taskNeeds.values().stream().reduce(0L, Long::sum);
		long questNeedCount = questNeeds.size();

		double total = itemTotal + taskTotal + questTotal;
		double need = itemNeedCount + taskNeedCount + questNeedCount;

		return (total - need) / total;
	}

	boolean isMeet() {
		return itemNeeds.isEmpty() && taskNeeds.isEmpty() && questNeeds.isEmpty();
	}
}
