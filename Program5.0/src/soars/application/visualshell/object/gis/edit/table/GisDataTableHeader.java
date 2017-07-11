/**
 * 
 */
package soars.application.visualshell.object.gis.edit.table;

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
import soars.application.visualshell.object.gis.GisData;
import soars.common.utility.swing.table.base.data.ColumnData;
import soars.common.utility.swing.table.spread_sheet.SpreadSheetTable;
import soars.common.utility.swing.table.spread_sheet.SpreadSheetTableHeader;

/**
 * @author kurata
 *
 */
public class GisDataTableHeader extends SpreadSheetTableHeader implements IBasicMenuHandler4 {

	/**
	 * 
	 */
	private GisData _gisData = null;

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
	 * @param gisData 
	 * @param owner
	 * @param parent
	 */
	public GisDataTableHeader(SpreadSheetTable spreadSheetTable, TableColumnModel tableColumnModel, GisData gisData, Frame owner, Component parent) {
		super(spreadSheetTable, tableColumnModel, owner, parent);
		_gisData = gisData;
	}

	/* (non-Javadoc)
	 * @see soars.common.utility.swing.table.base.StandardTableHeader#setup_popup_menu()
	 */
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
	protected boolean on_mouse_right_up(int column, MouseEvent mouseEvent) {
		if (!super.on_mouse_right_up(column, mouseEvent))
			return false;

		_copyColumnMenuItem.setEnabled( true);
		_cutColumnMenuItem.setEnabled( true);
		_pasteColumnMenuItem.setEnabled( false);

		if ( _spreadSheetTable.getColumnCount() > 0) {
			int[] columns = _spreadSheetTable.getSelectedColumns();

			Arrays.sort( columns);

			setup_copyMenuItem( columns);
			setup_cutMenuItem( columns);
			setup_pasteColumnMenuItem( columns);
		}

		_popupMenu.show( this, mouseEvent.getX(), mouseEvent.getY());

		return true;
	}

	/**
	 * @param columns
	 */
	private void setup_copyMenuItem(int[] columns) {
		_copyColumnMenuItem.setEnabled( is_correct_on_copy_or_remove( columns));
	}

	/**
	 * @param columns
	 */
	private void setup_cutMenuItem(int[] columns) {
		// 選択領域内が全てnullでないこと
		_cutColumnMenuItem.setEnabled( !is_empty( columns) && is_correct_on_copy_or_remove( columns));
	}

	/**
	 * @param columns
	 */
	private void setup_pasteColumnMenuItem(int[] columns) {
		_pasteColumnMenuItem.setEnabled( 1 == columns.length && is_correct_on_paste( columns));
	}

	/**
	 * @param rows
	 * @param columns
	 * @return
	 */
	private boolean is_correct_on_copy_or_remove(int[] columns) {
		// 選択領域に座標が含まれていたらfalseを返す
		for ( int column:columns) {
			if ( column >= _spreadSheetTable.getColumnCount() - 2)
				return false;
		}
		return true;
	}

	/**
	 * @param columns
	 * @return
	 */
	private boolean is_correct_on_paste(int[] columns) {
		if ( GisDataTable._selectedColumnData.isEmpty())
			return false;

		if ( GisDataTable._selectedColumnData.get( 0).isEmpty())
			return false;

		int start = GisDataTable._selectedColumnData.get( 0)._column;

		for ( ColumnData columnData:GisDataTable._selectedColumnData) {
			if ( columns[ 0] + columnData._column - start >= _spreadSheetTable.getColumnCount() - 2)
				return false;
		}

		for ( ColumnData columnData:GisDataTable._selectedColumnData) {
			if ( !is_type_equal( _gisData._classes.get( columns[ 0] + columnData._column - start), _gisData._classes.get( columnData._column)))
				return false;
		}

		return true;
	}

	/**
	 * @param class1
	 * @param class2
	 * @return
	 */
	private boolean is_type_equal(Class class1, Class class2) {
		if ( class1.equals( Integer.class) || class1.equals( Long.class) || class1.equals( Short.class) || class1.equals( Byte.class))
			return ( class2.equals( Integer.class) || class2.equals( Long.class) || class2.equals( Short.class) || class2.equals( Byte.class));
		else if ( class1.equals( Double.class) || class1.equals( Float.class))
			return ( class2.equals( Double.class) || class2.equals( Float.class));
		return class1.equals( class2);
	}

	/**
	 * @param columns
	 * @return
	 */
	private boolean is_empty(int[] columns) {
		// 選択領域内が全てnullならtrueを返す←必要か？
		return false;
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.common.menu.basic4.IBasicMenuHandler4#on_append_column(java.awt.event.ActionEvent)
	 */
	public void on_append_column(ActionEvent actionEvent) {
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.common.menu.basic4.IBasicMenuHandler4#on_insert_column(java.awt.event.ActionEvent)
	 */
	public void on_insert_column(ActionEvent actionEvent) {
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.common.menu.basic4.IBasicMenuHandler4#on_remove_column(java.awt.event.ActionEvent)
	 */
	public void on_remove_column(ActionEvent actionEvent) {
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.common.menu.basic4.IBasicMenuHandler4#on_copy_column(java.awt.event.ActionEvent)
	 */
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
		// 選択領域内が全てnullなら実行しない←必要か？

		GisDataTable.refresh();

		//int minColumn = columns[ 0];

		for ( int column:columns)
			GisDataTable._selectedColumnData.add( new ColumnData( column/*column - minColumn*/));

		_spreadSheetTable.on_copy_column( columns);

		return true;
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.common.menu.basic4.IBasicMenuHandler4#on_cut_column(java.awt.event.ActionEvent)
	 */
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
		if ( GisDataTable._selectedColumnData.isEmpty())
			return false;

		_spreadSheetTable.on_paste_column( column);

		return true;
	}
}
