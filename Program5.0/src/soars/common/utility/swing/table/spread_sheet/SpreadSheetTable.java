/**
 * 
 */
package soars.common.utility.swing.table.spread_sheet;

import java.awt.Component;
import java.awt.Frame;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Vector;

import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableColumnModel;
import javax.swing.undo.UndoManager;
import javax.swing.undo.UndoableEditSupport;

import soars.common.utility.swing.table.base.undo_redo.base.ITableUndoRedoCallBack;
import soars.common.utility.swing.table.base.undo_redo.cell.SetValueUndo;
import soars.common.utility.swing.table.base.undo_redo.cell.SetValuesUndo;
import soars.common.utility.swing.table.base.undo_redo.column.ColumnWidthUndo;
import soars.common.utility.swing.table.base.undo_redo.column.ColumnsUndo;
import soars.common.utility.swing.table.base.undo_redo.column.InsertColumnsBlockUndo;
import soars.common.utility.swing.table.base.undo_redo.column.InsertColumnsUndo;
import soars.common.utility.swing.table.base.undo_redo.column.RemoveColumnsBlockUndo;
import soars.common.utility.swing.table.base.undo_redo.column.RemoveColumnsUndo;
import soars.common.utility.swing.table.base.undo_redo.row.InsertRowsBlockUndo;
import soars.common.utility.swing.table.base.undo_redo.row.InsertRowsUndo;
import soars.common.utility.swing.table.base.undo_redo.row.RemoveRowsBlockUndo;
import soars.common.utility.swing.table.base.undo_redo.row.RemoveRowsUndo;
import soars.common.utility.swing.table.base.undo_redo.row.RowsUndo;
import soars.common.utility.swing.table.spread_sheet.undo_redo.column.SpreadSheetTableRemoveColumnsUndo;
import soars.common.utility.swing.table.spread_sheet.undo_redo.row.SpreadSheetTableRemoveRowsUndo;
import soars.common.utility.swing.table.spread_sheet.undo_redo.row.SpreadSheetTableRowsUndo;
import soars.common.utility.tool.common.Tool;

/**
 * @author kurata
 *
 */
public class SpreadSheetTable extends SpreadSheetTableBase implements ITableUndoRedoCallBack{

	/**
	 * 
	 */
	protected int _minimumRowCount = 0;

	/**
	 * 
	 */
	protected int _minimumColumnCount = 0;

	/**
	 * 
	 */
	protected UndoManager _undoManager = null;

	/**
	 * 
	 */
	protected UndoableEditSupport _undoableEditSupport = null;

	/**
	 * @param owner
	 * @param parent
	 */
	public SpreadSheetTable(Frame owner, Component parent) {
		super(owner, parent);
	}

	/**
	 * @param minimumRowCount
	 * @param minimumColumnCount
	 * @param owner
	 * @param parent
	 */
	public SpreadSheetTable(int minimumRowCount, int minimumColumnCount, Frame owner, Component parent) {
		super(owner, parent);
		_minimumRowCount = minimumRowCount;
		_minimumColumnCount = minimumColumnCount;
	}

	/**
	 * @param row
	 * @param column
	 * @return
	 */
	protected Object get_default_value(int row, int column) {
		return null;
	}

	/**
	 * @param row
	 * @param column
	 */
	public void select(int row, int column) {
		if ( 0 > row || 0 > column)
			return;

		setRowSelectionInterval( row, row);
		setColumnSelectionInterval( column, column);

		_spreadSheetTableBase.clearSelection();
		_spreadSheetTableBase.setRowSelectionInterval( row, row);

		requestFocus();
		internal_synchronize();

		Rectangle rectangle = getCellRect( row, column, true);
		scrollRectToVisible( rectangle);
	}

	/**
	 * @param rows
	 * @param columns
	 * @param columnWidthList
	 * @param defaultColumnWidth 
	 * @param point 
	 * @return
	 */
	public boolean restore(int rows[], int columns[], List<Integer> columnWidthList, int defaultColumnWidth, Point point/*, int topRow, int topColumn*/) {
		restore_column_widths( columnWidthList, defaultColumnWidth);
		boolean result1 = restore_selection( rows, columns);
		//boolean result2 = restore_visibleRect( point, topRow, topColumn);
		boolean result2 = restore_visibleRect( point);
		return ( result1 && result2);
	}

	/**
	 * @param columnWidthList
	 * @param defaultColumnWidth
	 */
	public void restore_column_widths(List<Integer> columnWidthList, int defaultColumnWidth) {
		DefaultTableColumnModel defaultTableColumnModel = ( DefaultTableColumnModel)getColumnModel();
		for ( int i = 0; i < defaultTableColumnModel.getColumnCount(); ++i)
			defaultTableColumnModel.getColumn( i).setPreferredWidth( ( null != columnWidthList && columnWidthList.size() > i) ? columnWidthList.get( i) : defaultColumnWidth);
	}

	/**
	 * @param rows
	 * @param columns
	 * @return
	 */
	public boolean restore_selection(int[] rows, int[] columns) {
		if ( null == rows || null == columns)
			return false;

		for ( int row:rows) {
			if ( 0 > row || getRowCount() <= row || _spreadSheetTableBase.getRowCount() <= row)
				return false;
		}

		for ( int column:columns) {
			if ( 0 > column	|| getColumnCount() <= column)
				return false;
		}

		for ( int i = 0; i < rows.length; ++i) {
			if ( 0 == i)
				setRowSelectionInterval( rows[ i], rows[ i]);
			else
				getSelectionModel().addSelectionInterval( rows[ i], rows[ i]);
		}
		for ( int i = 0; i < columns.length; ++i) {
			if ( 0 == i)
				setColumnSelectionInterval( columns[ i], columns[ i]);
			else
				getColumnModel().getSelectionModel().addSelectionInterval( columns[ i], columns[ i]);
		}
//		setRowSelectionInterval( rows[ 0], rows[ rows.length - 1]);
//		setColumnSelectionInterval( columns[ 0], columns[ columns.length - 1]);

		_spreadSheetTableBase.clearSelection();
		for ( int i = 0; i < rows.length; ++i) {
			if ( 0 == i)
				_spreadSheetTableBase.setRowSelectionInterval( rows[ i], rows[ i]);
			else
				_spreadSheetTableBase.getSelectionModel().addSelectionInterval( rows[ i], rows[ i]);
		}
//		_spreadSheetTableBase.setRowSelectionInterval( rows[ 0], rows[ rows.length - 1]);

		requestFocus();
		internal_synchronize();

		return true;
	}

	/**
	 * @param point 
	 * @return
	 */
	public boolean restore_visibleRect(Point point/*, int topRow, int topColumn*/) {
//		if ( 0 > topRow || getRowCount() <= topRow || _spreadSheetTableBase.getRowCount() <= topRow)
//			return false;
//
//		if ( 0 > topColumn	|| getColumnCount() <= topColumn)
//			return false;
//
//		Rectangle rectangle = getCellRect( topRow, topColumn, true);
		Rectangle rectangle = getVisibleRect();
		rectangle.x = point.x;
		rectangle.y = point.y;
//		rectangle.x = 0;
//		DefaultTableColumnModel defaultTableColumnModel = ( DefaultTableColumnModel)getColumnModel();
//		for ( int i = 0; i < topColumn; ++i)
//			rectangle.x += defaultTableColumnModel.getColumn( i).getPreferredWidth();
		scrollRectToVisible( rectangle);

		return true;
	}

	/**
	 * @param spreadSheetTableBase
	 * @param popupMenu
	 * @return
	 */
	public boolean setup(SpreadSheetTableBase spreadSheetTableBase, boolean popupMenu) {
		if ( !super.setup(popupMenu))
			return false;

		setAutoResizeMode( AUTO_RESIZE_OFF);
		setCellSelectionEnabled( true);

		_spreadSheetTableBase = spreadSheetTableBase;

//		addComponentListener( new ComponentAdapter() {
//			public void componentResized(ComponentEvent arg0) {
//				if ( _resized)
//					return;
//
//				on_componentResized( arg0);
//				_resized = true;
//			}
//		});

		addMouseMotionListener( new MouseMotionAdapter() {
			public void mouseDragged(MouseEvent arg0) {
				getTableHeader().repaint();
			}
		});

		return true;
	}

	/* (non-Javadoc)
	 * @see soars.common.utility.swing.table.base.StandardTable#on_mouse_pressed(java.awt.event.MouseEvent)
	 */
	protected void on_mouse_pressed(MouseEvent mouseEvent) {
		requestFocus();
		synchronize();
	}

	/* (non-Javadoc)
	 * @see soars.common.utility.swing.table.StandardTable#on_mouse_released(java.awt.event.MouseEvent)
	 */
	protected void on_mouse_released(MouseEvent mouseEvent) {
		requestFocus();
		synchronize();
	}

	/* (non-Javadoc)
	 * @see soars.common.utility.swing.table.spread_sheet.SpreadSheetTableBase#on_mouse_right_up(int, int, java.awt.event.MouseEvent)
	 */
	protected boolean on_mouse_right_up(int row, int column, MouseEvent mouseEvent) {
		requestFocus();

		stop_cell_editing();

		int[] rows = getSelectedRows();
		if ( null == rows || 0 == rows.length)
			return false;

		int[] columns = getSelectedColumns();
		if ( null == columns || 0 == columns.length)
			return false;

		Arrays.sort( rows);
		Arrays.sort( columns);
		if ( 0 > Arrays.binarySearch( rows, row) || 0 > Arrays.binarySearch( columns, column))
			select( row, column);

		requestFocus();
		internal_synchronize();

		return true;
	}

	/**
	 * @param column
	 * @param ctrl
	 * @param shift
	 */
	public void select_column(int column, boolean ctrl, boolean shift) {
		if ( ( ctrl && shift) || ( !ctrl && !shift))
			select_column( column, column);
		else {
			if ( ctrl) {
				if ( getRowCount() != getSelectedRowCount())
					select_column( column, column);
				else {
					int[] columns = getSelectedColumns();
					if ( null == columns || 0 == columns.length || ( 1 == columns.length && column == columns[ 0]))
						return;

					if ( isColumnSelected( column))
						removeColumnSelectionInterval( column, column);
					else
						addColumnSelectionInterval( column, column);
				}
			} else {
				if ( getRowCount() != getSelectedRowCount()) {
					int selectedColumn = getSelectedColumn();
					if ( 0 > selectedColumn || getColumnCount() <= selectedColumn)
						return;

					if ( selectedColumn == column)
						select_column( column, column);
					else {
						int min = Math.min( selectedColumn, column);
						int max = Math.max( selectedColumn, column);
						select_column( min, max);
					}
				} else {
					ListSelectionModel listSelectionModel = getColumnModel().getSelectionModel();
					int selectedColumn = listSelectionModel.getAnchorSelectionIndex();
					if ( 0 > selectedColumn || getColumnCount() <= selectedColumn)
						return;

					int min = Math.min( selectedColumn, column);
					int max = Math.max( selectedColumn, column);
					select_column( min, max);
				}
			}
		}
		internal_synchronize();
	}

	/**
	 * @param from
	 * @param to
	 */
	public void select_column(int from, int to) {
		_spreadSheetTableBase.clearSelection();
		_spreadSheetTableBase.setRowSelectionInterval( 0, getRowCount() - 1);

		clearSelection();
		addColumnSelectionInterval( from, to);
		setRowSelectionInterval( 0, getRowCount() - 1);
	}

	/**
	 * @param column
	 * @param previousColumn
	 * @param ctrl 
	 */
	public void drag_on_column(int column, int previousColumn, boolean ctrl) {
		if ( column == previousColumn || ctrl)
			addColumnSelectionInterval( column, column);
		else {
			if ( !isColumnSelected( column))
				addColumnSelectionInterval( column, column);
			else {
				if ( column > previousColumn) {
					for ( int i = 0; i <= previousColumn; ++i)
						removeColumnSelectionInterval( i, i);
				} else {
					for ( int i = previousColumn; i < getColumnCount(); ++i)
						removeColumnSelectionInterval( i, i);
				}
			}
		}
		internal_synchronize();
	}

	/**
	 * @param spreadSheetTableHeader
	 */
	protected void setup_undo_redo_manager(SpreadSheetTableHeader spreadSheetTableHeader) {
		_undoManager = new UndoManager();
		_undoableEditSupport = new UndoableEditSupport( this);
		_undoableEditSupport.addUndoableEditListener( _undoManager);
		super.setup_undo_redo_manager();
		_spreadSheetTableBase.setup_undo_redo_manager();
	}

	/* (non-Javadoc)
	 * @see soars.common.utility.swing.table.spread_sheet.SpreadSheetTableBase#on_undo()
	 */
	public void on_undo() {
		if ( null == _undoManager)
			return;

		stop_cell_editing();

		if ( !_undoManager.canUndo())
			return;

		_undoManager.undo();

		internal_synchronize();
	}

	/* (non-Javadoc)
	 * @see soars.common.utility.swing.table.spread_sheet.SpreadSheetTableBase#on_redo()
	 */
	public void on_redo() {
		if ( null == _undoManager)
			return;

		stop_cell_editing();

		if ( !_undoManager.canRedo())
			return;

		_undoManager.redo();

		internal_synchronize();
	}

	/* (non-Javadoc)
	 * @see javax.swing.JTable#setValueAt(java.lang.Object, int, int)
	 */
	public void setValueAt(Object arg0, int arg1, int arg2) {
		if ( null != _undoableEditSupport) {
			SetValueUndo setValueUndo = new SetValueUndo( arg0, arg1, arg2, this, this);
			_undoableEditSupport.postEdit( setValueUndo);
		}
		setValueAtDefault( arg0, arg1, arg2);
	}

	/* (non-Javadoc)
	 * @see soars.common.utility.swing.table.base.undo_redo.base.ITableUndoRedoCallBack#setValueAt(java.lang.String, soars.common.utility.swing.table.base.undo_redo.cell.SetValueUndo)
	 */
	public void setValueAt(String kind, SetValueUndo setValueUndo) {
		if ( kind.equals( "undo"))
			setValueAtDefault( setValueUndo._oldValue, setValueUndo._row, setValueUndo._column);
		else if ( kind.equals( "redo"))
			setValueAtDefault( setValueUndo._newValue, setValueUndo._row, setValueUndo._column);

		requestFocus();
		_spreadSheetTableBase.clearSelection();
		clearSelection();
		select( setValueUndo._row, setValueUndo._column);
	}

	/**
	 * @param setValueUndoList
	 * @return
	 */
	public SetValuesUndo setValuesAt(List<SetValueUndo> setValueUndoList) {
		return setValuesAt( ( ( null != _undoableEditSupport) ? ( new SetValuesUndo( setValueUndoList, this, this)) : null), setValueUndoList);
	}

	/**
	 * @param setValuesUndo
	 * @param setValueUndoList
	 * @return
	 */
	public SetValuesUndo setValuesAt(SetValuesUndo setValuesUndo, List<SetValueUndo> setValueUndoList) {
		if ( null == setValueUndoList || setValueUndoList.isEmpty())
			return null;

		if ( null != _undoableEditSupport && null != setValuesUndo)
			_undoableEditSupport.postEdit( setValuesUndo);

		for ( SetValueUndo setValueUndo:setValueUndoList)
			setValueAtDefault( setValueUndo._newValue, setValueUndo._row, setValueUndo._column);

		return setValuesUndo;
	}

	/* (non-Javadoc)
	 * @see soars.common.utility.swing.table.base.undo_redo.base.ITableUndoRedoCallBack#setValuesAt(java.lang.String, soars.common.utility.swing.table.base.undo_redo.cell.SetValuesUndo)
	 */
	public void setValuesAt(String kind, SetValuesUndo setValuesUndo) {
		for ( SetValueUndo setValueUndo:setValuesUndo._setValueUndoList) {
			if ( kind.equals( "undo"))
				setValueAtDefault( setValueUndo._oldValue, setValueUndo._row, setValueUndo._column);
			else if ( kind.equals( "redo"))
				setValueAtDefault( setValueUndo._newValue, setValueUndo._row, setValueUndo._column);
		}

		requestFocus();
		_spreadSheetTableBase.clearSelection();
		clearSelection();
		setRowSelectionInterval( setValuesUndo._rowFrom, setValuesUndo._rowTo);
		setColumnSelectionInterval( setValuesUndo._columnFrom, setValuesUndo._columnTo);

		requestFocus();
		internal_synchronize();

		Rectangle rectangle = getCellRect( setValuesUndo._rowFrom, setValuesUndo._columnFrom, true);
		scrollRectToVisible( rectangle);
	}

	/* (non-Javadoc)
	 * @see soars.common.utility.swing.table.spread_sheet.SpreadSheetTableBase#insertRows(int[])
	 */
	public void insertRows(int[] rows) {
		insertRows( ( ( null != _undoableEditSupport) ? ( new InsertRowsUndo( rows, this, this)) : null), rows);
	}

	/**
	 * @param insertRowsUndo
	 * @param rows
	 * @return
	 */
	public void insertRows(InsertRowsUndo insertRowsUndo, int[] rows) {
		if ( null == rows || 0 == rows.length)
			return;

		if ( null != _undoableEditSupport && null != insertRowsUndo)
			_undoableEditSupport.postEdit( insertRowsUndo);

		insert_rows( rows);
	}

	/* (non-Javadoc)
	 * @see soars.common.utility.swing.table.base.undo_redo.base.ITableUndoRedoCallBack#insertRows(java.lang.String, soars.common.utility.swing.table.base.undo_redo.row.InsertRowsUndo)
	 */
	public void insertRows(String kind, InsertRowsUndo insertRowsUndo) {
		if ( kind.equals( "undo")) {
			clearSelection();
			_spreadSheetTableBase.clearSelection();
			int[] rows;
			if ( Tool.is_consecutive( insertRowsUndo._rows)) {
				for ( int i = 0; i < insertRowsUndo._rows.length; ++i) {
					removeRow( insertRowsUndo._rows[ 0]);
					_spreadSheetTableBase.removeRow( insertRowsUndo._rows[ 0]);
				}
				rows = adjust( insertRowsUndo._rows, getRowCount());
				_spreadSheetTableBase.setRowSelectionInterval( rows[ 0], rows[ rows.length - 1]);
			} else {
				for ( int row:insertRowsUndo._rows) {
					removeRow( row);
					_spreadSheetTableBase.removeRow( row);
				}
				rows = adjust( insertRowsUndo._rows, getRowCount());
				for ( int row:rows)
					_spreadSheetTableBase.addRowSelectionInterval( row, row);
			}

			adjust_row_count( null);

			_spreadSheetTableBase.requestFocus();
			_spreadSheetTableBase.internal_synchronize();

			scroll_row( rows);
		} else if ( kind.equals( "redo")) {
			insert_rows( insertRowsUndo._rows);
		}
	}

	/**
	 * @param rows
	 */
	private void insert_rows(int[] rows) {
		clearSelection();
		_spreadSheetTableBase.clearSelection();
		if ( Tool.is_consecutive( rows)) {
			for ( int i = 0; i < rows.length; ++i) {
				insertRow( rows[ 0]);
				_spreadSheetTableBase.insertRow( rows[ 0]);
			}
			_spreadSheetTableBase.setRowSelectionInterval( rows[ 0], rows[ rows.length - 1]);
		} else {
			for ( int i = rows.length - 1; 0 <= i; --i) {
				insertRow( rows[ i]);
				_spreadSheetTableBase.insertRow( rows[ i]);
			}
			for ( int row:rows)
				_spreadSheetTableBase.addRowSelectionInterval( row, row);
		}

		_spreadSheetTableBase.requestFocus();
		_spreadSheetTableBase.internal_synchronize();

		scroll_row( rows);
	}

	/* (non-Javadoc)
	 * @see soars.common.utility.swing.table.spread_sheet.SpreadSheetTableBase#removeRows(int[])
	 */
	public void removeRows(int[] rows) {
		removeRows( ( ( null != _undoableEditSupport) ? ( new SpreadSheetTableRemoveRowsUndo( rows, this, _spreadSheetTableBase, this)) : null), rows);
	}

	/**
	 * @param spreadSheetTableRemoveRowsUndo
	 * @param rows
	 * @return
	 */
	public void removeRows(SpreadSheetTableRemoveRowsUndo spreadSheetTableRemoveRowsUndo, int[] rows) {
		if ( null == rows || 0 == rows.length)
			return;

		if ( null != _undoableEditSupport && null != spreadSheetTableRemoveRowsUndo)
			_undoableEditSupport.postEdit( spreadSheetTableRemoveRowsUndo);

		remove_rows( rows, spreadSheetTableRemoveRowsUndo);
	}

	/* (non-Javadoc)
	 * @see soars.common.utility.swing.table.base.undo_redo.base.ITableUndoRedoCallBack#removeRows(java.lang.String, soars.common.utility.swing.table.base.undo_redo.row.RemoveRowsUndo)
	 */
	public void removeRows(String kind, RemoveRowsUndo removeRowsUndo) {
		SpreadSheetTableRemoveRowsUndo spreadSheetTableRemoveRowsUndo = ( SpreadSheetTableRemoveRowsUndo)removeRowsUndo;
		if ( kind.equals( "undo")) {
			clearSelection();
			_spreadSheetTableBase.clearSelection();
			int counter = 0;
			while ( counter < spreadSheetTableRemoveRowsUndo._appendedRowCount) {
				removeRow( getRowCount() - 1);
				_spreadSheetTableBase.removeRow( _spreadSheetTableBase.getRowCount() - 1);
				++counter;
			}
			if ( Tool.is_consecutive( spreadSheetTableRemoveRowsUndo._rows)) {
				for ( int i = spreadSheetTableRemoveRowsUndo._rows.length - 1; 0 <= i; --i) {
					insertRow( spreadSheetTableRemoveRowsUndo._rows[ 0], spreadSheetTableRemoveRowsUndo._objects[ i]);
					_spreadSheetTableBase.insertRow( spreadSheetTableRemoveRowsUndo._rows[ 0], spreadSheetTableRemoveRowsUndo._rowHeaderObjects[ i]);
				}
				_spreadSheetTableBase.setRowSelectionInterval( spreadSheetTableRemoveRowsUndo._rows[ 0], spreadSheetTableRemoveRowsUndo._rows[ spreadSheetTableRemoveRowsUndo._rows.length - 1]);
			} else {
				for ( int i = 0; i < spreadSheetTableRemoveRowsUndo._rows.length; ++i) {
					insertRow( spreadSheetTableRemoveRowsUndo._rows[ i], spreadSheetTableRemoveRowsUndo._objects[ i]);
					_spreadSheetTableBase.insertRow( spreadSheetTableRemoveRowsUndo._rows[ i], spreadSheetTableRemoveRowsUndo._rowHeaderObjects[ i]);
				}
				for ( int row:spreadSheetTableRemoveRowsUndo._rows)
					_spreadSheetTableBase.addRowSelectionInterval( row, row);
			}

			_spreadSheetTableBase.requestFocus();
			_spreadSheetTableBase.internal_synchronize();

			scroll_row( spreadSheetTableRemoveRowsUndo._rows);
		} else if ( kind.equals( "redo")) {
			remove_rows( spreadSheetTableRemoveRowsUndo._rows, spreadSheetTableRemoveRowsUndo);
		}
	}

	/**
	 * @param rows
	 * @param spreadSheetTableRemoveRowsUndo 
	 */
	private void remove_rows(int[] rows, SpreadSheetTableRemoveRowsUndo spreadSheetTableRemoveRowsUndo) {
		clearSelection();
		_spreadSheetTableBase.clearSelection();
		int[] newRows;
		if ( Tool.is_consecutive( rows)) {
			for ( int i = 0; i < rows.length; ++i) {
				removeRow( rows[ 0]);
				_spreadSheetTableBase.removeRow( rows[ 0]);
			}
			newRows = adjust( rows, getRowCount());
			if ( 0 < getRowCount())
				_spreadSheetTableBase.setRowSelectionInterval( newRows[ 0], newRows[ newRows.length - 1]);
		} else {
			for ( int i = rows.length - 1; 0 <= i; --i) {
				removeRow( rows[ i]);
				_spreadSheetTableBase.removeRow( rows[ i]);
			}
			newRows = adjust( rows, getRowCount());
			for ( int row:newRows) {
				_spreadSheetTableBase.addRowSelectionInterval( row, row);
			}
		}

		adjust_row_count( spreadSheetTableRemoveRowsUndo);

		_spreadSheetTableBase.requestFocus();
		_spreadSheetTableBase.internal_synchronize();

		scroll_row( newRows);
	}

	/**
	 * @param rows
	 * @param setValueUndoLists
	 * @param rowHeaderSetValueUndoLists
	 */
	public void setValuesAt(int[] rows, List<List<SetValueUndo>> setValueUndoLists, List<List<SetValueUndo>> rowHeaderSetValueUndoLists) {
		setValuesAt( ( ( null != _undoableEditSupport) ? ( new SpreadSheetTableRowsUndo( rows, setValueUndoLists, this, rowHeaderSetValueUndoLists, _spreadSheetTableBase, this)) : null),
			rows, setValueUndoLists, rowHeaderSetValueUndoLists);
	}

	/**
	 * @param spreadSheetTableRowsUndo
	 * @param rows
	 * @param setValueUndoLists
	 * @param rowHeaderSetValueUndoLists
	 */
	public void setValuesAt(SpreadSheetTableRowsUndo spreadSheetTableRowsUndo, int[] rows, List<List<SetValueUndo>> setValueUndoLists, List<List<SetValueUndo>> rowHeaderSetValueUndoLists) {
		if ( null == setValueUndoLists || setValueUndoLists.isEmpty())
			return;

		if ( null == rowHeaderSetValueUndoLists || rowHeaderSetValueUndoLists.isEmpty())
			return;

		if ( null != _undoableEditSupport && null != spreadSheetTableRowsUndo)
			_undoableEditSupport.postEdit( spreadSheetTableRowsUndo);

		set_values_at( rows, setValueUndoLists, rowHeaderSetValueUndoLists);

		clearSelection();
		_spreadSheetTableBase.clearSelection();

		if ( Tool.is_consecutive( rows))
			_spreadSheetTableBase.setRowSelectionInterval( rows[ 0], rows[ rows.length - 1]);
		else {
			for ( int row:rows)
				_spreadSheetTableBase.addRowSelectionInterval( row, row);
		}

		_spreadSheetTableBase.requestFocus();
		_spreadSheetTableBase.internal_synchronize();

		Rectangle rectangle = getCellRect( rows[ 0], 0, true);
		scrollRectToVisible( rectangle);
	}

	/* (non-Javadoc)
	 * @see soars.common.utility.swing.table.base.undo_redo.base.ITableUndoRedoCallBack#setValuesAt(java.lang.String, soars.common.utility.swing.table.base.undo_redo.row.RowsUndo)
	 */
	public void setValuesAt(String kind, RowsUndo rowsUndo) {
		SpreadSheetTableRowsUndo spreadSheetTableRowsUndo = ( SpreadSheetTableRowsUndo)rowsUndo;
		if ( kind.equals( "undo")) {
			for ( int i = 0; i < spreadSheetTableRowsUndo._rows.length; ++i) {
				for ( SetValueUndo setValueUndo:spreadSheetTableRowsUndo._setValueUndoLists.get( i))
					setValueAtDefault( setValueUndo._oldValue, setValueUndo._row, setValueUndo._column);
				for ( SetValueUndo setValueUndo:spreadSheetTableRowsUndo._rowHeaderSetValueUndoLists.get( i))
					_spreadSheetTableBase.setValueAt(setValueUndo._oldValue, setValueUndo._row, setValueUndo._column);
			}
		} else if ( kind.equals( "redo")) {
			set_values_at( spreadSheetTableRowsUndo._rows, spreadSheetTableRowsUndo._setValueUndoLists, spreadSheetTableRowsUndo._rowHeaderSetValueUndoLists);
		}

		clearSelection();
		_spreadSheetTableBase.clearSelection();

		if ( Tool.is_consecutive( spreadSheetTableRowsUndo._rows))
			_spreadSheetTableBase.setRowSelectionInterval( spreadSheetTableRowsUndo._rows[ 0], spreadSheetTableRowsUndo._rows[ spreadSheetTableRowsUndo._rows.length - 1]);
		else {
			for ( int row:spreadSheetTableRowsUndo._rows)
				_spreadSheetTableBase.addRowSelectionInterval( row, row);
		}

		_spreadSheetTableBase.requestFocus();
		_spreadSheetTableBase.internal_synchronize();

		Rectangle rectangle = getCellRect( spreadSheetTableRowsUndo._rows[ 0], 0, true);
		scrollRectToVisible( rectangle);
	}

	/**
	 * @param rows
	 * @param setValueUndoLists
	 * @param rowHeaderSetValueUndoLists
	 */
	private void set_values_at(int[] rows, List<List<SetValueUndo>> setValueUndoLists, List<List<SetValueUndo>> rowHeaderSetValueUndoLists) {
		for ( int i = 0; i < rows.length; ++i) {
			for ( SetValueUndo setValueUndo:setValueUndoLists.get( i))
				setValueAtDefault( setValueUndo._newValue, setValueUndo._row, setValueUndo._column);
			for ( SetValueUndo setValueUndo:rowHeaderSetValueUndoLists.get( i))
				_spreadSheetTableBase.setValueAt(setValueUndo._newValue, setValueUndo._row, setValueUndo._column);
		}
	}

	/**
	 * @param insertRowsBlockUndo
	 * @return
	 */
	public void insertRowsBlock(InsertRowsBlockUndo insertRowsBlockUndo) {
		// TODO Auto-generated method stub
		if ( null != _undoableEditSupport && null != insertRowsBlockUndo)
			_undoableEditSupport.postEdit( insertRowsBlockUndo);

		insert_rows_block( insertRowsBlockUndo);
	}

	/* (non-Javadoc)
	 * @see soars.common.utility.swing.table.base.undo_redo.base.ITableUndoRedoCallBack#insertRowsBlock(java.lang.String, soars.common.utility.swing.table.base.undo_redo.row.InsertRowsBlockUndo)
	 */
	public void insertRowsBlock(String kind, InsertRowsBlockUndo insertRowsBlockUndo) {
		// TODO Auto-generated method stub
		if ( kind.equals( "undo")) {
			int size = insertRowsBlockUndo._rowTo - insertRowsBlockUndo._rowFrom + 1;
			for ( int row = insertRowsBlockUndo._rowFrom; row < getRowCount() - size; ++row) {
				for ( int column = insertRowsBlockUndo._columnFrom; column <= insertRowsBlockUndo._columnTo; ++column)
					setValueAtDefault( getValueAt( row + size, column), row, column);
			}
			for ( int i = 0; i < size; ++i) {
				removeRow( getRowCount() - 1);
				_spreadSheetTableBase.removeRow( _spreadSheetTableBase.getRowCount() - 1);
			}

			requestFocus();
			_spreadSheetTableBase.clearSelection();
			clearSelection();
			setRowSelectionInterval( insertRowsBlockUndo._rowFrom, insertRowsBlockUndo._rowTo);
			setColumnSelectionInterval( insertRowsBlockUndo._columnFrom, insertRowsBlockUndo._columnTo);

			requestFocus();
			internal_synchronize();

			Rectangle rectangle = getCellRect( insertRowsBlockUndo._rowFrom, insertRowsBlockUndo._columnFrom, true);
			scrollRectToVisible( rectangle);
		} else if ( kind.equals( "redo")) {
			insert_rows_block( insertRowsBlockUndo);
		}
	}

	/**
	 * @param insertRowsBlockUndo
	 */
	private void insert_rows_block(InsertRowsBlockUndo insertRowsBlockUndo) {
		// TODO Auto-generated method stub
		int size = insertRowsBlockUndo._rowTo - insertRowsBlockUndo._rowFrom + 1;
		for ( int i = 0; i < size; ++i) {
			addRow();
			_spreadSheetTableBase.addRow();
		}
		for ( int row = getRowCount() - size - 1; row >= insertRowsBlockUndo._rowFrom; --row) {
			for ( int column = insertRowsBlockUndo._columnFrom; column <= insertRowsBlockUndo._columnTo; ++column)
				setValueAtDefault( getValueAt( row, column), row + size, column);
		}
		for ( int row = insertRowsBlockUndo._rowFrom; row <= insertRowsBlockUndo._rowTo; ++row) {
			for ( int column = insertRowsBlockUndo._columnFrom; column <= insertRowsBlockUndo._columnTo; ++column)
				setValueAtDefault( get_default_value( row, column), row, column);
		}

		requestFocus();
		_spreadSheetTableBase.clearSelection();
		clearSelection();
		setRowSelectionInterval( insertRowsBlockUndo._rowFrom, insertRowsBlockUndo._rowTo);
		setColumnSelectionInterval( insertRowsBlockUndo._columnFrom, insertRowsBlockUndo._columnTo);

		requestFocus();
		internal_synchronize();

		Rectangle rectangle = getCellRect( insertRowsBlockUndo._rowFrom, insertRowsBlockUndo._columnFrom, true);
		scrollRectToVisible( rectangle);
	}

	/**
	 * @param removeRowsBlockUndo
	 * @return
	 */
	public void removeRowsBlock(RemoveRowsBlockUndo removeRowsBlockUndo) {
		// TODO Auto-generated method stub
		if ( null != _undoableEditSupport && null != removeRowsBlockUndo)
			_undoableEditSupport.postEdit( removeRowsBlockUndo);

		remove_rows_block( removeRowsBlockUndo);
	}

	/* (non-Javadoc)
	 * @see soars.common.utility.swing.table.base.undo_redo.base.ITableUndoRedoCallBack#removeRowsBlock(java.lang.String, soars.common.utility.swing.table.base.undo_redo.row.RemoveRowsBlockUndo)
	 */
	public void removeRowsBlock(String kind, RemoveRowsBlockUndo removeRowsBlockUndo) {
		// TODO Auto-generated method stub
		if ( kind.equals( "undo")) {
			int size = removeRowsBlockUndo._rowTo - removeRowsBlockUndo._rowFrom + 1;
			for ( int row = getRowCount() - size - 1; row >= removeRowsBlockUndo._rowFrom; --row) {
				for ( int column = removeRowsBlockUndo._columnFrom; column <= removeRowsBlockUndo._columnTo; ++column)
					setValueAtDefault( getValueAt( row, column), row + size, column);
			}
			// TODO 元のデータを復元
			for ( int row = removeRowsBlockUndo._rowFrom; row <= removeRowsBlockUndo._rowTo; ++row) {
				for ( int column = removeRowsBlockUndo._columnFrom; column <= removeRowsBlockUndo._columnTo; ++column)
					setValueAtDefault( removeRowsBlockUndo._objects[ row - removeRowsBlockUndo._rowFrom][ column - removeRowsBlockUndo._columnFrom], row, column);
			}

			requestFocus();
			_spreadSheetTableBase.clearSelection();
			clearSelection();
			setRowSelectionInterval( removeRowsBlockUndo._rowFrom, removeRowsBlockUndo._rowTo);
			setColumnSelectionInterval( removeRowsBlockUndo._columnFrom, removeRowsBlockUndo._columnTo);

			requestFocus();
			internal_synchronize();

			Rectangle rectangle = getCellRect( removeRowsBlockUndo._rowFrom, removeRowsBlockUndo._columnFrom, true);
			scrollRectToVisible( rectangle);
		} else if ( kind.equals( "redo")) {
			remove_rows_block( removeRowsBlockUndo);
		}
	}

	/**
	 * @param removeRowsBlockUndo
	 */
	private void remove_rows_block(RemoveRowsBlockUndo removeRowsBlockUndo) {
		// TODO Auto-generated method stub
		int size = removeRowsBlockUndo._rowTo - removeRowsBlockUndo._rowFrom + 1;
		for ( int row = removeRowsBlockUndo._rowFrom; row < getRowCount() - size; ++row) {
			for ( int column = removeRowsBlockUndo._columnFrom; column <= removeRowsBlockUndo._columnTo; ++column)
				setValueAtDefault( getValueAt( row + size, column), row, column);
		}
		// TODO 一番最後に空白を入れる
		for ( int i = 0; i < size; ++i) {
			for ( int column = removeRowsBlockUndo._columnFrom; column <= removeRowsBlockUndo._columnTo; ++column)
				setValueAtDefault( get_default_value( getRowCount() - 1 - i, column), getRowCount() - 1 - i, column);
		}

		requestFocus();
		_spreadSheetTableBase.clearSelection();
		clearSelection();
		setRowSelectionInterval( removeRowsBlockUndo._rowFrom, removeRowsBlockUndo._rowTo);
		setColumnSelectionInterval( removeRowsBlockUndo._columnFrom, removeRowsBlockUndo._columnTo);

		requestFocus();
		internal_synchronize();

		Rectangle rectangle = getCellRect( removeRowsBlockUndo._rowFrom, removeRowsBlockUndo._columnFrom, true);
		scrollRectToVisible( rectangle);
	}

	/**
	 * @param columns
	 * @param width
	 */
	public void insertColumns(int[] columns, int width) {
		insertColumns( ( ( null != _undoableEditSupport) ? ( new InsertColumnsUndo( columns, width, this, this)) : null), columns, width);
	}

	/**
	 * @param insertColumnsUndo
	 * @param columns
	 * @param width
	 */
	public void insertColumns(InsertColumnsUndo insertColumnsUndo, int[] columns, int width) {
		if ( null == columns || 0 == columns.length)
			return;

		if ( null != _undoableEditSupport && null != insertColumnsUndo)
			_undoableEditSupport.postEdit( insertColumnsUndo);

		Vector<Integer> columnWidths = new Vector();
		get_column_widths( columnWidths);

		insert_columns( columns, width, columnWidths);
	}

	/* (non-Javadoc)
	 * @see soars.common.utility.swing.table.base.undo_redo.base.ITableUndoRedoCallBack#insertColumns(java.lang.String, soars.common.utility.swing.table.base.undo_redo.column.InsertColumnsUndo)
	 */
	public void insertColumns(String kind, InsertColumnsUndo insertColumnsUndo) {
		clearSelection();
		_spreadSheetTableBase.clearSelection();

		Vector<Integer> columnWidths = new Vector();
		get_column_widths( columnWidths);

		if ( kind.equals( "undo")) {
			if ( Tool.is_consecutive( insertColumnsUndo._columns)) {
				for ( int column = insertColumnsUndo._columns[ 0]; column < getColumnCount() - insertColumnsUndo._columns.length; ++column) {
					for ( int row = 0; row < getRowCount(); ++row)
						setValueAtDefault( getValueAt( row, column + insertColumnsUndo._columns.length), row, column);
				}
				for ( int i = 0; i < insertColumnsUndo._columns.length; ++i)
					columnWidths.removeElementAt( insertColumnsUndo._columns[ 0]);
			} else {
				for ( int i = 0; i < insertColumnsUndo._columns.length; ++i) {
					for ( int column = insertColumnsUndo._columns[ i]; column < getColumnCount() - 1; ++column) {
						for ( int row = 0; row < getRowCount(); ++row)
							setValueAtDefault( getValueAt( row, column + 1), row, column);
					}
					columnWidths.removeElementAt( insertColumnsUndo._columns[ i]);
				}
			}

			adjust_column_count( insertColumnsUndo._columns.length, columnWidths, insertColumnsUndo._width, null);

			int[] columns = adjust( insertColumnsUndo._columns, getColumnCount());
			if ( Tool.is_consecutive( columns))
				setColumnSelectionInterval( columns[ 0], columns[ columns.length - 1]);
			else {
				for ( int column:columns)
					addColumnSelectionInterval( column, column);
			}

			setup_column( columnWidths);

			setRowSelectionInterval( 0, _spreadSheetTableBase.getRowCount() - 1);

			requestFocus();
			internal_synchronize();

			scroll_column( columns);
		} else if ( kind.equals( "redo")) {
			insert_columns( insertColumnsUndo._columns, insertColumnsUndo._width, columnWidths);
		}
	}

	/**
	 * @param columns
	 * @param width
	 * @param columnWidths
	 */
	private void insert_columns(int[] columns, int width, Vector<Integer> columnWidths) {
		for ( int column:columns)
			addColumn( "");

		if ( Tool.is_consecutive( columns)) {
			for ( int column = getColumnCount() - 1 - columns.length; columns[ 0] <= column; --column) {
				for ( int row = 0; row < getRowCount(); ++row)
					setValueAtDefault( getValueAt( row, column), row, column + columns.length);
			}
			for ( int i = 0; i < columns.length; ++i) {
				for ( int row = 0; row < getRowCount(); ++row)
					setValueAtDefault( get_default_value( row, columns[ i]), row, columns[ i]);
				columnWidths.insertElementAt( new Integer( width), columns[ 0]);
			}
			setColumnSelectionInterval( columns[ 0], columns[ columns.length - 1]);
		} else {
			for ( int i = columns.length - 1; 0 <= i; --i) {
				for ( int column = getColumnCount() - 2; columns[ i] <= column; --column) {
					for ( int row = 0; row < getRowCount(); ++row) {
						setValueAtDefault( getValueAt( row, column), row, column + 1);
						setValueAtDefault( get_default_value( row, column), row, column);
					}
				}
				columnWidths.insertElementAt( new Integer( width), columns[ i]);
			}
			for ( int column:columns)
				addColumnSelectionInterval( column, column);
		}

		setup_column( columnWidths);

		setRowSelectionInterval( 0, _spreadSheetTableBase.getRowCount() - 1);

		requestFocus();
		internal_synchronize();

		scroll_column( columns);
	}

	/**
	 * @param columns
	 * @param width
	 */
	public void removeColumns(int[] columns, int width) {
		removeColumns( ( ( null != _undoableEditSupport) ? ( new SpreadSheetTableRemoveColumnsUndo( columns, width, this, this)) : null), columns, width);
	}

	/**
	 * @param removeColumnsUndo
	 * @param columns
	 * @param width
	 */
	public void removeColumns(SpreadSheetTableRemoveColumnsUndo spreadSheetTableRemoveColumnsUndo, int[] columns, int width) {
		if ( null == columns || 0 == columns.length)
			return;

		if ( null != _undoableEditSupport && null != spreadSheetTableRemoveColumnsUndo)
			_undoableEditSupport.postEdit( spreadSheetTableRemoveColumnsUndo);

		Vector<Integer> columnWidths = new Vector();
		get_column_widths( columnWidths);

		remove_columns( columns, width, columnWidths, spreadSheetTableRemoveColumnsUndo);
	}

	/* (non-Javadoc)
	 * @see soars.common.utility.swing.table.base.undo_redo.base.ITableUndoRedoCallBack#removeColumns(java.lang.String, soars.common.utility.swing.table.base.undo_redo.column.RemoveColumnsUndo)
	 */
	public void removeColumns(String kind, RemoveColumnsUndo removeColumnsUndo) {
		SpreadSheetTableRemoveColumnsUndo spreadSheetTableRemoveColumnsUndo = ( SpreadSheetTableRemoveColumnsUndo)removeColumnsUndo;
		clearSelection();
		_spreadSheetTableBase.clearSelection();

		Vector<Integer> columnWidths = new Vector();
		get_column_widths( columnWidths);

		if ( kind.equals( "undo")) {
			setColumnCount( getColumnCount() - spreadSheetTableRemoveColumnsUndo._appendedColumnCount);

			for ( int column:spreadSheetTableRemoveColumnsUndo._columns) {
				addColumn( "");
				columnWidths.add( new Integer( spreadSheetTableRemoveColumnsUndo._width));
			}

			if ( Tool.is_consecutive( spreadSheetTableRemoveColumnsUndo._columns)) {
				for ( int column = getColumnCount() - 1 - spreadSheetTableRemoveColumnsUndo._columns.length; spreadSheetTableRemoveColumnsUndo._columns[ 0] <= column; --column) {
					for ( int row = 0; row < getRowCount(); ++row)
						setValueAtDefault( getValueAt( row, column), row, column + spreadSheetTableRemoveColumnsUndo._columns.length);
					columnWidths.set( column + spreadSheetTableRemoveColumnsUndo._columns.length, columnWidths.get( column));
				}
				for ( int i = 0; i < spreadSheetTableRemoveColumnsUndo._columns.length; ++i) {
					for ( int row = 0; row < getRowCount(); ++row)
						setValueAtDefault( spreadSheetTableRemoveColumnsUndo._objects[ row][ i], row, spreadSheetTableRemoveColumnsUndo._columns[ i]);
					columnWidths.set( spreadSheetTableRemoveColumnsUndo._columns[ i], new Integer( spreadSheetTableRemoveColumnsUndo._widths[ i]));
				}
				setColumnSelectionInterval( spreadSheetTableRemoveColumnsUndo._columns[ 0], spreadSheetTableRemoveColumnsUndo._columns[ spreadSheetTableRemoveColumnsUndo._columns.length - 1]);
			} else {
				for ( int i = 0; i < spreadSheetTableRemoveColumnsUndo._columns.length; ++i) {
					for ( int column = getColumnCount() - 2; spreadSheetTableRemoveColumnsUndo._columns[ i] <= column; --column) {
						for ( int row = 0; row < getRowCount(); ++row)
							setValueAtDefault( getValueAt( row, column), row, column + 1);
						columnWidths.set( column + 1, columnWidths.get( column));
					}
					for ( int row = 0; row < getRowCount(); ++row)
						setValueAtDefault( spreadSheetTableRemoveColumnsUndo._objects[ row][ i], row, spreadSheetTableRemoveColumnsUndo._columns[ i]);
					columnWidths.set( spreadSheetTableRemoveColumnsUndo._columns[ i], new Integer( spreadSheetTableRemoveColumnsUndo._widths[ i]));
				}
				for ( int column:spreadSheetTableRemoveColumnsUndo._columns)
					addColumnSelectionInterval( column, column);
			}

			setup_column( columnWidths);

			setRowSelectionInterval( 0, _spreadSheetTableBase.getRowCount() - 1);

			requestFocus();
			internal_synchronize();

			scroll_column( spreadSheetTableRemoveColumnsUndo._columns);
		} else if ( kind.equals( "redo")) {
			remove_columns( spreadSheetTableRemoveColumnsUndo._columns, spreadSheetTableRemoveColumnsUndo._width, columnWidths, spreadSheetTableRemoveColumnsUndo);
		}
	}

	/**
	 * @param columns
	 * @param width
	 * @param columnWidths
	 * @param spreadSheetTableRemoveColumnsUndo 
	 */
	private void remove_columns(int[] columns, int width, Vector<Integer> columnWidths, SpreadSheetTableRemoveColumnsUndo spreadSheetTableRemoveColumnsUndo) {
		if ( Tool.is_consecutive( columns)) {
			for ( int column = columns[ 0]; column < getColumnCount() - columns.length; ++column) {
				for ( int row = 0; row < getRowCount(); ++row)
					setValueAtDefault( getValueAt( row, column + columns.length), row, column);
			}
			for ( int i = 0; i < columns.length; ++i)
				columnWidths.removeElementAt( columns[ 0]);
		} else {
			for ( int i = columns.length - 1; 0 <= i; --i) {
				for ( int column = columns[ i]; column < getColumnCount() - 1; ++column) {
					for ( int row = 0; row < getRowCount(); ++row)
						setValueAtDefault( getValueAt( row, column + 1), row, column);
				}
				columnWidths.removeElementAt( columns[ i]);
			}
		}

		adjust_column_count( columns.length, columnWidths, width, spreadSheetTableRemoveColumnsUndo);

		int[] newColumns = adjust( columns, getColumnCount());
		if ( Tool.is_consecutive( newColumns)) {
			setColumnSelectionInterval( newColumns[ 0], newColumns[ newColumns.length - 1]);
		} else {
			for ( int column:newColumns) {
				if ( column < getColumnCount())
					addColumnSelectionInterval( column, column);
			}
		}

		setup_column( columnWidths);

		setRowSelectionInterval( 0, _spreadSheetTableBase.getRowCount() - 1);

		requestFocus();
		internal_synchronize();

		scroll_column( newColumns);
	}

	/**
	 * @param columns
	 * @param setValueUndoLists
	 */
	public void setValuesAt(int[] columns, List<List<SetValueUndo>> setValueUndoLists) {
		setValuesAt( ( ( null != _undoableEditSupport) ? ( new ColumnsUndo( columns, setValueUndoLists, this, this)) : null), columns, setValueUndoLists);
	}

	/**
	 * @param columnsUndo
	 * @param columns
	 * @param setValueUndoLists
	 */
	public void setValuesAt(ColumnsUndo columnsUndo, int[] columns, List<List<SetValueUndo>> setValueUndoLists) {
		if ( null == setValueUndoLists || setValueUndoLists.isEmpty())
			return;

		if ( null != _undoableEditSupport && null != columnsUndo)
			_undoableEditSupport.postEdit( columnsUndo);

		for ( int i = 0; i < columns.length; ++i) {
			for ( SetValueUndo setValueUndo:setValueUndoLists.get( i))
				setValueAtDefault( setValueUndo._newValue, setValueUndo._row, setValueUndo._column);
		}

		clearSelection();
		_spreadSheetTableBase.clearSelection();

		if ( Tool.is_consecutive( columns))
			setColumnSelectionInterval( columns[ 0], columns[ columns.length - 1]);
		else {
			for ( int column:columns)
				addColumnSelectionInterval( column, column);
		}

		setRowSelectionInterval( 0, getRowCount() - 1);

		requestFocus();
		internal_synchronize();

		Rectangle rectangle = getCellRect( 0, columns[ 0], true);
		scrollRectToVisible( rectangle);
	}

	/* (non-Javadoc)
	 * @see soars.common.utility.swing.table.base.undo_redo.base.ITableUndoRedoCallBack#setValuesAt(java.lang.String, soars.common.utility.swing.table.base.undo_redo.column.ColumnsUndo)
	 */
	public void setValuesAt(String kind, ColumnsUndo columnsUndo) {
		for ( int i = 0; i < columnsUndo._columns.length; ++i) {
			if ( kind.equals( "undo")) {
				for ( SetValueUndo setValueUndo:columnsUndo._setValueUndoLists.get( i))
					setValueAtDefault( setValueUndo._oldValue, setValueUndo._row, setValueUndo._column);
			} else if ( kind.equals( "redo")) {
				for ( SetValueUndo setValueUndo:columnsUndo._setValueUndoLists.get( i))
					setValueAtDefault( setValueUndo._newValue, setValueUndo._row, setValueUndo._column);
			}
		}

		clearSelection();
		_spreadSheetTableBase.clearSelection();

		if ( Tool.is_consecutive( columnsUndo._columns))
			setColumnSelectionInterval( columnsUndo._columns[ 0], columnsUndo._columns[ columnsUndo._columns.length - 1]);
		else {
			for ( int column:columnsUndo._columns)
				addColumnSelectionInterval( column, column);
		}

		setRowSelectionInterval( 0, _spreadSheetTableBase.getRowCount() - 1);

		requestFocus();
		internal_synchronize();

		Rectangle rectangle = getCellRect( 0, columnsUndo._columns[ 0], true);
		scrollRectToVisible( rectangle);
	}

	/**
	 * @param insertRowsBlockUndo
	 * @return
	 */
	public void insertColumnsBlock(InsertColumnsBlockUndo insertColumnsBlockUndo) {
		if ( null != _undoableEditSupport && null != insertColumnsBlockUndo)
			_undoableEditSupport.postEdit( insertColumnsBlockUndo);

		Vector<Integer> columnWidths = new Vector();
		get_column_widths( columnWidths);

		insert_columns_block( insertColumnsBlockUndo, columnWidths);
	}

	/* (non-Javadoc)
	 * @see soars.common.utility.swing.table.base.undo_redo.base.ITableUndoRedoCallBack#insertColumnsBlock(java.lang.String, soars.common.utility.swing.table.base.undo_redo.column.InsertColumnsBlockUndo)
	 */
	public void insertColumnsBlock(String kind, InsertColumnsBlockUndo insertColumnsBlockUndo) {
		Vector<Integer> columnWidths = new Vector();
		get_column_widths( columnWidths);

		if ( kind.equals( "undo")) {
			int size = insertColumnsBlockUndo._columnTo - insertColumnsBlockUndo._columnFrom + 1;
			for ( int column = insertColumnsBlockUndo._columnFrom; column < getColumnCount() - size; ++column) {
				for ( int row = insertColumnsBlockUndo._rowFrom; row <= insertColumnsBlockUndo._rowTo; ++row)
					setValueAtDefault( getValueAt( row, column + size), row, column);
			}

			setColumnCount( getColumnCount() - size);

			for ( int i = 0; i < size; ++i)
				columnWidths.removeElementAt( columnWidths.size() - 1);

			setup_column( columnWidths);

			requestFocus();
			_spreadSheetTableBase.clearSelection();
			clearSelection();
			setRowSelectionInterval( insertColumnsBlockUndo._rowFrom, insertColumnsBlockUndo._rowTo);
			setColumnSelectionInterval( insertColumnsBlockUndo._columnFrom, insertColumnsBlockUndo._columnTo);

			requestFocus();
			internal_synchronize();

			Rectangle rectangle = getCellRect( insertColumnsBlockUndo._rowFrom, insertColumnsBlockUndo._columnFrom, true);
			scrollRectToVisible( rectangle);
		} else if ( kind.equals( "redo")) {
			insert_columns_block( insertColumnsBlockUndo, columnWidths);
		}
	}

	/**
	 * @param insertColumnsBlockUndo
	 * @param columnWidths
	 */
	private void insert_columns_block(InsertColumnsBlockUndo insertColumnsBlockUndo, Vector<Integer> columnWidths) {
		// TODO Auto-generated method stub
		int size = insertColumnsBlockUndo._columnTo - insertColumnsBlockUndo._columnFrom + 1;
		for ( int i = 0; i < size; ++i) {
			addColumn( "");
			columnWidths.add( new Integer( insertColumnsBlockUndo._width));
		}

		for ( int column = getColumnCount() - size - 1; column >= insertColumnsBlockUndo._columnFrom; --column) {
			for ( int row = insertColumnsBlockUndo._rowFrom; row <= insertColumnsBlockUndo._rowTo; ++row)
				setValueAtDefault( getValueAt( row, column), row, column + size);
		}
		for ( int column = insertColumnsBlockUndo._columnFrom; column <= insertColumnsBlockUndo._columnTo; ++column) {
			for ( int row = insertColumnsBlockUndo._rowFrom; row <= insertColumnsBlockUndo._rowTo; ++row)
				setValueAtDefault( get_default_value( row, column), row, column);
		}

		setup_column( columnWidths);

		requestFocus();
		_spreadSheetTableBase.clearSelection();
		clearSelection();
		setRowSelectionInterval( insertColumnsBlockUndo._rowFrom, insertColumnsBlockUndo._rowTo);
		setColumnSelectionInterval( insertColumnsBlockUndo._columnFrom, insertColumnsBlockUndo._columnTo);

		requestFocus();
		internal_synchronize();

		Rectangle rectangle = getCellRect( insertColumnsBlockUndo._rowFrom, insertColumnsBlockUndo._columnFrom, true);
		scrollRectToVisible( rectangle);
	}

	/**
	 * @param removeColumnsBlockUndo
	 * @return
	 */
	public void removeColumnsBlock(RemoveColumnsBlockUndo removeColumnsBlockUndo) {
		// TODO Auto-generated method stub
		if ( null != _undoableEditSupport && null != removeColumnsBlockUndo)
			_undoableEditSupport.postEdit( removeColumnsBlockUndo);

		remove_columns_block( removeColumnsBlockUndo);
	}

	/* (non-Javadoc)
	 * @see soars.common.utility.swing.table.base.undo_redo.base.ITableUndoRedoCallBack#removeColumnsBlock(java.lang.String, soars.common.utility.swing.table.base.undo_redo.column.RemoveColumnsBlockUndo)
	 */
	public void removeColumnsBlock(String kind, RemoveColumnsBlockUndo removeColumnsBlockUndo) {
		// TODO Auto-generated method stub
		if ( kind.equals( "undo")) {
			int size = removeColumnsBlockUndo._columnTo - removeColumnsBlockUndo._columnFrom + 1;
			for ( int column = getColumnCount() - size - 1; column >= removeColumnsBlockUndo._columnFrom; --column) {
				for ( int row = removeColumnsBlockUndo._rowFrom; row <= removeColumnsBlockUndo._rowTo; ++row)
					setValueAtDefault( getValueAt( row, column), row, column + size);
			}
			// TODO 元のデータを復元
			for ( int column = removeColumnsBlockUndo._columnFrom; column <= removeColumnsBlockUndo._columnTo; ++column) {
				for ( int row = removeColumnsBlockUndo._rowFrom; row <= removeColumnsBlockUndo._rowTo; ++row)
					setValueAtDefault( removeColumnsBlockUndo._objects[ row - removeColumnsBlockUndo._rowFrom][ column - removeColumnsBlockUndo._columnFrom], row, column);
			}

			requestFocus();
			_spreadSheetTableBase.clearSelection();
			clearSelection();
			setRowSelectionInterval( removeColumnsBlockUndo._rowFrom, removeColumnsBlockUndo._rowTo);
			setColumnSelectionInterval( removeColumnsBlockUndo._columnFrom, removeColumnsBlockUndo._columnTo);

			requestFocus();
			internal_synchronize();

			Rectangle rectangle = getCellRect( removeColumnsBlockUndo._rowFrom, removeColumnsBlockUndo._columnFrom, true);
			scrollRectToVisible( rectangle);
		} else if ( kind.equals( "redo")) {
			remove_columns_block( removeColumnsBlockUndo);
		}
	}

	/**
	 * @param removeColumnsBlockUndo
	 */
	private void remove_columns_block(RemoveColumnsBlockUndo removeColumnsBlockUndo) {
		// TODO Auto-generated method stub
		int size = removeColumnsBlockUndo._columnTo - removeColumnsBlockUndo._columnFrom + 1;
		for ( int column = removeColumnsBlockUndo._columnFrom; column < getColumnCount() - size; ++column) {
			for ( int row = removeColumnsBlockUndo._rowFrom; row <= removeColumnsBlockUndo._rowTo; ++row)
				setValueAtDefault( getValueAt( row, column + size), row, column);
		}
		// TODO 一番最後に空白を入れる
		for ( int i = 0; i < size; ++i) {
			for ( int row = removeColumnsBlockUndo._rowFrom; row <= removeColumnsBlockUndo._rowTo; ++row)
				setValueAtDefault( get_default_value( row, getColumnCount() - 1 - i), row, getColumnCount() - 1 - i);
		}

		requestFocus();
		_spreadSheetTableBase.clearSelection();
		clearSelection();
		setRowSelectionInterval( removeColumnsBlockUndo._rowFrom, removeColumnsBlockUndo._rowTo);
		setColumnSelectionInterval( removeColumnsBlockUndo._columnFrom, removeColumnsBlockUndo._columnTo);

		requestFocus();
		internal_synchronize();

		Rectangle rectangle = getCellRect( removeColumnsBlockUndo._rowFrom, removeColumnsBlockUndo._columnFrom, true);
		scrollRectToVisible( rectangle);
	}

	/**
	 * @param columnWidthUndo
	 */
	public void column_width_changed(ColumnWidthUndo columnWidthUndo) {
		if ( null == _undoableEditSupport || null == columnWidthUndo)
			return;

		_undoableEditSupport.postEdit( columnWidthUndo);
	}

	/**
	 * @param array
	 * @param size
	 * @return
	 */
	private int[] adjust(int[] array, int size) {
		List<Integer> list = new ArrayList<Integer>();
		for ( int number:array) {
			if ( number < size)
				list.add( new Integer( number));
		}
		if ( list.isEmpty())
			return ( new int[] { size - 1});

		int[] result = new int[ list.size()];
		for ( int i = 0; i < list.size(); ++i)
			result[ i] = list.get( i).intValue();
		return result;
	}

	/**
	 * @param spreadSheetTableRemoveRowsUndo 
	 * 
	 */
	private void adjust_row_count(SpreadSheetTableRemoveRowsUndo spreadSheetTableRemoveRowsUndo) {
		if ( null != spreadSheetTableRemoveRowsUndo)
			spreadSheetTableRemoveRowsUndo._appendedRowCount = 0;

		while ( getRowCount() < _minimumRowCount) {
			addRow();
			_spreadSheetTableBase.addRow();
			if ( null != spreadSheetTableRemoveRowsUndo)
				++spreadSheetTableRemoveRowsUndo._appendedRowCount;
		}
	}

	/**
	 * @param length
	 * @param columnWidths
	 * @param width
	 * @param spreadSheetTableRemoveColumnsUndo 
	 */
	private void adjust_column_count(int length, Vector<Integer> columnWidths, int width, SpreadSheetTableRemoveColumnsUndo spreadSheetTableRemoveColumnsUndo) {
		if ( null != spreadSheetTableRemoveColumnsUndo)
			spreadSheetTableRemoveColumnsUndo._appendedColumnCount = 0;

		setColumnCount( getColumnCount() - length);
		while ( getColumnCount() < _minimumColumnCount) {
			addColumn( "");
			columnWidths.add( new Integer( width));
			if ( null != spreadSheetTableRemoveColumnsUndo)
				++spreadSheetTableRemoveColumnsUndo._appendedColumnCount;
		}
	}

	/**
	 * @param rows
	 */
	private void scroll_row(int[] rows) {
		Rectangle rectangle = getCellRect( rows[ rows.length - 1], 0, true);
		scrollRectToVisible( rectangle);
		rectangle = getCellRect( rows[ 0], 0, true);
		scrollRectToVisible( rectangle);
	}

	/**
	 * @param columns
	 */
	private void scroll_column(int[] columns) {
		Rectangle rectangle = getCellRect( 0, columns[ columns.length - 1], true);
		scrollRectToVisible( rectangle);
		rectangle = getCellRect( 0, columns[ 0], true);
		scrollRectToVisible( rectangle);
	}
}
