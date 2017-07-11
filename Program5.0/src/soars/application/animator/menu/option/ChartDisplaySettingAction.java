/**
 * 
 */
package soars.application.animator.menu.option;

import java.awt.event.ActionEvent;

import soars.application.animator.main.MainFrame;
import soars.common.utility.swing.menu.MenuAction;

/**
 * The menu handler of "Chart display setting" menu.
 * @author kurata / SOARS project
 */
public class ChartDisplaySettingAction extends MenuAction {

	/**
	 * Creates the menu handler of "Chart display setting" menu
	 * @param name the Menu name
	 */
	public ChartDisplaySettingAction(String name) {
		super(name);
	}

	/* (non-Javadoc)
	 * @see soars.common.utility.swing.menu.MenuAction#selected(java.awt.event.ActionEvent)
	 */
	public void selected(ActionEvent actionEvent) {
		MainFrame.get_instance().on_option_chart_display_setting(actionEvent);
	}
}
