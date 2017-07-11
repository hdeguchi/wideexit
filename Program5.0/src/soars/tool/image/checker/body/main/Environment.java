/*
 * 2005/01/31
 */
package soars.tool.image.checker.body.main;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import soars.common.utility.tool.environment.EnvironmentBase;

/**
 * The local properties maintenance class.
 * @author kurata / SOARS project
 */
public class Environment extends EnvironmentBase {

	/**
	 * Key mapped to the position and size of the main window.
	 */
	static public final String _main_window_rectangle_key = "MainWindow.window.rectangle.";

	/**
	 * Key mapped to the default directory for the file load.
	 */
	static public final String _open_directory_key = "Directory.open";

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
	 * Returns the instance of this class.
	 * @return the instance of this class
	 */
	public static Environment get_instance() {
		if ( null == _environment) {
			System.exit( 0);
		}

		return _environment;
	}

	/**
	 * Creates the local properties maintenance class.
	 */
	public Environment() {
		super(
			System.getProperty( Constant._soarsProperty) + File.separator
				+ "utility" + File.separator
				+ "image" + File.separator
				+ "checker" + File.separator
				+ "environment" + File.separator,
			"environment.properties",
			"SOARS Image checker properties");
	}
}
