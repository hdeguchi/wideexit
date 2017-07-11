/*
 * 2005/05/30
 */
package soars.application.visualshell.object.role.base.object.base;

import java.util.TreeMap;
import java.util.Vector;

import org.xml.sax.SAXException;
import org.xml.sax.helpers.AttributesImpl;

import soars.application.visualshell.common.tool.CommonTool;
import soars.application.visualshell.layer.Layer;
import soars.application.visualshell.main.ResourceManager;
import soars.application.visualshell.object.experiment.ExperimentManager;
import soars.application.visualshell.object.experiment.InitialValueMap;
import soars.application.visualshell.object.expression.VisualShellExpressionManager;
import soars.application.visualshell.object.role.base.Role;
import soars.application.visualshell.object.role.base.object.Rules;
import soars.application.visualshell.object.role.base.object.common.CommonRuleManipulator;
import soars.application.visualshell.object.role.base.object.generic.GenericRule;
import soars.application.visualshell.object.role.base.object.legacy.command.AttachCommand;
import soars.application.visualshell.object.role.base.object.legacy.command.CollectionCommand;
import soars.application.visualshell.object.role.base.object.legacy.command.CreateAgentCommand;
import soars.application.visualshell.object.role.base.object.legacy.command.CreateObjectCommand;
import soars.application.visualshell.object.role.base.object.legacy.command.DetachCommand;
import soars.application.visualshell.object.role.base.object.legacy.command.ExTransferCommand;
import soars.application.visualshell.object.role.base.object.legacy.command.ExchangeAlgebraCommand;
import soars.application.visualshell.object.role.base.object.legacy.command.FunctionalObjectCommand;
import soars.application.visualshell.object.role.base.object.legacy.command.GetEquipCommand;
import soars.application.visualshell.object.role.base.object.legacy.command.KeywordCommand;
import soars.application.visualshell.object.role.base.object.legacy.command.ListCommand;
import soars.application.visualshell.object.role.base.object.legacy.command.MapCommand;
import soars.application.visualshell.object.role.base.object.legacy.command.MoveCommand;
import soars.application.visualshell.object.role.base.object.legacy.command.NextStageCommand;
import soars.application.visualshell.object.role.base.object.legacy.command.OthersCommand;
import soars.application.visualshell.object.role.base.object.legacy.command.PutEquipCommand;
import soars.application.visualshell.object.role.base.object.legacy.command.RoleCommand;
import soars.application.visualshell.object.role.base.object.legacy.command.SetRoleCommand;
import soars.application.visualshell.object.role.base.object.legacy.command.SpotVariableCommand;
import soars.application.visualshell.object.role.base.object.legacy.command.SubstitutionCommand;
import soars.application.visualshell.object.role.base.object.legacy.command.TerminateCommand;
import soars.application.visualshell.object.role.base.object.legacy.command.TimeCommand;
import soars.application.visualshell.object.role.base.object.legacy.command.TraceCommand;
import soars.application.visualshell.object.role.base.object.legacy.common.expression.ExpressionElements;
import soars.application.visualshell.object.role.base.object.legacy.condition.AgentNameCondition;
import soars.application.visualshell.object.role.base.object.legacy.condition.CollectionCondition;
import soars.application.visualshell.object.role.base.object.legacy.condition.FunctionalObjectCondition;
import soars.application.visualshell.object.role.base.object.legacy.condition.KeywordCondition;
import soars.application.visualshell.object.role.base.object.legacy.condition.ListCondition;
import soars.application.visualshell.object.role.base.object.legacy.condition.NumberObjectCondition;
import soars.application.visualshell.object.role.base.object.legacy.condition.OthersCondition;
import soars.application.visualshell.object.role.base.object.legacy.condition.ProbabilityCondition;
import soars.application.visualshell.object.role.base.object.legacy.condition.RoleCondition;
import soars.application.visualshell.object.role.base.object.legacy.condition.SpotCondition;
import soars.application.visualshell.object.role.base.object.legacy.condition.SpotNameCondition;
import soars.application.visualshell.object.role.base.object.legacy.condition.StageCondition;
import soars.application.visualshell.object.role.base.object.legacy.condition.TimeCondition;
import soars.application.visualshell.object.role.spot.SpotRole;
import soars.common.soars.tool.SoarsCommonTool;
import soars.common.soars.warning.WarningManager;
import soars.common.utility.tool.expression.Expression;
import soars.common.utility.xml.sax.Writer;

/**
 * @author kurata
 */
public class Rule {

	/**
	 * Column number.
	 */
	public int _column = 0;

	/**
	 * Rule kind.
	 */
	public String _kind = "";

	/**
	 * Rule type.
	 */
	public String _type = "";

	/**
	 * Rule value.
	 */
	public String _value = "";

	/**
	 * Logical add.
	 */
	public boolean _or = false;

	/**
	 * Creates a new Rule with the specified parameters.
	 * @param kind
	 * @param type
	 * @param value
	 * @return a new Rule
	 */
	public static Rule create(String kind, String type, String value) {
		if ( kind.equals( "condition")) {
			if ( type.equals( ResourceManager.get_instance().get( "rule.type.condition.stage")))
				return new StageCondition( kind, type, value);
			else if ( type.equals( ResourceManager.get_instance().get( "rule.type.condition.spot")))
				return new SpotCondition( kind, type, value);
			else if ( type.equals( ResourceManager.get_instance().get( "rule.type.condition.keyword")))
				return new KeywordCondition( kind, type, value);
			else if ( type.equals( ResourceManager.get_instance().get( "rule.type.condition.time")))
				return new TimeCondition( kind, type, value);
			else if ( type.equals( ResourceManager.get_instance().get( "rule.type.condition.role")))
				return new RoleCondition( kind, type, value);
			else if ( type.equals( ResourceManager.get_instance().get( "rule.type.condition.probability")))
				return new ProbabilityCondition( kind, type, value);
			else if ( type.equals( ResourceManager.get_instance().get( "rule.type.condition.agent.name")))
				return new AgentNameCondition( kind, type, value);
			else if ( type.equals( ResourceManager.get_instance().get( "rule.type.condition.spot.name")))
				return new SpotNameCondition( kind, type, value);
			else if ( type.equals( ResourceManager.get_instance().get( "rule.type.condition.number.object")))
				return new NumberObjectCondition( kind, type, value);
			else if ( type.equals( ResourceManager.get_instance().get( "rule.type.condition.collection")))
				return new CollectionCondition( kind, type, value);
			else if ( type.equals( ResourceManager.get_instance().get( "rule.type.condition.list")))
				return new ListCondition( kind, type, value);
			else if ( type.equals( ResourceManager.get_instance().get( "rule.type.condition.functional.object")))
				return new FunctionalObjectCondition( kind, type, value);
			else if ( type.equals( ResourceManager.get_instance().get( "rule.type.condition.others")))
				return new OthersCondition( kind, type, value);
			else
				return new OthersCondition( kind, ResourceManager.get_instance().get( "rule.type.condition.others"), value);
		} else if ( kind.equals( "command")) {
			if ( type.equals( ResourceManager.get_instance().get( "rule.type.command.keyword")))
				return new KeywordCommand( kind, type, value);
			else if ( type.equals( ResourceManager.get_instance().get( "rule.type.command.move")))
				return new MoveCommand( kind, type, value);
			else if ( type.equals( ResourceManager.get_instance().get( "rule.type.command.spot.variable")))
				return new SpotVariableCommand( kind, type, value);
			else if ( type.equals( ResourceManager.get_instance().get( "rule.type.command.time")))
				return new TimeCommand( kind, type, value);
			else if ( type.equals( ResourceManager.get_instance().get( "rule.type.command.role")))
				return new RoleCommand( kind, type, value);
			else if ( type.equals( ResourceManager.get_instance().get( "rule.type.command.set.role")))
				return new SetRoleCommand( kind, type, value);
			else if ( type.equals( ResourceManager.get_instance().get( "rule.type.command.substitution")))
				return new SubstitutionCommand( kind, type, value);
			else if ( type.equals( ResourceManager.get_instance().get( "rule.type.command.collection")))
				return new CollectionCommand( kind, type, value);
			else if ( type.equals( ResourceManager.get_instance().get( "rule.type.command.list")))
				return new ListCommand( kind, type, value);
			else if ( type.equals( ResourceManager.get_instance().get( "rule.type.command.get.equip")))
				return new GetEquipCommand( kind, type, value);
			else if ( type.equals( ResourceManager.get_instance().get( "rule.type.command.put.equip")))
				return new PutEquipCommand( kind, type, value);
			else if ( type.equals( ResourceManager.get_instance().get( "rule.type.command.create.agent")))
				return new CreateAgentCommand( kind, type, value);
			else if ( type.equals( ResourceManager.get_instance().get( "rule.type.command.create.object")))
				return new CreateObjectCommand( kind, type, value);
			else if ( type.equals( ResourceManager.get_instance().get( "rule.type.command.attach")))
				return new AttachCommand( kind, type, value);
			else if ( type.equals( ResourceManager.get_instance().get( "rule.type.command.detach")))
				return new DetachCommand( kind, type, value);
			else if ( type.equals( ResourceManager.get_instance().get( "rule.type.command.functional.object")))
				return new FunctionalObjectCommand( kind, type, value);
			else if ( type.equals( ResourceManager.get_instance().get( "rule.type.command.map")))
				return new MapCommand( kind, type, value);
			else if ( type.equals( ResourceManager.get_instance().get( "rule.type.command.exchange.algebra")))
				return new ExchangeAlgebraCommand( kind, type, value);
			else if ( type.equals( ResourceManager.get_instance().get( "rule.type.command.extransfer")))
				return new ExTransferCommand( kind, type, value);
			else if ( type.equals( ResourceManager.get_instance().get( "rule.type.command.next.stage")))
				return new NextStageCommand( kind, type, value);
			else if ( type.equals( ResourceManager.get_instance().get( "rule.type.command.terminate")))
				return new TerminateCommand( kind, type, value);
			else if ( type.equals( ResourceManager.get_instance().get( "rule.type.command.trace")))
				return new TraceCommand( kind, type, value);
			else if ( type.equals( ResourceManager.get_instance().get( "rule.type.command.others")))
				return new OthersCommand( kind, type, value);
			else
				return new OthersCommand( kind, ResourceManager.get_instance().get( "rule.type.command.others"), value);
		} else {
			return null;
		}
	}

	/**
	 * Creates a new Rule with the specified Rule.
	 * @param rule
	 * @return a new Rule
	 */
	public static Rule create(Rule rule) {
		// TODO 2012.9.20
		if ( rule instanceof GenericRule)
			return new GenericRule( ( GenericRule)rule);
		else {
			Rule r = create( rule._kind, rule._type, rule._value);
			r._column = rule._column;
			r._or = rule._or;
			return r;
		}
	}

	/**
	 * Creates a new Rule with the specified parameters.
	 * @param column Column number
	 * @param type Rule type
	 * @param value Rule value
	 * @param or Logical add
	 * @return a new Rule
	 */
	public static Rule create(String kind, int column, String type, String value, boolean or) {
		Rule rule = create( kind, type, value);
		if ( null == rule)
			return null;

		rule._column = column;
		rule._or = or;
		return rule;
	}

	/**
	 * @param rule
	 */
	public Rule(Rule rule) {
		// TODO 2012.9.20
		super();
		_column = rule._column;
		_kind = rule._kind;
		_type = rule._type;
		_value = rule._value;
		_or = rule._or;
	}

	/**
	 * Creates a new Rule with the specified parameters.
	 * @param kind Rule kind
	 * @param type Rule type
	 * @param value Rule value
	 */
	public Rule(String kind, String type, String value) {
		super();
		_kind = kind;
		_type = type;
		_value = value;
	}

	/**
	 * Creates a new Rule with the specified parameters.
	 * @param kind Rule kind
	 * @param column Column number
	 * @param type Rule type
	 * @param or Logical add
	 */
	public Rule(String kind, int column, String type, boolean or) {
		// TODO 2012.9.20
		super();
		_kind = kind;
		_column = column;
		_type = type;
		_or = or;
	}

	/**
	 * Clears the all parameters.
	 */
	public void cleanup() {
		_column = 0;
		_kind = "";
		_type = "";
		_value = "";
		_or = false;
	}

	/**
	 * Adds this Rule's initial value to the specified array.
	 * @param initialValues Array of initial values
	 * @param suffixes Suffix characters
	 */
	public void get_initial_values(Vector<String> initialValues, String[] suffixes) {
		CommonTool.get_aliases( _value, initialValues, suffixes);
	}

	/**
	 * @param role
	 * @return
	 */
	public boolean transform_time_conditions_and_commands(Role role) {
		return true;
	}

	/**
	 * @param role
	 * @return
	 */
	public boolean transform_keyword_conditions_and_commands(Role role) {
		return true;
	}

	/**
	 * @param role
	 * @return
	 */
	public Rule transform_numeric_conditions_and_commands(Role role) {
		return null;
	}

	/**
	 * @param name
	 * @param number
	 * @return
	 */
	public boolean has_same_agent_name(String name, String number) {
		return false;
	}

	/**
	 * @param alias
	 * @return
	 */
	public boolean contains_this_alias(String alias) {
		if ( _value.equals( alias))
			return true;

		if ( _value.endsWith( alias))
			return true;

		for ( int i = 0; i < ExperimentManager._suffixes.length; ++i) {
			if ( 0 <= _value.indexOf( alias + ExperimentManager._suffixes[ i]))
				return true;
		}

		return false;
	}

	/**
	 * @param roleName
	 * @param row
	 * @param headName
	 * @param ranges
	 * @param rules
	 * @return
	 */
	public boolean can_adjust_agent_name(String roleName, int row, String headName, Vector<String[]> ranges, Rules rules) {
		String used_agent_names[] = get_used_agent_names();
		if ( null == used_agent_names)
			return true;

		boolean result = true;

		for ( int i = 0; i < used_agent_names.length; ++i) {
			if ( null == used_agent_names[ i])
				continue;

			if ( !SoarsCommonTool.has_same_name( headName, ranges, used_agent_names[ i]))
				continue;

			String[] message = new String[] {
				"Role",
				"name = " + roleName,
				"type = " + _kind + "::" + _type,
				"agent = " + used_agent_names[ i],
				"row = " + ( row + 1),
				"column = " + ( _column + 1)
			};
			WarningManager.get_instance().add( message);
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
	 * @param rules
	 * @return
	 */
	public boolean can_adjust_agent_name(String roleName, int row, String headName, Vector<String[]> ranges, String newHeadName, Vector<String[]> newRanges, Rules rules) {
		String used_agent_names[] = get_used_agent_names();
		if ( null == used_agent_names)
			return true;

		boolean result = true;

		for ( int i = 0; i < used_agent_names.length; ++i) {
			if ( null == used_agent_names[ i])
				continue;

			if ( !SoarsCommonTool.has_same_name( headName, ranges, used_agent_names[ i]))
				continue;

			if ( SoarsCommonTool.has_same_name( newHeadName, newRanges, used_agent_names[ i]))
				continue;

			String[] message = new String[] {
				"Role",
				"name = " + roleName,
				"type = " + _kind + "::" + _type,
				"agent = " + used_agent_names[ i],
				"row = " + ( row + 1),
				"column = " + ( _column + 1)
			};
			WarningManager.get_instance().add( message);
			result = false;
		}

		return result;
	}

	/**
	 * @return
	 */
	protected String[] get_used_agent_names() {
		return null;
	}

	/**
	 * @param roleName
	 * @param row
	 * @param headName
	 * @param ranges
	 * @param rules
	 * @return
	 */
	public boolean can_adjust_spot_name(String roleName, int row, String headName, Vector<String[]> ranges, Rules rules) {
		String used_spot_names[] = get_used_spot_names();
		if ( null == used_spot_names)
			return true;

		boolean result = true;

		for ( int i = 0; i < used_spot_names.length; ++i) {
			if ( null == used_spot_names[ i])
				continue;

			if ( !SoarsCommonTool.has_same_name( headName, ranges, used_spot_names[ i]))
				continue;

			String[] message = new String[] {
				"Role",
				"name = " + roleName,
				"type = " + _kind + "::" + _type,
				"spot = " + used_spot_names[ i],
				"row = " + ( row + 1),
				"column = " + ( _column + 1)
			};
			WarningManager.get_instance().add( message);
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
	 * @param rules
	 * @return
	 */
	public boolean can_adjust_spot_name(String roleName, int row, String headName, Vector<String[]> ranges, String newHeadName, Vector<String[]> newRanges, Rules rules) {
		String used_spot_names[] = get_used_spot_names();
		if ( null == used_spot_names)
			return true;

		boolean result = true;

		for ( int i = 0; i < used_spot_names.length; ++i) {
			if ( null == used_spot_names[ i])
				continue;

			if ( !SoarsCommonTool.has_same_name( headName, ranges, used_spot_names[ i]))
				continue;

			if ( SoarsCommonTool.has_same_name( newHeadName, newRanges, used_spot_names[ i]))
				continue;

			String[] message = new String[] {
				"Role",
				"name = " + roleName,
				"type = " + _kind + "::" + _type,
				"spot = " + used_spot_names[ i],
				"row = " + ( row + 1),
				"column = " + ( _column + 1)
			};
			WarningManager.get_instance().add( message);
			result = false;
		}

		return result;
	}

	/**
	 * @return
	 */
	protected String[] get_used_spot_names() {
		return null;
	}

	/**
	 * @param row
	 * @param kind
	 * @param name
	 * @param otherSpotsHaveThisObjectName
	 * @param headName
	 * @param ranges
	 * @param rules
	 * @param role
	 * @return
	 */
	public boolean can_remove(int row, String kind, String name, boolean otherSpotsHaveThisObjectName, String headName, Vector<String[]> ranges, Rules rules, Role role) {
		// TODO 従来のもの
		if ( kind.equals( "spot variable"))
			return can_remove_spot_variable_name( row, name, otherSpotsHaveThisObjectName, headName, ranges, rules, role);
		else if ( kind.equals( "probability"))
			return can_remove_probability_name( row, name, otherSpotsHaveThisObjectName, headName, ranges, rules, role);
		else if ( kind.equals( "collection"))
			return can_remove_collection_name( row, name, otherSpotsHaveThisObjectName, headName, ranges, rules, role);
		else if ( kind.equals( "list"))
			return can_remove_list_name( row, name, otherSpotsHaveThisObjectName, headName, ranges, rules, role);
		else if ( kind.equals( "map"))
			return can_remove_map_name( row, name, otherSpotsHaveThisObjectName, headName, ranges, rules, role);
		else if ( kind.equals( "keyword"))
			return can_remove_keyword_name( row, name, otherSpotsHaveThisObjectName, headName, ranges, rules, role);
		else if ( kind.equals( "number object"))
			return can_remove_number_object_name( row, name, otherSpotsHaveThisObjectName, headName, ranges, rules, role);
		else if ( kind.equals( "role variable"))
			return can_remove_role_variable_name( row, name, otherSpotsHaveThisObjectName, headName, ranges, rules, role);
		else if ( kind.equals( "time variable"))
			return can_remove_time_variable_name( row, name, otherSpotsHaveThisObjectName, headName, ranges, rules, role);
		else if ( kind.equals( "class variable"))
			return can_remove_class_variable_name( row, name, otherSpotsHaveThisObjectName, headName, ranges, rules, role);
		else if ( kind.equals( "file"))
			return can_remove_file_name( row, name, otherSpotsHaveThisObjectName, headName, ranges, rules, role);
		else if ( kind.equals( "exchange algebra"))
			return can_remove_exchange_algebra_name( row, name, otherSpotsHaveThisObjectName, headName, ranges, rules, role);
		else if ( kind.equals( "extransfer"))
			return can_remove_extransfer_name( row, name, otherSpotsHaveThisObjectName, headName, ranges, rules, role);
		else
			return true;
	}

	/**
	 * @param row
	 * @param entityType
	 * @param kind
	 * @param objectName
	 * @param otherEntitiesHaveThisObjectName
	 * @param headName
	 * @param ranges
	 * @param rules
	 * @param role
	 * @return
	 */
	public boolean can_remove(int row, String entityType, String kind, String objectName, boolean otherEntitiesHaveThisObjectName, String headName, Vector<String[]> ranges, Rules rules, Role role) {
		// TODO これからはこちらに移行してゆく→実装はRuleNewクラス
		return true;
	}

	/**
	 * @param role
	 * @return
	 */
	protected String[] get_used_agent_variable_names(Role role) {
		// TODO いずれ必要になるだろう
		return null;
	}

	/**
	 * @param row
	 * @param spotVariableName
	 * @param otherSpotsHaveThisSpotVariableName
	 * @param headName
	 * @param ranges
	 * @param rules
	 * @param role
	 * @return
	 */
	private boolean can_remove_spot_variable_name(int row, String spotVariableName, boolean otherSpotsHaveThisSpotVariableName, String headName, Vector<String[]> ranges, Rules rules, Role role) {
		String usedSpotVariableNames[] = get_used_spot_variable_names( role);
		if ( null == usedSpotVariableNames)
			return true;

		boolean result = true;

		for ( int i = 0; i < usedSpotVariableNames.length; ++i) {
			if ( null == usedSpotVariableNames[ i])
				continue;

			if ( !CommonRuleManipulator.correspond( usedSpotVariableNames[ i], spotVariableName, otherSpotsHaveThisSpotVariableName, headName, ranges))
				continue;

			String[] message = new String[] {
				"Role",
				"name = " + role._name,
				"type = " + _kind + "::" + _type,
				"spot variable = " + usedSpotVariableNames[ i],
				"row = " + ( row + 1),
				"column = " + ( _column + 1)
			};
			WarningManager.get_instance().add( message);
			result = false;
		}
		return result;
	}

	/**
	 * @param role
	 * @return
	 */
	protected String[] get_used_spot_variable_names(Role role) {
		return null;
	}

	/**
	 * @param row
	 * @param probabilityName
	 * @param otherSpotsHaveThisProbabilityName
	 * @param headName
	 * @param ranges
	 * @param rules
	 * @param role
	 * @return
	 */
	private boolean can_remove_probability_name(int row, String probabilityName, boolean otherSpotsHaveThisProbabilityName, String headName, Vector<String[]> ranges, Rules rules, Role role) {
		String usedProbabilityNames[] = get_used_probability_names();
		if ( null == usedProbabilityNames)
			return true;

		boolean result = true;

		for ( int i = 0; i < usedProbabilityNames.length; ++i) {
			if ( null == usedProbabilityNames[ i])
				continue;

			if ( !CommonRuleManipulator.correspond( usedProbabilityNames[ i], probabilityName, otherSpotsHaveThisProbabilityName, headName, ranges))
				continue;

			String[] message = new String[] {
				"Role",
				"name = " + role._name,
				"type = " + _kind + "::" + _type,
				"probability = " + usedProbabilityNames[ i],
				"row = " + ( row + 1),
				"column = " + ( _column + 1)
			};
			WarningManager.get_instance().add( message);
			result = false;
		}
		return result;
	}

	/**
	 * @return
	 */
	protected String[] get_used_probability_names() {
		return null;
	}

	/**
	 * @param row
	 * @param collectionName
	 * @param otherSpotsHaveThisCollectionName
	 * @param headName
	 * @param ranges
	 * @param rules
	 * @param role
	 * @return
	 */
	private boolean can_remove_collection_name(int row, String collectionName, boolean otherSpotsHaveThisCollectionName, String headName, Vector<String[]> ranges, Rules rules, Role role) {
		String usedCollectionNames[] = get_used_collection_names();
		if ( null == usedCollectionNames)
			return true;

		boolean result = true;

		for ( int i = 0; i < usedCollectionNames.length; ++i) {
			if ( null == usedCollectionNames[ i])
				continue;

			if ( !CommonRuleManipulator.correspond( usedCollectionNames[ i], collectionName, otherSpotsHaveThisCollectionName, headName, ranges))
				continue;

			String[] message = new String[] {
				"Role",
				"name = " + role._name,
				"type = " + _kind + "::" + _type,
				"collection = " + usedCollectionNames[ i],
				"row = " + ( row + 1),
				"column = " + ( _column + 1)
			};
			WarningManager.get_instance().add( message);
			result = false;
		}
		return result;
	}

	/**
	 * @return
	 */
	protected String[] get_used_collection_names() {
		return null;
	}

	/**
	 * @param row
	 * @param listName
	 * @param otherSpotsHaveThisListName
	 * @param headName
	 * @param ranges
	 * @param rules
	 * @param role
	 * @return
	 */
	private boolean can_remove_list_name(int row, String listName, boolean otherSpotsHaveThisListName, String headName, Vector<String[]> ranges, Rules rules, Role role) {
		String usedListNames[] = get_used_list_names();
		if ( null == usedListNames)
			return true;

		boolean result = true;

		for ( int i = 0; i < usedListNames.length; ++i) {
			if ( null == usedListNames[ i])
				continue;

			if ( !CommonRuleManipulator.correspond( usedListNames[ i], listName, otherSpotsHaveThisListName, headName, ranges))
				continue;

			String[] message = new String[] {
				"Role",
				"name = " + role._name,
				"type = " + _kind + "::" + _type,
				"list = " + usedListNames[ i],
				"row = " + ( row + 1),
				"column = " + ( _column + 1)
			};
			WarningManager.get_instance().add( message);
			result = false;
		}
		return result;
	}

	/**
	 * @return
	 */
	protected String[] get_used_list_names() {
		return null;
	}

	/**
	 * @param row
	 * @param mapMame
	 * @param otherSpotsHaveThisMapName
	 * @param headName
	 * @param ranges
	 * @param rules
	 * @param role
	 * @return
	 */
	private boolean can_remove_map_name(int row, String mapMame, boolean otherSpotsHaveThisMapName, String headName, Vector<String[]> ranges, Rules rules, Role role) {
		String usedMapNames[] = get_used_map_names();
		if ( null == usedMapNames)
			return true;

		boolean result = true;

		for ( int i = 0; i < usedMapNames.length; ++i) {
			if ( null == usedMapNames[ i])
				continue;

			if ( !CommonRuleManipulator.correspond( usedMapNames[ i], mapMame, otherSpotsHaveThisMapName, headName, ranges))
				continue;

			String[] message = new String[] {
				"Role",
				"name = " + role._name,
				"type = " + _kind + "::" + _type,
				"map = " + usedMapNames[ i],
				"row = " + ( row + 1),
				"column = " + ( _column + 1)
			};
			WarningManager.get_instance().add( message);
			result = false;
		}
		return result;
	}

	/**
	 * @return
	 */
	protected String[] get_used_map_names() {
		return null;
	}

	/**
	 * @param row
	 * @param keywordName
	 * @param otherSpotsHaveThisKeywordName
	 * @param headName
	 * @param ranges
	 * @param rules
	 * @param role
	 * @return
	 */
	private boolean can_remove_keyword_name(int row, String keywordName, boolean otherSpotsHaveThisKeywordName, String headName, Vector<String[]> ranges, Rules rules, Role role) {
		String usedKeywordNames[] = get_used_keyword_names();
		if ( null == usedKeywordNames)
			return true;

		boolean result = true;

		for ( int i = 0; i < usedKeywordNames.length; ++i) {
			if ( null == usedKeywordNames[ i])
				continue;

			if ( !CommonRuleManipulator.correspond( usedKeywordNames[ i], keywordName, otherSpotsHaveThisKeywordName, headName, ranges))
				continue;

			String[] message = new String[] {
				"Role",
				"name = " + role._name,
				"type = " + _kind + "::" + _type,
				"keyword = " + usedKeywordNames[ i],
				"row = " + ( row + 1),
				"column = " + ( _column + 1)
			};
			WarningManager.get_instance().add( message);
			result = false;
		}
		return result;
	}

	/**
	 * @return
	 */
	protected String[] get_used_keyword_names() {
		return null;
	}

	/**
	 * @param row
	 * @param numberObjectName
	 * @param otherSpotsHaveThisNumberObjectName
	 * @param headName
	 * @param ranges
	 * @param rules
	 * @param role
	 * @return
	 */
	private boolean can_remove_number_object_name(int row, String numberObjectName, boolean otherSpotsHaveThisNumberObjectName, String headName, Vector<String[]> ranges, Rules rules, Role role) {
		String usedNumberObjectNames[] = get_used_number_object_names();
		if ( null == usedNumberObjectNames)
			return true;

		boolean result = true;

		for ( int i = 0; i < usedNumberObjectNames.length; ++i) {
			if ( null == usedNumberObjectNames[ i])
				continue;

			if ( !CommonRuleManipulator.correspond( usedNumberObjectNames[ i], numberObjectName, otherSpotsHaveThisNumberObjectName, headName, ranges))
				continue;

			String[] message = new String[] {
				"Role",
				"name = " + role._name,
				"type = " + _kind + "::" + _type,
				"number object = " + usedNumberObjectNames[ i],
				"row = " + ( row + 1),
				"column = " + ( _column + 1)
			};
			WarningManager.get_instance().add( message);
			result = false;
		}
		return result;
	}

	/**
	 * @return
	 */
	protected String[] get_used_number_object_names() {
		return null;
	}

	/**
	 * @param row
	 * @param roleVariableName
	 * @param otherSpotsHaveThisRoleVariableName
	 * @param headName
	 * @param ranges
	 * @param rules
	 * @param role
	 * @return
	 */
	private boolean can_remove_role_variable_name(int row, String roleVariableName, boolean otherSpotsHaveThisRoleVariableName, String headName, Vector<String[]> ranges, Rules rules, Role role) {
		String usedRoleVariableNames[] = get_used_role_variable_names();
		if ( null == usedRoleVariableNames)
			return true;

		boolean result = true;

		for ( int i = 0; i < usedRoleVariableNames.length; ++i) {
			if ( null == usedRoleVariableNames[ i])
				continue;

			if ( !CommonRuleManipulator.correspond( usedRoleVariableNames[ i], roleVariableName, otherSpotsHaveThisRoleVariableName, headName, ranges))
				continue;

			String[] message = new String[] {
				"Role",
				"name = " + role._name,
				"type = " + _kind + "::" + _type,
				"role variable = " + usedRoleVariableNames[ i],
				"row = " + ( row + 1),
				"column = " + ( _column + 1)
			};
			WarningManager.get_instance().add( message);
			result = false;
		}
		return result;
	}

	/**
	 * @return
	 */
	protected String[] get_used_role_variable_names() {
		return null;
	}

	/**
	 * @param row
	 * @param timeVariableName
	 * @param otherSpotsHaveThisTimeVariableName
	 * @param headName
	 * @param ranges
	 * @param rules
	 * @param role
	 * @return
	 */
	private boolean can_remove_time_variable_name(int row, String timeVariableName, boolean otherSpotsHaveThisTimeVariableName, String headName, Vector<String[]> ranges, Rules rules, Role role) {
		String usedTimeVariableNames[] = get_used_time_variable_names();
		if ( null == usedTimeVariableNames)
			return true;

		boolean result = true;

		for ( int i = 0; i < usedTimeVariableNames.length; ++i) {
			if ( null == usedTimeVariableNames[ i])
				continue;

			if ( !CommonRuleManipulator.correspond( usedTimeVariableNames[ i], timeVariableName, otherSpotsHaveThisTimeVariableName, headName, ranges))
				continue;

			String[] message = new String[] {
				"Role",
				"name = " + role._name,
				"type = " + _kind + "::" + _type,
				"time variable = " + usedTimeVariableNames[ i],
				"row = " + ( row + 1),
				"column = " + ( _column + 1)
			};
			WarningManager.get_instance().add( message);
			result = false;
		}
		return result;
	}

	/**
	 * @return
	 */
	protected String[] get_used_time_variable_names() {
		return null;
	}

	/**
	 * @param row
	 * @param classVariableName
	 * @param otherSpotsHaveThisClassVariableName
	 * @param headName
	 * @param ranges
	 * @param rules
	 * @param role
	 * @return
	 */
	private boolean can_remove_class_variable_name(int row, String classVariableName, boolean otherSpotsHaveThisClassVariableName, String headName, Vector<String[]> ranges, Rules rules, Role role) {
		String[] usedClassVariableNames = get_used_class_variable_names( role);
		if ( null == usedClassVariableNames)
			return true;

		boolean result = true;

		for ( int i = 0; i < usedClassVariableNames.length; ++i) {
			if ( null == usedClassVariableNames[ i])
				continue;

			if ( !CommonRuleManipulator.correspond( usedClassVariableNames[ i], classVariableName, otherSpotsHaveThisClassVariableName, headName, ranges))
				continue;

			String[] message = new String[] {
				"Role",
				"name = " + role._name,
				"type = " + _kind + "::" + _type,
				"class variable = " + usedClassVariableNames[ i],
				"row = " + ( row + 1),
				"column = " + ( _column + 1)
			};
			WarningManager.get_instance().add( message);
			result = false;
		}
		return result;
	}

	/**
	 * @param role
	 * @return
	 */
	protected String[] get_used_class_variable_names(Role role) {
		return null;
	}

	/**
	 * @param row
	 * @param fileName
	 * @param other_spots_have_this_object_name
	 * @param headName
	 * @param ranges
	 * @param rules
	 * @param role
	 * @return
	 */
	private boolean can_remove_file_name(int row, String fileName, boolean otherSpotsHaveThisFileName, String headName,Vector<String[]> ranges, Rules rules, Role role) {
		String[] usedFileNames = get_used_file_names();
		if ( null == usedFileNames)
			return true;

		boolean result = true;

		for ( int i = 0; i < usedFileNames.length; ++i) {
			if ( null == usedFileNames[ i])
				continue;

			if ( !CommonRuleManipulator.correspond( usedFileNames[ i], fileName, otherSpotsHaveThisFileName, headName, ranges))
				continue;

			String[] message = new String[] {
				"Role",
				"name = " + role._name,
				"type = " + _kind + "::" + _type,
				"file = " + usedFileNames[ i],
				"row = " + ( row + 1),
				"column = " + ( _column + 1)
			};
			WarningManager.get_instance().add( message);
			result = false;
		}
		return result;
	}

	/**
	 * @return
	 */
	protected String[] get_used_file_names() {
		return null;
	}

	/**
	 * @param row
	 * @param exchangeAlgebraName
	 * @param otherSpotsHaveThisExchangeAlgebraName
	 * @param headName
	 * @param ranges
	 * @param rules
	 * @param role
	 * @return
	 */
	private boolean can_remove_exchange_algebra_name(int row, String exchangeAlgebraName, boolean otherSpotsHaveThisExchangeAlgebraName, String headName, Vector<String[]> ranges, Rules rules, Role role) {
		String used_exchange_algebra_names[] = get_used_exchange_algebra_names( role);
		if ( null == used_exchange_algebra_names)
			return true;

		boolean result = true;

		for ( int i = 0; i < used_exchange_algebra_names.length; ++i) {
			if ( null == used_exchange_algebra_names[ i])
				continue;

			if ( !CommonRuleManipulator.correspond( used_exchange_algebra_names[ i], exchangeAlgebraName, otherSpotsHaveThisExchangeAlgebraName, headName, ranges))
				continue;

			String[] message = new String[] {
				"Role",
				"name = " + role._name,
				"type = " + _kind + "::" + _type,
				"exchange algebra = " + used_exchange_algebra_names[ i],
				"row = " + ( row + 1),
				"column = " + ( _column + 1)
			};
			WarningManager.get_instance().add( message);
			result = false;
		}
		return result;
	}

	/**
	 * @param role
	 * @return
	 */
	protected String[] get_used_exchange_algebra_names(Role role) {
		return null;
	}

	/**
	 * @param row
	 * @param exTransferName
	 * @param otherSpotsHaveThisExTransferName
	 * @param headName
	 * @param ranges
	 * @param rules
	 * @param role
	 * @return
	 */
	private boolean can_remove_extransfer_name(int row, String exTransferName, boolean otherSpotsHaveThisExTransferName, String headName, Vector<String[]> ranges, Rules rules, Role role) {
		// TODO Auto-generated method stub
		String usedExtransferNames[] = get_used_extransfer_names( role);
		if ( null == usedExtransferNames)
			return true;

		boolean result = true;

		for ( String usedExtransferName:usedExtransferNames) {
			if ( null == usedExtransferName)
				continue;

			if ( !CommonRuleManipulator.correspond( usedExtransferName, exTransferName, otherSpotsHaveThisExTransferName, headName, ranges))
				continue;

			String[] message = new String[] {
				"Role",
				"name = " + role._name,
				"type = " + _kind + "::" + _type,
				"exchange algebra = " + usedExtransferName,
				"row = " + ( row + 1),
				"column = " + ( _column + 1)
			};
			WarningManager.get_instance().add( message);
			result = false;
		}
		return result;
	}

	/**
	 * @param role
	 * @return
	 */
	protected String[] get_used_extransfer_names(Role role) {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * @param row
	 * @param entityType
	 * @param numberObjectName
	 * @param newType
	 * @param rules
	 * @param role
	 * @return
	 */
	public boolean is_number_object_type_correct(int row, String entityType, String numberObjectName, String newType, Rules rules, Role role) {
		return true;
	}

	/**
	 * @param name
	 * @param row
	 * @param roleName
	 * @param rules
	 * @return
	 */
	public boolean can_remove_role(String name, int row, String roleName, Rules rules) {
		String used_role_names[] = get_used_role_names();
		if ( null == used_role_names)
			return true;

		boolean result = true;

		for ( int i = 0; i < used_role_names.length; ++i) {
			if ( null == used_role_names[ i])
				continue;

			if ( !used_role_names[ i].equals( roleName))
				continue;

			String[] message = new String[] {
				"Role",
				"name = " + name,
				"type = " + _kind + "::" + _type,
				"role = " + used_role_names[ i],
				"row = " + ( row + 1),
				"column = " + ( _column + 1)
			};
			WarningManager.get_instance().add( message);
			result = false;
		}
		return result;
	}

	/**
	 * @return
	 */
	protected String[] get_used_role_names() {
		return null;
	}

	/**
	 * @param name
	 * @param row
	 * @param expression
	 * @param rules
	 * @return
	 */
	public boolean can_remove_expression(String name, int row, Expression expression) {
		ExpressionElements[] expressionElements = get_used_expressions();
		if ( null == expressionElements)
			return true;

		boolean result1 = true;
		boolean result2 = true;
		for ( int i = 0; i < expressionElements.length; ++i) {
			if ( 0 <= expressionElements[ i]._function.indexOf( expression._value[ 0] + "(")) {
				WarningManager.get_instance().add(
					new String[] {
						"Role",
						"name = " + name,
						"type = "
							+ ( _type.equals( ResourceManager.get_instance().get( "rule.type.condition.number.object")) ? "condition" : "command")
							+ "::" + _type,
						"function = " + expressionElements[ i]._function,
						"row = " + ( row + 1),
						"column = " + ( _column + 1)}
				);
				result1 = false;
			}

			String expression_string = VisualShellExpressionManager.get_instance().get_expression( expressionElements[ i]._function);
			if ( 0 <= expression_string.indexOf( expression._value[ 0] + "(")) {
				WarningManager.get_instance().add(
					new String[] {
						"Role",
						"name = " + name,
						"type = "
							+ ( _type.equals( ResourceManager.get_instance().get( "rule.type.condition.number.object")) ? "condition" : "command")
							+ "::" + _type,
						"expression = " + expression_string,
						"row = " + ( row + 1),
						"column = " + ( _column + 1)}
				);
				result2 = false;
			}
		}

		return ( result1 && result2);
	}

	/**
	 * @return
	 */
	protected ExpressionElements[] get_used_expressions() {
		return null;
	}

	/**
	 * @param name
	 * @param row
	 * @param newName
	 * @param originalName
	 * @param rules
	 * @return
	 */
	public boolean update_stage_name(String name, int row, String newName, String originalName, Rules rules) {
		return update_stage_name( newName, originalName);
	}

	/**
	 * @param newName
	 * @param originalName
	 * @return
	 */
	public boolean update_stage_name(String newName, String originalName) {
		return false;
	}

	/**
	 * @param name
	 * @param row
	 * @param stageName
	 * @param rules
	 * @return
	 */
	public boolean can_remove_stage_name(String name, int row, String stageName, Rules rules) {
		String[] usedStageNames = get_used_stage_names();
		if ( null == usedStageNames)
			return true;

		boolean result = true;

		for ( String usedStageName:usedStageNames) {
			if ( null == usedStageName)
				continue;

			if ( !usedStageName.equals( stageName))
				continue;

			String[] message = new String[] {
				"Role",
				"name = " + name,
				"type = "
					+ ( _type.equals( ResourceManager.get_instance().get( "rule.type.condition.stage")) ? "condition" : "command")
					+ "::" + _type,
				"stage = " + _value,
				"row = " + ( row + 1),
				"column = " + ( _column + 1)
			};

			WarningManager.get_instance().add( message);
			result = false;
		}
		return result;
	}

	/**
	 * @param stageName
	 * @return
	 */
	public boolean can_remove_stage_name(String stageName) {
		String[] usedStageNames = get_used_stage_names();
		if ( null == usedStageNames)
			return true;

		boolean result = true;

		for ( String usedStageName:usedStageNames) {
			if ( null == usedStageName)
				continue;

			if ( !usedStageName.equals( stageName))
				continue;

			result = false;
		}
		return result;
	}

	/**
	 * @param name
	 * @param row
	 * @param stageNames
	 * @param rules
	 * @return
	 */
	public boolean can_adjust_stage_name(String name, int row, Vector<String> stageNames, Rules rules) {
		String[] usedStageNames = get_used_stage_names();
		if ( null == usedStageNames)
			return true;

		boolean result = true;

		for ( String usedStageName:usedStageNames) {
			if ( null == usedStageName)
				continue;

			if ( stageNames.contains( usedStageName))
				continue;

			String[] message = new String[] {
				"Role",
				"name = " + name,
				"type = "
					+ ( _type.equals( ResourceManager.get_instance().get( "rule.type.condition.stage")) ? "condition" : "command")
					+ "::" + _type,
				"stage = " + _value,
				"row = " + ( row + 1),
				"column = " + ( _column + 1)
			};

			WarningManager.get_instance().add( message);
			result = false;
		}
		return result;
	}

	/**
	 * @return
	 */
	protected String[] get_used_stage_names() {
		return null;
	}

	/**
	 * @return
	 */
	public boolean update_stage_manager() {
		return false;
	}

	/**
	 * @param expressionMap
	 * @param usedExpressionMap
	 */
	public void get_used_expressions(TreeMap<String, Expression> expressionMap, TreeMap<String, Expression> usedExpressionMap) {
	}

	/**
	 * @param originalFunctionName
	 * @param newFunctionName
	 * @return
	 */
	public boolean update_function(String originalFunctionName, String newFunctionName) {
		return false;
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
		return false;
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
		return false;
	}

	/**
	 * @param originalName
	 * @param newName
	 * @return
	 */
	public boolean update_role_name(String originalName, String newName) {
		return false;
	}

	/**
	 * @param originalName
	 * @param newName
	 * @param rules
	 * @return
	 */
	public boolean update_role_name(String originalName, String newName, Rules rules) {
		return update_role_name( originalName, newName);
	}

	/**
	 * @param kind
	 * @param originalName
	 * @param newName
	 * @param entityType
	 * @param rules
	 * @param role
	 * @return
	 */
	public boolean update_object_name(String kind, String originalName, String newName, String entityType, Rules rules, Role role) {
		if ( kind.equals( "spot variable"))
			return update_spot_variable_name( originalName, newName, entityType, role);
		else if ( kind.equals( "probability"))
			return update_probability_name( originalName, newName, entityType, role);
		else if ( kind.equals( "collection"))
			return update_collection_name( originalName, newName, entityType, role);
		else if ( kind.equals( "list"))
			return update_list_name( originalName, newName, entityType, role);
		else if ( kind.equals( "map"))
			return update_map_name( originalName, newName, entityType, role);
		else if ( kind.equals( "keyword"))
			return update_keyword_name( originalName, newName, entityType, role);
		else if ( kind.equals( "number object"))
			return update_number_object_name( originalName, newName, entityType, role);
		else if ( kind.equals( "role variable"))
			return update_role_variable_name( originalName, newName, entityType, role);
		else if ( kind.equals( "time variable"))
			return update_time_variable_name( originalName, newName, entityType, role);
		else if ( kind.equals( "class variable"))
			return update_class_variable_name( originalName, newName, entityType, role);
		else if ( kind.equals( "file"))
			return update_file_name( originalName, newName, entityType, role);
		else if ( kind.equals( "exchange algebra"))
			return update_exchange_algebra_name( originalName, newName, entityType, role);
		else if ( kind.equals( "extransfer"))
			return update_extransfer_name( originalName, newName, entityType, role);

		return false;
	}

	protected boolean update_agent_variable_name(String originalName, String newName, String entityType, Role role) {
		// TODO いずれ必要になるだろう
		return false;
	}

	/**
	 * @param originalName
	 * @param newName
	 * @param entityType
	 * @param role
	 * @return
	 */
	protected boolean update_spot_variable_name(String originalName, String newName, String entityType, Role role) {
		return false;
	}

	/**
	 * @param originalName
	 * @param newName
	 * @param entityType
	 * @param role
	 * @return
	 */
	protected boolean update_probability_name(String originalName, String newName, String entityType, Role role) {
		return false;
	}

	/**
	 * @param originalName
	 * @param newName
	 * @param entityType
	 * @param role
	 * @return
	 */
	protected boolean update_collection_name(String originalName, String newName, String entityType, Role role) {
		return false;
	}

	/**
	 * @param originalName
	 * @param newName
	 * @param entityType
	 * @param role
	 * @return
	 */
	protected boolean update_list_name(String originalName, String newName, String entityType, Role role) {
		return false;
	}

	/**
	 * @param originalName
	 * @param newName
	 * @param entityType
	 * @param role
	 * @return
	 */
	protected boolean update_map_name(String originalName, String newName, String entityType, Role role) {
		return false;
	}

	/**
	 * @param originalName
	 * @param newName
	 * @param entityType
	 * @param role
	 * @return
	 */
	protected boolean update_keyword_name(String originalName, String newName, String entityType, Role role) {
		return false;
	}

	/**
	 * @param originalName
	 * @param newName
	 * @param entityType
	 * @param role
	 * @return
	 */
	protected boolean update_number_object_name(String originalName, String newName, String entityType, Role role) {
		return false;
	}

	/**
	 * @param originalName
	 * @param newName
	 * @param entityType
	 * @param role
	 * @return
	 */
	protected boolean update_role_variable_name(String originalName, String newName, String entityType, Role role) {
		return false;
	}

	/**
	 * @param originalName
	 * @param newName
	 * @param entityType
	 * @param role
	 * @return
	 */
	protected boolean update_time_variable_name(String originalName, String newName, String entityType, Role role) {
		return false;
	}

	/**
	 * @param originalName
	 * @param newName
	 * @param entityType
	 * @param role
	 * @return
	 */
	protected boolean update_class_variable_name(String originalName, String newName, String entityType, Role role) {
		return false;
	}

	/**
	 * @param originalName
	 * @param newName
	 * @param entityType
	 * @param role
	 * @return
	 */
	protected boolean update_file_name(String originalName, String newName, String entityType, Role role) {
		return false;
	}

	/**
	 * @param originalName
	 * @param newName
	 * @param entityType
	 * @param role
	 * @return
	 */
	protected boolean update_exchange_algebra_name(String originalName, String newName, String entityType, Role role) {
		return false;
	}

	/**
	 * @param originalName
	 * @param newName
	 * @param entityType
	 * @param role
	 * @return
	 */
	protected boolean update_extransfer_name(String originalName, String newName, String entityType, Role role) {
		// TODO Auto-generated method stub
		return false;
	}

	/**
	 * @param visualShellExpressionManager
	 * @return
	 */
	public boolean update_expression(VisualShellExpressionManager visualShellExpressionManager) {
		return false;
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
		return false;
	}

	/**
	 * @param row
	 * @param rules
	 * @param role
	 * @param drawObjects
	 * @return
	 */
	public boolean can_paste(int row, Rules rules, Role role, Layer drawObjects) {
		if ( can_paste( role, drawObjects))
			return true;

		String[] message = new String[] {
			"Role",
			"name = " + role._name,
			"type = " + _kind + "::" + _type,
			"row = " + ( row + 1),
			"column = " + ( _column + 1),
			_value
		};

		WarningManager.get_instance().add( message);

		return false;
	}

	/**
	 * @param role
	 * @param drawObjects
	 * @return
	 */
	protected boolean can_paste(Role role, Layer drawObjects) {
		return true;
	}

	/**
	 * @param row
	 * @param initialValueMap
	 * @param demo
	 * @param role
	 * @return
	 */
	public String get_script(int row, InitialValueMap initialValueMap, boolean demo, Role role) {
		String value = ( null == initialValueMap || demo) ? _value : initialValueMap.get_script( _value);
		if ( null == value) {
			on_get_script_error( row, role);
			return "";
		}

		if ( ( ( _kind.equals( "condition") && _type.equals( ResourceManager.get_instance().get( "rule.type.condition.number.object")))
			|| ( _kind.equals( "command") && _type.equals( ResourceManager.get_instance().get( "rule.type.command.substitution"))))
			&& null != initialValueMap && demo) {
			value = initialValueMap.get_script( _value);
			if ( null == value) {
				on_get_script_error( row, role);
				return "";
			}
		}

		value = get_script( value, role);
		if ( null == value) {
			on_get_script_error( row, role);
			return "";
		}

		if ( value.equals( "")
			&& !_type.equals( ResourceManager.get_instance().get( "rule.type.condition.stage"))) {
			on_get_script_error( row, role);
			return "";
		}

		if ( role instanceof SpotRole
			&& !value.startsWith( "<")
			&& !value.startsWith( "!<")
			&& !_type.equals( ResourceManager.get_instance().get( "rule.type.condition.stage"))
			&& !_type.equals( ResourceManager.get_instance().get( "rule.type.condition.others"))
			&& !_type.equals( ResourceManager.get_instance().get( "rule.type.command.next.stage"))
			&& !_type.equals( ResourceManager.get_instance().get( "rule.type.command.terminate"))
			&& !_type.equals( ResourceManager.get_instance().get( "rule.type.command.trace"))
			&& !_type.equals( ResourceManager.get_instance().get( "rule.type.command.others")))
			return ( ( value.startsWith( "!"))
				? ( "!<>" + value.substring( "!".length()))
				: ( "<>" + value));

		return value;
	}

	/**
	 * @param value
	 * @param role
	 * @return
	 */
	protected String get_script(String value, Role role) {
		return value;
	}

	/**
	 * @param row
	 * @param role
	 */
	private void on_get_script_error(int row, Role role) {
		String[] message = new String[] {
			"Role",
			"name = " + role._name,
			"type = " + _kind + "::" + _type,
			"row = " + ( row + 1),
			"column = " + ( _column + 1),
			_value
		};
		WarningManager.get_instance().add( message);
	}

	/**
	 * @param role
	 * @return
	 */
	public String get_cell_text(Role role) {
		return _value;
	}

	/**
	 * @param writer
	 * @return
	 */
	public boolean write(/*String name, */Writer writer) throws SAXException {
		// 2011.6.6 _kindを使用するように変更
		AttributesImpl attributesImpl = new AttributesImpl();
		attributesImpl.addAttribute( null, null, "column", "", String.valueOf( _column));
		attributesImpl.addAttribute( null, null, "type", "", Writer.escapeAttributeCharData( _type));
		attributesImpl.addAttribute( null, null, "value", "", Writer.escapeAttributeCharData( _value));
		attributesImpl.addAttribute( null, null, "or", "", _or ? "true" : "false");
		writer.writeElement( null, null, _kind, attributesImpl);
		return true;
	}

	/**
	 * @param role
	 */
	public void make_unique_agent_name(Role role) {
//		if ( !_type.equals( ResourceManager.get_instance().get( "rule.type.command.create.agent")))
//			return;
//
//		String[] elements = CommonRuleManipulator2.get_elements( _value);
//		if ( null == elements || ( 2 != elements.length && 3 != elements.length))
//			return;
//
//		String name = "";
//		int index = 1;
//		while ( true) {
//			name = elements[ 0] + "(copy" + index + ")";
//
//			if ( LayerManager.get_instance().has_same_agent_name( name, elements[ 1])) {
//				++index;
//				continue;
//			}
//
//			if ( LayerManager.get_instance().has_same_agent_name( name, elements[ 1], role)) {
//				++index;
//				continue;
//			}
//
//			break;
//		}
//
//		String[] texts = _value.split( " ");
//		if ( null == texts || 2 != texts.length)
//			return;
//
//		_value = ( texts[ 0] + " " + name);
//		for ( int i = 1; i < elements.length; ++i)
//			_value += ( "=" + elements[ i]);
	}

	/**
	 * 
	 */
	public void print() {
		System.out.println( _kind + " : " + _type + " : " + _value);
	}
}
