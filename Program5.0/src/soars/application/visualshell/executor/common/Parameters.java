/**
 * 
 */
package soars.application.visualshell.executor.common;

import java.io.File;

import soars.application.visualshell.common.file.ScriptFile;
import soars.application.visualshell.layer.LayerManager;
import soars.application.visualshell.main.Constant;
import soars.common.soars.tool.SoarsCommonTool;
import soars.common.utility.tool.file.FileUtility;
import soars.common.utility.tool.file.ZipUtility;

/**
 * @author kurata
 *
 */
public class Parameters {

	/**
	 * Name of the root directory for Simulator data.
	 */
	static private final String _rootDirectoryName = "simulator";

	/**
	 * 
	 */
	public File _parentDirectory = null;

	/**
	 * 
	 */
	private File _rootDirectory = null;

	/**
	 * 
	 */
	public Parameters() {
		super();
	}

	/**
	 * @return
	 */
	private boolean setup_work_directory() {
		if ( null != _parentDirectory)
			return true;

		_parentDirectory = SoarsCommonTool.make_parent_directory();
		if ( null == _parentDirectory)
			return false;

		_rootDirectory = new File( _parentDirectory, _rootDirectoryName);
		if ( !_rootDirectory.mkdirs()) {
			FileUtility.delete( _parentDirectory, true);
			return false;
		}

		return true;
	}

	/**
	 * Simulator
	 * @param scriptFile
	 * @param graphicProperties
	 * @param chartProperties
	 * @return
	 */
	public boolean setup(ScriptFile scriptFile, String graphicProperties, String chartProperties) {
		if ( !setup_work_directory())
			return false;

		if ( !scriptFile.update( _rootDirectory)) {
			FileUtility.delete( _parentDirectory, true);
			return false;
		}

		return setup( graphicProperties, chartProperties);
	}

	/**
	 * @param graphicProperties
	 * @param chartProperties
	 * @return
	 */
	private boolean setup(String graphicProperties, String chartProperties) {
		if ( !setup_graphic_properties_file( graphicProperties)) {
			FileUtility.delete( _parentDirectory, true);
			return false;
		}

		if ( !setup_chart_properties_file( chartProperties)) {
			FileUtility.delete( _parentDirectory, true);
			return false;
		}

		if ( !setup_image_files()) {
			FileUtility.delete( _parentDirectory, true);
			return false;
		}

		if ( !setup_thumbnail_image_files()) {
			FileUtility.delete( _parentDirectory, true);
			return false;
		}

		if ( !setup_user_data_files()) {
			FileUtility.delete( _parentDirectory, true);
			return false;
		}

		if ( !setup_user_rule_jar_files()) {
			FileUtility.delete( _parentDirectory, true);
			return false;
		}

		if ( !setup_gaming_data_files()) {
			FileUtility.delete( _parentDirectory, true);
			return false;
		}

		return true;
	}

	/**
	 * ModelBuilder
	 * @param scriptFile
	 * @return
	 */
	public boolean setup(ScriptFile scriptFile) {
		if ( !LayerManager.get_instance().exist_user_data_directory()
			&& !LayerManager.get_instance().exist_gaming_data_directory())
			return true;

		_rootDirectory = new File( scriptFile._logFolderPath);
		if ( !_rootDirectory.exists() && !_rootDirectory.mkdirs())
			return false;

		if ( LayerManager.get_instance().exist_user_data_directory()) {
			if ( !scriptFile.update( _rootDirectory)) {
				FileUtility.delete( _rootDirectory, true);
				return false;
			}

			if ( !setup_user_data_files()) {
				FileUtility.delete( _rootDirectory, true);
				return false;
			}
		}

		if ( LayerManager.get_instance().exist_gaming_data_directory()
			&& !setup_gaming_data_files()) {
			FileUtility.delete( _rootDirectory, true);
			return false;
		}

		return true;
	}

	/**
	 * ModelBuilder
	 * @param scriptFile
	 * @return
	 */
	public boolean setup_for_genetic_algorithm(ScriptFile scriptFile) {
		if ( !LayerManager.get_instance().exist_user_data_directory()
			&& !LayerManager.get_instance().exist_gaming_data_directory())
			return true;

		_rootDirectory = new File( scriptFile._logFolderPath);
		if ( !_rootDirectory.exists()) {
			if ( !_rootDirectory.mkdirs())
				return false;

			if ( LayerManager.get_instance().exist_user_data_directory()) {
				if ( !scriptFile.update( _rootDirectory)) {
					FileUtility.delete( _rootDirectory, true);
					return false;
				}

				if ( !setup_user_data_files()) {
					FileUtility.delete( _rootDirectory, true);
					return false;
				}
			}

			if ( LayerManager.get_instance().exist_gaming_data_directory()
				&& !setup_gaming_data_files()) {
				FileUtility.delete( _rootDirectory, true);
				return false;
			}
		} else {
			if ( LayerManager.get_instance().exist_user_data_directory()
				&& !scriptFile.update( _rootDirectory))
				return false;
		}

		return true;
	}

	/**
	 * @param graphic_properties 
	 * @return
	 */
	private boolean setup_graphic_properties_file(String graphic_properties) {
		File file = new File( _rootDirectory, Constant._graphicPropertiesFilename);
		return FileUtility.write_text_to_file( file, graphic_properties, "UTF-8");
	}

	/**
	 * @param chart_properties
	 * @return
	 */
	private boolean setup_chart_properties_file(String chart_properties) {
		File file = new File( _rootDirectory, Constant._chartPropertiesFilename);
		return FileUtility.write_text_to_file( file, chart_properties, "UTF-8");
	}

	/**
	 * @return
	 */
	private boolean setup_image_files() {
		if ( !LayerManager.get_instance().exist_image_directory())
			return true;

		return FileUtility.copy_all( LayerManager.get_instance().get_image_directory(), new File( _rootDirectory, Constant._imageDirectory));
	}

	/**
	 * @return
	 */
	private boolean setup_thumbnail_image_files() {
		if ( !LayerManager.get_instance().exist_thumbnail_image_directory())
			return true;

		return FileUtility.copy_all( LayerManager.get_instance().get_thumbnail_image_directory(), new File( _rootDirectory, Constant._thumbnailImageDirectory));
	}

	/**
	 * @return
	 */
	private boolean setup_user_data_files() {
		if ( !LayerManager.get_instance().exist_user_data_directory())
			return true;

		return FileUtility.copy_all( LayerManager.get_instance().get_user_data_directory(), new File( _rootDirectory, Constant._userDataDirectory));
	}

	/**
	 * @return
	 */
	private boolean setup_user_rule_jar_files() {
		if ( !LayerManager.get_instance().exist_user_rule_jarFiles_directory())
			return true;

		return FileUtility.copy_all( LayerManager.get_instance().get_user_rule_jarFiles_directory(), new File( _rootDirectory, Constant._userRuleJarFilesInternalRelativePathName));
	}

	/**
	 * @return
	 */
	private boolean setup_gaming_data_files() {
		if ( !LayerManager.get_instance().exist_gaming_data_directory())
			return true;

		return FileUtility.copy_all( LayerManager.get_instance().get_gaming_data_directory(), new File( _rootDirectory, Constant._gamingDataDirectory));
	}

	/**
	 * ApplicatinBuilder
	 * @param file
	 * @param graphicProperties
	 * @param chartProperties
	 * @return
	 */
	public boolean compress(File file, String graphicProperties, String chartProperties) {
		if ( !setup_work_directory())
			return false;

		if ( !setup( graphicProperties, chartProperties))
			return false;

		boolean result = ZipUtility.compress( file, _rootDirectory, _parentDirectory);

		FileUtility.delete( _parentDirectory, true);

		return result;
	}

	/**
	 * @return
	 */
	public File get_user_data_directory() {
		File userDataDirectory = new File( _rootDirectory, Constant._userDataDirectory);
		return ( !userDataDirectory.exists() || !userDataDirectory.isDirectory()) ? null : userDataDirectory;
	}

	/**
	 * @return
	 */
	public File get_gaming_runner_file() {
		File gamingDataDirectory = new File( _rootDirectory, Constant._gamingDataDirectory);
		if ( !gamingDataDirectory.exists() || !gamingDataDirectory.isDirectory())
			return null;

		File gamingRunnerFile = new File( gamingDataDirectory, Constant._gamingRunnerFile);
		if ( !gamingRunnerFile.exists() || !gamingRunnerFile.isFile())
			return null;

		return gamingRunnerFile;
	}
}
