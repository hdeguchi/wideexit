/*
 * Created on 2006/09/30
 */
package soars.plugin.modelbuilder.chart.log_viewer.body.main;

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
	static public String _resource_directory = "/soars/plugin/modelbuilder/chart/log_viewer/body/resource";

	/**
	 * @return
	 */
	public static String get_version_message() {
		return ( SoarsCommonEnvironment.get_instance().get( SoarsCommonEnvironment._versionKey, "SOARS") + _lineSeparator
			+ "- Chart Log Viewer (" + _application_version  + ")" + _lineSeparator + _lineSeparator
			+ SoarsCommonEnvironment.get_instance().get( SoarsCommonEnvironment._copyrightKey, "Copyright (c) 2003-???? SOARS Project."));
	}
}
