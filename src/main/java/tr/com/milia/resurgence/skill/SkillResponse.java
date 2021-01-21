package tr.com.milia.resurgence.skill;

import tr.com.milia.resurgence.i18n.LocaleEnum;

public class SkillResponse implements LocaleEnum {

	private final Skill skill;
	private final String description;
	private final double expertise;

	public SkillResponse(Skill skill, String description, double expertise) {
		this.skill = skill;
		this.description = description;
		this.expertise = expertise;
	}

	public String getDescription() {
		return description;
	}

	public double getExpertise() {
		return expertise;
	}

	@Override
	public String[] getCodes() {
		return this.skill.getCodes();
	}

	@Override
	public Object[] getArguments() {
		return this.skill.getArguments();
	}

	@Override
	public String getDefaultMessage() {
		return this.skill.getDefaultMessage();
	}

	@Override
	public String name() {
		return this.skill.name();
	}
}
