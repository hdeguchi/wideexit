/*
 * 2005/07/06
 */
package soars.application.animator.observer;

import soars.application.animator.main.Administrator;
import soars.application.animator.main.Application;

/**
 * The observer for this application.
 * @author kurata / SOARS project
 */
public class Observer {

	/**
	 * 
	 */
	static private Object _lock = new Object();

	/**
	 * 
	 */
	static private Observer _observer = null;

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
			if ( null == _observer) {
				_observer = new Observer();
			}
		}
	}

	/**
	 * Returns the instance of the observer for this application.
	 * @return the instance of the observer for this application
	 */
	public static Observer get_instance() {
		if ( null == _observer) {
			System.exit( 0);
		}

		return _observer;
	}

	/**
	 * Creates the instance of the observer for this application.
	 */
	public Observer() {
		super();
	}

	/**
	 * Invoked when the data is modified.
	 */
	public void modified() {
		if ( Application._demo)
			return;

		Administrator.get_instance().modified( true);
	}
}
