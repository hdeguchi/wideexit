/**
 * 
 */
package soars.common.utility.swing.tree;

import java.awt.Component;
import java.awt.Frame;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;

import javax.swing.JPopupMenu;
import javax.swing.JTree;
import javax.swing.event.MouseInputAdapter;

import soars.common.utility.swing.gui.UserInterface;
import soars.common.utility.swing.tool.SwingTool;

/**
 * @author kurata
 *
 */
public class StandardTree extends JTree {

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
	public StandardTree(Frame owner, Component parent) {
		super();
		_owner = owner;
		_parent = parent;
	}

	/**
	 * @param popupMenu
	 * @return
	 */
	public boolean setup(boolean popupMenu) {
		addMouseListener( new MouseInputAdapter() {
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
