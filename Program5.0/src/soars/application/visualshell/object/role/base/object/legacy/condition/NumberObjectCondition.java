/**
 * 
 */
package soars.application.visualshell.object.role.base.object.legacy.condition;

import java.util.TreeMap;
import java.util.Vector;

import soars.application.visualshell.common.tool.CommonTool;
import soars.application.visualshell.layer.Layer;
import soars.application.visualshell.main.ResourceManager;
import soars.application.visualshell.object.expression.VisualShellExpressionManager;
import soars.application.visualshell.object.role.base.Role;
import soars.application.visualshell.object.role.base.object.base.Rule;
import soars.application.visualshell.object.role.base.object.common.CommonRuleManipulator;
import soars.application.visualshell.object.role.base.object.generic.GenericRule;
import soars.application.visualshell.object.role.base.object.legacy.common.expression.ExpressionElements;
import soars.application.visualshell.object.role.base.object.legacy.common.expression.NumberRule;
import soars.application.visualshell.object.role.base.object.legacy.common.expression.SubType;
import soars.common.utility.tool.expression.Expression;

/**
 * @author kurata
 *
 */
public class NumberObjectCondition extends NumberRule {

	/**
	 * 
	 */
	public static String[] _subTypes = {
		ResourceManager.get_instance().get( "rule.sub.type.condition.number.object.value.name"),
		ResourceManager.get_instance().get( "rule.sub.type.condition.number.object.number.object.name"),
		ResourceManager.get_instance().get( "rule.sub.type.condition.number.object.expression.name"),
		ResourceManager.get_instance().get( "rule.sub.type.condition.number.object.others.name")
	};

	/**
	 * @param elements
	 * @return
	 */
	public static ExpressionElements get1(String[] elements) {
		ExpressionElements expressionElements = new ExpressionElements();

		expressionElements._function = elements[ 2];

		for ( int i = 3; i < elements.length; ++i) {
			String[] words = elements[ i].split( "=");
			if ( null == words || 2 != words.length)
				return null;

			expressionElements._variables.add( words);
		}

		return expressionElements;
	}

	/**
	 * @param kind
	 * @param type
	 * @param value
	 */
	public NumberObjectCondition(String kind, String type, String value) {
		super(kind, type, value);
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.role.base.rule.base.Rule#get_used_expressions()
	 */
	@Override
	protected ExpressionElements[] get_used_expressions() {
		SubType subType = SubType.get( _value, _subTypes);
		if ( null == subType)
			return null;

		if ( 2 != subType._kind[ 0] && 2 != subType._kind[ 1])
			return null;

		String mainValue = subType.get_value( _value);
		if ( null == mainValue)
			return null;

		Vector<ExpressionElements> results = new Vector<ExpressionElements>();
		if ( subType._operator.equals( "")) {
			if ( 2 == subType._kind[ 0]) {
				ExpressionElements expressionElements = get1( mainValue);
				if ( null != expressionElements)
					results.add( expressionElements);
			}
		} else {
			String[] values = mainValue.split(
				( 0 > "-/".indexOf( subType._operator) ? " \\" : " ")
					+ subType._operator + " ");
			if ( null == values || 2 != values.length)
				return null;

			if ( 2 == subType._kind[ 0]) {
				ExpressionElements expressionElements = get1( values[ 0]);
				if ( null != expressionElements)
					results.add( expressionElements);
			}

			if ( 2 == subType._kind[ 1]) {
				ExpressionElements expressionElements = get2( values[ 1]);
				if ( null != expressionElements)
					results.add( expressionElements);
			}
		}

		if ( results.isEmpty())
			return null;

		return results.toArray( new ExpressionElements[ 0]);
	}

	/**
	 * @param value
	 * @return
	 */
	private ExpressionElements get1(String value) {
		String[] elements = value.split( " ");
		if ( null == elements || 3 > elements.length)
			return null;

		return get1( elements);
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.role.base.rule.base.Rule#update_expression(soars.application.visualshell.object.expression.VisualShellExpressionManager)
	 */
	@Override
	public boolean update_expression(VisualShellExpressionManager visualShellExpressionManager) {
		SubType subType = SubType.get( _value, _subTypes);
		if ( null == subType)
			return false;

		if ( 2 != subType._kind[ 0] && 2 != subType._kind[ 1])
			return false;

		String mainValue = subType.get_value( _value);
		if ( null == mainValue)
			return false;

		if ( subType._operator.equals( "")) {
			if ( 2 != subType._kind[ 0])
				return false;

			String newValue = update_expression1( mainValue, visualShellExpressionManager);
			if ( null == newValue)
				return false;

			_value = ( subType.get( _subTypes) + newValue);
		} else {
			String[] values = mainValue.split(
				( 0 > "-/".indexOf( subType._operator) ? " \\" : " ")
					+ subType._operator + " ");
			if ( null == values || 2 != values.length)
				return false;

			String[] newValues = new String[] { null, null};

			if ( 2 == subType._kind[ 0])
				newValues[ 0] = update_expression1( values[ 0], visualShellExpressionManager);

			if ( 2 == subType._kind[ 1])
				newValues[ 1] = update_expression2( values[ 1], visualShellExpressionManager);

			_value = ( subType.get( _subTypes)
				+ ( ( null == newValues[ 0]) ? values[ 0] : newValues[ 0])
				+ " " + subType._operator + " "
				+ ( ( null == newValues[ 1]) ? values[ 1] : newValues[ 1]));
		}

		return true;
	}

	/**
	 * @param value
	 * @param visualShellExpressionManager
	 * @return
	 */
	private String update_expression1(String value, VisualShellExpressionManager visualShellExpressionManager) {
		String[] elements = value.split( " ");
		if ( null == elements || 3 > elements.length)
			return null;

		ExpressionElements expressionElements = get1( elements);
		if ( null == expressionElements)
			return null;

		expressionElements = update_expression( expressionElements, visualShellExpressionManager);
		if ( null == expressionElements)
			return null;

		String newValue = get( expressionElements);
		if ( null == newValue)
			return null;

		return ( elements[ 0] + " " + elements[ 1] + " " + newValue);
	}

	/**
	 * @param value
	 * @param visualShellExpressionManager
	 * @return
	 */
	private String update_expression2(String value, VisualShellExpressionManager visualShellExpressionManager) {
		ExpressionElements expressionElements = get2( value);
		if ( null == expressionElements)
			return null;

		expressionElements = update_expression( expressionElements, visualShellExpressionManager);
		if ( null == expressionElements)
			return null;

		return get( expressionElements);
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
		SubType subType = SubType.get( _value, _subTypes);
		if ( null == subType)
			return null;

		String mainValue = subType.get_value( _value);
		if ( null == mainValue)
			return null;

		String[] elements = mainValue.split( " ");
		if ( null == elements || 2 > elements.length)
			return null;

		return CommonRuleManipulator.extract_spot_name2( elements[ 0]);
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.role.base.rule.base.Rule#get_used_spot_variable_names(soars.application.visualshell.object.role.base.Role)
	 */
	@Override
	protected String[] get_used_spot_variable_names(Role role) {
		return new String[] { get_used_spot_variable_name()};
	}

	/**
	 * @return
	 */
	private String get_used_spot_variable_name() {
		SubType subType = SubType.get( _value, _subTypes);
		if ( null == subType)
			return null;

		String mainValue = subType.get_value( _value);
		if ( null == mainValue)
			return null;

		String[] elements = mainValue.split( " ");
		if ( null == elements || 2 > elements.length)
			return null;

		return CommonRuleManipulator.get_spot_variable_name2( elements[ 0]);
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.role.base.rule.base.Rule#get_used_number_object_names()
	 */
	@Override
	protected String[] get_used_number_object_names() {
		SubType subType = SubType.get( _value, _subTypes);
		if ( null == subType)
			return null;

		String mainValue = subType.get_value( _value);
		if ( null == mainValue)
			return null;

		String[] elements = mainValue.split( " ");
		if ( null == elements || 2 > elements.length)
			return null;

		String prefix = CommonRuleManipulator.get_full_prefix( elements[ 0]);
		if ( null == prefix)
			return null;

		Vector<String> numberObjectNames = new Vector<String>();
		if ( subType._operator.equals( "")) {
			get_number_object_names1( mainValue, subType._kind[ 0], prefix, numberObjectNames);
		} else {
			String[] values = mainValue.split(
				( 0 > "-/".indexOf( subType._operator) ? " \\" : " ")
					+ subType._operator + " ");
			if ( null == values || 2 != values.length)
				return null;

			get_number_object_names1( values[ 0], subType._kind[ 0], prefix, numberObjectNames);
			get_number_object_names2( values[ 1], subType._kind[ 1], prefix, numberObjectNames);
		}

		if ( numberObjectNames.isEmpty())
			return null;

		return ( String[])numberObjectNames.toArray( new String[ 0]);
	}

	/**
	 * @param value
	 * @param kind
	 * @param prefix
	 * @param numberObjectNames
	 */
	private void get_number_object_names1(String value, int kind, String prefix, Vector<String> numberObjectNames) {
		switch ( kind) {
			case 0:
				get_number_object_names( value, 3, numberObjectNames);
				break;
			case 1:
				get_number_object_names( value, 3, numberObjectNames);
				get_number_object_names( value, 3, new int[] { 2}, prefix, numberObjectNames);
				break;
			case 2:
				String[] elements = value.split( " ");
				if ( null == elements || 3 > elements.length)
					return;

				numberObjectNames.add( elements[ 0]);

				ExpressionElements expressionElements = get1( elements);
				if ( null != expressionElements)
					get_number_object_names( expressionElements, prefix, numberObjectNames);

				break;
		}
	}

	/**
	 * @param value
	 * @param kind
	 * @param prefix
	 * @param numberObjectNames
	 */
	private void get_number_object_names2(String value, int kind, String prefix, Vector<String> numberObjectNames) {
		switch ( kind) {
			case 1:
				numberObjectNames.add( prefix + value);
	
				break;
			case 2:
				ExpressionElements expressionElements = get2( value);
				if ( null != expressionElements)
					get_number_object_names( expressionElements, prefix, numberObjectNames);
	
				break;
		}
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.role.base.rule.base.Rule#update_spot_name_and_number(java.lang.String, java.lang.String, java.lang.String, java.util.Vector, java.lang.String, java.util.Vector)
	 */
	@Override
	public boolean update_spot_name_and_number(String newName, String originalName, String headName, Vector<String[]> ranges, String newHeadName, Vector<String[]> newRanges) {
		SubType subType = SubType.get( _value, _subTypes);
		if ( null == subType)
			return false;

		String mainValue = subType.get_value( _value);
		if ( null == mainValue)
			return false;

		String[] elements = mainValue.split( " ");
		if ( null == elements || 3 > elements.length)
			return false;

		elements[ 0] = CommonRuleManipulator.update_spot_name2( elements[ 0], newName, originalName, headName, ranges, newHeadName, newRanges);
		if ( null == elements[ 0])
			return false;

		String newValue = "";
		for ( int i = 0; i < elements.length; ++i)
			newValue += ( ( ( 0 == i) ? "" : " ") + elements[ i]);

		_value = ( subType.get( _subTypes) + newValue);

		return true;
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.role.base.rule.base.Rule#update_spot_variable_name(java.lang.String, java.lang.String, java.lang.String, soars.application.visualshell.object.role.base.Role)
	 */
	@Override
	protected boolean update_spot_variable_name(String name, String newName, String type, Role role) {
		SubType subType = SubType.get( _value, _subTypes);
		if ( null == subType)
			return false;

		String mainValue = subType.get_value( _value);
		if ( null == mainValue)
			return false;

		String[] elements = mainValue.split( " ");
		if ( null == elements || 3 > elements.length)
			return false;

		elements[ 0] = CommonRuleManipulator.update_spot_variable_name2( elements[ 0], name, newName, type);
		if ( null == elements[ 0])
			return false;

		String newValue = "";
		for ( int i = 0; i < elements.length; ++i)
			newValue += ( ( ( 0 == i) ? "" : " ") + elements[ i]);

		_value = ( subType.get( _subTypes) + newValue);

		return true;
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.role.base.rule.base.Rule#update_number_object_name(java.lang.String, java.lang.String, java.lang.String, soars.application.visualshell.object.role.base.Role)
	 */
	@Override
	protected boolean update_number_object_name(String name, String newName, String type, Role role) {
		SubType subType = SubType.get( _value, _subTypes);
		if ( null == subType)
			return false;

		String mainValue = subType.get_value( _value);
		if ( null == mainValue)
			return false;

		String[] elements = mainValue.split( " ");
		if ( null == elements || 2 > elements.length)
			return false;

		String newValue = null;

		if ( subType._operator.equals( "")) {
			newValue = update_number_object_name1( mainValue, subType._kind[ 0], name, newName, type);
		} else {
			String[] values = mainValue.split(
				( 0 > "-/".indexOf( subType._operator) ? " \\" : " ")
					+ subType._operator + " ");
			if ( null == values || 2 != values.length)
				return false;

			String value0 = update_number_object_name1( values[ 0], subType._kind[ 0], name, newName, type);
			if ( null != value0)
				values[ 0] = value0;

			String prefix = CommonRuleManipulator.get_full_prefix( values[ 0]);
			if ( null == prefix)
				return false;

			String value1 = update_number_object_name2( prefix, values[ 1], subType._kind[ 1], name, newName, type);
			if ( null != value1)
				values[ 1] = value1;

			if ( null == value0 && null == value1)
				return false;

			newValue = ( values[ 0] + " " + subType._operator + " " + values[ 1]);
		}

		if ( null == newValue)
			return false;

		_value = ( subType.get( _subTypes) + newValue);

		return true;
	}

	/**
	 * @param value
	 * @param kind
	 * @param name
	 * @param newName
	 * @param type
	 * @return
	 */
	private String update_number_object_name1(String value, int kind, String name, String newName, String type) {
		switch ( kind) {
			case 0:
				return update_number_object_name10( value, name, newName, type);
			case 1:
				return update_number_object_name11( value, name, newName, type);
			case 2:
				return update_number_object_name12( value, name, newName, type);
		}
		return null;
	}

	/**
	 * @param value
	 * @param name
	 * @param newName
	 * @param type
	 * @return
	 */
	private String update_number_object_name10(String value, String name, String newName, String type) {
		String[] elements = value.split( " ");
		if ( null == elements || 3 != elements.length)
			return null;

		elements[ 0] = CommonRuleManipulator.update_object_name( elements[ 0], name, newName, type);
		if ( null == elements[ 0])
			return null;

		return ( elements[ 0] + " " + elements[ 1] + " " + elements[ 2]);
	}

	/**
	 * @param value
	 * @param name
	 * @param newName
	 * @param type
	 * @return
	 */
	private String update_number_object_name11(String value, String name, String newName, String type) {
		String[] elements = value.split( " ");
		if ( null == elements || 3 != elements.length)
			return null;

		String prefix = CommonRuleManipulator.get_full_prefix( elements[ 0]);
		if ( null == prefix)
			return null;

		String element0 = CommonRuleManipulator.update_object_name( elements[ 0], name, newName, type);
		if ( null != element0)
			elements[ 0] = element0;

		if ( elements[ 2].startsWith( "<")) {
			String element2 = CommonRuleManipulator.update_object_name( elements[ 2], name, newName, type);
			if ( null != element2)
				elements[ 2] = element2;

			if ( null == element0 && null == element2)
				return null;

		} else {
			boolean result2 = CommonRuleManipulator.correspond( prefix, elements[ 2], name, type);
			if ( result2)
				elements[ 2] = newName;

			if ( null == element0 && !result2)
				return null;

		}

		return ( elements[ 0] + " " + elements[ 1] + " " + elements[ 2]);
	}

	/**
	 * @param value
	 * @param name
	 * @param newName
	 * @param type
	 * @return
	 */
	private String update_number_object_name12(String value, String name, String newName, String type) {
		String[] elements = value.split( " ");
		if ( null == elements || 3 > elements.length)
			return null;

		String prefix = CommonRuleManipulator.get_full_prefix( elements[ 0]);
		if ( null == prefix)
			return null;

		String element0 = CommonRuleManipulator.update_object_name( elements[ 0], name, newName, type);
		if ( null != element0)
			elements[ 0] = element0;

		ExpressionElements expressionElements = get1( elements);
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

		if ( null == element0 && !result)
			return null;

		return ( elements[ 0] + " " + elements[ 1] + " " + expression);
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.role.base.rule.base.Rule#get_script(java.lang.String, soars.application.visualshell.object.role.base.Role)
	 */
	@Override
	protected String get_script(String value, Role role) {
		SubType subType = SubType.get( value, _subTypes);
		if ( null == subType)
			return "";

		String mainValue = subType.get_value( value);
		if ( null == mainValue)
			return "";

		if ( subType._operator.equals( "")) {
			String script = get_script1( mainValue, subType._kind[ 0], false);
			if ( null == script)
				return "";

			return script;
		} else {
			String[] values = mainValue.split(
				( 0 > "-/".indexOf( subType._operator) ? " \\" : " ")
					+ subType._operator + " ");
			if ( null == values || 2 != values.length)
				return "";

			String[] scripts = new String[] { null, null};

			scripts[ 0] = get_script1( values[ 0], subType._kind[ 0], true);
			scripts[ 1] = get_script2( values[ 1], subType._kind[ 1]);
			if ( null == scripts[ 0] || null == scripts[ 1])
				return "";

			return ( scripts[ 0] + subType._operator + scripts[ 1]);
		}
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.role.base.rule.base.Rule#can_paste(soars.application.visualshell.object.role.base.Role, soars.application.visualshell.layer.Layer)
	 */
	@Override
	protected boolean can_paste(Role role, Layer drawObjects) {
		if ( !can_paste_spot_and_spot_variable_name( drawObjects))
			return false;

		if ( !can_paste_number_object_name( drawObjects))
			return false;

		return true;
	}
	/**
	 * @param drawObjects
	 * @return
	 */
	private boolean can_paste_spot_and_spot_variable_name(Layer drawObjects) {
		SubType subType = SubType.get( _value, _subTypes);
		if ( null == subType)
			return false;

		String mainValue = subType.get_value( _value);
		if ( null == mainValue)
			return false;

		String[] elements = mainValue.split( " ");
		if ( null == elements || 2 > elements.length)
			return false;

		return CommonRuleManipulator.can_paste_spot_and_spot_variable_name2( elements[ 0], drawObjects);
	}

	/**
	 * @param drawObjects
	 * @return
	 */
	private boolean can_paste_number_object_name(Layer drawObjects) {
		SubType subType = SubType.get( _value, _subTypes);
		if ( null == subType)
			return false;

		String mainValue = subType.get_value( _value);
		if ( null == mainValue)
			return false;

		String[] elements = mainValue.split( " ");
		if ( null == elements || 2 > elements.length)
			return false;

		String prefix = CommonRuleManipulator.get_full_prefix( elements[ 0]);
		if ( null == prefix)
			return false;

		if ( subType._operator.equals( "")) {
			return can_paste_number_object_names1( mainValue, subType._kind[ 0], prefix, drawObjects);
		} else {
			String[] values = mainValue.split(
				( 0 > "-/".indexOf( subType._operator) ? " \\" : " ")
					+ subType._operator + " ");
			if ( null == values || 2 != values.length)
				return false;

			return ( can_paste_number_object_names1( values[ 0], subType._kind[ 0], prefix, drawObjects)
				&& can_paste_number_object_names2( values[ 1], subType._kind[ 1], prefix, drawObjects));
		}
	}

	/**
	 * @param value
	 * @param kind
	 * @param prefix
	 * @param drawObjects
	 */
	private boolean can_paste_number_object_names1(String value, int kind, String prefix, Layer drawObjects) {
		switch ( kind) {
			case 0:
				return can_paste_number_object_names( value, 3, drawObjects);
			case 1:
				return ( can_paste_number_object_names( value, 3, drawObjects)
					&& can_paste_number_object_names( value, 3, new int[] { 2}, prefix, drawObjects));
			case 2:
				String[] elements = value.split( " ");
				if ( null == elements || 3 > elements.length)
					return false;

				if ( !CommonRuleManipulator.can_paste_object( "number object", elements[ 0], drawObjects))
					return false;

				ExpressionElements expressionElements = get1( elements);
				if ( null == expressionElements)
					break;

				return can_paste_number_object_names( expressionElements, prefix, drawObjects);
		}
		return true;
	}

	/**
	 * @param value
	 * @param kind
	 * @param prefix
	 * @param drawObjects
	 */
	private boolean can_paste_number_object_names2(String value, int kind, String prefix, Layer drawObjects) {
		switch ( kind) {
			case 1:
				return CommonRuleManipulator.can_paste_object( "number object", prefix + value, drawObjects);
			case 2:
				ExpressionElements expressionElements = get2( value);
				if ( null == expressionElements)
					break;

				return can_paste_number_object_names( expressionElements, prefix, drawObjects);
		}
		return true;
	}

	/**
	 * @param value
	 * @param kind
	 * @param bracket
	 * @return
	 */
	private String get_script1(String value, int kind, boolean bracket) {
		switch ( kind) {
			case 0:
				return get_value_script1( value);
			case 1:
				return get_number_object_script1( value);
			case 2:
				String[] elements = value.split( " ");
				if ( null == elements || 3 > elements.length)
					return null;

				ExpressionElements expressionElements = get1( elements);
				if ( null == expressionElements)
					return "";

				return get_expression_script1( expressionElements, elements[ 0], elements[ 1], bracket);
			case 3:
				return get_others_script1( value);
		}

		return "";
	}

	/**
	 * @param value
	 * @param kind
	 * @return
	 */
	private String get_script2(String value, int kind) {
		switch ( kind) {
			case 0:
				return ( value.startsWith( "-") ? "(" + value + ")" : value);
			case 1:
				return get_number_object_script2( value);
			case 2:
				ExpressionElements expressionElements = get2( value);
				if ( null == expressionElements)
					return "";

				String expression = get_expression_script2( expressionElements);
				if ( expression.equals( ""))
					return "";

				return ( "(" + expression + ")");
			case 3:
				return value;
		}

		return "";
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.role.base.rule.base.Rule#get_used_expressions(java.util.TreeMap, java.util.TreeMap)
	 */
	@Override
	public void get_used_expressions(TreeMap<String, Expression> expressionMap, TreeMap<String, Expression> usedExpressionMap) {
		ExpressionElements[] expressionElements = get_used_expressions();
		if ( null == expressionElements)
			return;

		for ( int i = 0; i < expressionElements.length; ++i) {
			if ( null == expressionElements[ i])
				continue;

			if ( null != usedExpressionMap.get( expressionElements[ i].get_function()))
				continue;

			Expression expression = ( Expression)expressionMap.get( expressionElements[ i].get_function());
			if ( null == expression)
				continue;

			usedExpressionMap.put( expressionElements[ i].get_function(), expression);
		}
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.role.base.rule.base.Rule#update_function(java.lang.String, java.lang.String)
	 */
	@Override
	public boolean update_function(String originalFunctionName, String newFunctionName) {
		SubType subType = SubType.get( _value, _subTypes);
		if ( null == subType)
			return false;

		if ( 2 != subType._kind[ 0] && 2 != subType._kind[ 1])
			return false;

		String mainValue = subType.get_value( _value);
		if ( null == mainValue)
			return false;

		if ( subType._operator.equals( "")) {
			if ( 2 != subType._kind[ 0])
				return false;

			String newValue = update_function1( mainValue, originalFunctionName, newFunctionName);
			if ( null == newValue)
				return false;

			_value = ( subType.get( _subTypes) + newValue);
		} else {
			String[] values = mainValue.split(
				( 0 > "-/".indexOf( subType._operator) ? " \\" : " ")
					+ subType._operator + " ");
			if ( null == values || 2 != values.length)
				return false;

			String[] newValues = new String[] { null, null};

			if ( 2 == subType._kind[ 0])
				newValues[ 0] = update_function1( values[ 0], originalFunctionName, newFunctionName);

			if ( 2 == subType._kind[ 1])
				newValues[ 1] = update_function2( values[ 1], originalFunctionName, newFunctionName);

			_value = ( subType.get( _subTypes)
				+ ( ( null == newValues[ 0]) ? values[ 0] : newValues[ 0])
				+ " " + subType._operator + " "
				+ ( ( null == newValues[ 1]) ? values[ 1] : newValues[ 1]));
		}

		return true;
	}

	/**
	 * @param value
	 * @param originalFunctionName
	 * @param newFunctionName
	 * @return
	 */
	private String update_function1(String value, String originalFunctionName, String newFunctionName) {
		String[] elements = value.split( " ");
		if ( null == elements || 3 > elements.length)
			return null;

		ExpressionElements expressionElements = get1( elements);
		if ( null == expressionElements)
			return null;

		if ( !expressionElements.update_function( originalFunctionName, newFunctionName))
			return null;

		String new_value = get( expressionElements);
		if ( null == new_value)
			return null;

		return ( elements[ 0] + " " + elements[ 1] + " " + new_value);
	}

	/**
	 * @param value
	 * @param originalFunctionName
	 * @param newFunctionName
	 * @return
	 */
	private String update_function2(String value, String originalFunctionName, String newFunctionName) {
		ExpressionElements expressionElements = get2( value);
		if ( null == expressionElements)
			return null;

		if ( !expressionElements.update_function( originalFunctionName, newFunctionName))
			return null;

		return get( expressionElements);
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.role.base.object.legacy.common.expression.NumberRule#transform_numeric_conditions_and_commands(soars.application.visualshell.object.role.base.Role)
	 */
	@Override
	public Rule transform_numeric_conditions_and_commands(Role role) {
		// TODO 2014.2.12
		return get_genericRule( _subTypes, role);
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.role.base.object.legacy.common.expression.NumberRule#get_on_immediate_value(java.lang.String[], soars.application.visualshell.object.role.base.object.generic.GenericRule, soars.application.visualshell.object.role.base.Role)
	 */
	@Override
	protected boolean get_on_immediate_value(String[] elements, GenericRule genericRule, Role role) {
		// TODO 2014.2.12
		if ( null == elements || 3 > elements.length)
			return false;

		// TODO idを変更したらこれも変えること！
		genericRule._id = ResourceManager.get_instance().get( "generic.rule.condition.numeric.numeric.comparison.id");

		if ( !get_operator( elements[ 1], genericRule))
			return false;

		return get_on_immediate_value( elements[ 2], genericRule, role);
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.role.base.object.legacy.common.expression.NumberRule#get_on_numeric_variable(java.lang.String[], soars.application.visualshell.object.role.base.object.generic.GenericRule, soars.application.visualshell.object.role.base.Role)
	 */
	@Override
	protected boolean get_on_numeric_variable(String[] elements, GenericRule genericRule, Role role) {
		// TODO 2014.2.12
		if ( null == elements || 3 > elements.length)
			return false;

		// TODO idを変更したらこれも変えること！
		genericRule._id = ResourceManager.get_instance().get( "generic.rule.condition.numeric.numeric.comparison.id");

		if ( !get_operator( elements[ 1], genericRule))
			return false;

		return get_on_numeric_variable( elements[ 2], genericRule, role);
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.role.base.object.legacy.common.expression.NumberRule#get_on_expression(java.lang.String[], soars.application.visualshell.object.role.base.object.generic.GenericRule, soars.application.visualshell.object.role.base.Role)
	 */
	@Override
	protected boolean get_on_expression(String[] elements, GenericRule genericRule, Role role) {
		// TODO 2014.2.12
		if ( null == elements || 3 > elements.length)
			return false;

		// TODO idを変更したらこれも変えること！
		genericRule._id = ResourceManager.get_instance().get( "generic.rule.condition.numeric.expression.comparison.id");

		if ( !get_operator( elements[ 1], genericRule))
			return false;

		ExpressionElements expressionElements = NumberObjectCondition.get1( elements);
		if ( null == expressionElements)
			return false;

		return get_on_expression( expressionElements, genericRule, role);
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.role.base.object.legacy.common.expression.NumberRule#get_othersRule()
	 */
	@Override
	protected Rule get_othersRule() {
		// TODO 2014.2.12
		return Rule.create( _kind, _column, ResourceManager.get_instance().get( "rule.type.condition.others"), _value, _or);
	}
}
