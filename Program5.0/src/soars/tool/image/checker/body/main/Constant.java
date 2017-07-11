/*
 * Created on 2006/04/07
 */
package soars.tool.image.checker.body.main;

import soars.common.soars.constant.CommonConstant;
import soars.common.soars.environment.SoarsCommonEnvironment;

/**
 * @author kurata
 */
public class Constant extends CommonConstant {

	/**
	 * 
	 */
	static public final String _application_version = "20140121";

	/**
	 * 
	 */
	static public final String _resource_directory = "/soars/tool/image/checker/body/resource";

	/**
	 * 
	 */
	static public final String _plugin_spring_filename = "beans.xml";

	/**
	 * Plugin identifier.
	 */
	static public final String _plugin_spring_id = "plugin";

	/**
	 * @return
	 */
	public static String get_version_message() {
		return ( SoarsCommonEnvironment.get_instance().get( SoarsCommonEnvironment._versionKey, "SOARS") + _lineSeparator
			+ "- Image checker (" + _application_version  + ")" + _lineSeparator + _lineSeparator
			+ SoarsCommonEnvironment.get_instance().get( SoarsCommonEnvironment._copyrightKey, "Copyright (c) 2003-???? SOARS Project."));
	}
}
