package env;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashSet;
import java.util.Iterator;

import role.Role;
import role.RoleType;
import role.Rule;
import role.RuleCommandParser;
import script.Command;
import script.CommandParser;
import script.Condition;
import script.ScriptParser;
import time.Time;
import time.TimeCounter;

/**
 * The ObjectLoader class implements declaration script of SOARS.
 * @author H. Tanuma / SOARS project
 */
public class ObjectLoader extends ScriptParser {

	/**
	 * Paragraph method to register class paths.
	 * @param paragraph class paths
	 * @throws MalformedURLException
	 */
	public static void classURL(Iterator<?> paragraph) throws MalformedURLException {
		while (paragraph.hasNext()) {
			Environment.getCurrent().addURL(new URL(paragraph.next().toString().split("\t")[0]));
		}
	}
	/**
	 * Paragraph method to define role types.
	 * @param paragraph role types
	 * @throws ClassNotFoundException
	 */
	public static void role(Iterator<?> paragraph) throws ClassNotFoundException {
		while (paragraph.hasNext()) {
			String[] pair = paragraph.next().toString().split("\t");
			Class<?> roleClass = RoleType.DEFAULT_CLASS;
			if (pair.length > 1 && !(pair[1] = pair[1].trim()).equals("")) {
				roleClass = Environment.getCurrent().classForName(pair[1]);
			}
			new RoleType(roleClass).setName(pair[0].trim());
		}
	}
	/**
	 * Paragraph method to define spots.
	 * @param paragraph spot definitions
	 * @throws Exception
	 */
	public void spot(Iterator<?> paragraph) throws Exception {
		RuleCommandParser<Role> parser = new RuleCommandParser<Role>(RoleType.forName(RoleType.SPOT_NAME).getRoleClass());
		while (paragraph.hasNext()) {
			String[] split = paragraph.next().toString().split("\t");
			split[0] = split[0].trim();
			Spot spot;
			if (split[0].startsWith(">")) {
				spot = Spot.forName(split[0] = split[0].substring(1).trim());
				if (spot == null) {
					throw new RuntimeException("Wrong Spot Name - " + split[0]);
				}
			}
			else {
				spot = new Spot();
				spot.setName(split[0]);
			}
			Role role = RoleType.forName(RoleType.SPOT_NAME).createRole(spot);
			for (int i = 1; i < split.length; ++i) {
				split[i] = split[i].trim();
				if (split[i].equals("")) continue;
				parser.parseCommand(split[i]).invoke(role);
			}
		}
	}
	/**
	 * Paragraph method to define main stages.
	 * @param paragraph main stages
	 */
	public static void stage(Iterator<?> paragraph) {
		while (paragraph.hasNext()) {
			Environment.getCurrent().getStepCounter().addStageName(paragraph.next().toString().trim());
		}
	}
	/**
	 * Paragraph method to define initial stages.
	 * @param paragraph initial stages
	 */
	public static void initialStage(Iterator<?> paragraph) {
		while (paragraph.hasNext()) {
			Environment.getCurrent().getStepCounter().addInitialStageName(paragraph.next().toString().trim());
		}
	}
	/**
	 * Paragraph method to define terminal stages.
	 * @param paragraph terminal stages
	 */
	public static void terminalStage(Iterator<?> paragraph) {
		while (paragraph.hasNext()) {
			Environment.getCurrent().getStepCounter().addTerminalStageName(paragraph.next().toString().trim());
		}
	}

	protected int stage = -1;
	protected HashSet<String> ignoredStages = new HashSet<String>();
	protected RoleType roleType = null;
	protected CommandParser<Role> parser = null;
	protected Condition<Role> condition = null;
	protected Command<Role> command = null;
	protected String debugInfo = "";

	/**
	 * Item method to define stage condition.
	 * @param item stage name
	 */
	public void ruleStage(String item) {
		item = item.trim();
		ruleInfo("Stage=" + item);
		stage = Environment.getCurrent().getStepCounter().getStageNames().indexOf(item);
		if (stage < 0 && ignoredStages.add(item)) {
			Environment.getConsole().println("Warning: Ignored Stage - " + item);
		}
	}
	/**
	 * Item method to define role condition.
	 * @param item role type name
	 */
	public void ruleRole(String item) {
		item = item.trim();
		ruleInfo("Role=" + item);
		roleType = RoleType.forName(item);
		if (roleType == null) {
			throw new RuntimeException("Wrong Role Type - " + item);
		}
		parser = new RuleCommandParser<Role>(roleType.getRoleClass());
	}
	/**
	 * Item method to define rule condition.
	 * @param item condition rule script
	 * @throws Exception
	 */
	public void ruleCondition(String item) throws Exception {
		item = item.trim();
		if (item.equals("")) return;
		ruleInfo("Condition=\"" + item + '\"');
		Condition<Role> c = parser.parseCondition(item);
		if (command != null) {
			c = command.pair(c);
			command = null;
		}
		condition = (condition != null) ? condition.andand(c) : c;
	}
	/**
	 * Item method to define rule command.
	 * @param item command rule script
	 * @throws Exception
	 */
	public void ruleCommand(String item) throws Exception {
		item = item.trim();
		if (item.equals("")) return;
		ruleInfo("Command=\"" + item + '\"');
		Command<Role> c = parser.parseCommand(item);
		command = (command != null) ? command.pair(c) : c;
	}
	/**
	 * Item method to define next stage command.
	 * @param item stage name
	 */
	public void ruleNextStage(String item) {
		item = item.trim();
		if (item.equals("")) return;
		ruleInfo("NextStage=" + item);
		int next = Environment.getCurrent().getStepCounter().getStageNames().indexOf(item);
		if (next < 0) {
			throw new RuntimeException("Wrong Stage Name - " + item);
		}
		Command<Role> c = commandNextStage(next);
		command = (command != null) ? command.pair(c) : c;
	}
	private static Command<Role> commandNextStage(final int next) {
		return new Command<Role>() {
			private static final long serialVersionUID = 4753316821764394790L;
			public void invoke(Role obj) throws Exception {
				Environment.getCurrent().getStepCounter().setNextStage(next);
			}
		};
	}
	/**
	 * Item method to describe rule comment.
	 * @param item rule comment
	 */
	public void ruleInfo(String item) {
		item = item.trim();
		if (item.equals("")) return;
		if (!debugInfo.equals("")) {
			debugInfo += ", ";
		}
		debugInfo += item;
	}
	/**
	 * Item method to register rules.
	 * @param item rule comment
	 */
	public void ruleCreate(String item) {
		ruleInfo(item);
		if (stage >= 0) {
			if (command != null) {
				Condition<Role> c = command.condition();
				condition = (condition != null ? condition.andand(c) : c);  
			}
			else if (condition == null) {
				condition = Condition.TRUE();
			}
			ruleInfo(toString());
			roleType.addRule(new Rule(condition, debugInfo), stage);
			stage = -1;
		}
		roleType = null;
		parser = null;
		condition = null;
		command = null;
		debugInfo = "";
	}

	/**
	 * Default agent name prefix.
	 */
	public static final String DEFAULT_AGENT_NAME = "Agent";

	protected int numAgents = -1;
	protected String agentName = DEFAULT_AGENT_NAME;

	/**
	 * Item method to set number of agents.
	 * @param item number of agents
	 */
	public void agentNumber(String item) {
		item = item.trim();
		numAgents = item.equals("") ? -1 : Integer.parseInt(item);
	}
	/**
	 * Item method to set name prefix of agents.
	 * @param item agent name prefix
	 */
	public void agentName(String item) {
		agentName = item;
	}
	/**
	 * Item method to define initialization rule for agents.
	 * @param item initialization rule
	 * @throws Exception
	 */
	public void agentCommand(String item) throws Exception {
		item = item.trim();
		if (item.equals("")) return;
		if (parser == null) {
			parser = new RuleCommandParser<Role>(RoleType.forName(RoleType.GENERAL_NAME).getRoleClass());
		}
		ruleCommand(item);
	}
	/**
	 * Item method to create agents.
	 * @param item ignored
	 * @throws Exception
	 */
	public void agentCreate(String item) throws Exception {
		Environment current = Environment.getCurrent();
		current.getResolveRequests().addFirst(null); // Prepare Self Resolution
		for (int i = 1; i <= numAgents; ++i) {
			Agent agent = new Agent();
			agent.assignName(agentName);
			if (command != null) {
				command.invoke(agent.getGeneralRole());
				current.doResolution(); // Self Resolution
			}
		}
		if (numAgents < 0) {
			Agent agent = new Agent();
			agent.setName(agentName);
			if (command != null) {
				command.invoke(agent.getGeneralRole());
				current.doResolution(); // Self Resolution
			}
		}
		numAgents = -1;
		agentName = DEFAULT_AGENT_NAME;
		parser = null;
		command = null;
		debugInfo = "";
		current.getResolveRequests().removeFirst(); // Unprepare Self Resolution
	}
	/**
	 * Item method to re-initialize agents.
	 * @param item ignored
	 * @throws Exception
	 */
	public void agentInitialize(String item) throws Exception {
		Environment current = Environment.getCurrent();
		current.getResolveRequests().addFirst(null); // Prepare Self Resolution
		for (int i = 1; i <= numAgents; ++i) {
			Agent agent = Agent.forName(agentName + i);
			if (command != null) {
				command.invoke(agent.getGeneralRole());
				current.doResolution(); // Self Resolution
			}
		}
		if (numAgents < 0) {
			Agent agent = Agent.forName(agentName);
			if (command != null) {
				command.invoke(agent.getGeneralRole());
				current.doResolution(); // Self Resolution
			}
		}
		numAgents = -1;
		agentName = DEFAULT_AGENT_NAME;
		parser = null;
		command = null;
		debugInfo = "";
		current.getResolveRequests().removeFirst(); // Unprepare Self Resolution
	}

	/**
	 * Default spot name prefix.
	 */
	public static final String DEFAULT_SPOT_NAME = "Spot";

	protected int numSpots = -1;
	protected String spotName = DEFAULT_SPOT_NAME;

	/**
	 * Item method to set number of spots.
	 * @param item number of spots
	 */
	public void spotNumber(String item) {
		item = item.trim();
		numSpots = item.equals("") ? -1 : Integer.parseInt(item);
	}
	/**
	 * Item method to set name prefix of spots.
	 * @param item spot name prefix
	 */
	public void spotName(String item) {
		spotName = item;
	}
	/**
	 * Item method to define initialization rule for spots.
	 * @param item initialization rule
	 * @throws Exception
	 */
	public void spotCommand(String item) throws Exception {
		item = item.trim();
		if (item.equals("")) return;
		if (parser == null) {
			parser = new RuleCommandParser<Role>(RoleType.forName(RoleType.SPOT_NAME).getRoleClass());
		}
		ruleCommand(item);
	}
	/**
	 * Item method to create spots.
	 * @param item ignored
	 * @throws Exception
	 */
	public void spotCreate(String item) throws Exception {
		for (int i = 1; i <= numSpots; ++i) {
			Spot spot = new Spot();
			spot.assignName(spotName);
			if (command != null) {
				command.invoke(RoleType.forName(RoleType.SPOT_NAME).createRole(spot));
			}
		}
		if (numSpots < 0) {
			Spot spot = new Spot();
			spot.setName(spotName);
			if (command != null) {
				command.invoke(RoleType.forName(RoleType.SPOT_NAME).createRole(spot));
			}
		}
		numSpots = -1;
		spotName = DEFAULT_SPOT_NAME;
		parser = null;
		command = null;
		debugInfo = "";
	}
	/**
	 * Item method to re-initialize spots.
	 * @param item ignored
	 * @throws Exception
	 */
	public void spotInitialize(String item) throws Exception {
		for (int i = 1; i <= numSpots; ++i) {
			Spot spot = Spot.forName(spotName + i);
			if (command != null) {
				command.invoke(RoleType.forName(RoleType.SPOT_NAME).createRole(spot));
			}
		}
		if (numSpots < 0) {
			Spot spot = Spot.forName(spotName);
			if (command != null) {
				command.invoke(RoleType.forName(RoleType.SPOT_NAME).createRole(spot));
			}
		}
		numSpots = -1;
		spotName = DEFAULT_SPOT_NAME;
		parser = null;
		command = null;
		debugInfo = "";
	}

	/**
	 * Item method to specify step time.
	 * @param item step time
	 */
	public static void envStepTime(String item) {
		TimeCounter.getCurrentStepTime().set(Time.parse(item.trim()));
	}
	/**
	 * Item method to specify start time.
	 * @param item start time
	 */
	public static void envStartTime(String item) {
		TimeCounter.getCurrentTime().set(Time.parse(item.trim()));
	}
	/**
	 * Item method to load simulation.
	 * @param item file name
	 * @throws IOException
	 * @throws ClassNotFoundException
	 */
	public static void envLoad(String item) throws IOException, ClassNotFoundException {
		Environment.load(item).setCurrent();
	}
	/**
	 * Item method to save simulation.
	 * @param item file name
	 * @throws IOException
	 */
	public static void envSave(String item) throws IOException {
		Environment.getCurrent().save(item);
	}
	/**
	 * Item method to specify random seed.
	 * @param item random seed
	 */
	public static void envRandomSeed(String item) {
		item = item.trim();
		long seed = item.equals("") ? System.currentTimeMillis() : Long.parseLong(item);
		Environment.getCurrent().getRandom().setSeed(seed);
	}
}
