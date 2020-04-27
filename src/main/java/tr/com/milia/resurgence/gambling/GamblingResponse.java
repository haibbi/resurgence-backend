package tr.com.milia.resurgence.gambling;

public class GamblingResponse {

	private final boolean win;

	public GamblingResponse(boolean win) {
		this.win = win;
	}

	public boolean isWin() {
		return win;
	}
}
