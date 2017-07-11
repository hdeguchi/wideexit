/**
 * 
 */
package soars.application.visualshell.object.role.base.object.legacy.command;

import soars.application.visualshell.object.role.base.Role;
import soars.application.visualshell.object.role.base.object.base.Rule;

/**
 * @author kurata
 *
 */
public class TerminateCommand extends Rule {

	/**
	 * @param kind
	 * @param type
	 * @param value
	 */
	public TerminateCommand(String kind, String type, String value) {
		super(kind, type, value);
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.role.base.rule.base.Rule#get_script(java.lang.String, soars.application.visualshell.object.role.base.Role)
	 */
	@Override
	protected String get_script(String value, Role role) {
		return "terminate";
	}
}
