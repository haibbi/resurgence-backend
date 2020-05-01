package tr.com.milia.resurgence.family;

import tr.com.milia.resurgence.i18n.LocalizedException;

public class SelfAssignmentException extends LocalizedException {
	public SelfAssignmentException() {
		super("self.assignment");
	}
}
