/**
 * 
 */
package soars.application.visualshell.common.image;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.image.BufferedImage;
import java.io.File;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import soars.application.visualshell.layer.LayerManager;
import soars.application.visualshell.main.ResourceManager;
import soars.common.utility.swing.thumbnail.ThumbnailItem;
import soars.common.utility.swing.tool.SwingTool;

/**
 * The common GUI panel for thumbnail selection.
 * @author kurata / SOARS project
 */
public class SelectedImagefilePanel extends JPanel implements IThumbnailListCallback {

	/**
	 * Image for the selected thumbnail.
	 */
	static private BufferedImage _bufferedImage = new BufferedImage( VisualShellImageManager._thumbnailSize, VisualShellImageManager._thumbnailSize, BufferedImage.TYPE_INT_ARGB);

	/**
	 * Dummy label for padding.
	 */
	public JLabel _dummyLabel = null;

	/**
	 * Label for the selected thumbnail.
	 */
	public JLabel _selectedImageLabel = null;

	/**
	 * Panel for the selected thumbnail.
	 */
	public JPanel _selectedImageLabelPanel = null;

	/**
	 * Size of the panel for the selected thumbnail.
	 */
	public Dimension _selectedImageLabelPanelDimension = null;

	/**
	 * Label for the size of selected image.
	 */
	public JLabel _selectedImageSizeLabel = null;

	/**
	 * Text field for the size of selected image.
	 */
	public JTextField _selectedImageSizeTextField = null;

	/**
	 * Label for the selected image filename.
	 */
	public JLabel _selectedImageFilenameLabel = null;

	/**
	 * Text field for the selected image filename.
	 */
	public JTextField _selectedImageFilenameTextField = null;

	/**
	 * Check box which displays whether the image is used.
	 */
	public JCheckBox _imagefileCheckBox = null;

	/**
	 * Frame of the parent container.
	 */
	private Frame _owner = null;

	/**
	 * Parent container of this component.
	 */
	private Component _parent = null;

	/**
	 * Creates the common GUI panel for thumbnail selection.
	 * @param owner the frame of the parent container
	 * @param parent the parent container of this component
	 */
	public SelectedImagefilePanel(Frame owner, Component parent) {
		super();
		_owner = owner;
		_parent = parent;
	}

	/**
	 * Returns true if this component is initialized successfully.
	 * @param image_filename the selected image filename
	 * @return true if this component is initialized successfully
	 */
	public boolean setup(String image_filename) {
		setLayout( new BoxLayout( this, BoxLayout.X_AXIS));

		JPanel panel = new JPanel();
		panel.setLayout( new BoxLayout( panel, BoxLayout.Y_AXIS));

		panel.setBorder( BorderFactory.createTitledBorder(
			ResourceManager.get_instance().get( "selected.imagefile.panel.border.title")));

		setup_selected_image_label( panel);

		SwingTool.insert_horizontal_glue( panel);

		setup_selected_image_size_text_field( panel);

		SwingTool.insert_horizontal_glue( panel);

		setup_selected_image_filename_text_field( panel);

		SwingTool.insert_horizontal_glue( panel);

		setup_select_imagefile_check_box( image_filename, panel);

		add( panel);

		return true;
	}

	/**
	 * Creates the label for the selected thumbnail.
	 * @param parent the parent container of the component on which the label for the selected thumbnail is displayed
	 */
	private void setup_selected_image_label(JPanel parent) {
		JPanel panel = new JPanel();
		panel.setLayout( new BoxLayout( panel, BoxLayout.X_AXIS));

		panel.add( Box.createHorizontalStrut( 5));

		_dummyLabel = new JLabel();
		panel.add( _dummyLabel);

		panel.add( Box.createHorizontalStrut( 5));

		_selectedImageLabelPanel = new JPanel();

		_selectedImageLabel = new JLabel();
		set_default_bufferedImage();
		_selectedImageLabel.setOpaque( true);
		_selectedImageLabel.setVisible( true);
		_selectedImageLabelPanel.add( _selectedImageLabel);

		panel.add( _selectedImageLabelPanel);
		_selectedImageLabelPanel.setBorder( BorderFactory.createLineBorder( Color.black, 1));

		panel.add( Box.createHorizontalStrut( 5));

		parent.add( panel);

		_selectedImageLabelPanelDimension = _selectedImageLabelPanel.getPreferredSize();
	}

	/**
	 * Sets default image into the label for the selected thumbnail.
	 */
	public void set_default_bufferedImage() {
		_selectedImageLabel.setIcon( new ImageIcon( _bufferedImage));
		_selectedImageLabel.setBorder( BorderFactory.createEmptyBorder());

		if ( null != _selectedImageSizeTextField)
			_selectedImageSizeTextField.setText( "");

		if ( null != _selectedImageFilenameTextField)
			_selectedImageFilenameTextField.setText( "");
	}

	/**
	 * Creates the text field for the size of selected image.
	 * @param parent the parent container of the component on which the text field for the size of selected image is displayed
	 */
	private void setup_selected_image_size_text_field(JPanel parent) {
		JPanel panel = new JPanel();
		panel.setLayout( new BoxLayout( panel, BoxLayout.X_AXIS));

		panel.add( Box.createHorizontalStrut( 5));

		_selectedImageSizeLabel = new JLabel(
			ResourceManager.get_instance().get( "selected.imagefile.panel.imagefile.size"));
		_selectedImageSizeLabel.setHorizontalAlignment( SwingConstants.RIGHT);
		panel.add( _selectedImageSizeLabel);

		panel.add( Box.createHorizontalStrut( 5));

		_selectedImageSizeTextField = new JTextField();
		_selectedImageSizeTextField.setEditable( false);
		_selectedImageSizeTextField.setPreferredSize( new Dimension( 200,
			_selectedImageSizeTextField.getPreferredSize().height));
		panel.add( _selectedImageSizeTextField);

		panel.add( Box.createHorizontalStrut( 5));

		parent.add( panel);
	}

	/**
	 * Creates the text field for the selected image filename.
	 * @param parent the parent container of the component on which the text field for the selected image filename is displayed
	 */
	private void setup_selected_image_filename_text_field(JPanel parent) {
		JPanel panel = new JPanel();
		panel.setLayout( new BoxLayout( panel, BoxLayout.X_AXIS));

		panel.add( Box.createHorizontalStrut( 5));

		_selectedImageFilenameLabel = new JLabel(
			ResourceManager.get_instance().get( "selected.imagefile.panel.imagefile.name"));
		_selectedImageFilenameLabel.setHorizontalAlignment( SwingConstants.RIGHT);
		panel.add( _selectedImageFilenameLabel);

		panel.add( Box.createHorizontalStrut( 5));

		_selectedImageFilenameTextField = new JTextField();
		_selectedImageFilenameTextField.setEditable( false);
		_selectedImageFilenameTextField.setPreferredSize( new Dimension( 200,
			_selectedImageFilenameTextField.getPreferredSize().height));
		panel.add( _selectedImageFilenameTextField);

		panel.add( Box.createHorizontalStrut( 5));

		parent.add( panel);
	}

	/**
	 * Creates the check box which displays whether the image is used.
	 * @param image_filename the selected image filename
	 * @param parent the parent container of the component on which the check box which displays whether the image is used is displayed
	 */
	private void setup_select_imagefile_check_box(String image_filename, JPanel parent) {
		JPanel panel = new JPanel();
		panel.setLayout( new FlowLayout( FlowLayout.LEFT, 5, 0));

		_imagefileCheckBox = new JCheckBox(
			ResourceManager.get_instance().get( "selected.imagefile.panel.imagefile.checkbox"),
			( image_filename.equals( "")) ? false : true);
		panel.add( _imagefileCheckBox);

		parent.add( panel);
	}

	/**
	 * Returns the maximum width of components.
	 * @return the maximum width of components
	 */
	public int get_max_width() {
		int width = _dummyLabel.getPreferredSize().width;
		width = Math.max( width, _selectedImageSizeLabel.getPreferredSize().width);
		width = Math.max( width, _selectedImageFilenameLabel.getPreferredSize().width);
		return width;
	}

	/**
	 * Sets the width of components.
	 * @param width the new width of components
	 */
	public void set_width(int width) {
		_dummyLabel.setPreferredSize( new Dimension( width, _dummyLabel.getPreferredSize().height));
		_selectedImageSizeLabel.setPreferredSize( new Dimension( width, _selectedImageSizeLabel.getPreferredSize().height));
		_selectedImageFilenameLabel.setPreferredSize( new Dimension( width, _selectedImageFilenameLabel.getPreferredSize().height));
	}

	/* (non-Javadoc)
	 * @see soars.application.animator.common.image.IThumbnailListCallback#selected(soars.common.utility.swing.thumbnail.ThumbnailItem)
	 */
	public void selected(ThumbnailItem thumbnailItem) {
		_selectedImageLabel.setIcon( thumbnailItem._label.getIcon());
		_selectedImageLabel.setBorder( BorderFactory.createLineBorder( Color.red, 1));

		_selectedImageLabelPanel.setPreferredSize( _selectedImageLabelPanelDimension);

		_selectedImageSizeTextField.setText(
			String.valueOf( thumbnailItem._width) + " x " + String.valueOf( thumbnailItem._height));

		_selectedImageFilenameTextField.setText( thumbnailItem._file.getName());

		_imagefileCheckBox.setSelected( true);
	}

	/* (non-Javadoc)
	 * @see soars.application.animator.common.image.IThumbnailListCallback#update()
	 */
	public void update() {
		String filename = _selectedImageFilenameTextField.getText();
		if ( null == filename || filename.equals( ""))
			return;

		File file = new File( LayerManager.get_instance().get_thumbnail_image_directory(), filename);
		if ( null != file && file.exists())
			return;

		set_default_bufferedImage();
	}

	/**
	 * Returns the selected image filename.
	 * @return the selected image filename
	 */
	public String get_selected_image_filename() {
		return ( !_imagefileCheckBox.isSelected() ? "" : _selectedImageFilenameTextField.getText());
	}
}
