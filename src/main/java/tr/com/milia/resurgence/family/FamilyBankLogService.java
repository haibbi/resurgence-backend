package tr.com.milia.resurgence.family;

import org.springframework.context.event.EventListener;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tr.com.milia.resurgence.player.Player;
import tr.com.milia.resurgence.player.PlayerNotFound;
import tr.com.milia.resurgence.player.PlayerService;
import tr.com.milia.resurgence.security.TokenAuthentication;

import java.util.List;

@Service
public class FamilyBankLogService {

	private final FamilyBankLogRepository repository;
	private final PlayerService playerService;

	public FamilyBankLogService(FamilyBankLogRepository repository, PlayerService playerService) {
		this.repository = repository;
		this.playerService = playerService;
	}

	@EventListener(FamilyBankEvent.class)
	public void onFamilyBankEvent(FamilyBankEvent event) {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (!(authentication instanceof TokenAuthentication)) throw new PlayerNotFound();

		TokenAuthentication tokenAuthentication = (TokenAuthentication) authentication;
		String member = tokenAuthentication.getPlayerName();
		repository.save(new FamilyBankLog(member, event.getFamily(), event.getAmount(), event.getReason()));
	}

	@Transactional
	public List<FamilyBankLog> findAllLogs(String playerName) {
		Player player = findPlayer(playerName);
		Family family = player.getFamily().orElseThrow(FamilyNotFoundException::new);
		String familyName = family.getName();

		return repository.findAllByFamilyOrderByDateDesc(familyName);
	}

	private Player findPlayer(String player) {
		return playerService.findByName(player).orElseThrow(PlayerNotFound::new);
	}

}
