/**
 * 
 */
package soars.application.simulator.menu.simulation;

import java.awt.event.ActionEvent;

import javax.swing.JMenuItem;

import soars.common.utility.swing.menu.MenuAction;

/**
 * @author kurata
 *
 */
public class StopAction extends MenuAction {

	/**
	 * 
	 */
	private ISimulationMenuHandler _simulationMenuHandler = null;

	/**
	 * @param name
	 * @param simulationMenuHandler
	 */
	public StopAction(String name, ISimulationMenuHandler simulationMenuHandler) {
		super(name);
		_simulationMenuHandler = simulationMenuHandler;
	}

	/**
	 * @param name
	 * @param item
	 */
	public StopAction(String name, JMenuItem item) {
		super(name, item);
	}

	/* (non-Javadoc)
	 * @see soars.common.utility.swing.menu.MenuAction#selected(java.awt.event.ActionEvent)
	 */
	public void selected(ActionEvent actionEvent) {
		_simulationMenuHandler.on_simulation_stop(actionEvent);
	}
}
