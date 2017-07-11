/*
 * 2005/05/01
 */
package soars.application.visualshell.object.stage;

import org.xml.sax.SAXException;
import org.xml.sax.helpers.AttributesImpl;

import soars.application.visualshell.main.Constant;
import soars.common.utility.xml.sax.Writer;

/**
 * @author kurata
 */
public class Stage {

	/**
	 * 
	 */
	public String _name = "";

	/**
	 * 
	 */
	public boolean _random = false;

	/**
	 * 
	 */
	public String _comment = "";

	/**
	 * @param name
	 */
	public Stage(String name) {
		super();
		_name = name;
	}

	/**
	 * @param name
	 * @param random
	 */
	public Stage(String name, boolean random) {
		super();
		_random = random;
		_name = name;
	}

	/**
	 * @param name
	 * @param comment
	 */
	public Stage(String name, String comment) {
		super();
		_name = name;
		_comment = comment;
	}

	/**
	 * @param name
	 * @param random
	 * @param comment
	 */
	public Stage(String name, boolean random, String comment) {
		super();
		_name = name;
		_random = random;
		_comment = comment;
	}

	/**
	 * @param stage
	 */
	public Stage(Stage stage) {
		super();
		_name = stage._name;
		_random = stage._random;
		_comment = stage._comment;
	}

	/**
	 * 
	 */
	public void cleanup() {
	}

	/**
	 * @param prefix
	 * @return
	 */
	public Object get_initial_data(String prefix) {
		return ( prefix + "\t" + _name + "\t" + ( _random ? "true" : "false") + Constant._lineSeparator);
	}

	/**
	 * @return
	 */
	public String get_script() {
		return ( _name + ( _random ? "*" : "") + Constant._lineSeparator);
	}

	/**
	 * @param tagName
	 * @param writer
	 * @return
	 */
	public boolean write(String tagName, Writer writer) throws SAXException {
		AttributesImpl attributesImpl = new AttributesImpl();
		attributesImpl.addAttribute( null, null, "name", "", Writer.escapeAttributeCharData( _name));
		attributesImpl.addAttribute( null, null, "random", "", _random ? "true" : "false");
		write( tagName, writer, attributesImpl);
		return true;
	}

	/**
	 * @param tagName
	 * @param writer
	 * @param attributesImpl
	 * @throws SAXException
	 */
	private void write(String tagName, Writer writer, AttributesImpl attributesImpl) throws SAXException {
		if ( _comment.equals( ""))
			writer.writeElement( null, null, tagName, attributesImpl);
		else {
			writer.startElement( null, null, tagName, attributesImpl);
			writer.characters( _comment.toCharArray(), 0, _comment.length());
			writer.endElement( null, null, tagName);
		}
	}
}
