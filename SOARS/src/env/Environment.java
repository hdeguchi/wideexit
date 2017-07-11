package env;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.io.Serializable;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Random;

import role.Role;
import time.TimeCounter;
import util.Resolvable;

/**
 * The Environment class represents simulation instance of SOARS.
 * @author H. Tanuma / SOARS project
 */
public class Environment implements Serializable {

	private static final long serialVersionUID = 1470208654964159542L;
	static final String version = "20110728";
	/**
	 * Get version string.
	 * @return version string
	 */
	public static String getVersion() {
		return version;
	}
	StepCounter stepCounter = new TimeCounter();
	LinkedList<Resolvable> resolveRequests = new LinkedList<Resolvable>();
	Random random = new Random();
	class ShuffledArrayList<E> extends ArrayList<E> {
		private static final long serialVersionUID = 1492803261174065875L;
		public Iterator<E> iterator() {
			return new Iterator<E>() {
				int index = 0;
				public boolean hasNext() {
					return index < size();
				}
				public E next() {
					int sel = random.nextInt(size() - index) + index;
					return set(sel, set(index++, get(sel)));
				}
				public void remove() {
					throw new UnsupportedOperationException();
				}
			};
		}
	}
	ArrayList<Role> spotRules = new ArrayList<Role>();
	ArrayList<Role> shuffledSpotRules = new ShuffledArrayList<Role>();
	NamedObjectDB agents = new NamedObjectDB(new ShuffledArrayList<NamedObject>());
	NamedObjectDB spots = new NamedObjectDB(new ShuffledArrayList<NamedObject>());
	NamedObjectDB roleTypes = new NamedObjectDB(new ShuffledArrayList<NamedObject>()) {
		private static final long serialVersionUID = -7548863044480511587L;
		public <E extends NamedObject> E forName(String name) {
			return super.forName(name.split(":", 2)[0]);
		}
	};
	NamedObjectDB others = new NamedObjectDB(new ShuffledArrayList<NamedObject>());
	URL[] urls = new URL[0];
	transient URLClassLoader classLoader = null;
	HashMap<String, Class<?>> classMap = new HashMap<String, Class<?>>();
	{
		initClassMap();
	}
	private void initClassMap() {
		classMap.put("byte", byte[].class);
		classMap.put("char", char[].class);
		classMap.put("double", double[].class);
		classMap.put("float", float[].class);
		classMap.put("int", int[].class);
		classMap.put("long", long[].class);
		classMap.put("short", short[].class);
		classMap.put("boolean", boolean[].class);
	}
	static ThreadLocal<Environment> current = new ThreadLocal<Environment>() {
		protected Environment initialValue() {
			return new Environment();
		}
	};
	static ThreadLocal<PrintWriter> out = new ThreadLocal<PrintWriter>() {
		protected PrintWriter initialValue() {
			return new PrintWriter(System.err, true);
		}
	};

	/**
	 * Get simulation instance corresponds to current thread.
	 * @return simulation instance
	 */
	public static Environment getCurrent() {
		return (Environment) current.get();
	}
	/**
	 * Set simulation instance corresponds to current thread.
	 */
	public void setCurrent() {
		current.set(this);
	}
	/**
	 * Get console writer corresponds to current thread.
	 * @return console writer
	 */
	public static PrintWriter getConsole() {
		return (PrintWriter) out.get();
	}
	/**
	 * Set console writer corresponds to current thread.
	 * @param out console writer
	 */
	public static void setConsole(PrintWriter out) {
		Environment.out.set(out);
	}
	/**
	 * Execute simulation for a stage.
	 * @throws Exception
	 */
	public void doAgentPhase() throws Exception {
		if (Thread.interrupted()) {
			throw new InterruptedException();
		}
		int stage = stepCounter.getStage();
		// Prepare Sentinel for Self Resolution
		resolveRequests.addFirst(null);
		// Agent Turns
		Iterator<NamedObject> agents = (stepCounter.isShuffledStage() ? getAgents().getShuffledList() : getAgents().getList()).iterator();
		while (agents.hasNext()) {
			Agent agent = (Agent) agents.next();
			// General Rule Actions
			Role role = agent.getGeneralRole();
			role.getRoleType().applyRules(role, stage);
			// Active Role Actions
			role = agent.getActiveRole();
			if (role != null) {
				role.getRoleType().applyRules(role, stage);
			}
			// Self Resolution
			doResolution();
		}
		// Remove Sentinel
		resolveRequests.removeFirst();
		// Spot Rules
		Iterator<Role> spots = (stepCounter.isShuffledStage() ? shuffledSpotRules : spotRules).iterator();
		while (spots.hasNext()) {
			Role role = (Role) spots.next();
			role.getRoleType().applyRules(role, stage);
		}
	}
	/**
	 * Resolve all requests for resolver.
	 * @throws Exception
	 */
	public void doResolution() throws Exception {
		while (!resolveRequests.isEmpty()) {
			Resolvable resolver= (Resolvable) resolveRequests.getFirst();
			if (resolver == null) break;
			resolveRequests.removeFirst();
			resolver.resolve();
		}
	}
	/**
	 * Get step count.
	 * @return step count
	 */
	public StepCounter getStepCounter() {
		return stepCounter;
	}
	/**
	 * Get list of requests for resolver.
	 * @return list of requests for resolver
	 */
	public LinkedList<Resolvable> getResolveRequests() {
		return resolveRequests;
	}
	/**
	 * Start rules for a spot role.
	 * @param role spot role
	 */
	public void addSpotRule(Role role) {
		spotRules.add(role);
		shuffledSpotRules.add(role);
	}
	/**
	 * Stop rules for a spot role.
	 * @param role spot role
	 * @return true if rules are removed.
	 */
	public boolean removeSpotRule(Role role) {
		return spotRules.remove(role) && shuffledSpotRules.remove(role);
	}
	/**
	 * Get database of agents.
	 * @return databese of agents
	 */
	public NamedObjectDB getAgents() {
		return agents;
	}
	/**
	 * Get database of spots.
	 * @return database of spots
	 */
	public NamedObjectDB getSpots() {
		return spots;
	}
	/**
	 * Get database of role types.
	 * @return database of role types
	 */
	public NamedObjectDB getRoleTypes() {
		return roleTypes;
	}
	/**
	 * Get database of others.
	 * @return database of others
	 */
	public NamedObjectDB getOthers() {
		return others;
	}
	/**
	 * Get random generator.
	 * @return random generator
	 */
	public Random getRandom() {
		return random;
	}
	/**
	 * Register path for additional class file.
	 * @param url path for additional class file
	 */
	public void addURL(URL url) {
		URL[] urlsNew = new URL[urls.length + 1];
		System.arraycopy(urls, 0, urlsNew, 0, urls.length);
		urlsNew[urls.length] = url;
		urls = urlsNew;
		classLoader = null;
		classMap.clear();
		initClassMap();
		return;
	}
	private String className(String className) {
		if (className.endsWith("[]")) {
			return '[' + className(className.substring(0, className.length() - 2));
		}
		Class<?> c = classMap.get(className);
		return c != null ? c.getName() : "[L" + className + ';';
	}
	/**
	 * Get class for specified name.
	 * @param className class name
	 * @return class object
	 * @throws ClassNotFoundException
	 */
	public Class<?> classForName(String className) throws ClassNotFoundException {
		Class<?> c = classMap.get(className);
		if (c == null) {
			if (classLoader == null) {
				classLoader = URLClassLoader.newInstance(urls, getClass().getClassLoader());
			}
			c = Class.forName(className(className), true, classLoader);
			classMap.put(className, c);
		}
		return c.getComponentType();
	}
	/**
	 * Load serialized instance.
	 * @param fileName file name of saved instance
	 * @return loaded instance
	 * @throws IOException
	 * @throws ClassNotFoundException
	 */
	public static Environment load(String fileName) throws IOException, ClassNotFoundException {
		ObjectInputStream oin = new ObjectInputStream(new FileInputStream(fileName));
		Environment env = (Environment) oin.readObject();
		oin.close();
		return env;
	}
	/**
	 * Save serialized instance.
	 * @param fileName file name to save
	 * @throws IOException
	 */
	public void save(String fileName) throws IOException {
		ObjectOutputStream oout = new ObjectOutputStream(new FileOutputStream(fileName));
		oout.writeObject(this);
		oout.close();
	}
}
