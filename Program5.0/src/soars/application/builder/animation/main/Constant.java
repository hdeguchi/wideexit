/**
 * 
 */
package soars.application.builder.animation.main;

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
	static public final String _resourceDirectory = "/soars/application/builder/animation/resource";

	/**
	 * Library directory string.
	 */
	static public final String _libraryDirectory = "/../library/builder/animation";

	/**
	 * JASON directory string.
	 */
	static public final String _jsonLibraryDirectory = "/../library/json";

	/**
	 * JASON jar file name string.
	 */
	static public final String _jsonJarFilename = "json.jar";

	/**
	 * Chart directory string.
	 */
	static public final String _chartLibraryDirectory = "/../function/chart/module";

	/**
	 * Chart jar file name string.
	 */
	static public final String _chartJarFilename = "chart.jar";

	/**
	 * Plot directory string.
	 */
	static public final String _plotLibraryDirectory = "/../library/chart";

	/**
	 * Plot jar file name string.
	 */
	static public final String _plotJarFilename = "plot.jar";

	/**
	 * Icon file name string.
	 */
	static public final String _iconFilename = "icon.png";

	/**
	 * Returns the version message string.
	 * @return the version message string
	 */
	public static String get_version_message() {
		return ( SoarsCommonEnvironment.get_instance().get( SoarsCommonEnvironment._versionKey, "SOARS") + _lineSeparator
			+ "- Animation Builder (" + _applicationVersion  + ")" + _lineSeparator + _lineSeparator
			+ SoarsCommonEnvironment.get_instance().get( SoarsCommonEnvironment._copyrightKey, "Copyright (c) 2003-???? SOARS Project."));
	}
}
