package tr.com.milia.resurgence.family;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tr.com.milia.resurgence.family.Invitation.Direction;
import tr.com.milia.resurgence.player.Player;
import tr.com.milia.resurgence.player.PlayerNotFound;
import tr.com.milia.resurgence.player.PlayerService;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
public class HumanResourceService {

	private final PlayerService playerService;
	private final FamilyService familyService;
	private final InvitationRepository repository;

	public HumanResourceService(PlayerService playerService,
								FamilyService familyService,
								InvitationRepository repository) {
		this.playerService = playerService;
		this.familyService = familyService;
		this.repository = repository;
	}

	@Transactional
	public void invite(String playerName, String invitedByName) {
		Player player = findPlayer(playerName);
		Player invitedBy = findPlayer(invitedByName);

		Invitation invitation = new Invitation(player, invitedBy);
		if (repository.findByPlayerAndFamily(player, invitation.getFamily()).isPresent()) {
			throw new AlreadyInvitedException();
		}
		repository.save(invitation);
	}

	@Transactional
	public void application(String playerName, String familyName) {
		Player player = findPlayer(playerName);
		Family family = findFamily(familyName);

		Invitation invitation = new Invitation(player, family);
		if (repository.findByPlayerAndFamily(player, invitation.getFamily()).isPresent()) {
			throw new AlreadyAppliedException();
		}
		repository.save(invitation);
	}

	public List<Invitation> findAllByPlayer(String playerName) {
		Player player = findPlayer(playerName);
		return repository.findAllByPlayer(player);
	}

	public List<Invitation> findAllByFamily(String playerName) {
		Player player = findPlayer(playerName);

		Optional<Family> optionalFamily = player.getFamily();
		if (optionalFamily.isEmpty()) return Collections.emptyList();

		return repository.findAllByFamily(optionalFamily.get());
	}

	public List<Invitation> allInvitation(String playerName) {
		Player player = findPlayer(playerName);
		Optional<Family> family = player.getFamily();
		if (family.isPresent()) {
			return repository.findAllByPlayerOrFamily(player, family.get());
		}
		return repository.findAllByPlayer(player);
	}

	@Transactional
	public void accept(String playerOrBossName, Long id) {
		final Invitation invitation = repository.findById(id).orElseThrow(InvitationNotException::new);
		final Player player = invitation.getPlayer();
		Family family = invitation.getFamily();
		if (invitation.getDirection() == Direction.PLAYER
			&& player.getName().equals(playerOrBossName)) {
			family.addMember(player);
			repository.deleteAllByPlayer(player);
			return;
		} else if (invitation.getDirection() == Direction.FAMILY &&
			family.getBoss().getName().equals(playerOrBossName)) {
			family.addMember(player);
			repository.deleteAllByPlayer(player);
			return;
		}
		throw new InvitationNotException();
	}

	@Transactional
	public void leave(String playerName) {
		Player player = findPlayer(playerName);
		Family family = player.getFamily().orElseThrow(FamilyNotFoundException::new);

		if (family.getBoss().getName().equals(playerName)) throw new BossLeaveException();

		family.removeMember(player);
	}

	@Transactional
	public void fire(String playerName, String memberName) {
		Player player = findPlayer(playerName);
		Family family = player.getFamily().orElseThrow(FamilyNotFoundException::new);

		String boss = family.getBoss().getName();

		if (!boss.equals(playerName)) throw new FamilyAccessDeniedException();
		if (boss.equals(memberName)) throw new BossLeaveException();

		family.removeMember(memberName);
	}

	@Transactional
	public void destroy(String playerName) {
		Player player = findPlayer(playerName);
		Family family = player.getFamily().orElseThrow(FamilyNotFoundException::new);
		if (!family.getBoss().getName().equals(playerName)) throw new FamilyAccessDeniedException();

		familyService.delete(family);
	}

	@Transactional
	public void cancel(String player, Long id) {
		Invitation invitation = repository.findById(id).orElseThrow();

		Family family = invitation.getFamily();
		if (family.getBoss().getName().equals(player)) {
			repository.deleteById(id);
			return;
		}

		repository.deleteByIdAndPlayer_Name(id, player);
	}

	private Player findPlayer(String name) {
		return playerService.findByName(name).orElseThrow(PlayerNotFound::new);
	}

	private Family findFamily(String name) {
		return familyService.findByName(name).orElseThrow(FamilyNotFoundException::new);
	}

}
