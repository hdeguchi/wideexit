/*
 * 2005/03/28
 */
package soars.application.animator.menu.animation;

import java.awt.event.ActionEvent;

import soars.application.animator.main.MainFrame;
import soars.common.utility.swing.menu.MenuAction;

/**
 * The menu handler of "Retrieve agent property" menu.
 * @author kurata / SOARS project
 */
public class RetrieveAgentPropertyAction extends MenuAction {

	/**
	 * Creates the menu handler of "Retrieve agent property" menu
	 * @param name the Menu name
	 */
	public RetrieveAgentPropertyAction(String name) {
		super(name);
	}

	/* (Non Javadoc)
	 * @see soars.common.utility.swing.menu.MenuAction#selected(java.awt.event.ActionEvent)
	 */
	public void selected(ActionEvent actionEvent) {
		MainFrame.get_instance().on_retrieve_agent_property(actionEvent);
	}
}
