/*
 * Created on 2006/02/22
 */
package soars.application.visualshell.executor.modelbuilder;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JTextArea;

import soars.application.visualshell.common.file.ScriptFile;
import soars.application.visualshell.executor.common.Parameters;
import soars.application.visualshell.executor.monitor.MonitorPropertyPage;
import soars.application.visualshell.main.Constant;
import soars.common.soars.environment.CommonEnvironment;
import soars.common.utility.tool.clipboard.Clipboard;
import soars.common.utility.tool.common.Tool;
import soars.common.utility.tool.file.FileUtility;
import soars.common.utility.tool.stream.StreamPumper;

/**
 * The ModelBuilder runner class.
 * @author kurata / SOARS project
 */
public class ModelBuilder {

	/**
	 * Starts the ModelBuilders with the specified script files.
	 * @param scriptFiles the specified script files
	 */
	public static void start(ScriptFile[] scriptFiles) {
		String currentDirectoryName = System.getProperty( Constant._soarsHome);
		File currentDirectory = new File( currentDirectoryName);
		if ( null == currentDirectory) {
			remove( scriptFiles);
			return;
		}

		String propertyDirectoryName = System.getProperty( Constant._soarsProperty);
		String memorySize = CommonEnvironment.get_instance().get_memory_size();
		String osname = System.getProperty( "os.name");

		for ( int i = 0; i < scriptFiles.length; ++i) {
			Parameters parameters = new Parameters();
			if ( !parameters.setup( scriptFiles[ i]))
				continue;

			start( scriptFiles[ i], currentDirectory, currentDirectoryName, propertyDirectoryName, memorySize, osname, parameters.get_user_data_directory(), parameters.get_gaming_runner_file());
		}
	}

	/**
	 * @param scriptFiles
	 */
	private static void remove(ScriptFile[] scriptFiles) {
		for ( int i = 0; i < scriptFiles.length; ++i)
			FileUtility.delete( scriptFiles[ i]._path.getParentFile(), true);
	}

	/**
	 * @param scriptFile
	 * @param currentDirectory
	 * @param currentDirectoryName
	 * @param propertyDirectoryName
	 * @param memorySize
	 * @param osname
	 * @param userDataDirectory
	 * @param gamingRunnerFile
	 */
	private static void start(ScriptFile scriptFile, File currentDirectory, String currentDirectoryName, String propertyDirectoryName, String memorySize, String osname, File userDataDirectory, File gamingRunnerFile) {
		String[] cmdarray = get_cmdarray( scriptFile, currentDirectoryName, propertyDirectoryName, memorySize, osname, userDataDirectory, gamingRunnerFile);

		debug( "ModelBuilder.start", osname, System.getProperty( "os.version"), cmdarray);

		Process process;
		try {
			process = ( Process)Runtime.getRuntime().exec( cmdarray, null, currentDirectory);
			new StreamPumper( process.getInputStream(), false).start();
			new StreamPumper( process.getErrorStream(), false).start();
		} catch (IOException e) {
			e.printStackTrace();
			FileUtility.delete( scriptFile._path.getParentFile(), true);
			return;
		}
	}

	/**
	 * @param scriptFiles
	 * @param currentDirectoryName
	 * @param propertyDirectoryName
	 * @param memorySize
	 * @param osname
	 * @param userDataDirectory
	 * @param gamingRunnerFile
	 * @return
	 */
	private static String[] get_cmdarray(ScriptFile scriptFile, String currentDirectoryName, String propertyDirectoryName, String memorySize, String osname, File userDataDirectory, File gamingRunnerFile) {
		List< String>list = new ArrayList< String>();
		if ( 0 <= osname.indexOf( "Windows")) {
			list.add( Constant._windowsJava);
		} else if ( 0 <= osname.indexOf( "Mac")) {
			list.add( Constant.get_mac_java_command());
			//if ( System.getProperty( Constant._systemDefaultFileEncoding, "").equals( ""))
				list.add( "-Dfile.encoding=UTF-8");
//			list.add( "-D" + Constant._systemDefaultFileEncoding + "=" + System.getProperty( Constant._systemDefaultFileEncoding, ""));
				list.add( "-X" + Constant._macScreenMenuName + "=SOARS Engine");
				//list.add( "-D" + Constant._macScreenMenuName + "=SOARS Engine");
				//list.add( "-X" + Constant._macIconFilename + "=../resource/icon/application/modelbuilder/icon.png");
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
//		list.add( currentDirectoryName + "/" + Constant._soarsEngineJarFilename);
		list.add( "-cp");
		//list.add( currentDirectoryName + "/" + Constant._soarsEngineJarFilename
		//	+ ( ( null == gamingRunnerFile) ? "" : ( File.pathSeparator + currentDirectoryName + "/../" + Constant._gamingRunnerJarFilename)));
		list.add( currentDirectoryName + "/" + Constant._soarsEngineJarFilename + File.pathSeparator + currentDirectoryName + "/../" + Constant._gamingRunnerJarFilename);
		list.add( Constant._soarsEngineGuiMainClassname);
		list.add( scriptFile._path.getAbsolutePath());

		return list.toArray( new String[ 0]);
	}

	/**
	 * Returns true for running the ModelBuilder with the specified script file.
	 * @param scriptFile the specified script file
	 * @param monitorPropertyPage the monitor component to display the log output of the ModelBuilder
	 * @param currentDirectory the directory which contains the ModelBuilder.jar
	 * @param currentDirectoryName the name of the directory which contains the ModelBuilder.jar
	 * @return true for running the ModelBuilder with the specified script file
	 */
	public static boolean run(ScriptFile scriptFile, MonitorPropertyPage monitorPropertyPage, File currentDirectory, String currentDirectoryName) {
		Parameters parameters = new Parameters();
		if ( !parameters.setup( scriptFile))
			return false;

		String propertyDirectoryName = System.getProperty( Constant._soarsProperty);
		String memorySize = CommonEnvironment.get_instance().get_memory_size();
		String osname = System.getProperty( "os.name");
		String[] cmdarray = get_cmdarray( scriptFile._path, currentDirectory, currentDirectoryName, propertyDirectoryName, memorySize, osname, parameters.get_user_data_directory(), parameters.get_gaming_runner_file());

		debug( "ModelBuilder.run", osname, System.getProperty( "os.version"), cmdarray);

		try {
			InputStream inputStream, errorStream;

			synchronized( monitorPropertyPage._lockProcess) {
				monitorPropertyPage._process = ( Process)Runtime.getRuntime().exec( cmdarray, null, currentDirectory);
				inputStream = monitorPropertyPage._process.getInputStream();
				errorStream = monitorPropertyPage._process.getErrorStream();
			}

			new StreamPumper( inputStream, false).start();

			String text = read( errorStream, monitorPropertyPage);
			if ( null == text) {
				monitorPropertyPage.append( "");
				return false;
			}

			monitorPropertyPage.append( text);

			if ( -1 == text.indexOf( "Execution Time:"))
				return false;

			//System.out.println( text);
		} catch (IOException e) {
			//e.printStackTrace();
			monitorPropertyPage.append( "");
			return false;
		}

		return true;
	}

	/**
	 * @param scriptFile
	 * @param currentDirectory
	 * @param currentDirectoryName
	 * @param propertyDirectoryName
	 * @param memorySize
	 * @param osname
	 * @param userDataDirectory
	 * @param gamingRunnerFile
	 * @return
	 */
	private static String[] get_cmdarray(File scriptFile, File currentDirectory, String currentDirectoryName, String propertyDirectoryName, String memorySize, String osname, File userDataDirectory, File gamingRunnerFile) {
		List< String>list = new ArrayList< String>();
		if ( 0 <= osname.indexOf( "Windows")) {
			list.add( Constant._windowsJava);
		} else if ( 0 <= osname.indexOf( "Mac")) {
			list.add( Constant.get_mac_java_command());
			//if ( System.getProperty( Constant._systemDefaultFileEncoding, "").equals( ""))
				list.add( "-Dfile.encoding=UTF-8");
//		list.add( "-D" + Constant._systemDefaultFileEncoding + "=" + System.getProperty( Constant._systemDefaultFileEncoding, ""));
		} else {
			list.add( Tool.get_java_command());
		}

//		list,.add( "-Djava.endorsed.dirs=" + currentDirectoryName + "/../" + Constant._xercesDirectory);
		list.add( "-D" + Constant._soarsHome + "=" + currentDirectoryName);
		list.add( "-D" + Constant._soarsProperty + "=" + propertyDirectoryName);
		if ( null != gamingRunnerFile)
			list.add( "-D" + Constant._soarsGamingRunnerFile + "=" + gamingRunnerFile.getAbsolutePath());
		list.add( "-D" + Constant._soarsSorFile + "=" + scriptFile.getAbsolutePath());
		if ( null != userDataDirectory)
			list.add( "-D" + Constant._soarsUserDataDirectory + "=" + userDataDirectory.getAbsolutePath().replaceAll( "\\\\", "/"));
		if ( !memorySize.equals( "0")) {
			list.add( "-D" + Constant._soarsMemorySize + "=" + memorySize);
			list.add( "-Xmx" + memorySize + "m");
		}
		list.add( "-cp");
		//list.add( currentDirectoryName + "/" + Constant._soarsEngineJarFilename
		//	+ ( ( null == gamingRunnerFile) ? "" : ( File.pathSeparator + currentDirectoryName + "/../" + Constant._gamingRunnerJarFilename)));
		list.add( currentDirectoryName + "/" + Constant._soarsEngineJarFilename + File.pathSeparator + currentDirectoryName + "/../" + Constant._gamingRunnerJarFilename);
		list.add( Constant._soarsEngineConsoleMainClassname);
		list.add( "run=" + scriptFile.getAbsolutePath());

		return list.toArray( new String[ 0]);
	}

	/**
	 * Returns true for running the ModelBuilder with the specified script file.
	 * @param scriptFiles the specified script files
	 * @param monitorPropertyPage the monitor component to display the log output of the ModelBuilder
	 * @param currentDirectory the directory which contains the ModelBuilder.jar
	 * @param currentDirectoryName the name of the directory which contains the ModelBuilder.jar
	 * @return true for running the ModelBuilder with the specified script file
	 */
	public static boolean run(ScriptFile[] scriptFiles, MonitorPropertyPage monitorPropertyPage, File currentDirectory, String currentDirectoryName) {
		for ( ScriptFile scriptFile:scriptFiles) {
			Parameters parameters = new Parameters();
			if ( !parameters.setup( scriptFile))
				return false;
		}

		String propertyDirectoryName = System.getProperty( Constant._soarsProperty);
		String memorySize = CommonEnvironment.get_instance().get_memory_size();
		String osname = System.getProperty( "os.name");
		String[] cmdarray = get_cmdarray( scriptFiles, currentDirectory, currentDirectoryName, propertyDirectoryName, memorySize, osname);

		debug( "ModelBuilder.run", osname, System.getProperty( "os.version"), cmdarray);

		try {
			InputStream inputStream, errorStream;

			synchronized( monitorPropertyPage._lockProcess) {
				monitorPropertyPage._process = ( Process)Runtime.getRuntime().exec( cmdarray, null, currentDirectory);
				inputStream = monitorPropertyPage._process.getInputStream();
				errorStream = monitorPropertyPage._process.getErrorStream();
			}

			new StreamPumper( inputStream, false).start();

			String text = read( errorStream, monitorPropertyPage);
			if ( null == text) {
				monitorPropertyPage.append( "");
				return false;
			}

			monitorPropertyPage.append( text);

			if ( -1 == text.indexOf( "Execution Time:"))
				return false;

			synchronized( monitorPropertyPage._lockProcess) {
				monitorPropertyPage._process.waitFor();
			}

			//System.out.println( text);
		} catch (IOException e) {
			//e.printStackTrace();
			monitorPropertyPage.append( "");
			return false;
		} catch (InterruptedException e) {
			//e.printStackTrace();
			return false;
		}

		return true;
	}

/**
	 * @param scriptFiles
	 * @param currentDirectory
	 * @param currentDirectoryName
	 * @param propertyDirectoryName
	 * @param memorySize
	 * @param osname
	 * @return
	 */
	private static String[] get_cmdarray(ScriptFile[] scriptFiles, File currentDirectory, String currentDirectoryName, String propertyDirectoryName, String memorySize, String osname) {
		List< String>list = new ArrayList< String>();
		if ( 0 <= osname.indexOf( "Windows")) {
			list.add( Constant._windowsJava);
		} else if ( 0 <= osname.indexOf( "Mac")) {
			list.add( Constant.get_mac_java_command());
			//if ( System.getProperty( Constant._systemDefaultFileEncoding, "").equals( ""))
				list.add( "-Dfile.encoding=UTF-8");
//		list.add( "-D" + Constant._systemDefaultFileEncoding + "=" + System.getProperty( Constant._systemDefaultFileEncoding, ""));
		} else {
			list.add( Tool.get_java_command());
		}

//		list,.add( "-Djava.endorsed.dirs=" + currentDirectoryName + "/../" + Constant._xercesDirectory);
		list.add( "-D" + Constant._soarsHome + "=" + currentDirectoryName);
		list.add( "-D" + Constant._soarsProperty + "=" + propertyDirectoryName);
//		if ( null != gamingRunnerFile)
//			list.add( "-D" + Constant._soarsGamingRunnerFile + "=" + gamingRunnerFile.getAbsolutePath());
//		list.add( "-D" + Constant._soarsSorFile + "=" + scriptFile.getAbsolutePath());
//		if ( null != userDataDirectory)
//			list.add( "-D" + Constant._soarsUserDataDirectory + "=" + userDataDirectory.getAbsolutePath().replaceAll( "\\\\", "/"));
		if ( !memorySize.equals( "0")) {
			list.add( "-D" + Constant._soarsMemorySize + "=" + memorySize);
			list.add( "-Xmx" + memorySize + "m");
		}
		list.add( "-cp");
		//list.add( currentDirectoryName + "/" + Constant._soarsEngineJarFilename
		//	+ ( ( null == gamingRunnerFile) ? "" : ( File.pathSeparator + currentDirectoryName + "/../" + Constant._gamingRunnerJarFilename)));
		list.add( currentDirectoryName + "/" + Constant._soarsEngineJarFilename + File.pathSeparator + currentDirectoryName + "/../" + Constant._gamingRunnerJarFilename);
		list.add( Constant._soarsEngineConsoleMainClassname);

		for ( int i = 0; i < scriptFiles.length; ++i) {
			if ( 0 < i)
				list.add( "reset=");

			list.add( "run=" + scriptFiles[ i]._path.getAbsolutePath().replaceAll( "\\\\", "/"));
//			list.add( ( ( 0 == i) ? "run=" : "") + scriptFiles.get( i)._path.getAbsolutePath().replaceAll( "\\\\", "/"));
		}

//		String command = "";
//		for ( int i = 0; i < list.size(); ++i)
//			command += ( 0 == i) ? "" : " " + list.get( i);
//
//		System.out.println( command);

		return list.toArray( new String[ 0]);
	}

	/**
	 * @param inputStream
	 * @param monitorPropertyPage
	 * @return
	 */
	private static String read(InputStream inputStream, MonitorPropertyPage monitorPropertyPage) {
		List<String> list = new ArrayList<String>();
		InputStreamReader inputStreamReader = null;
		try {
//		inputStreamReader = new InputStreamReader( inputStream, "SJIS");
			String charsetName = "";
			if ( 0 <= System.getProperty( "os.name").indexOf( "Mac"))
				charsetName = System.getProperty( Constant._systemDefaultFileEncoding, "");
			else
				charsetName = System.getProperty( "file.encoding", "");

			if ( charsetName.equals( ""))
				inputStreamReader = new InputStreamReader( inputStream);
			else
				inputStreamReader = new InputStreamReader( inputStream, charsetName);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			return null;
		}
		try {
			boolean ignore = true;
			String line = "";
			while ( true) {
				int c = inputStreamReader.read();
				if ( -1 == c)
					break;

				line += ( char)c;
				if ( Constant._lineSeparator.charAt( 0) == c) {
//					System.out.println( line);
					if ( line.startsWith( "pleaseprint"))
						ignore = false;
					else 	if ( line.startsWith( "pleaseflush"))
						ignore = true;
					else {
						if ( !ignore)
							monitorPropertyPage.append( line);
						else {
							if ( line.startsWith( "Execution Time:") || line.startsWith( "Caused by:"))
								list.add( line);
						}
					}

					line = "";
				}
			}
		} catch (IOException e) {
			//e.printStackTrace();
			return null;
		}
//		try {
//			String line = "";
//			while ( true) {
//				int c = inputStream.read();
//				if ( -1 == c)
//					break;
//
//				line += ( char)c;
//				if ( Constant._lineSeparator.charAt( 0) == c) {
//					//System.out.println( line);
//					if ( line.startsWith( "Execution Time:") || line.startsWith( "Caused by:"))
//						list.add( line);
//
//					line = "";
//				}
//			}
//		} catch (IOException e) {
//			//e.printStackTrace();
//			return null;
//		}

		boolean emergence = false;
		List<String> lines = new ArrayList<String>();
		for ( int i = list.size() - 1; i >= 0; --i) {
			String line = list.get( i);
			if ( emergence) {
				if ( line.startsWith( "Execution Time:") || line.startsWith( "Caused by: java.lang.RuntimeException:"))
					lines.add( 0, line);
			} else {
				if ( line.startsWith( "Execution Time:"))
					lines.add( 0, line);
				else if ( line.startsWith( "Caused by:")) {
					lines.add( 0, line);
					emergence = true;
				}
			}
		}

		String text = "";
		for ( int i = 0; i < lines.size(); ++i) {
			String line = lines.get( i);
			text += line;
		}

		//System.out.println( text);

		return text;
	}

//	/**
//	 * Returns true for running the ModelBuilder with the specified script file on Genetic Algorithm.
//	 * @param scriptFile the specified script file
//	 * @param logTextArea the monitor component to display the log output of the ModelBuilder
//	 * @param process
//	 * @param lockProcess synchronized object
//	 * @return true for running the ModelBuilder with the specified script file
//	 */
//	public static boolean run_on_genetic_algorithm(ScriptFile scriptFile, JTextArea logTextArea, List<Process> processes, Object lockProcess) {
//		String currentDirectoryName = System.getProperty( Constant._soarsHome);
//		File currentDirectory = new File( currentDirectoryName);
//		if ( null == currentDirectory)
//			return false;
//
//		Parameters parameters = new Parameters();
//		if ( !parameters.setup( scriptFile))
//			return false;
//
//		String propertyDirectoryName = System.getProperty( Constant._soarsHome);
//		String memorySize = CommonEnvironment.get_instance().get_memory_size();
//		String osname = System.getProperty( "os.name");
//		String[] cmdarray = get_cmdarray( scriptFile._path, currentDirectory, currentDirectoryName, propertyDirectoryName, memorySize, osname, parameters.get_user_data_directory(), parameters.get_gaming_runner_file());
//
//		//debug( "ModelBuilder.run", osname, System.getProperty( "os.version"), cmdarray);
//
//		try {
//			InputStream inputStream, errorStream;
//
//			synchronized( lockProcess) {
//				processes.add( ( Process)Runtime.getRuntime().exec( cmdarray, null, currentDirectory));
//				inputStream = processes.get( 0).getInputStream();
//				errorStream = processes.get( 0).getErrorStream();
//			}
//
//			new StreamPumper( inputStream, false).start();
//
//			String text = read( errorStream, logTextArea);
//			if ( null == text) {
//				logTextArea.append( "");
//				return false;
//			}
//
//			logTextArea.append( text);
//
//			if ( -1 == text.indexOf( "Execution Time:"))
//				return false;
//
//			//System.out.println( text);
//		} catch (IOException e) {
//			//e.printStackTrace();
//			logTextArea.append( "");
//			return false;
//		}
//
//		return true;
//	}

	/**
	 * Returns true for running the ModelBuilder with the specified script file on Genetic Algorithm.
	 * @param scriptFiles the specified script files
	 * @param logTextArea the monitor component to display the log output of the ModelBuilder
	 * @param processes
	 * @param lockProcess synchronized object
	 * @return true for running the ModelBuilder with the specified script file
	 */
	public static boolean run_on_genetic_algorithm(List<ScriptFile> scriptFiles, JTextArea logTextArea, List<Process> processes, Object lockProcess) {
		String currentDirectoryName = System.getProperty( Constant._soarsHome);
		File currentDirectory = new File( currentDirectoryName);
		if ( null == currentDirectory)
			return false;

		for ( ScriptFile scriptFile:scriptFiles) {
			Parameters parameters = new Parameters();
			if ( !parameters.setup_for_genetic_algorithm( scriptFile))
				return false;
		}

		String propertyDirectoryName = System.getProperty( Constant._soarsProperty);
		String memorySize = CommonEnvironment.get_instance().get_memory_size();
		String osname = System.getProperty( "os.name");
		String[] cmdarray = get_cmdarray( scriptFiles, currentDirectory, currentDirectoryName, propertyDirectoryName, memorySize, osname);

		//debug( "ModelBuilder.run", osname, System.getProperty( "os.version"), cmdarray);

		try {
			InputStream inputStream, errorStream;

			synchronized( lockProcess) {
				processes.add( ( Process)Runtime.getRuntime().exec( cmdarray, null, currentDirectory));
				inputStream = processes.get( 0).getInputStream();
				errorStream = processes.get( 0).getErrorStream();
			}

			new StreamPumper( inputStream, false).start();

			String text = read( errorStream, logTextArea);
			if ( null == text) {
				logTextArea.append( "");
				return false;
			}

			logTextArea.append( text);

			if ( -1 == text.indexOf( "Execution Time:"))
				return false;

			synchronized( lockProcess) {
				processes.get( 0).waitFor();
			}

			//System.out.println( text);
		} catch (IOException e) {
			//e.printStackTrace();
			logTextArea.append( "");
			return false;
		} catch (InterruptedException e) {
			//e.printStackTrace();
			return false;
		}

		return true;
	}

	/**
	 * @param scriptFiles
	 * @param currentDirectory
	 * @param currentDirectoryName
	 * @param propertyDirectoryName
	 * @param memorySize
	 * @param osname
	 * @return
	 */
	private static String[] get_cmdarray(List<ScriptFile> scriptFiles, File currentDirectory, String currentDirectoryName, String propertyDirectoryName, String memorySize, String osname) {
		List< String>list = new ArrayList< String>();
		if ( 0 <= osname.indexOf( "Windows")) {
			list.add( Constant._windowsJava);
		} else if ( 0 <= osname.indexOf( "Mac")) {
			list.add( Constant.get_mac_java_command());
			//if ( System.getProperty( Constant._systemDefaultFileEncoding, "").equals( ""))
				list.add( "-Dfile.encoding=UTF-8");
//		list.add( "-D" + Constant._systemDefaultFileEncoding + "=" + System.getProperty( Constant._systemDefaultFileEncoding, ""));
		} else {
			list.add( Tool.get_java_command());
		}

//		list,.add( "-Djava.endorsed.dirs=" + current_directory_name + "/../" + Constant._xerces_directory);
		list.add( "-D" + Constant._soarsHome + "=" + currentDirectoryName);
		list.add( "-D" + Constant._soarsProperty + "=" + propertyDirectoryName);
//		if ( null != gamingRunnerFile)
//			list.add( "-D" + Constant._soarsGamingRunnerFile + "=" + gamingRunnerFile.getAbsolutePath());
//		list.add( "-D" + Constant._soarsSorFile + "=" + scriptFile.getAbsolutePath());
//		if ( null != userDataDirectory)
//			list.add( "-D" + Constant._soarsUserDataDirectory + "=" + userDataDirectory.getAbsolutePath().replaceAll( "\\\\", "/"));
		if ( !memorySize.equals( "0")) {
			list.add( "-D" + Constant._soarsMemorySize + "=" + memorySize);
			list.add( "-Xmx" + memorySize + "m");
		}
		list.add( "-cp");
		//list.add( currentDirectoryName + "/" + Constant._soarsEngineJarFilename
		//	+ ( ( null == gamingRunnerFile) ? "" : ( File.pathSeparator + currentDirectoryName + "/../" + Constant._gamingRunnerJarFilename)));
		list.add( currentDirectoryName + "/" + Constant._soarsEngineJarFilename + File.pathSeparator + currentDirectoryName + "/../" + Constant._gamingRunnerJarFilename);
		list.add( Constant._soarsEngineConsoleMainClassname);

		for ( int i = 0; i < scriptFiles.size(); ++i) {
			if ( 0 < i)
				list.add( "reset=");

			list.add( "run=" + scriptFiles.get( i)._path.getAbsolutePath().replaceAll( "\\\\", "/"));
//			list.add( ( ( 0 == i) ? "run=" : "") + scriptFiles.get( i)._path.getAbsolutePath().replaceAll( "\\\\", "/"));
		}

//		String command = "";
//		for ( int i = 0; i < list.size(); ++i)
//			command += ( 0 == i) ? "" : " " + list.get( i);
//
//		System.out.println( command);

		return list.toArray( new String[ 0]);
	}

	/**
	 * @param inputStream
	 * @param logTextArea
	 * @return
	 */
	private static String read(InputStream inputStream, JTextArea logTextArea) {
		InputStreamReader inputStreamReader = null;
		try {
//		inputStreamReader = new InputStreamReader( inputStream, "SJIS");
			String charsetName = "";
			if ( 0 <= System.getProperty( "os.name").indexOf( "Mac"))
				charsetName = System.getProperty( Constant._systemDefaultFileEncoding, "");
			else
				charsetName = System.getProperty( "file.encoding", "");

			if ( charsetName.equals( ""))
				inputStreamReader = new InputStreamReader( inputStream);
			else
				inputStreamReader = new InputStreamReader( inputStream, charsetName);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			return null;
		}

//		List<String> list = new ArrayList<String>();
		String text = "";
		try {
//			boolean ignore = true;
			String line = "";
			while ( true) {
				int c = inputStreamReader.read();
				if ( -1 == c)
					break;

				line += ( char)c;
				if ( Constant._lineSeparator.charAt( 0) == c) {
//					System.out.println( line);
//					if ( line.startsWith( "pleaseprint"))
//						ignore = false;
//					else 	if ( line.startsWith( "pleaseflush"))
//						ignore = true;
//					else {
//						if ( !ignore)
//							logTextArea.append( line);
//						else {
							if ( line.equals( "") || line.startsWith( "Execution Time:") || line.startsWith( "Caused by:"))
								text += line;
//						}
//					}

					line = "";
				}
			}
		} catch (IOException e) {
			//e.printStackTrace();
			return null;
		}

		return text;

//		try {
//			String line = "";
//			while ( true) {
//				int c = inputStream.read();
//				if ( -1 == c)
//					break;
//
//				line += ( char)c;
//				if ( Constant._lineSeparator.charAt( 0) == c) {
//					//System.out.println( line);
//					if ( line.startsWith( "Execution Time:") || line.startsWith( "Caused by:"))
//						list.add( line);
//
//					line = "";
//				}
//			}
//		} catch (IOException e) {
//			//e.printStackTrace();
//			return null;
//		}

//		boolean emergence = false;
//		List<String> lines = new ArrayList<String>();
//		for ( int i = list.size() - 1; i >= 0; --i) {
//			String line = list.get( i);
//			if ( emergence) {
//				if ( line.startsWith( "Execution Time:") || line.startsWith( "Caused by: java.lang.RuntimeException:"))
//					lines.add( 0, line);
//			} else {
//				if ( line.startsWith( "Execution Time:"))
//					lines.add( 0, line);
//				else if ( line.startsWith( "Caused by:")) {
//					lines.add( 0, line);
//					emergence = true;
//				}
//			}
//		}
//
//		String text = "";
//		for ( int i = 0; i < lines.size(); ++i)
//			text += lines.get( i);
//
//		//System.out.println( text);
//
//		return text;
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
