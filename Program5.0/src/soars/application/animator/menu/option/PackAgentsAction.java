/*
 * Created on 2006/09/05
 */
package soars.application.animator.menu.option;

import java.awt.event.ActionEvent;

import soars.application.animator.main.MainFrame;
import soars.common.utility.swing.menu.MenuAction;

/**
 * The menu handler of "Pack agents" menu.
 * @author kurata / SOARS project
 */
public class PackAgentsAction extends MenuAction {

	/**
	 * Creates the menu handler of "Pack agents" menu
	 * @param name the Menu name
	 */
	public PackAgentsAction(String name) {
		super(name);
	}

	/* (Non Javadoc)
	 * @see soars.common.utility.swing.menu.MenuAction#selected(java.awt.event.ActionEvent)
	 */
	public void selected(ActionEvent actionEvent) {
		MainFrame.get_instance().on_option_pack_agents(actionEvent);
	}
}
