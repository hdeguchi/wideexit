/*
 * Created on 2006/01/18
 */
package soars.common.utility.swing.color;

import java.awt.Color;
import java.awt.event.MouseEvent;

/**
 * @author kurata
 */
public class ColorControl extends ColorControlBase {

	/**
	 * 
	 */
	private IColorSelection _colorSelection = null;

	/**
	 * @param color
	 * @param colorSelection
	 * 
	 */
	public ColorControl(Color color, IColorSelection colorSelection) {
		super(color);
		_colorSelection = colorSelection;
	}

	/* (Non Javadoc)
	 * @see soars.common.utility.swing.window.Control#on_mouse_pressed(java.awt.event.MouseEvent)
	 */
	protected void on_mouse_pressed(MouseEvent mouseEvent) {
		_colorSelection.on_selected( _color);
	}
}
