/**
 * 
 */
package soars.application.visualshell.object.role.base.object.legacy.command;

import soars.application.visualshell.layer.Layer;
import soars.application.visualshell.layer.LayerManager;
import soars.application.visualshell.object.role.base.Role;
import soars.application.visualshell.object.role.base.object.base.Rule;
import soars.application.visualshell.object.role.base.object.common.CommonRuleManipulator;
import soars.application.visualshell.object.role.base.object.legacy.condition.RoleCondition;

/**
 * @author kurata
 *
 */
public class SetRoleCommand extends Rule {

	/**
	 * 
	 */
	static public String _reservedWord = "setRole ";

	/**
	 * @param kind
	 * @param type
	 * @param value
	 */
	public SetRoleCommand(String kind, String type, String value) {
		super(kind, type, value);
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.role.base.rule.base.Rule#get_used_role_variable_names()
	 */
	@Override
	protected String[] get_used_role_variable_names() {
		return new String[] { get_used_role_variable_name()};
	}

	/**
	 * @return
	 */
	private String get_used_role_variable_name() {
		String[] elements = RoleCondition.get_elements( _value);
		if ( null == elements)
			return null;

		switch ( elements.length) {
			case 1:
			case 2:
				if ( null != elements[ 0] && !elements[ 0].equals( ""))
					return elements[ 0];
				break;
		}

		return null;
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.role.base.rule.base.Rule#get_used_role_names()
	 */
	@Override
	protected String[] get_used_role_names() {
		return new String[] { get_used_role_name()};
	}

	/**
	 * @return
	 */
	private String get_used_role_name() {
		String[] elements = CommonRuleManipulator.get_elements( _value, 2);
		if ( null == elements)
			return null;

		return elements[ 1];
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.role.base.rule.base.Rule#update_role_variable_name(java.lang.String, java.lang.String, java.lang.String, soars.application.visualshell.object.role.base.Role)
	 */
	@Override
	protected boolean update_role_variable_name(String name, String newName, String type, Role role) {
		String[] elements = CommonRuleManipulator.get_elements( _value);
		if ( null == elements || 1 > elements.length)
			return false;

		elements[ 0] = CommonRuleManipulator.update_object_name( elements[ 0], name, newName, type);
		if ( null == elements[ 0])
			return false;

		_value = ( _reservedWord + elements[ 0] + "=");

		if ( 1 < elements.length)
			_value += elements[ 1];

		return true;
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.role.base.rule.base.Rule#update_role_name(java.lang.String, java.lang.String)
	 */
	@Override
	public boolean update_role_name(String originalName, String name) {
		String[] elements = _value.split( " ");
		if ( null == elements || 2 > elements.length)
			return false;

		String prefix = elements[ 0];

		elements = elements[ 1].split( "=");
		if ( null == elements || 2 > elements.length)
			return false;

		for ( int i = 0; i < elements.length; ++i) {
			if ( null == elements[ i] || elements[ i].equals( ""))
				return false;
		}

		if ( !elements[ 1].equals( originalName))
			return false;

		_value = ( prefix + " " + elements[ 0] + "=" + name);
		return true;
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.role.base.rule.base.Rule#can_paste(soars.application.visualshell.object.role.base.Role, soars.application.visualshell.layer.Layer)
	 */
	@Override
	protected boolean can_paste(Role role, Layer drawObjects) {
		String[] elements = RoleCondition.get_elements( _value);
		if ( null == elements || 1 > elements.length)
			return true;

		if ( null != elements[ 0] && !elements[ 0].equals( "")
			&& !drawObjects.is_agent_object_name( "role variable", elements[ 0]))
			return false;

		if ( 1 < elements.length && null != elements[ 1] && !elements[ 1].equals( "")
			&& !drawObjects.is_agent_role_name( elements[ 1]))
			return false;

		return true;
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.role.base.rule.base.Rule#get_script(java.lang.String, soars.application.visualshell.object.role.base.Role)
	 */
	@Override
	protected String get_script(String value, Role role) {
		String[] elements = value.split( " ");
		if ( null == elements || 2 > elements.length)
			return value;

		String prefix = elements[ 0];

		elements = elements[ 1].split( "=");
		if ( null == elements || 2 > elements.length)
			return value;

		for ( int i = 0; i < elements.length; ++i) {
			if ( null == elements[ i] || elements[ i].equals( ""))
				return value;
		}

		Role r = LayerManager.get_instance().get_role( elements[ 1]);
		if ( null == r)
			return null;

		return ( prefix + " " + elements[ 0] + "=" + r.get_name());
	}

	/**
	 * @param value
	 */
	public static String update_role_name(String value) {
		String[] elements = value.split( " ");
		if ( null == elements || 2 > elements.length)
			return value;

		String prefix = elements[ 0];

		elements = elements[ 1].split( "=");
		if ( null == elements || 2 > elements.length)
			return value;

		for ( int i = 0; i < elements.length; ++i) {
			if ( null == elements[ i] || elements[ i].equals( ""))
				return value;
		}

		if ( 0 > elements[ 1].indexOf( ":"))
			return value;

		String[] roles = elements[ 1].split( ":");
		if ( null == roles || 0 == roles.length)
			return value;

		return ( prefix + " " + elements[ 0] + "=" + roles[ 0]);
	}
}
