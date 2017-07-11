/**
 * 
 */
package soars.application.animator.object.transition.base;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * The scenario data.
 * @author kurata / SOARS project
 */
public class TransitionBase {

	/**
	 * Property value hashtable(name(String) - value(String)).
	 */
	public Map<String, String> _properties = new HashMap<String, String>();

	/**
	 * Creates the scenario data.
	 */
	public TransitionBase() {
		super();
	}

	/**
	 * Creates the scenario data with the specified scenario data.
	 * @param transitionBase the specified scenario data
	 */
	public TransitionBase(TransitionBase transitionBase) {
		super();
		Iterator iterator = transitionBase._properties.entrySet().iterator();
		while ( iterator.hasNext()) {
			Object object = iterator.next();
			Map.Entry entry = ( Map.Entry)object;
			_properties.put( ( String)entry.getKey(), ( String)entry.getValue());
		}
	}

	/**
	 * Clears all.
	 */
	public void cleanup() {
		_properties.clear();
	}
}
