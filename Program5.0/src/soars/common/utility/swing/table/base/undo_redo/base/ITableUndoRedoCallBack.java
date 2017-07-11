/**
 * 
 */
package soars.common.utility.swing.table.base.undo_redo.base;

import soars.common.utility.swing.table.base.undo_redo.cell.SetValueUndo;
import soars.common.utility.swing.table.base.undo_redo.cell.SetValuesUndo;
import soars.common.utility.swing.table.base.undo_redo.column.ColumnsUndo;
import soars.common.utility.swing.table.base.undo_redo.column.InsertColumnsBlockUndo;
import soars.common.utility.swing.table.base.undo_redo.column.InsertColumnsUndo;
import soars.common.utility.swing.table.base.undo_redo.column.RemoveColumnsBlockUndo;
import soars.common.utility.swing.table.base.undo_redo.column.RemoveColumnsUndo;
import soars.common.utility.swing.table.base.undo_redo.row.InsertRowsBlockUndo;
import soars.common.utility.swing.table.base.undo_redo.row.InsertRowsUndo;
import soars.common.utility.swing.table.base.undo_redo.row.RemoveRowsBlockUndo;
import soars.common.utility.swing.table.base.undo_redo.row.RemoveRowsUndo;
import soars.common.utility.swing.table.base.undo_redo.row.RowsUndo;

/**
 * @author kurata
 *
 */
public interface ITableUndoRedoCallBack {

	/**
	 * @param kind
	 * @param setValueUndo
	 */
	void setValueAt(String kind, SetValueUndo setValueUndo);

	/**
	 * @param kind
	 * @param setValuesUndo
	 */
	void setValuesAt(String kind, SetValuesUndo setValuesUndo);

	/**
	 * @param kind
	 * @param insertRowsUndo
	 */
	void insertRows(String kind, InsertRowsUndo insertRowsUndo);

	/**
	 * @param kind
	 * @param removeRowsUndo
	 */
	void removeRows(String kind, RemoveRowsUndo removeRowsUndo);

	/**
	 * @param kind
	 * @param rowsUndo
	 */
	void setValuesAt(String kind, RowsUndo rowsUndo);

	/**
	 * @param kind
	 * @param insertRowsBlockUndo
	 */
	void insertRowsBlock(String kind, InsertRowsBlockUndo insertRowsBlockUndo);

	/**
	 * @param kind
	 * @param removeRowsBlockUndo
	 */
	void removeRowsBlock(String kind, RemoveRowsBlockUndo removeRowsBlockUndo);

	/**
	 * @param kind
	 * @param insertColumnsUndo
	 */
	void insertColumns(String kind, InsertColumnsUndo insertColumnsUndo);

	/**
	 * @param kind
	 * @param removeColumnsUndo
	 */
	void removeColumns(String kind, RemoveColumnsUndo removeColumnsUndo);

	/**
	 * @param kind
	 * @param columnsUndo
	 */
	void setValuesAt(String kind, ColumnsUndo columnsUndo);

	/**
	 * @param kind
	 * @param insertColumnsBlockUndo
	 */
	void insertColumnsBlock(String kind, InsertColumnsBlockUndo insertColumnsBlockUndo);

	/**
	 * @param kind
	 * @param removeColumnsBlockUndo
	 */
	void removeColumnsBlock(String kind, RemoveColumnsBlockUndo removeColumnsBlockUndo);
}
