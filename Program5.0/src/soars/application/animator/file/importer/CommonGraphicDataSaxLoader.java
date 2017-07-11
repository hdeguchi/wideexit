/*
 * 2005/07/01
 */
package soars.application.animator.file.importer;

import java.io.File;
import java.util.TreeMap;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import soars.application.animator.setting.common.CommonProperty;
import soars.common.utility.swing.image.ImageProperty;
import soars.common.utility.swing.image.ImagePropertyManager;

/**
 * The XML SAX loader for Animator graphics data.
 * @author kurata / SOARS project
 */
public class CommonGraphicDataSaxLoader extends DefaultHandler {


	/**
	 * True while reading the data of the properties for SOARS.
	 */
	private boolean _property = false;

	/**
	 * True while reading the informations of image files.
	 */
	private boolean _image = false;

	/**
	 * String in which XML string is stored temporarily.
	 */
	private String _value = null;

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
	 * @return true if loading the specified file is completed successfully
	 */
	public static boolean execute(File file) {
		_result = false;
		try {
			SAXParserFactory saxParserFactory = SAXParserFactory.newInstance();
			SAXParser saxParser = saxParserFactory.newSAXParser();
			CommonGraphicDataSaxLoader commonGraphicDataSaxLoader = new CommonGraphicDataSaxLoader();
			saxParser.parse( file, commonGraphicDataSaxLoader);
			commonGraphicDataSaxLoader.at_end_of_load();
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

		ImagePropertyManager.get_instance().putAll( _imagePropertyMap);
	}

	/**
	 * Creates the XML SAX loader for Animator graphics data.
	 * @param fileDirectory the parent directory which contains XML file of Animator graphics data
	 * @param rootDirectory the root directory for Animator data
	 * @param objectManager
	 * @param graphics2D
	 */
	public CommonGraphicDataSaxLoader() {
		super();
	}

	/* (Non Javadoc)
	 * @see org.xml.sax.helpers.DefaultHandler#startElement(java.lang.String, java.lang.String, java.lang.String, org.xml.sax.Attributes)
	 */
	public void startElement(String arg0, String arg1, String arg2, Attributes arg3) throws SAXException {

		super.startElement(arg0, arg1, arg2, arg3);

		if ( arg2.equals( "animation_common_data")
			|| arg2.equals( "animator_common_graphic_data")) {
			_result = true;
			return;
		}

		if ( !_result)
			return;

		if ( arg2.equals( "property")) {
			on_property( arg3);
		} else if ( arg2.equals( "agent_width")) {
			on_agent_width( arg3);
		} else if ( arg2.equals( "agent_height")) {
			on_agent_height( arg3);
		} else if ( arg2.equals( "spot_width")) {
			on_spot_width( arg3);
		} else if ( arg2.equals( "spot_height")) {
			on_spot_height( arg3);
		} else if ( arg2.equals( "image")) {
			on_image( arg3);
		} else if ( arg2.equals( "data")) {
			on_data( arg3);
		}
	}

	/**
	 * Invoked at the head of "property" tag.
	 * @param attributes the interface for a list of XML attributes
	 */
	private void on_property(Attributes attributes) {
		_property = true;
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
	 * @param attributes the interface for a list of XML attributes
	 */
	private void on_image(Attributes attributes) {
		_image = true;
	}

	/**
	 * Invoked at the head of "data" tag.
	 * @param attributes the interface for a list of XML attributes
	 */
	private void on_data(Attributes attributes) {
		if ( _image) {
			get_image_property( attributes);
		}
	}

	/**
	 * Reads the image property data.
	 * @param attributes the interface for a list of XML attributes
	 */
	private void get_image_property(Attributes attributes) {
		String filename = attributes.getValue( "filename");
		if ( null == filename || filename.equals( "")) {
			_result = false;
			return;
		}

		int width = 0;
		String attribute = attributes.getValue( "width");
		if ( null != attribute && !attribute.equals( "")) {
			try {
				width = Integer.parseInt( attribute);
			} catch (NumberFormatException e) {
				//e.printStackTrace();
				_result = false;
				return;
			}
		}

		int height = 0;
		attribute = attributes.getValue( "height");
		if ( null != attribute && !attribute.equals( "")) {
			try {
				height = Integer.parseInt( attribute);
			} catch (NumberFormatException e) {
				//e.printStackTrace();
				_result = false;
				return;
			}
		}

		_imagePropertyMap.put( filename, new ImageProperty( width, height));
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
		} else if ( arg2.equals( "agent_width")) {
			on_agent_width();
		} else if ( arg2.equals( "agent_height")) {
			on_agent_height();
		} else if ( arg2.equals( "spot_width")) {
			on_spot_width();
		} else if ( arg2.equals( "spot_height")) {
			on_spot_height();
		} else if ( arg2.equals( "image")) {
			on_image();
		}
	}

	/**
	 * Invoked at the end of the "property" tag.
	 */
	private void on_property() {
		_property = false;
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
	 * 
	 */
	private void on_image() {
		_image = false;
	}
}
