/*
 * 2005/01/28
 */
package soars.application.builder.animation.menu.help;

import java.awt.event.ActionEvent;

import javax.swing.JMenuItem;

import soars.application.builder.animation.main.MainFrame;

import soars.common.utility.swing.menu.MenuAction;

/**
 * @author kurata
 */
public class AboutAction extends MenuAction {

	/**
	 * @param name
	 */
	public AboutAction(String name) {
		super(name);
	}

	/**
	 * @param name
	 * @param item
	 */
	public AboutAction(String name, JMenuItem item) {
		super(name, item);
	}

	/* (Non Javadoc)
	 * @see soars.common.utility.swing.menu.MenuAction#selected(java.awt.event.ActionEvent)
	 */
	public void selected(ActionEvent actionEvent) {
		MainFrame.get_instance().on_help_about(actionEvent);
	}
}
