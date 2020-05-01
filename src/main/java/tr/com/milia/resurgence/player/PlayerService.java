package tr.com.milia.resurgence.player;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.EventListener;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Service;
import tr.com.milia.resurgence.account.AccountService;
import tr.com.milia.resurgence.task.TaskSucceedResult;

import java.util.Optional;

@Service
public class PlayerService {
	private static final Logger log = LoggerFactory.getLogger(PlayerService.class);

	private final PlayerRepository repository;
	private final AccountService accountService;

	public PlayerService(PlayerRepository repository, AccountService accountService) {
		this.repository = repository;
		this.accountService = accountService;
	}

	public Optional<Player> findByName(String name) {
		return repository.findByName(name);
	}

	public Optional<Player> findByUsername(String username) {
		return repository.findByAccount_Email(username);
	}

	public Player create(String name, String username, Race race) {
		if (findByName(name).isPresent()) throw new NameAlreadyExistsException(name);
		if (findByUsername(username).isPresent()) throw new AccountAlreadyConnectedPlayer();

		var account = accountService.findByEmail(username).orElseThrow(AccountNotFound::new);

		var player = new Player(account, name, race, 500, 100, 0);

		player.created();
		return repository.save(player);
	}

	@EventListener(TaskSucceedResult.class)
	@Order(Ordered.HIGHEST_PRECEDENCE)
	public void onTaskSucceedResult(TaskSucceedResult result) {
		log.debug("Task Succeed Result {}", result);
		var player = result.getPlayer();
		player.increaseBalance(result.getMoneyGain());
		player.gainEXP(result.getExperienceGain());
	}

}
