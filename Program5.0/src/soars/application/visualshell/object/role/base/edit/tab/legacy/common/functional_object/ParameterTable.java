/**
 * 
 */
package soars.application.visualshell.object.role.base.edit.tab.legacy.common.functional_object;

import java.awt.Color;
import java.awt.Component;
import java.awt.Frame;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.util.Map;

import javax.swing.DefaultListSelectionModel;
import javax.swing.JMenuItem;
import javax.swing.table.DefaultTableColumnModel;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableColumn;

import soars.application.visualshell.common.menu.basic1.EditAction;
import soars.application.visualshell.common.menu.basic1.IBasicMenuHandler1;
import soars.application.visualshell.common.swing.TableBase;
import soars.application.visualshell.main.ResourceManager;
import soars.application.visualshell.object.role.base.Role;
import soars.application.visualshell.object.role.spot.SpotRole;

/**
 * @author kurata
 *
 */
public class ParameterTable extends TableBase implements IBasicMenuHandler1 {

	/**
	 * 
	 */
	private Color _color = null;

	/**
	 * 
	 */
	private Role _role = null;

	/**
	 * 
	 */
	private JMenuItem _editMenuItem = null;

	/**
	 * 
	 */
	protected String _spot = "";

	/**
	 * 
	 */
	protected String[] _probabilityNames = null;

	/**
	 * 
	 */
	protected String[] _keywordNames = null;

	/**
	 * 
	 */
	protected String[] _numberObjectNames = null;

	/**
	 * 
	 */
	protected String[] _integerNumberObjectNames = null;

	/**
	 * 
	 */
	protected String[] _realNumberObjectNames = null;

	/**
	 * 
	 */
	protected String[] _collectionNames = null;

	/**
	 * 
	 */
	protected String[] _listNames = null;

	/**
	 * 
	 */
	protected String[] _mapNames = null;

	/**
	 * 
	 */
	protected String[] _classVariableNames = null;

	/**
	 * 
	 */
	protected String[] _fileVariableNames = null;

	/**
	 * @param color
	 * @param role
	 * @param owner
	 * @param parent
	 */
	public ParameterTable(Color color, Role role, Frame owner, Component parent) {
		super(owner, parent);
		_color = color;
		_role = role;
	}

	/**
	 * @param spot
	 * @param probabilityNames
	 * @param keywordNames
	 * @param numberObjectNames
	 * @param integerNumberObjectNames
	 * @param realNumberObjectNames
	 * @param collectionNames
	 * @param listNames
	 * @param mapNames
	 * @param classVariableNames
	 */
	public void update(String spot, String[] probabilityNames, String[] keywordNames, String[] numberObjectNames,
		String[] integerNumberObjectNames, String[] realNumberObjectNames, String[] collectionNames,
		String[] listNames, String[] mapNames, String[] classVariableNames, String[] fileVariableNames) {
		_spot = spot;
		// These can be null.
		_probabilityNames = probabilityNames;
		_keywordNames = keywordNames;
		_numberObjectNames = numberObjectNames;
		_integerNumberObjectNames = integerNumberObjectNames;
		_realNumberObjectNames = realNumberObjectNames;
		_collectionNames = collectionNames;
		_listNames = listNames;
		_mapNames = mapNames;
		_classVariableNames = classVariableNames;
		_fileVariableNames = fileVariableNames;
	}

	/**
	 * @return
	 */
	public String get() {
		// TODO Auto-generated method stub
		String parameters = "";
		DefaultTableModel defaultTableModel = ( DefaultTableModel)getModel();
		for ( int i = 0; i < defaultTableModel.getRowCount(); ++i) {
			String type = ( String)defaultTableModel.getValueAt( i, 0);
			String value = ( String)defaultTableModel.getValueAt( i, 1);
			if ( _role instanceof SpotRole && type.equals( "env.Agent") && value.equals( ""))
				return null;

			parameters += ( " " + value + "=" + type);
		}

		return parameters;
	}

	/**
	 * @param parameters
	 * @return
	 */
	public boolean set(String[][] parameters) {
		DefaultTableModel defaultTableModel = ( DefaultTableModel)getModel();
		if ( parameters.length != defaultTableModel.getRowCount())
			return false;

		for ( int i = 0; i < parameters.length; ++i) {
			if ( !parameters[ i][ 1].equals( ( String)defaultTableModel.getValueAt( i, 0)))
				return false;

			defaultTableModel.setValueAt( parameters[ i][ 0], i, 1);
		}

		return true;
	}

	/**
	 * @param valueMap
	 * @param methodName
	 * @param returnValue
	 */
	public void update_value_map(Map<String, String[]> valueMap, String methodName, String returnValue) {
		DefaultTableModel defaultTableModel = ( DefaultTableModel)getModel();
		String[] values = new String[ getRowCount() + 1];
		for ( int i = 0; i < getRowCount(); ++i)
			values[ i] = ( String)defaultTableModel.getValueAt( i, 1);

		values[ values.length - 1] = returnValue;

		valueMap.put( methodName, values);
	}

	/**
	 * @return
	 */
	public boolean setup() {
		if ( !super.setup(true))
			return false;

		getTableHeader().setReorderingAllowed( false);
		setDefaultEditor( Object.class, null);
		setSelectionMode( DefaultListSelectionModel.SINGLE_SELECTION);

		JTableHeader tableHeader = getTableHeader();
		tableHeader.setDefaultRenderer( new ParameterTableHeaderRenderer( _color));

		DefaultTableModel defaultTableModel = ( DefaultTableModel)getModel();
		defaultTableModel.setColumnCount( 2);

		DefaultTableColumnModel defaultTableColumnModel = ( DefaultTableColumnModel)getColumnModel();
		defaultTableColumnModel.getColumn( 0).setHeaderValue(
			ResourceManager.get_instance().get( "edit.rule.dialog.common.functional.object.parameter.table.header.parameter"));
		defaultTableColumnModel.getColumn( 1).setHeaderValue(
			ResourceManager.get_instance().get( "edit.rule.dialog.common.functional.object.parameter.table.header.value"));

		for ( int i = 0; i < 2; ++i) {
			TableColumn tableColumn = defaultTableColumnModel.getColumn( i);
			tableColumn.setCellRenderer( new ParameterTableCellRenderer( _color));
		}

		setToolTipText( "ParameterTable");

		return true;
	}

	/* (non-Javadoc)
	 * @see soars.common.utility.swing.table.StandardTable#setup_popup_menu()
	 */
	@Override
	protected void setup_popup_menu() {
		super.setup_popup_menu();

		_editMenuItem = _userInterface.append_popup_menuitem(
			_popupMenu,
			ResourceManager.get_instance().get( "common.popup.menu.edit.menu"),
			new EditAction( ResourceManager.get_instance().get( "common.popup.menu.edit.menu"), this),
			ResourceManager.get_instance().get( "common.popup.menu.edit.mnemonic"),
			ResourceManager.get_instance().get( "common.popup.menu.edit.stroke"));
	}

	/* (Non Javadoc)
	 * @see soars.common.utility.swing.table.StandardTable#on_mouse_left_double_click(java.awt.event.MouseEvent)
	 */
	@Override
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
	@Override
	public void on_mouse_right_up(Point point) {
		if ( !isEnabled())
			return;

		_editMenuItem.setEnabled( true);

		//int index = getSelectedRow();
		DefaultTableModel defaultTableModel = ( DefaultTableModel)getModel();
		if ( 0 == defaultTableModel.getRowCount() /*|| -1 == index*/) {
			_editMenuItem.setEnabled( false);
		} else {
			int row = rowAtPoint( point);
			int column = columnAtPoint( point);
			if ( ( 0 <= row && getRowCount() > row)
				&& ( 0 <= column && getColumnCount() > column)) {
				setRowSelectionInterval( row, row);
				setColumnSelectionInterval( column, column);
			} else {
				_editMenuItem.setEnabled( false);
			}

			//if ( !isRowSelected( row) || !isColumnSelected( column)) {
			//	_edit_menuItem.setEnabled( false);
			//}
		}

		_popupMenu.show( this, point.x, point.y);
	}

	/* (Non Javadoc)
	 * @see soars.common.utility.swing.table.StandardTable#on_key_pressed(java.awt.event.KeyEvent)
	 */
	@Override
	protected void on_key_pressed(KeyEvent keyEvent) {
//		int row = getSelectedRow();
//		DefaultTableModel defaultTableModel = ( DefaultTableModel)getModel();
//		if ( 0 == defaultTableModel.getRowCount() || -1 == row)
//			return;
//
//		switch ( keyEvent.getKeyCode()) {
//			case KeyEvent.VK_DELETE:
//			case KeyEvent.VK_BACK_SPACE:
//				on_remove( null);
//				break;
//		}
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.common.swing.TableBase#on_enter(java.awt.event.ActionEvent)
	 */
	@Override
	protected void on_enter(ActionEvent actionEvent) {
		on_edit( actionEvent);
	}

	/* (non-Javadoc)
	 * @see javax.swing.JTable#getToolTipText(java.awt.event.MouseEvent)
	 */
	@Override
	public String getToolTipText(MouseEvent mouseEvent) {
		int row = rowAtPoint( mouseEvent.getPoint());
		if ( 0 > row || getRowCount() <= row)
			return null;

		int column = columnAtPoint( mouseEvent.getPoint());
		if ( 0 > column || getColumnCount() <= column)
			return null;

		if ( 0 == convertColumnIndexToModel( column)) {
			DefaultTableModel defaultTableModel = ( DefaultTableModel)getModel();
			String parameterType = ( String)defaultTableModel.getValueAt( row, column);
			if ( null == parameterType)
				return null;

			return parameterType;
		}

		return null;
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.common.menu.basic1.IBasicMenuHandler1#on_append(java.awt.event.ActionEvent)
	 */
	@Override
	public void on_append(ActionEvent actionEvent) {
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.common.menu.basic1.IBasicMenuHandler1#on_edit(java.awt.event.ActionEvent)
	 */
	@Override
	public void on_edit(ActionEvent actionEvent) {
		int row = getSelectedRow();
		DefaultTableModel defaultTableModel = ( DefaultTableModel)getModel();
		if ( 0 == defaultTableModel.getRowCount() || -1 == row)
			return;

		EditParameterDlg editParameterDlg = new EditParameterDlg(
			_owner,
			ResourceManager.get_instance().get( "edit.rule.dialog.common.functional.object.parameter.dialog.title"),
			true,
			_color,
			( String)defaultTableModel.getValueAt( row, 0),
			( String)defaultTableModel.getValueAt( row, 1),
			_spot,
			_probabilityNames,
			_keywordNames,
			_numberObjectNames,
			_integerNumberObjectNames,
			_realNumberObjectNames,
			_collectionNames,
			_listNames,
			_mapNames,
			_classVariableNames,
			_fileVariableNames,
			_role);

		if ( !editParameterDlg.do_modal( _parent))
			return;

		defaultTableModel.setValueAt( editParameterDlg._value, row, 1);
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.common.menu.basic1.IBasicMenuHandler1#on_insert(java.awt.event.ActionEvent)
	 */
	@Override
	public void on_insert(ActionEvent actionEvent) {
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.common.menu.basic1.IBasicMenuHandler1#on_remove(java.awt.event.ActionEvent)
	 */
	@Override
	public void on_remove(ActionEvent actionEvent) {
	}
}
