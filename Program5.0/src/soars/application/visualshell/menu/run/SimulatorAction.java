/**
 * 
 */
package soars.application.visualshell.menu.run;

import java.awt.event.ActionEvent;

import soars.application.visualshell.main.MainFrame;
import soars.common.utility.swing.menu.MenuAction;

/**
 * The menu handler of "Run(Simulator)" menu.
 * @author kurata / SOARS project
 */
public class SimulatorAction extends MenuAction {

	/**
	 * Creates the menu handler of "Run(Simulator)" menu
	 * @param name the Menu name
	 */
	public SimulatorAction(String name) {
		super(name);
	}

	/* (non-Javadoc)
	 * @see soars.common.utility.swing.menu.MenuAction#selected(java.awt.event.ActionEvent)
	 */
	@Override
	public void selected(ActionEvent actionEvent) {
		MainFrame.get_instance().on_run_simulator(actionEvent);
	}
}
