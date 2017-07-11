/**
 * 
 */
package soars.application.animator.common.panel;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

import soars.application.animator.main.ResourceManager;
import soars.common.utility.swing.color.ColorDlg;
import soars.common.utility.swing.window.Dialog;

/**
 * The common GUI panel for color selection.
 * @author kurata / SOARS project
 */
public class ColorPanel extends JPanel {

	/**
	 * Name of object.
	 */
	private String _name = "";

	/**
	 * Label for image color.
	 */
	public JLabel _imageColorLabel = null;

	/**
	 * Label for text color.
	 */
	public JLabel _textColorLabel = null;

	/**
	 * Button for image color selection.
	 */
	public JButton _imageColorButton = null;

	/**
	 * Button for text color selection.
	 */
	public JButton _textColorButton = null;

	/**
	 * Frame of the parent container.
	 */
	private Frame _owner = null;

	/**
	 * Parent container of this component.
	 */
	private Component _parent = null;

	/**
	 * Creates the common GUI panel for color selection.
	 * @param owner the frame of the parent container
	 * @param parent the parent container of this component
	 */
	public ColorPanel(Frame owner, Component parent) {
		super();
		_owner = owner;
		_parent = parent;
	}

	/**
	 * Creates the common GUI panel for color selection.
	 * @param name the name of object
	 * @param owner the frame of the parent container
	 * @param parent the parent container of this component
	 */
	public ColorPanel(String name, Frame owner, Component parent) {
		super();
		_name = name;
		_owner = owner;
		_parent = parent;
	}

	/**
	 * Creates the components to select image and text color.
	 * @param imageColor the specified image color
	 * @param textColor the specified text color
	 * @param dialog the parent container of this component
	 */
	public void setup(Color imageColor, Color textColor, Dialog dialog) {
		setLayout( new BoxLayout( this, BoxLayout.X_AXIS));

		add( Box.createHorizontalStrut( 5));

		JPanel panel = new JPanel();
		panel.setLayout( new BoxLayout( panel, BoxLayout.Y_AXIS));

		panel.setBorder( BorderFactory.createTitledBorder(
			ResourceManager.get_instance().get( "color.panel.border.title")));

		setup_image_color_label( imageColor, panel);

		dialog.insert_horizontal_glue( panel);

		setup_text_color_label( textColor, panel);

		add( panel);

		add( Box.createHorizontalStrut( 5));
	}

	/**
	 * Creates the components to select image color.
	 * @param imageColor the specified image color
	 * @param dialog the parent container of this component
	 */
	public void setup(Color imageColor, Dialog dialog) {
		setLayout( new BoxLayout( this, BoxLayout.X_AXIS));

		add( Box.createHorizontalStrut( 5));

		JPanel panel = new JPanel();
		panel.setLayout( new BoxLayout( panel, BoxLayout.Y_AXIS));

		panel.setBorder( BorderFactory.createTitledBorder(
			ResourceManager.get_instance().get( "color.panel.border.title")));

		setup_image_color_label( imageColor, panel);

		add( panel);

		add( Box.createHorizontalStrut( 5));
	}

	/**
	 * Creates the components to select image color.
	 * @param imageColor the specified image color
	 * @param parent the parent container of these components
	 */
	private void setup_image_color_label(Color imageColor, JPanel parent) {
		JPanel panel = new JPanel();
		panel.setLayout( new BoxLayout( panel, BoxLayout.X_AXIS));

		panel.add( Box.createHorizontalStrut( 5));

		_imageColorLabel = new JLabel( " ");
		_imageColorLabel.setOpaque( true);
		_imageColorLabel.setBackground( imageColor);
		_imageColorLabel.setBorder( BorderFactory.createLineBorder( _imageColorLabel.getForeground(), 1));
		panel.add( _imageColorLabel);

		panel.add( Box.createHorizontalStrut( 5));

		JPanel buttonPanel = new JPanel();
		buttonPanel.setLayout( new GridLayout( 1, 1));

		_imageColorButton = new JButton(
			ResourceManager.get_instance().get( "color.panel.image.color.button"));
		_imageColorButton.addActionListener( new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				on_get_color( _imageColorLabel,
					ResourceManager.get_instance().get( "color.dialog.image.color.title"));
			}
		});
		buttonPanel.add( _imageColorButton);

		panel.add( buttonPanel);

		panel.add( Box.createHorizontalStrut( 5));

		parent.add( panel);
	}

	/**
	 * Creates the components to select text color.
	 * @param textColor the specified text color
	 * @param parent the parent container of these components
	 */
	private void setup_text_color_label(Color textColor, JPanel parent) {
		JPanel panel = new JPanel();
		panel.setLayout( new BoxLayout( panel, BoxLayout.X_AXIS));

		panel.add( Box.createHorizontalStrut( 5));

		_textColorLabel = new JLabel( " ");
		_textColorLabel.setOpaque( true);
		_textColorLabel.setBackground( textColor);
		_textColorLabel.setBorder( BorderFactory.createLineBorder( _textColorLabel.getForeground(), 1));
		panel.add( _textColorLabel);

		panel.add( Box.createHorizontalStrut( 5));

		JPanel buttonPanel = new JPanel();
		buttonPanel.setLayout( new GridLayout( 1, 1));

		_textColorButton = new JButton(
			ResourceManager.get_instance().get( "color.panel.text.color.button"));
		_textColorButton.addActionListener( new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				on_get_color( _textColorLabel,
					ResourceManager.get_instance().get( "color.dialog.text.color.title"));
			}
		});
		buttonPanel.add( _textColorButton);

		panel.add( buttonPanel);

		panel.add( Box.createHorizontalStrut( 5));

		parent.add( panel);
	}

	/**
	 * Invoked when the button is clicked.
	 * @param label the label for the specified color
	 * @param title the title string of the color chooser dialog box
	 */
	protected void on_get_color(JLabel label, String title) {
		Color color = ColorDlg.showDialog( _owner,
			title + ( _name.equals( "") ? "" : ( " - " + _name)),
			label.getBackground(),
			_parent,
			ResourceManager.get_instance().get( "dialog.ok"),
			ResourceManager.get_instance().get( "dialog.cancel"),
			ResourceManager.get_instance().get( "make.color"));
		if ( null == color)
			return;
	
		label.setBackground( color);
	}

	/**
	 * Returns the maximum width of the labels on this component.
	 * @return the maximum width of the labels on this component
	 */
	public int get_max_width() {
		int width = 0;
		if ( null != _imageColorLabel)
			width = Math.max( width, _imageColorLabel.getPreferredSize().width);
		if ( null != _textColorLabel)
			width = Math.max( width, _textColorLabel.getPreferredSize().width);
		return width;
	}

	/**
	 * Sets the specified width of the labels on this component.
	 * @param width the specified width
	 */
	public void set_width(int width) {
		if ( null != _imageColorLabel)
			_imageColorLabel.setPreferredSize( new Dimension( width, _imageColorButton.getPreferredSize().height));
		if ( null != _textColorLabel)
			_textColorLabel.setPreferredSize( new Dimension( width, _textColorButton.getPreferredSize().height));
	}
}
