/*
 * 2005/03/03
 */
package soars.application.animator.object.property.base;

import java.awt.Dimension;
import java.awt.Frame;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.image.ImageObserver;
import java.io.File;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;
import java.util.Vector;

import org.xml.sax.SAXException;
import org.xml.sax.helpers.AttributesImpl;

import soars.application.animator.object.property.base.edit.EditPropertiesDlg;
import soars.application.animator.object.transition.base.TransitionBase;
import soars.common.utility.xml.sax.Writer;

/**
 * The Property hashtable(name(String) - value(String) - PropertyBase).
 * @author kurata / SOARS project
 */
public class PropertyManager extends HashMap<String, TreeMap<String, PropertyBase>> {

	/**
	 * 
	 */
	protected Vector<String> _order = new Vector<String>();

	/**
	 * 
	 */
	protected Vector<String> _selectedProperties = new Vector<String>();

	/**
	 * Creates the instance of the Property hasshtable.
	 */
	public PropertyManager() {
		super();
	}

	/** Copy constructor(For duplication)
	 * Creates the instance of the Property hasshtable with the specified Property hasshtable.
	 * @param propertyManager the specified Property hasshtable
	 */
	public PropertyManager(PropertyManager propertyManager) {
		super();
		for ( String name:propertyManager._order) {
			_order.add( name);
			TreeMap<String, PropertyBase> src = propertyManager.get( name);
			TreeMap<String, PropertyBase> dest = new TreeMap<String, PropertyBase>();
			put( name, dest);
			Iterator iterator = src.entrySet().iterator();
			while ( iterator.hasNext()) {
				Object object = iterator.next();
				Map.Entry entry = ( Map.Entry)object;
				String value = ( String)entry.getKey();
				PropertyBase propertyBase = create( ( PropertyBase)entry.getValue());
				dest.put( value, propertyBase);
			}
		}

		_selectedProperties.addAll( propertyManager._selectedProperties);
	}

	/**
	 * @param propertyBase
	 * @return
	 */
	public PropertyBase create(PropertyBase propertyBase) {
		return null;
	}

	/**
	 * Clears all.
	 */
	public void cleanup() {
		for ( String name:_order) {
			TreeMap<String, PropertyBase> properties = get( name);
			Iterator iterator = properties.entrySet().iterator();
			while ( iterator.hasNext()) {
				Object object = iterator.next();
				Map.Entry entry = ( Map.Entry)object;
				PropertyBase propertyBase = ( PropertyBase)entry.getValue();
				propertyBase.cleanup();
			}
			properties.clear();
		}

		clear();

		_order.clear();

		_selectedProperties.clear();
	}

	/**
	 * Returns the array of the Properties.
	 * @return the array of the Properties
	 */
	public Vector<String> get_order() {
		return _order;
	}

	/**
	 * Returns the array of the visible Properties.
	 * @return the array of the visible Properties
	 */
	public Vector<String> get_selected_properties() {
		return _selectedProperties;
	}

	/**
	 * Appends the specified visible Properties.
	 * @param selected_properties the specified visible Properties
	 */
	public void set_selected_properties(Vector<String> selected_properties) {
		_selectedProperties.addAll( selected_properties);
	}

	/**
	 * Returns true if the specified property is visible.
	 * @param name the name of the specified property
	 * @return true if the specified property is visible
	 */
	public boolean is_selected(String name) {
		return _selectedProperties.contains( name);
	}

	/**
	 * Returns the Property object which corresponds to the specified name and value.
	 * @param name the specified name
	 * @param value the specified value
	 * @return the Property object which corresponds to the specified name and value
	 */
	public PropertyBase get(String name, String value) {
		TreeMap<String, PropertyBase> properties = get( name);
		if ( null == properties)
			return null;

		return properties.get( value);
	}

	/** 
	 * Returns true if the new Property hashtable is added.
	 * @param name the name of the property
	 * @param properties the new Property hashtable(value(String) - PropertyBase)
	 * @param graphics2D the graphics object of JAVA
	 * @return true if the new Property hashtable is added
	 */
	public boolean append_properties(String name, TreeMap<String, PropertyBase> properties, Graphics2D graphics2D) {
		if ( _order.contains( name))
			return false;

		_order.add( name);
		put( name, properties);
		setup_property_image( properties, graphics2D);

		return true;
	}

	/**
	 * Returns true if the Property hashtable is updated with the specified one.
	 * @param name the name of the property
	 * @param properties the specified Property hashtable
	 * @param graphics2D the graphics object of JAVA
	 * @return true if the Property hashtable is updated with the specified one
	 */
	public boolean update_properties(String name, TreeMap<String, PropertyBase> properties, Graphics2D graphics2D) {
		if ( !_order.contains( name))
			return true;

		TreeMap<String, PropertyBase> originalProperties = get( name);
		if ( null == originalProperties)
			return true;

		Iterator iterator = originalProperties.entrySet().iterator();
		while ( iterator.hasNext()) {
			Object object = iterator.next();
			Map.Entry entry = ( Map.Entry)object;
			String value = ( String)entry.getKey();
			PropertyBase propertyBase = properties.get( value);
			if ( null == propertyBase)
				continue;

			originalProperties.put( value, propertyBase);
		}

		setup_property_image( properties, graphics2D);

		return true;
	}

	/**
	 * @param properties
	 * @param graphics2D
	 */
	private void setup_property_image(TreeMap<String, PropertyBase> properties, Graphics2D graphics2D) {
		Iterator iterator = properties.entrySet().iterator();
		while ( iterator.hasNext()) {
			Object object = iterator.next();
			Map.Entry entry = ( Map.Entry)object;
			String value = ( String)entry.getKey();
			PropertyBase propertyBase = ( PropertyBase)entry.getValue();
			propertyBase.setup( value, graphics2D);
		}
	}

	/**
	 * Appends the new Property which corresponds to the specified name and value.
	 * @param name the name of the property
	 * @param value the value of the property
	 * @param topOfOrder 
	 * @param graphics2D the graphics object of JAVA
	 */
	// TODO 2014.8.25 topOfOrderフラグを追加
	public void append(String name, String value, boolean topOfOrder, Graphics2D graphics2D) {
		TreeMap<String, PropertyBase> properties = get( name);
		if ( null == properties) {
			properties = new TreeMap<String, PropertyBase>();
			put( name, properties);
		}

		if ( null != properties.get( value))
			return;

		append( properties, value, graphics2D);

		if ( !_order.contains( name)) {
			if ( topOfOrder) {
				_order.insertElementAt( name, 0);
				_selectedProperties.insertElementAt( name, 0);
			} else {
				_selectedProperties.add( name);
				_order.add( name);
			}
		}
	}

	/**
	 * @param properties
	 * @param value
	 * @param graphics2D
	 */
	protected void append(TreeMap<String, PropertyBase> properties, String value, Graphics2D graphics2D) {
	}

	/**
	 * @param properties
	 * @param graphics2D
	 */
	protected void setup_default_property_image(TreeMap<String, PropertyBase> properties, Graphics2D graphics2D) {
	}

	/**
	 * Returns the maximum size of the property image.
	 * @return the maximum size of the property image
	 */
	public Dimension get_max_image_size() {
		Dimension maxSize = new Dimension();

		if ( _selectedProperties.isEmpty())
			return maxSize;

		int imageHeight = 0;
		int textHeight = 0;
		for ( String name:_selectedProperties) {
			TreeMap<String, PropertyBase> properties = get( name);
			if ( properties.isEmpty())
				continue;

			int maxTextHeight = 0;
			Iterator iterator = properties.entrySet().iterator();
			while ( iterator.hasNext()) {
				Object object = iterator.next();
				Map.Entry entry = ( Map.Entry)object;
				PropertyBase propertyBase = ( PropertyBase)entry.getValue();

				int width = propertyBase.get_width( /*name.equals( _selected_properties.get( 0))*/);
				if ( width > maxSize.width)
					maxSize.width = width;

				int height = propertyBase.get_image_height( /*name.equals( _selected_properties.get( 0))*/);
				if ( height > imageHeight)
					imageHeight = height;

				height = propertyBase.get_text_height();
				if ( height > maxTextHeight)
					maxTextHeight = height;
			}

			textHeight += maxTextHeight;
		}

		maxSize.height = Math.max( imageHeight, textHeight);

		return maxSize;
	}

	/**
	 * Returns true if at least one property exists.
	 * @return true if at least one property exists
	 */
	public boolean exist_property() {
		return !_order.isEmpty();
	}

	/**
	 * Returns true if at least one property is visible.
	 * @return true if at least one property is visible
	 */
	public boolean exist_selected_property() {
		return !_selectedProperties.isEmpty();
	}

	/**
	 * Returns true for drawing the image which corresponds to the specified property value successfully.
	 * @param graphics2D the graphics object of JAVA
	 * @param position the position of the image
	 * @param width the default width of the image
	 * @param height the default height of the image
	 * @param properties the property value hashtable(name(String) - value(String))
	 * @param rectangle the visible rectangle
	 * @param imageObserver an asynchronous update interface for receiving notifications about Image information as the Image is constructed
	 * @param imageExists true if the object has the image
	 * @return true for drawing the image which corresponds to the specified property value successfully
	 */
	public boolean draw_current_property_image(Graphics2D graphics2D, Point position, int width, int height, Map<String, String> properties, Rectangle rectangle, ImageObserver imageObserver, boolean imageExists) {
		if ( _selectedProperties.isEmpty())
			return false;

		boolean result = false;

		if ( !imageExists) {
			// スポットに画像ファイルが指定されていない場合はデフォルトの矩形描画を行う
			String value = properties.get( _selectedProperties.get( 0));
			if ( null == value)
				value = "";

			PropertyBase propertyBase = get( _selectedProperties.get( 0), value);
			if ( null == propertyBase)
				return false;

			result = propertyBase.draw_default_image( rectangle, graphics2D, position, width, height);
		}

		for ( int i = _selectedProperties.size() - 1; i >= 0; --i) {
			String value = properties.get( _selectedProperties.get( i));
			if ( null == value)
				value = "";

			PropertyBase propertyBase = get( _selectedProperties.get( i), value);
			if ( null == propertyBase)
				continue;

			if ( propertyBase.draw_image( rectangle, graphics2D, position, imageObserver))
				result = true;
		}

		return result;
	}

	/**
	 * Returns true for drawing the image which corresponds to the specified property value successfully.
	 * @param graphics2D the graphics object of JAVA
	 * @param position the position of the image
	 * @param width the default width of the image
	 * @param height the default height of the image
	 * @param transitionBase the scenario data
	 * @param rectangle the visible rectangle
	 * @param imageObserver an asynchronous update interface for receiving notifications about Image information as the Image is constructed
	 * @param imageExists true if the object has the image
	 * @return true for drawing the image which corresponds to the specified property value successfully
	 */
	public boolean draw_current_property_image(Graphics2D graphics2D, Point position, int width, int height, TransitionBase transitionBase, Rectangle rectangle, ImageObserver imageObserver, boolean imageExists) {
		if ( _selectedProperties.isEmpty())
			return false;

		boolean result = false;

		if ( !imageExists) {
			// スポットに画像ファイルが指定されていない場合はデフォルトの矩形描画を行う
			String value = ( null == transitionBase) 	? "" : transitionBase._properties.get( _selectedProperties.get( 0));
			if ( null == value)
				value = "";

			PropertyBase propertyBase = get( _selectedProperties.get( 0), value);
			if ( null == propertyBase)
				return false;

			result = propertyBase.draw_default_image( rectangle, graphics2D, position, width, height);
		}

		for ( int i = _selectedProperties.size() - 1; i >= 0; --i) {
			String value = ( null == transitionBase) 	? "" : transitionBase._properties.get( _selectedProperties.get( i));
			if ( null == value)
				value = "";

			PropertyBase propertyBase = get( _selectedProperties.get( i), value);
			if ( null == propertyBase)
				continue;

			if ( propertyBase.draw_image( rectangle, graphics2D, position, imageObserver))
				result = true;
		}

		return result;
	}

	/**
	 * Returns true for editting the properties successfully.
	 * @param titlePrefix
	 * @param frame the Frame from which the dialog is displayed
	 * @return true for editting the properties successfully
	 */
	public boolean edit(String titlePrefix, Frame frame) {
		return true;
	}

	/**
	 * @param title
	 * @param frame
	 * @return
	 */
	protected boolean edit(String title, String openDirectoryKey, Frame frame) {
		EditPropertiesDlg editPropertiesDlg = new EditPropertiesDlg( frame, title, true, openDirectoryKey, this);
		return editPropertiesDlg.do_modal();
	}

	/**
	 * Updates the properties with the specified properties.
	 * @param propertyManager the specified properties
	 * @param graphics2D the graphics object of JAVA
	 */
	public void update(PropertyManager propertyManager, Graphics2D graphics2D) {
		for ( String name:_order) {
			TreeMap<String, PropertyBase> src = propertyManager.get( name);
			TreeMap<String, PropertyBase> dest = get( name);
			Iterator iterator = src.entrySet().iterator();
			while ( iterator.hasNext()) {
				Object object = iterator.next();
				Map.Entry entry = ( Map.Entry)object;
				String value = ( String)entry.getKey();
				PropertyBase apb = ( PropertyBase)entry.getValue();
				PropertyBase propertyBase = dest.get( value);
				propertyBase.update( apb, graphics2D);
			}
		}
	}

	/**
	 * Updates the array of the visible properties with the specified one.
	 * @param selectedProperties the specified array of the visible properties
	 */
	public void update(Vector selectedProperties) {
		_selectedProperties.clear();
		_selectedProperties.addAll( selectedProperties);
	}

//	/**
//	 * @param width
//	 * @param height
//	 */
//	public void update_default_image(int width, int height) {
//		for ( String name:_order) {
//			TreeMap<String, PropertyBase> properties = get( name);
//			Iterator iterator = properties.entrySet().iterator();
//			while ( iterator.hasNext()) {
//				Object object = iterator.next();
//				Map.Entry entry = ( Map.Entry)object;
//				String value = ( String)entry.getKey();
//				PropertyBase propertyBase = ( PropertyBase)entry.getValue();
//				propertyBase.update_default_image( width, height);
//			}
//		}
//	}

	/**
	 * Returns true if the specified image file is in use.
	 * @param filename the name of the specified image file
	 * @return true if the specified image file is in use
	 */
	public boolean uses_this_image(String filename) {
		for ( String name:_order) {
			TreeMap<String, PropertyBase> properties = get( name);
			Iterator iterator = properties.entrySet().iterator();
			while ( iterator.hasNext()) {
				Object object = iterator.next();
				Map.Entry entry = ( Map.Entry)object;
				String value = ( String)entry.getKey();
				PropertyBase propertyBase = ( PropertyBase)entry.getValue();
				if ( propertyBase.uses_this_image( filename))
					return true;
			}
		}
		return false;
	}

	/**
	 * Sets the specified new image if the object uses the specified image.
	 * @param originalFilename the specified image file name
	 * @param newFilename the specified new image file name
	 */
	public void update_image(String originalFilename, String newFilename) {
		for ( String name:_order) {
			TreeMap<String, PropertyBase> properties = get( name);
			Iterator iterator = properties.entrySet().iterator();
			while ( iterator.hasNext()) {
				Object object = iterator.next();
				Map.Entry entry = ( Map.Entry)object;
				String value = ( String)entry.getKey();
				PropertyBase propertyBase = ( PropertyBase)entry.getValue();
				propertyBase.update_image( originalFilename, newFilename);
			}
		}
	}

	/**
	 * Returns true for writing all property data successfully.
	 * @param writer the abstract class for writing to character streams
	 * @param nodeName the name of the node
	 * @return true for writing all property data successfully
	 * @throws SAXException encapsulate a general SAX error or warning
	 */
	public boolean write(Writer writer, String nodeName) throws SAXException {
		if ( isEmpty())
			return true;

		if ( !write_properties( null, writer, nodeName))
			return false;

		if ( !write_selected_properties( writer, nodeName))
			return false;

		return true;
	}

	/**
	 * Returns true for writing all property graphic data successfully.
	 * @param rootDirectory the root directory for Animator data
	 * @param writer the abstract class for writing to character streams
	 * @param nodeName the name of the node
	 * @return for writing all property graphic data successfully
	 * @throws SAXException encapsulate a general SAX error or warning
	 */
	public boolean write_graphic_data(File rootDirectory, Writer writer, String nodeName) throws SAXException {
		if ( isEmpty())
			return true;

		if ( !write_properties( rootDirectory, writer, nodeName))
			return false;

		return true;
	}

	/**
	 * @param rootDirectory
	 * @param writer
	 * @param nodeName
	 * @return
	 * @throws SAXException
	 */
	private boolean write_properties(File rootDirectory, Writer writer, String nodeName) throws SAXException {
		for ( String name:_order) {

			TreeMap<String, PropertyBase> properties = get( name);
			if ( null == properties)
				return false;

			AttributesImpl attributesImpl = new AttributesImpl();
			attributesImpl.addAttribute( null, null, "name", "", Writer.escapeAttributeCharData( name));

			if ( properties.isEmpty()) {
				writer.writeElement( null, null, nodeName, attributesImpl);
				continue;
			}

			writer.startElement( null, null, nodeName, attributesImpl);

			Iterator iterator = properties.entrySet().iterator();
			while ( iterator.hasNext()) {
				Object object = iterator.next();
				Map.Entry entry = ( Map.Entry)object;
				String value = ( String)entry.getKey();
				PropertyBase propertyBase = ( PropertyBase)entry.getValue();
				if ( !propertyBase.write( rootDirectory, value, writer))
					return false;
			}

			writer.endElement( null, null, nodeName);
		}

		return true;
	}

	/**
	 * @param writer
	 * @param nodeName
	 * @return
	 * @throws SAXException
	 */
	private boolean write_selected_properties(Writer writer, String nodeName) throws SAXException {
		if ( _selectedProperties.isEmpty())
			return true;

		writer.startElement( null, null, "selected_" + nodeName + "_property", new AttributesImpl());
		for ( String name:_selectedProperties) {
			AttributesImpl attributesImpl = new AttributesImpl();
			attributesImpl.addAttribute( null, null, "name", "", name);

			writer.writeElement( null, null, "data", attributesImpl);
		}
		writer.endElement( null, null, "selected_" + nodeName + "_property");

		return true;
	}
}
