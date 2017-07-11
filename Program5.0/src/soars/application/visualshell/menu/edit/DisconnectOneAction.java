/**
 * 
 */
package soars.application.visualshell.menu.edit;

import java.awt.event.ActionEvent;

import javax.swing.JMenuItem;

import soars.application.visualshell.object.role.base.edit.inheritance.ConnectObject;
import soars.common.utility.swing.menu.MenuAction;

/**
 * @author kurata
 *
 */
public class DisconnectOneAction extends MenuAction {

	/**
	 * 
	 */
	private IEditMenuHandler _editMenuHandler = null;

	/**
	 * 
	 */
	private ConnectObject _connectObject = null;

	/**
	 * @param name
	 * @param connectObject
	 * @param editMenuHandler
	 */
	public DisconnectOneAction(String name, ConnectObject connectObject, IEditMenuHandler editMenuHandler) {
		super(name);
		_connectObject = connectObject;
		_editMenuHandler = editMenuHandler;
	}

	/**
	 * @param name
	 * @param item
	 */
	public DisconnectOneAction(String name, JMenuItem item) {
		super(name, item);
	}

	/* (non-Javadoc)
	 * @see soars.common.utility.swing.menu.MenuAction#selected(java.awt.event.ActionEvent)
	 */
	public void selected(ActionEvent actionEvent) {
		_editMenuHandler.on_disconnect_one(_connectObject, actionEvent);
	}
}
