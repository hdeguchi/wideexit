/**
 * 
 */
package soars.common.utility.swing.table.base.undo_redo.cell;

import javax.swing.JTable;
import javax.swing.undo.CannotRedoException;
import javax.swing.undo.CannotUndoException;

import soars.common.utility.swing.table.base.undo_redo.base.BaseUndo;
import soars.common.utility.swing.table.base.undo_redo.base.ITableUndoRedoCallBack;

/**
 * @author kurata
 *
 */
public class SetValueUndo extends BaseUndo {

	/**
	 * 
	 */
	public Object _oldValue = null;

	/**
	 * 
	 */
	public Object _newValue = null;

	/**
	 * 
	 */
	public int _row = 0;

	/**
	 * 
	 */
	public int _column = 0;

	/**
	 * @param newValue
	 * @param row
	 * @param column
	 * @param table
	 * @param tableUndoRedoCallBack
	 */
	public SetValueUndo(Object newValue, int row, int column, JTable table, ITableUndoRedoCallBack tableUndoRedoCallBack) {
		super(table, tableUndoRedoCallBack);
		_oldValue = table.getValueAt( row, column);
		_newValue = newValue;
		_row = row;
		_column = column;
		_tableUndoRedoCallBack = tableUndoRedoCallBack;
	}

	/* (non-Javadoc)
	 * @see javax.swing.undo.AbstractUndoableEdit#undo()
	 */
	public void undo() throws CannotUndoException {
		super.undo();
		_tableUndoRedoCallBack.setValueAt( "undo", this);
	}

	/* (non-Javadoc)
	 * @see javax.swing.undo.AbstractUndoableEdit#redo()
	 */
	public void redo() throws CannotRedoException {
		super.redo();
		_tableUndoRedoCallBack.setValueAt( "redo", this);
	}

	/* (non-Javadoc)
	 * @see javax.swing.undo.AbstractUndoableEdit#die()
	 */
	public void die() {
		super.die();
		_oldValue = null;
		_newValue = null;
	}
}
