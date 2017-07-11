/**
 * 
 */
package soars.common.utility.swing.table.base.undo_redo.column;

import java.awt.Rectangle;

import javax.swing.JTable;
import javax.swing.table.DefaultTableColumnModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableColumn;
import javax.swing.undo.CannotRedoException;
import javax.swing.undo.CannotUndoException;

import soars.common.utility.swing.table.base.undo_redo.base.BaseUndo;
import soars.common.utility.swing.table.base.undo_redo.base.ITableUndoRedoCallBack;

/**
 * @author kurata
 *
 */
public class ColumnWidthUndo extends BaseUndo {

	/**
	 * 
	 */
	public JTableHeader _tableHeader = null;

	/**
	 * 
	 */
	public int _column;

	/**
	 * 
	 */
	public int oldWidth;

	/**
	 * 
	 */
	public int _newWidth;

	/**
	 * @param tableColumn
	 * @param tableHeader
	 * @param table
	 * @param tableUndoRedoCallBack
	 */
	public ColumnWidthUndo(TableColumn tableColumn, JTableHeader tableHeader, JTable table, ITableUndoRedoCallBack tableUndoRedoCallBack) {
		super(table, tableUndoRedoCallBack);
		_tableHeader = tableHeader;
		_column = tableColumn.getModelIndex();
		oldWidth = tableColumn.getPreferredWidth();
	}

	/**
	 * 
	 */
	public void resize_completed() {
		DefaultTableColumnModel defaultTableColumnModel = ( DefaultTableColumnModel)_tableHeader.getColumnModel();
		_newWidth = defaultTableColumnModel.getColumn( _column).getPreferredWidth();
	}

	/* (non-Javadoc)
	 * @see javax.swing.undo.AbstractUndoableEdit#undo()
	 */
	public void undo() throws CannotUndoException {
		super.undo();
		set_column_width( oldWidth);
	}

	/* (non-Javadoc)
	 * @see javax.swing.undo.AbstractUndoableEdit#redo()
	 */
	public void redo() throws CannotRedoException {
		super.redo();
		set_column_width( _newWidth);
	}

	/**
	 * @param width
	 */
	private void set_column_width(int width) {
		DefaultTableColumnModel defaultTableColumnModel = ( DefaultTableColumnModel)_tableHeader.getColumnModel();
		defaultTableColumnModel.getColumn( _column).setPreferredWidth( width);
		Rectangle rectangle = _table.getCellRect( 0, _column, true);
		_table.scrollRectToVisible( rectangle);
	}

	/* (non-Javadoc)
	 * @see javax.swing.undo.AbstractUndoableEdit#die()
	 */
	public void die() {
		super.die();
		_tableHeader = null;
	}
}
