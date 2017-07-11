/**
 * 
 */
package soars.application.visualshell.object.experiment.edit.table;

import java.awt.Component;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;
import java.util.Arrays;

import javax.swing.JMenuItem;
import javax.swing.table.TableColumnModel;

import soars.application.visualshell.common.menu.basic4.CopyColumnAction;
import soars.application.visualshell.common.menu.basic4.CutColumnAction;
import soars.application.visualshell.common.menu.basic4.IBasicMenuHandler4;
import soars.application.visualshell.common.menu.basic4.PasteColumnAction;
import soars.application.visualshell.main.ResourceManager;
import soars.common.utility.swing.table.base.data.ColumnData;
import soars.common.utility.swing.table.spread_sheet.SpreadSheetTable;
import soars.common.utility.swing.table.spread_sheet.SpreadSheetTableHeader;

/**
 * @author kurata
 *
 */
public class ExperimentTableHeader extends SpreadSheetTableHeader implements IBasicMenuHandler4 {

	/**
	 * 
	 */
	private JMenuItem _copyColumnMenuItem = null;

	/**
	 * 
	 */
	private JMenuItem _cutColumnMenuItem = null;

	/**
	 * 
	 */
	private JMenuItem _pasteColumnMenuItem = null;

	/**
	 * @param spreadSheetTable
	 * @param tableColumnModel
	 * @param owner
	 * @param parent
	 */
	public ExperimentTableHeader(SpreadSheetTable spreadSheetTable, TableColumnModel tableColumnModel, Frame owner, Component parent) {
		super(spreadSheetTable, tableColumnModel, owner, parent);
	}

	/* (non-Javadoc)
	 * @see soars.common.utility.swing.table.base.StandardTableHeader#setup_popup_menu()
	 */
	@Override
	protected void setup_popup_menu() {
		super.setup_popup_menu();

		_copyColumnMenuItem = _userInterface.append_popup_menuitem(
			_popupMenu,
			ResourceManager.get_instance().get( "common.popup.menu.copy.column.menu"),
			new CopyColumnAction( ResourceManager.get_instance().get( "common.popup.menu.copy.column.menu"), this),
			ResourceManager.get_instance().get( "common.popup.menu.copy.column.mnemonic"),
			ResourceManager.get_instance().get( "common.popup.menu.copy.column.stroke"));
		_cutColumnMenuItem = _userInterface.append_popup_menuitem(
			_popupMenu,
			ResourceManager.get_instance().get( "common.popup.menu.cut.column.menu"),
			new CutColumnAction( ResourceManager.get_instance().get( "common.popup.menu.cut.column.menu"), this),
			ResourceManager.get_instance().get( "common.popup.menu.cut.column.mnemonic"),
			ResourceManager.get_instance().get( "common.popup.menu.cut.column.stroke"));
		_pasteColumnMenuItem = _userInterface.append_popup_menuitem(
			_popupMenu,
			ResourceManager.get_instance().get( "common.popup.menu.paste.column.menu"),
			new PasteColumnAction( ResourceManager.get_instance().get( "common.popup.menu.paste.column.menu"), this),
			ResourceManager.get_instance().get( "common.popup.menu.paste.column.mnemonic"),
			ResourceManager.get_instance().get( "common.popup.menu.paste.column.stroke"));
	}

	/* (non-Javadoc)
	 * @see soars.common.utility.swing.table.spread_sheet.SpreadSheetTableHeader#on_mouse_right_up(int, java.awt.event.MouseEvent)
	 */
	@Override
	protected boolean on_mouse_right_up(int column, MouseEvent mouseEvent) {
		if ( !super.on_mouse_right_up(column, mouseEvent))
			return false;

		_copyColumnMenuItem.setEnabled( true);
		_cutColumnMenuItem.setEnabled( true);
		_pasteColumnMenuItem.setEnabled( false);

		if ( _spreadSheetTable.getColumnCount() > 0) {
			int[] columns = _spreadSheetTable.getSelectedColumns();

			Arrays.sort( columns);

			_copyColumnMenuItem.setEnabled( 0 > Arrays.binarySearch( columns, 0) && 0 > Arrays.binarySearch( columns, 1) && 0 > Arrays.binarySearch( columns, 2));
			_cutColumnMenuItem.setEnabled( 0 > Arrays.binarySearch( columns, 0) && 0 > Arrays.binarySearch( columns, 1) && 0 > Arrays.binarySearch( columns, 2));
			_pasteColumnMenuItem.setEnabled( 1 == columns.length && is_correct( columns));
		}

		_popupMenu.show( this, mouseEvent.getX(), mouseEvent.getY());

		return true;
	}

	/**
	 * @param columns
	 * @return
	 */
	private boolean is_correct(int[] columns) {
		if ( ExperimentTable._selectedColumnData.isEmpty())
			return false;

		if ( ExperimentTable._selectedColumnData.get( 0).isEmpty())
			return false;

		return ( 0 > Arrays.binarySearch( columns, 0) && 0 > Arrays.binarySearch( columns, 1) && 0 > Arrays.binarySearch( columns, 2));
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.common.menu.basic4.IBasicMenuHandler4#on_append_column(java.awt.event.ActionEvent)
	 */
	@Override
	public void on_append_column(ActionEvent actionEvent) {
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.common.menu.basic4.IBasicMenuHandler4#on_insert_column(java.awt.event.ActionEvent)
	 */
	@Override
	public void on_insert_column(ActionEvent actionEvent) {
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.common.menu.basic4.IBasicMenuHandler4#on_remove_column(java.awt.event.ActionEvent)
	 */
	@Override
	public void on_remove_column(ActionEvent actionEvent) {
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.common.menu.basic4.IBasicMenuHandler4#on_copy_column(java.awt.event.ActionEvent)
	 */
	@Override
	public void on_copy_column(ActionEvent actionEvent) {
		int[] columns = _spreadSheetTable.getSelectedColumns();
		if ( null == columns || 0 == columns.length)
			return;

		Arrays.sort( columns);

		copy_column( columns);
	}

	/**
	 * @param columns
	 * @return
	 */
	private boolean copy_column(int[] columns) {
		ExperimentTable.refresh();

		int minColumn = columns[ 0];

		for ( int column:columns)
			ExperimentTable._selectedColumnData.add( new ColumnData<String>( column - minColumn));

		_spreadSheetTable.on_copy_column( columns);

		return true;
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.common.menu.basic4.IBasicMenuHandler4#on_cut_column(java.awt.event.ActionEvent)
	 */
	@Override
	public void on_cut_column(ActionEvent actionEvent) {
		int[] columns = _spreadSheetTable.getSelectedColumns();
		if ( null == columns || 0 == columns.length)
			return;

		Arrays.sort( columns);

		cut_column( columns);

		repaint();
	}

	/**
	 * @param columns
	 * @return
	 */
	private boolean cut_column(int[] columns) {
		if ( !copy_column( columns))
			return false;

		_spreadSheetTable.on_cut_column( columns);

		return true;
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.common.menu.basic4.IBasicMenuHandler4#on_paste_column(java.awt.event.ActionEvent)
	 */
	@Override
	public void on_paste_column(ActionEvent actionEvent) {
		int[] columns = _spreadSheetTable.getSelectedColumns();
		if ( 0 == _spreadSheetTable.getRowCount() || 1 != columns.length)
			return;

		paste_column( columns[ 0]);
	}

	/**
	 * @param column
	 * @return
	 */
	private boolean paste_column(int column) {
		if ( ExperimentTable._selectedColumnData.isEmpty())
			return false;

		_spreadSheetTable.on_paste_column( column);

		return true;
	}
}
