package tr.com.milia.resurgence.task.multiplayer;

import tr.com.milia.resurgence.i18n.LocalizedException;

public class PositionLimitExceededException extends LocalizedException {
	public PositionLimitExceededException() {
		super("position.limit");
	}
}
