package tr.com.milia.resurgence;

import java.security.SecureRandom;
import java.util.Random;

public class RandomUtils {

	private static final Random RANDOM = new SecureRandom();
	private static final Character[] ALPHA_NUMERIC = new Character[]{
		'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z',
		'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z',
		'0', '1', '2', '3', '4', '5', '6', '7', '8', '9'
	};

	private RandomUtils() {
		throw new IllegalStateException();
	}

	public static double random(double leftLimit, double rightLimit) {
		return leftLimit + (RANDOM.nextDouble() * (rightLimit - leftLimit));
	}

	/**
	 * @return a random number from 0 to 1
	 */
	public static double random() {
		return random(0, 1);
	}

	public static int random(int bound) {
		return RANDOM.nextInt(bound);
	}

	public static String randomAlphaNumeric(int count) {
		if (count < 0) throw new IllegalArgumentException();
		if (count == 0) return "";
		StringBuilder builder = new StringBuilder(count);
		while (count-- != 0) {
			int index = RANDOM.nextInt(ALPHA_NUMERIC.length);
			builder.append(ALPHA_NUMERIC[index]);
		}
		return builder.toString();
	}

}
