package tr.com.milia.resurgence.player;

import org.springframework.data.domain.AbstractAggregateRoot;
import tr.com.milia.resurgence.account.Account;
import tr.com.milia.resurgence.family.Chief;
import tr.com.milia.resurgence.family.Family;
import tr.com.milia.resurgence.item.PlayerItem;
import tr.com.milia.resurgence.skill.PlayerSkill;
import tr.com.milia.resurgence.smuggling.NotEnoughMoneyException;

import javax.persistence.*;
import javax.validation.constraints.Min;
import java.util.Collections;
import java.util.Optional;
import java.util.Set;

@Entity
public class Player extends AbstractAggregateRoot<Player> {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@OneToOne(fetch = FetchType.LAZY)
	private Account account;

	@Column(unique = true, nullable = false, updatable = false)
	private String name;

	private String image;

	@Column(nullable = false)
	@Min(0)
	private long balance;

	@Column(nullable = false)
	@Min(0)
	private int health;

	@Column(nullable = false)
	private int honor;

	@Column(nullable = false)
	private int usableHonor;

	@Column(nullable = false)
	@Min(0)
	private int experience;

	@OneToMany(mappedBy = "player")
	private Set<PlayerSkill> skills;

	@OneToMany(mappedBy = "player")
	private Set<PlayerItem> items;

	@Enumerated(value = EnumType.STRING)
	@Column(nullable = false, updatable = false)
	private Race race;

	@OneToOne(fetch = FetchType.LAZY)
	@JoinTable(name = "family_members", joinColumns = @JoinColumn(name = "members_id"))
	private Family family;

	@OneToOne(fetch = FetchType.LAZY)
	@JoinTable(name = "chief_members", joinColumns = @JoinColumn(name = "members_id"))
	private Chief chief;

	public Player() {
	}

	public Player(Account account, String name, Race race, long balance, int health, int honor) {
		this.account = account;
		this.name = name;
		this.race = race;
		this.balance = balance;
		this.health = health;
		this.honor = honor;
	}

	void created() {
		registerEvent(new PlayerCreatedEvent(this));
	}

	public void increaseBalance(long value) {
		if (value < 0) throw new IllegalStateException();
		balance += value;
	}

	public void decreaseBalance(long value) {
		if (value < 0) throw new IllegalStateException();
		if (value > balance) throw new NotEnoughMoneyException();
		balance -= value;
	}

	public int increaseHonor(int value) {
		if (value < 0) throw new IllegalStateException();
		return honor += value;
	}

	public int increaseUsableHonor(int value) {
		if (value < 0) throw new IllegalStateException();
		return usableHonor += value;
	}

	public int decreaseHonor(int value) {
		if (value < 0) throw new IllegalStateException();
		return honor -= value;
	}

	public void gainEXP(int value) {
		experience += value;
	}

	public int getExperience() {
		return experience;
	}

	public Long getId() {
		return id;
	}

	public Account getAccount() {
		return account;
	}

	public String getName() {
		return name;
	}

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}

	public long getBalance() {
		return balance;
	}

	public int getHealth() {
		return health;
	}

	public int getHonor() {
		return honor;
	}

	public Set<PlayerSkill> getSkills() {
		return skills == null ? Collections.emptySet() : Collections.unmodifiableSet(skills);
	}

	public Set<PlayerItem> getItems() {
		return items == null ? Collections.emptySet() : Collections.unmodifiableSet(items);
	}

	public Race getRace() {
		return race;
	}

	public Optional<Family> getFamily() {
		return Optional.ofNullable(family);
	}
}
