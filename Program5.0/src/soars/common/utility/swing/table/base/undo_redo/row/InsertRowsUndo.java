/**
 * 
 */
package soars.common.utility.swing.table.base.undo_redo.row;

import java.util.Arrays;

import javax.swing.JTable;
import javax.swing.undo.CannotRedoException;
import javax.swing.undo.CannotUndoException;

import soars.common.utility.swing.table.base.undo_redo.base.BaseUndo;
import soars.common.utility.swing.table.base.undo_redo.base.ITableUndoRedoCallBack;

/**
 * @author kurata
 *
 */
public class InsertRowsUndo extends BaseUndo {

	/**
	 * 
	 */
	public int[] _rows = null;

	/**
	 * @param rows
	 * @param table
	 * @param tableUndoRedoCallBack
	 */
	public InsertRowsUndo(int[] rows, JTable table, ITableUndoRedoCallBack tableUndoRedoCallBack) {
		super(table, tableUndoRedoCallBack);
		_rows = rows;
		_tableUndoRedoCallBack = tableUndoRedoCallBack;
		initialize();
	}

	/**
	 * 
	 */
	private void initialize() {
		if ( null == _rows || 0 == _rows.length)
			return;

		Arrays.sort( _rows);
	}

	/* (non-Javadoc)
	 * @see javax.swing.undo.AbstractUndoableEdit#undo()
	 */
	public void undo() throws CannotUndoException {
		super.undo();
		_tableUndoRedoCallBack.insertRows( "undo", this);
	}

	/* (non-Javadoc)
	 * @see javax.swing.undo.AbstractUndoableEdit#redo()
	 */
	public void redo() throws CannotRedoException {
		super.redo();
		_tableUndoRedoCallBack.insertRows( "redo", this);
	}

	/* (non-Javadoc)
	 * @see javax.swing.undo.AbstractUndoableEdit#die()
	 */
	public void die() {
		super.die();
		_rows = null;
	}
}
