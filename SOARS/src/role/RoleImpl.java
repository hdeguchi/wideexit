package role;

import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.SortedSet;

import script.Command;
import script.Condition;
import time.Time;
import util.Askable;
import util.Invoker;
import util.MutableNumber;
import util.Ownable;
import util.Resolver;
import env.Agent;
import env.Environment;
import env.EquippedObject;
import env.Spot;
import env.SpotVal;

/**
 * The RoleImpl class implements role methods.
 * @author H. Tanuma / SOARS project
 */
abstract class RoleImpl extends Role {

	private static final long serialVersionUID = -3905169839925731996L;
	/**
	 * Key prefix string to get time variable.
	 */
	public static final String TIME_KEY_HEAD = "$Time.";

	protected static String[] splitPair(String string, String regex) {
		String[] pair = string.split(regex, 2);
		pair[0] = pair[0].trim();
		if (pair.length == 2) pair[1] = pair[1].trim();
		return pair;
	}
	protected static Command<Role> spotCommand(final SpotVal spot, final Command<EquippedObject> command) {
		return new Command<Role>() {
			private static final long serialVersionUID = -4119175496381736553L;
			public void invoke(Role role) throws Exception {
				command.invoke(spot.getSpot(role));
			}
		};
	}
	protected static Condition<Role> spotCondition(final SpotVal spot, final Condition<EquippedObject> condition) {
		return new Condition<Role>() {
			private static final long serialVersionUID = -8698597905371089109L;
			public boolean is(Role role) throws Exception {
				return condition.is(spot.getSpot(role));
			}
		};
	}
	protected static Command<Role> selfCommand(final Command<EquippedObject> command) {
		return new Command<Role>() {
			private static final long serialVersionUID = -1388377487157340979L;
			public void invoke(Role role) throws Exception {
				command.invoke(role.getSelf());
			}
		};
	}
	protected static Condition<Role> selfCondition(final Condition<EquippedObject> condition) {
		return new Condition<Role>() {
			private static final long serialVersionUID = -445404169911138072L;
			public boolean is(Role role) throws Exception {
				return condition.is(role.getSelf());
			}
		};
	}

	static Time parseTime(EquippedObject self, String time) {
		if (time.startsWith("+")) {
			Time timeObj = parseTime(self, time.substring(1));
			return timeObj != null ? new Time().set().add(timeObj) : null;
		}
		if (time.startsWith("-")) {
			Time timeObj = parseTime(self, time.substring(1));
			return timeObj != null ? new Time().set().sub(timeObj) : null;
		}
		Time timeObj = (Time) self.getEquip(TIME_KEY_HEAD + time);
		if (timeObj == null) {
			timeObj = Time.parse(time);
			self.setEquip(TIME_KEY_HEAD + time, timeObj);
		}
		else {
			timeObj = new Time().set(timeObj);
		}
		return timeObj;
	}
	static Command<EquippedObject> setTimeImpl(final String equality) {
		final String[] pair = splitPair(equality, "=");
		pair[0] = TIME_KEY_HEAD + pair[0];
		if (pair.length == 1) {
			return new Command<EquippedObject>() {
				private static final long serialVersionUID = 60454080995348160L;
				public void invoke(EquippedObject obj) throws Exception {
					obj.setEquip(pair[0], new Time().set());
				}
			};
		}
		if (pair[0].endsWith("+")) {
			pair[0] = pair[0].substring(0, pair[0].length() - 1).trim();
			return new Command<EquippedObject>() {
				private static final long serialVersionUID = -7667260919415237760L;
				public void invoke(EquippedObject self) throws Exception {
					Time timeAdd = self.<Time>getEquip(pair[0]);
					if (timeAdd == null) {
						throw new RuntimeException("Undefined Time Value - setTime " + equality);
					}
					Time time = parseTime(self, pair[1]);
					if (time == null) {
						throw new RuntimeException("Wrong Time Format - setTime " + equality);
					}
					timeAdd.add(time);
				}
			};
		}
		if (pair[0].endsWith("-")) {
			pair[0] = pair[0].substring(0, pair[0].length() - 1).trim();
			return new Command<EquippedObject>() {
				private static final long serialVersionUID = -7667260919415237760L;
				public void invoke(EquippedObject self) throws Exception {
					Time timeAdd = (Time) self.getEquip(pair[0]);
					if (timeAdd == null) {
						throw new RuntimeException("Undefined Time Value - setTime " + equality);
					}
					Time time = parseTime(self, pair[1]);
					if (time == null) {
						throw new RuntimeException("Wrong Time Format - setTime " + equality);
					}
					timeAdd.sub(time);
				}
			};
		}
		return new Command<EquippedObject>() {
			private static final long serialVersionUID = -6849086928364352479L;
			public void invoke(EquippedObject self) throws Exception {
				Time time = parseTime(self, pair[1]);
				if (time == null) {
					throw new RuntimeException("Wrong Time Format - setTime " + equality);
				}
				self.setEquip(pair[0], time);
			}
		};
	}
	static Condition<EquippedObject> isTimeImpl(final String time) {
		if (time.startsWith("@")) {
			final String timeAt = time.substring(1);
			final String timeKey = TIME_KEY_HEAD + timeAt;
			return new Condition<EquippedObject>() {
				private static final long serialVersionUID = 8803733992099635413L;
				public boolean is(EquippedObject self) throws Exception {
					Time timeVal = (Time) self.getEquip(timeKey);
					if (timeVal == null) {
						timeVal = Time.parse(timeAt);
						if (timeVal == null) {
							throw new RuntimeException("Wrong Time Format - isTime " + time);
						}
						self.setEquip(timeKey, timeVal);
					}
					return timeVal.isTimeAt();
				}
			};
		}
		final String timeKey = TIME_KEY_HEAD + time;
		return new Condition<EquippedObject>() {
			private static final long serialVersionUID = -2356712427030597244L;
			public boolean is(EquippedObject self) throws Exception {
				Time timeVal = (Time) self.getEquip(timeKey);
				if (timeVal == null) {
					timeVal = Time.parse(time);
					if (timeVal == null) {
						throw new RuntimeException("Wrong Time Format - isTime " + time);
					}
					self.setEquip(timeKey, timeVal);
				}
				return timeVal.isTime();
			}
		};
	}
	static void keywordImpl(EquippedObject self, String equality) {
		String[] pair = splitPair(equality, "=");
		if (self.setProp(pair[0], pair.length > 1 ? pair[1] : "") != null) {
			throw new RuntimeException("Redefined Keyword - keyword " + equality);
		}
	}
	static Command<EquippedObject> setImpl(final String equality) {
		final String[] pair = splitPair(equality, "=");
		return new Command<EquippedObject>() {
			private static final long serialVersionUID = 8820296312131478492L;
			public void invoke(EquippedObject obj) throws Exception {
				if (obj.setProp(pair[0], pair.length > 1 ? pair[1] : null) == null) {
					throw new RuntimeException("Undefined Keyword - set " + equality);
				}
			}
		};
	}
	static void copyImpl(EquippedObject self, String equality) {
		String[] pair = splitPair(equality, "=");
		if (pair.length == 1) {
			throw new RuntimeException("Missing '=' - copy " + equality);
		}
		self.setProp(pair[0], self.getProp(pair[1]));
	}
	static boolean equalsImpl(EquippedObject self, String equality) {
		String[] pair = splitPair(equality, "=");
		if (pair.length == 1) {
			throw new RuntimeException("Missing '=' - equals " + equality);
		}
		String lhs = self.getProp(pair[0]);
		String rhs = self.getProp(pair[1]);
		if (lhs == null || rhs == null) {
			throw new RuntimeException("Undefined Keyword - equals " + equality);
		}
		return lhs.equals(rhs);
	}
	static Condition<EquippedObject> isImpl(final String equality) {
		final String[] pair = splitPair(equality, "=");
		if (pair.length == 1) {
			throw new RuntimeException("Missing '=' - is " + equality);
		}
		return new Condition<EquippedObject>() {
			private static final long serialVersionUID = 3924691199102512721L;
			public boolean is(EquippedObject obj) throws Exception {
				String value = obj.getProp(pair[0]);
				if (value == null) {
					throw new RuntimeException("Undefined Keyword - is " + equality);
				}
				return value.equals(pair[1]);
			}
		};
	}
	static void equipImpl(EquippedObject self, String equality) {
		String[] pair = splitPair(equality, "=");
		self.setEquip(pair[0], self.getProp(pair[pair.length - 1]));
	}
	static void setEquipImpl(EquippedObject self, String equality) throws InstantiationException, IllegalAccessException, ClassNotFoundException {
		String[] pair = splitPair(equality, "=");
		self.setEquip(pair[0], pair.length == 1 ? null : Environment.getCurrent().classForName(pair[1]).newInstance());
	}
	static void copyEquipImpl(EquippedObject self, String equality) {
		String[] pair = splitPair(equality, "=");
		if (pair.length == 1) {
			throw new RuntimeException("Missing '=' - copyEquip " + equality);
		}
		self.setEquip(pair[0], self.getEquip(pair[1]));
	}
	static final Class<?>[] emptyParam = new Class[0];
	static Command<EquippedObject> cloneEquipImpl(String equality) throws Exception {
		final String[] pair = splitPair(equality, "=");
		return new Command<EquippedObject>() {
			private static final long serialVersionUID = 2793087055094769673L;
			transient Class<?> c = null;
			transient Method m = null;
			public void invoke(EquippedObject self) throws Exception {
				Object o = self.getEquip(pair[pair.length - 1]);
				if (c != o.getClass()) {
					c = o.getClass();
					m = c.getMethod("clone", emptyParam);
				}
				o = m.invoke(o, (Object[])emptyParam);
				if (o instanceof Ownable) {
					((Ownable) o).setOwner(null);
				}
				self.setEquip(pair[0], o);
			}
		};
	}
	static boolean equalsEquipImpl(EquippedObject self, String equality) {
		String[] pair = splitPair(equality, "=");
		if (pair.length == 1) {
			throw new RuntimeException("Missing '=' - equalsEquip " + equality);
		}
		return self.getEquip(pair[0]).equals(self.getEquip(pair[1]));
	}
	static boolean isEquipImpl(EquippedObject self, String equality) throws ClassNotFoundException {
		String[] pair = splitPair(equality, "=");
		Object o = self.getEquip(pair[0]);
		return pair.length == 1 ? o != null : Environment.getCurrent().classForName(pair[1]).isInstance(o);
	}
	static void printEquipImpl(EquippedObject self, String equality) {
		String[] pair = splitPair(equality, "=");
		if (pair[0].equals(pair[pair.length - 1])) {
			return;
		}
		self.setProp(pair[0], String.valueOf(self.getEquip(pair[pair.length - 1])));
	}
	static void logEquipImpl(final EquippedObject self, String equality) {
		String[] pair = splitPair(equality, "=");
		final String key = pair[pair.length - 1];
		if (pair[0].equals(key)) {
			return;
		}
		self.setEquip(pair[0], new Serializable() {
			private static final long serialVersionUID = -8516751700833428711L;
			public String toString() {
				Object o = self.getEquip(key);
				return o == null ? null : self.getEquip(key).toString();
			}
		});
	}
	static void setClassImpl(EquippedObject self, String equality) throws ClassNotFoundException {
		String[] pair = splitPair(equality, "=");
		if (pair.length == 1) {
			throw new RuntimeException("Missing '=' - setClass " + equality);
		}
		self.setEquip(pair[0], new Invoker(Environment.getCurrent().classForName(pair[1])));
	}
	private static Object invokerGet(EquippedObject self, String key) {
		Object o = self.getEquip(key);
		if (o instanceof Invoker) {
			return ((Invoker) o).getInstance();
		}
		if (o instanceof MutableNumber) {
			return ((MutableNumber) o).numberValue();
		}
		return o;
	}
	private static void invokerSet(EquippedObject self, String key, Object obj) {
		Object o = self.getEquip(key);
		if (o instanceof Invoker) {
			((Invoker) o).setInstance(obj);
		}
		else if (o instanceof MutableNumber && obj instanceof Number) {
			((MutableNumber) o).setNumber((Number) obj);
		}
		else {
			self.setEquip(key, obj);
		}
	}
	static void copyInstanceImpl(EquippedObject self, String equality) {
		String[] pair = splitPair(equality, "=");
		invokerSet(self, pair[0], pair.length == 1 ? null : invokerGet(self, pair[1]));
	}
	static void addParamImpl(EquippedObject self, String equality) throws ClassNotFoundException {
		String[] trio = equality.split("=", 3);
		Invoker i = (Invoker) self.getEquip(trio[0].trim());
		Object o = null;
		Class<?> c = Object.class;
		if (trio.length >= 2) {
			o = invokerGet(self, trio[1].trim());
			if (trio.length == 3) {
				c = Environment.getCurrent().classForName(trio[2].trim());
			}
			else if (o != null) {
				c = o.getClass();
			}
		}
		i.addParameter(o, c);
	}
	static void addParamBooleanImpl(EquippedObject self, String equality) throws ClassNotFoundException {
		String[] trio = equality.split("=", 3);
		if (trio.length == 1) {
			throw new RuntimeException("Missing '=' - addParamBoolean " + equality);
		}
		Invoker i = (Invoker) self.getEquip(trio[0].trim());
		Object o = invokerGet(self, trio[1].trim());
		if (!(o instanceof Boolean)) {
			o = Boolean.valueOf(String.valueOf(o));
		}
		Class<?> c = boolean.class;
		if (trio.length == 3) {
			c = Environment.getCurrent().classForName(trio[2].trim());
		}
		i.addParameter(o, c);
	}
	static void addParamStringImpl(EquippedObject self, String equality) {
		String[] pair = equality.split("=", 2);
		((Invoker) self.getEquip(pair[0].trim())).addParameter(pair.length == 1 ? null : pair[1], String.class);
	}
	static void addParamClassImpl(EquippedObject self, String equality) throws ClassNotFoundException {
		String[] pair = splitPair(equality, "=");
		((Invoker) self.getEquip(pair[0])).addParameter(pair.length == 1 ? null : Environment.getCurrent().classForName(pair[1]), Class.class);
	}
	static void setParamTypeImpl(EquippedObject self, String equality) throws ClassNotFoundException {
		String[] pair = splitPair(equality, "=");
		if (pair.length == 1) {
			throw new RuntimeException("Missing '=' - setParamType " + equality);
		}
		((Invoker) self.getEquip(pair[0])).setLastParameterType(Environment.getCurrent().classForName(pair[1]));
	}
	static void newInstanceImpl(EquippedObject self, String equality) throws IllegalArgumentException, SecurityException, InstantiationException, IllegalAccessException, InvocationTargetException, NoSuchMethodException {
		String[] pair = splitPair(equality, "=");
		Invoker i = (Invoker) self.getEquip(pair[pair.length - 1]);
		i.newInstance();
		if (pair.length == 2) {
			invokerSet(self, pair[0], i.getInstance());
		}
	}
	static boolean invokeClassImpl(EquippedObject self, String equality) throws IllegalArgumentException, SecurityException, IllegalAccessException, InvocationTargetException, NoSuchMethodException {
		String[] trio = equality.split("=", 3);
		if (trio.length == 1) {
			throw new RuntimeException("Missing '=' - invokeClass " + equality);
		}
		Invoker i = (Invoker) self.getEquip(trio[trio.length - 2].trim());
		Object o = i.invoke(trio[trio.length - 1].trim());
		if (trio.length == 3) {
			invokerSet(self, trio[0].trim(), o);
		}
		if (o instanceof Boolean) {
			return ((Boolean) o).booleanValue();
		}
		return o != null;
	}
	static void getFieldImpl(EquippedObject self, String equality) throws IllegalArgumentException, SecurityException, IllegalAccessException, NoSuchFieldException {
		String[] trio = equality.split("=", 3);
		if (trio.length != 3) {
			throw new RuntimeException("Missing '=' - getField " + equality);
		}
		Invoker i = (Invoker) self.getEquip(trio[1].trim());
		invokerSet(self, trio[0].trim(), i.getField(trio[2].trim()));
	}
	static void setFieldImpl(EquippedObject self, String equality) throws IllegalArgumentException, SecurityException, IllegalAccessException, NoSuchFieldException {
		String[] pair = splitPair(equality, "=");
		if (pair.length != 2) {
			throw new RuntimeException("Missing '=' - setField " + equality);
		}
		((Invoker) self.getEquip(pair[0])).setField(pair[1]);
	}
	static boolean isEmptyImpl(EquippedObject self, String collection) {
		return self.<Collection<?>>getEquip(collection).isEmpty();
	}
	boolean addAgentImpl(EquippedObject self, String equality) {
		String[] pair = splitPair(equality, "=");
		Agent agent = pair.length == 1 ? getAgent() : Agent.forName(pair[1]);
		if (agent == null) {
			throw new RuntimeException("Wrong Agent Name - addAgent " + equality);
		}
		return self.<Collection<Object>>getEquip(pair[0]).add(agent);
	}
	boolean addSpotImpl(EquippedObject self, String equality) {
		String[] pair = splitPair(equality, "=");
		Spot spot = pair.length == 1 ? getSpot() : Spot.forName(pair[1]);
		if (spot == null) {
			throw new RuntimeException("Wrong Spot Name - addSpot " + equality);
		}
		return self.<Collection<Object>>getEquip(pair[0]).add(spot);
	}
	static boolean addEquipImpl(EquippedObject self, String equality) {
		String[] pair = splitPair(equality, "=");
		if (pair.length == 1) {
			throw new RuntimeException("Missing '=' - addEquip " + equality);
		}
		return self.<Collection<Object>>getEquip(pair[0]).add(self.getEquip(pair[1]));
	}
	static boolean addStringImpl(EquippedObject self, String equality) {
		String[] pair = splitPair(equality, "=");
		if (pair.length == 1) {
			throw new RuntimeException("Missing '=' - addString " + equality);
		}
		return self.<Collection<Object>>getEquip(pair[0]).add(pair[1]);
	}
	void addFirstAgentImpl(EquippedObject self, String equality) {
		String[] pair = splitPair(equality, "=");
		Agent agent = pair.length == 1 ? getAgent() : Agent.forName(pair[1]);
		if (agent == null) {
			throw new RuntimeException("Wrong Agent Name - addFirstAgent " + equality);
		}
		self.<List<Object>>getEquip(pair[0]).add(0, agent);
	}
	void addFirstSpotImpl(EquippedObject self, String equality) {
		String[] pair = splitPair(equality, "=");
		Spot spot = pair.length == 1 ? getSpot() : Spot.forName(pair[1]);
		if (spot == null) {
			throw new RuntimeException("Wrong Spot Name - addFirstSpot " + equality);
		}
		self.<List<Object>>getEquip(pair[0]).add(0, spot);
	}
	static void addFirstEquipImpl(EquippedObject self, String equality) {
		String[] pair = splitPair(equality, "=");
		if (pair.length == 1) {
			throw new RuntimeException("Missing '=' - addFirstEquip " + equality);
		}
		self.<List<Object>>getEquip(pair[0]).add(0, self.getEquip(pair[1]));
	}
	static void addFirstStringImpl(EquippedObject self, String equality) {
		String[] pair = splitPair(equality, "=");
		if (pair.length == 1) {
			throw new RuntimeException("Missing '=' - addFirstString " + equality);
		}
		self.<List<Object>>getEquip(pair[0]).add(0, pair[1]);
	}
	void addLastAgentImpl(EquippedObject self, String equality) {
		String[] pair = splitPair(equality, "=");
		Agent agent = pair.length == 1 ? getAgent() : Agent.forName(pair[1]);
		if (agent == null) {
			throw new RuntimeException("Wrong Agent Name - addLastAgent " + equality);
		}
		List<Object> list = self.getEquip(pair[0]);
		list.add(list.size(), agent);
	}
	void addLastSpotImpl(EquippedObject self, String equality) {
		String[] pair = splitPair(equality, "=");
		Spot spot = pair.length == 1 ? getSpot() : Spot.forName(pair[1]);
		if (spot == null) {
			throw new RuntimeException("Wrong Spot Name - addLastSpot " + equality);
		}
		List<Object> list = self.getEquip(pair[0]);
		list.add(list.size(), spot);
	}
	static void addLastEquipImpl(EquippedObject self, String equality) {
		String[] pair = splitPair(equality, "=");
		if (pair.length == 1) {
			throw new RuntimeException("Missing '=' - addLastEquip " + equality);
		}
		List<Object> list = self.getEquip(pair[0]);
		list.add(list.size(), self.getEquip(pair[1]));
	}
	static void addLastStringImpl(EquippedObject self, String equality) {
		String[] pair = splitPair(equality, "=");
		if (pair.length == 1) {
			throw new RuntimeException("Missing '=' - addLastString " + equality);
		}
		List<Object> list = self.getEquip(pair[0]);
		list.add(list.size(), pair[1]);
	}
	static boolean addAllImpl(EquippedObject self, String equality) {
		String[] pair = splitPair(equality, "=");
		if (pair.length == 1) {
			throw new RuntimeException("Missing '=' - addAll " + equality);
		}
		return self.<Collection<Object>>getEquip(pair[0]).addAll(self.<Collection<Object>>getEquip(pair[1]));
	}
	boolean containsAgentImpl(EquippedObject self, String equality) {
		String[] pair = splitPair(equality, "=");
		Agent agent = pair.length == 1 ? getAgent() : Agent.forName(pair[1]);
		if (agent == null) {
			throw new RuntimeException("Wrong Agent Name - containsAgent " + equality);
		}
		return self.<Collection<?>>getEquip(pair[0]).contains(agent);
	}
	boolean containsSpotImpl(EquippedObject self, String equality) {
		String[] pair = splitPair(equality, "=");
		Spot spot = pair.length == 1 ? getSpot() : Spot.forName(pair[1]);
		if (spot == null) {
			throw new RuntimeException("Wrong Spot Name - containsSpot " + equality);
		}
		return self.<Collection<?>>getEquip(pair[0]).contains(spot);
	}
	static boolean containsEquipImpl(EquippedObject self, String equality) {
		String[] pair = splitPair(equality, "=");
		if (pair.length == 1) {
			throw new RuntimeException("Missing '=' - containsEquip " + equality);
		}
		return self.<Collection<?>>getEquip(pair[0]).contains(self.getEquip(pair[1]));
	}
	static boolean containsStringImpl(EquippedObject self, String equality) {
		String[] pair = splitPair(equality, "=");
		if (pair.length == 1) {
			throw new RuntimeException("Missing '=' - containsString " + equality);
		}
		return self.<Collection<?>>getEquip(pair[0]).contains(pair[1]);
	}
	boolean isFirstAgentImpl(EquippedObject self, String equality) {
		String[] pair = splitPair(equality, "=");
		Agent agent = pair.length == 1 ? getAgent() : Agent.forName(pair[1]);
		if (agent == null) {
			throw new RuntimeException("Wrong Agent Name - isFirstAgent " + equality);
		}
		List<?> list = self.getEquip(pair[0]);
		return !list.isEmpty() && list.get(0).equals(agent);
	}
	boolean isFirstSpotImpl(EquippedObject self, String equality) {
		String[] pair = splitPair(equality, "=");
		Spot spot = pair.length == 1 ? getSpot() : Spot.forName(pair[1]);
		if (spot == null) {
			throw new RuntimeException("Wrong Spot Name - isFirstSpot " + equality);
		}
		List<?> list = self.getEquip(pair[0]);
		return !list.isEmpty() && list.get(0).equals(spot);
	}
	static boolean isFirstEquipImpl(EquippedObject self, String equality) {
		String[] pair = splitPair(equality, "=");
		if (pair.length == 1) {
			throw new RuntimeException("Missing '=' - isFirstEquip " + equality);
		}
		List<?> list = self.getEquip(pair[0]);
		return !list.isEmpty() && list.get(0).equals(self.getEquip(pair[1]));
	}
	static boolean isFirstStringImpl(EquippedObject self, String equality) {
		String[] pair = splitPair(equality, "=");
		if (pair.length == 1) {
			throw new RuntimeException("Missing '=' - isFirstString " + equality);
		}
		List<?> list = self.getEquip(pair[0]);
		return !list.isEmpty() && list.get(0).equals(pair[1]);
	}
	boolean isLastAgentImpl(EquippedObject self, String equality) {
		String[] pair = splitPair(equality, "=");
		Agent agent = pair.length == 1 ? getAgent() : Agent.forName(pair[1]);
		if (agent == null) {
			throw new RuntimeException("Wrong Agent Name - isLastAgent " + equality);
		}
		List<Agent> list = self.getEquip(pair[0]);
		return !list.isEmpty() && list.get(list.size() - 1).equals(agent);
	}
	boolean isLastSpotImpl(EquippedObject self, String equality) {
		String[] pair = splitPair(equality, "=");
		Spot spot = pair.length == 1 ? getSpot() : Spot.forName(pair[1]);
		if (spot == null) {
			throw new RuntimeException("Wrong Spot Name - isLastSpot " + equality);
		}
		List<Spot> list = self.getEquip(pair[0]);
		return !list.isEmpty() && list.get(list.size() - 1).equals(spot);
	}
	static boolean isLastEquipImpl(EquippedObject self, String equality) {
		String[] pair = splitPair(equality, "=");
		if (pair.length == 1) {
			throw new RuntimeException("Missing '=' - isLastEquip " + equality);
		}
		List<?> list = self.getEquip(pair[0]);
		return !list.isEmpty() && list.get(list.size() - 1).equals(self.getEquip(pair[1]));
	}
	static boolean isLastStringImpl(EquippedObject self, String equality) {
		String[] pair = splitPair(equality, "=");
		if (pair.length == 1) {
			throw new RuntimeException("Missing '=' - isLastString " + equality);
		}
		List<String> list = self.getEquip(pair[0]);
		return !list.isEmpty() && list.get(list.size() - 1).equals(pair[1]);
	}
	boolean isMinAgentImpl(EquippedObject self, String equality) {
		String[] pair = splitPair(equality, "=");
		Agent agent = pair.length == 1 ? getAgent() : Agent.forName(pair[1]);
		if (agent == null) {
			throw new RuntimeException("Wrong Agent Name - isMinAgent " + equality);
		}
		SortedSet<Agent> set = self.getEquip(pair[0]);
		return !set.isEmpty() && set.first().equals(agent);
	}
	boolean isMinSpotImpl(EquippedObject self, String equality) {
		String[] pair = splitPair(equality, "=");
		Spot spot = pair.length == 1 ? getSpot() : Spot.forName(pair[1]);
		if (spot == null) {
			throw new RuntimeException("Wrong Spot Name - isMinSpot " + equality);
		}
		SortedSet<Spot> set = self.getEquip(pair[0]);
		return !set.isEmpty() && set.first().equals(spot);
	}
	static boolean isMinEquipImpl(EquippedObject self, String equality) {
		String[] pair = splitPair(equality, "=");
		if (pair.length == 1) {
			throw new RuntimeException("Missing '=' - isMinEquip " + equality);
		}
		SortedSet<?> set = self.getEquip(pair[0]);
		return !set.isEmpty() && set.first().equals(self.getEquip(pair[1]));
	}
	static boolean isMinStringImpl(EquippedObject self, String equality) {
		String[] pair = splitPair(equality, "=");
		if (pair.length == 1) {
			throw new RuntimeException("Missing '=' - isMinString " + equality);
		}
		SortedSet<?> set = self.getEquip(pair[0]);
		return !set.isEmpty() && set.first().equals(pair[1]);
	}
	boolean isMaxAgentImpl(EquippedObject self, String equality) {
		String[] pair = splitPair(equality, "=");
		Agent agent = pair.length == 1 ? getAgent() : Agent.forName(pair[1]);
		if (agent == null) {
			throw new RuntimeException("Wrong Agent Name - isMaxAgent " + equality);
		}
		SortedSet<Agent> set = self.getEquip(pair[0]);
		return !set.isEmpty() && set.last().equals(agent);
	}
	boolean isMaxSpotImpl(EquippedObject self, String equality) {
		String[] pair = splitPair(equality, "=");
		Spot spot = pair.length == 1 ? getSpot() : Spot.forName(pair[1]);
		if (spot == null) {
			throw new RuntimeException("Wrong Spot Name - isMaxSpot " + equality);
		}
		SortedSet<Spot> set = self.getEquip(pair[0]);
		return !set.isEmpty() && set.last().equals(spot);
	}
	static boolean isMaxEquipImpl(EquippedObject self, String equality) {
		String[] pair = splitPair(equality, "=");
		if (pair.length == 1) {
			throw new RuntimeException("Missing '=' - isMaxEquip " + equality);
		}
		SortedSet<?> set = self.getEquip(pair[0]);
		return !set.isEmpty() && set.last().equals(self.getEquip(pair[1]));
	}
	static boolean isMaxStringImpl(EquippedObject self, String equality) {
		String[] pair = splitPair(equality, "=");
		if (pair.length == 1) {
			throw new RuntimeException("Missing '=' - isMaxString " + equality);
		}
		SortedSet<String> set = self.getEquip(pair[0]);
		return !set.isEmpty() && set.last().equals(pair[1]);
	}
	static boolean containsAllImpl(EquippedObject self, String equality) {
		String[] pair = splitPair(equality, "=");
		if (pair.length == 1) {
			throw new RuntimeException("Missing '=' - containsAll " + equality);
		}
		return self.<Collection<?>>getEquip(pair[0]).containsAll(self.<Collection<?>>getEquip(pair[1]));
	}
	boolean removeAgentImpl(EquippedObject self, String equality) {
		String[] pair = splitPair(equality, "=");
		Agent agent = pair.length == 1 ? getAgent() : Agent.forName(pair[1]);
		if (agent == null) {
			throw new RuntimeException("Wrong Agent Name - removeAgent " + equality);
		}
		return self.<Collection<Agent>>getEquip(pair[0]).remove(agent);
	}
	boolean removeSpotImpl(EquippedObject self, String equality) {
		String[] pair = splitPair(equality, "=");
		Spot spot = pair.length == 1 ? getSpot() : Spot.forName(pair[1]);
		if (spot == null) {
			throw new RuntimeException("Wrong Spot Name - removeSpot " + equality);
		}
		return self.<Collection<Spot>>getEquip(pair[0]).remove(spot);
	}
	static boolean removeEquipImpl(EquippedObject self, String equality) {
		String[] pair = splitPair(equality, "=");
		if (pair.length == 1) {
			throw new RuntimeException("Missing '=' - removeEquip " + equality);
		}
		return self.<Collection<?>>getEquip(pair[0]).remove(self.getEquip(pair[1]));
	}
	static boolean removeStringImpl(EquippedObject self, String equality) {
		String[] pair = splitPair(equality, "=");
		if (pair.length == 1) {
			throw new RuntimeException("Missing '=' - removeString " + equality);
		}
		return self.<Collection<String>>getEquip(pair[0]).remove(pair[1]);
	}
	static Iterator<?> equipOneImpl(EquippedObject self, String equality) {
		String[] pair = splitPair(equality, "=");
		Iterator<?> iterator = self.<Collection<?>>getEquip(pair[pair.length - 1]).iterator();
		Object o = iterator.next();
		if (pair.length == 2) {
			self.setEquip(pair[0], o);
		}
		return iterator;
	}
	static void removeOneImpl(EquippedObject self, String equality) {
		equipOneImpl(self, equality).remove();
	}
	static Iterator<?> equipRandomOneImpl(EquippedObject self, String equality) {
		String[] pair = splitPair(equality, "=");
		Collection<?> c = self.<Collection<?>>getEquip(pair[pair.length - 1]);
		Iterator<?> iterator = c.iterator();
		Object o;
		int i = Environment.getCurrent().getRandom().nextInt(c.size()) + 1;
		do {
			o = iterator.next();
		} while (--i > 0);
		if (pair.length == 2) {
			self.setEquip(pair[0], o);
		}
		return iterator;
	}
	static void removeRandomOneImpl(EquippedObject self, String equality) {
		equipRandomOneImpl(self, equality).remove();
	}
	static void equipFirstImpl(EquippedObject self, String equality) {
		String[] pair = splitPair(equality, "=");
		if (pair.length == 1) {
			throw new RuntimeException("Missing '=' - equipFirst " + equality);
		}
		self.setEquip(pair[0], self.<List<?>>getEquip(pair[1]).get(0));
	}
	static void removeFirstImpl(EquippedObject self, String equality) {
		String[] pair = splitPair(equality, "=");
		List<?> list = self.getEquip(pair[pair.length - 1]);
		if (list.isEmpty()) {
			return;
		}
		Object o = list.remove(0);
		if (pair.length == 2) {
			self.setEquip(pair[0], o);
		}
	}
	static void equipLastImpl(EquippedObject self, String equality) {
		String[] pair = splitPair(equality, "=");
		if (pair.length == 1) {
			throw new RuntimeException("Missing '=' - equipLast " + equality);
		}
		List<?> list = self.getEquip(pair[1]);
		self.setEquip(pair[0], list.get(list.size() - 1));
	}
	static void removeLastImpl(EquippedObject self, String equality) {
		String[] pair = splitPair(equality, "=");
		List<?> list = self.getEquip(pair[pair.length - 1]);
		if (list.isEmpty()) {
			return;
		}
		Object o = list.remove(list.size() - 1);
		if (pair.length == 2) {
			self.setEquip(pair[0], o);
		}
	}
	static void equipMinImpl(EquippedObject self, String equality) {
		String[] pair = splitPair(equality, "=");
		if (pair.length == 1) {
			throw new RuntimeException("Missing '=' - equipMin " + equality);
		}
		self.setEquip(pair[0], self.<SortedSet<?>>getEquip(pair[1]).first());
	}
	static void removeMinImpl(EquippedObject self, String equality) {
		String[] pair = splitPair(equality, "=");
		SortedSet<?> s = self.getEquip(pair[pair.length - 1]);
		if (s.isEmpty()) {
			return;
		}
		Object o = s.first();
		s.remove(o);
		if (pair.length == 2) {
			self.setEquip(pair[0], o);
		}
	}
	static void equipMaxImpl(EquippedObject self, String equality) {
		String[] pair = splitPair(equality, "=");
		if (pair.length == 1) {
			throw new RuntimeException("Missing '=' - equipMax " + equality);
		}
		self.setEquip(pair[0], self.<SortedSet<?>>getEquip(pair[1]).last());
	}
	static void removeMaxImpl(EquippedObject self, String equality) {
		String[] pair = splitPair(equality, "=");
		SortedSet<?> s = self.getEquip(pair[pair.length - 1]);
		if (s.isEmpty()) {
			return;
		}
		Object o = s.last();
		s.remove(o);
		if (pair.length == 2) {
			self.setEquip(pair[0], o);
		}
	}
	static boolean removeAllImpl(EquippedObject self, String equality) {
		String[] pair = splitPair(equality, "=");
		Collection<?> c = self.getEquip(pair[0]);
		if (pair.length == 2) {
			return c.removeAll(self.<Collection<?>>getEquip(pair[1]));
		}
		if (c.isEmpty()) {
			return false;
		}
		c.clear();
		return true;
	}
	static boolean retainAllImpl(EquippedObject self, String equality) {
		String[] pair = splitPair(equality, "=");
		if (pair.length == 1) {
			throw new RuntimeException("Missing '=' - retainAll " + equality);
		}
		return self.<Collection<?>>getEquip(pair[0]).retainAll(self.<Collection<?>>getEquip(pair[1]));
	}
	static void reverseAllImpl(EquippedObject self, String list) {
		Collections.reverse(self.<List<?>>getEquip(list));
	}
	static void rotateAllImpl(EquippedObject self, String equality) {
		String[] pair = splitPair(equality, "=");
		if (pair.length == 1) {
			throw new RuntimeException("Missing '=' - rotateAll " + equality);
		}
		List<?> list = self.getEquip(pair[0]);
		if (list.isEmpty()) {
			return;
		}
		boolean minus = pair[1].startsWith("-");
		if (minus) {
			pair[1] = pair[1].substring(1);
		}
		Object obj = self.getEquip(pair[1]);
		int distance = obj instanceof Number ? ((Number) obj).intValue() : Integer.parseInt(pair[1].toString());
		Collections.rotate(list, minus ? -distance : distance);
	}
	static void shuffleAllImpl(EquippedObject self, String list) {
		Collections.shuffle(self.<List<?>>getEquip(list), Environment.getCurrent().getRandom());
	}
	static void sortAllImpl(EquippedObject self, String list) {
		Collections.sort(self.<List<Comparable<Comparable<?>>>>getEquip(list));
	}
	static void setAllImpl(EquippedObject self, String equality) {
		String[] trio = equality.split("=", 3);
		if (trio.length != 3) {
			throw new RuntimeException("Missing '=' - setAll " + equality);
		}
		trio[1] = trio[1].trim();
		trio[2] = trio[2].trim();
		Iterator<EquippedObject> i = self.<Collection<EquippedObject>>getEquip(trio[0].trim()).iterator();
		while (i.hasNext()) {
			i.next().setProp(trio[1], trio[2]);
		}
	}
	void askAllImpl(EquippedObject self, String equality) throws Exception {
		String[] pair = splitPair(equality, "=");
		if (pair.length == 1) {
			throw new RuntimeException("Missing '=' - askAll " + equality);
		}
		Iterator<Askable<Role>> i = self.<Collection<Askable<Role>>>getEquip(pair[0]).iterator();
		while (i.hasNext()) {
			i.next().ask(this, pair[1]);
		}
	}
	static void startRuleAllImpl(EquippedObject self, String equality) throws InstantiationException, IllegalAccessException {
		String[] pair = splitPair(equality, "=");
		Iterator<Spot> i = self.<Collection<Spot>>getEquip(pair[0]).iterator();
		if (pair.length == 1) {
			while (i.hasNext()) {
				startRuleImpl(i.next());
			}
		}
		else {
			while (i.hasNext()) {
				startRuleImpl(i.next(), pair[1]);
			}
		}
	}
	static void startRuleImpl(Spot spot) throws InstantiationException, IllegalAccessException {
		startRuleImpl(spot, RoleType.SPOT_NAME);
	}
	static void startRuleImpl(Spot spot, String roleType) throws InstantiationException, IllegalAccessException {
		final Role role = RoleType.forName(roleType).createRole(spot);
		new Resolver() {
			private static final long serialVersionUID = 5201336405691617384L;
			public void resolve() {
				Environment.getCurrent().addSpotRule(role);
			}
		}.requestResolve();
	}
	static boolean invokeEquipImpl(EquippedObject self, String formula) throws Exception {
		ArrayList<Object> stack = new ArrayList<Object>();
		ArrayList<Object> param = new ArrayList<Object>();
		String[] pipes = formula.split("<", -1);
		for (int i = pipes.length - 1; i >= 0; --i) {
			String[] terms = pipes[i].split(">", -1);
			for (int j = terms.length - 1; j >= 0; --j) {
				String[] pair = splitPair(terms[j], "=");
				if (pair[0].equals("")) {
					if (j < terms.length - 1) {
						stack.add(null);
					}
					if (j > 0) {
						param.clear();
					}
				}
				else if (pair.length == 1) {
					Object o;
					if (param.isEmpty()) {
						o = self.getEquip(pair[0]);
					}
					else {
						o = param.get(param.size() - 1);
						param.clear();
						self.setEquip(pair[0], o);
					}
					stack.add(o);
				}
				else {
					Class<?>[] types = new Class[param.size()];
					for (int k = 0; k < types.length; ++k) {
						types[k] = Object.class;
					}
					Object o = self.getEquip(pair[0]);
					stack.add(o.getClass().getMethod(pair[1], types).invoke(o, param.toArray()));
					param.clear();
				}
			}
			if (i > 0) {
				param.add(stack.remove(stack.size() - 1));
			}
		}
		if (param.isEmpty()) {
			return stack.get(stack.size() - 1) != null;
		}
		return ((Boolean) param.get(param.size() - 1)).booleanValue();
	}
	static Object parseKey(EquippedObject self, String key) {
		if (key.startsWith("\"")) {
			if (key.endsWith("\"")) {
				return key.substring(1, key.length() - 1);
			}
		}
		else if (key.startsWith("{")) {
			if (key.endsWith("}")) {
				key = key.substring(1, key.length() - 1).trim();
				if (key.equals("")) {
					return (Agent) self;
				}
				return Agent.forName(key);
			}
		}
		else if (key.startsWith("<")) {
			if (key.endsWith(">")) {
				key = key.substring(1, key.length() - 1).trim();
				if (key.equals("")) {
					if (self instanceof Agent) {
						return ((Agent) self).getSpot();
					}
					return self;
				}
				return Spot.forName(key);
			}
		}
		return self.getEquip(key);
	}
	static void clearMapImpl(EquippedObject self, String map) {
		self.<Map<?, ?>>getEquip(map).clear();
	}
	static boolean isEmptyMapImpl(EquippedObject self, String map) {
		return self.<Map<?, ?>>getEquip(map).isEmpty();
	}
	static boolean containsKeyImpl(EquippedObject self, String equality) {
		String[] pair = splitPair(equality, "=");
		if (pair.length == 1) {
			throw new RuntimeException("Missing '=' - containsKey " + equality);
		}
		return self.<Map<?, ?>>getEquip(pair[0]).containsKey(parseKey(self, pair[1]));
	}
	static boolean containsValueImpl(EquippedObject self, String equality) {
		String[] pair = splitPair(equality, "=");
		if (pair.length == 1) {
			throw new RuntimeException("Missing '=' - containsValue " + equality);
		}
		return self.<Map<?, ?>>getEquip(pair[0]).containsValue(parseKey(self, pair[1]));
	}
	static void equipMapKeysImpl(EquippedObject self, String equality) {
		String[] pair = splitPair(equality, "=");
		if (pair.length == 1) {
			throw new RuntimeException("Missing '=' - equipMapKeys " + equality);
		}
		self.setEquip(pair[0], self.<Map<?, ?>>getEquip(pair[1]).keySet());
	}
	static void equipMapValuesImpl(EquippedObject self, String equality) {
		String[] pair = splitPair(equality, "=");
		if (pair.length == 1) {
			throw new RuntimeException("Missing '=' - equipMapValues " + equality);
		}
		self.setEquip(pair[0], self.<Map<?, ?>>getEquip(pair[1]).values());
	}
	static void getMapImpl(EquippedObject self, String equality) {
		String[] trio = equality.split("=", 3);
		if (trio.length != 3) {
			throw new RuntimeException("Missing '=' - getMap " + equality);
		}
		self.setEquip(trio[0].trim(), self.<Map<?, ?>>getEquip(trio[1].trim()).get(parseKey(self, trio[2].trim())));
	}
	static void removeMapImpl(EquippedObject self, String equality) {
		String[] trio = equality.split("=", 3);
		if (trio.length < 2) {
			throw new RuntimeException("Missing '=' - removeMap " + equality);
		}
		Object o = self.<Map<?, ?>>getEquip(trio[trio.length - 2].trim()).remove(parseKey(self, trio[trio.length - 1].trim()));
		if (trio.length == 3) {
			self.setEquip(trio[0].trim(), o);
		}
	}
	static boolean equalsMapImpl(EquippedObject self, String equality) {
		String[] trio = equality.split("=", 3);
		if (trio.length != 3) {
			throw new RuntimeException("Missing '=' - equalsMap " + equality);
		}
		Object value = parseKey(self, trio[2].trim());
		Object key = parseKey(self, trio[1].trim());
		return self.<Map<?, ?>>getEquip(trio[0].trim()).get(key).equals(value);
	}
	static void putMapImpl(EquippedObject self, String equality) {
		String[] quad = equality.split("=", 4);
		if (quad.length < 3) {
			throw new RuntimeException("Missing '=' - putMap " + equality);
		}
		Object value = parseKey(self, quad[quad.length - 1].trim());
		Object key = parseKey(self, quad[quad.length - 2].trim());
		Object o = self.<Map<Object,Object>>getEquip(quad[quad.length - 3].trim()).put(key, value);
		if (quad.length == 4) {
			self.setEquip(quad[0].trim(), o);
		}
	}
	static void putMapAllImpl(EquippedObject self, String equality) {
		String[] pair = splitPair(equality, "=");
		if (pair.length == 1) {
			throw new RuntimeException("Missing '=' - putMapAll " + equality);
		}
		self.<Map<Object,Object>>getEquip(pair[0]).putAll(self.<Map<?,?>>getEquip(pair[1]));
	}
}
