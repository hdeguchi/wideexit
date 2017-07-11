/*
 * 2005/06/01
 */
package soars.application.visualshell.object.log;

import org.xml.sax.SAXException;
import org.xml.sax.helpers.AttributesImpl;

import soars.common.utility.xml.sax.Writer;

/**
 * The log informations.
 * @author kurata / SOARS project
 */
public class LogOption {

	/**
	 * Log name.
	 */
	public String _name = "";

	/**
	 * Flag which indocates whether to export.
	 */
	public boolean _flag = false;

	/**
	 * Creates this object.
	 */
	public LogOption() {
		super();
	}

	/**
	 * Creates this object with the specified data.
	 * @param name the specified name
	 */
	public LogOption(String name) {
		super();
		_name = name;
	}

	/**
	 * Creates this object with the specified data.
	 * @param name the specified name
	 * @param flag the specified flag
	 */
	public LogOption(String name, boolean flag) {
		super();
		_name = name;
		_flag = flag;
	}

	/**
	 * Clears all.
	 */
	public void cleanup() {
		_name = "";
		_flag = false;
	}

	/**
	 * @param tag
	 * @param writer
	 * @throws SAXException
	 */
	public void write(String tag, Writer writer) throws SAXException {
		AttributesImpl attributesImpl = new AttributesImpl();
		attributesImpl.addAttribute( null, null, "name", "", Writer.escapeAttributeCharData( _name));
		attributesImpl.addAttribute( null, null, "flag", "", _flag ? "true" : "false");
		writer.writeElement( null, null, tag, attributesImpl);
	}
}
