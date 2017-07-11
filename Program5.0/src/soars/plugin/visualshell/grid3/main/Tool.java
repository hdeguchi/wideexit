/*
 * Created on 2006/06/19
 */
package soars.plugin.visualshell.grid3.main;

import java.awt.Component;
import java.awt.Frame;
import java.io.File;

import javax.swing.JFileChooser;

import soars.common.utility.swing.tool.ExampleFileFilter;
import soars.common.utility.tool.ssh.filechooser.SftpFileChooser;
import soars.common.utility.tool.ssh.SshTool;

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
	 * @param account
	 * @param password
	 * @param rootDirectory
	 * @param username
	 * @return
	 */
	public static String get_remote_directory(String directoryName, String title, Frame frame, Component component, String host, String account, String password, String rootDirectory, String username) {
		if ( null != directoryName && !directoryName.equals( ""))
			directoryName = ( SshTool.directory_exists( host, account, password, directoryName)
				? directoryName : rootDirectory + "/" + username);
		else
			directoryName = rootDirectory + "/" + username;

		SftpFileChooser sftpFileChooser = new SftpFileChooser( frame, title, host, account, password,
			directoryName, rootDirectory + "/" + username, rootDirectory + "/" + username);
		sftpFileChooser.setFileSelectionMode( SftpFileChooser.DIRECTORIES_ONLY);

		if ( !sftpFileChooser.do_modal( component))
			return null;

		directoryName = sftpFileChooser._selectedFile;

		return directoryName;
	}
}
