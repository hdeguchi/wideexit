/*
 * 2005/03/23
 */
package soars.application.animator.menu.animation;

import java.awt.event.ActionEvent;

import soars.application.animator.main.MainFrame;
import soars.common.utility.swing.menu.MenuAction;

/**
 * The menu handler of "Forward tail" menu.
 * @author kurata / SOARS project
 */
public class ForwardTailAction extends MenuAction {

	/**
	 * Creates the menu handler of "Forward tail" menu
	 * @param name the Menu name
	 */
	public ForwardTailAction(String name) {
		super(name);
	}

	/* (Non Javadoc)
	 * @see soars.common.utility.swing.menu.MenuAction#selected(java.awt.event.ActionEvent)
	 */
	public void selected(ActionEvent actionEvent) {
		MainFrame.get_instance().on_forward_tail(actionEvent);
	}
}
