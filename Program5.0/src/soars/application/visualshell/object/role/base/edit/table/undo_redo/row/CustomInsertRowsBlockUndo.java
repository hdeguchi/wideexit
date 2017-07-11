/**
 * 
 */
package soars.application.visualshell.object.role.base.edit.table.undo_redo.row;

import java.util.List;

import javax.swing.JTable;

import soars.common.utility.swing.table.base.undo_redo.base.ITableUndoRedoCallBack;
import soars.common.utility.swing.table.base.undo_redo.cell.SetValueUndo;
import soars.common.utility.swing.table.base.undo_redo.row.InsertRowsBlockUndo;

/**
 * @author kurata
 *
 */
public class CustomInsertRowsBlockUndo extends InsertRowsBlockUndo {

	/**
	 * 
	 */
	public List<SetValueUndo> _setValueUndoListForOr = null;

	/**
	 * @param rowFrom
	 * @param rowTo
	 * @param columnFrom
	 * @param columnTo
	 * @param setValueUndoListForOr
	 * @param table
	 * @param tableUndoRedoCallBack
	 */
	public CustomInsertRowsBlockUndo(int rowFrom, int rowTo, int columnFrom, int columnTo, List<SetValueUndo> setValueUndoListForOr, JTable table, ITableUndoRedoCallBack tableUndoRedoCallBack) {
		super(rowFrom, rowTo, columnFrom, columnTo, table, tableUndoRedoCallBack);
		_setValueUndoListForOr = setValueUndoListForOr;
	}
}
