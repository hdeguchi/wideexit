/**
 * 
 */
package soars.library.arbitrary.aadl.macro;

import java.io.IOException;

import soars.common.utility.tool.common.Tool;
import soars.common.utility.tool.stream.StreamPumper;

/**
 * @author kurata
 *
 */
public class AADLMacro {

	/*
		java -Xmx128m -jar
		"D:\Data\OpenSpace\GenConversionRatioSet20100112\\macro\AADLMacroEngine.jar"
		-heapmax 128m
		-libpath "D:\Data\OpenSpace\GenConversionRatioSet20100112\\aadlrt\Exalge2.jar"
		-macroencoding MS932
		-csvencoding MS932
		-txtencoding MS932
		"D:\Data\OpenSpace\GenConversionRatioSet20100112\\GenConversionRatioMacro\GenConversionRatio.amf"
	*/

	/**
	 * @param memorySize
	 * @param enginePath
	 * @param libPath
	 * @param macroEncoding
	 * @param csvEncoding
	 * @param txtEncoding
	 * @param macroPath
	 */
	public static boolean execute(String memorySize, String enginePath, String libPath, String macroEncoding, String csvEncoding, String txtEncoding, String macroPath) {
		String[] cmdarray = get_cmdarray( memorySize, enginePath, libPath, macroEncoding, csvEncoding, txtEncoding,macroPath);

		Process process;
		try {
			process = ( Process)Runtime.getRuntime().exec( cmdarray);
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
	 * @param memorySize
	 * @param enginePath
	 * @param libPath
	 * @param macroEncoding
	 * @param csvEncoding
	 * @param txtEncoding
	 * @param macroPath
	 * @return
	 */
	private static String[] get_cmdarray(String memorySize, String enginePath, String libPath, String macroEncoding, String csvEncoding, String txtEncoding, String macroPath) {
		return new String[] {
			get_java_command(),
			"-Xmx" + memorySize + "m",
			"-jar",
			enginePath,
			"-heapmax",
			memorySize + "m",
			"-libpath",
			libPath,
			"-macroencoding",
			macroEncoding,
			"-csvencoding",
			csvEncoding,
			"-txtencoding",
			txtEncoding,
			macroPath};
	}

	/**
	 * @return
	 */
	private static String get_java_command() {
		String osname = System.getProperty( "os.name");
		if ( 0 <= osname.indexOf( "Windows") || 0 <= osname.indexOf( "Mac"))
			return "java";
		else
			return Tool.get_java_command();
	}
}
