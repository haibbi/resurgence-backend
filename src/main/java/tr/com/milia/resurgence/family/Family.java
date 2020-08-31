package tr.com.milia.resurgence.family;

import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;
import org.springframework.data.domain.AbstractAggregateRoot;
import org.springframework.lang.Nullable;
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

	@OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
	private Set<Chief> chiefs;

	@Enumerated(value = EnumType.STRING)
	@Column(nullable = false)
	private Building building;

	@OneToMany(fetch = FetchType.LAZY)
	@LazyCollection(LazyCollectionOption.EXTRA)
	private Set<Player> members;

	@Column(nullable = false)
	@Min(0)
	private long bank;

	private String image;

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
		if (chiefs != null) {
			for (Chief chief : chiefs) {
				chief.removeMember(member);
			}
		}
		findChief(member.getName()).ifPresent(this::removeChief);
		removeConsultantIfPresent(member.getName());
	}

	/**
	 * Remove player from family.
	 *
	 * @param member Who will be removed.
	 * @return {@code Player} if anyone is removed.
	 */
	@Nullable
	Player removeMember(String member) {
		if (members == null) return null;
		Optional<Player> playerToBeRemoved = members.stream().filter(m -> m.getName().equals(member)).findFirst();
		playerToBeRemoved.ifPresent(this::removeMember);
		return playerToBeRemoved.orElse(null);
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
		Building next = building.getNext();
		if (next == null) throw new BuildingGrowthException();
		build(next);
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
		Chief createdChief = new Chief(chief, this);
		chiefs.add(createdChief);
		return createdChief;
	}

	Optional<Chief> findChief(String chiefName) {
		return chiefs.stream().filter(chief -> chief.getChief().getName().equals(chiefName)).findFirst();
	}

	public void removeChief(Chief chief) {
		chiefs.remove(chief);
	}

	void removeConsultant() {
		this.consultant = null;
	}

	void removeConsultantIfPresent(String name) {
		if (consultant == null) return;
		if (consultant.getName().equals(name)) consultant = null;
	}

	public Optional<Chief> getChief(String name) {
		return chiefs.stream().filter(c -> c.getChief().getName().equals(name)).findFirst();
	}

	public Long getId() {
		return id;
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

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}
}
