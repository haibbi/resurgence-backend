package tr.com.milia.resurgence.player;

class PlayerInfoResponse {
	private final String nickname;
	private final String image;
	private final Race race;
	private final int balance;
	private final int health;
	private final int honor;

	public PlayerInfoResponse(Player player) {
		nickname = player.getName();
		image = player.getImage();
		race = player.getRace();
		balance = player.getBalance();
		health = player.getHealth();
		honor = player.getHonor();
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

	public int getBalance() {
		return balance;
	}

	public int getHealth() {
		return health;
	}

	public int getHonor() {
		return honor;
	}
}
