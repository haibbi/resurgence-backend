package tr.com.milia.resurgence.bank;

import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;
import tr.com.milia.resurgence.player.Player;
import tr.com.milia.resurgence.player.PlayerNotFound;
import tr.com.milia.resurgence.player.PlayerService;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class BankTransferLogService {

	private final BankTransferLogRepository repository;
	private final PlayerService playerService;

	public BankTransferLogService(BankTransferLogRepository repository, PlayerService playerService) {
		this.repository = repository;
		this.playerService = playerService;
	}

	@EventListener
	public void on(TransferCompletedEvent event) {
		repository.save(new BankTransferLog(event.getFrom(), event.getTo(), event.getAmount(), event.getDescription()));
	}

	public List<BankTransferLog> findAll(String name) {
		Player player = playerService.findByName(name).orElseThrow(PlayerNotFound::new);

		List<BankTransferLog> fromList = repository.findTop5ByFromOrderByTimeDesc(player);
		List<BankTransferLog> toList = repository.findTop5ByToOrderByTimeDesc(player);

		return Stream.concat(fromList.stream(), toList.stream())
			.sorted(Comparator.comparing(BankTransferLog::getTime).reversed())
			.collect(Collectors.toList());
	}

}
