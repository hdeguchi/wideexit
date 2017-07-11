/*
 * 2005/01/28
 */
package soars.tool.image.checker.body.menu.help;

import java.awt.event.ActionEvent;

import soars.common.utility.swing.menu.MenuAction;
import soars.tool.image.checker.body.main.MainFrame;

/**
 * The menu handler of "About SOARS Visual Shell" menu.
 * @author kurata / SOARS project
 */
public class AboutAction extends MenuAction {

	/**
	 * Creates the menu handler of "About SOARS Visual Shell" menu
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
