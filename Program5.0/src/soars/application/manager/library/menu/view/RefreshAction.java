/**
 * 
 */
package soars.application.manager.library.menu.view;

import java.awt.event.ActionEvent;

import soars.application.manager.library.main.MainFrame;
import soars.common.utility.swing.menu.MenuAction;

/**
 * The menu handler of "Refresh" menu.
 * @author kurata / SOARS project
 */
public class RefreshAction extends MenuAction {

	/**
	 * Creates the menu handler of "Refresh" menu
	 * @param name the Menu name
	 */
	public RefreshAction(String name) {
		super(name);
	}

	/* (non-Javadoc)
	 * @see soars.common.utility.swing.menu.MenuAction#selected(java.awt.event.ActionEvent)
	 */
	public void selected(ActionEvent actionEvent) {
		MainFrame.get_instance().on_view_refresh(actionEvent);
	}
}
