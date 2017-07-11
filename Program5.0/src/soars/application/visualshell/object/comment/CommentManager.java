/*
 * 2005/06/04
 */
package soars.application.visualshell.object.comment;

import java.util.Calendar;
import java.util.Iterator;
import java.util.Map;

import org.xml.sax.SAXException;
import org.xml.sax.helpers.AttributesImpl;

import soars.application.visualshell.main.Constant;
import soars.application.visualshell.main.ResourceManager;
import soars.common.utility.xml.sax.Writer;

/**
 * Manages the comments.
 * @author kurata / SOARS project
 */
public class CommentManager {

	/**
	 * Title of this Visual Shell data.
	 */
	public String _title = "";

	/**
	 * Creation date.
	 */
	public String _date = "";

	/**
	 * Author of this Visual Shell data.
	 */
	public String _author = "";

	/**
	 * Author's email.
	 */
	public String _email = "";

	/**
	 * Comment of this Visual Shell data.
	 */
	public String _comment = "";
//	public boolean _export = false;

	/**
	 * 
	 */
	static private Object _lock = new Object();

	/**
	 * 
	 */
	static private CommentManager _commentManager = null;

	/**
	 * 
	 */
	static {
		startup();
	}

	/**
	 * 
	 */
	private static void startup() {
		synchronized( _lock) {
			if ( null == _commentManager) {
				_commentManager = new CommentManager();
			}
		}
	}

	/**
	 * Returns the instance of this object.
	 * @return the instance of this object
	 */
	public static CommentManager get_instance() {
		if ( null == _commentManager) {
			System.exit( 0);
		}

		return _commentManager;
	}

	/**
	 * Creates this object.
	 */
	public CommentManager() {
		super();
		_date = get_date();
	}

	/**
	 * Clears all.
	 */
	public void cleanup() {
		_title = "";
		_date = get_date();
		_author = "";
		_email = "";
		_comment = "";
//		_export = false;
	}

	/**
	 * @return
	 */
	private String get_date() {
		return ( String.valueOf( Calendar.getInstance().get( Calendar.YEAR)) + "."
			+ String.valueOf( Calendar.getInstance().get( Calendar.MONTH) + 1) + "."
			+ String.valueOf( Calendar.getInstance().get( Calendar.DATE)));
	}

	/**
	 * Returns true for updating the data of this object.
	 * @param commentDataMap the data hashtable.
	 * @return true for updating the data of this object
	 */
	public boolean update(Map<String, String> commentDataMap) {
		Iterator iterator = commentDataMap.entrySet().iterator();
		while ( iterator.hasNext()) {
			Object object = iterator.next();
			Map.Entry entry = ( Map.Entry)object;
			String key = ( String)entry.getKey();
			String value = ( String)entry.getValue();
			if ( key.equals( ResourceManager.get_instance().get( "initial.data.comment.title")))
				_title = value;
			else if ( key.equals( ResourceManager.get_instance().get( "initial.data.comment.date")))
				_date = value;
			else if ( key.equals( ResourceManager.get_instance().get( "initial.data.comment.author")))
				_author = value;
			else if ( key.equals( ResourceManager.get_instance().get( "initial.data.comment.mail")))
				_email = value;
			else if ( key.equals( ResourceManager.get_instance().get( "initial.data.comment.comment")))
				_comment += value;
//			else if ( key.equals( ResourceManager.get_instance().get( "initial.data.comment.export")))
//				_export = value.equals( "true");
		}
		return true;
	}

	/**
	 * Returns the initial data of this object.
	 * @return the initial data of this object
	 */
	public String get_initial_data() {
		String script = "";
		script += ( _title.equals( "") ? ""
			: ( ResourceManager.get_instance().get( "initial.data.comment")
			+ "\t" + ResourceManager.get_instance().get( "initial.data.comment.title")
			+ "\t" + _title + Constant._lineSeparator));
		script += ( _date.equals( "") ? ""
			: ( ResourceManager.get_instance().get( "initial.data.comment")
			+ "\t" + ResourceManager.get_instance().get( "initial.data.comment.date")
			+ "\t" + _date + Constant._lineSeparator));
		script += ( _author.equals( "") ? ""
			: ( ResourceManager.get_instance().get( "initial.data.comment")
			+ "\t" + ResourceManager.get_instance().get( "initial.data.comment.author")
			+ "\t" + _author + Constant._lineSeparator));
		script += ( _email.equals( "") ? ""
			: ( ResourceManager.get_instance().get( "initial.data.comment")
			+ "\t" + ResourceManager.get_instance().get( "initial.data.comment.mail")
			+ "\t" + _email + Constant._lineSeparator));

		String comment[] = _comment.split( Constant._lineSeparator);
		for ( int i = 0; i < comment.length; ++i) {
			if ( comment[ i].equals( "") || comment[ i].matches( "[ ]+"))
				continue;

			script += ( ResourceManager.get_instance().get( "initial.data.comment")
				+ "\t" + ResourceManager.get_instance().get( "initial.data.comment.comment")
				+ "\t" + comment[ i] + Constant._lineSeparator);
		}
		
//		script += ( ResourceManager.get_instance().get( "initial.data.comment")
//			+ "\t" + ResourceManager.get_instance().get( "initial.data.comment.export")
//			+ "\t" + String.valueOf( _export) + Constant._line_separator);

		return ( script + Constant._lineSeparator);
	}

//	/**
//	 * @return
//	 */
//	public String get_script() {
//		String text1 = get_script1();
//		String text2 = get_script2();
//		String text = "";
//		if ( text1.equals( "")) {
//			if ( !_export || text2.equals( ""))
//				return "";
//			else
//				text = text2;
//		} else {
//			if ( !_export || text2.equals( ""))
//				text = text1;
//			else
//				text = ( text1 + Constant._line_separator + text2);
//		}
//
//		if ( text.equals( ""))
//			return "";
//
//		String script = "ignore" + Constant._line_separator;
//		script += ( text + Constant._line_separator);
//		return ( script + Constant._line_separator);
//	}

	/**
	 * @return
	 */
	private String get_script1() {
		String text = ( "Title : " + _title);
		text += ( Constant._lineSeparator + "Date  : " + _date);
		text += ( Constant._lineSeparator + "Author: " + _author);
		text += ( Constant._lineSeparator + "E-mail: " + _email);
		return text;
//		String text = "";
//
//		if ( !_title.equals( "") && !_title.matches( "[ ]+"))
//			text += ( ( text.equals( "") ? "" : Constant._line_separator)
//				+ "Title : " + _title);
//
//		if ( !_date.equals( "") && !_date.matches( "[ ]+"))
//			text += ( ( text.equals( "") ? "" : Constant._line_separator)
//				+ "Date  : " + _date);
//
//		if ( !_author.equals( "") && !_author.matches( "[ ]+"))
//			text += ( ( text.equals( "") ? "" : Constant._line_separator)
//				+ "Author: " + _author);
//
//		if ( !_email.equals( "") && !_email.matches( "[ ]+"))
//			text += ( ( text.equals( "") ? "" : Constant._line_separator)
//				+ "E-mail: " + _email);
//
//		return text;
	}

	/**
	 * @return
	 */
	private String get_script2() {
		String text = "";
		String comment[] = _comment.split( Constant._lineSeparator);
		for ( int i = 0; i < comment.length; ++i) {
			if ( comment[ i].equals( "") || comment[ i].matches( "[ ]+"))
				continue;

			text += ( ( text.equals( "") ? "" : Constant._lineSeparator)
				+ comment[ i]);
		}
		return text;
	}

	/**
	 * Returns true for writing this object data successfully.
	 * @param writer the abstract class for writing to character streams
	 * @return true for writing this object data successfully
	 * @throws SAXException encapsulate a general SAX error or warning
	 */
	public boolean write(Writer writer) throws SAXException {
		AttributesImpl attributesImpl = new AttributesImpl();
		attributesImpl.addAttribute( null, null, "title", "", Writer.escapeAttributeCharData( _title));
		attributesImpl.addAttribute( null, null, "date", "", Writer.escapeAttributeCharData( _date));
		attributesImpl.addAttribute( null, null, "author", "", Writer.escapeAttributeCharData( _author));
		attributesImpl.addAttribute( null, null, "email", "", Writer.escapeAttributeCharData( _email));
//		attributesImpl.addAttribute( null, null, "export", "", _export ? "true" : "false");
		writer.startElement( null, null, "comment_data", attributesImpl);

		writer.characters( _comment.toCharArray(), 0, _comment.length());

		writer.endElement( null, null, "comment_data");

		return true;
	}
}
