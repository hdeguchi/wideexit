/**
 * 
 */
package soars.application.simulator.stream;

import java.io.PrintStream;

/**
 * @author kurata
 *
 */
public class StdErrOutStreamPumper extends SystemOutStreamPumper {

	/**
	 * 
	 */
	public StdErrOutStreamPumper() {
		super(System.err);
	}

	/* (non-Javadoc)
	 * @see soars.application.simulator.stream.SystemStreamPumper#set(java.io.PrintStream)
	 */
	protected void set(PrintStream printStream) {
		System.setErr( printStream);
	}

	/* (non-Javadoc)
	 * @see soars.application.simulator.stream.SystemStreamPumper#cleanup()
	 */
	public synchronized void cleanup() {
		if ( null != _out) {
			System.setErr( _out);
			_out = null;
			//System.err.println( "Terminate!");
		}
	}
}
