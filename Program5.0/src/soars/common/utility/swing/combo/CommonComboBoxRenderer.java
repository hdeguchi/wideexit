/*
 * 2005/06/01
 */
package soars.common.utility.swing.combo;

import java.awt.Color;
import java.awt.Component;

import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.plaf.basic.BasicComboBoxRenderer;

/**
 * @author kurata
 */
public class CommonComboBoxRenderer extends BasicComboBoxRenderer {

	/**
	 * 
	 */
	private Color _color = null;

	/**
	 * 
	 */
	private boolean _right = false;

	/**
	 * @param color
	 * @param right
	 */
	public CommonComboBoxRenderer(Color color, boolean right) {
		super();
		_color = color;
		_right = right;
//		if ( 0 <= System.getProperty( "os.name").indexOf( "Mac"))
//			setBorder( new EmptyBorder( 2, 0, 0, 0));
	}

	/* (Non Javadoc)
	 * @see javax.swing.ListCellRenderer#getListCellRendererComponent(javax.swing.JList, java.lang.Object, int, boolean, boolean)
	 */
	public Component getListCellRendererComponent(
		JList arg0,
		Object arg1,
		int arg2,
		boolean arg3,
		boolean arg4) {
		String text = ( null == arg1) ? "" : arg1.toString();
		setOpaque( true);
		if ( 0 <= System.getProperty( "os.name").indexOf( "Mac")) {
			if ( arg3) {
				setForeground( ( null == _color) ? arg0.getForeground() : Color.white);
				setBackground( ( null == _color) ? arg0.getSelectionBackground() : _color);
			} else {
				setForeground( ( null == _color) ? arg0.getForeground() : _color);
				setBackground( ( null == _color) ? arg0.getBackground() : Color.white);
			}
		} else {
			if ( arg3) {
				setForeground( Color.white);
				setBackground( ( null == _color) ? arg0.getSelectionBackground() : _color);
			} else {
				setForeground( ( null == _color) ? arg0.getForeground() : _color);
				setBackground( arg0.getBackground());
			}
		}

		if ( _right)
			setHorizontalAlignment( JLabel.RIGHT);

		setText( text);
		return this;
	}
}
