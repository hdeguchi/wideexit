/*
 * 2005/07/12
 */
package soars.application.visualshell.object.experiment.edit.table;

import java.awt.Component;
import java.awt.Frame;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableColumnModel;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;

import soars.application.visualshell.common.menu.basic1.IBasicMenuHandler1;
import soars.application.visualshell.common.menu.basic1.RemoveAction;
import soars.application.visualshell.common.menu.basic2.CopyAction;
import soars.application.visualshell.common.menu.basic2.CutAction;
import soars.application.visualshell.common.menu.basic2.IBasicMenuHandler2;
import soars.application.visualshell.common.menu.basic2.PasteAction;
import soars.application.visualshell.common.tool.CommonTool;
import soars.application.visualshell.file.exporter.experiment.ExperimentTableExporter;
import soars.application.visualshell.file.exporter.script.Exporter;
import soars.application.visualshell.file.importer.experiment.ExperimentTableImporter;
import soars.application.visualshell.main.Constant;
import soars.application.visualshell.main.Environment;
import soars.application.visualshell.main.ResourceManager;
import soars.application.visualshell.object.experiment.ExperimentManager;
import soars.application.visualshell.object.experiment.InitialValueMap;
import soars.application.visualshell.object.experiment.edit.table.data.ExperimentRowHeaderData;
import soars.common.utility.swing.table.base.data.BlockData;
import soars.common.utility.swing.table.base.data.ColumnData;
import soars.common.utility.swing.table.base.data.RowData;
import soars.common.utility.swing.table.base.undo_redo.cell.SetValueUndo;
import soars.common.utility.swing.table.base.undo_redo.cell.SetValuesUndo;
import soars.common.utility.swing.table.base.undo_redo.column.ColumnsUndo;
import soars.common.utility.swing.table.spread_sheet.SpreadSheetTable;
import soars.common.utility.tool.common.Tool;
import soars.common.utility.tool.sort.QuickSort;
import soars.common.utility.tool.sort.StringNumberComparator;

/**
 * The table component to edit the experiment data.
 * @author kurata / SOARS project
 */
public class ExperimentTable extends SpreadSheetTable implements IBasicMenuHandler1, IBasicMenuHandler2 {

	/**
	 * 
	 */
	private JButton _exportButton = null;

	/**
	 * 
	 */
	private ExperimentManager _experimentManager = null;

	/**
	 * 
	 */
	protected JMenuItem _removeMenuItem = null;

	/**
	 * 
	 */
	protected JMenuItem _copyMenuItem = null;

	/**
	 * 
	 */
	protected JMenuItem _cutMenuItem = null;

	/**
	 * 
	 */
	protected JMenuItem _pasteMenuItem = null;

	/**
	 * 
	 */
	protected final int _exportCheckBoxColumn = 0;

	/**
	 * 
	 */
	protected final int _nameColumn = 1;

	/**
	 * 
	 */
	protected final int _commentColumn = 2;

	/**
	 * 
	 */
	protected final int _startColumn = 3;

	/**
	 * 
	 */
	private boolean _resized = false;

	/**
	 * 
	 */
	static private Object _lock = new Object();

	/**
	 * 
	 */
	static public List<BlockData<Object>> _selectedBlockData = null;

	/**
	 * 
	 */
	static public List<RowData<String>> _selectedRowData = null;
	/**
	 * 
	 */
	static public List<ColumnData<String>> _selectedColumnData = null;

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
			_selectedBlockData = new ArrayList<BlockData<Object>>();
			_selectedRowData = new ArrayList<RowData<String>>();
			_selectedColumnData = new ArrayList<ColumnData<String>>();
		}
	}

	/**
	 * 
	 */
	public static void refresh() {
		_selectedBlockData.clear();
		_selectedRowData.clear();
		_selectedColumnData.clear();
	}

	/**
	 * Creates this object with the specified data.
	 * @param exportButton the button component to export the ModelBuilder scripts with the all selected data
	 * @param experimentManager the experiment support manager
	 * @param owner the frame of the parent container
	 * @param parent the parent container of this component
	 */
	public ExperimentTable(JButton exportButton, ExperimentManager experimentManager, Frame owner, Component parent) {
		super(owner, parent);
		_exportButton = exportButton;
		_experimentManager = experimentManager;
	}

	/**
	 * Returns true if the selected data exists.
	 * @return true if the selected data exists
	 */
	public boolean can_export() {
		DefaultTableModel defaultTableModel = ( DefaultTableModel)getModel();
		for ( int i = 0; i < defaultTableModel.getRowCount(); ++i) {
			JCheckBox checkBox = ( JCheckBox)defaultTableModel.getValueAt( i, _exportCheckBoxColumn);
			if ( checkBox.isSelected())
				return true;
		}
		return false;
	}

	/**
	 * @param algorithm
	 * @return
	 */
	public boolean can_export_for_genetic_algorithm(List<String> algorithm) {
		DefaultTableModel defaultTableModel = ( DefaultTableModel)getModel();

		int counter = 0;
		for ( int i = 0; i < defaultTableModel.getRowCount(); ++i) {
			JCheckBox checkBox = ( JCheckBox)defaultTableModel.getValueAt( i, _exportCheckBoxColumn);
			if ( checkBox.isSelected())
				++counter;
		}

		if ( 3 > counter)
			return false;


		String[] aliases = _experimentManager.get_aliases();
		if ( is_Uniform_Crossover( aliases, defaultTableModel)) {
			algorithm.add( "UxMgg");
			return true;
		} else if ( is_Unimodal_Normal_Distribution_Crossover( aliases, defaultTableModel)) {
			algorithm.add( "UndxMgg");
			return true;
		}

		return false;
	}

	/**
	 * @param aliases
	 * @param defaultTableModel 
	 * @return
	 */
	private boolean is_Uniform_Crossover(String[] aliases, DefaultTableModel defaultTableModel) {
		for ( int i = 1; i < defaultTableModel.getRowCount(); ++i) {
			for ( int j = _startColumn; j < defaultTableModel.getColumnCount(); ++j) {
				if ( !aliases[ j - _startColumn].startsWith( "$__val"))
					continue;

				String value = ( String)defaultTableModel.getValueAt( i, j);
				if ( value.equals( ""))
					continue;

				try {
					int val = Integer.parseInt( value);
					if ( 0 != val && 1 != val)
						return false;
				} catch (NumberFormatException e) {
					//e.printStackTrace();
					return false;
				}
			}
		}
		return true;
	}

	/**
	 * @param aliases
	 * @param defaultTableModel 
	 * @return
	 */
	private boolean is_Unimodal_Normal_Distribution_Crossover(String[] aliases, DefaultTableModel defaultTableModel) {
		for ( int i = 1; i < defaultTableModel.getRowCount(); ++i) {
			for ( int j = _startColumn; j < defaultTableModel.getColumnCount(); ++j) {
				if ( !aliases[ j - _startColumn].startsWith( "$__val"))
					continue;

				String value = ( String)defaultTableModel.getValueAt( i, j);
				if ( value.equals( ""))
					continue;

				try {
					double val = Double.parseDouble( value);
					if ( 0.0 > val && 1.0 < val)
						return false;
				} catch (NumberFormatException e) {
					//e.printStackTrace();
					return false;
				}
			}
		}
		return true;
	}

	/**
	 * @param name
	 * @param row
	 * @return
	 */
	private boolean contains(String name, int row) {
		DefaultTableModel defaultTableModel = ( DefaultTableModel)getModel();
		for ( int i = 1; i < defaultTableModel.getRowCount(); ++i) {
			if ( row == i)
				continue;

			if ( name.equals( ( String)getValueAt( i, _nameColumn)))
				return true;
		}

		return false;
	}

	/**
	 * @return
	 */
	private String get_unique_name() {
		String name;
		int index = 1;
		while ( true) {
			name = "name" + index;
			if ( !contains( name, -1))
				break;

			++index;
		}
		return name;
	}

	/**
	 * @param columnWidths
	 * @return
	 */
	public boolean setup_column(Vector<Integer> columnWidths) {
		DefaultTableColumnModel defaultTableColumnModel = ( DefaultTableColumnModel)getColumnModel();

		String[] aliases = _experimentManager.get_aliases();
		if ( null == aliases)
			return false;

		int index = 0;
		for ( int i = 0; i < defaultTableColumnModel.getColumnCount(); ++i) {
			if ( _startColumn <= i)
				defaultTableColumnModel.getColumn( i).setHeaderValue( aliases[ index++]);
			else
				if ( _exportCheckBoxColumn == i) {
					defaultTableColumnModel.getColumn( i).setHeaderValue(
						ResourceManager.get_instance().get( "edit.experiment.dialog.experiment.table.header.export"));
					defaultTableColumnModel.getColumn( i).setMinWidth( 50);
					defaultTableColumnModel.getColumn( i).setMaxWidth( 50);
				} else if ( _nameColumn == i)
					defaultTableColumnModel.getColumn( i).setHeaderValue(
						ResourceManager.get_instance().get( "edit.experiment.dialog.experiment.table.header.name"));
				else if ( _commentColumn == i)
					defaultTableColumnModel.getColumn( i).setHeaderValue(
						ResourceManager.get_instance().get( "edit.experiment.dialog.experiment.table.header.comment"));
				else
					defaultTableColumnModel.getColumn( i).setHeaderValue( "Unknown");
		}

		for ( int i = 0; i < defaultTableColumnModel.getColumnCount(); ++i) {
			TableColumn tableColumn = defaultTableColumnModel.getColumn( i);
			if ( _exportCheckBoxColumn == i)
				tableColumn.setCellRenderer( new CheckBoxTableCellRenderer());
			else
				tableColumn.setCellRenderer( new ExperimentTableCellRenderer());

			if ( null == columnWidths)
				continue;

			Integer integer = columnWidths.get( i);
			tableColumn.setPreferredWidth( integer.intValue());
		}

		return true;
	}

	/**
	 * Gets the array of the current column widths.
	 */
	public void get_column_widths() {
		_experimentManager._columnWidths.clear();

		DefaultTableColumnModel defaultTableColumnModel
			= ( DefaultTableColumnModel)getColumnModel();
		for ( int i = 0; i < defaultTableColumnModel.getColumnCount(); ++i) {
			_experimentManager._columnWidths.add(
				new Integer( defaultTableColumnModel.getColumn( i).getPreferredWidth()));
		}
	}

	/* (Non Javadoc)
	 * @see soars.application.visualshell.object.experiment.edit.base.ExperimentTableBase#setup(soars.application.visualshell.object.experiment.edit.base.ExperimentTableBase)
	 */
	public boolean setup(ExperimentRowHeaderTable experimentRowHeaderTable) {
		if ( !super.setup(experimentRowHeaderTable, true))
			return false;

		setColumnCount( _experimentManager.get_initial_value_count() + _startColumn);

		ExperimentTableHeader experimentTableHeader = new ExperimentTableHeader( this, getColumnModel(), _owner, _parent);
		if ( !experimentTableHeader.setup( new ExperimentTableHeaderRenderer(), true))
			return false;

		getTableHeader().setReorderingAllowed( false);

		if ( !setup_column( null))
			return false;

		String[] aliases = _experimentManager.get_aliases();
		if ( null == aliases)
			return false;

		Object[] objects = new Object[ _experimentManager.get_initial_value_count() + _startColumn];
		JCheckBox checkBox = new JCheckBox( "", false);
		objects[ _exportCheckBoxColumn] = checkBox;
		objects[ _nameColumn] = ResourceManager.get_instance().get( "edit.experiment.dialog.experiment.table.checkbox.select.all");
		objects[ _commentColumn] = "";

		DefaultTableColumnModel defaultTableColumnModel = ( DefaultTableColumnModel)getColumnModel();
		for ( int i = _startColumn; i < defaultTableColumnModel.getColumnCount(); ++i) {
			String alias = ( String)defaultTableColumnModel.getColumn( i).getHeaderValue();
			objects[ i] = ( String)_experimentManager._commentMap.get( alias);
		}

		addRow( objects);


		if ( !_experimentManager.isEmpty()) {
			objects = new Object[ _experimentManager.get_initial_value_count() + _startColumn];

			String[] names = ( String[])( new Vector( _experimentManager.keySet())).toArray( new String[ 0]);
			QuickSort.sort( names, new StringNumberComparator( true, false));

			for ( int i = 0; i < names.length; ++i) {
				InitialValueMap initialValueMap = ( InitialValueMap)_experimentManager.get( names[ i]);

				for ( int j = 0; j < objects.length; ++j)
					objects[ j] = null;

				objects[ _exportCheckBoxColumn] = new JCheckBox( "", initialValueMap._export);

				objects[ _nameColumn] = names[ i];

				objects[ _commentColumn] = initialValueMap._comment;

				for ( int j = 0; j < aliases.length; ++j)
					objects[ j + _startColumn] = initialValueMap.get( aliases[ j]);

				addRow( objects);
			}
		}


		if ( 0 < getRowCount()) {
			setRowSelectionInterval( 0, 0);
			setColumnSelectionInterval( 0, 0);
		}

		addComponentListener( new ComponentAdapter() {
			public void componentResized(ComponentEvent arg0) {
				if ( _resized)
					return;

				on_componentResized( arg0);
				_resized = true;
			}
		});

		addMouseMotionListener( new MouseMotionAdapter() {
			public void mouseDragged(MouseEvent arg0) {
				getTableHeader().repaint();
			}
		});

		setup_undo_redo_manager( experimentTableHeader);

		return true;
	}

	/**
	 * @param componentEvent
	 */
	protected void on_componentResized(ComponentEvent componentEvent) {
		setup_column_widths();
	}

	/**
	 * 
	 */
	protected void setup_column_widths() {
		setAutoResizeMode( AUTO_RESIZE_OFF);

		DefaultTableColumnModel defaultTableColumnModel
			= ( DefaultTableColumnModel)getColumnModel();

		if ( _experimentManager.get_column_count() != _experimentManager._columnWidths.size()) {
			for ( int i = 0; i < defaultTableColumnModel.getColumnCount(); ++i)
				defaultTableColumnModel.getColumn( i).setPreferredWidth(
					( 0 == i) ? ExperimentManager._defaultExperimentTableCheckBoxColumnWidth
					: ExperimentManager._defaultExperimentTableColumnWidth);
		} else {
			for ( int i = 0; i < defaultTableColumnModel.getColumnCount(); ++i) {
				if ( _experimentManager._columnWidths.size() <= i)
					defaultTableColumnModel.getColumn( i).setPreferredWidth( ExperimentManager._defaultExperimentTableColumnWidth);
				else {
					Integer integer = ( Integer)_experimentManager._columnWidths.get( i);
					defaultTableColumnModel.getColumn( i).setPreferredWidth( integer.intValue());
				}
			}
		}
	}

	/* (non-Javadoc)
	 * @see javax.swing.JTable#isCellEditable(int, int)
	 */
	@Override
	public boolean isCellEditable(int row, int column) {
		return ( 0 != column && !( 0 == row && 3 > column));
	}

	/* (Non Javadoc)
	 * @see soars.common.utility.swing.table.StandardTable#on_mouse_pressed(java.awt.event.MouseEvent)
	 */
	protected void on_mouse_pressed(MouseEvent mouseEvent) {
		int row = rowAtPoint( mouseEvent.getPoint());
		int column = columnAtPoint( mouseEvent.getPoint());

		if ( 1 != mouseEvent.getButton())
			return;

		if ( 0 > row || getRowCount() <= row)
			return;

		if ( 0 > column || getColumnCount() <= column)
			return;

		column = convertColumnIndexToModel( column);

		if ( _exportCheckBoxColumn == column) {
			JCheckBox checkBox = ( JCheckBox)getValueAt( row, column);
			if ( 0 == row) {
				checkBox.setSelected( !checkBox.isSelected());
				select_all( checkBox.isSelected());
			} else {
				List<SetValueUndo> setValueUndoList = new ArrayList<SetValueUndo>();
				setValueUndoList.add( new SetValueUndo( new JCheckBox( "", !checkBox.isSelected()), row, column, this, this));
				SetValuesUndo setValuesUndo = new SetValuesUndo( setValueUndoList, this, this);
				setValuesAt( setValuesUndo, setValueUndoList);
				_exportButton.setEnabled( can_export());
			}
		}

		repaint();
	}

	/**
	 * @param selected
	 */
	protected void select_all(boolean selected) {
		List<SetValueUndo> setValueUndoList = new ArrayList<SetValueUndo>();
		for ( int row = 1; row < getRowCount(); ++row) {
			JCheckBox checkBox = ( JCheckBox)getValueAt( row, _exportCheckBoxColumn);
			setValueUndoList.add( new SetValueUndo( new JCheckBox( "", checkBox.isSelected()), row, _exportCheckBoxColumn, this, this));
		}

		SetValuesUndo setValuesUndo = new SetValuesUndo( setValueUndoList, this, this);
		setValuesAt( setValuesUndo, setValueUndoList);

		for ( int row = 1; row < getRowCount(); ++row) {
			JCheckBox checkBox = ( JCheckBox)getValueAt( row, _exportCheckBoxColumn);
			checkBox.setSelected( selected);
		}
	}

	/* (Non Javadoc)
	 * @see soars.common.utility.swing.table.StandardTable#setup_popup_menu()
	 */
	protected void setup_popup_menu() {
		super.setup_popup_menu();

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
	}

	/* (non-Javadoc)
	 * @see soars.common.utility.swing.table.spread_sheet.SpreadSheetTable#on_mouse_right_up(int, int, java.awt.event.MouseEvent)
	 */
	@Override
	protected boolean on_mouse_right_up(int row, int column, MouseEvent mouseEvent) {
		super.on_mouse_right_up(row, column, mouseEvent);
		on_mouse_right_up( row, column, new Point( mouseEvent.getX(), mouseEvent.getY()));
		return true;
	}

	/**
	 * @param row
	 * @param column
	 * @param point
	 */
	private void on_mouse_right_up(int row, int column, Point point) {
		getTableHeader().repaint();

		_removeMenuItem.setEnabled( true);

		_copyMenuItem.setEnabled( true);
		_cutMenuItem.setEnabled( true);
		_pasteMenuItem.setEnabled( true);

		DefaultTableModel defaultTableModel = ( DefaultTableModel)getModel();
		if ( 0 == defaultTableModel.getRowCount()) {
			_removeMenuItem.setEnabled( false);
			_copyMenuItem.setEnabled( false);
			_cutMenuItem.setEnabled( false);
			_pasteMenuItem.setEnabled( false);
		} else {
			if ( ( 0 <= row && getRowCount() > row)
				&& ( 0 <= column && getColumnCount() > column)) {
				//setRowSelectionInterval( row, row);
				//setColumnSelectionInterval( column, column);
				if ( 0 == row) {
					if ( _startColumn > column) {
						_removeMenuItem.setEnabled( false);
						_copyMenuItem.setEnabled( false);
						_cutMenuItem.setEnabled( false);
						_pasteMenuItem.setEnabled( false);
					}
				} else {
					_copyMenuItem.setEnabled( is_possible( false, false));
					_cutMenuItem.setEnabled( is_possible( false, false));
					_pasteMenuItem.setEnabled( is_correct( false));
				}
			} else {
				_removeMenuItem.setEnabled( false);
				_copyMenuItem.setEnabled( false);
				_cutMenuItem.setEnabled( false);
				_pasteMenuItem.setEnabled( false);
			}
		}

		_popupMenu.show( this, point.x, point.y);
	}

	/* (Non Javadoc)
	 * @see soars.application.visualshell.common.menu.basic1.IBasicMenuHandler1#on_append(java.awt.event.ActionEvent)
	 */
	public void on_append(ActionEvent actionEvent) {
	}

	/* (Non Javadoc)
	 * @see soars.application.visualshell.common.menu.basic1.IBasicMenuHandler1#on_insert(java.awt.event.ActionEvent)
	 */
	public void on_insert(ActionEvent actionEvent) {
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.common.menu.basic1.IBasicMenuHandler1#on_remove(java.awt.event.ActionEvent)
	 */
	@Override
	public void on_remove(ActionEvent actionEvent) {
		int[] rows = getSelectedRows();
		int[] columns = getSelectedColumns();
		if ( !is_possible( rows, columns, true, true))
			return;

		remove( rows, columns);
		refresh();
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
			for ( int column:columns) {
				if ( 0 == row && ( 0 == column || 1 == column || 2 == column))
					continue;

				setValueUndoList.add( new SetValueUndo( 0 == column ? new JCheckBox() : "", row, column, this, this));
			}
		}
		SetValuesUndo setValuesUndo = new SetValuesUndo( setValueUndoList, this, this);
		setValuesAt( setValuesUndo, setValueUndoList);

		repaint();
		requestFocus();
		synchronize();

		return true;
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.common.menu.basic1.IBasicMenuHandler1#on_edit(java.awt.event.ActionEvent)
	 */
	@Override
	public void on_edit(ActionEvent actionEvent) {
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
	@Override
	public void on_undo() {
		super.on_undo();
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
	@Override
	public void on_redo() {
		super.on_redo();
		refresh();
		repaint();
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.common.menu.basic2.IBasicMenuHandler2#on_copy(java.awt.event.ActionEvent)
	 */
	@Override
	public void on_copy(ActionEvent actionEvent) {
		int[] rows = getSelectedRows();
		int[] columns = getSelectedColumns();
		if ( !is_possible( rows, columns, false, true))
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

		Arrays.sort( rows);
		Arrays.sort( columns);

		int minRow = rows[ 0];
		int minColumn = columns[ 0];

		for ( int row:rows) {
			BlockData<Object> blockData = new BlockData<Object>( row - minRow, minColumn);
			for ( int column:columns) {
				Object object = getValueAt( row, column);
				if ( 0 != column)
					blockData.add( null == object ? "" : ( String)object);
				else {
					JCheckBox checkBox = new JCheckBox( "", false);
					if ( null == object)
						blockData.add( checkBox);
					else {
						if ( !( object instanceof JCheckBox))
							blockData.add( checkBox);
						else {
							JCheckBox cb = ( JCheckBox)object;
							checkBox.setSelected( cb.isSelected());
							blockData.add( checkBox);
						}
					}
				}
			}
			_selectedBlockData.add( blockData);
		}

		return true;
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.common.menu.basic2.IBasicMenuHandler2#on_cut(java.awt.event.ActionEvent)
	 */
	@Override
	public void on_cut(ActionEvent actionEvent) {
		int[] rows = getSelectedRows();
		int[] columns = getSelectedColumns();
		if ( !is_possible( rows, columns, false, true))
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

	/**
	 * @param onRemove 
	 * @param showErrorMessage 
	 * @return
	 */
	private boolean is_possible(boolean onRemove, boolean showErrorMessage) {
		int[] rows = getSelectedRows();
		int[] columns = getSelectedColumns();
		return is_possible( rows, columns, onRemove, showErrorMessage);
	}

	/**
	 * @param rows 
	 * @param columns 
	 * @param onRemove 
	 * @param showErrorMessage 
	 * @param showMessage 
	 * @return
	 */
	private boolean is_possible(int[] rows, int[] columns, boolean onRemove, boolean showErrorMessage) {
		// 少なくとも１つのセルが選択されていて、選択領域が連続ならtrueを返す
		if ( null == rows || 0 == rows.length) {
			on_error_possible( showErrorMessage);
			return false;
		}

		if ( null == columns || 0 == columns.length) {
			on_error_possible( showErrorMessage);
			return false;
		}

		// 選択領域内が全てnullなら実行しない←必要か？
		//if ( is_empty( rows, columns)) {
		//	on_error_possible( showErrorMessage);
		//	return false;
		//}

		Arrays.sort( rows);
		Arrays.sort( columns);

		if ( !onRemove && 0 <= Arrays.binarySearch( rows, 0) && ( 0 <= Arrays.binarySearch( columns, 0) || 0 <= Arrays.binarySearch( columns, 1) || 0 <= Arrays.binarySearch( columns, 2))) {
			on_error_possible( showErrorMessage);
			return false;
		}

		if ( !Tool.is_consecutive( rows) || !Tool.is_consecutive( columns)) {
			on_error_possible( showErrorMessage);
			return false;
		}

		return true;
	}

	/**
	 * @param showErrorMessage 
	 */
	private void on_error_possible(boolean showErrorMessage) {
		if ( !showErrorMessage)
			return;

		// 選択領域が不正な場合のエラーメッセージ
		JOptionPane.showMessageDialog( _parent,
			ResourceManager.get_instance().get( "edit.experiment.dialog.experiment.table.possible.error.message"),
			ResourceManager.get_instance().get( "application.title"),
			JOptionPane.ERROR_MESSAGE);
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.common.menu.basic2.IBasicMenuHandler2#on_paste(java.awt.event.ActionEvent)
	 */
	@Override
	public void on_paste(ActionEvent actionEvent) {
		int[] rows = getSelectedRows();
		int[] columns = getSelectedColumns();
		if ( !is_correct( rows, columns, true))
			return;

		paste( rows, columns);
	}

	/**
	 * @param rows
	 * @param columns
	 * @return
	 */
	private boolean paste(int[] rows, int[] columns) {
		List<SetValueUndo> setValueUndoList = new ArrayList<SetValueUndo>();
		for ( BlockData<Object> blockData:_selectedBlockData) {
			int row = rows[ 0] + blockData._row;

			// はみ出している場合のチェック
			if ( getRowCount() <= row)
				continue;

			int column = columns[ 0];

			for ( Object object:blockData) {
				// はみ出している場合のチェック
				if ( getColumnCount() <= column)
					continue;

				if ( null == object)
					setValueUndoList.add( new SetValueUndo( "", row, column, this, this));
				else {
					if ( object instanceof String)
						setValueUndoList.add( new SetValueUndo( object, row, column, this, this));
					else if ( object instanceof JCheckBox) {
						JCheckBox checkBox = ( JCheckBox)object;
						setValueUndoList.add( new SetValueUndo( new JCheckBox( "", checkBox.isSelected()), row, column, this, this));
					} else
						setValueUndoList.add( new SetValueUndo( "", row, column, this, this));
				}

				++column;
			}
		}

		SetValuesUndo setValuesUndo = new SetValuesUndo( setValueUndoList, this, this);
		setValuesAt( setValuesUndo, setValueUndoList);

		// 貼付けた部分を選択する
		if ( null != setValuesUndo) {
			setRowSelectionInterval( setValuesUndo._rowFrom, setValuesUndo._rowTo);
			setColumnSelectionInterval( setValuesUndo._columnFrom, setValuesUndo._columnTo);
			requestFocus();
			internal_synchronize();
		}

		repaint();
		requestFocus();

		return true;
	}

	/**
	 * @param showErrorMessage 
	 * @return
	 */
	private boolean is_correct(boolean showErrorMessage) {
		int[] rows = getSelectedRows();
		int[] columns = getSelectedColumns();
		return is_correct( rows, columns, showErrorMessage);
	}

	/**
	 * @param rows
	 * @param columns
	 * @param showErrorMessage 
	 * @return
	 */
	private boolean is_correct(int[] rows, int[] columns, boolean showErrorMessage) {
		if ( _selectedBlockData.isEmpty())
			return false;

		if ( null == rows || 1 != rows.length) {
			on_error_correct( showErrorMessage);
			return false;
		}

		if ( null == columns || 1 != columns.length) {
			on_error_correct( showErrorMessage);
			return false;
		}

		if ( 0 == columns[ 0]) {
			for ( BlockData<Object> blockData:_selectedBlockData) {
				if ( !( blockData.get( 0) instanceof JCheckBox)) {
					on_error_correct( showErrorMessage);
					return false;
				}
			}
		} else {
			for ( BlockData<Object> blockData:_selectedBlockData) {
				if ( !( blockData.get( 0) instanceof String)) {
					on_error_correct( showErrorMessage);
					return false;
				}
			}
		}

		// 貼付けても変わらない場合は実行しない←必要か？
		//if ( is_same( rows[ 0], columns[ 0]))
		//	return false;

		return true;
	}

	/**
	 * @param showErrorMessage 
	 */
	private void on_error_correct(boolean showErrorMessage) {
		if ( !showErrorMessage)
			return;

		// チェックボックス列にチェックボックス列以外の文字列を貼り付けようとしている場合のエラーメッセージ
		JOptionPane.showMessageDialog( _parent,
			ResourceManager.get_instance().get( "edit.experiment.dialog.experiment.table.correct.error.message"),
			ResourceManager.get_instance().get( "application.title"),
			JOptionPane.ERROR_MESSAGE);
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
	 * @see soars.common.utility.swing.table.base.StandardTable#addRow()
	 */
	@Override
	public void addRow() {
		Object[] objects = new Object[ _experimentManager.get_initial_value_count() + _startColumn];

		for ( int j = 0; j < objects.length; ++j)
			objects[ j] = null;

		objects[ _exportCheckBoxColumn] = new JCheckBox( "", false);

		objects[ _nameColumn] = get_unique_name();

		objects[ _commentColumn] = "";

		for ( int i = _startColumn; i < objects.length; ++i)
			objects[ i] = "";

		addRow( objects);
	}

	/* (non-Javadoc)
	 * @see soars.common.utility.swing.table.base.StandardTable#insertRow(int)
	 */
	@Override
	public void insertRow(int row) {
		Object[] objects = new Object[ _experimentManager.get_initial_value_count() + _startColumn];

		for ( int j = 0; j < objects.length; ++j)
			objects[ j] = null;

		objects[ _exportCheckBoxColumn] = new JCheckBox( "", false);

		objects[ _nameColumn] = get_unique_name();

		objects[ _commentColumn] = "";

		for ( int i = _startColumn; i < objects.length; ++i)
			objects[ i] = "";

		insertRow( row, objects);
	}

	/* (non-Javadoc)
	 * @see soars.common.utility.swing.table.spread_sheet.SpreadSheetTableBase#on_copy_row(int[])
	 */
	@Override
	public void on_copy_row(int[] rows) {
		for ( int i = 0; i < rows.length; ++i) {
			Object object = getValueAt( rows[ i], 0);
			if ( null == object)
				_selectedRowData.get( i).add( "false");
			else {
				if ( !( object instanceof JCheckBox))
					_selectedRowData.get( i).add( "false");
				else {
					JCheckBox checkBox = ( JCheckBox)object;
					ExperimentRowHeaderData experimentRowHeaderData = ( ExperimentRowHeaderData)_selectedRowData.get( i)._rowHeaderData;
					experimentRowHeaderData._export = checkBox.isSelected();
					_selectedRowData.get( i).add( checkBox.isSelected() ? "true" : "false");
				}
			}
			for ( int column = 1; column < getColumnCount(); ++column) {
				object = getValueAt( rows[ i], column);
				_selectedRowData.get( i).add( null == object ? "" : ( String)object);
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
				setValueUndoList.add( new SetValueUndo( 0 == column ? new JCheckBox() : "", row, column, this, this));
			setValueUndoLists.add( setValueUndoList);
		}
		setValuesAt( rows, setValueUndoLists, rowHeaderSetValueUndoLists);

		repaint();
	}

	/* (non-Javadoc)
	 * @see soars.common.utility.swing.table.spread_sheet.SpreadSheetTableBase#on_paste_row(int, java.util.List)
	 */
	@Override
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

			ExperimentRowHeaderData experimentRowHeaderData = ( ExperimentRowHeaderData)_selectedRowData.get( i)._rowHeaderData;
			JCheckBox checkBox = new JCheckBox( "", experimentRowHeaderData._export);
			setValueUndoList.add( new SetValueUndo( checkBox, integer.intValue(), 0, this, this));

			setValueUndoList.add( new SetValueUndo( "", integer.intValue(), 1, this, this));

			for ( int column = 1; column < getColumnCount(); ++column)
				setValueUndoList.add( new SetValueUndo( null == rowData.get( column) ? "" : rowData.get( column), integer.intValue(), column, this, this));

			setValueUndoLists.add( setValueUndoList);
			list.add( integer);
		}

		int[] rows = new int[ list.size()];
		for ( int i = 0; i < rows.length; ++i)
			rows[ i] = list.get( i).intValue();

		setValuesAt( rows, setValueUndoLists, rowHeaderSetValueUndoLists);

		repaint();
	}

	/* (non-Javadoc)
	 * @see soars.common.utility.swing.table.spread_sheet.SpreadSheetTableBase#on_copy_column(int[])
	 */
	@Override
	public void on_copy_column(int[] columns) {
		for ( int i = 0; i < columns.length; ++i) {
			for ( int row = 0; row < getRowCount(); ++row) {
				String value = ( String)getValueAt( row, columns[ i]);
				_selectedColumnData.get( i).add( null == value ? "" : value);
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
				setValueUndoList.add( new SetValueUndo( "", row, column, this, this));
			setValueUndoLists.add( setValueUndoList);
		}

		ColumnsUndo columnsUndo = new ColumnsUndo( columns, setValueUndoLists, this, this);
		setValuesAt( columnsUndo, columns, setValueUndoLists);

		repaint();
	}

	/* (non-Javadoc)
	 * @see soars.common.utility.swing.table.spread_sheet.SpreadSheetTableBase#on_paste_column(int)
	 */
	@Override
	public void on_paste_column(int column) {
		List<Integer> list = new ArrayList<Integer>();
		List<List<SetValueUndo>> setValueUndoLists = new ArrayList<List<SetValueUndo>>();
		for ( int i = 0; i < _selectedColumnData.size(); ++i) {
			ColumnData<String> columnData = _selectedColumnData.get( i);
			Integer integer = Integer.valueOf( column + columnData._column);

			// はみ出している場合のチェック
			if ( getColumnCount() <= integer.intValue())
				continue;

			List<SetValueUndo> setValueUndoList = new ArrayList<SetValueUndo>();
			for ( int row = 0; row < getRowCount(); ++row)
				setValueUndoList.add( new SetValueUndo( null == columnData.get( row) ? "" : columnData.get( row), row, integer.intValue(), this, this));
			setValueUndoLists.add( setValueUndoList);
			list.add( integer);
		}

		int[] columns = new int[ list.size()];
		for ( int i = 0; i < columns.length; ++i)
			columns[ i] = list.get( i).intValue();

		ColumnsUndo columnsUndo = new ColumnsUndo( columns, setValueUndoLists, this, this);
		setValuesAt( columnsUndo, columns, setValueUndoLists);

		repaint();
	}

	/* (Non Javadoc)
	 * @see soars.application.visualshell.object.experiment.edit.base.ExperimentTableBase#set_export_script(int, boolean)
	 */
	public void set_export_script(int[] rows, boolean export) {
		DefaultTableModel defaultTableModel = ( DefaultTableModel)getModel();
		
		for ( int i = 0; i < rows.length; ++i) {
			if ( 0 == rows[ i])
				continue;

			JCheckBox checkBox = ( JCheckBox)defaultTableModel.getValueAt( rows[ i], _exportCheckBoxColumn);
			checkBox.setSelected( export);
		}

//		_experimentTableBase.clear();
	}

	/* (Non Javadoc)
	 * @see soars.application.visualshell.object.experiment.edit.base.ExperimentTableBase#export_to_clipboard(int)
	 */
	public void export_to_clipboard(int row) {
		InitialValueMap initialValueMap = new InitialValueMap();

		DefaultTableModel defaultTableModel = ( DefaultTableModel)getModel();
		DefaultTableColumnModel defaultTableColumnModel = ( DefaultTableColumnModel)getColumnModel();
		for ( int i = _startColumn; i < defaultTableColumnModel.getColumnCount(); ++i) {
			String alias = ( String)defaultTableColumnModel.getColumn( i).getHeaderValue();
			String initialValue = ( String)defaultTableModel.getValueAt( row, i);
			initialValueMap.put( alias, initialValue);
		}

		String experimentName = ( String)defaultTableModel.getValueAt( row, _nameColumn);

		Exporter.execute( initialValueMap, experimentName, true, false);
	}

	/**
	 * Returns true for exporting the ModelBuilder scripts into the specified directory successfully.
	 * @param file the specified directory
	 * @return true for exporting the ModelBuilder scripts into the specified directory successfully
	 */
	public boolean export(File file) {
		String absoluteName = file.getAbsolutePath();
		String name = file.getName();
		int index = name.lastIndexOf( '.');
		if ( -1 == index)
			file = new File( absoluteName + ".sor");
		else if ( name.length() - 1 == index)
			file = new File( absoluteName + "sor");

		String[] words = file.getName().split( "\\.");
		if ( 1 > words.length)
			return false;

		String filename = "";
		String extension = "";
		if ( 1 == words.length)
			filename = ( words[ 0] + '.');
		else {
			for ( int i = 0; i < words.length - 1; ++i)
				filename = ( words[ i] + '.');

			extension = ( '.' + words[ words.length - 1]);
		}

		DefaultTableModel defaultTableModel = ( DefaultTableModel)getModel();
		DefaultTableColumnModel defaultTableColumnModel = ( DefaultTableColumnModel)getColumnModel();
		for ( int i = 1; i < defaultTableModel.getRowCount(); ++i) {
			InitialValueMap initialValueMap = new InitialValueMap();
			JCheckBox checkBox = ( JCheckBox)defaultTableModel.getValueAt( i, _exportCheckBoxColumn);
			if ( !checkBox.isSelected())
				continue;

			String experimentName = ( String)defaultTableModel.getValueAt( i, _nameColumn);

			for ( int j = _startColumn; j < defaultTableColumnModel.getColumnCount(); ++j) {
				String alias = ( String)defaultTableColumnModel.getColumn( j).getHeaderValue();
				String initialValue = ( String)defaultTableModel.getValueAt( i, j);
				initialValueMap.put( alias, initialValue);
			}

			File newFile = new File( file.getParent() + File.separator + filename + experimentName + extension);
			Exporter.execute_on_model_builder( newFile, initialValueMap, null, experimentName, true, false, false);
		}

		return true;
	}

	/**
	 * Returns true for exporting this table component data to a file in CSV format successfully.
	 * @return true for exporting this table component data to a file in CSV format successfully
	 */
	public boolean export_table() {
		File file = CommonTool.get_save_file(
			Environment._exportTableDirectoryKey,
			ResourceManager.get_instance().get( "edit.experiment.dialog.export.table.button.name"),
			new String[] { "csv", "txt"},
			"SOARS experimental table data",
			_parent);

		if ( null == file)
			return false;

		String absoluteName = file.getAbsolutePath();
		String name = file.getName();
		int index = name.lastIndexOf( '.');
		if ( -1 == index)
			file = new File( absoluteName + ".txt");
		else if ( name.length() - 1 == index)
			file = new File( absoluteName + "txt");

		String[] aliases = _experimentManager.get_aliases();
		if ( null == aliases)
			return false;

		String[] comments = get_comments();
		if ( null == comments)
			return false;

		String[][] tableData = get_table_data();
		if ( null == tableData || 0 == tableData.length)
			return false;

		if ( !ExperimentTableExporter.execute( file, aliases, comments, tableData))
			return false;

		return true;
	}

	/**
	 * @return
	 */
	private String[] get_comments() {
		DefaultTableModel defaultTableModel = ( DefaultTableModel)getModel();
		DefaultTableColumnModel defaultTableColumnModel = ( DefaultTableColumnModel)getColumnModel();
		if ( 2 > defaultTableModel.getRowCount() || _startColumn >= defaultTableColumnModel.getColumnCount())
			return null;

		String[] comments = new String[ defaultTableColumnModel.getColumnCount()];
		for ( int i = 0; i < defaultTableColumnModel.getColumnCount(); ++i)
			comments[ i] = ( ( _startColumn > i) ? "" : ( String)defaultTableModel.getValueAt( 0, i));

		return comments;
	}

	/**
	 * @return
	 */
	private String[][] get_table_data() {
		DefaultTableModel defaultTableModel = ( DefaultTableModel)getModel();
		DefaultTableColumnModel defaultTableColumnModel = ( DefaultTableColumnModel)getColumnModel();
		if ( 2 > defaultTableModel.getRowCount() || _startColumn >= defaultTableColumnModel.getColumnCount())
			return null;

		String[][] tableData = new String[ defaultTableModel.getRowCount() - 1][ defaultTableColumnModel.getColumnCount()];
		for ( int i = 1; i < defaultTableModel.getRowCount(); ++i) {
			for ( int j = 0; j < defaultTableColumnModel.getColumnCount(); ++j) {
				if ( _exportCheckBoxColumn == j) {
					JCheckBox checkBox = ( JCheckBox)defaultTableModel.getValueAt( i, j);
					tableData[ i - 1][ j] = ( checkBox.isSelected() ? "true" : "false");
				} else
					tableData[ i - 1][ j] = ( String)defaultTableModel.getValueAt( i, j);
			}
		}

		return tableData;
	}

	/**
	 * Returns true for importing this table component data from a file in CSV format successfully.
	 * @return true for importing this table component data from a file in CSV format successfully
	 */
	public boolean import_table() {
		File file = CommonTool.get_open_file(
			Environment._importTableDirectoryKey,
			ResourceManager.get_instance().get( "edit.experiment.dialog.import.table.button.name"),
			new String[] { "csv", "txt"},
			"SOARS experimental table data",
			_parent);

		if ( null == file)
			return false;

		ExperimentTableImporter experimentTableImporter = new ExperimentTableImporter();
		if ( !experimentTableImporter.load( file))
			return false;

		String[] aliases = experimentTableImporter.get_aliases();
		if ( null == aliases || 0 == aliases.length)
			return false;

		String[] comments = experimentTableImporter.get_comments();
		if ( null == comments || 0 == comments.length)
			return false;

		String[][] tableData = experimentTableImporter.get_table_data();
		if ( null == tableData || 0 == tableData.length)
			return false;

		boolean appendNewData = false;
		if ( new_data_exists( tableData, experimentTableImporter.export_exists())) {
			int result = JOptionPane.showConfirmDialog(
				_parent,
				ResourceManager.get_instance().get( "edit.experiment.dialog.experiment.table.confirm.append.new.row.message"),
				ResourceManager.get_instance().get( "edit.experiment.dialog.experiment.table.confirm.append.new.row.title"),
				JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.INFORMATION_MESSAGE);
			switch ( result) {
				case 0:
					appendNewData = true;
					break;
				case 1:
					appendNewData = false;
					break;
				default:
					return true;
			}
		}

		update_comments( aliases, comments, experimentTableImporter.export_exists());

		update_table_data( aliases, tableData, experimentTableImporter.export_exists(), appendNewData);

		update_checkBoxes( tableData, experimentTableImporter.export_exists());

		repaint();

		return true;
	}

	/**
	 * @param tableData
	 * @param exportExists
	 * @return
	 */
	private boolean new_data_exists(String[][] tableData, boolean exportExists) {
		DefaultTableModel defaultTableModel = ( DefaultTableModel)getModel();
		for ( int i = 0; i < tableData.length; ++i) {
			if ( 1 > get_row( tableData[ i], exportExists, defaultTableModel))
				return true;
		}
		return false;
	}

	/**
	 * @param aliases
	 * @param comments
	 * @param exportExists
	 */
	private void update_comments(String[] aliases, String[] comments, 	boolean exportExists) {
		DefaultTableModel defaultTableModel = ( DefaultTableModel)getModel();
		for ( int i = 0; i < aliases.length; ++i) {
			int column = get_column( aliases[ i]);
			if ( 0 > column)
				continue;

			defaultTableModel.setValueAt( comments[ !exportExists ? column - 1 : column], 0, column);
		}
	}

	/**
	 * @param aliases
	 * @param tableData
	 * @param exportExists
	 * @param appendNewData
	 */
	private void update_table_data(String[] aliases, String[][] tableData, boolean exportExists, boolean appendNewData) {
		DefaultTableModel defaultTableModel = ( DefaultTableModel)getModel();

		for ( int i = 0; i < tableData.length; ++i) {
			int row = get_row( tableData[ i], exportExists, defaultTableModel);
			if ( 0 < row) {
				defaultTableModel.setValueAt( tableData[ i][ !exportExists ? 1 : 2], row, _commentColumn);
				for ( int j = 0; j < aliases.length; ++j) {
					int column = get_column( aliases[ j]);
					if ( 0 > column)
						continue;

					defaultTableModel.setValueAt( tableData[ i][ !exportExists ? column - 1 : column], row, column);
				}
			} else {
				if ( appendNewData) {
					Object[] objects = new Object[ defaultTableModel.getColumnCount()];
					objects[ _exportCheckBoxColumn] = new JCheckBox( "", false);
					objects[ _nameColumn] = tableData[ i][ !exportExists ? 0 : 1];
					objects[ _commentColumn] = tableData[ i][ !exportExists ? 1 : 2];
					for ( int j = 0; j < aliases.length; ++j) {
						int column = get_column( aliases[ j]);
						if ( 0 > column)
							continue;

						objects[ column] = tableData[ i][ !exportExists ? column - 1 : column];
					}

					//defaultTableModel.addRow( objects);
					//super.append_row();
					addRow( objects);
//					_experimentTableBase.append_row();
				}
			}
		}
//
//		for ( int i = 0; i < aliases.length; ++i) {
//			int column = get_column( aliases[ i]);
//			if ( 0 > column)
//				continue;
//
//			for ( int j = 0; j < table_data.length; ++j)
//				update_table_data( defaultTableModel, table_data[ j], column, export_exists, append_new_data);
//		}
	}

	/**
	 * @param alias
	 * @return
	 */
	private int get_column(String alias) {
		DefaultTableColumnModel defaultTableColumnModel = ( DefaultTableColumnModel)getColumnModel();
		for ( int i = _startColumn; i < defaultTableColumnModel.getColumnCount(); ++i) {
			TableColumn tableColumn = defaultTableColumnModel.getColumn( i);
			String value = ( String)tableColumn.getHeaderValue();
			if ( value.equals( alias))
				return i;
		}
		return -1;
	}

//	/**
//	 * @param defaultTableModel
//	 * @param row_data
//	 * @param column
//	 * @param export_exists
//	 * @param append_new_data
//	 */
//	private void update_table_data(DefaultTableModel defaultTableModel, String[] row_data, int column, boolean export_exists, boolean append_new_data) {
//		int row = get_row( row_data, export_exists, defaultTableModel);
//		if ( 0 < row)
//			defaultTableModel.setValueAt( row_data[ !export_exists ? column - 1 : column], row, column);
//		else {
//			if ( append_new_data) {
//				Object[] objects = new Object[ defaultTableModel.getColumnCount()];
//				objects[ _export_check_box_column] = new JCheckBox( "", false);
//				objects[ _name_column] = row_data[ !export_exists ? 0 : 1];
//				objects[ _comment_column] = row_data[ !export_exists ? 1 : 2];
//				objects[ column] = row_data[ !export_exists ? column - 1 : column];
//				defaultTableModel.addRow( objects);
//				super.append_row();
//				_experimentTableBase.append_row();
//			}
//		}
//	}

	/**
	 * @param tableData
	 * @param exportExists
	 */
	private void update_checkBoxes(String[][] tableData, boolean exportExists) {
		if ( !exportExists)
			return;

		DefaultTableModel defaultTableModel = ( DefaultTableModel)getModel();
		for ( int i = 0; i < tableData.length; ++i) {
			int row = get_row( tableData[ i], exportExists, defaultTableModel);
			if ( 1 > row)
				continue;

			JCheckBox checkBox = ( JCheckBox)defaultTableModel.getValueAt( row, _exportCheckBoxColumn);
			checkBox.setSelected( tableData[ i][ 0].equals( "true"));
		}
	}

	/**
	 * @param rowData
	 * @param exportExists
	 * @param defaultTableModel
	 * @return
	 */
	private int get_row(String[] rowData, boolean exportExists, DefaultTableModel defaultTableModel) {
		for ( int i = 1; i < defaultTableModel.getRowCount(); ++i) {
			String value = ( String)defaultTableModel.getValueAt( i, _nameColumn);
			if ( value.equals( rowData[ !exportExists ? 0 : 1]))
				return i;
		}
		return -1;
	}

	/**
	 * Returns true for saving this component data successfully.
	 * @return true for saving this component data successfully
	 */
	public boolean on_ok() {
		DefaultTableModel defaultTableModel = ( DefaultTableModel)getModel();
		DefaultTableColumnModel defaultTableColumnModel = ( DefaultTableColumnModel)getColumnModel();

		int counter = 0;
		for ( int i = 1; i < defaultTableModel.getRowCount(); ++i) {
			JCheckBox checkBox = ( JCheckBox)defaultTableModel.getValueAt( i, _exportCheckBoxColumn);
			if ( checkBox.isSelected())
				++counter;
		}
		if ( 0 == counter) {
			JOptionPane.showMessageDialog(
				_parent,
				ResourceManager.get_instance().get( "edit.experiment.dialog.experiment.table.no.selection.message"),
				ResourceManager.get_instance().get( "application.title"),
				JOptionPane.ERROR_MESSAGE);
			return false;
		}

		for ( int i = 1; i < defaultTableModel.getRowCount(); ++i) {
			String name = ( String)defaultTableModel.getValueAt( i, _nameColumn);
			if ( name.equals( "") || contains( name, i)) {
				JOptionPane.showMessageDialog(
					_parent,
					ResourceManager.get_instance().get( "edit.experiment.dialog.experiment.table.duplicated.name.message")
						+ " : line " + String.valueOf( i + 1) + " - "+ name,
					ResourceManager.get_instance().get( "application.title"),
					JOptionPane.ERROR_MESSAGE);
				return false;
			}

			if ( name.equals( Constant._gridFunctionalObjectRootDirectory)) {
				JOptionPane.showMessageDialog(
					_parent,
					ResourceManager.get_instance().get( "edit.experiment.dialog.experiment.table.invalid.name.message")
						+ " : line " + String.valueOf( i + 1) + " - "+ name,
					ResourceManager.get_instance().get( "application.title"),
					JOptionPane.ERROR_MESSAGE);
				return false;
			}

			for ( int j = 0; j < name.length(); ++j) {
				if ( 0 <= Constant._prohibitedCharacters8.indexOf( name.charAt( j))) {
					JOptionPane.showMessageDialog(
						_parent,
						ResourceManager.get_instance().get( "edit.experiment.dialog.experiment.table.invalid.name.message")
							+ " : line " + String.valueOf( i + 1) + " - "+ name,
						ResourceManager.get_instance().get( "application.title"),
						JOptionPane.ERROR_MESSAGE);
					return false;
				}
			}

			if ( name.startsWith( " ") || name.startsWith( ".") || name.endsWith( " ") || name.endsWith( ".")) {
				JOptionPane.showMessageDialog(
					_parent,
					ResourceManager.get_instance().get( "edit.experiment.dialog.experiment.table.invalid.name.message")
						+ " : line " + String.valueOf( i + 1) + " - "+ name,
					ResourceManager.get_instance().get( "application.title"),
					JOptionPane.ERROR_MESSAGE);
				return false;
			}
		}

		save();

		return true;
	}

	/**
	 * 
	 */
	public void save() {
		_experimentManager.clear();

		DefaultTableModel defaultTableModel = ( DefaultTableModel)getModel();
		DefaultTableColumnModel defaultTableColumnModel = ( DefaultTableColumnModel)getColumnModel();

		for ( int i = 1; i < defaultTableModel.getRowCount(); ++i) {
			InitialValueMap initialValueMap = new InitialValueMap();
			for ( int j = 0; j < defaultTableColumnModel.getColumnCount(); ++j) {
				if ( _exportCheckBoxColumn == j) {
					JCheckBox checkBox = ( JCheckBox)defaultTableModel.getValueAt( i, j);
					initialValueMap._export = checkBox.isSelected();
				} else if ( _nameColumn == j) {
					String name = ( String)defaultTableModel.getValueAt( i, j);
					_experimentManager.put( name, initialValueMap);
				} else if ( _commentColumn == j) {
					String comment = ( String)defaultTableModel.getValueAt( i, j);
					initialValueMap._comment = comment;
				} else {
					String alias = ( String)defaultTableColumnModel.getColumn( j).getHeaderValue();
					String initial_value = ( String)defaultTableModel.getValueAt( i, j);
					initialValueMap.put( alias, initial_value);
				}
			}
		}

		for ( int i = _startColumn; i < defaultTableColumnModel.getColumnCount(); ++i) {
			String alias = ( String)defaultTableColumnModel.getColumn( i).getHeaderValue();
			String comment = ( String)defaultTableModel.getValueAt( 0, i);
			_experimentManager._commentMap.put( alias, comment);
		}
	}
}
