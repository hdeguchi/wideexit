/*
 * Created on 2006/04/07
 */
package soars.application.animator.main;

import soars.common.soars.constant.CommonConstant;
import soars.common.soars.environment.SoarsCommonEnvironment;

/**
 * The definition class for Animator.
 * @author kurata / SOARS project
 */
public class Constant extends CommonConstant {

	/**
	 * Version string.
	 */
	static public final String _applicationVersion = "20140827";

	/**
	 * Resource directory string.
	 */
	static public final String _resourceDirectory = "/soars/application/animator/resource";

	/**
	 * Buffering size of animation scenario 
	 */
	static public final int _bufferingSize = 10;

	/**
	 * 
	 */
	static public String _soarsVersion = null;

	/**
	 * 
	 */
	static public String _copyright = null;

	/**
	 * @return
	 */
	public static String get_soars_version() {
		return ( ( null != _soarsVersion) ? _soarsVersion : SoarsCommonEnvironment.get_instance().get( SoarsCommonEnvironment._versionKey, "SOARS"));
	}

	/**
	 * @return
	 */
	public static String get_copyright() {
		return ( ( null != _copyright) ? _copyright : SoarsCommonEnvironment.get_instance().get( SoarsCommonEnvironment._copyrightKey, "Copyright (c) 2003-???? SOARS Project."));
	}

	/**
	 * Returns the version message string.
	 * @return the version message string
	 */
	public static String get_version_message() {
		return ( get_soars_version() + _lineSeparator
			+ "- Animator (" + _applicationVersion  + ")" + _lineSeparator + _lineSeparator
			+ get_copyright());
	}
}
