/**
 * 
 */
package soars.application.manager.library.main.tab.common.menu;

import java.awt.event.ActionEvent;

/**
 * @author kurata
 *
 */
public interface IContextMenuHandler {

	/**
	 * @param actionEvent
	 */
	public void on_edit_module(ActionEvent actionEvent);

	/**
	 * @param actionEvent
	 */
	public void on_enable_module(ActionEvent actionEvent);

	/**
	 * @param actionEvent
	 */
	public void on_disable_module(ActionEvent actionEvent);

	/**
	 * @param actionEvent
	 */
	public void on_update_annotation_file(ActionEvent actionEvent);
}
