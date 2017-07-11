/**
 * 
 */
package soars.common.utility.swing.table.spread_sheet.undo_redo.row;

import javax.swing.JTable;

import soars.common.utility.swing.table.base.undo_redo.base.ITableUndoRedoCallBack;
import soars.common.utility.swing.table.base.undo_redo.row.RemoveRowsUndo;

/**
 * @author kurata
 *
 */
public class SpreadSheetTableRemoveRowsUndo extends RemoveRowsUndo {

	/**
	 * 
	 */
	public Object[][] _rowHeaderObjects = null;

	/**
	 * 
	 */
	public int _appendedRowCount = 0;

	/**
	 * @param rows
	 * @param table
	 * @param rowHeaderTable
	 * @param tableUndoRedoCallBack
	 */
	public SpreadSheetTableRemoveRowsUndo(int[] rows, JTable table, JTable rowHeaderTable, ITableUndoRedoCallBack tableUndoRedoCallBack) {
		super(rows, table, tableUndoRedoCallBack);
		_rowHeaderObjects = setup( rowHeaderTable);
	}

	/* (non-Javadoc)
	 * @see javax.swing.undo.AbstractUndoableEdit#die()
	 */
	public void die() {
		super.die();
		_rowHeaderObjects = null;
		_appendedRowCount = 0;
	}
}
