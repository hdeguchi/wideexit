/*
 * 2005/03/11
 */
package soars.common.utility.swing.text;

import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.PlainDocument;

/**
 * @author kurata
 */
public class TextLimiter extends PlainDocument {

	/**
	 * 
	 */
	private String _string = "";

	/**
	 * @param string
	 */
	public TextLimiter(String string) {
		super();
		_string = string;
	}

	/* (Non Javadoc)
	 * @see javax.swing.text.Document#insertString(int, java.lang.String, javax.swing.text.AttributeSet)
	 */
	public void insertString(int arg0, String arg1, AttributeSet arg2)
		throws BadLocationException {
		if ( null == arg1 || arg1.equals( ""))
			 return;

		char[] text = arg1.toCharArray();
		String result = "";
		for ( int i = 0; i < text.length; ++i) {
			if ( -1 < _string.indexOf( text[ i]))
				result += text[ i];
		}

//		System.out.println( arg0 + ", " + arg1);
//
//		String text0 = "123456789";
//		String text1 = "0123456789";
//
//		char[] text = arg1.toCharArray();
//		String result = "";
//		for ( int i = 0; i < text.length; ++i) {
//			if ( 0 == arg0) {
//				if ( -1 < text0.indexOf( text[ i]))
//					result += text[ i];
//			} else {
//				if ( -1 < text1.indexOf( text[ i]))
//					result += text[ i];
//			}
//		}
		super.insertString(arg0, result, arg2);
	}
}
