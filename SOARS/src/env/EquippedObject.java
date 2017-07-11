package env;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;

import role.Role;
import util.Askable;
import util.Ownable;

/**
 * The EquippedObject abstract class represents object with attributes.
 * @author H. Tanuma / SOARS project
 */
public abstract class EquippedObject extends ObjectFacade implements Askable<Role> {

	private static final long serialVersionUID = -792246042683526258L;
	HashMap<String, Object> equip = new HashMap<String, Object>();

	/**
	 * Key string to get name property.
	 */
	public static final String NAME_KEY = "$Name";
	/**
	 * Key string to get ID number property.
	 */
	public static final String ID_KEY = "$ID";
	/**
	 * Key string to get class name property.
	 */
	public static final String CLASS_KEY = "$Class";

	/**
	 * Constructor for EquippedObject class.
	 * @param database database object
	 */
	public EquippedObject(NamedObjectDB database) {
		super(database);
		equip.put(ID_KEY, Integer.toString(getId()));
		equip.put(CLASS_KEY, getClass().getName());
	}
	/**
	 * Get map for object attributes.
	 * @return map for object attributes
	 */
	public Map<String, Object> getEquipMap() {
		return equip;
	}
	/**
	 * Get map for object attributes.
	 * @return map for object attributes
	 */
	public SortedMap<String, String> getPropMap() {
		TreeMap<String, String> propMap = new TreeMap<String, String>();
		Set<Entry<String, Object>> entrySet = equip.entrySet();
		for (Entry<String, Object> entry : entrySet) {
			propMap.put(entry.getKey(), String.valueOf(entry.getValue()));
		}
		return propMap;
	}
	/**
	 * Set object attribute.
	 * @param key attribute name
	 * @param obj attribute value
	 * @return previous attribute value
	 */
	public Object setEquip(String key, Object obj) {
		if (obj == null) {
			return equip.remove(key);
		}
		if (obj instanceof Ownable) {
			((Ownable) obj).setOwner(this);
		}
		return equip.put(key, obj);
	}
	/**
	 * Get object attribute.
	 * @param key attribute name
	 * @return attribute value
	 */
	@SuppressWarnings("unchecked")
	public <E> E getEquip(String key) {
		return (E) equip.get(key);
	}
	/**
	 * Set string attribute.
	 * @param key attribute name
	 * @param value attribute value
	 * @return previous attribute value
	 */
	public String setProp(String key, String value) {
		Object o = (value != null) ? equip.put(key, value) : equip.remove(key);
		return o != null ? o.toString() : null;
	}
	/**
	 * Get string attribute.
	 * @param key attribute name
	 * @return attribute value
	 */
	public String getProp(String key) {
		Object o = equip.get(key);
		return o != null ? o.toString() : null;
	}
	/**
	 * Set name string.
	 */
	public void setName(String name) {
		equip.put(NAME_KEY, name);
		super.setName(name);
	}
	/**
	 * Redirect ask message to object attribute.
	 * @param caller message sender
	 * @param message message string
	 * @return result of ask message
	 * @throws Exception
	 */
	public boolean ask(Role caller, Object message) throws Exception {
		return ask(message.toString()).ask(caller, this);
	}
	/**
	 * Get ask message broker.
	 * @param message message string.
	 * @return ask message broker
	 */
	public static Askable<Role> ask(final String message) {
		final String[] pair = message.split("=", 2);
		pair[0] = pair[0].trim();
		if (pair.length == 1) {
			return new Askable<Role>() {
				private static final long serialVersionUID = -5928970451011173709L;
				public boolean ask(Role caller, Object self) throws Exception {
					return ((EquippedObject) self).getEquip(pair[0]) instanceof Askable<?>;
				}
			};
		}
		pair[1] = pair[1].trim();
		return new Askable<Role>() {
			private static final long serialVersionUID = 3817441483252459951L;
			@SuppressWarnings("unchecked")
			public boolean ask(Role caller, Object self) throws Exception {
				self = ((EquippedObject) self).getEquip(pair[0]);
				if (self instanceof Askable) {
					return ((Askable<Role>) self).ask(caller, pair[1]);
				}
				throw new RuntimeException("Equipment Not Askable - " + message);
			}
		};
	}
	/**
	 * Get self.
	 * @return self
	 */
	public EquippedObject getSelf() {
		return this;
	}
}
