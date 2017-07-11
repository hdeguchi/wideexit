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
import soars.application.visualshell.main.Constant;
import soars.application.visualshell.main.ResourceManager;
import soars.application.visualshell.object.role.agent.AgentRole;
import soars.application.visualshell.object.role.base.Role;
import soars.application.visualshell.object.role.base.object.common.CommonRuleManipulator;
import soars.application.visualshell.object.role.base.object.legacy.common.time.TimeRule;
import soars.application.visualshell.object.role.spot.SpotRole;

/**
 * @author kurata
 *
 */
public class TimeCommand extends TimeRule {

	/**
	 * @param value
	 * @return
	 */
	public static String[] get_values(String value) {
		return get_values( value, LayerManager.get_instance());
	}

	/**
	 * @param value
	 * @param layerManipulator
	 * @return
	 */
	public static String[] get_values(String value, ILayerManipulator layerManipulator) {
		String[] values = new String[] { null, null, null, null, "", null};

		String[] spots = CommonRuleManipulator.get_spot( value, layerManipulator);
		if ( null == spots)
			return null;

		values[ 0] = spots[ 0];	// null, "", "spot"

		values[ 1] = spots[ 1];	// "", "sv"
		if ( null == values[ 1])
			return null;

		String[] elements = CommonRuleManipulator.get_elements( value);
		if ( null == elements || 2 != elements.length
			|| null == elements[ 0] || elements[ 0].equals( "")
			|| null == elements[ 1] || elements[ 1].equals( ""))
			return null;

		values[ 2] = elements[ 0];	// time variable

		String rightSide = elements[ 1];

		elements = rightSide.split( " [+-] ");
		if ( null == elements || ( 1 != elements.length && 2 != elements.length))
			return null;

		if ( 1 == elements.length) {
			if ( null == elements[ 0] || elements[ 0].equals( ""))
				return null;

			values[ 3] = elements[ 0];	// time, $ variable, Constant._current_time_name or time variable
		} else {
			if ( null == elements[ 0] || elements[ 0].equals( "")
				|| null == elements[ 1] || elements[ 1].equals( ""))
				return null;

			if ( rightSide.equals( elements[ 0] + " + " + elements[ 1]))
				values[ 4] = "+";	// operator
			else if ( rightSide.equals( elements[ 0] + " - " + elements[ 1]))
				values[ 4] = "-";	// operator
			else
				return null;

			values[ 3] = elements[ 0];	// time, $ variable, Constant._current_time_name or time variable
			values[ 5] = elements[ 1];	// time, $ variable, Constant._current_time_name or time variable
		}

		return values;
	}

	/**
	 * @param kind
	 * @param type
	 * @param value
	 */
	public TimeCommand(String kind, String type, String value) {
		super(kind, type, value);
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.role.base.rule.base.Rule#get_used_time_variable_names()
	 */
	@Override
	protected String[] get_used_time_variable_names() {
		String[] values = get_values( _value);
		if ( null == values)
			return null;

		String spot = CommonRuleManipulator.get_full_prefix( values);

		List<String> list = new ArrayList<String>();

		if ( CommonRuleManipulator.is_object( "time variable", spot + values[ 2], LayerManager.get_instance()))
			list.add( spot + values[ 2]);

		if ( CommonRuleManipulator.is_object( "time variable", spot + values[ 3], LayerManager.get_instance()))
			list.add( spot + values[ 3]);

		if ( !values[ 4].equals( "") && null != values[ 5]
		  && CommonRuleManipulator.is_object( "time variable", spot + values[ 5], LayerManager.get_instance()))
			list.add( spot + values[ 5]);

		return ( String[])list.toArray( new String[ 0]);
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.role.base.rule.base.Rule#update_spot_name_and_number(java.lang.String, java.lang.String, java.lang.String, java.util.Vector, java.lang.String, java.util.Vector)
	 */
	@Override
	public boolean update_spot_name_and_number(String newName, String originalName, String headName, Vector<String[]> ranges, String newHeadName, Vector<String[]> newRanges) {
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
	protected boolean update_spot_variable_name(String name, String newName, String type, Role role) {
		String value = CommonRuleManipulator.update_spot_variable_name2( _value, name, newName, type);
		if ( null == value)
			return false;

		_value = value;

		return true;
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.role.base.rule.base.Rule#update_time_variable_name(java.lang.String, java.lang.String, java.lang.String, soars.application.visualshell.object.role.base.Role)
	 */
	@Override
	protected boolean update_time_variable_name(String name, String newName, String type, Role role) {
		String[] values = get_values( _value);
		if ( null == values)
			return false;

		String spot = CommonRuleManipulator.get_full_prefix( values);

		boolean changed = false;

		if ( CommonRuleManipulator.correspond( spot, values[ 2], name, type)) {
			values[ 2] = newName;
			changed = true;
		}

		if ( CommonRuleManipulator.correspond( spot, values[ 3], name, type)) {
			values[ 3] = newName;
			changed = true;
		}

		if ( !values[ 4].equals( "") && null != values[ 5]
			&& CommonRuleManipulator.correspond( spot, values[ 5], name, type)) {
			values[ 5] = newName;
			changed = true;
		}

		if ( !changed)
			return false;

		update( values);

		return true;
	}

	/**
	 * @param values
	 */
	private void update(String[] values) {
		String spot = CommonRuleManipulator.get_full_prefix( values);

		_value = ( spot + "setTime " + values[ 2] + "=" + values[ 3]);
		if ( !values[ 4].equals( "") && null != values[ 5])
			_value += ( " " + values[ 4] + " " + values[ 5]);
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
		String spot = CommonRuleManipulator.get_full_prefix( _value);
		if ( null == spot)
			return false;

		String[] values = get_values( _value, drawObjects);
		if ( null == values)
			return false;

		if ( !CommonRuleManipulator.can_paste_object( "time variable", spot + values[ 2], drawObjects))
			return false;

		if ( !values[ 3].startsWith( "$") && !is_time( values[ 3])
			&& !values[ 3].equals( Constant._currentTimeName)
			&& !CommonRuleManipulator.can_paste_object( "time variable", spot + values[ 3], drawObjects))
			return false;

		if ( !values[ 4].equals( "") && null != values[ 5]
			&& !values[ 5].startsWith( "$") && !is_time( values[ 5])
			&& !values[ 5].equals( Constant._currentTimeName)
			&& !CommonRuleManipulator.can_paste_object( "time variable", spot + values[ 5], drawObjects))
			return false;

		return true;
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.role.base.rule.base.Rule#get_script(java.lang.String, soars.application.visualshell.object.role.base.Role)
	 */
	@Override
	protected String get_script(String value, Role role) {
		String[] values = get_values( value);
		if ( null == values)
			return null;

		String spot = CommonRuleManipulator.get_full_prefix( values);

		if ( values[ 4].equals( ""))
			return ( spot + "setTime " + values[ 2] + "="
				+ ( values[ 3].equals( Constant._currentTimeName) ? "+0/0:0" : values[ 3]));
		else {
			if ( null == values[ 5])
				return "";

			if ( values[ 3].equals( Constant._currentTimeName))
				return ( spot + "setTime " + values[ 2] + "=" + values[ 4] + values[ 5]);
			else
				return ( spot + "setTime " + values[ 2] + "=" + values[ 3]
					+ " ; " + spot + "setTime " + values[ 2] + values[ 4] +  "=" + values[ 5]);
		}
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.role.base.object.base.Rule#get_cell_text(soars.application.visualshell.object.role.base.Role)
	 */
	@Override
	public String get_cell_text(Role role) {
		String result = _value.replace( " + ", "+");
		result = result.replace( " - ", "-");
		result = result.replace( Constant._currentTimeName, "CurrentTime");
		return result;
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.role.base.rule.base.Rule#transform_time_conditions_and_commands(soars.application.visualshell.object.role.base.Role)
	 */
	@Override
	public boolean transform_time_conditions_and_commands(Role role) {
		String[] values = get_values( _value);
		if ( null == values) {
			_type = ResourceManager.get_instance().get( "rule.type.command.others");
			return true;
		}

		if ( role instanceof AgentRole) {
			if ( null == values[ 0] && values[ 1].equals( "")
				&& !CommonRuleManipulator.is_object( "time variable", values[ 2])) {
				_type = ResourceManager.get_instance().get( "rule.type.command.others");
				update( values);
			}
		} else if ( role instanceof SpotRole) {
			if ( null == values[ 0] && values[ 1].equals( "")) {
				values[ 0] = "";
				_type = ResourceManager.get_instance().get( "rule.type.command.others");
				update( values);
			}
		} else
			_type = ResourceManager.get_instance().get( "rule.type.command.others");

		return true;
	}
}
