/**
 * 
 */
package soars.application.manager.model.main.panel.tab.contents;

import java.awt.Component;
import java.awt.Frame;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.Vector;

import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.event.ListSelectionEvent;
import javax.swing.table.DefaultTableColumnModel;
import javax.swing.table.TableColumn;
import javax.swing.text.AbstractDocument.DefaultDocumentEvent;
import javax.swing.undo.UndoManager;

import soars.application.manager.model.executor.Animator;
import soars.application.manager.model.executor.Simulator;
import soars.application.manager.model.main.Constant;
import soars.application.manager.model.main.ResourceManager;
import soars.application.manager.model.main.panel.tab.contents.menu.RemoveAction;
import soars.application.manager.model.main.panel.tab.contents.menu.RunAction;
import soars.application.manager.model.main.panel.tree.ModelTree;
import soars.application.manager.model.main.panel.tree.property.Cell;
import soars.application.manager.model.main.panel.tree.property.TableSelection;
import soars.common.soars.property.Property;
import soars.common.utility.swing.table.spread_sheet.SpreadSheetTable;
import soars.common.utility.swing.table.spread_sheet.SpreadSheetTableBase;
import soars.common.utility.swing.text.ITextUndoRedoManagerCallBack;
import soars.common.utility.swing.text.TextUndoRedoManager;
import soars.common.utility.tool.file.ZipUtility;

/**
 * @author kurata
 *
 */
public class SoarsContentsTable extends SpreadSheetTable implements ITextUndoRedoManagerCallBack {

	/**
	 * 
	 */
	private ModelTree _modelTree = null;

	/**
	 * 
	 */
	private JTextField _titleTextField = null;

	/**
	 * 
	 */
	private JTextArea _commentTextArea = null;

	/**
	 * 
	 */
	private JMenuItem _runMenuItem = null;

	/**
	 * 
	 */
	private JMenuItem _removeMenuItem = null;

	/**
	 * 
	 */
	private List<TextUndoRedoManager> _textUndoRedoManagers = new ArrayList<TextUndoRedoManager>();

	/**
	 * 
	 */
	private TreeMap<String, Property> _propertyMap = new TreeMap<String, Property>();

	/**
	 * 
	 */
	private boolean _reverse = true;

	/**
	 * @param modelTree 
	 * @param titleTextField
	 * @param commentTextArea
	 * @param owner
	 * @param parent
	 */
	public SoarsContentsTable(ModelTree modelTree, JTextField titleTextField, JTextArea commentTextArea, Frame owner, Component parent) {
		super(owner, parent);
		_modelTree = modelTree;
		_titleTextField = titleTextField;
		_commentTextArea = commentTextArea;
	}

	/**
	 * @param spreadSheetTableBase
	 * @return
	 */
	public boolean setup(SpreadSheetTableBase spreadSheetTableBase) {
		if (!super.setup(spreadSheetTableBase, true))
			return false;

		setAutoResizeMode( AUTO_RESIZE_ALL_COLUMNS);
		setCellSelectionEnabled( true);

		getTableHeader().setReorderingAllowed( false);
		setDefaultEditor( Object.class, null);
		putClientProperty( "JTable.autoStartsEdit", Boolean.FALSE);	// キー入力でセルが編集モードにならないようにする

		SoarsContentsTableHeader soarsContentsTableHeader = new SoarsContentsTableHeader( getColumnModel(), _owner, _parent);
		if ( !soarsContentsTableHeader.setup( this, new SoarsContentsTableHeaderRenderer(), false))
			return false;

		setColumnCount( 2);

		if ( !setup_column( null))
			return false;

		setup_key_event();

		return true;
	}

	/* (non-Javadoc)
	 * @see soars.common.utility.swing.table.base.StandardTable#setup_column(java.util.Vector)
	 */
	public boolean setup_column(Vector<Integer> columnWidths) {
		DefaultTableColumnModel defaultTableColumnModel = ( DefaultTableColumnModel)getColumnModel();

		defaultTableColumnModel.getColumn( 0).setHeaderValue( ResourceManager.get_instance().get( "soars.contents.table.header.simulation"));
		defaultTableColumnModel.getColumn( 1).setHeaderValue( ResourceManager.get_instance().get( "soars.contents.table.header.animation"));

		for ( int i = 0; i < defaultTableColumnModel.getColumnCount(); ++i) {
			TableColumn tableColumn = defaultTableColumnModel.getColumn( i);
			tableColumn.setCellRenderer( new SoarsContentsTableCellRenderer());
		}

		return true;
	}

	/* (non-Javadoc)
	 * @see soars.common.utility.swing.table.base.StandardTable#setup_popup_menu()
	 */
	protected void setup_popup_menu() {
		super.setup_popup_menu();

		_runMenuItem = _userInterface.append_popup_menuitem(
			_popupMenu,
			ResourceManager.get_instance().get( "soars.contents.table.popup.menu.run.menu"),
			new RunAction( ResourceManager.get_instance().get( "soars.contents.table.popup.menu.run.menu"), this),
			ResourceManager.get_instance().get( "soars.contents.table.popup.menu.run.mnemonic"),
			ResourceManager.get_instance().get( "soars.contents.table.popup.menu.run.stroke"));

		_popupMenu.addSeparator();

		_removeMenuItem = _userInterface.append_popup_menuitem(
			_popupMenu,
			ResourceManager.get_instance().get( "soars.contents.table.popup.menu.remove.menu"),
			new RemoveAction( ResourceManager.get_instance().get( "soars.contents.table.popup.menu.remove.menu"), this),
			ResourceManager.get_instance().get( "soars.contents.table.popup.menu.remove.mnemonic"),
			ResourceManager.get_instance().get( "soars.contents.table.popup.menu.remove.stroke"));

	}

	/* (non-Javadoc)
	 * @see soars.common.utility.swing.table.spread_sheet.SpreadSheetTable#on_mouse_right_up(int, int, java.awt.event.MouseEvent)
	 */
	protected boolean on_mouse_right_up(int row, int column, MouseEvent mouseEvent) {
		if (!super.on_mouse_right_up(row, column, mouseEvent))
			return false;

		_runMenuItem.setEnabled( false);
		_removeMenuItem.setEnabled( false);

		if ( getRowCount() > 0) {
			int[] rows = getSelectedRows();
			int[] columns = getSelectedColumns();

			setup_runMenuItem( row, column, rows, columns);
			setup_removeMenuItem( row, column, rows, columns);
		}

		_popupMenu.show( this, mouseEvent.getX(), mouseEvent.getY());

		return true;
	}

	/**
	 * @param row
	 * @param column
	 * @param rows
	 * @param columns
	 */
	private void setup_runMenuItem(int row, int column, int[] rows, int[] columns) {
		Arrays.sort( rows);
		Arrays.sort( columns);

		if ( 0 > Arrays.binarySearch( rows, row))
			return;

		if ( 0 > Arrays.binarySearch( columns, column))
			return;

		if ( null == get_property( rows, columns))
			return;

		_runMenuItem.setEnabled( true);
	}

	/**
	 * @param rows
	 * @param columns
	 * @return
	 */
	private Property get_property(int[] rows, int[] columns) {
		if ( 1 != rows.length || 1 != columns.length)
			return null;

		Property property = ( Property)getValueAt( rows[ 0], columns[ 0]);
		if ( null == property)
			return null;

		return property;
	}

	/**
	 * @param row
	 * @param column
	 * @param rows
	 * @param columns
	 */
	private void setup_removeMenuItem(int row, int column, int[] rows, int[] columns) {
		Arrays.sort( rows);
		Arrays.sort( columns);

		if ( 0 > Arrays.binarySearch( rows, row))
			return;

		if ( 0 > Arrays.binarySearch( columns, column))
			return;

		if ( null == get_properties( rows, columns, false))
			return;

		_removeMenuItem.setEnabled( true);
	}

	/**
	 * @param rows
	 * @param columns
	 * @param message
	 * @return
	 */
	private List<Property> get_properties(int[] rows, int[] columns, boolean message) {
		if ( null == rows || 0 == rows.length)
			return null;

		if ( null == columns || 0 == columns.length)
			return null;

		if ( 1 != columns.length || is_empty( rows, columns)) {
			// 選択領域が１列でないかまたは選択領域内が全てnullの場合は実行しない
			if ( message)
				JOptionPane.showMessageDialog( _parent,
					ResourceManager.get_instance().get( "soars.contents.table.remove.error.message"),
					ResourceManager.get_instance().get( "application.title"),
					JOptionPane.ERROR_MESSAGE);
			return null;
		}

		List<Property> properties = new ArrayList<Property>();
		for ( int row:rows) {
			Property property = ( Property)getValueAt( row, columns[ 0]);
			if ( null == property)
				continue;

			properties.add( property);
		}

		if ( properties.isEmpty())
			return null;

		return properties;
	}

	/* (non-Javadoc)
	 * @see soars.common.utility.swing.table.spread_sheet.SpreadSheetTableBase#valueChanged(javax.swing.event.ListSelectionEvent)
	 */
	public void valueChanged(ListSelectionEvent e) {
		super.valueChanged(e);
	}

	/* (non-Javadoc)
	 * @see javax.swing.JTable#changeSelection(int, int, boolean, boolean)
	 */
	public void changeSelection(int arg0, int arg1, boolean arg2, boolean arg3) {
		int prevRow = -1, prevColumn = -1, row = -1, column = -1;

		if ( 1 == getSelectedRowCount() && 1 == getSelectedColumnCount()) {
			prevRow = getSelectedRow();
			prevColumn = getSelectedColumn();
		}

		super.changeSelection(arg0, arg1, arg2, arg3);

		if ( 1 == getSelectedRowCount() && 1 == getSelectedColumnCount()) {
			row = getSelectedRow();
			column = getSelectedColumn();
		}

		//internal_select();

		internal_synchronize();

		update_title_and_comment( prevRow, prevColumn, row, column);
	}

	/**
	 * @param prevRow
	 * @param prevColumn
	 * @param row
	 * @param column
	 * 
	 */
	private void update_title_and_comment(int prevRow, int prevColumn, int row, int column) {
		//System.out.println( "(" + prevRow + ", " + prevColumn + ") - > (" + row + ", " + column + ")");
		if ( 0 <= prevRow && 0 <= prevColumn && can_undo())
			save( prevRow, prevColumn);

		clear_textUndoRedoManagers();
		_titleTextField.setText( "");
		_commentTextArea.setText( "");

		if ( 0 <= row && 0 <= column) {
			Property property = ( Property)getValueAt( row, column);
			if ( null != property) {
				_titleTextField.setText( property._title);
				_commentTextArea.setText( property._comment);
			}
		}

		setup_textUndoRedoManagers();
	}

	/**
	 * @return
	 */
	private boolean can_undo() {
		for ( TextUndoRedoManager textUndoRedoManager:_textUndoRedoManagers) {
			if ( textUndoRedoManager.can_undo())
				return true;
		}
		return false;
	}

	/**
	 * @param row
	 * @param column
	 */
	private void save(int row, int column) {
		Property property = ( Property)getValueAt( row, column);
		if ( null == property)
			return;

		//System.out.println( "save");
		property._title = _titleTextField.getText();
		property._comment = _commentTextArea.getText();
		if ( !property.update_properties( _propertyMap))
			return;
	}

	/**
	 * 
	 */
	protected void clear_textUndoRedoManagers() {
		_textUndoRedoManagers.clear();
	}

	/**
	 * 
	 */
	protected void setup_textUndoRedoManagers() {
		if ( !_textUndoRedoManagers.isEmpty())
			return;

		_textUndoRedoManagers.add( new TextUndoRedoManager( _titleTextField, this));
		_textUndoRedoManagers.add( new TextUndoRedoManager( _commentTextArea, this));
	}

	/**
	 * @param tableSelection
	 */
	public void get(TableSelection tableSelection) {
		if ( 1 == getSelectedRowCount() && 1 == getSelectedColumnCount()) {
			int row = getSelectedRow();
			int column = getSelectedColumn();
			if ( 0 <= row && 0 <= column && can_undo())
				save( row, column);
		}

		tableSelection.clear();
		for ( int row = 0; row < getRowCount(); ++row) {
			for ( int column = 0; column < getColumnCount(); ++column) {
				if ( isCellSelected( row, column))
					tableSelection.add( new Cell( row, column));
			}
		}
	}

	/**
	 * @param file
	 * @param tableSelection
	 */
	public void update(File file, TableSelection tableSelection) {
		clear_textUndoRedoManagers();
		_titleTextField.setText( "");
		_commentTextArea.setText( "");

		while ( 0 < getRowCount()) {
			removeRow( 0);
			_spreadSheetTableBase.removeRow( 0);
		}

		_propertyMap.clear();

		if ( null == file)
			return;

		if ( !Property.get_simulation_properties( _propertyMap, file))
			return;

		if ( !Property.get_animation_properties( _propertyMap, file))
			return;

		if ( !update())
			return;

		if ( 0 < getRowCount()) {
			clearSelection();
			List<Cell> cells = tableSelection.get_available_cells( this);
			if ( cells.isEmpty()) {
				select( 0, 0);
				Property property = ( Property)getValueAt( 0, 0);
				if ( null != property) {
					_titleTextField.setText( property._title);
					_commentTextArea.setText( property._comment);
				}
			} else {
				for ( Cell cell:cells) {
					addRowSelectionInterval( cell._row, cell._row);
					addColumnSelectionInterval( cell._column, cell._column);
				}

				requestFocus();
				internal_synchronize();

				Rectangle rectangle = getCellRect( cells.get( 0)._row, cells.get( 0)._column, true);
				scrollRectToVisible( rectangle);

				if ( 1 == cells.size()) {
					Property property = ( Property)getValueAt( cells.get( 0)._row, cells.get( 0)._column);
					if ( null != property) {
						_titleTextField.setText( property._title);
						_commentTextArea.setText( property._comment);
					}
				}
			}
		}

		setup_textUndoRedoManagers();
	}

	/**
	 * @return
	 */
	private boolean update() {
		List<Property[]> data = new ArrayList<Property[]>();
		Set<String> set = _propertyMap.keySet();
		String[] ids = set.toArray( new String[ 0]);
		if ( _reverse) {
			for ( int i = ids.length - 1; i >= 0; --i) {
				if ( !get( ids[ i], data))
					return false;
			}
		} else {
			for ( String id:ids) {
				if ( !get( id, data))
					return false;
			}
		}

		for ( Property[] properties:data) {
			addRow( properties);
			_spreadSheetTableBase.addRow( new Object[] { new Object()});
		}

		clear_textUndoRedoManagers();

		return true;
	}

	/**
	 * @param id
	 * @param data
	 * @return
	 */
	private boolean get(String id, List<Property[]> data) {
		Property property = _propertyMap.get( id);
		if ( null == property)
			return false;

		if ( !property.get( data, _reverse))
			return false;

		return true;
	}

	/* (non-Javadoc)
	 * @see soars.common.utility.swing.table.base.StandardTable#on_mouse_left_double_click(java.awt.event.MouseEvent)
	 */
	protected void on_mouse_left_double_click(MouseEvent mouseEvent) {
		on_run();
	}

	/* (non-Javadoc)
	 * @see soars.common.utility.swing.table.spread_sheet.SpreadSheetTableBase#on_enter(java.awt.event.ActionEvent)
	 */
	public void on_enter(ActionEvent actionEvent) {
		on_run();
	}

	/**
	 * @param actionEvent
	 */
	public void on_run(ActionEvent actionEvent) {
		on_run();
	}

	/**
	 * 
	 */
	private void on_run() {
		if ( 0 >= getRowCount())
			return;

		int[] rows = getSelectedRows();
		int[] columns = getSelectedColumns();

		Property property = get_property( rows, columns);
		if ( null == property)
			return;

		if ( property._type.equals( "simulation"))
			Simulator.start( property._file, property._id, property._title, _modelTree.get_title( property._file));
		else if ( property._type.equals( "animation")) {
			Property parentProperty = _propertyMap.get( property._parentID);
			Animator.start( property._file, property._parentID, property._id, property._title, ( ( null == parentProperty) ? "" : parentProperty._title), _modelTree.get_title( property._file));
		}
	}

	/* (non-Javadoc)
	 * @see soars.common.utility.swing.table.spread_sheet.SpreadSheetTableBase#on_remove(java.awt.event.ActionEvent)
	 */
	public void on_remove(ActionEvent actionEvent) {
		if ( 0 >= getRowCount())
			return;

		int[] rows = getSelectedRows();
		int[] columns = getSelectedColumns();

		List<Property> properties = get_properties( rows, columns, true);
		if ( null == properties)
			return;

		if ( JOptionPane.YES_OPTION == JOptionPane.showConfirmDialog(
			_parent,
			ResourceManager.get_instance().get( "soars.contents.table.confirm.remove.message"),
			ResourceManager.get_instance().get( "application.title"),
			JOptionPane.YES_NO_OPTION)) {
			if ( properties.get( 0)._type.equals( "simulation"))
				remove_simulation( properties);
			else if ( properties.get( 0)._type.equals( "animation"))
				remove_animation( properties);
			else
				return;

			_titleTextField.setText( "");
			_commentTextArea.setText( "");

			if ( 0 < getRowCount()) {
				int row = ( ( getRowCount() > rows[ 0]) ? rows[ 0] : ( getRowCount() - 1));
				select( row, columns[ 0]);
				Property property = ( Property)getValueAt( row, columns[ 0]);
				if ( null != property) {
					_titleTextField.setText( property._title);
					_commentTextArea.setText( property._comment);
				}
			}

			setup_textUndoRedoManagers();
		}
	}

	/**
	 * @param rows
	 * @param columns
	 * @return
	 */
	private boolean is_empty(int[] rows, int[] columns) {
		for ( int row:rows) {
			for ( int column:columns)
				if ( null != getValueAt( row, column))
					return false;
		}
		return true;
	}

	/**
	 * @param properties
	 * @return
	 */
	private boolean remove_simulation(List<Property> properties) {
		List<String> exclusionFilenames = new ArrayList<String>();
		for ( Property property:properties) {
			exclusionFilenames.add( Constant._soarsRootDirectoryName + "/" + Constant._simulatorRootDirectoryName + "/" + property._id + ".zip");
			exclusionFilenames.add( Constant._soarsRootDirectoryName + "/" + Constant._animatorRootDirectoryName + "/" + property._id);
			_propertyMap.remove( property._id);
		}

		File simulationPropertyTemporaryFile = properties.get( 0).get_simulation_property_temporary_file( _propertyMap);
		if ( null == simulationPropertyTemporaryFile)
			return false;

		simulationPropertyTemporaryFile.deleteOnExit();

		File animationPropertyTemporaryFile = properties.get( 0).get_animation_property_temporary_file( _propertyMap);
		if ( null == animationPropertyTemporaryFile)
			return false;

		animationPropertyTemporaryFile.deleteOnExit();

		Map<String, File> fileMap = new HashMap<String, File>();
		fileMap.put( Constant._soarsRootDirectoryName + "/" + Constant._simulatorRootDirectoryName + "/" + Constant._propertyFileName, simulationPropertyTemporaryFile);
		fileMap.put( Constant._soarsRootDirectoryName + "/" + Constant._animatorRootDirectoryName + "/" + Constant._propertyFileName, animationPropertyTemporaryFile);
		if ( null == ZipUtility.update( properties.get( 0)._file, fileMap, exclusionFilenames)) {
			simulationPropertyTemporaryFile.delete();
			animationPropertyTemporaryFile.delete();
			return false;
		}

		simulationPropertyTemporaryFile.delete();
		animationPropertyTemporaryFile.delete();

		clear_textUndoRedoManagers();
		_titleTextField.setText( "");
		_commentTextArea.setText( "");

		while ( 0 < getRowCount()) {
			removeRow( 0);
			_spreadSheetTableBase.removeRow( 0);
		}

		return update();
	}

	/**
	 * @param properties
	 * @return
	 */
	private boolean remove_animation(List<Property> properties) {
		List<String> exclusionFilenames = new ArrayList<String>();
		for ( Property property:properties) {
			exclusionFilenames.add( Constant._soarsRootDirectoryName + "/" + Constant._animatorRootDirectoryName + "/" + property._parentID + "/" + property._id);
			_propertyMap.get( property._parentID)._propertyMap.remove( property._id);
		}

		File simulationPropertyTemporaryFile = properties.get( 0).get_simulation_property_temporary_file( _propertyMap);
		if ( null == simulationPropertyTemporaryFile)
			return false;

		simulationPropertyTemporaryFile.deleteOnExit();

		File animationPropertyTemporaryFile = properties.get( 0).get_animation_property_temporary_file( _propertyMap);
		if ( null == animationPropertyTemporaryFile)
			return false;

		animationPropertyTemporaryFile.deleteOnExit();

		Map<String, File> fileMap = new HashMap<String, File>();
		fileMap.put( Constant._soarsRootDirectoryName + "/" + Constant._simulatorRootDirectoryName + "/" + Constant._propertyFileName, simulationPropertyTemporaryFile);
		fileMap.put( Constant._soarsRootDirectoryName + "/" + Constant._animatorRootDirectoryName + "/" + Constant._propertyFileName, animationPropertyTemporaryFile);
		if ( null == ZipUtility.update( properties.get( 0)._file, fileMap, exclusionFilenames)) {
			simulationPropertyTemporaryFile.delete();
			animationPropertyTemporaryFile.delete();
			return false;
		}

		simulationPropertyTemporaryFile.delete();
		animationPropertyTemporaryFile.delete();

		clear_textUndoRedoManagers();
		_titleTextField.setText( "");
		_commentTextArea.setText( "");

		while ( 0 < getRowCount()) {
			removeRow( 0);
			_spreadSheetTableBase.removeRow( 0);
		}

		return update();
	}

	/**
	 * 
	 */
	public void refresh() {
		updateUI();
		repaint();
	}

	/* (non-Javadoc)
	 * @see soars.common.utility.swing.text.ITextUndoRedoManagerCallBack#on_changed(javax.swing.undo.UndoManager)
	 */
	public void on_changed(UndoManager undoManager) {
	}

	/* (non-Javadoc)
	 * @see soars.common.utility.swing.text.ITextUndoRedoManagerCallBack#on_changed(javax.swing.text.AbstractDocument.DefaultDocumentEvent, javax.swing.undo.UndoManager)
	 */
	public void on_changed(DefaultDocumentEvent defaultDocumentEvent, UndoManager undoManager) {
	}
}
