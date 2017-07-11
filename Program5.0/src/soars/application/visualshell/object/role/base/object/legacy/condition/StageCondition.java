/**
 * 
 */
package soars.application.visualshell.object.role.base.object.legacy.condition;

import soars.application.visualshell.object.role.base.Role;
import soars.application.visualshell.object.role.base.object.base.Rule;
import soars.application.visualshell.object.stage.Stage;
import soars.application.visualshell.object.stage.StageManager;

/**
 * @author kurata
 *
 */
public class StageCondition extends Rule {

	/**
	 * @param kind
	 * @param type
	 * @param value
	 */
	public StageCondition(String kind, String type, String value) {
		super(kind, type, value);
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.role.base.rule.base.Rule#get_used_stage_names()
	 */
	@Override
	protected String[] get_used_stage_names() {
		return new String[] { _value};
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.role.base.rule.base.Rule#update_stage_name(java.lang.String, java.lang.String)
	 */
	@Override
	public boolean update_stage_name(String newName, String originalName) {
		if ( _value.equals( originalName)) {
			_value = newName;
			return true;
		}

		return false;
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.role.base.rule.base.Rule#update_stage_manager()
	 */
	@Override
	public boolean update_stage_manager() {
		if ( _value.equals( ""))
			return false;

		StageManager.get_instance().append_main_stage( _value);

		return true;
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.role.base.rule.base.Rule#get_script(java.lang.String, soars.application.visualshell.object.role.base.Role)
	 */
	@Override
	protected String get_script(String value, Role role) {
		Stage stage = StageManager.get_instance().get( value);
		if ( null == stage)
			return "";

		return ( stage._name + ( stage._random ? "*" : ""));
	}
}
