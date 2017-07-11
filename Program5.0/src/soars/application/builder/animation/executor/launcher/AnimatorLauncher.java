/**
 * 
 */
package soars.application.builder.animation.executor.launcher;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import soars.application.builder.animation.main.Constant;
import soars.common.soars.constant.CommonConstant;
import soars.common.utility.tool.clipboard.Clipboard;
import soars.common.utility.tool.common.Tool;
import soars.common.utility.tool.stream.StreamPumper;

/**
 * @author kurata
 *
 */
public class AnimatorLauncher {

	/**
	 * @param language
	 * @param anm_filename
	 * @param parameter_filename
	 * @return
	 */
	public static boolean run(String language, String anm_filename, String parameter_filename) {
		String home_directory_name = System.getProperty( Constant._soarsHome);
		String property_directory_name = System.getProperty( Constant._soarsProperty);
		String osname = System.getProperty( "os.name");

		String[] cmdarray = get_cmdarray( language, home_directory_name, property_directory_name, anm_filename, parameter_filename, osname);

		debug( "Animator launcher", osname, System.getProperty( "os.version"), cmdarray);

		Process process;
		try {
			process = ( Process)Runtime.getRuntime().exec(
				cmdarray,
				null,
				new File( home_directory_name));
			new StreamPumper( process.getInputStream(), false).start();
			new StreamPumper( process.getErrorStream(), false).start();
		} catch (IOException e) {
			//e.printStackTrace();
			return false;
		}

		return true;
	}

	/**
	 * @param language
	 * @param home_directory_name
	 * @param property_directory_name
	 * @param anm_filename
	 * @param parameter_filename
	 * @param osname
	 * @return
	 */
	private static String[] get_cmdarray(String language, String home_directory_name, String property_directory_name, String anm_filename, String parameter_filename, String osname) {
		List< String>list = new ArrayList< String>();

		if ( 0 <= osname.indexOf( "Windows")) {
			list.add( CommonConstant._windowsJava);
		} else if ( 0 <= osname.indexOf( "Mac")) {
			list.add( CommonConstant.get_mac_java_command());
			list.add( "-Dfile.encoding=UTF-8");
			//list.add( "-D" + Constant._system_default_file_encoding + "=" + System.getProperty( Constant._system_default_file_encoding, ""));
			list.add( "-X" + Constant._macScreenMenuName + "=SOARS Animation");
			//list.add( "-D" + Constant._mac_screen_menu_name + "=SOARS Animation");
			list.add( "-X" + Constant._macIconFilename + "=" + home_directory_name + Constant._libraryDirectory + "/" + Constant._iconFilename);
			list.add( "-D" + Constant._macScreenMenu + "=true");
		} else {
			list.add( Tool.get_java_command());
		}
		//list.add( "-Djava.endorsed.dirs=" + home_directory_name + "/../" + Constant._xerces_directory);
		list.add( "-D" + CommonConstant._soarsHome + "=" + home_directory_name);
		list.add( "-D" + CommonConstant._soarsProperty + "=" + property_directory_name);
		list.add( "-jar");
		list.add( home_directory_name + Constant._libraryDirectory + "/" + CommonConstant._animatorRunnerInternalLauncherJarFilename);
		list.add( "-language");
		list.add( language);
		list.add( "-anm");
		list.add( anm_filename);
		list.add( "-parameter");
		list.add( parameter_filename);

		return list.toArray( new String[ 0]);
	}

	/**
	 * @param type
	 * @param osname
	 * @param osversion
	 * @param cmdarray
	 */
	private static void debug(String type, String osname, String osversion, String[] cmdarray) {
		String text = ""; 
		text += ( "Type : " + type + CommonConstant._lineSeparator);
		text += ( "OS : " + osname + " [" + osversion + "]" + CommonConstant._lineSeparator);
		for ( int i = 0; i < cmdarray.length; ++i)
			text += ( "Parameter : " + cmdarray[ i] + CommonConstant._lineSeparator);

		Clipboard.set( text);
	}
}
