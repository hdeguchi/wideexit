/**
 * 
 */
package soars.application.visualshell.object.role.base.object.legacy.command.base;

import soars.application.visualshell.object.role.base.object.base.Rule;

/**
 * @author kurata
 *
 */
public class EquipCommand extends Rule {

	protected static String[] _kinds = new String[] {
		"probability",
		"collection",
		"list",
		"map",
		"keyword",
		"number object",
		"time variable",
		"spot variable"
	};

	/**
	 * @param kind
	 * @param type
	 * @param value
	 */
	public EquipCommand(String kind, String type, String value) {
		super(kind, type, value);
	}

	/**
	 * @param spot
	 * @param command
	 * @param element1
	 * @param element2
	 * @param prefix
	 * @param keyword
	 * @param timeVariable
	 * @param spotVariable
	 * @return
	 */
	protected static String get_script(String spot, String command, String element1, String element2, String prefix, boolean keyword, boolean timeVariable, boolean spotVariable) {
		String result;

		if ( keyword)
			result = ( spot + command.substring( 0, command.length() - "Equip".length()) + " ");
		else
			result = ( spot + command + " ");

		String time_prefix = ( timeVariable ? "$Time." : "");

		result += ( time_prefix + element1 + "=" + time_prefix + element2);

		if ( keyword || spotVariable)
			return result;

		return ( result + " ; " + prefix + "cloneEquip " + time_prefix + element1);
	}
}
