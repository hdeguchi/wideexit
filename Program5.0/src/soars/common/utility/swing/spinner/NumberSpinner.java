/*
 * Created on 2006/01/27
 */
package soars.common.utility.swing.spinner;

import java.awt.Color;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.SpinnerNumberModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import soars.common.utility.swing.panel.StandardPanel;
import soars.common.utility.swing.window.Dialog;

/**
 * @author kurata
 */
public class NumberSpinner extends JSpinner {

	/**
	 * 
	 */
	static protected Color[] _textFieldDefaultColors = new Color[ 3];

	/**
	 * 
	 */
	protected Color _color = null;

	/**
	 * 
	 */
	protected INumberSpinnerHandler _numberSpinnerHandler = null;

	/**
	 * 
	 */
	protected JTextField _textField = null;

	/**
	 * 
	 */
	public NumberSpinner() {
		super();
		SpinnerNumberModel spinnerNumberModel = new SpinnerNumberModel( 1, 1, 1, 1);
		setModel( spinnerNumberModel);
		setup();
	}

	/**
	 * @param standardPanel
	 */
	public NumberSpinner(StandardPanel standardPanel) {
		super();
		SpinnerNumberModel spinnerNumberModel = new SpinnerNumberModel( 1, 1, 1, 1);
		setModel( spinnerNumberModel);
		setup( standardPanel);
	}

	/**
	 * @param dialog
	 */
	public NumberSpinner(Dialog dialog) {
		super();
		SpinnerNumberModel spinnerNumberModel = new SpinnerNumberModel( 1, 1, 1, 1);
		setModel( spinnerNumberModel);
		setup( dialog);
	}

	/**
	 * @param color
	 */
	public NumberSpinner(Color color) {
		super();
		_color = color;
		SpinnerNumberModel spinnerNumberModel = new SpinnerNumberModel( 1, 1, 1, 1);
		setModel( spinnerNumberModel);
		setup();
	}

	/**
	 * @param color
	 * @param standardPanel
	 */
	public NumberSpinner(Color color, StandardPanel standardPanel) {
		super();
		_color = color;
		SpinnerNumberModel spinnerNumberModel = new SpinnerNumberModel( 1, 1, 1, 1);
		setModel( spinnerNumberModel);
		setup( standardPanel);
	}

	/**
	 * @param color
	 * @param dialog
	 */
	public NumberSpinner(Color color, Dialog dialog) {
		super();
		_color = color;
		SpinnerNumberModel spinnerNumberModel = new SpinnerNumberModel( 1, 1, 1, 1);
		setModel( spinnerNumberModel);
		setup( dialog);
	}

	/**
	 * @param arg0
	 */
	public NumberSpinner(SpinnerNumberModel arg0) {
		super(arg0);
		setup();
	}

	/**
	 * @param arg0
	 * @param standardPanel
	 */
	public NumberSpinner(SpinnerNumberModel arg0, StandardPanel standardPanel) {
		super(arg0);
		setup( standardPanel);
	}

	/**
	 * @param arg0
	 * @param dialog
	 */
	public NumberSpinner(SpinnerNumberModel arg0, Dialog dialog) {
		super(arg0);
		setup( dialog);
	}

	/**
	 * @param arg0
	 * @param color
	 */
	public NumberSpinner(SpinnerNumberModel arg0, Color color) {
		super(arg0);
		_color = color;
		setup();
	}

	/**
	 * @param arg0
	 * @param color
	 * @param standardPanel
	 */
	public NumberSpinner(SpinnerNumberModel arg0, Color color, StandardPanel standardPanel) {
		super(arg0);
		_color = color;
		setup( standardPanel);
	}

	/**
	 * @param arg0
	 * @param color
	 * @param dialog
	 */
	public NumberSpinner(SpinnerNumberModel arg0, Color color, Dialog dialog) {
		super(arg0);
		_color = color;
		setup( dialog);
	}

	/**
	 * @param numberSpinnerHandler
	 */
	public NumberSpinner(INumberSpinnerHandler numberSpinnerHandler) {
		super();
		_numberSpinnerHandler = numberSpinnerHandler;
		SpinnerNumberModel spinnerNumberModel = new SpinnerNumberModel( 1, 1, 1, 1);
		setModel( spinnerNumberModel);
		setup();
	}

	/**
	 * @param numberSpinnerHandler
	 * @param standardPanel
	 */
	public NumberSpinner(INumberSpinnerHandler numberSpinnerHandler, StandardPanel standardPanel) {
		super();
		_numberSpinnerHandler = numberSpinnerHandler;
		SpinnerNumberModel spinnerNumberModel = new SpinnerNumberModel( 1, 1, 1, 1);
		setModel( spinnerNumberModel);
		setup( standardPanel);
	}

	/**
	 * @param numberSpinnerHandler
	 * @param dialog
	 */
	public NumberSpinner(INumberSpinnerHandler numberSpinnerHandler, Dialog dialog) {
		super();
		_numberSpinnerHandler = numberSpinnerHandler;
		SpinnerNumberModel spinnerNumberModel = new SpinnerNumberModel( 1, 1, 1, 1);
		setModel( spinnerNumberModel);
		setup( dialog);
	}

	/**
	 * @param color
	 * @param numberSpinnerHandler
	 */
	public NumberSpinner(Color color, INumberSpinnerHandler numberSpinnerHandler) {
		super();
		_color = color;
		_numberSpinnerHandler = numberSpinnerHandler;
		SpinnerNumberModel spinnerNumberModel = new SpinnerNumberModel( 1, 1, 1, 1);
		setModel( spinnerNumberModel);
		setup();
	}

	/**
	 * @param color
	 * @param numberSpinnerHandler
	 * @param standardPanel
	 */
	public NumberSpinner(Color color, INumberSpinnerHandler numberSpinnerHandler, StandardPanel standardPanel) {
		super();
		_color = color;
		_numberSpinnerHandler = numberSpinnerHandler;
		SpinnerNumberModel spinnerNumberModel = new SpinnerNumberModel( 1, 1, 1, 1);
		setModel( spinnerNumberModel);
		setup( standardPanel);
	}

	/**
	 * @param color
	 * @param numberSpinnerHandler
	 * @param dialog
	 */
	public NumberSpinner(Color color, INumberSpinnerHandler numberSpinnerHandler, Dialog dialog) {
		super();
		_color = color;
		_numberSpinnerHandler = numberSpinnerHandler;
		SpinnerNumberModel spinnerNumberModel = new SpinnerNumberModel( 1, 1, 1, 1);
		setModel( spinnerNumberModel);
		setup( dialog);
	}

	/**
	 * @param arg0
	 * @param numberSpinnerHandler
	 */
	public NumberSpinner(SpinnerNumberModel arg0, INumberSpinnerHandler numberSpinnerHandler) {
		super(arg0);
		_numberSpinnerHandler = numberSpinnerHandler;
		setup();
	}

	/**
	 * @param arg0
	 * @param numberSpinnerHandler
	 * @param standardPanel
	 */
	public NumberSpinner(SpinnerNumberModel arg0, INumberSpinnerHandler numberSpinnerHandler, StandardPanel standardPanel) {
		super(arg0);
		_numberSpinnerHandler = numberSpinnerHandler;
		setup( standardPanel);
	}

	/**
	 * @param arg0
	 * @param numberSpinnerHandler
	 * @param dialog
	 */
	public NumberSpinner(SpinnerNumberModel arg0, INumberSpinnerHandler numberSpinnerHandler, Dialog dialog) {
		super(arg0);
		_numberSpinnerHandler = numberSpinnerHandler;
		setup( dialog);
	}

	/**
	 * @param arg0
	 * @param color
	 * @param numberSpinnerHandler
	 */
	public NumberSpinner(SpinnerNumberModel arg0, Color color, INumberSpinnerHandler numberSpinnerHandler) {
		super(arg0);
		_numberSpinnerHandler = numberSpinnerHandler;
		_color = color;
		setup();
	}

	/**
	 * @param arg0
	 * @param color
	 * @param numberSpinnerHandler
	 * @param standardPanel
	 */
	public NumberSpinner(SpinnerNumberModel arg0, Color color, INumberSpinnerHandler numberSpinnerHandler, StandardPanel standardPanel) {
		super(arg0);
		_numberSpinnerHandler = numberSpinnerHandler;
		_color = color;
		setup( standardPanel);
	}

	/**
	 * @param arg0
	 * @param color
	 * @param numberSpinnerHandler
	 * @param dialog
	 */
	public NumberSpinner(SpinnerNumberModel arg0, Color color, INumberSpinnerHandler numberSpinnerHandler, Dialog dialog) {
		super(arg0);
		_numberSpinnerHandler = numberSpinnerHandler;
		_color = color;
		setup( dialog);
	}

	/**
	 * @param standardPanel
	 */
	private void setup(StandardPanel standardPanel) {
		setup();

		standardPanel.link_to_ok( _textField);
		standardPanel.link_to_cancel( _textField);

		standardPanel.link_to_ok( this);
		standardPanel.link_to_cancel( this);
	}

	/**
	 * @param dialog
	 */
	private void setup(Dialog dialog) {
		setup();

		dialog.link_to_ok( _textField);
		dialog.link_to_cancel( _textField);

		dialog.link_to_ok( this);
		dialog.link_to_cancel( this);
	}

	/**
	 * 
	 */
	private void setup() {
		//JSpinner.NumberEditor numberEditor = ( NumberEditor)getEditor();
		JSpinner.NumberEditor numberEditor = new JSpinner.NumberEditor( this, "0");
		setEditor( numberEditor);
		_textField = numberEditor.getTextField();

		if ( null != _color) {
			if ( null == _textFieldDefaultColors[ 0])
				_textFieldDefaultColors[ 0] = _textField.getSelectionColor();
			if ( null == _textFieldDefaultColors[ 1])
				_textFieldDefaultColors[ 1] = _textField.getForeground();
			_textField.setEditable( false);
			if ( null == _textFieldDefaultColors[ 2])
				_textFieldDefaultColors[ 2] = _textField.getBackground();

			_textField.setSelectionColor( _color);
			_textField.setForeground( Color.white);
			_textField.setBackground( _color);

			_textField.setEditable( true);
		}

		_textField.addKeyListener( new KeyListener() {
			public void keyPressed(KeyEvent arg0) {
			}
			public void keyReleased(KeyEvent arg0) {
				on_key_released( arg0);
			}
			public void keyTyped(KeyEvent arg0) {
			}
		});

		addChangeListener( new ChangeListener() {
			public void stateChanged(ChangeEvent arg0) {
				on_state_changed( arg0);
			}
		});
	}

	/**
	 * @param keyEvent
	 */
	protected void on_key_released(KeyEvent keyEvent) {
		String text = _textField.getText();
		String newText = "";
		for ( int i = 0; i < text.length(); ++i) {
			if ( 0 <= "0123456789".indexOf( text.charAt( i)))
				newText += text.charAt( i);
		}

		SpinnerNumberModel spinnerNumberModel = ( SpinnerNumberModel)getModel();
		Integer minInteger = ( Integer)spinnerNumberModel.getMinimum();
		Integer maxInteger = ( Integer)spinnerNumberModel.getMaximum();

		int value;
		try {
			value = Integer.parseInt( newText);
			if ( minInteger.intValue() > value)
				newText = String.valueOf( minInteger);
			if ( value > maxInteger.intValue())
				newText = String.valueOf( maxInteger);
		} catch (NumberFormatException e) {
			newText = String.valueOf( minInteger);
		}

		if ( !newText.equals( text))
			_textField.setText( newText);

		if ( null != _numberSpinnerHandler)
			_numberSpinnerHandler.changed( newText, this);
	}

	/**
	 * @param changeEvent
	 */
	protected void on_state_changed(ChangeEvent changeEvent) {
		if ( null != _numberSpinnerHandler)
			_numberSpinnerHandler.changed( String.valueOf( get_value()), this);
	}

	/* (Non Javadoc)
	 * @see java.awt.Component#setEnabled(boolean)
	 */
	public void setEnabled(boolean arg0) {
		super.setEnabled(arg0);
		_textField.setEditable( arg0);

		if ( null == _color)
			return;

		_textField.setSelectionColor( arg0 ? _color : _textFieldDefaultColors[ 0]);
		_textField.setForeground( arg0 ? Color.white : _textFieldDefaultColors[ 1]);
		_textField.setBackground( arg0 ? _color : _textFieldDefaultColors[ 2]);
	}

	/**
	 * @return
	 */
	public int get_value() {
		SpinnerNumberModel spinnerNumberModel = ( SpinnerNumberModel)getModel();
		Integer integer = ( Integer)spinnerNumberModel.getValue();
		return integer.intValue();
	}

	/**
	 * @return
	 */
	public int get_maximum() {
		SpinnerNumberModel spinnerNumberModel = ( SpinnerNumberModel)getModel();
		Integer integer = ( Integer)spinnerNumberModel.getMaximum();
		return integer.intValue();
	}

	/**
	 * @return
	 */
	public int get_minimum() {
		SpinnerNumberModel spinnerNumberModel = ( SpinnerNumberModel)getModel();
		Integer integer = ( Integer)spinnerNumberModel.getMinimum();
		return integer.intValue();
	}

	/**
	 * @param value
	 */
	public void set_value(int value) {
		SpinnerNumberModel spinnerNumberModel = ( SpinnerNumberModel)getModel();
		spinnerNumberModel.setValue( new Integer( value));
	}

	/**
	 * @param maximum
	 */
	public void set_maximum(int maximum) {
		SpinnerNumberModel spinnerNumberModel = ( SpinnerNumberModel)getModel();
		spinnerNumberModel.setMaximum( new Integer( maximum));
		
	}

	/**
	 * @param minimum
	 */
	public void set_minimum(int minimum) {
		SpinnerNumberModel spinnerNumberModel = ( SpinnerNumberModel)getModel();
		spinnerNumberModel.setMinimum( new Integer( minimum));
	}
}
