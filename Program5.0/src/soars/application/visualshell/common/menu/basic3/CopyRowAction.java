/*
 * 2005/09/02
 */
package soars.application.visualshell.common.menu.basic3;

import java.awt.event.ActionEvent;

import soars.common.utility.swing.menu.MenuAction;

/**
 * The menu handler of "Copy row" menu.
 * @author kurata / SOARS project
 */
public class CopyRowAction extends MenuAction {

	/**
	 * 
	 */
	private IBasicMenuHandler3 _basicMenuHandler3 = null;

	/**
	 * Creates the menu handler of "Copy row" menu
	 * @param name the Menu name
	 * @param basicMenuHandler3 the common menu handler interface
	 */
	public CopyRowAction(String name, IBasicMenuHandler3 basicMenuHandler3) {
		super(name);
		_basicMenuHandler3 = basicMenuHandler3;
	}

	/* (Non Javadoc)
	 * @see soars.common.utility.swing.menu.MenuAction#selected(java.awt.event.ActionEvent)
	 */
	public void selected(ActionEvent actionEvent) {
		_basicMenuHandler3.on_copy_row( actionEvent);
	}
}
