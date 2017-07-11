/*
 * 2005/04/26
 */
package soars.application.visualshell.menu.layer;

import java.awt.event.ActionEvent;

import soars.application.visualshell.main.MainFrame;
import soars.common.utility.swing.menu.MenuAction;

/**
 * The menu handler of "Remove layer" menu.
 * @author kurata / SOARS project
 */
public class RemoveLayerAction extends MenuAction {

	/**
	 * Creates the menu handler of "Remove layer" menu
	 * @param name the Menu name
	 */
	public RemoveLayerAction(String name) {
		super(name);
	}

	/* (Non Javadoc)
	 * @see soars.common.utility.swing.menu.MenuAction#selected(java.awt.event.ActionEvent)
	 */
	public void selected(ActionEvent actionEvent) {
		MainFrame.get_instance().on_remove_layer( actionEvent);
	}
}
