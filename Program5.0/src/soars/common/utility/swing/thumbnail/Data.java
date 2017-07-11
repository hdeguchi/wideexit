/**
 * 
 */
package soars.common.utility.swing.thumbnail;

import java.io.File;

import javax.swing.ImageIcon;

import soars.common.utility.swing.image.ImageProperty;

/**
 * The thumbnail data class.
 * @author kurata / SOARS project
 */
public class Data {

	/**
	 * Thumbnail image.
	 */
	public ImageIcon _imageIcon = null;

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
	 * Creates the thumbnail data.
	 * @param imageIcon the thumbnail image
	 * @param width the width of the image
	 * @param height the height of the image
	 * @param file the image file
	 */
	public Data(ImageIcon imageIcon, int width, int height, File file) {
		super();
		_imageIcon = imageIcon;
		_width = width;
		_height = height;
		_file = file;
	}

	/**
	 * Creates the thumbnail data.
	 * @param imageIcon
	 * @param imageProperty
	 * @param file
	 */
	public Data(ImageIcon imageIcon, ImageProperty imageProperty, File file) {
		super();
		_imageIcon = imageIcon;
		_file = file;
		if ( null != imageProperty) {
			_width = imageProperty._width;
			_height = imageProperty._height;
		}
	}
}
