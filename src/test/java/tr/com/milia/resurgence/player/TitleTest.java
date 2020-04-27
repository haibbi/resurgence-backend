package tr.com.milia.resurgence.player;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class TitleTest {

	@Test
	void emptySuit() {
		Title title = Title.find(0, false, false, true);
		assertEquals(Title.EMPTY_SUIT, title);
	}

	@Test
	void deliveryBoy() {
		Title title = Title.find(100_000, false, false, true);
		assertEquals(Title.DELIVERY_BOY, title);
	}

	@Test
	void deliveryGirl() {
		Title title = Title.find(100_000, false, false, false);
		assertEquals(Title.DELIVERY_GIRL, title);
	}

	@Test
	void assassin() {
		Title title = Title.find(25_000_000, false, false, true);
		assertEquals(Title.ASSASSIN, title);
	}

	@Test
	void bruglione() {
		Title title = Title.find(250_000_000, false, false, true);
		assertEquals(Title.BRUGLIONE, title);
	}

	@Test
	void capodecina() {
		Title title = Title.find(250_000_000, true, false, true);
		assertEquals(Title.CAPODECINA, title);
	}

	@Test
	void godfather() {
		Title title = Title.find(250_000_000, true, true, true);
		assertEquals(Title.GODFATHER, title);
	}

	@Test
	void godfather2() {
		Title title = Title.find(250_000_000, false, true, true);
		assertEquals(Title.GODFATHER, title);
	}

	@Test
	void firstLady() {
		Title title = Title.find(250_000_000, true, true, false);
		assertEquals(Title.FIRST_LADY, title);
	}

	@Test
	void firstLady2() {
		Title title = Title.find(250_000_000, false, true, false);
		assertEquals(Title.FIRST_LADY, title);
	}

}
