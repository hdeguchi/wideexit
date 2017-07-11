/**
 * 
 */
package soars.application.visualshell.common.file;

import java.io.File;

import soars.application.visualshell.layer.LayerManager;
import soars.application.visualshell.main.Constant;
import soars.application.visualshell.main.Environment;
import soars.common.utility.tool.file.FileUtility;

/**
 * The script file class.
 * @author kurata / SOARS project
 */
public class ScriptFile {

	/**
	 * Name of the experiment.
	 */
	public String _experimentName = "";

	/**
	 * Directory for the script file.
	 */
	public File _path = null;

	/**
	 * Name of the directory for the script file.
	 */
	public String _logFolderPath = "";

	/**
	 * Creates the instance of the script file class with the specified directory for the script file.
	 * @param path the specified directory for the script file
	 */
	public ScriptFile(File path) {
		super();
		_path = path;
	}

	/**
	 * Creates the instance of the script file class with the specified data.
	 * @param experimentName the specified name of the experiment
	 * @param path the specified directory for the script file
	 */
	public ScriptFile(String experimentName, File path) {
		super();
		_experimentName = experimentName;
		_path = path;
	}

	/**
	 * Creates the instance of the script file class with the specified data.
	 * @param path the specified directory for the script file
	 * @param logFolderPath the specified name of the directory for the script file
	 */
	public ScriptFile(File path, String logFolderPath) {
		super();
		_path = path;
		_logFolderPath = logFolderPath;
	}

	/**
	 * Creates the instance of the script file class with the specified data.
	 * @param experimentName the specified name of the experiment
	 * @param path the specified directory for the script file
	 * @param logFolderPath the specified name of the directory for the script file
	 */
	public ScriptFile(String experimentName, File path, String logFolderPath) {
		super();
		_experimentName = experimentName;
		_path = path;
		_logFolderPath = logFolderPath;
	}

	/**
	 * @param rootDirectory
	 * @return
	 */
	public boolean update(File rootDirectory) {
		if ( !LayerManager.get_instance().exist_user_data_directory()
			&& !LayerManager.get_instance().exist_user_rule_jarFiles_directory())
			return true;

		String script = FileUtility.read_text_from_file( _path);
		if ( null == script)
			return false;

		if ( LayerManager.get_instance().exist_user_data_directory()) {
			File directory = new File( rootDirectory, Constant._userDataDirectory);
			if ( null == directory)
				return false;

			String userDataDirectory = ( directory.getAbsolutePath().replaceAll( "\\\\", "/") + "/");
			while ( 0 <= script.indexOf( Constant._reservedUserDataDirectory))
				script = script.replace( Constant._reservedUserDataDirectory, userDataDirectory);
		}

		if ( LayerManager.get_instance().exist_user_rule_jarFiles_directory()) {
			File directory = new File( rootDirectory, Constant._userRuleJarFilesInternalRelativePathName);
			if ( null == directory)
				return false;

			String userRuleJarFilesFolder = ( directory.getAbsolutePath().replaceAll( "\\\\", "/")/* + "/"*/);
			while ( 0 <= script.indexOf( Constant._reservedUserRuleJarFilesInternalDirectory))
				script = script.replace( Constant._reservedUserRuleJarFilesInternalDirectory, userRuleJarFilesFolder);
		}

		return FileUtility.write_text_to_file( _path, script);
	}

	/**
	 * @return
	 */
	public boolean cleanup() {
		return _path.delete();
	}

	/**
	 * 
	 */
	public void clear_work_directory() {
		File rootDirectory = new File( _logFolderPath);
		if ( !rootDirectory.exists() || !rootDirectory.isDirectory())
			return;

		if ( !Environment.get_instance().get( Environment._editExportSettingDialogToFileKey, "false").equals( "true")) {
			if ( !Environment.get_instance().get( Environment._editExportSettingDialogKeepUserDataFileKey, "false").equals( "true"))
				FileUtility.delete( rootDirectory, true);
		} else {
			if ( Environment.get_instance().get( Environment._editExportSettingDialogKeepUserDataFileKey, "false").equals( "true"))
				return;

			File directory = new File( rootDirectory, Constant._userDataDirectory);
			if ( directory.exists() && directory.isDirectory())
				FileUtility.delete( directory, true);

			directory = new File( rootDirectory, Constant._gamingDataDirectory);
			if ( directory.exists() && directory.isDirectory())
				FileUtility.delete( directory, true);

			directory = new File( rootDirectory, Constant._userRuleJarFilesInternalRelativePathName);
			if ( directory.exists() && directory.isDirectory())
				FileUtility.delete( directory, true);
		}
	}
}
