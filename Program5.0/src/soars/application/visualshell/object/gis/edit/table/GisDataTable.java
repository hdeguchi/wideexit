/**
 * 
 */
package soars.application.visualshell.object.gis.edit.table;

import java.awt.Component;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.event.ListSelectionEvent;
import javax.swing.table.DefaultTableColumnModel;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;

import soars.application.visualshell.common.menu.basic1.IBasicMenuHandler1;
import soars.application.visualshell.common.menu.basic1.RemoveAction;
import soars.application.visualshell.common.menu.basic2.CopyAction;
import soars.application.visualshell.common.menu.basic2.CutAction;
import soars.application.visualshell.common.menu.basic2.IBasicMenuHandler2;
import soars.application.visualshell.common.menu.basic2.PasteAction;
import soars.application.visualshell.common.menu.basic2.RedoAction;
import soars.application.visualshell.common.menu.basic2.UndoAction;
import soars.application.visualshell.main.Constant;
import soars.application.visualshell.main.ResourceManager;
import soars.application.visualshell.object.gis.GisData;
import soars.application.visualshell.object.gis.GisDataRecord;
import soars.application.visualshell.object.gis.edit.table.menu.CopyToClipboardAction;
import soars.common.utility.swing.table.base.data.BlockData;
import soars.common.utility.swing.table.base.data.ColumnData;
import soars.common.utility.swing.table.base.data.RowData;
import soars.common.utility.swing.table.base.undo_redo.cell.SetValueUndo;
import soars.common.utility.swing.table.base.undo_redo.cell.SetValuesUndo;
import soars.common.utility.swing.table.spread_sheet.SpreadSheetTable;
import soars.common.utility.swing.table.spread_sheet.SpreadSheetTableBase;
import soars.common.utility.swing.text.TextExcluder;
import soars.common.utility.tool.clipboard.Clipboard;
import soars.common.utility.tool.common.Tool;

/**
 * @author kurata
 *
 */
public class GisDataTable extends SpreadSheetTable implements IBasicMenuHandler1, IBasicMenuHandler2 {

	/**
	 * 
	 */
	private GisData _gisData = null;

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
	private JMenuItem _copyToClipboardMenuItem = null;

	/**
	 * 
	 */
	static public List<BlockData<String>> _selectedBlockData = new ArrayList<BlockData<String>>();

	/**
	 * 
	 */
	static public List<RowData<String>> _selectedRowData = new ArrayList<RowData<String>>();

	/**
	 * 
	 */
	static public List<ColumnData<String>> _selectedColumnData = new ArrayList<ColumnData<String>>();

	/**
	 * 
	 */
	public JTextField _textField = null;

	/**
	 * 
	 */
	static public void refresh() {
		_selectedBlockData.clear();
		_selectedRowData.clear();
		_selectedColumnData.clear();
	}

	/**
	 * @param gisData
	 * @param textField 
	 * @param owner
	 * @param parent
	 */
	public GisDataTable(GisData gisData, JTextField textField, Frame owner, Component parent) {
		super(owner, parent);
		_gisData = gisData;
		_textField = textField;
	}

	/* (non-Javadoc)
	 * @see soars.common.utility.swing.table.spread_sheet.SpreadSheetTable#setup(soars.common.utility.swing.table.spread_sheet.SpreadSheetTableBase, boolean)
	 */
	public boolean setup(SpreadSheetTableBase spreadSheetTableBase, boolean popupMenu) {
		if (!super.setup(spreadSheetTableBase, popupMenu))
			return false;

		GisDataTableHeader gisDataTableHeader = new GisDataTableHeader( this, getColumnModel(), _gisData, _owner, _parent);
		if ( !gisDataTableHeader.setup( new GisDataTableHeaderRenderer( _gisData), true/*false*/))
			return false;

		getTableHeader().setReorderingAllowed( false);
		setDefaultEditor( Object.class, null);
		putClientProperty( "JTable.autoStartsEdit", Boolean.FALSE);	// キー入力でセルが編集モードにならないようにする
		setFillsViewportHeight( true);

		int columns = ( _gisData._names.size() + 2);

		DefaultTableModel defaultTableModel = ( DefaultTableModel)getModel();
		defaultTableModel.setColumnCount( columns);

		DefaultTableColumnModel defaultTableColumnModel = ( DefaultTableColumnModel)getColumnModel();
		for ( int i = 0; i < _gisData._names.size(); ++i) {
			defaultTableColumnModel.getColumn( i).setHeaderValue( _gisData._names.get( i));
			defaultTableColumnModel.getColumn( i).setPreferredWidth( 100);
		}

		defaultTableColumnModel.getColumn( columns - 2).setHeaderValue( "X coordinate");
		defaultTableColumnModel.getColumn( columns - 2).setPreferredWidth( 150);

		defaultTableColumnModel.getColumn( columns - 1).setHeaderValue( "Y coordinate");
		defaultTableColumnModel.getColumn( columns - 1).setPreferredWidth( 150);

		for ( int i = 0; i < columns; ++i) {
			TableColumn tableColumn = defaultTableColumnModel.getColumn( i);
			tableColumn.setCellRenderer( new GisDataTableCellRenderer( _gisData));
		}

		for ( GisDataRecord gisDataRecord:_gisData)
			addRow( ( String[])gisDataRecord.toArray( new String[ 0]));

		setup_undo_redo_manager( gisDataTableHeader);

		return true;
	}

//	/* (non-Javadoc)
//	 * @see soars.common.utility.swing.table.spread_sheet.SpreadSheetTableBase#setup_key_event()
//	 */
//	protected void setup_key_event() {
//		// なにもしないことでJTableのデフォルト動作をさせる
//	}

	/* (non-Javadoc)
	 * @see soars.common.utility.swing.table.spread_sheet.SpreadSheetTableBase#valueChanged(javax.swing.event.ListSelectionEvent)
	 */
	public void valueChanged(ListSelectionEvent e) {
		super.valueChanged(e);
		set_text_to_textField();
	}

	/* (non-Javadoc)
	 * @see soars.common.utility.swing.table.spread_sheet.SpreadSheetTableBase#changeSelection(int, int, boolean, boolean)
	 */
	public void changeSelection(int rowIndex, int columnIndex, boolean toggle, boolean extend) {
		super.changeSelection(rowIndex, columnIndex, toggle, extend);
		set_text_to_textField();
	}

	/* (non-Javadoc)
	 * @see soars.common.utility.swing.table.spread_sheet.SpreadSheetTable#setValueAt(java.lang.String, soars.common.utility.swing.table.base.undo_redo.cell.SetValueUndo)
	 */
	public void setValueAt(String kind, SetValueUndo setValueUndo) {
		if ( setValueUndo._table instanceof GisDataRowHeaderTable) {
			if ( kind.equals( "undo"))
				_spreadSheetTableBase.setValueAtDefault( setValueUndo._oldValue, setValueUndo._row, setValueUndo._column);
			else if ( kind.equals( "redo"))
				_spreadSheetTableBase.setValueAtDefault(setValueUndo._newValue, setValueUndo._row, setValueUndo._column);
			return;
		}
		super.setValueAt(kind, setValueUndo);
	}

	/* (non-Javadoc)
	 * @see soars.common.utility.swing.table.base.StandardTable#setup_popup_menu()
	 */
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

		_copyToClipboardMenuItem = _userInterface.append_popup_menuitem(
			_popupMenu,
			ResourceManager.get_instance().get( "gis.table.popup.menu.copy.to.clipboard.menu"),
			new CopyToClipboardAction( ResourceManager.get_instance().get( "gis.table.popup.menu.copy.to.clipboard.menu"), this),
			ResourceManager.get_instance().get( "gis.table.popup.menu.copy.to.clipboard.mnemonic"),
			ResourceManager.get_instance().get( "gis.table.popup.menu.copy.to.clipboard.stroke"));
	}

	/* (non-Javadoc)
	 * @see soars.common.utility.swing.table.spread_sheet.SpreadSheetTable#on_mouse_right_up(int, int, java.awt.event.MouseEvent)
	 */
	protected boolean on_mouse_right_up(int row, int column, MouseEvent mouseEvent) {
		if (!super.on_mouse_right_up(row, column, mouseEvent))
			return false;

		if ( !is_possible())
			return false;

		_undoMenuItem.setEnabled( false);
		_redoMenuItem.setEnabled( false);

		_removeMenuItem.setEnabled( false);

		_copyMenuItem.setEnabled( false);
		_cutMenuItem.setEnabled( false);
		_pasteMenuItem.setEnabled( false);

		_copyToClipboardMenuItem.setEnabled( false);

		if ( getRowCount() > 0) {
			int[] rows = getSelectedRows();
			int[] columns = getSelectedColumns();

			Arrays.sort( rows);
			Arrays.sort( columns);

			setup_undoMenuItem( rows, columns);
			setup_redoMenuItem( rows, columns);

			setup_removeMenuItem( rows, columns);

			setup_copyMenuItem( rows, columns);
			setup_cutMenuItem( rows, columns);
			setup_pasteMenuItem( rows, columns);

			setup_copyToClipboardMenuItem( rows, columns);
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
	private void setup_removeMenuItem(int[] rows, int[] columns) {
		// 選択領域内が全てnullでないこと
		_removeMenuItem.setEnabled( !is_empty( rows, columns) && is_correct_on_copy_or_remove( rows, columns));
	}

	/**
	 * @param rows
	 * @param columns
	 */
	private void setup_copyMenuItem(int[] rows, int[] columns) {
		_copyMenuItem.setEnabled( is_correct_on_copy_or_remove( rows, columns));
	}

	/**
	 * @param rows
	 * @param columns
	 */
	private void setup_cutMenuItem(int[] rows, int[] columns) {
		// 選択領域内が全てnullでないこと
		_cutMenuItem.setEnabled( !is_empty( rows, columns) && is_correct_on_copy_or_remove( rows, columns));
	}

	/**
	 * @param rows
	 * @param columns
	 */
	protected void setup_pasteMenuItem(int[] rows, int[] columns) {
		// １つのセルだけが選択されていること
		// バッファが空ではないこと
		// バッファの内容を貼付けても座標が変更されないこと
		// 貼付けを行ったら内容が変わること
		_pasteMenuItem.setEnabled( 1 == rows.length && 1 == columns.length
			&& !_selectedBlockData.isEmpty() && is_correct_on_paste( rows, columns) && !is_same( rows[ 0], columns[ 0]));
	}

	/**
	 * @param rows
	 * @param columns
	 */
	private void setup_copyToClipboardMenuItem(int[] rows, int[] columns) {
		_copyToClipboardMenuItem.setEnabled( 0 < rows.length && 0 < columns.length);
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
			ResourceManager.get_instance().get( "edit.gis.data.dialog.table.possible.error.message"),
			ResourceManager.get_instance().get( "edit.gis.data.dialog.title"),
			JOptionPane.ERROR_MESSAGE);
	}

	/**
	 * @param rows
	 * @param columns
	 * @return
	 */
	private boolean is_correct_on_copy_or_remove(int[] rows, int[] columns) {
		// 選択領域に座標が含まれていたらfalseを返す
		Arrays.sort( columns);
		return ( columns[ 0] - 1 + columns.length < getColumnCount() - 2);
	}

	/**
	 * 
	 */
	private void on_error_correct_on_copy_or_remove() {
		// 選択領域に座標が含まれている場合のエラーメッセージ
		JOptionPane.showMessageDialog( _parent,
			ResourceManager.get_instance().get( "edit.gis.data.dialog.table.correct.on.remove.error.message"),
			ResourceManager.get_instance().get( "edit.gis.data.dialog.title"),
			JOptionPane.ERROR_MESSAGE);
	}

	/**
	 * @param rows
	 * @param columns
	 * @return
	 */
	private boolean is_correct_on_paste(int[] rows, int[] columns) {
		// バッファの内容を貼り付けると座標が変更されてしまう場合にfalseを返す
		// バッファの内容と貼り付ける場所の型が異なる場合にfalseを返す
		if ( _selectedBlockData.isEmpty())
			return false;

		if ( columns[ 0] - 1 + _selectedBlockData.get( 0).size() >= getColumnCount() - 2)
			return false;

		for ( int i = 0; i < _selectedBlockData.get( 0).size() && i < getColumnCount() - 2; ++i) {
			if ( !is_type_equal( _gisData._classes.get( columns[ 0] + i), _gisData._classes.get( _selectedBlockData.get( 0)._column + i)))
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
	 * 
	 */
	private void on_error_correct_on_paste() {
		// バッファの内容を貼り付けると座標が変更されてしまう場合のエラーメッセージ
		JOptionPane.showMessageDialog( _parent,
			ResourceManager.get_instance().get( "edit.gis.data.dialog.table.correct.on.paste.error.message"),
			ResourceManager.get_instance().get( "edit.gis.data.dialog.title"),
			JOptionPane.ERROR_MESSAGE);
	}

	/**
	 * @param rows
	 * @param columns
	 * @return
	 */
	private boolean is_empty(int[] rows, int[] columns) {
		// 選択領域内が全てnullならtrueを返す←必要か？
		return false;
	}

	/* (non-Javadoc)
	 * @see soars.common.utility.swing.table.spread_sheet.SpreadSheetTableBase#on_enter(java.awt.event.ActionEvent)
	 */
	public void on_enter(ActionEvent actionEvent) {
		int[] rows = getSelectedRows();
		int[] columns = getSelectedColumns();
		if ( null == rows || 1 != rows.length || null == columns || 1 != columns.length || columns[ 0] >= getColumnCount() - 2)
			return;

		//editCellAt(rows[ 0], columns[ 0]);
		_textField.requestFocus( true);
	}

	/* (non-Javadoc)
	 * @see soars.common.utility.swing.table.spread_sheet.SpreadSheetTableBase#on_escape(java.awt.event.ActionEvent)
	 */
	public void on_escape(ActionEvent actionEvent) {
		stop_cell_editing();
		refresh();
	}

	/**
	 * @param row
	 * @param column
	 * @return
	 */
	private boolean is_same(int row, int column) {
		// バッファと選択領域の内容が同じならtrueを返す←必要か？
		return false;
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.common.menu.basic1.IBasicMenuHandler1#on_edit(java.awt.event.ActionEvent)
	 */
	public void on_edit(ActionEvent actionEvent) {
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.common.menu.basic1.IBasicMenuHandler1#on_append(java.awt.event.ActionEvent)
	 */
	public void on_append(ActionEvent actionEvent) {
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.common.menu.basic1.IBasicMenuHandler1#on_insert(java.awt.event.ActionEvent)
	 */
	public void on_insert(ActionEvent actionEvent) {
	}

	/* (Non Javadoc)
	 * @see soars.application.visualshell.common.menu.basic1.IBasicMenuHandler1#on_remove(java.awt.event.ActionEvent)
	 */
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

		// 選択領域に座標が含まれていたら実行しない
		if ( !is_correct_on_copy_or_remove( rows, columns)) {
			on_error_correct_on_copy_or_remove();
			return;
		}

		// 選択領域内が全てnullなら実行しない←必要か？
		if ( is_empty( rows, columns))
			return;

		if ( JOptionPane.YES_OPTION == JOptionPane.showConfirmDialog(
			_parent,
			ResourceManager.get_instance().get( "edit.gis.data.dialog.table.confirm.remove.message"),
			ResourceManager.get_instance().get( "edit.gis.data.dialog.title"),
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
				setValueUndoList.add( new SetValueUndo( "", row, column, this, this));
		}
		SetValuesUndo setValuesUndo = new SetValuesUndo( setValueUndoList, this, this);
		setValuesAt( setValuesUndo, setValueUndoList);

		//internal_select();
		set_text_to_textField();
		repaint();
		requestFocus();
		synchronize();

		return true;
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.common.menu.basic2.IBasicMenuHandler2#on_undo(java.awt.event.ActionEvent)
	 */
	public void on_undo(ActionEvent actionEvent) {
		on_undo();
	}

	/* (non-Javadoc)
	 * @see soars.common.utility.swing.table.spread_sheet.SpreadSheetTable#on_undo()
	 */
	public void on_undo() {
		super.on_undo();
		//internal_select();
		set_text_to_textField();
		refresh();
		repaint();
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.common.menu.basic2.IBasicMenuHandler2#on_redo(java.awt.event.ActionEvent)
	 */
	public void on_redo(ActionEvent actionEvent) {
		on_redo();
	}

	/* (non-Javadoc)
	 * @see soars.common.utility.swing.table.spread_sheet.SpreadSheetTable#on_redo()
	 */
	public void on_redo() {
		super.on_redo();
		//internal_select();
		set_text_to_textField();
		refresh();
		repaint();
	}

	/* (Non Javadoc)
	 * @see soars.application.visualshell.common.menu.basic2.IBasicMenuHandler2#on_copy(java.awt.event.ActionEvent)
	 */
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

		// 選択領域に座標が含まれていたら実行しない
		if ( !is_correct_on_copy_or_remove( rows, columns)) {
			on_error_correct_on_copy_or_remove();
			return;
		}

		copy( rows, columns);
	}

	/**
	 * @param rows 
	 * @param columns 
	 * @return
	 */
	private boolean copy(int[] rows, int[] columns) {
		refresh();

		Arrays.sort( rows);
		Arrays.sort( columns);

		int minRow = rows[ 0];
		int minColumn = columns[ 0];

		for ( int row:rows) {
			BlockData<String> blockData = new BlockData<String>( row - minRow, minColumn);
			for ( int column:columns)
				blockData.add( ( String)getValueAt( row, column));

			_selectedBlockData.add( blockData);
		}

		return true;
	}

	/* (Non Javadoc)
	 * @see soars.application.visualshell.common.menu.basic2.IBasicMenuHandler2#on_cut(java.awt.event.ActionEvent)
	 */
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

		// 選択領域に座標が含まれていたら実行しない
		if ( !is_correct_on_copy_or_remove( rows, columns)) {
			on_error_correct_on_copy_or_remove();
			return;
		}

		// 選択領域内が全てnullなら実行しない←必要か？
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

		if ( _selectedBlockData.isEmpty())
			return false;

		if ( !is_correct_on_paste( rows, columns)) {
			on_error_correct_on_paste();
			return false;
		}

		// 貼付けても変わらない場合は実行しない←必要か？
		if ( is_same( rows[ 0], columns[ 0]))
			return false;

		List<SetValueUndo> setValueUndoList = new ArrayList<SetValueUndo>();
		List<SetValueUndo> setValueUndoListOr = new ArrayList<SetValueUndo>();
		for ( BlockData<String> blockData:_selectedBlockData) {
			int row = rows[ 0] + blockData._row;

			// はみ出している場合のチェック
			if ( getRowCount() <= row)
				continue;

			int column = columns[ 0];

			for ( String value:blockData) {
				// はみ出している場合のチェック
				if ( getColumnCount() <= column)
					continue;

				setValueUndoList.add( new SetValueUndo( value, row, column, this, this));

				++column;
			}
		}

		SetValuesUndo setValuesUndo = setValuesAt( new SetValuesUndo( setValueUndoList, this, this), setValueUndoList);

		// 貼付けた部分を選択する
		if ( null != setValuesUndo) {
			setRowSelectionInterval( setValuesUndo._rowFrom, setValuesUndo._rowTo);
			setColumnSelectionInterval( setValuesUndo._columnFrom, setValuesUndo._columnTo);
			requestFocus();
			internal_synchronize();
		}

		//internal_select();
		set_text_to_textField();
		repaint();
		requestFocus();

		return true;
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.common.menu.basic2.IBasicMenuHandler2#on_select_all(java.awt.event.ActionEvent)
	 */
	public void on_select_all(ActionEvent actionEvent) {
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.common.menu.basic2.IBasicMenuHandler2#on_deselect_all(java.awt.event.ActionEvent)
	 */
	public void on_deselect_all(ActionEvent actionEvent) {
	}

	/* (non-Javadoc)
	 * @see soars.common.utility.swing.table.spread_sheet.SpreadSheetTableBase#on_copy_row(int[])
	 */
	public void on_copy_row(int[] rows) {
		for ( int i = 0; i < rows.length; ++i) {
			for ( int column = 0; column < getColumnCount() - 2; ++column)
				_selectedRowData.get( i).add( ( String)getValueAt( rows[ i], column));
		}
	}

	/* (non-Javadoc)
	 * @see soars.common.utility.swing.table.spread_sheet.SpreadSheetTableBase#on_cut_row(int[], java.util.List)
	 */
	public void on_cut_row(int[] rows, List<List<SetValueUndo>> rowHeaderSetValueUndoLists) {
		List<List<SetValueUndo>> setValueUndoLists = new ArrayList<List<SetValueUndo>>();
		for ( int row:rows) {
			List<SetValueUndo> setValueUndoList = new ArrayList<SetValueUndo>();
			for ( int column = 0; column < getColumnCount() - 2; ++column)
				setValueUndoList.add( new SetValueUndo( "", row, column, this, this));
			setValueUndoLists.add( setValueUndoList);
		}
		setValuesAt( rows, setValueUndoLists, rowHeaderSetValueUndoLists);

		set_text_to_textField();
		repaint();
	}

	/* (non-Javadoc)
	 * @see soars.common.utility.swing.table.spread_sheet.SpreadSheetTableBase#on_paste_row(int, java.util.List)
	 */
	public void on_paste_row(int row, List<List<SetValueUndo>> rowHeaderSetValueUndoLists) {
		List<Integer> list = new ArrayList<Integer>();
		List<List<SetValueUndo>> setValueUndoLists = new ArrayList<List<SetValueUndo>>();
		for ( int i = 0; i < _selectedRowData.size(); ++i) {
			RowData<String> rowData = _selectedRowData.get( i);
			Integer integer = Integer.valueOf( row + rowData._row);

			// はみ出している場合のチェック
			if ( getRowCount() <= integer.intValue())
				continue;

			List<SetValueUndo> setValueUndoList = new ArrayList<SetValueUndo>();
			for ( int column = 0; column < getColumnCount() - 2; ++column)
				setValueUndoList.add( new SetValueUndo( rowData.get( column), integer.intValue(), column, this, this));

			setValueUndoLists.add( setValueUndoList);
			list.add( integer);
		}

		int[] rows = new int[ list.size()];
		for ( int i = 0; i < rows.length; ++i)
			rows[ i] = list.get( i).intValue();

		setValuesAt( rows, setValueUndoLists, rowHeaderSetValueUndoLists);

		set_text_to_textField();
		repaint();
	}

	/* (non-Javadoc)
	 * @see soars.common.utility.swing.table.spread_sheet.SpreadSheetTableBase#on_copy_column(int[])
	 */
	public void on_copy_column(int[] columns) {
		for ( int i = 0; i < columns.length; ++i) {
			for ( int row = 0; row < getRowCount(); ++row)
				_selectedColumnData.get( i).add( ( String)getValueAt( row, columns[ i]));
		}
	}

	/* (non-Javadoc)
	 * @see soars.common.utility.swing.table.spread_sheet.SpreadSheetTableBase#on_cut_column(int[])
	 */
	public void on_cut_column(int[] columns) {
		List<List<SetValueUndo>> setValueUndoLists = new ArrayList<List<SetValueUndo>>();
		for ( int column:columns) {
			List<SetValueUndo> setValueUndoList = new ArrayList<SetValueUndo>();
			for ( int row = 0; row < getRowCount(); ++row)
				setValueUndoList.add( new SetValueUndo( "", row, column, this, this));
			setValueUndoLists.add( setValueUndoList);
		}
		setValuesAt( columns, setValueUndoLists);

		set_text_to_textField();
		repaint();
	}

	/* (non-Javadoc)
	 * @see soars.common.utility.swing.table.spread_sheet.SpreadSheetTableBase#on_paste_column(int)
	 */
	public void on_paste_column(int column) {
		List<Integer> list = new ArrayList<Integer>();
		List<List<SetValueUndo>> setValueUndoLists = new ArrayList<List<SetValueUndo>>();
		int start = _selectedColumnData.get( 0)._column;
		for ( int i = 0; i < _selectedColumnData.size(); ++i) {
			ColumnData columnData = _selectedColumnData.get( i);
			Integer integer = Integer.valueOf( column + columnData._column - start);

			// はみ出している場合のチェック
			if ( getColumnCount() <= integer.intValue())
				continue;

			List<SetValueUndo> setValueUndoList = new ArrayList<SetValueUndo>();
			for ( int row = 0; row < getRowCount(); ++row)
				setValueUndoList.add( new SetValueUndo( columnData.get( row), row, integer.intValue(), this, this));
			setValueUndoLists.add( setValueUndoList);
			list.add( integer);
		}

		int[] columns = new int[ list.size()];
		for ( int i = 0; i < columns.length; ++i)
			columns[ i] = list.get( i).intValue();

		setValuesAt( columns, setValueUndoLists);

		set_text_to_textField();
		repaint();
	}

	/**
	 * 
	 */
	public void set_text_to_textField() {
		int[] rows = getSelectedRows();
		int[] columns = getSelectedColumns();

		if ( 0 == getRowCount() || null == rows || 1 != rows.length || 0 > rows[ 0]) {
			_textField.setText( "");
			_textField.setEditable( false);
			return;
		}

		if ( null == columns || 1 != columns.length || 0 > columns[ 0] || columns[ 0] >= getColumnCount() - 2) {
			_textField.setText( "");
			_textField.setEditable( false);
			return;
		}

		if ( _gisData._classes.get( columns[ 0]).equals( String.class))
			_textField.setDocument( new TextExcluder( Constant._prohibitedCharacters3));
		else if ( _gisData._classes.get( columns[ 0]).equals( Integer.class)
			|| _gisData._classes.get( columns[ 0]).equals( Long.class)
			|| _gisData._classes.get( columns[ 0]).equals( Short.class))
			_textField.setDocument( new TextExcluder( Constant._prohibitedCharacters5));
		else if ( _gisData._classes.get( columns[ 0]).equals( Double.class)
			|| _gisData._classes.get( columns[ 0]).equals( Float.class))
			_textField.setDocument( new TextExcluder( Constant._prohibitedCharacters5));
		else {
			_textField.setText( "");
			_textField.setEditable( false);
			return;
		}

		_textField.setText( ( String)getValueAt( rows[ 0], columns[ 0]));
		_textField.setEditable( true);
	}

	/**
	 * @param actionEvent
	 */
	public void set_text_to_cell(ActionEvent actionEvent) {
		int[] rows = getSelectedRows();
		int[] columns = getSelectedColumns();

		if ( 0 == getRowCount() || null == rows || 1 != rows.length || 0 > rows[ 0])
			return;

		if ( null == columns || 1 != columns.length || 0 > columns[ 0] || getColumnCount() - 2 <= columns[ 0])
			return;

		String value = _textField.getText();
		if ( _gisData._classes.get( columns[ 0]).equals( String.class)) {
			if ( !Constant.is_correct_keyword_initial_value( value)) {
				JOptionPane.showMessageDialog( _parent,
					ResourceManager.get_instance().get( "edit.gis.data.dialog.invalid.keyword.initial.value.error.message") + " - \"" + value + "\"",
					ResourceManager.get_instance().get( "edit.gis.data.dialog.title"),
					JOptionPane.ERROR_MESSAGE);
				return;
			}
		} else if ( _gisData._classes.get( columns[ 0]).equals( Integer.class)
				|| _gisData._classes.get( columns[ 0]).equals( Long.class)
				|| _gisData._classes.get( columns[ 0]).equals( Short.class)) {
			value = Constant.is_correct_number_variable_initial_value( "integer", value);
			if ( null == value) {
				JOptionPane.showMessageDialog( _parent,
					ResourceManager.get_instance().get( "edit.gis.data.dialog.invalid.number.variable.initial.value.error.message") + " - \"" + _textField.getText() + "\"",
					ResourceManager.get_instance().get( "edit.gis.data.dialog.title"),
					JOptionPane.ERROR_MESSAGE);
				return;
			}
		} else if ( _gisData._classes.get( columns[ 0]).equals( Double.class)
				|| _gisData._classes.get( columns[ 0]).equals( Float.class)) {
			value = Constant.is_correct_number_variable_initial_value( "real number", value);
			if ( null == value) {
				JOptionPane.showMessageDialog( _parent,
					ResourceManager.get_instance().get( "edit.gis.data.dialog.invalid.number.variable.initial.value.error.message") + " - \"" + _textField.getText() + "\"",
					ResourceManager.get_instance().get( "edit.gis.data.dialog.title"),
					JOptionPane.ERROR_MESSAGE);
				return;
			}
		} else
			return;

		setValueAt( value, rows[ 0], columns[ 0]);
		requestFocus();
	}

	/**
	 * @param actionEvent
	 */
	public void restore_text_to_cell(ActionEvent actionEvent) {
		set_text_to_textField();
		requestFocus();
	}

	/**
	 * @param actionEvent
	 */
	public void on_copy_to_clipboard(ActionEvent actionEvent) {
		int[] rows = getSelectedRows();
		if ( null == rows || 1 > rows.length)
			return;

		int[] columns = getSelectedColumns();
		if ( null == columns || 1 > columns.length)
			return;

		Arrays.sort( rows);
		Arrays.sort( columns);

		String text = "";
		for ( int row:rows) {
			String line = "";
			for ( int column:columns)
				line += ( ( line.equals( "") ? "" : "\t") + ( String)getValueAt( row, column));
			text += ( line + "\n");
		}
		Clipboard.set( text);
	}
}
