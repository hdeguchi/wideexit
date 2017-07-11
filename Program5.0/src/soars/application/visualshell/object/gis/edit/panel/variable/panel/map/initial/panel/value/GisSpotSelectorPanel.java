/**
 * 
 */
package soars.application.visualshell.object.gis.edit.panel.variable.panel.map.initial.panel.value;

import java.awt.Color;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JPanel;

import soars.application.visualshell.common.selector.ObjectSelector;

/**
 * @author kurata
 *
 */
public class GisSpotSelectorPanel extends JPanel {

	/**
	 * 
	 */
	public ObjectSelector _spotSelector = null;

	/**
	 * 
	 */
	public GisSpotSelectorPanel() {
		super();
	}

	/**
	 * @param color
	 */
	public void create(Color color) {
		setLayout( new BoxLayout( this, BoxLayout.X_AXIS));

		_spotSelector = new ObjectSelector( "spot", false, 80, 20, color, true, null);
		_spotSelector.selectFirstItem();
		add( _spotSelector);

		add( Box.createHorizontalStrut( 5));
	}

	/* (non-Javadoc)
	 * @see javax.swing.JComponent#setEnabled(boolean)
	 */
	@Override
	public void setEnabled(boolean enabled) {
		_spotSelector.setEnabled( enabled);
		super.setEnabled(enabled);
	}

	/* (non-Javadoc)
	 * @see javax.swing.JComponent#setVisible(boolean)
	 */
	@Override
	public void setVisible(boolean aFlag) {
		_spotSelector.setVisible( aFlag);
		super.setVisible(aFlag);
	}

	/**
	 * @param value
	 */
	public void set(String value) {
		_spotSelector.set( value);
	}

	/**
	 * @return
	 */
	public String get() {
		return _spotSelector.get();
	}
}
