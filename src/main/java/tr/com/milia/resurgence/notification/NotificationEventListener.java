package tr.com.milia.resurgence.notification;

import com.google.firebase.messaging.FirebaseMessagingException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.MessageSource;
import org.springframework.context.event.EventListener;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import tr.com.milia.resurgence.account.Account;
import tr.com.milia.resurgence.account.AccountService;
import tr.com.milia.resurgence.bank.InterestCompletedEvent;
import tr.com.milia.resurgence.chat.ChatMessageEvent;
import tr.com.milia.resurgence.family.FamilyMemberFiredEvent;
import tr.com.milia.resurgence.player.Player;
import tr.com.milia.resurgence.player.PlayerService;
import tr.com.milia.resurgence.task.multiplayer.MultiplayerTaskResultEvent;

import java.util.HashSet;
import java.util.Locale;
import java.util.Set;

@Component
public class NotificationEventListener {

	private static final Logger log = LoggerFactory.getLogger(NotificationEventListener.class);

	private final FirebaseService firebaseService;
	private final AccountService accountService;
	private final PlayerService playerService;
	private final MessageSource messageSource;
	private final MessageSource enumMessageSource;
	private final MessageService messageService;

	public NotificationEventListener(FirebaseService firebaseService,
									 AccountService accountService,
									 PlayerService playerService,
									 @Qualifier("defaultMessageSource") MessageSource messageSource,
									 MessageService messageService) {
		this.firebaseService = firebaseService;
		this.accountService = accountService;
		this.playerService = playerService;
		this.messageSource = messageSource;
		this.messageService = messageService;
		ResourceBundleMessageSource enumMessageSource = new ResourceBundleMessageSource();
		enumMessageSource.setDefaultEncoding("UTF-8");
		enumMessageSource.setBasename("enum/messages");
		this.enumMessageSource = enumMessageSource;
	}

	@Async
	@Transactional
	@EventListener(InterestCompletedEvent.class)
	public void onInterestCompletedEvent(InterestCompletedEvent event) {
		var optionalPlayer = playerService.findByName(event.getPlayerName());
		if (optionalPlayer.isEmpty()) return;

		Player player = optionalPlayer.get();
		long amount = event.getAmount();

		Locale locale = Locale.ENGLISH; // todo get account or player locale

		String title = messageSource.getMessage("push.message.interest.complete.title", null, locale);
		String body = messageSource.getMessage("push.message.interest.complete.body", new Object[]{amount},
			locale);

		sendNotification(player, title, body);
		messageService.send(player, title, body);
	}

	@Async
	@Transactional
	@EventListener(FamilyMemberFiredEvent.class)
	public void onFamilyMemberFiredEvent(FamilyMemberFiredEvent event) {
		var optionalPlayer = playerService.findByName(event.getRemovedMember().getName());
		if (optionalPlayer.isEmpty()) return;

		Player player = optionalPlayer.get();

		Locale locale = Locale.ENGLISH; // todo get account or player locale

		String title = messageSource.getMessage("push.message.fired.from.family.title", null, locale);
		String body = messageSource.getMessage("push.message.fired.from.family.body",
			new Object[]{event.getFamily().getName()}, locale);


		sendNotification(player, title, body);
		messageService.send(player, title, body);
	}

	@Async
	@Transactional
	@EventListener(MultiplayerTaskResultEvent.class)
	public void onTaskResult(MultiplayerTaskResultEvent event) {
		var optionalPlayer = playerService.findByName(event.getPlayer());
		if (optionalPlayer.isEmpty()) return;

		Player player = optionalPlayer.get();

		Locale locale = Locale.ENGLISH; // todo get account or player locale

		final String title;
		final String body;

		if (event.getResult().isSucceed()) {
			title = messageSource.getMessage("push.message.task.succeed.title", null, locale);
			body = messageSource.getMessage("push.message.task.succeed.body", new Object[]{
				enumMessageSource.getMessage(event.getPosition(), locale),
				event.getLeader()
			}, locale);
		} else {
			title = messageSource.getMessage("push.message.task.failed.title", null, locale);
			body = messageSource.getMessage("push.message.task.failed.body", new Object[]{
				enumMessageSource.getMessage(event.getPosition(), locale),
				event.getLeader()
			}, locale);
		}

		sendNotification(player, title, body);
		messageService.send(player, title, body);
	}

	@Async
	@Transactional
	@EventListener(ChatMessageEvent.class)
	public void on(ChatMessageEvent event) {
		playerService.findByName(event.getTo())
			.ifPresent(player -> sendNotification(player, event.getFrom(), event.getContent()));
	}

	private void sendNotification(Player player, String title, String body) {
		Account account = player.getAccount();
		Set<String> tokens = account.getPushNotificationTokens();

		if (tokens.isEmpty()) {
			log.warn("Can not send notification, account[{}] does not have a push message token. title[{}] body[{}]",
				account.getEmail(), title, body);
			return;
		}

		Set<String> tokensToBeDeleted = new HashSet<>();
		for (String token : tokens) {
			try {
				firebaseService.sendSimpleNotificationToUser(token, title, body);
			} catch (FirebaseMessagingException e) {
				log.error("Can not send notification to user[{}]. title[{}] body[{}]",
					account.getEmail(), title, body, e);
				switch (e.getMessagingErrorCode()) {
					case UNREGISTERED, INVALID_ARGUMENT,
						SENDER_ID_MISMATCH -> tokensToBeDeleted.add(token);
				}
			}
		}
		if (!tokensToBeDeleted.isEmpty()) {
			accountService.removeToken(account, tokensToBeDeleted);
		}
	}
}
