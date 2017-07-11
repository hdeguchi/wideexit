/**
 * 
 */
package soars.application.visualshell.object.role.base.object.legacy.common.time;

import soars.application.visualshell.object.role.base.Role;
import soars.application.visualshell.object.role.base.object.base.Rule;
import soars.application.visualshell.object.role.base.object.common.CommonRuleManipulator;

/**
 * @author kurata
 *
 */
public class TimeRule extends Rule {

	/**
	 * @param value
	 * @return
	 */
	public static boolean is_time(String value) {
		return ( value.matches( "[0-9]:[0-9][0-9]")
			|| value.matches( "[1-2][0-9]:[0-9][0-9]")
			|| value.matches( "0/[0-9]:[0-9][0-9]")
			|| value.matches( "0/[1-2][0-9]:[0-9][0-9]")
			|| value.matches( "[1-9][0-9]*/[0-9]:[0-9][0-9]")
			|| value.matches( "[1-9][0-9]*/[1-2][0-9]:[0-9][0-9]"));
//		return ( value.matches( "[0-9]:[0-9][0-9]")
//			|| value.matches( "[1-2][0-9]:[0-9][0-9]")
//			|| value.matches( "[1-9][0-9]*/[0-9]:[0-9][0-9]")
//			|| value.matches( "[1-9][0-9]*/[1-2][0-9]:[0-9][0-9]"));
	}

	/**
	 * @param value
	 * @return
	 */
	public static String[] get_time_elements(String value) {
		String elements[] = new String[ 3];

		String[] words = value.split( "/");
		switch ( words.length) {
			case 1:
				if ( words[ 0].equals( ""))
					return null;

				elements[ 0] = "";
				words = words[ 0].split( ":");
				break;
			case 2:
				if ( words[ 0].equals( "") || words[ 1].equals( ""))
					return null;

				elements[ 0] = words[ 0];
				words = words[ 1].split( ":");
				break;
			default:
				return null;
		}

		if ( 2 != words.length)
			return null;

		if ( words[ 0].equals( "") || words[ 1].equals( ""))
			return null;

		elements[ 1] = words[ 0];
		elements[ 2] = words[ 1];

		return elements;
	}

	/**
	 * @param day
	 * @return
	 */
	public static String get_day(String day) {
		try {
			int number = Integer.parseInt( day);
//			if ( 0 == number)
//				return "";

			return String.valueOf( number);
		} catch (NumberFormatException e) {
			//e.printStackTrace();
			return "";
		}
	}

	/**
	 * @param kind
	 * @param type
	 * @param value
	 */
	public TimeRule(String kind, String type, String value) {
		super(kind, type, value);
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.role.base.rule.base.Rule#get_used_spot_names()
	 */
	@Override
	protected String[] get_used_spot_names() {
		return new String[] { CommonRuleManipulator.extract_spot_name1( _value)};
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.role.base.rule.base.Rule#get_used_spot_variable_names(soars.application.visualshell.object.role.base.Role)
	 */
	@Override
	protected String[] get_used_spot_variable_names(Role role) {
		return new String[] { CommonRuleManipulator.get_spot_variable_name2( _value)};
	}
}
