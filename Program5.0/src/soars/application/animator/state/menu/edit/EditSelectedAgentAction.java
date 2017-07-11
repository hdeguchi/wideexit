/*
 * 2005/03/25
 */
package soars.application.animator.state.menu.edit;

import java.awt.event.ActionEvent;

import soars.application.animator.state.EditState;
import soars.common.utility.swing.menu.MenuAction;

/**
 * The menu handler of "Edit selected agents" menu.
 * @author kurata / SOARS project
 */
public class EditSelectedAgentAction extends MenuAction {

	/**
	 * 
	 */
	private EditState _editState = null;

	/**
	 * Creates the menu handler of "Edit selected agents" menu
	 * @param name the Menu name
	 * @param editState the instance of the EditState class
	 */
	public EditSelectedAgentAction(String name, EditState editState) {
		super(name);
		_editState = editState;
	}

	/* (Non Javadoc)
	 * @see soars.common.utility.swing.menu.MenuAction#selected(java.awt.event.ActionEvent)
	 */
	public void selected(ActionEvent actionEvent) {
		_editState.on_edit_selected_agent(actionEvent);
	}
}
