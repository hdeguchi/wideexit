/**
 * 
 */
package soars.tool.animator.launcher.executor.animator;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import soars.common.utility.tool.clipboard.Clipboard;
import soars.common.utility.tool.common.Tool;
import soars.common.utility.tool.stream.StreamPumper;
import soars.tool.animator.launcher.main.Application;
import soars.tool.animator.launcher.main.Constant;

/**
 * @author kurata
 *
 */
public class Animator {

	/**
	 * @param memory_size
	 * @param title
	 * @return
	 */
	public static boolean run(String memory_size, String title) {
		String home_directory_name = System.getProperty( Constant._soarsHome);
		String property_directory_name = System.getProperty( Constant._soarsProperty);
		String osname = System.getProperty( "os.name");
		List< String>list = new ArrayList< String>();

		if ( 0 <= osname.indexOf( "Windows")) {
			list.add( Constant._windowsJava);
		} else if ( 0 <= osname.indexOf( "Mac")) {
			list.add( Constant.get_mac_java_command());
			list.add( "-Dfile.encoding=UTF-8");
			//list.add( "-D" + Constant._system_default_file_encoding + "=" + System.getProperty( Constant._system_default_file_encoding, ""));
			list.add( "-X" + Constant._macScreenMenuName + "=SOARS Animator");
			//list.add( "-D" + Constant._mac_screen_menu_name + "=SOARS Animator");
			list.add( "-X" + Constant._macIconFilename + "="
				+ ( ( null != Application._anm_file) ? "../resource/icon/application/animator/icon.png" : "icon.png"));
			list.add( "-D" + Constant._macScreenMenu + "=true");
		} else {
			list.add( Tool.get_java_command());
		}
		//list.add( "-Djava.endorsed.dirs=" + home_directory_name + "/../" + Constant._xerces_directory);
		list.add( "-D" + Constant._soarsHome + "=" + home_directory_name);
		list.add( "-D" + Constant._soarsProperty + "=" + property_directory_name);
		if ( !memory_size.equals( "0")) {
			list.add( "-D" + Constant._soarsMemorySize + "=" + memory_size);
			list.add( "-Xmx" + memory_size + "m");
		}
		list.add( "-jar");
		list.add( home_directory_name + "/" + Constant._animatorJarFilename);
		list.add( "-language");
		list.add( Locale.getDefault().getLanguage());
		list.add( "-anm");
		list.add( ( ( null != Application._anm_file)
			? Application._anm_file.getAbsolutePath()
			: ( home_directory_name + "/../" + Constant._animatorRunnerDataDirectory + "/" + Constant._animatorRunnerDataFilename)));
		list.add( "-title");
		list.add( title);
		list.add( "-soars_version");
		list.add( Constant.get_soars_version());
		list.add( "-copyright");
		list.add( Constant.get_copyright());
		list.add( "-demo");

		String[] cmdarray = list.toArray( new String[ 0]);

		debug( "Animator", osname, System.getProperty( "os.version"), cmdarray);

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
	 * @param type
	 * @param osname
	 * @param osversion
	 * @param cmdarray
	 */
	private static void debug(String type, String osname, String osversion, String[] cmdarray) {
		String text = ""; 
		text += ( "Type : " + type + Constant._lineSeparator);
		text += ( "OS : " + osname + " [" + osversion + "]" + Constant._lineSeparator);
		for ( int i = 0; i < cmdarray.length; ++i)
			text += ( "Parameter : " + cmdarray[ i] + Constant._lineSeparator);

		Clipboard.set( text);
	}
}
