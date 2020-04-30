package tr.com.milia.resurgence.family;

import tr.com.milia.resurgence.player.Player;

import javax.persistence.*;
import java.time.Instant;
import java.util.Optional;

@Entity
public class Invitation {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne(optional = false)
	private Player player;

	@ManyToOne(optional = false)
	private Family family;

	@Column(nullable = false)
	private Instant time;

	@Column(nullable = false)
	@Enumerated(EnumType.STRING)
	private Direction direction;

	public Invitation() {
	}

	public Invitation(Player player, Player boss) {
		if (boss.getName().equals(player.getName())) throw new SelfInvitationException();
		if (player.getFamily().isPresent()) throw new PlayerAlreadyHaveFamilyException();

		Optional<Family> optionalFamily = boss.getFamily();
		if (optionalFamily.isEmpty()) throw new FamilyNotFoundException();
		family = optionalFamily.get();

		if (!family.getBoss().getName().equals(boss.getName())) throw new FamilyAccessDeniedException();
		if (family.getRace() != player.getRace()) throw new RaceCompatibilityException();

		this.player = player;
		time = Instant.now();
		direction = Direction.PLAYER;
	}

	public Invitation(Player player, Family family) {
		this(player, family.getBoss());
		direction = Direction.FAMILY;
	}

	public Long getId() {
		return id;
	}

	public Player getPlayer() {
		return player;
	}

	public Family getFamily() {
		return family;
	}

	public Instant getTime() {
		return time;
	}

	public Direction getDirection() {
		return direction;
	}

	public enum Direction {
		PLAYER,
		FAMILY
	}
}
