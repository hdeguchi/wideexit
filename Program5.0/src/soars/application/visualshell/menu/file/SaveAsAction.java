/*
 * 2005/02/09
 */
package soars.application.visualshell.menu.file;

import java.awt.event.ActionEvent;

import soars.application.visualshell.main.MainFrame;
import soars.common.utility.swing.menu.MenuAction;

/**
 * The menu handler of "Save as" menu.
 * @author kurata / SOARS project
 */
public class SaveAsAction extends MenuAction {

	/**
	 * Creates the menu handler of "Save as" menu
	 * @param name the Menu name
	 */
	public SaveAsAction(String name) {
		super(name);
	}

	/* (Non Javadoc)
	 * @see soars.common.utility.swing.menu.MenuAction#selected(java.awt.event.ActionEvent)
	 */
	public void selected(ActionEvent actionEvent) {
		MainFrame.get_instance().on_file_save_as(actionEvent);
	}
}
