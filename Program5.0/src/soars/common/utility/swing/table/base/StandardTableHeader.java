/**
 * 
 */
package soars.common.utility.swing.table.base;

import java.awt.Component;
import java.awt.Frame;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import javax.swing.JPopupMenu;
import javax.swing.JTable;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumnModel;

import soars.common.utility.swing.gui.UserInterface;

/**
 * @author kurata
 *
 */
public class StandardTableHeader extends JTableHeader {

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
	protected Frame _owner = null;

	/**
	 * 
	 */
	protected Component _parent = null;

	/**
	 * 
	 */
	public StandardTableHeader() {
		super();
	}

	/**
	 * @param tableColumnModel
	 * @param owner
	 * @param parent
	 */
	public StandardTableHeader(TableColumnModel tableColumnModel, Frame owner, Component parent) {
		super(tableColumnModel);
		_owner = owner;
		_parent = parent;
	}

	/**
	 * @param table
	 * @param tableCellRenderer
	 * @param popupMenu
	 */
	public boolean setup(JTable table, TableCellRenderer tableCellRenderer, boolean popupMenu) {
		addMouseListener( new MouseListener() {
			public void mouseReleased(MouseEvent e) {
				on_mouse_released( e);
			}
			public void mousePressed(MouseEvent e) {
				on_mouse_pressed( e);
			}
			public void mouseExited(MouseEvent e) {
				on_mouse_exited( e);
			}
			public void mouseEntered(MouseEvent e) {
				on_mouse_entered( e);
			}
			public void mouseClicked(MouseEvent e) {
				on_mouse_clicked( e);
			}
		});

		addMouseMotionListener( new MouseMotionListener() {
			public void mouseMoved(MouseEvent e) {
				on_mouse_moved( e);
			}
			public void mouseDragged(MouseEvent e) {
				on_mouse_dragged( e);
			}
		});

		if ( popupMenu)
			setup_popup_menu();

		setDefaultRenderer( tableCellRenderer);

		table.setTableHeader( this);

		setVisible( true);

		return true;
	}

	/**
	 * @param mouseEvent
	 */
	protected void on_mouse_released(MouseEvent mouseEvent) {
	}

	/**
	 * @param mouseEvent
	 */
	protected void on_mouse_pressed(MouseEvent mouseEvent) {
	}

	/**
	 * @param mouseEvent
	 */
	protected void on_mouse_exited(MouseEvent mouseEvent) {
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
	 * 
	 */
	protected void setup_popup_menu() {
		_userInterface = new UserInterface();
		_popupMenu = new JPopupMenu();
	}
}
