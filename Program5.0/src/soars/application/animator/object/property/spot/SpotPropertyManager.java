/*
 * 2005/02/25
 */
package soars.application.animator.object.property.spot;

import java.awt.Frame;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.image.ImageObserver;
import java.io.File;
import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;

import org.xml.sax.SAXException;

import soars.application.animator.main.Environment;
import soars.application.animator.main.ResourceManager;
import soars.application.animator.object.property.base.PropertyBase;
import soars.application.animator.object.property.base.PropertyManager;
import soars.application.animator.object.transition.base.TransitionBase;
import soars.common.utility.xml.sax.Writer;

/**
 * The SpotProperty hashtable(name(String) - value(String) - SpotProperty).
 * @author kurata / SOARS project
 */
public class SpotPropertyManager extends PropertyManager {

	/**
	 * Creates the instance of the SpotProperty hasshtable.
	 */
	public SpotPropertyManager() {
		super();
	}

	/** Copy constructor(For duplication)
	 * @param spotPropertyManager
	 */
	public SpotPropertyManager(SpotPropertyManager spotPropertyManager) {
		super(spotPropertyManager);
	}

	/* (non-Javadoc)
	 * @see soars.application.animator.object.property.base.PropertyManager#create(soars.application.animator.object.property.base.PropertyBase)
	 */
	public PropertyBase create(PropertyBase propertyBase) {
		return new SpotProperty( ( SpotProperty)propertyBase);
	}

	/* (non-Javadoc)
	 * @see soars.application.animator.object.property.base.PropertyManager#append(java.util.TreeMap, java.lang.String, java.awt.Graphics2D)
	 */
	protected void append(TreeMap<String, PropertyBase> properties, String value, Graphics2D graphics2D) {
		SpotProperty spotProperty = new SpotProperty();
		properties.put( value, spotProperty);
		spotProperty.setup(
			new int[] {
				0,
				( int)( 255 * Math.random()),
				200 + ( int)( 55 * Math.random())},
			value, graphics2D);
	}

	/* (Non Javadoc)
	 * @see soars.application.animator.object.property.base.PropertyManager#setup_default_property_image(java.util.TreeMap, java.awt.Graphics2D)
	 */
	protected void setup_default_property_image(TreeMap<String, PropertyBase> properties, Graphics2D graphics2D) {
		Iterator iterator = properties.entrySet().iterator();
		while ( iterator.hasNext()) {
			Object object = iterator.next();
			Map.Entry entry = ( Map.Entry)object;
			String value = ( String)entry.getKey();
			PropertyBase propertyBase = ( PropertyBase)entry.getValue();
			propertyBase.setup(
				new int[] {
					0,
					( int)( 255 * Math.random()),
					200 + ( int)( 55 * Math.random())},
				value, graphics2D);
		}
	}

	/**
	 * Returns true for drawing the image which corresponds to the specified property value successfully.
	 * @param graphics2D the graphics object of JAVA
	 * @param position the position of the image
	 * @param deltaY the y-coordinates of the text
	 * @param width the default width of the image
	 * @param height the default height of the image
	 * @param properties the property value hashtable(name(String) - value(String))
	 * @param rectangle the visible rectangle
	 * @param imageObserver an asynchronous update interface for receiving notifications about Image information as the Image is constructed
	 * @param imageExists true if the object has the image
	 * @return true for drawing the image which corresponds to the specified property value successfully
	 */
	public boolean draw_current_property_image(Graphics2D graphics2D, Point position, int deltaY, int width, int height, Map<String, String> properties, Rectangle rectangle, ImageObserver imageObserver, boolean imageExists) {
		boolean result = super.draw_current_property_image( graphics2D, position, width, height, properties, rectangle, imageObserver, imageExists);

		int y = ( position.y + deltaY);
		for ( int i = _selectedProperties.size() - 1; i >= 0; --i) {
			String value = properties.get( _selectedProperties.get( i));
			if ( null == value)
				value = "";

			SpotProperty spotProperty = ( SpotProperty)get( _selectedProperties.get( i), value);
			if ( null == spotProperty)
				continue;

			if ( spotProperty.draw_text( rectangle, graphics2D, position.x, y - spotProperty._textDimension.height, imageObserver))
				result = true;

			y -= spotProperty._textDimension.height;
//			graphics2D.drawImage( spotProperty._text_image, position.x,
//				y - spotProperty._text_image.getHeight(), imageObserver);
//			y -= spotProperty._text_image.getHeight();
		}

		return result;
	}

	/**
	 * Returns true for drawing the image which corresponds to the specified property value successfully.
	 * @param graphics2D the graphics object of JAVA
	 * @param position the position of the image
	 * @param deltaY the y-coordinates of the text
	 * @param width the default width of the image
	 * @param height the default height of the image
	 * @param transitionBase the scenario data
	 * @param rectangle the visible rectangle
	 * @param imageObserver an asynchronous update interface for receiving notifications about Image information as the Image is constructed
	 * @param imageExists true if the object has the image
	 * @return true for drawing the image which corresponds to the specified property value successfully
	 */
	public boolean draw_current_property_image(Graphics2D graphics2D, Point position, int deltaY, int width, int height, TransitionBase transitionBase, Rectangle rectangle, ImageObserver imageObserver, boolean imageExists) {
		boolean result = super.draw_current_property_image( graphics2D, position, width, height, transitionBase, rectangle, imageObserver, imageExists);

		int y = ( position.y + deltaY);
		for ( int i = _selectedProperties.size() - 1; i >= 0; --i) {
			String value = ( null == transitionBase) 	? "" : transitionBase._properties.get( _selectedProperties.get( i));
			if ( null == value)
				value = "";

			SpotProperty spotProperty = ( SpotProperty)get( _selectedProperties.get( i), value);
			if ( null == spotProperty)
				continue;

			if ( spotProperty.draw_text( rectangle, graphics2D, position.x, y - spotProperty._textDimension.height, imageObserver))
				result = true;

			y -= spotProperty._textDimension.height;
//			graphics2D.drawImage( spotProperty._text_image, position.x,
//				y - spotProperty._text_image.getHeight(), imageObserver);
//			y -= spotProperty._text_image.getHeight();
		}

		return result;
	}

	/* (non-Javadoc)
	 * @see soars.application.animator.object.property.base.PropertyManager#edit(java.lang.String, java.awt.Frame)
	 */
	public boolean edit(String titlePrefix, Frame frame) {
		return edit( titlePrefix + ResourceManager.get_instance().get( "edit.spot.properties.dialog.title"),
			Environment._openSpotImageDirectoryKey,
			frame);
	}

	/**
	 * Returns true for writing all spot property data successfully.
	 * @param writer the abstract class for writing to character streams
	 * @return true for writing all spot property data successfully
	 * @throws SAXException encapsulate a general SAX error or warning
	 */
	public boolean write(Writer writer) throws SAXException {
		return write( writer, "spot");
	}

	/**
	 * Returns true for writing all spot property graphic data successfully.
	 * @param rootDirectory the root directory for Animator data
	 * @param writer the abstract class for writing to character streams
	 * @return true for writing all spot property graphic data successfully
	 * @throws SAXException encapsulate a general SAX error or warning
	 */
	public boolean write_graphic_data(File rootDirectory, Writer writer) throws SAXException {
		return write_graphic_data( rootDirectory, writer, "spot");
	}
}
