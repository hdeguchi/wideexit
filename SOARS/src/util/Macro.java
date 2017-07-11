package util;

import role.Role;
import script.Condition;

/**
 * The Macro class represents condition macro.
 * @author H. Tanuma / SOARS project
 */
public class Macro implements Cloneable, Askable<Role> {

	private static final long serialVersionUID = 9096479192426811465L;
	protected Condition<Role> condition = null;
	protected String cache = "";

	/**
	 * Create clone of the condition macro.
	 * @return new condition macro instance
	 * @throws CloneNotSupportedException
	 */
	public Object clone() throws CloneNotSupportedException {
		return super.clone();
	}
	/**
	 * Evaluate condition macro.
	 * @param caller role context of caller
	 * @param message string message
	 * @return result of evaluation
	 * @throws Exception
	 */
	public boolean ask(Role caller, Object message) throws Exception {
		String macro = message.toString();
		if (macro.equals("")) {
			if (condition == null) {
				condition = caller.parseCondition(cache);
			}
			return condition.is(caller);
		}
		if (!macro.equals(cache)) {
			cache = macro;
			condition = null;
		}
		return true;
	}
}
