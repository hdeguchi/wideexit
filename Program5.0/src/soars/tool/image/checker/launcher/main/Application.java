/**
 * 
 */
package soars.tool.image.checker.launcher.main;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import javax.swing.JOptionPane;

import soars.common.soars.environment.CommonEnvironment;
import soars.common.soars.environment.SoarsCommonEnvironment;
import soars.common.soars.tool.SoarsCommonTool;
import soars.common.utility.tool.clipboard.Clipboard;
import soars.common.utility.tool.common.Tool;

/**
 * @author kurata
 *
 */
public class Application {

	/**
	 * 
	 */
	public Application() {
		super();
	}

	/**
	 * @return
	 */
	public boolean init_instance() {
		File current_directory = new File( "");
		if ( null == current_directory)
			return false;

		File home_directory = new File( current_directory.getAbsolutePath() + Constant._home_directory);
		System.setProperty( Constant._soarsHome, home_directory.getAbsolutePath());

		File property_directory = SoarsCommonTool.get_default_property_directory();
		if ( null == property_directory)
			return false;

		if ( !write_test( property_directory))
			return false;

		System.setProperty( Constant._soarsProperty, property_directory.getAbsolutePath());

		File soars_properties = SoarsCommonEnvironment.get_instance().get();
		if ( null == soars_properties)
			return false;

		System.setProperty( Constant._soarsProperties, soars_properties.getAbsolutePath());

		return true;
	}

	/**
	 * @param property_directory
	 * @return
	 */
	private boolean write_test(File property_directory) {
		try {
			File file = File.createTempFile( "soars", "test", property_directory);
			if ( null == file)
				return false;

			file.delete();
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

	/**
	 * @return
	 */
	public boolean execute() {
		File current_directory = new File( "");
		if ( null == current_directory) {
			JOptionPane.showMessageDialog( null, "Could not start Image checker!", "SOARS", JOptionPane.ERROR_MESSAGE);
			return false;
		}

		String[] cmdarray = get_cmdarray( current_directory);
		if ( null == cmdarray) {
			JOptionPane.showMessageDialog( null, "Could not start Image checker!", "SOARS", JOptionPane.ERROR_MESSAGE);
			return false;
		}

		debug( "Image checker", System.getProperty( "os.name"), System.getProperty( "os.version"), cmdarray);

		Process process;
		try {
			process = ( Process)Runtime.getRuntime().exec(
				get_cmdarray( current_directory),
				null,
				current_directory.getAbsoluteFile());
		} catch (IOException e) {
			//e.printStackTrace();
			JOptionPane.showMessageDialog( null, "Could not start Image checker!", "SOARS", JOptionPane.ERROR_MESSAGE);
			return false;
		}

		return true;
	}

	/**
	 * @param current_directory
	 * @return
	 */
	private String[] get_cmdarray(File current_directory) {
		String home_directory_name = System.getProperty( Constant._soarsHome);
		String property_directory_name = System.getProperty( Constant._soarsProperty);
		String soars_properties = System.getProperty( Constant._soarsProperties);

		String memory_size = CommonEnvironment.get_instance().get_memory_size();
		String osname = System.getProperty( "os.name");

		List< String>list = new ArrayList< String>();
		if ( 0 <= osname.indexOf( "Windows")) {
			list.add( Constant._windowsJava);
		} else if ( 0 <= osname.indexOf( "Mac")) {
			list.add( Constant.get_mac_java_command());
			//if ( System.getProperty( Constant._system_default_file_encoding, "").equals( ""))
				list.add( "-Dfile.encoding=UTF-8");
			list.add( "-D" + Constant._systemDefaultFileEncoding + "=" + System.getProperty( Constant._systemDefaultFileEncoding, ""));
			list.add( "-X" + Constant._macScreenMenuName + "=SOARS Image checker");
			//list.add( "-D" + Constant._mac_screen_menu_name + "=SOARS Image checker");
			list.add( "-X" + Constant._macIconFilename + "=icon.png");
			list.add( "-D" + Constant._macScreenMenu + "=true");
		} else {
			list.add( Tool.get_java_command());
		}

		//list.add( "-Djava.endorsed.dirs=" + home_directory_name + "/../" + Constant._xerces_directory);
		list.add( "-D" + Constant._soarsHome + "=" + home_directory_name);
		list.add( "-D" + Constant._soarsProperty + "=" + property_directory_name);
		list.add( "-D" + Constant._soarsProperties + "=" + soars_properties);
		if ( !memory_size.equals( "0")) {
			list.add( "-D" + Constant._soarsMemorySize + "=" + memory_size);
			list.add( "-Xmx" + memory_size + "m");
		}
		list.add( "-jar");
		list.add( current_directory.getAbsolutePath() + "/" + Constant._image_checker_jar_filename);
		list.add( "-language");
		list.add( CommonEnvironment.get_instance().get( CommonEnvironment._localeKey, Locale.getDefault().getLanguage()));

		return list.toArray( new String[ 0]);
	}

	/**
	 * @param type
	 * @param osname
	 * @param osversion
	 * @param cmdarray
	 */
	private void debug(String type, String osname, String osversion, String[] cmdarray) {
		String text = ""; 
		text += ( "Type : " + type + Constant._lineSeparator);
		text += ( "OS : " + osname + " [" + osversion + "]" + Constant._lineSeparator);
		for ( int i = 0; i < cmdarray.length; ++i)
			text += ( "Parameter : " + cmdarray[ i] + Constant._lineSeparator);

		Clipboard.set( text);
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		Application application = new Application();
		if ( !application.init_instance())
			System.exit( 1);

		if ( !application.execute())
			System.exit( 1);
	}
}
