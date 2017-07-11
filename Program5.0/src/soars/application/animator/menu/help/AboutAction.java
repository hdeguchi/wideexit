/*
 * 2005/01/28
 */
package soars.application.animator.menu.help;

import java.awt.event.ActionEvent;

import soars.application.animator.main.MainFrame;
import soars.common.utility.swing.menu.MenuAction;

/**
 * The menu handler of "About SOARS Animator" menu.
 * @author kurata / SOARS project
 */
public class AboutAction extends MenuAction {

	/**
	 * Creates the menu handler of "About SOARS Animator" menu
	 * @param name the Menu name
	 */
	public AboutAction(String name) {
		super(name);
	}

	/* (Non Javadoc)
	 * @see soars.common.utility.swing.menu.MenuAction#selected(java.awt.event.ActionEvent)
	 */
	public void selected(ActionEvent actionEvent) {
		MainFrame.get_instance().on_help_about(actionEvent);
	}
}
