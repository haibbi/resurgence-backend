package tr.com.milia.resurgence.bank;

import tr.com.milia.resurgence.player.Player;

public class BankAccountAmountIncreasedEvent extends BankAccountAmountChangedEvent {
	public BankAccountAmountIncreasedEvent(Player owner, long value) {
		super(owner, value);
	}
}
