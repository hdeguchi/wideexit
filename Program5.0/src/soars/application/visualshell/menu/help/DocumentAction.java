/**
 * 
 */
package soars.application.visualshell.menu.help;

import java.awt.event.ActionEvent;

import soars.application.visualshell.main.MainFrame;
import soars.common.utility.swing.menu.MenuAction;

/**
 * The menu handler of "SOARS document" menu.
 * @author kurata / SOARS project
 */
public class DocumentAction extends MenuAction {

	/**
	 * Creates the menu handler of "SOARS document" menu
	 * @param name the Menu name
	 */
	public DocumentAction(String name) {
		super(name);
	}

	/* (non-Javadoc)
	 * @see soars.common.utility.swing.menu.MenuAction#selected(java.awt.event.ActionEvent)
	 */
	public void selected(ActionEvent actionEvent) {
		MainFrame.get_instance().on_help_document(actionEvent);
	}
}
