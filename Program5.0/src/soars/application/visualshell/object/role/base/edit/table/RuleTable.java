/**
 * 
 */
package soars.application.visualshell.object.role.base.edit.table;

import java.awt.Color;
import java.awt.Component;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Vector;

import javax.swing.DefaultCellEditor;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.swing.event.ListSelectionEvent;
import javax.swing.table.DefaultTableColumnModel;
import javax.swing.table.TableColumn;
import javax.swing.text.AbstractDocument.DefaultDocumentEvent;
import javax.swing.undo.UndoManager;

import soars.application.visualshell.common.menu.basic1.IBasicMenuHandler1;
import soars.application.visualshell.common.menu.basic1.RemoveAction;
import soars.application.visualshell.common.menu.basic2.CopyAction;
import soars.application.visualshell.common.menu.basic2.CutAction;
import soars.application.visualshell.common.menu.basic2.IBasicMenuHandler2;
import soars.application.visualshell.common.menu.basic2.PasteAction;
import soars.application.visualshell.common.menu.basic2.RedoAction;
import soars.application.visualshell.common.menu.basic2.UndoAction;
import soars.application.visualshell.common.menu.basic5.IBasicMenuHandler5;
import soars.application.visualshell.common.menu.basic5.InsertDownwardShiftAction;
import soars.application.visualshell.common.menu.basic5.InsertRightShiftAction;
import soars.application.visualshell.common.menu.basic5.OrAction;
import soars.application.visualshell.common.menu.basic5.RemoveLeftShiftAction;
import soars.application.visualshell.common.menu.basic5.RemoveUpwardShiftAction;
import soars.application.visualshell.common.tool.CommonTool;
import soars.application.visualshell.main.ResourceManager;
import soars.application.visualshell.object.role.agent.AgentRole;
import soars.application.visualshell.object.role.base.Role;
import soars.application.visualshell.object.role.base.edit.tab.CommandTabbedPane;
import soars.application.visualshell.object.role.base.edit.tab.ConditionTabbedPane;
import soars.application.visualshell.object.role.base.edit.tab.RuleTabbedPane;
import soars.application.visualshell.object.role.base.edit.tab.base.RulePropertyPanelBase;
import soars.application.visualshell.object.role.base.edit.tab.legacy.base.EditRuleValuePropertyPanel;
import soars.application.visualshell.object.role.base.edit.tab.legacy.command.OthersCommandPropertyPanel;
import soars.application.visualshell.object.role.base.edit.tab.legacy.command.TimeCommandPropertyPanel;
import soars.application.visualshell.object.role.base.edit.table.data.CommonRuleData;
import soars.application.visualshell.object.role.base.edit.table.data.RuleRowData;
import soars.application.visualshell.object.role.base.edit.table.undo_redo.cell.CustomSetValuesUndo;
import soars.application.visualshell.object.role.base.edit.table.undo_redo.column.CustomColumnsUndo;
import soars.application.visualshell.object.role.base.edit.table.undo_redo.column.CustomRemoveColumnsBlockUndo;
import soars.application.visualshell.object.role.base.edit.table.undo_redo.column.CustomRemoveColumnsUndo;
import soars.application.visualshell.object.role.base.edit.table.undo_redo.row.CustomInsertRowsBlockUndo;
import soars.application.visualshell.object.role.base.edit.table.undo_redo.row.CustomRemoveRowsBlockUndo;
import soars.application.visualshell.object.role.base.edit.tree.RuleTree;
import soars.application.visualshell.object.role.base.object.RuleManager;
import soars.application.visualshell.object.role.base.object.Rules;
import soars.application.visualshell.object.role.base.object.base.Rule;
import soars.application.visualshell.object.role.base.object.generic.GenericRule;
import soars.application.visualshell.object.stage.StageManager;
import soars.common.utility.swing.combo.ComboBox;
import soars.common.utility.swing.combo.CommonComboBoxRenderer;
import soars.common.utility.swing.table.base.data.BlockData;
import soars.common.utility.swing.table.base.data.ColumnData;
import soars.common.utility.swing.table.base.data.RowData;
import soars.common.utility.swing.table.base.undo_redo.cell.SetValueUndo;
import soars.common.utility.swing.table.base.undo_redo.cell.SetValuesUndo;
import soars.common.utility.swing.table.base.undo_redo.column.ColumnsUndo;
import soars.common.utility.swing.table.base.undo_redo.column.InsertColumnsBlockUndo;
import soars.common.utility.swing.table.base.undo_redo.column.RemoveColumnsBlockUndo;
import soars.common.utility.swing.table.base.undo_redo.column.RemoveColumnsUndo;
import soars.common.utility.swing.table.base.undo_redo.row.InsertRowsBlockUndo;
import soars.common.utility.swing.table.base.undo_redo.row.RemoveRowsBlockUndo;
import soars.common.utility.swing.table.spread_sheet.SpreadSheetTable;
import soars.common.utility.swing.text.ITextUndoRedoManagerCallBack;
import soars.common.utility.swing.text.TextUndoRedoManager;
import soars.common.utility.tool.common.Tool;
import soars.common.utility.tool.expression.Expression;

/**
 * @author kurata
 *
 */
public class RuleTable extends SpreadSheetTable implements IBasicMenuHandler1, IBasicMenuHandler2, IBasicMenuHandler5, ITextUndoRedoManagerCallBack {

	/**
	 * 
	 */
	private Role _role = null;

	/**
	 * 
	 */
	private JTextField _activeCellTextField = null;

	/**
	 * 
	 */
	private JTextField _ruleCommentTextField = null;

	/**
	 * 
	 */
	private JTabbedPane _ruleTreeTabbedPane = null;

	/**
	 * 
	 */
	private RuleTree _conditionRuleTree = null;

	/**
	 * 
	 */
	private RuleTree _commandRuleTree = null;

	/**
	 * 
	 */
	private JTabbedPane _ruleTabTabbedPane = null;

	/**
	 * 
	 */
	private ConditionTabbedPane _conditionTabbedPane = null;

	/**
	 * 
	 */
	private CommandTabbedPane _commandTabbedPane = null;

	/**
	 * 
	 */
	private ComboBox _stageComboBox = null;

	/**
	 * 
	 */
	private JMenuItem _undoMenuItem = null;

	/**
	 * 
	 */
	private JMenuItem _redoMenuItem = null;

	/**
	 * 
	 */
	private JMenuItem _insertRightShiftMenuItem = null;

	/**
	 * 
	 */
	private JMenuItem _insertDownwardShiftMenuItem = null;

	/**
	 * 
	 */
	private JMenuItem _removeLeftShiftMenuItem = null;

	/**
	 * 
	 */
	private JMenuItem _removeUpwardShiftMenuItem = null;

	/**
	 * 
	 */
	private JMenuItem _removeMenuItem = null;

	/**
	 * 
	 */
	private JMenuItem _copyMenuItem = null;

	/**
	 * 
	 */
	private JMenuItem _cutMenuItem = null;

	/**
	 * 
	 */
	private JMenuItem _pasteMenuItem = null;

	/**
	 * 
	 */
	private JCheckBoxMenuItem _orCheckBoxMenuItem = null;

	/**
	 * 
	 */
	private boolean _setupCompleted = false;

	/**
	 * 
	 */
	protected List<TextUndoRedoManager> _textUndoRedoManagers = new ArrayList<TextUndoRedoManager>();

	/**
	 * 
	 */
	static private Object _lock = new Object();

	/**
	 * 
	 */
	static public List<BlockData<Rule>> _selectedAgentRules = null;

	/**
	 * 
	 */
	static public List<BlockData<Rule>> _selectedSpotRules = null;

	/**
	 * 
	 */
	static public List<RowData<Rule>> _selectedAgentRowRules = null;

	/**
	 * 
	 */
	static public List<RowData<Rule>> _selectedSpotRowRules = null;

	/**
	 * 
	 */
	static public List<ColumnData<Rule>> _selectedAgentColumnRules = null;

	/**
	 * 
	 */
	static public List<ColumnData<Rule>> _selectedSpotColumnRules = null;

	/**
	 * 
	 */
	static {
		startup();
	}

	/**
	 * 
	 */
	private static void startup() {
		synchronized( _lock) {
			_selectedAgentRules = new ArrayList<BlockData<Rule>>();
			_selectedSpotRules = new ArrayList<BlockData<Rule>>();
			_selectedAgentRowRules = new ArrayList<RowData<Rule>>();
			_selectedSpotRowRules = new ArrayList<RowData<Rule>>();
			_selectedAgentColumnRules = new ArrayList<ColumnData<Rule>>();
			_selectedSpotColumnRules = new ArrayList<ColumnData<Rule>>();
		}
	}

	/**
	 * 
	 */
	public static void refresh() {
		_selectedAgentRules.clear();
		_selectedSpotRules.clear();
		_selectedAgentRowRules.clear();
		_selectedSpotRowRules.clear();
		_selectedAgentColumnRules.clear();
		_selectedSpotColumnRules.clear();
	}

	/**
	 * @param role
	 * @return
	 */
	public static List<BlockData<Rule>> get_selectedRules(Role role) {
		return ( ( role instanceof AgentRole) ? _selectedAgentRules : _selectedSpotRules);
	}

	/**
	 * @param role
	 * @return
	 */
	public static List<RowData<Rule>> get_selectedRowRules(Role role) {
		return ( ( role instanceof AgentRole) ? _selectedAgentRowRules : _selectedSpotRowRules);
	}

	/**
	 * @param role
	 * @return
	 */
	public static List<ColumnData<Rule>> get_selectedColumnRules(Role role) {
		return ( ( role instanceof AgentRole) ? _selectedAgentColumnRules : _selectedSpotColumnRules);
	}

	/**
	 * @param originalName
	 * @param role
	 */
	public static void on_update_role_name(String originalName, Role role) {
		on_update_role_name( _selectedAgentRules, _selectedAgentRowRules, _selectedAgentColumnRules, originalName, role._name);
		on_update_role_name( _selectedSpotRules, _selectedSpotRowRules, _selectedSpotColumnRules, originalName, role._name);
	}

	/**
	 * 
	 */
	public static void clear_selectedRules() {
		_selectedAgentRules.clear();
		_selectedSpotRules.clear();
	}

	/**
	 * 
	 */
	public static void clear_selectedRowRules() {
		_selectedAgentRowRules.clear();
		_selectedSpotRowRules.clear();
	}

	/**
	 * 
	 */
	public static void clear_selectedAgentRules() {
		_selectedAgentColumnRules.clear();
		_selectedSpotColumnRules.clear();
	}

	/**
	 * @param selectedRules
	 * @param selectedRowRules
	 * @param selectedColumnRules
	 * @param originalName
	 * @param newName
	 */
	private static void on_update_role_name(List<BlockData<Rule>> selectedRules, List<RowData<Rule>> selectedRowRules, List<ColumnData<Rule>> selectedColumnRules, String originalName, String newName) {
		for ( BlockData<Rule> blockData:selectedRules) {
			for ( Rule rule:blockData)
				on_update_role_name( rule, originalName, newName);
		}
		for ( RowData<Rule> rowData:selectedRowRules) {
			for ( Rule rule:rowData)
				on_update_role_name( rule, originalName, newName);
		}
		for ( ColumnData<Rule> columnData:selectedColumnRules) {
			for ( Rule rule:columnData)
				on_update_role_name( rule, originalName, newName);
		}
	}

	/**
	 * @param rule
	 * @param originalName
	 * @param newName
	 */
	private static void on_update_role_name(Rule rule, String originalName, String newName) {
		if ( null == rule)
			return;

		rule.update_role_name( originalName, newName);
	}

	/**
	 * @param role
	 * @param owner
	 * @param parent
	 */
	public RuleTable(Role role, Frame owner, Component parent) {
		super(RuleManager.get_row_minimum_count(), RuleManager.get_column_minimum_count(), owner, parent);
		_role = role;
	}

	/* (non-Javadoc)
	 * @see soars.common.utility.swing.table.spread_sheet.SpreadSheetTable#get_default_value(int, int)
	 */
	@Override
	protected Object get_default_value(int row, int column) {
		return ( ( 0 == column) ? "" : null);
	}

	/* (non-Javadoc)
	 * @see soars.common.utility.swing.table.spread_sheet.SpreadSheetTable#select(int, int)
	 */
	@Override
	public void select(int row, int column) {
		super.select(row, column);

		set_text_to_activeCellTextField();

		internal_select();
	}

	/**
	 * @param ruleManager 
	 * @param activeCellTextField 
	 * @param ruleCommentTextField 
	 * @param ruleRowHeaderTable
	 * @param ruleTreeTabbedPane 
	 * @param conditionRuleTree 
	 * @param commandRuleTree 
	 * @param ruleTabTabbedPane 
	 * @param conditionTabbedPane 
	 * @param commandTabbedPane 
	 * @return
	 */
	public boolean setup(RuleManager ruleManager, JTextField activeCellTextField, JTextField ruleCommentTextField, RuleRowHeaderTable ruleRowHeaderTable, JTabbedPane ruleTreeTabbedPane, RuleTree conditionRuleTree, RuleTree commandRuleTree, JTabbedPane ruleTabTabbedPane, ConditionTabbedPane conditionTabbedPane, CommandTabbedPane commandTabbedPane) {
		if ( !super.setup(ruleRowHeaderTable, true))
			return false;

		_activeCellTextField = activeCellTextField;
		_ruleCommentTextField = ruleCommentTextField;
		_ruleTreeTabbedPane = ruleTreeTabbedPane;
		_conditionRuleTree = conditionRuleTree;
		_commandRuleTree = commandRuleTree;
		_ruleTabTabbedPane = ruleTabTabbedPane;
		_conditionTabbedPane = conditionTabbedPane;
		_commandTabbedPane = commandTabbedPane;

		RuleTableHeader ruleTableHeader = new RuleTableHeader( _role, this, getColumnModel(), _owner, _parent);
		if ( !ruleTableHeader.setup( new RuleTableHeaderRenderer(), true))
			return false;

		getTableHeader().setReorderingAllowed( false);
		setDefaultEditor( Object.class, null);
		putClientProperty( "JTable.autoStartsEdit", Boolean.FALSE);	// キー入力でセルが編集モードにならないようにする

		setColumnCount( ruleManager._columnCount);

		if ( !setup_column( null))
			return false;

		Object[] objects = new Object[ ruleManager._columnCount];
		for ( Rules rules:ruleManager) {
			for ( int i = 0; i < objects.length; ++i)
				objects[ i] = ( ( 0 == i) ? "" : null);

			for ( Rule rule:rules)
				objects[ rule._column] = ( ( rule._kind.equals( "condition") && rule._type.equals( ResourceManager.get_instance().get( "rule.type.condition.stage"))) ? rule._value : Rule.create( rule));

			addRow( objects);
		}

		if ( 0 < getRowCount()) {
			setRowSelectionInterval( 0, 0);
			setColumnSelectionInterval( 0, 0);
		}

		setup_column_widths( ruleManager._columnWidths);

		setup_undo_redo_manager( ruleTableHeader);

		_textUndoRedoManagers.add( new TextUndoRedoManager( _ruleCommentTextField, this));

		return true;
	}

	/* (non-Javadoc)
	 * @see soars.common.utility.swing.table.base.StandardTable#setup_columns(java.util.Vector)
	 */
	@Override
	public boolean setup_column(Vector<Integer> columnWidths) {
		DefaultTableColumnModel defaultTableColumnModel = ( DefaultTableColumnModel)getColumnModel();

		for ( int i = 0; i < defaultTableColumnModel.getColumnCount(); ++i)
			defaultTableColumnModel.getColumn( i).setHeaderValue(
				( 0 == i)
				? ResourceManager.get_instance().get( "edit.role.dialog.stage.table.header")
				: ResourceManager.get_instance().get( "edit.role.dialog.rule.table.header") + String.valueOf( i));
		
		String[] stageNames = StageManager.get_instance().get_names( true);
		if ( null == stageNames || 0 == stageNames.length)
			return false;

		_stageComboBox = ComboBox.create( stageNames,
			CommonRuleData.get_color( ResourceManager.get_instance().get( "rule.type.condition.stage"), "condition"),
			false,
			new CommonComboBoxRenderer(
				CommonRuleData.get_color( ResourceManager.get_instance().get( "rule.type.condition.stage"), "condition"),
				false));

		TableColumn tableColumn = defaultTableColumnModel.getColumn( 0);
		DefaultCellEditor defaultCellEditor = new DefaultCellEditor( _stageComboBox);
		defaultCellEditor.setClickCountToStart( 2);	// ダブルクリックでコンボボックス入力
		tableColumn.setCellEditor( defaultCellEditor);

		for ( int i = 0; i < defaultTableColumnModel.getColumnCount(); ++i) {
			tableColumn = defaultTableColumnModel.getColumn( i);
			tableColumn.setCellRenderer( new RuleTableCellRenderer( _role));

			if ( null == columnWidths)
				continue;

			Integer integer = ( Integer)columnWidths.get( i);
			tableColumn.setPreferredWidth( integer.intValue());
		}

		return true;
	}

	/**
	 * @param columnWidths
	 */
	protected void setup_column_widths(Vector<Integer> columnWidths) {
		DefaultTableColumnModel defaultTableColumnModel = ( DefaultTableColumnModel)getColumnModel();

		if ( defaultTableColumnModel.getColumnCount() != columnWidths.size()) {
			for ( int i = 0; i < defaultTableColumnModel.getColumnCount(); ++i)
				defaultTableColumnModel.getColumn( i).setPreferredWidth(
					RuleManager._defaultRuleTableColumnWidth);
		} else {
			for ( int i = 0; i < defaultTableColumnModel.getColumnCount(); ++i) {
				Integer integer = ( Integer)columnWidths.get( i);
				defaultTableColumnModel.getColumn( i).setPreferredWidth( integer.intValue());
			}
		}
	}

	/**
	 * 
	 */
	public void on_setup_completed() {
		_setupCompleted = true;
	}

	/* (non-Javadoc)
	 * @see soars.common.utility.swing.table.spread_sheet.SpreadSheetTableBase#valueChanged(javax.swing.event.ListSelectionEvent)
	 */
	@Override
	public void valueChanged(ListSelectionEvent e) {
		super.valueChanged(e);
		//internal_select();
	}

	/* (non-Javadoc)
	 * @see soars.common.utility.swing.table.spread_sheet.SpreadSheetTableBase#changeSelection(int, int, boolean, boolean)
	 */
	@Override
	public void changeSelection(int rowIndex, int columnIndex, boolean toggle, boolean extend) {
		//System.out.println( rowIndex + ", " + columnIndex);

		// 現在選択されているセルを取得
		int[] previousRows = getSelectedRows();
		if ( 1 != previousRows.length) {
			super.changeSelection(rowIndex, columnIndex, toggle, extend);
			internal_select();
			return;
		}

		int[] previousColumns = getSelectedColumns();
		if ( 1 != previousColumns.length) {
			on_change_selection( previousRows[ 0], JOptionPane.YES_NO_OPTION);
			super.changeSelection(rowIndex, columnIndex, toggle, extend);
			internal_select();
			return;
		}

		//System.out.println( previousRows[ 0] + ", " + previousColumns[ 0]);

		if ( 0 > previousRows[ 0] || 0 > previousColumns[ 0]) {
			super.changeSelection(rowIndex, columnIndex, toggle, extend);
			internal_select();
			return;
		}

		if ( 0 == previousColumns[ 0]) {
			// ステージなら無視
			on_change_selection( previousRows[ 0], JOptionPane.YES_NO_OPTION);
			super.changeSelection(rowIndex, columnIndex, toggle, extend);
			internal_select();
			return;
		}

		if ( previousRows[ 0] == rowIndex && previousColumns[ 0] == columnIndex) {
			// 移動していない
			super.changeSelection(rowIndex, columnIndex, toggle, extend);
			return;
		}

		on_change_selection( previousRows[ 0], previousColumns[ 0], JOptionPane.YES_NO_OPTION);

		on_change_selection( previousRows[ 0], JOptionPane.YES_NO_OPTION);

		super.changeSelection(rowIndex, columnIndex, toggle, extend);

		internal_select();

		internal_synchronize();
	}

	/**
	 * @param row
	 * @param column
	 * @param optionType
	 * @return
	 */
	public int on_change_selection(int row, int column, int optionType) {
		//System.out.println( row + ", " + column);
		int result = JOptionPane.NO_OPTION;

		if ( 0 > row || 0 > column)
			return result;

		if ( 0 == column) {
			// ステージなら無視
			return result;
		}

		// 確認する必要があるのはどんな場合か？
		// 現在のセルの中身と表示されているページが異なる場合
		// 現在のセルの中身と表示されているページの内容が異なる場合
		RuleTree ruleTree = ( RuleTree)_ruleTreeTabbedPane.getSelectedComponent();
		RulePropertyPanelBase rulePropertyPanelBase = ruleTree.get_selected_page();
		if ( null == rulePropertyPanelBase)
			return result;

		Rule rule1 = rulePropertyPanelBase.get();
		if ( null == rule1)
			return result;

		//System.out.println( rule1._kind + ", " + rule1._type);
		//System.out.println( rule1._kind + ", " + rule1._value);

		Rule rule2 = ( Rule)getValueAt( row, column);
		if ( null == rule2)
			return result;

		rule2 = Rule.create( rule2);
		//System.out.println( rule2._kind + ", " + rule2._type);
		//System.out.println( rule2._kind + ", " + rule2._value);

		if ( !type_changed( rule1, rule2) && value_changed( rule1, rule2)) {
			result = JOptionPane.showConfirmDialog(
				getParent(),
				ResourceManager.get_instance().get( "edit.rule.dialog.confirm.save.rule.changes.message"),
				ResourceManager.get_instance().get( "edit.rule.dialog.title"),
				optionType,
				JOptionPane.INFORMATION_MESSAGE);
			if ( JOptionPane.YES_OPTION == result) {
				if ( ( ( rule1._kind.equals( "condition") && rule1._type.equals( ResourceManager.get_instance().get( "rule.type.condition.others"))) || ( rule1._kind.equals( "command") && rule1._type.equals( ResourceManager.get_instance().get( "rule.type.command.others")))) && rule1._value.equals( "")) {
					setValueAt( null, row, column);
				} else {
					// TODO 2012.9.20
					if ( !( rule1 instanceof GenericRule) && !( rule2 instanceof GenericRule)) {
						rule2._type = rule1._type;
						rule2._value = rule1._value;
					} else {
						( ( GenericRule)rule2).get( ( GenericRule)rule1);
					}
					setValueAt( rule2, row, column);
				}

				if ( rulePropertyPanelBase instanceof TimeCommandPropertyPanel)
					rulePropertyPanelBase.confirm();

				RuleTabbedPane ruleTabbedPane = ( RuleTabbedPane)_ruleTabTabbedPane.getSelectedComponent();
				ruleTabbedPane.update( rulePropertyPanelBase);
				refresh();
				repaint();
			} else
				rulePropertyPanelBase.set( rule2);
		}

		return result;
	}

	/**
	 * @param rule1
	 * @param rule2
	 * @return
	 */
	private boolean type_changed(Rule rule1, Rule rule2) {
		// TODO 2012.9.20
		if ( ( rule1 instanceof GenericRule && !( rule2 instanceof GenericRule))
			|| ( !( rule1 instanceof GenericRule) && rule2 instanceof GenericRule))
			return false;

		if ( !rule1._kind.equals( rule2._kind))
			return true;

		if ( !( rule1 instanceof GenericRule) && !( rule2 instanceof GenericRule)) {
			if ( type_changed( rule1, rule2, new String[] {
				ResourceManager.get_instance().get( "rule.type.command.get.equip"),
				ResourceManager.get_instance().get( "rule.type.command.put.equip")}))
				return false;

			if ( type_changed( rule1, rule2, new String[] {
				ResourceManager.get_instance().get( "rule.type.command.attach"),
				ResourceManager.get_instance().get( "rule.type.command.detach")}))
				return false;

			if ( type_changed( rule1, rule2, new String[] {
				ResourceManager.get_instance().get( "rule.type.command.next.stage"),
				ResourceManager.get_instance().get( "rule.type.command.terminate"),
				ResourceManager.get_instance().get( "rule.type.command.trace"),
				ResourceManager.get_instance().get( "rule.type.command.others")}))
				return false;
		}

		return !rule1._type.equals( rule2._type);
	}

	/**
	 * @param rule1
	 * @param rule2
	 * @return
	 */
	private boolean value_changed(Rule rule1, Rule rule2) {
		// TODO 2012.9.20
		if ( ( rule1 instanceof GenericRule && !( rule2 instanceof GenericRule))
			|| ( !( rule1 instanceof GenericRule) && rule2 instanceof GenericRule))
			return false;

		if ( !rule1._kind.equals( rule2._kind))
			return true;

		if ( !( rule1 instanceof GenericRule) && !( rule2 instanceof GenericRule)) {
			if ( type_changed( rule1, rule2, new String[] {
				ResourceManager.get_instance().get( "rule.type.command.terminate"),
				ResourceManager.get_instance().get( "rule.type.command.trace")}))
				return true;

			return !rule1._value.equals( rule2._value);
		} else {
			GenericRule genericRule1 = ( GenericRule)rule1;
			GenericRule genericRule2 = ( GenericRule)rule2;
			return !genericRule1.same_as( genericRule2);
		}
	}

	/**
	 * @param rule1
	 * @param rule2
	 * @param types
	 * @return
	 */
	private boolean type_changed(Rule rule1, Rule rule2, String[] types) {
		for ( int i = 0; i < types.length; ++i) {
			if ( rule1._type.endsWith( types[ i])) {
				for ( int j = 0; j < types.length; ++j) {
					if ( j == i)
						continue;

					if ( rule2._type.equals( types[ j]))
						return true;
				}
			}
		}
		return false;
	}

	/**
	 * @param row
	 * @param optionType
	 * @return
	 */
	public int on_change_selection(int row, int optionType) {
		int result = JOptionPane.NO_OPTION;

		if ( 0 > row)
			return result;

		RuleRowData ruleRowData = ( RuleRowData)_spreadSheetTableBase.getValueAt( row, 0);
		if ( ruleRowData._comment.equals( _ruleCommentTextField.getText()))
			return result;

		result = JOptionPane.showConfirmDialog(
			getParent(),
			ResourceManager.get_instance().get( "edit.rule.dialog.confirm.save.comment.changes.message"),
			ResourceManager.get_instance().get( "edit.rule.dialog.title"),
			optionType,
			JOptionPane.INFORMATION_MESSAGE);
		if ( JOptionPane.YES_OPTION == result) {
			update_ruleComment( row);
			refresh();
			repaint();
		}

		return result;
	}

	/**
	 * 
	 */
	private void internal_select() {
		if ( !_setupCompleted)
			return;

		if ( 0 == getRowCount())
			return;

		int[] rows = getSelectedRows();
		int[] columns = getSelectedColumns();

		if ( null == rows || 1 != rows.length || 0 > rows[ 0] || getRowCount() <= rows[ 0]
			|| null == columns || 1 != columns.length || 0 > columns[ 0] || getColumnCount() <= columns[ 0])
			return;

		if (  0 == columns[ 0]) {
			set_text_to_activeCellTextField();
			return;
		}

		//System.out.println( rows[ 0] + ", " + columns[ 0]);
		Rule rule = get_rule( rows[ 0], columns[ 0]);

		internal_select( rule);

		if ( null == rule) {
			set_text_to_activeCellTextField();
			return;
		}

		_ruleTreeTabbedPane.setSelectedComponent( rule._kind.equals( "condition") ? _conditionRuleTree : _commandRuleTree);
		_ruleTabTabbedPane.setSelectedComponent( rule._kind.equals( "condition") ? _conditionTabbedPane : _commandTabbedPane);

		set_text_to_activeCellTextField();
	}

	/**
	 * @param rule
	 */
	private void internal_select(Rule rule) {
		if ( _setupCompleted && null == rule)
			return;

		RuleTabbedPane ruleTabbedPane = ( rule._kind.equals( "condition") ? _conditionTabbedPane : _commandTabbedPane);
		RuleTree ruleTree = ( rule._kind.equals( "condition") ? _conditionRuleTree : _commandRuleTree);

		boolean exist = false;
		for ( RulePropertyPanelBase rulePropertyPanelBase:ruleTabbedPane._rulePropertyPanelBases) {
			rulePropertyPanelBase.on_setup_completed();
			if ( rulePropertyPanelBase.set( rule)) {
				ruleTabbedPane.select( rulePropertyPanelBase);
				ruleTree.select( rulePropertyPanelBase);
				exist = true;
			}
		}

		if ( exist)
			return;

		if ( null == rule || rule._type.equals( ""))
			return;

		if ( rule._kind.equals( "condition")) {
			RulePropertyPanelBase rulePropertyPanelBase = ruleTabbedPane.get( ResourceManager.get_instance().get( "rule.type.condition.others"));
			if ( null != rulePropertyPanelBase) {
				ruleTabbedPane.select( rulePropertyPanelBase);
				EditRuleValuePropertyPanel editRuleValuePropertyPanel = ( EditRuleValuePropertyPanel)rulePropertyPanelBase;
				// TODO 2012.9.20
				editRuleValuePropertyPanel.set( rule instanceof GenericRule ? rule.get_cell_text( _role) : rule._value);
				//editRuleValuePropertyPanel.set( rule._value);
				ruleTree.select( rulePropertyPanelBase);
			}
		} else {
			RulePropertyPanelBase rulePropertyPanelBase = ruleTabbedPane.get( ResourceManager.get_instance().get( "rule.type.command.others"));
			if ( null != rulePropertyPanelBase) {
				ruleTabbedPane.select( rulePropertyPanelBase);
				OthersCommandPropertyPanel othersCommandPropertyPanel = ( OthersCommandPropertyPanel)rulePropertyPanelBase;
				// TODO 2012.9.20
				if ( rule instanceof GenericRule) {
					GenericRule genericRule = new GenericRule( ( GenericRule)rule);
					genericRule._value = genericRule.get_cell_text( _role);
					othersCommandPropertyPanel.set( genericRule);
				} else
					othersCommandPropertyPanel.set( Rule.create( rule));
				//othersCommandPropertyPanel.set( Rule.create( rule));
				ruleTree.select( rulePropertyPanelBase);
			}
		}
	}

	/**
	 * 
	 */
	public void set_text_to_activeCellTextField() {
		int[] rows = getSelectedRows();
		int[] columns = getSelectedColumns();

		if ( 0 == getRowCount() || null == rows || 1 != rows.length || 0 > rows[ 0]) {
			_activeCellTextField.setText( "");
			update_ruleCommentTextField( "");
			return;
		}

		update_ruleCommentTextField( rows[ 0]);

		if ( null == columns || 1 != columns.length || 0 > columns[ 0]) {
			_activeCellTextField.setText( "");
			return;
		}

		Rule rule = get_rule( rows[ 0], columns[ 0]);
		if ( null == rule) {
			_activeCellTextField.setText( "");
			return;
		}

		update_activeCellTextField( rule);
	}

	/**
	 * @param rule
	 */
	private void update_activeCellTextField(Rule rule) {
		String value = rule.get_cell_text( _role);

		// TODO 2012.9.20
		_activeCellTextField.setText( ( rule instanceof GenericRule)
			? value
			: ( ( rule._or ? " || " : "") + CommonRuleData.get_name( rule) + " : " + value));

		Color color = RuleTableCellRenderer.get_color( rule, _role);
		if ( null != color)
			_activeCellTextField.setForeground( color);
	}

	/**
	 * 
	 */
	public void update_ruleCommentTextField() {
		int[] rows = getSelectedRows();

		if ( 0 == getRowCount() || null == rows || 1 != rows.length || 0 > rows[ 0]) {
			update_ruleCommentTextField( "");
			return;
		}

		RuleRowData ruleRowData = ( RuleRowData)_spreadSheetTableBase.getValueAt( rows[ 0], 0);
		update_ruleCommentTextField( ruleRowData._comment);
	}

	/**
	 * @param row
	 */
	public void update_ruleCommentTextField(int row) {
		RuleRowData ruleRowData = ( RuleRowData)_spreadSheetTableBase.getValueAt( row, 0);
		update_ruleCommentTextField( ruleRowData._comment);
	}

	/**
	 * @param comment
	 */
	private void update_ruleCommentTextField(String comment) {
		_ruleCommentTextField.setText( comment);
		_textUndoRedoManagers.clear();
		_textUndoRedoManagers.add( new TextUndoRedoManager( _ruleCommentTextField, this));
	}

	/**
	 * 
	 */
	public void update_ruleComment() {
		int[] rows = getSelectedRows();

		if ( 0 == getRowCount() || null == rows || 1 != rows.length || 0 > rows[ 0])
			return;

		update_ruleComment( rows[ 0]);
	}

	/**
	 * @param row
	 */
	private void update_ruleComment(int row) {
		RuleRowData ruleRowData = new RuleRowData( _ruleCommentTextField.getText());
		SetValueUndo setValueUndo = new SetValueUndo( ruleRowData, row, 0, _spreadSheetTableBase, this);
		_undoableEditSupport.postEdit( setValueUndo);
		_spreadSheetTableBase.setValueAtDefault( ruleRowData, row, 0);
	}

	/* (non-Javadoc)
	 * @see soars.common.utility.swing.table.spread_sheet.SpreadSheetTable#setValueAt(java.lang.String, soars.common.utility.swing.table.base.undo_redo.cell.SetValueUndo)
	 */
	@Override
	public void setValueAt(String kind, SetValueUndo setValueUndo) {
		if ( setValueUndo._table instanceof RuleRowHeaderTable) {
			if ( kind.equals( "undo"))
				_spreadSheetTableBase.setValueAtDefault( setValueUndo._oldValue, setValueUndo._row, setValueUndo._column);
			else if ( kind.equals( "redo"))
				_spreadSheetTableBase.setValueAtDefault(setValueUndo._newValue, setValueUndo._row, setValueUndo._column);
			return;
		}
		super.setValueAt(kind, setValueUndo);
	}

	/**
	 * @param rulePropertyPanelBase
	 * @return
	 */
	public boolean update(RulePropertyPanelBase rulePropertyPanelBase) {
		int[] rows = getSelectedRows();
		if ( 1 != rows.length)
			return false;

		int[] columns = getSelectedColumns();
		if ( 1 != columns.length)
			return false;

		if ( 0 == columns[ 0])
			return false;

		Rule newRule = rulePropertyPanelBase.get();
		if ( null == newRule)
			return false;

		if ( ( ( newRule._kind.equals( "condition") && newRule._type.equals( ResourceManager.get_instance().get( "rule.type.condition.others"))) || ( newRule._kind.equals( "command") && newRule._type.equals( ResourceManager.get_instance().get( "rule.type.command.others")))) && newRule._value.equals( "")) {
			newRule = null;
		} else {
			Rule rule = get_rule( rows[ 0], columns[ 0]);
			if ( null == rule)
				newRule._column = columns[ 0];
			else {
				newRule._column = rule._column;
				newRule._or = rule._or;
			}
		}

		set_rule( newRule, rows[ 0], columns[ 0]);

		refresh();

		return true;
	}

	/**
	 * @param row
	 * @param column
	 * @return
	 */
	protected Rule get_rule(int row, int column) {
		Object object = getValueAt( row, column);
		if ( null == object)
			return null;

		if ( 0 == column)
			return ( ( object instanceof String)
				? Rule.create( "condition", 0, ResourceManager.get_instance().get( "rule.type.condition.stage"), ( String)object, false)
				: null);

		if ( object instanceof Rule)
			return ( Rule)object;

		// TODO 2012.9.20
		if ( object instanceof GenericRule)
			return ( GenericRule)object;

		return null;
	}

	/**
	 * @param rule
	 * @param row
	 * @param column
	 * @return
	 */
	protected Rule set_rule(Rule rule, int row, int column) {
		if ( null == rule) {
			setValueAt( null, row, column);
			return null;
		}

		// TODO 2012.9.20
		if ( rule instanceof GenericRule) {
			return set_genericRule( ( GenericRule)rule, row, column);
		} else
			return set_rule( rule._kind, rule._type, rule._value, rule._or, row, column);
	}

	/**
	 * @param rule
	 * @param row
	 * @param column
	 * @return
	 */
	private Rule set_genericRule(GenericRule genericRule, int row, int column) {
		// TODO 2012.9.20
		Rule rule = Rule.create( genericRule);
		setValueAt( genericRule, row, column);
		return rule;
	}

	/**
	 * @param kind
	 * @param type
	 * @param value
	 * @param or
	 * @param row
	 * @param column
	 * @return
	 */
	protected Rule set_rule(String kind, String type, String value, boolean or, int row, int column) {
		Rule rule = Rule.create( kind, column, type, value, or);
		if ( 0 == column) {
			if ( !type.equals( ResourceManager.get_instance().get( "rule.type.condition.stage")))
				return null;

			setValueAt( value, row, column);
		} else
			setValueAt( rule, row, column);

		return rule;
	}

	/* (non-Javadoc)
	 * @see soars.common.utility.swing.table.base.StandardTable#addRow()
	 */
	@Override
	public void addRow() {
		Object[] objects = new Object[ getColumnCount()];
		objects[ 0] = "";
		addRow( objects);
	}

	/* (non-Javadoc)
	 * @see soars.common.utility.swing.table.base.StandardTable#insertRow(int)
	 */
	@Override
	public void insertRow(int row) {
		Object[] objects = new Object[ getColumnCount()];
		objects[ 0] = "";
		insertRow( row, objects);
	}

	/* (non-Javadoc)
	 * @see soars.common.utility.swing.table.base.StandardTable#setup_popup_menu()
	 */
	@Override
	protected void setup_popup_menu() {
		super.setup_popup_menu();

		_undoMenuItem = _userInterface.append_popup_menuitem(
			_popupMenu,
			ResourceManager.get_instance().get( "common.popup.menu.undo.menu"),
			new UndoAction( ResourceManager.get_instance().get( "common.popup.menu.undo.menu"), this),
			ResourceManager.get_instance().get( "common.popup.menu.undo.mnemonic"),
			ResourceManager.get_instance().get( "common.popup.menu.undo.stroke"));
		_redoMenuItem = _userInterface.append_popup_menuitem(
			_popupMenu,
			ResourceManager.get_instance().get( "common.popup.menu.redo.menu"),
			new RedoAction( ResourceManager.get_instance().get( "common.popup.menu.redo.menu"), this),
			ResourceManager.get_instance().get( "common.popup.menu.redo.mnemonic"),
			ResourceManager.get_instance().get( "common.popup.menu.redo.stroke"));

		_popupMenu.addSeparator();

		_insertRightShiftMenuItem = _userInterface.append_popup_menuitem(
			_popupMenu,
			ResourceManager.get_instance().get( "common.popup.menu.insert.right.shift.menu"),
			new InsertRightShiftAction( ResourceManager.get_instance().get( "common.popup.menu.insert.right.shift.menu"), this),
			ResourceManager.get_instance().get( "common.popup.menu.insert.right.shift.mnemonic"),
			ResourceManager.get_instance().get( "common.popup.menu.insert.right.shift.stroke"));
		_insertDownwardShiftMenuItem = _userInterface.append_popup_menuitem(
			_popupMenu,
			ResourceManager.get_instance().get( "common.popup.menu.insert.downward.shift.menu"),
			new InsertDownwardShiftAction( ResourceManager.get_instance().get( "common.popup.menu.insert.downward.shift.menu"), this),
			ResourceManager.get_instance().get( "common.popup.menu.insert.downward.shift.mnemonic"),
			ResourceManager.get_instance().get( "common.popup.menu.insert.downward.shift.stroke"));

		_popupMenu.addSeparator();

		_removeLeftShiftMenuItem = _userInterface.append_popup_menuitem(
			_popupMenu,
			ResourceManager.get_instance().get( "common.popup.menu.remove.left.shift.menu"),
			new RemoveLeftShiftAction( ResourceManager.get_instance().get( "common.popup.menu.remove.left.shift.menu"), this),
			ResourceManager.get_instance().get( "common.popup.menu.remove.left.shift.mnemonic"),
			ResourceManager.get_instance().get( "common.popup.menu.remove.left.shift.stroke"));
		_removeUpwardShiftMenuItem = _userInterface.append_popup_menuitem(
			_popupMenu,
			ResourceManager.get_instance().get( "common.popup.menu.remove.upward.shift.menu"),
			new RemoveUpwardShiftAction( ResourceManager.get_instance().get( "common.popup.menu.remove.upward.shift.menu"), this),
			ResourceManager.get_instance().get( "common.popup.menu.remove.upward.shift.mnemonic"),
			ResourceManager.get_instance().get( "common.popup.menu.remove.upward.shift.stroke"));
		_removeMenuItem = _userInterface.append_popup_menuitem(
			_popupMenu,
			ResourceManager.get_instance().get( "common.popup.menu.remove.menu"),
			new RemoveAction( ResourceManager.get_instance().get( "common.popup.menu.remove.menu"), this),
			ResourceManager.get_instance().get( "common.popup.menu.remove.mnemonic"),
			ResourceManager.get_instance().get( "common.popup.menu.remove.stroke"));

		_popupMenu.addSeparator();

		_copyMenuItem = _userInterface.append_popup_menuitem(
			_popupMenu,
			ResourceManager.get_instance().get( "common.popup.menu.copy.menu"),
			new CopyAction( ResourceManager.get_instance().get( "common.popup.menu.copy.menu"), this),
			ResourceManager.get_instance().get( "common.popup.menu.copy.mnemonic"),
			ResourceManager.get_instance().get( "common.popup.menu.copy.stroke"));
		_cutMenuItem = _userInterface.append_popup_menuitem(
			_popupMenu,
			ResourceManager.get_instance().get( "common.popup.menu.cut.menu"),
			new CutAction( ResourceManager.get_instance().get( "common.popup.menu.cut.menu"), this),
			ResourceManager.get_instance().get( "common.popup.menu.cut.mnemonic"),
			ResourceManager.get_instance().get( "common.popup.menu.cut.stroke"));
		_pasteMenuItem = _userInterface.append_popup_menuitem(
			_popupMenu,
			ResourceManager.get_instance().get( "common.popup.menu.paste.menu"),
			new PasteAction( ResourceManager.get_instance().get( "common.popup.menu.paste.menu"), this),
			ResourceManager.get_instance().get( "common.popup.menu.paste.mnemonic"),
			ResourceManager.get_instance().get( "common.popup.menu.paste.stroke"));

		_popupMenu.addSeparator();

		_orCheckBoxMenuItem = _userInterface.append_popup_checkbox_menuitem(
			_popupMenu,
			ResourceManager.get_instance().get( "common.popup.menu.or.menu"),
			new OrAction( ResourceManager.get_instance().get( "common.popup.menu.or.menu"), this),
			ResourceManager.get_instance().get( "common.popup.menu.or.mnemonic"),
			ResourceManager.get_instance().get( "common.popup.menu.or.stroke"));
	}

	/* (non-Javadoc)
	 * @see soars.common.utility.swing.table.spread_sheet.SpreadSheetTable#on_mouse_right_up(int, int, java.awt.event.MouseEvent)
	 */
	@Override
	protected boolean on_mouse_right_up(int row, int column, MouseEvent mouseEvent) {
		if (!super.on_mouse_right_up(row, column, mouseEvent))
			return false;

		if ( !is_possible())
			return false;

		_undoMenuItem.setEnabled( false);
		_redoMenuItem.setEnabled( false);

		_insertRightShiftMenuItem.setEnabled( false);
		_insertDownwardShiftMenuItem.setEnabled( false);

		_removeLeftShiftMenuItem.setEnabled( false);
		_removeUpwardShiftMenuItem.setEnabled( false);
		_removeMenuItem.setEnabled( false);

		_copyMenuItem.setEnabled( false);
		_cutMenuItem.setEnabled( false);
		_pasteMenuItem.setEnabled( false);

		_orCheckBoxMenuItem.setState( false);
		_orCheckBoxMenuItem.setEnabled( false);

		if ( getRowCount() > 0) {
			int[] rows = getSelectedRows();
			int[] columns = getSelectedColumns();

			Arrays.sort( rows);
			Arrays.sort( columns);

			setup_undoMenuItem( rows, columns);
			setup_redoMenuItem( rows, columns);

			setup_insertRightShiftMenuItem( rows, columns);
			setup_insertDownwardShiftMenuItem( rows, columns);

			setup_removeLeftShiftMenuItem( rows, columns);
			setup_removeUpwardShiftMenuItem( rows, columns);
			setup_removeMenuItem( rows, columns);

			setup_copyMenuItem( rows, columns);
			setup_cutMenuItem( rows, columns);
			setup_pasteMenuItem( rows, columns);

			setup_orCheckBoxMenuItem( rows, columns);
		}

		_popupMenu.show( this, mouseEvent.getX(), mouseEvent.getY());

		return true;
	}

	/**
	 * @param rows
	 * @param columns
	 */
	private void setup_undoMenuItem(int[] rows, int[] columns) {
		_undoMenuItem.setEnabled( _undoManager.canUndo());
	}

	/**
	 * @param rows
	 * @param columns
	 */
	private void setup_redoMenuItem(int[] rows, int[] columns) {
		_redoMenuItem.setEnabled( _undoManager.canRedo());
	}

	/**
	 * @param rows
	 * @param columns
	 */
	private void setup_insertRightShiftMenuItem(int[] rows, int[] columns) {
		_insertRightShiftMenuItem.setEnabled( 0 != columns[ 0]);
	}

	/**
	 * @param rows
	 * @param columns
	 */
	private void setup_insertDownwardShiftMenuItem(int[] rows, int[] columns) {
		_insertDownwardShiftMenuItem.setEnabled( true);
	}

	/**
	 * @param rows
	 * @param columns
	 */
	private void setup_removeLeftShiftMenuItem(int[] rows, int[] columns) {
		_removeLeftShiftMenuItem.setEnabled( 0 != columns[ 0]);
	}

	/**
	 * @param rows
	 * @param columns
	 */
	private void setup_removeUpwardShiftMenuItem(int[] rows, int[] columns) {
		_removeUpwardShiftMenuItem.setEnabled( true);
	}

	/**
	 * @param rows
	 * @param columns
	 */
	private void setup_removeMenuItem(int[] rows, int[] columns) {
		// 選択領域内が全てnullでないこと
		_removeMenuItem.setEnabled( !is_empty( rows, columns));
	}

	/**
	 * @param rows
	 * @param columns
	 */
	private void setup_copyMenuItem(int[] rows, int[] columns) {
		_copyMenuItem.setEnabled( true);
	}

	/**
	 * @param rows
	 * @param columns
	 */
	private void setup_cutMenuItem(int[] rows, int[] columns) {
		// 選択領域内が全てnullでないこと
		_cutMenuItem.setEnabled( !is_empty( rows, columns));
	}

	/**
	 * @param rows
	 * @param columns
	 */
	protected void setup_pasteMenuItem(int[] rows, int[] columns) {
		// １つのセルだけが選択されていること
		// バッファが空ではないこと
		// バッファと選択領域のステージ位置が矛盾していないこと
		// 貼付けを行ったら内容が変わること
		_pasteMenuItem.setEnabled( 1 == rows.length && 1 == columns.length
			&& !get_selectedRules( _role).isEmpty() && is_correct( rows, columns) && !is_same( rows[ 0], columns[ 0]));
	}

	/**
	 * @param rows
	 * @param columns
	 */
	private void setup_orCheckBoxMenuItem(int[] rows, int[] columns) {
		if ( 2 > columns[ 0]) {
			_orCheckBoxMenuItem.setState( false);
			_orCheckBoxMenuItem.setEnabled( false);
			return;
		}

		for ( int row:rows) {
			for ( int column:columns) {
				Rule rule = ( Rule)getValueAt( row, column);
				if ( null == rule)
					continue;

				if ( rule._kind.equals( "command")) {
					_orCheckBoxMenuItem.setState( false);
					_orCheckBoxMenuItem.setEnabled( false);
					return;
				}
			}
		}

		for ( int row:rows) {
			if ( no_rule( row, columns))
				continue;

			boolean condition = false;
			for ( int column = columns[ 0] - 1; 0 < column; --column) {
				Rule rule = ( Rule)getValueAt( row, column);
				if ( null == rule)
					continue;

				if ( rule._kind.equals( "command")) {
					_orCheckBoxMenuItem.setState( false);
					_orCheckBoxMenuItem.setEnabled( false);
					return;
				}

				if ( rule._kind.equals( "condition")) {
					condition = true;
					break;
				}
			}

			if ( !condition) {
				_orCheckBoxMenuItem.setState( false);
				_orCheckBoxMenuItem.setEnabled( false);
				return;
			}
		}

		_orCheckBoxMenuItem.setState( or_all( rows, columns));
		_orCheckBoxMenuItem.setEnabled( true);
	}

	/**
	 * @param row
	 * @param columns
	 * @return
	 */
	private boolean no_rule(int row, int[] columns) {
		for ( int column:columns) {
			Rule rule = ( Rule)getValueAt( row, column);
			if ( null != rule)
				return false;
		}
		return true;
	}

	/**
	 * @param rows
	 * @param columns
	 * @return
	 */
	private boolean or_all(int[] rows, int[] columns) {
		for ( int row:rows) {
			for ( int column:columns) {
				Rule rule = ( Rule)getValueAt( row, column);
				if ( null == rule)
					continue;

				if ( !rule._or)
					return false;
			}
		}
		return true;
	}

	/**
	 * @return
	 */
	private boolean is_possible() {
		// 少なくとも１つのセルが選択されていて、選択領域が連続ならtrueを返す
		int[] rows = getSelectedRows();
		if ( null == rows || 1 > rows.length)
			return false;

		int[] columns = getSelectedColumns();
		if ( null == columns || 1 > columns.length)
			return false;

		Arrays.sort( rows);
		Arrays.sort( columns);

		return ( Tool.is_consecutive( rows) && Tool.is_consecutive( columns));
	}

	/**
	 * 
	 */
	private void on_error_possible() {
		// 選択領域が不正な場合のエラーメッセージ
		JOptionPane.showMessageDialog( _parent,
			ResourceManager.get_instance().get( "edit.role.dialog.rule.table.possible.error.message"),
			ResourceManager.get_instance().get( "application.title"),
			JOptionPane.ERROR_MESSAGE);
	}

	/**
	 * @param rows
	 * @param columns
	 * @return
	 */
	private boolean is_correct(int[] rows, int[] columns) {
		// バッファと選択領域のステージ列の位置が矛盾していたらfalseを返す
		List<BlockData<Rule>> selectedRules = get_selectedRules( _role);
		if ( selectedRules.isEmpty())
			return false;

		return ( ( 0 == columns[ 0] && 0 == selectedRules.get( 0)._column) || ( 0 != columns[ 0] && 0 != selectedRules.get( 0)._column));
	}

	/**
	 * 
	 */
	private void on_error_correct() {
		// バッファと選択領域のステージ列の位置が矛盾している場合のエラーメッセージ
		JOptionPane.showMessageDialog( _parent,
			ResourceManager.get_instance().get( "edit.role.dialog.rule.table.correct.error.message"),
			ResourceManager.get_instance().get( "application.title"),
			JOptionPane.ERROR_MESSAGE);
	}

	/**
	 * @param rows
	 * @param columns
	 * @return
	 */
	private boolean is_empty(int[] rows, int[] columns) {
		// TODO 選択領域内が全てnullならtrueを返す←必要か？
		return false;
	}

	/**
	 * @param row
	 * @param column
	 * @return
	 */
	private boolean is_same(int row, int column) {
		// TODO バッファと選択領域の内容が同じならtrueを返す←必要か？
		return false;
	}

	/* (non-Javadoc)
	 * @see soars.common.utility.swing.table.spread_sheet.SpreadSheetTableBase#on_enter(java.awt.event.ActionEvent)
	 */
	public void on_enter(ActionEvent actionEvent) {
		int[] rows = getSelectedRows();
		int[] columns = getSelectedColumns();
		if ( null == rows || 1 != rows.length || null == columns || 1 != columns.length || 0 != columns[ 0])
			return;

		editCellAt(rows[ 0], columns[ 0]);
	}

	/* (non-Javadoc)
	 * @see soars.common.utility.swing.table.spread_sheet.SpreadSheetTableBase#on_escape(java.awt.event.ActionEvent)
	 */
	@Override
	public void on_escape(ActionEvent actionEvent) {
		stop_cell_editing();
		refresh();
	}

	/* (Non Javadoc)
	 * @see soars.application.visualshell.common.menu.basic1.IBasicMenuHandler1#on_edit(java.awt.event.ActionEvent)
	 */
	@Override
	public void on_edit(ActionEvent actionEvent) {
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
	 * @see soars.application.visualshell.common.menu.basic1.IBasicMenuHandler1#on_remove(java.awt.event.ActionEvent)
	 */
	@Override
	public void on_remove(ActionEvent actionEvent) {
		if ( !is_possible()) {
			on_error_possible();
			return;
		}

		int[] rows = getSelectedRows();
		if ( null == rows || 0 == rows.length)
			return;

		int[] columns = getSelectedColumns();
		if ( null == columns || 0 == columns.length)
			return;

		// TODO 選択領域内が全てnullなら実行しない←必要か？
		if ( is_empty( rows, columns))
			return;

		if ( JOptionPane.YES_OPTION == JOptionPane.showConfirmDialog(
			_parent,
			ResourceManager.get_instance().get( "edit.role.dialog.rule.table.confirm.remove.message"),
			ResourceManager.get_instance().get( "application.title"),
			JOptionPane.YES_NO_OPTION)) {
			remove( rows, columns);
			refresh();
		}
	}

	/**
	 * @param rows
	 * @param columns
	 * @return
	 */
	private boolean remove(int[] rows, int[] columns) {
		List<SetValueUndo> setValueUndoList = new ArrayList<SetValueUndo>();
		List<SetValueUndo> setValueUndoListOr = new ArrayList<SetValueUndo>();
		for ( int row:rows) {
			for ( int column:columns)
				setValueUndoList.add( new SetValueUndo( 0 == column ? "" : null, row, column, this, this));

			// 論理和(or)への対応
			get_or_updated_rule( setValueUndoListOr, row, columns[ columns.length - 1] + 1, condition_exists( row, columns[ 0] - 1));
		}
		CustomSetValuesUndo customSetValuesUndo = new CustomSetValuesUndo( setValueUndoList, setValueUndoListOr, this, this);
		setValuesAt( customSetValuesUndo, setValueUndoList);

		internal_select();
		set_text_to_activeCellTextField();
		repaint();
		requestFocus();
		synchronize();

		return true;
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.common.menu.basic2.IBasicMenuHandler2#on_undo(java.awt.event.ActionEvent)
	 */
	@Override
	public void on_undo(ActionEvent actionEvent) {
		on_undo();
	}

	/* (non-Javadoc)
	 * @see soars.common.utility.swing.table.spread_sheet.SpreadSheetTable#on_undo()
	 */
	@Override
	public void on_undo() {
		super.on_undo();
		internal_select();
		set_text_to_activeCellTextField();
		refresh();
		repaint();
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.common.menu.basic2.IBasicMenuHandler2#on_redo(java.awt.event.ActionEvent)
	 */
	@Override
	public void on_redo(ActionEvent actionEvent) {
		on_redo();
	}

	/* (non-Javadoc)
	 * @see soars.common.utility.swing.table.spread_sheet.SpreadSheetTable#on_redo()
	 */
	@Override
	public void on_redo() {
		super.on_redo();
		internal_select();
		set_text_to_activeCellTextField();
		refresh();
		repaint();
	}

	/* (Non Javadoc)
	 * @see soars.application.visualshell.common.menu.basic2.IBasicMenuHandler2#on_copy(java.awt.event.ActionEvent)
	 */
	@Override
	public void on_copy(ActionEvent actionEvent) {
		if ( !is_possible()) {
			on_error_possible();
			return;
		}

		int[] rows = getSelectedRows();
		if ( null == rows || 0 == rows.length)
			return;

		int[] columns = getSelectedColumns();
		if ( null == columns || 0 == columns.length)
			return;

		copy( rows, columns);
	}

	/**
	 * @param rows 
	 * @param columns 
	 * @return
	 */
	private boolean copy(int[] rows, int[] columns) {
		refresh();
		List<BlockData<Rule>> selectedRules = get_selectedRules( _role);

		Arrays.sort( rows);
		Arrays.sort( columns);

		int minRow = rows[ 0];
		int minColumn = columns[ 0];

		for ( int row:rows) {
			BlockData<Rule> blockData = new BlockData<Rule>( row - minRow, minColumn);
			for ( int column:columns) {
				Rule rule = get_rule( row, column);
				blockData.add( null == rule ? null : Rule.create( rule));
			}
			selectedRules.add( blockData);
		}

		return true;
	}

	/* (Non Javadoc)
	 * @see soars.application.visualshell.common.menu.basic2.IBasicMenuHandler2#on_cut(java.awt.event.ActionEvent)
	 */
	@Override
	public void on_cut(ActionEvent actionEvent) {
		if ( !is_possible()) {
			on_error_possible();
			return;
		}

		int[] rows = getSelectedRows();
		if ( null == rows || 0 == rows.length)
			return;

		int[] columns = getSelectedColumns();
		if ( null == columns || 0 == columns.length)
			return;

		// TODO 選択領域内が全てnullなら実行しない←必要か？
		if ( is_empty( rows, columns))
			return;

		cut( rows, columns);
	}

	/**
	 * @param rows
	 * @param columns
	 * @return
	 */
	private boolean cut(int[] rows, int[] columns) {
		if ( !copy( rows, columns))
			return false;

		return remove( rows, columns);
	}

	/* (Non Javadoc)
	 * @see soars.application.visualshell.common.menu.basic2.IBasicMenuHandler2#on_paste(java.awt.event.ActionEvent)
	 */
	@Override
	public void on_paste(ActionEvent actionEvent) {
		paste();
	}

	/**
	 * @return
	 */
	private boolean paste() {
		int[] rows = getSelectedRows();
		if ( null == rows || 1 != rows.length)
			return false;

		int[] columns = getSelectedColumns();
		if ( null == columns || 1 != columns.length)
			return false;

		List<BlockData<Rule>> selectedRules = get_selectedRules( _role);
		if ( selectedRules.isEmpty())
			return false;

		if ( !is_correct( rows, columns)) {
			on_error_correct();
			return false;
		}

		// TODO 貼付けても変わらない場合は実行しない←必要か？
		if ( is_same( rows[ 0], columns[ 0]))
			return false;

		List<SetValueUndo> setValueUndoList = new ArrayList<SetValueUndo>();
		List<SetValueUndo> setValueUndoListOr = new ArrayList<SetValueUndo>();
		for ( BlockData<Rule> blockData:selectedRules) {
			int row = rows[ 0] + blockData._row;

			// はみ出している場合のチェック
			if ( getRowCount() <= row)
				continue;

			int column = columns[ 0];

			boolean conditionExists = condition_exists( row, column - 1);

			for ( Rule r:blockData) {
				// はみ出している場合のチェック
				if ( getColumnCount() <= column)
					continue;

				if ( null == r)
					setValueUndoList.add( new SetValueUndo( 0 == column ? "" : null, row, column, this, this));
				else {
					Rule rule = Rule.create( r);
					rule._column = column;

					// 論理和(or)への対応
					if ( !rule._kind.equals( "condition"))
						conditionExists = false;
					else {
						if ( !conditionExists && rule._or)
							rule._or = false;

						conditionExists = true;
					}

					setValueUndoList.add( new SetValueUndo( 0 == column ? rule._value : rule, row, column, this, this));
				}
				++column;
			}

			// 論理和(or)への対応
			get_or_updated_rule( setValueUndoListOr, row, column, conditionExists);
		}

		CustomSetValuesUndo customSetValuesUndo = new CustomSetValuesUndo( setValueUndoList, setValueUndoListOr, this, this);
		SetValuesUndo setValuesUndo = setValuesAt( customSetValuesUndo, setValueUndoList);

		// 貼付けた部分を選択する
		if ( null != setValuesUndo) {
			setRowSelectionInterval( setValuesUndo._rowFrom, setValuesUndo._rowTo);
			setColumnSelectionInterval( setValuesUndo._columnFrom, setValuesUndo._columnTo);
			requestFocus();
			internal_synchronize();
		}

		internal_select();
		set_text_to_activeCellTextField();
		repaint();
		requestFocus();

		return true;
	}

	/* (non-Javadoc)
	 * @see soars.common.utility.swing.table.spread_sheet.SpreadSheetTable#setValuesAt(java.lang.String, soars.common.utility.swing.table.base.undo_redo.cell.SetValuesUndo)
	 */
	@Override
	public void setValuesAt(String kind, SetValuesUndo setValuesUndo) {
		if ( !( setValuesUndo instanceof CustomSetValuesUndo))
			super.setValuesAt(kind, setValuesUndo);
		else {
			// 論理和(or)への対応
			CustomSetValuesUndo customSetValuesUndo = ( CustomSetValuesUndo)setValuesUndo;
			if ( kind.equals( "undo")) {
				for ( SetValueUndo setValueUndo:customSetValuesUndo._setValueUndoListForOr)
					setValueAtDefault(setValueUndo._oldValue, setValueUndo._row, setValueUndo._column);
				super.setValuesAt(kind, customSetValuesUndo);
			} else if ( kind.equals( "redo")) {
				super.setValuesAt(kind, customSetValuesUndo);
				for ( SetValueUndo setValueUndo:customSetValuesUndo._setValueUndoListForOr)
					setValueAtDefault(setValueUndo._newValue, setValueUndo._row, setValueUndo._column);
			}
		}
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.common.menu.basic2.IBasicMenuHandler2#on_select_all(java.awt.event.ActionEvent)
	 */
	@Override
	public void on_select_all(ActionEvent actionEvent) {
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.common.menu.basic2.IBasicMenuHandler2#on_deselect_all(java.awt.event.ActionEvent)
	 */
	@Override
	public void on_deselect_all(ActionEvent actionEvent) {
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.common.menu.basic5.IBasicMenuHandler5#on_insert_right_shift(java.awt.event.ActionEvent)
	 */
	@Override
	public void on_insert_right_shift(ActionEvent actionEvent) {
		int[] rows = getSelectedRows();
		if ( null == rows || 0 == rows.length)
			return;

		int[] columns = getSelectedColumns();
		if ( null == columns || 0 == columns.length)
			return;

		Arrays.sort( rows);
		Arrays.sort( columns);

		if ( 0 == columns[ 0])
			return;

		InsertColumnsBlockUndo insertColumnsBlockUndo = new InsertColumnsBlockUndo( rows[ 0], rows[ rows.length - 1], columns[ 0], columns[ columns.length - 1], RuleManager._defaultRuleTableColumnWidth, this, this);
		insertColumnsBlock( insertColumnsBlockUndo);

		set_text_to_activeCellTextField();

		repaint();
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.common.menu.basic5.IBasicMenuHandler5#on_insert_downward_shift(java.awt.event.ActionEvent)
	 */
	@Override
	public void on_insert_downward_shift(ActionEvent actionEvent) {
		int[] rows = getSelectedRows();
		if ( null == rows || 0 == rows.length)
			return;

		int[] columns = getSelectedColumns();
		if ( null == columns || 0 == columns.length)
			return;

		Arrays.sort( rows);
		Arrays.sort( columns);

		CustomInsertRowsBlockUndo customInsertRowsBlockUndo = new CustomInsertRowsBlockUndo( rows[ 0], rows[ rows.length - 1], columns[ 0], columns[ columns.length - 1], new ArrayList<SetValueUndo>(), this, this);
		insertRowsBlock( customInsertRowsBlockUndo);

		// 論理和(or)への対応
		for ( int row = rows[ 0]; row < getRowCount(); ++row)
			get_or_updated_rules( customInsertRowsBlockUndo._setValueUndoListForOr, row, columns);

		set_text_to_activeCellTextField();

		repaint();
	}

	/* (non-Javadoc)
	 * @see soars.common.utility.swing.table.spread_sheet.SpreadSheetTable#insertRowsBlock(java.lang.String, soars.common.utility.swing.table.base.undo_redo.row.InsertRowsBlockUndo)
	 */
	@Override
	public void insertRowsBlock(String kind, InsertRowsBlockUndo insertRowsBlockUndo) {
		// TODO Auto-generated method stub
		if ( !( insertRowsBlockUndo instanceof CustomInsertRowsBlockUndo))
			super.insertRowsBlock(kind, insertRowsBlockUndo);
		else {
			// 論理和(or)への対応
			CustomInsertRowsBlockUndo customInsertRowsBlockUndo = ( CustomInsertRowsBlockUndo)insertRowsBlockUndo;
			if ( kind.equals( "undo")) {
				for ( SetValueUndo setValueUndo:customInsertRowsBlockUndo._setValueUndoListForOr)
					setValueAtDefault(setValueUndo._oldValue, setValueUndo._row, setValueUndo._column);
				super.insertRowsBlock(kind, customInsertRowsBlockUndo);
			} else if ( kind.equals( "redo")) {
				super.insertRowsBlock(kind, customInsertRowsBlockUndo);
				for ( SetValueUndo setValueUndo:customInsertRowsBlockUndo._setValueUndoListForOr)
					setValueAtDefault(setValueUndo._newValue, setValueUndo._row, setValueUndo._column);
			}
		}
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.common.menu.basic5.IBasicMenuHandler5#on_remove_left_shift(java.awt.event.ActionEvent)
	 */
	@Override
	public void on_remove_left_shift(ActionEvent actionEvent) {
		// TODO Auto-generated method stub
		int[] rows = getSelectedRows();
		if ( null == rows || 0 == rows.length)
			return;

		int[] columns = getSelectedColumns();
		if ( null == columns || 0 == columns.length)
			return;

		Arrays.sort( rows);
		Arrays.sort( columns);

		if ( 0 == columns[ 0])
			return;

		CustomRemoveColumnsBlockUndo customRemoveColumnsBlockUndo = new CustomRemoveColumnsBlockUndo( rows[ 0], rows[ rows.length - 1], columns[ 0], columns[ columns.length - 1], new ArrayList<SetValueUndo>(), this, this);
		removeColumnsBlock( customRemoveColumnsBlockUndo);

		// 論理和(or)への対応
		for ( int row:rows)
			get_or_updated_rules( customRemoveColumnsBlockUndo._setValueUndoListForOr, row, columns);

		set_text_to_activeCellTextField();

		repaint();
	}

	/* (non-Javadoc)
	 * @see soars.common.utility.swing.table.spread_sheet.SpreadSheetTable#removeColumnsBlock(java.lang.String, soars.common.utility.swing.table.base.undo_redo.column.RemoveColumnsBlockUndo)
	 */
	@Override
	public void removeColumnsBlock(String kind, RemoveColumnsBlockUndo removeColumnsBlockUndo) {
		// TODO Auto-generated method stub
		if ( !( removeColumnsBlockUndo instanceof CustomRemoveColumnsBlockUndo))
			super.removeColumnsBlock(kind, removeColumnsBlockUndo);
		else {
			// 論理和(or)への対応
			CustomRemoveColumnsBlockUndo customRemoveColumnsBlockUndo = ( CustomRemoveColumnsBlockUndo)removeColumnsBlockUndo;
			if ( kind.equals( "undo")) {
				for ( SetValueUndo setValueUndo:customRemoveColumnsBlockUndo._setValueUndoListForOr)
					setValueAtDefault(setValueUndo._oldValue, setValueUndo._row, setValueUndo._column);
				super.removeColumnsBlock(kind, customRemoveColumnsBlockUndo);
			} else if ( kind.equals( "redo")) {
				super.removeColumnsBlock(kind, customRemoveColumnsBlockUndo);
				for ( SetValueUndo setValueUndo:customRemoveColumnsBlockUndo._setValueUndoListForOr)
					setValueAtDefault(setValueUndo._newValue, setValueUndo._row, setValueUndo._column);
			}
		}
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.common.menu.basic5.IBasicMenuHandler5#on_remove_upward_shift(java.awt.event.ActionEvent)
	 */
	@Override
	public void on_remove_upward_shift(ActionEvent actionEvent) {
		// TODO Auto-generated method stub
		int[] rows = getSelectedRows();
		if ( null == rows || 0 == rows.length)
			return;

		int[] columns = getSelectedColumns();
		if ( null == columns || 0 == columns.length)
			return;

		Arrays.sort( rows);
		Arrays.sort( columns);

		CustomRemoveRowsBlockUndo customRemoveRowsBlockUndo = new CustomRemoveRowsBlockUndo( rows[ 0], rows[ rows.length - 1], columns[ 0], columns[ columns.length - 1], new ArrayList<SetValueUndo>(), this, this);
		removeRowsBlock( customRemoveRowsBlockUndo);

		set_text_to_activeCellTextField();

		repaint();
	}

	/* (non-Javadoc)
	 * @see soars.common.utility.swing.table.spread_sheet.SpreadSheetTable#removeRowsBlock(java.lang.String, soars.common.utility.swing.table.base.undo_redo.row.RemoveRowsBlockUndo)
	 */
	@Override
	public void removeRowsBlock(String kind, RemoveRowsBlockUndo removeRowsBlockUndo) {
		// TODO Auto-generated method stub
		if ( !( removeRowsBlockUndo instanceof CustomRemoveRowsBlockUndo))
			super.removeRowsBlock(kind, removeRowsBlockUndo);
		else {
			// 論理和(or)への対応
			CustomRemoveRowsBlockUndo customRemoveRowsBlockUndo = ( CustomRemoveRowsBlockUndo)removeRowsBlockUndo;
			if ( kind.equals( "undo")) {
				for ( SetValueUndo setValueUndo:customRemoveRowsBlockUndo._setValueUndoListForOr)
					setValueAtDefault(setValueUndo._oldValue, setValueUndo._row, setValueUndo._column);
				super.removeRowsBlock(kind, customRemoveRowsBlockUndo);
			} else if ( kind.equals( "redo")) {
				super.removeRowsBlock(kind, customRemoveRowsBlockUndo);
				for ( SetValueUndo setValueUndo:customRemoveRowsBlockUndo._setValueUndoListForOr)
					setValueAtDefault(setValueUndo._newValue, setValueUndo._row, setValueUndo._column);
			}
		}
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.common.menu.basic5.IBasicMenuHandler5#on_or(java.awt.event.ActionEvent)
	 */
	@Override
	public void on_or(ActionEvent actionEvent) {
		int[] rows = getSelectedRows();
		if ( null == rows || 1 > rows.length)
			return;

		int[] columns = getSelectedColumns();
		if ( null == columns || 1 > columns.length)
			return;

		Arrays.sort( rows);
		Arrays.sort( columns);

		if ( 2 > columns[ 0])
			return;

		boolean enable = !or_all( rows, columns);

		List<SetValueUndo> setValueUndoList = new ArrayList<SetValueUndo>();
		for ( int row:rows) {
			for ( int column:columns) {
				Rule r = ( Rule)getValueAt( row, column);
				if ( null == r)
					setValueUndoList.add( new SetValueUndo( null, row, column, this, this));
				else {
					Rule rule = Rule.create( r);
					rule._column = column;
					rule._or = enable;
					setValueUndoList.add( new SetValueUndo( rule, row, column, this, this));
				}
			}
		}
		SetValuesUndo setValuesUndo = setValuesAt( setValueUndoList);

		set_text_to_activeCellTextField();
		refresh();
		repaint();
		requestFocus();
	}

	/* (non-Javadoc)
	 * @see soars.common.utility.swing.table.spread_sheet.SpreadSheetTableBase#on_copy_row(int[])
	 */
	@Override
	public void on_copy_row(int[] rows) {
		List<RowData<Rule>> selectedRowRules = get_selectedRowRules( _role);
		for ( int i = 0; i < rows.length; ++i) {
			for ( int column = 0; column < getColumnCount(); ++column) {
				Rule rule = get_rule( rows[ i], column);
				selectedRowRules.get( i).add( null == rule ? null : Rule.create( rule));
			}
		}
	}

	/* (non-Javadoc)
	 * @see soars.common.utility.swing.table.spread_sheet.SpreadSheetTableBase#on_cut_row(int[], java.util.List)
	 */
	@Override
	public void on_cut_row(int[] rows, List<List<SetValueUndo>> rowHeaderSetValueUndoLists) {
		List<List<SetValueUndo>> setValueUndoLists = new ArrayList<List<SetValueUndo>>();
		for ( int row:rows) {
			List<SetValueUndo> setValueUndoList = new ArrayList<SetValueUndo>();
			for ( int column = 0; column < getColumnCount(); ++column)
				setValueUndoList.add( new SetValueUndo( 0 == column ? "" : null, row, column, this, this));
			setValueUndoLists.add( setValueUndoList);
		}
		setValuesAt( rows, setValueUndoLists, rowHeaderSetValueUndoLists);

		set_text_to_activeCellTextField();
		repaint();
	}

	/* (non-Javadoc)
	 * @see soars.common.utility.swing.table.spread_sheet.SpreadSheetTableBase#on_paste_row(int, java.util.List)
	 */
	@Override
	public void on_paste_row(int row, List<List<SetValueUndo>> rowHeaderSetValueUndoLists) {
		List<RowData<Rule>> selectedRowRules = get_selectedRowRules( _role);
		List<Integer> list = new ArrayList<Integer>();
		List<List<SetValueUndo>> setValueUndoLists = new ArrayList<List<SetValueUndo>>();
		for ( int i = 0; i < selectedRowRules.size(); ++i) {
			RowData<Rule> rowData = selectedRowRules.get( i);
			Integer integer = Integer.valueOf( row + rowData._row);

			// はみ出している場合のチェック
			if ( getRowCount() <= integer.intValue())
				continue;

			List<SetValueUndo> setValueUndoList = new ArrayList<SetValueUndo>();
			for ( int column = 0; column < getColumnCount(); ++column) {
				if ( null == rowData.get( column))
					setValueUndoList.add( new SetValueUndo( 0 == column ? "" : null, integer.intValue(), column, this, this));
				else {
					Rule rule = Rule.create( rowData.get( column));
					rule._column = column;
					setValueUndoList.add( new SetValueUndo( 0 == column ? rule._value : rule, integer.intValue(), column, this, this));
				}
			}
			setValueUndoLists.add( setValueUndoList);
			list.add( integer);
		}

		int[] rows = new int[ list.size()];
		for ( int i = 0; i < rows.length; ++i)
			rows[ i] = list.get( i).intValue();

		setValuesAt( rows, setValueUndoLists, rowHeaderSetValueUndoLists);

		set_text_to_activeCellTextField();
		repaint();
	}

	/* (non-Javadoc)
	 * @see soars.common.utility.swing.table.spread_sheet.SpreadSheetTableBase#on_remove_column(int[])
	 */
	@Override
	public void on_remove_column(int[] columns) {
		CustomRemoveColumnsUndo customRemoveColumnsUndo = new CustomRemoveColumnsUndo( columns, RuleManager._defaultRuleTableColumnWidth, new ArrayList<SetValueUndo>(), this, this);
		removeColumns( customRemoveColumnsUndo, columns, RuleManager._defaultRuleTableColumnWidth);

		// 論理和(or)への対応
		for ( int row = 0; row < getRowCount(); ++row)
			get_or_updated_rules( customRemoveColumnsUndo._setValueUndoListForOr, row, columns);

		set_text_to_activeCellTextField();

		repaint();
	}

	/* (non-Javadoc)
	 * @see soars.common.utility.swing.table.spread_sheet.SpreadSheetTable#removeColumns(java.lang.String, soars.common.utility.swing.table.base.undo_redo.column.RemoveColumnsUndo)
	 */
	@Override
	public void removeColumns(String kind, RemoveColumnsUndo removeColumnsUndo) {
		if ( !( removeColumnsUndo instanceof CustomRemoveColumnsUndo))
			super.removeColumns(kind, removeColumnsUndo);
		else {
			// 論理和(or)への対応
			CustomRemoveColumnsUndo customRemoveColumnsUndo = ( CustomRemoveColumnsUndo)removeColumnsUndo;
			if ( kind.equals( "undo")) {
				for ( SetValueUndo setValueUndo:customRemoveColumnsUndo._setValueUndoListForOr)
					setValueAtDefault(setValueUndo._oldValue, setValueUndo._row, setValueUndo._column);
				super.removeColumns(kind, customRemoveColumnsUndo);
			} else if ( kind.equals( "redo")) {
				super.removeColumns(kind, customRemoveColumnsUndo);
				for ( SetValueUndo setValueUndo:customRemoveColumnsUndo._setValueUndoListForOr)
					setValueAtDefault(setValueUndo._newValue, setValueUndo._row, setValueUndo._column);
			}
		}
	}

	/* (non-Javadoc)
	 * @see soars.common.utility.swing.table.spread_sheet.SpreadSheetTableBase#on_copy_column(int[])
	 */
	@Override
	public void on_copy_column(int[] columns) {
		List<ColumnData<Rule>> selectedColumnRules = get_selectedColumnRules( _role);
		for ( int i = 0; i < columns.length; ++i) {
			for ( int row = 0; row < getRowCount(); ++row) {
				Rule rule = get_rule( row, columns[ i]);
				selectedColumnRules.get( i).add( null == rule ? null : Rule.create( rule));
			}
		}
	}

	/* (non-Javadoc)
	 * @see soars.common.utility.swing.table.spread_sheet.SpreadSheetTableBase#on_cut_column(int[])
	 */
	@Override
	public void on_cut_column(int[] columns) {
		List<List<SetValueUndo>> setValueUndoLists = new ArrayList<List<SetValueUndo>>();
		for ( int column:columns) {
			List<SetValueUndo> setValueUndoList = new ArrayList<SetValueUndo>();
			for ( int row = 0; row < getRowCount(); ++row)
				setValueUndoList.add( new SetValueUndo( 0 == column ? "" : null, row, column, this, this));
			setValueUndoLists.add( setValueUndoList);
		}

		CustomColumnsUndo customColumnsUndo = new CustomColumnsUndo( columns, setValueUndoLists, new ArrayList<SetValueUndo>(), this, this);
		setValuesAt( customColumnsUndo, columns, setValueUndoLists);

		// 論理和(or)への対応
		for ( int row = 0; row < getRowCount(); ++row)
			get_or_updated_rules( customColumnsUndo._setValueUndoListForOr, row, columns);

		set_text_to_activeCellTextField();

		repaint();
	}

	/* (non-Javadoc)
	 * @see soars.common.utility.swing.table.spread_sheet.SpreadSheetTableBase#on_paste_column(int)
	 */
	@Override
	public void on_paste_column(int column) {
		List<ColumnData<Rule>> selectedColumnRules = get_selectedColumnRules( _role);
		List<Integer> list = new ArrayList<Integer>();
		List<List<SetValueUndo>> setValueUndoLists = new ArrayList<List<SetValueUndo>>();
		for ( int i = 0; i < selectedColumnRules.size(); ++i) {
			ColumnData<Rule> columnData = selectedColumnRules.get( i);
			Integer integer = Integer.valueOf( column + columnData._column);

			// はみ出している場合のチェック
			if ( getColumnCount() <= integer.intValue())
				continue;

			List<SetValueUndo> setValueUndoList = new ArrayList<SetValueUndo>();
			for ( int row = 0; row < getRowCount(); ++row) {
				if ( null == columnData.get( row))
					setValueUndoList.add( new SetValueUndo( 0 == integer.intValue() ? "" : null, row, integer.intValue(), this, this));
				else {
					Rule rule = Rule.create( columnData.get( row));
					rule._column = column;
					setValueUndoList.add( new SetValueUndo( 0 == integer.intValue() ? rule._value : rule, row, integer.intValue(), this, this));
				}
			}
			setValueUndoLists.add( setValueUndoList);
			list.add( integer);
		}

		int[] columns = new int[ list.size()];
		for ( int i = 0; i < columns.length; ++i)
			columns[ i] = list.get( i).intValue();

		CustomColumnsUndo customColumnsUndo = new CustomColumnsUndo( columns, setValueUndoLists, new ArrayList<SetValueUndo>(), this, this);
		setValuesAt( customColumnsUndo, columns, setValueUndoLists);

		// 論理和(or)への対応
		for ( int row = 0; row < getRowCount(); ++row)
			get_or_updated_rules( customColumnsUndo._setValueUndoListForOr, row, columns);

		set_text_to_activeCellTextField();

		repaint();
	}

	/* (non-Javadoc)
	 * @see soars.common.utility.swing.table.spread_sheet.SpreadSheetTable#setValuesAt(java.lang.String, soars.common.utility.swing.table.base.undo_redo.column.ColumnsUndo)
	 */
	@Override
	public void setValuesAt(String kind, ColumnsUndo columnsUndo) {
		if ( !( columnsUndo instanceof CustomColumnsUndo))
			super.setValuesAt(kind, columnsUndo);
		else {
			// 論理和(or)への対応
			CustomColumnsUndo customColumnsUndo = ( CustomColumnsUndo)columnsUndo;
			if ( kind.equals( "undo")) {
				for ( SetValueUndo setValueUndo:customColumnsUndo._setValueUndoListForOr)
					setValueAtDefault(setValueUndo._oldValue, setValueUndo._row, setValueUndo._column);
				super.setValuesAt(kind, customColumnsUndo);
			} else if ( kind.equals( "redo")) {
				super.setValuesAt(kind, customColumnsUndo);
				for ( SetValueUndo setValueUndo:customColumnsUndo._setValueUndoListForOr)
					setValueAtDefault(setValueUndo._newValue, setValueUndo._row, setValueUndo._column);
			}
		}
	}

	/** 論理和(or)への対応
	 * @param setValueUndoListForOr
	 * @param row
	 * @param columns
	 */
	private void get_or_updated_rules(List<SetValueUndo> setValueUndoListForOr, int row, int[] columns) {
		boolean conditionExists = condition_exists( row, columns[ 0] - 1);

		for ( int column = columns[ 0]; column <= columns[ columns.length - 1]; ++column) {
			if ( 0 == column || getColumnCount() <= column)
				continue;

			Rule r = ( Rule)getValueAt( row, column);
			if ( null == r)
				continue;

			if ( !r._kind.equals( "condition")) {
				conditionExists = false;
				continue;
			}

			if ( !conditionExists && r._or) {
				Rule rule = Rule.create( r);
				rule._or = false;
				setValueUndoListForOr.add( new SetValueUndo( rule, row, column, this, this));
				setValueAtDefault( rule, row, column);
			}

			if ( 0 < column)
				conditionExists = true;
		}

		get_or_updated_rule( setValueUndoListForOr, row, columns[ columns.length - 1] + 1, conditionExists);
	}

	/** 論理和(or)への対応
	 * @param row
	 * @param columnMax
	 * @return
	 */
	private boolean condition_exists(int row, int columnMax) {
		boolean conditionExists = false;
		for ( int column = columnMax; 0 < column; --column) {
			Rule rule = ( Rule)getValueAt( row, column);
			if ( null == rule)
				continue;

			if ( rule._kind.equals( "condition"))
				conditionExists = true;

			break;
		}
		return conditionExists;
	}

	/** 論理和(or)への対応
	 * @param setValueUndoListOr
	 * @param row
	 * @param columnMin
	 * @param conditionExists
	 */
	private void get_or_updated_rule(List<SetValueUndo> setValueUndoListOr, int row, int columnMin, boolean conditionExists) {
		for ( int column = columnMin; column < getColumnCount(); ++column) {
			Rule rule = ( Rule)getValueAt( row, column);
			if ( null == rule)
				continue;

			if ( !rule._kind.equals( "condition"))
				break;

			if ( !conditionExists && rule._or) {
				//Rule r = Rule.create( rule._kind, column, rule._type, rule._value, false);
				Rule r = Rule.create( rule);
				r._or = false;
				setValueUndoListOr.add( new SetValueUndo( r, row, column, this, this));
				setValueAtDefault( r, row, column);
			}

			break;
		}
	}

	/* (non-Javadoc)
	 * @see soars.common.utility.swing.table.spread_sheet.SpreadSheetTableBase#copied(int)
	 */
	@Override
	public boolean copied(int row) {
		return super.copied(row);
	}

	/**
	 * 
	 */
	public void on_update_stage() {
		CommonTool.update( _stageComboBox, StageManager.get_instance().get_names( true));
		_undoManager.discardAllEdits();
		refresh();
	}

	/**
	 * @param newName
	 * @param originalName
	 * @return
	 */
	public boolean update_stage_name(String newName, String originalName) {
		boolean result = false;
		for ( int row = 0; row < getRowCount(); ++row) {
			for ( int column = 0; column < getColumnCount(); ++column) {
				Rule rule = get_rule( row, column);
				if ( null == rule)
					continue;

				if ( rule.update_stage_name( newName, originalName)) {
					set_rule( rule, row, column);
					result = true;
				}
			}
		}

		CommonTool.update( _stageComboBox, StageManager.get_instance().get_names( true));

		_undoManager.discardAllEdits();

		refresh();

		return result;
	}

	/**
	 * @param name
	 * @return
	 */
	public boolean can_remove_stage_name(String name) {
		boolean result = true;
		for ( int row = 0; row < getRowCount(); ++row) {
			for ( int column = 0; column < getColumnCount(); ++column) {
				Rule rule = get_rule( row, column);
				if ( null == rule)
					continue;

				if ( !rule.can_remove_stage_name( name))
					result = false;
			}
		}

		return result;
	}

	/**
	 * @param newExpression
	 * @param newVariableCount
	 * @param originalExpression
	 * @return
	 */
	public boolean update_expression(Expression newExpression, int newVariableCount, Expression originalExpression) {
		// TODO 2014.2.14
		boolean result = false;
		for ( int row = 0; row < getRowCount(); ++row) {
			for ( int column = 0; column < getColumnCount(); ++column) {
				Rule rule = get_rule( row, column);
				if ( null == rule)
					continue;

				if ( rule.update_expression( newExpression, newVariableCount, originalExpression, row, _role)) {
					set_rule( rule, row, column);
					result = true;
				}
			}
		}

		_undoManager.discardAllEdits();

		refresh();

		return result;
	}

	/**
	 * @param expression
	 * @return
	 */
	public boolean can_remove_expression(Expression expression) {
		// TODO 2014.2.14
		boolean result = true;
		for ( int row = 0; row < getRowCount(); ++row) {
			for ( int column = 0; column < getColumnCount(); ++column) {
				Rule rule = get_rule( row, column);
				if ( null == rule)
					continue;

				if ( !rule.can_remove_expression( _role._name, row, expression))
					result = false;
			}
		}

		return result;
	}

	/**
	 * @param ruleManager
	 */
	public void on_ok(RuleManager ruleManager) {
		ruleManager.cleanup();
		ruleManager._columnCount = getColumnCount();
		for ( int row = 0; row < getRowCount(); ++row) {
			Rules rules = new Rules();
			for ( int column = 0; column < getColumnCount(); ++column) {
				Rule rule = get_rule( row, column);
				if ( null == rule)
					continue;

				if ( rule._kind.equals( "condition") && rule._type.equals( ResourceManager.get_instance().get( "rule.type.condition.stage")) && rule._value.equals( ""))
					continue;

				rule._column = column;
				rules.add( rule);
			}

			RuleRowData ruleRowData = ( RuleRowData)_spreadSheetTableBase.getValueAt( row, 0);
			rules._comment = ruleRowData._comment;
			ruleManager.add( rules);
		}

		get_column_widths( ruleManager._columnWidths);
	}

	/* (non-Javadoc)
	 * @see soars.common.utility.swing.text.ITextUndoRedoManagerCallBack#on_changed(javax.swing.undo.UndoManager)
	 */
	@Override
	public void on_changed(UndoManager undoManager) {
	}

	/* (non-Javadoc)
	 * @see soars.common.utility.swing.text.ITextUndoRedoManagerCallBack#on_changed(javax.swing.text.AbstractDocument.DefaultDocumentEvent, javax.swing.undo.UndoManager)
	 */
	@Override
	public void on_changed(DefaultDocumentEvent defaultDocumentEvent,UndoManager undoManager) {
	}
}
