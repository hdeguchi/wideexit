package role;

import java.io.Serializable;

import env.Environment;

import script.Condition;

/**
 * The Rule class represents rule in SOARS.
 * @author H. Tanuma / SOARS project
 */
public class Rule implements Serializable {

	private static final long serialVersionUID = 7905051101480578575L;
	Condition<Role> condition;
	String debugInfo;

	/**
	 * Constructor for Rule class.
	 * @param condition compiled condition
	 * @param debugInfo debug information string
	 */
	public Rule(Condition<Role> condition, String debugInfo) {
		this.condition = condition;
		this.debugInfo = debugInfo;
	}
	/**
	 * Execute rule action.
	 * @param role role instance related to rule
	 * @return result of rule action
	 */
	public boolean apply(Role role) {
		try {
			return condition.is(role);
		}
		catch (Exception e) {
			String info = "Time=";
			info += Environment.getCurrent().getStepCounter();
			info += ", Name=";
			info += role.getSelf();
			info += ", Spot=";
			info += role.getSpot();
			info += ", ";
			throw new RuntimeException(info + debugInfo, e);
		}
	}
	/**
	 * Output debug information string
	 * @return debug information string
	 */
	public String toString() {
		return debugInfo;
	}
}
