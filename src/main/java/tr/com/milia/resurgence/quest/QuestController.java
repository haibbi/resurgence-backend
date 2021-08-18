package tr.com.milia.resurgence.quest;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.MessageSource;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import tr.com.milia.resurgence.security.TokenAuthentication;

import java.util.List;
import java.util.Locale;

@RestController
@RequestMapping("/quest")
public class QuestController {

	private final QuestService service;
	private final MessageSource ms;

	public QuestController(QuestService service, @Qualifier("defaultMessageSource") MessageSource ms) {
		this.service = service;
		this.ms = ms;
	}

	@GetMapping
	@Transactional(readOnly = true)
	public List<QuestResponse> quests(TokenAuthentication authentication, Locale locale) {
		return service.playerQuests(authentication.getPlayerName())
			.stream()
			.map(q -> {
				// check unlocked first. Player can not complete locked quests.
				var unlocked = service.isUnlocked(q);
				var canComplete = unlocked && service.canComplete(q);
				var name = ms.getMessage("quest.name." + q.getName(), null, locale);
				var description = ms.getMessage("quest.description." + q.getName(), null, locale);
				return new QuestResponse(q.getId(), q.getQuest(), q.getStatus(),
					name, description, unlocked, canComplete);
			})
			.toList();
	}

	@PostMapping
	@Transactional
	public QuestResponse perform(TokenAuthentication authentication,
								 Locale locale,
								 @RequestParam("id") Long id) {
		var entity = this.service.complete(id, authentication.getPlayerName());
		var name = ms.getMessage("quest.name." + entity.getName(), null, locale);
		var description = ms.getMessage("quest.description." + entity.getName(), null, locale);
		return new QuestResponse(entity.getId(), entity.getQuest(), entity.getStatus(),
			name, description, true, false);
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
	boolean isUnlocked,
	boolean canComplete
) {
}
