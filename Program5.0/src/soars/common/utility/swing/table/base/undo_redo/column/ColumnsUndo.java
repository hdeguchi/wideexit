/**
 * 
 */
package soars.common.utility.swing.table.base.undo_redo.column;

import java.util.Arrays;
import java.util.List;

import javax.swing.JTable;
import javax.swing.undo.CannotRedoException;
import javax.swing.undo.CannotUndoException;

import soars.common.utility.swing.table.base.undo_redo.base.BaseUndo;
import soars.common.utility.swing.table.base.undo_redo.base.ITableUndoRedoCallBack;
import soars.common.utility.swing.table.base.undo_redo.cell.SetValueUndo;

/**
 * @author kurata
 *
 */
public class ColumnsUndo extends BaseUndo {

	/**
	 * 
	 */
	public int[] _columns = null;

	/**
	 * 
	 */
	public List<List<SetValueUndo>> _setValueUndoLists = null;

	/**
	 * @param columns
	 * @param setValueUndoLists
	 * @param table
	 * @param tableUndoRedoCallBack
	 */
	public ColumnsUndo(int[] columns, List<List<SetValueUndo>> setValueUndoLists, JTable table, ITableUndoRedoCallBack tableUndoRedoCallBack) {
		super(table, tableUndoRedoCallBack);
		_columns = columns;
		_setValueUndoLists = setValueUndoLists;
		_tableUndoRedoCallBack = tableUndoRedoCallBack;
		initialize();
		setup( _setValueUndoLists, table);
	}

	/**
	 * 
	 */
	private void initialize() {
		if ( null == _columns || 0 == _columns.length)
			return;

		Arrays.sort( _columns);
	}

	/**
	 * @param setValueUndoLists
	 * @param table
	 */
	protected void setup(List<List<SetValueUndo>> setValueUndoLists, JTable table) {
		if ( null == _columns || 0 == _columns.length)
			return;

		if ( null == setValueUndoLists || setValueUndoLists.isEmpty())
			return;

		for ( int i = 0; i < _columns.length; ++i) {
			if ( null == setValueUndoLists.get( i) || setValueUndoLists.get( i).isEmpty())
				return;

			for ( SetValueUndo setValueUndo:setValueUndoLists.get( i))
				setValueUndo._oldValue = table.getValueAt( setValueUndo._row, setValueUndo._column);
		}
	}

	/* (non-Javadoc)
	 * @see javax.swing.undo.AbstractUndoableEdit#undo()
	 */
	public void undo() throws CannotUndoException {
		super.undo();
		_tableUndoRedoCallBack.setValuesAt( "undo", this);
	}

	/* (non-Javadoc)
	 * @see javax.swing.undo.AbstractUndoableEdit#redo()
	 */
	public void redo() throws CannotRedoException {
		super.redo();
		_tableUndoRedoCallBack.setValuesAt( "redo", this);
	}

	/* (non-Javadoc)
	 * @see javax.swing.undo.AbstractUndoableEdit#die()
	 */
	public void die() {
		super.die();
		_columns = null;
		_setValueUndoLists = null;
	}
}
