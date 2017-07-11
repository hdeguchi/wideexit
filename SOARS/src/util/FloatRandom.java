package util;

import env.Environment;

/**
 * The FloatRandom class represents random variable.
 * @author H. Tanuma / SOARS project
 */
public class FloatRandom extends FloatValue {

	private static final long serialVersionUID = -119262116537601818L;

	/**
	 * Default condtructor for FloatRandom class.
	 */
	public FloatRandom() {
		value = 1;
	}
	/**
	 * Get uniform random value between 0 and last assigned value.
	 * @return uniform random value
	 */
	public float floatValue() {
		return value * Environment.getCurrent().getRandom().nextFloat();
	}
}
