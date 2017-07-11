/**
 * 
 */
package soars.common.soars.tool;

import java.io.File;
import java.io.IOException;

import soars.common.soars.constant.CommonConstant;
import soars.common.soars.environment.CommonEnvironment;
import soars.common.utility.tool.clipboard.Clipboard;
import soars.common.utility.tool.common.Tool;
import soars.common.utility.tool.file.FileUtility;
import soars.common.utility.tool.stream.StreamPumper;

/**
 * @author kurata
 *
 */
public class DocumentWriter {

	/**
	 * 
	 */
	private static final String _documentDirectory = "doc";

	/**
	 * 
	 */
	private static final String _documentFile = "document.html";

	/**
	 * @param rootDirectory
	 * @param vmlFile
	 * @return
	 */
	public static boolean execute(File rootDirectory, File vmlFile) {
		cleanup( rootDirectory);

		String currentDirectoryName = System.getProperty( CommonConstant._soarsHome);
		File currentDirectory = new File( currentDirectoryName);
		if ( null == currentDirectory)
			return false;

		String memorySize = CommonEnvironment.get_instance().get_memory_size();
		String osname = System.getProperty( "os.name");

		String[] cmdarray = get_cmdarray( vmlFile.getAbsolutePath(), currentDirectoryName, memorySize, osname);

		debug( "DocumentWriter.execute", osname, System.getProperty( "os.version"), cmdarray);

		Process process;
		try {
			process = ( Process)Runtime.getRuntime().exec( cmdarray, null, currentDirectory);
			new StreamPumper( process.getInputStream(), false).start();
			new StreamPumper( process.getErrorStream(), false).start();
			process.waitFor();
		} catch (IOException e) {
			//e.printStackTrace();
			return false;
		} catch (InterruptedException e) {
			//e.printStackTrace();
			return false;
		}

		return true;
	}

	/**
	 * @param vmlFilePath
	 * @param currentDirectoryName
	 * @param memorySize
	 * @param osname
	 * @return
	 */
	private static String[] get_cmdarray(String vmlFilePath, String currentDirectoryName, String memorySize, String osname) {
		if ( memorySize.equals( "0")) {
			if ( 0 <= osname.indexOf( "Windows"))
				return new String[] {
					CommonConstant._windowsJava,
					"-jar",
					currentDirectoryName + "/../" + CommonConstant._documentJarFilename,
					vmlFilePath};
			else if ( 0 <= osname.indexOf( "Mac"))
				return ( System.getProperty( CommonConstant._systemDefaultFileEncoding, "").equals( "")
					? new String[] {
						CommonConstant.get_mac_java_command(),
						"-Dfile.encoding=UTF-8",
						"-jar",
						currentDirectoryName + "/../" + CommonConstant._documentJarFilename,
						vmlFilePath}
					: new String[] {
						CommonConstant.get_mac_java_command(),
						"-jar",
						currentDirectoryName + "/../" + CommonConstant._documentJarFilename,
						vmlFilePath});
			else {
				return new String[] {
					Tool.get_java_command(),
					"-jar",
					currentDirectoryName + "/../" + CommonConstant._documentJarFilename,
					vmlFilePath};
			}
		} else {
			if ( 0 <= osname.indexOf( "Windows"))
				return new String[] {
					CommonConstant._windowsJava,
					"-Xmx" + memorySize + "m",
					"-jar",
					currentDirectoryName + "/../" + CommonConstant._documentJarFilename,
					vmlFilePath};
			else if ( 0 <= osname.indexOf( "Mac"))
				return ( System.getProperty( CommonConstant._systemDefaultFileEncoding, "").equals( "")
					? new String[] {
						CommonConstant.get_mac_java_command(),
						"-Dfile.encoding=UTF-8",
						"-Xmx" + memorySize + "m",
						"-jar",
						currentDirectoryName + "/../" + CommonConstant._documentJarFilename,
						vmlFilePath}
					: new String[] {
						CommonConstant.get_mac_java_command(),
						"-Xmx" + memorySize + "m",
						"-jar",
						currentDirectoryName + "/../" + CommonConstant._documentJarFilename,
						vmlFilePath});
			else {
				return new String[] {
					Tool.get_java_command(),
					"-Xmx" + memorySize + "m",
					"-jar",
					currentDirectoryName + "/../" + CommonConstant._documentJarFilename,
					vmlFilePath};
			}
		}
	}

	/**
	 * @param rootDirectory
	 */
	private static void cleanup(File rootDirectory) {
		File document_directory = new File( rootDirectory, _documentDirectory);
		if ( document_directory.exists() && document_directory.isDirectory())
			FileUtility.delete( document_directory, true);

		File document_file = new File( rootDirectory, _documentFile);
		if ( document_file.exists() && document_file.isFile())
			document_file.delete();
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
