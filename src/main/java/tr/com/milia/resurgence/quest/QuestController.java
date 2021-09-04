package tr.com.milia.resurgence.quest;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.MessageSource;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import tr.com.milia.resurgence.item.ItemResponse;
import tr.com.milia.resurgence.security.TokenAuthentication;
import tr.com.milia.resurgence.task.TaskResponse;

import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/quest")
public class QuestController {

	private final QuestService service;
	private final MessageSource ms;
	private final MessageSource enumMs;

	public QuestController(QuestService service,
						   @Qualifier("defaultMessageSource") MessageSource ms,
						   @Qualifier("enumMessageSource") MessageSource enumMs
	) {
		this.service = service;
		this.ms = ms;
		this.enumMs = enumMs;
	}

	@GetMapping
	@Transactional(readOnly = true)
	public List<QuestResponse> quests(TokenAuthentication authentication, Locale locale) {
		return service.playerQuests(authentication.getPlayerName())
			.stream()
			.filter(service::isUnlocked)
			.peek(service::updateDate)
			.sorted(Comparator.comparingInt(QuestEntity::ordinal))
			.map(q -> {
				final var canComplete = service.canComplete(q);
				final var name = enumMs.getMessage(q.getQuestEnum(), locale);
				final var description = ms.getMessage("quest.description." + q.getName(), null, locale);
				final var completeNeeds = service.needs(q);
				final var needsResponse = new CompleteNeedsResponse(completeNeeds);
				return new QuestResponse(q.getId(), q.getQuest(), q.getStatus(), name,
					description, canComplete, needsResponse, completeNeeds.percent());
			})
			.toList();
	}

	@PostMapping
	@Transactional
	public ResponseEntity<Void> perform(TokenAuthentication authentication, @RequestParam("id") Long id) {
		this.service.complete(id, authentication.getPlayerName());
		return ResponseEntity.ok().build();
	}

	@Transactional
	@DeleteMapping
	public void cancel(TokenAuthentication authentication, @RequestParam("id") Long id) {
		var entity = this.service.complete(id, authentication.getPlayerName());
		entity.cancel();
	}

}

record QuestResponse(
	Long id,
	Quest quest,
	QuestStatus status,
	String name,
	String description,
	boolean canComplete,
	CompleteNeedsResponse needs,
	double percentage
) {
}

class CompleteNeedsResponse {

	private final List<ItemNeed> itemNeeds;
	private final List<TaskNeed> taskNeeds;
	private final List<Quests> questNeeds;

	public CompleteNeedsResponse(CompleteNeeds needs) {
		this.itemNeeds = needs.itemNeeds().entrySet().stream()
			.map(e -> new ItemNeed(new ItemResponse(e.getKey()), e.getValue()))
			.collect(Collectors.toList());
		this.taskNeeds = needs.taskNeeds().entrySet().stream()
			.map(e -> new TaskNeed(new TaskResponse(e.getKey()), e.getValue()))
			.collect(Collectors.toList());
		this.questNeeds = needs.questNeeds();
	}

	public List<ItemNeed> getItemNeeds() {
		return itemNeeds;
	}

	public List<TaskNeed> getTaskNeeds() {
		return taskNeeds;
	}

	public List<Quests> getQuestNeeds() {
		return questNeeds;
	}

	record ItemNeed(ItemResponse item, long quantity) {
	}

	record TaskNeed(TaskResponse task, long quantity) {
	}
}
