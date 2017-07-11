/*
 * 2005/06/22
 */
package soars.common.soars.warning;

import java.util.Vector;

import soars.common.utility.tool.clipboard.Clipboard;

/**
 * @author kurata
 */
public class WarningManager extends Vector<String[]> {

	/**
	 * 
	 */
	static private Object _lock = new Object();

	/**
	 * 
	 */
	static private WarningManager _warningManager = null;

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
			if ( null == _warningManager) {
				_warningManager = new WarningManager();
			}
		}
	}

	/**
	 * @return
	 */
	public static WarningManager get_instance() {
		if ( null == _warningManager) {
			System.exit( 0);
		}

		return _warningManager;
	}

	/**
	 * 
	 */
	public WarningManager() {
		super();
	}

	/**
	 * 
	 */
	public void cleanup() {
		clear();
	}

	/**
	 * @param separator
	 * @return
	 */
	public Vector<String> get(String separator) {
		Vector<String> messages = new Vector<String>();
		for ( int i = 0; i < size(); ++i) {
			String[] words = get( i);
			String message = "";
			for ( int j = 0; j < words.length; ++j)
				message += ( ( ( 0 == j) ? "" : separator) + words[ j]);

			if ( message.equals( ""))
				continue;

			messages.add( message);
		}
		return messages;
	}

	/**
	 * @param messages
	 * @return
	 */
	private String get(Vector<String> messages) {
		String string = "";
		for ( int i = 0; i < messages.size(); ++i)
			string += ( messages.get( i)  + "\n");

		return string;
	}

	/**
	 * @param message
	 * @return
	 */
	public boolean has(String[] message) {
		for ( int i = 0; i < size(); ++i) {
			String[] m = get( i);
			if ( m.length != message.length)
				continue;

			if ( !same( m, message))
				continue;

			return true;
		}
		return false;
	}

	/**
	 * @param message1
	 * @param message2
	 * @return
	 */
	private boolean same(String[] message1, String[] message2) {
		for ( int i = 0; i < message1.length; ++i) {
			if ( !message1[ i].equals( message2[ i]))
				return false;
		}
		return true;
	}

	/**
	 * 
	 */
	public void copy_to_clipboard() {
		Vector<String> messages = get( "\t");
		String string = get( messages);
		if ( string.equals( ""))
			return;

		Clipboard.set( string);
	}

	/**
	 * 
	 */
	public void print() {
		Vector<String> messages = get( ", ");
		String string = get( messages);
		if ( string.equals( ""))
			return;

		System.out.println( string);
	}
}
