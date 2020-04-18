package tr.com.milia.resurgence;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.stream.IntStream;

class RandomUtilsTest {

	@Test
	public void randomInt2() {
		int random = RandomUtils.random(1);
		Assertions.assertEquals(0, random);
		Assertions.assertNotEquals(1, random);
	}

	@Test
	public void randomInt3() {
		int random = RandomUtils.random(100);
		Assertions.assertNotEquals(100, random);
		Assertions.assertTrue(random < 100);
		Assertions.assertTrue(random >= 0);
	}

	@Test
	public void forLoop() {
		for (int i = 0; i < 1_000_000; i++) {
			randomInt2();
			randomInt3();
		}
	}

	@Test
	public void randomString1() {
		String random = RandomUtils.randomAlphaNumeric(0);
		Assertions.assertEquals("", random);
	}

	@Test
	public void randomString2() {
		String random = RandomUtils.randomAlphaNumeric(10);
		Assertions.assertFalse(random.isBlank());
		Assertions.assertEquals(10, random.length());
	}

	@Test
	public void doubleRandom() {
		double random = RandomUtils.random(0d, 1d);
		Assertions.assertTrue(random <= 1 && random >= 0);
	}

	@Test
	public void doubleRandomMarathonTest(){
		IntStream.range(0, 1_000_000).forEach(__ -> doubleRandom());
	}

}
