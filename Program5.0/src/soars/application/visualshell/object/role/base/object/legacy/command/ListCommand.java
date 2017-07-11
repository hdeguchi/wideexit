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

/**
 * @author kurata
 *
 */
public class ListCommand extends Rule {

	/**
	 * Reserved words
	 */
	public static String[] _reservedWords = {
		"addFirstAgent ",
		"addFirstSpot ",
		"addFirstEquip ",
		"addFirstEquip ",
		"addFirstEquip ",
		"addFirstEquip ",
		"addFirstString ",

		"addLastAgent ",
		"addLastSpot ",
		"addLastEquip ",
		"addLastEquip ",
		"addLastEquip ",
		"addLastEquip ",
		"addLastString ",

		"equipFirst ",
		"equipFirst ",
		"equipFirst ",
		"equipFirst ",
		"equipLast ",
		"equipLast ",
		"equipLast ",
		"equipLast ",

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
		"removeFirst ",
		"removeLast ",
		"retainAll ",
		"retainAll ",
		"addAll ",
		"addAll ",

		"reverseAll ",
		"shuffleAll ",
		"sortAll ",
		"rotateAll ",
		"rotateAll ",
		"rotateAll ",
		"rotateAll ",

		"setAll ",
		"moveToRandomOne ",
		"moveToFirst ",
		"moveToLast "
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
		else if ( value.startsWith( _reservedWords[ 6]))
			return 6;
		else if ( value.startsWith( _reservedWords[ 7]))
			return 7;
		else if ( value.startsWith( _reservedWords[ 8]))
			return 8;
		else if ( value.startsWith( _reservedWords[ 13]))
			return 13;
		else if ( value.startsWith( _reservedWords[ 22]))
			return 22;
		else if ( value.startsWith( _reservedWords[ 23]))
			return 23;
		else if ( value.startsWith( _reservedWords[ 24]))
			return 24;
		else if ( value.startsWith( _reservedWords[ 25]))
			return get_kind( value, 25, 26, 31, layerManipulator);
		else if ( value.startsWith( _reservedWords[ 32]))
			return 32;
		else if ( value.startsWith( _reservedWords[ 33]))
			return 33;
		else if ( value.startsWith( _reservedWords[ 34]))
			return 34;
		else if ( value.startsWith( _reservedWords[ 35]))
			return get_kind( value, 35, 36, layerManipulator);
		else if ( value.startsWith( _reservedWords[ 37]))
			return get_kind( value, 37, 38, layerManipulator);
		else if ( value.startsWith( _reservedWords[ 39]))
			return 39;
		else if ( value.startsWith( _reservedWords[ 40]))
			return 40;
		else if ( value.startsWith( _reservedWords[ 41]))
			return 41;
		else if ( value.startsWith( _reservedWords[ 46]))
			return 46;
		else if ( value.startsWith( _reservedWords[ 47]))
			return 47;
		else if ( value.startsWith( _reservedWords[ 48]))
			return 48;
		else if ( value.startsWith( _reservedWords[ 49]))
			return 49;
		else {
			String[] elements = CommonRuleManipulator.get_elements( value);
			if ( null == elements || 2 != elements.length)
				return -2;

//			String[] values = CommonRuleManipulator.get_spot_and_object2( elements[ 0], layerManipulator);
//			if ( null == values)
//				return -2;

			String prefix = CommonRuleManipulator.get_full_prefix( elements[ 0]);
			if ( null == prefix)
				return -2;

			if ( value.startsWith( _reservedWords[ 2])) {
				if ( CommonRuleManipulator.is_object( "probability", prefix + elements[ 1], layerManipulator))
					return 2;
				else if ( CommonRuleManipulator.is_object( "keyword", prefix + elements[ 1], layerManipulator))
					return 3;
				else if ( CommonRuleManipulator.is_object( "number object", prefix + elements[ 1], layerManipulator))
					return 4;
				else
					return 5;
			} else if ( value.startsWith( _reservedWords[ 9])) {
				if ( CommonRuleManipulator.is_object( "probability", prefix + elements[ 1], layerManipulator))
					return 9;
				else if ( CommonRuleManipulator.is_object( "keyword", prefix + elements[ 1], layerManipulator))
					return 10;
				else if ( CommonRuleManipulator.is_object( "number object", prefix + elements[ 1], layerManipulator))
					return 11;
				else
					return 12;
			} else if ( value.startsWith( _reservedWords[ 14])) {
				if ( CommonRuleManipulator.is_object( "probability", elements[ 0], layerManipulator))
					return 14;
				else if ( CommonRuleManipulator.is_object( "keyword", elements[ 0], layerManipulator))
					return 15;
				else if ( CommonRuleManipulator.is_object( "number object", elements[ 0], layerManipulator))
					return 16;
				else
					return 17;
			} else if ( value.startsWith( _reservedWords[ 18])) {
				if ( CommonRuleManipulator.is_object( "probability", elements[ 0], layerManipulator))
					return 18;
				else if ( CommonRuleManipulator.is_object( "keyword", elements[ 0], layerManipulator))
					return 19;
				else if ( CommonRuleManipulator.is_object( "number object", elements[ 0], layerManipulator))
					return 20;
				else
					return 21;
			} else if ( value.startsWith( _reservedWords[ 27])) {
				if ( CommonRuleManipulator.is_object( "probability", prefix + elements[ 1], layerManipulator))
					return 27;
				else if ( CommonRuleManipulator.is_object( "keyword", prefix + elements[ 1], layerManipulator))
					return 28;
				else if ( CommonRuleManipulator.is_object( "number object", prefix + elements[ 1], layerManipulator))
					return 29;
				else
					return 30;
			} else if ( value.startsWith( _reservedWords[ 42])) {
				if ( elements[ 1].startsWith( "-")) {
					String element = elements[ 1].substring( 1);
					if ( CommonRuleManipulator.is_object( "number object", prefix + element, layerManipulator))
						return 43;
					else
						return 42;
				} else {
					if ( CommonRuleManipulator.is_object( "number object", prefix + elements[ 1], layerManipulator))
						return 45;
					else
						return 44;
				}
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

		String prefix = CommonRuleManipulator.get_full_prefix( elements[ 0]);
		if ( null == prefix)
			return -2;

		if ( CommonRuleManipulator.is_object( "collection", prefix + elements[ 1], layerManipulator))
			return index2;
		else if ( CommonRuleManipulator.is_object( "list", prefix + elements[ 1], layerManipulator))
			return index1;
		else
			return -2;
	}

//	/**
//	 * @param value
//	 * @return
//	 */
//	private static int get_kind2(String value) {
//		if ( value.startsWith( _reservedWords[ 0]))
//			return 0;
//		else if ( value.startsWith( _reservedWords[ 1]))
//			return 1;
//		else if ( value.startsWith( _reservedWords[ 2]))
//			return 2;
////		else if ( value.startsWith( _reserved_words[ 3]))
////			return 3;
////		else if ( value.startsWith( _reserved_words[ 4]))
////			return 4;
////		else if ( value.startsWith( _reserved_words[ 5]))
////			return 5;
//		else if ( value.startsWith( _reservedWords[ 6]))
//			return 6;
//		else if ( value.startsWith( _reservedWords[ 7]))
//			return 7;
//		else if ( value.startsWith( _reservedWords[ 8]))
//			return 8;
//		else if ( value.startsWith( _reservedWords[ 9]))
//			return 9;
////		else if ( value.startsWith( _reserved_words[ 10]))
////			return 10;
////		else if ( value.startsWith( _reserved_words[ 11]))
////			return 11;
////		else if ( value.startsWith( _reserved_words[ 12]))
////			return 12;
//		else if ( value.startsWith( _reservedWords[ 13]))
//			return 13;
//		else if ( value.startsWith( _reservedWords[ 14]))
//			return 14;
////		else if ( value.startsWith( _reserved_words[ 15]))
////			return 15;
////		else if ( value.startsWith( _reserved_words[ 16]))
////			return 16;
////		else if ( value.startsWith( _reserved_words[ 17]))
////			return 17;
//		else if ( value.startsWith( _reservedWords[ 18]))
//			return 18;
////		else if ( value.startsWith( _reserved_words[ 19]))
////			return 19;
////		else if ( value.startsWith( _reserved_words[ 20]))
////			return 20;
////		else if ( value.startsWith( _reserved_words[ 21]))
////			return 21;
//		else if ( value.startsWith( _reservedWords[ 22]))
//			return 22;
//		else if ( value.startsWith( _reservedWords[ 23]))
//			return 23;
//		else if ( value.startsWith( _reservedWords[ 24]))
//			return 24;
//		else if ( value.startsWith( _reservedWords[ 25]))
//			return 25;
////		else if ( value.startsWith( _reserved_words[ 26]))
////			return 26;
////		else if ( value.startsWith( _reserved_words[ 31]))
////			return 31;
//		else if ( value.startsWith( _reservedWords[ 27]))
//			return 27;
////		else if ( value.startsWith( _reserved_words[ 28]))
////			return 28;
////		else if ( value.startsWith( _reserved_words[ 29]))
////			return 29;
////		else if ( value.startsWith( _reserved_words[ 30]))
////			return 30;
//		else if ( value.startsWith( _reservedWords[ 32]))
//			return 32;
//		else if ( value.startsWith( _reservedWords[ 33]))
//			return 33;
//		else if ( value.startsWith( _reservedWords[ 34]))
//			return 34;
//		else if ( value.startsWith( _reservedWords[ 35]))
//			return 35;
////		else if ( value.startsWith( _reserved_words[ 36]))
////			return 36;
//		else if ( value.startsWith( _reservedWords[ 37]))
//			return 37;
////		else if ( value.startsWith( _reserved_words[ 38]))
////			return 38;
//		else if ( value.startsWith( _reservedWords[ 39]))
//			return 39;
//		else if ( value.startsWith( _reservedWords[ 40]))
//			return 40;
//		else if ( value.startsWith( _reservedWords[ 41]))
//			return 41;
//		else if ( value.startsWith( _reservedWords[ 42]))
//			return 42;
////		else if ( value.startsWith( _reserved_words[ 43]))
////			return 43;
////		else if ( value.startsWith( _reserved_words[ 44]))
////			return 44;
////		else if ( value.startsWith( _reserved_words[ 45]))
////			return 45;
//		else if ( value.startsWith( _reservedWords[ 46]))
//			return 46;
//		else if ( value.startsWith( _reservedWords[ 47]))
//			return 47;
//		else if ( value.startsWith( _reservedWords[ 48]))
//			return 48;
//		else if ( value.startsWith( _reservedWords[ 49]))
//			return 49;
//
//		return -1;
//	}

	/**
	 * @param kind
	 * @param type
	 * @param value
	 */
	public ListCommand(String kind, String type, String value) {
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
			case 0:
			case 7:
			case 23:
				if ( elements[ 1].equals( ""))
					break;

				return elements[ 1];
			case 17:
			case 21:
				elements = CommonRuleManipulator.get_prefix_and_object( elements[ 0]);
				return ( ( null != LayerManager.get_instance().get_agent_has_this_name( elements[ 1])) ? elements[ 1] : null);
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

		String[] usedSpotNames = new String[] { null, null};

		usedSpotNames[ 0] = CommonRuleManipulator.extract_spot_name2( elements[ 0]);

		if ( 2 == elements.length) {
			switch ( kind) {
				case 1:
				case 8:
				case 22:
					if ( elements[ 1].equals( ""))
						break;

					usedSpotNames[ 1] = elements[ 1];
					break;
				case 17:
				case 21:
					elements = CommonRuleManipulator.get_prefix_and_object( elements[ 0]);
					usedSpotNames[ 1] = ( ( null != LayerManager.get_instance().get_spot_has_this_name( elements[ 1])) ? elements[ 1] : null);
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
			case 2:
			case 9:
			case 27:
				if ( elements[ 1].equals( ""))
					break;

				String prefix = CommonRuleManipulator.get_full_prefix( elements[ 0]);
				if ( null == prefix)
					break;

				return ( prefix + elements[ 1]);
			case 14:
			case 18:
				if ( elements[ 0].equals( ""))
					break;

				return elements[ 0];
		}

		return null;
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.role.base.rule.base.Rule#get_used_collection_names()
	 */
	@Override
	protected String[] get_used_collection_names() {
		return new String[] { get_used_collection_name()};
	}

	/**
	 * @return
	 */
	private String get_used_collection_name() {
//		int kind = get_kind( _value);
//		if ( 0 > kind)
//			return null;
//
//		String[] elements = CommonRuleManipulator.get_elements( _value);
//		if ( null == elements || 1 > elements.length)
//			return null;
//
//		if ( 2 == elements.length && !elements[ 1].equals( "") && ( 26 == kind || 36 == kind || 38 == kind)) {
//			String prefix = CommonRuleManipulator.get_full_prefix( elements[ 0]);
//			if ( null == prefix)
//				return null;
//
//			return ( prefix + elements[ 1]);
//		}
//
//		return null;
		// TODO 要動作確認！
		int kind = get_kind( _value);
		if ( 0 > kind)
			return null;

		String[] elements = CommonRuleManipulator.get_elements( _value);
		if ( null == elements || 1 > elements.length)
			return null;

		if ( 2 == elements.length && !elements[ 1].equals( "") && ( 26 == kind || 36 == kind || 38 == kind)) {
			String prefix = CommonRuleManipulator.get_full_prefix( elements[ 0]);
			if ( null == prefix)
				return null;

			return ( prefix + elements[ 1]);
		}

		if ( !elements[ 0].equals( "") && ( 17 == kind || 21 == kind)) {
			if ( CommonRuleManipulator.is_object( "collection", elements[ 0]))
				return elements[ 0];
		}

		if ( 2 == elements.length && !elements[ 1].equals( "") && ( 5 == kind || 12 == kind)) {
			String prefix = CommonRuleManipulator.get_full_prefix( elements[ 0]);
			if ( null == prefix)
				return null;

			if ( CommonRuleManipulator.is_object( "collection", prefix + elements[ 1]))
				return ( prefix + elements[ 1]);
		}

		return null;
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.role.base.rule.base.Rule#get_used_list_names()
	 */
	@Override
	protected String[] get_used_list_names() {
//		int kind = get_kind( _value);
//		if ( 0 > kind)
//			return null;
//
//		String[] elements = CommonRuleManipulator.get_elements( _value);
//		if ( null == elements || 1 > elements.length)
//			return null;
//
//		String[] used_list_names = new String[] {
//			( 14 > kind || 21 < kind) ? elements[ 0] : null,
//			null};
//
//		if ( 2 == elements.length && !elements[ 1].equals( "")) {
//			switch ( kind) {
//				case 14:
//				case 15:
//				case 16:
//				case 17:
//				case 18:
//				case 19:
//				case 20:
//				case 21:
//				case 25:
//				case 35:
//				case 37:
//					String prefix = CommonRuleManipulator.get_full_prefix( elements[ 0]);
//					if ( null == prefix)
//						return null;
//
//					used_list_names[ 1] = ( prefix + elements[ 1]);
//					break;
//			}
//		}
//
//		return used_list_names;
		// TODO 要動作確認！
		int kind = get_kind( _value);
		if ( 0 > kind)
			return null;

		String[] elements = CommonRuleManipulator.get_elements( _value);
		if ( null == elements || 1 > elements.length)
			return null;

		List<String> list = new ArrayList<String>();

		if ( !elements[ 0].equals( "") && ( 14 > kind || 21 < kind))
			list.add( elements[ 0]);

		if ( !elements[ 0].equals( "") && ( 17 == kind || 21 == kind)) {
			if ( CommonRuleManipulator.is_object( "list", elements[ 0]))
				list.add( elements[ 0]);
		}

		if ( 2 == elements.length && !elements[ 1].equals( "")) {
			switch ( kind) {
				case 14:
				case 15:
				case 16:
				case 17:
				case 18:
				case 19:
				case 20:
				case 21:
				case 25:
				case 35:
				case 37:
					String prefix = CommonRuleManipulator.get_full_prefix( elements[ 0]);
					if ( null == prefix)
						return null;

					list.add( prefix + elements[ 1]);
					break;
			}

			if ( 5 == kind || 12 == kind) {
				String prefix = CommonRuleManipulator.get_full_prefix( elements[ 0]);
				if ( null == prefix)
					return null;

				if ( CommonRuleManipulator.is_object( "list", prefix + elements[ 1]))
					list.add( prefix + elements[ 1]);
			}
		}

		return ( list.isEmpty() ? null : list.toArray( new String[ 0]));
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

		switch ( kind) {
			case 3:
			case 10:
			case 28:
				if ( elements[ 1].equals( ""))
					break;

				String prefix = CommonRuleManipulator.get_full_prefix( elements[ 0]);
				if ( null == prefix)
					break;

				return ( prefix + elements[ 1]);
			case 15:
			case 19:
				if ( elements[ 0].equals( ""))
					break;

				return elements[ 0];
		}

		return null;
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

		switch ( kind) {
			case 4:
			case 11:
			case 29:
			case 43:
			case 45:
				if ( elements[ 1].startsWith( "-"))
					elements[ 1] = elements[ 1].substring( 1);

				if ( elements[ 1].equals( ""))
					break;

				String prefix = CommonRuleManipulator.get_full_prefix( elements[ 0]);
				if ( null == prefix)
					break;

				return ( prefix + elements[ 1]);
			case 16:
			case 20:
				if ( elements[ 0].equals( ""))
					break;

				return elements[ 0];
		}

		return null;
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
		if ( 0 > kind || ( 5 != kind && 12 != kind && 17 != kind && 21 != kind && 30 != kind))
			return null;

		String[] elements = CommonRuleManipulator.get_elements( _value);
		if ( null == elements || 2 != elements.length)
			return null;

		if ( !elements[ 0].equals( "") && ( 17 == kind || 21 == kind)) {
			if ( CommonRuleManipulator.is_object( type, elements[ 0]))
				return elements[ 0];
		}

		if ( !elements[ 1].equals( "") && ( 5 == kind || 12 == kind || 30 == kind)) {
			String prefix = CommonRuleManipulator.get_full_prefix( elements[ 0]);
			if ( null == prefix)
				return null;

			if ( CommonRuleManipulator.is_object( type, prefix + elements[ 1]))
				return ( prefix + elements[ 1]);
		}

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
			case 0:
			case 7:
			case 23:
				String agent_name = CommonRuleManipulator.get_new_agent_or_spot_name( elements[ 1], newName, originalName, headName, ranges, newHeadName, newRanges);
				if ( null == agent_name)
					return false;

				elements[ 1] = agent_name;
				break;
			case 17:
			case 21:
				String prefixAndObject[] = CommonRuleManipulator.get_prefix_and_object( elements[ 0]);
				if ( null == LayerManager.get_instance().get_agent_has_this_name( prefixAndObject[ 1]))
					return false;

				agent_name = CommonRuleManipulator.get_new_agent_or_spot_name( prefixAndObject[ 1], newName, originalName, headName, ranges, newHeadName, newRanges);
				if ( null == agent_name)
					return false;

				elements[ 0] = ( prefixAndObject[ 0] + agent_name);
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
//		int kind = get_kind2( _value);
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
				case 8:
				case 22:
					element = CommonRuleManipulator.get_new_agent_or_spot_name( elements[ 1], newName, originalName, headName, ranges, newHeadName, newRanges);
					if ( null != element) {
						elements[ 1] = element;
						result2 = true;
					}
					break;
				case 17:
				case 21:
					String prefixAndObject[] = CommonRuleManipulator.get_prefix_and_object( elements[ 0]);
					if ( null == LayerManager.get_instance().get_spot_has_this_name( prefixAndObject[ 1]))
						break;

					element = CommonRuleManipulator.get_new_agent_or_spot_name( prefixAndObject[ 1], newName, originalName, headName, ranges, newHeadName, newRanges);
					if ( null == element)
						break;

					elements[ 0] = ( prefixAndObject[ 0] + element);
					result2 = true;
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
//		int kind = get_kind2( _value);
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
//		return true;
		// TODO 要動作確認！
//		int kind = get_kind2( _value);
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

		kind = get_kind( _value);
		if ( ( 5 == kind || 12 == kind || 17 == kind || 21 == kind || 30 == kind) && update_object_name( kind, elements, name, newName, type))
			result = true;

		if ( !result)
			return false;

		_value = _reservedWords[ kind];
		for ( int i = 0; i < elements.length; ++i)
			_value += ( ( ( 0 == i) ? "" : "=") + elements[ i]);

		return true;
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
		if ( null == elements || 2 != elements.length)
			return false;

		switch ( kind) {
			case 2:
			case 9:
			case 27:
				if ( elements[ 1].equals( ""))
					return false;

				String prefix = CommonRuleManipulator.get_full_prefix( elements[ 0]);
				if ( null == prefix || !CommonRuleManipulator.correspond( prefix, elements[ 1], name, type))
					return false;

				elements[ 1] = newName;
				break;
			case 14:
			case 18:
				if ( elements[ 0].equals( ""))
					return false;

				elements[ 0] = CommonRuleManipulator.update_object_name( elements[ 0], name, newName, type);
				if ( null == elements[ 0])
					return false;

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
//		int kind = get_kind( _value);
//		if ( 0 > kind)
//			return false;
//
//		String[] elements = CommonRuleManipulator.get_elements( _value);
//		if ( null == elements || 1 > elements.length)
//			return false;
//
//		if ( 2 == elements.length && !elements[ 1].equals( "") && ( 26 == kind || 36 == kind || 38 == kind)) {
//			String prefix = CommonRuleManipulator.get_full_prefix( elements[ 0]);
//			if ( null == prefix || !CommonRuleManipulator.correspond( prefix, elements[ 1], name, type))
//				return false;
//
//			elements[ 1] = newName;
//		} else
//			return false;
//
//		_value = _reservedWords[ kind];
//		for ( int i = 0; i < elements.length; ++i)
//			_value += ( ( ( 0 == i) ? "" : "=") + elements[ i]);
//
//		return true;
		// TODO 要動作確認！
		int kind = get_kind( _value);
		if ( 0 > kind)
			return false;

		String[] elements = CommonRuleManipulator.get_elements( _value);
		if ( null == elements || 1 > elements.length)
			return false;

		boolean result = false;

		if ( 2 == elements.length && !elements[ 1].equals( "") && ( 26 == kind || 36 == kind || 38 == kind)) {
			String prefix = CommonRuleManipulator.get_full_prefix( elements[ 0]);
			if ( null != prefix && CommonRuleManipulator.correspond( prefix, elements[ 1], name, type)) {
				elements[ 1] = newName;
				result = true;
			}
		}

		if ( !elements[ 0].equals( "") && ( 17 == kind || 21 == kind)) {
			String element = CommonRuleManipulator.update_object_name( elements[ 0], name, newName, type);
			if ( null != element) {
				elements[ 0] = element;
				result = true;
			}
		}

		if ( 2 == elements.length && !elements[ 1].equals( "") && ( 5 == kind || 12 == kind)) {
			String prefix = CommonRuleManipulator.get_full_prefix( elements[ 0]);
			if ( null != prefix && CommonRuleManipulator.correspond( prefix, elements[ 1], name, type)) {
				elements[ 1] = newName;
				result = true;
			}
		}

		if ( !result)
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
//		int kind = get_kind( _value);
//		if ( 0 > kind)
//			return false;
//
//		String[] elements = CommonRuleManipulator.get_elements( _value);
//		if ( null == elements || 1 > elements.length)
//			return false;
//
//		boolean result1 = false;
//		if ( 14 > kind || 21 < kind) {
//			String element0 = CommonRuleManipulator.update_object_name( elements[ 0], name, newName, type);
//			if ( null != element0) {
//				elements[ 0] = element0;
//				result1 = true;
//			}
//		}
//
//		boolean result2 = false;
//		if ( 2 == elements.length && !elements[ 1].equals( "")) {
//			switch ( kind) {
//				case 14:
//				case 15:
//				case 16:
//				case 17:
//				case 18:
//				case 19:
//				case 20:
//				case 21:
//				case 25:
//				case 35:
//				case 37:
//					String prefix = CommonRuleManipulator.get_full_prefix( elements[ 0]);
//					if ( null != prefix && CommonRuleManipulator.correspond( prefix, elements[ 1], name, type)) {
//						elements[ 1] = newName;
//						result2 = true;
//					}
//					break;
//			}
//		}
//
//		if ( !result1 && !result2)
//			return false;
//
//		_value = _reservedWords[ kind];
//		for ( int i = 0; i < elements.length; ++i)
//			_value += ( ( ( 0 == i) ? "" : "=") + elements[ i]);
//
//		return true;
		// TODO 要動作確認！
		int kind = get_kind( _value);
		if ( 0 > kind)
			return false;

		String[] elements = CommonRuleManipulator.get_elements( _value);
		if ( null == elements || 1 > elements.length)
			return false;

		boolean result = false;

		if ( 14 > kind || 21 < kind) {
			String element = CommonRuleManipulator.update_object_name( elements[ 0], name, newName, type);
			if ( null != element) {
				elements[ 0] = element;
				result = true;
			}
		}

		if ( !elements[ 0].equals( "") && ( 17 == kind || 21 == kind)) {
			String element = CommonRuleManipulator.update_object_name( elements[ 0], name, newName, type);
			if ( null != element) {
				elements[ 0] = element;
				result = true;
			}
		}

		if ( 2 == elements.length && !elements[ 1].equals( "")) {
			switch ( kind) {
				case 14:
				case 15:
				case 16:
				case 17:
				case 18:
				case 19:
				case 20:
				case 21:
				case 25:
				case 35:
				case 37:
					String prefix = CommonRuleManipulator.get_full_prefix( elements[ 0]);
					if ( null != prefix && CommonRuleManipulator.correspond( prefix, elements[ 1], name, type)) {
						elements[ 1] = newName;
						result = true;
					}
					break;
			}

			if ( 5 == kind || 12 == kind) {
				String prefix = CommonRuleManipulator.get_full_prefix( elements[ 0]);
				if ( null != prefix && CommonRuleManipulator.correspond( prefix, elements[ 1], name, type)) {
					elements[ 1] = newName;
					result = true;
				}
			}
		}

		if ( !result)
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
		if ( null == elements || 2 != elements.length)
			return false;

		switch ( kind) {
			case 3:
			case 10:
			case 28:
				if ( elements[ 1].equals( ""))
					return false;

				String prefix = CommonRuleManipulator.get_full_prefix( elements[ 0]);
				if ( null == prefix || !CommonRuleManipulator.correspond( prefix, elements[ 1], name, type))
					return false;

				elements[ 1] = newName;
				break;
			case 15:
			case 19:
				if ( elements[ 0].equals( ""))
					return false;

				elements[ 0] = CommonRuleManipulator.update_object_name( elements[ 0], name, newName, type);
				if ( null == elements[ 0])
					return false;

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
		if ( null == elements || 2 != elements.length)
			return false;

		switch ( kind) {
			case 4:
			case 11:
			case 29:
			case 43:
			case 45:
				String sign = "";
				if ( elements[ 1].startsWith( "-")) {
					sign = "-";
					elements[ 1] = elements[ 1].substring( 1);
				}

				if ( elements[ 1].equals( ""))
					return false;

				String prefix = CommonRuleManipulator.get_full_prefix( elements[ 0]);
				if ( null == prefix || !CommonRuleManipulator.correspond( prefix, elements[ 1], name, type))
					return false;

				elements[ 1] = ( sign + newName);
				break;
			case 16:
			case 20:
				if ( elements[ 0].equals( ""))
					return false;

				elements[ 0] = CommonRuleManipulator.update_object_name( elements[ 0], name, newName, type);
				if ( null == elements[ 0])
					return false;

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
		if ( 0 > kind || ( 5 != kind && 12 != kind && 17 != kind && 21 != kind && 30 != kind))
			return false;

		String[] elements = CommonRuleManipulator.get_elements( _value);
		if ( null == elements || 2 != elements.length)
			return false;

		if ( !update_object_name( kind, elements, name, newName, type))
			return false;

		_value = _reservedWords[ kind];
		for ( int i = 0; i < elements.length; ++i)
			_value += ( ( ( 0 == i) ? "" : "=") + elements[ i]);

		return true;
	}

	/**
	 * @param kind
	 * @param elements
	 * @param name
	 * @param newName
	 * @param type
	 * @return
	 */
	private boolean update_object_name(int kind, String[] elements, String name, String newName, String type) {
		// TODO 要動作確認！
		if ( 0 < elements.length && !elements[ 0].equals( "") && ( 17 == kind || 21 == kind)) {
			String element = CommonRuleManipulator.update_object_name( elements[ 0], name, newName, type);
			if ( null == element)
				return false;

			elements[ 0] = element;
			return true;
		}

		if ( 2 == elements.length && !elements[ 1].equals( "") && ( 5 == kind || 12 == kind || 30 == kind)) {
			String prefix = CommonRuleManipulator.get_full_prefix( elements[ 0]);
			if ( null == prefix || !CommonRuleManipulator.correspond( prefix, elements[ 1], name, type))
				return false;

			elements[ 1] = newName;
			return true;
		}

		return false;
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
		if ( 0 != kind && 7 != kind && 23 != kind)
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
		if ( 1 != kind && 8 != kind && 22 != kind)
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
		if ( 2 != kind && 9 != kind && 27 != kind && 14 != kind && 18 != kind)
			return true;

		String[] elements = CommonRuleManipulator.get_elements( _value);
		if ( null == elements || 1 > elements.length)
			return false;

		if ( 2 == kind || 9 == kind || 27 == kind) {
			if ( 2 != elements.length)
				return false;

			if ( elements[ 1].equals( ""))
				return false;

			String prefix = CommonRuleManipulator.get_full_prefix( elements[ 0]);
			if ( null == prefix)
				return false;

			return CommonRuleManipulator.can_paste_object( "probability", prefix + elements[ 1], drawObjects);
		} else	// 14, 18
			return CommonRuleManipulator.can_paste_object( "probability", elements[ 0], drawObjects);
	}

	/**
	 * @param kind
	 * @param drawObjects
	 * @return
	 */
	private boolean can_paste_collection_name(int kind, Layer drawObjects) {
		if ( 26 != kind && 36 != kind && 38 != kind)
			return true;

		String[] elements = CommonRuleManipulator.get_elements( _value);
		if ( null == elements || 2 != elements.length)
			return false;

		if ( elements[ 1].equals( ""))
			return false;

		String prefix = CommonRuleManipulator.get_full_prefix( elements[ 0]);
		if ( null == prefix)
			return false;

		return CommonRuleManipulator.can_paste_object( "collection", prefix + elements[ 1], drawObjects);
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

		if ( ( 14 > kind || 21 < kind)
			&& !CommonRuleManipulator.can_paste_object( "list", elements[ 0], drawObjects))
			return false;

		switch ( kind) {
			case 14:
			case 15:
			case 16:
			case 17:
			case 18:
			case 19:
			case 20:
			case 21:
			case 25:
			case 35:
			case 37:
				if ( 2 != elements.length)
					return false;

				if ( elements[ 1].equals( ""))
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
	 * @param drawObjects
	 * @return
	 */
	private boolean can_paste_keyword_name(int kind, Layer drawObjects) {
		if ( 3 != kind && 10 != kind && 28 != kind && 15 != kind && 19 != kind)
			return true;

		String[] elements = CommonRuleManipulator.get_elements( _value);
		if ( null == elements || 1 > elements.length)
			return false;

		if ( 3 == kind || 10 == kind || 28 == kind) {
			if ( 2 != elements.length)
				return false;

			if ( elements[ 1].equals( ""))
				return false;

			String prefix = CommonRuleManipulator.get_full_prefix( elements[ 0]);
			if ( null == prefix)
				return false;

			return CommonRuleManipulator.can_paste_object( "keyword", prefix + elements[ 1], drawObjects);
		} else	// 15, 19
			return CommonRuleManipulator.can_paste_object( "keyword", elements[ 0], drawObjects);
	}

	/**
	 * @param kind
	 * @param drawObjects
	 * @return
	 */
	private boolean can_paste_number_object_name(int kind, Layer drawObjects) {
		if ( 4 != kind && 11 != kind && 29 != kind && 43 != kind && 45 != kind && 16 != kind && 20 != kind)
			return true;

		String[] elements = CommonRuleManipulator.get_elements( _value);
		if ( null == elements || 1 > elements.length)
			return false;

		if ( 4 == kind || 11 == kind || 29 == kind || 43 == kind || 45 == kind) {
			if ( 2 != elements.length)
				return false;

			if ( elements[ 1].equals( ""))
				return false;

			if ( elements[ 1].startsWith( "-"))
				elements[ 1] = elements[ 1].substring( 1);

			String prefix = CommonRuleManipulator.get_full_prefix( elements[ 0]);
			if ( null == prefix)
				return false;

			return CommonRuleManipulator.can_paste_object( "number object", prefix + elements[ 1], drawObjects);
		} else	// 16, 20
			return CommonRuleManipulator.can_paste_object( "number object", elements[ 0], drawObjects);
	}

	/**
	 * @param kind
	 * @param drawObjects
	 * @return
	 */
	private boolean can_paste_object_name(int kind, Layer drawObjects) {
		// TODO 要動作確認！
		if ( 5 != kind && 12 != kind && 17 != kind && 21 != kind && 30 != kind)
			return true;

		String[] elements = CommonRuleManipulator.get_elements( _value);
		if ( null == elements || 2 != elements.length)
			return false;

		if ( !elements[ 0].equals( "") && ( 17 == kind || 21 == kind)) {
			String[] types = new String[] {
				"collection",
				"list",
				"spot variable",
				"role variable",
				"time variable",
				"map",
				"exchange algebra",
				"file",
				"class variable"};
			for ( String type:types) {
				if ( CommonRuleManipulator.can_paste_object( type, elements[ 0], drawObjects))
					return true;
			}

			String prefixAndObject[] = CommonRuleManipulator.get_prefix_and_object( elements[ 0]);
			if ( ( null != drawObjects.get_agent_has_this_name( prefixAndObject[ 1]))
				|| ( null != drawObjects.get_spot_has_this_name( prefixAndObject[ 1])))
				return true;
		}

		if ( !elements[ 1].equals( "") && ( 5 == kind || 12 == kind || 30 == kind)) {
			String prefix = CommonRuleManipulator.get_full_prefix( elements[ 0]);
			if ( null == prefix)
				return false;

			String[] types = ( 30 == kind)
				? new String[] {
					"spot variable",
					"role variable",
					"time variable",
					"map",
					"exchange algebra",
					"file",
					"class variable"}
				: new String[] {
					"collection",
					"list",
					"spot variable",
					"role variable",
					"time variable",
					"map",
					"exchange algebra",
					"file",
					"class variable"};
			for ( String type:types) {
				if ( CommonRuleManipulator.can_paste_object( type, prefix + elements[ 1], drawObjects))
					return true;
			}
		}

		return false;
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.role.base.rule.base.Rule#get_script(java.lang.String, soars.application.visualshell.object.role.base.Role)
	 */
	@Override
	protected String get_script(String value, Role role) {
		return CollectionCommand.get_script( get_kind( value), value);
	}

	/**
	 * @param kind
	 * @param value
	 * @return
	 */
	public static String get_partial_script(int kind, String value) {
		if ( 14 > kind || kind > 21)
			return "";

		String prefix = CommonRuleManipulator.get_full_prefix( value);
		if ( null == prefix)
			return "";

		String[] elements = CommonRuleManipulator.get_elements( value);
		if ( null == elements || 2 != elements.length)
			return "";

		// TODO 修正済
		if ( elements[ 0].startsWith( "$Role."))
			return "";
		else if ( elements[ 0].startsWith( "$Time."))
			return ( " ; " + prefix + "cloneEquip " + elements[ 0]);
		else if ( ( null != LayerManager.get_instance().get_agent_has_this_name( elements[ 0]))
			|| ( null != LayerManager.get_instance().get_spot_has_this_name( elements[ 0]))
			|| CommonRuleManipulator.is_object( "spot variable", prefix + elements[ 0]))
			return "";
		else if ( CommonRuleManipulator.is_object( "file", prefix + elements[ 0]))
			return ( " ; " + prefix + "printEquip " + elements[ 0]);
		else
			return ( " ; " + prefix
				+ ( ( 15 == kind || 19 == kind) ? "printEquip " : "cloneEquip ")
				+ elements[ 0]);
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.role.base.object.base.Rule#get_cell_text(soars.application.visualshell.object.role.base.Role)
	 */
	@Override
	public String get_cell_text(Role role) {
		return CollectionCommand.get_cell_text( _value);
	}
}
