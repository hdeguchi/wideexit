/**
 * 
 */
package soars.plugin.modelbuilder.chart.chart.common.tool;

import java.awt.Component;
import java.io.File;

import javax.swing.JFileChooser;

import soars.common.utility.swing.tool.ExampleFileFilter;
import soars.plugin.modelbuilder.chart.chart.main.Environment;

/**
 * @author kurata
 *
 */
public class CommonTool {

	/**
	 * @param save_directory_key
	 * @param title
	 * @param extensions
	 * @param description
	 * @param component
	 * @return
	 */
	public static File get_save_file(String save_directory_key, String title, String[] extensions, String description, Component component) {
		String save_directory = "";

		String value = Environment.get_instance().get( save_directory_key, "");
		if ( null != value && !value.equals( "")) {
			File directory = new File( value);
			if ( directory.exists())
				save_directory = value;
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

		if ( !save_directory.equals( ""))
			fileChooser.setCurrentDirectory( new File( save_directory));

		if ( JFileChooser.APPROVE_OPTION == fileChooser.showSaveDialog( component)) {
			File file = fileChooser.getSelectedFile();

			File directory = fileChooser.getCurrentDirectory();
			save_directory = directory.getAbsolutePath();

			Environment.get_instance().set( save_directory_key, save_directory);

			return file;
		}

		return null;
	}
}
