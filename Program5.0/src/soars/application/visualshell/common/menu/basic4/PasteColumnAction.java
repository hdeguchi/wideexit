/*
 * 2005/09/02
 */
package soars.application.visualshell.common.menu.basic4;

import java.awt.event.ActionEvent;

import soars.common.utility.swing.menu.MenuAction;

/**
 * The menu handler of "Paste column" menu.
 * @author kurata / SOARS project
 */
public class PasteColumnAction extends MenuAction {

	/**
	 * 
	 */
	private IBasicMenuHandler4 _basicMenuHandler4 = null;

	/**
	 * Creates the menu handler of "Paste column" menu
	 * @param name the Menu name
	 * @param basicMenuHandler4 the common menu handler interface
	 */
	public PasteColumnAction(String name, IBasicMenuHandler4 basicMenuHandler4) {
		super(name);
		_basicMenuHandler4 = basicMenuHandler4;
	}

	/* (Non Javadoc)
	 * @see soars.common.utility.swing.menu.MenuAction#selected(java.awt.event.ActionEvent)
	 */
	public void selected(ActionEvent actionEvent) {
		_basicMenuHandler4.on_paste_column(actionEvent);
	}
}
