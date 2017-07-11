package role;

import script.Command;
import script.Condition;
import util.Askable;
import env.Agent;
import env.Context;
import env.EquippedObject;
import env.Spot;

/**
 * The Role abstact class represents role in SOARS.
 * @author H. Tanuma / SOARS project
 */
public abstract class Role extends RoleFacade implements Askable<Role>, Context {

	private static final long serialVersionUID = -7184313676053073774L;
	RoleType roleType = null;
	Agent agent = null;
	Spot spot = null;

	/**
	 * Get role type of the role.
	 * @return role type
	 */
	public RoleType getRoleType() {
		return roleType;
	}
	/**
	 * Get agent self of the role.
	 * @return agent
	 */
	public Agent getAgent() {
		return agent;
	}
	/**
	 * Get present spot of the role.
	 * @return spot
	 */
	public Spot getSpot() {
		return agent != null ? agent.getSpot() : spot;
	}
	/**
	 * Get self of the role.
	 * @return self
	 */
	public EquippedObject getSelf() {
		return agent != null ? (EquippedObject) agent : spot;
	}
	/**
	 * Check if in the debug mode.
	 * @return true if in the debug mode
	 */
	protected boolean isDebug() {
		return false;
	}
	/**
	 * Compile command script in context of the role.
	 * @param command command script
	 * @return command object
	 * @throws Exception
	 */
	public Command<Role> parseCommand(String command) throws Exception {
		return new RuleCommandParser<Role>(getClass()).parseCommand(command);
	}
	/**
	 * Compile condition script in context of the role.
	 * @param condition condition script
	 * @return condition object
	 * @throws Exception
	 */
	public Condition<Role> parseCondition(String condition) throws Exception {
		return new RuleCommandParser<Role>(getClass()).parseCondition(condition);
	}
	/**
	 * Evaluate condition script in context of the role.
	 * @param caller ignored
	 * @param message condition script
	 * @return result of condition script
	 * @throws Exception
	 */
	public boolean ask(Role caller, Object message) throws Exception {
		return parseCondition(message.toString()).is(this);
	}
	/**
	 * Get role type name.
	 * @return role type name
	 */
	public String toString() {
		return roleType.getName();
	}
}
