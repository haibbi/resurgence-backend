package tr.com.milia.resurgence.family;

import org.springframework.data.domain.AbstractAggregateRoot;
import tr.com.milia.resurgence.player.Player;
import tr.com.milia.resurgence.player.Race;

import javax.persistence.*;
import javax.validation.constraints.Min;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Set;

@Entity
public class Family extends AbstractAggregateRoot<Family> {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(unique = true, nullable = false, updatable = false)
	private String name;

	@OneToOne(fetch = FetchType.LAZY, optional = false)
	private Player boss;

	@Enumerated(value = EnumType.STRING)
	@Column(nullable = false)
	private Building building;

	@OneToMany(fetch = FetchType.LAZY)
	private Set<Player> members;

	@Column(nullable = false)
	@Min(0)
	private long bank;

	public Family() {
	}

	public Family(String name, Player boss, long bank, Building building) {
		this.name = name;
		this.boss = boss;
		this.bank = bank;
		members = new LinkedHashSet<>();
		build(building);
		addMember(boss);
	}

	/**
	 * Avoid non-transactional operation
	 */
	void addMember(Player member) {
		if (members.size() >= building.getSize()) {
			throw new MemberOutOfBoundsException();
		}
		if (member.getRace() != getRace()) {
			throw new RaceCompatibilityException();
		}
		members.add(member);
	}

	void removeMember(Player member) {
		members.remove(member);
	}

	void deposit(long amount) {
		bank += amount;
		registerEvent(new FamilyBankEvent(getName(), Reason.DEPOSIT, amount));
	}

	void withdraw(long amount) {
		if (amount > bank) throw new NotEnoughMoneyInBankException();
		bank -= amount;
		registerEvent(new FamilyBankEvent(getName(), Reason.WITHDRAW, amount));
	}

	void upgradeBuilding() {
		Building nextBuilding = Building.values()[building.ordinal() + 1];
		build(nextBuilding);
	}

	@Transient
	private void build(Building building) {
		long price = building.getPrice();
		if (price > bank) throw new NotEnoughMoneyInBankException();

		bank -= price;
		this.building = building;
		registerEvent(new FamilyBankEvent(getName(), Reason.BUILDING, price));
	}

	public String getName() {
		return name;
	}

	public Player getBoss() {
		return boss;
	}

	public Building getBuilding() {
		return building;
	}

	public Set<Player> getMembers() {
		return Collections.unmodifiableSet(members);
	}

	public long getBank() {
		return bank;
	}

	public Race getRace() {
		return boss.getRace();
	}
}
