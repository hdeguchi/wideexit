/*
 * Created on 2006/10/24
 */
package soars.common.utility.swing.table.base.data;

import java.util.ArrayList;

/**
 * @author kurata
 * @param <T>
 */
public class RowData<T> extends ArrayList<T> {

	/**
	 * 
	 */
	public int _row = 0;

	/**
	 * 
	 */
	public RowHeaderData _rowHeaderData = null;

	/**
	 * @param row
	 */
	public RowData(int row) {
		super();
		_row = row;
	}

	/**
	 * @param row
	 * @param rowHeaderData
	 */
	public RowData(int row, RowHeaderData rowHeaderData) {
		super();
		_row = row;
		_rowHeaderData = rowHeaderData;
	}
}
