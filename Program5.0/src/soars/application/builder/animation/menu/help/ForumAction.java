/**
 * 
 */
package soars.application.builder.animation.menu.help;

import java.awt.event.ActionEvent;

import javax.swing.JMenuItem;

import soars.application.builder.animation.main.MainFrame;

import soars.common.utility.swing.menu.MenuAction;

/**
 * @author kurata
 *
 */
public class ForumAction extends MenuAction {

	/**
	 * @param name
	 */
	public ForumAction(String name) {
		super(name);
	}

	/**
	 * @param name
	 * @param item
	 */
	public ForumAction(String name, JMenuItem item) {
		super(name, item);
	}

	/* (non-Javadoc)
	 * @see soars.common.utility.swing.menu.MenuAction#selected(java.awt.event.ActionEvent)
	 */
	public void selected(ActionEvent actionEvent) {
		MainFrame.get_instance().on_help_forum(actionEvent);
	}
}
