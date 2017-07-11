/*
 * 2005/05/31
 */
package soars.application.visualshell.menu.setting;

import java.awt.event.ActionEvent;

import soars.application.visualshell.main.MainFrame;
import soars.common.utility.swing.menu.MenuAction;

/**
 * The menu handler of "Log1" menu.
 * @author kurata / SOARS project
 */
public class Log1Action extends MenuAction {

	/**
	 * Creates the menu handler of "Log1" menu
	 * @param name the Menu name
	 */
	public Log1Action(String name) {
		super(name);
	}

	/* (Non Javadoc)
	 * @see soars.common.utility.swing.menu.MenuAction#selected(java.awt.event.ActionEvent)
	 */
	public void selected(ActionEvent actionEvent) {
		MainFrame.get_instance().on_setting_log1(actionEvent);
	}
}
