/**
 * 
 */
package soars.application.visualshell.object.role.base.object.legacy.condition;

import java.util.Vector;

import soars.application.visualshell.layer.Layer;
import soars.application.visualshell.layer.LayerManager;
import soars.application.visualshell.main.ResourceManager;
import soars.application.visualshell.object.role.agent.AgentRole;
import soars.application.visualshell.object.role.base.Role;
import soars.application.visualshell.object.role.base.object.base.Rule;
import soars.application.visualshell.object.role.base.object.common.CommonRuleManipulator;
import soars.application.visualshell.object.role.base.object.legacy.common.time.TimeRule;
import soars.application.visualshell.object.role.spot.SpotRole;

/**
 * @author kurata
 *
 */
public class TimeCondition extends TimeRule {

	/**
	 * @param rule
	 * @return
	 */
	public static int get_kind(Rule rule) {
		if ( rule._value.matches( "^!*isTime @.+") || rule._value.matches( "^!*<.*>isTime @.+"))
			return 0;
		else if ( rule._value.matches( ".+ && !.+"))
			return 3;
		else if ( rule._value.matches( "^isTime .+") || rule._value.matches( "^<.*>isTime .+"))
			return 1;
		else if ( rule._value.matches( "^!isTime .+") || rule._value.matches( "^!<.*>isTime .+"))
			return 2;

		return -1;
	}

	/**
	 * @param kind
	 * @param type
	 * @param value
	 */
	public TimeCondition(String kind, String type, String value) {
		super(kind, type, value);
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.role.base.rule.base.Rule#get_used_time_variable_names()
	 */
	@Override
	protected String[] get_used_time_variable_names() {
		int kind = get_kind( this);
		if ( 0 > kind)
			return null;

		String timeVariableName = null;
		switch ( kind) {
			case 0:
				timeVariableName = get_time_variable_name1( _value);
				if ( null == timeVariableName)
					return null;

				return new String[] { timeVariableName};
			case 1:
			case 2:
				timeVariableName = get_time_variable_name2( _value);
				if ( null == timeVariableName)
					return null;

				return new String[] { timeVariableName};
			case 3:
				String[] elements = _value.split( " && ");
				if ( null == elements || 2 != elements.length
					|| null == elements[ 0] || elements[ 0].equals( "")
					|| null == elements[ 1] || elements[ 1].equals( ""))
					return null;

				String time_variableName1 = get_time_variable_name2( elements[ 0]);
				if ( null == time_variableName1)
					return null;

				String time_variableName2 = get_time_variable_name2( elements[ 1]);
				if ( null == time_variableName2)
					return null;

				return new String[] { time_variableName1, time_variableName2};
		}
		return null;
	}

	/**
	 * @param value
	 * @return
	 */
	private String get_time_variable_name1(String value) {
		String prefix = CommonRuleManipulator.get_full_prefix( value);
		if ( null == prefix)
			return null;

		String[] elements = CommonRuleManipulator.get_elements( value);
		if ( null == elements || 1 != elements.length
			|| null == elements[ 0] || 2 > elements[ 0].length())
			return null;

		if ( !CommonRuleManipulator.is_object( "time variable", prefix + elements[ 0].substring( "@".length())))
			return null;

		return ( prefix + elements[ 0].substring( "@".length()));
	}

	/**
	 * @param value
	 * @return
	 */
	private String get_time_variable_name2(String value) {
		String prefix = CommonRuleManipulator.get_full_prefix( value);
		if ( null == prefix)
			return null;

		String[] elements = CommonRuleManipulator.get_elements( value);
		if ( null == elements || 1 != elements.length
			|| null == elements[ 0] || elements[ 0].equals( ""))
			return null;

		if ( !CommonRuleManipulator.is_object( "time variable", prefix + elements[ 0]))
			return null;

		return ( prefix + elements[ 0]);
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.role.base.rule.base.Rule#update_spot_name_and_number(java.lang.String, java.lang.String, java.lang.String, java.util.Vector, java.lang.String, java.util.Vector)
	 */
	@Override
	public boolean update_spot_name_and_number(String newName, String originalName, String headName, Vector<String[]> ranges, String newHeadName, Vector<String[]> newRanges) {
		int kind = get_kind( this);
		if ( 0 > kind)
			return false;

		switch ( kind) {
			case 0:
			case 1:
			case 2:
				String value = CommonRuleManipulator.update_spot_name2( _value, newName, originalName, headName, ranges, newHeadName, newRanges);
				if ( null != value) {
					_value = value;
					return true;
				}
				break;
			case 3:
				String[] elements = _value.split( " && ");
				if ( null == elements || 2 != elements.length
					|| null == elements[ 0] || elements[ 0].equals( "")
					|| null == elements[ 1] || elements[ 1].equals( ""))
					break;

				String value1 = CommonRuleManipulator.update_spot_name2( elements[ 0], newName, originalName, headName, ranges, newHeadName, newRanges);
				String value2 = CommonRuleManipulator.update_spot_name2( elements[ 1], newName, originalName, headName, ranges, newHeadName, newRanges);
				if ( null == value1 && null == value2)
					return false;

				_value = ( ( ( null != value1) ? value1 : elements[ 0]) + " && " + ( ( null != value2) ? value2 : elements[ 1]));
				return true;
		}

		return false;
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.role.base.rule.base.Rule#update_spot_variable_name(java.lang.String, java.lang.String, java.lang.String, soars.application.visualshell.object.role.base.Role)
	 */
	@Override
	protected boolean update_spot_variable_name(String name, String newName, String type, Role role) {
		int kind = get_kind( this);
		if ( 0 > kind)
			return false;

		switch ( kind) {
			case 0:
			case 1:
			case 2:
				String value = CommonRuleManipulator.update_spot_variable_name2( _value, name, newName, type);
				if ( null == value)
					return false;

				_value = value;
				return true;
			case 3:
				String[] elements = _value.split( " && ");
				if ( null == elements || 2 != elements.length
					|| null == elements[ 0] || elements[ 0].equals( "")
					|| null == elements[ 1] || elements[ 1].equals( ""))
					break;

				String value1 = CommonRuleManipulator.update_spot_variable_name2( elements[ 0], name, newName, type);
				String value2 = CommonRuleManipulator.update_spot_variable_name2( elements[ 1], name, newName, type);
				if ( null == value1 && null == value2)
					return false;

				_value = ( ( ( null != value1) ? value1 : elements[ 0]) + " && " + ( ( null != value2) ? value2 : elements[ 1]));
				return true;
		}

		return false;
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.role.base.rule.base.Rule#update_time_variable_name(java.lang.String, java.lang.String, java.lang.String, soars.application.visualshell.object.role.base.Role)
	 */
	@Override
	protected boolean update_time_variable_name(String name, String newName, String type, Role role) {
		int kind = get_kind( this);
		if ( 0 > kind)
			return false;

		String value = null;
		switch ( kind) {
			case 0:
				value = update_time_variable_name1( name, newName, type, _value);
				if ( null != value) {
					_value = value;
					return true;
				}
				break;
			case 1:
			case 2:
				value = update_time_variable_name2( name, newName, type, _value);
				if ( null != value) {
					_value = value;
					return true;
				}
				break;
			case 3:
				String[] elements = _value.split( " && ");
				if ( null == elements || 2 != elements.length
					|| null == elements[ 0] || elements[ 0].equals( "")
					|| null == elements[ 1] || elements[ 1].equals( ""))
					break;

				String value1 = update_time_variable_name2( name, newName, type, elements[ 0]);
				String value2 = update_time_variable_name2( name, newName, type, elements[ 1]);
				if ( null != value1 || null != value2) {
					_value = ( ( ( null != value1) ? value1 : elements[ 0]) + " && " + ( ( null != value2) ? value2 : elements[ 1]));
					return true;
				}
				break;
		}

		return false;
	}

	/**
	 * @param name
	 * @param newName
	 * @param type
	 * @param value
	 * @return
	 */
	private String update_time_variable_name1(String name, String newName, String type, String value) {
		String prefix = CommonRuleManipulator.get_full_prefix( value);
		if ( null == prefix)
			return null;

		String[] elements = CommonRuleManipulator.get_elements( value);
		if ( null == elements || 1 != elements.length
			|| null == elements[ 0] || 2 > elements[ 0].length())
			return null;

		if ( !CommonRuleManipulator.correspond( prefix, elements[ 0].substring( "@".length()), name, type))
			return null;

		return ( ( value.startsWith( "!") ? "!" : "") + prefix + "isTime @" + newName);
	}

	/**
	 * @param name
	 * @param newName
	 * @param type
	 * @param value
	 * @return
	 */
	private String update_time_variable_name2(String name, String newName, String type, String value) {
		String prefix = CommonRuleManipulator.get_full_prefix( value);
		if ( null == prefix)
			return null;

		String[] elements = CommonRuleManipulator.get_elements( value);
		if ( null == elements || 1 != elements.length
			|| null == elements[ 0] || elements[ 0].equals( ""))
			return null;

		if ( !CommonRuleManipulator.correspond( prefix, elements[ 0], name, type))
			return null;

		return ( ( value.startsWith( "!") ? "!" : "") + prefix + "isTime " + newName);
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.role.base.rule.base.Rule#can_paste(soars.application.visualshell.object.role.base.Role, soars.application.visualshell.layer.Layer)
	 */
	@Override
	protected boolean can_paste(Role role, Layer drawObjects) {
		if ( !CommonRuleManipulator.can_paste_spot_and_spot_variable_name1( _value, drawObjects))
			return false;

		if ( !can_paste_time_variable_name( drawObjects))
			return false;

		return true;
	}

	/**
	 * @param drawObjects
	 * @return
	 */
	private boolean can_paste_time_variable_name(Layer drawObjects) {
		int kind = get_kind( this);
		if ( 0 > kind)
			return false;

		switch ( kind) {
			case 0:
				return can_paste_time_variable_name1( _value, drawObjects);
			case 1:
			case 2:
				return can_paste_time_variable_name2( _value, drawObjects);
			case 3:
				String[] elements = _value.split( " && ");
				if ( null == elements || 2 != elements.length
					|| null == elements[ 0] || elements[ 0].equals( "")
					|| null == elements[ 1] || elements[ 1].equals( ""))
					return false;

				return can_paste_time_variable_name2( elements[ 0], drawObjects)
					&& can_paste_time_variable_name2( elements[ 1], drawObjects);
		}

		return false;
	}

	/**
	 * @param value
	 * @param drawObjects
	 * @return
	 */
	private boolean can_paste_time_variable_name1(String value, Layer drawObjects) {
		String prefix = CommonRuleManipulator.get_full_prefix( value);
		if ( null == prefix)
			return false;

		String[] elements = CommonRuleManipulator.get_elements( value);
		if ( null == elements || 1 != elements.length
			|| null == elements[ 0] || 2 > elements[ 0].length())
			return false;

		return ( elements[ 0].substring( "@".length()).startsWith( "$")
			|| is_time( elements[ 0].substring( "@".length()))
			|| CommonRuleManipulator.can_paste_object( "time variable", prefix + elements[ 0].substring( "@".length()), drawObjects));
	}

	/**
	 * @param value
	 * @param drawObjects
	 * @return
	 */
	private boolean can_paste_time_variable_name2(String value, Layer drawObjects) {
		String prefix = CommonRuleManipulator.get_full_prefix( value);
		if ( null == prefix)
			return false;

		String[] elements = CommonRuleManipulator.get_elements( value);
		if ( null == elements || 1 != elements.length
			|| null == elements[ 0] || elements[ 0].equals( ""))
			return false;

		return ( elements[ 0].startsWith( "$") || is_time( elements[ 0])
			|| CommonRuleManipulator.can_paste_object( "time variable", prefix + elements[ 0], drawObjects));
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.role.base.rule.base.Rule#transform_time_conditions_and_commands(soars.application.visualshell.object.role.base.Role)
	 */
	@Override
	public boolean transform_time_conditions_and_commands(Role role) {
		int kind = get_kind( this);
		if ( 0 > kind) {
			_type = ResourceManager.get_instance().get( "rule.type.condition.others");
			return true;
		}

		String[] values = new String[] { _value, null};
		switch ( kind) {
			case 0:
				if ( !update1( values, role))
					_type = ResourceManager.get_instance().get( "rule.type.condition.others");

				if ( null != values[ 1])
					_value = values[ 1];

				break;
			case 1:
			case 2:
				if ( !update2( values, role))
					_type = ResourceManager.get_instance().get( "rule.type.condition.others");

				if ( null != values[ 1])
					_value = values[ 1];

				break;
			case 3:
				update( role);
				break;
		}

		return true;
	}

	/**
	 * @param values 
	 * @param role
	 * @return
	 */
	private boolean update1(String[] values, Role role) {
		String prefix = CommonRuleManipulator.get_full_prefix( values[ 0]);
		if ( null == prefix)
			return false;

		if ( !prefix.equals( ""))
			return true;

		String[] elements = CommonRuleManipulator.get_elements( values[ 0]);
		if ( null == elements || 1 != elements.length
			|| null == elements[ 0] || 2 > elements[ 0].length())
			return false;

		if ( role instanceof AgentRole) {
			if ( elements[ 0].substring( "@".length()).startsWith( "$")
				|| is_time( elements[ 0].substring( "@".length())))
				return true;

			if ( !CommonRuleManipulator.is_object( "time variable", elements[ 0].substring( "@".length())))
				return false;

		} else if ( role instanceof SpotRole) {
			values[ 1] = ( ( values[ 0].startsWith( "!") ? "!" : "") + "<>isTime " + elements[ 0]);

			if ( elements[ 0].substring( "@".length()).startsWith( "$")
				|| is_time( elements[ 0].substring( "@".length())))
				return true;

			if ( !CommonRuleManipulator.is_object( "time variable", "<>" + elements[ 0].substring( "@".length())))
				return false;

		} else
			return false;

		return true;
	}

	/**
	 * @param values 
	 * @param role
	 * @return
	 */
	private boolean update2(String[] values, Role role) {
		String prefix = CommonRuleManipulator.get_full_prefix( values[ 0]);
		if ( null == prefix)
			return false;

		if ( !prefix.equals( ""))
			return true;

		String[] elements = CommonRuleManipulator.get_elements( values[ 0]);
		if ( null == elements || 1 != elements.length
			|| null == elements[ 0] || elements[ 0].equals( ""))
			return false;

		if ( role instanceof AgentRole) {
			if ( elements[ 0].startsWith( "$") || is_time( elements[ 0]))
				return true;

			if ( !CommonRuleManipulator.is_object( "time variable", elements[ 0], LayerManager.get_instance()))
				return false;

		} else if ( role instanceof SpotRole) {
			values[ 1] = ( ( values[ 0].startsWith( "!") ? "!" : "") + "<>isTime " + elements[ 0]);

			if ( elements[ 0].startsWith( "$") || is_time( elements[ 0]))
				return true;

			if ( !CommonRuleManipulator.is_object( "time variable", "<>" + elements[ 0], LayerManager.get_instance()))
				return false;

		} else
			return false;

		return true;
	}

	/**
	 * @param role
	 */
	private void update(Role role) {
		String[] elements = _value.split( " && ");
		if ( null == elements || 2 != elements.length
			|| null == elements[ 0] || elements[ 0].equals( "")
			|| null == elements[ 1] || elements[ 1].equals( "")) {
			_type = ResourceManager.get_instance().get( "rule.type.condition.others");
			return;
		}

		String[][] values = new String[][] {
			{ elements[ 0], null},
			{ elements[ 1], null}
		};

		boolean result1 = update2( values[ 0], role);
		boolean result2 = update2( values[ 1], role);

		if ( !result1 || !result2)
			_type = ResourceManager.get_instance().get( "rule.type.condition.others");

		if ( null != values[ 0][ 1] || null != values[ 1][ 1])
			_value = ( ( ( null == values[ 0][ 1]) ? values[ 0][ 0] : values[ 0][ 1])
				+ " && " + ( ( null == values[ 1][ 1]) ? values[ 1][ 0] : values[ 1][ 1]));
	}
}
