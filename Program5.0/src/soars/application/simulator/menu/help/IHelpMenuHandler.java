/**
 * 
 */
package soars.application.simulator.menu.help;

import java.awt.event.ActionEvent;

/**
 * @author kurata
 *
 */
public interface IHelpMenuHandler {

	/**
	 * @param actionEvent
	 */
	void on_help_contents(ActionEvent actionEvent);

	/**
	 * @param actionEvent
	 */
	void on_help_about(ActionEvent actionEvent);

	/**
	 * @param actionEvent
	 */
	void on_help_forum(ActionEvent actionEvent);
}
