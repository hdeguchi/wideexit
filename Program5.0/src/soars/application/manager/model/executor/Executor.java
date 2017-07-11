/**
 * 
 */
package soars.application.manager.model.executor;

import java.io.File;

import soars.application.manager.model.main.Constant;
import soars.common.utility.tool.clipboard.Clipboard;
import soars.common.utility.tool.file.ZipUtility;

/**
 * @author kurata
 *
 */
public class Executor {

	/**
	 * @param file
	 * @return
	 */
	public static boolean is_visualShell4(File file) {
		return ZipUtility.contains( file, Constant._soarsRootDirectoryName + "/" + Constant._visualShell4IdentifyFileName);
	}

	/**
	 * @param type
	 * @param osname
	 * @param osversion
	 * @param cmdarray
	 */
	protected static void debug(String type, String osname, String osversion, String[] cmdarray) {
		String text = ""; 
		text += ( "Type : " + type + Constant._lineSeparator);
		text += ( "OS : " + osname + " [" + osversion + "]" + Constant._lineSeparator);
		for ( int i = 0; i < cmdarray.length; ++i)
			text += ( "Parameter : " + cmdarray[ i] + Constant._lineSeparator);

		Clipboard.set( text);
	}
}
