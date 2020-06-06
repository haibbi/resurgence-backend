package tr.com.milia.resurgence.chat;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tr.com.milia.resurgence.player.Player;
import tr.com.milia.resurgence.player.PlayerNotFound;
import tr.com.milia.resurgence.player.PlayerService;

import java.util.List;

@Service
public class MailService {

	private final PlayerService playerService;
	private final MailRepository repository;
	private final ReportedMailRepository reportedMailRepository;

	public MailService(PlayerService playerService,
					   MailRepository repository,
					   ReportedMailRepository reportedMailRepository) {
		this.playerService = playerService;
		this.repository = repository;
		this.reportedMailRepository = reportedMailRepository;
	}

	public void send(String fromName, String toName, String content) {
		Player from = findPlayer(fromName);
		Player to = findPlayer(toName);

		Mail mail = new Mail(from, to, content);
		repository.save(mail);
	}

	public List<Mail> incoming(String playerName) {
		return repository.findAllByTo_NameAndDeletedIsFalseOrderByIdDesc(playerName);
	}

	public List<Mail> outgoing(String playerName) {
		return repository.findAllByFrom_NameAndDeletedBySenderIsFalseOrderByIdDesc(playerName);
	}

	@Transactional
	public void report(String player, Long id) {
		if (reportedMailRepository.findById(id).isPresent()) return;

		repository.findById(id)
			.filter(mail -> mail.getTo().getName().equals(player))
			.map(ReportedMail::new)
			.ifPresent(reportedMailRepository::save);
	}

	@Transactional
	public void read(String to, Long id) {
		repository.findById(id)
			.filter(mail -> mail.getTo().getName().equals(to))
			.ifPresent(Mail::markAsRead);
	}

	@Transactional
	public void delete(String player, Long id) {
		repository.findById(id)
			.ifPresent(mail -> {
				if (mail.getTo().getName().equals(player)) {
					mail.markAsDeleted();
				} else {
					mail.markAsDeletedBySender();
				}
			});
	}

	private Player findPlayer(String name) {
		return playerService.findByName(name).orElseThrow(PlayerNotFound::new);
	}
}
