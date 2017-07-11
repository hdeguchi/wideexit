/**
 * 
 */
package soars.common.utility.swing.table.base.undo_redo.column;

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
public class InsertColumnsUndo extends BaseUndo {

	/**
	 * 
	 */
	public int[] _columns = null;

	/**
	 * 
	 */
	public int _width;

	/**
	 * @param columns
	 * @param width
	 * @param table
	 * @param tableUndoRedoCallBack
	 */
	public InsertColumnsUndo(int[] columns, int width, JTable table, ITableUndoRedoCallBack tableUndoRedoCallBack) {
		super(table, tableUndoRedoCallBack);
		_columns = columns;
		_width = width;
		_tableUndoRedoCallBack = tableUndoRedoCallBack;
		initialize();
	}

	/**
	 * 
	 */
	private void initialize() {
		if ( null == _columns || 0 == _columns.length)
			return;

		Arrays.sort( _columns);
	}

	/* (non-Javadoc)
	 * @see javax.swing.undo.AbstractUndoableEdit#undo()
	 */
	public void undo() throws CannotUndoException {
		super.undo();
		_tableUndoRedoCallBack.insertColumns( "undo", this);
	}

	/* (non-Javadoc)
	 * @see javax.swing.undo.AbstractUndoableEdit#redo()
	 */
	public void redo() throws CannotRedoException {
		super.redo();
		_tableUndoRedoCallBack.insertColumns( "redo", this);
	}

	/* (non-Javadoc)
	 * @see javax.swing.undo.AbstractUndoableEdit#die()
	 */
	public void die() {
		super.die();
		_columns = null;
	}
}
