/**
 * 
 */
package soars.application.manager.model.menu.history;

import java.awt.event.ActionEvent;

import soars.application.manager.model.main.MainFrame;
import soars.common.utility.swing.menu.MenuAction;

/**
 * The menu handler of "Back" menu.
 * @author kurata / SOARS project
 */
public class BackAction extends MenuAction {

	/**
	 * Creates the menu handler of "Back" menu
	 * @param name the Menu name
	 */
	public BackAction(String name) {
		super(name);
	}

	/* (non-Javadoc)
	 * @see soars.common.utility.swing.menu.MenuAction#selected(java.awt.event.ActionEvent)
	 */
	public void selected(ActionEvent actionEvent) {
		MainFrame.get_instance().on_history_back(actionEvent);
	}
}
