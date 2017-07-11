/*
 * 2005/01/31
 */
package soars.application.manager.library.main;

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
	static public final String _module_file_manager_divider_location1_key = "Module.file.manager.divider.location1";

	/**
	 * 
	 */
	static public final String _module_file_manager_divider_location2_key = "Module.file.manager.divider.location2";

	/**
	 * 
	 */
	static public final String _file_manager_divider_location1_key = "File.manager.divider.location1";

	/**
	 * 
	 */
	static public final String _file_manager_divider_location2_key = "File.manager.divider.location2";

	/**
	 * 
	 */
	static public final String _file_editor_window_rectangle_key = "File.editor.rectangle.";

	/**
	 * 
	 */
	static public final String _edit_module_dialog_rectangle_key = "Edit.module.dialog.rectangle.";

	/**
	 * 
	 */
	static public final String _export_files_directory_key = "Directory.export.files";

	/**
	 * 
	 */
	static public String _annotationFrameRectangleKey = "AnnotationWindow.window.rectangle.";

	/**
	 * 
	 */
	static public String _annotation_pane_divider_location1_key = "Annotation.pane.divider.location1;";

	/**
	 * 
	 */
	static public String _annotation_pane_divider_location2_key = "Annotation.pane.divider.location2;";

	/**
	 * 
	 */
	static public String _warningDialogRectangleKey1="Warning.dialog1.rectangle.";

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
				+ "library" + File.separator
				+ "environment" + File.separator,
			"environment.properties",
			"SOARS Library Manager properties");
	}
}
