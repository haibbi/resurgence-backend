package tr.com.milia.resurgence.quest;

import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;
import tr.com.milia.resurgence.player.Player;
import tr.com.milia.resurgence.player.Title;
import tr.com.milia.resurgence.skill.PlayerSkill;
import tr.com.milia.resurgence.skill.Skill;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.temporal.ChronoUnit;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class QuestUnlockedTest {
//
//	@Test
//	void questShouldBeUnlocked() {
//		Quest q = new Quest(
//			Title.EMPTY_SUIT,
//			Set.of(),
//			Set.of(),
//			Set.of(),
//			null,
//			null,
//			null,
//			null,
//			null,
//			null
//		);
//		Player p = new Player();
//		boolean unlocked = q.isUnlocked(p, Set.of());
//		assertTrue(unlocked);
//	}
//
//	@Test
//	void playerMustBeThief() {
//		Quest q = new Quest(
//			Title.THIEF,
//			Set.of(),
//			Set.of(),
//			Set.of(),
//			null,
//			null,
//			null,
//			null,
//			null,
//			null
//		);
//		Player p = new Player();
//		ReflectionTestUtils.setField(p, "experience", 2_000_000);
//		boolean unlocked = q.isUnlocked(p, Set.of());
//		assertTrue(unlocked);
//	}
//
//	@Test
//	void playerMustHaveSomeSkill() {
//		Quest q = new Quest(
//			Title.EMPTY_SUIT,
//			Set.of(
//				new SkillRequirement(Skill.SNEAK, BigDecimal.valueOf(50)),
//				new SkillRequirement(Skill.DEFENCE, BigDecimal.valueOf(40))
//			),
//			Set.of(),
//			Set.of(),
//			null,
//			null,
//			null,
//			null,
//			null,
//			null
//		);
//		Player p = new Player();
//		Set<PlayerSkill> ps = Set.of(
//			new PlayerSkill(p, Skill.SNEAK, 50),
//			new PlayerSkill(p, Skill.DEFENCE, 50)
//		);
//
//		ReflectionTestUtils.setField(p, "skills", ps);
//
//		boolean unlocked = q.isUnlocked(p, Set.of());
//		assertTrue(unlocked);
//	}
//
//	@Test
//	void lackOfSkills() {
//		Quest q = new Quest(
//			Title.EMPTY_SUIT,
//			Set.of(
//				new SkillRequirement(Skill.SNEAK, BigDecimal.valueOf(50)),
//				new SkillRequirement(Skill.DEFENCE, BigDecimal.valueOf(40))
//			),
//			Set.of(),
//			Set.of(),
//			null,
//			null,
//			null,
//			null,
//			null,
//			null
//		);
//		Player p = new Player();
//		Set<PlayerSkill> ps = Set.of(
//			new PlayerSkill(p, Skill.SNEAK, 50),
//			new PlayerSkill(p, Skill.DEFENCE, 39.9)
//		);
//
//		ReflectionTestUtils.setField(p, "skills", ps);
//
//		boolean unlocked = q.isUnlocked(p, Set.of());
//		assertFalse(unlocked);
//	}
//
//	@Test
//	void questMustBeLockedWhenTimeRangeReqAreNotMeet() {
//		Quest q = new Quest(
//			Title.EMPTY_SUIT,
//			Set.of(),
//			Set.of(),
//			Set.of(
//				new TimeRangeRequirement(
//					LocalDateTime.of(2020, 1, 1, 0, 0, 0).toInstant(ZoneOffset.UTC),
//					LocalDateTime.of(2020, 1, 1, 23, 59, 59).toInstant(ZoneOffset.UTC)
//				)
//			),
//			null,
//			null,
//			null,
//			null,
//			null,
//			null
//		);
//		Player p = new Player();
//
//		boolean unlocked = q.isUnlocked(p, Set.of());
//		assertFalse(unlocked);
//	}
//
//	@Test
//	void questUnlockedWhenTimeCome() {
//		Quest q = new Quest(
//			Title.EMPTY_SUIT,
//			Set.of(),
//			Set.of(),
//			Set.of(
//				new TimeRangeRequirement(
//					Instant.now().truncatedTo(ChronoUnit.DAYS),
//					Instant.now().truncatedTo(ChronoUnit.DAYS).plus(1, ChronoUnit.DAYS)
//				)
//			),
//			null,
//			null,
//			null,
//			null,
//			null,
//			null
//		);
//		Player p = new Player();
//
//		boolean unlocked = q.isUnlocked(p, Set.of());
//		assertTrue(unlocked);
//	}

}
