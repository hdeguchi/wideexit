/*
 * 2005/03/18
 */
package soars.application.animator.object.entity.base;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;
import java.io.File;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Vector;

import org.xml.sax.SAXException;
import org.xml.sax.helpers.AttributesImpl;

import soars.application.animator.main.Administrator;
import soars.application.animator.main.internal.ObjectManager;
import soars.common.utility.tool.file.FileUtility;
import soars.common.utility.xml.sax.Writer;

/**
 * The base object class.
 * @author kurata / SOARS project
 */
public class EntityBase {

	/**
	 * Name of this object.
	 */
	public String _name = "";

	/**
	 * True if this object is visible.
	 */
	public boolean _visible = true;

	/**
	 * True if the name string is visible.
	 */
	public boolean _visibleName = true;

	/**
	 * Name of image file.
	 */
	public String _imageFilename = "";

	/**
	 * Position of this object.
	 */
	public Point _position = new Point();

	/**
	 * Size of this object.
	 */
	public Dimension _dimension = new Dimension();

	/**
	 * Size of image.
	 */
	public Dimension _imageDimension = new Dimension();

	/**
	 * Size of name string of this object.
	 */
	public Dimension _nameDimension = null;

	/**
	 * Color for this object.
	 */
	public Color _imageColor = null;

	/**
	 * Color for the name of this object.
	 */
	public Color _textColor = null; 

	/**
	 * Font object for the name string of this object.
	 */
	public Font _font = null;

	/**
	 * Property value hashtable(name[String] - value[String])
	 */
	public Map<String, String> _properties = new HashMap<String, String>();

	/**
	 * 
	 */
	public ObjectManager _objectManager = null;

	/**
	 * 
	 */
	protected Color _selectedColor = new Color( 255, 0, 0); 

	/**
	 * True if this object is selected.
	 */
	public boolean _selected = false;

	/**
	 * Creates the instance of the base object class.
	 * @param name the name of the object
	 * @param defaultImageColor the color for the object
	 * @param defaultTextColor the color for the name of the object
	 * @param objectManager
	 */
	public EntityBase(String name, Color defaultImageColor, Color defaultTextColor, ObjectManager objectManager) {
		super();
		_name = name;
		_imageColor = defaultImageColor;
		_textColor = defaultTextColor;
		_objectManager = objectManager;
	}

	/** Copy constructor(For duplication)
	 * @param spotObject
	 * @param objectManager
	 */
	public EntityBase(EntityBase entityBase, ObjectManager objectManager) {
		_name = entityBase._name;
		_visible = entityBase._visible;
		_visibleName = entityBase._visibleName;
		_imageFilename = entityBase._imageFilename;
		if ( null != entityBase._position)
			_position = new Point( entityBase._position);
		if ( null != entityBase._dimension)
			_dimension = new Dimension( entityBase._dimension);
		if ( null != entityBase._imageDimension)
			_imageDimension = new Dimension( entityBase._imageDimension);
		if ( null != entityBase._nameDimension)
			_nameDimension = new Dimension( entityBase._nameDimension);
		if ( null != entityBase._imageColor)
			_imageColor = new Color( entityBase._imageColor.getRGB());
		if ( null != entityBase._textColor)
			_textColor = new Color( entityBase._textColor.getRGB()); 
		if ( null != entityBase._font)
			_font = new Font( entityBase._font.getName(), entityBase._font.getStyle(), entityBase._font.getSize());
		_objectManager = objectManager;
		if ( null != entityBase._selectedColor)
			_selectedColor = new Color( entityBase._selectedColor.getRGB()); 
		_selected = entityBase._selected;
		Iterator iterator = entityBase._properties.entrySet().iterator();
		while ( iterator.hasNext()) {
			Object object = iterator.next();
			Map.Entry entry = ( Map.Entry)object;
			_properties.put( ( String)entry.getKey(), ( String)entry.getValue());
		}
	}

	/**
	 * Returns the name of this object.
	 * @return the name of this object
	 */
	public String get_name() {
		return _name;
	}

	/**
	 * Clears all.
	 */
	public void cleanup() {
	}

	/**
	 * Returns true for setting property successfully.
	 * @param name the name of the property
	 * @param value the value of the property
	 * @return true for setting property successfully
	 */
	public boolean set_property(String name, String value) {
		_properties.put( name, value);
		return true;
	}

	/**
	 * Returns true if this object is updated successfully.
	 * @param visible whether this object is visible
	 * @param visibleName whether the name string is visible
	 * @param imageR the red value for image color of this object
	 * @param imageG the green value for image color of this object
	 * @param imageB the blue value for image color of this object
	 * @param textR the red value for text color of this object
	 * @param textG the green value for text color of this object
	 * @param textB the blue value for text color of this object
	 * @param fontFamily the name of font for the name string
	 * @param fontStyle the style of font for the name string
	 * @param fontSize the size of font for the name string
	 * @param imageFilename the name of image file
	 * @param graphics2D the graphics object of JAVA
	 * @return true if this object is updated successfully
	 */
	public boolean update(boolean visible, boolean visibleName, int imageR, int imageG, int imageB, int textR, int textG, int textB, String fontFamily, int fontStyle, int fontSize, String imageFilename, Graphics2D graphics2D) {
		_visible = visible;

		boolean visibleNameChanged = ( _visibleName != visibleName);
		if ( visibleNameChanged)
			_visibleName = visibleName;

		boolean fontChanged = ( !_font.getFamily().equals( fontFamily)
			|| _font.getStyle() != fontStyle || _font.getSize() != fontSize);
		if ( fontChanged)
			_font = new Font( fontFamily, fontStyle, fontSize);

		boolean nameDimensionChanged = false;
		if ( visibleNameChanged) {
			if ( !_visibleName)
				_nameDimension = new Dimension( 0, 0);
			else {
				Font font = graphics2D.getFont();
				graphics2D.setFont( _font);

				FontMetrics fontMetrics = graphics2D.getFontMetrics();
				_nameDimension = new Dimension(
					fontMetrics.stringWidth( _name) + _name.length(),
					fontMetrics.getHeight());

				graphics2D.setFont( font);
			}
			nameDimensionChanged = true;
		} else {
			if ( _visibleName && fontChanged) {
				Font font = graphics2D.getFont();
				graphics2D.setFont( _font);

				FontMetrics fontMetrics = graphics2D.getFontMetrics();
				_nameDimension = new Dimension(
					fontMetrics.stringWidth( _name) + _name.length(),
					fontMetrics.getHeight());

				graphics2D.setFont( font);

				nameDimensionChanged = true;
			}
		}

		if ( _imageColor.getRed() != imageR
			|| _imageColor.getGreen() != imageG
			|| _imageColor.getBlue() != imageB)
			_imageColor = new Color( imageR, imageG, imageB);

		if ( _textColor.getRed() != textR
			|| _textColor.getGreen() != textG
			|| _textColor.getBlue() != textB)
			_textColor = new Color( textR, textG, textB);

		boolean imageChanged = false;
		if ( !_imageFilename.equals( imageFilename)) {
			_imageFilename = imageFilename;
			imageChanged = true;
		}

		return ( imageChanged || nameDimensionChanged);
	}

	/**
	 * Returns true if this object is updated successfully.
	 * @param imageR the red value for image color of this object
	 * @param imageG the green value for image color of this object
	 * @param imageB the blue value for image color of this object
	 * @param imageFilename the name of image file
	 * @param graphics2D the graphics object of JAVA
	 * @return true if this object is updated successfully
	 */
	public boolean update(int imageR, int imageG, int imageB, String imageFilename, Graphics2D graphics2D) {
		if ( _imageColor.getRed() != imageR
			|| _imageColor.getGreen() != imageG
			|| _imageColor.getBlue() != imageB)
			_imageColor = new Color( imageR, imageG, imageB);

		boolean imageChanged = false;
		if ( !_imageFilename.equals( imageFilename)) {
			_imageFilename = imageFilename;
			imageChanged = true;
		}

		return imageChanged;
	}

	/**
	 * Updates this object with the specified object.
	 * @param entityBase the specified object
	 */
	public void update(EntityBase entityBase) {
		_visible = entityBase._visible;

		_visibleName = entityBase._visibleName;

		_imageFilename = entityBase._imageFilename;

		_position = entityBase._position;
		_dimension = entityBase._dimension;

		_imageDimension = entityBase._imageDimension;

		_nameDimension = entityBase._nameDimension;

//		_bufferedImage = entityBase._bufferedImage;
		
		_imageColor = entityBase._imageColor;

		_textColor = entityBase._textColor;

		_font = entityBase._font;
	}

	/**
	 * Sets the selection of this object.
	 * @param selected whether this object is selected
	 */
	public void select(boolean selected) {
		if ( !_visible && selected)
			return;

		_selected = selected;
	}

//	/**
//	 * @param rectangle
//	 */
//	public void select(Rectangle rectangle) {
//		if ( _visible)
//			_selected = ( rectangle.intersects( new Rectangle( _position, _imageDimension))
//				|| rectangle.intersects( new Rectangle( _position.x, _position.y + _imageDimension.height, _nameDimension.width, _nameDimension.height)));
//	}

	/**
	 * Returns true if this object is selected.
	 * @return true if this object is selected
	 */
	public boolean selected() {
		return _selected;
	}

	/**
	 * @param imageFilename
	 * @return
	 */
	protected boolean update_image(String imageFilename) {
		return true;
	}

	/**
	 * Returns the tool tip text of this object.
	 * @param state the current state
	 * @return the tooo tip text of this object
	 */
	public String get_tooltip_text(String state) {
		return null;
	}

	/**
	 * @param rectangle
	 * @param graphics2D
	 * @param position
	 * @param width
	 * @param height
	 */
	protected void draw_default_image(Rectangle rectangle, Graphics2D graphics2D, Point position, int width, int height) {
		if ( !rectangle.intersects( new Rectangle( position.x, position.y, width, height)))
			return;

		draw_default_image( graphics2D, position.x, position.y, width, height);
	}

	/**
	 * @param graphics2D
	 * @param x
	 * @param y
	 * @param width
	 * @param height
	 */
	protected void draw_default_image(Graphics2D graphics2D, int x, int y, int width, int height) {
	}

	/**
	 * @param rectangle
	 * @param graphics2D
	 * @param bufferedImage
	 * @param position
	 * @param imageObserver
	 */
	protected void draw_image(Rectangle rectangle, Graphics2D graphics2D, BufferedImage bufferedImage, Point position, ImageObserver imageObserver) {
		if ( !rectangle.intersects( new Rectangle( position.x, position.y, bufferedImage.getWidth(), bufferedImage.getHeight())))
			return;

		graphics2D.drawImage( bufferedImage, position.x, position.y, imageObserver);
	}

	/**
	 * @param rectangle
	 * @param graphics2D
	 * @param bufferedImage
	 * @param position
	 * @param dimension
	 * @param imageObserver
	 */
	protected void draw_image(Rectangle rectangle, Graphics2D graphics2D, BufferedImage bufferedImage, Point position, Dimension dimension, ImageObserver imageObserver) {
		if ( !rectangle.intersects( new Rectangle( position.x, position.y, dimension.width/*bufferedImage.getWidth()*/, dimension.height/*bufferedImage.getHeight()*/)))
			return;

		graphics2D.drawImage( bufferedImage, position.x, position.y, dimension.width, dimension.height, imageObserver);
	}

	/**
	 * @param rectangle
	 * @param graphics2D
	 * @param positionX
	 * @param positionY
	 * @param x
	 * @param y
	 */
	protected void draw_name(Rectangle rectangle, Graphics2D graphics2D, int positionX, int positionY, int x, int y) {
		if ( !rectangle.intersects( new Rectangle( positionX, positionY, _nameDimension.width, _nameDimension.height)))
			return;

		draw_name( graphics2D, x, y);
	}

	/**
	 * @param graphics2D
	 * @param x
	 * @param y
	 */
	protected void draw_name(Graphics2D graphics2D, int x, int y) {
		graphics2D.setColor( _textColor);
		Font font = graphics2D.getFont();
		graphics2D.setFont( _font);
		graphics2D.drawString( _name, x, y);
		graphics2D.setFont( font);
	}

	/**
	 * @param selectedProperties
	 * @return
	 */
	protected String get_tooltip_text(Vector<String> selectedProperties) {
		if ( selectedProperties.isEmpty())
			return "";

		String result = "";
		for ( String name:selectedProperties) {
			String value = _properties.get( name);
			if ( null == value)
				value = "";

			if ( !result.equals( ""))
				result += ", ";

			result += ( name + " = \"" + value + "\"");
		}

		if ( result.equals( ""))
			return "";

		return ( _name + " : " + result);
	}

	/**
	 * Returns true for writing this object data successfully.
	 * @param attributesImpl the default implementation of the SAX2 Attributes interface
	 * @return true for writing this object data successfully
	 * @throws SAXException encapsulate a general SAX error or warning
	 */
	public boolean write(AttributesImpl attributesImpl) throws SAXException {
		attributesImpl.addAttribute( null, null, "name", "", Writer.escapeAttributeCharData( _name));

		attributesImpl.addAttribute( null, null, "visible", "", _visible ? "true" : "false");

		attributesImpl.addAttribute( null, null, "visible_name", "", _visibleName ? "true" : "false");

		attributesImpl.addAttribute( null, null, "image_r", "", String.valueOf( _imageColor.getRed()));
		attributesImpl.addAttribute( null, null, "image_g", "", String.valueOf( _imageColor.getGreen()));
		attributesImpl.addAttribute( null, null, "image_b", "", String.valueOf( _imageColor.getBlue()));

		attributesImpl.addAttribute( null, null, "text_r", "", String.valueOf( _textColor.getRed()));
		attributesImpl.addAttribute( null, null, "text_g", "", String.valueOf( _textColor.getGreen()));
		attributesImpl.addAttribute( null, null, "text_b", "", String.valueOf( _textColor.getBlue()));

		attributesImpl.addAttribute( null, null, "font_name", "", _font.getFamily());
		attributesImpl.addAttribute( null, null, "font_style", "", String.valueOf( _font.getStyle()));
		attributesImpl.addAttribute( null, null, "font_size", "", String.valueOf( _font.getSize()));

		String imageFilename = _imageFilename.replaceAll( "\\\\", "/");
		attributesImpl.addAttribute( null, null, "image", "", ( null == imageFilename) ? "" : imageFilename);

		return true;
	}

	/**
	 * Returns true for writing this object graphic data successfully.
	 * @param rootDirectory the root directory for Animator data
	 * @param attributesImpl the default implementation of the SAX2 Attributes interface
	 * @return true for writing this object graphic data successfully
	 */
	public boolean write_graphic_data(File rootDirectory, AttributesImpl attributesImpl) {
		attributesImpl.addAttribute( null, null, "name", "", Writer.escapeAttributeCharData( _name));

		attributesImpl.addAttribute( null, null, "visible", "", _visible ? "true" : "false");

		attributesImpl.addAttribute( null, null, "visible_name", "", _visibleName ? "true" : "false");

		attributesImpl.addAttribute( null, null, "image_r", "", String.valueOf( _imageColor.getRed()));
		attributesImpl.addAttribute( null, null, "image_g", "", String.valueOf( _imageColor.getGreen()));
		attributesImpl.addAttribute( null, null, "image_b", "", String.valueOf( _imageColor.getBlue()));

		attributesImpl.addAttribute( null, null, "text_r", "", String.valueOf( _textColor.getRed()));
		attributesImpl.addAttribute( null, null, "text_g", "", String.valueOf( _textColor.getGreen()));
		attributesImpl.addAttribute( null, null, "text_b", "", String.valueOf( _textColor.getBlue()));

		attributesImpl.addAttribute( null, null, "font_name", "", _font.getFamily());
		attributesImpl.addAttribute( null, null, "font_style", "", String.valueOf( _font.getStyle()));
		attributesImpl.addAttribute( null, null, "font_size", "", String.valueOf( _font.getSize()));

		String imageFilename = _imageFilename.replaceAll( "\\\\", "/");
		attributesImpl.addAttribute( null, null, "image", "", ( null == imageFilename) ? "" : imageFilename);

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
