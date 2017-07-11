package util;

import java.io.Serializable;

/**
 * The Ownable interface represents functional object assigned to owner.
 * @author H. Tanuma / SOARS project
 */
public interface Ownable extends Serializable {

	/**
	 * Set owner of the object.
	 * @param owner owner of the object
	 */
	public void setOwner(Object owner);
}
