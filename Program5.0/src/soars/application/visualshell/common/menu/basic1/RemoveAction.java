/*
 * 2005/05/01
 */
package soars.application.visualshell.common.menu.basic1;

import java.awt.event.ActionEvent;

import soars.common.utility.swing.menu.MenuAction;

/**
 * The menu handler of "Remove" menu.
 * @author kurata / SOARS project
 */
public class RemoveAction extends MenuAction {

	/**
	 * 
	 */
	private IBasicMenuHandler1 _basicMenuHandler1 = null;

	/**
	 * Creates the menu handler of "Remove" menu
	 * @param name the Menu name
	 * @param basicMenuHandler1 the common menu handler interface
	 */
	public RemoveAction(String name, IBasicMenuHandler1 basicMenuHandler1) {
		super(name);
		_basicMenuHandler1 = basicMenuHandler1;
	}

	/* (Non Javadoc)
	 * @see soars.common.utility.swing.menu.MenuAction#selected(java.awt.event.ActionEvent)
	 */
	public void selected(ActionEvent actionEvent) {
		_basicMenuHandler1.on_remove( actionEvent);
	}
}
