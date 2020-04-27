package tr.com.milia.resurgence.gambling;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tr.com.milia.resurgence.RandomUtils;
import tr.com.milia.resurgence.player.Player;
import tr.com.milia.resurgence.player.PlayerService;
import tr.com.milia.resurgence.smuggling.NotEnoughMoneyException;
import tr.com.milia.resurgence.task.PlayerNotFound;

@Service
public class NumberGame {

	private final PlayerService playerService;

	public NumberGame(PlayerService playerService) {
		this.playerService = playerService;
	}

	@Transactional
	public boolean play(String playerName, int number, int bet) {
		if (number < 1 || number > 12) return false;
		return play(playerName, bet, number(number), 5);
	}

	@Transactional
	public boolean play(String playerName, boolean even, int bet) {
		return play(playerName, bet, even == even(), 1);
	}

	private boolean play(String playerName, int bet, boolean win, int ratio) {
		if (bet < 0) return true;

		Player player = playerService.findByName(playerName).orElseThrow(PlayerNotFound::new);
		if (player.getBalance() < bet) throw new NotEnoughMoneyException();

		if (win) player.increaseBalance(bet * ratio);
		else player.decreaseBalance(bet);

		return win;
	}

	private boolean number(int number) {
		return random() == number;
	}

	private boolean even() {
		return random() % 2 == 0;
	}

	/**
	 * @return [1 - 12]
	 */
	private int random() {
		return RandomUtils.random(12) + 1;
	}

}
