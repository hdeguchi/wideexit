/*
 * Created on 2006/05/10
 */
package soars.application.visualshell.menu.run;

import java.awt.event.ActionEvent;

import soars.application.visualshell.main.MainFrame;
import soars.common.utility.swing.menu.MenuAction;

/**
 * The menu handler of "Run(Model Builder)" menu.
 * @author kurata / SOARS project
 */
public class ModelBuilderAction extends MenuAction {

	/**
	 * Creates the menu handler of "Run(Model Builder)" menu
	 * @param name the Menu name
	 */
	public ModelBuilderAction(String name) {
		super(name);
	}

	/* (Non Javadoc)
	 * @see soars.common.utility.swing.menu.MenuAction#selected(java.awt.event.ActionEvent)
	 */
	public void selected(ActionEvent actionEvent) {
		MainFrame.get_instance().on_run_model_builder(actionEvent);
	}
}
