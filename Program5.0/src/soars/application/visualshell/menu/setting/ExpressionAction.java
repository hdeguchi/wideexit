/**
 * 
 */
package soars.application.visualshell.menu.setting;

import java.awt.event.ActionEvent;

import soars.application.visualshell.main.MainFrame;
import soars.common.utility.swing.menu.MenuAction;

/**
 * The menu handler of "Expression" menu.
 * @author kurata / SOARS project
 */
public class ExpressionAction extends MenuAction {

	/**
	 * Creates the menu handler of "Expression" menu
	 * @param name the Menu name
	 */
	public ExpressionAction(String name) {
		super(name);
	}

	/* (non-Javadoc)
	 * @see soars.common.utility.swing.menu.MenuAction#selected(java.awt.event.ActionEvent)
	 */
	public void selected(ActionEvent actionEvent) {
		MainFrame.get_instance().on_setting_expression(actionEvent);
	}
}
