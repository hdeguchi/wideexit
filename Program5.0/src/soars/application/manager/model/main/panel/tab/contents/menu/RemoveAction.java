/**
 * 
 */
package soars.application.manager.model.main.panel.tab.contents.menu;

import java.awt.event.ActionEvent;

import soars.application.manager.model.main.panel.tab.contents.SoarsContentsTable;
import soars.common.utility.swing.menu.MenuAction;

/**
 * @author kurata
 *
 */
public class RemoveAction extends MenuAction {

	/**
	 * 
	 */
	private SoarsContentsTable _soarsContentsTable = null;

	/**
	 * @param name
	 * @param soarsContentsTable
	 */
	public RemoveAction(String name, SoarsContentsTable soarsContentsTable) {
		super(name);
		_soarsContentsTable = soarsContentsTable;
	}

	/* (non-Javadoc)
	 * @see soars.common.utility.swing.menu.MenuAction#selected(java.awt.event.ActionEvent)
	 */
	public void selected(ActionEvent actionEvent) {
		_soarsContentsTable.on_remove(actionEvent);
	}
}
