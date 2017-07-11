/**
 * 
 */
package soars.application.simulator.menu.file;

import java.awt.event.ActionEvent;

/**
 * @author kurata
 *
 */
public interface IFileMenuHandler {

	/**
	 * @param actionEvent
	 */
	void on_file_exit(ActionEvent actionEvent);

	/**
	 * @param actionEvent
	 */
	void on_file_new(ActionEvent actionEvent);

	/**
	 * @param actionEvent
	 */
	void on_file_open(ActionEvent actionEvent);

	/**
	 * @param actionEvent
	 */
	void on_file_close(ActionEvent actionEvent);

	/**
	 * @param actionEvent
	 */
	void on_file_save(ActionEvent actionEvent);

	/**
	 * @param actionEvent
	 */
	void on_file_save_as(ActionEvent actionEvent);

	/**
	 * @param actionEvent
	 */
	void on_file_save_image_as(ActionEvent actionEvent);
}
