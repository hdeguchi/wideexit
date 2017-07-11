/**
 * 
 */
package soars.application.visualshell.object.role.base.edit.tab.legacy.common.time;

import java.awt.Color;
import java.awt.Frame;

import soars.application.visualshell.common.selector.ObjectSelector;
import soars.application.visualshell.main.Constant;
import soars.application.visualshell.object.role.base.Role;
import soars.application.visualshell.object.role.base.edit.EditRoleFrame;
import soars.application.visualshell.object.role.base.edit.tab.base.RulePropertyPanelBase;
import soars.application.visualshell.object.role.base.object.legacy.common.time.TimeRule;
import soars.common.utility.swing.button.CheckBox;
import soars.common.utility.swing.button.RadioButton;
import soars.common.utility.swing.combo.ComboBox;
import soars.common.utility.swing.text.TextField;

/**
 * @author kurata
 *
 */
public class TimePropertyPanel extends RulePropertyPanelBase {

	/**
	 * 
	 */
	protected CheckBox _spotCheckBox = null;

	/**
	 * 
	 */
	protected ObjectSelector _spotSelector = null;

	/**
	 * 
	 */
	protected CheckBox _spotVariableCheckBox = null;

	/**
	 * 
	 */
	protected ComboBox _spotVariableComboBox = null;

	/**
	 * @param title
	 * @param kind
	 * @param type
	 * @param color
	 * @param role
	 * @param index
	 * @param owner
	 * @param parent
	 */
	public TimePropertyPanel(String title, String kind, String type, Color color, Role role, int index, Frame owner, EditRoleFrame parent) {
		super(title, kind, type, color, role, index, owner, parent);
	}

	/**
	 * @param value
	 * @param timeTextField
	 * @param timeComboBoxes
	 * @param timeVariableComboBox
	 * @param radioButtons
	 */
	protected void set(String value, TextField timeTextField, ComboBox[] timeComboBoxes, ComboBox timeVariableComboBox, RadioButton[] radioButtons) {
		if ( TimeRule.is_time( value)) {
			String[] elements = TimeRule.get_time_elements( value);
			if ( null == elements)
				return;

			timeTextField.setText( elements[ 0]);
			timeComboBoxes[ 0].setSelectedItem( elements[ 1]);
			timeComboBoxes[ 1].setSelectedItem( elements[ 2]);
			radioButtons[ 0].setSelected( true);
		} else {
			if ( value.startsWith( "$")) {
				timeTextField.setText( value);
				timeComboBoxes[ 0].setSelectedIndex( 0);
				timeComboBoxes[ 1].setSelectedIndex( 0);
				radioButtons[ 0].setSelected( true);
			} else {
				if ( value.equals( Constant._currentTimeName)) {
					if ( 2 < radioButtons.length)
						radioButtons[ 2].setSelected( true);
				} else {
					timeVariableComboBox.setSelectedItem( value);
					radioButtons[ 1].setSelected( true);
				}
			}
		}
	}

	/**
	 * @param timeTextField
	 * @param timeComboBoxes
	 * @param timeVariableComboBox
	 * @param radioButtons
	 * @return
	 */
	protected String get(TextField timeTextField, ComboBox[] timeComboBoxes, ComboBox timeVariableComboBox, RadioButton[] radioButtons) {
		if ( radioButtons[ 0].isSelected()) {
			if ( timeTextField.getText().startsWith( "$")) {
				if ( timeTextField.getText().equals( "$")
					|| timeTextField.getText().equals( "$Name")
					|| timeTextField.getText().equals( "$Role")
					|| timeTextField.getText().equals( "$Spot")
					|| 0 <= timeTextField.getText().indexOf( Constant._experimentName)
					|| 0 <= timeTextField.getText().indexOf( Constant._currentTimeName))
					return null;

				if ( 0 < timeTextField.getText().indexOf( "$", 1)
					|| 0 < timeTextField.getText().indexOf( ")", 1))
					return null;

				return timeTextField.getText();
			} else {
				String day = TimeRule.get_day( timeTextField.getText());
				return ( ( day.equals( "")) ? "" : ( day + "/"))
					+ ( ( String)timeComboBoxes[ 0].getSelectedItem()
					+ ":"
					+ ( String)timeComboBoxes[ 1].getSelectedItem());
			}
		} else if ( radioButtons[ 1].isSelected())
			return ( String)timeVariableComboBox.getSelectedItem();
		else
			return Constant._currentTimeName;
	}
}
