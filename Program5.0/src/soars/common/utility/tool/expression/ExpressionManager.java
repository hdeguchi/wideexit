/*
 * Created on 2005/10/28
 */
package soars.common.utility.tool.expression;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;
import java.util.Vector;

/**
 * @author kurata
 */
public class ExpressionManager extends TreeMap<String, Expression> {

	/**
	 * 
	 */
	static public Vector<String> _functions = null;

	/**
	 * 
	 */
	static public HashMap<String, Integer> _argumentCountMap = null;

	/**
	 * 
	 */
	static public Vector<String> _constants = null;

	/**
	 * 
	 */
	public ExpressionManager() {
		super();
	}

	/**
	 * 
	 */
	protected void initialize() {
		if ( null != _functions && null != _constants)
			return;

		Object[][] functionWords = new Object[][] {
			{ "abs", new Integer( 1)},
			{ "acos", new Integer( 1)},
			{ "asin", new Integer( 1)},
			{ "atan", new Integer( 1)},
			{ "atan2", new Integer( 2)},
			{ "ceil", new Integer( 1)},
			{ "cos", new Integer( 1)},
			{ "exp", new Integer( 1)},
			{ "floor", new Integer( 1)},
			{ "IEEEremainder", new Integer( 2)},
			{ "log", new Integer( 1)},
			{ "max", new Integer( 2)},
			{ "min", new Integer( 2)},
			{ "pow", new Integer( 2)},
			{ "random", new Integer( 0)},
			{ "rint", new Integer( 1)},
			{ "round", new Integer( 1)},
			{ "sin", new Integer( 1)},
			{ "sqrt", new Integer( 1)},
			{ "tan", new Integer( 1)},
			{ "toDegrees", new Integer( 1)},
			{ "toRadians", new Integer( 1)}
		};

		_functions = new Vector<String>();
		_argumentCountMap = new HashMap<String, Integer>();
		for ( int i = 0; i < functionWords.length; ++i) {
			_functions.add( ( String)functionWords[ i][ 0]);
			_argumentCountMap.put( ( String)functionWords[ i][ 0], ( Integer)functionWords[ i][ 1]);
		}


		String[] constantWords = new String[] {
			"E",
			"PI"
		};

		_constants = new Vector<String>();
		for ( String constantWord:constantWords)
			_constants.add( constantWord);
	}

	/**
	 * 
	 */
	public void cleanup() {
		Iterator iterator = entrySet().iterator();
		while ( iterator.hasNext()) {
			Object object = iterator.next();
			Map.Entry entry = ( Map.Entry)object;
			Expression expression = ( Expression)entry.getValue();
			expression.cleanup();
		}
		clear();
	}

	/**
	 * @param word
	 * @return
	 */
	public static boolean is_reserved_word(String word) {
		return ( _functions.contains( word) || _constants.contains( word));
	}

	/**
	 * @return
	 */
	public Map<String, Vector<String[]>> get_variable_map() {
		HashMap<String, Vector<String[]>> variableMap = new HashMap<String, Vector<String[]>>();
		Iterator iterator = entrySet().iterator();
		while ( iterator.hasNext()) {
			Object object = iterator.next();
			Map.Entry entry = ( Map.Entry)object;
			Expression expression = ( Expression)entry.getValue();

			if ( expression._value[ 1].equals( ""))
				variableMap.put( expression.get_function(), new Vector<String[]>());
			else {
				String[] words = expression._value[ 1].split( ",");
				if ( null == words || 0 == words.length)
					continue;

				Vector<String[]> variables = new Vector<String[]>();
				for ( int i = 0; i < words.length; ++i) {
					if ( words[ i].equals( ""))
						continue;

					variables.add( new String[] { words[ i], "0.0"});
				}

				variableMap.put( expression.get_function(), variables);
			}
		}

		return variableMap;
	}

	/**
	 * @return
	 */
	public String[] get_functions() {
		if ( isEmpty())
			return null;

		Vector<String> functions = new Vector<String>();

		Iterator iterator = entrySet().iterator();
		while ( iterator.hasNext()) {
			Object object = iterator.next();
			Map.Entry entry = ( Map.Entry)object;
			Expression expression = ( Expression)entry.getValue();
			functions.add( expression.get_function());
		}

		return functions.toArray( new String[ 0]);
	}

	/**
	 * @param function
	 * @return
	 */
	public String get_expression(String function) {
		if ( null == function)
			return "";

		String[] words = function.split( "\\(");
		if ( null == words || 2 > words.length)
			return "";

		Expression expression = get( words[ 0]);
		if ( null == expression)
			return "";

		return expression._value[ 2];
	}

	/**
	 * @return
	 */
	public Vector<String> get_all_functions() {
		Vector<String> allFunctions = ( Vector<String>)_functions.clone();
		allFunctions.addAll( new Vector( keySet()));
		return allFunctions;
	}

	/**
	 * @return
	 */
	public Map<String, Integer> get_all_argument_count_map() {
		HashMap<String, Integer> allArgumentCountMap = ( HashMap<String, Integer>)_argumentCountMap.clone();
		Iterator iterator = entrySet().iterator();
		while ( iterator.hasNext()) {
			Object object = iterator.next();
			Map.Entry entry = ( Map.Entry)object;
			Expression expression = ( Expression)entry.getValue();
			Integer argumentCountInteger = expression.get_argument_count();
			if ( null == argumentCountInteger)
				return null;

			allArgumentCountMap.put( expression._value[ 0], argumentCountInteger);
		}
		return allArgumentCountMap;
	}

	/**
	 * @param expressionString
	 * @param variables
	 * @param excludedFunction
	 * @return
	 */
	public boolean is_correct(String expressionString, Vector<String> variables, String excludedFunction) {
		Map<String, Integer> allArgumentCountMap = get_all_argument_count_map();
		if ( null == allArgumentCountMap)
			return false;

		Vector<String> allFunctions = get_all_functions();

		if ( null != excludedFunction) {
			int index = allFunctions.indexOf( excludedFunction);
			if ( -1 < index)
				allFunctions.removeElementAt( index);
		}

		ExpressionManipulator expressionManipulator = new ExpressionManipulator();
		return expressionManipulator.is_correct( expressionString, allFunctions, allArgumentCountMap, _constants, variables);
	}

	/**
	 * @param expressionString
	 * @return
	 */
	public String expand(String expressionString) {
		Map<String, Integer> allArgumentCountMap = get_all_argument_count_map();
		if ( null == allArgumentCountMap)
			return null;

		Vector<String> allFunctions = get_all_functions();

		return ExpressionManipulator.expand( expressionString, allFunctions, allArgumentCountMap, _functions, _constants, this);
	}

	/**
	 * @param expressionString
	 * @param variables
	 * @param variableMap
	 * @return
	 */
	public String get(String expressionString, Vector<String> variables, Map<String, String> variableMap) {
		ExpressionManipulator expressionManipulator = new ExpressionManipulator();
		if ( !expressionManipulator.parse(
			expressionString,
			get_all_functions(),
			_constants,
			variables))
			return null;

		String result = expressionManipulator.get( variableMap);
		if ( result.equals( ""))
			return null;

//		System.out.println( expressionManipulator.parse(
//			expression_string,
//			get_all_functions(),
//			_constants,
//			variables));

		return result;
	}

	/**
	 * @param expression_string
	 * @param variables
	 * @param functionNames
	 * @return
	 */
	public String get(String expression_string, Vector<String> variables, String[] functionNames) {
		ExpressionManipulator expressionManipulator = new ExpressionManipulator();
		if ( !expressionManipulator.parse(
			expression_string,
			get_all_functions(),
			_constants,
			variables))
			return null;

		String result = expressionManipulator.get( functionNames);
		if ( result.equals( ""))
			return null;

//		System.out.println( expressionManipulator.parse(
//			expression_string,
//			get_all_functions(),
//			_constants,
//			variables));

		return result;
	}

	/**
	 * @param expressionString
	 * @param variables
	 * @param from
	 * @param to
	 * @return
	 */
	public String get(String expressionString, Vector<String> variables, String from, String to) {
		ExpressionManipulator expressionManipulator = new ExpressionManipulator();
		if ( !expressionManipulator.parse(
			expressionString,
			get_all_functions(),
			_constants,
			variables))
			return null;

		String result = expressionManipulator.get( from, to);
		if ( result.equals( ""))
			return null;

//		System.out.println( expressionManipulator.parse(
//			expression_string,
//			get_all_functions(),
//			_constants,
//			variables));

		return result;
	}

//	/**
//	 * @return
//	 */
//	public Expression[] get_expressions() {
//		if ( isEmpty())
//			return null;
//
//		Vector<Expression> expressions = new Vector( values());
//		return ( Expression[])expressions.toArray( new Expression[ 0]);
//	}
//
//	/**
//	 * @param function
//	 * @return
//	 */
//	public Vector<String[]> get_variables(String function) {
//		String[] words = function.split( "\\(");
//		if ( null == words || 2 > words.length)
//			return null;
//
//		Expression expression = get( words[ 0]);
//		if ( null == expression)
//			return null;
//
//		words = expression._value[ 1].split( ",");
//		if ( null == words || 0 == words.length)
//			return null;
//
//		Vector<String[]> variables = new Vector<String[]>();
//		for ( int i = 0; i < words.length; ++i)
//			variables.add( new String[] { words[ i], "0.0"});
//
//		return variables;
//	}
}
