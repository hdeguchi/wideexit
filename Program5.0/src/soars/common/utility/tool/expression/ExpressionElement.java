/*
 * Created on 2005/11/05
 */
package soars.common.utility.tool.expression;

/**
 * @author kurata
 */
public class ExpressionElement {

	/**
	 * 
	 */
	public String _word = "";

	/**
	 * 
	 */
	public boolean _variable = false;

	/**
	 * 
	 */
	public boolean _constant = false;

	/**
	 * 
	 */
	public int _nextDown = -1;

	/**
	 * 
	 */
	public int _nextUp = -1;

	/**
	 * 
	 */
	public ExpressionElement() {
		super();
	}

	/**
	 * @param word
	 */
	public ExpressionElement(String word) {
		super();
		_word = word;
	}

	/**
	 * @param word
	 * @param variable
	 * @param constant
	 */
	public ExpressionElement(String word, boolean variable, boolean constant) {
		super();
		_word = word;
		_variable = variable;
		_constant = constant;
	}
}
