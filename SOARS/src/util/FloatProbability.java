package util;

import role.Role;
import env.Environment;

/**
 * The FloatProbability class represents probability variable.
 * @author H. Tanuma / SOARS project
 */
public class FloatProbability extends FloatValue {

	private static final long serialVersionUID = 8467998448719202044L;

	/**
	 * Evaluate probability formula.
	 * @param caller role context of caller
	 * @param message probability formula
	 * @return true in probability of the value
	 * @throws Exception
	 */
	public boolean ask(Role caller, Object message) throws Exception {
		if (!message.equals("")) {
			super.ask(caller, message);
		}
		return Environment.getCurrent().getRandom().nextFloat() < floatValue();
	}
}
