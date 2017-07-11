/**
 * 
 */
package soars.application.simulator.menu.file;

import java.awt.event.ActionEvent;

import soars.application.simulator.main.MainFrame;
import soars.common.utility.swing.menu.MenuAction;

/**
 * The menu handler of "New" menu.
 * @author kurata / SOARS project
 */
public class NewAction extends MenuAction {

	/**
	 * Creates the menu handler of "New" menu
	 * @param name the Menu name
	 */
	public NewAction(String name) {
		super(name);
	}

	/* (non-Javadoc)
	 * @see soars.common.utility.swing.menu.MenuAction#selected(java.awt.event.ActionEvent)
	 */
	public void selected(ActionEvent actionEvent) {
		MainFrame.get_instance().on_file_new(actionEvent);
	}
}
