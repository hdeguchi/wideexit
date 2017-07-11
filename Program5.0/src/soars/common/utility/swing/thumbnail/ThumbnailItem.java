/**
 * 
 */
package soars.common.utility.swing.thumbnail;

import java.awt.Color;
import java.awt.event.MouseEvent;
import java.io.File;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.border.Border;

import soars.common.utility.swing.border.DotBorder;
import soars.common.utility.swing.image.ImageProperty;

/**
 * The thumbnail panel as a element of the thumbnail list.
 * @author kurata / SOARS project
 */
public class ThumbnailItem extends JPanel {

	/**
	 * Customized empty border.
	 */
	static private final DotBorder _dotBorder = new DotBorder( 2, 2, 2, 2);

	/**
	 * Empty and transparent border which takes up space but does no drawing.
	 */
	static private final Border _emptyBorder = BorderFactory.createEmptyBorder( 2, 2, 2, 2);

  public JLabel _label = null;

	/**
	 * Width of the image.
	 */
	public int _width = 0;

	/**
	 * Height of the image.
	 */
	public int _height = 0;

	/**
	 * Image file.
	 */
	public File _file = null;

	/**
	 * File name.
	 */
	public JLabel _name = null;

	/**
	 * Creates the thumbnail panel as a element of the thumbnail list.
	 * @param imageIcon the thumbnail image
	 * @param imageProperty the image property
	 * @param file the image file
	 */
	public ThumbnailItem(ImageIcon imageIcon, ImageProperty imageProperty, File file) {
		super();
		initialize( imageIcon, imageProperty._width, imageProperty._height, file);
	}

	/**
	 * Initializes this thumbnail panel as a element of the thumbnail list.
	 * @param imageIcon the thumbnail image
	 * @param width the width of the image
	 * @param height the height of the image
	 * @param file the image file
	 */
	private void initialize(ImageIcon imageIcon, int width, int height, File file) {
		_width = width;
		_height = height;
		_file = file;

		setBackground( Color.white);

		JPanel parent = new JPanel();
		parent.setLayout( new BoxLayout( parent, BoxLayout.Y_AXIS));
		parent.setBackground( Color.white);

		JPanel panel = new JPanel();
		panel.setBackground( Color.white);
		_label = new JLabel();
		_label.setIcon( imageIcon);
		_label.setBackground( Color.white);
		_label.setOpaque( true);
		_label.setVisible( true);
		_label.revalidate();
		panel.add( _label);
		parent.add( panel);

		panel = new JPanel();
		panel.setBackground( Color.white);
		_name = new JLabel( _file.getName());
		_name.setOpaque( true);
		_name.setVisible( true);
		_name.revalidate();
		panel.add( _name);
		parent.add( panel);

		add( parent, "North");
	}

	/* (non-Javadoc)
	 * @see javax.swing.JComponent#getToolTipText(java.awt.event.MouseEvent)
	 */
	public String getToolTipText(MouseEvent mouseEvent) {
		if ( !_label.getBounds().contains( mouseEvent.getPoint()))
			return null;

		return ( _width + " x " + _height);
	}

	/**
	 * Sets the border for this panel.
	 * @param list the thumbnail list
	 * @param index the index of this component
	 * @param isSelected true if this component was selected
	 * @param cellHasFocus true if this component has the focus
	 */
	public void set_border(JList list, int index, boolean isSelected, boolean cellHasFocus) {
		_label.setBorder( BorderFactory.createLineBorder( ( isSelected ? Color.red : Color.blue), 1/*( isSelected ? 2 : 1)*/));
		_name.setBorder( cellHasFocus ? _dotBorder : _emptyBorder);
		_name.setForeground( isSelected ? list.getSelectionForeground() : list.getForeground());
		_name.setBackground( isSelected ? list.getSelectionBackground() : list.getBackground());
	}
}
