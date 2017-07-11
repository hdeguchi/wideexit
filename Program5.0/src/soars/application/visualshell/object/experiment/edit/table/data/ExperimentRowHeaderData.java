/**
 * 
 */
package soars.application.visualshell.object.experiment.edit.table.data;

import soars.common.utility.swing.table.base.data.RowHeaderData;

/**
 * @author kurata
 *
 */
public class ExperimentRowHeaderData extends RowHeaderData {

	/**
	 * 
	 */
	public boolean _export = false;

	/**
	 * 
	 */
	public ExperimentRowHeaderData() {
		super();
	}

	/**
	 * @param export
	 */
	public ExperimentRowHeaderData(boolean export) {
		super();
		_export = export;
	}

	/**
	 * @param experimentRowHeaderData
	 */
	public ExperimentRowHeaderData(ExperimentRowHeaderData experimentRowHeaderData) {
		super();
		_export = experimentRowHeaderData._export;
	}
}
