/*
 * 2005/06/10
 */
package soars.common.utility.swing.text;

import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.PlainDocument;

/**
 * @author kurata
 */
public class TextExcluder extends PlainDocument {

	/**
	 * 
	 */
	private String _string = "";

	/**
	 * @param string
	 */
	public TextExcluder(String string) {
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
			if ( 0 > _string.indexOf( text[ i]))
				result += text[ i];
		}

		super.insertString(arg0, result, arg2);
	}
}
