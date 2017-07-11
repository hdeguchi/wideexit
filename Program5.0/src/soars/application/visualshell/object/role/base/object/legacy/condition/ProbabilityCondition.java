/**
 * 
 */
package soars.application.visualshell.object.role.base.object.legacy.condition;

import java.util.Vector;

import soars.application.visualshell.common.tool.CommonTool;
import soars.application.visualshell.layer.Layer;
import soars.application.visualshell.object.role.base.Role;
import soars.application.visualshell.object.role.base.object.base.Rule;
import soars.application.visualshell.object.role.base.object.common.CommonRuleManipulator;

/**
 * @author kurata
 *
 */
public class ProbabilityCondition extends Rule {

	/**
	 * 
	 */
	static public String _reservedWord = "askEquip ";

	/**
	 * @param kind
	 * @param type
	 * @param value
	 */
	public ProbabilityCondition(String kind, String type, String value) {
		super(kind, type, value);
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.role.base.rule.base.Rule#get_used_spot_names()
	 */
	@Override
	protected String[] get_used_spot_names() {
		return new String[] { get_used_spot_name()};
	}

	/**
	 * @return
	 */
	private String get_used_spot_name() {
		String[] elements = CommonRuleManipulator.get_elements( _value, 1);
		if ( null == elements)
			return null;

		return CommonRuleManipulator.extract_spot_name2( elements[ 0]);
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.role.base.rule.base.Rule#get_used_spot_variable_names(soars.application.visualshell.object.role.base.Role)
	 */
	@Override
	protected String[] get_used_spot_variable_names(Role role) {
		return new String[] { get_used_spot_variable_name()};
	}

	/**
	 * @return
	 */
	private String get_used_spot_variable_name() {
		String[] elements = CommonRuleManipulator.get_elements( _value, 1);
		if ( null == elements)
			return null;

		return CommonRuleManipulator.get_spot_variable_name2( elements[ 0]);
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.role.base.rule.base.Rule#get_used_probability_names()
	 */
	@Override
	protected String[] get_used_probability_names() {
		return new String[] { get_used_probability_name()};
	}

	/**
	 * @return
	 */
	private String get_used_probability_name() {
		String[] elements = CommonRuleManipulator.get_elements( _value, 1);
		if ( null == elements)
			return null;

		return elements[ 0];
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.role.base.rule.base.Rule#get_used_number_object_names()
	 */
	@Override
	protected String[] get_used_number_object_names() {
		return new String[] { get_number_object_name()};
	}

	/**
	 * @return
	 */
	private String get_number_object_name() {
		String[] elements = CommonRuleManipulator.get_elements( _value, 1);
		if ( null == elements)
			return null;

		if ( 2 > elements.length || CommonTool.is_probability_correct( elements[ 1]))
			return null;

		String prefix = CommonRuleManipulator.get_full_prefix( elements[ 0]);
		if ( null == prefix)
			return null;

		return ( prefix + elements[ 1]);
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.role.base.rule.base.Rule#update_spot_name_and_number(java.lang.String, java.lang.String, java.lang.String, java.util.Vector, java.lang.String, java.util.Vector)
	 */
	@Override
	public boolean update_spot_name_and_number(String newName, String originalName, String headName, Vector<String[]> ranges, String newHeadName, Vector<String[]> newRanges) {
		String[] elements = CommonRuleManipulator.get_elements( _value, 1);
		if ( null == elements)
			return false;

		String spot_name = get_used_spot_name();
		if ( null == spot_name)
			return false;

		elements[ 0] = CommonRuleManipulator.update_spot_name2( elements[ 0], newName, originalName, headName, ranges, newHeadName, newRanges);
		if ( null == elements[ 0])
			return false;

		_value = ( ( _value.startsWith( "!") ? "!" : "")
			+ _reservedWord + elements[ 0] + "=");

		if ( 1 < elements.length)
			_value += elements[ 1];

		return true;
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.role.base.rule.base.Rule#update_spot_variable_name(java.lang.String, java.lang.String, java.lang.String, soars.application.visualshell.object.role.base.Role)
	 */
	@Override
	protected boolean update_spot_variable_name(String name, String newName, String type, Role role) {
		String[] elements = CommonRuleManipulator.get_elements( _value, 1);
		if ( null == elements)
			return false;

		elements[ 0] = CommonRuleManipulator.update_spot_variable_name2( elements[ 0], name, newName, type);
		if ( null == elements[ 0])
			return false;

		_value = ( ( _value.startsWith( "!") ? "!" : "")
			+ _reservedWord + elements[ 0] + "=");

		if ( 1 < elements.length)
			_value += elements[ 1];

		return true;
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.role.base.rule.base.Rule#update_probability_name(java.lang.String, java.lang.String, java.lang.String, soars.application.visualshell.object.role.base.Role)
	 */
	@Override
	protected boolean update_probability_name(String name, String newName, String type, Role role) {
		String[] elements = CommonRuleManipulator.get_elements( _value, 1);
		if ( null == elements)
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
	 * @see soars.application.visualshell.object.role.base.rule.base.Rule#update_number_object_name(java.lang.String, java.lang.String, java.lang.String, soars.application.visualshell.object.role.base.Role)
	 */
	@Override
	protected boolean update_number_object_name(String name, String newName, String type, Role role) {
		String[] elements = CommonRuleManipulator.get_elements( _value);
		if ( null == elements || 2 > elements.length || elements[ 1].equals( ""))
			return false;

		String prefix = CommonRuleManipulator.get_full_prefix( elements[ 0]);
		if ( null == prefix || !CommonRuleManipulator.correspond( prefix, elements[ 1], name, type))
			return false;

		elements[ 1] = newName;

		_value = ( ( _value.startsWith( "!") ? "!" : "") + _reservedWord);
		for ( int i = 0; i < elements.length; ++i)
			_value += ( ( ( 0 == i) ? "" : "=") + elements[ i]);

		return true;
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.role.base.rule.base.Rule#can_paste(soars.application.visualshell.object.role.base.Role, soars.application.visualshell.layer.Layer)
	 */
	@Override
	protected boolean can_paste(Role role, Layer drawObjects) {
		if ( !can_paste_probability_name( drawObjects))
			return false;

		if ( !can_paste_number_object_name( drawObjects))
			return false;

		return true;
	}

	/**
	 * @param drawObjects
	 * @return
	 */
	private boolean can_paste_probability_name(Layer drawObjects) {
		String[] elements = CommonRuleManipulator.get_elements( _value, 1);
		if ( null == elements)
			return false;

		return CommonRuleManipulator.can_paste_object( "probability", elements[ 0], drawObjects);
	}

	/**
	 * @param drawObjects
	 * @return
	 */
	private boolean can_paste_number_object_name(Layer drawObjects) {
		String[] elements = CommonRuleManipulator.get_elements( _value, 1);
		if ( null == elements)
			return false;

		if ( 2 > elements.length || CommonTool.is_probability_correct( elements[ 1]))
			return true;

		String prefix = CommonRuleManipulator.get_full_prefix( elements[ 0]);
		if ( null == prefix)
			return false;

		return CommonRuleManipulator.can_paste_object( "number object", prefix + elements[ 1], drawObjects);
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.role.base.rule.base.Rule#get_script(java.lang.String, soars.application.visualshell.object.role.base.Role)
	 */
	@Override
	protected String get_script(String value, Role role) {
		String denial = ( value.startsWith( "!") ? "!" : "");

		String method = CommonRuleManipulator.get_reserved_word( value);
		if ( null == method)
			return "";

		String[] elements = CommonRuleManipulator.get_elements( value);
		if ( null == elements)
			return "";

		String[] prefixAndObject = CommonRuleManipulator.get_prefix_and_object( elements[ 0]);

		String script = ( denial + prefixAndObject[ 0] + method + prefixAndObject[ 1] + "=");

		if ( 1 < elements.length)
			script += elements[ 1];

		return script;
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.role.base.object.base.Rule#get_cell_text(soars.application.visualshell.object.role.base.Role)
	 */
	@Override
	public String get_cell_text(Role role) {
		return get_script( _value, null);
	}
}
