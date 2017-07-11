/*
 * 2005/04/01
 */
package soars.application.visualshell.menu.file;

import java.awt.event.ActionEvent;

import soars.application.visualshell.main.MainFrame;
import soars.common.utility.swing.menu.MenuAction;

/**
 * The menu handler of "Close" menu.
 * @author kurata / SOARS project
 */
public class CloseAction extends MenuAction {

	/**
	 * Creates the menu handler of "Close" menu
	 * @param name the Menu name
	 */
	public CloseAction(String name) {
		super(name);
	}

	/* (Non Javadoc)
	 * @see soars.common.utility.swing.menu.MenuAction#selected(java.awt.event.ActionEvent)
	 */
	public void selected(ActionEvent actionEvent) {
		MainFrame.get_instance().on_file_close(actionEvent);
	}
}
