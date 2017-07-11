package env;

import java.io.Serializable;
import java.util.List;

import role.Role;
import role.RoleType;

/**
 * The Agent class represents agent in SOARS.
 * @author H. Tanuma / SOARS project
 */
public class Agent extends EquippedObject {

	private static final long serialVersionUID = 5232107168547676822L;
	Role generalRole = RoleType.forName(RoleType.GENERAL_NAME).createRole(this);
	Role activeRole = null;
	Spot[] spot = new Spot[1];

	/**
	 * Key string to get active role property.
	 */
	public static final String ROLE_KEY = "$Role";
	/**
	 * Key string to get present spot property.
	 */
	public static final String SPOT_KEY = "$Spot";

	/**
	 * Get list of agents.
	 * @return list of agents
	 */
	public static List<Agent> getList() {
		return Environment.getCurrent().getAgents().getList();
	}
	/**
	 * Get agent by name.
	 * @param name name string of an agent
	 * @return agent with specified name or null
	 */
	public static Agent forName(String name) {
		return Environment.getCurrent().getAgents().forName(name);
	}
	/**
	 * Get agent by ID number.
	 * @param idNumber ID number in integer
	 * @return agent with specified ID or null
	 */
	public static Agent forId(int idNumber) {
		return Environment.getCurrent().getAgents().forId(idNumber);
	}
	/**
	 * Default constructor for the Agent class.
	 * @throws InstantiationException
	 * @throws IllegalAccessException
	 */
	public Agent() throws InstantiationException, IllegalAccessException {
		super(Environment.getCurrent().getAgents());
		equip.put(SPOT_KEY, new Serializable() {
			private static final long serialVersionUID = -5898138700269502115L;
			public String toString() {
				return spot[0] != null ? spot[0].getName() : null;
			}
		});
	}
	/**
	 * Get general role of the agent.
	 * @return general role
	 */
	public Role getGeneralRole() {
		return generalRole;
	}
	/**
	 * Activate specified role for the agent.
	 * @param role role to activate
	 */
	public void setActiveRole(Role role) {
		if (role == null || role.getRoleType() == generalRole.getRoleType()) {
			activeRole = null;
			setProp(ROLE_KEY, null);
			// General Role Type never be activated
			// because applying General Rules twice is meaningless.
		}
		else {
			activeRole = role;
			setProp(ROLE_KEY, role.getRoleType().getName());
		}
	}
	/**
	 * Get active role of the agent.
	 * @return active role of the agent
	 */
	public Role getActiveRole() {
		return activeRole;
	}
	/**
	 * Set present spot of the agent.
	 * @param spot present spot
	 */
	public void setSpot(Spot spot) {
		this.spot[0] = spot;
	}
	/**
	 * Get present spot of the agent.
	 * @return present spot
	 */
	public Spot getSpot() {
		return spot[0];
	}
	/**
	 * Attach to a specified agent.
	 * @param agent agent to attach
	 */
	public void attachTo(Agent agent) {
		spot = agent.spot;
	}
	/**
	 * Detach from other agent.
	 */
	public void detach() {
		spot = new Spot[]{spot[0]};
	}
}
