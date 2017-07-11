/*
 * Created on 2006/10/24
 */
package soars.common.utility.swing.table.base.data;

import java.util.ArrayList;

/**
 * @author kurata
 * @param <T>
 */
public class BlockData<T> extends ArrayList<T> {

	/**
	 * 
	 */
	public int _row = 0;

	/**
	 * 
	 */
	public int _column = 0;

	/**
	 * @param row
	 * @param column
	 */
	public BlockData(int row, int column) {
		super();
		_row = row;
		_column = column;
	}
}
