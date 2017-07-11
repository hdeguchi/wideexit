/**
 * 
 */
package soars.application.visualshell.object.role.base.object.generic;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
import soars.application.visualshell.object.role.base.edit.tab.CommandTabbedPane;
import soars.application.visualshell.object.role.base.edit.tab.ConditionTabbedPane;
import soars.application.visualshell.object.role.base.object.Rules;
import soars.application.visualshell.object.role.base.object.base.RuleNew;
import soars.application.visualshell.object.role.base.object.generic.element.ConstantRule;
import soars.application.visualshell.object.role.base.object.generic.element.EntityVariableRule;
import soars.application.visualshell.object.role.base.object.generic.element.ExpressionRule;
import soars.application.visualshell.object.role.base.object.generic.element.IObject;
import soars.common.soars.warning.WarningManager;
import soars.common.utility.tool.common.Tool;
import soars.common.utility.tool.expression.Expression;
import soars.common.utility.xml.sax.Writer;

/**
 * @author kurata
 *
 */
public class GenericRule extends RuleNew {

	/**
	 * 
	 */
	public String _id = "";

	/**
	 * 
	 */
	public boolean _denial = false;

	/**
	 * 
	 */
	public boolean _system = true;

	/**
	 * 
	 */
	public EntityVariableRule _subject = new EntityVariableRule();

	/**
	 * 
	 */
	public List<IObject> _objects = new ArrayList<IObject>();

	/**
	 * 
	 */
	static public Map<String, String[]> _operatorScriptMap = null;

	/**
	 * 
	 */
	static {
		startup();
	}

	/**
	 * 
	 */
	private static void startup() {
		_operatorScriptMap = new HashMap<String, String[]>();
		_operatorScriptMap.put( "",   new String[] { "",  "=" });
		_operatorScriptMap.put( ">",  new String[] { "!", "=<"});
		_operatorScriptMap.put( ">=", new String[] { "",  "=>"});
		_operatorScriptMap.put( "==", new String[] { "",  "=="});
		_operatorScriptMap.put( "!=", new String[] { "!", "=="});
		_operatorScriptMap.put( "<=", new String[] { "",  "=<"});
		_operatorScriptMap.put( "<",  new String[] { "!", "=>"});
	}

	/**
	 * @param kind
	 * @param type
	 * @param system
	 * @return
	 */
	public static GenericRule create(String kind, String type, boolean system) {
		return new GenericRule( kind, type, system);
	}

	/**
	 * @param kind
	 * @param type
	 * @param system
	 */
	public GenericRule(String kind, String type, boolean system) {
		super(kind, type, "");
		_system = system;
	}

	/**
	 * @param genericRule
	 */
	public GenericRule(GenericRule genericRule) {
		super(genericRule);
		_id = genericRule._id;
		_denial = genericRule._denial;
		_system = genericRule._system;
		_subject = new EntityVariableRule( genericRule._subject);
		for ( IObject object:genericRule._objects)
			_objects.add( create( object));
	}

	/**
	 * @param object
	 * @return
	 */
	private IObject create(IObject object) {
		if ( object instanceof EntityVariableRule)
			return new EntityVariableRule( ( EntityVariableRule)object);
		else if ( object instanceof ConstantRule)
			return new ConstantRule( ( ConstantRule)object);
		else if ( object instanceof ExpressionRule)
			return new ExpressionRule( ( ExpressionRule)object);
		return null;
	}

	/**
	 * @param kind
	 * @param column
	 * @param type
	 * @param or
	 * @param id
	 * @param denial
	 * @param system
	 */
	public GenericRule(String kind, int column, String type, boolean or, String id, boolean denial, boolean system) {
		super(kind, column, type, or);
		_id = id;
		_denial = denial;
		_system = system;
	}

	/**
	 * @param kind
	 * @param column
	 * @param type
	 * @param or
	 */
	public GenericRule(String kind, int column, String type, boolean or) {
		super(kind, column, type, or);
	}

	/**
	 * @param kind
	 * @param column
	 * @param type
	 * @param or
	 * @param role
	 */
	public GenericRule(String kind, int column, String type, boolean or, Role role) {
		super(kind, column, type, or);
		_subject._entity = ( role instanceof AgentRole) ? "self" : "currentspot";
	}

	/**
	 * @param genericRule
	 * @return
	 */
	public boolean same_as(GenericRule genericRule) {
		if ( !_kind.equals( genericRule._kind)
			|| !_type.equals( genericRule._type)
			|| !_id.equals( genericRule._id)
			|| _denial != genericRule._denial
			|| _system != genericRule._system
			|| !_subject.same_as( genericRule._subject))
			return false;

		if ( _objects.size() != genericRule._objects.size())
			return false;

		for ( int i = 0; i < _objects.size(); ++i) {
			if ( !_objects.get( i).same_as( genericRule._objects.get( i)))
				return false;
		}

		return true;
	}

	/**
	 * @param genericRule
	 */
	public void get(GenericRule genericRule) {
		_type = genericRule._type;
		_value = genericRule._value;
		_id = genericRule._id;
		_denial = genericRule._denial;
		_system = genericRule._system;
		_subject.copy( genericRule._subject);
		_objects.clear();
		for ( IObject object:genericRule._objects)
			_objects.add( create( object));
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.role.base.object.base.Rule#get_initial_values(java.util.Vector, java.lang.String[])
	 */
	@Override
	public void get_initial_values(Vector<String> initialValues, String[] suffixes) {
		_subject.get_initial_values( initialValues);
		for ( IObject object:_objects)
			object.get_initial_values( initialValues);
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.role.base.object.base.Rule#has_same_agent_name(java.lang.String, java.lang.String)
	 */
	@Override
	public boolean has_same_agent_name(String name, String number) {
		if ( _subject.has_same_agent_name( name, number))
			return true;

		for ( IObject object:_objects) {
			if ( object.has_same_agent_name( name, number))
				return true;
		}
		return false;
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.role.base.object.base.Rule#contains_this_alias(java.lang.String)
	 */
	@Override
	public boolean contains_this_alias(String alias) {
		if ( _subject.contains_this_alias( alias))
			return true;

		for ( IObject object:_objects) {
			if ( object.contains_this_alias( alias))
				return true;
		}
		return false;
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.role.base.object.base.Rule#get_used_agent_names()
	 */
	@Override
	protected String[] get_used_agent_names() {
		List<String> names = new ArrayList<String>();
		_subject.get_used_agent_names( names);
		for ( IObject object:_objects)
			object.get_used_agent_names( names);
		return names.toArray( new String[ 0]);
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.role.base.object.base.Rule#get_used_spot_names()
	 */
	@Override
	protected String[] get_used_spot_names() {
		List<String> names = new ArrayList<String>();
		_subject.get_used_spot_names( names);
		for ( IObject object:_objects)
			object.get_used_spot_names( names);
		return names.toArray( new String[ 0]);
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.role.base.object.base.Rule#get_used_role_names()
	 */
	@Override
	protected String[] get_used_role_names() {
		List<String> names = new ArrayList<String>();
		get_used_independent_variable_names( "role", names);
		get_used_independent_variable_names( "agent role", names);
		get_used_independent_variable_names( "spot role", names);
		return names.toArray( new String[ 0]);
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.role.base.object.base.Rule#get_used_stage_names()
	 */
	@Override
	protected String[] get_used_stage_names() {
		List<String> names = new ArrayList<String>();
		get_used_independent_variable_names( "stage", names);
		return names.toArray( new String[ 0]);
	}

	/**
	 * @param type
	 * @param names
	 */
	private void get_used_independent_variable_names(String type, List<String> names) {
		_subject.get_used_independent_variable_names( type, names);
		for ( IObject object:_objects)
			object.get_used_independent_variable_names( type, names);
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.role.base.object.base.Rule#get_used_agent_variable_names(soars.application.visualshell.object.role.base.Role)
	 */
	@Override
	protected String[] get_used_agent_variable_names(Role role) {
		// TODO いずれ必要になるだろう
		List<String> names = new ArrayList<String>();
		_subject.get_used_agent_variable_names( names);
		for ( IObject object:_objects)
			object.get_used_agent_variable_names( names, _subject);
		return names.toArray( new String[ 0]);
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.role.base.object.base.Rule#get_used_spot_variable_names(soars.application.visualshell.object.role.base.Role)
	 */
	@Override
	protected String[] get_used_spot_variable_names(Role role) {
		List<String> names = new ArrayList<String>();
		_subject.get_used_spot_variable_names( names);
		for ( IObject object:_objects)
			object.get_used_spot_variable_names( names, _subject);
		return names.toArray( new String[ 0]);
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.role.base.object.base.Rule#get_used_probability_names()
	 */
	@Override
	protected String[] get_used_probability_names() {
		return get_used_variable_names( "probability");
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.role.base.object.base.Rule#get_used_collection_names()
	 */
	@Override
	protected String[] get_used_collection_names() {
		return get_used_variable_names( "collection");
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.role.base.object.base.Rule#get_used_list_names()
	 */
	@Override
	protected String[] get_used_list_names() {
		return get_used_variable_names( "list");
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.role.base.object.base.Rule#get_used_map_names()
	 */
	@Override
	protected String[] get_used_map_names() {
		return get_used_variable_names( "map");
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.role.base.object.base.Rule#get_used_keyword_names()
	 */
	@Override
	protected String[] get_used_keyword_names() {
		return get_used_variable_names( "keyword");
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.role.base.object.base.Rule#get_used_number_object_names()
	 */
	@Override
	protected String[] get_used_number_object_names() {
		return get_used_variable_names( "number object");
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.role.base.object.base.Rule#get_used_role_variable_names()
	 */
	@Override
	protected String[] get_used_role_variable_names() {
		return get_used_variable_names( "role variable");
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.role.base.object.base.Rule#get_used_time_variable_names()
	 */
	@Override
	protected String[] get_used_time_variable_names() {
		return get_used_variable_names( "time variable");
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.role.base.object.base.Rule#get_used_class_variable_names(soars.application.visualshell.object.role.base.Role)
	 */
	@Override
	protected String[] get_used_class_variable_names(Role role) {
		return get_used_variable_names( "class variable");
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.role.base.object.base.Rule#get_used_file_names()
	 */
	@Override
	protected String[] get_used_file_names() {
		return get_used_variable_names( "file");
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.role.base.object.base.Rule#get_used_exchange_algebra_names(soars.application.visualshell.object.role.base.Role)
	 */
	@Override
	protected String[] get_used_exchange_algebra_names(Role role) {
		return get_used_variable_names( "exchange algebra");
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.role.base.object.base.Rule#get_used_extransfer_names(soars.application.visualshell.object.role.base.Role)
	 */
	@Override
	protected String[] get_used_extransfer_names(Role role) {
		return get_used_variable_names( "extransfer");
	}

	/**
	 * @param type
	 * @return
	 */
	private String[] get_used_variable_names(String type) {
		List<String> names = new ArrayList<String>();
		get_used_variable_names( type, names);
		return names.toArray( new String[ 0]);
	}

	/**
	 * @param type
	 * @param names
	 */
	private void get_used_variable_names(String type, List<String> names) {
		_subject.get_used_variable_names( type, names);
		for ( IObject object:_objects)
			object.get_used_variable_names( type, names, _subject);
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.role.base.object.base.Rule#can_remove_expression(java.lang.String, int, soars.common.utility.tool.expression.Expression)
	 */
	@Override
	public boolean can_remove_expression(String name, int row, Expression expression) {
		// TODO 2014.2.14
		List<String> functionNames = new ArrayList<String>();
		for ( IObject object:_objects)
			object.get_used_expressions( functionNames);

		boolean result = true;
		for ( String functionName:functionNames) {
			if ( !functionName.equals( expression._value[ 0]))
				continue;

			String functionFullname = VisualShellExpressionManager.get_instance().get_function( functionName);
			functionFullname = ( null != functionFullname) ? functionFullname : "";
			WarningManager.get_instance().add(
				new String[] {
					"Role",
					"name = " + name,
					"type = " + _kind + "::" + _type,
					"function = " + functionFullname,
					"row = " + ( row + 1),
					"column = " + ( _column + 1)}
			);
			result = false;
		}

		return result;
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.role.base.object.base.Rule#get_used_expressions(java.util.TreeMap, java.util.TreeMap)
	 */
	@Override
	public void get_used_expressions(TreeMap<String, Expression> expressionMap, TreeMap<String, Expression> usedExpressionMap) {
		// TODO 2014.2.5
		List<String> functionNames = new ArrayList<String>();
		for ( IObject object:_objects)
			object.get_used_expressions( functionNames);

		for ( String functionName:functionNames) {
			if ( null != usedExpressionMap.get( functionName))
				continue;

			Expression expression = expressionMap.get( functionName);
			if ( null == expression)
				continue;

			usedExpressionMap.put( functionName, expression);
		}
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.role.base.object.base.Rule#update_agent_name_and_number(java.lang.String, java.lang.String, java.lang.String, java.util.Vector, java.lang.String, java.util.Vector)
	 */
	@Override
	public boolean update_agent_name_and_number(String newName, String originalName, String headName, Vector<String[]> ranges, String newHeadName, Vector<String[]> newRanges) {
		boolean result = _subject.update_agent_or_spot_name_and_number( "agent", newName, originalName, headName, ranges, newHeadName, newRanges);
		for ( IObject object:_objects) {
			if ( object.update_agent_or_spot_name_and_number( "agent", newName, originalName, headName, ranges, newHeadName, newRanges))
				result = true;
		}
		return result;
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.role.base.object.base.Rule#update_spot_name_and_number(java.lang.String, java.lang.String, java.lang.String, java.util.Vector, java.lang.String, java.util.Vector)
	 */
	@Override
	public boolean update_spot_name_and_number(String newName, String originalName, String headName, Vector<String[]> ranges, String newHeadName, Vector<String[]> newRanges) {
		boolean result = _subject.update_agent_or_spot_name_and_number( "spot", newName, originalName, headName, ranges, newHeadName, newRanges);
		for ( IObject object:_objects) {
			if ( object.update_agent_or_spot_name_and_number( "spot", newName, originalName, headName, ranges, newHeadName, newRanges))
				result = true;
		}
		return result;
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.role.base.object.base.Rule#update_role_name(java.lang.String, java.lang.String)
	 */
	@Override
	public boolean update_role_name(String originalName, String newName) {
		boolean result = _subject.update_role_name( originalName, newName);
		for ( IObject object:_objects) {
			if ( object.update_role_name( originalName, newName))
				result = true;
		}
		return result;
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.role.base.object.base.Rule#update_stage_name(java.lang.String, java.lang.String)
	 */
	@Override
	public boolean update_stage_name(String newName, String originalName) {
		boolean result = _subject.update_independent_variable_name( "stage", originalName, newName);
		for ( IObject object:_objects) {
			if ( object.update_independent_variable_name( "stage", originalName, newName))
				result = true;
		}
		return result;
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.role.base.object.base.Rule#update_agent_variable_name(java.lang.String, java.lang.String, java.lang.String, soars.application.visualshell.object.role.base.Role)
	 */
	@Override
	protected boolean update_agent_variable_name(String originalName, String newName, String entityType, Role role) {
		// TODO いずれ必要になるだろう
		boolean result = _subject.update_agent_variable_name( originalName, newName, entityType);
		for ( IObject object:_objects) {
			if ( object.update_agent_variable_name( originalName, newName, entityType, _subject))
				result = true;
		}
		return result;
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.role.base.object.base.Rule#update_spot_variable_name(java.lang.String, java.lang.String, java.lang.String, soars.application.visualshell.object.role.base.Role)
	 */
	@Override
	protected boolean update_spot_variable_name(String originalName, String newName, String entityType, Role role) {
		boolean result = _subject.update_spot_variable_name( originalName, newName, entityType);
		for ( IObject object:_objects) {
			if ( object.update_spot_variable_name( originalName, newName, entityType, _subject))
				result = true;
		}
		return result;
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.role.base.object.base.Rule#update_probability_name(java.lang.String, java.lang.String, java.lang.String, soars.application.visualshell.object.role.base.Role)
	 */
	@Override
	protected boolean update_probability_name(String originalName, String newName, String entityType, Role role) {
		return update_variable_name( "probability", originalName, newName, entityType);
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.role.base.object.base.Rule#update_collection_name(java.lang.String, java.lang.String, java.lang.String, soars.application.visualshell.object.role.base.Role)
	 */
	@Override
	protected boolean update_collection_name(String originalName, String newName, String entityType, Role role) {
		return update_variable_name( "collection", originalName, newName, entityType);
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.role.base.object.base.Rule#update_list_name(java.lang.String, java.lang.String, java.lang.String, soars.application.visualshell.object.role.base.Role)
	 */
	@Override
	protected boolean update_list_name(String originalName, String newName, String entityType, Role role) {
		return update_variable_name( "list", originalName, newName, entityType);
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.role.base.object.base.Rule#update_map_name(java.lang.String, java.lang.String, java.lang.String, soars.application.visualshell.object.role.base.Role)
	 */
	@Override
	protected boolean update_map_name(String originalName, String newName, String entityType, Role role) {
		return update_variable_name( "map", originalName, newName, entityType);
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.role.base.object.base.Rule#update_keyword_name(java.lang.String, java.lang.String, java.lang.String, soars.application.visualshell.object.role.base.Role)
	 */
	@Override
	protected boolean update_keyword_name(String originalName, String newName, String entityType, Role role) {
		return update_variable_name( "keyword", originalName, newName, entityType);
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.role.base.object.base.Rule#update_number_object_name(java.lang.String, java.lang.String, java.lang.String, soars.application.visualshell.object.role.base.Role)
	 */
	@Override
	protected boolean update_number_object_name(String originalName, String newName, String entityType, Role role) {
		return update_variable_name( "number object", originalName, newName, entityType);
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.role.base.object.base.Rule#update_role_variable_name(java.lang.String, java.lang.String, java.lang.String, soars.application.visualshell.object.role.base.Role)
	 */
	@Override
	protected boolean update_role_variable_name(String originalName, String newName, String entityType, Role role) {
		return update_variable_name( "role variable", originalName, newName, entityType);
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.role.base.object.base.Rule#update_time_variable_name(java.lang.String, java.lang.String, java.lang.String, soars.application.visualshell.object.role.base.Role)
	 */
	@Override
	protected boolean update_time_variable_name(String originalName, String newName, String entityType, Role role) {
		return update_variable_name( "time variable", originalName, newName, entityType);
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.role.base.object.base.Rule#update_class_variable_name(java.lang.String, java.lang.String, java.lang.String, soars.application.visualshell.object.role.base.Role)
	 */
	@Override
	protected boolean update_class_variable_name(String originalName, String newName, String entityType, Role role) {
		return update_variable_name( "class variable", originalName, newName, entityType);
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.role.base.object.base.Rule#update_file_name(java.lang.String, java.lang.String, java.lang.String, soars.application.visualshell.object.role.base.Role)
	 */
	@Override
	protected boolean update_file_name(String originalName, String newName, String entityType, Role role) {
		return update_variable_name( "file", originalName, newName, entityType);
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.role.base.object.base.Rule#update_exchange_algebra_name(java.lang.String, java.lang.String, java.lang.String, soars.application.visualshell.object.role.base.Role)
	 */
	@Override
	protected boolean update_exchange_algebra_name(String originalName, String newName, String entityType, Role role) {
		return update_variable_name( "exchange algebra", originalName, newName, entityType);
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.role.base.object.base.Rule#update_extransfer_name(java.lang.String, java.lang.String, java.lang.String, soars.application.visualshell.object.role.base.Role)
	 */
	@Override
	protected boolean update_extransfer_name(String originalName, String newName, String entityType, Role role) {
		return update_variable_name( "extransfer", originalName, newName, entityType);
	}

	/**
	 * @param type
	 * @param originalName
	 * @param newName
	 * @param entityType
	 * @return
	 */
	private boolean update_variable_name(String type, String originalName, String newName, String entityType) {
		boolean result = _subject.update_variable_name( type, originalName, newName, entityType);
		for ( IObject object:_objects) {
			if ( object.update_variable_name( type, originalName, newName, entityType, _subject))
				result = true;
		}
		return result;
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.role.base.object.base.Rule#update_function(java.lang.String, java.lang.String)
	 */
	@Override
	public boolean update_function(String originalFunctionName, String newFunctionName) {
		// TODO Auto-generated method stub
		boolean result = false;
		for ( IObject object:_objects) {
			if ( object.update_function( originalFunctionName, newFunctionName))
				result = true;
		}
		return result;
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.role.base.object.base.Rule#update_expression(soars.application.visualshell.object.expression.VisualShellExpressionManager)
	 */
	@Override
	public boolean update_expression(VisualShellExpressionManager visualShellExpressionManager) {
		// TODO Auto-generated method stub
		boolean result = false;
		for ( IObject object:_objects) {
			if ( object.update_expression( visualShellExpressionManager))
				result = true;
		}
		return result;
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.role.base.object.base.Rule#update_expression(soars.common.utility.tool.expression.Expression, int, soars.common.utility.tool.expression.Expression, int, soars.application.visualshell.object.role.base.Role)
	 */
	@Override
	public boolean update_expression(Expression newExpression, int newVariableCount, Expression originalExpression, int row, Role role) {
		// TODO Auto-generated method stub
		boolean result = false;
		for ( IObject object:_objects) {
			if ( object.update_expression( newExpression, newVariableCount, originalExpression)) {
				WarningManager.get_instance().add(
					new String[] {
						"Role = " + role._name,
						"type = " + _kind + "::" + _type,
						originalExpression.get_function() + " -> " + newExpression.get_function(),
						"row = " + ( row + 1),
						"column = " + ( _column + 1)}
				);
				result = true;
			}
		}
		return result;
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.role.base.object.base.Rule#can_paste(soars.application.visualshell.object.role.base.Role, soars.application.visualshell.layer.Layer)
	 */
	@Override
	protected boolean can_paste(Role role, Layer drawObjects) {
		if ( !_subject.can_paste( drawObjects))
			return false;
		for ( IObject object:_objects) {
			if ( !object.can_paste( drawObjects, _subject))
				return false;
		}
		return true;
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.role.base.object.base.Rule#is_number_object_type_correct(int, java.lang.String, java.lang.String, java.lang.String, soars.application.visualshell.object.role.base.object.Rules, soars.application.visualshell.object.role.base.Role)
	 */
	@Override
	public boolean is_number_object_type_correct(int row, String entityType, String numberObjectName, String newType, Rules rules, Role role) {
		boolean result = _subject.is_number_object_type_correct( entityType, numberObjectName, newType);
		for ( IObject object:_objects) {
			if ( !object.is_number_object_type_correct( entityType, numberObjectName, newType, _subject))
				result = false;
		}
		return result;
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.role.base.object.base.Rule#update_stage_manager()
	 */
	@Override
	public boolean update_stage_manager() {
		boolean result = _subject.update_stage_manager();
		for ( IObject object:_objects) {
			if ( object.update_stage_manager())
				result = true;
		}
		return result;
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.role.base.object.base.Rule#get_script(int, soars.application.visualshell.object.experiment.InitialValueMap, boolean, soars.application.visualshell.object.role.base.Role)
	 */
	@Override
	public String get_script(int row, InitialValueMap initialValueMap, boolean demo, Role role) {
		// TODO 2015.7.29
		if ( _kind.equals( "condition")) {
			if ( _id.equals( ResourceManager.get_instance().get( "generic.rule.condition.numeric.numeric.comparison.id")))
				return get_script_on_numeric_numeric_comparison( row, initialValueMap, demo, role);
			else if ( _id.equals( ResourceManager.get_instance().get( "generic.rule.condition.numeric.expression.comparison.id")))
				return get_script_on_numeric_expression_comparison( row, initialValueMap, demo, role);
			else if ( _id.equals( ResourceManager.get_instance().get( "generic.rule.condition.expression.expression.comparison.id")))
				return get_script_on_expression_expression_comparison( row, initialValueMap, demo, role);
		} else if ( _kind.equals( "command")) {
			if ( _id.equals( ResourceManager.get_instance().get( "generic.rule.command.numeric.expression.substitution.id")))
				return get_script_on_numeric_expression_substitution( row, initialValueMap, demo, role);
		}

		if ( _system) {
			String text = _subject.get_script( initialValueMap, role, true);
			for ( IObject object:_objects) {
				text += ( text.equals( "") ? "" : "=");
				text += object.get_script( initialValueMap, _subject, role);
			}

			return ( ( _denial ? "!" : "") + get_method( role) + " " + text);
		} else {
			String text = "<" + Constant._userRuleSpotName + ">setEquip " + Constant._userRuleArgumentsListName + "=java.util.LinkedList" + get_separator();
			text += "<" + Constant._userRuleSpotName + ">logEquip " + Constant._userRuleArgumentsListName + get_separator();
			text += "<" + Constant._userRuleSpotName + ">" + ( ( role instanceof AgentRole) ? "addLastAgent" : "addLastSpot") + " " + Constant._userRuleArgumentsListName + get_separator();
			text += "<" + Constant._userRuleSpotName + ">addLastString " + Constant._userRuleArgumentsListName + "=" + get_method( role) + get_separator();
			text += "<" + Constant._userRuleSpotName + ">addLastString " + Constant._userRuleArgumentsListName + "=" + _subject.get_script( initialValueMap, role, true) + get_separator();
			for ( IObject object:_objects)
				text += "<" + Constant._userRuleSpotName + ">addLastString " + Constant._userRuleArgumentsListName + "=" + object.get_script( initialValueMap, _subject, role) + get_separator();

			text += "<" + Constant._userRuleSpotName + ">addParam " + Constant._userRuleClassVariableName + "=" + Constant._userRuleArgumentsListName + "=java.util.LinkedList" + get_separator();
			text += "<" + Constant._userRuleSpotName + ">invokeClass " + Constant._userRuleClassVariableName + "=";

			if ( _kind.equals( "condition"))
				text += Constant._userRuleConditionMethodName;
			else if ( _kind.equals( "command"))
				text += Constant._userRuleCommandMethodName;

			return ( ( _denial ? "!" : "") + text);
		}
	}

	/**
	 * @return
	 */
	private String get_separator() {
		// TODO 2015.7.29
		return _kind.equals( "condition") ? " , " : " ; ";
	}

	/**
	 * @param row
	 * @param initialValueMap
	 * @param demo
	 * @param role
	 * @return
	 */
	private String get_script_on_numeric_numeric_comparison(int row, InitialValueMap initialValueMap, boolean demo, Role role) {
		// TODO 2014.2.17
		if ( 3 != _objects.size())
			return "";

		String[] operatorScripts = ( String[])_operatorScriptMap.get( _objects.get( 1).get_script( initialValueMap, _subject, role));
		if ( null == operatorScripts || 2 != operatorScripts.length)
			return "";

		// 数値変数または数値を数式スポットの数値変数へ代入
		String script = ResourceManager.get_instance().get( "generic.rule.command.set.variable.command") + " " + Constant._expressionSpotName + "." + Constant._expressionResultNemericVariable1 + "=" + _objects.get( 0).get_script( initialValueMap, _subject, role) + " , ";

		// 数値変数または数値を数式スポットの数値変数へ代入
		script += ResourceManager.get_instance().get( "generic.rule.command.set.variable.command") + " " + Constant._expressionSpotName + "." + Constant._expressionResultNemericVariable2 + "=" + _objects.get( 2).get_script( initialValueMap, _subject, role) + " , ";

		// 数式スポットの数値変数を比較
		script += operatorScripts[ 0] + get_prefix() + Constant._expressionResultNemericVariable1  + operatorScripts[ 1] + Constant._expressionResultNemericVariable2;

		return script;
	}

	/**
	 * @param row
	 * @param initialValueMap
	 * @param demo
	 * @param role
	 * @return
	 */
	private String get_script_on_numeric_expression_comparison(int row, InitialValueMap initialValueMap, boolean demo, Role role) {
		// TODO 2014.2.17
		if ( 3 != _objects.size())
			return "";

		String[] operatorScripts = ( String[])_operatorScriptMap.get( _objects.get( 1).get_script( initialValueMap, _subject, role));
		if ( null == operatorScripts || 2 != operatorScripts.length)
			return "";

		// 数値変数または数値を数式スポットの数値変数へ代入
		String script = ResourceManager.get_instance().get( "generic.rule.command.set.variable.command") + " " + Constant._expressionSpotName + "." + Constant._expressionResultNemericVariable1 + "=" + _objects.get( 0).get_script( initialValueMap, _subject, role) + " , ";

		// 数式の各引数(数値変数または数値)を数式スポットの数値変数へ代入
		script += _objects.get( 2).get_script_to_set_variables_into_expression_spot( initialValueMap, _subject, role, " , ");

		// 数式の計算を行って、結果を数式スポットの数値変数へ代入
		script += get_prefix() + Constant._expressionResultNemericVariable2 + "=" + _objects.get( 2).get_script( initialValueMap, _subject, role) + " , ";

		// 数式スポットの数値変数を比較
		script += operatorScripts[ 0] + get_prefix() + Constant._expressionResultNemericVariable1  + operatorScripts[ 1] + Constant._expressionResultNemericVariable2;

		return script;
	}

	/**
	 * @param row
	 * @param initialValueMap
	 * @param demo
	 * @param role
	 * @return
	 */
	private String get_script_on_expression_expression_comparison(int row, InitialValueMap initialValueMap, boolean demo, Role role) {
		// TODO 2014.2.17
		if ( 3 != _objects.size())
			return "";

		String[] operatorScripts = ( String[])_operatorScriptMap.get( _objects.get( 1).get_script( initialValueMap, _subject, role));
		if ( null == operatorScripts || 2 != operatorScripts.length)
			return "";

		// 数式の各引数(数値変数または数値)を数式スポットの数値変数へ代入
		String script = _objects.get( 0).get_script_to_set_variables_into_expression_spot( initialValueMap, _subject, role, " , ");

		// 数式の計算を行って、結果を数式スポットの数値変数へ代入
		script += get_prefix() + Constant._expressionResultNemericVariable1 + "=" + _objects.get( 0).get_script( initialValueMap, _subject, role) + " , ";

		// 数式の各引数(数値変数または数値)を数式スポットの数値変数へ代入
		script += _objects.get( 2).get_script_to_set_variables_into_expression_spot( initialValueMap, _subject, role, " , ");

		// 数式の計算を行って、結果を数式スポットの数値変数へ代入
		script += get_prefix() + Constant._expressionResultNemericVariable2 + "=" + _objects.get( 2).get_script( initialValueMap, _subject, role) + " , ";

		// 数式スポットの数値変数を比較
		script += operatorScripts[ 0] + get_prefix() + Constant._expressionResultNemericVariable1  + operatorScripts[ 1] + Constant._expressionResultNemericVariable2;

		return script;
	}

	/**
	 * @param row
	 * @param initialValueMap
	 * @param demo
	 * @param role
	 * @return
	 */
	private String get_script_on_numeric_expression_substitution(int row, InitialValueMap initialValueMap, boolean demo, Role role) {
		// TODO 2014.2.17
		if ( 2 != _objects.size())
			return "";

		// 数式の各引数(数値変数または数値)を数式スポットの数値変数へ代入
		String script = _objects.get( 1).get_script_to_set_variables_into_expression_spot( initialValueMap, _subject, role, " ; ");

		// 数式の計算を行って、結果を数式スポットの数値変数へ代入
		script += get_prefix() + Constant._expressionResultNemericVariable1 + "=" + _objects.get( 1).get_script( initialValueMap, _subject, role) + " ; ";

		// 計算結果を指定された数値変数へ代入
		script += ResourceManager.get_instance().get( "generic.rule.command.set.variable.command") + " " + _objects.get( 0).get_script( initialValueMap, _subject, role) + "=" + Constant._expressionSpotName + "." + Constant._expressionResultNemericVariable1;

		return script;
	}

	/**
	 * @return
	 */
	private String get_prefix() {
		// TODO 2014.2.7
		return ( "<" + Constant._expressionSpotName + ">askEquip " );
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.role.base.object.base.Rule#get_cell_text(soars.application.visualshell.object.role.base.Role)
	 */
	@Override
	public String get_cell_text(Role role) {
		// TODO 2015.7.30
		if ( _kind.equals( "condition")) {
			if ( _id.equals( ResourceManager.get_instance().get( "generic.rule.condition.numeric.numeric.comparison.id"))
				|| _id.equals( ResourceManager.get_instance().get( "generic.rule.condition.numeric.expression.comparison.id"))
				||_id.equals( ResourceManager.get_instance().get( "generic.rule.condition.expression.expression.comparison.id")))
				return get_cell_text( " ", "", role);
		}

		if ( _kind.equals( "command")) {
			if ( _id.equals( ResourceManager.get_instance().get( "generic.rule.command.numeric.expression.substitution.id")))
				return get_cell_text( "=", "", role);
		}

		if ( _system)
			return get_cell_text( "=", get_method( role), role);
		else {
			String[] words = Tool.split( get_method( role), ':');
			if ( 2 != words.length)
				return "Unknown";

			return get_cell_text( "=", words[ 1], role);
		}
	}

	/**
	 * @param separator
	 * @param method
	 * @param role
	 * @return
	 */
	private String get_cell_text(String separator, String method, Role role) {
		// TODO 2014.2.17
		String text = _subject.get_cell_text( role, true);
		for ( IObject object:_objects) {
			text += ( text.equals( "") ? "" : separator);
			text += object.get_cell_text( _subject, role);
		}
		return ( ( _or ? " || " : "") + get_name( role) + " : " + ( _denial ? "!" : "") + method + " " + text);
	}

	/**
	 * @param role
	 * @return
	 */
	protected String get_name(Role role) {
		return _kind.equals( "condition") ? ConditionTabbedPane.get_name( this, role) : CommandTabbedPane.get_name( this, role);
	}

	/**
	 * @param role
	 * @return
	 */
	protected String get_method(Role role) {
		return _kind.equals( "condition") ? ConditionTabbedPane.get_method( this, role) : CommandTabbedPane.get_method( this, role);
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.role.base.object.base.Rule#write(soars.common.utility.xml.sax.Writer)
	 */
	@Override
	public boolean write(Writer writer) throws SAXException {
		AttributesImpl attributesImpl = new AttributesImpl();
		attributesImpl.addAttribute( null, null, "column", "", String.valueOf( _column));
		attributesImpl.addAttribute( null, null, "type", "", Writer.escapeAttributeCharData( _type));
		attributesImpl.addAttribute( null, null, "or", "", _or ? "true" : "false");
		attributesImpl.addAttribute( null, null, "value", "", Writer.escapeAttributeCharData( _value));
		attributesImpl.addAttribute( null, null, "id", "", Writer.escapeAttributeCharData( _id));
		attributesImpl.addAttribute( null, null, "denial", "", _denial ? "true" : "false");
		attributesImpl.addAttribute( null, null, "system", "", _system ? "true" : "false");
		attributesImpl.addAttribute( null, null, "generic", "", "true");

		writer.startElement( null, null, _kind, attributesImpl);

		if ( !_subject.write( "subject", writer))
			return false;

		for ( IObject object:_objects) {
			if ( !object.write( writer))
				return false;
		}

		writer.endElement( null, null, _kind);

		return true;
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.role.base.object.base.Rule#print()
	 */
	@Override
	public void print() {
		System.out.println( _kind + " : " + _type + " : " + _id + " : " + _denial + " : " + _system);
		_subject.print();
		for ( IObject object:_objects)
			object.print();
	}
}
