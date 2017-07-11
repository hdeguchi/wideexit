/*
 * 2005/04/27
 */
package soars.application.visualshell.menu.layer;

import java.awt.event.ActionEvent;

import soars.application.visualshell.main.MainFrame;
import soars.common.utility.swing.menu.MenuAction;

/**
 * The menu handler of "Insert new layer" menu.
 * @author kurata / SOARS project
 */
public class InsertLayerAction extends MenuAction {

	/**
	 * Creates the menu handler of "Insert new layer" menu
	 * @param name the Menu name
	 */
	public InsertLayerAction(String name) {
		super(name);
	}

	/* (Non Javadoc)
	 * @see soars.common.utility.swing.menu.MenuAction#selected(java.awt.event.ActionEvent)
	 */
	public void selected(ActionEvent actionEvent) {
		MainFrame.get_instance().on_insert_layer( actionEvent);
	}
}
