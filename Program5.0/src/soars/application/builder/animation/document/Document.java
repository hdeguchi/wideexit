/**
 * 
 */
package soars.application.builder.animation.document;

import org.json.JSONArray;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.AttributesImpl;

import soars.application.builder.animation.main.Constant;
import soars.common.soars.environment.SoarsCommonEnvironment;
import soars.common.utility.xml.sax.Writer;

/**
 * @author kurata
 *
 */
public class Document {

	/**
	 * 
	 */
	public String _language = "";

	/**
	 * 
	 */
	public String _language_name = "";

	/**
	 * 
	 */
	public String _title = "";

	/**
	 * 
	 */
	public String _comment = "";

	/**
	 * @param language
	 */
	public Document(String[] language) {
		super();
		_language = language[ 0];
		_language_name = language[ 1];
	}

	/**
	 * @param language
	 */
	public Document(String language) {
		super();
		_language = language;
	}

	/**
	 * 
	 */
	public void reset() {
		_title = "";
		_comment = "";
	}

	/**
	 * @param jsonArray
	 * @return
	 */
	public boolean write(JSONArray jsonArray) {
		jsonArray.put( _language);
		jsonArray.put( _title);
		jsonArray.put( _comment);
		jsonArray.put( SoarsCommonEnvironment.get_instance().get( SoarsCommonEnvironment._versionKey, "SOARS"));
		jsonArray.put( Constant._applicationVersion);
		jsonArray.put( SoarsCommonEnvironment.get_instance().get( SoarsCommonEnvironment._copyrightKey, "Copyright (c) 2003-???? SOARS Project."));
		return true;
	}

	/**
	 * @param writer
	 * @return
	 * @throws SAXException 
	 */
	public boolean write(Writer writer) throws SAXException {
		AttributesImpl attributesImpl = new AttributesImpl();
		attributesImpl.addAttribute( null, null, "name", "", Writer.escapeAttributeCharData( _language));

		writer.startElement( null, null, "language", attributesImpl);

		writer.startElement( null, null, "title", new AttributesImpl());
		writer.characters( _title.toCharArray(), 0, _title.length());
		writer.endElement( null, null, "title");

		writer.startElement( null, null, "comment", new AttributesImpl());
		writer.characters( _comment.toCharArray(), 0, _comment.length());
		writer.endElement( null, null, "comment");

		writer.endElement( null, null, "language");

		return true;
	}
}
