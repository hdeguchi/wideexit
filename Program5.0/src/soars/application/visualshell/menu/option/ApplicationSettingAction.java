/*
 * Created on 2006/06/26
 */
package soars.application.visualshell.menu.option;

import java.awt.event.ActionEvent;

import soars.application.visualshell.main.MainFrame;
import soars.common.utility.swing.menu.MenuAction;

/**
 * The menu handler of "Application setting" menu.
 * @author kurata / SOARS project
 */
public class ApplicationSettingAction extends MenuAction {

	/**
	 * Creates the menu handler of "Application setting" menu
	 * @param name the Menu name
	 */
	public ApplicationSettingAction(String name) {
		super(name);
	}

	/* (Non Javadoc)
	 * @see soars.common.utility.swing.menu.MenuAction#selected(java.awt.event.ActionEvent)
	 */
	public void selected(ActionEvent actionEvent) {
		MainFrame.get_instance().on_application_setting(actionEvent);
	}
}
