/*
 * 2005/06/25
 */
package soars.application.visualshell.common.menu.basic4;

import java.awt.event.ActionEvent;

import soars.common.utility.swing.menu.MenuAction;

/**
 * The menu handler of "Remove column" menu.
 * @author kurata / SOARS project
 */
public class RemoveColumnAction extends MenuAction {

	/**
	 * 
	 */
	private IBasicMenuHandler4 _basicMenuHandler4 = null;

	/**
	 * Creates the menu handler of "New column" menu
	 * @param name the Menu name
	 * @param basicMenuHandler4 the common menu handler interface
	 */
	public RemoveColumnAction(String name, IBasicMenuHandler4 basicMenuHandler4) {
		super(name);
		_basicMenuHandler4 = basicMenuHandler4;
	}

	/* (Non Javadoc)
	 * @see soars.common.utility.swing.menu.MenuAction#selected(java.awt.event.ActionEvent)
	 */
	public void selected(ActionEvent actionEvent) {
		_basicMenuHandler4.on_remove_column(actionEvent);
	}
}
