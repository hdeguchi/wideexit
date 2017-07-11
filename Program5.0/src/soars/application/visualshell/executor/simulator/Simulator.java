/**
 * 
 */
package soars.application.visualshell.executor.simulator;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import soars.application.visualshell.common.file.ScriptFile;
import soars.application.visualshell.executor.common.Parameters;
import soars.application.visualshell.layer.LayerManager;
import soars.application.visualshell.main.Constant;
import soars.application.visualshell.main.Environment;
import soars.application.visualshell.object.comment.CommentManager;
import soars.common.soars.environment.CommonEnvironment;
import soars.common.utility.tool.clipboard.Clipboard;
import soars.common.utility.tool.common.Tool;
import soars.common.utility.tool.stream.StreamPumper;

/**
 * The Simulator runner class.
 * @author kurata / SOARS project
 */
public class Simulator {

	/**
	 * Returns true for running the Simulator with the specified data.
	 * @param scriptFile the specified script file
	 * @param parameters the simulation parameters
	 * @param currentDirectory the directory which contains the simulator.jar
	 * @param currentDirectoryName the name of the directory which contains the simulator.jar
	 * @param direct 
	 * @return true for running the Simulator with the specified data
	 */
	public static boolean run(ScriptFile scriptFile, Parameters parameters, File currentDirectory, String currentDirectoryName, boolean direct) {
		String propertyDirectoryName = System.getProperty( Constant._soarsProperty);
		String memorySize = CommonEnvironment.get_instance().get_memory_size();
		String osname = System.getProperty( "os.name");
		File soarsFile = LayerManager.get_instance().get_current_file();
		String[] cmdarray = get_cmdarray( scriptFile, parameters, currentDirectoryName, propertyDirectoryName, memorySize, osname, soarsFile, parameters.get_user_data_directory(), parameters.get_gaming_runner_file());

		debug( "Simulator.run", osname, System.getProperty( "os.version"), cmdarray);

		Process process;
		try {
			process = ( Process)Runtime.getRuntime().exec( cmdarray, null, currentDirectory);
			if ( !direct) {
				new StreamPumper( process.getInputStream(), false).start();
				new StreamPumper( process.getErrorStream(), false).start();
			}
		} catch (IOException e) {
			//e.printStackTrace();
			return false;
		}

		return true;
	}

	/**
	 * @param scriptFile
	 * @param parameters
	 * @param currentDirectoryName
	 * @param propertyDirectoryName
	 * @param memorySize
	 * @param osname
	 * @param soarsFile
	 * @param userDataDirectory
	 * @param gamingRunnerFile
	 * @return
	 */
	private static String[] get_cmdarray(ScriptFile scriptFile, Parameters parameters, String currentDirectoryName, String propertyDirectoryName, String memorySize, String osname, File soarsFile, File userDataDirectory, File gamingRunnerFile) {
		List< String>list = new ArrayList< String>();
		if ( 0 <= osname.indexOf( "Windows")) {
			list.add( Constant._windowsJava);
		} else if ( 0 <= osname.indexOf( "Mac")) {
			list.add( Constant.get_mac_java_command());
			//if ( System.getProperty( Constant._systemDefaultFileEncoding, "").equals( ""))
				list.add( "-Dfile.encoding=UTF-8");
			list.add( "-D" + Constant._systemDefaultFileEncoding + "=" + System.getProperty( Constant._systemDefaultFileEncoding, ""));
			list.add( "-X" + Constant._macScreenMenuName + "=SOARS Simulator");
			//list.add( "-D" + Constant._macScreenMenuName + "=SOARS Simulator");
			list.add( "-X" + Constant._macIconFilename + "=../resource/icon/application/simulator/icon.png");
			list.add( "-D" + Constant._macScreenMenu + "=true");
		} else {
			list.add( Tool.get_java_command());
		}

//	list.add( "-Djava.endorsed.dirs=" + currentDirectoryName + "/../" + Constant._xercesDirectory);
		list.add( "-D" + Constant._soarsHome + "=" + currentDirectoryName);
		list.add( "-D" + Constant._soarsProperty + "=" + propertyDirectoryName);
		if ( null != gamingRunnerFile)
			list.add( "-D" + Constant._soarsGamingRunnerFile + "=" + gamingRunnerFile.getAbsolutePath());
		list.add( "-D" + Constant._soarsSorFile + "=" + scriptFile._path.getAbsolutePath());
		if ( null != userDataDirectory)
			list.add( "-D" + Constant._soarsUserDataDirectory + "=" + userDataDirectory.getAbsolutePath().replaceAll( "\\\\", "/"));
		if ( !memorySize.equals( "0")) {
			list.add( "-D" + Constant._soarsMemorySize + "=" + memorySize);
			list.add( "-Xmx" + memorySize + "m");
		}
//		list.add( "-jar");
//		list.add( currentDirectoryName + "/" + Constant._simulatorJarFilename);
		list.add( "-cp");
		//list.add( currentDirectoryName + "/" + Constant._simulatorJarFilename
		//	+ ( ( null == gamingRunnerFile) ? "" : ( File.pathSeparator + currentDirectoryName + "/../" + Constant._gamingRunnerJarFilename)));
		list.add( currentDirectoryName + "/" + Constant._simulatorJarFilename + File.pathSeparator + currentDirectoryName + "/../" + Constant._gamingRunnerJarFilename);
		list.add( Constant._simulatorMainClassname);
		list.add( "-language");
		list.add( CommonEnvironment.get_instance().get( CommonEnvironment._localeKey, Locale.getDefault().getLanguage()));
		list.add( "-script");
		list.add( scriptFile._path.getAbsolutePath());
		list.add( "-parent_directory");
		list.add( parameters._parentDirectory.getAbsolutePath());
		if ( null != soarsFile) {
			list.add( "-soars");
			list.add( soarsFile.getAbsolutePath());
		}
		if ( !scriptFile._experimentName.equals( "")) {
			list.add( "-experiment");
			list.add( scriptFile._experimentName);
		}
		if ( Environment.get_instance().get( Environment._editExportSettingDialogToFileKey, "false").equals( "true") && !scriptFile._logFolderPath.equals( "")) {
			list.add( "-log");
			list.add( scriptFile._logFolderPath);
		}
		if ( null != CommentManager.get_instance()._title && !CommentManager.get_instance()._title.equals( "")) {
			list.add( "-visualshell_title");
			list.add( CommentManager.get_instance()._title);
		}

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
