/*
 * 2005/02/02
 */
package soars.application.animator.menu.animation;

import java.awt.event.ActionEvent;

import soars.application.animator.main.MainFrame;
import soars.common.utility.swing.menu.MenuAction;

/**
 * The menu handler of "Play" menu.
 * @author kurata / SOARS project
 */
public class PlayAction extends MenuAction {

	/**
	 * Creates the menu handler of "Play" menu
	 * @param name the Menu name
	 */
	public PlayAction(String name) {
		super(name);
	}

	/* (Non Javadoc)
	 * @see soars.common.utility.swing.menu.MenuAction#selected(java.awt.event.ActionEvent)
	 */
	public void selected(ActionEvent actionEvent) {
		MainFrame.get_instance().on_play(actionEvent);
	}
}
