/**
 * 
 */
package soars.application.visualshell.object.gis.edit.table;

import java.awt.Component;
import java.awt.FontMetrics;
import java.awt.Frame;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.event.ListSelectionEvent;
import javax.swing.table.DefaultTableColumnModel;

import soars.application.visualshell.common.menu.basic3.CopyRowAction;
import soars.application.visualshell.common.menu.basic3.CutRowAction;
import soars.application.visualshell.common.menu.basic3.IBasicMenuHandler3;
import soars.application.visualshell.common.menu.basic3.PasteRowAction;
import soars.application.visualshell.common.menu.basic3.RemoveRowAction;
import soars.application.visualshell.main.ResourceManager;
import soars.application.visualshell.object.gis.GisData;
import soars.common.utility.swing.table.base.data.RowData;
import soars.common.utility.swing.table.base.data.RowHeaderData;
import soars.common.utility.swing.table.base.undo_redo.cell.SetValueUndo;
import soars.common.utility.swing.table.spread_sheet.SpreadSheetRowHeaderTable;
import soars.common.utility.swing.table.spread_sheet.SpreadSheetTableBase;

/**
 * @author kurata
 *
 */
public class GisDataRowHeaderTable extends SpreadSheetRowHeaderTable implements IBasicMenuHandler3 {

	/**
	 * 
	 */
	private JMenuItem _removeRowMenuItem = null;

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
	private final int _defaultColumnWidth = 40;

	/**
	 * @param owner
	 * @param parent
	 */
	public GisDataRowHeaderTable(Frame owner, Component parent) {
		super(owner, parent);
	}

	/**
	 * @param gisData
	 * @param spreadSheetTableBase
	 * @param graphics2D
	 * @return
	 */
	public boolean setup(GisData gisData, SpreadSheetTableBase spreadSheetTableBase, Graphics2D graphics2D) {
		if (!super.setup(spreadSheetTableBase, true))
			return false;

		getTableHeader().setReorderingAllowed( false);
		setFillsViewportHeight( true);

		DefaultTableColumnModel defaultTableColumnModel = ( DefaultTableColumnModel)getColumnModel();
		defaultTableColumnModel.getColumn( 0).setCellRenderer( new GisDataRowHeaderTableCellRenderer());

		int column_width = get_column_width( gisData.size(), graphics2D);
		defaultTableColumnModel.getColumn( 0).setMinWidth( column_width);
		defaultTableColumnModel.getColumn( 0).setMaxWidth( column_width);

		for ( int i = 0; i < gisData.size(); ++i)
			addRow( new Object[] { null});

		if ( 0 < getRowCount()) {
			setRowSelectionInterval( 0, 0);
			setColumnSelectionInterval( 0, 0);
		}

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

		String max_number = String.valueOf( rows);
		FontMetrics fontMetrics = graphics2D.getFontMetrics();
		return ( Math.max( _defaultColumnWidth,
			fontMetrics.stringWidth( max_number) * 3 / 2));
	}

	/* (non-Javadoc)
	 * @see soars.common.utility.swing.table.spread_sheet.SpreadSheetTableBase#valueChanged(javax.swing.event.ListSelectionEvent)
	 */
	public void valueChanged(ListSelectionEvent e) {
		super.valueChanged(e);
		( ( GisDataTable)_spreadSheetTableBase).set_text_to_textField();
	}

	/* (non-Javadoc)
	 * @see soars.common.utility.swing.table.spread_sheet.SpreadSheetTableBase#changeSelection(int, int, boolean, boolean)
	 */
	public void changeSelection(int rowIndex, int columnIndex, boolean toggle, boolean extend) {
		super.changeSelection(rowIndex, columnIndex, toggle, extend);
		( ( GisDataTable)_spreadSheetTableBase).set_text_to_textField();
	}

	/* (non-Javadoc)
	 * @see soars.common.utility.swing.table.spread_sheet.SpreadSheetTableBase#on_enter(java.awt.event.ActionEvent)
	 */
	public void on_enter(ActionEvent actionEvent) {
		_spreadSheetTableBase.on_enter(actionEvent);
	}

	/* (non-Javadoc)
	 * @see soars.common.utility.swing.table.spread_sheet.SpreadSheetTableBase#on_escape(java.awt.event.ActionEvent)
	 */
	public void on_escape(ActionEvent actionEvent) {
		_spreadSheetTableBase.on_escape(actionEvent);
	}

	/* (non-Javadoc)
	 * @see soars.common.utility.swing.table.spread_sheet.SpreadSheetTableBase#on_remove(java.awt.event.ActionEvent)
	 */
	public void on_remove(ActionEvent actionEvent) {
		on_remove_row(actionEvent);
		//_spreadSheetTableBase.on_remove(actionEvent);
	}

	/* (non-Javadoc)
	 * @see soars.common.utility.swing.table.spread_sheet.SpreadSheetTableBase#on_copy(java.awt.event.ActionEvent)
	 */
	public void on_copy(ActionEvent actionEvent) {
		on_copy_row(actionEvent);
		//_spreadSheetTableBase.on_copy(actionEvent);
	}

	/* (non-Javadoc)
	 * @see soars.common.utility.swing.table.spread_sheet.SpreadSheetTableBase#on_cut(java.awt.event.ActionEvent)
	 */
	public void on_cut(ActionEvent actionEvent) {
		on_cut_row(actionEvent);
		//_spreadSheetTableBase.on_cut(actionEvent);
	}

	/* (non-Javadoc)
	 * @see soars.common.utility.swing.table.spread_sheet.SpreadSheetTableBase#on_paste(java.awt.event.ActionEvent)
	 */
	public void on_paste(ActionEvent actionEvent) {
		on_paste_row(actionEvent);
		//_spreadSheetTableBase.on_paste(actionEvent);
	}

	/* (non-Javadoc)
	 * @see soars.common.utility.swing.table.spread_sheet.SpreadSheetTableBase#on_undo()
	 */
	public void on_undo() {
		_spreadSheetTableBase.on_undo();
	}

	/* (non-Javadoc)
	 * @see soars.common.utility.swing.table.spread_sheet.SpreadSheetTableBase#on_redo()
	 */
	public void on_redo() {
		_spreadSheetTableBase.on_redo();
	}

	/* (non-Javadoc)
	 * @see soars.common.utility.swing.table.base.StandardTable#setup_popup_menu()
	 */
	protected void setup_popup_menu() {
		super.setup_popup_menu();

		_removeRowMenuItem = _userInterface.append_popup_menuitem(
			_popupMenu,
			ResourceManager.get_instance().get( "common.popup.menu.remove.row.menu"),
			new RemoveRowAction( ResourceManager.get_instance().get( "common.popup.menu.remove.row.menu"), this),
			ResourceManager.get_instance().get( "common.popup.menu.remove.row.mnemonic"),
			ResourceManager.get_instance().get( "common.popup.menu.remove.row.stroke"));

		_popupMenu.addSeparator();

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
	}

	/* (non-Javadoc)
	 * @see soars.common.utility.swing.table.spread_sheet.SpreadSheetRowHeaderTable#on_mouse_right_up(int, int, java.awt.event.MouseEvent)
	 */
	protected boolean on_mouse_right_up(int row, int column, MouseEvent mouseEvent) {
		if (!super.on_mouse_right_up(row, column, mouseEvent))
			return false;

//		_appendRowMenuItem.setEnabled( true);
//		_insertRowMenuItem.setEnabled( false);
		_removeRowMenuItem.setEnabled( false);

		_copyRowMenuItem.setEnabled( true);
		_cutRowMenuItem.setEnabled( true);
		_pasteRowMenuItem.setEnabled( false);

		if ( getRowCount() > 0) {
			int[] rows = getSelectedRows();

			Arrays.sort( rows);

//			setup_insertRowMenuItem( rows);
			setup_removeRowMenuItem( rows);
			setup_pasteRowMenuItem( rows);
		}

		_popupMenu.show( this, mouseEvent.getX(), mouseEvent.getY());

		return true;
	}

//	/**
//	 * @param rows
//	 */
//	private void setup_insertRowMenuItem(int[] rows) {
//		_insertRowMenuItem.setEnabled( 0 < rows.length);
//	}

	/**
	 * @param rows
	 */
	private void setup_removeRowMenuItem(int[] rows) {
		_removeRowMenuItem.setEnabled( true/*rows.length < getRowCount()*/);
	}

	/**
	 * @param rows
	 */
	private void setup_pasteRowMenuItem(int[] rows) {
		_pasteRowMenuItem.setEnabled( 1 == rows.length && !GisDataTable._selectedRowData.isEmpty());
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.common.menu.basic3.IBasicMenuHandler3#on_append_row(java.awt.event.ActionEvent)
	 */
	public void on_append_row(ActionEvent actionEvent) {
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.common.menu.basic3.IBasicMenuHandler3#on_insert_row(java.awt.event.ActionEvent)
	 */
	public void on_insert_row(ActionEvent actionEvent) {
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.common.menu.basic3.IBasicMenuHandler3#on_remove_row(java.awt.event.ActionEvent)
	 */
	public void on_remove_row(ActionEvent actionEvent) {
		if ( 0 == getRowCount())
		//if ( 2 > getRowCount())
			return;

		int[] rows = getSelectedRows();
		if ( null == rows || 0 == rows.length /*|| getRowCount() == rows.length*/)
			return;

		Arrays.sort( rows);

		if ( JOptionPane.YES_OPTION == JOptionPane.showConfirmDialog(
			_parent,
			ResourceManager.get_instance().get( "edit.gis.data.dialog.table.confirm.remove.row.message"),
			ResourceManager.get_instance().get( "edit.gis.data.dialog.title"),
			JOptionPane.YES_NO_OPTION)) {
			_spreadSheetTableBase.removeRows( rows);
			GisDataTable.refresh();
		}
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.common.menu.basic3.IBasicMenuHandler3#on_copy_row(java.awt.event.ActionEvent)
	 */
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
		// 選択領域内が全てnullなら実行しない←必要か？
		GisDataTable gisDataTable = ( GisDataTable)_spreadSheetTableBase;
		GisDataTable.refresh();

		int minRow = rows[ 0];

		for ( int row:rows) {
			RowHeaderData rowHeaderData = ( RowHeaderData)getValueAt( row, 0);
			GisDataTable._selectedRowData.add( ( null == rowHeaderData) ? ( new RowData<String>( row - minRow)) : ( new RowData<String>( row - minRow, new RowHeaderData())));
		}

		gisDataTable.on_copy_row( rows);

		return true;
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.common.menu.basic3.IBasicMenuHandler3#on_cut_row(java.awt.event.ActionEvent)
	 */
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
			rowHeaderSetValueUndoList.add( new SetValueUndo( null/*new RowHeaderData()*/, row, 0, this, null));
			rowHeaderSetValueUndoLists.add( rowHeaderSetValueUndoList);
		}

		_spreadSheetTableBase.on_cut_row( rows, rowHeaderSetValueUndoLists);

		return true;
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.common.menu.basic3.IBasicMenuHandler3#on_paste_row(java.awt.event.ActionEvent)
	 */
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
		GisDataTable gisDataTable = ( GisDataTable)_spreadSheetTableBase;
		if ( GisDataTable._selectedRowData.isEmpty())
			return false;

		List<List<SetValueUndo>> rowHeaderSetValueUndoLists = new ArrayList<List<SetValueUndo>>();
		for ( RowData<String> rowData:GisDataTable._selectedRowData) {
			int row = baseRow + rowData._row;

			// はみ出している場合のチェック
			if ( getRowCount() <= row)
				continue;

			List<SetValueUndo> rowHeaderSetValueUndoList = new ArrayList<SetValueUndo>();
			rowHeaderSetValueUndoList.add( new SetValueUndo( rowData._rowHeaderData, row, 0, this, null));
			rowHeaderSetValueUndoLists.add( rowHeaderSetValueUndoList);
		}

		gisDataTable.on_paste_row( baseRow, rowHeaderSetValueUndoLists);

		return true;
	}
}
