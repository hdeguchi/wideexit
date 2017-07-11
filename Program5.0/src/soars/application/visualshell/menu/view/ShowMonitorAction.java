/**
 * 
 */
package soars.application.visualshell.menu.view;

import java.awt.event.ActionEvent;

import soars.application.visualshell.main.MainFrame;
import soars.common.utility.swing.menu.MenuAction;

/**
 * The menu handler of "Show monitor" menu.
 * @author kurata / SOARS project
 */
public class ShowMonitorAction extends MenuAction {

	/**
	 * Creates the menu handler of "Show monitor" menu
	 * @param name the Menu name
	 */
	public ShowMonitorAction(String name) {
		super(name);
	}

	/* (non-Javadoc)
	 * @see soars.common.utility.swing.menu.MenuAction#selected(java.awt.event.ActionEvent)
	 */
	public void selected(ActionEvent actionEvent) {
		MainFrame.get_instance().on_show_monitor(actionEvent);
	}
}
