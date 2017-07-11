/**
 * 
 */
package soars.application.simulator.stream;

import java.io.PrintStream;

/**
 * @author kurata
 *
 */
public class StdOutStreamPumper extends SystemOutStreamPumper {

	/**
	 * 
	 */
	public StdOutStreamPumper() {
		super(System.out);
	}

	/* (non-Javadoc)
	 * @see soars.application.simulator.stream.SystemStreamPumper#set(java.io.PrintStream)
	 */
	protected void set(PrintStream printStream) {
		System.setOut( printStream);
	}

	/* (non-Javadoc)
	 * @see soars.application.simulator.stream.SystemStreamPumper#cleanup()
	 */
	public synchronized void cleanup() {
		if ( null != _out) {
			System.setOut( _out);
			_out = null;
			//System.out.println( "Terminate!");
		}
	}
}
