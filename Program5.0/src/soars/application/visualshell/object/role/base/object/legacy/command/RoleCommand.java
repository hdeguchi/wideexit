/**
 * 
 */
package soars.application.visualshell.object.role.base.object.legacy.command;

import soars.application.visualshell.layer.ILayerManipulator;
import soars.application.visualshell.layer.Layer;
import soars.application.visualshell.layer.LayerManager;
import soars.application.visualshell.object.role.agent.AgentRole;
import soars.application.visualshell.object.role.base.Role;
import soars.application.visualshell.object.role.base.object.base.Rule;
import soars.application.visualshell.object.role.spot.SpotRole;

/**
 * @author kurata
 *
 */
public class RoleCommand extends Rule {

	/**
	 * @param value
	 * @return
	 */
	public static int get_kind(String value) {
		return get_kind( value, LayerManager.get_instance());
	}

	/**
	 * @param value
	 * @param layerManipulator
	 * @return
	 */
	private static int get_kind(String value, ILayerManipulator layerManipulator) {
		if ( value.equals( ""))
			return 1;
		if ( layerManipulator.is_agent_object_name( "role variable", value))
			return 0;
		if ( layerManipulator.is_agent_role_name( value))
			return 1;

		return -1;
	}

	/**
	 * @param kind
	 * @param type
	 * @param value
	 */
	public RoleCommand(String kind, String type, String value) {
		super(kind, type, value);
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.role.base.rule.base.Rule#get_used_role_variable_names()
	 */
	@Override
	protected String[] get_used_role_variable_names() {
		return new String[] { get_used_role_variable_name( LayerManager.get_instance())};
	}

	/**
	 * @param layerManipulator
	 * @return
	 */
	private String get_used_role_variable_name(ILayerManipulator layerManipulator) {
		if ( _value.equals( ""))
			return null;

		return ( layerManipulator.is_agent_object_name( "role variable", _value) ? _value : null);
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.role.base.rule.base.Rule#get_used_role_names()
	 */
	@Override
	protected String[] get_used_role_names() {
		return new String[] { get_used_role_name( LayerManager.get_instance())};
	}

	/**
	 * @param layerManipulator
	 * @return
	 */
	private String get_used_role_name(ILayerManipulator layerManipulator) {
		if ( _value.equals( ""))
			return null;

		return ( layerManipulator.is_agent_role_name( _value) ? _value : null);
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.role.base.rule.base.Rule#update_role_variable_name(java.lang.String, java.lang.String, java.lang.String, soars.application.visualshell.object.role.base.Role)
	 */
	@Override
	protected boolean update_role_variable_name(String name, String newName, String type, Role role) {
		if ( _value.equals( ""))
			return false;

		if ( !_value.equals( name))
			return false;

		if ( !LayerManager.get_instance().is_agent_object_name( "role variable", _value))
			return false;

		_value = newName;

		return true;
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.role.base.rule.base.Rule#update_role_name(java.lang.String, java.lang.String)
	 */
	@Override
	public boolean update_role_name(String originalName, String name) {
		if ( _value.equals( ""))
			return false;

		if ( !_value.equals( originalName))
			return false;

		if ( LayerManager.get_instance().is_agent_object_name( "role variable", _value))
			return false;

		_value = name;

		return true;
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.role.base.rule.base.Rule#can_paste(soars.application.visualshell.object.role.base.Role, soars.application.visualshell.layer.Layer)
	 */
	@Override
	protected boolean can_paste(Role role, Layer drawObjects) {
		if ( ( role instanceof AgentRole) && !_value.equals( "")
			&& !drawObjects.is_agent_object_name( "role variable", _value)
			&& !drawObjects.is_agent_role_name( _value))
			return false;

		if ( ( role instanceof SpotRole) && !_value.equals( "")
			&& !drawObjects.is_spot_role_name( _value))
			return false;

		return true;
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.role.base.rule.base.Rule#get_script(java.lang.String, soars.application.visualshell.object.role.base.Role)
	 */
	@Override
	protected String get_script(String value, Role role) {
		if ( role instanceof AgentRole) {
			Role r = LayerManager.get_instance().get_agent_role( value);
			return ( "activateRole " + ( ( null == r) ? value : r.get_name()));
		} else if ( role instanceof SpotRole) {
			Role r = LayerManager.get_instance().get_spot_role( value);
			return ( "startRule " + ( ( null == r) ? value : r.get_name()) + " ; stopRule");
		} else
			return null;
	}

	/**
	 * @param value
	 */
	public static String update_role_name(String value) {
		if ( value.equals( ""))
			return value;

		if ( 0 > value.indexOf( ":"))
			return value;

		String[] roles = value.split( ":");
		if ( null == roles || 0 == roles.length)
			return value;

		return roles[ 0];
	}
}
