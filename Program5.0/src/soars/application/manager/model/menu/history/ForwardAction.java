/**
 * 
 */
package soars.application.manager.model.menu.history;

import java.awt.event.ActionEvent;

import soars.application.manager.model.main.MainFrame;
import soars.common.utility.swing.menu.MenuAction;

/**
 * The menu handler of "Forward" menu.
 * @author kurata / SOARS project
 */
public class ForwardAction extends MenuAction {

	/**
	 * Creates the menu handler of "Forward" menu
	 * @param name the Menu name
	 */
	public ForwardAction(String name) {
		super(name);
	}

	/* (non-Javadoc)
	 * @see soars.common.utility.swing.menu.MenuAction#selected(java.awt.event.ActionEvent)
	 */
	public void selected(ActionEvent actionEvent) {
		MainFrame.get_instance().on_history_forward(actionEvent);
	}
}
