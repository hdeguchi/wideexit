/**
 * 
 */
package soars.common.soars.property;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.TreeMap;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import soars.common.soars.constant.CommonConstant;

/**
 * @author kurata
 *
 */
public class AnimationSaxLoader extends DefaultHandler {

	/**
	 * 
	 */
	private File _file = null;

	/**
	 * 
	 */
	private TreeMap<String, Property> _propertyMap = null;

	/**
	 * 
	 */
	private Property _parent = null;

	/**
	 * 
	 */
	private Property _property = null;

	/**
	 * 
	 */
	static private boolean _result;

	/**
	 * @param file
	 * @param propertyMap
	 * @return
	 */
	public static boolean execute(File file, TreeMap<String, Property> propertyMap) {
		_result = false;

		AnimationSaxLoader animationSaxLoader = new AnimationSaxLoader( file, propertyMap);

		try {
			ZipInputStream zipInputStream = new ZipInputStream( new FileInputStream( file));
			try {
				ZipEntry zipEntry = null;
				while ( null != ( zipEntry = zipInputStream.getNextEntry())) {
					if ( !zipEntry.getName().equals( CommonConstant._soarsRootDirectoryName + "/" + CommonConstant._animatorRootDirectoryName + "/" + CommonConstant._propertyFileName)) {
						zipInputStream.closeEntry();
						continue;
					}

					if ( zipEntry.isDirectory()) {
						zipInputStream.closeEntry();
						return false;
					}

					SAXParserFactory saxParserFactory = SAXParserFactory.newInstance();
					SAXParser saxParser = saxParserFactory.newSAXParser();
					saxParser.parse( zipInputStream, animationSaxLoader);
					animationSaxLoader.at_end_of_load();
					//zipInputStream.closeEntry();
					return _result;
				}
			} finally {
				//zipInputStream.closeEntry();
				zipInputStream.close();
			}
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		} catch (Exception e) {
			e.printStackTrace();
			animationSaxLoader.at_end_of_load();
			return false;
		}

		return _result;
	}

	/**
	 * 
	 */
	private void at_end_of_load() {
	}

	/**
	 * @param file
	 * @param propertyMap
	 */
	public AnimationSaxLoader(File file, TreeMap<String, Property> propertyMap) {
		super();
		_file= file;
		_propertyMap = propertyMap;
	}

	/* (non-Javadoc)
	 * @see org.xml.sax.helpers.DefaultHandler#startElement(java.lang.String, java.lang.String, java.lang.String, org.xml.sax.Attributes)
	 */
	public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {

		super.startElement(uri, localName, qName, attributes);

		if ( qName.equals( "animation_properties")) {
			_result = true;
			return;
		}

		if ( !_result)
			return;

		if ( qName.equals( "simulation"))
			on_simulation( attributes);		
		else if ( qName.equals( "property"))
			on_property( attributes);		
	}

	/**
	 * @param attributes
	 */
	private void on_simulation(Attributes attributes) {
		if ( null != _parent) {
			_result = false;
			return;
		}

		String id = attributes.getValue( "id");
		if ( null == id || id.equals( "")) {
			_result = false;
			return;
		}

		_parent = _propertyMap.get( id);
		if ( null == _parent) {
			_result = false;
			return;
		}
	}

	/**
	 * @param attributes
	 */
	private void on_property(Attributes attributes) {
		if ( null == _parent) {
			_result = false;
			return;
		}

		if ( null != _property) {
			_result = false;
			return;
		}

		String id = attributes.getValue( "id");
		if ( null == id || id.equals( "")) {
			_result = false;
			return;
		}

		String title = attributes.getValue( "title");
		if ( null == title) {
			_result = false;
			return;
		}

		_property = new Property( _file, "animation", id, title, "", _parent._id);
	}

	/* (non-Javadoc)
	 * @see org.xml.sax.helpers.DefaultHandler#characters(char[], int, int)
	 */
	public void characters(char[] ch, int start, int length) throws SAXException {
		if ( !_result)
			return;

		if ( null != _property) {
			String comment = new String( ch, start, length);
			_property._comment += comment;
		}
	}

	/* (non-Javadoc)
	 * @see org.xml.sax.helpers.DefaultHandler#endElement(java.lang.String, java.lang.String, java.lang.String)
	 */
	public void endElement(String uri, String localName, String qName) throws SAXException {

		if ( !_result) {
			super.endElement(uri, localName, qName);
			return;
		}

		if ( qName.equals( "simulation"))
			on_simulation();
		else if ( qName.equals( "property"))
			on_property();
	}

	/**
	 * 
	 */
	private void on_simulation() {
		if ( null == _parent) {
			_result = false;
			return;
		}

		_parent = null;
	}

	/**
	 * 
	 */
	private void on_property() {
		if ( null == _property) {
			_result = false;
			return;
		}

		_parent._propertyMap.put( _property._id, _property);
		_property = null;
	}
}
