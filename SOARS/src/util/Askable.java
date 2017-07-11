package util;

import java.io.Serializable;

/**
 * The Askable interface represents string message handler.
 * @author H. Tanuma / SOARS project
 */
public interface Askable<T> extends Serializable {

	/**
	 * Handle string message.
	 * @param caller role context of caller
	 * @param message string message
	 * @return result of condition
	 * @throws Exception
	 */
	public boolean ask(T caller, Object message) throws Exception;
}
