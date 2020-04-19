package tr.com.milia.resurgence.skill;

import tr.com.milia.resurgence.player.Player;

import javax.persistence.*;
import javax.validation.constraints.Max;
import java.io.Serializable;
import java.math.BigDecimal;

@Entity
public class PlayerSkill implements Serializable {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY)
	private Player player;

	@Enumerated(value = EnumType.STRING)
	@Column(nullable = false, updatable = false)
	private Skill skill;

	@Column(nullable = false)
	@Max(100)
	private BigDecimal expertise;

	public PlayerSkill() {
	}

	public PlayerSkill(Player player, Skill skill, double expertise) {
		this.player = player;
		this.skill = skill;
		this.expertise = BigDecimal.valueOf(expertise);
	}

	void learn(double value) {
		BigDecimal result = expertise.add(BigDecimal.valueOf(value));
		if (result.compareTo(BigDecimal.valueOf(100)) > 0) return;
		expertise = result;
	}

	public Player getPlayer() {
		return player;
	}

	public Skill getSkill() {
		return skill;
	}

	public BigDecimal getExpertise() {
		return expertise;
	}

}
