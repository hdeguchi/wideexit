/**
 * 
 */
package soars.application.visualshell.object.gis.edit.field.selector;

import java.util.ArrayList;
import java.util.List;

import org.xml.sax.SAXException;
import org.xml.sax.helpers.AttributesImpl;

import soars.common.utility.xml.sax.Writer;

/**
 * @author kurata
 *
 */
public class Field {

	/**
	 * true if _value is field data.
	 */
	public boolean _flag = true;

	/**
	 * 
	 */
	public String _value = "";

	/**
	 * @param value
	 */
	public Field(String value) {
		super();
		_value = value;
	}

	/**
	 * @param flag
	 * @param value
	 */
	public Field(boolean flag, String value) {
		super();
		_flag = flag;
		_value = value;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if ( !( obj instanceof Field))
			return false;

		Field field = ( Field)obj;
		return ( ( _flag == field._flag) && _value.equals( field._value));
	}

	/**
	 * @param fields
	 * @return
	 */
	public static String get(List<Field> fields) {
		String text = "";
		for ( Field field:fields)
			text += ( ( text.equals( "") ? "" : "+" ) + ( field._flag ? "" : "\"") + field._value + ( field._flag ? "" : "\""));
		return text;
	}

	/**
	 * @param value
	 * @return
	 */
	public static List<Field> get(String value) {
		List<Field> fields = new ArrayList<Field>();
		fields.add( new Field( true, value));
		return fields;
	}

	/**
	 * @param writer
	 * @return
	 * @throws SAXException
	 */
	public boolean write(Writer writer) throws SAXException {
		AttributesImpl attributesImpl = new AttributesImpl();
		attributesImpl.addAttribute( null, null, "flag", "", _flag ? "true" : "false");
		attributesImpl.addAttribute( null, null, "value", "", Writer.escapeAttributeCharData( _value));
		writer.writeElement( null, null, "field", attributesImpl);
		return true;
	}
}
