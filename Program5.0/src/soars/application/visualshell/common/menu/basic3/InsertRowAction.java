/*
 * 2005/05/30
 */
package soars.application.visualshell.common.menu.basic3;

import java.awt.event.ActionEvent;

import soars.common.utility.swing.menu.MenuAction;

/**
 * The menu handler of "Insert row" menu.
 * @author kurata / SOARS project
 */
public class InsertRowAction extends MenuAction {

	/**
	 * 
	 */
	private IBasicMenuHandler3 _basicMenuHandler3 = null;

	/**
	 * Creates the menu handler of "Insert row" menu
	 * @param name the Menu name
	 * @param basicMenuHandler3 the common menu handler interface
	 */
	public InsertRowAction(String name, IBasicMenuHandler3 basicMenuHandler3) {
		super(name);
		_basicMenuHandler3 = basicMenuHandler3;
	}

	/* (Non Javadoc)
	 * @see soars.common.utility.swing.menu.MenuAction#selected(java.awt.event.ActionEvent)
	 */
	public void selected(ActionEvent actionEvent) {
		_basicMenuHandler3.on_insert_row( actionEvent);
	}
}
