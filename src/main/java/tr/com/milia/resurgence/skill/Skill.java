package tr.com.milia.resurgence.skill;

import tr.com.milia.resurgence.i18n.LocaleEnum;

public enum Skill implements LocaleEnum {

	/**
	 * Ability to use weapons
	 */
	GUN_MASTERY(90),

	/**
	 * Ability to use detective agent
	 */
	TRACING(90),

	/**
	 * Protection
	 */
	DEFENCE(90),

	/**
	 * Increases the rate of not getting caught while performing task.
	 */
	SNEAK(90);

	private final int contribution;

	Skill(int contribution) {
		this.contribution = contribution;
	}

	public double contribution() {
		return contribution;
	}

}
