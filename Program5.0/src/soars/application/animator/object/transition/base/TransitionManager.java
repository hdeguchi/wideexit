/**
 * 
 */
package soars.application.animator.object.transition.base;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Vector;

/**
 * The scenario data manager. The array of the hashtable(oblect[AgentObject or SpotObject] - TransitionBase).
 * @author kurata / SOARS project
 */
public class TransitionManager extends Vector {

	/**
	 * Creates the scenario data manager.
	 */
	public TransitionManager() {
		super();
	}

	/**
	 * Clears all.
	 */
	public void cleanup() {
		for ( int i = 0; i < size(); ++i) {
			Map transitionMap = ( Map)get( i);
			Iterator iterator = transitionMap.entrySet().iterator();
			while ( iterator.hasNext()) {
				Object object = iterator.next();
				Map.Entry entry = ( Map.Entry)object;
				TransitionBase transitionBase = ( TransitionBase)entry.getValue();
				if ( null == transitionBase)
					continue;

				transitionBase.cleanup();
			}
			transitionMap.clear();
		}
		clear();
	}

	/**
	 * Appnds a empty hashtable.
	 */
	public void append() {
		add( new HashMap());
	}

	/**
	 * Returns the scenario data which corresponds to the specified object and positon of the scenario.
	 * @param object the specified object
	 * @param index the specified positon of the scenario
	 * @return the scenario data which corresponds to the specified object and positon of the scenario
	 */
	public TransitionBase get(Object object, int index) {
		if ( size() <= index)
			return null;

		Map transitionMap = ( Map)get( index);
		return ( TransitionBase)transitionMap.get( object);
	}

	/**
	 * @return
	 */
	protected TransitionBase createTransition() {
		return null;
	}

	/**
	 * Returns true if the scenario data is set.
	 * @param object the specified object
	 * @param index the specified positon of the scenario
	 * @param name the name of the property
	 * @param value the value of the property
	 * @return true if the scenario data is set
	 */
	public boolean set_property(Object object, int index, String name, String value) {
		Map transitionMap = ( Map)get( index);
		TransitionBase transitionBase = ( TransitionBase)transitionMap.get( object);
		if ( null == transitionBase) {
			transitionBase = createTransition();
			transitionMap.put( object, transitionBase);
		}
		transitionBase._properties.put( name, value);
		return true;
	}

//	/**
//	 * @param object
//	 * @param name
//	 * @param value
//	 * @param index
//	 * @return
//	 */
//	public int retrieve_backward(Object object, String name, String value, int index) {
//		if ( size() < index)
//			return -1;
//
//		for ( int i = index - 1; i >= 0; --i) {
//			Map transitionMap = ( Map)get( i);
//			TransitionBase transitionBase = ( TransitionBase)transitionMap.get( object);
//			if ( null == transitionBase)
//				continue;
//
//			String v = ( String)transitionBase._properties.get( name);
//			if ( null != v && v.equals( value))
//				return i;
//		}
//
//		return -1;
//	}
//
//	/**
//	 * @param object
//	 * @param name
//	 * @param value
//	 * @param index
//	 * @return
//	 */
//	public int retrieve_backward_more_than(Object object, String name, double value, int index) {
//		if ( size() < index)
//			return -1;
//
//		for ( int i = index - 1; i >= 0; --i) {
//			Map transitionMap = ( Map)get( i);
//			TransitionBase transitionBase = ( TransitionBase)transitionMap.get( object);
//			if ( null == transitionBase)
//				continue;
//
//			String text = ( String)transitionBase._properties.get( name);
//			if ( null == text)
//				return -1;
//
//			double v;
//			try {
//				v = Double.parseDouble( text);
//			} catch (NumberFormatException e) {
//				continue;
//			}
//
//			if ( v >= value)
//				return i;
//		}
//
//		return -1;
//	}
//
//	/**
//	 * @param object
//	 * @param name
//	 * @param value
//	 * @param index
//	 * @return
//	 */
//	public int retrieve_backward_less_than(Object object, String name, double value, int index) {
//		if ( size() < index)
//			return -1;
//
//		for ( int i = index - 1; i >= 0; --i) {
//			Map transitionMap = ( Map)get( i);
//			TransitionBase transitionBase = ( TransitionBase)transitionMap.get( object);
//			if ( null == transitionBase)
//				continue;
//
//			String text = ( String)transitionBase._properties.get( name);
//			if ( null == text)
//				return -1;
//
//			double v;
//			try {
//				v = Double.parseDouble( text);
//			} catch (NumberFormatException e) {
//				continue;
//			}
//
//			if ( v <= value)
//				return i;
//		}
//
//		return -1;
//	}
//
//	/**
//	 * @param object
//	 * @param name
//	 * @param value0
//	 * @param value1
//	 * @param index
//	 * @return
//	 */
//	public int retrieve_backward_more_than_less_than(Object object, String name, double value0, double value1, int index) {
//		if ( size() < index)
//			return -1;
//
//		for ( int i = index - 1; i >= 0; --i) {
//			Map transitionMap = ( Map)get( i);
//			TransitionBase transitionBase = ( TransitionBase)transitionMap.get( object);
//			if ( null == transitionBase)
//				continue;
//
//			String text = ( String)transitionBase._properties.get( name);
//			if ( null == text)
//				return -1;
//
//			double v;
//			try {
//				v = Double.parseDouble( text);
//			} catch (NumberFormatException e) {
//				continue;
//			}
//
//			if ( v >= value0 && v <= value1)
//				return i;
//		}
//
//		return -1;
//	}

	/**
	 * Returns the position of the scenario if the specified value is found forward.
	 * @param object the specified object
	 * @param name the specified name
	 * @param value the specified value
	 * @param index the start position of the scenario
	 * @return the position of the scenario if the specified value is found forward
	 */
	public int retrieve_forward(Object object, String name, String value, int index) {
		if ( size() < index)
			return -1;

		for ( int i = index + 1; i < size(); ++i) {
			Map transitionMap = ( Map)get( i);
			TransitionBase transitionBase = ( TransitionBase)transitionMap.get( object);
			if ( null == transitionBase)
				continue;

			String v = ( String)transitionBase._properties.get( name);
			if ( null != v && v.equals( value))
				return i;
		}

		return -1;
	}

	/**
	 * Returns the position of the scenario if the specified value, which is not less than the specified value, is found forward.
	 * @param object the specified object
	 * @param name the specified name
	 * @param value the specified value
	 * @param index the start position of the scenario
	 * @return the position of the scenario if the specified value, which is not less than the specified value, is found forward
	 */
	public int retrieve_forward_more_than(Object object, String name, double value, int index) {
		if ( size() < index)
			return -1;

		for ( int i = index + 1; i < size(); ++i) {
			Map transitionMap = ( Map)get( i);
			TransitionBase transitionBase = ( TransitionBase)transitionMap.get( object);
			if ( null == transitionBase)
				continue;

			String text = ( String)transitionBase._properties.get( name);
			if ( null == text)
				return -1;

			double v;
			try {
				v = Double.parseDouble( text);
			} catch (NumberFormatException e) {
				continue;
			}

			if ( v >= value)
				return i;
		}

		return -1;
	}

	/**
	 * Returns the position of the scenario if the specified value, which is not more than the specified value, is found forward.
	 * @param object the specified object
	 * @param name the specified name
	 * @param value the specified value
	 * @param index the start position of the scenario
	 * @return the position of the scenario if the specified value, which is not more than the specified value, is found forward
	 */
	public int retrieve_forward_less_than(Object object, String name, double value, int index) {
		if ( size() < index)
			return -1;

		for ( int i = index + 1; i < size(); ++i) {
			Map transitionMap = ( Map)get( i);
			TransitionBase transitionBase = ( TransitionBase)transitionMap.get( object);
			if ( null == transitionBase)
				continue;

			String text = ( String)transitionBase._properties.get( name);
			if ( null == text)
				return -1;

			double v;
			try {
				v = Double.parseDouble( text);
			} catch (NumberFormatException e) {
				continue;
			}

			if ( v <= value)
				return i;
		}

		return -1;
	}

	/**
	 * Returns the position of the scenario if the specified value, which is not less than the value0 and not more than value1, is found forward.
	 * @param object the specified object
	 * @param name the specified name
	 * @param value0 the specified value
	 * @param value1 the specified value
	 * @param index the start position of the scenario
	 * @return the position of the scenario if the specified value, which is not less than the value0 and not more than value1, is found forward
	 */
	public int retrieve_forward_more_than_less_than(Object object, String name, double value0, double value1, int index) {
		if ( size() < index)
			return -1;

		for ( int i = index + 1; i < size(); ++i) {
			Map transitionMap = ( Map)get( i);
			TransitionBase transitionBase = ( TransitionBase)transitionMap.get( object);
			if ( null == transitionBase)
				continue;

			String text = ( String)transitionBase._properties.get( name);
			if ( null == text)
				return -1;

			double v;
			try {
				v = Double.parseDouble( text);
			} catch (NumberFormatException e) {
				continue;
			}

			if ( v >= value0 && v <= value1)
				return i;
		}

		return -1;
	}

	/**
	 * Returns the tool tip text of the specified object.
	 * @param object the specified object
	 * @param selectedProperties the array of the visible properties
	 * @param index the position of the scenario
	 * @return the tool tip text of the specified object
	 */
	public String get_tooltip_text(Object object, Vector selectedProperties, int index) {
		if ( selectedProperties.isEmpty())
			return "";

		if ( size() <= index)
			return "";

		Map transitionMap = ( Map)get( index);
		TransitionBase transitionBase = ( TransitionBase)transitionMap.get( object);

		String result = "";
		for ( int i = 0; i < selectedProperties.size(); ++i) {
			String name = ( String)selectedProperties.get( i);
			String value = ( null == transitionBase) ? "" : ( String)transitionBase._properties.get( name);
			if ( null == value)
				value = "";

			if ( !result.equals( ""))
				result += ", ";

			result += ( name + " = \"" + value + "\"");
		}

		if ( result.equals( ""))
			return "";

		return " : " + result;
	}
}
