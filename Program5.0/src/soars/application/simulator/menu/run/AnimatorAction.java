/**
 * 
 */
package soars.application.simulator.menu.run;

import java.awt.event.ActionEvent;

import javax.swing.JMenuItem;

import soars.common.utility.swing.menu.MenuAction;

/**
 * @author kurata
 *
 */
public class AnimatorAction extends MenuAction {

	/**
	 * 
	 */
	private IRunMenuHandler _runMenuHandler = null;

	/**
	 * @param name
	 * @param runMenuHandler
	 */
	public AnimatorAction(String name, IRunMenuHandler runMenuHandler) {
		super(name);
		_runMenuHandler = runMenuHandler;
	}

	/**
	 * @param name
	 * @param item
	 */
	public AnimatorAction(String name, JMenuItem item) {
		super(name, item);
	}

	/* (non-Javadoc)
	 * @see soars.common.utility.swing.menu.MenuAction#selected(java.awt.event.ActionEvent)
	 */
	public void selected(ActionEvent actionEvent) {
		_runMenuHandler.on_run_animator(actionEvent);
	}
}
