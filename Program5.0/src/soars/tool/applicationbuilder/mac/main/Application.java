/**
 * 
 */
package soars.tool.applicationbuilder.mac.main;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JOptionPane;

import soars.common.soars.constant.CommonConstant;
import soars.common.utility.tool.clipboard.Clipboard;
import soars.common.utility.tool.common.Tool;

/**
 * @author kurata
 *
 */
public class Application {

	/**
	 * @return
	 */
	private boolean init_instance() {
		try {
			Process process = ( Process)Runtime.getRuntime().exec( get_cmdarray(), null);
		} catch (IOException e) {
			//e.printStackTrace();
			return false;
		}
		return true;
	}

	/**
	 * @return
	 */
	private String[] get_cmdarray() {
		String osname = System.getProperty( "os.name");

		List< String>list = new ArrayList< String>();
		if ( 0 <= osname.indexOf( "Windows")) {
			list.add( CommonConstant._windowsJava);
		} else if ( 0 <= osname.indexOf( "Mac")) {
			list.add( CommonConstant.get_mac_java_command());
			list.add( "-Dfile.encoding=UTF-8");
			list.add( "-D" + CommonConstant._systemDefaultFileEncoding + "=" + System.getProperty( CommonConstant._systemDefaultFileEncoding, ""));
			list.add( "-X" + CommonConstant._macScreenMenuName + "=SOARS Application Builder (simulation)");
			list.add( "-X" + CommonConstant._macIconFilename + "=resource/icon/application/manager/soars/icon.png");
			list.add( "-D" + CommonConstant._macScreenMenu + "=true");
		} else {
			list.add( Tool.get_java_command());
		}

		list.add( "-Xmx" + CommonConstant._defaultMemorySize + "m");
		list.add( "-jar");
		list.add( "run.jar");

		String[] cmdarray = list.toArray( new String[ 0]);

		debug( "SOARS Application Builder (simulation)", osname, System.getProperty( "os.version"), cmdarray);

		return cmdarray;
	}

	/**
	 * @param type
	 * @param osname
	 * @param osversion
	 * @param cmdarray
	 */
	protected static void debug(String type, String osname, String osversion, String[] cmdarray) {
		String text = ""; 
		text += ( "Type : " + type + CommonConstant._lineSeparator);
		text += ( "OS : " + osname + " [" + osversion + "]" + CommonConstant._lineSeparator);
		for ( int i = 0; i < cmdarray.length; ++i)
			text += ( "Parameter : " + cmdarray[ i] + CommonConstant._lineSeparator);

		Clipboard.set( text);
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		Application application = new Application();
		if ( !application.init_instance()) {
			JOptionPane.showMessageDialog( null,
				"Could not start run.jar",
				"Application Builder",
				JOptionPane.ERROR_MESSAGE);
			System.exit( 1);
		}
	}
}
