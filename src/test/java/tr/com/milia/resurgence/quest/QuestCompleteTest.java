package tr.com.milia.resurgence.quest;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.springframework.test.util.ReflectionTestUtils;
import tr.com.milia.resurgence.item.Item;
import tr.com.milia.resurgence.item.PlayerItem;
import tr.com.milia.resurgence.item.PlayerItemService;
import tr.com.milia.resurgence.player.Player;
import tr.com.milia.resurgence.player.Race;
import tr.com.milia.resurgence.task.TaskLogService;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class QuestCompleteTest {
//
//	private QuestService questService;
//	private PlayerItemService pis;
//	private QuestRepository questRepository;
//	private TaskLogService taskLogService;
//
//	@BeforeEach
//	void setUp() {
//		pis = Mockito.mock(PlayerItemService.class);
//		questRepository = Mockito.mock(QuestRepository.class);
//		taskLogService = Mockito.mock(TaskLogService.class);
//		questService = new QuestService(questRepository, pis, taskLogService, null);
//	}
//
//	@Test
//	void playerCanNotCompleteWhenMissingItems() {
//		Quest q = new Quest(
//			null,
//			null,
//			null,
//			null,
//			Set.of(
//				new ItemCompleteRequirement(Item.KNIFE, 10),
//				new ItemCompleteRequirement(Item.ASPIRIN, 20)
//			),
//			Set.of(
//				new ItemCompleteRequirement(Item.BULLET, 50),
//				new ItemCompleteRequirement(Item.FORD_FIESTA, 1)
//			),
//			null,
//			null,
//			null,
//			null
//		);
//		Player p = new Player();
//		boolean canComplete = q.canComplete(p, Set.of(), Map.of());
//		assertFalse(canComplete);
//	}
//
//	@Test
//	void playerCanCompleteWhenHaveEnoughItems() {
//		Quest q = new Quest(
//			null,
//			null,
//			null,
//			null,
//			Set.of(
//				new ItemCompleteRequirement(Item.KNIFE, 1),
//				new ItemCompleteRequirement(Item.BULLET, 2)
//			),
//			Set.of(
//				new ItemCompleteRequirement(Item.GLOCK, 1),
//				new ItemCompleteRequirement(Item.BULLET, 2)
//			),
//			Collections.emptySet(),
//			Collections.emptySet(),
//			null,
//			null
//		);
//		Player p = new Player();
//		Set<PlayerItem> playerItems = Set.of(
//			new PlayerItem(p, Item.KNIFE, 1),
//			new PlayerItem(p, Item.GLOCK, 1),
//			new PlayerItem(p, Item.BULLET, 4)
//		);
//		ReflectionTestUtils.setField(p, "items", playerItems);
//		boolean canComplete = q.canComplete(p, Set.of(), Map.of());
//		assertTrue(canComplete);
//	}
//
//	@Test
//	void consumableItemRequirementsMustBeConsumed() {
//		Quest q = new Quest(
//			null,
//			null,
//			null,
//			null,
//			Collections.emptySet(),
//			Set.of(
//				new ItemCompleteRequirement(Item.GLOCK, 1),
//				new ItemCompleteRequirement(Item.BULLET, 2)
//			),
//			Collections.emptySet(),
//			Collections.emptySet(),
//			new ExperienceReward(0),
//			Collections.emptySet()
//		);
//		Player p = new Player(null, "dummy", Race.COSA_NOSTRA, 0, 0, 0);
//		Set<PlayerItem> playerItems = Set.of(
//			new PlayerItem(p, Item.GLOCK, 2),
//			new PlayerItem(p, Item.BULLET, 4)
//		);
//		ReflectionTestUtils.setField(p, "items", playerItems);
//		var questEntity = new QuestEntity(p, null);
//		when(questRepository.findById(eq(0L))).thenReturn(Optional.of(questEntity));
//		ReflectionTestUtils.setField(questEntity, "status", QuestStatus.IN_PROGRESS);
//		questService.complete(0L, "dummy");
//
//		ArgumentCaptor<Item> itemArg = ArgumentCaptor.forClass(Item.class);
//		ArgumentCaptor<Long> quantityArg = ArgumentCaptor.forClass(Long.class);
//		verify(pis, times(2)).removeItem(any(), itemArg.capture(), quantityArg.capture());
//
//		var allRemovedItems = itemArg.getAllValues();
//		var allQuantities = quantityArg.getAllValues();
//		assertEquals(2, allRemovedItems.size());
//		assertTrue(allRemovedItems.containsAll(List.of(Item.BULLET, Item.GLOCK)));
//		for (int i = 0, allRemovedItemsSize = allRemovedItems.size(); i < allRemovedItemsSize; i++) {
//			var item = allRemovedItems.get(i);
//			var quantity = allQuantities.get(i);
//			if (item == Item.GLOCK) assertEquals(1, quantity);
//			if (item == Item.BULLET) assertEquals(2, quantity);
//		}
//	}
//
//	@Test
//	void mustGainExp() {
//		ExperienceReward expReward = new ExperienceReward(1);
//		Quest q = new Quest(
//			null,
//			null,
//			null,
//			null,
//			Collections.emptySet(),
//			Collections.emptySet(),
//			Collections.emptySet(),
//			Collections.emptySet(),
//			expReward,
//			Collections.emptySet()
//		);
//		Player p = new Player(null, "dummy", Race.COSA_NOSTRA, 0, 0, 0);
//		int oldExp = p.getExperience();
//		var questEntity = new QuestEntity(p, null);
//		when(questRepository.findById(eq(0L))).thenReturn(Optional.of(questEntity));
//		ReflectionTestUtils.setField(questEntity, "status", QuestStatus.IN_PROGRESS);
//		questService.complete(0L, "dummy");
//		int newExp = p.getExperience();
//		assertEquals(oldExp + expReward.experience(), newExp);
//	}
//

}
