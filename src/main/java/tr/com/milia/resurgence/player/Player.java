package tr.com.milia.resurgence.player;

import org.springframework.data.domain.AbstractAggregateRoot;
import tr.com.milia.resurgence.account.Account;
import tr.com.milia.resurgence.family.Chief;
import tr.com.milia.resurgence.family.Family;
import tr.com.milia.resurgence.item.Item;
import tr.com.milia.resurgence.item.PlayerItem;
import tr.com.milia.resurgence.skill.PlayerSkill;

import javax.persistence.*;
import javax.validation.constraints.Min;
import java.util.*;
import java.util.stream.Collectors;

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

	@OneToMany(mappedBy = "id.player")
	private Set<PlayerSkill> skills;

	@OneToMany(mappedBy = "id.player")
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
		final Title oldTitle = getTitle();
		experience += value;
		final Title newTitle = getTitle();

		if (oldTitle != newTitle) {
			registerEvent(new PlayerLevelUpEvent(name, newTitle));
		}
	}

	public int getExperience() {
		return experience;
	}

	public boolean isChief() {
		return family.getChief(name).isPresent();
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

	public void increaseHealth(int value) {
		this.health += value;
		if (this.health > 100) this.health = 100;
	}

	public void decreaseHealth(int value) {
		this.health -= value;
		if (this.health < 0) this.health = 0;
	}

	public boolean isHealthy() {
		return this.health >= 100;
	}

	public int getHonor() {
		return honor;
	}

	public int getUsableHonor() {
		return usableHonor;
	}

	public Set<PlayerSkill> getSkills() {
		return skills == null ? Collections.emptySet() : Collections.unmodifiableSet(skills);
	}

	public Set<PlayerItem> getItems() {
		return items == null ? Collections.emptySet() : Collections.unmodifiableSet(items);
	}

	public Map<Item, PlayerItem> getItemMap() {
		return getItems().stream().collect(Collectors.toMap(PlayerItem::getItem, p -> p));
	}

	public Race getRace() {
		return race;
	}

	public Optional<Family> getFamily() {
		return Optional.ofNullable(family);
	}

	public Optional<Chief> getChief() {
		return Optional.ofNullable(chief);
	}

	public Title getTitle() {
		Optional<Family> family = getFamily();
		boolean isBoss = family.map(Family::getBoss)
			.map(Player::getName)
			.stream()
			.anyMatch(n -> n.equals(name));
		boolean isCapo = false;
		if (!isBoss) {
			isCapo = family.map(Family::getChiefs)
				.stream()
				.flatMap(Collection::stream)
				.map(Chief::getChief)
				.map(Player::getName)
				.anyMatch(s -> s.equals(name));
		}
		return Title.find(experience, isCapo, isBoss, true);
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		Player player = (Player) o;
		return Objects.equals(id, player.id) &&
			name.equals(player.name);
	}

	@Override
	public int hashCode() {
		return Objects.hash(id, name);
	}
}
