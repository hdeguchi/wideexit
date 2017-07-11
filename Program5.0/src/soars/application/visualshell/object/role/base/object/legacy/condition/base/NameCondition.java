/**
 * 
 */
package soars.application.visualshell.object.role.base.object.legacy.condition.base;

import java.util.Vector;

import soars.application.visualshell.object.role.base.object.base.Rule;
import soars.application.visualshell.object.role.base.object.common.CommonRuleManipulator;

/**
 * @author kurata
 *
 */
public class NameCondition extends Rule {

	/**
	 * @param kind
	 * @param type
	 * @param value
	 */
	public NameCondition(String kind, String type, String value) {
		super(kind, type, value);
	}

	/**
	 * @return
	 */
	protected String get_name() {
		String name;
		if ( !_value.startsWith( "!"))
			name = _value;
		else {
			if ( _value.equals( "!"))
				name = "";
			else
				name = _value.substring( "!".length());
		}
		return name;
	}

	/**
	 * @param newName
	 * @param originalName
	 * @param headName
	 * @param ranges
	 * @param newHeadName
	 * @param newRanges
	 * @return
	 */
	protected boolean update_name(String newName, String originalName, String headName, Vector<String[]> ranges, String newHeadName, Vector<String[]> newRanges) {
		String value = ( _value.startsWith( "!") ? _value.substring( "!".length()) : _value);
		value = CommonRuleManipulator.get_new_agent_or_spot_name( value, newName, originalName, headName, ranges, newHeadName, newRanges);
		if ( null == value)
			return false;

		_value = ( ( _value.startsWith( "!") ? "!" : "") + value);

		return true;
	}
}
