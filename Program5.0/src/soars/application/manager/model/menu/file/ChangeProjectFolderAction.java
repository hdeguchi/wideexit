/**
 * 
 */
package soars.application.manager.model.menu.file;

import java.awt.event.ActionEvent;

import soars.application.manager.model.main.MainFrame;
import soars.common.utility.swing.menu.MenuAction;

/**
 * The menu handler of "Change project folder" menu.
 * @author kurata / SOARS project
 */
public class ChangeProjectFolderAction extends MenuAction {

	/**
	 * Creates the menu handler of "Change project folder" menu
	 * @param name the Menu name
	 */
	public ChangeProjectFolderAction(String name) {
		super(name);
	}

	/* (non-Javadoc)
	 * @see soars.common.utility.swing.menu.MenuAction#selected(java.awt.event.ActionEvent)
	 */
	public void selected(ActionEvent actionEvent) {
		MainFrame.get_instance().on_file_change_project_folder(actionEvent);
	}
}
