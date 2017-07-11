/**
 * 
 */
package soars.common.utility.swing.table.base.undo_redo.row;

import javax.swing.JTable;
import javax.swing.undo.CannotRedoException;
import javax.swing.undo.CannotUndoException;

import soars.common.utility.swing.table.base.undo_redo.base.BaseUndo;
import soars.common.utility.swing.table.base.undo_redo.base.ITableUndoRedoCallBack;

/**
 * @author kurata
 *
 */
public class InsertRowsBlockUndo extends BaseUndo {

	/**
	 * 
	 */
	public int _rowFrom;

	/**
	 * 
	 */
	public int _rowTo;

	/**
	 * 
	 */
	public int _columnFrom;

	/**
	 * 
	 */
	public int _columnTo;

	/**
	 * @param rowFrom
	 * @param rowTo
	 * @param columnFrom
	 * @param columnTo
	 * @param table
	 * @param tableUndoRedoCallBack
	 */
	public InsertRowsBlockUndo(int rowFrom, int rowTo, int columnFrom, int columnTo, JTable table, ITableUndoRedoCallBack tableUndoRedoCallBack) {
		super(table, tableUndoRedoCallBack);
		_rowFrom = rowFrom;
		_rowTo = rowTo;
		_columnFrom = columnFrom;
		_columnTo = columnTo;
		_tableUndoRedoCallBack = tableUndoRedoCallBack;
	}

	/* (non-Javadoc)
	 * @see javax.swing.undo.AbstractUndoableEdit#undo()
	 */
	public void undo() throws CannotUndoException {
		super.undo();
		_tableUndoRedoCallBack.insertRowsBlock( "undo", this);
	}

	/* (non-Javadoc)
	 * @see javax.swing.undo.AbstractUndoableEdit#redo()
	 */
	public void redo() throws CannotRedoException {
		super.redo();
		_tableUndoRedoCallBack.insertRowsBlock( "redo", this);
	}
}
