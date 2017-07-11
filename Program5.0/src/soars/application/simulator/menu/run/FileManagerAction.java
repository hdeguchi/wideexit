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
public class FileManagerAction extends MenuAction {

	/**
	 * 
	 */
	private IRunMenuHandler _runMenuHandler = null;

	/**
	 * @param name
	 * @param runMenuHandler
	 */
	public FileManagerAction(String name, IRunMenuHandler runMenuHandler) {
		super(name);
		_runMenuHandler = runMenuHandler;
	}

	/**
	 * @param name
	 * @param item
	 */
	public FileManagerAction(String name, JMenuItem item) {
		super(name, item);
	}

	/* (non-Javadoc)
	 * @see soars.common.utility.swing.menu.MenuAction#selected(java.awt.event.ActionEvent)
	 */
	public void selected(ActionEvent actionEvent) {
		_runMenuHandler.on_run_file_manager(actionEvent);
	}
}
