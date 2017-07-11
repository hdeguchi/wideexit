/**
 * 
 */
package soars.application.visualshell.object.entity.base.object.time;

import java.nio.IntBuffer;

import soars.application.visualshell.main.ResourceManager;
import soars.application.visualshell.object.entity.base.object.base.SimpleVariableObject;
import soars.application.visualshell.object.experiment.InitialValueMap;

/**
 * The time variable class
 * @author kurata / SOARS project
 */
public class TimeVariableObject extends SimpleVariableObject {

	/**
	 * Creates this object.
	 */
	public TimeVariableObject() {
		super("time variable");
	}

	/**
	 * Creates this object with the spcified name and initial value.
	 * @param name the specified name
	 * @param initialValue the specified initial value
	 */
	public TimeVariableObject(String name, String initialValue) {
		super("time variable", name, initialValue);
	}

	/**
	 * Creates this object with the spcified data.
	 * @param name the specified name
	 * @param initialValue the specified initial value
	 * @param comment the specified comment
	 */
	public TimeVariableObject(String name, String initialValue, String comment) {
		super("time variable", name, initialValue, comment);
	}

	/**
	 * Creates this object with the spcified data.
	 * @param timeVariableObject the spcified data
	 */
	public TimeVariableObject(TimeVariableObject timeVariableObject) {
		super(timeVariableObject);
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	public boolean equals(Object object) {
		if ( !( object instanceof TimeVariableObject))
			return false;

		return super.equals( object);
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.entiy.base.object.base.ObjectBase#get_initial_data()
	 */
	public String get_initial_data() {
		return get_initial_data( ResourceManager.get_instance().get( "initial.data.time.variable"));
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.entiy.base.object.base.ObjectBase#get_script(java.lang.String, java.nio.IntBuffer, soars.application.visualshell.object.experiment.InitialValueMap)
	 */
	public String get_script(String prefix, IntBuffer counter, InitialValueMap initialValueMap) {
		String script = ( "\t" + prefix + "setTime " + _name + "=");

		String initial_value = ( ( null == initialValueMap) ? _initialValue : initialValueMap.get_initial_value( _initialValue));
		script += ( ( null != initial_value && !initial_value.equals( "")) ? initial_value : "0:00");

		script += ( " ; " + prefix + "logEquip " + _name + "=$Time." + _name);

		counter.put( 0, counter.get( 0) + 1);

		return script;
	}
}
