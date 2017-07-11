/**
 * 
 */
package soars.application.manager.model.common.tool;

import java.awt.Component;
import java.io.File;

import javax.swing.JFileChooser;

import soars.common.utility.tool.environment.EnvironmentBase;

/**
 * @author kurata
 *
 */
public class CommonTool {

	/**
	 * Returns the directory selected by the user.
	 * @param environmentBase
	 * @param directoryKey the key mapped to the default directory for the file chooser dialog
	 * @param defaultFolder
	 * @param title the title of the file chooser dialog
	 * @param component the parent component of the file chooser dialog
	 * @return the directory selected by the user
	 */
	public static File get_directory(EnvironmentBase environmentBase, String directoryKey, String title, Component component) {
		return get_directory( environmentBase, directoryKey, ( File)null, title, component);
	}

	/**
	 * Returns the directory selected by the user.
	 * @param environmentBase
	 * @param directoryKey the key mapped to the default directory for the file chooser dialog
	 * @param defaultFolder
	 * @param title the title of the file chooser dialog
	 * @param component the parent component of the file chooser dialog
	 * @return the directory selected by the user
	 */
	public static File get_directory(EnvironmentBase environmentBase, String directoryKey, String defaultFolder, String title, Component component) {
		return get_directory( environmentBase, directoryKey, new File( defaultFolder), title, component);
	}

	/**
	 * Returns the directory selected by the user.
	 * @param environmentBase
	 * @param directoryKey the key mapped to the default directory for the file chooser dialog
	 * @param title the title of the file chooser dialog
	 * @param defaultFolder
	 * @param component the parent component of the file chooser dialog
	 * @return the directory selected by the user
	 */
	public static File get_directory(EnvironmentBase environmentBase, String directoryKey, File defaultFolder, String title, Component component) {
		JFileChooser fileChooser = new JFileChooser();
		fileChooser.setDialogTitle( title);
		fileChooser.setFileSelectionMode( JFileChooser.DIRECTORIES_ONLY);

		File directory = null;
		String value = environmentBase.get( directoryKey, "");
		if ( null != value && !value.equals( ""))
			directory = new File( value);

		if ( null != directory && directory.exists() && directory.isDirectory()) {
			fileChooser.setCurrentDirectory( new File( directory.getAbsolutePath() + "/../"));
			fileChooser.setSelectedFile( directory);
		} else {
			if ( null != defaultFolder) {
				fileChooser.setCurrentDirectory( new File( defaultFolder.getAbsolutePath() + "/../"));
				fileChooser.setSelectedFile( defaultFolder);
			}
		}

		if ( JFileChooser.APPROVE_OPTION == fileChooser.showOpenDialog( component)) {
			directory = fileChooser.getSelectedFile();
			environmentBase.set( directoryKey, directory.getAbsolutePath());
			return directory;
		}

		return null;
	}
}
