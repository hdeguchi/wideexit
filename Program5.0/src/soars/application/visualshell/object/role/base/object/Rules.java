/*
 * 2005/05/30
 */
package soars.application.visualshell.object.role.base.object;

import java.nio.IntBuffer;
import java.util.TreeMap;
import java.util.Vector;

import org.xml.sax.SAXException;
import org.xml.sax.helpers.AttributesImpl;

import soars.application.visualshell.layer.Layer;
import soars.application.visualshell.main.Constant;
import soars.application.visualshell.main.ResourceManager;
import soars.application.visualshell.object.experiment.InitialValueMap;
import soars.application.visualshell.object.expression.VisualShellExpressionManager;
import soars.application.visualshell.object.role.agent.AgentRole;
import soars.application.visualshell.object.role.base.Role;
import soars.application.visualshell.object.role.base.edit.table.data.CommonRuleData;
import soars.application.visualshell.object.role.base.object.base.Rule;
import soars.common.utility.tool.expression.Expression;
import soars.common.utility.xml.sax.Writer;

/**
 * @author kurata
 */
public class Rules extends Vector<Rule> {
	/**
	 * 
	 */
	public String _comment = "";

	/**
	 * 
	 */
	public Rules() {
		super();
	}

	/**
	 * @param rules
	 */
	public Rules(Rules rules) {
		super();
		copy( rules);
	}

	/**
	 * @param rules
	 */
	private void copy(Rules rules) {
		clear();
		for ( Rule rule:rules)
			add( Rule.create( rule));
	}

	/**
	 * @return
	 */
	public boolean is_empty() {
		return ( isEmpty() && _comment.equals( ""));
	}

	/**
	 * 
	 */
	public void cleanup() {
		for ( Rule rule:this)
			rule.cleanup();

		clear();

		_comment = "";
	}

	/**
	 * @return
	 */
	public int get_max_column_count() {
		int column = -1;
		for ( Rule rule:this)
			column = Math.max( column, rule._column);

		return column + 1;
	}

	/**
	 * @param role
	 */
	public void on_paste(Role role) {
		for ( Rule rule:this)
			rule.make_unique_agent_name( role);
	}

	/**
	 * @param roleName
	 * @param row
	 * @param headName
	 * @param ranges
	 * @return
	 */
	public boolean can_adjust_agent_name(String roleName, int row, String headName, Vector<String[]> ranges) {
		boolean result = true;
		for ( Rule rule:this) {
			if ( !rule.can_adjust_agent_name( roleName, row, headName, ranges, this))
				result = false;
		}
		return result;
	}

	/**
	 * @param roleName
	 * @param row
	 * @param headName
	 * @param ranges
	 * @param newHeadName
	 * @param newRanges
	 * @return
	 */
	public boolean can_adjust_agent_name(String roleName, int row, String headName, Vector<String[]> ranges, String newHeadName, Vector<String[]> newRanges) {
		boolean result = true;
		for ( Rule rule:this) {
			if ( !rule.can_adjust_agent_name( roleName, row, headName, ranges, newHeadName, newRanges, this))
				result = false;
		}
		return result;
	}

	/**
	 * @param roleName
	 * @param row
	 * @param headName
	 * @param ranges
	 * @return
	 */
	public boolean can_adjust_spot_name(String roleName, int row, String headName, Vector<String[]> ranges) {
		boolean result = true;
		for ( Rule rule:this) {
			if ( !rule.can_adjust_spot_name( roleName, row, headName, ranges, this))
				result = false;
		}
		return result;
	}

	/**
	 * @param roleName
	 * @param row
	 * @param headName
	 * @param ranges
	 * @param newHeadName
	 * @param newRanges
	 * @return
	 */
	public boolean can_adjust_spot_name(String roleName, int row, String headName, Vector<String[]> ranges, String newHeadName, Vector<String[]> newRanges) {
		boolean result = true;
		for ( Rule rule:this) {
			if ( !rule.can_adjust_spot_name( roleName, row, headName, ranges, newHeadName, newRanges, this))
				result = false;
		}
		return result;
	}

	/**
	 * @param row
	 * @param kind
	 * @param name
	 * @param otherSpotsHaveThisObjectName
	 * @param headName
	 * @param ranges
	 * @param role
	 * @return
	 */
	public boolean can_remove(int row, String kind, String name, boolean otherSpotsHaveThisObjectName, String headName, Vector<String[]> ranges, Role role) {
		// TODO 従来のもの
		boolean result = true;
		for ( Rule rule:this) {
			if ( !rule.can_remove( row, kind, name, otherSpotsHaveThisObjectName, headName, ranges, this, role))
				result = false;
		}
		return result;
	}

	/**
	 * @param row
	 * @param entityType
	 * @param kind
	 * @param objectName
	 * @param otherEntitiesHaveThisObjectName
	 * @param headName
	 * @param ranges
	 * @param role
	 * @return
	 */
	public boolean can_remove(int row, String entityType, String kind, String objectName, boolean otherEntitiesHaveThisObjectName, String headName, Vector<String[]> ranges, Role role) {
		// TODO これからはこちらに移行してゆく
		boolean result = true;
		for ( Rule rule:this) {
			if ( !rule.can_remove( row, entityType, kind, objectName, otherEntitiesHaveThisObjectName, headName, ranges, this, role))
				result = false;
		}
		return result;
	}

	/**
	 * @param row
	 * @param entityType
	 * @param numberObjectName
	 * @param newType
	 * @param role
	 * @return
	 */
	public boolean is_number_object_type_correct(int row, String entityType, String numberObjectName, String newType, Role role) {
		boolean result = true;
		for ( Rule rule:this) {
			if ( !rule.is_number_object_type_correct( row, entityType, numberObjectName, newType, this, role))
				result = false;
		}
		return result;
	}

	/**
	 * @param name
	 * @param row
	 * @param roleName
	 * @return
	 */
	public boolean can_remove_role_name(String name, int row, String roleName) {
		boolean result = true;
		for ( Rule rule:this) {
			if ( !rule.can_remove_role( name, row, roleName, this))
				result = false;
		}
		return result;
	}

	/**
	 * @param name
	 * @param row
	 * @param expression
	 * @return
	 */
	public boolean can_remove_expression(String name, int row, Expression expression) {
		boolean result = true;
		for ( Rule rule:this) {
			if ( !rule.can_remove_expression( name, row, expression))
				result = false;
		}
		return result;
	}

	/**
	 * @param name
	 * @param row
	 * @param newName
	 * @param originalName
	 * @return
	 */
	public boolean update_stage_name(String name, int row, String newName, String originalName) {
		boolean result = false;
		for ( Rule rule:this) {
			if ( rule.update_stage_name( name, row, newName, originalName, this))
				result = true;
		}
		return result;
	}

	/**
	 * @param name
	 * @param row
	 * @param stageName
	 * @return
	 */
	public boolean can_remove_stage_name(String name, int row, String stageName) {
		boolean result = true;
		for ( Rule rule:this) {
			if ( !rule.can_remove_stage_name( name, row, stageName, this))
				result = false;
		}
		return result;
	}

	/**
	 * @param name
	 * @param row
	 * @param stageNames
	 * @return
	 */
	public boolean can_adjust_stage_name(String name, int row, Vector<String> stageNames) {
		boolean result = true;
		for ( Rule rule:this) {
			if ( !rule.can_adjust_stage_name( name, row, stageNames, this))
				result = false;
		}
		return result;
	}

	/**
	 * @param row
	 * @param role
	 * @param drawObjects
	 * @return
	 */
	public boolean can_paste(int row, Role role, Layer drawObjects) {
		boolean result = true;
		for ( Rule rule:this) {
			if ( !rule.can_paste( row, this, role, drawObjects))
				result = false;
		}
		return result;
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
	public boolean update_agent_name_and_number(String newName, String originalName, String headName, Vector<String[]> ranges, String newHeadName, Vector<String[]> newRanges) {
		boolean result = false;
		for ( Rule rule:this) {
			if ( rule.update_agent_name_and_number( newName, originalName, headName, ranges, newHeadName, newRanges))
				result = true;
		}
		return result;
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
	public boolean update_spot_name_and_number(String newName, String originalName, String headName, Vector<String[]> ranges, String newHeadName, Vector<String[]> newRanges) {
		boolean result = false;
		for ( Rule rule:this) {
			if ( rule.update_spot_name_and_number( newName, originalName, headName, ranges, newHeadName, newRanges))
				result = true;
		}
		return result;
	}

	/**
	 * @param originalName
	 * @param name
	 * @return
	 */
	public boolean update_role_name(String originalName, String name) {
		boolean result = false;
		for ( Rule rule:this) {
			if ( rule.update_role_name( originalName, name, this))
			result = true;
		}
		return result;
	}

	/**
	 * @param kind
	 * @param originalName
	 * @param newName
	 * @param entityType
	 * @param role
	 * @return
	 */
	public boolean update_object_name(String kind, String originalName, String newName, String entityType, Role role) {
		boolean result = false;
		for ( Rule rule:this) {
			if ( rule.update_object_name( kind, originalName, newName, entityType, this, role))
				result = true;
		}
		return result;
	}

	/**
	 * @param visualShellExpressionManager
	 * @return
	 */
	public boolean update_expression(VisualShellExpressionManager visualShellExpressionManager) {
		boolean result = false;
		for ( Rule rule:this) {
			if ( rule.update_expression( visualShellExpressionManager))
				result = true;
		}
		return result;
	}

	/**
	 * @param newExpression
	 * @param newVariableCount
	 * @param originalExpression
	 * @param row
	 * @param role
	 * @return
	 */
	public boolean update_expression(Expression newExpression, int newVariableCount, Expression originalExpression, int row, Role role) {
		boolean result = false;
		for ( Rule rule:this) {
			if ( rule.update_expression( newExpression, newVariableCount, originalExpression, row, role))
				result = true;
		}
		return result;
	}

	/**
	 * @param headName
	 * @param ranges
	 */
	public void on_remove_agent_name_and_number(String headName, Vector<String[]> ranges) {
	}

	/**
	 * @param headName
	 * @param ranges
	 */
	public void on_remove_spot_name_and_number(String headName, Vector<String[]> ranges) {
//		for ( Rule rule:this) {
//			if ( ( rule._type.equals( ResourceManager.get_instance().get( "rule.type.condition.spot"))
//				|| rule._type.equals( ResourceManager.get_instance().get( "rule.type.command.move")))
//				&& !spot_names.contains( rule._value))
//				rule._value = "";
//		}
	}

	/**
	 * @param roleNames
	 */
	public void on_remove_role_name(Vector<String> roleNames) {
//		for ( Rule rule:this) {
//			if ( rule._type.equals( ResourceManager.get_instance().get( "rule.type.command.role"))
//				&& !roleNames.contains( rule._value))
//				rule._value = "";
//		}
	}

	/**
	 * @param stageNames
	 */
	public void on_remove_stage_name(Vector<String> stageNames) {
//		for ( Rule rule:this) {
//			if ( rule._type.equals( ResourceManager.get_instance().get( "rule.type.condition.stage"))
//				&& !stageNames.contains( rule._value))
//				rule._value = "";
//		}
	}

	/**
	 * @return
	 */
	public boolean update_stage_manager() {
		boolean result = false;
		for ( Rule rule:this) {
			if ( rule.update_stage_manager())
				result = true;
		}
		return result;
	}

	/**
	 * @param expressionMap
	 * @param usedExpressionMap
	 */
	public void get_used_expressions(TreeMap<String, Expression> expressionMap, TreeMap<String, Expression> usedExpressionMap) {
		for ( Rule rule:this)
			rule.get_used_expressions( expressionMap, usedExpressionMap);
	}

	/**
	 * @param originalFunctionName
	 * @param newFunctionName
	 * @return
	 */
	public boolean update_function(String originalFunctionName, String newFunctionName) {
		boolean result = false;
		for ( Rule rule:this) {
			if ( rule.update_function( originalFunctionName, newFunctionName))
				result = true;
		}
		return result;
	}

	/**
	 * @param initialValues
	 * @param suffixes
	 */
	public void get_initial_values(Vector<String> initialValues, String[] suffixes) {
		for ( Rule rule:this)
			rule.get_initial_values( initialValues, suffixes);
	}

	/**
	 * @param role
	 * @return
	 */
	public boolean transform_time_conditions_and_commands(Role role) {
		for ( Rule rule:this) {
			if ( !rule.transform_time_conditions_and_commands( role))
				return false;
		}
		return true;
	}

	/**
	 * @param role
	 * @return
	 */
	public boolean transform_keyword_conditions_and_commands(Role role) {
		for ( Rule rule:this) {
			if ( !rule.transform_keyword_conditions_and_commands( role))
				return false;
		}
		return true;
	}

	/**
	 * @param role
	 * @return
	 */
	public boolean transform_numeric_conditions_and_commands(Role role) {
		for ( int i = 0; i < size(); ++i) {
			Rule rule = get( i).transform_numeric_conditions_and_commands( role);
			if ( null != rule)
				set( i, rule);
		}
		return true;
	}

	/**
	 * @param name
	 * @param number
	 * @return
	 */
	public boolean has_same_agent_name(String name, String number) {
		for ( Rule rule:this) {
			if ( rule.has_same_agent_name( name, number))
				return true;
		}
		return false;
	}

	/**
	 * @param alias
	 * @return
	 */
	public boolean contains_this_alias(String alias) {
		for ( Rule rule:this) {
			if ( rule.contains_this_alias( alias))
				return true;
		}
		return false;
	}

	/**
	 * @param ruleCount
	 */
	public void how_many_rules(IntBuffer ruleCount) {
		if ( isEmpty())
			return;

		for ( int i = size() - 1; i >= 0 ; --i) {
			Rule rule = ( Rule)get( i);
			if ( rule._or)
				continue;

			if ( ruleCount.get( 0) < ( rule._column + 1))
				ruleCount.put( 0, rule._column + 1);

			return;
		}
	}

	/**
	 * @param roleName
	 * @param role
	 * @return
	 */
	public String get_initial_data(String roleName, Role role) {
		if ( isEmpty())
			return "";

		String script = "";
		for ( Rule rule:this) {
			if ( 0 == rule._column) {
				if ( !script.equals( ""))
					return null;

				script = rule._value;
			} else {
				String name = CommonRuleData.get_name( rule._type, rule._kind);
				if ( null == name || name.equals( ""))
					return null;

				script += ( "\t" + rule._kind + "\t" + name + "\t" + rule._value
					+ ( rule._kind.equals( "command") ? "" : ( "\t" + ( rule._or ? "true" : "false"))));
			}
		}

		return ( ( ( role instanceof AgentRole) ? ResourceManager.get_instance().get( "initial.data.agent.role") : ResourceManager.get_instance().get( "initial.data.spot.role"))
			+ "\t" + roleName/*role._name*/ + "\tcondition\t" + script + Constant._lineSeparator);
	}

	/**
	 * @param name
	 * @param row
	 * @param rulesCount
	 * @param initialValueMap
	 * @param demo
	 * @param role
	 * @return
	 */
	public String get_script(String name, int row, int rulesCount, InitialValueMap initialValueMap, boolean demo, Role role) {
		if ( isEmpty())
			return "";

		if ( !get( 0)._kind.equals( "condition") || !get( 0)._type.equals( ResourceManager.get_instance().get( "rule.type.condition.stage")))
			return "";

		int index = 0;
		String script = "", value = "";
		for ( Rule rule:this) {
			if ( rule._or)
				value += ( " || " + rule.get_script( row, initialValueMap, demo, role));
			else {
				if ( !value.equals( "")) {
					script += ( value + "\t");
					value = "";
				}

				if ( rule._kind.equals( "condition"))
					value = rule.get_script( row, initialValueMap, demo, role);
				else {
					script += rule.get_script( row, initialValueMap, demo, role);
					script += " ; TRUE\t";
				}

				for ( int i = index; i < rule._column; ++i)
					script += "\t";

				index = ( rule._column + 1);
			}
		}

		script += ( value.equals( "") ? "" : ( value + "\t"));

		for ( int i = index; i < rulesCount; ++i)
			script += "\t";

		return ( name + "\t" + script + "Line=" + String.valueOf( row + 1) + Constant._lineSeparator);
	}

	/**
	 * @param row
	 * @param writer
	 * @return
	 */
	public boolean write(int row, Writer writer) throws SAXException {
		if ( is_empty())
			return true;

		AttributesImpl attributesImpl = new AttributesImpl();
		attributesImpl.addAttribute( null, null, "row", "", String.valueOf( row));

		writer.startElement( null, null, "rule", attributesImpl);
		for ( Rule rule:this) {
			if ( !rule.write( writer))
				return false;
		}

		if ( !_comment.equals( "")) {
			writer.startElement( null, null, "comment", new AttributesImpl());
			writer.characters( _comment.toCharArray(), 0, _comment.length());
			writer.endElement( null, null, "comment");
		}

		writer.endElement( null, null, "rule");
		return true;
	}
}
