package tr.com.milia.resurgence.task;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.test.util.ReflectionTestUtils;
import tr.com.milia.resurgence.item.Item;
import tr.com.milia.resurgence.player.Player;
import tr.com.milia.resurgence.player.PlayerService;
import tr.com.milia.resurgence.skill.PlayerSkill;
import tr.com.milia.resurgence.skill.Skill;

import java.util.Collections;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import static java.util.Collections.emptyMap;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.eq;

class TaskServiceTest {

	static final String USERNAME = "john_doe";
	TaskService taskService;
	PlayerService playerService;

	@BeforeEach
	void setUp() {
		playerService = Mockito.mock(PlayerService.class);
		taskService = new TaskService(playerService, Mockito.mock(ApplicationEventPublisher.class));
	}

	@Test
	void taskMustSucceed() {
		// task
		Task task = Task.BANK_RUBBERY;
		Skill skill = Skill.SNEAK;
		// difficulty and contribution are same, 'cause task must be performed successfully
		ReflectionTestUtils.setField(task, "difficulty", 5);
		ReflectionTestUtils.setField(skill, "contribution", 5);

		// player
		Player player = new Player();
		// the player have maximum skill expertise
		PlayerSkill sneakSkill = new PlayerSkill(player, skill, 100);
		ReflectionTestUtils.setField(player, "skills", Set.of(sneakSkill));

		// mock
		Mockito.when(playerService.findByName(eq(USERNAME))).thenReturn(Optional.of(player));

		// execution
		TaskResult result = taskService.perform(task, USERNAME, null);

		// asserts
		assertSucceedTask(task, result);
	}

	@Test
	void taskMustFailedWhenPlayerHaveNoExpertise() {
		// task
		Task task = Task.BANK_RUBBERY;
		Skill skill = Skill.SNEAK;
		ReflectionTestUtils.setField(task, "difficulty", 5);
		ReflectionTestUtils.setField(skill, "contribution", 5);

		// player
		Player player = new Player();
		// the player have maximum skill expertise
		PlayerSkill sneakSkill = new PlayerSkill(player, skill, 0);
		ReflectionTestUtils.setField(player, "skills", Set.of(sneakSkill));

		// mock
		Mockito.when(playerService.findByName(eq(USERNAME))).thenReturn(Optional.of(player));

		// execution
		TaskResult result = taskService.perform(task, USERNAME, null);

		// asserts
		assertFailedTask(result);
	}

	@Test
	void taskMustFailedWhenPlayerHaveNoSkill() {
		// task
		Task task = Task.BANK_RUBBERY;
		ReflectionTestUtils.setField(task, "difficulty", 5);

		// player
		Player player = new Player();
		ReflectionTestUtils.setField(player, "skills", Collections.emptySet());

		// mock
		Mockito.when(playerService.findByName(eq(USERNAME))).thenReturn(Optional.of(player));

		// execution
		TaskResult result = taskService.perform(task, USERNAME, null);

		// asserts
		assertFailedTask(result);
	}

	@Test
	void taskMustSucceedWithItem() {
		// task
		Task task = Task.BANK_RUBBERY;
		Skill skill = Skill.SNEAK;
		Item item = Item.KNIFE;

		// difficulty must equals (contribution + item contribution)
		// 'cause task must be performed successfully
		ReflectionTestUtils.setField(task, "difficulty", 10);
		ReflectionTestUtils.setField(skill, "contribution", 5);
		ReflectionTestUtils.setField(item, "skills", Map.of(skill, 5));

		// player
		Player player = new Player();
		// the player have maximum skill expertise
		PlayerSkill sneakSkill = new PlayerSkill(player, skill, 100);
		ReflectionTestUtils.setField(player, "skills", Set.of(sneakSkill));

		// mock
		Mockito.when(playerService.findByName(eq(USERNAME))).thenReturn(Optional.of(player));

		// execution
		TaskResult result = taskService.perform(task, USERNAME, Map.of(item, 1));

		// asserts
		assertSucceedTask(task, result);
	}

	@Test
	void taskNonAuxiliaryTest() {
		// task
		Task task = Task.BANK_RUBBERY;
		Skill nonAuxiliarySkill = Skill.GUN_MASTERY;
		Skill auxiliarySkill = Skill.SNEAK;

		ReflectionTestUtils.setField(task, "difficulty", 5);
		ReflectionTestUtils.setField(nonAuxiliarySkill, "contribution", 5);
		ReflectionTestUtils.setField(auxiliarySkill, "contribution", 5);

		// player
		Player player = new Player();
		// the player have maximum skill expertise
		PlayerSkill nonAuxiliaryPlayerSkill = new PlayerSkill(player, nonAuxiliarySkill, 100);
		PlayerSkill auxiliaryPlayerSkill = new PlayerSkill(player, auxiliarySkill, 0);
		ReflectionTestUtils.setField(player, "skills", Set.of(nonAuxiliaryPlayerSkill, auxiliaryPlayerSkill));

		// mock
		Mockito.when(playerService.findByName(eq(USERNAME))).thenReturn(Optional.of(player));

		// execution
		TaskResult result = taskService.perform(task, USERNAME, emptyMap());

		// asserts
		assertFailedTask(result);
	}

	@Test
	void taskAuxiliaryTest() {
		// task
		Task task = Task.BANK_RUBBERY;
		Skill nonAuxiliarySkill = Skill.GUN_MASTERY;
		Skill auxiliarySkill = Skill.SNEAK;

		ReflectionTestUtils.setField(task, "difficulty", 5);
		ReflectionTestUtils.setField(nonAuxiliarySkill, "contribution", 5);
		ReflectionTestUtils.setField(auxiliarySkill, "contribution", 5);

		// player
		Player player = new Player();
		// the player have maximum skill expertise
		PlayerSkill nonAuxiliaryPlayerSkill = new PlayerSkill(player, nonAuxiliarySkill, 0);
		PlayerSkill auxiliaryPlayerSkill = new PlayerSkill(player, auxiliarySkill, 100);
		ReflectionTestUtils.setField(player, "skills", Set.of(nonAuxiliaryPlayerSkill, auxiliaryPlayerSkill));

		// mock
		Mockito.when(playerService.findByName(eq(USERNAME))).thenReturn(Optional.of(player));

		// execution
		TaskResult result = taskService.perform(task, USERNAME, emptyMap());

		// asserts
		assertSucceedTask(task, result);
	}

	private void assertSucceedTask(Task task, TaskResult result) {
		assert result.isSucceed();
		assert result instanceof TaskSucceedResult;
		TaskSucceedResult succeedResult = (TaskSucceedResult) result;
		assert succeedResult.getExperienceGain() > 0;
		assert succeedResult.getMoneyGain() > 0;
		assert succeedResult.getSkillGain() != null;

		if (!succeedResult.getSkillGain().isEmpty()) {
			succeedResult.getSkillGain().forEach(s -> assertThat(task.getSkillGain().contains(s)));
		}
	}

	private void assertFailedTask(TaskResult result) {
		assert !result.isSucceed();
		assert result instanceof TaskFailedResult;
	}

}
