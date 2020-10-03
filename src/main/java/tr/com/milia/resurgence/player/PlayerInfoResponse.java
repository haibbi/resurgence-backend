package tr.com.milia.resurgence.player;

import tr.com.milia.resurgence.family.Chief;
import tr.com.milia.resurgence.family.Family;

import java.util.Collection;
import java.util.Optional;

class PlayerInfoResponse {
	private final String nickname;
	private final String image;
	private final Race race;
	private final int honor;
	private final Title title;
	private final String family;
	private Long balance;
	private Integer health;
	private Integer usableHonor;
	private Integer experience;

	public PlayerInfoResponse(Player player) {
		this(player, false);
	}

	public PlayerInfoResponse(Player player, boolean restricted) {
		nickname = player.getName();
		image = player.getImage();
		race = player.getRace();
		honor = player.getHonor();
		Optional<Family> optionalFamily = player.getFamily();
		family = optionalFamily.map(Family::getName).orElse(null);
		boolean isBoss = optionalFamily.map(Family::getBoss)
			.map(Player::getName)
			.stream()
			.anyMatch(n -> n.equals(nickname));
		boolean isCapo = false;
		if (!isBoss) {
			isCapo = optionalFamily.map(Family::getChiefs)
				.stream()
				.flatMap(Collection::stream)
				.map(Chief::getChief)
				.map(Player::getName)
				.anyMatch(s -> s.equals(nickname));
		}
		title = Title.find(player.getExperience(), isCapo, isBoss, true);
		if (!restricted) {
			experience = player.getExperience();
			usableHonor = player.getUsableHonor();
			health = player.getHealth();
			balance = player.getBalance();
		}
	}

	public String getNickname() {
		return nickname;
	}

	public String getImage() {
		return image;
	}

	public Race getRace() {
		return race;
	}

	public int getHonor() {
		return honor;
	}

	public Title getTitle() {
		return title;
	}

	public String getFamily() {
		return family;
	}

	public Long getBalance() {
		return balance;
	}

	public Integer getHealth() {
		return health;
	}

	public Integer getUsableHonor() {
		return usableHonor;
	}

	public Integer getExperience() {
		return experience;
	}
}
