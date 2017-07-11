/**
 * 
 */
package soars.application.manager.model.file;

import java.awt.Component;
import java.io.File;

import soars.application.manager.model.main.Constant;
import soars.common.soars.tool.SoarsCommonTool;
import soars.common.utility.tool.file.FileUtility;
import soars.common.utility.tool.file.ZipUtility;

/**
 * @author kurata
 *
 */
public class SoarsDataManager {

	/**
	 * 
	 */
	private File _folder = null;

	/**
	 * 
	 */
	private VisualShell _visualShell = null;

	/**
	 * 
	 */
	private File _parentDirectory = null;

	/**
	 * 
	 */
	private File _rootDirectory = null;

	/**
	 * @param folder
	 * @param title
	 * @param date
	 * @param author
	 * @param email
	 * @param comment
	 * @param mainPanel
	 * @return
	 */
	public static File create(File folder, String title, String date, String author, String email, String comment, Component mainPanel) {
		SoarsDataManager soarsDataManager = new SoarsDataManager( folder, new VisualShell( title, date, author, email, comment));
		return soarsDataManager.make( mainPanel);
	}

	/**
	 * @param folder
	 * @param visualShell
	 */
	public SoarsDataManager(File folder, VisualShell visualShell) {
		super();
		_folder = folder;
		_visualShell = visualShell;
	}

	/**
	 * @param mainPanel
	 * @return
	 */
	private File make(Component mainPanel) {
		if ( !setup_work_directory())
			return null;

		if ( !_visualShell.make( new File( _rootDirectory, Constant._visualShellZipFileName), mainPanel)) {
			FileUtility.delete( _parentDirectory, true);
			return null;
		}

		File file = create_new_file();
		if ( !ZipUtility.compress( file, _rootDirectory, _parentDirectory)) {
			FileUtility.delete( _parentDirectory, true);
			return null;
		}

		FileUtility.delete( _parentDirectory, true);

		return file;
	}

	/**
	 * @return
	 */
	private boolean setup_work_directory() {
		if ( null != _parentDirectory)
			return true;

		File parentDirectory = SoarsCommonTool.make_parent_directory();
		if ( null == parentDirectory)
			return false;

		File rootDirectory = new File( parentDirectory, Constant._soarsRootDirectoryName);
		if ( !rootDirectory.mkdirs()) {
			FileUtility.delete( parentDirectory, true);
			return false;
		}

		_parentDirectory = parentDirectory;
		_rootDirectory = rootDirectory;

		return true;
	}

	/**
	 * @return
	 */
	private File create_new_file() {
		int index = 1;
		while ( true) {
			File file = new File( _folder, "file" + String.valueOf( index++) + ".soars");
			if ( !file.exists())
				return file;
		}
	}
}
