/*
 * 2005/05/26
 */
package soars.common.utility.swing.table.base;

import java.awt.Color;
import java.awt.Component;
import java.awt.Frame;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.EventObject;
import java.util.Vector;

import javax.swing.CellEditor;
import javax.swing.JPopupMenu;
import javax.swing.JTable;
import javax.swing.table.DefaultTableColumnModel;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellEditor;
import javax.swing.text.JTextComponent;

import soars.common.utility.swing.gui.UserInterface;
import soars.common.utility.swing.tool.SwingTool;

/**
 * @author kurata
 */
public class StandardTable extends JTable {

	/**
	 * 
	 */
	protected UserInterface _userInterface = null;

	/**
	 * 
	 */
	protected JPopupMenu _popupMenu = null;

	/**
	 * 
	 */
	protected boolean _enabledAllTextSelectionAtTextEditorWhenEditingStart = true;

	/**
	 * 
	 */
	protected Frame _owner = null;

	/**
	 * 
	 */
	protected Component _parent = null;

	/**
	 * @param owner
	 * @param parent
	 */
	public StandardTable(Frame owner, Component parent) {
		super();
		_owner = owner;
		_parent = parent;
	}

	/**
	 * @param owner
	 * @param parent
	 * @param enabledAllTextSelectionAtTextEditorWhenEditingStart
	 */
	public StandardTable(Frame owner, Component parent, boolean enabledAllTextSelectionAtTextEditorWhenEditingStart) {
		super();
		_owner = owner;
		_parent = parent;
		_enabledAllTextSelectionAtTextEditorWhenEditingStart = enabledAllTextSelectionAtTextEditorWhenEditingStart;
	}

	/**
	 * @param popupMenu
	 * @return
	 */
	public boolean setup(boolean popupMenu) {
		setGridColor( new Color( 128, 128, 128));


		addMouseListener( new MouseListener() {
			public void mousePressed(MouseEvent arg0) {
				on_mouse_pressed( arg0);
			}
			public void mouseClicked(MouseEvent arg0) {
				if ( SwingTool.is_mouse_left_button_double_click( arg0))
					on_mouse_left_double_click( arg0);
			}
			public void mouseReleased(MouseEvent arg0) {
				if ( SwingTool.is_mouse_right_button( arg0))
					on_mouse_right_up( arg0);
				else
					on_mouse_released( arg0);
			}
			public void mouseEntered(MouseEvent arg0) {
				on_mouse_entered( arg0);
			}
			public void mouseExited(MouseEvent arg0) {
				on_mouse_clicked( arg0);
			}
		});

		addMouseMotionListener( new MouseMotionListener() {
			public void mouseMoved(MouseEvent arg0) {
				on_mouse_moved( arg0);
			}
			public void mouseDragged(MouseEvent arg0) {
				on_mouse_dragged( arg0);
			}
		});

		addKeyListener( new KeyListener() {
			public void keyPressed(KeyEvent arg0) {
				on_key_pressed( arg0);
			}
			public void keyReleased(KeyEvent arg0) {
				on_key_released( arg0);
			}
			public void keyTyped(KeyEvent arg0) {
			}
		});


		setup_key_event();


		if ( popupMenu)
			setup_popup_menu();


		return true;
	}

	/**
	 * @param mouseEvent
	 */
	protected void on_mouse_pressed(MouseEvent mouseEvent) {
	}

	/**
	 * @param mouseEvent
	 */
	protected void on_mouse_left_double_click(MouseEvent mouseEvent) {
	}

	/**
	 * @param mouseEvent
	 */
	protected void on_mouse_right_up(MouseEvent mouseEvent) {
	}

	/**
	 * @param mouseEvent
	 */
	protected void on_mouse_released(MouseEvent mouseEvent) {
	}

	/**
	 * @param mouseEvent
	 */
	protected void on_mouse_entered(MouseEvent mouseEvent) {
	}

	/**
	 * @param mouseEvent
	 */
	protected void on_mouse_clicked(MouseEvent mouseEvent) {
	}

	/**
	 * @param mouseEvent
	 */
	protected void on_mouse_moved(MouseEvent mouseEvent) {
	}

	/**
	 * @param mouseEvent
	 */
	protected void on_mouse_dragged(MouseEvent mouseEvent) {
	}

	/**
	 * @param keyEvent
	 */
	protected void on_key_pressed(KeyEvent keyEvent) {
	}

	/**
	 * @param keyEvent
	 */
	protected void on_key_released(KeyEvent keyEvent) {
	}

	/**
	 * 
	 */
	protected void setup_key_event() {
	}

	/**
	 * 
	 */
	protected void setup_popup_menu() {
		_userInterface = new UserInterface();
		_popupMenu = new JPopupMenu();
	}

	/**
	 * @param columnWidths
	 * @return
	 */
	public boolean setup_column(Vector<Integer> columnWidths) {
		return true;
	}

	/**
	 * @param columnWidths
	 */
	public void get_column_widths(Vector<Integer> columnWidths) {
		columnWidths.clear();

		DefaultTableColumnModel defaultTableColumnModel = ( DefaultTableColumnModel)getColumnModel();
		for ( int i = 0; i < defaultTableColumnModel.getColumnCount(); ++i)
			columnWidths.add( new Integer( defaultTableColumnModel.getColumn( i).getPreferredWidth()));
	}

	/**
	 * @param columnCount
	 */
	public void setColumnCount(int columnCount) {
		DefaultTableModel defaultTableModel = ( DefaultTableModel)getModel();
		defaultTableModel.setColumnCount( columnCount);
	}

	/**
	 * @param object
	 * @param row
	 * @param column
	 */
	public void setValueAtDefault(Object object, int row, int column) {
		DefaultTableModel defaultTableModel = ( DefaultTableModel)getModel();
		defaultTableModel.setValueAt(object, row, column);
	}

	/**
	 * 
	 */
	public void addRow() {
		addRow( new Object[ getColumnCount()]);
	}

	/**
	 * @param rowData
	 */
	public void addRow(Object[] rowData) {
		DefaultTableModel defaultTableModel = ( DefaultTableModel)getModel();
		defaultTableModel.addRow( rowData);
	}

	/**
	 * @param row
	 */
	public void insertRow(int row) {
		insertRow( row, new Object[ getColumnCount()]);
	}

	/**
	 * @param row
	 * @param rowData
	 */
	public void insertRow(int row, Object[] rowData) {
		DefaultTableModel defaultTableModel = ( DefaultTableModel)getModel();
		defaultTableModel.insertRow( row, rowData);
	}

	/**
	 * @param row
	 */
	public void removeRow(int row) {
		DefaultTableModel defaultTableModel = ( DefaultTableModel)getModel();
		defaultTableModel.removeRow( row);
	}

	/**
	 * @param columnName
	 */
	public void addColumn(Object columnName) {
		DefaultTableModel defaultTableModel = ( DefaultTableModel)getModel();
		defaultTableModel.addColumn( columnName);
	}

	/**
	 * @param columnData
	 */
	public void addColumn(Object[] columnData) {
		DefaultTableModel defaultTableModel = ( DefaultTableModel)getModel();
		defaultTableModel.addColumn( columnData);
	}

	/**
	 * @param columnName
	 * @param columnData
	 */
	public void addColumn(Object columnName, Object[] columnData) {
		DefaultTableModel defaultTableModel = ( DefaultTableModel)getModel();
		defaultTableModel.addColumn( columnName, columnData);
	}

	/* (non-Javadoc)
	 * @see javax.swing.JTable#prepareEditor(javax.swing.table.TableCellEditor, int, int)
	 */
	@Override
	public Component prepareEditor(TableCellEditor editor, int row, int column) {
		Component component = super.prepareEditor(editor, row, column);
		//--- component が JTextComponent の場合、初期状態でテキストを全選択する
		if (component instanceof JTextComponent && _enabledAllTextSelectionAtTextEditorWhenEditingStart) {
			((JTextComponent)component).selectAll();
		}
		return component;
	}

	/** セル編集停止
	 * 
	 */
	public void stop_cell_editing() {
		if ( !isEditing())
			return;

		CellEditor cellEditor = getCellEditor();
		if ( null != cellEditor)
			cellEditor.stopCellEditing();
	}

	/* (non-Javadoc)
	 * @see javax.swing.JTable#editCellAt(int, int, java.util.EventObject)
	 */
	@Override
	public boolean editCellAt(int arg0, int arg1, EventObject arg2) {
		if ( arg2 instanceof KeyEvent) {
//			System.out.println("[" + System.currentTimeMillis() + "] SpreadSheetTable#editCellAt(" + arg0 + ", " + arg1 + ", " + String.valueOf(arg2) + ")");
			KeyEvent keyEvent = ( KeyEvent)arg2;
			switch ( keyEvent.getKeyCode()) {
				case 0 :													// (Mac)[Fn]
				case KeyEvent.VK_CAPS_LOCK :		// (Mac)[caps]=20(0x14)
				case KeyEvent.VK_ESCAPE :				// [ESC]=27(0x1B)
				case KeyEvent.VK_NONCONVERT :		// (Win)[無変換]=29(0x1D)
				case KeyEvent.VK_META :					// (Mac)[command]=157(0x9D)
				case KeyEvent.VK_WINDOWS :			// (Win)[Windows]=524(0x20C)
				case KeyEvent.VK_CONTEXT_MENU :// (Win)[Menu]=525(0x20D)
				case KeyEvent.VK_NUM_LOCK :			// (Win)[NumLock]=144(0x90)
				case KeyEvent.VK_SCROLL_LOCK :	// (Win)[ScrollLock]=145(0x91)
				case KeyEvent.VK_INSERT :				// (Win)[Insert]=155(0x9B)
					return false;	// キー入力を無視
			}
//			System.out.println("  ke.getKeyChar()    =[" + keyEvent.getKeyChar() + "]");
//			System.out.println("  ke.getKeyCode()    =[" + keyEvent.getKeyCode() + "]");
//			System.out.println("  ke.getKeyLocation()=[" + keyEvent.getKeyLocation() + "]");
//			System.out.println("  ke.getModifiers()  =[" + keyEvent.getModifiers() + "]");
//			System.out.println("  ke.getModifiersEx()=[" + keyEvent.getModifiersEx() + "]");
//			System.out.println("  ke.getWhen()=[" + keyEvent.getWhen() + "]");
//			System.out.println("  ke.isActionKey()   =[" + keyEvent.isActionKey() + "]");
//			System.out.println("  ke.isAltDown()     =[" + keyEvent.isAltDown() + "]");
//			System.out.println("  ke.isAltGraphDown()=[" + keyEvent.isAltGraphDown() + "]");
//			System.out.println("  ke.isConsumed()    =[" + keyEvent.isConsumed() + "]");
//			System.out.println("  ke.isControlDown() =[" + keyEvent.isControlDown() + "]");
//			System.out.println("  ke.isMetaDown()    =[" + keyEvent.isMetaDown() + "]");
//			System.out.println("  ke.isShiftDown()   =[" + keyEvent.isShiftDown() + "]");
		}
		return super.editCellAt(arg0, arg1, arg2);
	}

//	/* (non-Javadoc)
//	 * @see javax.swing.JTable#processKeyBinding(javax.swing.KeyStroke, java.awt.event.KeyEvent, int, boolean)
//	 */
//	@Override
//	protected boolean processKeyBinding(KeyStroke arg0, KeyEvent arg1, int arg2, boolean arg3) {
//		if ( 0 <= System.getProperty( "os.name").indexOf( "Mac") && KeyEvent.VK_META == arg0.getKeyCode())
//			return false;
//
//		return super.processKeyBinding(arg0, arg1, arg2, arg3);
//	}
}
