/**
 * 
 */
package soars.application.manager.model.main;

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
	static public final String _applicationVersion = "20170630";

	/**
	 * Resource directory string.
	 */
	static public final String _resourceDirectory = "/soars/application/manager/model/resource";

	/**
	 * 
	 */
	static public final String[][] _languages = {
		{ "en", "english"},
		{ "ja", "japanese"}
//		{ "zh", "chinese"},
//		{ "en", "english"},
//		{ "in", "indonesian"},
//		{ "ja", "japanese"},
//		{ "kr", "korean"},
//		{ "es", "spanish"}
	};

	/**
	 * Returns the version message string.
	 * @return the version message string
	 */
	public static String get_version_message() {
		return ( SoarsCommonEnvironment.get_instance().get( SoarsCommonEnvironment._versionKey, "SOARS") + _lineSeparator
			+ "- Model Manager (" + _applicationVersion  + ")" + _lineSeparator + _lineSeparator
			+ SoarsCommonEnvironment.get_instance().get( SoarsCommonEnvironment._copyrightKey, "Copyright (c) 2003-???? SOARS Project."));
	}
}
