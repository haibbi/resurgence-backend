package tr.com.milia.resurgence.family;

import com.google.firebase.cloud.StorageClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.EventListener;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.util.UriComponentsBuilder;
import tr.com.milia.resurgence.firebase.FirebaseConfiguration;
import tr.com.milia.resurgence.player.Player;
import tr.com.milia.resurgence.player.PlayerNotFound;
import tr.com.milia.resurgence.player.PlayerService;
import tr.com.milia.resurgence.task.TaskSucceedResult;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

@Service
public class FamilyService {

	public static final int REQUIRED_HONOR_FOUND_A_FAMILY = 1000;
	private static final Logger log = LoggerFactory.getLogger(FamilyService.class);

	private final FamilyRepository repository;
	private final PlayerService playerService;
	private final FirebaseConfiguration firebaseConfiguration;

	public FamilyService(FamilyRepository repository,
						 PlayerService playerService,
						 FirebaseConfiguration firebaseConfiguration) {
		this.repository = repository;
		this.playerService = playerService;
		this.firebaseConfiguration = firebaseConfiguration;
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
	public void assignMemberToChief(String playerName, String chiefName, String memberName) {
		Player player = findPlayer(playerName);
		Family family = player.getFamily().orElseThrow(FamilyNotFoundException::new);
		if (!family.getBoss().getName().equals(playerName)) throw new FamilyAccessDeniedException();
		family.findChief(chiefName).ifPresent(chief -> {
			Player member = findPlayer(memberName);
			if (member.getChief().isPresent()) throw new MemberAlreadyHaveChiefException();
			chief.addMember(member);
		});

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

		var storageClient = StorageClient.getInstance();
		var bucket = storageClient.bucket();
		var contentType = file.getContentType();
		var stream = file.getInputStream();

		var split = StringUtils.split(contentType, "/");
		Assert.state(Objects.requireNonNull(split).length == 2,
			"Invalid content type " + contentType);
		var type = split[1];
		var filename = "family-image/" + family.getName() + "." + type;

		bucket.create(filename, stream, contentType);

		String filepath = URLEncoder.encode(filename, StandardCharsets.UTF_8);
		String uri = UriComponentsBuilder.newInstance()
			.scheme("https")
			.host("firebasestorage.googleapis.com")
			.pathSegment("v0", "b", firebaseConfiguration.getStorageBucket(), "o", filepath)
			.queryParam("alt", "media")
			.build().toUriString();

		family.setImage(uri);
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
			log.debug("Total Money: {}, Chief's share: {}, Boss'es Share: {}",
				totalMoneyGain, chiefsShare, bossesShare);
			chief.getChief().increaseBalance(chiefsShare);
			chief.getFamily().getBoss().increaseBalance(bossesShare);
			player.decreaseBalance(diminishing);
		}, () -> {
			// boss take the all revenue
			long totalMoneyGain = event.getMoneyGain();
			long bossesShare = (long) (totalMoneyGain * .05);
			log.debug("Total Money: {}, Boss'es Share: {}", totalMoneyGain, bossesShare);
			player.getFamily().ifPresent(family -> family.getBoss().increaseBalance(bossesShare));
			player.decreaseBalance(bossesShare);
		});
	}

}
