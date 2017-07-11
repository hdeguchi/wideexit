/**
 * 
 */
package soars.common.utility.swing.image;

import java.io.File;
import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;

import org.xml.sax.SAXException;
import org.xml.sax.helpers.AttributesImpl;

import soars.common.utility.xml.sax.Writer;

/**
 * The image property hashtable(String[filename] - ImageProperty)
 * @author kurata / SOARS project
 */
public class ImagePropertyManager extends TreeMap<String, ImageProperty> {

	/**
	 * Synchronized object.
	 */
	static private Object _lock = new Object();

	/**
	 * The instance of ImagePropertyManager.
	 */
	static private ImagePropertyManager _imagePropertyManager = null;

	/**
	 * The startup routine.
	 */
	static {
		startup();
	}

	/**
	 * Creates the instance of ImagePropertyManager, if it is null.
	 */
	private static void startup() {
		synchronized( _lock) {
			if ( null == _imagePropertyManager) {
				_imagePropertyManager = new ImagePropertyManager();
			}
		}
	}

	/**
	 * Returns the instance of ImagePropertyManager.
	 * @return the instance of ImagePropertyManager
	 */
	public static ImagePropertyManager get_instance() {
		if ( null == _imagePropertyManager) {
			System.exit( 0);
		}

		return _imagePropertyManager;
	}

	/**
	 * Creates the Image property hashtable(String[filename] - ImageProperty).
	 */
	public ImagePropertyManager() {
		super();
	}

	/**
	 * Clears hashtable.
	 */
	public void cleanup() {
		clear();
	}

	/**
	 * Returns true if this Class object writes all data to xml stream successfully.
	 * @param writer the XML stream
	 * @return true if this Class object writes all data to xml stream successfully
	 * @throws SAXException
	 */
	public boolean write(Writer writer) throws SAXException {
		Iterator iterator = entrySet().iterator();
		while ( iterator.hasNext()) {
			Object object = iterator.next();
			Map.Entry entry = ( Map.Entry)object;
			String filename = ( String)entry.getKey();
			ImageProperty imageProperty = ( ImageProperty)entry.getValue();
			write( filename, imageProperty, writer);
		}
		return true;
	}

	/**
	 * Returns true if this Class object writes all data to xml stream successfully.
	 * @param files the specified image files
	 * @param writer the XML stream
	 * @return true if this Class object writes all data to xml stream successfully
	 * @return
	 * @throws SAXException
	 */
	public boolean write(File[] files, Writer writer) throws SAXException {
		for ( int i = 0; i < files.length; ++i) {
			ImageProperty imageProperty = get( files[ i].getName());
			if ( null == imageProperty)
				return false;

			write( files[ i].getName(), imageProperty, writer);
		}
		return true;
	}

	/**
	 * @param filename
	 * @param imageProperty
	 * @param writer
	 * @throws SAXException
	 */
	private void write(String filename, ImageProperty imageProperty, Writer writer) throws SAXException {
		AttributesImpl attributesImpl = new AttributesImpl();

		attributesImpl.addAttribute( null, null, "filename", "", Writer.escapeAttributeCharData( filename));
		attributesImpl.addAttribute( null, null, "width", "", String.valueOf( imageProperty._width));
		attributesImpl.addAttribute( null, null, "height", "", String.valueOf( imageProperty._height));

		writer.writeElement( null, null, "data", attributesImpl);
	}
}
