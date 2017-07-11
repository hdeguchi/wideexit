/*
 * 2005/03/23
 */
package soars.application.animator.menu.animation;

import java.awt.event.ActionEvent;

import soars.application.animator.main.MainFrame;
import soars.common.utility.swing.menu.MenuAction;

/**
 * The menu handler of "Forward" menu.
 * @author kurata / SOARS project
 */
public class ForwardAction extends MenuAction {

	/**
	 * Creates the menu handler of "Forward" menu
	 * @param name the Menu name
	 */
	public ForwardAction(String name) {
		super(name);
	}

	/* (Non Javadoc)
	 * @see soars.common.utility.swing.menu.MenuAction#selected(java.awt.event.ActionEvent)
	 */
	public void selected(ActionEvent actionEvent) {
		MainFrame.get_instance().on_forward(actionEvent);
	}
}
