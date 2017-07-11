/*
 * 2005/01/31
 */
package soars.application.manager.model.main;

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
	static public final String _mainWindowRectangleKey = "MainWindow.window.rectangle.";

	/**
	 * 
	 */
	static public final String _mainPanelDividerLocation1key = "Main.panel.divider.location1";

	/**
	 * 
	 */
	static public final String _mainPanelDividerLocation2key = "Main.panel.divider.location2";

	/**
	 * 
	 */
	static public final String _soarsContentsPageDividerLocationKey = "Soars.contents.page.divider.location";

	/**
	 * 
	 */
	static public final String _exportFilesDirectoryKey = "Directory.export.files";

	/**
	 * 
	 */
	static public final String _userRuleImportDirectoryKey = "Directory.user.rule.import";

	/**
	 * 
	 */
	static public final String _userRuleExportDirectoryKey = "Directory.user.rule.export";

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
				+ "program" + File.separator
				+ "manager" + File.separator
				+ "model" + File.separator
				+ "environment" + File.separator,
			"environment.properties",
			"SOARS Model Manager properties");
	}

	/**
	 * 
	 */
	public void update() {
		_directory = System.getProperty( Constant._soarsProperty) + File.separator
			+ "program" + File.separator
			+ "manager" + File.separator
			+ "model" + File.separator
			+ "environment" + File.separator;
		File directory = new File( _directory);
		if ( !directory.exists())
			directory.mkdirs();
	}
}
