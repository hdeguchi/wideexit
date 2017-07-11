/*
 * Created on 2006/04/30
 */
package soars.common.utility.swing.spinner;

import java.awt.event.KeyEvent;

import javax.swing.SpinnerNumberModel;

import soars.common.utility.swing.spinner.INumberSpinnerHandler;
import soars.common.utility.swing.spinner.NumberSpinner;

/**
 * @author kurata
 */
public class CustomNumberSpinner extends NumberSpinner {

	/**
	 * @param numberSpinnerHandler
	 */
	public CustomNumberSpinner(INumberSpinnerHandler numberSpinnerHandler) {
		super(numberSpinnerHandler);
	}

	/* (Non Javadoc)
	 * @see soars.common.utility.swing.spinner.NumberSpinner#on_key_released(java.awt.event.KeyEvent)
	 */
	protected void on_key_released(KeyEvent keyEvent) {
		String text = _textField.getText();
		String newText = "";
		for ( int i = 0; i < text.length(); ++i) {
			if ( 0 <= "0123456789".indexOf( text.charAt( i)))
				newText += text.charAt( i);
		}

		if ( text.matches( "[0]{2,}"))
			newText = newText.replaceFirst( "[0]{2,}", "0");
		else if ( text.matches( "[0]+[^0]+.*"))
			newText = newText.replaceFirst( "[0]+", "");

		SpinnerNumberModel spinnerNumberModel = ( SpinnerNumberModel)getModel();
		Integer minInteger = ( Integer)spinnerNumberModel.getMinimum();
		Integer maxInteger = ( Integer)spinnerNumberModel.getMaximum();

		try {
			int value = Integer.parseInt( newText);
			if ( minInteger.intValue() > value)
				newText = String.valueOf( minInteger);
			if ( value > maxInteger.intValue())
				newText = String.valueOf( maxInteger);
		} catch (NumberFormatException e) {
			newText = "";
			set_value( 0);
			_textField.setText( newText);
//			new_text = String.valueOf( min_integer);
		}

		if ( !newText.equals( text))
			_textField.setText( newText);

		if ( null != _numberSpinnerHandler)
			_numberSpinnerHandler.changed( newText, this);
	}
}
