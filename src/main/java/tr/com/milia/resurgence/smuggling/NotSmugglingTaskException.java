package tr.com.milia.resurgence.smuggling;

import tr.com.milia.resurgence.i18n.LocalizedException;

public class NotSmugglingTaskException extends LocalizedException {
	public NotSmugglingTaskException() {
		super("not.smuggling.task");
	}
}
