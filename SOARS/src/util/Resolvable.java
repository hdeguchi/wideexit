package util;

import java.io.Serializable;

/**
 * The Resolvable interface represents resolver.
 * @author H. Tanuma / SOARS project
 */
public interface Resolvable extends Serializable {

	public void resolve() throws Exception;
	public void requestResolve();
}
