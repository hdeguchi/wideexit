/**
 * 
 */
package soars.application.visualshell.object.role.base.edit.table.data;

import soars.common.utility.swing.table.base.data.RowHeaderData;

/**
 * @author kurata
 *
 */
public class RuleRowData extends RowHeaderData {

	/**
	 * 
	 */
	public String _comment = "";

	/**
	 * 
	 */
	public RuleRowData() {
		super();
	}

	/**
	 * @param comment
	 */
	public RuleRowData(String comment) {
		super();
		_comment = comment;
	}

	/**
	 * @param ruleRowData
	 */
	public RuleRowData(RuleRowData ruleRowData) {
		super();
		_comment = ruleRowData._comment;
	}
}
