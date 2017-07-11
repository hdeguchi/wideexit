/**
 * 
 */
package soars.application.visualshell.common.menu.basic5;

import java.awt.event.ActionEvent;

import soars.common.utility.swing.menu.MenuAction;

/**
 * The menu handler of "Remove and shift left" menu.
 * @author kurata / SOARS project
 */
public class RemoveLeftShiftAction extends MenuAction {

	/**
	 * 
	 */
	private IBasicMenuHandler5 _basicMenuHandler5 = null;

	/**
	 * Creates the menu handler of "Remove and shift left" menu
	 * @param name the Menu name
	 * @param basicMenuHandler5 the common menu handler interface
	 */
	public RemoveLeftShiftAction(String name, IBasicMenuHandler5 basicMenuHandler5) {
		super(name);
		_basicMenuHandler5 = basicMenuHandler5;
	}

	/* (non-Javadoc)
	 * @see soars.common.utility.swing.menu.MenuAction#selected(java.awt.event.ActionEvent)
	 */
	public void selected(ActionEvent actionEvent) {
		_basicMenuHandler5.on_remove_left_shift(actionEvent);
	}
}
