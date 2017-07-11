/**
 * 
 */
package soars.application.visualshell.menu.file;

import java.awt.event.ActionEvent;

import soars.application.visualshell.main.MainFrame;
import soars.common.utility.swing.menu.MenuAction;

/**
 * The menu handler of "Create docker fileset" menu.
 * @author kurata / SOARS project
 */
public class CreateDockerFilesetAction extends MenuAction {

	/**
	 * Creates the menu handler of "Create docker fileset" menu
	 * @param name the Menu name
	 */
	public CreateDockerFilesetAction(String name) {
		super(name);
	}

	/* (non-Javadoc)
	 * @see soars.common.utility.swing.menu.MenuAction#selected(java.awt.event.ActionEvent)
	 */
	@Override
	public void selected(ActionEvent actionEvent) {
		MainFrame.get_instance().on_file_create_docker_fileset(actionEvent);
	}
}
