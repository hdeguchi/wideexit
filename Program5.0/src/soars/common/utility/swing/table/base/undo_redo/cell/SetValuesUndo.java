/**
 * 
 */
package soars.common.utility.swing.table.base.undo_redo.cell;

import java.util.List;

import javax.swing.JTable;
import javax.swing.undo.CannotRedoException;
import javax.swing.undo.CannotUndoException;

import soars.common.utility.swing.table.base.undo_redo.base.BaseUndo;
import soars.common.utility.swing.table.base.undo_redo.base.ITableUndoRedoCallBack;

/**
 * @author kurata
 *
 */
public class SetValuesUndo extends BaseUndo {

	/**
	 * 
	 */
	public List<SetValueUndo> _setValueUndoList = null;

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
	 * @param setValueUndoList
	 * @param table
	 * @param tableUndoRedoCallBack
	 */
	public SetValuesUndo(List<SetValueUndo> setValueUndoList, JTable table, ITableUndoRedoCallBack tableUndoRedoCallBack) {
		super(table, tableUndoRedoCallBack);
		_setValueUndoList = setValueUndoList;
		_tableUndoRedoCallBack = tableUndoRedoCallBack;
		setup( table);
	}

	/**
	 * @param table 
	 */
	private void setup(JTable table) {
		if ( null == _setValueUndoList || _setValueUndoList.isEmpty())
			return;

		_rowFrom = _rowTo = _setValueUndoList.get( 0)._row;
		_columnFrom = _columnTo = _setValueUndoList.get( 0)._column;

		for ( SetValueUndo setValueUndo:_setValueUndoList) {
			setValueUndo._oldValue = table.getValueAt( setValueUndo._row, setValueUndo._column);
			_rowFrom = Math.min( _rowFrom, setValueUndo._row);
			_rowTo = Math.max( _rowTo, setValueUndo._row);
			_columnFrom = Math.min( _columnFrom, setValueUndo._column);
			_columnTo = Math.max( _columnTo, setValueUndo._column);
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
		_setValueUndoList = null;
	}
}
