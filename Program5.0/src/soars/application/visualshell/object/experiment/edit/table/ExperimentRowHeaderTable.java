/**
 * 
 */
package soars.application.visualshell.object.experiment.edit.table;

import java.awt.Component;
import java.awt.FontMetrics;
import java.awt.Frame;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.swing.JMenuItem;
import javax.swing.table.DefaultTableColumnModel;
import javax.swing.table.DefaultTableModel;

import soars.application.visualshell.common.menu.basic3.AppendRowAction;
import soars.application.visualshell.common.menu.basic3.CopyRowAction;
import soars.application.visualshell.common.menu.basic3.CutRowAction;
import soars.application.visualshell.common.menu.basic3.IBasicMenuHandler3;
import soars.application.visualshell.common.menu.basic3.InsertRowAction;
import soars.application.visualshell.common.menu.basic3.PasteRowAction;
import soars.application.visualshell.common.menu.basic3.RemoveRowAction;
import soars.application.visualshell.main.ResourceManager;
import soars.application.visualshell.object.experiment.ExperimentManager;
import soars.application.visualshell.object.experiment.InitialValueMap;
import soars.application.visualshell.object.experiment.edit.table.data.ExperimentRowHeaderData;
import soars.application.visualshell.object.experiment.edit.table.menu.ExportScriptAction;
import soars.application.visualshell.object.experiment.edit.table.menu.ExportToClipboardAction;
import soars.application.visualshell.object.experiment.edit.table.menu.NotExportScriptAction;
import soars.common.utility.swing.table.base.data.RowData;
import soars.common.utility.swing.table.base.undo_redo.cell.SetValueUndo;
import soars.common.utility.swing.table.spread_sheet.SpreadSheetRowHeaderTable;
import soars.common.utility.tool.sort.QuickSort;
import soars.common.utility.tool.sort.StringNumberComparator;

/**
 * @author kurata
 *
 */
public class ExperimentRowHeaderTable extends SpreadSheetRowHeaderTable implements IBasicMenuHandler3 {

	/**
	 * 
	 */
	private JMenuItem _copyRowMenuItem = null;

	/**
	 * 
	 */
	private JMenuItem _cutRowMenuItem = null;

	/**
	 * 
	 */
	private JMenuItem _pasteRowMenuItem = null;

	/**
	 * 
	 */
	private JMenuItem _appendRowMenuItem = null;

	/**
	 * 
	 */
	private JMenuItem _insertRowMenuItem = null;

	/**
	 * 
	 */
	private JMenuItem _removeRowMenuItem = null;

	/**
	 * 
	 */
	private JMenuItem _exportScriptMenuItem = null;

	/**
	 * 
	 */
	private JMenuItem _notExportScriptMenuItem = null;

	/**
	 * 
	 */
	private JMenuItem _exportToClipboardMenuItem = null;

	/**
	 * 
	 */
	private final int _defaultColumnWidth = 40;

	/**
	 * @param owner
	 * @param parent
	 */
	public ExperimentRowHeaderTable(Frame owner, Component parent) {
		super(owner, parent);
	}

	/**
	 * @param experimentManager 
	 * @param experimentTable
	 * @param graphics
	 * @return
	 */
	public boolean setup(ExperimentManager experimentManager, ExperimentTable experimentTable, Graphics2D graphics2D) {
		if ( !super.setup(experimentTable, true))
			return false;

		getTableHeader().setReorderingAllowed( false);
		setFillsViewportHeight( true);

		DefaultTableColumnModel defaultTableColumnModel = ( DefaultTableColumnModel)getColumnModel();
		defaultTableColumnModel.getColumn( 0).setCellRenderer( new ExperimentRowHeaderTableCellRenderer());

		int column_width = get_column_width( 100, graphics2D);
		defaultTableColumnModel.getColumn( 0).setMinWidth( column_width);
		defaultTableColumnModel.getColumn( 0).setMaxWidth( column_width);

		addRow( new Object[] { null});

		if ( !experimentManager.isEmpty()) {
			String[] names = ( String[])( new ArrayList( experimentManager.keySet())).toArray( new String[ 0]);
			QuickSort.sort( names, new StringNumberComparator( true, false));

			for ( int i = 0; i < names.length; ++i) {
				InitialValueMap initialValueMap = ( InitialValueMap)experimentManager.get( names[ i]);
				addRow( new Object[] { new ExperimentRowHeaderData( initialValueMap._export)});
			}
		}

		if ( 0 < getRowCount()) {
			setRowSelectionInterval( 0, 0);
			setColumnSelectionInterval( 0, 0);
		}

		setToolTipText( "ExperimentRowHeaderTable");

		return true;
	}

	/**
	 * @param rows
	 * @param graphics2D
	 * @return
	 */
	private int get_column_width(int rows, Graphics2D graphics2D) {
		if ( 0 == rows || null == graphics2D)
			return _defaultColumnWidth;

		String maxNumber = String.valueOf( rows);
		FontMetrics fontMetrics = graphics2D.getFontMetrics();
		return ( Math.max( _defaultColumnWidth, fontMetrics.stringWidth( maxNumber) * 3 / 2));
	}

	/* (non-Javadoc)
	 * @see soars.common.utility.swing.table.base.StandardTable#setup_popup_menu()
	 */
	@Override
	protected void setup_popup_menu() {
		super.setup_popup_menu();

		_copyRowMenuItem = _userInterface.append_popup_menuitem(
			_popupMenu,
			ResourceManager.get_instance().get( "common.popup.menu.copy.row.menu"),
			new CopyRowAction( ResourceManager.get_instance().get( "common.popup.menu.copy.row.menu"), this),
			ResourceManager.get_instance().get( "common.popup.menu.copy.row.mnemonic"),
			ResourceManager.get_instance().get( "common.popup.menu.copy.row.stroke"));
		_cutRowMenuItem = _userInterface.append_popup_menuitem(
			_popupMenu,
			ResourceManager.get_instance().get( "common.popup.menu.cut.row.menu"),
			new CutRowAction( ResourceManager.get_instance().get( "common.popup.menu.cut.row.menu"), this),
			ResourceManager.get_instance().get( "common.popup.menu.cut.row.mnemonic"),
			ResourceManager.get_instance().get( "common.popup.menu.cut.row.stroke"));
		_pasteRowMenuItem = _userInterface.append_popup_menuitem(
			_popupMenu,
			ResourceManager.get_instance().get( "common.popup.menu.paste.row.menu"),
			new PasteRowAction( ResourceManager.get_instance().get( "common.popup.menu.paste.row.menu"), this),
			ResourceManager.get_instance().get( "common.popup.menu.paste.row.mnemonic"),
			ResourceManager.get_instance().get( "common.popup.menu.paste.row.stroke"));

		_popupMenu.addSeparator();

		_appendRowMenuItem = _userInterface.append_popup_menuitem(
			_popupMenu,
			ResourceManager.get_instance().get( "common.popup.menu.append.row.menu"),
			new AppendRowAction( ResourceManager.get_instance().get( "common.popup.menu.append.row.menu"), this),
			ResourceManager.get_instance().get( "common.popup.menu.append.row.mnemonic"),
			ResourceManager.get_instance().get( "common.popup.menu.append.row.stroke"));
		_insertRowMenuItem = _userInterface.append_popup_menuitem(
			_popupMenu,
			ResourceManager.get_instance().get( "common.popup.menu.insert.row.menu"),
			new InsertRowAction( ResourceManager.get_instance().get( "common.popup.menu.insert.row.menu"), this),
			ResourceManager.get_instance().get( "common.popup.menu.insert.row.mnemonic"),
			ResourceManager.get_instance().get( "common.popup.menu.insert.row.stroke"));
		_removeRowMenuItem = _userInterface.append_popup_menuitem(
			_popupMenu,
			ResourceManager.get_instance().get( "common.popup.menu.remove.row.menu"),
			new RemoveRowAction( ResourceManager.get_instance().get( "common.popup.menu.remove.row.menu"), this),
			ResourceManager.get_instance().get( "common.popup.menu.remove.row.mnemonic"),
			ResourceManager.get_instance().get( "common.popup.menu.remove.row.stroke"));

		_popupMenu.addSeparator();

		_exportScriptMenuItem = _userInterface.append_popup_menuitem(
			_popupMenu,
			ResourceManager.get_instance().get( "export.script.menu"),
			new ExportScriptAction( ResourceManager.get_instance().get( "export.script.menu"), this),
			ResourceManager.get_instance().get( "export.script.mnemonic"),
			ResourceManager.get_instance().get( "export.script.stroke"));
		_notExportScriptMenuItem = _userInterface.append_popup_menuitem(
			_popupMenu,
			ResourceManager.get_instance().get( "not.export.script.menu"),
			new NotExportScriptAction( ResourceManager.get_instance().get( "not.export.script.menu"), this),
			ResourceManager.get_instance().get( "not.export.script.mnemonic"),
			ResourceManager.get_instance().get( "not.export.script.stroke"));

		_popupMenu.addSeparator();

		_exportToClipboardMenuItem = _userInterface.append_popup_menuitem(
			_popupMenu,
			ResourceManager.get_instance().get( "file.export.to.clipboard.menu"),
			new ExportToClipboardAction( ResourceManager.get_instance().get( "file.export.to.clipboard.menu"), this),
			ResourceManager.get_instance().get( "file.export.to.clipboard.mnemonic"),
			ResourceManager.get_instance().get( "file.export.to.clipboard.stroke"));
	}

	/**
	 * @param point
	 */
	public void on_mouse_right_up(Point point) {
		on_mouse_right_up( rowAtPoint( point), columnAtPoint( point), point);
	}

	/* (non-Javadoc)
	 * @see soars.common.utility.swing.table.spread_sheet.SpreadSheetRowHeaderTable#on_mouse_right_up(int, int, java.awt.event.MouseEvent)
	 */
	@Override
	protected boolean on_mouse_right_up(int row, int column, MouseEvent mouseEvent) {
		if ( !super.on_mouse_right_up(row, column, mouseEvent))
			return false;

		return on_mouse_right_up( row, column, new Point( mouseEvent.getX(), mouseEvent.getY()));
	}

	/**
	 * @param row
	 * @param column
	 * @param point
	 * @return
	 */
	private boolean on_mouse_right_up(int row, int column, Point point) {
		_copyRowMenuItem.setEnabled( true);
		_cutRowMenuItem.setEnabled( true);
		_pasteRowMenuItem.setEnabled( true);
		_appendRowMenuItem.setEnabled( true);
		_insertRowMenuItem.setEnabled( true);
		_removeRowMenuItem.setEnabled( true);
		_exportScriptMenuItem.setEnabled( true);
		_notExportScriptMenuItem.setEnabled( true);
		_exportToClipboardMenuItem.setEnabled( true);

		DefaultTableModel defaultTableModel = ( DefaultTableModel)getModel();
		if ( 0 == defaultTableModel.getRowCount()) {
			_copyRowMenuItem.setEnabled( false);
			_pasteRowMenuItem.setEnabled( false);
			_insertRowMenuItem.setEnabled( false);
			_removeRowMenuItem.setEnabled( false);
			_exportScriptMenuItem.setEnabled( false);
			_notExportScriptMenuItem.setEnabled( false);
			_exportToClipboardMenuItem.setEnabled( false);
		} else {
			if ( ( 0 <= row && getRowCount() > row)
				&& ( 0 <= column && getColumnCount() > column)) {
				//setRowSelectionInterval( row, row);
				//setColumnSelectionInterval( column, column);
				int[] rows = getSelectedRows();
				boolean contains = ( 0 <= Arrays.binarySearch( rows, row));
				boolean contains0 = ( 0 <= Arrays.binarySearch( rows, 0));
				_copyRowMenuItem.setEnabled( contains && ( !contains0 || 1 < rows.length));
				_cutRowMenuItem.setEnabled( contains && ( !contains0 || 1 < rows.length));
				_pasteRowMenuItem.setEnabled( !ExperimentTable._selectedRowData.isEmpty() && 1 == rows.length && contains && !contains0);
				_insertRowMenuItem.setEnabled( 1 == rows.length && contains && !contains0);
				_removeRowMenuItem.setEnabled( contains && ( !contains0 || 1 < rows.length)/* && rows.length < defaultTableModel.getRowCount() - 1*/);
				_exportScriptMenuItem.setEnabled( contains && ( !contains0 || 1 < rows.length));
				_notExportScriptMenuItem.setEnabled( contains && ( !contains0 || 1 < rows.length));
				_exportToClipboardMenuItem.setEnabled( 1 == rows.length && contains && !contains0);
			} else {
				_copyRowMenuItem.setEnabled( false);
				_cutRowMenuItem.setEnabled( false);
				_pasteRowMenuItem.setEnabled( false);
				_insertRowMenuItem.setEnabled( false);
				_removeRowMenuItem.setEnabled( false);
				_exportScriptMenuItem.setEnabled( false);
				_notExportScriptMenuItem.setEnabled( false);
				_exportToClipboardMenuItem.setEnabled( false);
			}
		}

		_popupMenu.show( this, point.x, point.y);

		return true;
	}

	/* (non-Javadoc)
	 * @see soars.common.utility.swing.table.spread_sheet.SpreadSheetTableBase#on_undo()
	 */
	@Override
	public void on_undo() {
		_spreadSheetTableBase.on_undo();
	}

	/* (non-Javadoc)
	 * @see soars.common.utility.swing.table.spread_sheet.SpreadSheetTableBase#on_redo()
	 */
	@Override
	public void on_redo() {
		_spreadSheetTableBase.on_redo();
	}

	/* (non-Javadoc)
	 * @see soars.common.utility.swing.table.base.StandardTable#addRow()
	 */
	@Override
	public void addRow() {
		addRow( new Object[] { new ExperimentRowHeaderData( false)});
	}

	/* (non-Javadoc)
	 * @see soars.common.utility.swing.table.base.StandardTable#insertRow(int)
	 */
	@Override
	public void insertRow(int row) {
		insertRow( row, new Object[] { new ExperimentRowHeaderData( false)});
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.common.menu.basic3.IBasicMenuHandler3#on_append_row(java.awt.event.ActionEvent)
	 */
	@Override
	public void on_append_row(ActionEvent actionEvent) {
		insertRows( new int[] { getRowCount()});
		_spreadSheetTableBase.insertRows( new int[] { getRowCount()});
//		addRow( new Object[] { new Integer( getRowCount())});
//		_spreadSheetTableBase.addRow();
		//_spreadSheetTableBase.insertRow( _spreadSheetTableBase.getRowCount());
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.common.menu.basic3.IBasicMenuHandler3#on_insert_row(java.awt.event.ActionEvent)
	 */
	@Override
	public void on_insert_row(ActionEvent actionEvent) {
		int[] rows = getSelectedRows();
		if ( null == rows || 0 == rows.length)
			return;

		Arrays.sort( rows);

		_spreadSheetTableBase.insertRows( rows);

		ExperimentTable.refresh();
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.common.menu.basic3.IBasicMenuHandler3#on_remove_row(java.awt.event.ActionEvent)
	 */
	@Override
	public void on_remove_row(ActionEvent actionEvent) {
		if ( 2 > getRowCount())
			return;

		int[] rows = getSelectedRows();
		if ( null == rows || 0 == rows.length || getRowCount() == rows.length)
			return;

		Arrays.sort( rows);

		remove_row( rows);
	}

	/**
	 * 
	 */
	public void remove_all() {
		if ( 2 > getRowCount())
			return;

		int[] rows = new int[ getRowCount() - 1];
		for ( int index = 0, row = 1; row < getRowCount(); ++index, ++row)
			rows[ index] = row;

		remove_row( rows);
	}

	/**
	 * @param rows
	 * @return
	 */
	private boolean remove_row(int[] rows) {
		_spreadSheetTableBase.removeRows( rows);
		ExperimentTable.refresh();
		return true;
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.common.menu.basic3.IBasicMenuHandler3#on_copy_row(java.awt.event.ActionEvent)
	 */
	@Override
	public void on_copy_row(ActionEvent actionEvent) {
		int[] rows = getSelectedRows();
		if ( null == rows || 0 == rows.length)
			return;

		Arrays.sort( rows);

		copy_row( rows);
	}

	/**
	 * @param rows
	 * @return
	 */
	private boolean copy_row(int[] rows) {
		ExperimentTable.refresh();

		int minRow = rows[ 0];

		for ( int row:rows)
			ExperimentTable._selectedRowData.add( new RowData<String>( row - minRow, new ExperimentRowHeaderData()));
//		for ( int row:rows) {
//			ExperimentRowHeaderData experimentRowHeaderData = ( ExperimentRowHeaderData)getValueAt( row, 0);
//			ExperimentTable._selectedRowData.add( ( null == experimentRowHeaderData) ? ( new RowData<String>( row - minRow)) : ( new RowData<String>( row - minRow, new ExperimentRowHeaderData( experimentRowHeaderData))));
//		}

		_spreadSheetTableBase.on_copy_row( rows);

		return true;
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.common.menu.basic3.IBasicMenuHandler3#on_cut_row(java.awt.event.ActionEvent)
	 */
	@Override
	public void on_cut_row(ActionEvent actionEvent) {
		int[] rows = getSelectedRows();
		if ( 0 == getRowCount())
			return;

		cut_row( rows);

		repaint();
	}

	/**
	 * @param rows
	 * @return
	 */
	private boolean cut_row(int[] rows) {
		if ( !copy_row( rows))
			return false;

		List<List<SetValueUndo>> rowHeaderSetValueUndoLists = new ArrayList<List<SetValueUndo>>();
		for ( int row:rows) {
			List<SetValueUndo> rowHeaderSetValueUndoList = new ArrayList<SetValueUndo>();
			rowHeaderSetValueUndoList.add( new SetValueUndo( new ExperimentRowHeaderData(), row, 0, this, null));
			rowHeaderSetValueUndoLists.add( rowHeaderSetValueUndoList);
		}

		_spreadSheetTableBase.on_cut_row( rows, rowHeaderSetValueUndoLists);

		return true;
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.common.menu.basic3.IBasicMenuHandler3#on_paste_row(java.awt.event.ActionEvent)
	 */
	@Override
	public void on_paste_row(ActionEvent actionEvent) {
		int[] rows = getSelectedRows();
		if ( 0 == getRowCount() || 1 != rows.length)
			return;

		paste_row( rows[ 0]);
	}

	/**
	 * @param baseRow
	 * @return
	 */
	private boolean paste_row(int baseRow) {
		if ( ExperimentTable._selectedRowData.isEmpty())
			return false;

		List<List<SetValueUndo>> rowHeaderSetValueUndoLists = new ArrayList<List<SetValueUndo>>();
		for ( RowData<String> rowData:ExperimentTable._selectedRowData) {
			int row = baseRow + rowData._row;

			// はみ出している場合のチェック
			if ( getRowCount() <= row)
				continue;

			List<SetValueUndo> rowHeaderSetValueUndoList = new ArrayList<SetValueUndo>();
			rowHeaderSetValueUndoList.add( new SetValueUndo( rowData._rowHeaderData, row, 0, this, null));
			rowHeaderSetValueUndoLists.add( rowHeaderSetValueUndoList);
		}

		_spreadSheetTableBase.on_paste_row( baseRow, rowHeaderSetValueUndoLists);

		return true;
	}

	/**
	 * @param actionEvent
	 */
	public void on_export_script(ActionEvent actionEvent) {
		int[] rows = getSelectedRows();
		DefaultTableModel defaultTableModel = ( DefaultTableModel)getModel();
		if ( 1 == defaultTableModel.getRowCount())
			return;

		( ( ExperimentTable)_spreadSheetTableBase).set_export_script( rows, true);
	}

	/**
	 * @param actionEvent
	 */
	public void on_not_export_script(ActionEvent actionEvent) {
		int[] rows = getSelectedRows();
		DefaultTableModel defaultTableModel = ( DefaultTableModel)getModel();
		if ( 1 == defaultTableModel.getRowCount())
			return;

		( ( ExperimentTable)_spreadSheetTableBase).set_export_script( rows, false);
	}

	/**
	 * @param actionEvent
	 */
	public void on_export_to_clipboard(ActionEvent actionEvent) {
		int row = getSelectedRow();
		DefaultTableModel defaultTableModel = ( DefaultTableModel)getModel();
		if ( 1 == defaultTableModel.getRowCount() || 1 > row)
			return;

		( ( ExperimentTable)_spreadSheetTableBase).export_to_clipboard( row);
	}
}
