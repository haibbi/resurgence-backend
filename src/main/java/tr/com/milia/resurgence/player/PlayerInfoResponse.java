package tr.com.milia.resurgence.player;

import java.math.BigDecimal;

class PlayerInfoResponse {
	private final String nickname;
	private final String image;
	private final BigDecimal balance;
	private final int health;
	private final int honor;

	public PlayerInfoResponse(Player player) {
		nickname = player.getName();
		image = player.getImage();
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

	public BigDecimal getBalance() {
		return balance;
	}

	public int getHealth() {
		return health;
	}

	public int getHonor() {
		return honor;
	}
}
