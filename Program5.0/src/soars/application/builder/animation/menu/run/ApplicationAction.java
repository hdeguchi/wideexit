/*
 * 2005/02/02
 */
package soars.application.builder.animation.menu.run;

import java.awt.event.ActionEvent;

import javax.swing.JMenuItem;

import soars.application.builder.animation.main.MainFrame;

import soars.common.utility.swing.menu.MenuAction;

/**
 * @author kurata
 */
public class ApplicationAction extends MenuAction {

	/**
	 * @param name
	 */
	public ApplicationAction(String name) {
		super(name);
	}

	/**
	 * @param name
	 * @param item
	 */
	public ApplicationAction(String name, JMenuItem item) {
		super(name, item);
	}

	/* (Non Javadoc)
	 * @see soars.common.utility.swing.menu.MenuAction#selected(java.awt.event.ActionEvent)
	 */
	public void selected(ActionEvent actionEvent) {
		MainFrame.get_instance().on_run_application(actionEvent);
	}
}
