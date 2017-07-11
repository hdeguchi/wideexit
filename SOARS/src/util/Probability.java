package util;

import role.Role;
import env.Environment;

/**
 * The Probability class represents probalility value.
 * @author H. Tanuma / SOARS project
 */
public class Probability extends MutableNumber implements Cloneable, Askable<Role> {

	private static final long serialVersionUID = -7236199753609230394L;
	protected float probability = 0;
	protected String cache = "";

	public Object clone() throws CloneNotSupportedException {
		return super.clone();
	}
	public boolean ask(Role caller, Object message) throws Exception {
		String arg = message.toString();
		if (!arg.equals("") && !arg.equals(cache)) {
			cache = arg;
			String[] pair = arg.split("/", 2);
			probability = Float.parseFloat(pair[0].trim());
			if (pair.length == 2) {
				probability /= Float.parseFloat(pair[1].trim());
			}
		}
		return Environment.getCurrent().getRandom().nextFloat() < probability;
	}
	public float getProbability() {
		return probability;
	}
	public void setProbablilty(float probability) {
		this.probability = probability;
		cache = "";
	}
	public void setNumber(Number value) {
		setProbablilty(value.floatValue());
	}
	public Number numberValue() {
		return new Float(floatValue());
	}
	public double doubleValue() {
		return probability;
	}
	public float floatValue() {
		return probability;
	}
	public int intValue() {
		return (int) probability;
	}
	public long longValue() {
		return (long) probability;
	}
	public String toString() {
		return cache.equals("") ? Float.toString(probability) : cache;
	}
}
