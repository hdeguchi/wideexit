/**
 * 
 */
package soars.tool.animator.runner.executor.launcher;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

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
	 * @return
	 */
	public static boolean run(String language) {
		String home_directory_name = System.getProperty( CommonConstant._soarsHome);
		String property_directory_name = System.getProperty( CommonConstant._soarsProperty);
		String osname = System.getProperty( "os.name");

		String[] cmdarray = get_cmdarray( language, home_directory_name, property_directory_name, osname);

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
	 * @param osname
	 * @return
	 */
	private static String[] get_cmdarray(String language, String home_directory_name, String property_directory_name, String osname) {
		List< String>list = new ArrayList< String>();

		if ( 0 <= osname.indexOf( "Windows")) {
			list.add( CommonConstant._windowsJava);
		} else if ( 0 <= osname.indexOf( "Mac")) {
			list.add( CommonConstant.get_mac_java_command());
			list.add( "-Dfile.encoding=UTF-8");
			//list.add( "-D" + CommonConstant._system_default_file_encoding + "=" + System.getProperty( CommonConstant._system_default_file_encoding, ""));
			list.add( "-X" + CommonConstant._macScreenMenuName + "=SOARS Animation");
			//list.add( "-D" + CommonConstant._mac_screen_menu_name + "=SOARS Animation");
			list.add( "-X" + CommonConstant._macIconFilename + "=" + home_directory_name + "/icon.png");
			list.add( "-D" + CommonConstant._macScreenMenu + "=true");
		} else {
			list.add( Tool.get_java_command());
		}
		//list.add( "-Djava.endorsed.dirs=" + home_directory_name + "/../" + CommonConstant._xerces_directory);
		list.add( "-D" + CommonConstant._soarsHome + "=" + home_directory_name);
		list.add( "-D" + CommonConstant._soarsProperty + "=" + property_directory_name);
		list.add( "-jar");
		list.add( home_directory_name + "/" + CommonConstant._animatorRunnerLauncherJarFilename);
		list.add( "-language");
		list.add( language);

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
