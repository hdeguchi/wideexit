/**
 * 
 */
package soars.common.utility.swing.table.base.undo_redo.column;

import javax.swing.JTable;
import javax.swing.undo.CannotRedoException;
import javax.swing.undo.CannotUndoException;

import soars.common.utility.swing.table.base.undo_redo.base.BaseUndo;
import soars.common.utility.swing.table.base.undo_redo.base.ITableUndoRedoCallBack;

/**
 * @author kurata
 *
 */
public class RemoveColumnsBlockUndo extends BaseUndo {

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
	 * 
	 */
	public Object[][] _objects = null;

	/**
	 * @param rowFrom
	 * @param rowTo
	 * @param columnFrom
	 * @param columnTo
	 * @param table
	 * @param tableUndoRedoCallBack
	 */
	public RemoveColumnsBlockUndo(int rowFrom, int rowTo, int columnFrom, int columnTo, JTable table, ITableUndoRedoCallBack tableUndoRedoCallBack) {
		super(table, tableUndoRedoCallBack);
		_rowFrom = rowFrom;
		_rowTo = rowTo;
		_columnFrom = columnFrom;
		_columnTo = columnTo;
		_tableUndoRedoCallBack = tableUndoRedoCallBack;
		_objects = setup( table);
	}

	/**
	 * @param table
	 * @return
	 */
	private Object[][] setup(JTable table) {
		Object[][] objects = new Object[ _rowTo - _rowFrom + 1][ _columnTo - _columnFrom + 1];
		for ( int row = _rowFrom; row <= _rowTo; ++row) {
			for ( int column = _columnFrom; column <= _columnTo; ++column)
				objects[ row - _rowFrom][ column - _columnFrom] = table.getValueAt( row, column);
		}
		return objects;
	}

	/* (non-Javadoc)
	 * @see javax.swing.undo.AbstractUndoableEdit#undo()
	 */
	public void undo() throws CannotUndoException {
		super.undo();
		_tableUndoRedoCallBack.removeColumnsBlock( "undo", this);
	}

	/* (non-Javadoc)
	 * @see javax.swing.undo.AbstractUndoableEdit#redo()
	 */
	public void redo() throws CannotRedoException {
		super.redo();
		_tableUndoRedoCallBack.removeColumnsBlock( "redo", this);
	}
}
