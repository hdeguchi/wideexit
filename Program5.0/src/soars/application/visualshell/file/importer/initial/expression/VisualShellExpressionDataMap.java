/**
 * 
 */
package soars.application.visualshell.file.importer.initial.expression;

import java.util.Vector;

import soars.application.visualshell.main.Constant;
import soars.application.visualshell.object.expression.VisualShellExpressionManager;
import soars.common.utility.tool.expression.Expression;

/**
 * The expression data hashtable(function name[String] - the instance of Expression class).
 * @author kurata / SOARS project
 */
public class VisualShellExpressionDataMap extends VisualShellExpressionManager {

	/**
	 * Creates this object with the specified expression data hashtable.
	 * @param visualShellExpressionManager the specified expression data hashtable
	 */
	public VisualShellExpressionDataMap(VisualShellExpressionManager visualShellExpressionManager) {
		super(visualShellExpressionManager);
	}

	/**
	 * Returns true for appending the expression data successfully.
	 * @param words the array of words extracted from the specified line
	 * @param line the specified line
	 * @param number the line number
	 * @return true for appending the expression data successfully
	 */
	public boolean append(String[] words, String line, int number) {
		if ( words[ 1].equals( "")
			|| !is_valid_word( words[ 1], Constant._prohibitedExpressionCharacters1)
			|| !words[ 1].matches( "[^0-9]+.*")
			|| is_reserved_word( words[ 1]))
			return false;

		Expression expression = ( Expression)get( words[ 1]);
		if ( null != expression) {
			if ( expression._value[ 1].equals( words[ 2]) && expression._value[ 2].equals( words[ 3]))
				return true;

			return false;
		} else {
			if ( words[ 2].equals( "")
				|| !is_valid_word( words[ 2], Constant._prohibitedExpressionCharacters2))
				return false;

			if ( words[ 3].equals( "")
				|| !is_valid_word( words[ 3], Constant._prohibitedExpressionCharacters3))
				return false;

			Vector<String> variables;
			if ( words[ 2].equals( ""))
				variables = new Vector<String>();
			else {
				if ( words[ 2].endsWith( ","))
					return false;

				variables = Expression.get_variables( words[ 2]);
				if ( null == variables)
					return false;

				if ( variables.contains( words[ 1]))
					return false;
			}

			if ( !is_correct( words[ 3], variables, words[ 1]))
				return false;

			put( words[ 1], new Expression( words[ 1], words[ 2], words[ 3]));
		}

		return true;
	}

	/**
	 * @param word
	 * @param prohibited_characters
	 * @return
	 */
	private boolean is_valid_word(String word, String prohibited_characters) {
		for ( int i = 0; i < prohibited_characters.length(); ++i) {
			if ( 0 <= word.indexOf( prohibited_characters.charAt( i)))
				return false;
		}
		return true;
	}
}
