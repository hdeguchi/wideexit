/**
 * 
 */
package soars.application.visualshell.menu.file;

import java.awt.event.ActionEvent;

import soars.application.visualshell.main.MainFrame;
import soars.common.utility.swing.menu.MenuAction;

/**
 * The menu handler of "Open GIS Data" menu.
 * @author kurata / SOARS project
 */
public class OpenGisDataAction extends MenuAction {

	/**
	 * Creates the menu handler of "Open GIS Data" menu
	 * @param name the Menu name
	 */
	public OpenGisDataAction(String name) {
		super(name);
	}

	/* (non-Javadoc)
	 * @see soars.common.utility.swing.menu.MenuAction#selected(java.awt.event.ActionEvent)
	 */
	public void selected(ActionEvent actionEvent) {
		MainFrame.get_instance().on_file_open_gis_data(actionEvent);
	}
}
