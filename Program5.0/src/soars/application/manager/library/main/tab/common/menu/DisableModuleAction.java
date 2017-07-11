/**
 * 
 */
package soars.application.manager.library.main.tab.common.menu;

import java.awt.event.ActionEvent;

import soars.common.utility.swing.menu.MenuAction;

/**
 * @author kurata
 *
 */
public class DisableModuleAction extends MenuAction {

	/**
	 * 
	 */
	private IContextMenuHandler _contextMenuHandler = null;

	/**
	 * @param name
	 * @param contextMenuHandler
	 */
	public DisableModuleAction(String name, IContextMenuHandler contextMenuHandler) {
		super(name);
		_contextMenuHandler = contextMenuHandler;
	}

	/* (non-Javadoc)
	 * @see soars.common.utility.swing.menu.MenuAction#selected(java.awt.event.ActionEvent)
	 */
	public void selected(ActionEvent actionEvent) {
		_contextMenuHandler.on_disable_module(actionEvent);
	}
}
