/**
 * 
 */
package soars.common.utility.swing.table.spread_sheet.undo_redo.column;

import javax.swing.JTable;

import soars.common.utility.swing.table.base.undo_redo.base.ITableUndoRedoCallBack;
import soars.common.utility.swing.table.base.undo_redo.column.RemoveColumnsUndo;

/**
 * @author kurata
 *
 */
public class SpreadSheetTableRemoveColumnsUndo extends RemoveColumnsUndo {

	/**
	 * 
	 */
	public int _appendedColumnCount = 0;

	/**
	 * @param columns
	 * @param width
	 * @param table
	 * @param tableUndoRedoCallBack
	 */
	public SpreadSheetTableRemoveColumnsUndo(int[] columns, int width, JTable table, ITableUndoRedoCallBack tableUndoRedoCallBack) {
		super(columns, width, table, tableUndoRedoCallBack);
	}
}
