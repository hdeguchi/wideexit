/*
 * Created on 2006/01/18
 */
package soars.common.utility.swing.color;

import java.awt.Color;
import java.awt.Graphics;

import soars.common.utility.swing.window.Control;

/**
 * @author kurata
 */
public class ColorControlBase extends Control {

	/**
	 * 
	 */
	public Color _color = null;

	/**
	 * @param color
	 */
	public ColorControlBase(Color color) {
		super();
		_color = new Color( color.getRGB());
	}

	/* (Non Javadoc)
	 * @see javax.swing.JComponent#paintComponent(java.awt.Graphics)
	 */
	protected void paintComponent(Graphics arg0) {
		arg0.setColor( _color);
		arg0.fillRect( 0, 0, getPreferredSize().width, getPreferredSize().height);

		arg0.setColor( Color.black);
		arg0.fillRect( 0, 0, getPreferredSize().width, 1);
		arg0.fillRect( 0, 0, 1, getPreferredSize().height);

		arg0.setColor( Color.white);
		arg0.fillRect( 0, getPreferredSize().height - 1, getPreferredSize().width, 1);
		arg0.fillRect( getPreferredSize().width - 1, 0, 1, getPreferredSize().height);
	}
}
