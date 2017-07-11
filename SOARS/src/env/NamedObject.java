package env;

import java.io.Serializable;

/**
 * The NamedObject class represents object with unique name and ID number.
 * @author H. Tanuma / SOARS project
 */
public class NamedObject implements Comparable<NamedObject>, Serializable {

	private static final long serialVersionUID = -2601521887096171628L;
	String name = null;
	int idNumber;
	NamedObjectDB database;

	/**
	 * Constructor for NamedObject class.
	 * @param database database object
	 */
	public NamedObject(NamedObjectDB database) {
		this.database = database;
		idNumber = database.list.size();
		database.list.add(this);
		database.shuffled.add(this);
	}
	/**
	 * Set name and check collision.
	 * @param name name string
	 */
	public void setName(String name) {
		if (this.name != null) {
			database.nameDB.remove(this.name);
		}
		this.name = name;
		if (name != null && database.nameDB.put(name, this) != null) {
			throw new RuntimeException("Name Collision - Name=" + name + ", ID=" + idNumber + ", Class=" + getClass().getName());
		}
	}
	/**
	 * Assign unique name with index number suffix.
	 * @param nameHead prefix name string
	 */
	public void assignName(String nameHead) {
		setName(null);
		int[] index = database.nameIndexDB.get(nameHead);
		if (index == null) {
			index = new int[] {0};
			database.nameIndexDB.put(nameHead, index);
		}
		String name;
		do {
			name = nameHead + ++index[0];
		} while (database.nameDB.containsKey(name));
		setName(name);
	}
	/**
	 * Get name string.
	 * @return name string
	 */
	public String getName() {
		return name;
	}
	/**
	 * Get ID number.
	 * @return ID number
	 */
	public int getId() {
		return idNumber;
	}
	/**
	 * Compare to other object in ID order.
	 * @param o object to compare
	 */
	public int compareTo(NamedObject o) {
		int another = o.getId();
		return idNumber < another ? -1 : (idNumber == another ? 0 : 1);
	}
	/**
	 * Get name string.
	 * @return name string
	 */
	public String toString() {
		return name;
	}
}
