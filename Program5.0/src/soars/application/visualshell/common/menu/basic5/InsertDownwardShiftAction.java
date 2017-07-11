/**
 * 
 */
package soars.application.visualshell.common.menu.basic5;

import java.awt.event.ActionEvent;

import soars.common.utility.swing.menu.MenuAction;

/**
 * The menu handler of "Insert and shift downward" menu.
 * @author kurata / SOARS project
 */
public class InsertDownwardShiftAction extends MenuAction {

	/**
	 * 
	 */
	private IBasicMenuHandler5 _basicMenuHandler5 = null;

	/**
	 * Creates the menu handler of "Insert and shift downward" menu
	 * @param name the Menu name
	 * @param basicMenuHandler5 the common menu handler interface
	 */
	public InsertDownwardShiftAction(String name, IBasicMenuHandler5 basicMenuHandler5) {
		super(name);
		_basicMenuHandler5 = basicMenuHandler5;
	}

	/* (non-Javadoc)
	 * @see soars.common.utility.swing.menu.MenuAction#selected(java.awt.event.ActionEvent)
	 */
	public void selected(ActionEvent actionEvent) {
		_basicMenuHandler5.on_insert_downward_shift(actionEvent);
	}
}
