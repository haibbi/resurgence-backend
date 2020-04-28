package tr.com.milia.resurgence.family;

import org.springframework.context.event.EventListener;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import tr.com.milia.resurgence.security.TokenAuthentication;
import tr.com.milia.resurgence.task.PlayerNotFound;

import java.util.List;

@Service
public class FamilyBankLogService {

	private final FamilyBankLogRepository repository;

	public FamilyBankLogService(FamilyBankLogRepository repository) {
		this.repository = repository;
	}

	@EventListener(FamilyBankEvent.class)
	public void onFamilyBankEvent(FamilyBankEvent event) {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (!(authentication instanceof TokenAuthentication)) throw new PlayerNotFound();

		TokenAuthentication tokenAuthentication = (TokenAuthentication) authentication;
		String member = tokenAuthentication.getPlayerName().orElseThrow(PlayerNotFound::new);
		repository.save(new FamilyBankLog(member, event.getAmount(), event.getReason()));
	}

	List<FamilyBankLog> findAllLogs() {
		return repository.findAllByOrderByDateDesc();
	}

}
