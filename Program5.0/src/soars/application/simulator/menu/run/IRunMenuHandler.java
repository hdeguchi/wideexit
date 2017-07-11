/**
 * 
 */
package soars.application.simulator.menu.run;

import java.awt.event.ActionEvent;

/**
 * @author kurata
 *
 */
public interface IRunMenuHandler {

	/**
	 * @param actionEvent
	 */
	void on_run_animator(ActionEvent actionEvent);

	/**
	 * @param actionEvent
	 */
	void on_run_file_manager(ActionEvent actionEvent);
}
