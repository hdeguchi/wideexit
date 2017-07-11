/**
 * 
 */
package soars.application.visualshell.file.importer.initial.role;

import java.awt.Point;

import soars.application.visualshell.file.importer.initial.base.DataBase;
import soars.application.visualshell.file.importer.initial.stage.StageDataMap;
import soars.application.visualshell.main.ResourceManager;
import soars.application.visualshell.object.role.base.edit.table.data.CommonRuleData;
import soars.application.visualshell.object.role.base.object.RuleManager;
import soars.application.visualshell.object.role.base.object.Rules;
import soars.application.visualshell.object.role.base.object.base.Rule;
import soars.common.soars.warning.WarningManager;

/**
 * The class for the initial data of the agent role or the spot role.
 * @author kurata / SOARS project
 */
public class RoleData extends DataBase {

	/**
	 * Array of the role conditions.
	 */
	public RuleManager _ruleManager = null;
	
	/**
	 * Creates this object with the specified data.
	 * @param type the type of this object
	 * @param name the name of this object
	 */
	public RoleData(String type, String name) {
		super(type, name);
		_ruleManager = new RuleManager();
	}

	/**
	 * Creates this object with the specified data.
	 * @param type the type of this object
	 * @param name the name of this object
	 * @param position the position of this object
	 */
	public RoleData(String type, String name, Point position) {
		super(type, name, position);
		_ruleManager = new RuleManager();
	}

	/**
	 * Returns true for appending the role data to this object successfully.
	 * @param words the array of words extracted from the line
	 * @return true for appending the role data to this object successfully
	 */
	public boolean append(String[] words) {
		Rules rules = new Rules();

		int column = 0;


		if ( !words[ 3].equals( ""))
			rules.add( Rule.create( "condition", column, ResourceManager.get_instance().get( "rule.type.condition.stage"), words[ 3], false));

		++column;


		int index = 4;
		while ( words.length > index) {
			if ( words[ index].equals( "condition")) {
				if ( index + 3 >= words.length)
					return false;

				Rule rule = create_rulle( words[ index], column, words[ index + 1], words[ index + 2], words[ index + 3]);
				if ( null == rule)
					return false;

				rules.add( rule);
				index += 4;
			} else if ( words[ index].equals( "command")) {
				if ( index + 2 >= words.length)
					return false;

				Rule rule = create_rulle( words[ index], column, words[ index + 1], words[ index + 2], "false");
				if ( null == rule)
					return false;

				rules.add( rule);
				index += 3;
			} else
				return false;

			++column;
		}


		_ruleManager._columnCount = Math.max( column, _ruleManager._columnCount);


		_ruleManager.add( rules);


		return true;
	}

	/**
	 * @param kind
	 * @param column
	 * @param name
	 * @param value
	 * @param or 
	 * @return
	 */
	private Rule create_rulle(String kind, int column, String name, String value, String or) {
		String type = CommonRuleData.get_type( kind, name);
		if ( null == type || type.equals( ""))
			return null;

		return Rule.create( kind, column, type, value, or.equals( "true"));
	}

	/**
	 * Returns true if the loaded data are valid.
	 * @param stageDataMaps the stage data hashtables
	 * @return true if the loaded data are valid
	 */
	public boolean verify(StageDataMap[] stageDataMaps) {
		for ( Rules rules:_ruleManager) {
			if ( rules.isEmpty())
				continue;

			Rule rule = rules.get( 0);

			if ( !rule._type.equals( "condition") || !rule._type.equals( ResourceManager.get_instance().get( "rule.type.condition.stage")))
				continue;

			boolean found = false;
			for ( int j = 0; j < stageDataMaps.length; ++j) {
				if ( stageDataMaps[ j].contains( rule._value))
					found = true;
			}

			if ( !found) {
				String[] message = new String[] {
					"Rule",
					rule._value
				};
				WarningManager.get_instance().add( message);
				return false;
			}
		}
		return true;
	}
}
