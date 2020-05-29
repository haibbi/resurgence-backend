package tr.com.milia.resurgence.bank;

import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tr.com.milia.resurgence.player.Player;
import tr.com.milia.resurgence.player.PlayerNotFound;
import tr.com.milia.resurgence.player.PlayerService;

import java.util.List;

@Service
public class BankAccountLogService {

	private final BankAccountLogRepository repository;
	private final PlayerService playerService;

	public BankAccountLogService(BankAccountLogRepository repository, PlayerService playerService) {
		this.repository = repository;
		this.playerService = playerService;
	}

	@EventListener
	public void on(BankAccountAmountIncreasedEvent event) {
		repository.save(new BankAccountLog(event.getOwner(), event.getAmount(), true));
	}

	@EventListener
	public void on(BankAccountAmountDecreasedEvent event) {
		repository.save(new BankAccountLog(event.getOwner(), event.getAmount(), false));
	}

	@Transactional(readOnly = true)
	public List<BankAccountLog> logs(String ownerName) {
		Player owner = playerService.findByName(ownerName).orElseThrow(PlayerNotFound::new);

		return repository.findTop5ByOwnerOrderByTimeDesc(owner);
	}

}
