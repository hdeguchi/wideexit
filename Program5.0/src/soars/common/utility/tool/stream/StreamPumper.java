/*
 * Created on 2006/02/26
 */
package soars.common.utility.tool.stream;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;

/**
 * @author kurata
 */
public class StreamPumper extends Thread {

	/**
	 * 
	 */
	private InputStream _inputStream = null;

	/**
	 * 
	 */
	private PrintStream _printStream = null;

	/**
	 * 
	 */
	private boolean _save = false;

	/**
	 * 
	 */
	public String _text = "";

	/**
	 * @param inputStream
	 * @param save
	 */
	public StreamPumper(InputStream inputStream, boolean save) {
		super();
		_inputStream = inputStream;
		_save = save;
	}

	/**
	 * @param inputStream
	 * @param printStream
	 */
	public StreamPumper(InputStream inputStream, PrintStream printStream) {
		super();
		_inputStream = inputStream;
		_printStream = printStream;
	}

	/* (Non Javadoc)
	 * @see java.lang.Runnable#run()
	 */
	public void run() {
		if ( null == _printStream) {
			try {
				while ( null != this) {
					int c = _inputStream.read();
					if ( -1 == c)
						break;

					if ( _save)
						_text += ( char)c;
				}

				//System.out.println( _text);
			} catch (IOException e) {
				//e.printStackTrace();
			}
		} else {
			try {
				while ( null != this) {
					int c = _inputStream.read();
					if ( -1 == c)
						break;

					_printStream.print( ( char)c);
				}
			} catch (IOException e) {
				//e.printStackTrace();
			}
		}
	}
}
