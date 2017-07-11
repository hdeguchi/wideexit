/**
 * 
 */
package soars.application.visualshell.menu.edit;

import java.awt.event.ActionEvent;

import soars.application.visualshell.object.role.base.edit.inheritance.ConnectObject;

/**
 * @author kurata
 *
 */
public interface IEditMenuHandler {

	/**
	 * @param actionEvent
	 */
	public void on_disconnect(ActionEvent actionEvent);

	/**
	 * @param connectObject
	 * @param actionEvent
	 */
	public void on_disconnect_one(ConnectObject connectObject, ActionEvent actionEvent);

	/**
	 * @param actionEvent
	 */
	public void on_move(ActionEvent actionEvent);

	/**
	 * @param actionEvent
	 */
	public void on_flush_top(ActionEvent actionEvent);

	/**
	 * @param actionEvent
	 */
	public void on_flush_bottom(ActionEvent actionEvent);

	/**
	 * @param actionEvent
	 */
	public void on_flush_left(ActionEvent actionEvent);

	/**
	 * @param actionEvent
	 */
	public void on_flush_right(ActionEvent actionEvent);

	/**
	 * @param actionEvent
	 */
	public void on_vertical_equal_layout(ActionEvent actionEvent);

	/**
	 * @param actionEvent
	 */
	public void on_horizontal_equal_layout(ActionEvent actionEvent);

	/**
	 * @param actionEvent
	 */
	public void on_swap_names(ActionEvent actionEvent);

	/**
	 * @param actionEvent
	 */
	public void on_copy_objects(ActionEvent actionEvent);

	/**
	 * @param actionEvent
	 */
	public void on_paste_objects(ActionEvent actionEvent);
}
