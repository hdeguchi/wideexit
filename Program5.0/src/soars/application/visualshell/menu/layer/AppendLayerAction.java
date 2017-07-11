/*
 * 2005/04/25
 */
package soars.application.visualshell.menu.layer;

import java.awt.event.ActionEvent;

import soars.application.visualshell.main.MainFrame;
import soars.common.utility.swing.menu.MenuAction;

/**
 * The menu handler of "Append new layer" menu.
 * @author kurata / SOARS project
 */
public class AppendLayerAction extends MenuAction {

	/**
	 * Creates the menu handler of "Append new layer" menu
	 * @param name the Menu name
	 */
	public AppendLayerAction(String name) {
		super(name);
	}

	/* (Non Javadoc)
	 * @see soars.common.utility.swing.menu.MenuAction#selected(java.awt.event.ActionEvent)
	 */
	public void selected(ActionEvent actionEvent) {
		MainFrame.get_instance().on_append_layer( actionEvent);
	}
}
