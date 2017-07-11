/*
 * Created on 2005/10/28
 */
package soars.common.utility.tool.expression;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

/**
 * @author kurata
 */
public class Expression {

	/**
	 * 
	 */
	public String[] _value = new String[ 3];

	/**
	 * @param function1
	 * @param function2
	 * @param expression
	 * 
	 */
	public Expression(String function1, String function2, String expression) {
		super();
		_value[ 0] = function1;
		_value[ 1] = function2;
		_value[ 2] = expression;
	}

	/**
	 * @param value
	 */
	public Expression(String[] value) {
		_value = value;
	}

	/**
	 * @param expression
	 */
	public Expression(Expression expression) {
		super();
		_value[ 0] = expression._value[ 0];
		_value[ 1] = expression._value[ 1];
		_value[ 2] = expression._value[ 2];
	}

	/**
	 * 
	 */
	public void cleanup() {
		for ( int i = 0; i < _value.length; ++i)
			_value[ i] = "";
	}

	/**
	 * @param expression
	 * @return
	 */
	public boolean same_as(Expression expression) {
		return ( _value[ 0].equals( expression._value[ 0])
			&& _value[ 1].equals( expression._value[ 1])
			&& _value[ 2].equals( expression._value[ 2]));
	}

	/**
	 * @return
	 */
	public Integer get_argument_count() {
		if ( _value[ 1].equals( ""))
			return ( new Integer( 0));

		String[] arguments = _value[ 1].split( ",");
		if ( null == arguments || 0 == arguments.length)
			return null;

		return ( new Integer( arguments.length));
	}

	/**
	 * @return
	 */
	public String get_function() {
		return ( _value[ 0] + "(" + _value[ 1] + ")");
	}

	/**
	 * @return
	 */
	public String[] get_arguments() {
		if ( _value[ 1].equals( ""))
			return null;

		String[] arguments = _value[ 1].split( ",");
		if ( null == arguments || 0 == arguments.length)
			return null;

		return arguments;
	}

	/**
	 * @param arguments
	 * @param functions
	 * @param argumentCountMap
	 * @param constants
	 * @return
	 */
	public String replace(Vector<String> arguments, Vector<String> functions, Map<String, Integer> argumentCountMap, Vector<String> constants) {
		if ( _value[ 2].equals( ""))
			return null;

		if ( _value[ 1].equals( "")) {
			 if ( !arguments.isEmpty())
			 	return null;

			 return _value[ 2];
		}

		String[] words = _value[ 1].split( ",");
		if ( null == words || 0 == words.length)
			return null;

		if ( words.length != arguments.size())
			return null;

		Map<String, String> variableMap = new HashMap<String, String>();
		for ( int i = 0; i < words.length; ++i)
			variableMap.put( words[ i], arguments.get( i));

		Vector<String> variables = new Vector<String>();
		for ( int i = 0; i < words.length; ++i)
			variables.add( words[ i]);

		ExpressionManipulator expressionManipulator = new ExpressionManipulator();
		if ( !expressionManipulator.is_correct( _value[ 2], functions, argumentCountMap, constants, variables))
			return null;

		String result = expressionManipulator.get( variableMap);
		if ( result.equals( ""))
			return null;

		return result;
	}

	/**
	 * @param expressionManager
	 * @param originalFunctionName
	 * @param newFunctionName
	 * @return
	 */
	public boolean update_expression(ExpressionManager expressionManager, String originalFunctionName, String newFunctionName) {
		String[] arguments = get_arguments();
		if ( null == arguments)
			return true;

		Vector<String> variables = new Vector<String>( Arrays.asList( arguments));

		if ( !variables.contains( newFunctionName))
			return true;

		String newVariableName;
		int index = 1;
		while ( true) {
			newVariableName = ( newFunctionName + index);
			if ( !variables.contains( newVariableName))
				break;

			++index;
		}

		Map<String, String> variableMap = new HashMap<String, String>();
		for ( int i = 0; i < arguments.length; ++i)
			variableMap.put( arguments[ i], ( ( arguments[ i].equals( newFunctionName)) ? newVariableName : arguments[ i]));

		String expression = expressionManager.get( _value[ 2], variables, variableMap);
		if ( null == expression)
			return false;

		String value = "";
		for ( int i = 0; i < variables.size(); ++i) {
			String variable = ( String)variables.get( i);
			if ( variable.equals( newFunctionName))
				variable = newVariableName;

			value += ( ( ( 0 == i) ? "" : ",") + variable);
		}

		_value[ 1] = value;

		_value[ 2] = expression;

		return true;
	}

	/**
	 * @param expressionManager
	 * @param originalFunctionName
	 * @param newFunctionName
	 * @return
	 */
	public boolean update_function(ExpressionManager expressionManager, String originalFunctionName, String newFunctionName) {
		String[] arguments = get_arguments();
		Vector<String> variables = ( ( null == arguments)
			? new Vector<String>()
			: new Vector<String>( Arrays.asList( arguments)));

		String expression = expressionManager.get( _value[ 2], variables, new String[] { originalFunctionName, newFunctionName});
		if ( null == expression)
			return false;

		_value[ 2] = expression;

		return true;
	}

	/**
	 * @return
	 */
	public int get_variables_count() {
		// TODO 2014.2.7
		return get_variables_count( _value[ 1]);
	}

	/**
	 * @param text
	 * @return
	 */
	public static int get_variables_count(String text) {
		// TODO 2014.2.7
		Vector<String> variables = get_variables( text);
		if ( null == variables)
			return -1;

		return variables.size();
	}

	/**
	 * @param text
	 * @return
	 */
	public Vector<String> get_variables() {
		// TODO 2014.2.7
		return get_variables( _value[ 1]);
	}

	/**
	 * @param text
	 * @return
	 */
	public static Vector<String> get_variables(String text) {
		// TODO 2014.2.7
		if ( text.equals( ""))
			return new Vector<String>();

		String[] arguments = text.split( ",");
		if ( null == arguments || 0 == arguments.length)
			return null;

		for ( int i = 0; i < arguments.length; ++i) {
			if ( arguments[ i].equals( "")
				|| ExpressionManager.is_reserved_word( arguments[ i])
				|| !arguments[ i].matches( "[^0-9]+.*"))
				return null;
		}

		Vector<String> variables = new Vector<String>();
		for ( int i = 0; i < arguments.length; ++i) {
			if ( variables.contains( arguments[ i]))
				return null;

			variables.add( arguments[ i]);
		}

		return variables;
	}

	/**
	 * @param newFunctionName
	 * @param newVariableCount
	 * @param originalFunctionName
	 * @param originalVariableCount
	 * @return
	 */
	public boolean update(String newFunctionName, int newVariableCount, String originalFunctionName, int originalVariableCount) {
		// TODO 2014.2.6
		//System.out.println( _value[ 2]);
		if ( newFunctionName.equals( originalFunctionName) && newVariableCount == originalVariableCount) {
			//System.out.println( "\n");
			return true;
		}

		String[] words = _value[ 2].split( originalFunctionName + "\\(");
		if ( null == words)
			return false;

		String result = "";
		if ( 1 == words.length) {
			//System.out.println( _value[ 2] + "\n");
			return true;
		} else {
			for ( int i = 1; i < words.length; ++i) {
				if ( ( words[ i - 1].equals( "") || words[ i - 1].matches( ".*[+-\\\\*/(),]")) && newVariableCount != originalVariableCount) {
					words[ i] = adjust( words[ i], newVariableCount, originalVariableCount);
					if ( null == words[ i])
						return false;
				}
				words[ i] = ( ( words[ i - 1].equals( "") || words[ i - 1].matches( ".*[+-\\\\*/(),]")) ? newFunctionName : originalFunctionName) + "(" + words[ i];
				//System.out.println( words[ i]);
			}
			for ( String word:words)
				result += word;
		}

		_value[ 2] = result;
		//System.out.println( _value[ 2] + "\n");

		return true;
	}

	/**
	 * @param string
	 * @param newVariableCount
	 * @param originalVariableCount
	 * @return
	 */
	private String adjust(String string, int newVariableCount, int originalVariableCount) {
		// TODO 2014.2.6
		String last = "";
		Vector<String> words = new Vector<String>();
		String word = "";
		int stack = 1;
		for ( int i = 0; i < string.length(); ++i) {
			char c = string.charAt( i);
			if ( 0 == stack)
				last += c;
			else {
				switch ( c) {
					case '(':
						word += c;
						++stack;
						break;
					case ')':
						--stack;
						if ( 0 < stack)
							word += c;
						break;
					case ',':
						if ( 1 != stack)
							word += c;
						else {
							words.add( word);
							word = "";
						}
						break;
					default:
						word += c;
						break;
				}
			}
		}

		if ( !word.equals( ""))
			words.add( word);

		if ( 0 != stack || words.size() != originalVariableCount || words.contains( ""))
			return null;

		String result = "";
		for ( int i = 0; i < newVariableCount; ++i) {
			result += ( ( i < originalVariableCount) ? words.get( i) : ( words.isEmpty() ? "0" : words.lastElement()));
			result += ( i < newVariableCount - 1) ? "," : ")";
		}

		return ( result + last);
	}

	/**
	 * @param expression
	 * @return
	 */
	public boolean same_as_logically(Expression expression) {
		// TODO 2014.2.13
		Vector<String> variables1 = get_variables();
		if ( null == variables1)
			return false;

		Vector<String> variables2 = expression.get_variables();
		if ( null == variables2)
			return false;

		if ( variables1.size() != variables2.size())
			return false;

		if ( equals( variables1, variables2))
			return _value[ 2].equals( expression._value[ 2]);
		else
			return same_as( variables1, _value[ 2], variables2, expression._value[ 2]);
	}

	/**
	 * @param variables1
	 * @param variables2
	 * @return
	 */
	private boolean equals(Vector<String> variables1, Vector<String> variables2) {
		// TODO 2014.2.13
		for ( int i = 0; i < variables1.size(); ++i) {
			if ( !variables1.get( i).equals( variables2.get( i)))
				return false;
		}

		return true;
	}

	/**
	 * @param variables1
	 * @param expression1
	 * @param variables2
	 * @param expression2
	 * @return
	 */
	private boolean same_as(Vector<String> variables1, String expression1, Vector<String> variables2, String expression2) {
		// TODO 2014.2.13
		for ( int i = 0; i < variables1.size(); ++i) {
			if ( variables1.get( i).equals( variables2.get( i)))
				continue;

			expression1 = replace( variables1.get( i), expression1, variables2.get( i), expression2);
		}
		return expression1.equals( expression2);
	}

	/**
	 * @param variable1
	 * @param expression1
	 * @param variable1
	 * @param expression2
	 * @return
	 */
	private String replace(String variable1, String expression1, String variable2, String expression2) {
		// TODO 2014.2.13
		String[] words = expression1.split( variable1);
		if ( null == words)
			return expression1;

		String result = "";
		if ( 1 == words.length) {
			return expression1;
		} else {
			for ( int i = 1; i < words.length; ++i)
				words[ i] = ( ( words[ i - 1].equals( "") || words[ i - 1].matches( ".*[+-\\\\*/(),]") && ( words[ i].equals( "") || words[ i].matches( "[+-\\\\*/(),].*"))) ? variable1 : variable2);

			for ( String word:words)
				result += word;
		}

		return result;
	}
}
