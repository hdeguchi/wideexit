/**
 * 
 */
package soars.library.adapter.userrules;

import soars.common.utility.tool.common.Tool;
import util.DoubleValue;
import util.IntValue;
import env.Agent;
import env.EquippedObject;
import env.Spot;

/**
 * @author kurata
 *
 */
public class Unit {

	/**
	 * 
	 */
	public String[] _elements = new String[] { "", "", ""};

	/**
	 * 
	 */
	public EquippedObject _entity = null;

	/**
	 * @param equippedObject
	 * @param argument
	 * @return
	 */
	public static Unit create(EquippedObject equippedObject, String argument) {
		Unit unit = new Unit();
		if ( !unit.set( equippedObject, argument))
			return null;

		return unit;
	}

	/**
	 * @param equippedObject
	 * @param argument
	 * @return
	 */
	public static Object get(EquippedObject equippedObject, String argument) {
		Unit unit = create( equippedObject, argument);
		if ( null == unit)
			return null;

		return unit.get();
	}

	/**
	 * 
	 */
	public Unit() {
		super();
	}

	/**
	 * @param equippedObject
	 * @param argument
	 * @return
	 */
	public boolean set(EquippedObject equippedObject, String argument) {
		if ( !get( argument))
			return false;

		_entity = getEntity( equippedObject, _elements[ 0]);
		if ( null == _entity)
			return false;

		return true;
	}

	/**
	 * @param argument
	 * @return
	 */
	public boolean get(String argument) {
		String[] words = Tool.split( argument, '.');
		if ( 2 != words.length)
			return false;

		_elements[ 2] = words[ 1];

		words = Tool.split( words[ 0], ':');
		switch ( words.length) {
			case 1:
				_elements[ 0] = words[ 0];
				break;
			case 2:
				_elements[ 0] = words[ 0];
				_elements[ 1] = words[ 1];
				break;
			default:
				return false;
		}

		return true;
	}

	/**
	 * @param equippedObject
	 * @param name
	 * @return
	 */
	public static EquippedObject getEntity(EquippedObject equippedObject, String name) {
		if ( name.equals( "__self"))
			return equippedObject;
		else if ( name.equals( "__currentspot"))
			return ( equippedObject instanceof Agent) ? equippedObject.getSpot() : null;
		else {
			Agent agent = Agent.forName( name);
			return ( null != agent) ? agent : Spot.forName( name);
		}
	}

	/**
	 * @return
	 */
	public Object get() {
		return _entity.getEquip( _elements[ 2]);
	}

	/**
	 * @param value
	 */
	public void set(String value) {
		_entity.setEquip( _elements[ 2], value);
	}

	/**
	 * @param value
	 */
	public void set(int value) {
		IntValue intValue = new IntValue();
		intValue.setInteger( value);
		_entity.setEquip( _elements[ 2], intValue);
	}

	/**
	 * @param value
	 */
	public void set(double value) {
		DoubleValue doubleValue = new DoubleValue();
		doubleValue.setDouble( value);
		_entity.setEquip( _elements[ 2], doubleValue);
	}
}
