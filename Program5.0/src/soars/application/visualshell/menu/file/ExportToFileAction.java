/*
 * 2005/06/09
 */
package soars.application.visualshell.menu.file;

import java.awt.event.ActionEvent;

import soars.application.visualshell.main.MainFrame;
import soars.common.utility.swing.menu.MenuAction;

/**
 * The menu handler of "Export SOARS Engine script to file" menu.
 * @author kurata / SOARS project
 */
public class ExportToFileAction extends MenuAction {

	/**
	 * Creates the menu handler of "Export SOARS Engine script to file" menu
	 * @param name the Menu name
	 */
	public ExportToFileAction(String name) {
		super(name);
	}

	/* (Non Javadoc)
	 * @see soars.common.utility.swing.menu.MenuAction#selected(java.awt.event.ActionEvent)
	 */
	public void selected(ActionEvent actionEvent) {
		MainFrame.get_instance().on_file_export_to_file(actionEvent);
	}
}
