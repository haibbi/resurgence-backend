package tr.com.milia.resurgence.murder;

import tr.com.milia.resurgence.i18n.LocalizedException;

public class AgentLimitExceededException extends LocalizedException {
	public AgentLimitExceededException() {
		super("agent.limit.exceeded");
	}
}
