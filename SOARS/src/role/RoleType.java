package role;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import env.Agent;
import env.Environment;
import env.NamedObject;
import env.Spot;
import env.StepCounter;

/**
 * The RoleType class represents role type in SOARS.
 * @author H. Tanuma / SOARS project
 */
public class RoleType extends NamedObject {

	private static final long serialVersionUID = -3884826013462161353L;
	Class<?> roleClass;
	String[] roleNames = null;
	ArrayList<Rule>[] roleRules = null;

	/**
	 * Default general role type class.
	 */
	public static final Class<?> DEFAULT_CLASS = RoleStructure.class;
	/**
	 * Default general role type name.
	 */
	public static final String GENERAL_NAME = "";
	/**
	 * Default spot role type class.
	 */
	public static final Class<?> SPOT_CLASS = RoleForSpots.class;
	/**
	 * Default spot role type name.
	 */
	public static final String SPOT_NAME = "<>";

	/**
	 * Get list of all role types.
	 * @return list of role types
	 */
	public static List<RoleType> getList() {
		return Environment.getCurrent().getRoleTypes().getList();
	}
	/**
	 * Get role type by name.
	 * @param name role type name
	 * @return role type
	 */
	public static RoleType forName(String name) {
		name = name.split(":", 2)[0];
		RoleType roleType = Environment.getCurrent().getRoleTypes().forName(name);
		if (roleType == null) {
			if (name.equals(GENERAL_NAME)) {
				roleType = new RoleType(DEFAULT_CLASS);
				roleType.setName(GENERAL_NAME);
				Environment.getConsole().println("Info: Default General Role Type automatically defined.");
			}
			else if (name.equals(SPOT_NAME)) {
				roleType = new RoleType(SPOT_CLASS);
				roleType.setName(SPOT_NAME);
				Environment.getConsole().println("Info: Default Spot Role Type '<>' automatically defined.");
			}
		}
		return roleType;
	}
	/**
	 * Get role type by ID.
	 * @param idNumber ID number of role type
	 * @return role type
	 */
	public static RoleType forId(int idNumber) {
		return Environment.getCurrent().getRoleTypes().forId(idNumber);
	}
	/**
	 * Constructor for RoleType class.
	 * @param roleClass role class which implements rules
	 */
	public RoleType(Class<?> roleClass) {
		super(Environment.getCurrent().getRoleTypes());
		this.roleClass = roleClass;
	}
	/**
	 * Get role class which implements rules.
	 * @return role class
	 */
	public Class<?> getRoleClass() {
		return roleClass;
	}
	/**
	 * Set role type name.
	 * @param name role type name
	 */
	public void setName(String name) {
		roleNames = name.split(":", -1);
		super.setName(roleNames[0]);
	}
	/**
	 * Get role instance related to agent.
	 * @param agent agent related to role type
	 * @return role instance
	 * @throws InstantiationException
	 * @throws IllegalAccessException
	 */
	public Role createRole(Agent agent) throws InstantiationException, IllegalAccessException {
		Role role = (Role) roleClass.newInstance();
		role.roleType = this;
		role.agent = agent;
		return role;
	}
	/**
	 * Get role instance related to spot.
	 * @param spot spot related to role type
	 * @return role instance
	 * @throws InstantiationException
	 * @throws IllegalAccessException
	 */
	public Role createRole(Spot spot) throws InstantiationException, IllegalAccessException {
		Role role = (Role) roleClass.newInstance();
		role.roleType = this;
		role.spot = spot;
		return role;
	}
	/**
	 * Register new rule related to a stage.
	 * @param rule new rule to register
	 * @param stage stage index
	 */
	@SuppressWarnings("unchecked")
	public void addRule(Rule rule, int stage) {
		if (roleRules == null) {
			roleRules = new ArrayList[Environment.getCurrent().getStepCounter().getStageNames().size()];
		}
		if (roleRules[stage] == null) {
			roleRules[stage] = new ArrayList<Rule>();
		}
		roleRules[stage].add(rule);
	}
	@SuppressWarnings("unchecked")
	ArrayList<Rule> getRules(int stage) {
		if (roleRules == null) {
			roleRules = new ArrayList[Environment.getCurrent().getStepCounter().getStageNames().size()];
		}
		ArrayList<Rule> rules = roleRules[stage];
		if (rules == null) {
			rules = new ArrayList<Rule>();
			for (int i = 1; i < roleNames.length; ++i) {
				RoleType type = forName(roleNames[i]);
				if (type == null) {
					for (int j = 1; j <= i; ++j) {
						roleNames[0] += ':' + roleNames[j];
					}
					throw new RuntimeException("Wrong Role Type - " + roleNames[0]);
				}
				rules = type.getRules(stage);
				if (!rules.isEmpty()) {
					break;
				}
			}
			roleRules[stage] = rules;
		}
		return rules;
	}
	/**
	 * Execute rule action related to role instance.
	 * @param role role instance
	 * @param stage stage index
	 */
	public void applyRules(Role role, int stage) {
		ArrayList<Rule> rules = getRules(stage);
		for (int i = 0; i < rules.size(); ++i) {
			Rule rule = (Rule) rules.get(i);
			if (rule.apply(role)) {
				if (role.isDebug()) {
					PrintWriter out = Environment.getConsole();
					out.print("Time=");
					out.print(Environment.getCurrent().getStepCounter());
					out.print(", Name=");
					out.print(role.getSelf());
					out.print(", Spot=");
					out.print(role.getSpot());
					out.print(", ");
					out.println(rule);
				}
				return;
			}
		}
		if (role.isDebug()) {
			PrintWriter out = Environment.getConsole();
			out.print("Time=");
			out.print(Environment.getCurrent().getStepCounter());
			out.print(", Name=");
			out.print(role.getSelf());
			out.print(", Spot=");
			out.print(role.getSpot());
			out.print(", Role=");
			out.print(getName());
			out.print(", Stage=");
			StepCounter stepCounter = Environment.getCurrent().getStepCounter();
			out.print(stepCounter.getStageNames().get(stepCounter.getStage()));
			out.println(", No Rule Applied.");
		}
	}
}
