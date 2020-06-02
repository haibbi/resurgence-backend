package tr.com.milia.resurgence.bank;

import tr.com.milia.resurgence.player.Player;

public abstract class BankAccountAmountChangedEvent {
	private final Player owner;
	private final long amount;

	public BankAccountAmountChangedEvent(Player owner, long amount) {
		this.owner = owner;
		this.amount = amount;
	}

	public Player getOwner() {
		return owner;
	}

	public long getAmount() {
		return amount;
	}
}
