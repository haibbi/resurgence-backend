package tr.com.milia.resurgence.player;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.validation.constraints.Max;
import java.io.Serializable;

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
	private double expertise;

	public PlayerSkill() {
	}

	public PlayerSkill(Player player, Skill skill, double expertise) {
		this.player = player;
		this.skill = skill;
		this.expertise = expertise;
	}

	private void learn(double value) {
		expertise += value;
	}

	public Player getPlayer() {
		return player;
	}

	public Skill getSkill() {
		return skill;
	}

	public double getExpertise() {
		return expertise;
	}

}
