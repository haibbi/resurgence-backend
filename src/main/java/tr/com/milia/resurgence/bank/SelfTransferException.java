package tr.com.milia.resurgence.bank;

import tr.com.milia.resurgence.i18n.LocalizedException;

public class SelfTransferException extends LocalizedException {
	public SelfTransferException() {
		super("self.transfer");
	}
}
