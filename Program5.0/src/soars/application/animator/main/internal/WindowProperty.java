/**
 * 
 */
package soars.application.animator.main.internal;

import java.awt.Rectangle;

/**
 * @author kurata
 *
 */
public class WindowProperty {

	/**
	 * 
	 */
	public Rectangle _rectangle = null;

	/**
	 * 
	 */
	public boolean _maximum = false;

	/**
	 * 
	 */
	public boolean _icon = false;

	/**
	 * 
	 */
	public int _order = -1;

	/**
	 * 
	 */
	public WindowProperty() {
		super();
	}

	/**
	 * @param width
	 * @param height
	 */
	public WindowProperty(int width, int height) {
		super();
		_rectangle = new Rectangle( 0, 0, width, height);
	}

	/**
	 * @param rectangle
	 * @param isMaximum
	 * @param isIcon
	 * @param order
	 */
	public WindowProperty(Rectangle rectangle, boolean maximum, boolean icon, int order) {
		super();
		_rectangle = rectangle;
		_maximum = maximum;
		_icon = icon;
		_order = order;
	}

	/**
	 * @param windowProperty
	 */
	public WindowProperty(WindowProperty windowProperty) {
		super();
		_rectangle = new Rectangle( windowProperty._rectangle);
		_maximum = windowProperty._maximum;
		_icon = windowProperty._icon;
		_order = windowProperty._order;
	}
}
