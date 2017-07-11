/**
 * 
 */
package soars.library.arbitrary.test001;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.swing.JOptionPane;

/**
 * @author kurata / SOARS project
 *
 */
public class Test001 implements TestInterface {

	/**
	 * 
	 */
	static public Test001 create() {
		return new Test001();
	}

	/**
	 * 
	 */
	public Test001() {
		super();
	}

	/**
	 * 
	 */
	public void test() {
		JOptionPane.showMessageDialog( null, "Test!", "Test001", JOptionPane.INFORMATION_MESSAGE);
	}

	/**
	 * @param i
	 * @param j
	 * @return
	 */
	public int plus(int i, int j) {
		return i + j;
	}

	/**
	 * @param i
	 * @param j
	 * @return
	 */
	public int minus(int i, int j) {
		return i - j;
	}

	/**
	 * @param i
	 * @param j
	 * @return
	 */
	public int multi(int i, int j) {
		return i * j;
	}

	/**
	 * @param i
	 * @param j
	 * @return
	 */
	public int divide(int i, int j) {
		return i * j;
	}

	/**
	 * @param i
	 * @param j
	 * @return
	 */
	public double plus(double i, double j) {
		return i + j;
	}

	/**
	 * @param i
	 * @param j
	 * @return
	 */
	public double minus(double i, double j) {
		return i - j;
	}

	/**
	 * @param i
	 * @param j
	 * @return
	 */
	public double multi(double i, double j) {
		return i * j;
	}

	/**
	 * @param i
	 * @param j
	 * @return
	 */
	public double divide(double i, double j) {
		return i * j;
	}

	/**
	 * @see soars.library.arbitrary.test001.TestInterface#plus(java.lang.String, java.lang.String)
	 */
	public String plus(String text1, String text2) {
		return ( text1 + text2);
	}

	/**
	 * @param value
	 * @return
	 */
	public boolean boolean_test(boolean value) {
		return ( value ? false : true);
	}

	/**
	 * @param collection
	 * @return
	 */
	public Collection Collection_test(Collection collection) {
		if ( null == collection)
			return collection;

		String name = "";
		Iterator iterator = collection.iterator();
		while ( iterator.hasNext()) {
			Object object = iterator.next();
			name = object.getClass().getName();
		}

		JOptionPane.showMessageDialog( null, name, "Collection_test", JOptionPane.INFORMATION_MESSAGE);

		return collection;
	}

	/**
	 * @param set
	 * @return
	 */
	public Set Set_test(Set set) {
		if ( null == set)
			return set;

		String name = "";
		Iterator iterator = set.iterator();
		while ( iterator.hasNext()) {
			Object object = iterator.next();
			name = object.getClass().getName();
		}

		JOptionPane.showMessageDialog( null, name, "Set_test", JOptionPane.INFORMATION_MESSAGE);

		return set;
	}

	/**
	 * @param hashSet
	 * @return
	 */
	public HashSet HashSet_test(HashSet hashSet) {
		if ( null == hashSet)
			return hashSet;

		String name = "";
		Iterator iterator = hashSet.iterator();
		while ( iterator.hasNext()) {
			Object object = iterator.next();
			name = object.getClass().getName();
		}

		JOptionPane.showMessageDialog( null, name, "HashSet_test", JOptionPane.INFORMATION_MESSAGE);

		return hashSet;
	}

	/**
	 * @param list
	 * @return
	 */
	public List List_test(List list) {
		if ( null == list)
			return list;

		String name = "";
		Iterator iterator = list.iterator();
		while ( iterator.hasNext()) {
			Object object = iterator.next();
			name = object.getClass().getName();
		}

		JOptionPane.showMessageDialog( null, name, "List_test", JOptionPane.INFORMATION_MESSAGE);

		return list;
	}

	/**
	 * @param linkedList
	 * @return
	 */
	public LinkedList LinkedList_test(LinkedList linkedList) {
		if ( null == linkedList)
			return linkedList;

		String name = "";
		Iterator iterator = linkedList.iterator();
		while ( iterator.hasNext()) {
			Object object = iterator.next();
			name = object.getClass().getName();
		}

		JOptionPane.showMessageDialog( null, name, "LinkedList_test", JOptionPane.INFORMATION_MESSAGE);

		return linkedList;
	}

	/**
	 * @param map
	 * @return
	 */
	public Map Map_test(Map map) {
		if ( null == map)
			return map;

		String name = "";
		Iterator iterator = map.entrySet().iterator();
		while ( iterator.hasNext()) {
			Object object = iterator.next();
			name = object.getClass().getName();
		}

		JOptionPane.showMessageDialog( null, name, "Map_test", JOptionPane.INFORMATION_MESSAGE);

		return map;
	}

	/**
	 * @param hashMap
	 * @return
	 */
	public HashMap HashMap_test(HashMap hashMap) {
		if ( null == hashMap)
			return hashMap;

		String name = "";
		Iterator iterator = hashMap.entrySet().iterator();
		while ( iterator.hasNext()) {
			Object object = iterator.next();
			name = object.getClass().getName();
		}

		JOptionPane.showMessageDialog( null, name, "HashMap_test", JOptionPane.INFORMATION_MESSAGE);

		return hashMap;
	}

	/**
	 * @param i
	 * @param j
	 * @return
	 */
	protected int protected_plus(int i, int j) {
		return i + j;
	}

	/**
	 * @param i
	 * @param j
	 * @return
	 */
	protected int protected_minus(int i, int j) {
		return i - j;
	}

	/**
	 * @param i
	 * @param j
	 * @return
	 */
	protected int protected_multi(int i, int j) {
		return i * j;
	}

	/**
	 * @param i
	 * @param j
	 * @return
	 */
	protected int protected_divide(int i, int j) {
		return i * j;
	}

	/**
	 * @param i
	 * @param j
	 * @return
	 */
	private int private_plus(int i, int j) {
		return i + j;
	}

	/**
	 * @param i
	 * @param j
	 * @return
	 */
	private int private_minus(int i, int j) {
		return i - j;
	}

	/**
	 * @param i
	 * @param j
	 * @return
	 */
	private int private_multi(int i, int j) {
		return i * j;
	}

	/**
	 * @param i
	 * @param j
	 * @return
	 */
	private int private_divide(int i, int j) {
		return i * j;
	}
}
