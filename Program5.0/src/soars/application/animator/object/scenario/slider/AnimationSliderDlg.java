/*
 * 2005/03/31
 */
package soars.application.animator.object.scenario.slider;

import java.awt.BorderLayout;
import java.awt.Frame;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import soars.application.animator.object.scenario.IScenarioManipulator;
import soars.common.utility.swing.window.Dialog;

/**
 * The dialog box to set the position of the scenario.
 * @author kurata / SOARS project
 */
public class AnimationSliderDlg extends Dialog {

	/**
	 * 
	 */
	private IScenarioManipulator _scenarioManipulator = null;

	/**
	 * 
	 */
	private JSlider _animationSlider = null;

	/**
	 * 
	 */
	private int _minimumWidth = 400;

	/**
	 * 
	 */
	private int _minimumHeight;

	/**
	 * Creates a non-modal dialog with the specified title and with the specified owner frame. If owner is null, a shared, hidden frame will be set as the owner of the dialog. 
	 * @param arg0 the Frame from which the dialog is displayed
	 * @param arg1 the String to display in the dialog's title bar
	 * @param arg2 true for a modal dialog, false for one that allows other windows to be active at the same time
	 * @param scenarioManipulator
	 */
	public AnimationSliderDlg(Frame arg0, String arg1, boolean arg2, IScenarioManipulator scenarioManipulator) {
		super(arg0, arg1, arg2);
		_scenarioManipulator = scenarioManipulator;
	}

	/* (Non Javadoc)
	 * @see soars.common.utility.swing.window.Dialog#on_init_dialog()
	 */
	protected boolean on_init_dialog() {
		if ( !super.on_init_dialog())
			return false;

		//setResizable( false);

		getContentPane().setLayout( new BorderLayout());


		JPanel centerPanel = new JPanel();
		centerPanel.setLayout( new BoxLayout( centerPanel, BoxLayout.Y_AXIS));

		setup_animationSlider( centerPanel);

		getContentPane().add( centerPanel);


		pack();


		_minimumHeight = getPreferredSize().height;


		int width = getSize().width;
		setSize( ( _minimumWidth > width) ? _minimumWidth : width, _minimumHeight);


		addComponentListener( new ComponentAdapter() {
			public void componentResized(ComponentEvent e){
				int width = getSize().width;
				setSize( ( _minimumWidth > width) ? _minimumWidth : width, _minimumHeight);
			}
		});


		return true;
	}

	/**
	 * @param parent
	 * 
	 */
	private void setup_animationSlider(JPanel parent) {
		JPanel panel = new JPanel();
		panel.setLayout( new BoxLayout( panel, BoxLayout.X_AXIS));

		panel.add( Box.createHorizontalStrut( 5));

		_animationSlider = new JSlider();
		_animationSlider.setMinimum( 1);
		_animationSlider.setMaximum( _scenarioManipulator.get_size());
		_animationSlider.setValue( _scenarioManipulator.get_current_position());
		_animationSlider.setPaintTicks( true);
		_animationSlider.setPaintLabels( true);
		_animationSlider.setMajorTickSpacing( _animationSlider.getMaximum() / 5);
		_animationSlider.setMinorTickSpacing( _animationSlider.getMajorTickSpacing() / 4);
//		_animationSlider.setMajorTickSpacing( 20);
//		_animationSlider.setMinorTickSpacing( 5);
		_animationSlider.addChangeListener( new ChangeListener() {
			public void stateChanged(ChangeEvent arg0) {
				on_state_changed( arg0);
			}
		});

		panel.add( _animationSlider);

		panel.add( Box.createHorizontalStrut( 5));

		parent.add( panel);
	}

	/**
	 * @param changeEvent
	 */
	protected void on_state_changed(ChangeEvent changeEvent) {
		if ( _animationSlider.isEnabled() && !_animationSlider.getValueIsAdjusting()) {
			_scenarioManipulator.on_animationSlider_state_changed( _animationSlider.getValue() - 1);
			_animationSlider.requestFocus();
		}
	}

	/**
	 * Sets whether or not the slider is enabled.
	 * @param enable true if the slider should be enabled, false otherwise
	 */
	public void enable_user_interface(boolean enable) {
		_animationSlider.setEnabled( enable);
	}

	/**
	 * Sets the position of the slider with the specified new position.
	 * @param index the specified new position
	 */
	public void update(int index) {
		_animationSlider.setValue( index + 1);
	}
}
