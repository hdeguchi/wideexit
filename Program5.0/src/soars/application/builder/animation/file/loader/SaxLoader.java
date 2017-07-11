/*
 * 2005/04/22
 */
package soars.application.builder.animation.file.loader;

import java.io.File;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import soars.application.builder.animation.document.Document;
import soars.application.builder.animation.document.DocumentManager;
import soars.application.builder.animation.main.MainFrame;

/**
 * @author kurata
 */
public class SaxLoader extends DefaultHandler {


	/**
	 * 
	 */
	private Document _document = null;


	/**
	 * 
	 */
	private String _value = "";


	/**
	 * 
	 */
	static private boolean _result;

	/**
	 * @param file
	 * @return
	 */
	public static boolean execute(File file) {
		_result = false;

		SaxLoader saxLoader = new SaxLoader();

		try {
			SAXParserFactory saxParserFactory = SAXParserFactory.newInstance();
			SAXParser saxParser = saxParserFactory.newSAXParser();
			saxParser.parse( file, saxLoader);
			saxLoader.at_end_of_load();
		} catch (Exception e) {
			e.printStackTrace();
			saxLoader.at_end_of_load();
			return false;
		}
		return _result;
	}

	/**
	 * 
	 */
	private void at_end_of_load() {
		if ( !_result)
			return;

	}

	/**
	 * 
	 */
	public SaxLoader() {
		super();
	}

	/* (Non Javadoc)
	 * @see org.xml.sax.helpers.DefaultHandler#startElement(java.lang.String, java.lang.String, java.lang.String, org.xml.sax.Attributes)
	 */
	public void startElement( String arg0, String arg1, String arg2, Attributes arg3) throws SAXException {

		super.startElement(arg0, arg1, arg2, arg3);

		if ( arg2.equals( "animator_builder_data")) {
			_result = true;
			return;
		}

		if ( !_result)
			return;

		if ( arg2.equals( "language")) {
			on_language( arg3);
		} else if ( arg2.equals( "title")) {
			on_title( arg3);
		} else if ( arg2.equals( "comment")) {
			on_comment( arg3);
		} else if ( arg2.equals( "selected_language")) {
			on_selected_language( arg3);
		} else if ( arg2.equals( "animator_file")) {
			on_animator_file( arg3);
		}
	}

	/**
	 * @param attributes
	 */
	private void on_language(Attributes attributes) {
		String language = attributes.getValue( "name");
		if ( null == language || language.equals( "")) {
			_result = false;
			return;
		}

		_document = new Document( language);
	}

	/**
	 * @param attributes
	 */
	private void on_title(Attributes attributes) {
		if ( null == _document) {
			_result = false;
			return;
		}

		_value = "";
	}

	/**
	 * @param attributes
	 */
	private void on_comment(Attributes attributes) {
		if ( null == _document) {
			_result = false;
			return;
		}

		_value = "";
	}

	/**
	 * @param attributes
	 */
	private void on_selected_language(Attributes attributes) {
		_value = "";
	}

	/**
	 * @param attributes
	 */
	private void on_animator_file(Attributes attributes) {
		_value = "";
	}

	/* (Non Javadoc)
	 * @see org.xml.sax.helpers.DefaultHandler#characters(char[], int, int)
	 */
	public void characters(char[] arg0, int arg1, int arg2) throws SAXException {
		if ( !_result)
			return;

		String value = new String( arg0, arg1, arg2);
		_value += value;
	}

	/* (Non Javadoc)
	 * @see org.xml.sax.helpers.DefaultHandler#endElement(java.lang.String, java.lang.String, java.lang.String)
	 */
	public void endElement(String arg0, String arg1, String arg2) throws SAXException {

		if ( !_result) {
			super.endElement(arg0, arg1, arg2);
			return;
		}

		if ( arg2.equals( "language")) {
			on_language();
		} else if ( arg2.equals( "title")) {
			on_title();
		} else if ( arg2.equals( "comment")) {
			on_comment();
		} else if ( arg2.equals( "selected_language")) {
			on_selected_language();
		} else if ( arg2.equals( "animator_file")) {
			on_animator_file();
		}

		super.endElement(arg0, arg1, arg2);
	}

	/**
	 * 
	 */
	private void on_language() {
		if ( null == _document) {
			_result = false;
			return;
		}

		Document document = DocumentManager.get_instance().get_document( _document._language);
		if ( null == document) {
			_result = false;
			return;
		}

		document._title = _document._title;
		document._comment = _document._comment;

		_document = null;
	}

	/**
	 * 
	 */
	private void on_title() {
		if ( null == _document) {
			_result = false;
			return;
		}

		_document._title = _value;
		_value = "";
	}

	/**
	 * 
	 */
	private void on_comment() {
		if ( null == _document) {
			_result = false;
			return;
		}

		_document._comment = _value;
		_value = "";
	}

	/**
	 * 
	 */
	private void on_selected_language() {
		MainFrame.get_instance().set_selected_language( _value);
		_value = "";
	}

	/**
	 * 
	 */
	private void on_animator_file() {
		MainFrame.get_instance().set_animator_file( _value);
		_value = "";
	}
}
