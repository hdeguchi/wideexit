/*
 * 2005/07/01
 */
package soars.application.animator.file.importer;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.io.File;
import java.util.TreeMap;
import java.util.Vector;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import soars.application.animator.file.common.ImageManipulator;
import soars.application.animator.main.Administrator;
import soars.application.animator.main.internal.ObjectManager;
import soars.application.animator.object.entity.agent.AgentObject;
import soars.application.animator.object.entity.spot.SpotObject;
import soars.application.animator.object.property.agent.AgentProperty;
import soars.application.animator.object.property.base.PropertyBase;
import soars.application.animator.object.property.spot.SpotProperty;
import soars.application.animator.setting.common.CommonProperty;
import soars.common.utility.swing.image.ImageProperty;
import soars.common.utility.swing.image.ImagePropertyManager;

/**
 * The XML SAX loader for Animator graphics data.
 * @author kurata / SOARS project
 */
public class GraphicDataSaxLoader extends DefaultHandler {

	/**
	 * Root directory for Animator graphics file.
	 */
	private File _rootDirectory = null;

	/**
	 * 
	 */
	private ObjectManager _objectManager = null;

	/**
	 * Graphics object of JAVA.
	 */
	private Graphics2D _graphics2D = null;

	/**
	 * True while reading the data of the properties for SOARS.
	 */
	private boolean _property = false;

	/**
	 * True while reading the data of the SOARS objects.
	 */
	private boolean _object = false;

	/**
	 * String in which XML string is stored temporarily.
	 */
	private String _value = null;

	/**
	 * String in which the name of the property for SOARS is stored temporarily.
	 */
	private String _name = null;

	/**
	 * SpotProperty hashtable(String[name] - SpotProperty)
	 */
	private TreeMap<String, PropertyBase> _spotPropertyMap = null;

	/**
	 * Array of the names of visible spot properties.
	 */
	private Vector<PropertyBase> _selectedSpotProperties = null;

	/**
	 * AgentProperty hashtable(String[name] - AgentProperty)
	 */
	private TreeMap<String, PropertyBase> _agentPropertyMap = null;

	/**
	 * Array of the names of visible agent properties.
	 */
	private Vector<PropertyBase> _selectedAgentProperties = null;

	/**
	 * Temporary SpotObject.
	 */
	private SpotObject _spotObject = null;

	/**
	 * Temporary AgentObject.
	 */
	private AgentObject _agentObject = null;

	/**
	 * ImageProperty hashtable(String[filename] - ImageProperty)
	 */
	private TreeMap<String, ImageProperty> _imagePropertyMap = new TreeMap<String, ImageProperty>();

	/**
	 * False if error occurred.
	 */
	static private boolean _result;

	/**
	 * Returns true if loading the specified file is completed successfully.
	 * @param file the specified XML file of Animator graphics data
	 * @param rootDirectory the root directory for Animator graphics data
	 * @param objectManager
	 * @param graphics2D the graphics object of JAVA
	 * @return true if loading the specified file is completed successfully
	 */
	public static boolean execute(File file, File rootDirectory, ObjectManager objectManager, Graphics2D graphics2D) {
		_result = false;
		try {
			SAXParserFactory saxParserFactory = SAXParserFactory.newInstance();
			SAXParser saxParser = saxParserFactory.newSAXParser();
			GraphicDataSaxLoader graphicDataSaxLoader = new GraphicDataSaxLoader( rootDirectory, objectManager, graphics2D);
			saxParser.parse( file, graphicDataSaxLoader);
			graphicDataSaxLoader.at_end_of_load();
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return _result;
	}

	/**
	 * Invoked at the end of the loading.
	 */
	private void at_end_of_load() {
		if ( !_result)
			return;

		_objectManager._agentObjectManager.arrange();
		ImagePropertyManager.get_instance().putAll( _imagePropertyMap);
	}

	/**
	 * Creates the XML SAX loader for Animator graphics data.
	 * @param rootDirectory the root directory for Animator graphics data
	 * @param objectManager
	 * @param graphics2D
	 */
	public GraphicDataSaxLoader(File rootDirectory, ObjectManager objectManager, Graphics2D graphics2D) {
		super();
		_rootDirectory = rootDirectory;
		_objectManager = objectManager;
		_graphics2D = graphics2D;
	}

	/* (Non Javadoc)
	 * @see org.xml.sax.helpers.DefaultHandler#startElement(java.lang.String, java.lang.String, java.lang.String, org.xml.sax.Attributes)
	 */
	public void startElement(String arg0, String arg1, String arg2, Attributes arg3) throws SAXException {

		super.startElement(arg0, arg1, arg2, arg3);

		if ( arg2.equals( "animation_data")
			|| arg2.equals( "animator_graphic_data")) {
			_result = true;
			return;
		}

		if ( !_result)
			return;

		if ( arg2.equals( "property")) {
			on_property( arg3);
		} else if ( arg2.equals( "object")) {
			on_object( arg3);
		} else if ( arg2.equals( "agent_width")) {
			on_agent_width( arg3);
		} else if ( arg2.equals( "agent_height")) {
			on_agent_height( arg3);
		} else if ( arg2.equals( "spot_width")) {
			on_spot_width( arg3);
		} else if ( arg2.equals( "spot_height")) {
			on_spot_height( arg3);
		} else if ( arg2.equals( "minimum_width")) {
			on_minimum_width( arg3);
		} else if ( arg2.equals( "velocity")) {
			on_velocity( arg3);
		} else if ( arg2.equals( "selected_spot_property")) {
			on_selected_spot_property( arg3);
		} else if ( arg2.equals( "selected_agent_property")) {
			on_selected_agent_property( arg3);
		} else if ( arg2.equals( "spot")) {
			on_spot( arg3);
		} else if ( arg2.equals( "agent")) {
			on_agent( arg3);
		} else if ( arg2.equals( "data")) {
			on_data( arg3);
		}
	}

	/**
	 * Invoked at the head of "property" tag.
	 * @param attributes the interface for a list of XML attributes
	 */
	private void on_property(Attributes attributes) {
		if ( null == _spotObject && null == _agentObject)
			_property = true;
	}

	/**
	 * Invoked at the head of "object" tag.
	 * @param attributes the interface for a list of XML attributes
	 */
	private void on_object(Attributes attributes) {
		_object = true;
	}

	/**
	 * Invoked at the head of "agent_width" tag.
	 * @param attributes the interface for a list of XML attributes
	 */
	private void on_agent_width(Attributes attributes) {
		_value = "";
	}

	/**
	 * Invoked at the head of "agent_height" tag.
	 * @param attributes the interface for a list of XML attributes
	 */
	private void on_agent_height(Attributes attributes) {
		_value = "";
	}

	/**
	 * Invoked at the head of "spot_width" tag.
	 * @param attributes the interface for a list of XML attributes
	 */
	private void on_spot_width(Attributes attributes) {
		_value = "";
	}

	/**
	 * Invoked at the head of "spot_height" tag.
	 * @param attributes the interface for a list of XML attributes
	 */
	private void on_spot_height(Attributes attributes) {
		_value = "";
	}

	/**
	 * Invoked at the head of "minimum_width" tag.
	 * @param attributes the interface for a list of XML attributes
	 */
	private void on_minimum_width(Attributes attributes) {
		_value = "";
	}

	/**
	 * Invoked at the head of "velocity" tag.
	 * @param attributes the interface for a list of XML attributes
	 */
	private void on_velocity(Attributes attributes) {
		_value = "";
	}

	/**
	 * Invoked at the head of "selected_spot_property" tag.
	 * @param attributes the interface for a list of XML attributes
	 */
	private void on_selected_spot_property(Attributes attributes) {
		_selectedSpotProperties = new Vector<PropertyBase>();
	}

	/**
	 * Invoked at the head of "selected_agent_property" tag.
	 * @param attributes the interface for a list of XML attributes
	 */
	private void on_selected_agent_property(Attributes attributes) {
		_selectedAgentProperties = new Vector<PropertyBase>();
	}

	/**
	 * Invoked at the head of "spot" tag.
	 * @param attributes the interface for a list of XML attributes
	 */
	private void on_spot(Attributes attributes) {
		if ( _property)
			on_spot_property( attributes);
		else if ( _object)
			on_spot_object( attributes);
	}

	/**
	 * Invoked at the head of "agent" tag.
	 * @param attributes the interface for a list of XML attributes
	 */
	private void on_agent(Attributes attributes) {
		if ( _property)
			on_agent_property( attributes);
		else if ( _object)
			on_agent_object( attributes);
	}

	/**
	 * Prepares for reading the new spot properties.
	 * @param attributes the interface for a list of XML attributes
	 */
	private void on_spot_property(Attributes attributes) {
		_name = attributes.getValue( "name");
		if ( null == _name || _name.equals( "")) {
			_result = false;
			return;
		}

		_spotPropertyMap = new TreeMap<String, PropertyBase>();
	}

	/**
	 * Prepares for reading the new agent properties.
	 * @param attributes the interface for a list of XML attributes
	 */
	private void on_agent_property(Attributes attributes) {
		_name = attributes.getValue( "name");
		if ( null == _name || _name.equals( "")) {
			_result = false;
			return;
		}

		_agentPropertyMap = new TreeMap<String, PropertyBase>();
	}

	/**
	 * Reads the spot object data.
	 * @param attributes the interface for a list of XML attributes
	 */
	private void on_spot_object(Attributes attributes) {
		String name = attributes.getValue( "name");
		if ( null == name/* || name.equals( "")*/) {
			_result = false;
			return;
		}

//		if ( null != SpotObjectManager.get_instance().get( name)) {
//			_result = false;
//			return;
//		}

		_spotObject = new SpotObject( name, null, _objectManager);


		String attribute = attributes.getValue( "visible");
		if ( null != attribute && !attribute.equals( ""))
			_spotObject._visible = ( attribute.equals( "true")) ? true : false;


		attribute = attributes.getValue( "visible_name");
		if ( null != attribute && !attribute.equals( ""))
			_spotObject._visibleName = ( attribute.equals( "true")) ? true : false;


		String imageR = attributes.getValue( "image_r");
		if ( null != imageR && !imageR.equals( "")) {
			String imageG = attributes.getValue( "image_g");
			if ( null != imageG && !imageG.equals( "")) {
				String imageB = attributes.getValue( "image_b");
				if ( null != imageB && !imageB.equals( ""))
					_spotObject._imageColor = new Color( Integer.parseInt( imageR), Integer.parseInt( imageG), Integer.parseInt( imageB));
			}
		}


		String textR = attributes.getValue( "text_r");
		if ( null != textR && !textR.equals( "")) {
			String textG = attributes.getValue( "text_g");
			if ( null != textG && !textG.equals( "")) {
				String textB = attributes.getValue( "text_b");
				if ( null != textB && !textB.equals( ""))
					_spotObject._textColor = new Color( Integer.parseInt( textR), Integer.parseInt( textG), Integer.parseInt( textB));
			}
		}


		String fontName = attributes.getValue( "font_name");
		if ( null != fontName && !fontName.equals( "")) {
			String fontStyle = attributes.getValue( "font_style");
			if ( null != fontStyle && !fontStyle.equals( "")) {
				String fontSize = attributes.getValue( "font_size");
				if ( null != fontSize && !fontSize.equals( "")) {
					_spotObject._font = new Font( fontName, Integer.parseInt( fontStyle), Integer.parseInt( fontSize));
				}
			}
		}

		if ( null == _spotObject._font) {
			Font font = _graphics2D.getFont();
			_spotObject._font = new Font( font.getFontName(), font.getStyle(), font.getSize());
		}


		if ( !_spotObject.setup( _graphics2D)) {
			_result = false;
			return;
		}


		// TODO エラーメッセージを表示(未実装)
		if ( !get_image( attributes, _spotObject)) {
			System.out.println( "Could not get image!");
		}


		int x, y;

		attribute = attributes.getValue( "x");
		if ( null == attribute || attribute.equals( "")) {
			_result = false;
			return;
		}

		x = Integer.parseInt( attribute);

		attribute = attributes.getValue( "y");
		if ( null == attribute || attribute.equals( "")) {
			_result = false;
			return;
		}

		y = Integer.parseInt( attribute);

		_spotObject.set_position( x, y);

		if ( !set_image_position_and_image_size( attributes)) {
			_result = false;
			return;
		}

		_objectManager._spotObjectManager.update( name, _spotObject);
	}

	/**
	 * @param attributes
	 * @return
	 */
	private boolean set_image_position_and_image_size(Attributes attributes) {
		int imageX, imageY, imageWidth, imageHeight;

		String attribute = attributes.getValue( "image_x");
		if ( null == attribute || attribute.equals( ""))
			return true;

		try {
			imageX = Integer.parseInt( attribute);
		} catch (NumberFormatException e) {
			//e.printStackTrace();
			return false;
		}

		attribute = attributes.getValue( "image_y");
		if ( null == attribute || attribute.equals( ""))
			return true;

		try {
			imageY = Integer.parseInt( attribute);
		} catch (NumberFormatException e) {
			//e.printStackTrace();
			return false;
		}

		attribute = attributes.getValue( "image_width");
		if ( null == attribute || attribute.equals( ""))
			return true;

		try {
			imageWidth = Integer.parseInt( attribute);
		} catch (NumberFormatException e) {
			//e.printStackTrace();
			return false;
		}

		attribute = attributes.getValue( "image_height");
		if ( null == attribute || attribute.equals( ""))
			return true;

		try {
			imageHeight = Integer.parseInt( attribute);
		} catch (NumberFormatException e) {
			//e.printStackTrace();
			return false;
		}

		_spotObject.set_image_position_and_image_size( imageX, imageY, imageWidth, imageHeight);

		return true;
	}

	/**
	 * Reads the agent object data.
	 * @param attributes the interface for a list of XML attributes
	 */
	private void on_agent_object(Attributes attributes) {
		String name = attributes.getValue( "name");
		if ( null == name || name.equals( "")) {
			_result = false;
			return;
		}

		_agentObject = new AgentObject( name, _objectManager);


		String attribute = attributes.getValue( "visible");
		if ( null != attribute && !attribute.equals( ""))
			_agentObject._visible = ( attribute.equals( "true")) ? true : false;


		attribute = attributes.getValue( "visible_name");
		if ( null != attribute && !attribute.equals( ""))
			_agentObject._visibleName = ( attribute.equals( "true")) ? true : false;


		String imageR = attributes.getValue( "image_r");
		if ( null != imageR && !imageR.equals( "")) {
			String imageG = attributes.getValue( "image_g");
			if ( null != imageG && !imageG.equals( "")) {
				String imageB = attributes.getValue( "image_b");
				if ( null != imageB && !imageB.equals( ""))
					_agentObject._imageColor = new Color( Integer.parseInt( imageR), Integer.parseInt( imageG), Integer.parseInt( imageB));
			}
		}


		String textR = attributes.getValue( "text_r");
		if ( null != textR && !textR.equals( "")) {
			String textG = attributes.getValue( "text_g");
			if ( null != textG && !textG.equals( "")) {
				String textB = attributes.getValue( "text_b");
				if ( null != textB && !textB.equals( ""))
					_agentObject._textColor = new Color( Integer.parseInt( textR), Integer.parseInt( textG), Integer.parseInt( textB));
			}
		}


		String fontName = attributes.getValue( "font_name");
		if ( null != fontName && !fontName.equals( "")) {
			String fontStyle = attributes.getValue( "font_style");
			if ( null != fontStyle && !fontStyle.equals( "")) {
				String fontSize = attributes.getValue( "font_size");
				if ( null != fontSize && !fontSize.equals( "")) {
					_agentObject._font = new Font( fontName, Integer.parseInt( fontStyle), Integer.parseInt( fontSize));
				}
			}
		}

		if ( null == _agentObject._font) {
			Font font = _graphics2D.getFont();
			_agentObject._font = new Font( font.getFontName(), font.getStyle(), font.getSize());
		}


		if ( !_agentObject.setup( _graphics2D)) {
			_result = false;
			return;
		}


		// TODO エラーメッセージを表示(未実装)
		if ( !get_image( attributes, _agentObject)) {
			System.out.println( "Could not get image!");
		}

		_objectManager._agentObjectManager.update( name, _agentObject);
	}

	/**
	 * Invoked at the head of "data" tag.
	 * @param attributes the interface for a list of XML attributes
	 */
	private void on_data(Attributes attributes) {
		if ( _property) {
			if ( null != _spotPropertyMap) {
				get_spot_property( attributes);
				return;
			}
			if ( null != _agentPropertyMap) {
				get_agent_property( attributes);
				return;
			}
			if ( null != _selectedSpotProperties) {
				get_selected_spot_property( attributes);
				return;
			}
			if ( null != _selectedAgentProperties) {
				get_selected_agent_property( attributes);
				return;
			}
		}
	}

	/**
	 * Reads the spot property data.
	 * @param attributes the interface for a list of XML attributes
	 */
	private void get_spot_property(Attributes attributes) {
		if ( null == _spotPropertyMap)
			return;

		String value = attributes.getValue( "value");
		if ( null == value) {
			_result = false;
			return;
		}

		SpotProperty spotProperty = new SpotProperty();
		get_property( spotProperty, attributes);
		_spotPropertyMap.put( value, spotProperty);
	}

	/**
	 * Reads the agent property data.
	 * @param attributes the interface for a list of XML attributes
	 */
	private void get_agent_property(Attributes attributes) {
		if ( null == _agentPropertyMap)
			return;

		String value = attributes.getValue( "value");
		if ( null == value) {
			_result = false;
			return;
		}

		AgentProperty agentProperty = new AgentProperty();
		get_property( agentProperty, attributes);
		_agentPropertyMap.put( value, agentProperty);
	}

	/**
	 * Reads the property data into the specified property data.
	 * @param propertyBase the specified property data.
	 * @param attributes the interface for a list of XML attributes
	 */
	private void get_property(PropertyBase propertyBase, Attributes attributes) {
		String attribute = attributes.getValue( "visible");
		if ( null != attribute && !attribute.equals( ""))
			propertyBase._visible = ( attribute.equals( "true")) ? true : false;

		attribute = attributes.getValue( "image_r");
		if ( null != attribute && !attribute.equals( ""))
			propertyBase._imageR = Integer.parseInt( attribute);

		attribute = attributes.getValue( "image_g");
		if ( null != attribute && !attribute.equals( ""))
			propertyBase._imageG = Integer.parseInt( attribute);

		attribute = attributes.getValue( "image_b");
		if ( null != attribute && !attribute.equals( ""))
			propertyBase._imageB = Integer.parseInt( attribute);


		attribute = attributes.getValue( "text_r");
		if ( null != attribute && !attribute.equals( ""))
			propertyBase._textR = Integer.parseInt( attribute);

		attribute = attributes.getValue( "text_g");
		if ( null != attribute && !attribute.equals( ""))
			propertyBase._textG = Integer.parseInt( attribute);

		attribute = attributes.getValue( "text_b");
		if ( null != attribute && !attribute.equals( ""))
			propertyBase._textB = Integer.parseInt( attribute);


		String fontName = attributes.getValue( "font_name");
		if ( null != fontName && !fontName.equals( "")) {
			String fontStyle = attributes.getValue( "font_style");
			if ( null != fontStyle && !fontStyle.equals( "")) {
				String fontSize = attributes.getValue( "font_size");
				if ( null != fontSize && !fontSize.equals( "")) {
					propertyBase._font = new Font( fontName, Integer.parseInt( fontStyle), Integer.parseInt( fontSize));
				}
			}
		}

		if ( null == propertyBase._font) {
			Font font = _graphics2D.getFont();
			propertyBase._font = new Font( font.getFontName(), font.getStyle(), font.getSize());
		}



		// TODO エラーメッセージを表示(未実装)
		if ( !get_image( attributes, propertyBase)) {
			System.out.println( "Could not get image!");
		}
	}

	/**
	 * Reads the name of the visible spot property.
	 * @param attributes the interface for a list of XML attributes
	 */
	private void get_selected_spot_property(Attributes attributes) {
//		String name = attributes.getValue( "name");
//		if ( null == name || name.equals( "")) {
//			_result = false;
//			return;
//		}
//
//		_selected_spot_properties.add( name);
	}

	/**
	 * Reads the name of the visible agent property.
	 * @param attributes the interface for a list of XML attributes
	 */
	private void get_selected_agent_property(Attributes attributes) {
//		String name = attributes.getValue( "name");
//		if ( null == name || name.equals( "")) {
//			_result = false;
//			return;
//		}
//
//		_selected_agent_properties.add( name);
	}

	/**
	 * Returns true if the specified image is set to the specified object.
	 * @param attributes the interface for a list of XML attributes
	 * @param object the specified object(AgentObject, SpotObject, AgentProperty or SpotProperty)
	 * @return true if the specified image is set to the specified object
	 */
	private boolean get_image(Attributes attributes, Object object) {
		String attribute = attributes.getValue( "image");
		if ( null == attribute || attribute.equals( ""))
			return true;

		File src = new File( Administrator.get_instance().get_image_directory( _rootDirectory), attribute);
		if ( !src.exists() || !src.isFile() || !src.canRead())
			return ImageManipulator.get_default_image( object, src.getName(), _imagePropertyMap);

		return ImageManipulator.get_image( object, src, _imagePropertyMap);
	}

	/* (Non Javadoc)
	 * @see org.xml.sax.helpers.DefaultHandler#characters(char[], int, int)
	 */
	public void characters(char[] arg0, int arg1, int arg2) throws SAXException {
		if ( !_result)
			return;

		if ( null != _value) {
			String value = new String( arg0, arg1, arg2);
			_value += value;
		}
	}

	/* (Non Javadoc)
	 * @see org.xml.sax.helpers.DefaultHandler#endElement(java.lang.String, java.lang.String, java.lang.String)
	 */
	public void endElement(String arg0, String arg1, String arg2) throws SAXException {

		if ( !_result) {
			super.endElement(arg0, arg1, arg2);
			return;
		}

		if ( arg2.equals( "property")) {
			on_property();
		} else if ( arg2.equals( "object")) {
			on_object();
		} else if ( arg2.equals( "agent_width")) {
			on_agent_width();
		} else if ( arg2.equals( "agent_height")) {
			on_agent_height();
		} else if ( arg2.equals( "spot_width")) {
			on_spot_width();
		} else if ( arg2.equals( "spot_height")) {
			on_spot_height();
		} else if ( arg2.equals( "minimum_width")) {
			on_minimum_width();
		} else if ( arg2.equals( "velocity")) {
			on_velocity();
		} else if ( arg2.equals( "selected_spot_property")) {
			on_selected_spot_property();
		} else if ( arg2.equals( "selected_agent_property")) {
			on_selected_agent_property();
		} else if ( arg2.equals( "spot")) {
			on_spot();
		} else if ( arg2.equals( "agent")) {
			on_agent();
		}
	}

	/**
	 * Invoked at the end of the "property" tag.
	 */
	private void on_property() {
		_property = false;
	}

	/**
	 * Invoked at the end of the "object" tag.
	 */
	private void on_object() {
		_object = false;
	}

	/**
	 * Invoked at the end of the "agent_width" tag.
	 */
	private void on_agent_width() {
		try {
			CommonProperty.get_instance()._agentWidth = Integer.parseInt( _value);
		} catch (NumberFormatException e) {
			//e.printStackTrace();
			_result = false;
			return;
		}
		_value = null;
	}

	/**
	 * Invoked at the end of the "agent_height" tag.
	 */
	private void on_agent_height() {
		try {
			CommonProperty.get_instance()._agentHeight = Integer.parseInt( _value);
		} catch (NumberFormatException e) {
			//e.printStackTrace();
			_result = false;
			return;
		}
		_value = null;
	}

	/**
	 * Invoked at the end of the "spot_width" tag.
	 */
	private void on_spot_width() {
		try {
			CommonProperty.get_instance()._spotWidth = Integer.parseInt( _value);
		} catch (NumberFormatException e) {
			//e.printStackTrace();
			_result = false;
			return;
		}
		_value = null;
	}

	/**
	 * Invoked at the end of the "spot_height" tag.
	 */
	private void on_spot_height() {
		try {
			CommonProperty.get_instance()._spotHeight = Integer.parseInt( _value);
		} catch (NumberFormatException e) {
			//e.printStackTrace();
			_result = false;
			return;
		}
		_value = null;
	}

	/**
	 * Invoked at the end of the "minimum_width" tag.
	 */
	private void on_minimum_width() {
//		try {
//		CommonProperty.get_instance()._minimum_width = Integer.parseInt( _value);
//	} catch (NumberFormatException e) {
//		//e.printStackTrace();
//		_result = false;
//		return;
//	}
		_value = null;
	}

	/**
	 * Invoked at the end of the "velocity" tag.
	 */
	private void on_velocity() {
		try {
			CommonProperty.get_instance()._divide = Integer.parseInt( _value);
		} catch (NumberFormatException e) {
			//e.printStackTrace();
			_result = false;
			return;
		}
		_value = null;
	}

	/**
	 * Invoked at the end of "selected_spot_property" tag.
	 */
	private void on_selected_spot_property() {
//		if ( _selectedSpotProperties.isEmpty())
//			return;
//
//		_result = _objectManager._scenarioManager.set_selected_spot_properties( _selectedSpotProperties);
//		_selectedSpotProperties = null;
	}

	/**
	 * Invoked at the end of "selected_agent_property" tag.
	 */
	private void on_selected_agent_property() {
//		if ( _selectedAgentProperties.isEmpty())
//			return;
//
//		_result = _objectManager._scenarioManager.set_selected_agent_properties( _selectedAgentProperties);
//		_selectedAgentProperties = null;
	}

	/**
	 * Invoked at the end of "spot" tag.
	 */
	private void on_spot() {
		if ( _property)
			on_spot_property();
		else if ( _object)
			on_spot_object();
	}

	/**
	 * Invoked at the end of "agent" tag.
	 */
	private void on_agent() {
		if ( _property)
			on_agent_property();
		else if ( _object)
			on_agent_object();
	}

	/**
	 * Invoked after reading the new spot properties.
	 */
	private void on_spot_property() {
		_result = _objectManager._scenarioManager._spotPropertyManager.update_properties( _name, _spotPropertyMap, _graphics2D);
		_name = null;
		_spotPropertyMap = null;
	}

	/**
	 * Invoked after reading the new agent properties.
	 */
	private void on_agent_property() {
		_result = _objectManager._scenarioManager._agentPropertyManager.update_properties( _name, _agentPropertyMap, _graphics2D);
		_name = null;
		_agentPropertyMap = null;
	}

	/**
	 * Invoked after reading the spot object data.
	 */
	private void on_spot_object() {
		_spotObject = null;
	}

	/**
	 * Invoked after reading the agent object data.
	 */
	private void on_agent_object() {
		_agentObject = null;
	}
}
