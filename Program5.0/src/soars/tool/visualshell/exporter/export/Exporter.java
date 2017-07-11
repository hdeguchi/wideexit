/**
 * 
 */
package soars.tool.visualshell.exporter.export;

import java.io.File;

import soars.application.visualshell.main.Constant;
import soars.application.visualshell.main.Environment;
import soars.application.visualshell.object.arbitrary.JarFileProperties;
import soars.common.utility.tool.file.FileUtility;

/**
 * @author kurata
 *
 */
public class Exporter {

	/**
	 * @param filename
	 * @return
	 */
	public static boolean run(String filename) {
		File file = new File( filename);
		return run( file, file.getParentFile());
	}

	/**
	 * @param filename
	 * @param folder
	 * @return
	 */
	public static boolean run(String filename, String folder) {
		return run( new File( filename), new File( folder));
	}

	/**
	 * @param file
	 * @param folder
	 * @return
	 */
	public static boolean run(File file, File folder) {
		if ( !Constant.initialize_functionalObjectDirectories())
			return false;

		Environment.get_instance().enable_functional_object();

		if ( !JarFileProperties.get_instance().merge())
			return false;

		MainFrame mainFrame = MainFrame.get_instance();
		if ( !mainFrame.create())
			return false;

		if ( !mainFrame.export( file, folder))
			return false;

		mainFrame.dispose();

		if ( !copy_resource( folder))
			return false;

		return true;
	}

	/**
	 * @param folder
	 * @return
	 */
	private static boolean copy_resource(File folder) {
		String[] subFolders = new String[] { "common", "properties", "icon"};
		for ( String subFolder:subFolders) {
			File srcPath = new File( "../resource/" + subFolder);

			if ( !FileUtility.copy_all( srcPath, new File( folder.getParent(), "simulation/resource/" + subFolder)))
				return false;

			if ( !FileUtility.copy_all( srcPath, new File( folder, "run/resource/" + subFolder)))
				return false;
		}

		if ( !FileUtility.copy( new File( "../function/org.soars.pack/runtime/mac.jar"), new File( folder.getParent(), "simulation/mac.jar")))
			return false;

		return true;
	}
}
