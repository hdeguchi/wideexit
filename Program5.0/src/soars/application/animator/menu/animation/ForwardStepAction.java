/*
 * 2005/03/23
 */
package soars.application.animator.menu.animation;

import java.awt.event.ActionEvent;

import soars.application.animator.main.MainFrame;
import soars.common.utility.swing.menu.MenuAction;

/**
 * The menu handler of "Forward step" menu.
 * @author kurata / SOARS project
 */
public class ForwardStepAction extends MenuAction {

	/**
	 * Creates the menu handler of "Forward step" menu
	 * @param name the Menu name
	 */
	public ForwardStepAction(String name) {
		super(name);
	}

	/* (Non Javadoc)
	 * @see soars.common.utility.swing.menu.MenuAction#selected(java.awt.event.ActionEvent)
	 */
	public void selected(ActionEvent actionEvent) {
		MainFrame.get_instance().on_forward_step(actionEvent);
	}
}
