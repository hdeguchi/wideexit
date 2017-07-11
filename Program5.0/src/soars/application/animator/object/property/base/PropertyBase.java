/*
 * 2005/03/03
 */
package soars.application.animator.object.property.base;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;
import java.io.File;

import org.xml.sax.SAXException;
import org.xml.sax.helpers.AttributesImpl;

import soars.application.animator.common.image.AnimatorImageManager;
import soars.application.animator.main.Administrator;
import soars.common.utility.tool.file.FileUtility;
import soars.common.utility.xml.sax.Writer;

/**
 * The base property class.
 * @author kurata / SOARS project
 */
public class PropertyBase {

	/**
	 * Value of this object.
	 */
	public String _value = "";

	/**
	 * Whether this object is visible.
	 */
	public boolean _visible = true;

	/**
	 *  Red value for image color of this object.
	 */
	public int _imageR = 0;

	/**
	 *  Green value for image color of this object.
	 */
	public int _imageG = 0;

	/**
	 *  Blue value for image color of this object.
	 */
	public int _imageB = 0;

	/**
	 *  Red value for text color of this object.
	 */
	public int _textR = 0;

	/**
	 *  Green value for text color of this object.
	 */
	public int _textG = 0;

	/**
	 *  Blue value for text color of this object.
	 */
	public int _textB = 0;

	/**
	 * Name of image file.
	 */
	public String _imageFilename = "";

	/**
	 * Font object for the value string of this object.
	 */
	public Font _font = null;

	/**
	 * Size of value string of this object.
	 */
	public Dimension _textDimension = null;

	/**
	 * Creates the instance of base property class.
	 */
	public PropertyBase() {
		super();
	}

	/**
	 * Creates the instance of base property class with the specified base property.
	 * @param propertyBase the specified base property
	 */
	public PropertyBase(PropertyBase propertyBase) {
		super();
		_value = propertyBase._value;

		_visible = propertyBase._visible;

		_imageR = propertyBase._imageR;
		_imageG = propertyBase._imageG;
		_imageB = propertyBase._imageB;

		_textR = propertyBase._textR;
		_textG = propertyBase._textG;
		_textB = propertyBase._textB;

		_font = new Font( propertyBase._font.getFamily(),
			propertyBase._font.getStyle(),
			propertyBase._font.getSize());

		_imageFilename = propertyBase._imageFilename;

		if ( null != propertyBase._textDimension)
			_textDimension = new Dimension( propertyBase._textDimension);
	}

	/**
	 * Clears all.
	 */
	public void cleanup() {
	}

	/**
	 * Returns true if the specified image is set to this property successfully.
	 * @param filename the specified image file name
	 * @return true if the specified image is set to this property successfully
	 */
	public boolean load_image(String filename) {
		if ( null == filename || filename.equals( "")) {
			_imageFilename = "";
			return false;
		}

		if ( null == AnimatorImageManager.get_instance().load_image( filename))
			return false;

		_imageFilename = filename;
		return true;
	}

	/** 
	 * Initialized this property.
	 * @param value the value of property
	 * @param graphics2D the graphics object of JAVA
	 */
	public void setup(String value, Graphics2D graphics2D) {
		_value = value;

		Font font = graphics2D.getFont();
		graphics2D.setFont( _font);
		int textWidth = graphics2D.getFontMetrics().stringWidth( _value) + _value.length();
		int textHeight = graphics2D.getFontMetrics().getHeight();
		graphics2D.setFont( font);

		_textDimension = new Dimension( ( 0 == textWidth) ? 1 : textWidth, textHeight);
	}

//	/** 
//	 * @param value
//	 * @param width
//	 * @param height
//	 * @param graphics2D
//	 */
//	public void setup(String value, int width, int height, Graphics2D graphics2D) {
//		_value = value;
//
////		_default_image = new BufferedImage( width, height, BufferedImage.TYPE_INT_ARGB);
//
//		Font font = graphics2D.getFont();
//		graphics2D.setFont( _font);
//		int textWidth = graphics2D.getFontMetrics().stringWidth( _value) + _value.length();
//		int textHeight = graphics2D.getFontMetrics().getHeight();
//		graphics2D.setFont( font);
//
//		_text_dimension = new Dimension( ( 0 == textWidth) ? 1 : textWidth, textHeight);
////		_text_image = new BufferedImage( ( 0 == textWidth) ? 1 : textWidth,
////			textHeight, BufferedImage.TYPE_INT_ARGB);
//	}

	/** 
	 * Initialized this object with the specified values.
	 * @param color the specified image color
	 * @param value the specified value
	 * @param graphics2D the graphics object of JAVA
	 */
	public void setup(int[] color, String value, Graphics2D graphics2D) {
		_value = value;

		_imageR = color[ 0];
		_imageG = color[ 1];
		_imageB = color[ 2];

		Font font = graphics2D.getFont();
		_font = new Font( font.getFamily(), font.getStyle(), font.getSize());
		int textWidth = graphics2D.getFontMetrics().stringWidth( _value) + _value.length();
		int textHeight = graphics2D.getFontMetrics().getHeight();

		_textDimension = new Dimension( ( 0 == textWidth) ? 1 : textWidth, textHeight);
	}

//	/** From AgentPropertyImporter or SpotPropertyImporter
//	 * @param color
//	 * @param value
//	 * @param width
//	 * @param height
//	 * @param graphics2D
//	 */
//	public void setup(int[] color, String value, int width, int height, Graphics2D graphics2D) {
//		_value = value;
//
//		_image_r = color[ 0];
//		_image_g = color[ 1];
//		_image_b = color[ 2];
//
////		_default_image = new BufferedImage( width, height, BufferedImage.TYPE_INT_ARGB);
//
//		Font font = graphics2D.getFont();
//		_font = new Font( font.getFamily(), font.getStyle(), font.getSize());
//		int textWidth = graphics2D.getFontMetrics().stringWidth( _value) + _value.length();
//		int textHeight = graphics2D.getFontMetrics().getHeight();
//
//		_text_dimension = new Dimension( ( 0 == textWidth) ? 1 : textWidth, textHeight);
////		_text_image = new BufferedImage( ( 0 == textWidth) ? 1 : textWidth,
////			textHeight, BufferedImage.TYPE_INT_ARGB);
//	}

	/**
	 * Updates this object with the specified values.
	 * @param visible whether this object is visible
	 * @param imageR the red value for image color of this object
	 * @param imageG the green value for image color
	 * @param imageB the blue value for image color
	 * @param textR the red value for text color
	 * @param textG the green value for text color
	 * @param textB the blue value for text color
	 * @param fontFamily the the font name
	 * @param fontStyle the font style
	 * @param fontSize the font size
	 * @param imageFilename the name of image file
	 * @param graphics2D the graphics object of JAVA
	 */
	public void update(boolean visible, int imageR, int imageG, int imageB, int textR, int textG, int textB, String fontFamily, int fontStyle, int fontSize, String imageFilename, Graphics2D graphics2D) {
		_imageR = imageR;
		_imageG = imageG;
		_imageB = imageB;


		_textR = textR;
		_textG = textG;
		_textB = textB;


		if ( ( _visible != visible)
			|| !_font.getFamily().equals( fontFamily)
			|| _font.getStyle() != fontStyle
			|| _font.getSize() != fontSize) {
			_font = new Font( fontFamily, fontStyle, fontSize);

			Font font = graphics2D.getFont();
			graphics2D.setFont( _font);
			int textWidth = graphics2D.getFontMetrics().stringWidth( visible ? _value : "") + _value.length();
			int textHeight = graphics2D.getFontMetrics().getHeight();
			graphics2D.setFont( font);

			_textDimension = new Dimension( ( 0 == textWidth) ? 1 : textWidth, textHeight);
//			_text_image = new BufferedImage( ( 0 == textWidth) ? 1 : textWidth,
//				textHeight, BufferedImage.TYPE_INT_ARGB);
		}


		if ( !_imageFilename.equals( imageFilename))
			_imageFilename = imageFilename;

		_visible = visible;
	}

//	/**
//	 * @param visible
//	 * @param imageR
//	 * @param imageG
//	 * @param imageB
//	 * @param textR
//	 * @param textG
//	 * @param textB
//	 * @param fontFamily
//	 * @param fontStyle
//	 * @param fontSize
//	 * @param imageFilename
//	 * @param width
//	 * @param height
//	 * @param graphics2D
//	 */
//	public void update(boolean visible, int imageR, int imageG, int imageB, int textR, int textG, int textB, String fontFamily, int fontStyle, int fontSize, String imageFilename, int width, int height, Graphics2D graphics2D) {
//		_imageR = imageR;
//		_imageG = imageG;
//		_imageB = imageB;
//
//
//		_textR = textR;
//		_textG = textG;
//		_textB = textB;
//
//
//		if ( ( _visible != visible)
//			|| !_font.getFamily().equals( fontFamily)
//			|| _font.getStyle() != fontStyle
//			|| _font.getSize() != fontSize) {
//			_font = new Font( fontFamily, fontStyle, fontSize);
//
//			Font font = graphics2D.getFont();
//			graphics2D.setFont( _font);
//			int textWidth = graphics2D.getFontMetrics().stringWidth( visible ? _value : "") + _value.length();
//			int textHeight = graphics2D.getFontMetrics().getHeight();
//			graphics2D.setFont( font);
//
//			_textDimension = new Dimension( ( 0 == textWidth) ? 1 : textWidth, textHeight);
////			_textImage = new BufferedImage( ( 0 == textWidth) ? 1 : textWidth,
////				textHeight, BufferedImage.TYPE_INT_ARGB);
//		}
//
//
//		if ( !_imageFilename.equals( imageFilename))
//			_imageFilename = imageFilename;
//
//		_visible = visible;
//	}

	/**
	 * Updates this object with the specified object.
	 * @param propertyBase the specified object
	 * @param graphics2D the graphics object of JAVA
	 */
	public void update(PropertyBase propertyBase, Graphics2D graphics2D) {
	}

	/**
	 * Updates this object with the specified object.
	 * @param propertyBase the specified object
	 * @param width the default width
	 * @param height the default height
	 * @param graphics2D the graphics object of JAVA
	 */
	public void update(PropertyBase propertyBase, int width, int height, Graphics2D graphics2D) {
		_value = propertyBase._value;


		_imageR = propertyBase._imageR;
		_imageG = propertyBase._imageG;
		_imageB = propertyBase._imageB;


		_textR = propertyBase._textR;
		_textG = propertyBase._textG;
		_textB = propertyBase._textB;


		if ( ( _visible != propertyBase._visible)
			|| !_font.getFamily().equals( propertyBase._font.getFamily())
			|| _font.getStyle() != propertyBase._font.getStyle()
			|| _font.getSize() != propertyBase._font.getSize()) {
			_font = new Font( propertyBase._font.getFamily(),
				propertyBase._font.getStyle(), propertyBase._font.getSize());

			Font font = graphics2D.getFont();
			graphics2D.setFont( _font);
			int textWidth = graphics2D.getFontMetrics().stringWidth( propertyBase._visible ? _value : "") + _value.length();
			int textHeight = graphics2D.getFontMetrics().getHeight();
			graphics2D.setFont( font);

			_textDimension = new Dimension( ( 0 == textWidth) ? 1 : textWidth, textHeight);
//			_text_image = new BufferedImage( ( 0 == textWidth) ? 1 : textWidth,
//				textHeight, BufferedImage.TYPE_INT_ARGB);
		}


		if ( !_imageFilename.equals( propertyBase._imageFilename)) {
			_imageFilename = propertyBase._imageFilename;
		}


		_visible = propertyBase._visible;
	}

//	/**
//	 * 
//	 */
//	public void draw() {
//		Graphics2D graphics2D = ( Graphics2D)_textImage.getGraphics();
//		Font font = graphics2D.getFont();
//		graphics2D.setFont( _font);
//		graphics2D.setColor( new Color( _text_r, _text_g, _text_b));
//		graphics2D.drawString( ( _visible ? _value : ""), 0, _textImage.getHeight() - 2);
//		//graphics2D.drawString( ( _visible ? _value : ""), 0, _textImage.getHeight() - 4);
//		graphics2D.setFont( font);
//		graphics2D.dispose();
//	}

	/**
	 * Returns the maximum width.
	 * @return the maximum width
	 */
	public int get_width(/*boolean selected*/) {
		BufferedImage image = AnimatorImageManager.get_instance().load_image( _imageFilename);
		if ( null != image)
			return Math.max( image.getWidth(), _textDimension.width);
//			return Math.max( image.getWidth(), _textImage.getWidth());

		return _textDimension.width;

//		return selected ? get_width() : _textDimension.width;

//			? Math.max( _default_image.getWidth(), _textImage.getWidth())
//			: _textImage.getWidth();
	}

	/**
	 * Returns the maximum height.
	 * @return the maximum height
	 */
	public int get_image_height(/*boolean selected*/) {
		BufferedImage image = AnimatorImageManager.get_instance().load_image( _imageFilename);
		if ( null != image)
			return image.getHeight();

		return 0;

//		return selected ? get_height() : 0;

//		return selected ? _defaultImage.getHeight() : 0;
	}

	/**
	 * Returns the height of text string.
	 * @return the height of text string
	 */
	public int get_text_height() {
		return _textDimension.height;
//		return _textImage.getHeight();
	}

//	/**
//	 * @param width
//	 * @param height
//	 */
//	public void update_default_image(int width, int height) {
//		if ( width == _defaultImage.getWidth()
//			&& height == _defaultImage.getHeight())
//			return;
//
//		_defaultImage = new BufferedImage( width, height, BufferedImage.TYPE_INT_ARGB);
//		draw_default_image( width, height);		
//	}

	/**
	 * Returns true for drawing the default image successfully.
	 * @param rectangle the visible rectangle
	 * @param graphics2D the graphics object of JAVA
	 * @param position the position of the image
	 * @param width the default width of the image
	 * @param height the default height of the image
	 * @return true for drawing the default image successfully
	 */
	public boolean draw_default_image(Rectangle rectangle, Graphics2D graphics2D, Point position, int width, int height) {
		if ( !rectangle.intersects( new Rectangle( position.x, position.y, width, height)))
			return false;

		draw_default_image( graphics2D, position, width, height);
		return true;
	}

	/**
	 * @param graphics2D
	 * @param position
	 * @param width
	 * @param height
	 */
	protected void draw_default_image(Graphics2D graphics2D, Point position, int width, int height) {
	}

//	/**
//	 * @param width
//	 * @param height
//	 */
//	protected void draw_default_image(int width, int height) {
//	}

	/**
	 * Returns true for drawing the image successfully.
	 * @param rectangle the visible rectangle
	 * @param graphics2D the graphics object of JAVA
	 * @param position the position of the image
	 * @param imageObserver an asynchronous update interface for receiving notifications about Image information as the Image is constructed
	 * @return true for drawing the image successfully
	 */
	public boolean draw_image(Rectangle rectangle, Graphics2D graphics2D, Point position, ImageObserver imageObserver) {
		BufferedImage bufferedImage = AnimatorImageManager.get_instance().load_image( _imageFilename);
		if ( null == bufferedImage)
			return false;

		if ( !rectangle.intersects( new Rectangle( position.x, position.y, bufferedImage.getWidth(), bufferedImage.getHeight())))
			return false;

		graphics2D.drawImage( bufferedImage, position.x, position.y, imageObserver);
		return true;
	}

	/**
	 * Returns true for drawing the text successfully.
	 * @param rectangle the visible rectangle
	 * @param graphics2D the graphics object of JAVA
	 * @param x the x-coordinates of the text
	 * @param y the y-coordinates of the text
	 * @param imageObserver an asynchronous update interface for receiving notifications about Image information as the Image is constructed
	 * @return true for drawing the text successfully
	 */
	public boolean draw_text(Rectangle rectangle, Graphics2D graphics2D, int x, int y, ImageObserver imageObserver) {
		if ( !_visible || _value.equals( ""))
			return false;

		if ( !rectangle.intersects( new Rectangle( x, y, _textDimension.width, _textDimension.height)))
			return false;

		Font font = graphics2D.getFont();
		graphics2D.setFont( _font);
		graphics2D.setColor( new Color( _textR, _textG, _textB));
		graphics2D.drawString( _value, x, y + _textDimension.height - 2);
		//graphics2D.drawString( ( _visible ? _value : ""), x, y + _textDimension.height - 2);
		//graphics2D.drawString( ( _visible ? _value : ""), x, y + _textDimension.height - 4);
		graphics2D.setFont( font);
		return true;
	}

	/**
	 * Returns true if the specified image file is in use.
	 * @param filename the name of the specified image file
	 * @return true if the specified image file is in use
	 */
	public boolean uses_this_image(String filename) {
		return filename.equals( _imageFilename);
	}

	/**
	 * Sets the specified new image if this object uses the specified image.
	 * @param original_filename the specified image file name
	 * @param new_filename the specified new image file name
	 */
	public void update_image(String original_filename, String new_filename) {
		if ( original_filename.equals( _imageFilename))
			_imageFilename = new_filename;
	}

	/**
	 * Returns true for writing this object data successfully.
	 * @param rootDirectory the root directory for Animator data
	 * @param value the value of this object
	 * @param writer the abstract class for writing to character streams
	 * @return true for writing this object data successfully
	 * @throws SAXException encapsulate a general SAX error or warning
	 */
	public boolean write(File rootDirectory, String value, Writer writer) throws SAXException {
		AttributesImpl attributesImpl = new AttributesImpl();

		attributesImpl.addAttribute( null, null, "value", "", Writer.escapeAttributeCharData( value));

		attributesImpl.addAttribute( null, null, "visible", "", _visible ? "true" : "false");

		attributesImpl.addAttribute( null, null, "image_r", "", String.valueOf( _imageR));
		attributesImpl.addAttribute( null, null, "image_g", "", String.valueOf( _imageG));
		attributesImpl.addAttribute( null, null, "image_b", "", String.valueOf( _imageB));

		attributesImpl.addAttribute( null, null, "text_r", "", String.valueOf( _textR));
		attributesImpl.addAttribute( null, null, "text_g", "", String.valueOf( _textG));
		attributesImpl.addAttribute( null, null, "text_b", "", String.valueOf( _textB));

		attributesImpl.addAttribute( null, null, "font_name", "", _font.getFamily());
		attributesImpl.addAttribute( null, null, "font_style", "", String.valueOf( _font.getStyle()));
		attributesImpl.addAttribute( null, null, "font_size", "", String.valueOf( _font.getSize()));

		String image_filename = _imageFilename.replaceAll( "\\\\", "/");
		attributesImpl.addAttribute( null, null, "image", "", ( null == image_filename) ? "" : image_filename);

		writer.writeElement( null, null, "data", attributesImpl);

		if ( null != rootDirectory && !_imageFilename.equals( "")) {
			File dest = new File( Administrator.get_instance().get_image_directory( rootDirectory), _imageFilename);
			if ( !dest.exists()) {
				if ( !FileUtility.copy( new File( Administrator.get_instance().get_image_directory(), _imageFilename), dest))
					return false;
				if ( !FileUtility.copy( new File( Administrator.get_instance().get_thumbnail_image_directory(), _imageFilename),
					new File( Administrator.get_instance().get_thumbnail_image_directory( rootDirectory), _imageFilename)))
					return false;
			}
		}

		return true;
	}
}
