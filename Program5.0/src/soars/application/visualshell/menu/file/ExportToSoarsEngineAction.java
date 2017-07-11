/*
 * Created on 2006/01/11
 */
package soars.application.visualshell.menu.file;

import java.awt.event.ActionEvent;

import soars.application.visualshell.main.MainFrame;
import soars.common.utility.swing.menu.MenuAction;

/**
 * The menu handler of "Export to SOARS Engine" menu.
 * @author kurata / SOARS project
 */
public class ExportToSoarsEngineAction extends MenuAction {

	/**
	 * Creates the menu handler of "Export to SOARS Engine" menu
	 * @param name the Menu name
	 */
	public ExportToSoarsEngineAction(String name) {
		super(name);
	}

	/* (Non Javadoc)
	 * @see soars.common.utility.swing.menu.MenuAction#selected(java.awt.event.ActionEvent)
	 */
	public void selected(ActionEvent actionEvent) {
		MainFrame.get_instance().on_file_export_to_soars_engine(actionEvent);
	}
}
