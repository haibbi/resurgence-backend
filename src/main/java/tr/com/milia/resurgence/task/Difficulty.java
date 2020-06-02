package tr.com.milia.resurgence.task;

import tr.com.milia.resurgence.i18n.LocaleEnum;

public enum Difficulty implements LocaleEnum {
	EASY(0, 100),
	MEDIUM(101, 200),
	HARD(200, Integer.MAX_VALUE);

	private final int min;
	private final int max;

	Difficulty(int min, int max) {
		if (min < 0 || min >= max) throw new IllegalStateException();
		this.min = min;
		this.max = max;
	}

	static Difficulty valueOf(int difficulty) {
		for (Difficulty value : values()) {
			if (difficulty >= value.min && difficulty <= value.max) return value;
		}
		throw new IllegalArgumentException("Difficulty not found. " + difficulty);
	}
}
