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
public class RemoveRowsUndo extends BaseUndo {

	/**
	 * 
	 */
	public int[] _rows = null;

	/**
	 * 
	 */
	public Object[][] _objects = null;

	/**
	 * @param rows
	 * @param table
	 * @param tableUndoRedoCallBack
	 */
	public RemoveRowsUndo(int[] rows, JTable table, ITableUndoRedoCallBack tableUndoRedoCallBack) {
		super(table, tableUndoRedoCallBack);
		_rows = rows;
		_tableUndoRedoCallBack = tableUndoRedoCallBack;
		initialize();
		_objects = setup( table);
	}

	/**
	 * 
	 */
	private void initialize() {
		if ( null == _rows || 0 == _rows.length)
			return;

		Arrays.sort( _rows);
	}

	/**
	 * @param table
	 * @return
	 */
	protected Object[][] setup(JTable table) {
		if ( null == _rows || 0 == _rows.length)
			return null;

		Object[][] objects = new Object[ _rows.length][ table.getColumnCount()];
		for ( int i = 0; i < _rows.length; ++i) {
			for ( int column = 0; column < table.getColumnCount(); ++column)
				objects[ i][ column] = table.getValueAt( _rows[ i], column);
		}

		return objects;
	}

	/* (non-Javadoc)
	 * @see javax.swing.undo.AbstractUndoableEdit#undo()
	 */
	public void undo() throws CannotUndoException {
		super.undo();
		_tableUndoRedoCallBack.removeRows( "undo", this);
	}

	/* (non-Javadoc)
	 * @see javax.swing.undo.AbstractUndoableEdit#redo()
	 */
	public void redo() throws CannotRedoException {
		super.redo();
		_tableUndoRedoCallBack.removeRows( "redo", this);
	}

	/* (non-Javadoc)
	 * @see javax.swing.undo.AbstractUndoableEdit#die()
	 */
	public void die() {
		super.die();
		_rows = null;
		_objects = null;
	}
}
