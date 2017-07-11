/**
 * 
 */
package soars.application.visualshell.object.role.base.object.legacy.condition;

import soars.application.visualshell.object.role.base.object.legacy.common.keyword.KeywordRule;

/**
 * @author kurata
 *
 */
public class KeywordCondition extends KeywordRule {

	static public String[] _reservedWords = new String[] { "is ", "equals "};

	/**
	 * @param kind
	 * @param type
	 * @param value
	 */
	public KeywordCondition(String kind, String type, String value) {
		super(_reservedWords, kind, type, value);
	}
}
