package tr.com.milia.resurgence.family;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.EventListener;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import tr.com.milia.resurgence.FileUtils;
import tr.com.milia.resurgence.firebase.FirebaseService;
import tr.com.milia.resurgence.player.Player;
import tr.com.milia.resurgence.player.PlayerNotFound;
import tr.com.milia.resurgence.player.PlayerService;
import tr.com.milia.resurgence.task.TaskSucceedResult;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class FamilyService {

	public static final int REQUIRED_HONOR_FOUND_A_FAMILY = 1000;
	private static final Logger log = LoggerFactory.getLogger(FamilyService.class);

	private final FamilyRepository repository;
	private final PlayerService playerService;
	private final FirebaseService firebaseService;

	public FamilyService(FamilyRepository repository,
						 PlayerService playerService,
						 FirebaseService firebaseService) {
		this.repository = repository;
		this.playerService = playerService;
		this.firebaseService = firebaseService;
	}

	@Transactional
	public Family found(String playerName, String familyName) {
		final Building building = Building.HOME;
		final long buildingPrice = building.getPrice();
		final Player player = findPlayer(playerName);

		if (player.getHonor() < REQUIRED_HONOR_FOUND_A_FAMILY) throw new NotEnoughHonorException();
		if (player.getFamily().isPresent()) throw new AlreadyInFamilyException();
		if (repository.findByName(familyName).isPresent()) throw new DuplicateFamilyNameException();

		player.decreaseBalance(buildingPrice);

		Family family = new Family(familyName, player, buildingPrice, building);

		return repository.save(family);
	}

	public List<Family> findAll() {
		return repository.findAll();
	}

	@Transactional
	public Optional<Family> findFamilyByPlayerName(String playerName) {
		final Player player = findPlayer(playerName);
		return player.getFamily();
	}

	@Transactional
	public void deposit(String playerName, long amount) {
		final Player player = findPlayer(playerName);

		Family family = player.getFamily().orElseThrow(FamilyNotFoundException::new);

		player.decreaseBalance(amount);
		family.deposit(amount);

		repository.save(family); // todo sadece FamilyBankEvent atılması için save kullanıldı. Araştır
	}

	@Transactional
	public void withdraw(String playerName, long amount) {
		final Player player = findPlayer(playerName);

		Family family = player.getFamily().orElseThrow(FamilyNotFoundException::new);

		// only boss can withdraw
		if (!family.getBoss().getName().equals(player.getName())) throw new FamilyBankAccessDeniedException();

		family.withdraw(amount);
		player.increaseBalance(amount);

		repository.save(family); // todo sadece FamilyBankEvent atılması için save kullanıldı. Araştır
	}

	@Transactional
	public void assignConsultant(String playerName, String consultantName) {
		Player player = findPlayer(playerName);
		Family family = player.getFamily().orElseThrow(FamilyNotFoundException::new);
		if (!family.getBoss().getName().equals(playerName)) throw new FamilyAccessDeniedException();

		Player consultant = findPlayer(consultantName);
		family.assignConsultant(consultant);
	}

	@Transactional
	public void fireConsultant(String playerName) {
		Player player = findPlayer(playerName);
		Family family = player.getFamily().orElseThrow(FamilyNotFoundException::new);
		if (!family.getBoss().getName().equals(playerName)) throw new FamilyAccessDeniedException();

		family.removeConsultant();
	}

	@Transactional
	public void assignChief(String playerName, String chiefName) {
		Player player = findPlayer(playerName);
		Family family = player.getFamily().orElseThrow(FamilyNotFoundException::new);
		if (!family.getBoss().getName().equals(playerName)) throw new FamilyAccessDeniedException();

		Player chiefCandidate = findPlayer(chiefName);
		family.createChief(chiefCandidate);
	}

	@Transactional
	public void fireChief(String playerName, String chiefName) {
		Player player = findPlayer(playerName);
		Family family = player.getFamily().orElseThrow(FamilyNotFoundException::new);
		if (!family.getBoss().getName().equals(playerName)) throw new FamilyAccessDeniedException();
		family.findChief(chiefName).ifPresent(family::removeChief);
	}

	@Transactional
	public void assignMemberToChief(String bossName, String chiefName, String memberName) {
		Player boss = findPlayer(bossName);
		Family family = boss.getFamily().orElseThrow(FamilyNotFoundException::new);

		if (!family.getBoss().getName().equals(bossName)) throw new FamilyAccessDeniedException();

		Player member = findPlayer(memberName);

		if (member.getChief().isPresent()) throw new MemberAlreadyHaveChiefException();

		if (member.isChief()) throw new MemberAlreadyChiefException();

		family.findChief(chiefName).ifPresent(chief -> chief.addMember(member));

	}

	@Transactional
	public void removeMemberFromChief(String playerName, String chiefName, String memberName) {
		Player player = findPlayer(playerName);
		Family family = player.getFamily().orElseThrow(FamilyNotFoundException::new);
		if (!family.getBoss().getName().equals(playerName)) throw new FamilyAccessDeniedException();
		family.findChief(chiefName).ifPresent(chief -> {
			Player member = findPlayer(memberName);
			chief.removeMember(member);
		});

	}

	public void delete(Family family) {
		Set<Player> members = family.getMembers();
		for (Player member : members) {
			member.increaseBalance(family.getBank() / members.size());
		}
		repository.deleteById(family.getId());
	}

	@Transactional
	public void editImage(String boss, MultipartFile file) throws IOException {
		Family family = findFamilyByPlayerName(boss).orElseThrow();

		if (!family.getBoss().getName().equals(boss)) throw new FamilyAccessDeniedException();

		String filename = FileUtils.addExtension(file, "image/family/" + family.getName());
		String uri = firebaseService.uploadFile(file, filename);

		family.setImage(uri);
	}

	@Transactional
	public Long bankAccount(String playerName) {
		return findPlayer(playerName).getFamily().map(Family::getBank).orElseThrow();
	}

	private Player findPlayer(String playerName) {
		return playerService.findByName(playerName).orElseThrow(PlayerNotFound::new);
	}

	public Optional<Family> findByName(String name) {
		return repository.findByName(name);
	}

	@EventListener(TaskSucceedResult.class)
	@Order(Ordered.HIGHEST_PRECEDENCE + 3)
	public void onTaskSucceedResult(TaskSucceedResult event) {
		log.debug("Task Succeed Result {}", event);
		Player player = event.getPlayer();
		player.getChief().ifPresentOrElse(chief -> {
			// boss and chief share revenue
			long totalMoneyGain = event.getMoneyGain();
			long chiefsShare = (long) (totalMoneyGain * .03);
			long bossesShare = (long) (totalMoneyGain * .02);
			long diminishing = chiefsShare + bossesShare;
			log.debug("Total Money: {}, Chief's share: {}, Boss's Share: {}",
				totalMoneyGain, chiefsShare, bossesShare);
			chief.getChief().increaseBalance(chiefsShare);
			chief.getFamily().getBoss().increaseBalance(bossesShare);
			player.decreaseBalance(diminishing);
		}, () -> {
			// boss take the all revenue
			long totalMoneyGain = event.getMoneyGain();
			long bossesShare = (long) (totalMoneyGain * .05);
			log.debug("Total Money: {}, Boss's Share: {}", totalMoneyGain, bossesShare);
			player.getFamily().ifPresent(family -> family.getBoss().increaseBalance(bossesShare));
			player.decreaseBalance(bossesShare);
		});
	}

}
