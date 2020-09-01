package tr.com.milia.resurgence.family;

import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tr.com.milia.resurgence.player.Player;
import tr.com.milia.resurgence.player.PlayerNotFound;
import tr.com.milia.resurgence.player.PlayerService;

import java.util.Collections;
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
	public void add(String playerName, String title, String content, boolean secret) {
		Player player = findPlayer(playerName);
		Family family = player.getFamily().orElseThrow(FamilyNotFoundException::new);
		Player boss = family.getBoss();

		if (!boss.equals(player)) throw new FamilyAccessDeniedException();

		Announcement announcement = new Announcement(family, title, content, secret);
		repository.save(announcement);
	}

	@Transactional
	public void edit(Long id, String playerName, String title, String content, boolean secret) {
		Announcement announcement = repository.findById(id).orElseThrow();

		// only edit who is boss
		if (!announcement.getFamily().getBoss().getName().equals(playerName)) throw new FamilyAccessDeniedException();

		announcement.setTitle(title);
		announcement.setContent(content);
		announcement.setSecret(secret);
	}

	@Transactional
	public void remove(Long id, String playerName) {
		Announcement announcement = repository.findById(id).orElseThrow();

		// only delete who is boss
		if (!announcement.getFamily().getBoss().getName().equals(playerName)) throw new FamilyAccessDeniedException();

		repository.deleteById(id);
	}

	public List<Announcement> findAll(String playerName, @Nullable String familyName) {
		Player player = findPlayer(playerName);

		return player.getFamily().map(repository::findAllByFamilyOrderByTimeDesc).orElseGet(() -> {
			if (familyName == null) return Collections.emptyList();
			return repository.findAllByFamily_NameAndSecretIsFalseOrderByTimeDesc(familyName);
		});
	}

	private Player findPlayer(String name) {
		return playerService.findByName(name).orElseThrow(PlayerNotFound::new);
	}
}
