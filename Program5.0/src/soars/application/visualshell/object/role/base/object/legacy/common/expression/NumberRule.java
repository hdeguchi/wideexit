/**
 * 
 */
package soars.application.visualshell.object.role.base.object.legacy.common.expression;

import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

import soars.application.visualshell.common.tool.CommonTool;
import soars.application.visualshell.layer.Layer;
import soars.application.visualshell.main.Constant;
import soars.application.visualshell.object.expression.VisualShellExpressionManager;
import soars.application.visualshell.object.role.base.Role;
import soars.application.visualshell.object.role.base.object.base.Rule;
import soars.application.visualshell.object.role.base.object.common.CommonRuleManipulator;
import soars.application.visualshell.object.role.base.object.common.CommonRuleManipulatorNew;
import soars.application.visualshell.object.role.base.object.generic.GenericRule;
import soars.application.visualshell.object.role.base.object.generic.element.ConstantRule;
import soars.application.visualshell.object.role.base.object.generic.element.EntityVariableRule;
import soars.application.visualshell.object.role.base.object.generic.element.ExpressionRule;
import soars.common.utility.tool.expression.Expression;
import soars.common.utility.tool.expression.ExpressionManipulator;

/**
 * @author kurata
 *
 */
public class NumberRule extends Rule {

	/**
	 * 
	 */
	static private Map<String, String[]> _operatorScriptMap = null;

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
	 * @param value
	 * @return
	 */
	public static ExpressionElements get2(String value) {
		String[] elements = value.split( " ");
		if ( null == elements || 1 > elements.length)
			return null;

		ExpressionElements expressionElements = new ExpressionElements();

		expressionElements._function = elements[ 0];

		for ( int i = 1; i < elements.length; ++i) {
			String[] words = elements[ i].split( "=");
			if ( null == words || 2 != words.length)
				return null;

			expressionElements._variables.add( words);
		}

		return expressionElements;
	}

	/**
	 * @param function
	 * @param variables
	 * @return
	 */
	public static String get(String function, Vector<String[]> variables) {
		String value = function;

		for ( int i = 0; i < variables.size(); ++i) {
			value += " ";
			String[] words = variables.get( i);
			value += ( words[ 0] + "=" + words[ 1]);
		}

		return value;
	}

	/**
	 * @param kind
	 * @param type
	 * @param value
	 */
	public NumberRule(String kind, String type, String value) {
		super(kind, type, value);
	}

	/**
	 * @param expressionElements
	 * @return
	 */
	public String get(ExpressionElements expressionElements) {
		String value = expressionElements._function;

		for ( int i = 0; i < expressionElements._variables.size(); ++i) {
			value += " ";
			String[] words = expressionElements._variables.get( i);
			value += ( words[ 0] + "=" + words[ 1]);
		}

		return value;
	}

	/**
	 * @param expressionElements
	 * @param visualShellExpressionManager
	 * @return
	 */
	protected ExpressionElements update_expression(ExpressionElements expressionElements, VisualShellExpressionManager visualShellExpressionManager) {
		String function = expressionElements.get_function();
		if ( null == function || function.equals( ""))
			return null;

		Expression expression = visualShellExpressionManager.get( function);
		if ( null == expression)
			return null;

		if ( expressionElements._function.equals( expression.get_function()))
			return null;

		expressionElements._function = expression.get_function();

		String[] arguments = expression.get_arguments();
		if ( null == arguments)
			expressionElements._variables.clear();
		else {
			if ( arguments.length < expressionElements._variables.size())
				expressionElements._variables.setSize( arguments.length);

			for ( int i = 0; i < arguments.length; ++i) {
				if ( expressionElements._variables.size() > i) {
					String[] words = expressionElements._variables.get( i);
					words[ 0] = arguments[ i];
				} else {
					String[] words = new String[ 2];
					words[ 0] = arguments[ i];
					words[ 1] = "0.0";
					expressionElements._variables.add( words);
				}
			}
		}

		return expressionElements;
	}

	/**
	 * @param value
	 * @param length
	 * @param numberObjectNames
	 */
	protected void get_number_object_names(String value, int length, Vector<String> numberObjectNames) {
		String[] elements = value.split( " ");
		if ( null == elements || length > elements.length)
			return;

		numberObjectNames.add( elements[ 0]);
	}

	/**
	 * @param value
	 * @param length
	 * @param indices
	 * @param prefix
	 * @param numberObjectNames
	 */
	protected void get_number_object_names(String value, int length, int[] indices, String prefix, Vector<String> numberObjectNames) {
		String[] elements = value.split( " ");
		if ( null == elements || length > elements.length)
			return;

		for ( int i = 0; i < indices.length; ++i)
			numberObjectNames.add( prefix + elements[ indices[ i]]);
	}

	/**
	 * @param expressionElements
	 * @param prefix
	 * @param numberObjectNames
	 */
	protected void get_number_object_names(ExpressionElements expressionElements, String prefix, Vector<String> numberObjectNames) {
		for ( int i = 0; i < expressionElements._variables.size(); ++i) {
			String[] words = expressionElements._variables.get( i);
			if ( CommonTool.is_number_correct( words[ 1]))
				continue;

			numberObjectNames.add( prefix + words[ 1]);
		}
	}

	/**
	 * @param prefix
	 * @param value
	 * @param kind
	 * @param name
	 * @param newName
	 * @param type
	 * @return
	 */
	protected String update_number_object_name2(String prefix, String value, int kind, String name, String newName, String type) {
		switch ( kind) {
			case 1:
				return update_number_object_name21( prefix, value, name, newName, type);
			case 2:
				return update_number_object_name22( prefix, value, name, newName, type);
		}
		return null;
	}

	/**
	 * @param prefix
	 * @param value
	 * @param name
	 * @param newName
	 * @param type
	 * @return
	 */
	protected String update_number_object_name21(String prefix, String value, String name, String newName, String type) {
		if ( value.startsWith( "<"))
			return CommonRuleManipulator.update_object_name( value, name, newName, type);
		else {
			if ( !CommonRuleManipulator.correspond( prefix, value, name, type))
				return null;

			return newName;
		}
	}

	/**
	 * @param prefix
	 * @param value
	 * @param name
	 * @param newName
	 * @param type
	 * @return
	 */
	protected String update_number_object_name22(String prefix, String value, String name, String newName, String type) {
		ExpressionElements expressionElements = get2( value);
		if ( null == expressionElements)
			return null;

		String expression = expressionElements._function;
		boolean result = false;
		for ( int i = 0; i < expressionElements._variables.size(); ++i) {
			String[] words = expressionElements._variables.get( i);
			expression += ( " " + words[ 0] + "=");
			if ( CommonTool.is_number_correct( words[ 1])) {
				expression += words[ 1];
				continue;
			}

			if ( words[ 1].startsWith( "<")) {
				String word = CommonRuleManipulator.update_object_name( words[ 1], name, newName, type);
				if ( null == word)
					expression += words[ 1];
				else {
					expression += newName;
					result = true;
				}
			} else {
				if ( !CommonRuleManipulator.correspond( prefix, words[ 1], name, type))
					expression += words[ 1];
				else {
					expression += newName;
					result = true;
				}
			}
		}

		if ( !result)
			return null;

		return expression;
	}

	/**
	 * @param value
	 * @param length
	 * @param drawObjects
	 * @return
	 */
	protected boolean can_paste_number_object_names(String value, int length, Layer drawObjects) {
		String[] elements = value.split( " ");
		if ( null == elements || length > elements.length)
			return false;

		return CommonRuleManipulator.can_paste_object( "number object", elements[ 0], drawObjects);
	}

	/**
	 * @param value
	 * @param length
	 * @param indices
	 * @param prefix
	 * @param drawObjects
	 * @return
	 */
	protected boolean can_paste_number_object_names(String value, int length, int[] indices, String prefix, Layer drawObjects) {
		String[] elements = value.split( " ");
		if ( null == elements || length > elements.length)
			return false;

		for ( int i = 0; i < indices.length; ++i) {
			if ( !CommonRuleManipulator.can_paste_object( "number object", prefix + elements[ indices[ i]], drawObjects))
				return false;
		}

		return true;
	}

	/**
	 * @param expressionElements
	 * @param prefix
	 * @param drawObjects
	 * @return
	 */
	protected boolean can_paste_number_object_names(ExpressionElements expressionElements, String prefix, Layer drawObjects) {
		for ( int i = 0; i < expressionElements._variables.size(); ++i) {
			String[] words = expressionElements._variables.get( i);
			if ( CommonTool.is_number_correct( words[ 1]))
				continue;

			if ( !CommonRuleManipulator.can_paste_object( "number object", prefix + words[ 1], drawObjects))
				return false;
		}

		return true;
	}

	/**
	 * @param value
	 * @return
	 */
	protected String get_value_script1(String value) {
		String[] elements = value.split( " ");
		if ( null == elements || ( 2 != elements.length && 3 != elements.length))
			return "";

		String[] prefixAndObject = CommonRuleManipulator.get_prefix_and_object( elements[ 0]);

		String[] operatorScripts = ( String[])_operatorScriptMap.get(
			2 == elements.length ? "" : elements[ 1]);
		if ( null == operatorScripts || 2 != operatorScripts.length)
			return "";

		String val = ( 2 == elements.length ? elements[ 1] : elements[ 2]);

		return ( operatorScripts[ 0] + prefixAndObject[ 0] + "askEquip " + prefixAndObject[ 1] + operatorScripts[ 1]
			+ ( val.startsWith( "-") ? "(" + val + ")" : val));
	}

	/**
	 * @param value
	 * @return
	 */
	protected String get_number_object_script1(String value) {
		String[] elements = value.split( " ");
		if ( null == elements || ( 2 != elements.length && 3 != elements.length))
			return "";

		String[] prefixAndObject = CommonRuleManipulator.get_prefix_and_object( elements[ 0]);

		String[] operatorScripts = ( String[])_operatorScriptMap.get(
			2 == elements.length ? "" : elements[ 1]);
		if ( null == operatorScripts || 2 != operatorScripts.length)
			return "";

		String numberObject = get_number_object_script2(
			( 2 == elements.length ? elements[ 1] : elements[ 2]));

		return ( operatorScripts[ 0] + prefixAndObject[ 0] + "askEquip " + prefixAndObject[ 1] + operatorScripts[ 1] + numberObject);
	}

	/**
	 * @param element
	 * @return
	 */
	protected String get_number_object_script2(String element) {
		String[] prefixAndObject = CommonRuleManipulator.get_prefix_and_object( element);
		return prefixAndObject[ 1];
	}

	/**
	 * @param expressionElements
	 * @param operator
	 * @param numberObject
	 * @param bracket
	 * @return
	 */
	protected String get_expression_script1(ExpressionElements expressionElements, String numberObject, String operator, boolean bracket) {
		String expression = get_expression_script2( expressionElements);
		if ( expression.equals( ""))
			return "";

		if ( bracket)
			expression = ( "(" + expression + ")");

		String prefixSpotName = get_prefix_spot_name( numberObject);
		if ( null == prefixSpotName)
			return "";

		String[] words = ( String[])numberObject.split( ">");
		if ( null == words)
			return "";

		String numberObjectScript; 
		if ( 1 == words.length)
			numberObjectScript = words[ 0];
		else if ( 2 == words.length)
			numberObjectScript = words[ 1];
		else
			return "";

		String[] operatorScripts = ( String[])_operatorScriptMap.get( operator);
		if ( null == operatorScripts || 2 != operatorScripts.length)
			return "";

		return ( operatorScripts[ 0] + prefixSpotName + "askEquip " + numberObjectScript + operatorScripts[ 1] + expression);
	}

	/**
	 * @param expressionElements
	 * @return
	 */
	protected String get_expression_script2(ExpressionElements expressionElements) {
		String expression = VisualShellExpressionManager.get_instance().get_expression( expressionElements._function);
		if ( expression.equals( ""))
			return "";

		//System.out.println( expression);	//

		String expandedExpression = VisualShellExpressionManager.get_instance().expand( expression);
		if ( null == expandedExpression)
			return "";

		//System.out.println( expanded_expression);	//

		Vector<String> variables = new Vector<String>();
		for ( int i = 0; i < expressionElements._variables.size(); ++i) {
			String[] words = expressionElements._variables.get( i);
			variables.add( words[ 0]);
		}

		ExpressionManipulator expressionManipulator = new ExpressionManipulator();
		if ( !expressionManipulator.parse(
			expandedExpression,
			VisualShellExpressionManager.get_instance().get_all_functions(),
			VisualShellExpressionManager._constants,
			variables))
			return "";

		//System.out.println( expressionManipulator.get());	//

		String script = VisualShellExpressionManager.get_instance().get( expandedExpression, variables, ",", ")(");
		if ( null == script)
			return "";

		//System.out.println( script);	//

		if ( !expressionManipulator.parse(
			script,
			VisualShellExpressionManager.get_instance().get_all_functions(),
			VisualShellExpressionManager._constants,
			variables))
			return "";

		//System.out.println( expressionManipulator.get());	//

		Map<String, String> updateVariableMap = new HashMap<String, String>();
		updateVariableMap.put( "PI", "PI()");
		updateVariableMap.put( "E", "E()");
		for ( int i = 0; i < expressionElements._variables.size(); ++i) {
			String[] words = expressionElements._variables.get( i);
			String[] texts = words[ 1].split( ">");
			if ( null == texts)
				return "";

			if ( 1 == texts.length)
				updateVariableMap.put( words[ 0], texts[ 0]);
			else if ( 2 == texts.length)
				updateVariableMap.put( words[ 0], texts[ 1]);
			else
				return "";
		}

		expressionManipulator.update_variable( updateVariableMap);

		//System.out.println( expressionManipulator.get());	//
		//System.out.println( "");	//

		return expressionManipulator.get();
	}

	/**
	 * @param value
	 * @return
	 */
	private String get_prefix_spot_name(String value) {
		if ( !value.startsWith( "<"))
			return "";

		if ( value.startsWith( "<>"))
			return "<>";

		String[] elements = value.substring( "<".length()).split( ">");
		if ( 2 != elements.length)
			return null;

		return ( "<" + elements[ 0] + ">");
	}

	/**
	 * @param value
	 * @return
	 */
	protected String get_others_script1(String value) {
		String[] elements = value.split( " ");
		if ( null == elements || ( 2 != elements.length && 3 != elements.length))
			return "";

		String[] prefixAndObject = CommonRuleManipulator.get_prefix_and_object( elements[ 0]);

		String[] operatorScripts = ( String[])_operatorScriptMap.get(
			2 == elements.length ? "" : elements[ 1]);
		if ( null == operatorScripts || 2 != operatorScripts.length)
			return "";

		return ( operatorScripts[ 0] + prefixAndObject[ 0] + "askEquip " + prefixAndObject[ 1] + operatorScripts[ 1]
			+ ( 2 == elements.length ? elements[ 1] : elements[ 2]));
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.role.base.object.base.Rule#get_cell_text(soars.application.visualshell.object.role.base.Role)
	 */
	@Override
	public String get_cell_text(Role role) {
		String[] elements = _value.split( ": ");
		return ( ( null == elements || 2 > elements.length) ? _value : elements[ 1]);
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.role.base.object.base.Rule#transform_numeric_conditions_and_commands(soars.application.visualshell.object.role.base.Role)
	 */
	@Override
	public Rule transform_numeric_conditions_and_commands(Role role) {
		// TODO 2014.2.12
		return null;
	}

	/**
	 * @param subTypes
	 * @param role
	 * @return
	 */
	protected Rule get_genericRule(String[] subTypes, Role role) {
		// TODO 2014.2.12
		SubType subType = SubType.get( _value, subTypes);
		if ( null == subType)
			return get_othersRule();

		Rule rule = get_genericRule( subType, role);
		return ( null != rule) ? rule : get_othersRule();
	}

	/**
	 * @param subType
	 * @param role
	 * @return
	 */
	protected Rule get_genericRule(SubType subType, Role role) {
		// TODO 2014.2.12
		String mainValue = subType.get_value( _value);
		if ( null == mainValue)
			return null;

		if ( !subType._operator.equals( ""))
			return null;
//			String[] values = mainValue.split(
//				( 0 > "-/".indexOf( subType._operator) ? " \\" : " ")
//					+ subType._operator + " ");
//			if ( null == values || 2 != values.length)
//				return null;
//
//			if ( !set1( values[ 0], subType._kind[ 0], 0))
//				return null;
//
//			if ( !set2( values[ 1], subType._kind[ 1], 1))
//				return null;

		return get( mainValue, subType._kind[ 0], role);
	}

	/**
	 * @param value
	 * @param kind
	 * @param role
	 * @return
	 */
	private Rule get(String value, int kind, Role role) {
		// TODO 2014.2.12
		String[] elements = value.split( " ");
		if ( null == elements || 2 > elements.length)
			return null;

		// "number object"として作成
		EntityVariableRule entityVariableRule = CommonRuleManipulatorNew.get_entityVariableRule( elements[ 0], "number object");
		if ( null == entityVariableRule)
			return null;

		GenericRule genericRule = new GenericRule( _kind, _column, _type, _or, role);
		genericRule._objects.add( entityVariableRule);

		switch ( kind) {
			case 0:
				if ( !get_on_immediate_value( elements, genericRule, role))
					return null;
				break;
			case 1:
				if ( !get_on_numeric_variable( elements, genericRule, role))
					return null;
				break;
			case 2:
				if ( !get_on_expression( elements, genericRule, role))
					return null;
				break;
		}
	
		return genericRule;
	}

	/**
	 * @param elements
	 * @param genericRule
	 * @param role
	 * @return
	 */
	protected boolean get_on_immediate_value(String[] elements, GenericRule genericRule, Role role) {
		return false;
	}

	/**
	 * @param elements
	 * @param genericRule
	 * @param role
	 * @return
	 */
	protected boolean get_on_numeric_variable(String[] elements, GenericRule genericRule, Role role) {
		return false;
	}

	/**
	 * @param elements
	 * @param genericRule
	 * @param role
	 * @return
	 */
	protected boolean get_on_expression(String[] elements, GenericRule genericRule, Role role) {
		return false;
	}

	/**
	 * @param element
	 * @param genericRule
	 * @return
	 */
	protected boolean get_operator(String element, GenericRule genericRule) {
		// TODO 2014.2.12
		if ( null == GenericRule._operatorScriptMap.get( element))
			return false;

		genericRule._objects.add( new ConstantRule( element));

		return true;
	}

	/**
	 * @param element
	 * @param genericRule
	 * @param role
	 * @return
	 */
	protected boolean get_on_immediate_value(String element, GenericRule genericRule, Role role) {
		// TODO 2014.2.19 デフォルト
		return get_on_immediate_value( element, "immediate real number", "real number", genericRule, role);
	}

	/**
	 * @param element
	 * @param variableType
	 * @param valueType
	 * @param genericRule
	 * @param role
	 * @return
	 */
	protected boolean get_on_immediate_value(String element, String variableType, String valueType, GenericRule genericRule, Role role) {
		// TODO 2014.2.19
		EntityVariableRule entityVariableRule = new EntityVariableRule( ( EntityVariableRule)genericRule._objects.get( 0));
		entityVariableRule._variableType = variableType;
		element = Constant.is_correct_number_variable_initial_value( valueType, element);
		if ( null == element)
			return false;

		entityVariableRule._variableValue = element;

		genericRule._objects.add( entityVariableRule);

		return true;
	}

	/**
	 * @param element
	 * @param genericRule
	 * @param role
	 * @return
	 */
	protected boolean get_on_numeric_variable(String element, GenericRule genericRule, Role role) {
		// TODO 2014.2.12
		EntityVariableRule entityVariableRule = new EntityVariableRule( ( EntityVariableRule)genericRule._objects.get( 0));
		entityVariableRule._variableType = "number object";
		entityVariableRule._variableValue = element;
		genericRule._objects.add( entityVariableRule);
		return true;
	}

	/**
	 * @param expressionElements
	 * @param genericRule
	 * @param role
	 * @return
	 */
	protected boolean get_on_expression(ExpressionElements expressionElements, GenericRule genericRule, Role role) {
		// TODO 2014.2.12
		String prefix = ( ( EntityVariableRule)genericRule._objects.get( 0)).get_prefix();
		if ( null == prefix)
			return false;

		ExpressionRule expressionRule = new ExpressionRule( expressionElements.get_function(), role);
		for ( String[] variable:expressionElements._variables) {
			EntityVariableRule entityVariableRule = new EntityVariableRule( ( EntityVariableRule)genericRule._objects.get( 0));
			entityVariableRule._variableType = CommonRuleManipulator.is_object( "number object", prefix + variable[ 1]) ? "number object" : "immediate real number";
			entityVariableRule._variableValue = entityVariableRule._variableType.equals( "number object") ? variable[ 1] : Constant.is_correct_number_variable_initial_value( "real number", variable[ 1]);
			if ( null == entityVariableRule._variableValue)
				return false;

			expressionRule._entityVariableRules.add( entityVariableRule);
		}

		genericRule._objects.add( expressionRule);

		return true;
	}

	/**
	 * @return
	 */
	protected Rule get_othersRule() {
		return null;
	}
}
