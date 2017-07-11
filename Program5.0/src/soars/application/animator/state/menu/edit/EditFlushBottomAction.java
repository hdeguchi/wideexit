/*
 * Created on 2005/11/14
 */
package soars.application.animator.state.menu.edit;

import java.awt.event.ActionEvent;

import soars.application.animator.state.EditState;
import soars.common.utility.swing.menu.MenuAction;

/**
 * The menu handler of "Flush bottom" menu.
 * @author kurata / SOARS project
 */
public class EditFlushBottomAction extends MenuAction {

	/**
	 * 
	 */
	private EditState _editState = null;

	/**
	 * Creates the menu handler of "Flush bottom" menu
	 * @param name the Menu name
	 * @param editState the instance of the EditState class
	 */
	public EditFlushBottomAction(String name, EditState editState) {
		super(name);
		_editState = editState;
	}

	/* (Non Javadoc)
	 * @see soars.common.utility.swing.menu.MenuAction#selected(java.awt.event.ActionEvent)
	 */
	public void selected(ActionEvent actionEvent) {
		_editState.on_edit_flush_bottom(actionEvent);
	}
}
