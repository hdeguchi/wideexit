/**
 * 
 */
package soars.application.visualshell.object.role.base.object.legacy.command;

import java.util.Arrays;
import java.util.Vector;

import soars.application.visualshell.layer.ILayerManipulator;
import soars.application.visualshell.layer.Layer;
import soars.application.visualshell.layer.LayerManager;
import soars.application.visualshell.object.role.base.Role;
import soars.application.visualshell.object.role.base.object.Rules;
import soars.application.visualshell.object.role.base.object.base.RuleNew;
import soars.application.visualshell.object.role.base.object.common.CommonRuleManipulatorNew;

/**
 * @author kurata
 *
 */
public class MoveCommand extends RuleNew {

	/**
	 * 
	 */
	public static final String[] _reservedWords = new String[] {
		"moveTo ",
		"moveToRandom ",
		"moveToFirst ",
		"moveToLast ",
		"moveToCertain ",
		"moveToKey "
	};

	/**
	 * @param value
	 * @return
	 */
	public static String[] get_values(String value) {
		return get_values( value, LayerManager.get_instance());
	}

	/**
	 * @param value
	 * @param layerManipulator
	 * @return
	 */
	private static String[] get_values(String value, ILayerManipulator layerManipulator) {
		if ( null == get_reservedWord( value))
			return null;

		String[] elements = value.split( " ");
		if ( 2 > elements.length)
			return null;

		return CommonRuleManipulatorNew.get_entity_and_value( elements[ 1], layerManipulator);
	}

	/**
	 * @param value
	 * @return
	 */
	private static String get_reservedWord(String value) {
		for ( String reservedWord:_reservedWords) {
			if ( value.startsWith( reservedWord))
				return reservedWord;
		}
		return null;
	}

	/**
	 * @param kind
	 * @param type
	 * @param value
	 */
	public MoveCommand(String kind, String type, String value) {
		super(kind, type, value);
	}

	/**
	 * @param reservedWords
	 * @return
	 */
	private boolean is_target(String[] reservedWords) {
		if ( 1 == reservedWords.length)
			return _value.startsWith( reservedWords[ 0]);

		String reservedWord = get_reservedWord( _value);
		if ( null == reservedWord || reservedWord.equals( ""))
			return false;

		Arrays.sort( reservedWords);
		return ( 0 <= Arrays.binarySearch( reservedWords, reservedWord));
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.role.base.object.base.Rule#get_used_agent_names()
	 */
	@Override
	protected String[] get_used_agent_names() {
		return new String[] { get_used_agent_name()};
	}

	/**
	 * @return
	 */
	private String get_used_agent_name() {
		String[] values = get_values( _value);
		if ( null == values)
			return null;

		return ( ( ( null == values[ 0]) || ( values[ 0].equals( ""))) ? null : values[ 0]);
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
		String[] values = get_values( _value);
		if ( null == values)
			return null;

		return ( ( ( null == values[ 1]) || ( values[ 1].equals( ""))) ? null : values[ 1]);
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.role.base.rule.base.Rule#get_used_spot_variable_names(soars.application.visualshell.object.role.base.Role)
	 */
	@Override
	protected String[] get_used_spot_variable_names(Role role) {
		String[] values = get_values( _value);
		if ( null == values)
			return null;

		return new String[] { CommonRuleManipulatorNew.get_spot_variable_name( values)};
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.role.base.object.base.Rule#get_used_collection_names()
	 */
	@Override
	protected String[] get_used_collection_names() {
		return get_used_variable_names1( new String[] { _reservedWords[ 1]}, "collection");
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.role.base.object.base.Rule#get_used_list_names()
	 */
	@Override
	protected String[] get_used_list_names() {
		return get_used_variable_names1( new String[] { _reservedWords[ 1], _reservedWords[ 2], _reservedWords[ 3], _reservedWords[ 4]}, "list");
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.role.base.object.base.Rule#get_used_map_names()
	 */
	@Override
	protected String[] get_used_map_names() {
		return get_used_variable_names1( new String[] { _reservedWords[ 1], _reservedWords[ 5]}, "map");
	}

	/**
	 * @param reservedWords
	 * @param kind
	 * @return
	 */
	private String[] get_used_variable_names1(String[] reservedWords, String kind) {
		if ( !is_target( reservedWords))
			return null;

		String[] values = get_values( _value);
		if ( null == values)
			return null;

		if ( null == values[ 4] || values[ 4].equals( ""))
			return null;

		String[] elements = values[ 4].split( "=");
		if ( null == elements || 1 > elements.length || null == elements[ 0] || elements[ 0].equals( ""))
			return null;

		if ( !CommonRuleManipulatorNew.is_object( kind, values, elements[ 0]))
			return null;

		values[ 4] = elements[ 0];
		return new String[] { CommonRuleManipulatorNew.make( values)};
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.role.base.object.base.Rule#get_used_number_object_names()
	 */
	@Override
	protected String[] get_used_number_object_names() {
		return get_used_variable_names2( new String[] { _reservedWords[ 4]}, "number object");
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.role.base.object.base.Rule#get_used_keyword_names()
	 */
	@Override
	protected String[] get_used_keyword_names() {
		return get_used_variable_names2( new String[] { _reservedWords[ 5]}, "keyword");
	}

	/**
	 * @param reservedWords
	 * @param kind
	 * @return
	 */
	private String[] get_used_variable_names2(String[] reservedWords, String kind) {
		if ( !is_target( reservedWords))
			return null;

		String[] values = get_values( _value);
		if ( null == values)
			return null;

		if ( null == values[ 4] || values[ 4].equals( ""))
			return null;

		String[] elements = values[ 4].split( "=");
		if ( null == elements || 2 > elements.length || null == elements[ 1] || elements[ 1].equals( ""))
			return null;

		if ( !CommonRuleManipulatorNew.is_object( kind, values, elements[ 1]))
			return null;

		values[ 4] = elements[ 1];
		return new String[] { CommonRuleManipulatorNew.make( values)};
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.role.base.object.base.Rule#is_number_object_type_correct(int, java.lang.String, java.lang.String, java.lang.String, soars.application.visualshell.object.role.base.object.Rules, soars.application.visualshell.object.role.base.Role)
	 */
	@Override
	public boolean is_number_object_type_correct(int row, String entityType, String numberObjectName, String newType, Rules rules, Role role) {
		// TODO Auto-generated method stub
		if ( !is_target( new String[] { _reservedWords[ 4]}))
			return true;

		String[] values = get_values( _value);
		if ( null == values)
			return true;

		if ( entityType.equals( "agent") && ( null == values[ 0] || values[ 2].equals( "")))
		if ( null == values[ 4] || values[ 4].equals( ""))
			return true;

		String[] elements = values[ 4].split( "=");
		if ( null == elements || 2 > elements.length || null == elements[ 1] || elements[ 1].equals( ""))
			return true;

		if ( !CommonRuleManipulatorNew.can_update_object_name( values, elements[ 1], numberObjectName, entityType))
			return true;

		return newType.equals( "integer");
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.role.base.object.base.Rule#update_agent_name_and_number(java.lang.String, java.lang.String, java.lang.String, java.util.Vector, java.lang.String, java.util.Vector)
	 */
	@Override
	public boolean update_agent_name_and_number(String newName, String originalName, String headName, Vector<String[]> ranges, String newHeadName, Vector<String[]> newRanges) {
		String[] values = get_values( _value);
		if ( null == values)
			return false;

		String reservedWord = get_reservedWord( _value);
		if ( null == reservedWord)
			return false;

		String value = CommonRuleManipulatorNew.update_agent_name( values, newName, originalName, headName, ranges, newHeadName, newRanges);
		if ( null == value)
			return false;

		_value = ( reservedWord + value);

		return true;
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.role.base.rule.base.Rule#update_spot_name_and_number(java.lang.String, java.lang.String, java.lang.String, java.util.Vector, java.lang.String, java.util.Vector)
	 */
	@Override
	public boolean update_spot_name_and_number(String newName, String originalName, String headName, Vector<String[]> ranges, String newHeadName, Vector<String[]> newRanges) {
		String[] values = get_values( _value);
		if ( null == values)
			return false;

		String reservedWord = get_reservedWord( _value);
		if ( null == reservedWord)
			return false;

		String value = CommonRuleManipulatorNew.update_spot_name( values, newName, originalName, headName, ranges, newHeadName, newRanges);
		if ( null == value)
			return false;

		_value = ( reservedWord + value);

		return true;
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.role.base.rule.base.Rule#update_spot_variable_name(java.lang.String, java.lang.String, java.lang.String, soars.application.visualshell.object.role.base.Role)
	 */
	@Override
	protected boolean update_spot_variable_name(String originalName, String newName, String entityType, Role role) {
		String[] values = get_values( _value);
		if ( null == values)
			return false;

		String reservedWord = get_reservedWord( _value);
		if ( null == reservedWord)
			return false;

		String value = CommonRuleManipulatorNew.update_spot_variable_name( values, originalName, newName, entityType);
		if ( null == value)
			return false;

		_value = ( reservedWord + value);

		return true;
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.role.base.object.base.Rule#update_collection_name(java.lang.String, java.lang.String, java.lang.String, soars.application.visualshell.object.role.base.Role)
	 */
	@Override
	protected boolean update_collection_name(String originalName, String newName, String entityType, Role role) {
		return update_object_name1( new String[] { _reservedWords[ 1]}, originalName, newName, entityType, role);
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.role.base.object.base.Rule#update_list_name(java.lang.String, java.lang.String, java.lang.String, soars.application.visualshell.object.role.base.Role)
	 */
	@Override
	protected boolean update_list_name(String originalName, String newName, String entityType, Role role) {
		return update_object_name1( new String[] { _reservedWords[ 1], _reservedWords[ 2], _reservedWords[ 3], _reservedWords[ 4]}, originalName, newName, entityType, role);
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.role.base.object.base.Rule#update_map_name(java.lang.String, java.lang.String, java.lang.String, soars.application.visualshell.object.role.base.Role)
	 */
	@Override
	protected boolean update_map_name(String originalName, String newName, String entityType, Role role) {
		return update_object_name1( new String[] { _reservedWords[ 1], _reservedWords[ 5]}, originalName, newName, entityType, role);
	}

	/**
	 * @param reservedWords
	 * @param originalName
	 * @param newName
	 * @param entityType
	 * @param role
	 * @return
	 */
	private boolean update_object_name1(String[] reservedWords, String originalName, String newName, String entityType, Role role) {
		if ( !is_target( reservedWords))
			return false;

		String reservedWord = get_reservedWord( _value);
		if ( null == reservedWord)
			return false;

		String[] values = get_values( _value);
		if ( null == values)
			return false;

		if ( null == values[ 4] || values[ 4].equals( ""))
			return false;

		String[] elements = values[ 4].split( "=");
		if ( null == elements || 1 > elements.length || null == elements[ 0] || elements[ 0].equals( ""))
			return false;

		if ( !CommonRuleManipulatorNew.can_update_object_name( values, elements[ 0], originalName, entityType))
			return false;

		elements[ 0] = newName;

		values[ 4] = CommonRuleManipulatorNew.concatenate( elements, "=");
		if ( null == values[ 4])
			return false;

		_value = ( reservedWord + CommonRuleManipulatorNew.make( values));

		return true;
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.role.base.object.base.Rule#update_number_object_name(java.lang.String, java.lang.String, java.lang.String, soars.application.visualshell.object.role.base.Role)
	 */
	@Override
	protected boolean update_number_object_name(String originalName, String newName, String entityType, Role role) {
		return update_object_name2( new String[] { _reservedWords[ 4]}, originalName, newName, entityType, role);
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.role.base.object.base.Rule#update_keyword_name(java.lang.String, java.lang.String, java.lang.String, soars.application.visualshell.object.role.base.Role)
	 */
	@Override
	protected boolean update_keyword_name(String originalName, String newName, String entityType, Role role) {
		return update_object_name2( new String[] { _reservedWords[ 5]}, originalName, newName, entityType, role);
	}

	/**
	 * @param reservedWords
	 * @param originalName
	 * @param newName
	 * @param entityType
	 * @param role
	 * @return
	 */
	private boolean update_object_name2(String[] reservedWords, String originalName, String newName, String entityType, Role role) {
		if ( !is_target( reservedWords))
			return false;

		String reservedWord = get_reservedWord( _value);
		if ( null == reservedWord)
			return false;

		String[] values = get_values( _value);
		if ( null == values)
			return false;

		if ( null == values[ 4] || values[ 4].equals( ""))
			return false;

		String[] elements = values[ 4].split( "=");
		if ( null == elements || 2 > elements.length || null == elements[ 1] || elements[ 1].equals( ""))
			return false;

		if ( !CommonRuleManipulatorNew.can_update_object_name( values, elements[ 1], originalName, entityType))
			return false;

		elements[ 1] = newName;

		values[ 4] = CommonRuleManipulatorNew.concatenate( elements, "=");
		if ( null == values[ 4])
			return false;

		_value = ( reservedWord + CommonRuleManipulatorNew.make( values));

		return true;
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.role.base.rule.base.Rule#can_paste(soars.application.visualshell.object.role.base.Role, soars.application.visualshell.layer.Layer)
	 */
	@Override
	protected boolean can_paste(Role role, Layer drawObjects) {
		String[] values = get_values( _value, drawObjects);
		if ( null == values)
			return false;

		if ( !CommonRuleManipulatorNew.can_paste_agent_name( values, drawObjects))
			return false;

		if ( !CommonRuleManipulatorNew.can_paste_spot_name( values, drawObjects))
			return false;

		if ( !CommonRuleManipulatorNew.can_paste_spot_variable_name( values, drawObjects))
			return false;

		if ( !can_paste_object_name( values, drawObjects))
			return false;

		return true;
	}

	/**
	 * @param values
	 * @param drawObjects
	 * @return
	 */
	private boolean can_paste_object_name(String[] values, Layer drawObjects) {
		if ( null == values[ 4] || values[ 4].equals( ""))
			return true;

		String[] elements = values[ 4].split( "=");
		if ( null == elements || 1 > elements.length || null == elements[ 0] || elements[ 0].equals( ""))
			return true;

		String kind = get_object_kind( values, elements[ 0], drawObjects);
		if ( null == kind)
			return false;

		return can_paste_object_name( kind, values, elements, drawObjects);
	}

	/**
	 * @param values
	 * @param object
	 * @param drawObjects
	 * @return
	 */
	private String get_object_kind(String[] values, String object, Layer drawObjects) {
		String[] kinds = { "collection", "list", "map"};
		for ( String kind:kinds) {
			if ( CommonRuleManipulatorNew.is_object( kind, values, object, drawObjects))
				return kind;
		}
		return null;
	}

	/**
	 * @param kind 
	 * @param values
	 * @param elements 
	 * @param drawObjects
	 * @return
	 */
	private boolean can_paste_object_name(String kind, String[] values, String[] elements, Layer drawObjects) {
		if ( 2 > elements.length || null == elements[ 1] || elements[ 1].equals( "") || elements[ 1].startsWith( "\""))
			return true;

		return ( ( kind.equals( "list") && CommonRuleManipulatorNew.is_object( "number object", values, elements[ 1], drawObjects))
			|| ( kind.equals( "map") && CommonRuleManipulatorNew.is_object( "keyword", values, elements[ 1], drawObjects)));
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.role.base.rule.base.Rule#get_script(java.lang.String, soars.application.visualshell.object.role.base.Role)
	 */
	@Override
	protected String get_script(String value, Role role) {
		return _value;
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.role.base.object.base.Rule#get_cell_text(soars.application.visualshell.object.role.base.Role)
	 */
	@Override
	public String get_cell_text(Role role) {
		String[] values = get_values( _value);
		if ( null == values)
			return _value;

		if ( null == values[ 4] || values[ 4].equals( ""))
			return _value;

		String[] elements = values[ 4].split( "=");
		if ( null == elements || 1 > elements.length || null == elements[ 0] || elements[ 0].equals( ""))
			return _value;

		values[ 4] = elements[ 0];

		String reservedWord = get_reservedWord( _value);
		if ( null == reservedWord)
			return _value;

		if ( 2 > elements.length || null == elements[ 1] || elements[ 1].equals( ""))
			return ( reservedWord + CommonRuleManipulatorNew.make( values));

		values[ 4] += ( "[" + elements[ 1] + "]");
		//values[ 4] += ( "(" + elements[ 1] + ")");
		return ( reservedWord + CommonRuleManipulatorNew.make( values));
	}

	/**
	 * @param value
	 * @return
	 */
	public static String update(String value) {
		return ( value.startsWith( "moveTo") ? value : ( _reservedWords[ 0] + "<" + value + ">"));
	}
}
