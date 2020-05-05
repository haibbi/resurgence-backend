package tr.com.milia.resurgence.family;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tr.com.milia.resurgence.player.Player;
import tr.com.milia.resurgence.player.PlayerNotFound;
import tr.com.milia.resurgence.player.PlayerService;

import java.util.List;

@Service
public class AnnouncementService {

	private final PlayerService playerService;
	private final AnnouncementRepository repository;

	public AnnouncementService(PlayerService playerService, AnnouncementRepository repository) {
		this.playerService = playerService;
		this.repository = repository;
	}

	@Transactional
	public void add(String playerName, String title, String content, boolean general) {
		Player player = findPlayer(playerName);
		Family family = player.getFamily().orElseThrow(FamilyNotFoundException::new);
		Player boss = family.getBoss();

		if (!boss.equals(player)) throw new FamilyAccessDeniedException();

		Announcement announcement = new Announcement(family, boss, title, content, general);
		repository.save(announcement);
	}

	@Transactional
	public void edit(Long id, String playerName, String title, String content, boolean general) {
		Announcement announcement = repository.findById(id).orElseThrow();

		// only edit who crated it
		if (!announcement.getPlayer().getName().equals(playerName)) throw new FamilyAccessDeniedException();

		announcement.setTitle(title);
		announcement.setContent(content);
		announcement.setGeneral(general);
	}

	public void remove(Long id, String playerName) {
		Announcement announcement = repository.findById(id).orElseThrow();

		// only delete who crated it
		if (!announcement.getPlayer().getName().equals(playerName)) throw new FamilyAccessDeniedException();

		repository.deleteById(id);
	}

	public List<Announcement> findAll(String playerName) {
		Player player = findPlayer(playerName);
		Family family = player.getFamily().orElseThrow(FamilyNotFoundException::new);
		return repository.findAllByFamilyOrderByTimeDesc(family);
	}

	private Player findPlayer(String name) {
		return playerService.findByName(name).orElseThrow(PlayerNotFound::new);
	}
}
