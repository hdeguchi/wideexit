/**
 * 
 */
package soars.application.visualshell.object.role.base.object.generic.element;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
import soars.common.utility.tool.expression.Expression;
import soars.common.utility.tool.expression.ExpressionManipulator;
import soars.common.utility.xml.sax.Writer;

/**
 * @author kurata
 *
 */
public class ExpressionRule implements IObject {

	/**
	 * 
	 */
	public String _name = "";

	/**
	 * 
	 */
	public List<EntityVariableRule> _entityVariableRules = new ArrayList<EntityVariableRule>();

	/**
	 * 
	 */
	public Role _role = null;

	/**
	 * @param role 
	 * @return
	 */
	public static EntityVariableRule get_default_entityVariableRule(Role role) {
		// TODO Auto-generated method stub
		EntityVariableRule entityVariableRule = new EntityVariableRule();
		entityVariableRule._entity = ( role instanceof AgentRole) ? "self" : "currentspot";
		entityVariableRule._variableType = "immediate real number";
		entityVariableRule._variableValue = "0.0";
		return entityVariableRule;
	}

	/**
	 * @param name
	 * @param role
	 */
	public ExpressionRule(String name, Role role) {
		super();
		_name = name;
		_role = role;
	}

	/**
	 * @param expressionRule
	 */
	public ExpressionRule(ExpressionRule expressionRule) {
		super();
		copy( expressionRule);
	}

	/**
	 * @param expressionRule
	 */
	private void copy(ExpressionRule expressionRule) {
		_name = expressionRule._name;
		_role = expressionRule._role;
		for ( EntityVariableRule entityVariableRule:expressionRule._entityVariableRules)
			_entityVariableRules.add( new EntityVariableRule( entityVariableRule));
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.role.base.object.generic.element.IObject#same_as(soars.application.visualshell.object.role.base.object.generic.element.IObject)
	 */
	@Override
	public boolean same_as(IObject object) {
		if ( !( object instanceof ExpressionRule))
			return false;

		ExpressionRule expressionRule = ( ExpressionRule)object;
		if ( !_name.equals( expressionRule._name))
			return false;

		for ( int i = 0; i < _entityVariableRules.size(); ++i) {
			if ( !_entityVariableRules.get( i).same_as( expressionRule._entityVariableRules.get( i)))
				return false;
		}

		return true;
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.role.base.object.generic.element.IObject#get_initial_values(java.util.Vector)
	 */
	@Override
	public void get_initial_values(Vector<String> initialValues) {
		for ( EntityVariableRule entityVariableRule:_entityVariableRules)
			entityVariableRule.get_initial_values( initialValues);
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.role.base.object.generic.element.IObject#has_same_agent_name(java.lang.String, java.lang.String)
	 */
	@Override
	public boolean has_same_agent_name(String name, String number) {
		for ( EntityVariableRule entityVariableRule:_entityVariableRules) {
			if ( entityVariableRule.has_same_agent_name( name, number))
				return true;
		}
		return false;
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.role.base.object.generic.element.IObject#contains_this_alias(java.lang.String)
	 */
	@Override
	public boolean contains_this_alias(String alias) {
		for ( EntityVariableRule entityVariableRule:_entityVariableRules) {
			if ( entityVariableRule.contains_this_alias( alias))
				return true;
		}
		return false;
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.role.base.object.generic.element.IObject#get_used_agent_names(java.util.List)
	 */
	@Override
	public void get_used_agent_names(List<String> names) {
		for ( EntityVariableRule entityVariableRule:_entityVariableRules)
			entityVariableRule.get_used_agent_names( names);
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.role.base.object.generic.element.IObject#get_used_spot_names(java.util.List)
	 */
	@Override
	public void get_used_spot_names(List<String> names) {
		for ( EntityVariableRule entityVariableRule:_entityVariableRules)
			entityVariableRule.get_used_spot_names( names);
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.role.base.object.generic.element.IObject#get_used_independent_variable_names(java.lang.String, java.util.List)
	 */
	@Override
	public void get_used_independent_variable_names(String type, List<String> names) {
		for ( EntityVariableRule entityVariableRule:_entityVariableRules)
			entityVariableRule.get_used_independent_variable_names( type, names);
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.role.base.object.generic.element.IObject#get_used_agent_variable_names(java.util.List)
	 */
	@Override
	public void get_used_agent_variable_names(List<String> names) {
		for ( EntityVariableRule entityVariableRule:_entityVariableRules)
			entityVariableRule.get_used_agent_variable_names( names);
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.role.base.object.generic.element.IObject#get_used_agent_variable_names(java.util.List, soars.application.visualshell.object.role.base.object.generic.EntityVariableRule)
	 */
	@Override
	public void get_used_agent_variable_names(List<String> names, EntityVariableRule subject) {
		for ( EntityVariableRule entityVariableRule:_entityVariableRules)
			entityVariableRule.get_used_agent_variable_names( names, subject);
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.role.base.object.generic.element.IObject#get_used_spot_variable_names(java.util.List)
	 */
	@Override
	public void get_used_spot_variable_names(List<String> names) {
		for ( EntityVariableRule entityVariableRule:_entityVariableRules)
			entityVariableRule.get_used_spot_variable_names( names);
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.role.base.object.generic.element.IObject#get_used_spot_variable_names(java.util.List, soars.application.visualshell.object.role.base.object.generic.EntityVariableRule)
	 */
	@Override
	public void get_used_spot_variable_names(List<String> names, EntityVariableRule subject) {
		for ( EntityVariableRule entityVariableRule:_entityVariableRules)
			entityVariableRule.get_used_spot_variable_names( names, subject);
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.role.base.object.generic.element.IObject#get_used_variable_names(java.lang.String, java.util.List)
	 */
	@Override
	public void get_used_variable_names(String type, List<String> names) {
		for ( EntityVariableRule entityVariableRule:_entityVariableRules)
			entityVariableRule.get_used_variable_names( type, names);
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.role.base.object.generic.element.IObject#get_used_variable_names(java.lang.String, java.util.List, soars.application.visualshell.object.role.base.object.generic.EntityVariableRule)
	 */
	@Override
	public void get_used_variable_names(String type, List<String> names, EntityVariableRule subject) {
		for ( EntityVariableRule entityVariableRule:_entityVariableRules)
			entityVariableRule.get_used_variable_names( type, names, subject);
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.role.base.object.generic.element.IObject#get_used_expressions(java.util.List)
	 */
	@Override
	public void get_used_expressions(List<String> names) {
		// TODO Auto-generated method stub
		if ( names.contains( _name))
			return;

		names.add( _name);
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.role.base.object.generic.element.IObject#update_agent_or_spot_name_and_number(java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.util.Vector, java.lang.String, java.util.Vector)
	 */
	@Override
	public boolean update_agent_or_spot_name_and_number(String type, String newName, String originalName, String headName, Vector<String[]> ranges, String newHeadName, Vector<String[]> newRanges) {
		boolean result = false;
		for ( EntityVariableRule entityVariableRule:_entityVariableRules) {
			if ( entityVariableRule.update_agent_or_spot_name_and_number( type, newName, originalName, headName, ranges, newHeadName, newRanges))
				result = true;
		}
		return result;
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.role.base.object.generic.element.IObject#update_role_name(java.lang.String, java.lang.String)
	 */
	@Override
	public boolean update_role_name(String originalName, String newName) {
		boolean result = false;
		for ( EntityVariableRule entityVariableRule:_entityVariableRules) {
			if ( entityVariableRule.update_role_name( originalName, newName))
				result = true;
		}
		return result;
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.role.base.object.generic.element.IObject#update_independent_variable_name(java.lang.String, java.lang.String, java.lang.String)
	 */
	@Override
	public boolean update_independent_variable_name(String type, String originalName, String newName) {
		boolean result = false;
		for ( EntityVariableRule entityVariableRule:_entityVariableRules) {
			if ( entityVariableRule.update_independent_variable_name( type, originalName, newName))
				result = true;
		}
		return result;
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.role.base.object.generic.element.IObject#update_agent_variable_name(java.lang.String, java.lang.String, java.lang.String)
	 */
	@Override
	public boolean update_agent_variable_name(String originalName, String newName, String entityType) {
		boolean result = false;
		for ( EntityVariableRule entityVariableRule:_entityVariableRules) {
			if ( entityVariableRule.update_agent_variable_name( originalName, newName, entityType))
				result = true;
		}
		return result;
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.role.base.object.generic.element.IObject#update_agent_variable_name(java.lang.String, java.lang.String, java.lang.String, soars.application.visualshell.object.role.base.object.generic.EntityVariableRule)
	 */
	@Override
	public boolean update_agent_variable_name(String originalName, String newName, String entityType, EntityVariableRule subject) {
		boolean result = false;
		for ( EntityVariableRule entityVariableRule:_entityVariableRules) {
			if ( entityVariableRule.update_agent_variable_name( originalName, newName, entityType, subject))
				result = true;
		}
		return result;
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.role.base.object.generic.element.IObject#update_spot_variable_name(java.lang.String, java.lang.String, java.lang.String)
	 */
	@Override
	public boolean update_spot_variable_name(String originalName, String newName, String entityType) {
		boolean result = false;
		for ( EntityVariableRule entityVariableRule:_entityVariableRules) {
			if ( entityVariableRule.update_spot_variable_name( originalName, newName, entityType))
				result = true;
		}
		return result;
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.role.base.object.generic.element.IObject#update_spot_variable_name(java.lang.String, java.lang.String, java.lang.String, soars.application.visualshell.object.role.base.object.generic.EntityVariableRule)
	 */
	@Override
	public boolean update_spot_variable_name(String originalName, String newName, String entityType, EntityVariableRule subject) {
		boolean result = false;
		for ( EntityVariableRule entityVariableRule:_entityVariableRules) {
			if ( entityVariableRule.update_spot_variable_name( originalName, newName, entityType, subject))
				result = true;
		}
		return result;
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.role.base.object.generic.element.IObject#update_variable_name(java.lang.String, java.lang.String, java.lang.String, java.lang.String)
	 */
	@Override
	public boolean update_variable_name(String type, String originalName, String newName, String entityType) {
		boolean result = false;
		for ( EntityVariableRule entityVariableRule:_entityVariableRules) {
			if ( entityVariableRule.update_variable_name( type, originalName, newName, entityType))
				result = true;
		}
		return result;
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.role.base.object.generic.element.IObject#update_variable_name(java.lang.String, java.lang.String, java.lang.String, java.lang.String, soars.application.visualshell.object.role.base.object.generic.EntityVariableRule)
	 */
	@Override
	public boolean update_variable_name(String type, String originalName, String newName, String entityType, EntityVariableRule subject) {
		boolean result = false;
		for ( EntityVariableRule entityVariableRule:_entityVariableRules) {
			if ( entityVariableRule.update_variable_name( type, originalName, newName, entityType, subject))
				result = true;
		}
		return result;
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.role.base.object.generic.element.IObject#update_expression(soars.common.utility.tool.expression.Expression, int, soars.common.utility.tool.expression.Expression)
	 */
	@Override
	public boolean update_expression(Expression newExpression, int newVariableCount, Expression originalExpression) {
		// TODO 編集により数式が変更された時に呼ばれる
		if ( !_name.equals( originalExpression._value[ 0]))
			return false;

		_name = newExpression._value[ 0];

		while ( newVariableCount < _entityVariableRules.size())
			_entityVariableRules.remove( _entityVariableRules.size() - 1);

		while ( newVariableCount > _entityVariableRules.size()) {
			if ( _entityVariableRules.isEmpty())
				_entityVariableRules.add( new EntityVariableRule( get_default_entityVariableRule( _role)));
			else
				_entityVariableRules.add( new EntityVariableRule( _entityVariableRules.get( _entityVariableRules.size() - 1)));
		}

		return true;
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.role.base.object.generic.element.IObject#can_paste(soars.application.visualshell.layer.Layer)
	 */
	@Override
	public boolean can_paste(Layer drawObjects) {
		// TODO 数式の貼り付けについてここで考慮する必要は無い
		// 数式名は変えられるが引数の数は変えられない
		// 数式名が帰られた場合は、この後 update_function( ... )が呼ばれるので整合性は保たれる
		for ( EntityVariableRule entityVariableRule:_entityVariableRules) {
			if ( !entityVariableRule.can_paste( drawObjects))
				return false;
		}

		return true;
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.role.base.object.generic.element.IObject#can_paste(soars.application.visualshell.layer.Layer, soars.application.visualshell.object.role.base.object.generic.EntityVariableRule)
	 */
	@Override
	public boolean can_paste(Layer drawObjects, EntityVariableRule subject) {
		// TODO 数式の貼り付けについてここで考慮する必要は無い
		// 数式名は変えられるが引数の数は変えられない
		// 数式名が帰られた場合は、この後 update_function( ... )が呼ばれるので整合性は保たれる
		for ( EntityVariableRule entityVariableRule:_entityVariableRules) {
			if ( !entityVariableRule.can_paste( drawObjects, subject))
				return false;
		}
		return true;
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.role.base.object.generic.element.IObject#update_function(java.lang.String, java.lang.String)
	 */
	@Override
	public boolean update_function(String originalFunctionName, String newFunctionName) {
		// TODO オブジェクトの貼り付け時に、数式名が重複していた為に数式名が変更された時に呼ばれる
		// 数式名が変えられても、引数の数は変えられないから名前を更新するだけで整合性が保たれる
		if ( !_name.equals( originalFunctionName))
			return false;

		_name = newFunctionName;
		return true;
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.role.base.object.generic.element.IObject#update_expression(soars.application.visualshell.object.expression.VisualShellExpressionManager)
	 */
	@Override
	public boolean update_expression(VisualShellExpressionManager visualShellExpressionManager) {
		// TODO バージョン5以上ではルールが引数名に依存したデータ["f(x)"や"x=numeric1"のような文字列]を保持していないので不要
		return false;
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.role.base.object.generic.element.IObject#is_number_object_type_correct(java.lang.String, java.lang.String, java.lang.String)
	 */
	@Override
	public boolean is_number_object_type_correct(String entityType, String numberObjectName, String numberObjectNewType) {
		boolean result = true;
		for ( EntityVariableRule entityVariableRule:_entityVariableRules) {
			if ( !entityVariableRule.is_number_object_type_correct( entityType, numberObjectName, numberObjectNewType))
				result = false;
		}
		return result;
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.role.base.object.generic.element.IObject#is_number_object_type_correct(java.lang.String, java.lang.String, java.lang.String, soars.application.visualshell.object.role.base.object.generic.EntityVariableRule)
	 */
	@Override
	public boolean is_number_object_type_correct(String entityType, String numberObjectName, String numberObjectNewType, EntityVariableRule subject) {
		boolean result = true;
		for ( EntityVariableRule entityVariableRule:_entityVariableRules) {
			if ( !entityVariableRule.is_number_object_type_correct( entityType, numberObjectName, numberObjectNewType, subject))
				result = false;
		}
		return result;
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.role.base.object.generic.element.IObject#update_stage_manager()
	 */
	@Override
	public boolean update_stage_manager() {
		boolean result = false;
		for ( EntityVariableRule entityVariableRule:_entityVariableRules) {
			if ( entityVariableRule.update_stage_manager())
				result = true;
		}
		return result;
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.role.base.object.generic.element.IObject#get_script(soars.application.visualshell.object.experiment.InitialValueMap, soars.application.visualshell.object.role.base.Role, boolean)
	 */
	@Override
	public String get_script(InitialValueMap initialValueMap, Role role, boolean isSubject) {
		// TODO 2014.2.7
		return get_expression_script();
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.role.base.object.generic.element.IObject#get_script(soars.application.visualshell.object.experiment.InitialValueMap, soars.application.visualshell.object.role.base.object.generic.EntityVariableRule, soars.application.visualshell.object.role.base.Role)
	 */
	@Override
	public String get_script(InitialValueMap initialValueMap, EntityVariableRule subject, Role role) {
		// TODO 2014.2.7
		return get_expression_script();
	}

	/**
	 * @return
	 */
	private String get_expression_script() {
		// TODO 2014.2.7
		Expression expression = VisualShellExpressionManager.get_instance().get( _name);
		if ( null == expression)
			return "";

		//System.out.println( expression);

		String expandedExpression = VisualShellExpressionManager.get_instance().expand( expression._value[ 2]);
		if ( null == expandedExpression)
			return "";

		//System.out.println( expanded_expression);

		Vector<String> variables = expression.get_variables();

		if ( variables.size() != _entityVariableRules.size())
			return "";

		ExpressionManipulator expressionManipulator = new ExpressionManipulator();
		if ( !expressionManipulator.parse(
			expandedExpression,
			VisualShellExpressionManager.get_instance().get_all_functions(),
			VisualShellExpressionManager._constants,
			variables))
			return "";

		//System.out.println( expressionManipulator.get());

		String script = VisualShellExpressionManager.get_instance().get( expandedExpression, variables, ",", ")(");
		if ( null == script)
			return "";

		//System.out.println( script);

		if ( !expressionManipulator.parse(
			script,
			VisualShellExpressionManager.get_instance().get_all_functions(),
			VisualShellExpressionManager._constants,
			variables))
			return "";

		//System.out.println( expressionManipulator.get());

		Map<String, String> updateVariableMap = new HashMap<String, String>();
		updateVariableMap.put( "PI", "PI()");
		updateVariableMap.put( "E", "E()");
		for ( int i = 0; i < variables.size(); ++i)
			updateVariableMap.put( variables.get( i), Constant._expressionNemericVariablePrefix + String.valueOf( i));

		expressionManipulator.update_variable( updateVariableMap);

		//System.out.println( expressionManipulator.get());
		//System.out.println( "");

		return expressionManipulator.get();
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.role.base.object.generic.element.IObject#get_script_to_set_variables_into_expression_spot(soars.application.visualshell.object.experiment.InitialValueMap, soars.application.visualshell.object.role.base.object.generic.EntityVariableRule, soars.application.visualshell.object.role.base.Role, java.lang.String)
	 */
	@Override
	public String get_script_to_set_variables_into_expression_spot(InitialValueMap initialValueMap, EntityVariableRule subject, Role role, String separator) {
		// TODO 2014.2.10
		String script = "";
		for ( int i = 0; i < _entityVariableRules.size(); ++i)
			script += ResourceManager.get_instance().get( "generic.rule.command.set.variable.command") + " " + Constant._expressionSpotName + "." + Constant._expressionNemericVariablePrefix + String.valueOf( i) + "=" + _entityVariableRules.get( i).get_script( initialValueMap, subject, role) + separator;
		return script;
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.role.base.object.generic.element.IObject#get_cell_text(soars.application.visualshell.object.role.base.Role, boolean)
	 */
	@Override
	public String get_cell_text(Role role, boolean isSubject) {
		String variables = "";
		for ( EntityVariableRule entityVariableRule:_entityVariableRules) {
			variables += ( variables.equals( "") ? "" : ", ");
			variables += entityVariableRule.get_cell_text( role, isSubject);
		}
		return _name + "(" + variables + ")";
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.role.base.object.generic.element.IObject#get_cell_text(soars.application.visualshell.object.role.base.object.generic.EntityVariableRule, soars.application.visualshell.object.role.base.Role)
	 */
	@Override
	public String get_cell_text(EntityVariableRule subject, Role role) {
		String variables = "";
		for ( EntityVariableRule entityVariableRule:_entityVariableRules) {
			variables += ( variables.equals( "") ? "" : ", ");
			variables += entityVariableRule.get_cell_text( subject, role);
		}
		return _name + "(" + variables + ")";
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.role.base.object.generic.element.IObject#write(soars.common.utility.xml.sax.Writer)
	 */
	@Override
	public boolean write(Writer writer) throws SAXException {
		return write( "expression", writer);
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.role.base.object.generic.element.IObject#write(java.lang.String, soars.common.utility.xml.sax.Writer)
	 */
	@Override
	public boolean write(String name, Writer writer) throws SAXException {
		AttributesImpl attributesImpl = new AttributesImpl();
		attributesImpl.addAttribute( null, null, "name", "", Writer.escapeAttributeCharData( _name));

		writer.startElement( null, null, name, attributesImpl);

		for ( EntityVariableRule entityVariableRule:_entityVariableRules) {
			if ( !entityVariableRule.write( writer))
				return false;
		}

		writer.endElement( null, null, name);

		return true;
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.role.base.object.generic.element.IObject#print()
	 */
	@Override
	public void print() {
		System.out.println( "Name=" + _name);
		for ( EntityVariableRule entityVariableRule:_entityVariableRules)
			entityVariableRule.print();
	}
}
