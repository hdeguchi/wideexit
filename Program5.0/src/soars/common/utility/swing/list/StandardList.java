/*
 * 2005/05/26
 */
package soars.common.utility.swing.list;

import java.awt.Component;
import java.awt.Frame;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JList;
import javax.swing.JPopupMenu;

import soars.common.utility.swing.gui.UserInterface;
import soars.common.utility.swing.tool.SwingTool;

/**
 * @author kurata
 */
public class StandardList extends JList {

	/**
	 * 
	 */
	protected DefaultComboBoxModel _defaultComboBoxModel = null;

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
	 * @param owner
	 * @param parent
	 */
	public StandardList(Frame owner, Component parent) {
		super();
		_owner = owner;
		_parent = parent;
	}

	/**
	 * @param popupMenu
	 * @return
	 */
	public boolean setup(boolean popupMenu) {
		_defaultComboBoxModel = new DefaultComboBoxModel();
		setModel( _defaultComboBoxModel);


		addMouseListener( new MouseListener() {
			public void mouseClicked(MouseEvent arg0) {
			}
			public void mouseEntered(MouseEvent arg0) {
			}
			public void mouseExited(MouseEvent arg0) {
			}
			public void mousePressed(MouseEvent arg0) {
				if ( SwingTool.is_mouse_left_button( arg0)) {
					if ( 1 == arg0.getClickCount())
						on_mouse_left_down( arg0);
					else if ( 2 == arg0.getClickCount())
						on_mouse_left_double_click( arg0);
				}
			}
			public void mouseReleased(MouseEvent arg0) {
				if ( SwingTool.is_mouse_right_button( arg0))
					on_mouse_right_up( arg0);
				else if ( 1 == arg0.getButton())
					on_mouse_left_up( arg0);
			}
		});

		addMouseMotionListener( new MouseMotionListener() {
			public void mouseDragged(MouseEvent arg0) {
				on_mouse_dragged( arg0);
			}
			public void mouseMoved(MouseEvent arg0) {
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
	 * @param mouseEvent
	 */
	protected void on_mouse_left_down(MouseEvent mouseEvent) {
	}

	/**
	 * @param mouseEvent
	 */
	protected void on_mouse_left_double_click(MouseEvent mouseEvent) {
	}

	/**
	 * @param mouseEvent
	 */
	protected void on_mouse_left_up(MouseEvent mouseEvent) {
	}

	/**
	 * @param mouseEvent
	 */
	protected void on_mouse_right_up(MouseEvent mouseEvent) {
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
}
