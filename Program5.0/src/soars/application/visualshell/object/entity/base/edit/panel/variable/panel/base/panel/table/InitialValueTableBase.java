/**
 * 
 */
package soars.application.visualshell.object.entity.base.edit.panel.variable.panel.base.panel.table;

import java.awt.Color;
import java.awt.Component;
import java.awt.Frame;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JMenuItem;
import javax.swing.KeyStroke;
import javax.swing.event.ListSelectionEvent;
import javax.swing.table.DefaultTableModel;

import soars.application.visualshell.common.menu.basic1.IBasicMenuHandler1;
import soars.application.visualshell.common.menu.basic1.RemoveAction;
import soars.application.visualshell.common.menu.basic2.CopyAction;
import soars.application.visualshell.common.menu.basic2.CutAction;
import soars.application.visualshell.common.menu.basic2.IBasicMenuHandler2;
import soars.application.visualshell.common.menu.basic2.PasteAction;
import soars.application.visualshell.common.menu.basic2.SelectAllAction;
import soars.application.visualshell.common.swing.TableBase;
import soars.application.visualshell.main.ResourceManager;
import soars.application.visualshell.object.entity.base.EntityBase;
import soars.application.visualshell.object.entity.base.edit.panel.variable.panel.base.panel.VariablePanelBase;

/**
 * @author kurata
 *
 */
public class InitialValueTableBase extends TableBase implements IBasicMenuHandler1, IBasicMenuHandler2 {

	/**
	 * 
	 */
	protected Color _color = null;

	/**
	 * 
	 */
	protected EntityBase _entityBase = null;

	/**
	 * 
	 */
	protected VariablePanelBase _variablePanelBase = null;

	/**
	 * 
	 */
	public InitialValueTableBase _initialValueTableBase = null;

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
	private JMenuItem _selectAllMenuItem = null;

	/**
	 * 
	 */
	static private Object _lock = new Object();

	/**
	 * 
	 */
	static public Map<String, String>__typeMap = null;

	/**
	 * 
	 */
	static public Map<String, String>__nameMap = null;

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
			create__typeMap();
			create__nameMap();
		}
	}

	/**
	 * 
	 */
	private static void create__typeMap() {
		if ( null != __typeMap)
			return;

		__typeMap = new HashMap<String, String>();
		__typeMap.put( ResourceManager.get_instance().get( "edit.collection.value.dialog.label.agent"), "agent");
		__typeMap.put( ResourceManager.get_instance().get( "edit.collection.value.dialog.label.spot"), "spot");
		__typeMap.put( ResourceManager.get_instance().get( "edit.object.dialog.tree.probability"), "probability");
		__typeMap.put( ResourceManager.get_instance().get( "edit.object.dialog.tree.keyword"), "keyword");
		__typeMap.put( ResourceManager.get_instance().get( "edit.object.dialog.tree.number.object"), "number object");
		__typeMap.put( ResourceManager.get_instance().get( "edit.object.dialog.tree.role.variable"), "role variable");
		__typeMap.put( ResourceManager.get_instance().get( "edit.object.dialog.tree.time.variable"), "time variable");
		__typeMap.put( ResourceManager.get_instance().get( "edit.object.dialog.tree.spot.variable"), "spot variable");
		__typeMap.put( ResourceManager.get_instance().get( "edit.object.dialog.tree.collection"), "collection");
		__typeMap.put( ResourceManager.get_instance().get( "edit.object.dialog.tree.list"), "list");
		__typeMap.put( ResourceManager.get_instance().get( "edit.object.dialog.tree.map"), "map");
		__typeMap.put( ResourceManager.get_instance().get( "edit.object.dialog.tree.class.variable"), "class variable");
		__typeMap.put( ResourceManager.get_instance().get( "edit.object.dialog.tree.file"), "file");
		__typeMap.put( ResourceManager.get_instance().get( "edit.object.dialog.tree.exchange.algebra"), "exchange algebra");
		__typeMap.put( ResourceManager.get_instance().get( "edit.map.value.dialog.map.key.immediate.data"), "immediate");
	}

	/**
	 * 
	 */
	private static void create__nameMap() {
		if ( null != __nameMap)
			return;

		__nameMap = new HashMap<String, String>();
		__nameMap.put( "agent", ResourceManager.get_instance().get( "edit.collection.value.dialog.label.agent"));
		__nameMap.put( "spot", ResourceManager.get_instance().get( "edit.collection.value.dialog.label.spot"));
		__nameMap.put( "probability", ResourceManager.get_instance().get( "edit.object.dialog.tree.probability"));
		__nameMap.put( "keyword", ResourceManager.get_instance().get( "edit.object.dialog.tree.keyword"));
		__nameMap.put( "number object", ResourceManager.get_instance().get( "edit.object.dialog.tree.number.object"));
		__nameMap.put( "role variable", ResourceManager.get_instance().get( "edit.object.dialog.tree.role.variable"));
		__nameMap.put( "time variable", ResourceManager.get_instance().get( "edit.object.dialog.tree.time.variable"));
		__nameMap.put( "spot variable", ResourceManager.get_instance().get( "edit.object.dialog.tree.spot.variable"));
		__nameMap.put( "collection", ResourceManager.get_instance().get( "edit.object.dialog.tree.collection"));
		__nameMap.put( "list", ResourceManager.get_instance().get( "edit.object.dialog.tree.list"));
		__nameMap.put( "map", ResourceManager.get_instance().get( "edit.object.dialog.tree.map"));
		__nameMap.put( "class variable", ResourceManager.get_instance().get( "edit.object.dialog.tree.class.variable"));
		__nameMap.put( "file", ResourceManager.get_instance().get( "edit.object.dialog.tree.file"));
		__nameMap.put( "exchange algebra", ResourceManager.get_instance().get( "edit.object.dialog.tree.exchange.algebra"));
		__nameMap.put( "immediate", ResourceManager.get_instance().get( "edit.map.value.dialog.map.key.immediate.data"));
	}

	/**
	 * @param color
	 * @param entityBase
	 * @param variablePanelBase
	 * @param owner
	 * @param parent
	 */
	public InitialValueTableBase(Color color, EntityBase entityBase, VariablePanelBase variablePanelBase, Frame owner, Component parent) {
		super(owner, parent);
		_color = color;
		_entityBase = entityBase;
		_variablePanelBase = variablePanelBase;
	}

	/**
	 * 
	 */
	public void cleanup() {
		clearSelection();
		DefaultTableModel defaultTableModel = ( DefaultTableModel)getModel();
		while ( 0 < defaultTableModel.getRowCount())
			defaultTableModel.removeRow( 0);
	}

	/**
	 * @param initialValueTableBase
	 * @return
	 */
	public boolean setup(InitialValueTableBase initialValueTableBase) {
		if ( !super.setup(/*false*/true))
			return false;

		getTableHeader().setReorderingAllowed( false);
		setDefaultEditor( Object.class, null);
		setFillsViewportHeight( true);

		_initialValueTableBase = initialValueTableBase;

		return true;
	}

	/* (non-Javadoc)
	 * @see soars.common.utility.swing.table.StandardTable#on_mouse_pressed(java.awt.event.MouseEvent)
	 */
	@Override
	protected void on_mouse_pressed(MouseEvent mouseEvent) {
		if ( InputEvent.getModifiersExText( mouseEvent.getModifiersEx()).equals( "Command+Button1")
			|| InputEvent.getModifiersExText( mouseEvent.getModifiersEx()).equals( "Command+Button1+Button3")) {
			int[] rows = getSelectedRows();
			if ( 0 == rows.length) {
				int row = rowAtPoint( mouseEvent.getPoint());
				int column = columnAtPoint( mouseEvent.getPoint());
				if ( ( 0 <= row && getRowCount() > row)
					&& ( 0 <= column && getColumnCount() > column)) {
					addRowSelectionInterval( row, row);
					_initialValueTableBase.addRowSelectionInterval( row, row);
				}
			}
			return;
		}
		if ( 0 != ( InputEvent.CTRL_DOWN_MASK & mouseEvent.getModifiersEx())) {
			int[] rows = getSelectedRows();
			if ( 0 == rows.length) {
				int row = rowAtPoint( mouseEvent.getPoint());
				int column = columnAtPoint( mouseEvent.getPoint());
				if ( ( 0 <= row && getRowCount() > row)
					&& ( 0 <= column && getColumnCount() > column)) {
					addRowSelectionInterval( row, row);
					_initialValueTableBase.addRowSelectionInterval( row, row);
				}
			}
			return;
		}

		int[] rows = getSelectedRows();
		if ( 1 < rows.length)
			return;

		int row = rowAtPoint( mouseEvent.getPoint());
		int column = columnAtPoint( mouseEvent.getPoint());
		if ( ( 0 <= row && getRowCount() > row)
			&& ( 0 <= column && getColumnCount() > column)) {
			select( row);
			setColumnSelectionInterval( 0, getColumnCount() - 1);
			_initialValueTableBase.setColumnSelectionInterval( 0, _initialValueTableBase.getColumnCount() - 1);
		}
	}

	/* (non-Javadoc)
	 * @see javax.swing.JTable#valueChanged(javax.swing.event.ListSelectionEvent)
	 */
	@Override
	public void valueChanged(ListSelectionEvent arg0) {
		synchronize();
		super.valueChanged(arg0);
	}

	/**
	 * @return
	 */
	private boolean synchronize() {
		if ( !isFocusOwner())
			return true;

		int[] rows = getSelectedRows();
		if ( 0 == rows.length)
			return false;

		Arrays.sort( rows);

		_initialValueTableBase.clearSelection();

		for ( int row:rows)
			_initialValueTableBase.addRowSelectionInterval( row, row);

		_initialValueTableBase.setColumnSelectionInterval( 0, _initialValueTableBase.getColumnCount() - 1);

		return true;
	}

	/**
	 * Returns true if the data of the specified row number are selected successfully.
	 * @param row the specified row
	 * @return true if the data of the specified row number are selected successfully
	 */
	protected boolean select(int row) {
		clearSelection();
		addRowSelectionInterval( row, row);
		_initialValueTableBase.clearSelection();
		_initialValueTableBase.addRowSelectionInterval( row, row);
		return true;
	}

	/**
	 * @param row
	 */
	protected void scroll(int row) {
		Rectangle rect = getCellRect( row, 0, true);
		scrollRectToVisible( rect);
	}

	/**
	 * @param rows
	 */
	public boolean select(int[] rows) {
		return true;
	}

	/**
	 * @param rows
	 * @param rowIndex
	 */
	public void changeSelection(int[] rows, int rowIndex) {
	}

	/**
	 * 
	 */
	public void changeSelection() {
	}

	/* (non-Javadoc)
	 * @see soars.common.utility.swing.table.StandardTable#setup_popup_menu()
	 */
	@Override
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

		_popupMenu.addSeparator();

		_selectAllMenuItem = _userInterface.append_popup_menuitem(
			_popupMenu,
			ResourceManager.get_instance().get( "common.popup.menu.select.all.menu"),
			new SelectAllAction( ResourceManager.get_instance().get( "common.popup.menu.select.all.menu"), this),
			ResourceManager.get_instance().get( "common.popup.menu.select.all.mnemonic"),
			ResourceManager.get_instance().get( "common.popup.menu.select.all.stroke"));
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.common.swing.TableBase#on_mouse_right_up(java.awt.Point)
	 */
	@Override
	protected void on_mouse_right_up(Point point) {
		if ( null == _userInterface)
			return;

		if ( 0 == getRowCount()) {
			_removeMenuItem.setEnabled( false);
			_copyMenuItem.setEnabled( false);
			_cutMenuItem.setEnabled( false);
			_selectAllMenuItem.setEnabled( false);
		} else {
			int row = rowAtPoint( point);
			int column = columnAtPoint( point);
			if ( ( 0 <= row && getRowCount() > row)
				&& ( 0 <= column && getColumnCount() > column)) {
				int[] rows = getSelectedRows();
				boolean contains = ( 0 <= Arrays.binarySearch( rows, row));
				_removeMenuItem.setEnabled( contains);
				_copyMenuItem.setEnabled( contains);
				_cutMenuItem.setEnabled( contains);
			} else {
				_removeMenuItem.setEnabled( false);
				_copyMenuItem.setEnabled( false);
				_cutMenuItem.setEnabled( false);
			}

			_selectAllMenuItem.setEnabled( true);
		}

		_pasteMenuItem.setEnabled( can_paste( point));

		_popupMenu.show( this, point.x, point.y);
	}

	/**
	 * @param point
	 * @return
	 */
	protected boolean can_paste(Point point) {
		return false;
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.common.swing.TableBase#setup_key_event()
	 */
	@Override
	protected void setup_key_event() {
		super.setup_key_event();

		Action deleteAction = new AbstractAction() {
			public void actionPerformed(ActionEvent e) {
				on_remove();
			}
		};
		getInputMap().put( KeyStroke.getKeyStroke( KeyEvent.VK_DELETE, 0), "delete");
		getActionMap().put( "delete", deleteAction);


		Action backSpaceAction = new AbstractAction() {
			public void actionPerformed(ActionEvent e) {
				on_remove();
			}
		};
		getInputMap().put( KeyStroke.getKeyStroke( KeyEvent.VK_BACK_SPACE, 0), "backspace");
		getActionMap().put( "backspace", backSpaceAction);


		Action copyAction = new AbstractAction() {
			public void actionPerformed(ActionEvent e) {
				on_copy();
			}
		};
		//getInputMap().put( KeyStroke.getKeyStroke( KeyEvent.VK_C, Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()), "copy");
		getActionMap().put( "copy", copyAction);


		Action cutAction = new AbstractAction() {
			public void actionPerformed(ActionEvent e) {
				on_cut();
			}
		};
		//getInputMap().put( KeyStroke.getKeyStroke( KeyEvent.VK_X, Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()), "cut");
		getActionMap().put( "cut", cutAction);


		Action pasteAction = new AbstractAction() {
			public void actionPerformed(ActionEvent e) {
				on_paste();
			}
		};
		//getInputMap().put( KeyStroke.getKeyStroke( KeyEvent.VK_V, Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()), "paste");
		getActionMap().put( "paste", pasteAction);
	}

	/**
	 * 
	 */
	public void on_remove() {
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.common.menu.basic1.IBasicMenuHandler1#on_remove(java.awt.event.ActionEvent)
	 */
	@Override
	public void on_remove(ActionEvent actionEvent) {
	}

	/**
	 * @param actionEvent
	 */
	public void remove(int[] rows) {
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.common.menu.basic2.IBasicMenuHandler2#on_redo(java.awt.event.ActionEvent)
	 */
	@Override
	public void on_redo(ActionEvent actionEvent) {
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.common.menu.basic2.IBasicMenuHandler2#on_undo(java.awt.event.ActionEvent)
	 */
	@Override
	public void on_undo(ActionEvent actionEvent) {
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.common.menu.basic2.IBasicMenuHandler2#on_copy(java.awt.event.ActionEvent)
	 */
	@Override
	public void on_copy(ActionEvent actionEvent) {
		on_copy();
	}

	/**
	 * 
	 */
	public void on_copy() {
	}

	/**
	 * @param rows
	 */
	public void copy(int[] rows) {
	}

	/**
	 * 
	 */
	public void on_cut() {
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.common.menu.basic2.IBasicMenuHandler2#on_cut(java.awt.event.ActionEvent)
	 */
	@Override
	public void on_cut(ActionEvent actionEvent) {
		on_cut();
	}

	/**
	 * @param rows
	 */
	public void cut(int[] rows) {
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.common.menu.basic2.IBasicMenuHandler2#on_paste(java.awt.event.ActionEvent)
	 */
	@Override
	public void on_paste(ActionEvent actionEvent) {
		on_paste();
	}

	/**
	 * 
	 */
	public void on_paste() {
	}

	/**
	 * @param rows
	 */
	public void paste(int[] rows) {
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

	/**
	 * @param objects
	 */
	public void append(Object[] objects) {
	}

	/**
	 * @param objects
	 */
	public void insert(Object[] objects) {
	}

	/**
	 * @param objects
	 */
	public void update(Object[] objects) {
	}

	/**
	 * 
	 */
	public void append() {
	}

	/**
	 * @param row
	 */
	public void insert(int row) {
	}

	/**
	 * @param row
	 */
	public void removeRow(int row) {
		DefaultTableModel defaultTableModel = ( DefaultTableModel)getModel();
		if ( defaultTableModel.getRowCount() <= row)
			return;

		defaultTableModel.removeRow( row);
	}

	/**
	 * 
	 */
	public void up() {
	}

	/**
	 * @param rows
	 */
	public void exchange_on_up(int[] rows) {
		DefaultTableModel defaultTableModel = ( DefaultTableModel)getModel();
		for ( int i = 0; i < rows.length; ++i)
			exchange( rows[ i] - 1, rows[ i], defaultTableModel);
	}

	/**
	 * 
	 */
	public void down() {
	}

	/**
	 * @param rows
	 */
	public void exchange_on_down(int[] rows) {
		DefaultTableModel defaultTableModel = ( DefaultTableModel)getModel();
		for ( int i = rows.length - 1; i >= 0 ; --i)
			exchange( rows[ i], rows[ i] + 1, defaultTableModel);
	}

	/**
	 * @param i
	 * @param j
	 * @param defaultTableModel
	 */
	protected void exchange(int i, int j, DefaultTableModel defaultTableModel) {
		for ( int column = 0; column < defaultTableModel.getColumnCount(); ++column) {
			Object object = defaultTableModel.getValueAt( i, column);
			defaultTableModel.setValueAt( defaultTableModel.getValueAt( j, column), i, column);
			defaultTableModel.setValueAt( object, j, column);
		}
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
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.common.menu.basic1.IBasicMenuHandler1#on_insert(java.awt.event.ActionEvent)
	 */
	@Override
	public void on_insert(ActionEvent actionEvent) {
	}
}
