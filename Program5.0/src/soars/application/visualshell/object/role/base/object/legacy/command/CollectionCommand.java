/**
 * 
 */
package soars.application.visualshell.object.role.base.object.legacy.command;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import soars.application.visualshell.layer.ILayerManipulator;
import soars.application.visualshell.layer.Layer;
import soars.application.visualshell.layer.LayerManager;
import soars.application.visualshell.object.role.base.Role;
import soars.application.visualshell.object.role.base.object.base.Rule;
import soars.application.visualshell.object.role.base.object.common.CommonRuleManipulator;
import soars.application.visualshell.object.role.base.object.legacy.condition.base.CollectionAndListCondition;

/**
 * @author kurata
 *
 */
public class CollectionCommand extends Rule {

	/**
	 * 
	 */
	public static String[] _reservedWords = {
		"addSpot ",
		"addAgent ",
		"addString ",
		"addAll ",
		"addAll ",
		"addEquip ",
		"addEquip ",
		"addEquip ",
		"addEquip ",

		"removeSpot ",
		"removeAgent ",
		"removeString ",
		"removeAll ",
		"removeAll ",
		"removeEquip ",
		"removeEquip ",
		"removeEquip ",
		"removeEquip ",

		"removeAll ",
		"removeRandomOne ",
		"retainAll ",
		"retainAll ",
		"setAll ",
		"moveToRandomOne "
	};

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
		if ( value.startsWith( _reservedWords[ 0]))
			return 0;
		else if ( value.startsWith( _reservedWords[ 1]))
			return 1;
		else if ( value.startsWith( _reservedWords[ 2]))
			return 2;
		else if ( value.startsWith( _reservedWords[ 3]))
			return get_kind( value, 3, 4, layerManipulator);
		else if ( value.startsWith( "equip "))
			return 6;
		else if ( value.startsWith( _reservedWords[ 9]))
			return 9;
		else if ( value.startsWith( _reservedWords[ 10]))
			return 10;
		else if ( value.startsWith( _reservedWords[ 11]))
			return 11;
		else if ( value.startsWith( _reservedWords[ 12]))
			return get_kind( value, 12, 13, 18, layerManipulator);
		else if ( value.startsWith( _reservedWords[ 19]))
			return 19;
		else if ( value.startsWith( _reservedWords[ 20]))
			return get_kind( value, 20, 21, layerManipulator);
		else if ( value.startsWith( _reservedWords[ 22]))
			return 22;
		else if ( value.startsWith( _reservedWords[ 23]))
			return 23;
		else {
			String[] elements = CommonRuleManipulator.get_elements( value);
			if ( null == elements || 2 != elements.length)
				return -2;

			String prefix = CommonRuleManipulator.get_full_prefix( elements[ 0]);
			if ( null == prefix)
				return -2;

			if ( value.startsWith( _reservedWords[ 5])) {
				if ( CommonRuleManipulator.is_object( "probability", prefix + elements[ 1], layerManipulator))
					return 5;
				else if ( CommonRuleManipulator.is_object( "keyword", prefix + elements[ 1], layerManipulator))
					return 6;
				else if ( CommonRuleManipulator.is_object( "number object", prefix + elements[ 1], layerManipulator))
					return 7;
				else
					return 8;
			} else if ( value.startsWith( _reservedWords[ 14])) {
				if ( CommonRuleManipulator.is_object( "probability", prefix + elements[ 1], layerManipulator))
					return 14;
				else if ( CommonRuleManipulator.is_object( "keyword", prefix + elements[ 1], layerManipulator))
					return 15;
				else if ( CommonRuleManipulator.is_object( "number object", prefix + elements[ 1], layerManipulator))
					return 16;
				else
					return 17;
			}
		}

		return -1;
	}

	/**
	 * @param value
	 * @param index1
	 * @param index2
	 * @param index3
	 * @param layerManipulator
	 * @return
	 */
	private static int get_kind(String value, int index1, int index2, int index3, ILayerManipulator layerManipulator) {
		String[] elements = CommonRuleManipulator.get_elements( value);
		if ( null == elements)
			return -2;

		if ( 1 == elements.length)
			return index3;
		else if ( 2 == elements.length)
			return get_kind( value, index1, index2, layerManipulator);
		else
			return -2;
	}

	/**
	 * @param value
	 * @param index1
	 * @param index2
	 * @param layerManipulator
	 * @return
	 */
	private static int get_kind(String value, int index1, int index2, ILayerManipulator layerManipulator) {
		String[] elements = CommonRuleManipulator.get_elements( value);
		if ( null == elements || 2 != elements.length)
			return -2;

		String spot = CommonRuleManipulator.get_full_prefix( elements[ 0]);
		if ( null == spot)
			return -2;

		if ( CommonRuleManipulator.is_object( "collection", spot + elements[ 1], layerManipulator))
			return index1;
		else if ( CommonRuleManipulator.is_object( "list", spot + elements[ 1], layerManipulator))
			return index2;
		else
			return -2;
	}

	/**
	 * @return
	 */
	private int get_kind2() {
		if ( _value.startsWith( _reservedWords[ 0]))
			return 0;
		else if ( _value.startsWith( _reservedWords[ 1]))
			return 1;
		else if ( _value.startsWith( _reservedWords[ 2]))
			return 2;
		else if ( _value.startsWith( _reservedWords[ 3]))
			return 3;
//		else if ( _value.startsWith( _reserved_words[ 4]))
//			return 4;
		else if ( _value.startsWith( "equip "))
			return 6;
		else if ( _value.startsWith( _reservedWords[ 5]))
			return 5;
//		else if ( _value.startsWith( _reserved_words[ 6]))
//			return 6;
//		else if ( _value.startsWith( _reserved_words[ 7]))
//			return 7;
//		else if ( _value.startsWith( _reserved_words[ 8]))
//			return 8;
		else if ( _value.startsWith( _reservedWords[ 9]))
			return 9;
		else if ( _value.startsWith( _reservedWords[ 10]))
			return 10;
		else if ( _value.startsWith( _reservedWords[ 11]))
			return 11;
		else if ( _value.startsWith( _reservedWords[ 12]))
			return 12;
//		else if ( _value.startsWith( _reserved_words[ 13]))
//			return 13;
//		else if ( _value.startsWith( _reserved_words[ 18]))
//			return 18;
		else if ( _value.startsWith( _reservedWords[ 14]))
			return 14;
//		else if ( _value.startsWith( _reserved_words[ 15]))
//			return 15;
//		else if ( _value.startsWith( _reserved_words[ 16]))
//			return 16;
//		else if ( _value.startsWith( _reserved_words[ 17]))
//			return 17;
		else if ( _value.startsWith( _reservedWords[ 19]))
			return 19;
		else if ( _value.startsWith( _reservedWords[ 20]))
			return 20;
//		else if ( _value.startsWith( _reserved_words[ 21]))
//			return 21;
		else if ( _value.startsWith( _reservedWords[ 22]))
			return 22;
		else if ( _value.startsWith( _reservedWords[ 23]))
			return 23;

		return -1;
	}

	/**
	 * @param kind
	 * @param type
	 * @param value
	 */
	public CollectionCommand(String kind, String type, String value) {
		super(kind, type, value);
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.role.base.rule.base.Rule#get_used_agent_names()
	 */
	@Override
	protected String[] get_used_agent_names() {
		return new String[] { get_used_agent_name()};
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
			case 1:
			case 10:
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
		int kind = get_kind( _value);
		if ( 0 > kind)
			return null;

		String[] elements = CommonRuleManipulator.get_elements( _value);
		if ( null == elements || 1 > elements.length)
			return null;

		String[] usedSpotNames = new String[] {
			CommonRuleManipulator.extract_spot_name2( elements[ 0]),
			null};

		if ( 2 == elements.length) {
			switch ( kind) {
				case 0:
				case 9:
					if ( elements[ 1].equals( ""))
						break;

					usedSpotNames[ 1] = elements[ 1];
					break;
			}
		}

		return usedSpotNames;
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.role.base.rule.base.Rule#get_used_spot_variable_names(soars.application.visualshell.object.role.base.Role)
	 */
	@Override
	protected String[] get_used_spot_variable_names(Role role) {
//		return new String[] { get_used_spot_variable_name()};
		// TODO 要動作確認！
		int kind = get_kind( _value);
		if ( 0 > kind)
			return null;

		String[] elements = CommonRuleManipulator.get_elements( _value);
		if ( null == elements || 1 > elements.length)
			return null;

		List<String> list = new ArrayList<String>();

		String name = CommonRuleManipulator.get_spot_variable_name2( elements[ 0]);
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
		return new String[] { get_used_probability_name()};
	}

	/**
	 * @return
	 */
	private String get_used_probability_name() {
		int kind = get_kind( _value);
		if ( 0 > kind)
			return null;

		String[] elements = CommonRuleManipulator.get_elements( _value);
		if ( null == elements || 2 != elements.length)
			return null;

		switch ( kind) {
			case 5:
			case 14:
				if ( elements[ 1].equals( ""))
					break;

				String prefix = CommonRuleManipulator.get_full_prefix( elements[ 0]);
				if ( null == prefix)
					break;

				return ( prefix + elements[ 1]);
		}

		return null;
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.role.base.rule.base.Rule#get_used_collection_names()
	 */
	@Override
	protected String[] get_used_collection_names() {
		int kind = get_kind( _value);
		if ( 0 > kind)
			return null;

		String[] elements = CommonRuleManipulator.get_elements( _value);
		if ( null == elements || 1 > elements.length)
			return null;

		String[] usedCollectionNames = new String[] { elements[ 0], null};

		if ( 2 == elements.length && !elements[ 1].equals( "") && ( 3 == kind || 12 == kind || 20 == kind)) {
			String prefix = CommonRuleManipulator.get_full_prefix( elements[ 0]);
			if ( null != prefix)
				usedCollectionNames[ 1] = ( prefix + elements[ 1]);
		}

		return usedCollectionNames;
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.role.base.rule.base.Rule#get_used_list_names()
	 */
	@Override
	protected String[] get_used_list_names() {
		return new String[] { get_list_name()};
	}

	/**
	 * @return
	 */
	private String get_list_name() {
		int kind = get_kind( _value);
		if ( 0 > kind)
			return null;

		String[] elements = CommonRuleManipulator.get_elements( _value);
		if ( null == elements || 1 > elements.length)
			return null;

		if ( 2 == elements.length && !elements[ 1].equals( "") && ( 4 == kind || 13 == kind || 21 == kind)) {
			String prefix = CommonRuleManipulator.get_full_prefix( elements[ 0]);
			if ( null == prefix)
				return null;

			return ( prefix + elements[ 1]);
		}

		return null;
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.role.base.rule.base.Rule#get_used_keyword_names()
	 */
	@Override
	protected String[] get_used_keyword_names() {
		return new String[] { get_used_keyword_name()};
	}

	/**
	 * @return
	 */
	private String get_used_keyword_name() {
		int kind = get_kind( _value);
		if ( 0 > kind)
			return null;

		String[] elements = CommonRuleManipulator.get_elements( _value);
		if ( null == elements || 2 != elements.length)
			return null;

		String keywordName = null;

		switch ( kind) {
			case 6:
			case 15:
				if ( elements[ 1].equals( ""))
					break;

				String prefix = CommonRuleManipulator.get_full_prefix( elements[ 0]);
				if ( null == prefix)
					break;

				return ( prefix + elements[ 1]);
		}

		return keywordName;
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.role.base.rule.base.Rule#get_used_number_object_names()
	 */
	@Override
	protected String[] get_used_number_object_names() {
		return new String[] { get_used_number_object_name()};
	}

	/**
	 * @return
	 */
	private String get_used_number_object_name() {
		int kind = get_kind( _value);
		if ( 0 > kind)
			return null;

		String[] elements = CommonRuleManipulator.get_elements( _value);
		if ( null == elements || 2 != elements.length)
			return null;

		String numberObjectName = null;

		switch ( kind) {
			case 7:
			case 16:
				if ( elements[ 1].equals( ""))
					break;

				String prefix = CommonRuleManipulator.get_full_prefix( elements[ 0]);
				if ( null == prefix)
					break;

				return ( prefix + elements[ 1]);
		}

		return numberObjectName;
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
		if ( 0 > kind || ( 8 != kind && 17 != kind))
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
		int kind = get_kind( _value);
		if ( 0 > kind)
			return false;

		String[] elements = CommonRuleManipulator.get_elements( _value);
		if ( null == elements || 2 != elements.length)
			return false;

		switch ( kind) {
			case 1:
			case 10:
				String agentName = CommonRuleManipulator.get_new_agent_or_spot_name( elements[ 1], newName, originalName, headName, ranges, newHeadName, newRanges);
				if ( null == agentName)
					return false;

				elements[ 1] = agentName;
				break;
			default:
				return false;
		}

		_value = _reservedWords[ kind];
		for ( int i = 0; i < elements.length; ++i)
			_value += ( ( ( 0 == i) ? "" : "=") + elements[ i]);

		return true;
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.role.base.rule.base.Rule#update_spot_name_and_number(java.lang.String, java.lang.String, java.lang.String, java.util.Vector, java.lang.String, java.util.Vector)
	 */
	@Override
	public boolean update_spot_name_and_number(String newName, String originalName, String headName, Vector<String[]> ranges, String newHeadName, Vector<String[]> newRanges) {
		int kind = get_kind2();
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
				case 0:
				case 9:
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

		_value = _reservedWords[ kind];
		for ( int i = 0; i < elements.length; ++i)
			_value += ( ( ( 0 == i) ? "" : "=") + elements[ i]);

		return true;
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.role.base.rule.base.Rule#update_spot_variable_name(java.lang.String, java.lang.String, java.lang.String, soars.application.visualshell.object.role.base.Role)
	 */
	@Override
	protected boolean update_spot_variable_name(String name, String newName, String type, Role role) {
//		int kind = get_kind2();
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
//		_value = _reservedWords[ kind];
//		for ( int i = 0; i < elements.length; ++i)
//			_value += ( ( ( 0 == i) ? "" : "=") + elements[ i]);
//
//		return false;
		// TODO 要動作確認！
		int kind = get_kind2();
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

		kind = get_kind( _value);
		if ( ( 8 == kind || 17 == kind) && 2 == elements.length && update_object_name( elements, name, newName, type))
			result = true;

		if ( !result)
			return false;

		_value = _reservedWords[ kind];
		for ( int i = 0; i < elements.length; ++i)
			_value += ( ( ( 0 == i) ? "" : "=") + elements[ i]);

		return false;
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.role.base.rule.base.Rule#update_probability_name(java.lang.String, java.lang.String, java.lang.String, soars.application.visualshell.object.role.base.Role)
	 */
	@Override
	protected boolean update_probability_name(String name, String newName, String type, Role role) {
		int kind = get_kind( _value);
		if ( 0 > kind)
			return false;

		String[] elements = CommonRuleManipulator.get_elements( _value);
		if ( null == elements || 2 != elements.length || elements[ 1].equals( ""))
			return false;

		switch ( kind) {
			case 5:
			case 14:
				String prefix = CommonRuleManipulator.get_full_prefix( elements[ 0]);
				if ( null == prefix || !CommonRuleManipulator.correspond( prefix, elements[ 1], name, type))
					return false;

				elements[ 1] = newName;
				break;
			default:
				return false;
		}

		_value = _reservedWords[ kind];
		for ( int i = 0; i < elements.length; ++i)
			_value += ( ( ( 0 == i) ? "" : "=") + elements[ i]);

		return true;
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.role.base.rule.base.Rule#update_collection_name(java.lang.String, java.lang.String, java.lang.String, soars.application.visualshell.object.role.base.Role)
	 */
	@Override
	protected boolean update_collection_name(String name, String newName, String type, Role role) {
		int kind = get_kind( _value);
		if ( 0 > kind)
			return false;

		String[] elements = CommonRuleManipulator.get_elements( _value);
		if ( null == elements || 1 > elements.length)
			return false;

		boolean result1 = false;
		String element0 = CommonRuleManipulator.update_object_name( elements[ 0], name, newName, type);
		if ( null != element0) {
			elements[ 0] = element0;
			result1 = true;
		}

		boolean result2 = false;
		if ( 2 == elements.length && !elements[ 1].equals( "") && ( 3 == kind || 12 == kind || 20 == kind)) {
			String prefix = CommonRuleManipulator.get_full_prefix( elements[ 0]);
			if ( null != prefix && CommonRuleManipulator.correspond( prefix, elements[ 1], name, type)) {
				elements[ 1] = newName;
				result2 = true;
			}
		}

		if ( !result1 && !result2)
			return false;

		_value = _reservedWords[ kind];
		for ( int i = 0; i < elements.length; ++i)
			_value += ( ( ( 0 == i) ? "" : "=") + elements[ i]);

		return true;
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.role.base.rule.base.Rule#update_list_name(java.lang.String, java.lang.String, java.lang.String, soars.application.visualshell.object.role.base.Role)
	 */
	@Override
	protected boolean update_list_name(String name, String newName, String type, Role role) {
		int kind = get_kind( _value);
		if ( 0 > kind)
			return false;

		String[] elements = CommonRuleManipulator.get_elements( _value);
		if ( null == elements || 1 > elements.length)
			return false;

		if ( 2 == elements.length && !elements[ 1].equals( "") && ( 4 == kind || 13 == kind || 21 == kind)) {
			String prefix = CommonRuleManipulator.get_full_prefix( elements[ 0]);
			if ( null == prefix || !CommonRuleManipulator.correspond( prefix, elements[ 1], name, type))
				return false;

			elements[ 1] = newName;
		} else
			return false;

		_value = _reservedWords[ kind];
		for ( int i = 0; i < elements.length; ++i)
			_value += ( ( ( 0 == i) ? "" : "=") + elements[ i]);

		return true;
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.role.base.rule.base.Rule#update_keyword_name(java.lang.String, java.lang.String, java.lang.String, soars.application.visualshell.object.role.base.Role)
	 */
	@Override
	protected boolean update_keyword_name(String name, String newName, String type, Role role) {
		int kind = get_kind( _value);
		if ( 0 > kind)
			return false;

		String[] elements = CommonRuleManipulator.get_elements( _value);
		if ( null == elements || 2 != elements.length || elements[ 1].equals( ""))
			return false;

		switch ( kind) {
			case 6:
			case 15:
				String prefix = CommonRuleManipulator.get_full_prefix( elements[ 0]);
				if ( null == prefix || !CommonRuleManipulator.correspond( prefix, elements[ 1], name, type))
					return false;

				elements[ 1] = newName;
				break;
			default:
				return false;
		}

		_value = _reservedWords[ kind];
		for ( int i = 0; i < elements.length; ++i)
			_value += ( ( ( 0 == i) ? "" : "=") + elements[ i]);

		return true;
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.role.base.rule.base.Rule#update_number_object_name(java.lang.String, java.lang.String, java.lang.String, soars.application.visualshell.object.role.base.Role)
	 */
	@Override
	protected boolean update_number_object_name(String name, String newName, String type, Role role) {
		int kind = get_kind( _value);
		if ( 0 > kind)
			return false;

		String[] elements = CommonRuleManipulator.get_elements( _value);
		if ( null == elements || 2 != elements.length || elements[ 1].equals( ""))
			return false;

		switch ( kind) {
			case 7:
			case 16:
				String prefix = CommonRuleManipulator.get_full_prefix( elements[ 0]);
				if ( null == prefix || !CommonRuleManipulator.correspond( prefix, elements[ 1], name, type))
					return false;

				elements[ 1] = newName;
				break;
			default:
				return false;
		}

		_value = _reservedWords[ kind];
		for ( int i = 0; i < elements.length; ++i)
			_value += ( ( ( 0 == i) ? "" : "=") + elements[ i]);

		return true;
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
		if ( 0 > kind || ( 8 != kind && 17 != kind))
			return false;

		String[] elements = CommonRuleManipulator.get_elements( _value);
		if ( null == elements || 2 != elements.length)
			return false;

		if ( !update_object_name( elements, name, newName, type))
			return false;

		_value = _reservedWords[ kind];
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
		int kind = get_kind( _value, drawObjects);
		if ( 0 > kind)
			return false;
//		switch ( kind) {
//			case -1:
//				return true;
//			case -2:
//				return false;
//		}

		if ( !can_paste_agent_name( kind, drawObjects))
			return false;

		if ( !can_paste_spot_name( kind, drawObjects))
			return false;

		if ( !can_paste_probability_name( kind, drawObjects))
			return false;

		if ( !can_paste_collection_name( kind, drawObjects))
			return false;

		if ( !can_paste_list_name( kind, drawObjects))
			return false;

		if ( !can_paste_keyword_name( kind, drawObjects))
			return false;

		if ( !can_paste_number_object_name( kind, drawObjects))
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
		if ( 1 != kind && 10 != kind)
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
		if ( 0 != kind && 9 != kind)
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
	private boolean can_paste_probability_name(int kind, Layer drawObjects) {
		if ( 5 != kind && 14 != kind)
			return true;

		String[] elements = CommonRuleManipulator.get_elements( _value);
		if ( null == elements || 2 != elements.length)
			return false;

		if ( elements[ 1].equals( ""))
			return false;

		String prefix = CommonRuleManipulator.get_full_prefix( elements[ 0]);
		if ( null == prefix)
			return false;

		return CommonRuleManipulator.can_paste_object( "probability", prefix + elements[ 1], drawObjects);
	}

	/**
	 * @param kind
	 * @param drawObjects
	 * @return
	 */
	private boolean can_paste_collection_name(int kind, Layer drawObjects) {
		String[] elements = CommonRuleManipulator.get_elements( _value);
		if ( null == elements || 1 > elements.length)
			return false;

		if ( !CommonRuleManipulator.can_paste_object( "collection", elements[ 0], drawObjects))
			return false;

		if ( 3 == kind || 12 == kind || 20 == kind) {
			if ( 2 > elements.length || null == elements[ 1] || elements[ 1].equals( ""))
				return false;

			String prefix = CommonRuleManipulator.get_full_prefix( elements[ 0]);
			if ( null == prefix)
				return false;

			return CommonRuleManipulator.can_paste_object( "collection", prefix + elements[ 1], drawObjects);
		}

		return true;
	}

	/**
	 * @param kind
	 * @param drawObjects
	 * @return
	 */
	private boolean can_paste_list_name(int kind, Layer drawObjects) {
		if ( 4 != kind && 13 != kind && 21 != kind)
			return true;

		String[] elements = CommonRuleManipulator.get_elements( _value);
		if ( null == elements || 2 != elements.length)
			return false;

		if ( elements[ 1].equals( ""))
			return false;

		String prefix = CommonRuleManipulator.get_full_prefix( elements[ 0]);
		if ( null == prefix)
			return false;

		return CommonRuleManipulator.can_paste_object( "list", prefix + elements[ 1], drawObjects);
	}

	/**
	 * @param kind
	 * @param drawObjects
	 * @return
	 */
	private boolean can_paste_keyword_name(int kind, Layer drawObjects) {
		if ( 6 != kind && 15 != kind)
			return true;

		String[] elements = CommonRuleManipulator.get_elements( _value);
		if ( null == elements || 2 != elements.length)
			return false;

		if ( elements[ 1].equals( ""))
			return false;

		String prefix = CommonRuleManipulator.get_full_prefix( elements[ 0]);
		if ( null == prefix)
			return false;

		return CommonRuleManipulator.can_paste_object( "keyword", prefix + elements[ 1], drawObjects);
	}

	/**
	 * @param kind
	 * @param drawObjects
	 * @return
	 */
	private boolean can_paste_number_object_name(int kind, Layer drawObjects) {
		if ( 7 != kind && 16 != kind)
			return true;

		String[] elements = CommonRuleManipulator.get_elements( _value);
		if ( null == elements || 2 != elements.length)
			return false;

		if ( elements[ 1].equals( ""))
			return false;

		String prefix = CommonRuleManipulator.get_full_prefix( elements[ 0]);
		if ( null == prefix)
			return false;

		return CommonRuleManipulator.can_paste_object( "number object", prefix + elements[ 1], drawObjects);
	}

	/**
	 * @param kind
	 * @param drawObjects
	 * @return
	 */
	private boolean can_paste_object_name(int kind, Layer drawObjects) {
		// TODO 要動作確認！
		if ( 8 != kind && 17 != kind)
			return true;

		String[] elements = CommonRuleManipulator.get_elements( _value);
		if ( null == elements || 2 != elements.length)
			return false;

		if ( elements[ 1].equals( ""))
			return false;

		String prefix = CommonRuleManipulator.get_full_prefix( elements[ 0]);
		if ( null == prefix)
			return false;

		String[] types = new String[] {
			"spot variable",
			"role variable",
			"time variable",
			"map",
			"exchange algebra",
			"file",
			"class variable"
		};
		for ( String type:types) {
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
		return get_script( get_kind( value), value);
	}

	/**
	 * @param kind
	 * @param value
	 * @return
	 */
	public static String get_script(int kind, String value) {
		if ( value.startsWith( "equip ")
			|| value.startsWith( "addEquip ")
			|| value.startsWith( "removeEquip ")
			|| value.startsWith( "addFirstEquip ")
			|| value.startsWith( "addLastEquip "))
			return get_updated_script( value);
		else if ( value.startsWith( "equipFirst ") || value.startsWith( "equipLast ")) {
			value = CollectionAndListCondition.get_script( value);
			return ( value + ListCommand.get_partial_script( kind, value));
		} else
			return CollectionAndListCondition.get_script( value);
	}

	/**
	 * @param value
	 * @return
	 */
	private static String get_updated_script(String value) {
		String[][] commands = new String[][] {
			{ "equip ", "addEquip "},
			{ "addEquip ", "addEquip "},
			{ "removeEquip ", "removeEquip "},
			{ "addFirstEquip ", "addFirstEquip "},
			{ "addLastEquip ", "addLastEquip "}
		};

		String command = "";
		for ( int i = 0; i < commands.length; ++i) {
			if ( value.startsWith( commands[ i][ 0])) {
				command = commands[ i][ 1];
				break;
			}
		}

		if ( command.equals( ""))
			return "";

		value = CollectionAndListCondition.get_script( value);

		String prefix = CommonRuleManipulator.get_full_prefix( value);
		if ( null == prefix)
			return "";

		String[] elements = CommonRuleManipulator.get_elements( value);
		if ( null == elements || 2 != elements.length)
			return "";

		// TODO 修正済
		if ( elements[ 1].startsWith( "$Role."))
			return ( prefix + command + elements[ 0] + "=" + elements[ 1]);
		else if ( elements[ 1].startsWith( "$Time."))
			return ( prefix + command + elements[ 0] + "=" + elements[ 1] + " ; " + prefix + "cloneEquip " + elements[ 1]);
		else if ( CommonRuleManipulator.is_object( "keyword", prefix + elements[ 1])
			|| CommonRuleManipulator.is_object( "file", prefix + elements[ 1]))
			return ( prefix + "equip " + elements[ 1] + " ; " + prefix + command + elements[ 0] + "=" + elements[ 1]);
		else if ( ( null != LayerManager.get_instance().get_agent_has_this_name( elements[ 1]))
			|| ( null != LayerManager.get_instance().get_spot_has_this_name( elements[ 1]))
			|| CommonRuleManipulator.is_object( "spot variable", prefix + elements[ 1]))
			return ( prefix + command + elements[ 0] + "=" + elements[ 1]);
		else
			return ( prefix + command + elements[ 0] + "=" + elements[ 1] + " ; " + prefix + "cloneEquip " + elements[ 1]);
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.role.base.object.base.Rule#get_cell_text(soars.application.visualshell.object.role.base.Role)
	 */
	@Override
	public String get_cell_text(Role role) {
		return get_cell_text( _value);
	}

	/**
	 * @param value
	 */
	public static String get_cell_text(String value) {
		if ( value.startsWith( "equip ")
			|| value.startsWith( "addEquip ")
			|| value.startsWith( "removeEquip ")
			|| value.startsWith( "addFirstEquip ")
			|| value.startsWith( "addLastEquip "))
			return get_updated_cell_text( value);
		else
			return CollectionAndListCondition.get_cell_text( value);
			//return CollectionAndListCondition.get_script( value);
	}

	/**
	 * @return
	 */
	private static String get_updated_cell_text(String value) {
		String[][] commands = new String[][] {
			{ "equip ", "addEquip "},
			{ "addEquip ", "addEquip "},
			{ "removeEquip ", "removeEquip "},
			{ "addFirstEquip ", "addFirstEquip "},
			{ "addLastEquip ", "addLastEquip "}
		};

		String command = "";
		for ( int i = 0; i < commands.length; ++i) {
			if ( value.startsWith( commands[ i][ 0])) {
				command = commands[ i][ 1];
				break;
			}
		}

		value = CollectionAndListCondition.get_cell_text( value);
		//value = CollectionAndListCondition.get_script( value);

		if ( command.equals( ""))
			return "";

		String prefix = CommonRuleManipulator.get_full_prefix( value);
		if ( null == prefix)
			return "";

		String[] elements = CommonRuleManipulator.get_elements( value);
		if ( null == elements || 2 != elements.length)
			return "";

		return ( prefix + command + elements[ 0] + "=" + elements[ 1]);
	}
}
