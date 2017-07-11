/**
 * 
 */
package soars.common.utility.swing.file.manager.edit.menu.edit;

import java.awt.event.ActionEvent;

import soars.common.utility.swing.menu.MenuAction;

/**
 * The menu handler of "Redo" menu.
 * @author kurata / SOARS project
 */
public class RedoAction extends MenuAction {

	/**
	 * 
	 */
	private IEditMenuHandler _editMenuHandler = null;

	/**
	 * Creates the menu handler of "Redo" menu
	 * @param name the Menu name
	 * @param name editMenuHandler the instance of IEditMenuHandler
	 */
	public RedoAction(String name, IEditMenuHandler editMenuHandler) {
		super(name);
		_editMenuHandler = editMenuHandler;
	}

	/* (non-Javadoc)
	 * @see soars.common.utility.swing.menu.MenuAction#selected(java.awt.event.ActionEvent)
	 */
	public void selected(ActionEvent actionEvent) {
		_editMenuHandler.on_edit_redo(actionEvent);
	}
}
