/*
 * 2005/05/16
 */
package soars.common.utility.xml.sax;

import java.io.IOException;
import java.io.OutputStreamWriter;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

/**
 * @author kurata
 */
public class Writer extends DefaultHandler {

	/**
	 * 
	 */
	protected OutputStreamWriter _outputStreamWriter = null;

	/**
	 * 
	 */
	protected StringBuffer _stringBuffer = null;

	/**
	 * @param outputStreamWriter
	 */
	public Writer(OutputStreamWriter outputStreamWriter) {
		super();
		_outputStreamWriter = outputStreamWriter;
	}

	/* (Non Javadoc)
	 * @see org.xml.sax.ContentHandler#startDocument()
	 */
	public void startDocument() throws SAXException {
		_stringBuffer = null;
	}

	/* (Non Javadoc)
	 * @see org.xml.sax.ContentHandler#startPrefixMapping(java.lang.String, java.lang.String)
	 */
	public void startPrefixMapping(String arg0, String arg1)
		throws SAXException {
		if ( null == _stringBuffer) {
			_stringBuffer = new StringBuffer();
		}
		if ( "".equals( arg0)) {
			_stringBuffer.append( " xmlns=\"");
			_stringBuffer.append( arg1);
			_stringBuffer.append( "\"");
		} else {
			_stringBuffer.append( " xmlns:");
			_stringBuffer.append( arg0);
			_stringBuffer.append( "=\"");
			_stringBuffer.append( arg1);
			_stringBuffer.append( "\"");
		}
	}

	/**
	 * @param arg0
	 * @param arg1
	 * @param arg2
	 * @param arg3
	 */
	public void writeElement(
		String arg0,
		String arg1,
		String arg2,
		Attributes arg3)
		throws SAXException {
		writeElement( arg2, arg3, "/>");
	}

	/* (Non Javadoc)
	 * @see org.xml.sax.ContentHandler#startElement(java.lang.String, java.lang.String, java.lang.String, org.xml.sax.Attributes)
	 */
	public void startElement(
		String arg0,
		String arg1,
		String arg2,
		Attributes arg3)
		throws SAXException {
		writeElement( arg2, arg3, ">");
	}

	/* (Non Javadoc)
	 * @see org.xml.sax.ContentHandler#endElement(java.lang.String, java.lang.String, java.lang.String)
	 */
	public void endElement(String arg0, String arg1, String arg2)
		throws SAXException {
		try {
			_outputStreamWriter.write( "</");
			_outputStreamWriter.write( arg2);
			_outputStreamWriter.write( '>');
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * @param arg2
	 * @param arg3
	 * @param termination
	 */
	private void writeElement(
		String arg2,
		Attributes arg3,
		String termination)
		throws SAXException {
		try {
			_outputStreamWriter.write( '<');
			_outputStreamWriter.write( arg2);
			if ( null != _stringBuffer) {
				_outputStreamWriter.write( new String( _stringBuffer));
				_stringBuffer = null;
			}
			int size = arg3.getLength();
			for ( int i = 0; i < size; i++) {
				_outputStreamWriter.write( ' ');
				_outputStreamWriter.write( arg3.getQName( i));
				_outputStreamWriter.write( "=\"");
				_outputStreamWriter.write( arg3.getValue( i));
				_outputStreamWriter.write( '\"');
			}
			_outputStreamWriter.write( termination);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/* (Non Javadoc)
	 * @see org.xml.sax.ContentHandler#characters(char[], int, int)
	 */
	public void characters(char[] arg0, int arg1, int arg2)
		throws SAXException {
		escapeCharData(arg0, arg1, arg2, _outputStreamWriter);
	}

	/**
	 * @param chars
	 * @param offset
	 * @param length
	 * @param outputStreamWriter
	 */
	private void escapeCharData(char[] chars, int offset, int length, OutputStreamWriter outputStreamWriter) {
		if ( null == chars)
			return;

		try {
			int last = offset + length;
			for (int i = offset; i < last; i++) {
				char c = chars[ i];
				if ( '<' == c) {
					outputStreamWriter.write( "&lt;");
				} else if ( '&' == c) {
					outputStreamWriter.write( "&amp;");
				} else if ( '>' == c) {
					outputStreamWriter.write( "&gt;");
				} else if ( ( 0x20 > c && ( 0x09 != c && 0x0a != c && 0x0d != c)) || 0x7f == c) {
					outputStreamWriter.write( " ");
				//} else if ( '"' == c) {
				//	outputStreamWriter.write( "&quot;");
				} else {
					outputStreamWriter.write( c);
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * @param string
	 * @return
	 */
	public static String escapeAttributeCharData(String string) {
		String result = "";

		if ( null == string)
			return result;

		char[] chars = string.toCharArray();
		for ( int i = 0; i < chars.length; ++i) {
			char c = chars[ i];
			if ( '<' == c) {
				result += "&lt;";
			} else if ( '&' == c) {
				result += "&amp;";
			} else if ( '>' == c) {
				result += "&gt;";
			} else if ( '"' == c) {
				result += "&quot;";
			} else if ( 0x20 > c || 0x7f == c) {
				result += " ";
			} else {
				result += c;
			}
		}
		return result;
	}
}
