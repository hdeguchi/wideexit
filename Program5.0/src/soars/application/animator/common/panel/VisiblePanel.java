/**
 * 
 */
package soars.application.animator.common.panel;

import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.Frame;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JCheckBox;
import javax.swing.JPanel;

import soars.application.animator.main.ResourceManager;
import soars.common.utility.swing.window.Dialog;

/**
 * The common GUI panel for visible/invisible selection.
 * @author kurata / SOARS project
 */
public class VisiblePanel extends JPanel {

	/**
	 * Check box which displays whether the object is visible.
	 */
	public JCheckBox _visibleCheckBox = null;

	/**
	 * Check box which displays whether the object's name is visible.
	 */
	public JCheckBox _visibleNameCheckBox = null;

	/**
	 * Frame of the parent container.
	 */
	private Frame _owner = null;

	/**
	 * Parent container of this component.
	 */
	private Component _parent = null;

	/**
	 * Creates the common GUI panel for visible/invisible selection.
	 * @param owner the frame of the parent container
	 * @param parent the parent container of this component
	 */
	public VisiblePanel(Frame owner, Component parent) {
		super();
		_owner = owner;
		_parent = parent;
	}

	/**
	 * Creates the check boxes. 
	 * @param visible true if the object is visible
	 * @param visibleName true if the object's name is visible
	 * @param dialog the parent container of this component
	 */
	public void setup(boolean visible, boolean visibleName, Dialog dialog) {
		setLayout( new BoxLayout( this, BoxLayout.X_AXIS));

		add( Box.createHorizontalStrut( 5));

		JPanel panel = new JPanel();
		panel.setLayout( new BoxLayout( panel, BoxLayout.Y_AXIS));

		panel.setBorder( BorderFactory.createTitledBorder(
			ResourceManager.get_instance().get( "visible.panel.border.title")));

		setup_visible_check_box( visible, panel);

		//dialog.insert_horizontal_glue( panel);

		setup_visible_name_check_box( visibleName, panel);

		add( panel);

		add( Box.createHorizontalStrut( 5));
	}

	/**
	 * Creates the check box to select which displays whether the object is visible.
	 * @param visible true if the object is visible
	 * @param parent the parent container of the component on which the check box is displayed
	 */
	private void setup_visible_check_box(boolean visible, JPanel parent) {
		JPanel panel = new JPanel();
		panel.setLayout( new FlowLayout( FlowLayout.LEFT, 0, 0));

		_visibleCheckBox = new JCheckBox(
			ResourceManager.get_instance().get( "visible.panel.visible.checkbox"),
			visible);

		panel.add( _visibleCheckBox);

		parent.add( panel);
	}

	/**
	 * Creates the check box which displays whether the object's name is visible.
	 * @param visibleName true if the object's name is visible
	 * @param parent the parent container of the component on which the check box is displayed
	 */
	private void setup_visible_name_check_box(boolean visibleName, JPanel parent) {
		JPanel panel = new JPanel();
		panel.setLayout( new FlowLayout( FlowLayout.LEFT, 0, 0));

		_visibleNameCheckBox = new JCheckBox(
			ResourceManager.get_instance().get( "visible.panel.visible.name.checkbox"),
			visibleName);

		panel.add( _visibleNameCheckBox);

		parent.add( panel);
	}
}
