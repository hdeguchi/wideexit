/*
 * 2005/08/05
 */
package soars.application.visualshell.object.role.base.object.legacy.condition.base;

import java.util.Vector;

import soars.application.visualshell.layer.ILayerManipulator;
import soars.application.visualshell.layer.Layer;
import soars.application.visualshell.layer.LayerManager;
import soars.application.visualshell.main.ResourceManager;
import soars.application.visualshell.object.role.base.Role;
import soars.application.visualshell.object.role.base.object.base.Rule;
import soars.application.visualshell.object.role.base.object.common.CommonRuleManipulator;

/**
 * @author kurata
 */
public class CollectionAndListCondition {

	/**
	 * 
	 */
	static public String[] _reservedWords = {
		"isEmpty ",
		"containsAgent ",
		"containsSpot ",
		"containsEquip ",
		"containsEquip ",
		"containsString ",
		"containsAll ",
		"containsAll ",
		"askAll "
	};

	/**
	 * @param rule
	 * @return
	 */
	public static int get_kind(Rule rule) {
		return get_kind( rule, LayerManager.get_instance());
	}

	/**
	 * @param rule
	 * @param layerManipulator
	 * @return
	 */
	public static int get_kind(Rule rule, ILayerManipulator layerManipulator) {
		if ( rule._value.startsWith( _reservedWords[ 0])
			|| rule._value.startsWith( "!" + _reservedWords[ 0]))
			return 0;
		else if ( rule._value.startsWith( _reservedWords[ 1])
			|| rule._value.startsWith( "!" + _reservedWords[ 1]))
			return 1;
		else if ( rule._value.startsWith( _reservedWords[ 2])
			|| rule._value.startsWith( "!" + _reservedWords[ 2]))
			return 2;
		else if ( rule._value.startsWith( _reservedWords[ 5])
			|| rule._value.startsWith( "!" + _reservedWords[ 5]))
			return 5;
		else if ( rule._value.startsWith( _reservedWords[ 8])
			|| rule._value.startsWith( "!" + _reservedWords[ 8]))
			return 8;
		else {
			String[] elements = CommonRuleManipulator.get_elements( rule._value);
			if ( null == elements || 2 != elements.length)
				return -2;

			String prefix = CommonRuleManipulator.get_full_prefix( elements[ 0]);
			if ( null == prefix)
				return -2;

			if ( rule._value.startsWith( _reservedWords[ 3])
				|| rule._value.startsWith( "!" + _reservedWords[ 3])) {
				if ( CommonRuleManipulator.is_object( "number object", prefix + elements[ 1], layerManipulator))
					return 3;
				else if ( CommonRuleManipulator.is_object( "probability", prefix + elements[ 1], layerManipulator))
					return 4;
				else
					return -2;
			} else if ( rule._value.startsWith( _reservedWords[ 6])
				|| rule._value.startsWith( "!" + _reservedWords[ 6])) {
				if ( CommonRuleManipulator.is_object( "collection", prefix + elements[ 1], layerManipulator))
					return ( ( rule._type.equals( ResourceManager.get_instance().get( "rule.type.condition.collection"))) ? 6 : 7);
				else if ( CommonRuleManipulator.is_object( "list", prefix + elements[ 1], layerManipulator))
					return ( ( rule._type.equals( ResourceManager.get_instance().get( "rule.type.condition.collection"))) ? 7 : 6);
				else
					return -2;
			}
		}

		return -1;
	}

	/**
	 * @param value
	 * @return
	 */
	private static int get_kind2(String value) {
		if ( value.startsWith( _reservedWords[ 0])
			|| value.startsWith( "!" + _reservedWords[ 0]))
			return 0;
		else if ( value.startsWith( _reservedWords[ 1])
			|| value.startsWith( "!" + _reservedWords[ 1]))
			return 1;
		else if ( value.startsWith( _reservedWords[ 2])
			|| value.startsWith( "!" + _reservedWords[ 2]))
			return 2;
		else if ( value.startsWith( _reservedWords[ 3])
			|| value.startsWith( "!" + _reservedWords[ 3]))
			return 3;
//		else if ( value.startsWith( _reserved_words[ 4])
//			|| value.startsWith( "!" + _reserved_words[ 4]))
//			return 4;
		else if ( value.startsWith( _reservedWords[ 5])
			|| value.startsWith( "!" + _reservedWords[ 5]))
			return 5;
		else if ( value.startsWith( _reservedWords[ 6])
			|| value.startsWith( "!" + _reservedWords[ 6]))
			return 6;
//		else if ( value.startsWith( _reserved_words[ 7])
//			|| value.startsWith( "!" + _reserved_words[ 7]))
//			return 7;
		else if ( value.startsWith( _reservedWords[ 8])
			|| value.startsWith( "!" + _reservedWords[ 8]))
			return 8;

		return -1;
	}

	/**
	 * @param rule
	 * @return
	 */
	public static String get_used_agent_name(Rule rule) {
		int kind = get_kind( rule);
		if ( 0 > kind)
			return null;

		String[] elements = CommonRuleManipulator.get_elements( rule._value);
		if ( null == elements || 2 != elements.length)
			return null;

		switch ( kind) {
			case 1:
				if ( elements[ 1].equals( ""))
					break;

				return elements[ 1];
		}

		return null;
	}

	/**
	 * @param rule
	 * @return
	 */
	public static String[] get_used_spot_names(Rule rule) {
		int kind = get_kind( rule);
		if ( 0 > kind)
			return null;

		String[] elements = CommonRuleManipulator.get_elements( rule._value);
		if ( null == elements || 1 > elements.length)
			return null;

		String[] spotNames = new String[] {
			CommonRuleManipulator.extract_spot_name2( elements[ 0]),
			null};

		if ( 2 == elements.length) {
			switch ( kind) {
				case 2:
					if ( elements[ 1].equals( ""))
						break;

					spotNames[ 1] = elements[ 1];
					break;
			}
		}

		return spotNames;
	}

	/**
	 * @param rule
	 * @return
	 */
	public static String get_used_spot_variable_name(Rule rule) {
		int kind = get_kind( rule);
		if ( 0 > kind)
			return null;

		String[] elements = CommonRuleManipulator.get_elements( rule._value);
		if ( null == elements || 1 > elements.length)
			return null;

		return CommonRuleManipulator.get_spot_variable_name2( elements[ 0]);
	}

	/**
	 * @param rule
	 * @return
	 */
	public static String get_used_probability_name(Rule rule) {
		int kind = get_kind( rule);
		if ( 0 > kind)
			return null;

		String[] elements = CommonRuleManipulator.get_elements( rule._value);
		if ( null == elements || 2 != elements.length)
			return null;

		switch ( kind) {
			case 4:
				if ( elements[ 1].equals( ""))
					break;

				String prefix = CommonRuleManipulator.get_full_prefix( elements[ 0]);
				if ( null == prefix)
					break;

				return ( prefix + elements[ 1]);
		}

		return null;
	}

	/**
	 * @param rule
	 * @return
	 */
	public static String[] get_used_collection_names(Rule rule) {
		int kind = get_kind( rule);
		if ( 0 > kind)
			return null;

		String[] elements = CommonRuleManipulator.get_elements( rule._value);
		if ( null == elements || 1 > elements.length)
			return null;

		String[] collectionNames = new String[] {
			rule._type.equals( ResourceManager.get_instance().get( "rule.type.condition.collection")) ? elements[ 0] : null,
			null};

		if ( 2 == elements.length && !elements[ 1].equals( "")
			&& ( ( rule._type.equals( ResourceManager.get_instance().get( "rule.type.condition.collection")) && 6 == kind)
				|| ( rule._type.equals( ResourceManager.get_instance().get( "rule.type.condition.list")) && 7 == kind))) {
			String prefix = CommonRuleManipulator.get_full_prefix( elements[ 0]);
			if ( null != prefix)
				collectionNames[ 1] = ( prefix + elements[ 1]);
		}

		return collectionNames;
	}

	/**
	 * @param rule
	 * @return
	 */
	public static String[] get_used_list_names(Rule rule) {
		int kind = get_kind( rule);
		if ( 0 > kind)
			return null;

		String[] elements = CommonRuleManipulator.get_elements( rule._value);
		if ( null == elements || 1 > elements.length)
			return null;

		String[] listNames = new String[] {
			rule._type.equals( ResourceManager.get_instance().get( "rule.type.condition.list")) ? elements[ 0] : null,
			null};

		if ( 2 == elements.length && !elements[ 1].equals( "")
			&& ( ( rule._type.equals( ResourceManager.get_instance().get( "rule.type.condition.list")) && 6 == kind)
				|| ( rule._type.equals( ResourceManager.get_instance().get( "rule.type.condition.collection")) && 7 == kind))) {
			String prefix = CommonRuleManipulator.get_full_prefix( elements[ 0]);
			if ( null != prefix)
				listNames[ 1] = ( prefix + elements[ 1]);
		}

		return listNames;
	}

	/**
	 * @param rule
	 * @return
	 */
	public static String get_used_number_object_name(Rule rule) {
		int kind = get_kind( rule);
		if ( 0 > kind)
			return null;

		String[] elements = CommonRuleManipulator.get_elements( rule._value);
		if ( null == elements || 2 != elements.length)
			return null;

		switch ( kind) {
			case 3:
				if ( elements[ 1].equals( ""))
					break;

				String prefix = CommonRuleManipulator.get_full_prefix( elements[ 0]);
				if ( null == prefix)
					break;

				return ( prefix + elements[ 1]);
		}

		return null;
	}

	/**
	 * @param newName
	 * @param originalName
	 * @param headName
	 * @param ranges
	 * @param newHeadName
	 * @param newRanges
	 * @param rule
	 * @return
	 */
	public static boolean update_agent_name_and_number(String newName, String originalName, String headName, Vector<String[]> ranges, String newHeadName, Vector<String[]> newRanges, Rule rule) {
		int kind = get_kind( rule);
		if ( 0 > kind)
			return false;

		String[] elements = CommonRuleManipulator.get_elements( rule._value);
		if ( null == elements || 2 > elements.length)
			return false;

		switch ( kind) {
			case 1:
				String agentName = CommonRuleManipulator.get_new_agent_or_spot_name( elements[ 1], newName, originalName, headName, ranges, newHeadName, newRanges);
				if ( null == agentName)
					return false;

				elements[ 1] = agentName;
				break;
			default:
				return false;
		}

		rule._value = ( ( rule._value.startsWith( "!") ? "!" : "") + _reservedWords[ kind]);
		for ( int i = 0; i < elements.length; ++i)
			rule._value += ( ( ( 0 == i) ? "" : "=") + elements[ i]);

		return true;
	}

	/**
	 * @param newName
	 * @param originalName
	 * @param headName
	 * @param ranges
	 * @param newHeadName
	 * @param newRanges
	 * @param rule
	 * @return
	 */
	public static boolean update_spot_name_and_number(String newName, String originalName, String headName, Vector<String[]> ranges, String newHeadName, Vector<String[]> newRanges, Rule rule) {
		int kind = get_kind2( rule._value);
		if ( 0 > kind)
			return false;

		String[] elements = CommonRuleManipulator.get_elements( rule._value);
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
				case 2:
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

		rule._value = ( ( rule._value.startsWith( "!") ? "!" : "") + _reservedWords[ kind]);
		for ( int i = 0; i < elements.length; ++i)
			rule._value += ( ( ( 0 == i) ? "" : "=") + elements[ i]);

		return true;
	}

	/**
	 * @param name
	 * @param newName
	 * @param type
	 * @param rule
	 * @return
	 */
	public static boolean update_spot_variable_name(String name, String newName, String type, Rule rule) {
		int kind = get_kind2( rule._value);
		if ( 0 > kind)
			return false;

		String[] elements = CommonRuleManipulator.get_elements( rule._value);
		if ( null == elements || 1 > elements.length)
			return false;

		elements[ 0] = CommonRuleManipulator.update_spot_variable_name2( elements[ 0], name, newName, type);
		if ( null == elements[ 0])
			return false;

		rule._value = ( ( rule._value.startsWith( "!") ? "!" : "") + _reservedWords[ kind]);
		for ( int i = 0; i < elements.length; ++i)
			rule._value += ( ( ( 0 == i) ? "" : "=") + elements[ i]);

		return true;
	}

	/**
	 * @param name
	 * @param newName
	 * @param type
	 * @param rule
	 * @return
	 */
	public static boolean update_probability_name(String name, String newName, String type, Rule rule) {
		int kind = get_kind( rule);
		if ( 0 > kind)
			return false;

		String[] elements = CommonRuleManipulator.get_elements( rule._value);
		if ( null == elements || 2 != elements.length || elements[ 1].equals( ""))
			return false;

		switch ( kind) {
			case 4:
				String prefix = CommonRuleManipulator.get_full_prefix( elements[ 0]);
				if ( null == prefix || !CommonRuleManipulator.correspond( prefix, elements[ 1], name, type))
					return false;

				elements[ 1] = newName;
				break;
			default:
				return false;
		}

		rule._value = ( ( rule._value.startsWith( "!") ? "!" : "") + _reservedWords[ kind]);
		for ( int i = 0; i < elements.length; ++i)
			rule._value += ( ( ( 0 == i) ? "" : "=") + elements[ i]);

		return true;
	}

	/**
	 * @param name
	 * @param newName
	 * @param type
	 * @param rule
	 * @return
	 */
	public static boolean update_collection_name(String name, String newName, String type, Rule rule) {
		int kind = get_kind( rule);
		if ( 0 > kind)
			return false;

		String[] elements = CommonRuleManipulator.get_elements( rule._value);
		if ( null == elements || 1 > elements.length)
			return false;

		boolean result1 = false;
		if ( rule._type.equals( ResourceManager.get_instance().get( "rule.type.condition.collection"))) {
			String element0 = CommonRuleManipulator.update_object_name( elements[ 0], name, newName, type);
			if ( null != element0) {
				elements[ 0] = element0;
				result1 = true;
			}
		}

		boolean result2 = false;
		if ( 2 == elements.length && !elements[ 1].equals( "")
			&& ( ( rule._type.equals( ResourceManager.get_instance().get( "rule.type.condition.collection")) && 6 == kind)
				|| ( rule._type.equals( ResourceManager.get_instance().get( "rule.type.condition.list")) && 7 == kind))) {
			String prefix = CommonRuleManipulator.get_full_prefix( elements[ 0]);
			if ( null != prefix && CommonRuleManipulator.correspond( prefix, elements[ 1], name, type)) {
				elements[ 1] = newName;
				result2 = true;
			}
		}

		if ( !result1 && !result2)
			return false;

		rule._value = ( ( rule._value.startsWith( "!") ? "!" : "") + _reservedWords[ kind]);
		for ( int i = 0; i < elements.length; ++i)
			rule._value += ( ( ( 0 == i) ? "" : "=") + elements[ i]);

		return true;
	}

	/**
	 * @param name
	 * @param newName
	 * @param type
	 * @param rule
	 * @return
	 */
	public static boolean update_list_name(String name, String newName, String type, Rule rule) {
		int kind = get_kind( rule);
		if ( 0 > kind)
			return false;

		String[] elements = CommonRuleManipulator.get_elements( rule._value);
		if ( null == elements || 1 > elements.length)
			return false;

		boolean result1 = false;
		if ( rule._type.equals( ResourceManager.get_instance().get( "rule.type.condition.list"))) {
			String element0 = CommonRuleManipulator.update_object_name( elements[ 0], name, newName, type);
			if ( null != element0) {
				elements[ 0] = element0;
				result1 = true;
			}
		}

		boolean result2 = false;
		if ( 2 == elements.length && !elements[ 1].equals( "")
			&& ( ( rule._type.equals( ResourceManager.get_instance().get( "rule.type.condition.list")) && 6 == kind)
				|| ( rule._type.equals( ResourceManager.get_instance().get( "rule.type.condition.collection")) && 7 == kind))) {
			String prefix = CommonRuleManipulator.get_full_prefix( elements[ 0]);
			if ( null != prefix && CommonRuleManipulator.correspond( prefix, elements[ 1], name, type)) {
				elements[ 1] = newName;
				result2 = true;
			}
		}

		if ( !result1 && !result2)
			return false;

		rule._value = ( ( rule._value.startsWith( "!") ? "!" : "") + _reservedWords[ kind]);
		for ( int i = 0; i < elements.length; ++i)
			rule._value += ( ( ( 0 == i) ? "" : "=") + elements[ i]);

		return true;
	}

	/**
	 * @param name
	 * @param newName
	 * @param type
	 * @param rule
	 * @return
	 */
	public static boolean update_number_object_name(String name, String newName, String type, Rule rule) {
		int kind = get_kind( rule);
		if ( 0 > kind)
			return false;

		String[] elements = CommonRuleManipulator.get_elements( rule._value);
		if ( null == elements || 2 != elements.length || elements[ 1].equals( ""))
			return false;

		switch ( kind) {
			case 3:
				String prefix = CommonRuleManipulator.get_full_prefix( elements[ 0]);
				if ( null == prefix || !CommonRuleManipulator.correspond( prefix, elements[ 1], name, type))
					return false;

				elements[ 1] = newName;
				break;
			default:
				return false;
		}

		rule._value = ( ( rule._value.startsWith( "!") ? "!" : "") + _reservedWords[ kind]);
		for ( int i = 0; i < elements.length; ++i)
			rule._value += ( ( ( 0 == i) ? "" : "=") + elements[ i]);

		return true;
	}

	/**
	 * @param rule
	 * @param role
	 * @param drawObjects
	 * @return
	 */
	public static boolean can_paste(Rule rule, Role role, Layer drawObjects) {
		int kind = get_kind( rule, drawObjects);
		if ( 0 > kind)
			return false;

		return can_paste( kind, rule, role, drawObjects);
	}

	/**
	 * @param kind
	 * @param rule
	 * @param role
	 * @param drawObjects
	 * @return
	 */
	public static boolean can_paste(int kind, Rule rule, Role role, Layer drawObjects) {
		if ( !can_paste_agent_name( kind, rule, drawObjects))
			return false;

		if ( !can_paste_spot_name( kind, rule, drawObjects))
			return false;

		if ( !can_paste_probability_name( kind, rule, drawObjects))
			return false;

		if ( !can_paste_collection_name( kind, rule, drawObjects))
			return false;

		if ( !can_paste_list_name( kind, rule, drawObjects))
			return false;

		if ( !can_paste_number_object_name( kind, rule, drawObjects))
			return false;

		return true;
	}

	/**
	 * @param kind
	 * @param value
	 * @param type
	 * @param drawObjects
	 * @return
	 */
	private static boolean can_paste_agent_name(int kind, Rule rule, Layer drawObjects) {
		if ( 1 != kind)
			return true;

		String[] elements = CommonRuleManipulator.get_elements( rule._value);
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
	 * @param value
	 * @param type
	 * @param drawObjects
	 * @return
	 */
	private static boolean can_paste_spot_name(int kind, Rule rule, Layer drawObjects) {
		if ( 2 != kind)
			return true;

		String[] elements = CommonRuleManipulator.get_elements( rule._value);
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
	 * @param value
	 * @param type
	 * @param drawObjects
	 * @return
	 */
	private static boolean can_paste_probability_name(int kind, Rule rule, Layer drawObjects) {
		if ( 4 != kind)
			return true;

		String[] elements = CommonRuleManipulator.get_elements( rule._value);
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
	 * @param value
	 * @param type
	 * @param drawObjects
	 * @return
	 */
	private static boolean can_paste_collection_name(int kind, Rule rule, Layer drawObjects) {
		String[] elements = CommonRuleManipulator.get_elements( rule._value);
		if ( null == elements || 1 > elements.length)
			return false;

		if ( rule._type.equals( ResourceManager.get_instance().get( "rule.type.condition.collection"))
			&& !CommonRuleManipulator.can_paste_object( "collection", elements[ 0], drawObjects))
			return false;

		if ( ( rule._type.equals( ResourceManager.get_instance().get( "rule.type.condition.collection")) && 6 == kind)
			|| ( rule._type.equals( ResourceManager.get_instance().get( "rule.type.condition.list")) && 7 == kind)) {
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
	 * @param value
	 * @param type
	 * @param drawObjects
	 * @return
	 */
	private static boolean can_paste_list_name(int kind, Rule rule, Layer drawObjects) {
		String[] elements = CommonRuleManipulator.get_elements( rule._value);
		if ( null == elements || 1 > elements.length)
			return false;

		if ( rule._type.equals( ResourceManager.get_instance().get( "rule.type.condition.list"))
			&& !CommonRuleManipulator.can_paste_object( "list", elements[ 0], drawObjects))
			return false;

		if ( ( rule._type.equals( ResourceManager.get_instance().get( "rule.type.condition.list")) && 6 == kind)
			|| ( rule._type.equals( ResourceManager.get_instance().get( "rule.type.condition.collection")) && 7 == kind)) {
			if ( 2 > elements.length || null == elements[ 1] || elements[ 1].equals( ""))
				return false;

			String prefix = CommonRuleManipulator.get_full_prefix( elements[ 0]);
			if ( null == prefix)
				return false;

			return CommonRuleManipulator.can_paste_object( "list", prefix + elements[ 1], drawObjects);
		}

		return true;
	}

	/**
	 * @param kind
	 * @param value
	 * @param type
	 * @param drawObjects
	 * @return
	 */
	private static boolean can_paste_number_object_name(int kind, Rule rule, Layer drawObjects) {
		if ( 3 != kind)
			return true;

		String[] elements = CommonRuleManipulator.get_elements( rule._value);
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
	 * @param value
	 * @return
	 */
	public static String get_script(String value) {
		// TODO 修正済
		String denial = ( value.startsWith( "!") ? "!" : "");

		String method = CommonRuleManipulator.get_reserved_word( value);
		if ( null == method)
			return "";

		String[] elements = CommonRuleManipulator.get_elements( value);
		if ( null == elements)
			return "";

		String[] prefixAndObject = get_prefix_and_object( elements[ 0]);
		//String[] prefixAndObject = CommonRuleManipulator.get_prefix_and_object( elements[ 0]);

		String script = ( denial + prefixAndObject[ 0] + method + prefixAndObject[ 1]);

		for ( int i = 1; i < elements.length; ++i) {
			prefixAndObject = get_prefix_and_object( elements[ i]);	// 旧いデータへの配慮 - 旧いデータでは頭に<>または<spot>がついているので、それを取り除く必要がある
			//prefixAndObject = CommonRuleManipulator.get_prefix_and_object( elements[ i]);	// 旧いデータへの配慮
			script += ( "=" + prefixAndObject[ 1]);
		}

		return script;
	}

	/**
	 * @param element
	 * @return
	 */
	public static String[] get_prefix_and_object(String element) {
		// TODO 追加
		String[] spotAndObject = CommonRuleManipulator.separate_into_spot_and_object( element);
		if ( null == spotAndObject) {
			if ( CommonRuleManipulator.is_object( "role variable", element))
				return new String[] { "", "$Role." + element};
			else if ( CommonRuleManipulator.is_object( "time variable", element))
				return new String[] { "", "$Time." + element};
			else
				return new String[] { "", element};
		} else {
			if ( CommonRuleManipulator.is_object( "role variable", element))
				spotAndObject[ 1] = "$Role." + spotAndObject[ 1];
			else if ( CommonRuleManipulator.is_object( "time variable", element))
				spotAndObject[ 1] = "$Time." + spotAndObject[ 1];
			if ( spotAndObject[ 0].equals( ""))
				return new String[] { "<>", spotAndObject[ 1]};
			else
				return new String[] { "<" + spotAndObject[ 0] + ">", spotAndObject[ 1]};
		}
	}

	/**
	 * @param value
	 * @return
	 */
	public static String get_cell_text(String value) {
		// TODO 修正済
		//return get_script( value);
		String denial = ( value.startsWith( "!") ? "!" : "");

		String method = CommonRuleManipulator.get_reserved_word( value);
		if ( null == method)
			return "";

		String[] elements = CommonRuleManipulator.get_elements( value);
		if ( null == elements)
			return "";

		String[] prefixAndObject = CommonRuleManipulator.get_prefix_and_object( elements[ 0]);

		String script = ( denial + prefixAndObject[ 0] + method + prefixAndObject[ 1]);

		for ( int i = 1; i < elements.length; ++i) {
			prefixAndObject = CommonRuleManipulator.get_prefix_and_object( elements[ i]);	// 旧いデータへの配慮
			script += ( "=" + prefixAndObject[ 1]);
		}

		return script;
	}
}
