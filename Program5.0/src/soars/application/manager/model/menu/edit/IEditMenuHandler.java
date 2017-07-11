/**
 * 
 */
package soars.application.manager.model.menu.edit;

import java.awt.event.ActionEvent;

/**
 * @author kurata
 *
 */
public interface IEditMenuHandler {

	/**
	 * @param actionEvent
	 */
	void on_edit_copy(ActionEvent actionEvent);

	/**
	 * @param actionEvent
	 */
	void on_edit_paste(ActionEvent actionEvent);

	/**
	 * @param actionEvent
	 */
	void on_edit_duplicate(ActionEvent actionEvent);

	/**
	 * @param actionEvent
	 */
	void on_edit_export(ActionEvent actionEvent);

	/**
	 * @param actionEvent
	 */
	void on_edit_remove(ActionEvent actionEvent);

	/**
	 * @param actionEvent
	 */
	void on_edit_rename(ActionEvent actionEvent);

	/**
	 * @param actionEvent
	 */
	void on_edit_new_directory(ActionEvent actionEvent);

	/**
	 * @param actionEvent
	 */
	void on_edit_new_simulation_model(ActionEvent actionEvent);

	/**
	 * @param actionEvent
	 */
	void on_edit_model_information(ActionEvent actionEvent);

	/**
	 * @param actionEvent
	 */
	void on_edit_import_user_defined_rule(ActionEvent actionEvent);

	/**
	 * @param actionEvent
	 */
	void on_edit_update_user_defined_rule(ActionEvent actionEvent);

	/**
	 * @param actionEvent
	 */
	void on_edit_export_user_defined_rule(ActionEvent actionEvent);

	/**
	 * @param actionEvent
	 */
	void on_edit_remove_user_defined_rule(ActionEvent actionEvent);

	/**
	 * @param actionEvent
	 */
	void on_edit_clear_image(ActionEvent actionEvent);
}
