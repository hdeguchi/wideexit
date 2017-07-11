/*
 * Created on 2006/03/28
 */
package soars.common.soars.environment;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import soars.common.soars.constant.CommonConstant;
import soars.common.soars.tool.SoarsCommonTool;
import soars.common.utility.tool.environment.EnvironmentBase;

/**
 * @author kurata
 */
public class BasicEnvironment extends EnvironmentBase {

	/**
	 * 
	 */
	static public final String _projectFolderDirectoryKey = "Project.folder.directory";

	/**
	 * 
	 */
	static public final String _projectFolderKey = "Project.folder";

	/**
	 * 
	 */
	static public final String _defaultProjectFolderKey = "Default.project.folder";

	/**
	 * 
	 */
	static public final String _usePropertyInProjectFolderKey = "Use.property.in.project.folder";

	/**
	 * 
	 */
	static public final String[] _projectSubFolderNames = {
		CommonConstant._modelDirectoryName,
		CommonConstant._userModuleDirectoryName,
		CommonConstant._filterDirectoryName,
		CommonConstant._userRuleScriptsExternalRelativePathName,
		CommonConstant._userRuleJarFilesExternalRelativePathName
	};

	/**
	 * 
	 */
	static private Object _lock = new Object();

	/**
	 * 
	 */
	static private BasicEnvironment _basicEnvironment = null;

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
	public static void startup() throws FileNotFoundException, IOException {
		synchronized( _lock) {
			if ( null == _basicEnvironment) {
				File propertyDirectory = SoarsCommonTool.get_default_property_directory();
				if ( null == propertyDirectory)
					System.exit( 0);
				_basicEnvironment = new BasicEnvironment( propertyDirectory);
				if ( !_basicEnvironment.initialize())
					System.exit( 0);
			}
		}
	}

	/**
	 * @return
	 */
	public static BasicEnvironment get_instance() {
		if ( null == _basicEnvironment) {
			System.exit( 0);
		}

		return _basicEnvironment;
	}

	/**
	 * @param propertyDirectory
	 * 
	 */
	public BasicEnvironment(File propertyDirectory) {
		super(
			propertyDirectory.getAbsolutePath() + File.separator
				+ "basic" + File.separator
				+ "environment" + File.separator,
			"environment.properties",
			"SOARS basic properties");
	}

	/**
	 * @param projectFolder
	 * @return
	 */
	public boolean create_projectSubFolers(File projectFolder) {
		for ( String projectSubFolderName:_projectSubFolderNames) {
			if ( !create_projectSubFoler( projectFolder, projectSubFolderName))
				return false;
		}
		return true;
	}

	/**
	 * @param projectFolder
	 * @param projectSubFolderName
	 * @return
	 */
	private boolean create_projectSubFoler(File projectFolder, String projectSubFolderName) {
		File projectSubFolder = new File( projectFolder, projectSubFolderName);
		if ( !projectSubFolder.exists()) {
			if ( !projectSubFolder.mkdirs())
				return false;
		} else {
			if ( !projectSubFolder.isDirectory())
				return false;
		}
		return true;
	}

	/**
	 * @param projectSubFolderName
	 * @return
	 */
	public File get_projectSubFoler(String projectSubFolderName) {
		String projectFolderName = get( _projectFolderDirectoryKey, "");
		if ( projectFolderName.equals( ""))
			return null;

		File projectFolder = new File( projectFolderName);
		if ( !projectFolder.exists() || !projectFolder.isDirectory())
			return null;

		File projectSubFolder = new File( projectFolder, projectSubFolderName);
		if ( !projectSubFolder.exists() || !projectSubFolder.isDirectory())
			return null;

		return projectSubFolder;
	}
}
