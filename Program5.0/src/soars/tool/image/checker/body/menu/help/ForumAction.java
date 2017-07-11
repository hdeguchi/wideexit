/**
 * 
 */
package soars.tool.image.checker.body.menu.help;

import java.awt.event.ActionEvent;

import soars.common.utility.swing.menu.MenuAction;
import soars.tool.image.checker.body.main.MainFrame;

/**
 * The menu handler of "SOARS forum" menu.
 * @author kurata / SOARS project
 */
public class ForumAction extends MenuAction {

	/**
	 * Creates the menu handler of "SOARS forum" menu
	 * @param name the Menu name
	 */
	public ForumAction(String name) {
		super(name);
	}

	/* (non-Javadoc)
	 * @see soars.common.utility.swing.menu.MenuAction#selected(java.awt.event.ActionEvent)
	 */
	public void selected(ActionEvent actionEvent) {
		MainFrame.get_instance().on_help_forum(actionEvent);
	}
}
