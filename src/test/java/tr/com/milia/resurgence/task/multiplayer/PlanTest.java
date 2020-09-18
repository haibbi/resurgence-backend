package tr.com.milia.resurgence.task.multiplayer;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static tr.com.milia.resurgence.task.multiplayer.MultiPlayerTask.HEIST;
import static tr.com.milia.resurgence.task.multiplayer.MultiPlayerTask.Position.DRIVER;
import static tr.com.milia.resurgence.task.multiplayer.MultiPlayerTask.Position.LEADER;

class PlanTest {

	private static final String leader = "p-leader";
	private static final String driver = "p-driver";

	@BeforeEach
	void setUp() {
		Plan.clean();
	}

	@Test
	void planMustHaveALeaderAndTask() {
		assertThrows(NullPointerException.class, () -> new Plan(HEIST, null, null));
		assertThrows(NullPointerException.class, () -> new Plan(null, leader, null));
	}

	@Test
	void quorumMustBeHonored() {
		Plan plan = new Plan(HEIST, leader, null);
		assertThrows(QuorumException.class, plan::check);
	}

	@Test
	void allMemberMustBeReady() {
		Plan plan = new Plan(HEIST, leader, null);
		plan.add(DRIVER, driver);
		assertThrows(NotAllMemberReadyException.class, plan::check);
	}

	@Test
	void heistMustPassCheckWithOneLeaderAndOneDriver() {
		Plan plan = new Plan(HEIST, leader, null);
		plan.add(DRIVER, driver);
		plan.ready(driver, null);
		assertDoesNotThrow(plan::check);
	}

	@Test
	void leaderMustOnlyInOnePlan() {
		new Plan(HEIST, leader, null);
		assertThrows(MemberHaveAPlanException.class, () -> new Plan(HEIST, leader, null));
		assertEquals(1, Plan.getPlans().size());
	}

	@Test
	void memberShouldAddedOnce() {
		Plan plan = new Plan(HEIST, leader, null);
		plan.add(DRIVER, driver);

		assertThrows(MemberHaveAPlanException.class, () -> plan.add(DRIVER, driver));
	}

	@Test
	void thereIsOnlyOneLeader() {
		Plan plan = new Plan(HEIST, leader, null);
		assertThrows(IllegalArgumentException.class, () -> plan.add(LEADER, leader + 2));

	}

	@Test
	void canAddOnlyQuorumCount() {
		Plan plan = new Plan(HEIST, leader, null);
		plan.add(DRIVER, driver);
		assertThrows(PositionLimitExceededException.class, () -> plan.add(DRIVER, driver + 2));
	}

	@Test
	void ifLeaderExitAllPlanGoesAway() {
		Plan plan = new Plan(HEIST, leader, null);
		assertEquals(1, Plan.getPlans().size());
		plan.remove(leader);
		assertTrue(Plan.getPlans().isEmpty());
	}

	@Test
	void memberShouldBeRemovable() {
		Plan plan = new Plan(HEIST, leader, null);
		plan.add(DRIVER, driver);
		plan.ready(driver, null);
		assertDoesNotThrow(plan::check);
		plan.remove(driver);
		assertThrows(QuorumException.class, plan::check);
	}
}
