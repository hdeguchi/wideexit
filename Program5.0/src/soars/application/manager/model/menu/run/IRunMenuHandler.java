/**
 * 
 */
package soars.application.manager.model.menu.run;

import java.awt.event.ActionEvent;

/**
 * @author kurata
 *
 */
public interface IRunMenuHandler {

	/**
	 * @param actionEvent
	 */
	void on_run_start_visual_shell(ActionEvent actionEvent);

	/**
	 * @param actionEvent
	 */
	void on_run_start_simulator(ActionEvent actionEvent);

	/**
	 * @param actionEvent
	 */
	void on_run_start_library_manager(ActionEvent actionEvent);

	/**
	 * @param actionEvent
	 */
	void on_run_start_application_builder(ActionEvent actionEvent);
}
