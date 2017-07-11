/**
 * 
 */
package soars.library.adapter.userrules;

import util.DoubleValue;
import util.IntValue;
import util.Invoker;
import env.EquippedObject;

/**
 * @author kurata
 *
 */
public class UserRuleUtility {

	/**
	 * @param equippedObject
	 * @param argument
	 * @return
	 */
	public static String getString(EquippedObject equippedObject, String argument) {
		Unit unit = Unit.create( equippedObject, argument);
		if ( null == unit)
			return null;

		return ( String)unit.get();
	}

	/**
	 * @param equippedObject
	 * @param argument
	 * @return
	 */
	public static int getIntValue(EquippedObject equippedObject, String argument) {
		Unit unit = Unit.create( equippedObject, argument);
		if ( null == unit)
			return 0;

		return ( ( IntValue)unit.get()).intValue();
	}

	/**
	 * @param equippedObject
	 * @param argument
	 * @return
	 */
	public static double getDoubleValue(EquippedObject equippedObject, String argument) {
		Unit unit = Unit.create( equippedObject, argument);
		if ( null == unit)
			return 0.0;

		return ( ( DoubleValue)unit.get()).doubleValue();
	}

	/**
	 * @param equippedObject
	 * @param argument
	 * @return
	 */
	public static Object getClassVariable(EquippedObject equippedObject, String argument) {
		Unit unit = Unit.create( equippedObject, argument);
		if ( null == unit)
			return 0;

		Object object = unit.get();
		if ( null == object || !( object instanceof Invoker))
			return null;

		Invoker invoker = ( Invoker)object;

		return invoker.getInstance();
	}

	/**
	 * @param equippedObject
	 * @param argument
	 * @return
	 */
	public static Object get(EquippedObject equippedObject, String argument) {
		Unit unit = Unit.create( equippedObject, argument);
		if ( null == unit)
			return null;

		return unit.get();
	}

	/**
	 * @param equippedObject
	 * @param argument
	 * @param value
	 */
	public static void set(EquippedObject equippedObject, String argument, String value) {
		Unit unit = Unit.create( equippedObject, argument);
		if ( null == unit)
			return;

		unit.set( value);
	}

	/**
	 * @param equippedObject
	 * @param argument
	 * @param value
	 */
	public static void set(EquippedObject equippedObject, String argument, int value) {
		Unit unit = Unit.create( equippedObject, argument);
		if ( null == unit)
			return;

		unit.set( value);
	}

	/**
	 * @param equippedObject
	 * @param argument
	 * @param value
	 */
	public static void set(EquippedObject equippedObject, String argument, double value) {
		Unit unit = Unit.create( equippedObject, argument);
		if ( null == unit)
			return;

		unit.set( value);
	}
}
