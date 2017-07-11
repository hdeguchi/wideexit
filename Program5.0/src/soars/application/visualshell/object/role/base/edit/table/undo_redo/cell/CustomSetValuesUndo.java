/**
 * 
 */
package soars.application.visualshell.object.role.base.edit.table.undo_redo.cell;

import java.util.List;

import javax.swing.JTable;

import soars.common.utility.swing.table.base.undo_redo.base.ITableUndoRedoCallBack;
import soars.common.utility.swing.table.base.undo_redo.cell.SetValueUndo;
import soars.common.utility.swing.table.base.undo_redo.cell.SetValuesUndo;

/**
 * @author kurata
 *
 */
public class CustomSetValuesUndo extends SetValuesUndo {

	/**
	 * 
	 */
	public List<SetValueUndo> _setValueUndoListForOr = null;

	/**
	 * @param setValueUndoList
	 * @param setValueUndoListForOr
	 * @param table
	 * @param tableUndoRedoCallBack
	 */
	public CustomSetValuesUndo(List<SetValueUndo> setValueUndoList, List<SetValueUndo> setValueUndoListForOr, JTable table, ITableUndoRedoCallBack tableUndoRedoCallBack) {
		super(setValueUndoList, table, tableUndoRedoCallBack);
		_setValueUndoListForOr = setValueUndoListForOr;
	}
}
