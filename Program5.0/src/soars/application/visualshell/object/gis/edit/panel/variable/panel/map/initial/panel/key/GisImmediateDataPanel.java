/**
 * 
 */
package soars.application.visualshell.object.gis.edit.panel.variable.panel.map.initial.panel.key;

import java.awt.Color;

import javax.swing.Box;
import javax.swing.BoxLayout;

import soars.application.visualshell.main.Constant;
import soars.application.visualshell.object.gis.edit.panel.variable.panel.base.panel.GisInitialValueTextPanelBase;
import soars.common.utility.swing.text.TextExcluder;
import soars.common.utility.swing.text.TextField;
import soars.common.utility.swing.text.TextUndoRedoManager;

/**
 * @author kurata
 *
 */
public class GisImmediateDataPanel extends GisInitialValueTextPanelBase {

	/**
	 * 
	 */
	public GisImmediateDataPanel() {
		super();
	}

	/**
	 * @param color
	 */
	public void create(Color color) {
		setLayout( new BoxLayout( this, BoxLayout.X_AXIS));

		_textField = new TextField( new TextExcluder( Constant._prohibitedCharacters3), color, false);
		_textUndoRedoManagers.add( new TextUndoRedoManager( _textField, this));
		add( _textField);

		add( Box.createHorizontalStrut( 5));
	}

	/* (non-Javadoc)
	 * @see javax.swing.JComponent#setEnabled(boolean)
	 */
	@Override
	public void setEnabled(boolean enabled) {
		_textField.setEnabled( enabled);
		super.setEnabled(enabled);
	}

	/* (non-Javadoc)
	 * @see javax.swing.JComponent#setVisible(boolean)
	 */
	@Override
	public void setVisible(boolean aFlag) {
		_textField.setVisible( aFlag);
		super.setVisible(aFlag);
	}
}
