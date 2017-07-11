package util;

import role.Role;
import script.Command;
import script.Condition;

/**
 * The AskCommand class represents string message handler.
 * @author H. Tanuma / SOARS project
 */
public abstract class AskCommand<T> implements Askable<Role> {

	private static final long serialVersionUID = 3152468473330973910L;
	/**
	 * Parse command formula for target object.
	 * @param target target object implements command methods
	 * @param caller role context of caller
	 * @param command command formula
	 * @return compiled command
	 * @throws Exception
	 */
	public static <T> Command<T> parseCommand(T target, Role caller, String command) throws Exception {
		return new AskCommandParser<T>(target, caller, Role.class).parseCommand(command);
	}
	/**
	 * Parse condition formula for target object.
	 * @param target target object implements condition methods
	 * @param caller role context of caller
	 * @param condition condition formula
	 * @return compiled condition
	 * @throws Exception
	 */
	public static <T> Condition<T> parseCondition(T target, Role caller, String condition) throws Exception {
		return new AskCommandParser<T>(target, caller, Role.class).parseCondition(condition);
	}
	/**
	 * Evaluate condition formula for target object.
	 * @param target target object implements condition methods
	 * @param caller role context of caller
	 * @param message condition formula
	 * @return result of condition formula
	 * @throws Exception
	 */
	public static <T> boolean ask(T target, Role caller, Object message) throws Exception {
		return parseCondition(target, caller, message.toString()).is(target);
	}
	/**
	 * Evaluate condition formula for self as target.
	 * @param caller role context of caller
	 * @param message condition formula
	 * @return result of condition formula
	 * @throws Exception
	 */
	public boolean ask(Role caller, Object message) throws Exception {
		return ask(this, caller, message);
	}
}
