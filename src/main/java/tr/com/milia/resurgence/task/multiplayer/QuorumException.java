package tr.com.milia.resurgence.task.multiplayer;

import tr.com.milia.resurgence.i18n.LocalizedException;

public class QuorumException extends LocalizedException {
	public QuorumException() {
		super("quorum.failed");
	}
}
