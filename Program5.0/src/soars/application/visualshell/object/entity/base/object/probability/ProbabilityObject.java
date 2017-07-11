/**
 * 
 */
package soars.application.visualshell.object.entity.base.object.probability;

import java.nio.IntBuffer;

import soars.application.visualshell.main.ResourceManager;
import soars.application.visualshell.object.entity.base.object.base.SimpleVariableObject;
import soars.application.visualshell.object.experiment.InitialValueMap;

/**
 * @author kurata
 *
 */
public class ProbabilityObject extends SimpleVariableObject {

	/**
	 * 
	 */
	public ProbabilityObject() {
		super("probability");
	}

	/**
	 * @param name
	 * @param initialValue
	 */
	public ProbabilityObject(String name, String initialValue) {
		super("probability", name, initialValue);
	}

	/**
	 * @param name
	 * @param initialValue
	 * @param comment
	 */
	public ProbabilityObject(String name, String initialValue, String comment) {
		super("probability", name, initialValue, comment);
	}

	/**
	 * @param probabilityObject
	 */
	public ProbabilityObject(ProbabilityObject probabilityObject) {
		super(probabilityObject);
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	public boolean equals(Object object) {
		if ( !( object instanceof ProbabilityObject))
			return false;

		return super.equals( object);
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.entiy.base.object.base.ObjectBase#get_initial_data()
	 */
	public String get_initial_data() {
		return get_initial_data( ResourceManager.get_instance().get( "initial.data.probability"));
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.entiy.base.object.base.ObjectBase#get_script(java.lang.String, java.nio.IntBuffer, soars.application.visualshell.object.experiment.InitialValueMap)
	 */
	public String get_script(String prefix, IntBuffer counter, InitialValueMap initialValueMap) {
		String script = ( "\t" + prefix + "setEquip " + _name + "=util.DoubleProbability ; " + prefix + "logEquip " + _name);

		String initial_value = ( ( null == initialValueMap) ? _initialValue : initialValueMap.get_initial_value( _initialValue));
		if ( null != initial_value && !initial_value.equals( ""))
			script += " ; " + prefix + "askEquip " + _name + "=" + initial_value;

		counter.put( 0, counter.get( 0) + 1);

		return script;
	}
}
