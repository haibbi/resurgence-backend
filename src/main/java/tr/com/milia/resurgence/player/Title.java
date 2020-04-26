package tr.com.milia.resurgence.player;

import tr.com.milia.resurgence.i18n.LocaleEnum;

import java.util.Arrays;

public enum Title implements LocaleEnum {
	EMPTY_SUIT(10_000),
	DELIVERY_BOY(100_000),
	DELIVERY_GIRL(100_000),
	PICCIOTTO(250_000),
	SHOPLIFTER(500_000),
	PICKPOCKET(1_000_000),
	THIEF(2_000_000),
	ASSOCIATE(4_000_000),
	MOBSTER(6_000_000),
	SOLDIER(10_000_000),
	SWINDLER(15_000_000),
	ASSASSIN(25_000_000),
	LOCAL_CHIEF(50_000_000),
	CHIEF(100_000_000),
	BRUGLIONE(250_000_000),
	CAPODECINA(250_000_000),
	GODFATHER(250_000_000),
	FIRST_LADY(250_000_000);

	private final int exp;

	Title(int exp) {
		this.exp = exp;
	}

	public static Title find(int exp, boolean isCapo, boolean isFather, boolean isMale) {
		return Arrays.stream(Title.values())
			.filter(title -> {
				if (isMale) return title != DELIVERY_GIRL && title != FIRST_LADY;
				else return title != DELIVERY_BOY && title != GODFATHER;
			})
			.filter(title -> {
				if (isCapo) return title != BRUGLIONE;
				return true;
			})
			.filter(title -> {
				if (isFather) return title != CAPODECINA && title != BRUGLIONE;
				return true;
			})
			.filter(title -> exp <= title.exp)
			.findFirst()
			.orElseThrow(RankNotFound::new);
	}

	private static class RankNotFound extends RuntimeException {
	}
}
