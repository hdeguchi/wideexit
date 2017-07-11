/**
 * 
 */
package soars.application.visualshell.object.role.base.object.legacy.command;

import soars.application.visualshell.object.role.base.object.legacy.common.keyword.KeywordRule;

/**
 * @author kurata
 *
 */
public class KeywordCommand extends KeywordRule {

	static public String[] _reservedWords = new String[] { "set ", "copy "};

	/**
	 * @param kind
	 * @param type
	 * @param value
	 */
	public KeywordCommand(String kind, String type, String value) {
		super(_reservedWords, kind, type, value);
	}
}
