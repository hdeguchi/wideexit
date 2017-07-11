package util;

import env.Environment;

/**
 * The DoubleRandom class represents random variable in double precision.
 * @author H. Tanuma / SOARS project
 */
public class DoubleRandom extends DoubleValue {

	private static final long serialVersionUID = -9182787553377699352L;

	/**
	 * Default condtructor for DoubleRandom class.
	 */
	public DoubleRandom() {
		value = 1;
	}
	/**
	 * Get uniform random value between 0 and last assigned value.
	 * @return uniform random value
	 */
	public double doubleValue() {
		return value * Environment.getCurrent().getRandom().nextDouble();
	}
}
