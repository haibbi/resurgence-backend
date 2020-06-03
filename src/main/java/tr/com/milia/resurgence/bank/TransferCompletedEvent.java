package tr.com.milia.resurgence.bank;

import tr.com.milia.resurgence.player.Player;

class TransferCompletedEvent {

	private final Player from;
	private final Player to;
	private final String description;
	private final long amount;

	public TransferCompletedEvent(Player from, Player to, String description, long amount) {
		this.from = from;
		this.to = to;
		this.description = description;
		this.amount = amount;
	}

	public Player getFrom() {
		return from;
	}

	public Player getTo() {
		return to;
	}

	public String getDescription() {
		return description;
	}

	public long getAmount() {
		return amount;
	}
}
