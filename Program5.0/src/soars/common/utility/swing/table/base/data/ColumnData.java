/*
 * Created on 2006/10/24
 */
package soars.common.utility.swing.table.base.data;

import java.util.ArrayList;

/**
 * @author kurata
 * @param <T>
 */
public class ColumnData<T> extends ArrayList<T> {

	/**
	 * 
	 */
	public int _column = 0;

	/**
	 * @param column
	 */
	public ColumnData(int column) {
		super();
		_column = column;
	}
}
