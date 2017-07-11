/**
 * 
 */
package soars.common.utility.swing.table.spread_sheet;

import java.awt.Component;
import java.awt.Event;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.util.Arrays;
import java.util.List;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.KeyStroke;
import javax.swing.event.ListSelectionEvent;

import soars.common.utility.swing.table.base.StandardTable;
import soars.common.utility.swing.table.base.undo_redo.cell.SetValueUndo;

/**
 * @author kurata
 *
 */
public class SpreadSheetTableBase extends StandardTable {

	/**
	 * 
	 */
	public SpreadSheetTableBase _spreadSheetTableBase = null;

	/**
	 * @param owner
	 * @param parent
	 */
	public SpreadSheetTableBase(Frame owner, Component parent) {
		super(owner, parent);
	}

	/* (non-Javadoc)
	 * @see soars.common.utility.swing.table.base.StandardTable#setup_key_event()
	 */
	protected void setup_key_event() {
		super.setup_key_event();

//		Action enterAction = new AbstractAction() {
//			public void actionPerformed(ActionEvent e) {
//				on_enter( e);
//			}
//		};
//		getInputMap().put( KeyStroke.getKeyStroke( KeyEvent.VK_ENTER, 0), "enter");
//		getActionMap().put( "enter", enterAction);
//
//
//		Action escapeAction = new AbstractAction() {
//			public void actionPerformed(ActionEvent e) {
//				on_escape( e);
//			}
//		};
//		getInputMap().put( KeyStroke.getKeyStroke( KeyEvent.VK_ESCAPE, 0), "escape");
//		getActionMap().put( "escape", escapeAction);


		Action deleteAction = new AbstractAction() {
			public void actionPerformed(ActionEvent e) {
				on_remove( e);
			}
		};
		getInputMap( WHEN_ANCESTOR_OF_FOCUSED_COMPONENT).put( KeyStroke.getKeyStroke( KeyEvent.VK_DELETE, 0), "delete");
		getActionMap().put( "delete", deleteAction);


//		Action backSpaceAction = new AbstractAction() {
//			public void actionPerformed(ActionEvent e) {
//				on_remove( e);
//			}
//		};
//		getInputMap( WHEN_ANCESTOR_OF_FOCUSED_COMPONENT).put( KeyStroke.getKeyStroke( KeyEvent.VK_BACK_SPACE, 0), "backspace");
//		getActionMap().put( "backspace", backSpaceAction);


		Action copyAction = new AbstractAction() {
			public void actionPerformed(ActionEvent e) {
				on_copy( e);
			}
		};
		//getInputMap( WHEN_ANCESTOR_OF_FOCUSED_COMPONENT).put( KeyStroke.getKeyStroke( KeyEvent.VK_C, Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()), "copy");
		getActionMap().put( "copy", copyAction);


		Action pasteAction = new AbstractAction() {
			public void actionPerformed(ActionEvent e) {
				on_paste( e);
			}
		};
		//getInputMap( WHEN_ANCESTOR_OF_FOCUSED_COMPONENT).put( KeyStroke.getKeyStroke( KeyEvent.VK_V, Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()), "paste");
		getActionMap().put( "paste", pasteAction);


		Action cutAction = new AbstractAction() {
			public void actionPerformed(ActionEvent e) {
				on_cut( e);
			}
		};
		//getInputMap( WHEN_ANCESTOR_OF_FOCUSED_COMPONENT).put( KeyStroke.getKeyStroke( KeyEvent.VK_X, Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()), "cut");
		getActionMap().put( "cut", cutAction);
	}

	/**
	 * @param actionEvent
	 */
	public void on_enter(ActionEvent actionEvent) {
	}

	/**
	 * @param actionEvent
	 */
	public void on_escape(ActionEvent actionEvent) {
	}

	/**
	 * @param actionEvent
	 */
	public void on_remove(ActionEvent actionEvent) {
	}

	/**
	 * @param actionEvent
	 */
	public void on_copy(ActionEvent actionEvent) {
	}

	/**
	 * @param actionEvent
	 */
	public void on_paste(ActionEvent actionEvent) {
	}

	/**
	 * @param actionEvent
	 */
	public void on_cut(ActionEvent actionEvent) {
	}

	/* (non-Javadoc)
	 * @see soars.common.utility.swing.table.base.StandardTable#on_mouse_right_up(java.awt.event.MouseEvent)
	 */
	protected void on_mouse_right_up(MouseEvent mouseEvent) {
		int row = rowAtPoint( mouseEvent.getPoint());
		if ( 0 > row || getRowCount() <= row)
			return;

		int column = columnAtPoint( mouseEvent.getPoint());
		if ( 0 > column || getColumnCount() <= column)
			return;

		on_mouse_right_up(row, column, mouseEvent);
	}

	/**
	 * @param row
	 * @param column
	 * @param mouseEvent
	 * @return
	 */
	protected boolean on_mouse_right_up(int row, int column, MouseEvent mouseEvent) {
		return true;
	}

	/* (non-Javadoc)
	 * @see javax.swing.JTable#valueChanged(javax.swing.event.ListSelectionEvent)
	 */
	public void valueChanged(ListSelectionEvent e) {
		synchronize();
		super.valueChanged(e);
	}

	/* (non-Javadoc)
	 * @see javax.swing.JTable#changeSelection(int, int, boolean, boolean)
	 */
	public void changeSelection(int rowIndex, int columnIndex, boolean toggle, boolean extend) {
		super.changeSelection(rowIndex, columnIndex, toggle, extend);
		synchronize();
	}

	/**
	 * @return
	 */
	protected boolean synchronize() {
		if ( !isFocusOwner())
			return true;

		return internal_synchronize();
	}

	/**
	 * @return
	 */
	protected boolean internal_synchronize() {
		int[] rows = getSelectedRows();
		if ( null == rows || 0 == rows.length)
			return false;

		Arrays.sort( rows);

		_spreadSheetTableBase.clearSelection();

		for ( int row:rows)
			_spreadSheetTableBase.addRowSelectionInterval( row, row);

		_spreadSheetTableBase.setColumnSelectionInterval( 0, _spreadSheetTableBase.getColumnCount() - 1);

		_spreadSheetTableBase.getTableHeader().repaint();

		getTableHeader().repaint();

		return true;
	}

	/**
	 * @param rows
	 */
	public void insertRows(int[] rows) {
	}

	/**
	 * @param rows
	 */
	public void removeRows(int[] rows) {
	}

	/**
	 * 
	 */
	public void setup_undo_redo_manager() {
		Action undoAction = new AbstractAction() {
			public void actionPerformed(ActionEvent e) {
				on_undo();
			}
		};
		getInputMap( WHEN_ANCESTOR_OF_FOCUSED_COMPONENT).put( KeyStroke.getKeyStroke( 'Z', ( 0 <= System.getProperty( "os.name").indexOf( "Mac")) ? Event.META_MASK : Event.CTRL_MASK), "undo");
		getActionMap().put( "undo", undoAction);


		Action redoAction = new AbstractAction() {
			public void actionPerformed(ActionEvent e) {
				on_redo();
			}
		};

		getInputMap( WHEN_ANCESTOR_OF_FOCUSED_COMPONENT).put( KeyStroke.getKeyStroke( 'Y', ( 0 <= System.getProperty( "os.name").indexOf( "Mac")) ? Event.META_MASK : Event.CTRL_MASK), "redo");
		getActionMap().put( "redo", redoAction);
	}

	/**
	 * 
	 */
	public void on_undo() {
	}

	/**
	 * 
	 */
	public void on_redo() {
	}

	/**
	 * @param rows
	 */
	public void on_indert_row(int[] rows) {
	}

	/**
	 * @param rows
	 */
	public void on_remove_row(int[] rows) {
	}

	/**
	 * @param rows
	 */
	public void on_copy_row(int[] rows) {
	}

	/**
	 * @param rows
	 * @param rowHeaderSetValueUndoLists
	 */
	public void on_cut_row(int[] rows, List<List<SetValueUndo>> rowHeaderSetValueUndoLists) {
	}

	/**
	 * @param row
	 * @param rowHeaderSetValueUndoLists
	 */
	public void on_paste_row(int row, List<List<SetValueUndo>> rowHeaderSetValueUndoLists) {
	}

	/**
	 * @param columns
	 */
	public void on_indert_column(int[] columns) {
	}

	/**
	 * @param columns
	 */
	public void on_remove_column(int[] columns) {
	}

	/**
	 * @param columns
	 */
	public void on_copy_column(int[] columns) {
	}

	/**
	 * @param columns
	 */
	public void on_cut_column(int[] columns) {
	}

	/**
	 * @param column
	 */
	public void on_paste_column(int column) {
	}

	/**
	 * @param row
	 * @return
	 */
	public boolean copied(int row) {
		return false;
	}
}
