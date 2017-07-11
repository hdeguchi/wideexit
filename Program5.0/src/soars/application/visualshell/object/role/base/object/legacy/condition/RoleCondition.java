/**
 * 
 */
package soars.application.visualshell.object.role.base.object.legacy.condition;

import soars.application.visualshell.layer.Layer;
import soars.application.visualshell.layer.LayerManager;
import soars.application.visualshell.object.role.base.Role;
import soars.application.visualshell.object.role.base.object.base.Rule;
import soars.application.visualshell.object.role.base.object.common.CommonRuleManipulator;

/**
 * @author kurata
 *
 */
public class RoleCondition extends Rule {

	/**
	 * 
	 */
	static public String _reservedWord = "isRole ";

	/**
	 * @param kind
	 * @param type
	 * @param value
	 */
	public RoleCondition(String kind, String type, String value) {
		super(kind, type, value);
	}

	/**
	 * @param value
	 * @return
	 */
	public static String[] get_elements(String value) {
		String[] elements = value.split( " ");
		if ( null == elements || 2 > elements.length)
			return null;

		elements = elements[ 1].split( "=");
		if ( null == elements || 1 > elements.length || 2 < elements.length)
			return null;

		for ( int i = 0; i < elements.length; ++i) {
			if ( null == elements[ i] || elements[ i].equals( ""))
				return null;
		}

		return elements;
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
		String[] elements = get_elements( _value);
		if ( null == elements || 2 > elements.length)
			return null;

		return elements[ 0];
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.role.base.rule.base.Rule#get_used_role_names()
	 */
	@Override
	protected String[] get_used_role_names() {
		return new String[] { get_used_role_name( _value)};
	}

	/**
	 * @param value
	 * @return
	 */
	private static String get_used_role_name(String value) {
		String[] elements = get_elements( value);
		if ( null == elements)
			return null;

		if ( 1 == elements.length)
			return elements[ 0];

		if ( 1 < elements.length)
			return elements[ 1];

		return null;
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

		_value = ( ( _value.startsWith( "!") ? "!" : "")
			+ _reservedWord + elements[ 0] + "=");

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
		if ( null == elements || 1 > elements.length || 2 < elements.length)
			return false;

		for ( int i = 0; i < elements.length; ++i) {
			if ( null == elements[ i] || elements[ i].equals( ""))
				return false;
		}

		switch ( elements.length) {
			case 1:
				if ( elements[ 0].equals( originalName))
					_value = ( prefix + " " + name);
				break;
			case 2:
				if ( elements[ 1].equals( originalName))
					_value = ( prefix + " " + elements[ 0] + "=" + name);
				break;
		}

		return true;
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.role.base.rule.base.Rule#can_paste(soars.application.visualshell.object.role.base.Role, soars.application.visualshell.layer.Layer)
	 */
	@Override
	protected boolean can_paste(Role role, Layer drawObjects) {
		String[] elements = get_elements( _value);
		if ( null == elements || 1 > elements.length)
			return true;

		if ( 1 == elements.length) {
			if ( null != elements[ 0] && !elements[ 0].equals( "")
				&& !drawObjects.is_agent_role_name( elements[ 0]))
				return false;
		} else {
			if ( null != elements[ 0] && !elements[ 0].equals( "")
				&& !drawObjects.is_agent_object_name( "role variable", elements[ 0]))
				return false;

			if ( null != elements[ 1] && !elements[ 1].equals( "")
				&& !drawObjects.is_agent_role_name( elements[ 1]))
				return false;
		}

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
		if ( null == elements || 1 > elements.length || 2 < elements.length)
			return value;

		for ( int i = 0; i < elements.length; ++i) {
			if ( null == elements[ i] || elements[ i].equals( ""))
				return value;
		}

		String roleName = get_used_role_name( value);
		if ( null == roleName || roleName.equals( ""))
			return value;

		Role r = LayerManager.get_instance().get_role( roleName);
		if ( null == r)
			return null;

		switch ( elements.length) {
			case 1:
				value = ( prefix + " " + r.get_name());
				break;
			case 2:
				value = ( prefix + " " + elements[ 0] + "=" + r.get_name());
				break;
		}

		return value;
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
		if ( null == elements || 1 > elements.length || 2 < elements.length)
			return value;

		for ( int i = 0; i < elements.length; ++i) {
			if ( null == elements[ i] || elements[ i].equals( ""))
				return value;
		}

		String role = get_used_role_name( value);
		if ( null == role || role.equals( "") || 0 > role.indexOf( ":"))
			return value;

		String[] roles = role.split( ":");
		if ( null == roles || 0 == roles.length)
			return value;

		switch ( elements.length) {
			case 1:
				value = ( prefix + " " + roles[ 0]);
				break;
			case 2:
				value = ( prefix + " " + elements[ 0] + "=" + roles[ 0]);
				break;
		}

		return value;
	}
}
