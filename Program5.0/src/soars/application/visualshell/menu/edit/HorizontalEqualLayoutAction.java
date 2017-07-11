/*
 * Created on 2005/11/15
 */
package soars.application.visualshell.menu.edit;

import java.awt.event.ActionEvent;

import javax.swing.JMenuItem;

import soars.common.utility.swing.menu.MenuAction;

/**
 * @author kurata
 */
public class HorizontalEqualLayoutAction extends MenuAction {

	/**
	 * 
	 */
	private IEditMenuHandler _editMenuHandler = null;

	/**
	 * @param name
	 * @param editMenuHandler
	 */
	public HorizontalEqualLayoutAction(String name, IEditMenuHandler editMenuHandler) {
		super(name);
		_editMenuHandler = editMenuHandler;
	}

	/**
	 * @param name
	 * @param item
	 */
	public HorizontalEqualLayoutAction(String name, JMenuItem item) {
		super(name, item);
	}

	/* (Non Javadoc)
	 * @see soars.common.utility.swing.menu.MenuAction#selected(java.awt.event.ActionEvent)
	 */
	public void selected(ActionEvent actionEvent) {
		_editMenuHandler.on_horizontal_equal_layout(actionEvent);
	}
}
