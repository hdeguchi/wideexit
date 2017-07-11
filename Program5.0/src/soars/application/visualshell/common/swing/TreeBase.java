/**
 * 
 */
package soars.application.visualshell.common.swing;

import java.awt.Component;
import java.awt.Frame;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.KeyStroke;

import soars.common.utility.swing.tree.StandardTree;

/**
 * The common Tree class for Visual Shell.
 * @author kurata / SOARS project
 */
public class TreeBase extends StandardTree {

	/**
	 * Creates the instance of the common Tree class with the specified data.
	 * @param owner the frame of the parent container
	 * @param parent the parent container of this component
	 */
	public TreeBase(Frame owner, Component parent) {
		super(owner, parent);
	}

	/**
	 * Returns true if this component is initialized successfully.
	 * @param popupMenu whether to use the context menu
	 * @return true if this component is initialized successfully
	 */
	public boolean setup(boolean popupMenu) {
		if ( !super.setup( popupMenu))
			return false;

		return true;
	}

	/* (non-Javadoc)
	 * @see soars.common.utility.swing.tree.StandardTree#setup_key_event()
	 */
	protected void setup_key_event() {
		super.setup_key_event();

		Action enterAction = new AbstractAction() {
			public void actionPerformed(ActionEvent e) {
				on_enter( e);
			}
		};
		getInputMap().put( KeyStroke.getKeyStroke( KeyEvent.VK_ENTER, 0), "enter");
		getActionMap().put( "enter", enterAction);
	}

	/* (non-Javadoc)
	 * @see soars.common.utility.swing.tree.StandardTree#on_mouse_right_up(java.awt.event.MouseEvent)
	 */
	protected void on_mouse_right_up(MouseEvent mouseEvent) {
		on_mouse_right_up( mouseEvent.getPoint());
	}

	/**
	 * @param point
	 */
	protected void on_mouse_right_up(Point point) {
	}

	/**
	 * @param actionEvent
	 */
	protected void on_enter(ActionEvent actionEvent) {
	}
}
