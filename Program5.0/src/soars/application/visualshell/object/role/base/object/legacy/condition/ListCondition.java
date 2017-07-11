/**
 * 
 */
package soars.application.visualshell.object.role.base.object.legacy.condition;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import soars.application.visualshell.layer.Layer;
import soars.application.visualshell.main.Constant;
import soars.application.visualshell.object.role.base.Role;
import soars.application.visualshell.object.role.base.object.base.Rule;
import soars.application.visualshell.object.role.base.object.common.CommonRuleManipulator;
import soars.application.visualshell.object.role.base.object.legacy.condition.base.CollectionAndListCondition;

/**
 * @author kurata
 *
 */
public class ListCondition extends Rule {

	/**
	 * 
	 */
	public static String[] _reservedWords = {
		"isFirstAgent ",
		"isFirstSpot ",
		"isFirstEquip ",
		"isFirstString ",
		"isLastAgent ",
		"isLastSpot ",
		"isLastEquip ",
		"isLastString "
	};

	/**
	 * @param value
	 * @return
	 */
	public static int get_kind(String value) {
		for ( int i = 0; i < _reservedWords.length; ++i) {
			if ( value.startsWith( _reservedWords[ i])
				|| value.startsWith( "!" + _reservedWords[ i]))
				return i;
		}
		return -1;
	}

	/**
	 * @param kind
	 * @param type
	 * @param value
	 */
	public ListCondition(String kind, String type, String value) {
		super(kind, type, value);
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.role.base.rule.base.Rule#get_used_agent_names()
	 */
	@Override
	protected String[] get_used_agent_names() {
		String usedAgentName = CollectionAndListCondition.get_used_agent_name( this);
		if ( null == usedAgentName)
			usedAgentName = get_used_agent_name();

		return new String[] { usedAgentName};
	}

	/**
	 * @return
	 */
	private String get_used_agent_name() {
		int kind = get_kind( _value);
		if ( 0 > kind)
			return null;

		String[] elements = CommonRuleManipulator.get_elements( _value);
		if ( null == elements || 2 != elements.length)
			return null;

		switch ( kind) {
			case 0:
			case 4:
				if ( elements[ 1].equals( ""))
					break;

				return elements[ 1];
		}

		return null;
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.role.base.rule.base.Rule#get_used_spot_names()
	 */
	@Override
	protected String[] get_used_spot_names() {
		String[] usedSpotNames = CollectionAndListCondition.get_used_spot_names( this);
		if ( null != usedSpotNames)
			return usedSpotNames;

		int kind = get_kind( _value);
		if ( 0 > kind)
			return null;

		String[] elements = CommonRuleManipulator.get_elements( _value);
		if ( null == elements || 1 > elements.length)
			return null;

		String[] spotNames = new String[] {
			CommonRuleManipulator.extract_spot_name2( elements[ 0]),
			null};

		if ( 2 == elements.length) {
			switch ( kind) {
				case 1:
				case 5:
					if ( elements[ 1].equals( ""))
						break;

					spotNames[ 1] = elements[ 1];
					break;
			}
		}

		return spotNames;
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.role.base.rule.base.Rule#get_used_spot_variable_names(soars.application.visualshell.object.role.base.Role)
	 */
	@Override
	protected String[] get_used_spot_variable_names(Role role) {
//		String usedSpotVariableName = CollectionAndListCondition.get_used_spot_variable_name( this);
//		if ( null == usedSpotVariableName)
//			usedSpotVariableName = get_used_spot_variable_name();
//
//		return new String[] { used_spot_variable_name};
		// TODO 要動作確認！
		String name = CollectionAndListCondition.get_used_spot_variable_name( this);
		if ( null != name)
			return new String[] { name};

		int kind = get_kind( _value);
		if ( 0 > kind)
			return null;

		String[] elements = CommonRuleManipulator.get_elements( _value);
		if ( null == elements || 1 > elements.length)
			return null;

		List<String> list = new ArrayList<String>();

		name = CommonRuleManipulator.get_spot_variable_name2( elements[ 0]);
		if ( null != name)
			list.add( name);

		name = get_used_object_name( "spot variable");
		if ( null != name)
			list.add( name);

		return ( list.isEmpty() ? null : list.toArray( new String[ 0]));
	}

//	/**
//	 * @return
//	 */
//	private String get_used_spot_variable_name() {
//		int kind = get_kind( _value);
//		if ( 0 > kind)
//			return null;
//
//		String[] elements = CommonRuleManipulator.get_elements( _value);
//		if ( null == elements || 1 > elements.length)
//			return null;
//
//		return CommonRuleManipulator.get_spot_variable_name2( elements[ 0]);
//	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.role.base.rule.base.Rule#get_used_probability_names()
	 */
	@Override
	protected String[] get_used_probability_names() {
//		return new String[] { CollectionAndListCondition.get_used_probability_name( this)};
		// TODO 要動作確認！
		String name = CollectionAndListCondition.get_used_probability_name( this);
		if ( null != name)
			return new String[] { name};

		return new String[] { get_used_object_name( "probability")};
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.role.base.rule.base.Rule#get_used_collection_names()
	 */
	@Override
	protected String[] get_used_collection_names() {
//		return CollectionAndListCondition.get_used_collection_names( this);
		// TODO 要動作確認！
		String[] names = CollectionAndListCondition.get_used_collection_names( this);
		if ( null != names)
			return names;

		return new String[] { get_used_object_name( "collection")};
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.role.base.rule.base.Rule#get_used_list_names()
	 */
	@Override
	protected String[] get_used_list_names() {
//		String[] usedListNames = CollectionAndListCondition.get_used_list_names( this);
//		if ( null != usedListNames)
//			return usedListNames;
//
//		int kind = get_kind( _value);
//		if ( 0 > kind)
//			return null;
//
//		String[] elements = CommonRuleManipulator.get_elements( _value);
//		if ( null == elements || 1 > elements.length)
//			return null;
//
//		return new String[] { elements[ 0]};
		// TODO 要動作確認！
		String[] names = CollectionAndListCondition.get_used_list_names( this);
		if ( null != names)
			return names;

		int kind = get_kind( _value);
		if ( 0 > kind)
			return null;

		String[] elements = CommonRuleManipulator.get_elements( _value);
		if ( null == elements || 1 > elements.length)
			return null;

		List<String> list = new ArrayList<String>();
		list.add( elements[ 0]);

		String name = get_used_object_name( "list");
		if ( null != name)
			list.add( name);

		return list.toArray( new String[ 0]);
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.role.base.rule.base.Rule#get_used_number_object_names()
	 */
	@Override
	protected String[] get_used_number_object_names() {
//		return new String[] { CollectionAndListCondition.get_used_number_object_name( this)};
		// TODO 要動作確認！
		String name = CollectionAndListCondition.get_used_number_object_name( this);
		if ( null != name)
			return new String[] { name};

		return new String[] { get_used_object_name( "number object")};
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.role.base.rule.base.Rule#get_used_keyword_names()
	 */
	@Override
	protected String[] get_used_keyword_names() {
		// TODO 要動作確認！
		return new String[] { get_used_object_name( "keyword")};
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.role.base.rule.base.Rule#get_used_role_variable_names()
	 */
	@Override
	protected String[] get_used_role_variable_names() {
		// TODO 要動作確認！
		return new String[] { get_used_object_name( "role variable")};
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.role.base.rule.base.Rule#get_used_time_variable_names()
	 */
	@Override
	protected String[] get_used_time_variable_names() {
		// TODO 要動作確認！
		return new String[] { get_used_object_name( "time variable")};
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.role.base.rule.base.Rule#get_used_map_names()
	 */
	@Override
	protected String[] get_used_map_names() {
		// TODO 要動作確認！
		return new String[] { get_used_object_name( "map")};
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.role.base.rule.base.Rule#get_used_exchange_algebra_names(soars.application.visualshell.object.role.base.Role)
	 */
	@Override
	protected String[] get_used_exchange_algebra_names(Role role) {
		// TODO 要動作確認！
		return new String[] { get_used_object_name( "exchange algebra")};
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.role.base.rule.base.Rule#get_used_file_names()
	 */
	@Override
	protected String[] get_used_file_names() {
		// TODO 要動作確認！
		return new String[] { get_used_object_name( "file")};
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.role.base.object.base.Rule#get_used_class_variable_names(soars.application.visualshell.object.role.base.Role)
	 */
	@Override
	protected String[] get_used_class_variable_names(Role role) {
		// TODO 要動作確認！
		return new String[] { get_used_object_name( "class variable")};
	}

	/**
	 * @param type
	 * @return
	 */
	private String get_used_object_name(String type) {
		// TODO 要動作確認！
		int kind = get_kind( _value);
		if ( 0 > kind || ( 2 != kind && 6 != kind))
			return null;

		String[] elements = CommonRuleManipulator.get_elements( _value);
		if ( null == elements || 2 != elements.length)
			return null;

		String prefix = CommonRuleManipulator.get_full_prefix( elements[ 0]);
		if ( null == prefix)
			return null;

		if ( CommonRuleManipulator.is_object( type, prefix + elements[ 1]))
			return ( prefix + elements[ 1]);

		return null;
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.role.base.rule.base.Rule#update_agent_name_and_number(java.lang.String, java.lang.String, java.lang.String, java.util.Vector, java.lang.String, java.util.Vector)
	 */
	@Override
	public boolean update_agent_name_and_number(String newName, String originalName, String headName, Vector<String[]> ranges, String newHeadName, Vector<String[]> newRanges) {
		if ( CollectionAndListCondition.update_agent_name_and_number(newName, originalName, headName, ranges, newHeadName, newRanges, this))
			return true;

		int kind = get_kind( _value);
		if ( 0 > kind)
			return false;

		String[] elements = CommonRuleManipulator.get_elements( _value);
		if ( null == elements || 2 != elements.length)
			return false;

		switch ( kind) {
			case 0:
			case 4:
				String agentName = CommonRuleManipulator.get_new_agent_or_spot_name( elements[ 1], newName, originalName, headName, ranges, newHeadName, newRanges);
				if ( null == agentName)
					return false;

				elements[ 1] = agentName;
				break;
			default:
				return false;
		}

		_value = ( ( _value.startsWith( "!") ? "!" : "") + _reservedWords[ kind]);
		for ( int i = 0; i < elements.length; ++i)
			_value += ( ( ( 0 == i) ? "" : "=") + elements[ i]);

		return true;
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.role.base.rule.base.Rule#update_spot_name_and_number(java.lang.String, java.lang.String, java.lang.String, java.util.Vector, java.lang.String, java.util.Vector)
	 */
	@Override
	public boolean update_spot_name_and_number(String newName, String originalName, String headName, Vector<String[]> ranges, String newHeadName, Vector<String[]> newRanges) {
		if ( CollectionAndListCondition.update_spot_name_and_number(newName, originalName, headName, ranges, newHeadName, newRanges, this))
			return true;

		int kind = get_kind( _value);
		if ( 0 > kind)
			return false;

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
		if ( 2 == elements.length) {
			switch ( kind) {
				case 1:
				case 5:
					element = CommonRuleManipulator.get_new_agent_or_spot_name( elements[ 1], newName, originalName, headName, ranges, newHeadName, newRanges);
					if ( null != element) {
						elements[ 1] = element;
						result2 = true;
					}
					break;
			}
		}

		if ( !result1 && !result2)
			return false;

		_value = ( ( _value.startsWith( "!") ? "!" : "") + _reservedWords[ kind]);
		for ( int i = 0; i < elements.length; ++i)
			_value += ( ( ( 0 == i) ? "" : "=") + elements[ i]);

		return true;
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.role.base.rule.base.Rule#update_spot_variable_name(java.lang.String, java.lang.String, java.lang.String, soars.application.visualshell.object.role.base.Role)
	 */
	@Override
	protected boolean update_spot_variable_name(String name, String newName, String type, Role role) {
//		if ( CollectionAndListCondition.update_spot_variable_name(name, newName, type, this))
//			return true;
//
//		int kind = get_kind( _value);
//		if ( 0 > kind)
//			return false;
//
//		String[] elements = CommonRuleManipulator.get_elements( _value);
//		if ( null == elements || 1 > elements.length)
//			return false;
//
//		elements[ 0] = CommonRuleManipulator.update_spot_variable_name2( elements[ 0], name, newName, type);
//		if ( null == elements[ 0])
//			return false;
//
//		_value = ( ( _value.startsWith( "!") ? "!" : "") + _reserved_words[ kind]);
//		for ( int i = 0; i < elements.length; ++i)
//			_value += ( ( ( 0 == i) ? "" : "=") + elements[ i]);
//
//		return true;
		// TODO 要動作確認！
		if ( CollectionAndListCondition.update_spot_variable_name(name, newName, type, this))
			return true;

		int kind = get_kind( _value);
		if ( 0 > kind)
			return false;

		String[] elements = CommonRuleManipulator.get_elements( _value);
		if ( null == elements || 1 > elements.length)
			return false;

		boolean result = false;

		String element = CommonRuleManipulator.update_spot_variable_name2( elements[ 0], name, newName, type);
		if ( null != element) {
			elements[ 0] = element;
			result = true;
		}

		if ( ( 2 == kind || 6 == kind) && 2 == elements.length && update_object_name( elements, name, newName, type))
			result = true;

		if ( !result)
			return false;

		_value = ( ( _value.startsWith( "!") ? "!" : "") + _reservedWords[ kind]);
		for ( int i = 0; i < elements.length; ++i)
			_value += ( ( ( 0 == i) ? "" : "=") + elements[ i]);

		return true;
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.role.base.rule.base.Rule#update_probability_name(java.lang.String, java.lang.String, java.lang.String, soars.application.visualshell.object.role.base.Role)
	 */
	@Override
	protected boolean update_probability_name(String name, String newName, String type, Role role) {
//		return CollectionAndListCondition.update_probability_name(name, newName, type, this);
		// TODO 要動作確認！
		if ( CollectionAndListCondition.update_probability_name(name, newName, type, this))
			return true;

		return update_object_name( name, newName, type);
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.role.base.rule.base.Rule#update_collection_name(java.lang.String, java.lang.String, java.lang.String, soars.application.visualshell.object.role.base.Role)
	 */
	@Override
	protected boolean update_collection_name(String name, String newName, String type, Role role) {
//		return CollectionAndListCondition.update_collection_name(name, newName, type, this);
		// TODO 要動作確認！
		if ( CollectionAndListCondition.update_collection_name(name, newName, type, this))
			return true;

		return update_object_name( name, newName, type);
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.role.base.rule.base.Rule#update_list_name(java.lang.String, java.lang.String, java.lang.String, soars.application.visualshell.object.role.base.Role)
	 */
	@Override
	protected boolean update_list_name(String name, String newName, String type, Role role) {
//		if ( CollectionAndListCondition.update_list_name(name, newName, type, this))
//			return true;
//
//		int kind = get_kind( _value);
//		if ( 0 > kind)
//			return false;
//
//		String[] elements = CommonRuleManipulator.get_elements( _value);
//		if ( null == elements || 1 > elements.length)
//			return false;
//
//		elements[ 0] = CommonRuleManipulator.update_object_name( elements[ 0], name, newName, type);
//		if ( null == elements[ 0])
//			return false;
//
//		_value = ( ( _value.startsWith( "!") ? "!" : "") + _reserved_words[ kind]);
//		for ( int i = 0; i < elements.length; ++i)
//			_value += ( ( ( 0 == i) ? "" : "=") + elements[ i]);
//
//		return true;
		// TODO 要動作確認！
		if ( CollectionAndListCondition.update_list_name(name, newName, type, this))
			return true;

		int kind = get_kind( _value);
		if ( 0 > kind)
			return false;

		String[] elements = CommonRuleManipulator.get_elements( _value);
		if ( null == elements || 1 > elements.length)
			return false;

		boolean result = false;

		String element = CommonRuleManipulator.update_object_name( elements[ 0], name, newName, type);
		if ( null != element) {
			elements[ 0] = element;
			result = true;
		}

		if ( ( 2 == kind || 6 == kind) && 2 == elements.length && update_object_name( elements, name, newName, type))
			result = true;

		if ( !result)
			return false;

		_value = ( ( _value.startsWith( "!") ? "!" : "") + _reservedWords[ kind]);
		for ( int i = 0; i < elements.length; ++i)
			_value += ( ( ( 0 == i) ? "" : "=") + elements[ i]);

		return true;
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.role.base.rule.base.Rule#update_number_object_name(java.lang.String, java.lang.String, java.lang.String, soars.application.visualshell.object.role.base.Role)
	 */
	@Override
	protected boolean update_number_object_name(String name, String newName, String type, Role role) {
//		return CollectionAndListCondition.update_number_object_name(name, newName, type, this);
		// TODO 要動作確認！
		if ( CollectionAndListCondition.update_number_object_name(name, newName, type, this))
			return true;

		return update_object_name( name, newName, type);
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.role.base.rule.base.Rule#update_keyword_name(java.lang.String, java.lang.String, java.lang.String, soars.application.visualshell.object.role.base.Role)
	 */
	@Override
	protected boolean update_keyword_name(String name, String newName, String type, Role role) {
		// TODO 要動作確認！
		return update_object_name( name, newName, type);
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.role.base.rule.base.Rule#update_role_variable_name(java.lang.String, java.lang.String, java.lang.String, soars.application.visualshell.object.role.base.Role)
	 */
	@Override
	protected boolean update_role_variable_name(String name, String newName, String type, Role role) {
		// TODO 要動作確認！
		return update_object_name( name, newName, type);
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.role.base.rule.base.Rule#update_time_variable_name(java.lang.String, java.lang.String, java.lang.String, soars.application.visualshell.object.role.base.Role)
	 */
	@Override
	protected boolean update_time_variable_name(String name, String newName, String type, Role role) {
		// TODO 要動作確認！
		return update_object_name( name, newName, type);
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.role.base.rule.base.Rule#update_map_name(java.lang.String, java.lang.String, java.lang.String, soars.application.visualshell.object.role.base.Role)
	 */
	@Override
	protected boolean update_map_name(String name, String newName, String type, Role role) {
		// TODO 要動作確認！
		return update_object_name( name, newName, type);
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.role.base.rule.base.Rule#update_exchange_algebra_name(java.lang.String, java.lang.String, java.lang.String, soars.application.visualshell.object.role.base.Role)
	 */
	@Override
	protected boolean update_exchange_algebra_name(String name, String newName, String type, Role role) {
		// TODO 要動作確認！
		return update_object_name( name, newName, type);
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.role.base.rule.base.Rule#update_file_name(java.lang.String, java.lang.String, java.lang.String, soars.application.visualshell.object.role.base.Role)
	 */
	@Override
	protected boolean update_file_name(String name, String newName, String type, Role role) {
		// TODO 要動作確認！
		return update_object_name( name, newName, type);
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.role.base.rule.base.Rule#update_class_variable_name(java.lang.String, java.lang.String, java.lang.String, soars.application.visualshell.object.role.base.Role)
	 */
	@Override
	protected boolean update_class_variable_name(String name, String newName, String type, Role role) {
		// TODO 要動作確認！
		return update_object_name( name, newName, type);
	}

	/**
	 * @param name
	 * @param newName
	 * @param type
	 * @return
	 */
	private boolean update_object_name(String name, String newName, String type) {
		// TODO 要動作確認！
		int kind = get_kind( _value);
		if ( 0 > kind || ( 2 != kind && 6 != kind))
			return false;

		String[] elements = CommonRuleManipulator.get_elements( _value);
		if ( null == elements || 2 != elements.length)
			return false;

		if ( !update_object_name( elements, name, newName, type))
			return false;

		_value = ( ( _value.startsWith( "!") ? "!" : "") + _reservedWords[ kind]);
		for ( int i = 0; i < elements.length; ++i)
			_value += ( ( ( 0 == i) ? "" : "=") + elements[ i]);

		return true;
	}

	/**
	 * @param elements
	 * @param name
	 * @param newName
	 * @param type
	 * @return
	 */
	private boolean update_object_name(String[] elements, String name, String newName, String type) {
		// TODO 要動作確認！
		String prefix = CommonRuleManipulator.get_full_prefix( elements[ 0]);
		if ( null == prefix || !CommonRuleManipulator.correspond( prefix, elements[ 1], name, type))
			return false;

		elements[ 1] = newName;
		return true;
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.role.base.rule.base.Rule#can_paste(soars.application.visualshell.object.role.base.Role, soars.application.visualshell.layer.Layer)
	 */
	@Override
	protected boolean can_paste(Role role, Layer drawObjects) {
		int kind = CollectionAndListCondition.get_kind( this, drawObjects);
		if ( 0 <= kind)
			return CollectionAndListCondition.can_paste( kind, this, role, drawObjects);

		kind = get_kind( _value);
		if ( 0 > kind)
			return false;

		if ( !can_paste_agent_name( kind, drawObjects))
			return false;

		if ( !can_paste_spot_name( kind, drawObjects))
			return false;

		if ( !can_paste_list_name( kind, drawObjects))
			return false;

		// TODO 要動作確認！
		if ( !can_paste_object_name( kind, drawObjects))
			return false;

		return true;
	}

	/**
	 * @param kind
	 * @param drawObjects
	 * @return
	 */
	private boolean can_paste_agent_name(int kind, Layer drawObjects) {
		if ( 0 != kind && 4 != kind)
			return true;

		String[] elements = CommonRuleManipulator.get_elements( _value);
		if ( null == elements || 1 > elements.length)
			return false;

		if ( 2 > elements.length)
			return true;

		if ( elements[ 1].equals( ""))
			return true;

		return ( null != drawObjects.get_agent_has_this_name( elements[ 1]));
	}

	/**
	 * @param kind
	 * @param drawObjects
	 * @return
	 */
	private boolean can_paste_spot_name(int kind, Layer drawObjects) {
		if ( 1 != kind && 5 != kind)
			return true;

		String[] elements = CommonRuleManipulator.get_elements( _value);
		if ( null == elements || 1 > elements.length)
			return false;

		if ( 2 > elements.length)
			return true;

		if ( elements[ 1].equals( ""))
			return true;

		return ( null != drawObjects.get_spot_has_this_name( elements[ 1]));
	}

	/**
	 * @param kind
	 * @param drawObjects
	 * @return
	 */
	private boolean can_paste_list_name(int kind, Layer drawObjects) {
		String[] elements = CommonRuleManipulator.get_elements( _value);
		if ( null == elements || 1 > elements.length)
			return false;

		return CommonRuleManipulator.can_paste_object( "list", elements[ 0], drawObjects);
	}

	/**
	 * @param kind
	 * @param drawObjects
	 * @return
	 */
	private boolean can_paste_object_name(int kind, Layer drawObjects) {
		// TODO 要動作確認！
		if ( 2 != kind && 6 != kind)
			return true;

		String[] elements = CommonRuleManipulator.get_elements( _value);
		if ( null == elements || 2 != elements.length)
			return false;

		if ( elements[ 1].equals( ""))
			return false;

		String prefix = CommonRuleManipulator.get_full_prefix( elements[ 0]);
		if ( null == prefix)
			return false;

		for ( String type:Constant._kinds) {
			if ( CommonRuleManipulator.can_paste_object( type, prefix + elements[ 1], drawObjects))
				return true;
		}

		return false;
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.role.base.rule.base.Rule#get_script(java.lang.String, soars.application.visualshell.object.role.base.Role)
	 */
	@Override
	protected String get_script(String value, Role role) {
		return CollectionAndListCondition.get_script( value);
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.role.base.object.base.Rule#get_cell_text(soars.application.visualshell.object.role.base.Role)
	 */
	@Override
	public String get_cell_text(Role role) {
		return CollectionAndListCondition.get_cell_text( _value);
	}
}
