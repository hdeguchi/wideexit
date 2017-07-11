/**
 * 
 */
package soars.application.simulator.common.tool;

import java.awt.Component;
import java.io.File;

import javax.swing.JFileChooser;

import soars.application.simulator.main.Environment;
import soars.common.utility.swing.tool.ExampleFileFilter;

/**
 * @author kurata
 *
 */
public class CommonTool {

	/**
	 * @param openDirectoryKey
	 * @param title
	 * @param extensions
	 * @param description
	 * @param component
	 * @return
	 */
	public static File get_open_file(String openDirectoryKey, String title, String[] extensions, String description, Component component) {
		String openDirectory = "";

		String value = Environment.get_instance().get( openDirectoryKey, "");
		if ( null != value && !value.equals( "")) {
			File directory = new File( value);
			if ( directory.exists())
				openDirectory = value;
		}

		JFileChooser fileChooser = new JFileChooser();
		fileChooser.setDialogTitle( title);

		if ( null != extensions || null != description) {
			ExampleFileFilter filter = new ExampleFileFilter();

			if ( null != extensions) {
				for ( int i = 0; i < extensions.length; ++i)
					filter.addExtension( extensions[ i]);
			}

			if ( null != description)
				filter.setDescription( description);

			fileChooser.setFileFilter( filter);
		}

		if ( !openDirectory.equals( ""))
			fileChooser.setCurrentDirectory( new File( openDirectory));

		if ( JFileChooser.APPROVE_OPTION == fileChooser.showOpenDialog( component)) {
			File file = fileChooser.getSelectedFile();

			File directory = fileChooser.getCurrentDirectory();
			openDirectory = directory.getAbsolutePath();

			Environment.get_instance().set( openDirectoryKey, openDirectory);

			return file;
		}

		return null;
	}

	/**
	 * @param saveDirectoryKey
	 * @param title
	 * @param extensions
	 * @param description
	 * @param component
	 * @return
	 */
	public static File get_save_file(String saveDirectoryKey, String title, String[] extensions, String description, Component component) {
		String saveDirectory = "";

		String value = Environment.get_instance().get( saveDirectoryKey, "");
		if ( null != value && !value.equals( "")) {
			File directory = new File( value);
			if ( directory.exists())
				saveDirectory = value;
		}

		JFileChooser fileChooser = new JFileChooser();
		fileChooser.setDialogTitle( title);

		if ( null != extensions || null != description) {
			ExampleFileFilter filter = new ExampleFileFilter();

			if ( null != extensions) {
				for ( int i = 0; i < extensions.length; ++i)
					filter.addExtension( extensions[ i]);
			}

			if ( null != description)
				filter.setDescription( description);

			fileChooser.setFileFilter( filter);
		}

		if ( !saveDirectory.equals( ""))
			fileChooser.setCurrentDirectory( new File( saveDirectory));

		if ( JFileChooser.APPROVE_OPTION == fileChooser.showSaveDialog( component)) {
			File file = fileChooser.getSelectedFile();

			File directory = fileChooser.getCurrentDirectory();
			saveDirectory = directory.getAbsolutePath();

			Environment.get_instance().set( saveDirectoryKey, saveDirectory);

			return file;
		}

		return null;
	}
}
