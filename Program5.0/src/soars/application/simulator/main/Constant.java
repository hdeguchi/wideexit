/**
 * 
 */
package soars.application.simulator.main;

import soars.common.soars.constant.CommonConstant;
import soars.common.soars.environment.SoarsCommonEnvironment;

/**
 * @author kurata
 */
public class Constant extends CommonConstant {

	/**
	 * 
	 */
	static public final String _applicationVersion = "20151019";

	/**
	 * 
	 */
	static public final String _resourceDirectory = "/soars/application/simulator/resource";

	/**
	 * 
	 */
	static public final String _consoleFilename = "console.log";

	/**
	 * 
	 */
	static public final String _standardOutFilename = "stdout.log";

	/**
	 * 
	 */
	static public final String _standardErrorFilename = "stderr.log";

	/**
	 * @return
	 */
	public static String get_version_message() {
		return ( SoarsCommonEnvironment.get_instance().get( SoarsCommonEnvironment._versionKey, "SOARS") + _lineSeparator
			+ "- Simulator (" + _applicationVersion  + ")" + _lineSeparator + _lineSeparator
			+ SoarsCommonEnvironment.get_instance().get( SoarsCommonEnvironment._copyrightKey, "Copyright (c) 2003-???? SOARS Project."));
	}
}
