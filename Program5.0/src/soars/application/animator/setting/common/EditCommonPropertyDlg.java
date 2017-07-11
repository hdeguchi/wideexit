/*
 * 2005/02/14
 */
package soars.application.animator.setting.common;

import java.awt.Dimension;
import java.awt.Frame;
import java.awt.event.ActionEvent;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import soars.common.utility.swing.text.TextLimiter;
import soars.common.utility.swing.window.Dialog;
import soars.application.animator.main.Application;
import soars.application.animator.main.ResourceManager;
import soars.application.animator.observer.Observer;

/**
 * The dialog box to edit the common properties.
 * @author kurata / SOARS project
 */
public class EditCommonPropertyDlg extends Dialog {

	/**
	 * 
	 */
	private JTextField _velocity_textField = null;

	/**
	 * 
	 */
	private JSlider _velocity_slider = null;

	/**
	 * 
	 */
	private JLabel _velocity_label = null;

	/**
	 * 
	 */
	private JLabel _agent_width_label = null;

	/**
	 * 
	 */
	private JLabel _agent_height_label = null;

	/**
	 * 
	 */
	private JLabel _spot_width_label = null;

	/**
	 * 
	 */
	private JLabel _spot_height_label = null;

	/**
	 * 
	 */
	private JTextField _agent_width_textField = null;

	/**
	 * 
	 */
	private JTextField _agent_height_textField = null;

	/**
	 * 
	 */
	private JTextField _spot_width_textField = null;

	/**
	 * 
	 */
	private JTextField _spot_height_textField = null;

	/**
	 * Creates a non-modal dialog with the specified title and with the specified owner frame. If owner is null, a shared, hidden frame will be set as the owner of the dialog. 
	 * @param arg0 the Frame from which the dialog is displayed
	 * @param arg1 the String to display in the dialog's title bar
	 * @param arg2 true for a modal dialog, false for one that allows other windows to be active at the same time
	 */
	public EditCommonPropertyDlg(Frame arg0, String arg1, boolean arg2) {
		super(arg0, arg1, arg2);
	}

	/* (Non Javadoc)
	 * @see soars.common.utility.swing.window.Dialog#on_init_dialog()
	 */
	protected boolean on_init_dialog() {
		if ( !super.on_init_dialog())
			return false;


		setResizable( false);


		getContentPane().setLayout( new BoxLayout( getContentPane(), BoxLayout.Y_AXIS));

		insert_horizontal_glue();

		setup_velocity_textField();

		insert_horizontal_glue();

		setup_velocity_slider();

		if ( !Application._demo) {
			insert_horizontal_glue();

			setup_agent_width_textField();

			insert_horizontal_glue();

			setup_agent_height_textField();

			insert_horizontal_glue();

			setup_spot_width_textField();

			insert_horizontal_glue();

			setup_spot_height_textField();
		}

		insert_horizontal_glue();

		setup_ok_and_cancel_button(
			ResourceManager.get_instance().get( "dialog.ok"),
			ResourceManager.get_instance().get( "dialog.cancel"),
			false, false);

		insert_horizontal_glue();


		adjust();


		setDefaultCloseOperation( DISPOSE_ON_CLOSE);

		return true;
	}

	/**
	 * 
	 */
	private void setup_velocity_textField() {
		JPanel panel = new JPanel();
		panel.setLayout( new BoxLayout( panel, BoxLayout.X_AXIS));

		panel.add( Box.createHorizontalStrut( 5));

		_velocity_label = new JLabel(
			ResourceManager.get_instance().get( "edit.common.property.dialog.animation.velocity"));
		_velocity_label.setHorizontalAlignment( SwingConstants.RIGHT);
		panel.add( _velocity_label);

		panel.add( Box.createHorizontalStrut( 5));

		_velocity_textField = new JTextField( String.valueOf( 101 - CommonProperty.get_instance()._divide));
		_velocity_textField.setEditable( false);
		_velocity_textField.setHorizontalAlignment( SwingConstants.RIGHT);
		panel.add( _velocity_textField);
		panel.add( Box.createHorizontalStrut( 5));
		getContentPane().add( panel);
	}

	/**
	 * 
	 */
	private void setup_velocity_slider() {
		JPanel panel = new JPanel();
		panel.setLayout( new BoxLayout( panel, BoxLayout.X_AXIS));

		panel.add( Box.createHorizontalStrut( 5));

		_velocity_slider = new JSlider();
		_velocity_slider.setValue( 101 - CommonProperty.get_instance()._divide);
		_velocity_slider.setPaintTicks( true);
		_velocity_slider.setPaintLabels( true);
		_velocity_slider.setMajorTickSpacing( 20);
		_velocity_slider.setMinorTickSpacing( 5);
		_velocity_slider.addChangeListener( new ChangeListener() {
			public void stateChanged(ChangeEvent arg0) {
				_velocity_textField.setText( String.valueOf( _velocity_slider.getValue()));
			}
		});

		panel.add( _velocity_slider);
		panel.add( Box.createHorizontalStrut( 5));
		getContentPane().add( panel);
	}

	/**
	 * 
	 */
	private void setup_agent_width_textField() {
		JPanel panel = new JPanel();
		panel.setLayout( new BoxLayout( panel, BoxLayout.X_AXIS));

		panel.add( Box.createHorizontalStrut( 5));

		_agent_width_label = new JLabel(
			ResourceManager.get_instance().get( "edit.common.property.dialog.agent.width"));
		_agent_width_label.setHorizontalAlignment( SwingConstants.RIGHT);
		panel.add( _agent_width_label);

		panel.add( Box.createHorizontalStrut( 5));

		_agent_width_textField = new JTextField( new TextLimiter( "0123456789"),
			String.valueOf( CommonProperty.get_instance()._agentWidth), 0);
		_agent_width_textField.setHorizontalAlignment( SwingConstants.RIGHT);

		panel.add( _agent_width_textField);
		panel.add( Box.createHorizontalStrut( 5));
		getContentPane().add( panel);
	}

	/**
	 * 
	 */
	private void setup_agent_height_textField() {
		JPanel panel = new JPanel();
		panel.setLayout( new BoxLayout( panel, BoxLayout.X_AXIS));

		panel.add( Box.createHorizontalStrut( 5));

		_agent_height_label = new JLabel(
			ResourceManager.get_instance().get( "edit.common.property.dialog.agent.height"));
		_agent_height_label.setHorizontalAlignment( SwingConstants.RIGHT);
		panel.add( _agent_height_label);

		panel.add( Box.createHorizontalStrut( 5));

		_agent_height_textField = new JTextField( new TextLimiter( "0123456789"),
			String.valueOf( CommonProperty.get_instance()._agentHeight), 0);
		_agent_height_textField.setHorizontalAlignment( SwingConstants.RIGHT);

		panel.add( _agent_height_textField);
		panel.add( Box.createHorizontalStrut( 5));
		getContentPane().add( panel);
	}

	/**
	 * 
	 */
	private void setup_spot_width_textField() {
		JPanel panel = new JPanel();
		panel.setLayout( new BoxLayout( panel, BoxLayout.X_AXIS));

		panel.add( Box.createHorizontalStrut( 5));

		_spot_width_label = new JLabel(
			ResourceManager.get_instance().get( "edit.common.property.dialog.spot.width"));
		_spot_width_label.setHorizontalAlignment( SwingConstants.RIGHT);
		panel.add( _spot_width_label);

		panel.add( Box.createHorizontalStrut( 5));

		_spot_width_textField = new JTextField( new TextLimiter( "0123456789"),
			String.valueOf( CommonProperty.get_instance()._spotWidth), 0);
		_spot_width_textField.setHorizontalAlignment( SwingConstants.RIGHT);

		panel.add( _spot_width_textField);
		panel.add( Box.createHorizontalStrut( 5));
		getContentPane().add( panel);
	}

	/**
	 * 
	 */
	private void setup_spot_height_textField() {
		JPanel panel = new JPanel();
		panel.setLayout( new BoxLayout( panel, BoxLayout.X_AXIS));

		panel.add( Box.createHorizontalStrut( 5));

		_spot_height_label = new JLabel(
			ResourceManager.get_instance().get( "edit.common.property.dialog.spot.height"));
		_spot_height_label.setHorizontalAlignment( SwingConstants.RIGHT);
		panel.add( _spot_height_label);

		panel.add( Box.createHorizontalStrut( 5));

		_spot_height_textField = new JTextField( new TextLimiter( "0123456789"),
			String.valueOf( CommonProperty.get_instance()._spotHeight), 0);
		_spot_height_textField.setHorizontalAlignment( SwingConstants.RIGHT);

		panel.add( _spot_height_textField);
		panel.add( Box.createHorizontalStrut( 5));
		getContentPane().add( panel);
	}

	/**
	 * 
	 */
	private void adjust() {
		if ( Application._demo)
			return;

		int width = _velocity_label.getPreferredSize().width;
		width = Math.max( width, _agent_width_label.getPreferredSize().width);
		width = Math.max( width, _agent_height_label.getPreferredSize().width);
		width = Math.max( width, _spot_width_label.getPreferredSize().width);
		width = Math.max( width, _spot_height_label.getPreferredSize().width);

		Dimension dimension = new Dimension( width,
			_velocity_label.getPreferredSize().height);
		_velocity_label.setPreferredSize( dimension);
		_agent_width_label.setPreferredSize( dimension);
		_agent_height_label.setPreferredSize( dimension);
		_spot_width_label.setPreferredSize( dimension);
		_spot_height_label.setPreferredSize( dimension);
	}

	/* (Non Javadoc)
	 * @see soars.common.utility.swing.window.Dialog#on_setup_completed()
	 */
	protected void on_setup_completed() {
		_velocity_slider.requestFocusInWindow();
	}

	/* (Non Javadoc)
	 * @see soars.common.utility.swing.window.Dialog#on_ok(java.awt.event.ActionEvent)
	 */
	protected void on_ok(ActionEvent actionEvent) {
		CommonProperty.get_instance()._divide = 101 - _velocity_slider.getValue();

		if ( !Application._demo) {
			int value = get_value( _agent_width_textField);
			CommonProperty.get_instance()._agentWidth = value;

			value = get_value( _agent_height_textField);
			CommonProperty.get_instance()._agentHeight = value;

			value = get_value( _spot_width_textField);
			CommonProperty.get_instance()._spotWidth = value;

			value = get_value( _spot_height_textField);
			CommonProperty.get_instance()._spotHeight = value;

			Observer.get_instance().modified();
		}

		super.on_ok(actionEvent);
	}

	/**
	 * @param textField
	 * @return
	 */
	private int get_value(JTextField textField) {
		int value = Integer.parseInt( textField.getText());
		if ( CommonProperty.get_instance()._minimumWidth > value)
			value = CommonProperty.get_instance()._minimumWidth;
//		if ( CommonProperty.get_instance()._maximum_width < value)
//			value = CommonProperty.get_instance()._maximum_width;
		return value;
	}
}
