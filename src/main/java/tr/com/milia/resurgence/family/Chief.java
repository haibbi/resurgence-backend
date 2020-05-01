package tr.com.milia.resurgence.family;

import tr.com.milia.resurgence.player.Player;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import java.io.Serializable;
import java.util.LinkedHashSet;
import java.util.Set;

@Entity
public class Chief implements Serializable {

	@Id
	@OneToOne
	private Player chief;

	@OneToOne(fetch = FetchType.LAZY)
	private Family family;

	@OneToMany(fetch = FetchType.LAZY)
	private Set<Player> members;

	public Chief() {
	}

	public Chief(Player chief, Family family) {
		this.chief = chief;
		this.family = family;
		this.members = new LinkedHashSet<>();
	}

	void addMember(Player player) {
		members.add(player);
	}

	void removeMember(Player player) {
		members.remove(player);
	}

	public Player getChief() {
		return chief;
	}

	public Family getFamily() {
		return family;
	}

	public Set<Player> getMembers() {
		return members;
	}
}
