package util;

import env.Environment;

/**
 * The IntRandom class represents random variable.
 * @author H. Tanuma / SOARS project
 */
public class IntRandom extends IntValue {

	private static final long serialVersionUID = -165695056334040162L;

	/**
	 * Get uniform random integer value less than last assigned value.
	 * @return uniform random integer value
	 */
	public int intValue() {
		return Environment.getCurrent().getRandom().nextInt(value);
	}
}
