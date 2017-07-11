/*
 * Created on 2006/09/05
 */
package soars.application.animator.menu.option;

import java.awt.event.ActionEvent;

import soars.application.animator.main.MainFrame;
import soars.common.utility.swing.menu.MenuAction;

/**
 * The menu handler of "Repeat" menu.
 * @author kurata / SOARS project
 */
public class RepeatAction extends MenuAction {

	/**
	 * Creates the menu handler of "Repeat" menu
	 * @param name the Menu name
	 */
	public RepeatAction(String name) {
		super(name);
	}

	/* (Non Javadoc)
	 * @see soars.common.utility.swing.menu.MenuAction#selected(java.awt.event.ActionEvent)
	 */
	public void selected(ActionEvent actionEvent) {
		MainFrame.get_instance().on_option_repeat(actionEvent);
	}
}
