/**
 * 
 */
package soars.plugin.modelbuilder.chart.log_viewer.body.menu.file;

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
	void on_file_save_image_as(ActionEvent actionEvent);

	/**
	 * @param actionEvent
	 */
	void on_file_open(ActionEvent actionEvent);
}
