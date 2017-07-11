/**
 * 
 */
package soars.application.visualshell.object.role.base.object.legacy.command;

import java.util.Vector;

import soars.application.visualshell.layer.Layer;
import soars.application.visualshell.layer.LayerManager;
import soars.application.visualshell.main.Constant;
import soars.application.visualshell.main.Environment;
import soars.application.visualshell.object.entity.base.object.class_variable.ClassVariableObject;
import soars.application.visualshell.object.entity.spot.SpotObject;
import soars.application.visualshell.object.role.agent.AgentRole;
import soars.application.visualshell.object.role.base.Role;
import soars.application.visualshell.object.role.base.object.base.Rule;
import soars.application.visualshell.object.role.base.object.common.CommonRuleManipulator;
import soars.application.visualshell.object.role.spot.SpotRole;

/**
 * @author kurata
 *
 */
public class CreateObjectCommand extends Rule {

	/**
	 * 
	 */
	public static final String[] _reservedWords = {
		"Probability ",
		"Collection ",
		"List ",
		"Map ",
		"Keyword ",
		"NumberObject ",
		"Role ",
		"TimeVariable ",
		"SpotVariable ",
		"ClassObject "
	};

	/**
	 * @param value
	 * @return
	 */
	public static int get_kind(String value) {
		for ( int i = 0; i < _reservedWords.length; ++i) {
			if ( value.startsWith( _reservedWords[ i]))
				return i;
		}
		return -1;
	}

	/**
	 * @param spotName
	 * @param numberObjectName
	 * @return
	 */
	public static String get_number_object_type(String spotName, String numberObjectName) {
		if ( spotName.equals( ""))
			return LayerManager.get_instance().get_agent_number_object_type( numberObjectName);
		else if ( spotName.equals( "<>"))
			return LayerManager.get_instance().get_spot_number_object_type( numberObjectName);
		else {
			SpotObject spotObject = LayerManager.get_instance().get_spot_has_this_name( spotName);
			if ( null == spotObject)
				return null;

			return spotObject.get_number_object_type( numberObjectName);
		}
	}

	/**
	 * @param kind
	 * @param type
	 * @param value
	 */
	public CreateObjectCommand(String kind, String type, String value) {
		super(kind, type, value);
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.role.base.rule.base.Rule#get_used_spot_names()
	 */
	@Override
	protected String[] get_used_spot_names() {
		String[] elements = CommonRuleManipulator.get_elements( _value);
		if ( null == elements || 1 > elements.length)
			return null;

		return new String[] {
			CommonRuleManipulator.extract_spot_name2( elements[ 0]),
			( 8 == get_kind( _value) && 1 < elements.length && null != elements[ 1] && !elements[ 1].equals( "")) ? elements[ 1] : null
		};
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.role.base.rule.base.Rule#get_used_spot_variable_names(soars.application.visualshell.object.role.base.Role)
	 */
	@Override
	protected String[] get_used_spot_variable_names(Role role) {
		return new String[] { get_used_spot_variable_name(), get_used_object_name( 8)};
	}

	/**
	 * @return
	 */
	private String get_used_spot_variable_name() {
		String[] elements = CommonRuleManipulator.get_elements( _value);
		if ( null == elements || 1 > elements.length)
			return null;

		return CommonRuleManipulator.get_spot_variable_name2( elements[ 0]);
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.role.base.rule.base.Rule#get_used_probability_names()
	 */
	@Override
	protected String[] get_used_probability_names() {
		return new String[] { get_used_object_name( 0)};
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.role.base.rule.base.Rule#get_used_collection_names()
	 */
	@Override
	protected String[] get_used_collection_names() {
		return new String[] { get_used_object_name( 1)};
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.role.base.rule.base.Rule#get_used_list_names()
	 */
	@Override
	protected String[] get_used_list_names() {
		return new String[] { get_used_object_name( 2)};
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.role.base.rule.base.Rule#get_used_map_names()
	 */
	@Override
	protected String[] get_used_map_names() {
		return new String[] { get_used_object_name( 3)};
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.role.base.rule.base.Rule#get_used_keyword_names()
	 */
	@Override
	protected String[] get_used_keyword_names() {
		return new String[] { get_used_object_name( 4)};
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.role.base.rule.base.Rule#get_used_number_object_names()
	 */
	@Override
	protected String[] get_used_number_object_names() {
		return new String[] { get_used_object_name( 5)};
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.role.base.rule.base.Rule#get_used_role_variable_names()
	 */
	@Override
	protected String[] get_used_role_variable_names() {
		return new String[] { get_used_object_name( 6)};
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.role.base.rule.base.Rule#get_used_role_names()
	 */
	@Override
	protected String[] get_used_role_names() {
		if ( 6 != get_kind( _value))
			return null;

		String[] elements = CommonRuleManipulator.get_elements( _value, 2);
		if ( null == elements)
			return null;

		return new String[] { elements[ 1]};
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.role.base.rule.base.Rule#get_used_time_variable_names()
	 */
	@Override
	protected String[] get_used_time_variable_names() {
		return new String[] { get_used_object_name( 7)};
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.role.base.object.base.Rule#get_used_class_variable_names(soars.application.visualshell.object.role.base.Role)
	 */
	@Override
	protected String[] get_used_class_variable_names(Role role) {
		return new String[] { get_used_object_name( 9)};
	}

	/**
	 * @param kind
	 * @return
	 */
	private String get_used_object_name(int kind) {
		if ( get_kind( _value) != kind)
			return null;

		String[] elements = CommonRuleManipulator.get_elements( _value);
		if ( null == elements || 1 > elements.length)
			return null;

		return elements[ 0];
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.role.base.rule.base.Rule#update_spot_name_and_number(java.lang.String, java.lang.String, java.lang.String, java.util.Vector, java.lang.String, java.util.Vector)
	 */
	@Override
	public boolean update_spot_name_and_number(String newName, String originalName, String headName, Vector<String[]> ranges, String newHeadName, Vector<String[]> newRanges) {
		String[] elements = CommonRuleManipulator.get_elements( _value);
		if ( null == elements || 1 > elements.length)
			return false;

		boolean result1 = false;
		String element = CommonRuleManipulator.update_spot_name2( elements[ 0], newName, originalName, headName, ranges, newHeadName, newRanges);
		if ( null != element) {
			elements[ 0] = element;
			result1 = true;
		}

		boolean result2 = false;
		int kind = get_kind( _value);
		if ( 8 == get_kind( _value) && 1 < elements.length) {
			element = CommonRuleManipulator.get_new_agent_or_spot_name( elements[ 1], newName, originalName, headName, ranges, newHeadName, newRanges);
			if ( null != element) {
				elements[ 1] = element;
				result2 = true;
			}
		}

		if ( !result1 && !result2)
			return false;

		_value = ( _reservedWords[ kind] + elements[ 0]);

		if ( 0 == kind || 4 == kind || 5 == kind || 7 == kind || 8 == kind) {
			_value += "=";
			if ( 1 < elements.length)
				_value += elements[ 1];
		}

		return true;
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.role.base.rule.base.Rule#update_spot_variable_name(java.lang.String, java.lang.String, java.lang.String, soars.application.visualshell.object.role.base.Role)
	 */
	@Override
	protected boolean update_spot_variable_name(String name, String newName, String type, Role role) {
		boolean result1 = update_object_name( name, newName, type);

		String[] elements = CommonRuleManipulator.get_elements( _value);
		if ( null == elements || 1 > elements.length)
			return result1;

		elements[ 0] = CommonRuleManipulator.update_spot_variable_name2( elements[ 0], name, newName, type);
		if ( null == elements[ 0])
			return result1;

		int kind = get_kind( _value);
		if ( 0 > kind)
			return result1;

		_value = ( _reservedWords[ kind] + elements[ 0]);

		if ( 0 == kind || 4 == kind || 5 == kind || 7 == kind || 8 == kind) {
			_value += "=";
			if ( 1 < elements.length)
				_value += elements[ 1];
		}

		return true;
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.role.base.rule.base.Rule#update_probability_name(java.lang.String, java.lang.String, java.lang.String, soars.application.visualshell.object.role.base.Role)
	 */
	@Override
	protected boolean update_probability_name(String name, String newName, String type, Role role) {
		return update_object_name( name, newName, type);
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.role.base.rule.base.Rule#update_collection_name(java.lang.String, java.lang.String, java.lang.String, soars.application.visualshell.object.role.base.Role)
	 */
	@Override
	protected boolean update_collection_name(String name, String newName, String type, Role role) {
		return update_object_name( name, newName, type);
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.role.base.rule.base.Rule#update_list_name(java.lang.String, java.lang.String, java.lang.String, soars.application.visualshell.object.role.base.Role)
	 */
	@Override
	protected boolean update_list_name(String name, String newName, String type, Role role) {
		return update_object_name( name, newName, type);
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.role.base.rule.base.Rule#update_map_name(java.lang.String, java.lang.String, java.lang.String, soars.application.visualshell.object.role.base.Role)
	 */
	@Override
	protected boolean update_map_name(String name, String newName, String type, Role role) {
		return update_object_name( name, newName, type);
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.role.base.rule.base.Rule#update_keyword_name(java.lang.String, java.lang.String, java.lang.String, soars.application.visualshell.object.role.base.Role)
	 */
	@Override
	protected boolean update_keyword_name(String name, String newName, String type, Role role) {
		return update_object_name( name, newName, type);
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.role.base.rule.base.Rule#update_number_object_name(java.lang.String, java.lang.String, java.lang.String, soars.application.visualshell.object.role.base.Role)
	 */
	@Override
	protected boolean update_number_object_name(String name, String newName, String type, Role role) {
		return update_object_name( name, newName, type);
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.role.base.rule.base.Rule#update_role_variable_name(java.lang.String, java.lang.String, java.lang.String, soars.application.visualshell.object.role.base.Role)
	 */
	@Override
	protected boolean update_role_variable_name(String name, String newName, String type, Role role) {
		return update_object_name( name, newName, type);
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.role.base.rule.base.Rule#update_role_name(java.lang.String, java.lang.String)
	 */
	@Override
	public boolean update_role_name(String originalName, String name) {
		if ( 6 != get_kind( _value))
			return false;

		String[] elements = CommonRuleManipulator.get_elements( _value, 2);
		if ( null == elements)
			return false;

		if ( !elements[ 1].equals( originalName))
			return false;

		_value = ( _reservedWords[ 6] + elements[ 0] + "=" + name);

		return true;
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.role.base.rule.base.Rule#update_time_variable_name(java.lang.String, java.lang.String, java.lang.String, soars.application.visualshell.object.role.base.Role)
	 */
	@Override
	protected boolean update_time_variable_name(String name, String newName, String type, Role role) {
		return update_object_name( name, newName, type);
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.role.base.rule.base.Rule#update_class_variable_name(java.lang.String, java.lang.String, java.lang.String, soars.application.visualshell.object.role.base.Role)
	 */
	@Override
	protected boolean update_class_variable_name(String name, String newName, String type, Role role) {
		return update_object_name( name, newName, type);
	}

	/**
	 * @param name
	 * @param newName
	 * @param type
	 * @return
	 */
	private boolean update_object_name(String name, String newName, String type) {
		String[] elements = CommonRuleManipulator.get_elements( _value);
		if ( null == elements || 1 > elements.length)
			return false;

		elements[ 0] = CommonRuleManipulator.update_object_name( elements[ 0], name, newName, type);
		if ( null == elements[ 0])
			return false;

		int kind = get_kind( _value);
		if ( 0 > kind)
			return false;

		_value = ( _reservedWords[ kind] + elements[ 0]);

		if ( 0 == kind || 4 == kind || 5 == kind || 6 == kind || 7 == kind || 8 == kind) {
			_value += "=";
			if ( 1 < elements.length)
				_value += elements[ 1];
		}

		return true;
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.role.base.rule.base.Rule#can_paste(soars.application.visualshell.object.role.base.Role, soars.application.visualshell.layer.Layer)
	 */
	@Override
	protected boolean can_paste(Role role, Layer drawObjects) {
		int kind = get_kind( _value);
		if ( 0 > kind)
			return false;

		if ( !can_paste_spot_name( kind, drawObjects))
			return false;

		switch ( kind) {
			case 0:
				return can_paste_probability_name( drawObjects);
			case 1:
				return can_paste_collection_name( drawObjects);
			case 2:
				return can_paste_list_name( drawObjects);
			case 3:
				return can_paste_map_name( drawObjects);
			case 4:
				return can_paste_keyword_name( drawObjects);
			case 5:
				return can_paste_number_object_name( drawObjects);
			case 6:
				return can_paste_role_variable_name( drawObjects);
			case 7:
				return can_paste_time_variable_name( drawObjects);
			case 8:
				return can_paste_spot_variable_name( drawObjects);
			case 9:
				return can_paste_class_variable_name( drawObjects);
		}

		return false;
	}

	/**
	 * @param kind
	 * @param drawObjects
	 * @return
	 */
	private boolean can_paste_spot_name(int kind, Layer drawObjects) {
		String[] elements = CommonRuleManipulator.get_elements( _value);
		if ( null == elements || 1 > elements.length)
			return false;

		boolean result1 = CommonRuleManipulator.can_paste_spot_and_spot_variable_name2( elements[ 0], drawObjects);

		boolean result2 = true;
		if ( 8 == kind && 1 < elements.length && null != elements[ 1] && !elements[ 1].equals( ""))
			result2 = ( null != drawObjects.get_spot_has_this_name( elements[ 1]));

		return ( result1 && result2);
	}

	/**
	 * @param drawObjects
	 * @return
	 */
	private boolean can_paste_probability_name(Layer drawObjects) {
		return can_paste_object_name( "probability", drawObjects);
	}

	/**
	 * @param drawObjects
	 * @return
	 */
	private boolean can_paste_collection_name(Layer drawObjects) {
		return can_paste_object_name( "collection", drawObjects);
	}

	/**
	 * @param drawObjects
	 * @return
	 */
	private boolean can_paste_list_name(Layer drawObjects) {
		return can_paste_object_name( "list", drawObjects);
	}

	/**
	 * @param drawObjects
	 * @return
	 */
	private boolean can_paste_map_name(Layer drawObjects) {
		return can_paste_object_name( "map", drawObjects);
	}

	/**
	 * @param drawObjects
	 * @return
	 */
	private boolean can_paste_keyword_name(Layer drawObjects) {
		return can_paste_object_name( "keyword", drawObjects);
	}

	/**
	 * @param drawObjects
	 * @return
	 */
	private boolean can_paste_number_object_name(Layer drawObjects) {
		return can_paste_object_name( "number object", drawObjects);
	}

	/**
	 * @param drawObjects
	 * @return
	 */
	private boolean can_paste_role_variable_name(Layer drawObjects) {
		String[] elements = CommonRuleManipulator.get_elements( _value);
		if ( null == elements || 1 > elements.length)
			return false;

		if ( null != elements[ 0] && !elements[ 0].equals( "")
			&& !drawObjects.is_agent_object_name( "role variable", elements[ 0]))
			return false;

		if ( 1 < elements.length && null != elements[ 1] && !elements[ 1].equals( "")
			&& !drawObjects.is_agent_role_name( elements[ 1]))
			return false;

		return true;
	}

	/**
	 * @param drawObjects
	 * @return
	 */
	private boolean can_paste_time_variable_name(Layer drawObjects) {
		return can_paste_object_name( "time variable", drawObjects);
	}

	/**
	 * @param drawObjects
	 * @return
	 */
	private boolean can_paste_spot_variable_name(Layer drawObjects) {
		return can_paste_object_name( "spot variable", drawObjects);
	}

	/**
	 * @param drawObjects
	 * @return
	 */
	private boolean can_paste_class_variable_name(Layer drawObjects) {
		if ( !Environment.get_instance().is_functional_object_enable())
			return false;

		return can_paste_object_name( "class variable", drawObjects);
	}

	/**
	 * @param kind
	 * @param drawObjects
	 * @return
	 */
	private boolean can_paste_object_name(String kind, Layer drawObjects) {
		String[] elements = CommonRuleManipulator.get_elements( _value);
		if ( null == elements || 1 > elements.length)
			return false;

		return CommonRuleManipulator.can_paste_object( kind, elements[ 0], drawObjects);
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.role.base.rule.base.Rule#get_script(java.lang.String, soars.application.visualshell.object.role.base.Role)
	 */
	@Override
	protected String get_script(String value, Role role) {
		int kind = get_kind( value);
		if ( 0 > kind)
			return "";

		switch ( kind) {
			case 0:
				return get_probability_script( value, role);
			case 1:
				return get_collection_script( value, role);
			case 2:
				return get_list_script( value, role);
			case 3:
				return get_map_script( value, role);
			case 4:
				return get_keyword_script( value, role);
			case 5:
				return get_number_object_script( value, role);
			case 6:
				return get_role_variable_script( value, role);
			case 7:
				return get_time_variable_script( value, role);
			case 8:
				return get_spot_variable_script( value, role);
			case 9:
				return get_class_variable_script( value, role);
		}

		return "";
	}

	/**
	 * @param value
	 * @param role
	 * @return
	 */
	private String get_probability_script(String value, Role role) {
		String[] elements = CommonRuleManipulator.get_elements( value);
		if ( null == elements || 1 > elements.length)
			return "";

		String[] words = CommonRuleManipulator.get_prefix_and_object( elements[ 0]);

		String prefix = get_prefix( words[ 0], role);

		String script = ( prefix + "setEquip " + words[ 1] + "=util.DoubleProbability ; " + prefix + "logEquip " + words[ 1]);

		if ( 1 < elements.length)
			script += ( " ; " + prefix + "askEquip " + words[ 1] + "=" + elements[ 1]);

		return script;
	}

	/**
	 * @param value
	 * @param role
	 * @return
	 */
	private String get_collection_script(String value, Role role) {
		String[] elements = CommonRuleManipulator.get_elements( value);
		if ( null == elements || 1 > elements.length)
			return "";

		String[] words = CommonRuleManipulator.get_prefix_and_object( elements[ 0]);

		String prefix = get_prefix( words[ 0], role);

		return ( prefix + "setEquip " + words[ 1] + "=java.util.HashSet ; " + prefix + "logEquip " + words[ 1]);
	}

	/**
	 * @param value
	 * @param role
	 * @return
	 */
	private String get_list_script(String value, Role role) {
		String[] elements = CommonRuleManipulator.get_elements( value);
		if ( null == elements || 1 > elements.length)
			return "";

		String[] words = CommonRuleManipulator.get_prefix_and_object( elements[ 0]);

		String prefix = get_prefix( words[ 0], role);

		return ( prefix + "setEquip " + words[ 1] + "=java.util.LinkedList ; " + prefix + "logEquip " + words[ 1]);
	}

	/**
	 * @param value
	 * @param role
	 * @return
	 */
	private String get_map_script(String value, Role role) {
		String[] elements = CommonRuleManipulator.get_elements( value);
		if ( null == elements || 1 > elements.length)
			return "";

		String[] words = CommonRuleManipulator.get_prefix_and_object( elements[ 0]);

		String prefix = get_prefix( words[ 0], role);

		return ( prefix + "setEquip " + words[ 1] + "=java.util.HashMap ; " + prefix + "logEquip " + words[ 1]);
	}

	/**
	 * @param value
	 * @param role
	 * @return
	 */
	private String get_keyword_script(String value, Role role) {
		String[] elements = CommonRuleManipulator.get_elements( value);
		if ( null == elements || 1 > elements.length)
			return "";

		String[] words = CommonRuleManipulator.get_prefix_and_object( elements[ 0]);

		String prefix = get_prefix( words[ 0], role);

		String script = ( prefix + "keyword " + words[ 1]);

		if ( 1 < elements.length)
			script += ( "=" + elements[ 1]);

		return script;
	}

	/**
	 * @param value
	 * @param role
	 * @return
	 */
	private String get_number_object_script(String value, Role role) {
		String[] elements = CommonRuleManipulator.get_elements( value);
		if ( null == elements || 1 > elements.length)
			return "";

		String[] words = CommonRuleManipulator.get_prefix_and_object( elements[ 0]);

		String spot = CommonRuleManipulator.get_semantic_prefix( words[ 0]);
		if ( null == spot)
			return "";

		String type = get_number_object_type( spot, words[ 1]);

		String prefix = get_prefix( words[ 0], role);

		String script = ( prefix + "setEquip " + words[ 1]);
		if ( type.equals( "integer"))
			script += "=util.IntValue";
		else if ( type.equals( "real number"))
			script += "=util.DoubleValue";
		else
			return "";

		script += ( " ; " + prefix + "logEquip " + words[ 1]);

		if ( 1 < elements.length)
			script += ( " ; " + prefix + "askEquip " + words[ 1] + "=" + elements[ 1]);

		return script;
	}

	/**
	 * @param value
	 * @param role
	 * @return
	 */
	private String get_role_variable_script(String value, Role role) {
		if ( !( role instanceof AgentRole))
			return "";

		String[] elements = CommonRuleManipulator.get_elements( value);
		if ( null == elements || 1 > elements.length)
			return "";

		String script = "setRole ";
		if ( 1 == elements.length)
			script += elements[ 0];
		else {
			Role r = LayerManager.get_instance().get_role( elements[ 1]);
			if ( null == r)
				return null;

			script += ( elements[ 0] + "=" + r.get_name());
		}

		script += ( " ; logEquip " + elements[ 0] + "=$Role." + elements[ 0]);

		return script;
	}

	/**
	 * @param value
	 * @param role
	 * @return
	 */
	private String get_time_variable_script(String value, Role role) {
		String[] elements = CommonRuleManipulator.get_elements( value);
		if ( null == elements || 1 > elements.length)
			return "";

		String[] words = CommonRuleManipulator.get_prefix_and_object( elements[ 0]);

		String prefix = get_prefix( words[ 0], role);

		String script = ( prefix + "setTime " + words[ 1]);

		if ( 1 < elements.length)
			script += ( "=" + elements[ 1]);

		script += " ; " + prefix + "logEquip " + words[ 1] + "=$Time." + words[ 1];

		return script;
	}

	/**
	 * @param value
	 * @param role
	 * @return
	 */
	private String get_spot_variable_script(String value, Role role) {
		String[] elements = CommonRuleManipulator.get_elements( value);
		if ( null == elements || 1 > elements.length)
			return "";

		String[] words = CommonRuleManipulator.get_prefix_and_object( elements[ 0]);

		String script = "";
		if ( words[ 0].equals( "")) {
			script = ( "<" + ( ( 1 < elements.length) ? elements[ 1] : "") + ">setSpot " + words[ 1]);
		} else {
			script = ( "<" + ( ( 1 < elements.length) ? elements[ 1] : "") + ">setSpot " + Constant._spotVariableName);
			script += ( " ; " +  words[ 0] + "putEquip " + words[ 1] + "=" + Constant._spotVariableName);
		}

		script += " ; " + words[ 0] + "logEquip " + words[ 1];

		return script;
	}

	/**
	 * @param value
	 * @param role
	 * @return
	 */
	private String get_class_variable_script(String value, Role role) {
		if ( !Environment.get_instance().is_functional_object_enable())
			return "";

		String[] elements = CommonRuleManipulator.get_elements( value);
		if ( null == elements || 1 > elements.length)
			return "";

		String[] words = CommonRuleManipulator.get_prefix_and_object( elements[ 0]);

		String prefix = get_prefix( words[ 0], role);

		ClassVariableObject classVariableObject = LayerManager.get_instance().get_class_variable(
			CommonRuleManipulator.get_semantic_prefix( elements[ 0]), words[ 1]);
		if ( null == classVariableObject)
			return "";

		return ( prefix + "setClass " + classVariableObject._name + "=" + classVariableObject._classname
			+ " ; " + prefix + "logEquip " + classVariableObject._name);
	}

	/**
	 * @param prefix
	 * @param role
	 * @return
	 */
	private String get_prefix(String prefix, Role role) {
		return ( ( prefix.equals( "") && ( role instanceof SpotRole)) ? "<>" : prefix);
	}

	/** Invoked on file loading.
	 * @param value
	 * @return
	 */
	public static String update_role_name(String value) {
		if ( 6 != get_kind( value))
			return value;

		String[] elements = CommonRuleManipulator.get_elements( value, 2);
		if ( null == elements)
			return value;

		String[] roles = elements[ 1].split( ":");
		if ( null == roles || 0 == roles.length)
			return value;

		return ( _reservedWords[ 6] + elements[ 0] + "=" + roles[ 0]);
	}
}
