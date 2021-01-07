package tr.com.milia.resurgence.player;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class WealthTest {

	@Test
	void allWealth() {
		Assertions.assertEquals(Wealth.POOR, Wealth.findByBalance(0));
		Assertions.assertEquals(Wealth.POOR, Wealth.findByBalance(1));
		Assertions.assertEquals(Wealth.POOR, Wealth.findByBalance(249_999));
		Assertions.assertEquals(Wealth.MEDIUM, Wealth.findByBalance(250_000));
		Assertions.assertEquals(Wealth.MEDIUM, Wealth.findByBalance(250_001));
		Assertions.assertEquals(Wealth.MEDIUM, Wealth.findByBalance(499_999));
		Assertions.assertEquals(Wealth.RICH, Wealth.findByBalance(500_000));
		Assertions.assertEquals(Wealth.RICH, Wealth.findByBalance(999_999));
		Assertions.assertEquals(Wealth.VERY_RICH, Wealth.findByBalance(1_000_000));
		Assertions.assertEquals(Wealth.VERY_RICH, Wealth.findByBalance(1_000_001));
	}
}
