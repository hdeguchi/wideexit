/**
 * 
 */
package soars.plugin.modelbuilder.chart.chart.menu.file;

import java.awt.event.ActionEvent;

/**
 * @author kurata
 *
 */
public interface IFileMenuHandler {

	/**
	 * @param actionEvent
	 */
	void on_save_as(ActionEvent actionEvent);

	/**
	 * @param actionEvent
	 */
	void on_save_image_as(ActionEvent actionEvent);
}
