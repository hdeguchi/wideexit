/**
 * 
 */
package soars.common.utility.swing.file.manager.edit.menu.edit;

import java.awt.event.ActionEvent;

/**
 * @author kurata
 *
 */
public interface IEditMenuHandler {

	/**
	 * Invoked when the "Undo" menu item is selected.
	 * @param actionEvent the semantic event which indicates that a component-defined action occurred
	 */
	public void on_edit_undo(ActionEvent actionEvent);

	/**
	 * Invoked when the "Redo" menu item is selected.
	 * @param actionEvent the semantic event which indicates that a component-defined action occurred
	 */
	public void on_edit_redo(ActionEvent actionEvent);
}
