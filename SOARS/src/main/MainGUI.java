package main;

import java.io.PrintWriter;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import javax.swing.SwingUtilities;

import view.JMainFrame;
import view.JTabbedWriter;
import view.LogWriter;
import env.Agent;
import env.EquippedObject;
import env.Spot;

/**
 * The MainGUI class implements GUI of SOARS Model Builder.
 * @author H. Tanuma / SOARS project
 */
public class MainGUI extends MainConsole {

	/**
	 * Item method to dump attributes of all agents.
	 * @param arg title string
	 */
	public static void dumpAgents(String arg) {
		dumpAgentsToTab(arg);
	}
	/**
	 * Item method to dump attributes of all spots.
	 * @param arg title string
	 */
	public static void dumpSpots(String arg) {
		dumpSpotsToTab(arg);
	}
	/**
	 * Item method to dump attributes of all agents.
	 * @param arg title string
	 */
	public static void dumpAgentsToTab(String arg) {
		if (arg.equals("")) {
			arg = "Agents Dump";
		}
		PrintWriter out = new PrintWriter(new JTabbedWriter(JMainFrame.getCurrent(), arg));
		Iterator<Agent> it = Agent.getList().iterator();
		while (it.hasNext()) {
			Agent agent = it.next();
			out.println(agent.getEquipMap());
		}
	}
	/**
	 * Item method to dump attributes of all spots.
	 * @param arg title string
	 */
	public static void dumpSpotsToTab(String arg) {
		if (arg.equals("")) {
			arg = "Spots Dump";
		}
		PrintWriter out = new PrintWriter(new JTabbedWriter(JMainFrame.getCurrent(), arg));
		Iterator<Spot> it = Spot.getList().iterator();
		while (it.hasNext()) {
			Spot spot = it.next();
			out.println(spot.getEquipMap());
		}
	}

	protected void initLogFactory()
	{
		logAgentsToTab("");
		logSpotsToTab("<>");
	}
	/**
	 * Item method to set agent log output to display tab.
	 * @param head tab title
	 */
	public void logAgentsToTab(final String head) {
		logFactoryAgents = new LogWriter.Factory() {
			public LogWriter create(List<? extends EquippedObject> source, String key) {
				return new LogWriter(new JTabbedWriter(JMainFrame.getCurrent(), head + key + " (logging)", head + key), source, key);
			}
		};
	}
	/**
	 * Item method to set spot log output to display tab.
	 * @param head tab title
	 */
	public void logSpotsToTab(final String head) {
		logFactorySpots = new LogWriter.Factory() {
			public LogWriter create(List<? extends EquippedObject> source, String key) {
				return new LogWriter(new JTabbedWriter(JMainFrame.getCurrent(), head + key + " (logging)", head + key), source, key);
			}
		};
	}

	/**
	 * Main function for SOARS Model Builder.
	 * @param args parameters
	 * @throws Exception
	 */
	public static void main(String[] args) throws Exception {
		Class<?> parserClass = main(MainGUI.class, args);
		if (parserClass != null) {
			new JMainFrame(parserClass);
		}
	}
	/**
	 * Command line method to process default parameters.
	 * @param parserClass command line parser
	 * @param args parameters
	 * @throws Exception
	 */
	public static void arg(Class<?> parserClass, LinkedList<String> args) throws Exception {
		edit(parserClass, args);
	}
	/**
	 * Command line method to open script file.
	 * @param parserClass command line parser
	 * @param args parameters
	 * @throws Exception
	 */
	public static void edit(Class<?> parserClass, LinkedList<String> args) throws Exception {
		final JMainFrame frame = new JMainFrame(parserClass);
		final String fileName = args.removeFirst().toString();
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				frame.editScript(fileName);
			}
		});
		String[] array = new String[args.size()];
		args.toArray(array);
		args.clear();
		main(parserClass, array);
	}
}
