/**
 * 
 */
package soars.application.simulator.stream;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;

import soars.application.simulator.main.Constant;

/**
 * @author kurata
 *
 */
public class SystemOutStreamPumper extends Thread {

	/**
	 * 
	 */
	public String _text = "";

	/**
	 * 
	 */
	protected PrintStream _out = null;

	/**
	 * @param out
	 */
	public SystemOutStreamPumper(PrintStream out) {
		super();
		_out = out;
	}

	/**
	 * 
	 */
	public synchronized void cleanup() {
	}

	/**
	 * @param printStream
	 */
	protected void set(PrintStream printStream) {
	}

	/* (non-Javadoc)
	 * @see java.lang.Thread#run()
	 */
	public void run() {
		PipedInputStream pipedInputStream = new PipedInputStream();
		PrintStream printStream = null;
		try {
			printStream = new PrintStream( new PipedOutputStream( pipedInputStream));
		} catch (IOException e) {
			e.printStackTrace();
			return;
		}

		set( printStream);

		BufferedReader bufferedReader;
		try {
			if ( 0 <= System.getProperty( "os.name").indexOf( "Mac")
				&& !System.getProperty( Constant._systemDefaultFileEncoding, "").equals( ""))
				bufferedReader = new BufferedReader( new InputStreamReader( pipedInputStream,
					System.getProperty( Constant._systemDefaultFileEncoding, "")));
			else
				bufferedReader = new BufferedReader( new InputStreamReader( pipedInputStream));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			cleanup();
			return;
		}

		while ( true) {
			String line = null;
			try {
				line = bufferedReader.readLine();
			} catch (IOException e) {
				//e.printStackTrace();
				break;
			}
			if ( null == line)
				break;

			_text += ( line + Constant._lineSeparator);
		}

		try {
			bufferedReader.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

		cleanup();
	}
}
