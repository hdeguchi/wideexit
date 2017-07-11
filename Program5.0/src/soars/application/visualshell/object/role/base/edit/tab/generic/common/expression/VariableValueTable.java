/*
 * Created on 2005/10/31
 */
package soars.application.visualshell.object.role.base.edit.tab.generic.common.expression;

import java.awt.Component;
import java.awt.Frame;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.util.Map;
import java.util.Vector;

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
import soars.application.visualshell.object.role.base.edit.tab.base.RulePropertyPanelBase;
import soars.application.visualshell.object.role.base.edit.tab.generic.property.Property;
import soars.application.visualshell.object.role.base.edit.tab.generic.property.Subject;
import soars.application.visualshell.object.role.base.object.generic.element.EntityVariableRule;
import soars.application.visualshell.object.role.base.object.generic.element.ExpressionRule;

/**
 * @author kurata
 */
public class VariableValueTable extends TableBase implements IBasicMenuHandler1 {

	/**
	 * 
	 */
	protected Map<String, Vector<Variable>> _variablesMap = null;

	/**
	 * 
	 */
	private Subject _object = null;

	/**
	 * 
	 */
	protected Property _property = null;

	/**
	 * 
	 */
	protected Role _role = null;

	/**
	 * 
	 */
	private RulePropertyPanelBase _rulePropertyPanelBase = null;

	/**
	 * 
	 */
	private JMenuItem _editMenuItem = null;

	/**
	 * @param variablesMap
	 * @param object
	 * @param property
	 * @param role
	 * @param rulePropertyPanelBase
	 * @param owner
	 * @param parent
	 */
	public VariableValueTable(Map<String, Vector<Variable>> variablesMap, Subject object, Property property, Role role, RulePropertyPanelBase rulePropertyPanelBase, Frame owner, Component parent) {
		super(owner, parent);
		_variablesMap = variablesMap;
		_object = object;
		_property = property;
		_role = role;
		_rulePropertyPanelBase = rulePropertyPanelBase;
	}

	/**
	 * @param name
	 */
	public void set(String name) {
		Vector<Variable> variables = _variablesMap.get( name);
		if ( null == variables)
			return;

		DefaultTableModel defaultTableModel = ( DefaultTableModel)getModel();
		defaultTableModel.setRowCount( 0);
		for ( Variable variable:variables)
			defaultTableModel.addRow( new Object[] { variable, variable});
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
		tableHeader.setDefaultRenderer( new VariableValueTableHeaderRenderer( _rulePropertyPanelBase._color));

		DefaultTableModel defaultTableModel = ( DefaultTableModel)getModel();
		defaultTableModel.setColumnCount( 2);

		DefaultTableColumnModel defaultTableColumnModel = ( DefaultTableColumnModel)getColumnModel();
		defaultTableColumnModel.getColumn( 0).setHeaderValue(
			ResourceManager.get_instance().get( "generic.gui.rule.expression.table.header.variable"));
		defaultTableColumnModel.getColumn( 1).setHeaderValue(
			ResourceManager.get_instance().get( "generic.gui.rule.expression.table.header.value"));

		for ( int i = 0; i < 2; ++i) {
			TableColumn tableColumn = defaultTableColumnModel.getColumn( i);
			tableColumn.setCellRenderer( new VariableValueTableCellRenderer( _rulePropertyPanelBase._color, _role));
		}

		return true;
	}

	/* (Non Javadoc)
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

	/* (Non Javadoc)
	 * @see soars.application.visualshell.common.menu.basic1.IBasicMenuHandler1#on_append(java.awt.event.ActionEvent)
	 */
	@Override
	public void on_append(ActionEvent actionEvent) {
	}

	/* (Non Javadoc)
	 * @see soars.application.visualshell.common.menu.basic1.IBasicMenuHandler1#on_insert(java.awt.event.ActionEvent)
	 */
	@Override
	public void on_insert(ActionEvent actionEvent) {
	}

	/* (Non Javadoc)
	 * @see soars.application.visualshell.common.menu.basic1.IBasicMenuHandler1#on_edit(java.awt.event.ActionEvent)
	 */
	@Override
	public void on_edit(ActionEvent actionEvent) {
		int row = getSelectedRow();
		DefaultTableModel defaultTableModel = ( DefaultTableModel)getModel();
		if ( 0 == defaultTableModel.getRowCount() || -1 == row)
			return;

		EditValueDlg editValueDlg = new EditValueDlg(
			_owner,
			ResourceManager.get_instance().get( "generic.gui.rule.expression.dialog.title"),
			true,
			( Variable)defaultTableModel.getValueAt( row, 0),
			_object,
			_property,
			_role,
			_rulePropertyPanelBase);

		if ( !editValueDlg.do_modal( _parent))
			return;

		repaint();
//		defaultTableModel.setValueAt( editValueDlg._value, row, 1);
	}

	/* (Non Javadoc)
	 * @see soars.application.visualshell.common.menu.basic1.IBasicMenuHandler1#on_remove(java.awt.event.ActionEvent)
	 */
	@Override
	public void on_remove(ActionEvent actionEvent) {
	}

	/**
	 * @param expressionRule
	 * @param function
	 * @return
	 */
	public boolean set(ExpressionRule expressionRule, String function) {
		Vector<Variable> variables = _variablesMap.get( function);
		if ( null == variables)
			return false;

		if ( variables.size() != expressionRule._entityVariableRules.size())
			return false;

		DefaultTableModel defaultTableModel = ( DefaultTableModel)getModel();
		defaultTableModel.setRowCount( 0);
		for ( int i = 0; i < variables.size(); ++i) {
			variables.get( i).set( expressionRule._entityVariableRules.get( i));
			defaultTableModel.addRow( new Object[] { variables.get( i), variables.get( i)});
		}
		return true;
	}

	/**
	 * @param expressionRule
	 * @return
	 */
	public boolean get(ExpressionRule expressionRule) {
		DefaultTableModel defaultTableModel = ( DefaultTableModel)getModel();
		for ( int i = 0; i < defaultTableModel.getRowCount(); ++i) {
			Variable variable = ( Variable)defaultTableModel.getValueAt( i, 0);
			if ( null == variable)
				return false;

			expressionRule._entityVariableRules.add( new EntityVariableRule( variable._entityVariableRule));
		}
		return true;
	}
}
