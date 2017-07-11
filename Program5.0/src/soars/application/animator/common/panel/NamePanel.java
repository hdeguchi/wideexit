/**
 * 
 */
package soars.application.animator.common.panel;

import java.awt.Component;
import java.awt.Frame;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JPanel;
import javax.swing.JTextField;

import soars.application.animator.main.ResourceManager;
import soars.common.utility.swing.window.Dialog;

/**
 * The common GUI panel for the object name label.
 * @author kurata / SOARS project
 */
public class NamePanel extends JPanel {

	/**
	 * Frame of the parent container.
	 */
	private Frame _owner = null;

	/**
	 * Parent container of this component.
	 */
	private Component _parent = null;

	/**
	 * Creates the common GUI panel for the object name label.
	 * @param owner the frame of the parent container
	 * @param parent the parent container of this component
	 */
	public NamePanel(Frame owner, Component parent) {
		super();
		_owner = owner;
		_parent = parent;
	}

	/**
	 * Creates the components to display the object name.
	 * @param name the object name
	 * @param dialog the parent container of this component
	 */
	public void setup(String name, Dialog dialog) {
		setLayout( new BoxLayout( this, BoxLayout.X_AXIS));

		add( Box.createHorizontalStrut( 5));

		JPanel panel = new JPanel();
		panel.setLayout( new BoxLayout( panel, BoxLayout.Y_AXIS));

		panel.setBorder( BorderFactory.createTitledBorder(
			ResourceManager.get_instance().get( "name.panel.border.title")));

		setup_name_text_field( name, panel);

		dialog.insert_horizontal_glue( panel);

		add( panel);

		add( Box.createHorizontalStrut( 5));
	}

	/**
	 * Creates the text field to display the object name.
	 * @param name the object name
	 * @param parent the parent container of the component on which the text field is displayed
	 */
	private void setup_name_text_field(String name, JPanel parent) {
		JPanel panel = new JPanel();
		panel.setLayout( new BoxLayout( panel, BoxLayout.X_AXIS));

		panel.add( Box.createHorizontalStrut( 5));

		JTextField textField = new JTextField( name);
		textField.setEditable( false);
		panel.add( textField);

		panel.add( Box.createHorizontalStrut( 5));

		parent.add( panel);
	}
}
