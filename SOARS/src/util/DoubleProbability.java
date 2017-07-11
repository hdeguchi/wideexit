package util;

import role.Role;
import env.Environment;

/**
 * The DoubleProbability class represents probability variable in double precision.
 * @author H. Tanuma / SOARS project
 */
public class DoubleProbability extends DoubleValue {

	private static final long serialVersionUID = -4316331324448353691L;

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
		return Environment.getCurrent().getRandom().nextDouble() < doubleValue();
	}
}
