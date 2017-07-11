/**
 * 
 */
package soars.application.visualshell.object.role.base.object.legacy.command;

import java.util.Vector;

import soars.application.visualshell.layer.ILayerManipulator;
import soars.application.visualshell.layer.Layer;
import soars.application.visualshell.layer.LayerManager;
import soars.application.visualshell.object.role.agent.AgentRole;
import soars.application.visualshell.object.role.base.Role;
import soars.application.visualshell.object.role.base.object.base.Rule;
import soars.application.visualshell.object.role.base.object.common.CommonRuleManipulator;
import soars.application.visualshell.object.role.spot.SpotRole;
import soars.common.utility.tool.common.Tool;

/**
 * @author kurata
 *
 */
public class SpotVariableCommand extends Rule {

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
	private static String[] get_values(String value, ILayerManipulator layerManipulator) {
		String[] elements = Tool.split( value, ' ');
		if ( null == elements || 2 != elements.length)
			return null;

		String[] spots = CommonRuleManipulator.get_spot( "<" + elements[ 1] + ">", layerManipulator);
		if ( null == spots)
			return null;
			
		return new String[] { elements[ 0], spots[ 0], spots[ 1]};
	}

	/**
	 * @param spotVariable
	 * @param spot
	 * @return
	 */
	public static String get_rule_value(String spotVariable, String spot) {
		return ( spotVariable + " " + spot.substring( 1, spot.length() - 1));
	}

	/**
	 * @param kind
	 * @param type
	 * @param value
	 */
	public SpotVariableCommand(String kind, String type, String value) {
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
		String[] values = get_values( _value);
		if ( null == values)
			return null;

		return ( ( ( null == values[ 1]) || ( values[ 1].equals( ""))) ? null : values[ 1]);
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.role.base.rule.base.Rule#get_used_spot_variable_names(soars.application.visualshell.object.role.base.Role)
	 */
	@Override
	protected String[] get_used_spot_variable_names(Role role) {
		String[] values = get_values( _value);
		if ( null == values)
			return null;

		return new String[] {
			( ( role instanceof SpotRole) ? "<>" : "") + values[ 0],
			CommonRuleManipulator.get_spot_variable_name3( new String[] { values[ 1], values[ 2]})
		};
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.role.base.rule.base.Rule#update_spot_name_and_number(java.lang.String, java.lang.String, java.lang.String, java.util.Vector, java.lang.String, java.util.Vector)
	 */
	@Override
	public boolean update_spot_name_and_number(String newName, String originalName, String headName, Vector<String[]> ranges, String newHeadName, Vector<String[]> newRanges) {
		String[] elements = Tool.split( _value, ' ');
		if ( null == elements || 2 != elements.length)
			return false;

		elements[ 1] = CommonRuleManipulator.update_spot_name1( elements[ 1], newName, originalName, headName, ranges, newHeadName, newRanges);
		if ( null == elements[ 1])
			return false;

		_value = ( elements[ 0] + " " + elements[ 1]);

		return true;
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.role.base.rule.base.Rule#update_spot_variable_name(java.lang.String, java.lang.String, java.lang.String, soars.application.visualshell.object.role.base.Role)
	 */
	@Override
	protected boolean update_spot_variable_name(String name, String newName, String type, Role role) {
		String[] elements = Tool.split( _value, ' ');
		if ( null == elements || 2 != elements.length)
			return false;

		boolean result1 = false;
		if ( ( type.equals( "agent") && ( role instanceof AgentRole) && name.equals( elements[ 0]))
			|| ( type.equals( "spot") && ( role instanceof SpotRole) && name.equals( elements[ 0]))) {
			elements[ 0] = newName;
			result1 = true;
		}

		boolean result2 = false;
		String element = CommonRuleManipulator.update_spot_variable_name1( elements[ 1], name, newName, type);
		if ( null != element) {
			elements[ 1] = element;
			result2 = true;
		}

		if ( !result1 && !result2)
			return false;

		_value = ( elements[ 0] + " " + elements[ 1]);

		return true;
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.role.base.rule.base.Rule#can_paste(soars.application.visualshell.object.role.base.Role, soars.application.visualshell.layer.Layer)
	 */
	@Override
	protected boolean can_paste(Role role, Layer drawObjects) {
		String[] elements = Tool.split( _value, ' ');
		if ( null == elements || 2 != elements.length)
			return false;

		String[] spots = CommonRuleManipulator.get_spot( "<" + elements[ 1] + ">", drawObjects);
		if ( null == spots)
			return false;

		if ( role instanceof AgentRole) {
			if ( !drawObjects.is_agent_object_name( "spot variable", elements[ 0]))
				return false;
		} else if ( role instanceof SpotRole) {
			if ( !drawObjects.is_spot_object_name( "spot variable", elements[ 0]))
				return false;
		} else
			return false;

		if ( !CommonRuleManipulator.can_paste_spot_and_spot_variable_name( spots, drawObjects))
			return false;

		return true;
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.role.base.rule.base.Rule#get_script(java.lang.String, soars.application.visualshell.object.role.base.Role)
	 */
	@Override
	protected String get_script(String value, Role role) {
		String[] elements = Tool.split( value, ' ');
		if ( null == elements || 2 != elements.length)
			return "";

		return ( "<" + elements[ 1] + ">setSpot " + elements[ 0]);
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.role.base.object.base.Rule#get_cell_text(soars.application.visualshell.object.role.base.Role)
	 */
	@Override
	public String get_cell_text(Role role) {
		String[] elements = Tool.split( _value, ' ');
		if ( null == elements || 2 != elements.length)
			return _value;

		return ( "<" + elements[ 0] + ">=<" + elements[ 1] + ">");
	}
}
