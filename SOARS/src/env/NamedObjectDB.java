package env;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * The NamedObjectDB class represents database for NamedObject instances.
 * @author H. Tanuma / SOARS project
 */
public class NamedObjectDB implements Serializable {

	private static final long serialVersionUID = 2526430615876715978L;
	ArrayList<NamedObject> list = new ArrayList<NamedObject>();
	List<NamedObject> shuffled;
	HashMap<String,NamedObject> nameDB = new HashMap<String,NamedObject>();
	HashMap<String,int[]> nameIndexDB = new HashMap<String,int[]>();

	/**
	 * Constructor for NamedObjectDB class.
	 * @param shuffled empty list with shuffling function.
	 */
	public NamedObjectDB(List<NamedObject> shuffled) {
		this.shuffled = shuffled;
	}
	/**
	 * Get list of objects.
	 * @return list of objects.
	 */
	@SuppressWarnings("unchecked")
	public <E extends NamedObject> List<E> getList() {
		return (List<E>) list;
	}
	/**
	 * Get shuffled list of objects.
	 * @return shuffled list of objects.
	 */
	@SuppressWarnings("unchecked")
	public <E extends NamedObject> List<E> getShuffledList() {
		return (List<E>) shuffled;
	}
	/**
	 * Get object by name.
	 * @param name name string
	 * @return object with specified name
	 */
	@SuppressWarnings("unchecked")
	public <E extends NamedObject> E forName(String name) {
		return (E) nameDB.get(name);
	}
	/**
	 * Get object by ID.
	 * @param idNumber ID number
	 * @return object with specified ID
	 */
	@SuppressWarnings("unchecked")
	public <E extends NamedObject> E forId(int idNumber) {
		return (E) list.get(idNumber);
	}
	/**
	 * Get list of objects by index range.
	 * @param name name with index range
	 * @return list of objects
	 */
	public <E extends NamedObject> List<E> getList(String name) {
		List<E> list = new ArrayList<E>();
		String[] pair = name.split("\\[", 2);
		if (pair.length == 1 || !name.endsWith("]")) {
			E named = forName(name);
			if (named != null) {
				list.add(named);
			}
			return list;
		}
		name = pair[0];
		String[] array = pair[1].substring(0, pair[1].length() - 1).split("\\,");
		for (int i = 0; i < array.length; ++i) {
			pair = array[i].split("\\-", 2);
			if (pair.length == 1) {
				E named = forName(name + pair[0]);
				if (named != null) {
					list.add(named);
				}
				continue;
			}
			int from;
			try {
				from = Integer.parseInt(pair[0].trim());
			} catch(NumberFormatException e) {
				from = 1;
			}
			int to;
			try {
				to = Integer.parseInt(pair[1].trim());
			} catch(NumberFormatException e) {
				to = this.list.size();
			}
			for (int j = from; j <= to; ++j) {
				E named = forName(name + j);
				if (named == null) {
					break;
				}
				list.add(named);
			}
		}
		return list;
	}
}
