/*
 * 2005/01/31
 */
package soars.application.visualshell.menu.file;

import java.awt.event.ActionEvent;

import soars.application.visualshell.main.MainFrame;
import soars.common.utility.swing.menu.MenuAction;

/**
 * The menu handler of "Import script" menu.
 * @author kurata / SOARS project
 */
public class ImportAction extends MenuAction {

	/**
	 * Creates the menu handler of "Import script" menu
	 * @param name the Menu name
	 */
	public ImportAction(String name) {
		super(name);
	}

	/* (Non Javadoc)
	 * @see soars.common.utility.swing.menu.MenuAction#selected(java.awt.event.ActionEvent)
	 */
	public void selected(ActionEvent actionEvent) {
		MainFrame.get_instance().on_file_import(actionEvent);
	}
}
