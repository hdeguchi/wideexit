/**
 * 
 */
package soars.application.simulator.main;

import java.io.File;
import java.io.StringReader;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import soars.common.utility.tool.file.ZipUtility;

/**
 * @author kurata
 *
 */
public class VisualShellSaxLoader extends DefaultHandler {

	/**
	 * 
	 */
	private String _title = "";

	/**
	 * @param soarsFilename
	 * @return
	 */
	public static String get_title(String soarsFilename) {
		File file = new File( soarsFilename);
		if ( !file.exists())
			return "";

		byte[] data = ZipUtility.get_binary( file, Constant._soarsRootDirectoryName + "/" + Constant._visualShellZipFileName);
		if ( null == data)
			return "";

		String text = ZipUtility.get_text( data, Constant._visualShellRootDirectoryName + "/" + Constant._visualShellDataFilename, "UTF8");
		if ( null == text)
			return "";

		VisualShellSaxLoader soarsSaxLoader = new VisualShellSaxLoader();

		try {
			SAXParserFactory saxParserFactory = SAXParserFactory.newInstance();
			SAXParser saxParser = saxParserFactory.newSAXParser();
			saxParser.parse( new InputSource( new StringReader( text)), soarsSaxLoader);
		} catch (Exception e) {
			e.printStackTrace();
			return "";
		}

		return soarsSaxLoader._title;
	}

	/**
	 * 
	 */
	public VisualShellSaxLoader() {
		super();
	}

	/* (non-Javadoc)
	 * @see org.xml.sax.helpers.DefaultHandler#startElement(java.lang.String, java.lang.String, java.lang.String, org.xml.sax.Attributes)
	 */
	@Override
	public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
		super.startElement(uri, localName, qName, attributes);

		if ( qName.equals( "comment_data"))
			on_comment_data( attributes);
	}

	/**
	 * @param attributes
	 */
	private void on_comment_data(Attributes attributes) {
		String value = attributes.getValue( "title");
		if ( null != value)
			_title = value;
	}

	/* (non-Javadoc)
	 * @see org.xml.sax.helpers.DefaultHandler#endElement(java.lang.String, java.lang.String, java.lang.String)
	 */
	@Override
	public void endElement(String uri, String localName, String qName) throws SAXException {
		if ( qName.equals( "comment_data"))
			on_comment_data();

		super.endElement(uri, localName, qName);
	}

	/**
	 * 
	 */
	private void on_comment_data() {
	}
}
