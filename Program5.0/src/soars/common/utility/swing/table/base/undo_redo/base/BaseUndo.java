/**
 * 
 */
package soars.common.utility.swing.table.base.undo_redo.base;

import javax.swing.JTable;
import javax.swing.undo.AbstractUndoableEdit;

/**
 * @author kurata
 *
 */
public class BaseUndo extends AbstractUndoableEdit {

	/**
	 * 
	 */
	public JTable _table = null;

	/**
	 * 
	 */
	protected ITableUndoRedoCallBack _tableUndoRedoCallBack = null;

	/**
	 * @param table 
	 * @param tableUndoRedoCallBack 
	 */
	public BaseUndo(JTable table, ITableUndoRedoCallBack tableUndoRedoCallBack) {
		super();
		_table = table;
		_tableUndoRedoCallBack = tableUndoRedoCallBack;
	}
}
