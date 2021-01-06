package tr.com.milia.resurgence.chat;

import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import tr.com.milia.resurgence.chat.persistence.OnlineState;
import tr.com.milia.resurgence.chat.persistence.OnlineStateRepository;

import java.util.LinkedList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

@Service
public class PresenceService {

	private final ExecutorService executor = Executors.newSingleThreadExecutor();

	private final SimpMessagingTemplate template;
	private final OnlineStateRepository onlineStateRepository;

	public PresenceService(SimpMessagingTemplate template,
						   OnlineStateRepository onlineStateRepository) {
		this.template = template;
		this.onlineStateRepository = onlineStateRepository;
	}

	public void save(Presence presence) {
		executor.execute(() -> {
			onlineStateRepository.save(new OnlineState(presence));
			if (!presence.isOnline()) {
				sendPresenceInfo();
			}
		});
	}

	@EventListener(value = TopicSubscribeEvent.class, condition = "#event.userStat")
	public void on(TopicSubscribeEvent event) {
		sendPresenceInfo();
	}

	private void sendPresenceInfo() {
		var onlineUsers = new LinkedList<String>();

		var allUserStats = onlineStateRepository.findTopByName()
			.stream()
			.map(Presence::from)
			.peek(os -> {
				if (os.isOnline()) onlineUsers.add(os.getName());
			})
			.collect(Collectors.toList());

		for (String user : onlineUsers) {
			template.convertAndSendToUser(user, Topics.USER_STATS, allUserStats);
		}
	}

}
