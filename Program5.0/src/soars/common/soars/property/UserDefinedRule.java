/**
 * 
 */
package soars.common.soars.property;

import java.io.File;

import soars.common.soars.constant.CommonConstant;
import soars.common.soars.tool.SoarsCommonTool;
import soars.common.utility.tool.file.FileUtility;
import soars.common.utility.tool.file.ZipUtility;

/**
 * @author kurata
 *
 */
public class UserDefinedRule {

	/**
	 * 
	 */
	private File _file = null;

	/**
	 * @param file 
	 */
	public UserDefinedRule(File file) {
		super();
		_file = file;
	}

	/**
	 * @return
	 */
	public boolean exists() {
		byte[] data = ZipUtility.get_binary( _file, CommonConstant._soarsRootDirectoryName + "/" + CommonConstant._visualShellZipFileName);
		if ( null == data)
			return false;

		return ZipUtility.contains( data, CommonConstant._visualShellRootDirectoryName + CommonConstant._userRuleInternalRelativePathName + "/");
	}

	/**
	 * @param srcFolder
	 * @return
	 */
	public boolean update(File srcFolder) {
		if ( null == _file || !_file.exists() || !_file.isFile())
			return false;

		File parentDirectory = SoarsCommonTool.make_parent_directory();
		if ( null == parentDirectory)
			return false;

		if ( !SoarsCommonTool.decompress( _file, parentDirectory)) {
			FileUtility.delete( parentDirectory, true);
			return false;
		}

		File rootDirectory = new File( parentDirectory, CommonConstant._visualShellRootDirectoryName);
		if ( !rootDirectory.exists() || !rootDirectory.isDirectory()) {
			FileUtility.delete( parentDirectory, true);
			return false;
		}

		File destFolder = new File( rootDirectory, CommonConstant._userRuleInternalRelativePathName);
		FileUtility.delete( destFolder, false);
		if ( !destFolder.exists() && !destFolder.mkdirs())
			return false;

		if ( !FileUtility.copy_all( srcFolder , destFolder)) {
			FileUtility.delete( destFolder, false);
			return false;
		}

		if ( !SoarsCommonTool.update( _file, rootDirectory, parentDirectory)) {
			FileUtility.delete( parentDirectory, true);
			return false;
		}

		FileUtility.delete( parentDirectory, true);

		return true;
	}

	/**
	 * @param destFolder
	 * @return
	 */
	public boolean export(File destFolder) {
		byte[] data = ZipUtility.get_binary( _file, CommonConstant._soarsRootDirectoryName + "/" + CommonConstant._visualShellZipFileName);
		if ( null == data)
			return false;

		if ( !FileUtility.delete( destFolder, false))
			return false;

		return ( ZipUtility.decompress( data,
			new String[] {
				CommonConstant._visualShellRootDirectoryName + CommonConstant._userRuleScriptsInternalRelativePathName + "/",
				CommonConstant._visualShellRootDirectoryName + CommonConstant._userRuleJarFilesInternalRelativePathName + "/"},
			CommonConstant._visualShellRootDirectoryName + CommonConstant._userRuleInternalRelativePathName + "/",
			destFolder));
	}

	/**
	 * @return
	 */
	public boolean remove() {
		if ( null == _file || !_file.exists() || !_file.isFile())
			return false;

		File parentDirectory = SoarsCommonTool.make_parent_directory();
		if ( null == parentDirectory)
			return false;

		if ( !SoarsCommonTool.decompress( _file, parentDirectory)) {
			FileUtility.delete( parentDirectory, true);
			return false;
		}

		File rootDirectory = new File( parentDirectory, CommonConstant._visualShellRootDirectoryName);
		if ( !rootDirectory.exists() || !rootDirectory.isDirectory()) {
			FileUtility.delete( parentDirectory, true);
			return false;
		}

		File userRuleFolder = new File( rootDirectory, CommonConstant._userRuleInternalRelativePathName);
		FileUtility.delete( userRuleFolder, true);
		if ( userRuleFolder.exists())
			return false;

		if ( !SoarsCommonTool.update( _file, rootDirectory, parentDirectory)) {
			FileUtility.delete( parentDirectory, true);
			return false;
		}

		FileUtility.delete( parentDirectory, true);

		return true;
	}
}
