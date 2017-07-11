/**
 * 
 */
package soars.application.manager.library.main;

import soars.common.soars.constant.CommonConstant;
import soars.common.soars.environment.SoarsCommonEnvironment;

/**
 * @author kurata
 *
 */
public class Constant extends CommonConstant {

	/**
	 * Application version string.
	 */
	static public final String _applicationVersion = "20140121";

	/**
	 * Resource directory string.
	 */
	static public final String _resourceDirectory = "/soars/application/manager/library/resource";

	/**
	 * Path of program file which analyzes jar file.
	 */
	static public final String _jarfileAnalyzerJarFilename = "../library/jarfile_analyzer/jarfile_analyzer.jar";

	/**
	 * Returns the version message string.
	 * @return the version message string
	 */
	public static String get_version_message() {
		return ( SoarsCommonEnvironment.get_instance().get( SoarsCommonEnvironment._versionKey, "SOARS") + _lineSeparator
			+ "- Library Manager (" + _applicationVersion  + ")" + _lineSeparator + _lineSeparator
			+ SoarsCommonEnvironment.get_instance().get( SoarsCommonEnvironment._copyrightKey, "Copyright (c) 2003-???? SOARS Project."));
	}
}
