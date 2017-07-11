/*
 * Created on 2006/03/13
 */
package soars.application.visualshell.object.chart.edit;

import java.awt.Component;
import java.awt.Frame;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.util.Arrays;
import java.util.Vector;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.KeyStroke;
import javax.swing.table.DefaultTableColumnModel;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableColumn;

import soars.application.visualshell.common.menu.basic1.AppendAction;
import soars.application.visualshell.common.menu.basic1.EditAction;
import soars.application.visualshell.common.menu.basic1.IBasicMenuHandler1;
import soars.application.visualshell.common.menu.basic1.InsertAction;
import soars.application.visualshell.common.menu.basic1.RemoveAction;
import soars.application.visualshell.common.swing.TableBase;
import soars.application.visualshell.main.ResourceManager;
import soars.application.visualshell.object.chart.NumberObjectData;
import soars.common.utility.swing.table.base.StandardTableHeaderRenderer;
import soars.common.utility.tool.common.Tool;

/**
 * The table component for the numeric data of the chart object
 * @author kurata / SOARS project
 */
public class NumberObjectDataPairTable extends TableBase implements IBasicMenuHandler1 {

	/**
	 * 
	 */
	protected JMenuItem _appendMenuItem = null;

	/**
	 * 
	 */
	protected JMenuItem _insertMenuItem = null;

	/**
	 * 
	 */
	protected JMenuItem _editMenuItem = null;

	/**
	 * 
	 */
	protected JMenuItem _removeMenuItem = null;

	/**
	 * Creates this object with the specified data.
	 * @param owner the frame of the parent container
	 * @param parent the parent container of this component
	 */
	public NumberObjectDataPairTable(Frame owner, Component parent) {
		super(owner, parent);
	}

	/**
	 * Returns true if this object is initialized with specified data successfully.
	 * @param numberObjectDataPairs the array for the pairs of the NumberObjectData objects
	 * @return true if this object is initialized with specified data successfully
	 */
	public boolean setup(Vector<NumberObjectData[]> numberObjectDataPairs) {
		if ( !super.setup( true))
			return false;

		getTableHeader().setReorderingAllowed( false);
		setDefaultEditor( Object.class, null);

		JTableHeader tableHeader = getTableHeader();
		tableHeader.setDefaultRenderer( new StandardTableHeaderRenderer());

		DefaultTableModel defaultTableModel = ( DefaultTableModel)getModel();
		defaultTableModel.setColumnCount( 2);

		DefaultTableColumnModel defaultTableColumnModel = ( DefaultTableColumnModel)getColumnModel();
		defaultTableColumnModel.getColumn( 0).setHeaderValue(
			ResourceManager.get_instance().get( "edit.chart.dialog.number.object.pairs.table.header.horizontal"));
		defaultTableColumnModel.getColumn( 1).setHeaderValue(
			ResourceManager.get_instance().get( "edit.chart.dialog.number.object.pairs.table.header.vertical"));

		for ( int i = 0; i < 2; ++i) {
			TableColumn tableColumn = defaultTableColumnModel.getColumn( i);
			tableColumn.setCellRenderer( new NumberObjectDataPairTableRowRenderer());
		}

		for ( int i = 0; i < numberObjectDataPairs.size(); ++i) {
			NumberObjectData[] numberObjectDataPair = ( NumberObjectData[])numberObjectDataPairs.get( i);
			if ( 2 != numberObjectDataPair.length)
				return false;

			defaultTableModel.addRow( new NumberObjectData[] {
				new NumberObjectData( numberObjectDataPair[ 0]),
				new NumberObjectData( numberObjectDataPair[ 1])});
		}

		return true;
	}

	/* (Non Javadoc)
	 * @see soars.common.utility.swing.table.StandardTable#setup_popup_menu()
	 */
	protected void setup_popup_menu() {
		super.setup_popup_menu();

		_appendMenuItem = _userInterface.append_popup_menuitem(
			_popupMenu,
			ResourceManager.get_instance().get( "common.popup.menu.append.menu"),
			new AppendAction( ResourceManager.get_instance().get( "common.popup.menu.append.menu"), this),
			ResourceManager.get_instance().get( "common.popup.menu.append.mnemonic"),
			ResourceManager.get_instance().get( "common.popup.menu.append.stroke"));
		_insertMenuItem = _userInterface.append_popup_menuitem(
			_popupMenu,
			ResourceManager.get_instance().get( "common.popup.menu.insert.menu"),
			new InsertAction( ResourceManager.get_instance().get( "common.popup.menu.insert.menu"), this),
			ResourceManager.get_instance().get( "common.popup.menu.insert.mnemonic"),
			ResourceManager.get_instance().get( "common.popup.menu.insert.stroke"));
		_editMenuItem = _userInterface.append_popup_menuitem(
			_popupMenu,
			ResourceManager.get_instance().get( "common.popup.menu.edit.menu"),
			new EditAction( ResourceManager.get_instance().get( "common.popup.menu.edit.menu"), this),
			ResourceManager.get_instance().get( "common.popup.menu.edit.mnemonic"),
			ResourceManager.get_instance().get( "common.popup.menu.edit.stroke"));
		_removeMenuItem = _userInterface.append_popup_menuitem(
			_popupMenu,
			ResourceManager.get_instance().get( "common.popup.menu.remove.menu"),
			new RemoveAction( ResourceManager.get_instance().get( "common.popup.menu.remove.menu"), this),
			ResourceManager.get_instance().get( "common.popup.menu.remove.mnemonic"),
			ResourceManager.get_instance().get( "common.popup.menu.remove.stroke"));
	}

	/* (Non Javadoc)
	 * @see soars.common.utility.swing.table.StandardTable#on_mouse_left_double_click(java.awt.event.MouseEvent)
	 */
	protected void on_mouse_left_double_click(MouseEvent mouseEvent) {
		int row = rowAtPoint( mouseEvent.getPoint());
		if ( 0 > row || getRowCount() <= row)
			return;

		int column = columnAtPoint( mouseEvent.getPoint());
		if ( 0 > column || getColumnCount() <= column)
			return;

		on_edit( null);
	}

	/* (Non Javadoc)
	 * @see soars.application.visualshell.common.swing.TableBase#on_mouse_right_up(java.awt.Point)
	 */
	public void on_mouse_right_up(Point point) {
		if ( null == _userInterface)
			return;

		_insertMenuItem.setEnabled( true);
		_editMenuItem.setEnabled( true);
		_removeMenuItem.setEnabled( true);

		//int index = getSelectedRow();
		DefaultTableModel defaultTableModel = ( DefaultTableModel)getModel();
		if ( 0 == defaultTableModel.getRowCount() /*|| -1 == index*/) {
			_insertMenuItem.setEnabled( false);
			_editMenuItem.setEnabled( false);
			_removeMenuItem.setEnabled( false);
		} else {
			int row = rowAtPoint( point);
			int column = columnAtPoint( point);
			if ( ( 0 <= row && getRowCount() > row)
				&& ( 0 <= column && getColumnCount() > column)) {
				//setRowSelectionInterval( row, row);
				//setColumnSelectionInterval( column, column);
				int[] rows = getSelectedRows();
				boolean contains = ( 0 <= Arrays.binarySearch( rows, row));
				_insertMenuItem.setEnabled( 1 == rows.length && contains);
				_editMenuItem.setEnabled( 1 == rows.length && contains);
				_removeMenuItem.setEnabled( contains);
			} else {
				_insertMenuItem.setEnabled( false);
				_editMenuItem.setEnabled( false);
				_removeMenuItem.setEnabled( false);
			}
		}

		_popupMenu.show( this, point.x, point.y);
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.common.swing.TableBase#setup_key_event()
	 */
	protected void setup_key_event() {
		super.setup_key_event();

		Action deleteAction = new AbstractAction() {
			public void actionPerformed(ActionEvent e) {
				on_remove( null);
			}
		};
		getInputMap().put( KeyStroke.getKeyStroke( KeyEvent.VK_DELETE, 0), "delete");
		getActionMap().put( "delete", deleteAction);

		Action backSpaceAction = new AbstractAction() {
			public void actionPerformed(ActionEvent e) {
				on_remove( null);
			}
		};
		getInputMap().put( KeyStroke.getKeyStroke( KeyEvent.VK_BACK_SPACE, 0), "backspace");
		getActionMap().put( "backspace", backSpaceAction);
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.common.swing.TableBase#on_enter(java.awt.event.ActionEvent)
	 */
	protected void on_enter(ActionEvent actionEvent) {
		on_edit( actionEvent);
	}

	/* (Non Javadoc)
	 * @see soars.application.visualshell.common.menu.basic1.IBasicMenuHandler1#on_append(java.awt.event.ActionEvent)
	 */
	public void on_append(ActionEvent actionEvent) {
		EditNumberObjectDataPairDlg editNumberObjectDataPairDlg = new EditNumberObjectDataPairDlg(
			_owner,
			ResourceManager.get_instance().get( "append.number.object.pair.dialog.title"),
			true,
			this);

		if ( !editNumberObjectDataPairDlg.do_modal( _parent))
			return;

		DefaultTableModel defaultTableModel = ( DefaultTableModel)getModel();
		defaultTableModel.addRow( editNumberObjectDataPairDlg._numberObjectDataPair);
	}

	/* (Non Javadoc)
	 * @see soars.application.visualshell.common.menu.basic1.IBasicMenuHandler1#on_insert(java.awt.event.ActionEvent)
	 */
	public void on_insert(ActionEvent actionEvent) {
		int[] rows = getSelectedRows();
		if ( 1 != rows.length)
			return;

		DefaultTableModel defaultTableModel = ( DefaultTableModel)getModel();
		if ( 0 == defaultTableModel.getRowCount() || -1 == rows[ 0])
			return;

		EditNumberObjectDataPairDlg editNumberObjectDataPairDlg = new EditNumberObjectDataPairDlg(
			_owner,
			ResourceManager.get_instance().get( "append.number.object.pair.dialog.title"),
			true,
			this);

		if ( !editNumberObjectDataPairDlg.do_modal( _parent))
			return;

		defaultTableModel.insertRow( rows[ 0], editNumberObjectDataPairDlg._numberObjectDataPair);
		setRowSelectionInterval( rows[ 0], rows[ 0]);
	}

	/* (Non Javadoc)
	 * @see soars.application.visualshell.common.menu.basic1.IBasicMenuHandler1#on_edit(java.awt.event.ActionEvent)
	 */
	public void on_edit(ActionEvent actionEvent) {
		int[] rows = getSelectedRows();
		if ( 1 != rows.length)
			return;

		DefaultTableModel defaultTableModel = ( DefaultTableModel)getModel();
		if ( 0 == defaultTableModel.getRowCount() || -1 == rows[ 0])
			return;

		EditNumberObjectDataPairDlg editNumberObjectDataPairDlg = new EditNumberObjectDataPairDlg(
			_owner,
			ResourceManager.get_instance().get( "edit.number.object.pair.dialog.title"),
			true,
			( NumberObjectData)defaultTableModel.getValueAt( rows[ 0], 0),
			( NumberObjectData)defaultTableModel.getValueAt( rows[ 0], 1),
			this);

		if ( !editNumberObjectDataPairDlg.do_modal( _parent))
			return;

		defaultTableModel.setValueAt( editNumberObjectDataPairDlg._numberObjectDataPair[ 0], rows[ 0], 0);
		defaultTableModel.setValueAt( editNumberObjectDataPairDlg._numberObjectDataPair[ 1], rows[ 0], 1);
	}

	/* (Non Javadoc)
	 * @see soars.application.visualshell.common.menu.basic1.IBasicMenuHandler1#on_remove(java.awt.event.ActionEvent)
	 */
	public void on_remove(ActionEvent actionEvent) {
		int[] rows = getSelectedRows();
		if ( 1 > rows.length)
			return;

		DefaultTableModel defaultTableModel = ( DefaultTableModel)getModel();
		if ( 0 == defaultTableModel.getRowCount())
			return;

		if ( JOptionPane.YES_OPTION == JOptionPane.showConfirmDialog(
			_parent,
			ResourceManager.get_instance().get( "edit.chart.dialog.number.object.pairs.table.confirm.remove.message"),
			ResourceManager.get_instance().get( "application.title"),
			JOptionPane.YES_NO_OPTION)) {

			rows = Tool.quick_sort_int( rows, true);
			for ( int i = rows.length - 1; i >= 0; --i)
				defaultTableModel.removeRow( rows[ i]);

			if ( 0 < defaultTableModel.getRowCount()) {
				if ( rows[ 0] < defaultTableModel.getRowCount())
					setRowSelectionInterval( rows[ 0], rows[ 0]);
				else
					setRowSelectionInterval( defaultTableModel.getRowCount() - 1, defaultTableModel.getRowCount() - 1);
			}
		}
	}

	/**
	 * @param numberObjectPairs
	 */
	protected void get(Vector<NumberObjectData[]> numberObjectPairs) {
		numberObjectPairs.clear();
		DefaultTableModel defaultTableModel = ( DefaultTableModel)getModel();
		for ( int i = 0; i < getRowCount(); ++i)
			numberObjectPairs.add( new NumberObjectData[] {
				( NumberObjectData)defaultTableModel.getValueAt( i, 0),
				( NumberObjectData)defaultTableModel.getValueAt( i, 1)});
	}
}
