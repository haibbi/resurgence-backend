package tr.com.milia.resurgence.player;

import tr.com.milia.resurgence.i18n.LocaleEnum;

import java.util.Arrays;
import java.util.Comparator;

public enum Wealth implements LocaleEnum {

	POOR(0),
	MEDIUM(250_000),
	RICH(500_000),
	VERY_RICH(1_000_000)
	;

	private final long balance;

	Wealth(long balance) {
		this.balance = balance;
	}

	static Wealth findByBalance(long balance) {
		return Arrays.stream(values())
			.filter(wealth -> balance >= wealth.balance)
			.max(Comparator.comparingLong(o -> o.balance))
			.orElseThrow();
	}

}
