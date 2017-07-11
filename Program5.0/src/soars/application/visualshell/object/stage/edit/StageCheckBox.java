/**
 * 
 */
package soars.application.visualshell.object.stage.edit;

import javax.swing.JCheckBox;

import soars.application.visualshell.object.stage.Stage;

/**
 * @author kurata
 *
 */
public class StageCheckBox extends JCheckBox {

	public Stage _stage = null;

	/**
	 * @param stage
	 */
	public StageCheckBox(Stage stage) {
		super();
		_stage = stage;
		setSelected( stage._random);
	}
}
