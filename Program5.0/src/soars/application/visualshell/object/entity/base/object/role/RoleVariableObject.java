/**
 * 
 */
package soars.application.visualshell.object.entity.base.object.role;

import java.nio.IntBuffer;
import java.util.Vector;

import soars.application.visualshell.layer.Layer;
import soars.application.visualshell.layer.LayerManager;
import soars.application.visualshell.main.ResourceManager;
import soars.application.visualshell.object.entity.agent.AgentObject;
import soars.application.visualshell.object.entity.base.EntityBase;
import soars.application.visualshell.object.entity.base.object.base.SimpleVariableObject;
import soars.application.visualshell.object.experiment.InitialValueMap;
import soars.application.visualshell.object.role.base.Role;
import soars.common.soars.warning.WarningManager;

/**
 * @author kurata
 *
 */
public class RoleVariableObject extends SimpleVariableObject {

	/**
	 * 
	 */
	public RoleVariableObject() {
		super("role variable");
	}

	/**
	 * @param name
	 * @param initialValue
	 */
	public RoleVariableObject(String name, String initialValue) {
		super("role variable", name, initialValue);
	}

	/**
	 * @param name
	 * @param initialValue
	 * @param comment
	 */
	public RoleVariableObject(String name, String initialValue, String comment) {
		super("role variable", name, initialValue, comment);
	}

	/**
	 * @param roleVariableObject
	 */
	public RoleVariableObject(RoleVariableObject roleVariableObject) {
		super(roleVariableObject);
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	public boolean equals(Object object) {
		if ( !( object instanceof RoleVariableObject))
			return false;

		return super.equals( object);
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.entiy.base.object.base.ObjectBase#can_paste(soars.application.visualshell.object.entiy.base.EntityBase, soars.application.visualshell.layer.Layer)
	 */
	public boolean can_paste(EntityBase entityBase, Layer drawObjects) {
		if ( _initialValue.equals( ""))
			return true;

		if ( entityBase instanceof AgentObject) {
			if ( null == drawObjects.get_agent_role( _initialValue)) {
				String[] message = new String[] {
					"Agent",
					"name = " + entityBase._name,
					"Role variable " + _name + " uses " + _initialValue + "."
				};

				WarningManager.get_instance().add( message);

				return false;
			}
		} else {
			if ( null == drawObjects.get_spot_role( _initialValue)) {
				String[] message = new String[] {
					"Spot",
					"name = " + entityBase._name,
					"Role variable " + _name + " uses " + _initialValue + "."
				};

				WarningManager.get_instance().add( message);

				return false;
			}
		}

		return true;
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.entiy.base.object.base.ObjectBase#update_role_name(java.lang.String, java.lang.String)
	 */
	public boolean update_role_name(String originalName, String name) {
		if ( !_initialValue.equals( originalName))
			return false;

		_initialValue = name;
		return true;
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.entiy.base.object.base.ObjectBase#on_remove_role_name(java.util.Vector)
	 */
	public void on_remove_role_name(Vector<String> roleNames) {
		if ( !roleNames.contains( _initialValue))
			_initialValue = "";
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.entiy.base.object.base.ObjectBase#get_initial_data()
	 */
	public String get_initial_data() {
		return get_initial_data( ResourceManager.get_instance().get( "initial.data.role.variable"));
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.entiy.base.object.base.ObjectBase#get_script(java.lang.String, java.nio.IntBuffer, soars.application.visualshell.object.experiment.InitialValueMap)
	 */
	public String get_script(String prefix, IntBuffer counter, InitialValueMap initialValueMap) {
		String script = ( "\t" + prefix + "setRole " + _name + "=");

		String initial_value = ( ( null == initialValueMap) ? _initialValue : initialValueMap.get_initial_value( _initialValue));
		if ( null != initial_value && !initial_value.equals( "")) {
			Role role = LayerManager.get_instance().get_agent_role( initial_value);
			if ( null != role)
				script += role.get_name();
		}

		script += ( " ; " + prefix + "logEquip " + _name + "=$Role." + _name);

		counter.put( 0, counter.get( 0) + 1);

		return script;
	}
}
