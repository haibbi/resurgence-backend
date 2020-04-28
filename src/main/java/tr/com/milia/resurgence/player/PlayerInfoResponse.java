package tr.com.milia.resurgence.player;

import tr.com.milia.resurgence.family.Family;

class PlayerInfoResponse {
	private final String nickname;
	private final String image;
	private final Race race;
	private final long balance;
	private final int health;
	private final int honor;
	private final Title title;
	private final String family;

	public PlayerInfoResponse(Player player) {
		nickname = player.getName();
		image = player.getImage();
		race = player.getRace();
		balance = player.getBalance();
		health = player.getHealth();
		honor = player.getHonor();
		// todo capo, father and male control
		title = Title.find(player.getExperience(), false, false, false);
		family = player.getFamily().map(Family::getName).orElse(null);
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

	public long getBalance() {
		return balance;
	}

	public int getHealth() {
		return health;
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
}
