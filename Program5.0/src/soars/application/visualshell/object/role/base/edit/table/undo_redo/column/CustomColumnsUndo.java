/**
 * 
 */
package soars.application.visualshell.object.role.base.edit.table.undo_redo.column;

import java.util.List;

import javax.swing.JTable;

import soars.common.utility.swing.table.base.undo_redo.base.ITableUndoRedoCallBack;
import soars.common.utility.swing.table.base.undo_redo.cell.SetValueUndo;
import soars.common.utility.swing.table.base.undo_redo.column.ColumnsUndo;

/**
 * @author kurata
 *
 */
public class CustomColumnsUndo extends ColumnsUndo {

	/**
	 * 
	 */
	public List<SetValueUndo> _setValueUndoListForOr = null;

	/**
	 * @param columns
	 * @param setValueUndoLists
	 * @param setValueUndoListForOr
	 * @param table
	 * @param tableUndoRedoCallBack
	 */
	public CustomColumnsUndo(int[] columns, List<List<SetValueUndo>> setValueUndoLists, List<SetValueUndo> setValueUndoListForOr, JTable table, ITableUndoRedoCallBack tableUndoRedoCallBack) {
		super(columns, setValueUndoLists, table, tableUndoRedoCallBack);
		_setValueUndoListForOr = setValueUndoListForOr;
	}
}
