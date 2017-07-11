/**
 * 
 */
package soars.application.visualshell.object.role.base.edit.table.undo_redo.column;

import java.util.List;

import javax.swing.JTable;

import soars.common.utility.swing.table.base.undo_redo.base.ITableUndoRedoCallBack;
import soars.common.utility.swing.table.base.undo_redo.cell.SetValueUndo;
import soars.common.utility.swing.table.spread_sheet.undo_redo.column.SpreadSheetTableRemoveColumnsUndo;

/**
 * @author kurata
 *
 */
public class CustomRemoveColumnsUndo extends SpreadSheetTableRemoveColumnsUndo {

	/**
	 * 
	 */
	public List<SetValueUndo> _setValueUndoListForOr = null;

	/**
	 * @param columns
	 * @param width
	 * @param setValueUndoListForOr
	 * @param table
	 * @param tableUndoRedoCallBack
	 */
	public CustomRemoveColumnsUndo(int[] columns, int width, List<SetValueUndo> setValueUndoListForOr, JTable table, ITableUndoRedoCallBack tableUndoRedoCallBack) {
		super(columns, width, table, tableUndoRedoCallBack);
		_setValueUndoListForOr = setValueUndoListForOr;
	}
}
