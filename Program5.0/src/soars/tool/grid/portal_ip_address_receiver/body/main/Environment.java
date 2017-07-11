/*
 * 2005/01/31
 */
package soars.tool.grid.portal_ip_address_receiver.body.main;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import soars.common.utility.tool.environment.EnvironmentBase;

/**
 * @author kurata
 */
public class Environment extends EnvironmentBase {


	/**
	 * 
	 */
	static public final String _window_rectangle_key = "MainWindow.window.rectangle.";



	/**
	 * 
	 */
	static private Object _lock = new Object();


	/**
	 * 
	 */
	static private Environment _environment = null;


	/**
	 * 
	 */
	static {
		try {
			startup();
		} catch (FileNotFoundException e) {
			throw new RuntimeException( e);
		} catch (IOException e) {
			throw new RuntimeException( e);
		}
	}

	/**
	 * 	
	 */
	private static void startup() throws FileNotFoundException, IOException {
		synchronized( _lock) {
			if ( null == _environment) {
				_environment = new Environment();
				if ( !_environment.initialize())
					System.exit( 0);
			}
		}
	}

	/**
	 * @return
	 */
	public static EnvironmentBase get_instance() {
		if ( null == _environment) {
			System.exit( 0);
		}

		return _environment;
	}

	/**
	 * 
	 */
	public Environment() {
		super(
			System.getProperty( Constant._soarsProperty) + File.separator
				+ "utility" + File.separator
				+ "grid" + File.separator
				+ "portal_ip_address_receiver" + File.separator
				+ "environment" + File.separator,
			"environment.properties",
			"SOARS Grid portal IP address receiver properties");
	}
}
