package tr.com.milia.resurgence.bank;

import tr.com.milia.resurgence.player.Player;

public class BankAccountAmountDecreasedEvent extends BankAccountAmountChangedEvent {
	public BankAccountAmountDecreasedEvent(Player owner, long value) {
		super(owner, value);
	}
}
