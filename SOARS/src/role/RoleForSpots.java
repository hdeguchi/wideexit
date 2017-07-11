package role;

import java.lang.reflect.InvocationTargetException;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.SortedSet;

import script.Command;
import script.Condition;
import util.Askable;
import util.Invoker;
import util.Resolver;
import env.Agent;
import env.Environment;
import env.EquippedObject;
import env.Spot;
import env.SpotVal;

/**
 * The RoleForSpots class implements spot role methods.
 * @author H. Tanuma / SOARS project
 */
public class RoleForSpots extends RoleImpl {

	private static final long serialVersionUID = 2586749080661114108L;
	/**
	 * Key prefix string to get role variable.
	 */
	public static final String ROLE_KEY_HEAD = "$Role.";
	protected boolean debug = false;

	protected boolean isDebug() {
		boolean d = debug;
		debug = false;
		return d;
	}
	/**
	 * Enable debug tracing.
	 * @return false
	 */
	public boolean trace() {
		debug = true;
		return false;
	}
	/**
	 * Disable debug tracing.
	 * @return false
	 */
	public boolean traceOff() {
		debug = false;
		return false;
	}
	/**
	 * Always returns true.
	 * @return true
	 */
	public static boolean TRUE() {
		return true;
	}
	/**
	 * Always returns false.
	 * @return false
	 */
	public static boolean FALSE() {
		return false;
	}
	/**
	 * Condition method to check spot condition.
	 * @param spot destination spot
	 * @return true if it is at destination spot
	 */
	public static Condition<Role> isSpot(final SpotVal spot) {
		return new Condition<Role>() {
			private static final long serialVersionUID = -5642611521837451290L;
			public boolean is(Role role) throws Exception {
				return role.getSpot() == spot.getSpot(role);
			}
		};
	}
	/**
	 * Condition method to check spot variable condition.
	 * @param spot destination spot
	 * @param key spot variable name
	 * @return true if spot variable equals to destination spot.
	 */
	public boolean isSpot(SpotVal spot, String key) {
		return spot.getSpot(this) == getSelf().getEquip(key);
	}
	/**
	 * Command method to set spot variable.
	 * @param spot destination spot
	 * @param key spot variable name
	 */
	public void setSpot(SpotVal spot, String key) {
		getSelf().setEquip(key, spot.getSpot(this));
	}
	/**
	 * Command method to set time variable.
	 * @param spot spot of time variable
	 * @param equality formula to set time
	 * @return compiled command
	 */
	public static Command<Role> setTime(SpotVal spot, String equality) {
		return spotCommand(spot, setTimeImpl(equality));
	}
	/**
	 * Condition method to check time condition.
	 * @param spot spot of time variable
	 * @param time time condition formula
	 * @return compiled condition
	 */
	public static Condition<Role> isTime(SpotVal spot, String time) {
		return spotCondition(spot, isTimeImpl(time));
	}
	/**
	 * Command method to define keyword property.
	 * @param spot spot of keyword property
	 * @param equality initialization formula
	 */
	public void keyword(SpotVal spot, String equality) {
		keywordImpl(spot.getSpot(this), equality);
	}
	/**
	 * Command method to set keyword property.
	 * @param spot spot of keyword property
	 * @param equality setting formula
	 * @return compiled command
	 */
	public static Command<Role> set(SpotVal spot, String equality) {
		return spotCommand(spot, setImpl(equality));
	}
	/**
	 * Command method to copy keyword property from spot to agent.
	 * @param spot source spot
	 * @param equality setting formula
	 */
	public void get(SpotVal spot, String equality) {
		String[] pair = splitPair(equality, "=");
		getSelf().setProp(pair[0], spot.getSpot(this).getProp(pair[pair.length - 1]));
	}
	/**
	 * Command method to copy keyword property from agent to spot.
	 * @param spot destination spot
	 * @param equality setting formula
	 */
	public void put(SpotVal spot, String equality) {
		String[] pair = splitPair(equality, "=");
		spot.getSpot(this).setProp(pair[0], getSelf().getProp(pair[pair.length - 1]));
	}
	/**
	 * Command method to copy keyword property of spot.
	 * @param spot spot of keyword property
	 * @param equality setting formula
	 */
	public void copy(SpotVal spot, String equality) {
		copyImpl(spot.getSpot(this), equality);
	}
	/**
	 * Condition method to check keyword condition.
	 * @param spot spot of keyword property
	 * @param equality keyword condition formula
	 * @return true if equation satisfied
	 */
	public boolean equals(SpotVal spot, String equality) {
		return equalsImpl(spot.getSpot(this), equality);
	}
	/**
	 * Condition method to check keyword condition.
	 * @param spot spot of keyword property
	 * @param equality keyword condition formula
	 * @return true if equation satisfied
	 */
	public static Condition<Role> is(SpotVal spot, String equality) {
		return spotCondition(spot, isImpl(equality));
	}
	/**
	 * Command method to copy object attribute to string property.
	 * @param spot spot of object attribute
	 * @param equality setting formula
	 */
	public void equip(SpotVal spot, String equality) {
		equipImpl(spot.getSpot(this), equality);
	}
	/**
	 * Command method to create object attribute.
	 * @param spot spot of object attribute
	 * @param equality setting formula
	 * @throws InstantiationException
	 * @throws IllegalAccessException
	 * @throws ClassNotFoundException
	 */
	public void setEquip(SpotVal spot, String equality) throws InstantiationException, IllegalAccessException, ClassNotFoundException {
		setEquipImpl(spot.getSpot(this), equality);
	}
	/**
	 * Command method to copy object attribute from spot to agent.
	 * @param spot source spot
	 * @param equality setting formula
	 * @return compiled command
	 */
	public static Command<Role> getEquip(final SpotVal spot, String equality) {
		final String[] pair = splitPair(equality, "=");
		return new Command<Role>() {
			private static final long serialVersionUID = -8441725632516196445L;
			public void invoke(Role role) throws Exception {
				role.getSelf().setEquip(pair[0], spot.getSpot(role).getEquip(pair[pair.length - 1]));
			}
		};
	}
	/**
	 * Command method to copy object attribute from agent to spot.
	 * @param spot destination spot
	 * @param equality setting formula
	 * @return compiled command
	 */
	public static Command<Role> putEquip(final SpotVal spot, String equality) {
		final String[] pair = splitPair(equality, "=");
		return new Command<Role>() {
			private static final long serialVersionUID = 2687146734429539569L;
			public void invoke(Role role) throws Exception {
				spot.getSpot(role).setEquip(pair[0], role.getSelf().getEquip(pair[pair.length - 1]));
			}
		};
	}
	/**
	 * Command method to copy object attribute.
	 * @param spot spot of object attribute
	 * @param equality setting formula
	 */
	public void copyEquip(SpotVal spot, String equality) {
		copyEquipImpl(spot.getSpot(this), equality);
	}
	/**
	 * Command method to create clone of object attribute.
	 * @param spot spot of object attribute
	 * @param equality setting formula
	 * @return compiled command
	 * @throws Exception
	 */
	public static Command<Role> cloneEquip(SpotVal spot, String equality) throws Exception {
		return spotCommand(spot, cloneEquipImpl(equality));
	}
	/**
	 * Condition method to compare object attributes.
	 * @param spot spot of object attributes
	 * @param equality comparison formula
	 * @return compiled condition
	 */
	public boolean equalsEquip(SpotVal spot, String equality) {
		return equalsEquipImpl(spot.getSpot(this), equality);
	}
	/**
	 * Condition method to check class of object attribute.
	 * @param spot spot of object attribute
	 * @param equality condition formula
	 * @return compiled condition
	 * @throws ClassNotFoundException
	 */
	public boolean isEquip(SpotVal spot, String equality) throws ClassNotFoundException {
		return isEquipImpl(spot.getSpot(this), equality);
	}
	/**
	 * Command method to print string expression of object attribute to string property.
	 * @param spot spot of object attribute
	 * @param equality setting formula
	 */
	public void printEquip(SpotVal spot, String equality) {
		printEquipImpl(spot.getSpot(this), equality);
	}
	/**
	 * Command method to assign alias of object attribute to string property.
	 * @param spot spot of object attribute
	 * @param equality setting formula
	 */
	public void logEquip(SpotVal spot, String equality) {
		logEquipImpl(spot.getSpot(this), equality);
	}
	/**
	 * Condition method to send string message to object attribute.
	 * @param spot spot of object attribute
	 * @param message message string
	 * @return compiled condition
	 */
	public static Condition<Role> askEquip(final SpotVal spot, String message) {
		final Askable<Role> askable = EquippedObject.ask(message);
		return new Condition<Role>() {
			private static final long serialVersionUID = -1321835827034334624L;
			public boolean is(Role role) throws Exception {
				return askable.ask(role, spot.getSpot(role));
			}
		};
	}
	/**
	 * Command method to set role variable.
	 * @param spot spot of role variable
	 * @param equality setting formula
	 */
	public void setRole(SpotVal spot, String equality) {
		String[] pair = splitPair(equality, "=");
		RoleType roleType = pair.length == 1 ? getRoleType() : RoleType.forName(pair[1]);
		if (roleType == null) {
			throw new RuntimeException("Wrong Role Type - setRole " + equality);
		}
		spot.getSpot(this).setEquip(ROLE_KEY_HEAD + pair[0], roleType);
	}
	/**
	 * Condition method to check role variable.
	 * @param spot spot of role variable
	 * @param equality condition formula
	 * @return true if condition is satisfied
	 */
	public boolean isRole(SpotVal spot, String equality) {
		String[] pair = splitPair(equality, "=");
		RoleType roleType = RoleType.forName(pair[pair.length - 1]);
		if (roleType == null) {
			throw new RuntimeException("Wrong Role Type - isRole " + equality);
		}
		return roleType == (pair.length == 1 ? getRoleType() : spot.getSpot(this).getEquip(ROLE_KEY_HEAD + pair[0]));
	}
	/**
	 * Command method to create class variable.
	 * @param spot spot of class variable
	 * @param equality setting formula
	 * @throws ClassNotFoundException
	 */
	public void setClass(SpotVal spot, String equality) throws ClassNotFoundException {
		setClassImpl(spot.getSpot(this), equality);
	}
	/**
	 * Command method to copy instance of class variable.
	 * @param spot spot of class variable
	 * @param equality setting formula
	 */
	public void copyInstance(SpotVal spot, String equality) {
		copyInstanceImpl(spot.getSpot(this), equality);
	}
	/**
	 * Command method to add parameter to class variable.
	 * @param spot spot of class variable
	 * @param equality setting formula
	 * @throws ClassNotFoundException
	 */
	public void addParam(SpotVal spot, String equality) throws ClassNotFoundException {
		addParamImpl(spot.getSpot(this), equality);
	}
	/**
	 * Command method to add boolean parameter to class variable.
	 * @param spot spot of class variable
	 * @param equality setting formula
	 * @throws ClassNotFoundException
	 */
	public void addParamBoolean(SpotVal spot, String equality) throws ClassNotFoundException {
		addParamBooleanImpl(spot.getSpot(this), equality);
	}
	/**
	 * Command method to add string parameter to class variable.
	 * @param spot spot of class variable
	 * @param equality setting formula
	 */
	public void addParamString(SpotVal spot, String equality) {
		addParamStringImpl(spot.getSpot(this), equality);
	}
	/**
	 * Command method to add class parameter to class variable.
	 * @param spot spot of class variable
	 * @param equality setting formula
	 * @throws ClassNotFoundException
	 */
	public void addParamClass(SpotVal spot, String equality) throws ClassNotFoundException {
		addParamClassImpl(spot.getSpot(this), equality);
	}
	/**
	 * Command method to add agent parameter to class variable.
	 * @param spot spot of class variable
	 * @param equality setting formula
	 */
	public void addParamAgent(SpotVal spot, String equality) {
		String[] pair = equality.split("=", 2);
		spot.getSpot(this).<Invoker>getEquip(pair[0].trim()).addParameter(pair.length == 1 ? getSelf() : Agent.forName(pair[1]), Agent.class);
	}
	/**
	 * Command method to add spot parameter to class variable.
	 * @param spot spot of class variable
	 * @param equality setting formula
	 */
	public void addParamSpot(SpotVal spot, String equality) {
		String[] pair = equality.split("=", 2);
		spot.getSpot(this).<Invoker>getEquip(pair[0].trim()).addParameter(pair.length == 1 ? getSpot() : Spot.forName(pair[1]), Spot.class);
	}
	/**
	 * Command method to assign type of parameter in class variable.
	 * @param spot spot of class variable
	 * @param equality setting formula
	 * @throws ClassNotFoundException
	 */
	public void setParamType(SpotVal spot, String equality) throws ClassNotFoundException {
		setParamTypeImpl(spot.getSpot(this), equality);
	}
	/**
	 * Command method to create new instance in class variable.
	 * @param spot spot of class variable
	 * @param equality setting formula
	 * @throws IllegalArgumentException
	 * @throws SecurityException
	 * @throws InstantiationException
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 * @throws NoSuchMethodException
	 */
	public void newInstance(SpotVal spot, String equality) throws IllegalArgumentException, SecurityException, InstantiationException, IllegalAccessException, InvocationTargetException, NoSuchMethodException {
		newInstanceImpl(spot.getSpot(this), equality);
	}
	/**
	 * Condition method to invoke method in class variable.
	 * @param spot spot of class variable
	 * @param equality setting variable
	 * @return false if result is null or false
	 * @throws IllegalArgumentException
	 * @throws SecurityException
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 * @throws NoSuchMethodException
	 */
	public boolean invokeClass(SpotVal spot, String equality) throws IllegalArgumentException, SecurityException, IllegalAccessException, InvocationTargetException, NoSuchMethodException {
		return invokeClassImpl(spot.getSpot(this), equality);
	}
	/**
	 * Command method to get field value of class variable.
	 * @param spot spot of class variable
	 * @param equality setting formula
	 * @throws IllegalArgumentException
	 * @throws SecurityException
	 * @throws IllegalAccessException
	 * @throws NoSuchFieldException
	 */
	public void getField(SpotVal spot, String equality) throws IllegalArgumentException, SecurityException, IllegalAccessException, NoSuchFieldException {
		getFieldImpl(spot.getSpot(this), equality);
	}
	/**
	 * Command method to set field value of class variable.
	 * @param spot spot of class variable
	 * @param equality setting formula
	 * @throws IllegalArgumentException
	 * @throws SecurityException
	 * @throws IllegalAccessException
	 * @throws NoSuchFieldException
	 */
	public void setField(SpotVal spot, String equality) throws IllegalArgumentException, SecurityException, IllegalAccessException, NoSuchFieldException {
		setFieldImpl(spot.getSpot(this), equality);
	}
	/**
	 * Condition method to check if collection variable is empty.
	 * @param spot spot of collection variable
	 * @param collection collection variable name
	 * @return true if collection variable is empty
	 */
	public boolean isEmpty(SpotVal spot, String collection) {
		return isEmptyImpl(spot.getSpot(this), collection);
	}
	/**
	 * Condition method to add agent to collection variable. 
	 * @param spot spot of collection variable
	 * @param equality setting formula
	 * @return true if collection is changed
	 */
	public boolean addAgent(SpotVal spot, String equality) {
		return addAgentImpl(spot.getSpot(this), equality);
	}
	/**
	 * Condition method to add spot to collection variable.
	 * @param spot spot of collection variable
	 * @param equality setting formula
	 * @return true if collection is changed
	 */
	public boolean addSpot(SpotVal spot, String equality) {
		return addSpotImpl(spot.getSpot(this), equality);
	}
	/**
	 * Condition method to add object attribute value to collection variable.
	 * @param spot spot of collection variable
	 * @param equality setting formula
	 * @return true if collection is changed
	 */
	public boolean addEquip(SpotVal spot, String equality) {
		return addEquipImpl(spot.getSpot(this), equality);
	}
	/**
	 * Condition method to add string value to collection variable.
	 * @param spot spot of collection variable
	 * @param equality setting formula
	 * @return true if collection is changed
	 */
	public boolean addString(SpotVal spot, String equality) {
		return addStringImpl(spot.getSpot(this), equality);
	}
	/**
	 * Command method to add agent to list variable as first element. 
	 * @param spot spot of list variable
	 * @param equality setting formula
	 */
	public void addFirstAgent(SpotVal spot, String equality) {
		addFirstAgentImpl(spot.getSpot(this), equality);
	}
	/**
	 * Command method to add spot to list variable as first element. 
	 * @param spot spot of list variable
	 * @param equality setting formula
	 */
	public void addFirstSpot(SpotVal spot, String equality) {
		addFirstSpotImpl(spot.getSpot(this), equality);
	}
	/**
	 * Command method to add object attribute value to list variable as first element.
	 * @param spot spot of list variable
	 * @param equality setting formula
	 */
	public void addFirstEquip(SpotVal spot, String equality) {
		addFirstEquipImpl(spot.getSpot(this), equality);
	}
	/**
	 * Command method to add string value to list variable as first element.
	 * @param spot spot of list variable
	 * @param equality setting formula
	 */
	public void addFirstString(SpotVal spot, String equality) {
		addFirstStringImpl(spot.getSpot(this), equality);
	}
	/**
	 * Command method to add agent to list variable as last element. 
	 * @param spot spot of list variable
	 * @param equality setting formula
	 */
	public void addLastAgent(SpotVal spot, String equality) {
		addLastAgentImpl(spot.getSpot(this), equality);
	}
	/**
	 * Command method to add spot to list variable as last element. 
	 * @param spot spot of list variable
	 * @param equality setting formula
	 */
	public void addLastSpot(SpotVal spot, String equality) {
		addLastSpotImpl(spot.getSpot(this), equality);
	}
	/**
	 * Command method to add object attribute value to list variable as last element.
	 * @param spot spot of list variable
	 * @param equality setting formula
	 */
	public void addLastEquip(SpotVal spot, String equality) {
		addLastEquipImpl(spot.getSpot(this), equality);
	}
	/**
	 * Command method to add string value to list variable as last element.
	 * @param spot spot of list variable
	 * @param equality setting formula
	 */
	public void addLastString(SpotVal spot, String equality) {
		addLastStringImpl(spot.getSpot(this), equality);
	}
	/**
	 * Condition method to merge collection variables. 
	 * @param spot spot of collection variables
	 * @param equality setting formula
	 * @return true if destination collection is changed
	 */
	public boolean addAll(SpotVal spot, String equality) {
		return addAllImpl(spot.getSpot(this), equality);
	}
	/**
	 * Condition method to check if agent is contained in collection variable.
	 * @param spot spot of collection variable
	 * @param equality condition formula
	 * @return true if agent is contained in collection variable
	 */
	public boolean containsAgent(SpotVal spot, String equality) {
		return containsAgentImpl(spot.getSpot(this), equality);
	}
	/**
	 * Condition method to check if spot is contained in collection variable.
	 * @param spot spot of collection variable
	 * @param equality condition formula
	 * @return true if spot is contained in collection variable
	 */
	public boolean containsSpot(SpotVal spot, String equality) {
		return containsSpotImpl(spot.getSpot(this), equality);
	}
	/**
	 * Condition method to check if object attribute value is contained in collection variable.
	 * @param spot spot of collection variable
	 * @param equality condition formula
	 * @return true if object attribute value is contained in collection variable
	 */
	public boolean containsEquip(SpotVal spot, String equality) {
		return containsEquipImpl(spot.getSpot(this), equality);
	}
	/**
	 * Condition method to check if string value is contained in collection variable.
	 * @param spot spot of collection variable
	 * @param equality condition formula
	 * @return true if string value is contained in collection variable
	 */
	public boolean containsString(SpotVal spot, String equality) {
		return containsStringImpl(spot.getSpot(this), equality);
	}
	/**
	 * Condition method to check if first element of list variable is specified agent.
	 * @param spot spot of list variable
	 * @param equality condition formula
	 * @return true if first element is specified agent
	 */
	public boolean isFirstAgent(SpotVal spot, String equality) {
		return isFirstAgentImpl(spot.getSpot(this), equality);
	}
	/**
	 * Condition method to check if first element of list variable is specified spot.
	 * @param spot spot of list variable
	 * @param equality condition formula
	 * @return true if first element is specified spot
	 */
	public boolean isFirstSpot(SpotVal spot, String equality) {
		return isFirstSpotImpl(spot.getSpot(this), equality);
	}
	/**
	 * Condition method to check if first element of list variable is specified object value.
	 * @param spot spot of list variable
	 * @param equality condition formula
	 * @return true if first element is specified object value
	 */
	public boolean isFirstEquip(SpotVal spot, String equality) {
		return isFirstEquipImpl(spot.getSpot(this), equality);
	}
	/**
	 * Condition method to check if first element of list variable is specified string value.
	 * @param spot spot of list variable
	 * @param equality condition formula
	 * @return true if first element is specified string value
	 */
	public boolean isFirstString(SpotVal spot, String equality) {
		return isFirstStringImpl(spot.getSpot(this), equality);
	}
	/**
	 * Condition method to check if last element of list variable is specified agent.
	 * @param spot spot of list variable
	 * @param equality condition formula
	 * @return true if last element is specified agent
	 */
	public boolean isLastAgent(SpotVal spot, String equality) {
		return isLastAgentImpl(spot.getSpot(this), equality);
	}
	/**
	 * Condition method to check if last element of list variable is specified spot.
	 * @param spot spot of list variable
	 * @param equality condition formula
	 * @return true if last element is specified spot
	 */
	public boolean isLastSpot(SpotVal spot, String equality) {
		return isLastSpotImpl(spot.getSpot(this), equality);
	}
	/**
	 * Condition method to check if last element of list variable is specified object value.
	 * @param spot spot of list variable
	 * @param equality condition formula
	 * @return true if last element is specified object value
	 */
	public boolean isLastEquip(SpotVal spot, String equality) {
		return isLastEquipImpl(spot.getSpot(this), equality);
	}
	/**
	 * Condition method to check if last element of list variable is specified string value.
	 * @param spot spot of list variable
	 * @param equality condition formula
	 * @return true if last element is specified string value
	 */
	public boolean isLastString(SpotVal spot, String equality) {
		return isLastStringImpl(spot.getSpot(this), equality);
	}
	/**
	 * Condition method to check if minimum element of sorted set variable is specified agent.
	 * @param spot spot of sorted set variable
	 * @param equality condition formula
	 * @return true if minimum element is specified agent
	 */
	public boolean isMinAgent(SpotVal spot, String equality) {
		return isMinAgentImpl(spot.getSpot(this), equality);
	}
	/**
	 * Condition method to check if minimum element of sorted set variable is specified spot.
	 * @param spot spot of sorted set variable
	 * @param equality condition formula
	 * @return true if minimum element is specified spot
	 */
	public boolean isMinSpot(SpotVal spot, String equality) {
		return isMinSpotImpl(spot.getSpot(this), equality);
	}
	/**
	 * Condition method to check if minimum element of sorted set variable is specified object value.
	 * @param spot spot of sorted set variable
	 * @param equality condition formula
	 * @return true if minimum element is specified object value
	 */
	public boolean isMinEquip(SpotVal spot, String equality) {
		return isMinEquipImpl(spot.getSpot(this), equality);
	}
	/**
	 * Condition method to check if minimum element of sorted set variable is specified string value.
	 * @param spot spot of sorted set variable
	 * @param equality condition formula
	 * @return true if minimum element is specified string value
	 */
	public boolean isMinString(SpotVal spot, String equality) {
		return isMinStringImpl(spot.getSpot(this), equality);
	}
	/**
	 * Condition method to check if maximum element of sorted set variable is specified agent.
	 * @param spot spot of sorted set variable
	 * @param equality condition formula
	 * @return true if maximum element is specified agent
	 */
	public boolean isMaxAgent(SpotVal spot, String equality) {
		return isMaxAgentImpl(spot.getSpot(this), equality);
	}
	/**
	 * Condition method to check if maximum element of sorted set variable is specified spot.
	 * @param spot spot of sorted set variable
	 * @param equality condition formula
	 * @return true if maximum element is specified spot
	 */
	public boolean isMaxSpot(SpotVal spot, String equality) {
		return isMaxSpotImpl(spot.getSpot(this), equality);
	}
	/**
	 * Condition method to check if maximum element of sorted set variable is specified object value.
	 * @param spot spot of sorted set variable
	 * @param equality condition formula
	 * @return true if maximum element is specified object value
	 */
	public boolean isMaxEquip(SpotVal spot, String equality) {
		return isMaxEquipImpl(spot.getSpot(this), equality);
	}
	/**
	 * Condition method to check if maximum element of sorted set variable is specified string value.
	 * @param spot spot of sorted set variable
	 * @param equality condition formula
	 * @return true if maximum element is specified string value
	 */
	public boolean isMaxString(SpotVal spot, String equality) {
		return isMaxStringImpl(spot.getSpot(this), equality);
	}
	/**
	 * Condition method to check if collection variable includes other collection.
	 * @param spot spot of collection variable
	 * @param equality condition formula
	 * @return true if collection variable includes other collection.
	 */
	public boolean containsAll(SpotVal spot, String equality) {
		return containsAllImpl(spot.getSpot(this), equality);
	}
	/**
	 * Condition method to remove agent from collection variable.
	 * @param spot spot of collection variable
	 * @param equality setting formula
	 * @return true if agent is removed
	 */
	public boolean removeAgent(SpotVal spot, String equality) {
		return removeAgentImpl(spot.getSpot(this), equality);
	}
	/**
	 * Condition method to remove spot from collection variable.
	 * @param spot spot of collectionvariable
	 * @param equality setting formula
	 * @return true if spot is removed
	 */
	public boolean removeSpot(SpotVal spot, String equality) {
		return removeSpotImpl(spot.getSpot(this), equality);
	}
	/**
	 * Condition method to remove object from collection variable.
	 * @param spot spot of collectionvariable
	 * @param equality setting formula
	 * @return true if object is removed
	 */
	public boolean removeEquip(SpotVal spot, String equality) {
		return removeEquipImpl(spot.getSpot(this), equality);
	}
	/**
	 * Condition method to remove string from collection variable.
	 * @param spot spot of collectionvariable
	 * @param equality setting formula
	 * @return true if string is removed
	 */
	public boolean removeString(SpotVal spot, String equality) {
		return removeStringImpl(spot.getSpot(this), equality);
	}
	/**
	 * Command method to copy one element from collection variable to object attribute.
	 * @param spot spot of collection variable
	 * @param equality setting formula
	 */
	public void equipOne(SpotVal spot, String equality) {
		equipOneImpl(spot.getSpot(this), equality);
	}
	/**
	 * Command method to remove one element from collection variable.
	 * @param spot spot of collection variable
	 * @param equality setting formula
	 */
	public void removeOne(SpotVal spot, String equality) {
		removeOneImpl(spot.getSpot(this), equality);
	}
	/**
	 * Command method to copy randomly choosed one element from collection variable to object attribute.
	 * @param spot spot of collection variable
	 * @param equality setting formula
	 */
	public void equipRandomOne(SpotVal spot, String equality) {
		equipRandomOneImpl(spot.getSpot(this), equality);
	}
	/**
	 * Command method to remove randomly choosed one element from collection variable.
	 * @param spot spot of collection variable
	 * @param equality setting formula
	 */
	public void removeRandomOne(SpotVal spot, String equality) {
		removeRandomOneImpl(spot.getSpot(this), equality);
	}
	/**
	 * Command method to copy first element of list variable to object attribute.
	 * @param spot spot of list variable
	 * @param equality setting formula
	 */
	public void equipFirst(SpotVal spot, String equality) {
		equipFirstImpl(spot.getSpot(this), equality);
	}
	/**
	 * Command method to remove first element of list variable.
	 * @param spot spot of list variable
	 * @param equality setting formula
	 */
	public void removeFirst(SpotVal spot, String equality) {
		removeFirstImpl(spot.getSpot(this), equality);
	}
	/**
	 * Command method to copy last element of list variable to object attribute.
	 * @param spot spot of list variable
	 * @param equality setting formula
	 */
	public void equipLast(SpotVal spot, String equality) {
		equipLastImpl(spot.getSpot(this), equality);
	}
	/**
	 * Command method to remove last element of list variable.
	 * @param spot spot of list variable
	 * @param equality setting formula
	 */
	public void removeLast(SpotVal spot, String equality) {
		removeLastImpl(spot.getSpot(this), equality);
	}
	/**
	 * Command method to copy minimum element of sorted set variable to object attribute.
	 * @param spot spot of sorted set variable
	 * @param equality setting formula
	 */
	public void equipMin(SpotVal spot, String equality) {
		equipMinImpl(spot.getSpot(this), equality);
	}
	/**
	 * Command method to remove minimum element of sorted set variable.
	 * @param spot spot of sorted set variable
	 * @param equality setting formula
	 */
	public void removeMin(SpotVal spot, String equality) {
		removeMinImpl(spot.getSpot(this), equality);
	}
	/**
	 * Command method to copy maximum element of sorted set variable to object attribute.
	 * @param spot spot of sorted set variable
	 * @param equality setting formula
	 */
	public void equipMax(SpotVal spot, String equality) {
		equipMaxImpl(spot.getSpot(this), equality);
	}
	/**
	 * Command method to remove maximum element of sorted set variable.
	 * @param spot spot of sorted set variable
	 * @param equality setting formula
	 */
	public void removeMax(SpotVal spot, String equality) {
		removeMaxImpl(spot.getSpot(this), equality);
	}
	/**
	 * Condition method to remove all elements of collection variable.
	 * @param spot spot of collection variable
	 * @param equality condition formula
	 * @return true if collection variable is changed
	 */
	public boolean removeAll(SpotVal spot, String equality) {
		return removeAllImpl(spot.getSpot(this), equality);
	}
	/**
	 * Condition method to get intersection of collection variables.
	 * @param spot spot of collection variable
	 * @param equality condition formula
	 * @return true if collection variable is changed
	 */
	public boolean retainAll(SpotVal spot, String equality) {
		return retainAllImpl(spot.getSpot(this), equality);
	}
	/**
	 * Command method to reverse order of list variable.
	 * @param spot spot of list variable
	 * @param list list variable name
	 */
	public void reverseAll(SpotVal spot, String list) {
		reverseAllImpl(spot.getSpot(this), list);
	}
	/**
	 * Command method to rotate elements of list variable.
	 * @param spot spot of list variable
	 * @param equality setting formula
	 */
	public void rotateAll(SpotVal spot, String equality) {
		rotateAllImpl(spot.getSpot(this), equality);
	}
	/**
	 * Command method to shuffle elements of list variable.
	 * @param spot spot of list variable
	 * @param list list variable name
	 */
	public void shuffleAll(SpotVal spot, String list) {
		shuffleAllImpl(spot.getSpot(this), list);
	}
	/**
	 * Command method to sort elements of list variable.
	 * @param spot spot of list variable
	 * @param list list variable name
	 */
	public void sortAll(SpotVal spot, String list) {
		sortAllImpl(spot.getSpot(this), list);
	}
	/**
	 * Command method to set keyword value of all elements in collection variable.
	 * @param spot spot of collection variable
	 * @param equality setting formula
	 */
	public void setAll(SpotVal spot, String equality) {
		setAllImpl(spot.getSpot(this), equality);
	}
	/**
	 * Command method to send string message to all elements in collection variable.
	 * @param spot spot of collection variable
	 * @param equality setting formula
	 * @throws Exception
	 */
	public void askAll(SpotVal spot, String equality) throws Exception {
		askAllImpl(spot.getSpot(this), equality);
	}
	/**
	 * Command method to start rule action of all elements in collection variable.
	 * @param spot spot of collection variable
	 * @param equality setting formula
	 * @throws InstantiationException
	 * @throws IllegalAccessException
	 */
	public void startRuleAll(SpotVal spot, String equality) throws InstantiationException, IllegalAccessException {
		startRuleAllImpl(spot.getSpot(this), equality);
	}
	/**
	 * Command method to start default spot role action.
	 * @param spot spot to execute role action
	 * @throws InstantiationException
	 * @throws IllegalAccessException
	 */
	public void startRule(SpotVal spot) throws InstantiationException, IllegalAccessException {
		startRuleImpl(spot.getSpot(this));
	}
	/**
	 * Command method to start spot role action.
	 * @param spot spot to execute role action
	 * @param roleType role type name
	 * @throws InstantiationException
	 * @throws IllegalAccessException
	 */
	public void startRule(SpotVal spot, String roleType) throws InstantiationException, IllegalAccessException {
		startRuleImpl(spot.getSpot(this), roleType);
	}
	/**
	 * Command method to stop role action.
	 */
	public void stopRule() {
		new Resolver() {
			private static final long serialVersionUID = 3955283635638630974L;
			public void resolve() {
				if (!Environment.getCurrent().removeSpotRule(RoleForSpots.this)) {
					throw new RuntimeException("Illegal Spot Rule - stopRule");
				}
			}
		}.requestResolve();
	}
	/**
	 * Command method to terminate simulation.
	 */
	public static void terminate() {
		Environment.getCurrent().getStepCounter().terminate();
	}
	/**
	 * Command method to specify next stage.
	 * @param stage stage name
	 * @return compiled command
	 */
	public static Command<Role> nextStage(String stage) {
		final int next = Environment.getCurrent().getStepCounter().getStageNames().indexOf(stage);
		if (next < 0) {
			throw new RuntimeException("Wrong Stage Name - " + stage);
		}
		return new Command<Role>() {
			private static final long serialVersionUID = 8681375900828567890L;
			public void invoke(Role role) throws Exception {
				Environment.getCurrent().getStepCounter().setNextStage(next);
			}
		};
	}
	/**
	 * Command method to create new agent.
	 * @param equality setting formula
	 */
	public void createAgent(String equality) {
		createAgent(null, equality);
	}
	/**
	 * Command method to create new agent at specified spot.
	 * @param spot spot to create new agent
	 * @param equality setting formula
	 */
	public void createAgent(final SpotVal spot, String equality) {
		String[] trio = equality.split("=", 3);
		final String nameHead = trio[0].trim();
		final int number = trio.length >= 2 ? Integer.parseInt(trio[1].trim()) : 1;
		final RoleType roleType = trio.length >= 3 ? RoleType.forName(trio[2].trim()) : null;
		new Resolver() {
			private static final long serialVersionUID = -1536656787544987148L;
			public void resolve() throws InstantiationException, IllegalAccessException {
				int n = number;
				while (n-- > 0) {
					Agent agent = new Agent();
					agent.assignName(nameHead);
					if (roleType != null) {
						agent.setActiveRole(roleType.createRole(agent));
					}
					if (spot != null) {
						agent.setSpot(spot.getSpot(RoleForSpots.this));
					}
				}
			}
		}.requestResolve();
	}
	/**
	 * Command method to move all agents in collection variable.
	 * @param spot spot to move
	 * @param collection collection variable name
	 */
	public void moveToAll(SpotVal spot, String collection) {
		Iterator<Agent> it = getSelf().<Collection<Agent>>getEquip(collection).iterator();
		while (it.hasNext()) {
			it.next().setSpot(spot.getSpot(this));
		}
	}
	static void detachAllImpl(EquippedObject self, String collection) {
		Iterator<Agent> it = self.<Collection<Agent>>getEquip(collection).iterator();
		while (it.hasNext()) {
			it.next().detach();
		}
	}
	/**
	 * Command method to detach all agents in collection variable.
	 * @param spot spot of collection variable
	 * @param collection collection variable name
	 */
	public void detachAll(SpotVal spot, String collection) {
		detachAllImpl(spot.getSpot(this), collection);
	}
	static void detachOneImpl(EquippedObject self, String collection) {
		((Agent) ((Collection<?>) self.getEquip(collection)).iterator().next()).detach();
	}
	static void detachRandomOneImpl(EquippedObject self, String collection) {
		Collection<Agent> c = self.<Collection<Agent>>getEquip(collection);
		if (c.isEmpty()) {
			return;
		}
		Iterator<Agent> iterator = c.iterator();
		Agent agent;
		int i = Environment.getCurrent().getRandom().nextInt(c.size()) + 1;
		do {
			agent = iterator.next();
		} while (--i > 0);
		agent.detach();
	}
	static void detachFirstImpl(EquippedObject self, String list) {
		List<Agent> l = self.<List<Agent>>getEquip(list);
		if (l.isEmpty()) {
			return;
		}
		l.get(0).detach();
	}
	static void detachLastImpl(EquippedObject self, String list) {
		List<Agent> l = self.<List<Agent>>getEquip(list);
		if (l.isEmpty()) {
			return;
		}
		l.get(l.size() - 1).detach();
	}
	static void detachMinImpl(EquippedObject self, String list) {
		SortedSet<Agent> set = self.<SortedSet<Agent>>getEquip(list);
		if (set.isEmpty()) {
			return;
		}
		set.first().detach();
	}
	static void detachMaxImpl(EquippedObject self, String list) {
		SortedSet<Agent> set = self.<SortedSet<Agent>>getEquip(list);
		if (set.isEmpty()) {
			return;
		}
		set.last().detach();
	}
	/**
	 * Command method to detach one agent in collection variable.
	 * @param spot spot of collection variable
	 * @param collection collection variable name
	 */
	public void detachOne(SpotVal spot, String collection) {
		detachOneImpl(spot.getSpot(this), collection);
	}
	/**
	 * Command method to detach randomly one agent in collection variable.
	 * @param spot spot of collection variable
	 * @param collection collection variable name
	 */
	public void detachRandomOne(SpotVal spot, String collection) {
		detachRandomOneImpl(spot.getSpot(this), collection);
	}
	/**
	 * Command method to detach first agent in list variable.
	 * @param spot spot of list variable
	 * @param list list variable name
	 */
	public void detachFirst(SpotVal spot, String list) {
		detachFirstImpl(spot.getSpot(this), list);
	}
	/**
	 * Command method to detach last agent in list variable.
	 * @param spot spot of list variable
	 * @param list list variable name
	 */
	public void detachLast(SpotVal spot, String list) {
		detachLastImpl(spot.getSpot(this), list);
	}
	/**
	 * Command method to detach minimum agent in sorted set variable.
	 * @param spot spot of sorted set variable
	 * @param list sorted set variable name
	 */
	public void detachMin(SpotVal spot, String list) {
		detachMinImpl(spot.getSpot(this), list);
	}
	/**
	 * Command method to detach maximum agent in sorted set variable.
	 * @param spot spot of sorted set variable
	 * @param list sorted set variable name
	 */
	public void detachMax(SpotVal spot, String list) {
		detachMaxImpl(spot.getSpot(this), list);
	}
	/**
	 * Condition method to invoke method of object attribute.
	 * @param spot spot of object attribute
	 * @param formula condition formula
	 * @return result of method calling
	 * @throws Exception
	 */
	public boolean invokeEquip(SpotVal spot, String formula) throws Exception {
		return invokeEquipImpl(spot.getSpot(this), formula);
	}
	/**
	 * Command method to clear elements of map variable.
	 * @param spot spot of map variable
	 * @param map map variable name
	 */
	public void clearMap(SpotVal spot, String map) {
		clearMapImpl(spot.getSpot(this), map);
	}
	/**
	 * Condition method to check if map variable is empty.
	 * @param spot spot of map variable
	 * @param map map variable name
	 * @return true if map variable is empty
	 */
	public boolean isEmptyMap(SpotVal spot, String map) {
		return isEmptyMapImpl(spot.getSpot(this), map);
	}
	/**
	 * Condition method to check if map variable contains specified key.
	 * @param spot spot of map variable
	 * @param equality condition formula
	 * @return true if map variable contains specified key
	 */
	public boolean containsKey(SpotVal spot, String equality) {
		return containsKeyImpl(spot.getSpot(this), equality);
	}
	/**
	 * Condition method to check if map variable contains specified value.
	 * @param spot spot of map variable
	 * @param equality condition formula
	 * @return true if map variable contains specified value
	 */
	public boolean containsValue(SpotVal spot, String equality) {
		return containsValueImpl(spot.getSpot(this), equality);
	}
	/**
	 * Command method to get key collection of map variable.
	 * @param spot spot of map variable
	 * @param equality setting formula
	 */
	public void equipMapKeys(SpotVal spot, String equality) {
		equipMapKeysImpl(spot.getSpot(this), equality);
	}
	/**
	 * Command method to get value collection of map variable.
	 * @param spot spot of map variable
	 * @param equality setting formula
	 */
	public void equipMapValues(SpotVal spot, String equality) {
		equipMapValuesImpl(spot.getSpot(this), equality);
	}
	/**
	 * Command method to get value of map variable.
	 * @param spot spot of map variable
	 * @param equality setting formula
	 */
	public void getMap(SpotVal spot, String equality) {
		getMapImpl(spot.getSpot(this), equality);
	}
	/**
	 * Command method to remove mapping from map variable.
	 * @param spot spot of map variable
	 * @param equality setting formula
	 */
	public void removeMap(SpotVal spot, String equality) {
		removeMapImpl(spot.getSpot(this), equality);
	}
	/**
	 * Condition method to check mapping value of map variable.
	 * @param spot spot of map variable
	 * @param equality condition formula
	 * @return true if mapping value equals to specified object
	 */
	public boolean equalsMap(SpotVal spot, String equality) {
		return equalsMapImpl(spot.getSpot(this), equality);
	}
	/**
	 * Command method to put value of map variable.
	 * @param spot destination spot
	 * @param equality setting formula
	 */
	public void putMap(SpotVal spot, String equality) {
		putMapImpl(spot.getSpot(this), equality);
	}
	/**
	 * Command method to merge map variables.
	 * @param spot spot of map variables
	 * @param equality setting formula
	 */
	public void putMapAll(SpotVal spot, String equality) {
		putMapAllImpl(spot.getSpot(this), equality);
	}
}
