package tr.com.milia.resurgence.skill;

import tr.com.milia.resurgence.player.Player;

import javax.persistence.*;
import javax.validation.constraints.Max;
import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;

@Entity
public class PlayerSkill implements Serializable {

	@EmbeddedId
	private PlayerSkillId id;

	@Column(nullable = false)
	@Max(100)
	private BigDecimal expertise;

	public PlayerSkill() {
	}

	public PlayerSkill(Player player, Skill skill, double expertise) {
		this.id = new PlayerSkillId(player, skill);
		this.expertise = BigDecimal.valueOf(expertise);
	}

	void learn(double value) {
		BigDecimal result = expertise.add(BigDecimal.valueOf(value));
		if (result.compareTo(BigDecimal.valueOf(100)) > 0) return;
		expertise = result;
	}

	public Skill getSkill() {
		return id.getSkill();
	}

	public BigDecimal getExpertise() {
		return expertise;
	}

	public BigDecimal skillContribution() {
		return expertise.multiply(BigDecimal.valueOf(id.getSkill().contribution()))
			.divide(BigDecimal.valueOf(100), 2, RoundingMode.CEILING);
	}

}
