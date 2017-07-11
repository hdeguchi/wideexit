/**
 * 
 */
package soars.application.animator.state.menu.edit;

import java.awt.event.ActionEvent;

import soars.application.animator.state.EditState;
import soars.common.utility.swing.menu.MenuAction;

/**
 * @author kurata
 *
 */
public class DuplicateWindowAction extends MenuAction {

	/**
	 * 
	 */
	private EditState _editState = null;

	/**
	 * @param name
	 * @param editState
	 */
	public DuplicateWindowAction(String name, EditState editState) {
		super(name);
		_editState = editState;
	}

	/* (non-Javadoc)
	 * @see soars.common.utility.swing.menu.MenuAction#selected(java.awt.event.ActionEvent)
	 */
	public void selected(ActionEvent actionEvent) {
		_editState.on_duplicate_window(actionEvent);
	}
}
