package tr.com.milia.resurgence.bank;

import java.time.Instant;


class BankTransferLogResponse {

	private final String from;
	private final String to;
	private final long amount;
	private final String description;
	private final Instant time;
	private Direction direction;


	private BankTransferLogResponse(BankTransferLog log, Direction direction) {
		from = log.getFrom().getName();
		to = log.getTo().getName();
		amount = log.getAmount();
		description = log.getDescription();
		time = log.getTime();
		this.direction = direction;
	}

	public static BankTransferLogResponse in(BankTransferLog log) {
		return new BankTransferLogResponse(log, Direction.IN);
	}

	public static BankTransferLogResponse out(BankTransferLog log) {
		return new BankTransferLogResponse(log, Direction.OUT);
	}

	public String getFrom() {
		return from;
	}

	public String getTo() {
		return to;
	}

	public long getAmount() {
		return amount;
	}

	public String getDescription() {
		return description;
	}

	public Instant getTime() {
		return time;
	}

	public Direction getDirection() {
		return direction;
	}

	public enum Direction {
		IN, OUT
	}
}
