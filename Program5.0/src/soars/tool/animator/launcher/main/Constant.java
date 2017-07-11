/*
 * Created on 2006/04/07
 */
package soars.tool.animator.launcher.main;

import org.json.JSONArray;
import org.json.JSONException;

import soars.common.soars.constant.CommonConstant;
import soars.common.soars.environment.SoarsCommonEnvironment;

/**
 * @author kurata
 */
public class Constant extends CommonConstant {

	/**
	 * 
	 */
	static public final String _resource_directory = "/soars/tool/animator/launcher/resource";

	/**
	 * 
	 */
	static public String _soars_version = null;

	/**
	 * 
	 */
	static public String _application_version = null;

	/**
	 * 
	 */
	static public String _copyright = null;

	/**
	 * @param jsonArray
	 * @return
	 */
	public static boolean setup(JSONArray jsonArray) {
		try {
			_soars_version = jsonArray.getString( 3);
		} catch (JSONException e) {
			e.printStackTrace();
			return false;
		}

		try {
			_application_version = jsonArray.getString( 4);
		} catch (JSONException e) {
			e.printStackTrace();
			return false;
		}

		try {
			_copyright = jsonArray.getString( 5);
		} catch (JSONException e) {
			e.printStackTrace();
			return false;
		}

		return true;
	}

	/**
	 * @return
	 */
	public static String get_soars_version() {
		return ( ( null != _soars_version) ? _soars_version : SoarsCommonEnvironment.get_instance().get( SoarsCommonEnvironment._versionKey, "SOARS"));
	}

	/**
	 * @return
	 */
	public static String get_application_version() {
		return ( ( null != _application_version) ? _application_version : "");
	}

	/**
	 * @return
	 */
	public static String get_copyright() {
		return ( ( null != _copyright) ? _copyright : SoarsCommonEnvironment.get_instance().get( SoarsCommonEnvironment._copyrightKey, "Copyright (c) 2003-???? SOARS Project."));
	}

	/**
	 * @return
	 */
	public static String get_version_message() {
		return ( get_soars_version() + _lineSeparator
			+ "- Animation (" + get_application_version()  + ")" + _lineSeparator + _lineSeparator
			+ get_copyright());
	}
}
