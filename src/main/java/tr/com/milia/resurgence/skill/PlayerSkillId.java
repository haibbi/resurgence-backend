package tr.com.milia.resurgence.skill;

import tr.com.milia.resurgence.player.Player;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class PlayerSkillId implements Serializable {

	@ManyToOne(fetch = FetchType.LAZY)
	private Player player;

	@Enumerated(value = EnumType.STRING)
	@Column(nullable = false, updatable = false)
	private Skill skill;

	public PlayerSkillId() {
	}

	public PlayerSkillId(Player player, Skill skill) {
		this.player = player;
		this.skill = skill;
	}

	public Player getPlayer() {
		return player;
	}

	public Skill getSkill() {
		return skill;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		PlayerSkillId that = (PlayerSkillId) o;
		return Objects.equals(player, that.player) &&
			skill == that.skill;
	}

	@Override
	public int hashCode() {
		return Objects.hash(player, skill);
	}
}
