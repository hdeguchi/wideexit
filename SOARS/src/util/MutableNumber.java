package util;

/**
 * The MutableNumber class represents mutable number object.
 * @author H. Tanuma / SOARS project
 */
public abstract class MutableNumber extends Number {

	private static final long serialVersionUID = -2398901363961658632L;
	/**
	 * Set number value.
	 * @param value number value
	 */
	public abstract void setNumber(Number value);
	/**
	 * Get number value.
	 * @return number value
	 */
	public abstract Number numberValue();
}
