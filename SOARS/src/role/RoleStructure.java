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
import env.Agent;
import env.Environment;
import env.EquippedObject;
import env.Spot;
import env.SpotVal;

/**
 * The RoleStructure class implements agent role methods.
 * @author H. Tanuma / SOARS project
 */
public class RoleStructure extends RoleForSpots {

	private static final long serialVersionUID = -7456857617493839640L;

	/**
	 * Command method to move agent to specified spot.
	 * @param spot destination spot
	 * @return compiled command
	 */
	public static Command<Role> moveTo(final SpotVal spot) {
		return new Command<Role>() {
			private static final long serialVersionUID = 9205471569597784956L;
			public void invoke(Role role) throws Exception {
				role.getAgent().setSpot(spot.getSpot(role));
			}
		};
	}
	/**
	 * Command method to attach agent to other agent.
	 * @param agent destination agent name
	 */
	public void attachTo(String agent) {
		getAgent().attachTo(Agent.forName(agent));
	}
	/**
	 * Command method to detach from other agent.
	 */
	public void detach() {
		getAgent().detach();
	}
	/**
	 * Command method to set time variable.
	 * @param equality setting formula
	 * @return compiled command
	 */
	public static Command<Role> setTime(String equality) {
		return selfCommand(setTimeImpl(equality));
	}
	/**
	 * Condition method to check if specified time has elapsed.
	 * @param time time formula
	 * @return compiled condition
	 */
	public static Condition<Role> isTime(String time) {
		return selfCondition(isTimeImpl(time));
	}
	/**
	 * Command method to define keyword variable.
	 * @param equality setting formula
	 */
	public void keyword(String equality) {
		keywordImpl(getAgent(), equality);
	}
	/**
	 * Command method to set keyword variable.
	 * @param equality setting formula
	 * @return compiled command
	 */
	public static Command<Role> set(String equality) {
		return selfCommand(setImpl(equality));
	}
	/**
	 * Command method to copy string property.
	 * @param equality setting formula
	 */
	public void copy(String equality) {
		copyImpl(getAgent(), equality);
	}
	/**
	 * Condition method to compare string properties.
	 * @param equality condition formula
	 * @return true if string properties agree
	 */
	public boolean equals(String equality) {
		return equalsImpl(getAgent(), equality);
	}
	/**
	 * Condition method to check value of string property.
	 * @param equality condition formula
	 * @return true if string property agrees specified value
	 */
	public static Condition<Role> is(String equality) {
		return selfCondition(isImpl(equality));
	}
	/**
	 * Command method to copy string property to object attribute.
	 * @param equality setting formula
	 */
	public void equip(String equality) {
		equipImpl(getAgent(), equality);
	}
	/**
	 * Command method to set object attribute.
	 * @param equality setting formula
	 * @throws InstantiationException
	 * @throws IllegalAccessException
	 * @throws ClassNotFoundException
	 */
	public void setEquip(String equality) throws InstantiationException, IllegalAccessException, ClassNotFoundException {
		setEquipImpl(getAgent(), equality);
	}
	/**
	 * Command method to copy object attribute.
	 * @param equality setting formula
	 */
	public void copyEquip(String equality) {
		copyEquipImpl(getAgent(), equality);
	}
	/**
	 * Command method to create clone of object attribute.
	 * @param equality setting formula
	 * @return compiled command
	 * @throws Exception
	 */
	public static Command<Role> cloneEquip(String equality) throws Exception {
		return selfCommand(cloneEquipImpl(equality));
	}
	/**
	 * Condition method to compare object attributes.
	 * @param equality condition formula
	 * @return compiled condition
	 */
	public boolean equalsEquip(String equality) {
		return equalsEquipImpl(getAgent(), equality);
	}
	/**
	 * Condition method to check object attribute.
	 * @param equality condition formula
	 * @return true if condition is satisfied
	 * @throws ClassNotFoundException
	 */
	public boolean isEquip(String equality) throws ClassNotFoundException {
		return isEquipImpl(getAgent(), equality);
	}
	/**
	 * Command method to copy string expression of object attribute to string property.
	 * @param equality setting formula
	 */
	public void printEquip(String equality) {
		printEquipImpl(getAgent(), equality);
	}
	/**
	 * Command method to put alias of object attribute to string property.
	 * @param equality setting formula
	 */
	public void logEquip(String equality) {
		logEquipImpl(getAgent(), equality);
	}
	/**
	 * Condition method to send string message to object attribute.
	 * @param message string message
	 * @return compiled condition
	 */
	public static Condition<Role> askEquip(String message) {
		final Askable<Role> askable = EquippedObject.ask(message);
		return new Condition<Role>() {
			private static final long serialVersionUID = 8373633037057525449L;
			public boolean is(Role obj) throws Exception {
				return askable.ask(obj, obj.getSelf());
			}
		};
	}
	/**
	 * Command method to set role variable.
	 * @param equality setting formula
	 * @throws InstantiationException
	 * @throws IllegalAccessException
	 */
	public void setRole(String equality) throws InstantiationException, IllegalAccessException {
		String[] pair = splitPair(equality, "=");
		pair[0] = ROLE_KEY_HEAD + pair[0];
		if (pair.length == 1) {
			getAgent().setEquip(pair[0], this);
			return;
		}
		RoleType roleType = RoleType.forName(pair[1]);
		if (roleType == null) {
			throw new RuntimeException("Wrong Role Type - setRole " + equality);
		}
		getAgent().setEquip(pair[0], roleType.createRole(getAgent()));
	}
	/**
	 * Condition method to check role variable.
	 * @param equality condition method
	 * @return true if condition is satisfied.
	 */
	public boolean isRole(String equality) {
		String[] pair = splitPair(equality, "=");
		RoleType roleType = RoleType.forName(pair[pair.length - 1]);
		if (roleType == null) {
			throw new RuntimeException("Wrong Role Type - isRole " + equality);
		}
		return roleType == (pair.length == 1 ? getRoleType() : ((Role) getAgent().getEquip(ROLE_KEY_HEAD + pair[0])).getRoleType());
	}
	/**
	 * Condition method to check if active role of agent is null.
	 * @return true if active role of agent is null
	 */
	public boolean isRole() {
		return getAgent().getActiveRole() == null;
	}
	/**
	 * Command method to set active role type.
	 * @param role role type name or role variable name
	 * @throws InstantiationException
	 * @throws IllegalAccessException
	 */
	public void activateRole(String role) throws InstantiationException, IllegalAccessException {
		String roleKey = ROLE_KEY_HEAD + role;
		Role roleVal = (Role) getAgent().getEquip(roleKey);
		if (roleVal == null) {
			RoleType roleType = RoleType.forName(role);
			if (roleType == null) {
				throw new RuntimeException("Wrong Role Type - activateRole " + role);
			}
			roleVal = roleType.createRole(getAgent());
			getAgent().setEquip(roleKey, roleVal);
		}
		getAgent().setActiveRole(roleVal);
	}
	/**
	 * Command method to set active role type.
	 * @param spot spot of role variable
	 * @param role role type name or role variable name
	 * @throws InstantiationException
	 * @throws IllegalAccessException
	 */
	public void activateRole(SpotVal spot, String role) throws InstantiationException, IllegalAccessException {
		String roleKey = ROLE_KEY_HEAD + role;
		RoleType roleVal = (RoleType) spot.getSpot(this).getEquip(roleKey);
		if (roleVal == null) {
			roleVal = RoleType.forName(role);
			if (roleVal == null) {
				throw new RuntimeException("Wrong Role Type - activateRole " + role);
			}
			spot.getSpot(this).setEquip(roleKey, roleVal);
		}
		getAgent().setActiveRole(roleVal.createRole(getAgent()));
	}
	/**
	 * Command method to set active role as null.
	 */
	public void activateRole() {
		getAgent().setActiveRole(null);
	}
	/**
	 * Command method to execute self resolution manually.
	 * @throws Exception
	 */
	public static void doSelfResolution() throws Exception {
		Environment.getCurrent().doResolution();
	}
	/**
	 * Command method to create class variable.
	 * @param equality setting formula
	 * @throws ClassNotFoundException
	 */
	public void setClass(String equality) throws ClassNotFoundException {
		setClassImpl(getSelf(), equality);
	}
	/**
	 * Command method to copy instance of class variable.
	 * @param equality setting formula
	 */
	public void copyInstance(String equality) {
		copyInstanceImpl(getSelf(), equality);
	}
	/**
	 * Command method to add parameter to class variable.
	 * @param equality setting formula
	 * @throws ClassNotFoundException
	 */
	public void addParam(String equality) throws ClassNotFoundException {
		addParamImpl(getSelf(), equality);
	}
	/**
	 * Command method to add boolean parameter to class variable.
	 * @param equality setting formula
	 * @throws ClassNotFoundException
	 */
	public void addParamBoolean(String equality) throws ClassNotFoundException {
		addParamBooleanImpl(getSelf(), equality);
	}
	/**
	 * Command method to add string parameter to class variable.
	 * @param equality setting formula
	 */
	public void addParamString(String equality) {
		addParamStringImpl(getSelf(), equality);
	}
	/**
	 * Command method to add class parameter to class variable.
	 * @param equality setting formula
	 * @throws ClassNotFoundException
	 */
	public void addParamClass(String equality) throws ClassNotFoundException {
		addParamClassImpl(getSelf(), equality);
	}
	/**
	 * Command method to add agent parameter to class variable.
	 * @param equality setting formula
	 */
	public void addParamAgent(String equality) {
		String[] pair = equality.split("=", 2);
		getSelf().<Invoker>getEquip(pair[0].trim()).addParameter(pair.length == 1 ? getSelf() : Agent.forName(pair[1]), Agent.class);
	}
	/**
	 * Command method to add spot parameter to class variable.
	 * @param equality setting formula
	 */
	public void addParamSpot(String equality) {
		String[] pair = equality.split("=", 2);
		getSelf().<Invoker>getEquip(pair[0].trim()).addParameter(pair.length == 1 ? getSpot() : Spot.forName(pair[1]), Spot.class);
	}
	/**
	 * Command method to specify class type of parameter in class variable.
	 * @param equality setting formula
	 * @throws ClassNotFoundException
	 */
	public void setParamType(String equality) throws ClassNotFoundException {
		setParamTypeImpl(getSelf(), equality);
	}
	/**
	 * Command method to create new instance in class variable.
	 * @param equality setting formula
	 * @throws IllegalArgumentException
	 * @throws SecurityException
	 * @throws InstantiationException
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 * @throws NoSuchMethodException
	 */
	public void newInstance(String equality) throws IllegalArgumentException, SecurityException, InstantiationException, IllegalAccessException, InvocationTargetException, NoSuchMethodException {
		newInstanceImpl(getSelf(), equality);
	}
	/**
	 * Condition method to invoke method in class variable.
	 * @param equality condition formula
	 * @return false if return value is null or false
	 * @throws IllegalArgumentException
	 * @throws SecurityException
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 * @throws NoSuchMethodException
	 */
	public boolean invokeClass(String equality) throws IllegalArgumentException, SecurityException, IllegalAccessException, InvocationTargetException, NoSuchMethodException {
		return invokeClassImpl(getSelf(), equality);
	}
	/**
	 * Command method to get field value of class variable.
	 * @param equality setting formula
	 * @throws IllegalArgumentException
	 * @throws SecurityException
	 * @throws IllegalAccessException
	 * @throws NoSuchFieldException
	 */
	public void getField(String equality) throws IllegalArgumentException, SecurityException, IllegalAccessException, NoSuchFieldException {
		getFieldImpl(getSelf(), equality);
	}
	/**
	 * Command method to set field value of class variable.
	 * @param equality setting formula
	 * @throws IllegalArgumentException
	 * @throws SecurityException
	 * @throws IllegalAccessException
	 * @throws NoSuchFieldException
	 */
	public void setField(String equality) throws IllegalArgumentException, SecurityException, IllegalAccessException, NoSuchFieldException {
		setFieldImpl(getSelf(), equality);
	}
	/**
	 * Condition method to check if collection variable is empty.
	 * @param collection collection variable name
	 * @return true if collection variable is empty
	 */
	public boolean isEmpty(String collection) {
		return isEmptyImpl(getAgent(), collection);
	}
	/**
	 * Condition method to add agent to collection variable.
	 * @param equality condition formula
	 * @return true if agent is newly added
	 */
	public boolean addAgent(String equality) {
		return addAgentImpl(getAgent(), equality);
	}
	/**
	 * Condition method to add spot to collection variable.
	 * @param equality condition formula
	 * @return true if spot is newly added
	 */
	public boolean addSpot(String equality) {
		return addSpotImpl(getAgent(), equality);
	}
	/**
	 * Condition method to add object value to collection variable.
	 * @param equality condition formula
	 * @return true if object value is newly added
	 */
	public boolean addEquip(String equality) {
		return addEquipImpl(getAgent(), equality);
	}
	/**
	 * Condition method to add string value to collection variable.
	 * @param equality condition formula
	 * @return true is string value is newly added
	 */
	public boolean addString(String equality) {
		return addStringImpl(getAgent(), equality);
	}
	/**
	 * Command method to add agent to list variable as first element.
	 * @param equality setting formula
	 */
	public void addFirstAgent(String equality) {
		addFirstAgentImpl(getAgent(), equality);
	}
	/**
	 * Command method to add spot to list variable as first element.
	 * @param equality setting formula
	 */
	public void addFirstSpot(String equality) {
		addFirstSpotImpl(getAgent(), equality);
	}
	/**
	 * Command method to add object value to list variable as first element.
	 * @param equality setting formula
	 */
	public void addFirstEquip(String equality) {
		addFirstEquipImpl(getAgent(), equality);
	}
	/**
	 * Command method to add string value to list variable as first element.
	 * @param equality setting formula
	 */
	public void addFirstString(String equality) {
		addFirstStringImpl(getAgent(), equality);
	}
	/**
	 * Command method to add agent to list variable as last element.
	 * @param equality setting formula
	 */
	public void addLastAgent(String equality) {
		addLastAgentImpl(getAgent(), equality);
	}
	/**
	 * Command method to add spot to list variable as last element.
	 * @param equality setting formula
	 */
	public void addLastSpot(String equality) {
		addLastSpotImpl(getAgent(), equality);
	}
	/**
	 * Command method to add object value to list variable as last element.
	 * @param equality setting formula
	 */
	public void addLastEquip(String equality) {
		addLastEquipImpl(getAgent(), equality);
	}
	/**
	 * Command method to add string value to list variable as last element.
	 * @param equality setting formula
	 */
	public void addLastString(String equality) {
		addLastStringImpl(getAgent(), equality);
	}
	/**
	 * Condition method to merge collection variables.
	 * @param equality condition formula
	 * @return true is collection variable is changed
	 */
	public boolean addAll(String equality) {
		return addAllImpl(getAgent(), equality);
	}
	/**
	 * Condition method to check if agent is contained in collection variable.
	 * @param equality condition formula
	 * @return true if agent is contained in collection variable
	 */
	public boolean containsAgent(String equality) {
		return containsAgentImpl(getAgent(), equality);
	}
	/**
	 * Condition method to check if spot is contained in collection variable.
	 * @param equality condition formula
	 * @return true if spot is contained in collection variable
	 */
	public boolean containsSpot(String equality) {
		return containsSpotImpl(getAgent(), equality);
	}
	/**
	 * Condition method to check if object value is contained in collection variable.
	 * @param equality condition formula
	 * @return true if object value is contained in collection variable
	 */
	public boolean containsEquip(String equality) {
		return containsEquipImpl(getAgent(), equality);
	}
	/**
	 * Condition method to check if string value is contained in collection variable.
	 * @param equality condition formula
	 * @return true if string value is contained in collection variable
	 */
	public boolean containsString(String equality) {
		return containsStringImpl(getAgent(), equality);
	}
	/**
	 * Condition method to check if first element of list variable agrees specified agent.
	 * @param equality condition formula
	 * @return true if condition is satisfied
	 */
	public boolean isFirstAgent(String equality) {
		return isFirstAgentImpl(getAgent(), equality);
	}
	/**
	 * Condition method to check if first element of list variable agrees specified spot.
	 * @param equality condition formula
	 * @return true if condition is satisfied
	 */
	public boolean isFirstSpot(String equality) {
		return isFirstSpotImpl(getAgent(), equality);
	}
	/**
	 * Condition method to check if first element of list variable agrees specified object value.
	 * @param equality condition formula
	 * @return true if condition is satisfied
	 */
	public boolean isFirstEquip(String equality) {
		return isFirstEquipImpl(getAgent(), equality);
	}
	/**
	 * Condition method to check if first element of list variable agrees specified string value.
	 * @param equality condition formula
	 * @return true if condition is satisfied
	 */
	public boolean isFirstString(String equality) {
		return isFirstStringImpl(getAgent(), equality);
	}
	/**
	 * Condition method to check if last element of list variable agrees specified agent.
	 * @param equality condition formula
	 * @return true if condition is satisfied
	 */
	public boolean isLastAgent(String equality) {
		return isLastAgentImpl(getAgent(), equality);
	}
	/**
	 * Condition method to check if last element of list variable agrees specified spot.
	 * @param equality condition formula
	 * @return true if condition is satisfied
	 */
	public boolean isLastSpot(String equality) {
		return isLastSpotImpl(getAgent(), equality);
	}
	/**
	 * Condition method to check if last element of list variable agrees specified object value.
	 * @param equality condition formula
	 * @return true if condition is satisfied
	 */
	public boolean isLastEquip(String equality) {
		return isLastEquipImpl(getAgent(), equality);
	}
	/**
	 * Condition method to check if last element of list variable agrees specified string value.
	 * @param equality condition formula
	 * @return true if condition is satisfied
	 */
	public boolean isLastString(String equality) {
		return isLastStringImpl(getAgent(), equality);
	}
	/**
	 * Condition method to check if minimum element of sorted set variable agrees specified agent.
	 * @param equality condition formula
	 * @return true if condition is satisfied
	 */
	public boolean isMinAgent(String equality) {
		return isMinAgentImpl(getAgent(), equality);
	}
	/**
	 * Condition method to check if minimum element of sorted set variable agrees specified spot.
	 * @param equality condition formula
	 * @return true if condition is satisfied
	 */
	public boolean isMinSpot(String equality) {
		return isMinSpotImpl(getAgent(), equality);
	}
	/**
	 * Condition method to check if minimum element of sorted set variable agrees specified object value.
	 * @param equality condition formula
	 * @return true if condition is satisfied
	 */
	public boolean isMinEquip(String equality) {
		return isMinEquipImpl(getAgent(), equality);
	}
	/**
	 * Condition method to check if minimum element of sorted set variable agrees specified string value.
	 * @param equality condition formula
	 * @return true if condition is satisfied
	 */
	public boolean isMinString(String equality) {
		return isMinStringImpl(getAgent(), equality);
	}
	/**
	 * Condition method to check if maximum element of sorted set variable agrees specified agent.
	 * @param equality condition formula
	 * @return true if condition is satisfied
	 */
	public boolean isMaxAgent(String equality) {
		return isMaxAgentImpl(getAgent(), equality);
	}
	/**
	 * Condition method to check if maximum element of sorted set variable agrees specified spot.
	 * @param equality condition formula
	 * @return true if condition is satisfied
	 */
	public boolean isMaxSpot(String equality) {
		return isMaxSpotImpl(getAgent(), equality);
	}
	/**
	 * Condition method to check if maximum element of sorted set variable agrees specified object value.
	 * @param equality condition formula
	 * @return true if condition is satisfied
	 */
	public boolean isMaxEquip(String equality) {
		return isMaxEquipImpl(getAgent(), equality);
	}
	/**
	 * Condition method to check if maximum element of sorted set variable agrees specified string value.
	 * @param equality condition formula
	 * @return true if condition is satisfied
	 */
	public boolean isMaxString(String equality) {
		return isMaxStringImpl(getAgent(), equality);
	}
	/**
	 * Condition method to check if collection variable is included in other collection variable.
	 * @param equality condition formula
	 * @return true if condition is satisfied
	 */
	public boolean containsAll(String equality) {
		return containsAllImpl(getAgent(), equality);
	}
	/**
	 * Condition method to remove agent from collection variable.
	 * @param equality condition formula
	 * @return true if collection variable is changed
	 */
	public boolean removeAgent(String equality) {
		return removeAgentImpl(getAgent(), equality);
	}
	/**
	 * Condition method to remove spot from collection variable.
	 * @param equality condition formula
	 * @return true if collection variable is changed
	 */
	public boolean removeSpot(String equality) {
		return removeSpotImpl(getAgent(), equality);
	}
	/**
	 * Condition method to remove object value from collection variable.
	 * @param equality condition formula
	 * @return true if collection variable is changed
	 */
	public boolean removeEquip(String equality) {
		return removeEquipImpl(getAgent(), equality);
	}
	/**
	 * Condition method to remove string value from collection variable.
	 * @param equality condition formula
	 * @return true if collection variable is changed
	 */
	public boolean removeString(String equality) {
		return removeStringImpl(getAgent(), equality);
	}
	/**
	 * Command method to get one element of collection variable.
	 * @param equality setting formula
	 */
	public void equipOne(String equality) {
		equipOneImpl(getAgent(), equality);
	}
	/**
	 * Command method to remove one element of collection variable.
	 * @param equality setting formula
	 */
	public void removeOne(String equality) {
		removeOneImpl(getAgent(), equality);
	}
	/**
	 * Command method to get randomly one element of collection variable.
	 * @param equality setting formula
	 */
	public void equipRandomOne(String equality) {
		equipRandomOneImpl(getAgent(), equality);
	}
	/**
	 * Command method to remove randomly one element of collection variable.
	 * @param equality setting formula
	 */
	public void removeRandomOne(String equality) {
		removeRandomOneImpl(getAgent(), equality);
	}
	/**
	 * Command method to get first element of collection variable.
	 * @param equality setting formula
	 */
	public void equipFirst(String equality) {
		equipFirstImpl(getAgent(), equality);
	}
	/**
	 * Command method to remove first element of collection variable.
	 * @param equality setting formula
	 */
	public void removeFirst(String equality) {
		removeFirstImpl(getAgent(), equality);
	}
	/**
	 * Command method to get last element of collection variable.
	 * @param equality setting formula
	 */
	public void equipLast(String equality) {
		equipLastImpl(getAgent(), equality);
	}
	/**
	 * Command method to remove last element of collection variable.
	 * @param equality setting formula
	 */
	public void removeLast(String equality) {
		removeLastImpl(getAgent(), equality);
	}
	/**
	 * Command method to get minimum element of sorted set variable.
	 * @param equality setting formula
	 */
	public void equipMin(String equality) {
		equipMinImpl(getAgent(), equality);
	}
	/**
	 * Command method to remove minimum element of sorted set variable.
	 * @param equality setting formula
	 */
	public void removeMin(String equality) {
		removeMinImpl(getAgent(), equality);
	}
	/**
	 * Command method to get maximum element of sorted set variable.
	 * @param equality setting formula
	 */
	public void equipMax(String equality) {
		equipMaxImpl(getAgent(), equality);
	}
	/**
	 * Command method to remove maximum element of sorted set variable.
	 * @param equality setting formula
	 */
	public void removeMax(String equality) {
		removeMaxImpl(getAgent(), equality);
	}
	/**
	 * Condition method to remove all elements of collection variable from other collection variable.
	 * @param equality condition formula
	 * @return true if collection variable is changed
	 */
	public boolean removeAll(String equality) {
		return removeAllImpl(getAgent(), equality);
	}
	/**
	 * Condition method to retain all elements of collection variable from other collection variable.
	 * @param equality condition formula
	 * @return true if collection variable is changed
	 */
	public boolean retainAll(String equality) {
		return retainAllImpl(getAgent(), equality);
	}
	/**
	 * Command method to reverse order of list variable.
	 * @param list list variable name
	 */
	public void reverseAll(String list) {
		reverseAllImpl(getAgent(), list);
	}
	/**
	 * Command method to rotate order of list variable.
	 * @param equality setting formula
	 */
	public void rotateAll(String equality) {
		rotateAllImpl(getAgent(), equality);
	}
	/**
	 * Command method to shuffle order of list variable.
	 * @param list list variable name
	 */
	public void shuffleAll(String list) {
		shuffleAllImpl(getAgent(), list);
	}
	/**
	 * Command method to sort all elements of list variable.
	 * @param list list variable name
	 */
	public void sortAll(String list) {
		sortAllImpl(getAgent(), list);
	}
	/**
	 * Command method to set string property of all elements in collection variable.
	 * @param equality setting formula
	 */
	public void setAll(String equality) {
		setAllImpl(getAgent(), equality);
	}
	/**
	 * Command method to send string message to all elements in collection variable.
	 * @param equality setting formula
	 * @throws Exception
	 */
	public void askAll(String equality) throws Exception {
		askAllImpl(getAgent(), equality);
	}
	/**
	 * Command method to start rule action of all elements in collection variable.
	 * @param equality setting formula
	 * @throws InstantiationException
	 * @throws IllegalAccessException
	 */
	public void startRuleAll(String equality) throws InstantiationException, IllegalAccessException {
		startRuleAllImpl(getAgent(), equality);
	}
	void moveToOneImpl(EquippedObject self, String collection) {
		getAgent().setSpot(self.<Collection<Spot>>getEquip(collection).iterator().next());
	}
	void moveToRandomOneImpl(EquippedObject self, String collection) {
		Collection<Spot> c = self.getEquip(collection);
		if (c.isEmpty()) {
			return;
		}
		Iterator<Spot> iterator = c.iterator();
		Spot spot;
		int i = Environment.getCurrent().getRandom().nextInt(c.size()) + 1;
		do {
			spot = iterator.next();
		} while (--i > 0);
		getAgent().setSpot(spot);
	}
	void moveToFirstImpl(EquippedObject self, String list) {
		List<Spot> l = self.getEquip(list);
		if (l.isEmpty()) {
			return;
		}
		getAgent().setSpot(l.get(0));
	}
	void moveToLastImpl(EquippedObject self, String list) {
		List<Spot> l = self.getEquip(list);
		if (l.isEmpty()) {
			return;
		}
		getAgent().setSpot(l.get(l.size() - 1));
	}
	void moveToMinImpl(EquippedObject self, String list) {
		SortedSet<Spot> set = self.getEquip(list);
		getAgent().setSpot(set.first());
	}
	void moveToMaxImpl(EquippedObject self, String list) {
		SortedSet<Spot> set = self.getEquip(list);
		getAgent().setSpot(set.last());
	}
	/**
	 * Command method to move agent to one spot in collection variable.
	 * @param collection collection variable name
	 */
	public void moveToOne(String collection) {
		moveToOneImpl(getAgent(), collection);
	}
	/**
	 * Command method to move agent to randomly one spot in collection variable.
	 * @param collection collection variable name
	 */
	public void moveToRandomOne(String collection) {
		moveToRandomOneImpl(getAgent(), collection);
	}
	/**
	 * Command method to move agent to first spot in list variable.
	 * @param list list variable name
	 */
	public void moveToFirst(String list) {
		moveToFirstImpl(getAgent(), list);
	}
	/**
	 * Command method to move agent to last spot in list variable.
	 * @param list list variable name
	 */
	public void moveToLast(String list) {
		moveToLastImpl(getAgent(), list);
	}
	/**
	 * Command method to move agent to minimum spot in sorted set variable.
	 * @param list sorted set variable name
	 */
	public void moveToMin(String list) {
		moveToMinImpl(getAgent(), list);
	}
	/**
	 * Command method to move agent to maximum spot in sorted set variable.
	 * @param list sorted set variable name
	 */
	public void moveToMax(String list) {
		moveToMaxImpl(getAgent(), list);
	}
	/**
	 * Command method to move agent to one spot in collection variable.
	 * @param collection collection variable name
	 */
	public void moveToOne(SpotVal spot, String collection) {
		moveToOneImpl(spot.getSpot(this), collection);
	}
	/**
	 * Command method to move agent to randomly one spot in collection variable.
	 * @param collection collection variable name
	 */
	public void moveToRandomOne(SpotVal spot, String collection) {
		moveToRandomOneImpl(spot.getSpot(this), collection);
	}
	/**
	 * Command method to move agent to first spot in list variable.
	 * @param spot spot of list variable
	 * @param list list variable name
	 */
	public void moveToFirst(SpotVal spot, String list) {
		moveToFirstImpl(spot.getSpot(this), list);
	}
	/**
	 * Command method to move agent to last spot in list variable.
	 * @param spot spot of list variable
	 * @param list list variable name
	 */
	public void moveToLast(SpotVal spot, String list) {
		moveToLastImpl(spot.getSpot(this), list);
	}
	/**
	 * Command method to move agent to minimum spot in sorted set variable.
	 * @param spot spot of sorted set variable
	 * @param list sorted set variable name
	 */
	public void moveToMin(SpotVal spot, String list) {
		moveToMinImpl(spot.getSpot(this), list);
	}
	/**
	 * Command method to move agent to maximum spot in sorted set variable.
	 * @param spot spot of sorted set variable
	 * @param list sorted set variable name
	 */
	public void moveToMax(SpotVal spot, String list) {
		moveToMaxImpl(spot.getSpot(this), list);
	}
	void attachToAllImpl(EquippedObject self, String collection) {
		Iterator<Agent> it = self.<Collection<Agent>>getEquip(collection).iterator();
		while (it.hasNext()) {
			it.next().attachTo(getAgent());
		}
	}
	/**
	 * Command method to attach all agents in collection variable.
	 * @param collection collection variable name
	 */
	public void attachToAll(String collection) {
		attachToAllImpl(getAgent(), collection);
	}
	/**
	 * Command method to attach all agents in collection variable.
	 * @param spot spot of collection variable
	 * @param collection collection variable name
	 */
	public void attachToAll(SpotVal spot, String collection) {
		attachToAllImpl(spot.getSpot(this), collection);
	}
	void attachToOneImpl(EquippedObject self, String collection) {
		getAgent().attachTo(self.<Collection<Agent>>getEquip(collection).iterator().next());
	}
	void attachToRandomOneImpl(EquippedObject self, String collection) {
		Collection<Agent> c = self.getEquip(collection);
		if (c.isEmpty()) {
			return;
		}
		Iterator<Agent> iterator = c.iterator();
		Agent agent;
		int i = Environment.getCurrent().getRandom().nextInt(c.size()) + 1;
		do {
			agent = iterator.next();
		} while (--i > 0);
		getAgent().attachTo(agent);
	}
	void attachToFirstImpl(EquippedObject self, String list) {
		List<Agent> l = self.getEquip(list);
		if (l.isEmpty()) {
			return;
		}
		getAgent().attachTo(l.get(0));
	}
	void attachToLastImpl(EquippedObject self, String list) {
		List<Agent> l = self.getEquip(list);
		if (l.isEmpty()) {
			return;
		}
		getAgent().attachTo(l.get(l.size() - 1));
	}
	void attachToMinImpl(EquippedObject self, String list) {
		SortedSet<Agent> set = self.getEquip(list);
		if (set.isEmpty()) {
			return;
		}
		getAgent().attachTo(set.first());
	}
	void attachToMaxImpl(EquippedObject self, String list) {
		SortedSet<Agent> set = self.getEquip(list);
		if (set.isEmpty()) {
			return;
		}
		getAgent().attachTo(set.last());
	}
	/**
	 * Command method to attach to one agent in collection variable. 
	 * @param collection collection variable name
	 */
	public void attachToOne(String collection) {
		attachToOneImpl(getAgent(), collection);
	}
	/**
	 * Command method to attach to randomly one agent in collection variable. 
	 * @param collection collection variable name
	 */
	public void attachToRandomOne(String collection) {
		attachToRandomOneImpl(getAgent(), collection);
	}
	/**
	 * Command method to attach to first agent in list variable.
	 * @param list list variable name
	 */
	public void attachToFirst(String list) {
		attachToFirstImpl(getAgent(), list);
	}
	/**
	 * Command method to attach to last agent in list vairable.
	 * @param list list variable name
	 */
	public void attachToLast(String list) {
		attachToLastImpl(getAgent(), list);
	}
	/**
	 * Command method to attach to minimum agent in sorted set variable.
	 * @param list sorted set variable name
	 */
	public void attachToMin(String list) {
		attachToMinImpl(getAgent(), list);
	}
	/**
	 * Command method to attach to maximum agent in sorted set variable.
	 * @param list sorted set variable name
	 */
	public void attachToMax(String list) {
		attachToMaxImpl(getAgent(), list);
	}
	/**
	 * Command method to attach to one agent in collection variable. 
	 * @param spot spot of collection variable
	 * @param collection collection variable name
	 */
	public void attachToOne(SpotVal spot, String collection) {
		attachToOneImpl(spot.getSpot(this), collection);
	}
	/**
	 * Command method to attach to randomly one agent in collection variable. 
	 * @param spot spot of collection variable
	 * @param collection collection variable name
	 */
	public void attachToRandomOne(SpotVal spot, String collection) {
		attachToRandomOneImpl(spot.getSpot(this), collection);
	}
	/**
	 * Command method to attach to first agent in list variable.
	 * @param spot spot of list variable
	 * @param list list variable name
	 */
	public void attachToFirst(SpotVal spot, String list) {
		attachToFirstImpl(spot.getSpot(this), list);
	}
	/**
	 * Command method to attach to last agent in list variable.
	 * @param spot spot of list variable
	 * @param list list variable name
	 */
	public void attachToLast(SpotVal spot, String list) {
		attachToLastImpl(spot.getSpot(this), list);
	}
	/**
	 * Command method to attach to minimum agent in sorted set variable.
	 * @param spot spot of sorted set variable
	 * @param list sorted set variable name
	 */
	public void attachToMin(SpotVal spot, String list) {
		attachToMinImpl(spot.getSpot(this), list);
	}
	/**
	 * Command method to attach to maximum agent in sorted set variable.
	 * @param spot spot of sorted set variable
	 * @param list sorted set variable name
	 */
	public void attachToMax(SpotVal spot, String list) {
		attachToMaxImpl(spot.getSpot(this), list);
	}
	/**
	 * Command method to detach all agents in collection variable.
	 * @param collection collection variable name
	 */
	public void detachAll(String collection) {
		detachAllImpl(getAgent(), collection);
	}
	/**
	 * Command method to detach one agent in collection variable.
	 * @param collection collection variable name
	 */
	public void detachOne(String collection) {
		detachOneImpl(getAgent(), collection);
	}
	/**
	 * Command method to detach randomly one agent in collection variable.
	 * @param collection collection variable name
	 */
	public void detachRandomOne(String collection) {
		detachRandomOneImpl(getAgent(), collection);
	}
	/**
	 * Command method to detach first agent in list variable.
	 * @param list list variable name
	 */
	public void detachFirst(String list) {
		detachFirstImpl(getAgent(), list);
	}
	/**
	 * Command method to detach last agent in list variable.
	 * @param list list variable name
	 */
	public void detachLast(String list) {
		detachLastImpl(getAgent(), list);
	}
	/**
	 * Command method to detach minumum agent in sorted set variable.
	 * @param list sorted set variable name
	 */
	public void detachMin(String list) {
		detachMinImpl(getAgent(), list);
	}
	/**
	 * Command method to detach maximum agent in sorted set variable.
	 * @param list sorted set variable name
	 */
	public void detachMax(String list) {
		detachMaxImpl(getAgent(), list);
	}
	/**
	 * Condition method to invoke method of object attribute.
	 * @param formula condition formula
	 * @return result of method calling
	 * @throws Exception
	 */
	public boolean invokeEquip(String formula) throws Exception {
		return invokeEquipImpl(getAgent(), formula);
	}
	/**
	 * Command method to clear elements of map variable.
	 * @param map map variable name
	 */
	public void clearMap(String map) {
		clearMapImpl(getAgent(), map);
	}
	/**
	 * Condition method to check if map variable is empty.
	 * @param map map variable name
	 * @return true if map variable is empty
	 */
	public boolean isEmptyMap(String map) {
		return isEmptyMapImpl(getAgent(), map);
	}
	/**
	 * Condition method to check if map variable contains specified key.
	 * @param equality condition formula
	 * @return true if map variable contains specified key
	 */
	public boolean containsKey(String equality) {
		return containsKeyImpl(getAgent(), equality);
	}
	/**
	 * Condition method to check if map variable contains specified value.
	 * @param equality condition formula
	 * @return true if map variable contains specified value
	 */
	public boolean containsValue(String equality) {
		return containsValueImpl(getAgent(), equality);
	}
	/**
	 * Command method to get key collection of map variable.
	 * @param equality setting formula
	 */
	public void equipMapKeys(String equality) {
		equipMapKeysImpl(getAgent(), equality);
	}
	/**
	 * Command method to get value collection of map variable.
	 * @param equality setting formula
	 */
	public void equipMapValues(String equality) {
		equipMapValuesImpl(getAgent(), equality);
	}
	/**
	 * Command method to get value of map variable.
	 * @param equality setting formula
	 */
	public void getMap(String equality) {
		getMapImpl(getAgent(), equality);
	}
	/**
	 * Command method to remove mapping from map variable.
	 * @param equality setting formula
	 */
	public void removeMap(String equality) {
		removeMapImpl(getAgent(), equality);
	}
	/**
	 * Condition method to check mapping value of map variable.
	 * @param equality condition formula
	 * @return true if mapping value equals to specified object
	 */
	public boolean equalsMap(String equality) {
		return equalsMapImpl(getAgent(), equality);
	}
	/**
	 * Command method to put value of map variable.
	 * @param equality setting formula
	 */
	public void putMap(String equality) {
		putMapImpl(getAgent(), equality);
	}
	/**
	 * Command method to merge map variables.
	 * @param equality setting formula
	 */
	public void putMapAll(String equality) {
		putMapAllImpl(getAgent(), equality);
	}
}
