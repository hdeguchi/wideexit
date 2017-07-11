/*
 * Created on 2005/10/31
 */
package soars.application.visualshell.object.role.base.object.legacy.common.expression;

import java.util.Vector;

/**
 * @author kurata
 */
public class ExpressionElements {

	/**
	 * 
	 */
	public String _function = "";

	/**
	 * 
	 */
	public Vector<String[]> _variables = new Vector<String[]>();

	/**
	 * 
	 */
	public ExpressionElements() {
		super();
	}

	/**
	 * @param numberObject
	 * @param function
	 * @param variables
	 */
	public ExpressionElements(String numberObject, String function, Vector<String[]> variables) {
		super();
		_function = function;
		_variables = variables;
	}

	/**
	 * @param numberObject
	 * @param operator
	 * @param function
	 * @param variables
	 */
	public ExpressionElements(String numberObject, String operator, String function, Vector<String[]> variables) {
		super();
		_function = function;
		_variables = variables;
	}

	/**
	 * @param function
	 * @param variables
	 */
	public ExpressionElements(String function, Vector<String[]> variables) {
		super();
		_function = function;
		_variables = variables;
	}

	/**
	 * @return
	 */
	public String get_function() {
		String[] words = _function.split( "\\(");
		if ( null == words || 2 > words.length)
			return null;

		return words[ 0];
	}

	/**
	 * @param originalFunction
	 * @param newFunction
	 * @return
	 */
	public boolean update_function(String originalFunction, String newFunction) {
		String[] words = _function.split( "\\(");
		if ( null == words || 2 > words.length)
			return false;

		if ( !words[ 0].equals( originalFunction))
			return true;

		_function = ( newFunction + "(" + words[ 1]);
		return true;
	}
}
