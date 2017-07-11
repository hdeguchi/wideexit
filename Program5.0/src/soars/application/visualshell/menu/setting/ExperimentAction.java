/*
 * 2005/07/12
 */
package soars.application.visualshell.menu.setting;

import java.awt.event.ActionEvent;

import soars.application.visualshell.main.MainFrame;
import soars.common.utility.swing.menu.MenuAction;

/**
 * The menu handler of "Experinental support" menu.
 * @author kurata / SOARS project
 */
public class ExperimentAction extends MenuAction {

	/**
	 * Creates the menu handler of "Experinental support" menu
	 * @param name the Menu name
	 */
	public ExperimentAction(String name) {
		super(name);
	}

	/* (Non Javadoc)
	 * @see soars.common.utility.swing.menu.MenuAction#selected(java.awt.event.ActionEvent)
	 */
	public void selected(ActionEvent actionEvent) {
		MainFrame.get_instance().on_setting_experiment(actionEvent);
	}
}
