/**
 * 
 */
package soars.common.utility.swing.border;

import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Insets;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JComponent;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.border.Border;

/**
 * @author kurata
 *
 */
public class ComponentTitledBorder implements Border, MouseListener, SwingConstants {

  /**
	 * 
	 */
	private static final int _offset = 5;

  /**
	 * 
	 */
	private final Component _component;

  /**
	 * 
	 */
	private final JComponent _container;

  /**
	 * 
	 */
	private final Border _border;

  /**
	 * 
	 */
	private Rectangle _rectangle = null;

  /**
	 * @param component
	 * @param container
	 * @param border
	 */
	public ComponentTitledBorder(Component component, JComponent container, Border border) {
		super();
		_component = component;
		_container = container;
		_border = border;
		if ( component instanceof JComponent) {
			( ( JComponent)component).setOpaque( true);
		}
		container.addMouseListener( this);
	}

	/* (non-Javadoc)
	 * @see javax.swing.border.Border#isBorderOpaque()
	 */
	public boolean isBorderOpaque() {
		return true;
	}

	/* (non-Javadoc)
	 * @see javax.swing.border.Border#paintBorder(java.awt.Component, java.awt.Graphics, int, int, int, int)
	 */
	public void paintBorder(Component component, Graphics graphics, int x, int y, int width, int height) {
		Insets borderInsets = _border.getBorderInsets( component);
		Insets insets = getBorderInsets( component);
		int temp = ( insets.top - borderInsets.top) / 2;
		_border.paintBorder( component, graphics, x, y + temp, width, height - temp);
		Dimension size = _component.getPreferredSize();
		_rectangle = new Rectangle( _offset, 0, size.width, size.height);
		SwingUtilities.paintComponent( graphics, _component, ( Container)component, _rectangle);
	}

	/* (non-Javadoc)
	 * @see javax.swing.border.Border#getBorderInsets(java.awt.Component)
	 */
	public Insets getBorderInsets(Component component) {
		Dimension size = _component.getPreferredSize();
		Insets insets = _border.getBorderInsets( component);
		insets.top = Math.max( insets.top, size.height);
		return insets;
	}

	/**
	 * @param mouseEvent
	 */
	private void dispatchEvent(MouseEvent mouseEvent) {
		if ( null != _rectangle && _rectangle.contains( mouseEvent.getX(), mouseEvent.getY())) {
			Point point = mouseEvent.getPoint();
			point.translate( -_offset, 0);
			_component.setBounds( _rectangle);
			_component.dispatchEvent( new MouseEvent(_component, mouseEvent.getID(), mouseEvent.getWhen(),
				mouseEvent.getModifiers(), point.x, point.y, mouseEvent.getClickCount(),mouseEvent.isPopupTrigger(), mouseEvent.getButton()));
			if ( !_component.isValid())
				_container.repaint();
		}
	}

	/* (non-Javadoc)
	 * @see java.awt.event.MouseListener#mouseClicked(java.awt.event.MouseEvent)
	 */
	public void mouseClicked(MouseEvent arg0) {
		dispatchEvent( arg0);
	}

	/* (non-Javadoc)
	 * @see java.awt.event.MouseListener#mouseEntered(java.awt.event.MouseEvent)
	 */
	public void mouseEntered(MouseEvent arg0) {
		dispatchEvent( arg0);
	}

	/* (non-Javadoc)
	 * @see java.awt.event.MouseListener#mouseExited(java.awt.event.MouseEvent)
	 */
	public void mouseExited(MouseEvent arg0) {
		dispatchEvent( arg0);
	}

	/* (non-Javadoc)
	 * @see java.awt.event.MouseListener#mousePressed(java.awt.event.MouseEvent)
	 */
	public void mousePressed(MouseEvent arg0) {
		dispatchEvent( arg0);
	}

	/* (non-Javadoc)
	 * @see java.awt.event.MouseListener#mouseReleased(java.awt.event.MouseEvent)
	 */
	public void mouseReleased(MouseEvent arg0) {
		dispatchEvent( arg0);
	}
}
