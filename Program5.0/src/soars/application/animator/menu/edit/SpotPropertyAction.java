/*
 * 2005/02/23
 */
package soars.application.animator.menu.edit;

import java.awt.event.ActionEvent;

import soars.application.animator.main.MainFrame;
import soars.common.utility.swing.menu.MenuAction;

/**
 * The menu handler of "Edit spot property" menu.
 * @author kurata / SOARS project
 */
public class SpotPropertyAction extends MenuAction {

	/**
	 * Creates the menu handler of "Edit spot property" menu
	 * @param name the Menu name
	 */
	public SpotPropertyAction(String name) {
		super(name);
	}

	/* (Non Javadoc)
	 * @see soars.common.utility.swing.menu.MenuAction#selected(java.awt.event.ActionEvent)
	 */
	public void selected(ActionEvent actionEvent) {
		MainFrame.get_instance().on_edit_spot_property(actionEvent);
	}
}
