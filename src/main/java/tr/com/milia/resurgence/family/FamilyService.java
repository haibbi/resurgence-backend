package tr.com.milia.resurgence.family;

import org.hibernate.Hibernate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tr.com.milia.resurgence.player.Player;
import tr.com.milia.resurgence.player.PlayerService;
import tr.com.milia.resurgence.task.PlayerNotFound;

import java.util.List;
import java.util.Optional;

@Service
public class FamilyService {

	public static final int REQUIRED_HONOR_FOUND_A_FAMILY = 1000;

	private final FamilyRepository repository;
	private final PlayerService playerService;

	public FamilyService(FamilyRepository repository, PlayerService playerService) {
		this.repository = repository;
		this.playerService = playerService;
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

	@Transactional
	public List<Family> findAll() {
		List<Family> all = repository.findAll();
		for (Family family : all) {
			// initialize lazy fields
			Hibernate.initialize(family.getBoss());
			Hibernate.initialize(family.getMembers().size());
		}
		return all;
	}

	@Transactional
	public Optional<Family> findFamilyByPlayerName(String playerName) {
		final Player player = findPlayer(playerName);
		Optional<Family> family = player.getFamily();
		family.ifPresent(f -> Hibernate.initialize(f.getMembers().size()));

		return family;
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

		family.fireConsultant();
	}

	private Player findPlayer(String playerName) {
		return playerService.findByName(playerName).orElseThrow(PlayerNotFound::new);
	}

	public Optional<Family> findByName(String name) {
		return repository.findByName(name);
	}

}
