/**
 * 
 */
package soars.application.simulator.executor.animator;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import soars.application.simulator.main.Application;
import soars.application.simulator.main.Constant;
import soars.common.soars.environment.CommonEnvironment;
import soars.common.utility.tool.clipboard.Clipboard;
import soars.common.utility.tool.common.Tool;
import soars.common.utility.tool.stream.StreamPumper;

/**
 * @author kurata
 *
 */
public class Animator {

	/**
	 * 
	 */
	public Animator() {
		super();
	}

	/**
	 * @param currentDirectory
	 * @param currentDirectoryName
	 * @param parentDirectory
	 * @param rootDirectory
	 * @param soarsFile
	 * @param id
	 * @param title
	 * @param visualShellTitle
	 * @return
	 */
	public static boolean run(File currentDirectory, String currentDirectoryName, File parentDirectory, File rootDirectory, File soarsFile, long id, String title, String visualShellTitle) {
		String propertyDirectoryName = System.getProperty( Constant._soarsProperty);
		String memorySize = CommonEnvironment.get_instance().get_memory_size();
		String osname = System.getProperty( "os.name");
		String[] cmdarray = get_cmdarray( currentDirectoryName, parentDirectory, rootDirectory, propertyDirectoryName, memorySize, osname, soarsFile, id, title, visualShellTitle);

		debug( "Animator.run", osname, System.getProperty( "os.version"), cmdarray);

		Process process;
		try {
			process = ( Process)Runtime.getRuntime().exec( cmdarray, null, currentDirectory);
			new StreamPumper( process.getInputStream(), false).start();
			new StreamPumper( process.getErrorStream(), false).start();
		} catch (IOException e) {
			//e.printStackTrace();
			return false;
		}

		return true;
	}

	/**
	 * @param currentDirectoryName
	 * @param parentDirectory
	 * @param rootDirectory
	 * @param propertyDirectoryName
	 * @param memorySize
	 * @param osname
	 * @param soarsFile
	 * @param id
	 * @param title
	 * @param visualShellTitle
	 * @return
	 */
	private static String[] get_cmdarray(String currentDirectoryName, File parentDirectory, File rootDirectory, String propertyDirectoryName, String memorySize, String osname, File soarsFile, long id, String title, String visualShellTitle) {
		List< String>list = new ArrayList< String>();
		if ( 0 <= osname.indexOf( "Windows")) {
			list.add( Constant._windowsJava);
		} else if ( 0 <= osname.indexOf( "Mac")) {
			list.add( Constant.get_mac_java_command());
			//if ( System.getProperty( Constant._systemDefaultFileEncoding, "").equals( ""))
				list.add( "-Dfile.encoding=UTF-8");
			list.add( "-D" + Constant._systemDefaultFileEncoding + "=" + System.getProperty( Constant._systemDefaultFileEncoding, ""));
			list.add( "-X" + Constant._macScreenMenuName + "=SOARS Animator");
			//list.add( "-D" + Constant._macScreenMenuName + "=SOARS Animator");
			list.add( "-X" + Constant._macIconFilename + "=../resource/icon/application/animator/icon.png");
			list.add( "-D" + Constant._macScreenMenu + "=true");
		} else {
			list.add( Tool.get_java_command());
		}

		list.add( "-D" + Constant._soarsHome + "=" + currentDirectoryName);
		list.add( "-D" + Constant._soarsProperty + "=" + propertyDirectoryName);
		if ( !memorySize.equals( "0")) {
			list.add( "-D" + Constant._soarsMemorySize + "=" + memorySize);
			list.add( "-Xmx" + memorySize + "m");
		}
		list.add( "-jar");
		list.add( currentDirectoryName + "/" + Constant._animatorJarFilename);
		list.add( "-language");
		list.add( CommonEnvironment.get_instance().get( CommonEnvironment._localeKey, Locale.getDefault().getLanguage()));
		list.add( "-parent_directory");
		list.add( parentDirectory.getAbsolutePath());
		list.add( "-root_directory");
		list.add( rootDirectory.getAbsolutePath());
		if ( null != soarsFile) {
			list.add( "-soars");
			list.add( soarsFile.getAbsolutePath());
		}
		list.add( "-id");
		list.add( String.valueOf( id));
		if ( null != title && !title.equals( "")) {
			list.add( "-simulator_title");
			list.add( title);
		}
		if ( null != visualShellTitle && !visualShellTitle.equals( "")) {
			list.add( "-visualshell_title");
			list.add( visualShellTitle);
		}
		if ( Application._demo)
			list.add( "-demo");

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
		text += ( "Type : " + type + Constant._lineSeparator);
		text += ( "OS : " + osname + " [" + osversion + "]" + Constant._lineSeparator);
		for ( int i = 0; i < cmdarray.length; ++i)
			text += ( "Parameter : " + cmdarray[ i] + Constant._lineSeparator);

		Clipboard.set( text);
	}
}
