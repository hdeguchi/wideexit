/**
 * 
 */
package soars.common.utility.swing.table.base.undo_redo.column;

import java.util.Arrays;

import javax.swing.JTable;
import javax.swing.table.DefaultTableColumnModel;
import javax.swing.undo.CannotRedoException;
import javax.swing.undo.CannotUndoException;

import soars.common.utility.swing.table.base.undo_redo.base.BaseUndo;
import soars.common.utility.swing.table.base.undo_redo.base.ITableUndoRedoCallBack;

/**
 * @author kurata
 *
 */
public class RemoveColumnsUndo extends BaseUndo {

	/**
	 * 
	 */
	public int[] _columns = null;

	/**
	 * 
	 */
	public int _width;

	/**
	 * 
	 */
	public Object[][] _objects = null;

	/**
	 * 
	 */
	public int[] _widths = null;

	/**
	 * @param columns
	 * @param width
	 * @param table
	 * @param tableUndoRedoCallBack
	 */
	public RemoveColumnsUndo(int[] columns, int width, JTable table, ITableUndoRedoCallBack tableUndoRedoCallBack) {
		super(table, tableUndoRedoCallBack);
		_columns = columns;
		_width = width;
		_tableUndoRedoCallBack = tableUndoRedoCallBack;
		initialize();
		_objects = setup( table);
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
	 * @param table
	 * @return
	 */
	protected Object[][] setup(JTable table) {
		if ( null == _columns || 0 == _columns.length)
			return null;

		Object[][] objects = new Object[ table.getRowCount()][ _columns.length];
		for ( int i = 0; i < _columns.length; ++i) {
			for ( int row = 0; row < table.getRowCount(); ++row)
				objects[ row][ i] = table.getValueAt( row, _columns[ i]);
		}

		_widths = new int[ _columns.length];
		DefaultTableColumnModel defaultTableColumnModel = ( DefaultTableColumnModel)table.getColumnModel();
		for ( int i = 0; i < _columns.length; ++i)
			_widths[ i] = defaultTableColumnModel.getColumn( _columns[ i]).getPreferredWidth();
	
		return objects;
	}

	/* (non-Javadoc)
	 * @see javax.swing.undo.AbstractUndoableEdit#undo()
	 */
	public void undo() throws CannotUndoException {
		super.undo();
		_tableUndoRedoCallBack.removeColumns( "undo", this);
	}

	/* (non-Javadoc)
	 * @see javax.swing.undo.AbstractUndoableEdit#redo()
	 */
	public void redo() throws CannotRedoException {
		super.redo();
		_tableUndoRedoCallBack.removeColumns( "redo", this);
	}

	/* (non-Javadoc)
	 * @see javax.swing.undo.AbstractUndoableEdit#die()
	 */
	public void die() {
		super.die();
		_columns = null;
		_objects = null;
	}
}
