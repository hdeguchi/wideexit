/*
 * 2005/01/31
 */
package soars.tool.animator.launcher.main;

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
	static public final String _main_window_rectangle_key = "MainWindow.window.rectangle.";

	/**
	 * 
	 */
	static public final String _memory_size_key = "Memory.size";

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
	public static void startup() throws FileNotFoundException, IOException {
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
	public static Environment get_instance() {
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
				+ "animator" + File.separator
				+ "launcher" + File.separator
				+ "environment" + File.separator,
			"environment.properties",
			"SOARS Animator Launcher properties");
	}
}
