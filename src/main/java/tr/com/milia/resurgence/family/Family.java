package tr.com.milia.resurgence.family;

import org.springframework.data.domain.AbstractAggregateRoot;
import tr.com.milia.resurgence.player.Player;
import tr.com.milia.resurgence.player.Race;

import javax.persistence.*;
import javax.validation.constraints.Min;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Optional;
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

	@OneToOne(fetch = FetchType.LAZY)
	private Player consultant;

	@OneToMany(orphanRemoval = true)
	@JoinColumn(name = "family_id")
	private Set<Chief> chiefs;

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
		chiefs = new LinkedHashSet<>();
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

	void assignConsultant(Player consultant) {
		if (consultant.getName().equals(boss.getName())) throw new SelfAssignmentException();
		if (!consultant.getFamily().orElseThrow(FamilyNotFoundException::new).getName().equals(this.getName())) {
			throw new DifferentFamilyException();
		}

		this.consultant = consultant;
	}

	Chief createChief(Player chief) {
		if (chief.getName().equals(boss.getName())) throw new SelfAssignmentException();
		if (!chief.getFamily().orElseThrow(FamilyNotFoundException::new).getName().equals(this.getName())) {
			throw new DifferentFamilyException();
		}
		return new Chief(chief, this);
	}

	Optional<Chief> findChief(String chiefName) {
		return chiefs.stream().filter(chief -> chief.getChief().getName().equals(chiefName)).findFirst();
	}

	void removeConsultant() {
		this.consultant = null;
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

	public Optional<Player> getConsultant() {
		return Optional.ofNullable(consultant);
	}

	public Set<Chief> getChiefs() {
		return Collections.unmodifiableSet(chiefs);
	}
}
