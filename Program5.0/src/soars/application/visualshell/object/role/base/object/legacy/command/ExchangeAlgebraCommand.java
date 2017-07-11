/**
 * 
 */
package soars.application.visualshell.object.role.base.object.legacy.command;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import org.soars.exalge.util.ExalgeFactory;

import soars.application.visualshell.layer.ILayerManipulator;
import soars.application.visualshell.layer.Layer;
import soars.application.visualshell.layer.LayerManager;
import soars.application.visualshell.main.Constant;
import soars.application.visualshell.main.Environment;
import soars.application.visualshell.object.role.agent.AgentRole;
import soars.application.visualshell.object.role.base.Role;
import soars.application.visualshell.object.role.base.object.base.Rule;
import soars.application.visualshell.object.role.base.object.common.CommonRuleManipulator;
import soars.application.visualshell.object.role.spot.SpotRole;
import exalge2.ExBase;

/**
 * @author kurata
 *
 */
public class ExchangeAlgebraCommand extends Rule {

	/**
	 * 
	 */
	public static final String[] _reservedWords = {
		"projection ",
		"projection ",
		"projection ",

		"plus ",
		"plus ",
		"plus ",
		"plus ",
		"plus ",
		"plus ",
		"plusExalge ",

		"clearValue ",
		"bar ",
		"multiple ",
		"multiple ",
		"hat ",
		"copyExalge ",

		"projectionValue ",
		"projectionValue ",
		"projectionValue ",
		"norm ",

		"getExalge ",
		"putExalge ",

		"createExalge "
	};

	/**
	 * 
	 */
	public static final String _delimiter = "=";

	/**
	 * @param baseOneKeyString
	 * @return
	 */
	public static String toExbaseLiteral(String baseOneKeyString) {
		ExBase exBase = ExalgeFactory.newExBaseByOneKeyString( baseOneKeyString);
		if ( null == exBase)
			return null;

		String exbaseLiteral = exBase.toString();
		if ( null == exbaseLiteral)
			return null;

		return exbaseLiteral;
	}

	/**
	 * @param text
	 * @return
	 */
	public static String toBaseOneKeyString(String text) {
		ExBase exBase = ExalgeFactory.newExBaseByLiteral( text);
		if ( null == exBase) {
			exBase = ExalgeFactory.newExBaseByOneKeyString( text);
			if ( null == exBase)
				return null;
		}

		String baseOneKeyString = exBase.key();
		if ( null == baseOneKeyString)
			return null;

		while ( 0 <= baseOneKeyString.indexOf( "-#-"))
			baseOneKeyString = baseOneKeyString.replace( "-#-", "--");

		return baseOneKeyString;
	}

//	/**
//	 * @param base
//	 * @return
//	 */
//	public static boolean is_base_correct(String base) {
//		if ( null == base
//			|| base.equals( "")
//			|| base.equals( "$")
//			|| 0 < base.indexOf( '$'))
//			return false;
//
//		if ( base.startsWith( "$")
//			&& 0 < base.indexOf( "$", 1))
//			return false;
//
//		if ( base.equals( "$Name")
//			|| base.equals( "$Role")
//			|| base.equals( "$Spot")
//			|| 0 <= base.indexOf( Constant._experiment_name))
//			return false;
//
//		if ( base.startsWith( "$"))
//			return true;
//
//		String[] bases = Tool.split( base, '-');
//		if ( 2 > bases.length || 5 < bases.length)
//			return false;
//
//		if ( null == bases[ 0] || bases[ 0].equals( "") || !is_valid( bases[ 0], Constant._prohibitedCharacters11))
//			return false;
//
//		if ( null == bases[ 1] || ( !bases[ 1].equals( "NO_HAT") && !bases[ 1].equals( "HAT")))
//			return false;
//
//		if ( 3 <= bases.length && ( null == bases[ 2] || !is_valid( bases[ 2], Constant._prohibitedCharacters11)))
//			return false;
//
//		if ( 4 <= bases.length && ( null == bases[ 3] || !is_valid( bases[ 3], Constant._prohibitedCharacters11)))
//			return false;
//
//		if ( 5 <= bases.length && ( null == bases[ 4] || !is_valid( bases[ 4], Constant._prohibitedCharacters11)))
//			return false;
//
//		return true;
//	}
//
//	/**
//	 * @param value
//	 * @param prohibited_characters
//	 * @return
//	 */
//	private static boolean is_valid(String value, String prohibitedCharacters) {
//		for ( int i = 0; i < prohibitedCharacters.length(); ++i) {
//			if ( 0 > value.indexOf( prohibitedCharacters.charAt( i)))
//				continue;
//
//			return false;
//		}
//		return true;
//	}

	/**
	 * @param value
	 * @return
	 */
	public static boolean is_value_correct(String value) {
		if ( value.equals( "$") || 0 < value.indexOf( '$'))
			return false;

		if ( value.startsWith( "$") && 0 < value.indexOf( "$", 1))
			return false;

		if ( value.startsWith( "$") && 0 < value.indexOf( ")", 1))
			return false;

		if ( value.equals( "$Name")
			|| value.equals( "$Role")
			|| value.equals( "$Spot")
			|| 0 <= value.indexOf( Constant._experimentName))
			return false;

		return true;
	}

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
		String reservedWord = CommonRuleManipulator.get_reserved_word( value);
		if ( null == reservedWord)
			return -1;

		if ( reservedWord.equals( _reservedWords[ 9]))
			return 9;
		else if ( reservedWord.equals( _reservedWords[ 10]))
			return 10;
		else if ( reservedWord.equals( _reservedWords[ 11]))
			return 11;
		else if ( reservedWord.equals( _reservedWords[ 14]))
			return 14;
		else if ( reservedWord.equals( _reservedWords[ 15]))
			return 15;
		else if ( reservedWord.equals( _reservedWords[ 19]))
			return 19;
		else if ( reservedWord.equals( _reservedWords[ 20]))
			return 20;
		else if ( reservedWord.equals( _reservedWords[ 21]))
			return 21;
		else if ( reservedWord.equals( _reservedWords[ 22]))
			return 22;
		else if ( reservedWord.equals( _reservedWords[ 0])) {
			String[] elements = CommonRuleManipulator.get_elements( value);
			if ( null == elements || ( 3 != elements.length && 7 != elements.length))
				return -2;

			if ( 3 == elements.length)
				return ( ( CommonRuleManipulator.is_object( "keyword", CommonRuleManipulator.get_full_prefix( value) + elements[ 2], layerManipulator)) ? 1 : 0);
			else
				return 2;
		} else if ( reservedWord.equals( _reservedWords[ 3])) {
			String[] elements = CommonRuleManipulator.get_elements( value);
			if ( null == elements || ( 3 != elements.length && 7 != elements.length))
				return -2;

			int result = ( CommonRuleManipulator.is_object( "number object", CommonRuleManipulator.get_full_prefix( value) + elements[ 1], layerManipulator) ? 6 : 3);
			if ( 3 == elements.length)
				return ( result + ( CommonRuleManipulator.is_object( "keyword", CommonRuleManipulator.get_full_prefix( value) + elements[ 2], layerManipulator) ? 1 : 0));
			else
				return ( result + 2);
		} else if ( reservedWord.equals( _reservedWords[ 12])) {
			String[] elements = CommonRuleManipulator.get_elements( value);
			if ( null == elements || 2 != elements.length)
				return -2;

			return ( ( CommonRuleManipulator.is_object( "number object", CommonRuleManipulator.get_full_prefix( value) + elements[ 1], layerManipulator)) ? 13 : 12);
		} else if ( reservedWord.equals( _reservedWords[ 16])) {
			String[] elements = CommonRuleManipulator.get_elements( value);
			if ( null == elements || ( 3 != elements.length && 7 != elements.length))
				return -2;

			if ( 3 == elements.length)
				return ( ( CommonRuleManipulator.is_object( "keyword", CommonRuleManipulator.get_full_prefix( value) + elements[ 2], layerManipulator)) ? 17 : 16);
			else
				return 18;
		}

		return -1;
	}

//	/**
//	 * @return
//	 */
//	private int get_kind2() {
//		String reservedWord = CommonRuleManipulator.get_reserved_word( _value);
//		if ( null == reservedWord)
//			return -1;
//
//		if ( reservedWord.equals( _reservedWords[ 0]))
//			return 0;
////		if ( reservedWord.equals( _reservedWords[ 1]))
////			return 1;
////		else if ( reservedWord.equals( _reservedWords[ 2]))
////			return 2;
//		else if ( reservedWord.equals( _reservedWords[ 3]))
//			return 3;
////		else if ( reservedWord.equals( _reservedWords[ 4]))
////			return 4;
////		else if ( reservedWord.equals( _reservedWords[ 5]))
////			return 5;
////		else if ( reservedWord.equals( _reservedWords[ 6]))
////			return 6;
////		else if ( reservedWord.equals( _reservedWords[ 7]))
////			return 7;
////		else if ( reservedWord.equals( _reservedWords[ 8]))
////			return 8;
//		else if ( reservedWord.equals( _reservedWords[ 9]))
//			return 9;
//		else if ( reservedWord.equals( _reservedWords[ 10]))
//			return 10;
//		else if ( reservedWord.equals( _reservedWords[ 11]))
//			return 11;
//		else if ( reservedWord.equals( _reservedWords[ 12]))
//			return 12;
////		else if ( reservedWord.equals( _reservedWords[ 13]))
////			return 13;
//		else if ( reservedWord.equals( _reservedWords[ 14]))
//			return 14;
//		else if ( reservedWord.equals( _reservedWords[ 15]))
//			return 15;
//		else if ( reservedWord.equals( _reservedWords[ 16]))
//			return 16;
////		else if ( reservedWord.equals( _reservedWords[ 17]))
////			return 17;
////		else if ( reservedWord.equals( _reservedWords[ 18]))
////			return 18;
//		else if ( reservedWord.equals( _reservedWords[ 19]))
//			return 19;
//		else if ( reservedWord.equals( _reservedWords[ 20]))
//			return 20;
//		else if ( reservedWord.equals( _reservedWords[ 21]))
//			return 21;
//
//		return -1;
//	}

	/**
	 * @param kind
	 * @param type
	 * @param value
	 */
	public ExchangeAlgebraCommand(String kind, String type, String value) {
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
		if ( !Environment.get_instance().is_exchange_algebra_enable())
			return null;

		int kind = get_kind( _value);
		if ( 0 > kind)
			return null;

		return CommonRuleManipulator.extract_spot_name1( _value);
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
		if ( !Environment.get_instance().is_exchange_algebra_enable())
			return null;

		int kind = get_kind( _value);
		if ( 0 > kind)
			return null;

		return CommonRuleManipulator.get_spot_variable_name2( _value);
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.role.base.rule.base.Rule#get_used_keyword_names()
	 */
	@Override
	protected String[] get_used_keyword_names() {
		if ( !Environment.get_instance().is_exchange_algebra_enable())
			return null;

		int kind = get_kind( _value);
		if ( 0 > kind)
			return null;

		String prefix = CommonRuleManipulator.get_full_prefix( _value);
		if ( null == prefix)
			return null;

		String[] elements = CommonRuleManipulator.get_elements( _value);
		if ( null == elements)
			return null;

		switch ( kind) {
			case 1:
			case 4:
			case 7:
			case 17:
				if ( 3 <= elements.length || !elements[ 2].equals( ""))
					return new String[] { prefix + elements[ 2]};

				break;
			case 2:
			case 5:
			case 8:
			case 18:
				if ( 7 <= elements.length)
					return get_used_object_names( "keyword", prefix, elements);

				break;
		}

		return null;
	}

	/**
	 * @param string
	 * @param prefix
	 * @param elements
	 * @return
	 */
	private String[] get_used_object_names(String kind, String prefix, String[] elements) {
		List<String> keywords = new ArrayList<String>();
		for ( int i = 2; i < 7; ++i) {
			if ( 3 == i)
				continue;

			if ( !elements[ i].equals( "") && CommonRuleManipulator.is_object( kind, prefix + elements[ i]))
				keywords.add( prefix + elements[ i]);
		}
		return keywords.toArray( new String[ 0]);
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
		if ( !Environment.get_instance().is_exchange_algebra_enable())
			return null;

		int kind = get_kind( _value);
		if ( 0 > kind)
			return null;

		switch ( kind) {
			case 6:
			case 7:
			case 8:
			case 13:
				String prefix = CommonRuleManipulator.get_full_prefix( _value);
				if ( null == prefix)
					return null;

				String[] elements = CommonRuleManipulator.get_elements( _value, 2);
				if ( null == elements || elements[ 1].equals( ""))
					return null;

				return ( prefix + elements[ 1]);
			case 16:
			case 17:
			case 18:
			case 19:
				prefix = CommonRuleManipulator.get_full_prefix( _value);
				if ( null == prefix)
					return null;

				elements = CommonRuleManipulator.get_elements( _value);
				if ( null == elements || elements[ 0].equals( ""))
					return null;

				return ( prefix + elements[ 0]);
		}

		return null;
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.role.base.object.base.Rule#get_used_time_variable_names()
	 */
	@Override
	protected String[] get_used_time_variable_names() {
		if ( !Environment.get_instance().is_exchange_algebra_enable())
			return null;

		int kind = get_kind( _value);
		if ( 0 > kind)
			return null;

		String prefix = CommonRuleManipulator.get_full_prefix( _value);
		if ( null == prefix)
			return null;

		String[] elements = CommonRuleManipulator.get_elements( _value, 7);
		if ( null == elements)
			return null;

		switch ( kind) {
			case 2:
			case 5:
			case 8:
			case 18:
				return get_used_object_names( "time variable", prefix, elements);
		}

		return null;
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.role.base.object.base.Rule#get_used_class_variable_names(soars.application.visualshell.object.role.base.Role)
	 */
	@Override
	protected String[] get_used_class_variable_names(Role role) {
		return get_used_object_names( "class variable", role);
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.role.base.rule.base.Rule#get_used_exchange_algebra_names(soars.application.visualshell.object.role.base.Role)
	 */
	@Override
	protected String[] get_used_exchange_algebra_names(Role role) {
		return get_used_object_names( "exchange algebra", role);
	}

	/**
	 * @param type
	 * @param role
	 * @return
	 */
	private String[] get_used_object_names(String type, Role role) {
		if ( !Environment.get_instance().is_exchange_algebra_enable())
			return null;

		int kind = get_kind( _value);
		if ( 0 > kind)
			return null;

		List<String> names = new ArrayList<String>();

		switch ( kind) {
			case 0:
			case 1:
			case 2:
			case 9:
			case 15:
				String prefix = CommonRuleManipulator.get_full_prefix( _value);
				if ( null == prefix)
					return null;

				String[] elements = CommonRuleManipulator.get_elements( _value, 2);
				if ( null == elements || elements[ 0].equals( "") || elements[ 1].equals( ""))
					return null;

				for ( String element:elements) {
					if ( CommonRuleManipulator.is_object( type, prefix + element))
						names.add( prefix + element);
				}

				return ( !names.isEmpty() ? names.toArray( new String[ 0]) : null);
			case 3:
			case 4:
			case 5:
			case 6:
			case 7:
			case 8:
			case 10:
			case 11:
			case 12:
			case 13:
			case 14:
			case 22:
				prefix = CommonRuleManipulator.get_full_prefix( _value);
				if ( null == prefix)
					return null;

				elements = CommonRuleManipulator.get_elements( _value);
				if ( null == elements || elements[ 0].equals( ""))
					return null;

				return ( CommonRuleManipulator.is_object( type, prefix + elements[ 0]) ? new String[] { prefix + elements[ 0]} : null);
			case 16:
			case 17:
			case 18:
			case 19:
				prefix = CommonRuleManipulator.get_full_prefix( _value);
				if ( null == prefix)
					return null;

				elements = CommonRuleManipulator.get_elements( _value, 2);
				if ( null == elements || elements[ 1].equals( ""))
					return null;

				return ( CommonRuleManipulator.is_object( type, prefix + elements[ 1]) ? new String[] { prefix + elements[ 1]} : null);
			case 20:
				prefix = CommonRuleManipulator.get_full_prefix( _value);
				if ( null == prefix)
					return null;

				elements = CommonRuleManipulator.get_elements( _value, 2);
				if ( null == elements || elements[ 0].equals( "") || elements[ 1].equals( ""))
					return null;

				if ( role instanceof AgentRole) {
					if ( CommonRuleManipulator.is_object( type, elements[ 0]))
						names.add( elements[ 0]);
				} else if ( role instanceof SpotRole) {
					if ( CommonRuleManipulator.is_object( type, "<>" + elements[ 0]))
						names.add( "<>" + elements[ 0]);
				}

				if ( CommonRuleManipulator.is_object( type, prefix + elements[ 1]))
					names.add( prefix + elements[ 1]);

				return ( !names.isEmpty() ? names.toArray( new String[ 0]) : null);
			case 21:
				prefix = CommonRuleManipulator.get_full_prefix( _value);
				if ( null == prefix)
					return null;

				elements = CommonRuleManipulator.get_elements( _value, 2);
				if ( null == elements || elements[ 0].equals( "") || elements[ 1].equals( ""))
					return null;

				if ( CommonRuleManipulator.is_object( type, prefix + elements[ 0]))
					names.add( prefix + elements[ 0]);

				if ( role instanceof AgentRole) {
					if ( CommonRuleManipulator.is_object( type, elements[ 1]))
						names.add( elements[ 0]);
				} else if ( role instanceof SpotRole) {
					if ( CommonRuleManipulator.is_object( type, "<>" + elements[ 1]))
						names.add( "<>" + elements[ 0]);
				}

				return ( !names.isEmpty() ? names.toArray( new String[ 0]) : null);
		}

		return null;
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.role.base.rule.base.Rule#update_spot_name_and_number(java.lang.String, java.lang.String, java.lang.String, java.util.Vector, java.lang.String, java.util.Vector)
	 */
	@Override
	public boolean update_spot_name_and_number(String newName, String originalName, String headName, Vector<String[]> ranges, String newHeadName, Vector<String[]> newRanges) {
		if ( !Environment.get_instance().is_exchange_algebra_enable())
			return false;

		String value = CommonRuleManipulator.update_spot_name2( _value, newName, originalName, headName, ranges, newHeadName, newRanges);
		if ( null == value)
			return false;

		_value = value;

		return true;
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.role.base.rule.base.Rule#update_spot_variable_name(java.lang.String, java.lang.String, java.lang.String, soars.application.visualshell.object.role.base.Role)
	 */
	@Override
	protected boolean update_spot_variable_name(String originalName, String newName, String entityType, Role role) {
		if ( !Environment.get_instance().is_exchange_algebra_enable())
			return false;

		String value = CommonRuleManipulator.update_spot_variable_name2( _value, originalName, newName, entityType);
		if ( null == value)
			return false;

		_value = value;

		return true;
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.role.base.rule.base.Rule#update_keyword_name(java.lang.String, java.lang.String, java.lang.String, soars.application.visualshell.object.role.base.Role)
	 */
	@Override
	protected boolean update_keyword_name(String originalName, String newName, String entityType, Role role) {
		if ( !Environment.get_instance().is_exchange_algebra_enable())
			return false;

		int kind = get_kind( _value);
		if ( 0 > kind || 20 <= kind)
			return false;

		String prefix = CommonRuleManipulator.get_full_prefix( _value);
		if ( null== prefix)
			return false;

		String[] elements = CommonRuleManipulator.get_elements( _value);
		if ( null == elements)
			return false;

		switch ( kind) {
			case 1:
			case 4:
			case 7:
			case 17:
				if ( 3 <= elements.length
					&& !elements[ 2].equals( "")
					&& CommonRuleManipulator.correspond( prefix, elements[ 2], originalName, entityType)) {

					elements[ 2] = newName;

					_value = ( prefix + _reservedWords[ kind]);
					for ( int i = 0; i < elements.length; ++i)
						_value += ( ( ( 0 == i) ? "" : _delimiter) + elements[ i]);

					return true;
				}
				break;
			case 2:
			case 5:
			case 8:
			case 18:
				return update_object_name( kind, prefix, elements, originalName, newName, entityType);
		}

		return false;
	}

	/**
	 * @param kind
	 * @param prefix
	 * @param elements
	 * @param originalName
	 * @param newName
	 * @param entityType
	 * @return
	 */
	private boolean update_object_name(int kind, String prefix, String[] elements, String originalName, String newName, String entityType) {
		if ( 7 > elements.length)
			return false;

		boolean result = false;
		for ( int i = 2; i < 7; ++i) {
			if ( 3 == i)
				continue;

			if ( !elements[ i].equals( "") && CommonRuleManipulator.correspond( prefix, elements[ i], originalName, entityType)) {
				elements[ i] = newName;
				result = true;
			}
		}

		if ( !result)
			return false;
			
		_value = ( prefix + _reservedWords[ kind]);
		for ( int i = 0; i < elements.length; ++i)
			_value += ( ( ( 0 == i) ? "" : _delimiter) + elements[ i]);

		return true;
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.role.base.rule.base.Rule#update_number_object_name(java.lang.String, java.lang.String, java.lang.String, soars.application.visualshell.object.role.base.Role)
	 */
	@Override
	protected boolean update_number_object_name(String originalName, String newName, String entityType, Role role) {
		if ( !Environment.get_instance().is_exchange_algebra_enable())
			return false;

		int kind = get_kind( _value);
		if ( 0 > kind || 20 <= kind)
			return false;

		String prefix = CommonRuleManipulator.get_full_prefix( _value);
		if ( null== prefix)
			return false;

		String[] elements = CommonRuleManipulator.get_elements( _value);
		if ( null == elements)
			return false;

		switch ( kind) {
			case 6:
			case 7:
			case 8:
			case 13:
				if ( 2 <= elements.length
					&& !elements[ 1].equals( "")
					&& CommonRuleManipulator.correspond( prefix, elements[ 1], originalName, entityType)) {

					elements[ 1] = newName;

					_value = ( prefix + _reservedWords[ kind]);
					for ( int i = 0; i < elements.length; ++i)
						_value += ( ( ( 0 == i) ? "" : _delimiter) + elements[ i]);

					return true;
				}
				break;
			case 16:
			case 17:
			case 18:
			case 19:
				if ( 1 <= elements.length
					&& !elements[ 0].equals( "")
					&& CommonRuleManipulator.correspond( prefix, elements[ 0], originalName, entityType)) {

					elements[ 0] = newName;

					_value = ( prefix + _reservedWords[ kind]);
					for ( int i = 0; i < elements.length; ++i)
						_value += ( ( ( 0 == i) ? "" : _delimiter) + elements[ i]);

					return true;
				}
				break;
		}

		return false;
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.role.base.object.base.Rule#update_time_variable_name(java.lang.String, java.lang.String, java.lang.String, soars.application.visualshell.object.role.base.Role)
	 */
	@Override
	protected boolean update_time_variable_name(String originalName, String newName, String entityType, Role role) {
		if ( !Environment.get_instance().is_exchange_algebra_enable())
			return false;

		int kind = get_kind( _value);
		if ( 0 > kind || 20 <= kind)
			return false;

		String prefix = CommonRuleManipulator.get_full_prefix( _value);
		if ( null== prefix)
			return false;

		String[] elements = CommonRuleManipulator.get_elements( _value);
		if ( null == elements)
			return false;

		switch ( kind) {
			case 2:
			case 5:
			case 8:
			case 18:
				return update_object_name( kind, prefix, elements, originalName, newName, entityType);
		}

		return false;
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.role.base.object.base.Rule#update_class_variable_name(java.lang.String, java.lang.String, java.lang.String, soars.application.visualshell.object.role.base.Role)
	 */
	@Override
	protected boolean update_class_variable_name(String originalName, String newName, String entityType, Role role) {
		return update_object_name( originalName, newName, entityType, role);
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.role.base.rule.base.Rule#update_exchange_algebra_name(java.lang.String, java.lang.String, java.lang.String, soars.application.visualshell.object.role.base.Role)
	 */
	@Override
	protected boolean update_exchange_algebra_name(String originalName, String newName, String entityType, Role role) {
		return update_object_name( originalName, newName, entityType, role);
	}

	/**
	 * @param originalName
	 * @param newName
	 * @param entityType
	 * @param role
	 * @return
	 */
	private boolean update_object_name(String originalName, String newName, String entityType, Role role) {
		if ( !Environment.get_instance().is_exchange_algebra_enable())
			return false;

		int kind = get_kind( _value);
		String prefix = CommonRuleManipulator.get_full_prefix( _value);
		if ( null== prefix)
			return false;

		String[] elements = CommonRuleManipulator.get_elements( _value);
		if ( null == elements)
			return false;

		switch ( kind) {
			case 0:
			case 1:
			case 2:
			case 9:
			case 15:
				if ( 2 <= elements.length && !elements[ 0].equals( "") && !elements[ 1].equals( "")) {

					boolean result1 = CommonRuleManipulator.correspond( prefix, elements[ 0], originalName, entityType);
					boolean result2 = CommonRuleManipulator.correspond( prefix, elements[ 1], originalName, entityType);

					if ( result1)
						elements[ 0] = newName;

					if ( result2)
						elements[ 1] = newName;

					if ( result1 || result2) {
						_value =( prefix + _reservedWords[ kind]);
						for ( int i = 0; i < elements.length; ++i)
							_value += ( ( ( 0 == i) ? "" : _delimiter) + elements[ i]);

						return true;
					}
				}
				break;
			case 3:
			case 4:
			case 5:
			case 6:
			case 7:
			case 8:
			case 10:
			case 11:
			case 12:
			case 13:
			case 14:
			case 22:
				if ( 1 <= elements.length
					&& !elements[ 0].equals( "")
					&& CommonRuleManipulator.correspond( prefix, elements[ 0], originalName, entityType)) {

					elements[ 0] = newName;

					_value =( prefix + _reservedWords[ kind]);
					for ( int i = 0; i < elements.length; ++i)
						_value += ( ( ( 0 == i) ? "" : _delimiter) + elements[ i]);

					return true;
				}
				break;
			case 16:
			case 17:
			case 18:
			case 19:
				if ( 2 <= elements.length
					&& !elements[ 1].equals( "")
					&& CommonRuleManipulator.correspond( prefix, elements[ 1], originalName, entityType)) {

					elements[ 1] = newName;

					_value =( prefix + _reservedWords[ kind]);
					for ( int i = 0; i < elements.length; ++i)
						_value += ( ( ( 0 == i) ? "" : _delimiter) + elements[ i]);

					return true;
				}
				break;
			case 20:
				if ( 2 <= elements.length && !elements[ 0].equals( "") && !elements[ 1].equals( "")) {

					boolean result1 = false;
					boolean result2 = false;

					if ( role instanceof AgentRole) {
						result1 = CommonRuleManipulator.correspond( "", elements[ 0], originalName, entityType);
						result2 = CommonRuleManipulator.correspond( prefix, elements[ 1], originalName, entityType);
					} else if ( role instanceof SpotRole) {
						result1 = CommonRuleManipulator.correspond( "<>", elements[ 0], originalName, entityType);
						result2 = CommonRuleManipulator.correspond( prefix, elements[ 1], originalName, entityType);
					}

					if ( result1)
						elements[ 0] = newName;

					if ( result2)
						elements[ 1] = newName;

					if ( result1 || result2) {
						_value =( prefix + _reservedWords[ kind]);
						for ( int i = 0; i < elements.length; ++i)
							_value += ( ( ( 0 == i) ? "" : _delimiter) + elements[ i]);

						return true;
					}
				}
				break;
			case 21:
				if ( 2 <= elements.length && !elements[ 0].equals( "") && !elements[ 1].equals( "")) {

					boolean result1 = false;
					boolean result2 = false;

					if ( role instanceof AgentRole) {
						result1 = CommonRuleManipulator.correspond( prefix, elements[ 0], originalName, entityType);
						result2 = CommonRuleManipulator.correspond( "", elements[ 1], originalName, entityType);
					} else if ( role instanceof SpotRole) {
						result1 = CommonRuleManipulator.correspond( prefix, elements[ 0], originalName, entityType);
						result2 = CommonRuleManipulator.correspond( "<>", elements[ 1], originalName, entityType);
					}

					if ( result1)
						elements[ 0] = newName;

					if ( result2)
						elements[ 1] = newName;

					if ( result1 || result2) {
						_value =( prefix + _reservedWords[ kind]);
						for ( int i = 0; i < elements.length; ++i)
							_value += ( ( ( 0 == i) ? "" : _delimiter) + elements[ i]);

						return true;
					}
				}
				break;
		}
		return false;
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.role.base.rule.base.Rule#can_paste(soars.application.visualshell.object.role.base.Role, soars.application.visualshell.layer.Layer)
	 */
	@Override
	protected boolean can_paste(Role role, Layer drawObjects) {
		if ( !Environment.get_instance().is_exchange_algebra_enable())
			return false;

		int kind = get_kind( _value, drawObjects);
		if ( 0 > kind)
			return false;
//		switch ( kind) {
//			case -1:
//				return true;
//			case -2:
//				return false;
//		}

		if ( !CommonRuleManipulator.can_paste_spot_and_spot_variable_name1( _value, drawObjects))
			return false;

		if ( !can_paste_keyword_name( kind, drawObjects))
			return false;

		if ( !can_paste_number_object_name( kind, drawObjects))
			return false;

		if ( !can_paste_time_variable_name( kind, drawObjects))
			return false;

		if ( !can_paste_class_variable_name( kind, role, drawObjects))
			return false;

		if ( !can_paste_exchange_algebra_name( kind, role, drawObjects))
			return false;

		return true;
	}

	/**
	 * @param kind
	 * @param drawObjects
	 * @return
	 */
	private boolean can_paste_keyword_name(int kind, Layer drawObjects) {
		if ( 1 != kind && 4 != kind && 7 != kind && 17 != kind && 2 != kind && 5 != kind && 8 != kind && 18 != kind)
			return true;

		String prefix = CommonRuleManipulator.get_full_prefix( _value);
		if ( null == prefix)
			return false;

		if ( 1 == kind || 4 == kind || 7 == kind || 17 == kind) {
			String[] elements = CommonRuleManipulator.get_elements( _value, 3);
			if ( null == elements || elements[ 2].equals( ""))
				return false;

			return CommonRuleManipulator.can_paste_object( "keyword", prefix + elements[ 2], drawObjects);
		} else {
			String[] elements = CommonRuleManipulator.get_elements( _value, 7);
			if ( null == elements)
				return false;

			return can_paste_object_name( "keyword", prefix, elements, drawObjects);
		}
	}

	/**
	 * @param kind
	 * @param prefix
	 * @param elements
	 * @param drawObjects
	 * @return
	 */
	private boolean can_paste_object_name(String kind, String prefix, String[] elements, Layer drawObjects) {
		for ( int i = 2; i < 7; ++i) {
			if ( 3 == i)
				continue;

			if ( CommonRuleManipulator.is_object( kind, prefix + elements[ i], drawObjects)
				&& !CommonRuleManipulator.can_paste_object( kind, prefix + elements[ i], drawObjects))
				return false;
		}

		return true;
	}

	/**
	 * @param kind
	 * @param drawObjects
	 * @return
	 */
	private boolean can_paste_number_object_name(int kind, Layer drawObjects) {
		if ( 6 != kind && 7 != kind && 8 != kind && 13 != kind && 16 != kind && 17 != kind && 18 != kind && 19 != kind)
			return true;

		String prefix = CommonRuleManipulator.get_full_prefix( _value);
		if ( null == prefix)
			return false;

		if ( 6 == kind || 7 == kind || 8 == kind || 13 == kind) {
			String[] elements = CommonRuleManipulator.get_elements( _value, 2);
			if ( null == elements || elements[ 1].equals( ""))
				return false;

			return CommonRuleManipulator.can_paste_object( "number object", prefix + elements[ 1], drawObjects);
		} else {
			String[] elements = CommonRuleManipulator.get_elements( _value);
			if ( null == elements || elements[ 0].equals( ""))
				return false;

			return CommonRuleManipulator.can_paste_object( "number object", prefix + elements[ 0], drawObjects);
		}
	}

	/**
	 * @param kind
	 * @param drawObjects
	 * @return
	 */
	private boolean can_paste_time_variable_name(int kind, Layer drawObjects) {
		if ( 2 != kind && 5 != kind && 8 != kind && 18 != kind)
			return true;

		String prefix = CommonRuleManipulator.get_full_prefix( _value);
		if ( null == prefix)
			return false;

		String[] elements = CommonRuleManipulator.get_elements( _value, 7);
		if ( null == elements)
			return false;

		return can_paste_object_name( "time variable", prefix, elements, drawObjects);
	}

	/**
	 * @param kind
	 * @param role
	 * @param drawObjects
	 * @return
	 */
	private boolean can_paste_class_variable_name(int kind, Role role, Layer drawObjects) {
		return can_paste_object_name( "class variable", kind, role, drawObjects);
	}

	/**
	 * @param kind
	 * @param role
	 * @param drawObjects
	 * @return
	 */
	private boolean can_paste_exchange_algebra_name(int kind, Role role, Layer drawObjects) {
		return can_paste_object_name( "exchange algebra", kind, role, drawObjects);
	}

	/**
	 * @param type
	 * @param kind
	 * @param role
	 * @param drawObjects
	 * @return
	 */
	private boolean can_paste_object_name(String type, int kind, Role role, Layer drawObjects) {
		String prefix = CommonRuleManipulator.get_full_prefix( _value);
		if ( null == prefix)
			return false;

		switch ( kind) {
			case 0:
			case 1:
			case 2:
			case 9:
			case 15:
				String[] elements = CommonRuleManipulator.get_elements( _value, 2);
				if ( null == elements || elements[ 0].equals( "") || elements[ 1].equals( ""))
					return false;

				for ( String element:elements) {
					if ( CommonRuleManipulator.is_object( type, prefix + element, drawObjects)
						&& !CommonRuleManipulator.can_paste_object( type, prefix + element, drawObjects))
						return false;
				}

				return true;
			case 3:
			case 4:
			case 5:
			case 6:
			case 7:
			case 8:
			case 10:
			case 11:
			case 12:
			case 13:
			case 14:
			case 22:
				elements = CommonRuleManipulator.get_elements( _value);
				if ( null == elements || elements[ 0].equals( ""))
					return false;

				if ( CommonRuleManipulator.is_object( type, prefix + elements[ 0], drawObjects)
					&& !CommonRuleManipulator.can_paste_object( type, prefix + elements[ 0], drawObjects))
					return false;

				return true;
			case 16:
			case 17:
			case 18:
			case 19:
				elements = CommonRuleManipulator.get_elements( _value, 2);
				if ( null == elements || elements[ 1].equals( ""))
					return false;

				if ( CommonRuleManipulator.is_object( type, prefix + elements[ 1], drawObjects)
					&& !CommonRuleManipulator.can_paste_object( type, prefix + elements[ 1], drawObjects))
					return false;

				return true;
			case 20:
				elements = CommonRuleManipulator.get_elements( _value, 2);
				if ( null == elements || elements[ 0].equals( "") || elements[ 1].equals( ""))
					return false;

				if ( role instanceof AgentRole) {
					if ( CommonRuleManipulator.is_object( type, elements[ 0], drawObjects)
						&& !CommonRuleManipulator.can_paste_object( type, elements[ 0], drawObjects))
						return false;
				} else if ( role instanceof SpotRole) {
					if ( CommonRuleManipulator.is_object( type, "<>" + elements[ 0], drawObjects)
						&& !CommonRuleManipulator.can_paste_object( type, "<>" + elements[ 0], drawObjects))
						return false;
				} else
					return false;

				if ( CommonRuleManipulator.is_object( type, prefix + elements[ 1], drawObjects)
					&& !CommonRuleManipulator.can_paste_object( type, prefix + elements[ 1], drawObjects))
					return false;

				return true;
			case 21:
				elements = CommonRuleManipulator.get_elements( _value, 2);
				if ( null == elements || elements[ 0].equals( "") || elements[ 1].equals( ""))
					return false;

				if ( CommonRuleManipulator.is_object( type, prefix + elements[ 0], drawObjects)
					&& !CommonRuleManipulator.can_paste_object( type, prefix + elements[ 0], drawObjects))
					return false;

				if ( role instanceof AgentRole) {
					if ( CommonRuleManipulator.is_object( type, elements[ 1], drawObjects)
						&& !CommonRuleManipulator.can_paste_object( type, elements[ 1], drawObjects))
						return false;
				} else if ( role instanceof SpotRole) {
					if ( CommonRuleManipulator.is_object( type, "<>" + elements[ 1], drawObjects)
						&& !CommonRuleManipulator.can_paste_object( type, "<>" + elements[ 1], drawObjects))
						return false;
				} else
					return false;

				return true;
		}

		return false;
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.role.base.rule.base.Rule#get_script(java.lang.String, soars.application.visualshell.object.role.base.Role)
	 */
	@Override
	protected String get_script(String value, Role role) {
		if ( !Environment.get_instance().is_exchange_algebra_enable())
			return "";

		int kind = get_kind( value);
		switch ( kind) {
			case 0:
			case 1:
			case 2:
				return projection( kind, value, role);
			case 3:
			case 4:
			case 5:
			case 6:
			case 7:
			case 8:
				return plus( kind, value, role);
			case 9:
				return plusExalge( value, role);
			case 10:
				return clearValue( value, role);
			case 11:
				return bar( value, role);
			case 12:
			case 13:
				return multiple( kind, value, role);
			case 14:
				return hat( value, role);
			case 15:
				return copyExalge( value, role);
			case 16:
			case 17:
			case 18:
				return projectionValue( kind, value, role);
			case 19:
				return norm( value, role);
			case 20:
				return getExalge( value, role);
			case 21:
				return putExalge( value, role);
			case 22:
				return createExalge( value, role);
		}
		return "";
	}

	/**
	 * @param kind
	 * @param value
	 * @param role
	 * @return
	 */
	private String projection(int kind, String value, Role role) {
		String prefix = CommonRuleManipulator.get_full_prefix( value);

		String[] elements = CommonRuleManipulator.get_elements( value);

		String classVariableName = ( ( 2 == kind) ? Constant._exchangeAlgebraUtilityClassVariableName : Constant._exchangeAlgebraMathClassVariableName);

		String script = ( prefix + "addParam " + classVariableName + "=" + elements[ 1] + "=" + Constant._exchangeAlgebraClassname);

		if ( 2 == kind) {
			for ( int i = 2; i < 7; ++i) {
				if ( 3 == i)
					script += ( " ; " + prefix + "addParamString " + classVariableName + "=" + elements[ 3]);
				else {
					if ( CommonRuleManipulator.is_object( "keyword", prefix + elements[ i]))
						script += ( " ; " + prefix + "equip " + elements[ i] + " ; " + prefix + "addParam " + classVariableName + "=" + elements[ i] + "=" + "java.lang.String");
					else if ( CommonRuleManipulator.is_object( "time variable", prefix + elements[ i]))
						script += ( " ; " + prefix + "addParam " + classVariableName + "=" + "$Time." + elements[ i] + " ; " + prefix + "cloneEquip " + "$Time." + elements[ i]);
					else
						script += ( " ; " + prefix + "addParamString " + classVariableName + "=" + elements[ i].substring( 1, elements[ i].length() - 1));
				}
			}
		} else {
			switch ( kind) {
				case 0:
					script += ( " ; " + prefix + "addParamString " + classVariableName + "=" + elements[ 2]);
					break;
				case 1:
					script += ( " ; " + prefix + "equip " + elements[ 2] + " ; " + prefix + "addParam " + classVariableName + "=" + elements[ 2] + "=" + "java.lang.String");
					break;
				default:
					return "";
			}
		}

		script += ( " ; " + prefix + "invokeClass " + elements[ 0] + "=" + classVariableName + "=" + "projection");

		return script;
	}

	/**
	 * @param kind
	 * @param value
	 * @param role
	 * @return
	 */
	private String plus(int kind, String value, Role role) {
		String prefix = CommonRuleManipulator.get_full_prefix( value);

		String[] elements = CommonRuleManipulator.get_elements( value);

		String classVariableName = ( ( 5 == kind || 8 == kind) ? Constant._exchangeAlgebraUtilityClassVariableName : Constant._exchangeAlgebraMathClassVariableName);

		String script = ( prefix + "addParam " + classVariableName + "=" + elements[ 0] + "=" + Constant._exchangeAlgebraClassname);

		switch ( kind) {
			case 3:
			case 4:
			case 5:
				script += ( " ; " + prefix + "addParamString " + classVariableName + "=" + elements[ 1]);
				break;
			case 6:
			case 7:
			case 8:
				script += ( " ; " + prefix + "addParam " + classVariableName + "=" + elements[ 1] + "=double");
				break;
			default:
				return "";
		}

		if ( 5 == kind || 8 == kind) {
			for ( int i = 2; i < 7; ++i) {
				if ( 3 == i)
					script += ( " ; " + prefix + "addParamString " + classVariableName + "=" + elements[ 3]);
				else {
					if ( CommonRuleManipulator.is_object( "keyword", prefix + elements[ i]))
						script += ( " ; " + prefix + "equip " + elements[ i] + " ; " + prefix + "addParam " + classVariableName + "=" + elements[ i] + "=" + "java.lang.String");
					else if ( CommonRuleManipulator.is_object( "time variable", prefix + elements[ i]))
						script += ( " ; " + prefix + "addParam " + classVariableName + "=" + "$Time." + elements[ i] + " ; " + prefix + "cloneEquip " + "$Time." + elements[ i]);
					else
						script += ( " ; " + prefix + "addParamString " + classVariableName + "=" + elements[ i].substring( 1, elements[ i].length() - 1));
				}
			}
		} else {
			switch ( kind) {
				case 3:
				case 6:
					script += ( " ; " + prefix + "addParamString " + classVariableName + "=" + elements[ 2]);
					break;
				case 4:
				case 7:
					script += ( " ; " + prefix + "equip " + elements[ 2] + " ; " + prefix + "addParam " + classVariableName + "=" + elements[ 2] + "=" + "java.lang.String");
					break;
				default:
					return "";
			}
		}

		script += ( " ; " + prefix + "invokeClass " + elements[ 0] + "=" + classVariableName + "=" + "plus");

		return script;
	}

	/**
	 * @param value
	 * @param role
	 * @return
	 */
	private String plusExalge(String value, Role role) {
		String prefix = CommonRuleManipulator.get_full_prefix( value);

		String[] elements = CommonRuleManipulator.get_elements( value);

		String script = ( prefix + "addParam " + Constant._exchangeAlgebraMathClassVariableName + "=" + elements[ 0] + "=" + Constant._exchangeAlgebraClassname);

		script += ( " ; " + prefix + "addParam " + Constant._exchangeAlgebraMathClassVariableName + "=" + elements[ 1] + "=" + Constant._exchangeAlgebraClassname);

		script += ( " ; " + prefix + "invokeClass " + elements[ 0] + "=" + Constant._exchangeAlgebraMathClassVariableName + "=" + "plus");

		return script;
	}

	/**
	 * @param value
	 * @param role
	 * @return
	 */
	private String clearValue(String value, Role role) {
		String prefix = CommonRuleManipulator.get_full_prefix( value);

		String[] elements = CommonRuleManipulator.get_elements( value);

		String script = ( prefix + "invokeClass " + elements[ 0] + "=" + Constant._exchangeAlgebraFactoryClassVariableName + "=" + "newExalge");

		return script;
	}

	/**
	 * @param value
	 * @param role
	 * @return
	 */
	private String bar(String value, Role role) {
		String prefix = CommonRuleManipulator.get_full_prefix( value);

		String[] elements = CommonRuleManipulator.get_elements( value);

		String script = ( prefix + "addParam " + Constant._exchangeAlgebraMathClassVariableName + "=" + elements[ 0] + "=" + Constant._exchangeAlgebraClassname);

		script += ( " ; " + prefix + "invokeClass " + elements[ 0] + "=" + Constant._exchangeAlgebraMathClassVariableName + "=" + "bar");

		return script;
	}

	/**
	 * @param kind
	 * @param value
	 * @param role
	 * @return
	 */
	private String multiple(int kind, String value, Role role) {
		String prefix = CommonRuleManipulator.get_full_prefix( value);

		String[] elements = CommonRuleManipulator.get_elements( value);

		String script = ( prefix + "addParam " + Constant._exchangeAlgebraMathClassVariableName + "=" + elements[ 0] + "=" + Constant._exchangeAlgebraClassname);

		switch ( kind) {
			case 12:
				script += ( " ; " + prefix + "addParamString " + Constant._exchangeAlgebraMathClassVariableName + "=" + elements[ 1]);
				break;
			case 13:
				script += ( " ; " + prefix + "addParam " + Constant._exchangeAlgebraMathClassVariableName + "=" + elements[ 1] + "=double");
				break;
			default:
				return "";
		}

		script += ( " ; " + prefix + "invokeClass " + elements[ 0] + "=" + Constant._exchangeAlgebraMathClassVariableName + "=" + "multiple");

		return script;
	}

	/**
	 * @param value
	 * @param role
	 * @return
	 */
	private String hat(String value, Role role) {
		String prefix = CommonRuleManipulator.get_full_prefix( value);

		String[] elements = CommonRuleManipulator.get_elements( value);

		String script = ( prefix + "addParam " + Constant._exchangeAlgebraMathClassVariableName + "=" + elements[ 0] + "=" + Constant._exchangeAlgebraClassname);

		script += ( " ; " + prefix + "invokeClass " + elements[ 0] + "=" + Constant._exchangeAlgebraMathClassVariableName + "=" + "hat");

		return script;
	}

	/**
	 * @param value
	 * @param role
	 * @return
	 */
	private String copyExalge(String value, Role role) {
		String prefix = CommonRuleManipulator.get_full_prefix( value);

		String[] elements = CommonRuleManipulator.get_elements( value);

		String script = ( prefix + "addParam " + Constant._exchangeAlgebraMathClassVariableName + "=" + elements[ 1] + "=" + Constant._exchangeAlgebraClassname);

		script += ( " ; " + prefix + "invokeClass " + elements[ 0] + "=" + Constant._exchangeAlgebraMathClassVariableName + "=" + "copyExalge");

		return script;
	}

	/**
	 * @param kind
	 * @param value
	 * @param role
	 * @return
	 */
	private String projectionValue(int kind, String value, Role role) {
		String prefix = CommonRuleManipulator.get_full_prefix( value);

		String[] elements = CommonRuleManipulator.get_elements( value);

		String classVariableName = ( ( 18 == kind) ? Constant._exchangeAlgebraUtilityClassVariableName : Constant._exchangeAlgebraMathClassVariableName);

		String script = ( prefix + "addParam " + classVariableName + "=" + elements[ 1] + "=" + Constant._exchangeAlgebraClassname);

		if ( 18 == kind) {
			for ( int i = 2; i < 7; ++i) {
				if ( 3 == i)
					script += ( " ; " + prefix + "addParamString " + classVariableName + "=" + elements[ 3]);
				else {
					if ( CommonRuleManipulator.is_object( "keyword", prefix + elements[ i]))
						script += ( " ; " + prefix + "equip " + elements[ i] + " ; " + prefix + "addParam " + classVariableName + "=" + elements[ i] + "=" + "java.lang.String");
					else if ( CommonRuleManipulator.is_object( "time variable", prefix + elements[ i]))
						script += ( " ; " + prefix + "addParam " + classVariableName + "=" + "$Time." + elements[ i] + " ; " + prefix + "cloneEquip " + "$Time." + elements[ i]);
					else
						script += ( " ; " + prefix + "addParamString " + classVariableName + "=" + elements[ i].substring( 1, elements[ i].length() - 1));
				}
			}
		} else {
			switch ( kind) {
				case 16:
					script += ( " ; " + prefix + "addParamString " + classVariableName + "=" + elements[ 2]);
					break;
				case 17:
					script += ( " ; " + prefix + "equip " + elements[ 2] + " ; " + prefix + "addParam " + classVariableName + "=" + elements[ 2] + "=" + "java.lang.String");
					break;
				default:
					return "";
			}
		}

		script += ( " ; " + prefix + "invokeClass " + elements[ 0] + "=" + classVariableName + "=" + "getValue");

		return script;
	}

	/**
	 * @param value
	 * @param role
	 * @return
	 */
	private String norm(String value, Role role) {
		String prefix = CommonRuleManipulator.get_full_prefix( value);

		String[] elements = CommonRuleManipulator.get_elements( value);

		String script = ( prefix + "addParam " + Constant._exchangeAlgebraMathClassVariableName + "=" + elements[ 1] + "=" + Constant._exchangeAlgebraClassname);

		script += ( " ; " + prefix + "invokeClass " + elements[ 0] + "=" + Constant._exchangeAlgebraMathClassVariableName + "=" + "norm");

		return script;
	}

	/**
	 * @param value
	 * @param role
	 * @return
	 */
	private String getExalge(String value, Role role) {
		String[] elements = CommonRuleManipulator.get_elements( value, 2);
		if ( null == elements)
			return "";

		String prefix = ( ( role instanceof AgentRole) ? "" : "<>");

		return ( value.replaceFirst( "getExalge ", "getEquip ") + " ; " + prefix + "cloneEquip " + elements[ 0]);
	}

	/**
	 * @param value
	 * @param role
	 * @return
	 */
	private String putExalge(String value, Role role) {
		String[] elements = CommonRuleManipulator.get_elements( value, 2);
		if ( null == elements)
			return "";

		String prefix = CommonRuleManipulator.get_full_prefix( value);

		return ( value.replaceFirst( "putExalge ", "putEquip ") + " ; " + prefix + "cloneEquip " + elements[ 0]);
	}

	/**
	 * @param value
	 * @param role
	 * @return
	 */
	private String createExalge(String value, Role role) {
		// TODO Auto-generated method stub
		String prefix = CommonRuleManipulator.get_full_prefix( value);

		String[] elements = CommonRuleManipulator.get_elements( value);

		return ( prefix + "setClass " + Constant._exchangeAlgebraFactoryClassVariableName + "=" + Constant._exchangeAlgebraFactoryClassname
			+ " ; " + prefix + "setClass " + Constant._exchangeAlgebraMathClassVariableName + "=" + Constant._exchangeAlgebraMathClassname
			+ " ; " + prefix + "setClass " + Constant._exchangeAlgebraUtilityClassVariableName + "=" + Constant._exchangeAlgebraUtilityClassname
			+ " ; " + prefix + "setClass " + elements[ 0] + "=" + Constant._exchangeAlgebraClassname
			+ " ; " + prefix + "invokeClass " + elements[ 0] + "=" + Constant._exchangeAlgebraFactoryClassVariableName+ "=" + "toExalge"
			+ " ; " + prefix + "logEquip " + elements[ 0]);
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.role.base.object.base.Rule#get_cell_text(soars.application.visualshell.object.role.base.Role)
	 */
	@Override
	public String get_cell_text(Role role) {
		int kind = get_kind( _value);
		if ( 0 > kind || 20 <= kind)
			return super.get_cell_text( role);

		String prefix = CommonRuleManipulator.get_full_prefix( _value);
		if ( null== prefix)
			return super.get_cell_text( role);

		String[] elements = CommonRuleManipulator.get_elements( _value);
		if ( null == elements)
			return super.get_cell_text( role);

		switch ( kind) {
			case 0:
			case 3:
			case 6:
			case 16:
				if ( 3 <= elements.length && !elements[ 2].equals( "")) {
					elements[ 2] = ExchangeAlgebraCommand.toExbaseLiteral( elements[ 2]);
					if ( null == elements[ 2])
						return super.get_cell_text( role);

					String value =( prefix + _reservedWords[ kind]);
					for ( int i = 0; i < elements.length; ++i)
						value += ( ( ( 0 == i) ? "" : _delimiter) + elements[ i]);

					return value;
				}
				break;
			case 2:
			case 5:
			case 8:
			case 18:
				if ( 7 <= elements.length) {
					String value =( prefix + _reservedWords[ kind] + elements[ 0] + _delimiter + elements[ 1] + _delimiter);
					value += ( elements[ 3].equals( "HAT") ? "^<" : "<");
					value += ( elements[ 2] + "," + elements[ 4] + "," + elements[ 5] + "," + elements[ 6] + ">");

					return value;
				}
				break;
		}

		return super.get_cell_text( role);
	}
}
