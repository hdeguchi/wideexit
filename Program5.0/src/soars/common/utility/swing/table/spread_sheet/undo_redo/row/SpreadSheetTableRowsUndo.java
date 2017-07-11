/**
 * 
 */
package soars.common.utility.swing.table.spread_sheet.undo_redo.row;

import java.util.List;

import javax.swing.JTable;

import soars.common.utility.swing.table.base.undo_redo.base.ITableUndoRedoCallBack;
import soars.common.utility.swing.table.base.undo_redo.cell.SetValueUndo;
import soars.common.utility.swing.table.base.undo_redo.row.RowsUndo;

/**
 * @author kurata
 *
 */
public class SpreadSheetTableRowsUndo extends RowsUndo {

	/**
	 * 
	 */
	public List<List<SetValueUndo>> _rowHeaderSetValueUndoLists = null;

	/**
	 * @param rows
	 * @param setValueUndoLists
	 * @param table
	 * @param rowHeaderSetValueUndoLists
	 * @param rowHeaderTable
	 * @param tableUndoRedoCallBack
	 */
	public SpreadSheetTableRowsUndo(int[] rows, List<List<SetValueUndo>> setValueUndoLists, JTable table, List<List<SetValueUndo>> rowHeaderSetValueUndoLists, JTable rowHeaderTable, ITableUndoRedoCallBack tableUndoRedoCallBack) {
		super(rows, setValueUndoLists, table, tableUndoRedoCallBack);
		_rowHeaderSetValueUndoLists = rowHeaderSetValueUndoLists;
		setup( _rowHeaderSetValueUndoLists, rowHeaderTable);
	}

	/* (non-Javadoc)
	 * @see javax.swing.undo.AbstractUndoableEdit#die()
	 */
	public void die() {
		super.die();
		_rows = null;
		_rowHeaderSetValueUndoLists = null;
	}
}
