/*
 * 2004/12/17
 */
package soars.plugin.modelbuilder.chart.log_viewer.body.main;

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
	static public final String _open_directory_key = "Directory.open";

	/**
	 * 
	 */
	static public final String _save_image_directory_key = "Directory.save.image";

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
				+ "chart" + File.separator
				+ "log_viewer" + File.separator
				+ "environment" + File.separator,
			"environment.properties",
			"SOARS Chart LogViewer properties");
	}
}
