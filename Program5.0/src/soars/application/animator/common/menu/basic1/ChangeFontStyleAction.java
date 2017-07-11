/*
 * 2005/03/10
 */
package soars.application.animator.common.menu.basic1;

import java.awt.event.ActionEvent;

import soars.common.utility.swing.menu.MenuAction;

/**
 * The menu handler to change the font style.
 * @author kurata / SOARS project
 */
public class ChangeFontStyleAction extends MenuAction {

	/**
	 * Menu handler interface.
	 */
	private IBasicMenuHandler1 _basicMenuHandler1 = null;

	/**
	 * Creates the menu handler to change the font style.
	 * @param name the Menu name
	 * @param the menu handler interface
	 */
	public ChangeFontStyleAction(String name, IBasicMenuHandler1 basicMenuHandler1) {
		super(name);
		_basicMenuHandler1 = basicMenuHandler1;
	}

	/* (Non Javadoc)
	 * @see soars.common.utility.swing.menu.MenuAction#selected(java.awt.event.ActionEvent)
	 */
	public void selected(ActionEvent actionEvent) {
		_basicMenuHandler1.on_change_font_style(actionEvent);
	}
}
