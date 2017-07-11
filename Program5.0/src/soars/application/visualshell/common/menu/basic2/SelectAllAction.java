/*
 * 2005/06/28
 */
package soars.application.visualshell.common.menu.basic2;

import java.awt.event.ActionEvent;

import soars.common.utility.swing.menu.MenuAction;

/**
 * The menu handler of "Select all" menu.
 * @author kurata / SOARS project
 */
public class SelectAllAction extends MenuAction {

	/**
	 * 
	 */
	private IBasicMenuHandler2 _basicMenuHandler2 = null;

	/**
	 * Creates the menu handler of "Select all" menu
	 * @param name the Menu name
	 * @param basicMenuHandler2 the common menu handler interface
	 */
	public SelectAllAction(String name, IBasicMenuHandler2 basicMenuHandler2) {
		super(name);
		_basicMenuHandler2 = basicMenuHandler2;
	}

	/* (Non Javadoc)
	 * @see soars.common.utility.swing.menu.MenuAction#selected(java.awt.event.ActionEvent)
	 */
	public void selected(ActionEvent actionEvent) {
		_basicMenuHandler2.on_select_all(actionEvent);
	}
}
