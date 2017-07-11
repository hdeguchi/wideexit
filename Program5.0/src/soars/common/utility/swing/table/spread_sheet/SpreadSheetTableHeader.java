/**
 * 
 */
package soars.common.utility.swing.table.spread_sheet;

import java.awt.Component;
import java.awt.Frame;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.util.Arrays;

import javax.swing.SwingUtilities;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;

import soars.common.utility.swing.table.base.StandardTableHeader;
import soars.common.utility.swing.table.base.undo_redo.column.ColumnWidthUndo;
import soars.common.utility.swing.tool.SwingTool;

/**
 * @author kurata
 *
 */
public class SpreadSheetTableHeader extends StandardTableHeader {

	/**
	 * 
	 */
	protected SpreadSheetTable _spreadSheetTable = null;

	/**
	 * 
	 */
	protected int _previousColumn;

	/**
	 * 
	 */
	protected boolean _selectedOnPress = false;

	/**
	 * 
	 */
	protected Component dispatchComponent = null;

	/**
	 * 
	 */
	protected ColumnWidthUndo _columnWidthUndo = null;

	/**
	 * @param spreadSheetTable
	 * @param tableColumnModel
	 * @param owner
	 * @param parent
	 */
	public SpreadSheetTableHeader(SpreadSheetTable spreadSheetTable, TableColumnModel tableColumnModel, Frame owner, Component parent) {
		super(tableColumnModel, owner, parent);
		_spreadSheetTable = spreadSheetTable;
	}

	/**
	 * @param tableCellRenderer
	 * @param popupMenu
	 * @return
	 */
	public boolean setup(TableCellRenderer tableCellRenderer, boolean popupMenu) {
		if (!super.setup(_spreadSheetTable, tableCellRenderer, popupMenu))
			return false;

		return true;
	}

	/* (non-Javadoc)
	 * @see soars.common.utility.swing.table.base.StandardTableHeader#on_mouse_pressed(java.awt.event.MouseEvent)
	 */
	protected void on_mouse_pressed(MouseEvent mouseEvent) {
		if ( SwingTool.is_mouse_right_button( mouseEvent))
			return;

		if ( mouseEvent.isConsumed()) {
			_selectedOnPress = false;
			return;
		}

		if ( is_resizing_mouseEvent( mouseEvent)) {
			_selectedOnPress = false;
			TableColumn tableColumn = getResizingColumn();
			if ( null == tableColumn)
				return;

			_columnWidthUndo = new ColumnWidthUndo( tableColumn, this, _spreadSheetTable, _spreadSheetTable);
			return;
		}

		_selectedOnPress = true;

//		if ( should_ignore_mouseEvent( mouseEvent))
//		return;

		_spreadSheetTable.stop_cell_editing();

		Point point = mouseEvent.getPoint();
		int column = _spreadSheetTable.columnAtPoint( point);
		if ( 0 > column)
			return;

		//--- SwingUtilities2.adjustFocus(table)
		if ( !_spreadSheetTable.hasFocus() && _spreadSheetTable.isRequestFocusEnabled())
			_spreadSheetTable.requestFocus();

		make_selection_change( column, mouseEvent);
		_previousColumn = column;
	}

	/**
	 * @param column
	 * @param mouseEvent
	 */
	protected void make_selection_change(int column, MouseEvent mouseEvent) {
//		if ( !_enableChangeSelection)
//			return;
		
		boolean ctrl = ( 0 <= System.getProperty( "os.name").indexOf( "Mac"))
			? mouseEvent.isMetaDown() : mouseEvent.isControlDown();
		_spreadSheetTable.select_column( column, ctrl, ( !ctrl && mouseEvent.isShiftDown()));
	}

	/* (non-Javadoc)
	 * @see soars.common.utility.swing.table.base.StandardTableHeader#on_mouse_dragged(java.awt.event.MouseEvent)
	 */
	protected void on_mouse_dragged(MouseEvent mouseEvent) {
		if ( !_selectedOnPress)
			return;

		repost_event( mouseEvent);

		boolean ctrl = ( 0 <= System.getProperty( "os.name").indexOf( "Mac"))
			? mouseEvent.isMetaDown() : mouseEvent.isControlDown();

		Point point = mouseEvent.getPoint();
		int column = _spreadSheetTable.columnAtPoint( point);
		if ( 0 > column)
			return;

		_spreadSheetTable.drag_on_column( column, _previousColumn, ctrl);
		_previousColumn = column;
	}

	/**
	 * @param mouseEvent
	 * @return
	 */
	protected boolean repost_event(MouseEvent mouseEvent) {
		// Check for isEditing() in case another event has
		// caused the editor to be removed. See bug #4306499.
		if ( null == dispatchComponent || !_spreadSheetTable.isEditing())
			return false;

		MouseEvent e2 = SwingUtilities.convertMouseEvent( _spreadSheetTable, mouseEvent, dispatchComponent);
		dispatchComponent.dispatchEvent( e2);
		return true;
	}

//	/**
//	 * @param mouseEvent
//	 * @return
//	 */
//	protected boolean should_ignore_mouseEvent(MouseEvent mouseEvent) {
//		return ( mouseEvent.isConsumed() || !SwingUtilities.isLeftMouseButton( mouseEvent) || null == _spreadSheetTable || !_spreadSheetTable.isEnabled());
//		//--- return e.isConsumed() || SwingUtilities2.shouldIgnore( mouseEvent, _spreadSheetTable);
//		//--- SwingUtilities2 は使用しない。
//	}
//
//	/**
//	 * @param flag
//	 */
//	protected void set_value_is_adjusting(boolean flag) {
//		_spreadSheetTable.getSelectionModel().setValueIsAdjusting( flag);
//		_spreadSheetTable.getColumnModel().getSelectionModel().setValueIsAdjusting( flag);
//	}

	/**
	 * @param mouseEvent
	 * @return
	 */
	protected boolean is_resizing_mouseEvent(MouseEvent mouseEvent) {
		return ( SwingUtilities.isLeftMouseButton( mouseEvent) && can_resize( get_resizing_column_index( mouseEvent.getPoint())));
	}

	/**
	 * @param column
	 * @return
	 */
	protected boolean can_resize(int column) {
		return ( ( 0 > column) ? false : can_resize( _spreadSheetTable.getTableHeader().getColumnModel().getColumn( column)));
	}

	/**
	 * @param tableColumn
	 * @return
	 */
	protected boolean can_resize(TableColumn tableColumn) { 
		return ( ( null != tableColumn) && _spreadSheetTable.getTableHeader().getResizingAllowed() && tableColumn.getResizable()); 
	}

	/**
	 * @param point
	 */
	protected int get_resizing_column_index(Point point) {
		return get_resizing_column_index( point, _spreadSheetTable.getTableHeader().columnAtPoint( point));
	}
	
	/**
	 * @param point
	 * @param column
	 * @return
	 */
	protected int get_resizing_column_index(Point point, int column) {
		if ( 0 > column)
			return -1;
		
		Rectangle rectangle = _spreadSheetTable.getTableHeader().getHeaderRect( column);
		rectangle.grow( -3, 0);
		if ( rectangle.contains( point))
			return -1;

		int midPoint = rectangle.x + rectangle.width / 2;
		
		int target;
		if ( _spreadSheetTable.getTableHeader().getComponentOrientation().isLeftToRight())
			target = ( point.x < midPoint) ? column - 1 : column;
		else
			target = ( point.x < midPoint) ? column : column - 1;

		if ( 0 > target)
			return -1;
		
		return target;
	}

	/* (non-Javadoc)
	 * @see soars.common.utility.swing.table.base.StandardTableHeader#on_mouse_released(java.awt.event.MouseEvent)
	 */
	protected void on_mouse_released(MouseEvent mouseEvent) {
		if ( !_selectedOnPress) {
			if ( is_resizing_mouseEvent( mouseEvent) && null != _columnWidthUndo) {
				_columnWidthUndo.resize_completed();
				_spreadSheetTable.column_width_changed( _columnWidthUndo);
				return;
			}
		}

		_selectedOnPress = false;
		if ( !SwingTool.is_mouse_right_button( mouseEvent))
			return;

		int column = columnAtPoint( mouseEvent.getPoint());
		if ( 0 > column)
			return;

		on_mouse_right_up( column, mouseEvent);
	}

	/**
	 * @param column 
	 * @param mouseEvent
	 * @return
	 */
	protected boolean on_mouse_right_up(int column, MouseEvent mouseEvent) {
		_spreadSheetTable.requestFocus();

		int[] columns = _spreadSheetTable.getSelectedColumns();
		if ( null == columns || 0 == columns.length)
			return false;

		_spreadSheetTable.stop_cell_editing();

		Arrays.sort( columns);
		if ( ( 0 > Arrays.binarySearch( columns, column))
			|| _spreadSheetTable.getSelectedRowCount() != _spreadSheetTable.getRowCount()) {
			_spreadSheetTable.select_column( column, column);
			//make_selection_change( column, mouseEvent);
			_previousColumn = column;
		}

		repaint();

		return true;
	}
}
