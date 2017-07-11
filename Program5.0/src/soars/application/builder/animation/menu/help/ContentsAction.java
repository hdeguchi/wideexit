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
public class ContentsAction extends MenuAction {

	/**
	 * @param name
	 */
	public ContentsAction(String name) {
		super(name);
	}

	/**
	 * @param name
	 * @param item
	 */
	public ContentsAction(String name, JMenuItem item) {
		super(name, item);
	}

	/* (non-Javadoc)
	 * @see soars.common.utility.swing.menu.MenuAction#selected(java.awt.event.ActionEvent)
	 */
	public void selected(ActionEvent actionEvent) {
		MainFrame.get_instance().on_help_contents(actionEvent);
	}
}
