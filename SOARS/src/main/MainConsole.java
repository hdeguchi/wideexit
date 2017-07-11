package main;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.io.StringReader;
import java.io.StringWriter;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;

import role.Role;
import role.RoleType;
import script.ScriptParser;
import script.ScriptReader;
import time.Time;
import time.TimeCounter;
import view.Clipboard;
import view.LogWriter;
import env.Agent;
import env.Environment;
import env.EquippedObject;
import env.ObjectLoader;
import env.Spot;

/**
 * The MainConsole class implements command line interface of SOARS.
 * @author H. Tanuma / SOARS project
 */
public class MainConsole extends ObjectLoader {

	/**
	 * Item method to dump attributes of all agents.
	 * @param arg title string
	 */
	public static void dumpAgents(String arg) {
		dumpAgentsToConsole(arg);
	}
	/**
	 * Item method to dump attributes of all spots.
	 * @param arg title string
	 */
	public static void dumpSpots(String arg) {
		dumpSpotsToConsole(arg);
	}
	/**
	 * Item method to dump attributes of all agents.
	 * @param arg title string
	 */
	public static void dumpAgentsToConsole(String arg) {
		PrintStream out = System.out;
		out.println(arg.equals("") ? "[Agents Dump]" : arg);
		Iterator<Agent> it = Agent.getList().iterator();
		while (it.hasNext()) {
			Agent agent = it.next();
			out.println(agent.getEquipMap());
		}
		out.println();
	}
	/**
	 * Item method to dump attributes of all spots.
	 * @param arg title string
	 */
	public static void dumpSpotsToConsole(String arg) {
		PrintStream out = System.out;
		out.println(arg.equals("") ? "[Spots Dump]" : arg);
		Iterator<Spot> it = Spot.getList().iterator();
		while (it.hasNext()) {
			Spot spot = it.next();
			out.println(spot.getEquipMap());
		}
		out.println();
	}

	ArrayList<StringWriter> logBuffers = new ArrayList<StringWriter>();
	protected LogWriter.Factory logFactoryAgents;
	protected LogWriter.Factory logFactorySpots;
	{
		initLogFactory();
	}
	protected void initLogFactory()
	{
		logAgentsToConsole("");
		logSpotsToConsole("");
	}
	/**
	 * Item method to set agent log output to console.
	 * @param ignore ignored
	 */
	public void logAgentsToConsole(String ignore) {
		logFactoryAgents = new LogWriter.Factory() {
			public LogWriter create(List<? extends EquippedObject> source, String key) {
				StringWriter buffer = new StringWriter();
				logBuffers.add(buffer);
				return new LogWriter(buffer, source, key);
			}
		};
	}
	/**
	 * Item method to set spot log output to console.
	 * @param ignore ignored
	 */
	public void logSpotsToConsole(String ignore) {
		logFactorySpots = new LogWriter.Factory() {
			public LogWriter create(List<? extends EquippedObject> source, String key) {
				StringWriter buffer = new StringWriter();
				logBuffers.add(buffer);
				return new LogWriter(buffer, source, key);
			}
		};
	}
	/**
	 * Item method to set agent log output to file.
	 * @param head file name prefix
	 */
	public void logAgentsToFile(final String head) {
		logFactoryAgents = new LogWriter.Factory() {
			public LogWriter create(List<? extends EquippedObject> source, String key) throws IOException {
				return new LogWriter(new BufferedWriter(new FileWriter(head + key + ".log")), source, key);
			}
		};
	}
	/**
	 * Item method to set spot log output to file.
	 * @param head file name prefix
	 */
	public void logSpotsToFile(final String head) {
		logFactorySpots = new LogWriter.Factory() {
			public LogWriter create(List<? extends EquippedObject> source, String key) throws IOException {
				return new LogWriter(new BufferedWriter(new FileWriter(head + key + ".log")), source, key);
			}
		};
	}
	/**
	 * Item method to create sub directory.
	 * @param dirs sub directory path
	 */
	public void logMkdirs(String dirs) {
		new File(dirs).mkdirs();
	}

	List<Agent> logListAgents = Agent.getList();
	List<Spot> logListSpots = Spot.getList();

	/**
	 * Item method to register agents to output logs.
	 * @param name agent name
	 * @throws Exception
	 */
	public void logAddAgents(String name) throws Exception {
		Environment.getCurrent().doResolution();
		if (name.equals("")) {
			logListAgents = Agent.getList();
			return;
		}
		if (logListAgents == Agent.getList()) {
			logListAgents = new ArrayList<Agent>();
		}
		logListAgents.addAll(Environment.getCurrent().getAgents().<Agent>getList(name));
	}
	/**
	 * Item method to register spots to output logs.
	 * @param name spot name
	 * @throws Exception
	 */
	public void logAddSpots(String name) throws Exception {
		Environment.getCurrent().doResolution();
		if (name.equals("")) {
			logListSpots = Spot.getList();
			return;
		}
		if (logListSpots == Spot.getList()) {
			logListSpots = new ArrayList<Spot>();
		}
		logListSpots.addAll(Environment.getCurrent().getSpots().<Spot>getList(name));
	}

	ArrayList<LogWriter> logWriters = new ArrayList<LogWriter>();
	Time logStepTime = null;
	Time logTime = null;

	/**
	 * Item method to register agent attribute to output logs.
	 * @param key attribute name
	 * @throws Exception
	 */
	public void logAgents(String key) throws Exception {
		Environment.getCurrent().doResolution();
		logWriters.add(logFactoryAgents.create(logListAgents, key.trim()));
	}
	/**
	 * Item method to register spot attribute to output logs.
	 * @param key attribute name
	 * @throws Exception
	 */
	public void logSpots(String key) throws Exception {
		Environment.getCurrent().doResolution();
		logWriters.add(logFactorySpots.create(logListSpots, key.trim()));
	}
	/**
	 * Item method to specify interval time to output logs.
	 * @param time interval time
	 */
	public void logStepTime(String time) {
		time = time.trim();
		logStepTime = time.equals("") ? null : Time.parse(time);
	}
	/**
	 * Item method to specify start time to output logs.
	 * @param time start time
	 */
	public void logStartTime(String time) {
		logTime = new Time().set(Time.parse(time.trim()));
	}
	private static final Time endless = new Time() {
		private static final long serialVersionUID = -6367525121866429775L;
		public boolean isTime() {
			return false;
		}
	};
	/**
	 * Item method to execute simulation.
	 * @param time time to finish simulation
	 * @throws Exception
	 */
	public void execUntil(String time) throws Exception {
		time = time.trim();
		Time endTime = time.equals("") ? endless : Time.parse(time);
		boolean skip = false;
		if (logStepTime == null) {
			logStepTime = TimeCounter.getCurrentStepTime();
			skip = true;
		}
		if (logTime == null) {
			logTime = new Time().set();
		}
		Environment current = Environment.getCurrent();
		long ms = System.currentTimeMillis();
		current.doResolution();
		try {
			while (current.getStepCounter().isInitialStage()) {
				current.doAgentPhase();
				current.doResolution();
				current.getStepCounter().nextStage();
			}
			for (int i = 0; i < logWriters.size(); ++i) {
				((LogWriter) logWriters.get(i)).title();
			}
			for (;;) {
				if (endTime.isTime()) {
					endTime = endless;
					logTime = endless;
					current.getStepCounter().terminate();
					if (current.getStepCounter().isTerminated()) {
						break;
					}
					current.getStepCounter().nextStage();
				}
				current.doAgentPhase();
				current.doResolution();
				if (current.getStepCounter().isTerminated()) {
					for (int i = 0; i < logWriters.size(); ++i) {
						((LogWriter) logWriters.get(i)).log(skip);
					}
					break;
				}
				if (current.getStepCounter().isLastStage() && logTime.isTime()) {
					for (int i = 0; i < logWriters.size(); ++i) {
						((LogWriter) logWriters.get(i)).log(skip);
					}
					logTime.add(logStepTime);
				}
				current.getStepCounter().nextStage();
			}
			ms = System.currentTimeMillis() - ms;
		}
		finally {
			for (int i = 0; i < logWriters.size(); ++i) {
				((LogWriter) logWriters.get(i)).close();
			}
			for (int i = 0; i < logBuffers.size(); ++i) {
				System.out.println(logBuffers.get(i));
			}
			logWriters.clear();
			logBuffers.clear();
			logStepTime = null;
			logTime = null;
		}
		PrintWriter out = Environment.getConsole();
		out.print("Execution Time:  ");
		out.print(ms);
		out.println(" [ms]");
		out.println();
		out.close();
	}

	/**
	 * Entry main function for MainConsole class.
	 * @param args command parameters
	 * @throws Exception
	 */
	public static void main(String[] args) throws Exception {
		main(MainConsole.class, args);
	}
	/**
	 * Parse command line parameters.
	 * @param parserClass command line parser
	 * @param args command line parameters
	 * @return last command line parser
	 * @throws Exception
	 */
	public static Class<?> main(Class<?> parserClass, String[] args) throws Exception {
		LinkedList<String> list = new LinkedList<String>();
		for (int i = 0; i < args.length; ++i) {
			list.addLast(args[i]);
		}
		while (!list.isEmpty()) {
			String[] pair = list.getFirst().toString().split("=", 2);
			list.set(0, pair[pair.length - 1]);
			if (pair.length == 1) {
				pair[0] = "arg";
			}
			parserClass.getMethod(pair[0], new Class[]{Class.class, LinkedList.class}).invoke(null, new Object[]{parserClass, list});
			if (list.isEmpty()) {
				return null;
			}
			Object o = list.removeFirst();
			if (o instanceof Class<?>) {
				parserClass = (Class<?>) o;
			}
		}
		return parserClass;
	}
	/**
	 * Command line method to invoke main function.
	 * @param parserClass command line parser
	 * @param args parameters
	 * @throws Exception
	 */
	public static void main(Class<?> parserClass, LinkedList<?> args) throws Exception {
		parserClass = Environment.getCurrent().classForName(args.removeFirst().toString());
		String[] array = new String[args.size()];
		args.toArray(array);
		args.clear();
		parserClass.getMethod("main", new Class[]{String[].class}).invoke(null, new Object[]{array});
	}
	/**
	 * Command line method to change parser class.
	 * @param parserClass command line parser
	 * @param args parameters
	 * @throws Exception
	 */
	public static void parser(Class<?> parserClass, LinkedList<Object> args) throws Exception {
		args.set(0, Environment.getCurrent().classForName(args.getFirst().toString()));
	}
	/**
	 * Command line method to register class URL.
	 * @param parserClass command line parser
	 * @param args parameters
	 * @throws Exception
	 */
	public static void classURL(Class<?> parserClass, LinkedList<?> args) throws Exception {
		Environment.getCurrent().addURL(new URL(args.getFirst().toString()));
	}
	/**
	 * Command line method to reset simulation.
	 * @param parserClass command line parser
	 * @param args parameters
	 * @throws Exception
	 */
	public static void reset(Class<?> parserClass, LinkedList<?> args) throws Exception {
		new Environment().setCurrent();
		if (!args.getFirst().equals("")) {
			args.addFirst(null);
		}
	}
	/**
	 * Command line method to save simulation.
	 * @param parserClass command line parser
	 * @param args parameters
	 * @throws Exception
	 */
	public static void save(Class<?> parserClass, LinkedList<?> args) throws Exception {
		Environment.getCurrent().save(args.getFirst().toString());
	}
	/**
	 * Command line method to load simulation.
	 * @param parserClass command line parser
	 * @param args parameters
	 * @throws Exception
	 */
	public static void load(Class<?> parserClass, LinkedList<?> args) throws Exception {
		Environment.load(args.getFirst().toString()).setCurrent();
	}
	/**
	 * Command line method to create spot.
	 * @param parserClass command line parser
	 * @param args parameters
	 * @throws Exception
	 */
	public static void spot(Class<?> parserClass, LinkedList<?> args) throws Exception {
		new Spot().setName(args.getFirst().toString());
	}
	/**
	 * Command line method to execute command script.
	 * @param parserClass command line parser
	 * @param args parameters
	 * @throws Exception
	 */
	public static void command(Class<?> parserClass, LinkedList<?> args) throws Exception {
		List<Spot> spots = Spot.getList();
		Role role = RoleType.forName(RoleType.SPOT_NAME).createRole((Spot) spots.get(spots.size() - 1));
		role.parseCommand(args.getFirst().toString()).invoke(role);
	}
	/**
	 * Command line method to set locale.
	 * @param parserClass command line parser
	 * @param args parameters
	 * @throws Exception
	 */
	public static void locale(Class<?> parserClass, LinkedList<?> args) throws Exception {
		Locale.setDefault(new Locale(args.getFirst().toString()));
	}
	/**
	 * Command line method to process default parameters.
	 * @param parserClass command line parser
	 * @param args parameters
	 * @throws Exception
	 */
	public static void arg(Class<?> parserClass, LinkedList<?> args) throws Exception {
		run(parserClass, args);
	}
	/**
	 * Command line method to execute simulation.
	 * @param parserClass command line parser
	 * @param args parameters
	 * @throws Exception
	 */
	public static void run(Class<?> parserClass, LinkedList<?> args) throws Exception {
		String arg = args.getFirst().toString();
		if (arg.equals("-")) {
			((ScriptParser) parserClass.newInstance()).parseAll(new ScriptReader(new StringReader(Clipboard.getString()), "Clipboard"));
		}
		else {
			((ScriptParser) parserClass.newInstance()).parseAll(arg);
		}
	}
}
