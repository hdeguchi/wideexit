/**
 * 
 */
package soars.application.visualshell.object.role.base.edit.table;

import java.awt.Component;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;
import java.util.Arrays;
import java.util.List;

import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.table.TableColumnModel;

import soars.application.visualshell.common.menu.basic4.CopyColumnAction;
import soars.application.visualshell.common.menu.basic4.CutColumnAction;
import soars.application.visualshell.common.menu.basic4.IBasicMenuHandler4;
import soars.application.visualshell.common.menu.basic4.InsertColumnAction;
import soars.application.visualshell.common.menu.basic4.PasteColumnAction;
import soars.application.visualshell.common.menu.basic4.RemoveColumnAction;
import soars.application.visualshell.main.ResourceManager;
import soars.application.visualshell.object.role.base.Role;
import soars.application.visualshell.object.role.base.object.RuleManager;
import soars.application.visualshell.object.role.base.object.base.Rule;
import soars.common.utility.swing.table.base.data.ColumnData;
import soars.common.utility.swing.table.spread_sheet.SpreadSheetTableHeader;

/**
 * @author kurata
 *
 */
public class RuleTableHeader extends SpreadSheetTableHeader implements IBasicMenuHandler4 {

	/**
	 * 
	 */
	private Role _role = null;

//	/**
//	 * 
//	 */
//	private JMenuItem _appendColumnMenuItem = null;

	/**
	 * 
	 */
	private JMenuItem _insertColumnMenuItem = null;

	/**
	 * 
	 */
	private JMenuItem _removeColumnMenuItem = null;

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
	 * @param role
	 * @param ruleTable
	 * @param tableColumnModel
	 * @param owner
	 * @param parent
	 */
	public RuleTableHeader(Role role, RuleTable ruleTable, TableColumnModel tableColumnModel, Frame owner, Component parent) {
		super(ruleTable, tableColumnModel, owner, parent);
		_role = role;
	}

	/* (non-Javadoc)
	 * @see soars.common.utility.swing.table.base.StandardTableHeader#setup_popup_menu()
	 */
	@Override
	protected void setup_popup_menu() {
		super.setup_popup_menu();

//		_appendColumnMenuItem = _userInterface.append_popup_menuitem(
//			_popupMenu,
//			ResourceManager.get_instance().get( "common.popup.menu.append.column.menu"),
//			new AppendColumnAction( ResourceManager.get_instance().get( "common.popup.menu.append.column.menu"), this),
//			ResourceManager.get_instance().get( "common.popup.menu.append.column.mnemonic"),
//			ResourceManager.get_instance().get( "common.popup.menu.append.column.stroke"));
		_insertColumnMenuItem = _userInterface.append_popup_menuitem(
			_popupMenu,
			ResourceManager.get_instance().get( "common.popup.menu.insert.column.menu"),
			new InsertColumnAction( ResourceManager.get_instance().get( "common.popup.menu.insert.column.menu"), this),
			ResourceManager.get_instance().get( "common.popup.menu.insert.column.mnemonic"),
			ResourceManager.get_instance().get( "common.popup.menu.insert.column.stroke"));
		_removeColumnMenuItem = _userInterface.append_popup_menuitem(
			_popupMenu,
			ResourceManager.get_instance().get( "common.popup.menu.remove.column.menu"),
			new RemoveColumnAction( ResourceManager.get_instance().get( "common.popup.menu.remove.column.menu"), this),
			ResourceManager.get_instance().get( "common.popup.menu.remove.column.mnemonic"),
			ResourceManager.get_instance().get( "common.popup.menu.remove.column.stroke"));

		_popupMenu.addSeparator();

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
		if (!super.on_mouse_right_up(column, mouseEvent))
			return false;

//		_appendColumnMenuItem.setEnabled( true);
		_insertColumnMenuItem.setEnabled( false);
		_removeColumnMenuItem.setEnabled( false);

		_copyColumnMenuItem.setEnabled( true);
		_cutColumnMenuItem.setEnabled( true);
		_pasteColumnMenuItem.setEnabled( false);

		if ( _spreadSheetTable.getColumnCount() > 0) {
			int[] columns = _spreadSheetTable.getSelectedColumns();

			Arrays.sort( columns);

			setup_insertColumnMenuItem( columns);
			setup_removeColumnMenuItem( columns);
			setup_pasteColumnMenuItem( columns);
		}

		_popupMenu.show( this, mouseEvent.getX(), mouseEvent.getY());

		return true;
	}

	/**
	 * @param columns
	 */
	private void setup_insertColumnMenuItem(int[] columns) {
		_insertColumnMenuItem.setEnabled( 0 < columns.length && 0 != columns[ 0]);
	}

	/**
	 * @param columns
	 */
	private void setup_removeColumnMenuItem(int[] columns) {
		_removeColumnMenuItem.setEnabled( columns.length < _spreadSheetTable.getColumnCount() && 0 != columns[ 0]);
	}

	/**
	 * @param columns
	 */
	private void setup_pasteColumnMenuItem(int[] columns) {
		_pasteColumnMenuItem.setEnabled( 1 == columns.length && is_correct( columns));
	}

	/**
	 * @param columns
	 * @return
	 */
	private boolean is_correct(int[] columns) {
		List<ColumnData<Rule>> selectedColumnRules = RuleTable.get_selectedColumnRules( _role);
		if ( selectedColumnRules.isEmpty())
			return false;

		if ( selectedColumnRules.get( 0).isEmpty())
			return false;

		Rule rule = selectedColumnRules.get( 0).get( 0);
		if ( null == rule)
			return ( 0 != columns[ 0]);

		return ( ( 0 == columns[ 0] && rule._kind.equals( "condition") && rule._type.equals( ResourceManager.get_instance().get( "rule.type.condition.stage")))
			|| ( 0 != columns[ 0] && !( rule._kind.equals( "condition") && rule._type.equals( ResourceManager.get_instance().get( "rule.type.condition.stage")))));
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.common.menu.basic4.IBasicMenuHandler4#on_append_column(java.awt.event.ActionEvent)
	 */
	@Override
	public void on_append_column(ActionEvent actionEvent) {
//		_ruleTable.on_append_column( actionEvent);
//		RuleTable.refresh();
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.common.menu.basic4.IBasicMenuHandler4#on_insert_column(java.awt.event.ActionEvent)
	 */
	@Override
	public void on_insert_column(ActionEvent actionEvent) {
		int[] columns = _spreadSheetTable.getSelectedColumns();
		if ( null == columns || 0 == columns.length)
			return;

		Arrays.sort( columns);

		_spreadSheetTable.insertColumns( columns, RuleManager._defaultRuleTableColumnWidth);

		RuleTable.refresh();
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.common.menu.basic4.IBasicMenuHandler4#on_remove_column(java.awt.event.ActionEvent)
	 */
	@Override
	public void on_remove_column(ActionEvent actionEvent) {
		int[] columns = _spreadSheetTable.getSelectedColumns();
		if ( null == columns || 0 == columns.length)
			return;

		Arrays.sort( columns);

		if ( 0 == columns[ 0])
			return;

		if ( JOptionPane.YES_OPTION == JOptionPane.showConfirmDialog(
			_parent,
			ResourceManager.get_instance().get( "edit.role.dialog.rule.table.confirm.remove.column.message"),
			ResourceManager.get_instance().get( "application.title"),
			JOptionPane.YES_NO_OPTION)) {
			_spreadSheetTable.on_remove_column( columns);
			RuleTable.refresh();
		}
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
		// TODO 選択領域内が全てnullなら実行しない←必要か？

		RuleTable.refresh();
		List<ColumnData<Rule>> selectedColumnRules = RuleTable.get_selectedColumnRules( _role);

		int minColumn = columns[ 0];

		for ( int column:columns)
			selectedColumnRules.add( new ColumnData<Rule>( column - minColumn));

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
		List<ColumnData<Rule>> selectedColumnRules = RuleTable.get_selectedColumnRules( _role);
		if ( selectedColumnRules.isEmpty())
			return false;

		_spreadSheetTable.on_paste_column( column);

		return true;
	}
}
