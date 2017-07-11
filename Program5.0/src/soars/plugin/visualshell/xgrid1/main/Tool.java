/*
 * Created on 2006/06/19
 */
package soars.plugin.visualshell.xgrid1.main;

import java.awt.Component;
import java.awt.Frame;
import java.io.File;

import javax.swing.JFileChooser;

import soars.common.utility.swing.tool.ExampleFileFilter;
import soars.common.utility.tool.ssh2.filechooser.SftpFileChooser;
import soars.common.utility.tool.ssh2.SshTool2;

/**
 * @author kurata
 */
public class Tool {

	/**
	 * @param filename
	 * @param title
	 * @param extensions
	 * @param description
	 * @param component
	 * @return
	 */
	public static File get_file(String filename, String title, String[] extensions, String description, Component component) {
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

		File file = new File( filename);
		File directory = file.getParentFile();

		if ( null != directory && directory.exists())
			fileChooser.setCurrentDirectory( directory);

		if ( null != file && file.exists())
			fileChooser.setSelectedFile( file);

		if ( JFileChooser.APPROVE_OPTION == fileChooser.showOpenDialog( component))
			return fileChooser.getSelectedFile();

		return null;
	}

	/**
	 * @param directoryName
	 * @param title
	 * @param component
	 * @return
	 */
	public static File get_directory(String directoryName, String title, Component component) {
		JFileChooser fileChooser = new JFileChooser();
		fileChooser.setDialogTitle( title);
		fileChooser.setFileSelectionMode( JFileChooser.DIRECTORIES_ONLY);

		if ( null != directoryName && !directoryName.equals( "")) {
			File directory = new File( directoryName);
			if ( directory.exists()) {
				fileChooser.setCurrentDirectory( new File( directoryName + "/../"));
				fileChooser.setSelectedFile( new File( directoryName));
			}
		}

		if ( JFileChooser.APPROVE_OPTION == fileChooser.showOpenDialog( component))
			return fileChooser.getSelectedFile();

		return null;
	}

	/**
	 * @param directoryName
	 * @param title
	 * @param frame
	 * @param component
	 * @param host
	 * @param username
	 * @param keyFilename
	 * @param rootDirectory
	 * @return
	 */
	public static String get_remote_directory(String directoryName, String title, Frame frame, Component component, String host, String username, String keyFilename, String rootDirectory) {
		if ( null != directoryName && !directoryName.equals( ""))
			directoryName = ( directoryName.startsWith( rootDirectory) && SshTool2.directory_exists( host, username, new File( keyFilename), directoryName) ? directoryName : rootDirectory);
		else
			directoryName = rootDirectory;

		SftpFileChooser sftpFileChooser = new SftpFileChooser( frame, title, host, username, keyFilename, directoryName, rootDirectory, rootDirectory);
		sftpFileChooser.setFileSelectionMode( SftpFileChooser.DIRECTORIES_ONLY);

		if ( !sftpFileChooser.do_modal( component))
			return null;

		directoryName = sftpFileChooser._selectedFile;

		return directoryName;
	}
}
