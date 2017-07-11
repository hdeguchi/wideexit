/*
 * 2005/05/31
 */
package soars.application.visualshell.menu.setting;

import java.awt.event.ActionEvent;

import soars.application.visualshell.main.MainFrame;
import soars.common.utility.swing.menu.MenuAction;

/**
 * The menu handler of "Simulation" menu.
 * @author kurata / SOARS project
 */
public class SimulationAction extends MenuAction {

	/**
	 * Creates the menu handler of "Simulation" menu
	 * @param name the Menu name
	 */
	public SimulationAction(String name) {
		super(name);
	}

	/* (Non Javadoc)
	 * @see soars.common.utility.swing.menu.MenuAction#selected(java.awt.event.ActionEvent)
	 */
	public void selected(ActionEvent actionEvent) {
		MainFrame.get_instance().on_setting_simulation(actionEvent);
	}
}
