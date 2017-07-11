/*
 * Created on 2005/11/28
 */
package soars.application.visualshell.menu.file;

import java.awt.event.ActionEvent;

import soars.application.visualshell.main.MainFrame;
import soars.common.utility.swing.menu.MenuAction;

/**
 * The menu handler of "Export initial data" menu.
 * @author kurata / SOARS project
 */
public class ExportInitialDataAction extends MenuAction {

	/**
	 * Creates the menu handler of "Export initial data" menu
	 * @param name the Menu name
	 */
	public ExportInitialDataAction(String name) {
		super(name);
	}

	/* (Non Javadoc)
	 * @see soars.common.utility.swing.menu.MenuAction#selected(java.awt.event.ActionEvent)
	 */
	public void selected(ActionEvent actionEvent) {
		MainFrame.get_instance().on_file_export_initial_data(actionEvent);
	}
}
