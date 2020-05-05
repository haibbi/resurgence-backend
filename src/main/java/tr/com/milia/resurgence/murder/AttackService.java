package tr.com.milia.resurgence.murder;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tr.com.milia.resurgence.RandomUtils;
import tr.com.milia.resurgence.item.Item;
import tr.com.milia.resurgence.item.PlayerItem;
import tr.com.milia.resurgence.item.PlayerItemService;
import tr.com.milia.resurgence.player.Player;
import tr.com.milia.resurgence.player.PlayerNotFound;
import tr.com.milia.resurgence.player.PlayerService;
import tr.com.milia.resurgence.skill.PlayerSkill;
import tr.com.milia.resurgence.skill.Skill;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Set;

@Service
public class AttackService {

	public static final long MAX_BULLET_COUNT = 50_000;

	private static final Logger log = LoggerFactory.getLogger(AttackService.class);
	private static final Set<Skill> ATTACKER_SKILL = Set.of(Skill.GUN_MASTERY);
	private static final Set<Skill> VICTIM_SKILL = Set.of(Skill.DEFENCE, Skill.SNEAK);

	private final PlayerService playerService;
	private final DetectiveAgency agency;
	private final PlayerItemService itemService;
	private final ApplicationEventPublisher eventPublisher;

	public AttackService(PlayerService playerService,
						 DetectiveAgency agency,
						 PlayerItemService itemService,
						 ApplicationEventPublisher eventPublisher) {
		this.playerService = playerService;
		this.agency = agency;
		this.itemService = itemService;
		this.eventPublisher = eventPublisher;
	}

	@Transactional
	public AttackResult attack(String attackerName, String victimName, long bullet) {
		Player attacker = findPlayer(attackerName);
		Player victim = findPlayer(victimName);

		boolean succeed = attack(attacker, victim, bullet, false);

		if (succeed) {
			log.info("{} killed {}.", attackerName, victimName);
			eventPublisher.publishEvent(new PlayerDeadEvent(victim));
			return new AttackResult(true, false);
		}

		log.info("{}'s kill attempt to {} failed.", attackerName, victimName);
		boolean backfireSucceed = attack(victim, attacker, bullet, true);

		if (backfireSucceed) {
			log.info("[BACKFIRE] {} killed {}.", attackerName, victimName);
			eventPublisher.publishEvent(new PlayerDeadEvent(attacker));
		} else {
			log.info("[BACKFIRE] {}'s kill attempt to {} failed.", attackerName, victimName);
		}

		return new AttackResult(false, backfireSucceed);
	}

	/**
	 * @param attacker   Attacker
	 * @param victim     Victim
	 * @param bullet     Bullet quantity
	 * @param isBackfire is backfire
	 * @return {@code true} if succeed
	 */
	private boolean attack(Player attacker, Player victim, long bullet, boolean isBackfire) {
		if (!isBackfire && !canAttack(attacker.getName(), victim.getName())) throw new VictimDeterminationException();

		long attackerBulletCount = attacker.getItems().stream()
			.filter(playerItem -> playerItem.getItem() == Item.BULLET)
			.mapToLong(PlayerItem::getQuantity)
			.sum();

		if (attackerBulletCount == 0) {
			if (isBackfire) return false;
			throw new LackOfBulletException();
		}

		// backfire attacker can only use what it got
		if (isBackfire && attackerBulletCount < bullet) {
			bullet = attackerBulletCount;
		}

		itemService.removeItem(attacker, Item.BULLET, bullet);

		double bulletFactor = (double) bullet / MAX_BULLET_COUNT;

		BigDecimal attackerPoint = attacker.getSkills().stream()
			.filter(s -> ATTACKER_SKILL.contains(s.getSkill()))
			.findFirst()
			.map(PlayerSkill::skillContribution)
			.orElse(BigDecimal.ZERO);

		attackerPoint = attackerPoint.multiply(BigDecimal.valueOf(bulletFactor));

		BigDecimal victimPoint = victim.getSkills().stream()
			.filter(s -> VICTIM_SKILL.contains(s.getSkill()))
			.findFirst()
			.map(PlayerSkill::skillContribution)
			.orElse(BigDecimal.ZERO);

		double powerFactor = attackerPoint
			.divide(victimPoint, 2, RoundingMode.CEILING).doubleValue();

		double successRatio = powerFactor / (powerFactor + 1);
		if (isBackfire) successRatio += .05;

		double random = RandomUtils.random();

		if (!isBackfire) agency.clean(attacker.getName(), victim.getName());

		return random < successRatio;
	}

	private boolean canAttack(String attacker, String victim) {
		List<AgencyStatus> statuses = agency.status(attacker);
		for (AgencyStatus status : statuses) {
			if (status.wanted.getName().equals(victim)
				&& status.status == AgencyStatus.Status.FOUND) {
				return true;
			}
		}
		return false;
	}

	private Player findPlayer(String name) {
		return playerService.findByName(name).orElseThrow(PlayerNotFound::new);
	}

}
