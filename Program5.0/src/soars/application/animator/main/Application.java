/*
 * Created on 2005/02/14
 */
package soars.application.animator.main;

import java.awt.Image;
import java.io.File;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Locale;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import soars.common.soars.environment.CommonEnvironment;
import soars.common.soars.tool.SoarsCommonTool;
import soars.common.utility.swing.window.SplashWindow;
import soars.common.utility.tool.resource.Resource;

/**
 * The Animator main class.
 * @author kurata / SOARS project
 */
public class Application {

//	/**
//	 * Title string of the main window.
//	 */
//	static public String _title = "";

	/**
	 * True for the demo mode.
	 */
	static public boolean _demo = false;

//	/**
//	 * Returns the appropriate title string.
//	 * @return
//	 */
//	public static String get_title() {
//		return ( _title.equals( "") ? "" : ( " - " + _title));
//	}

	/**
	 * Returns true if this application is initialized successfully.
	 * @param parentDirectory the name of the specified directory which contains log files
	 * @param rootDirectory the name of the specified directory which contains log files
	 * @param soarsFilename 
	 * @param id
	 * @param index
	 * @param title
	 * @param simulatorTitle
	 * @param visualShellTitle
	 * @param anmFilename the name of the specified Animator data file
	 * @param agdFilename the name of the specified Animator graphics data file 
	 * @return true if this application is initialized successfully
	 */
	public boolean init_instance(String parentDirectory, String rootDirectory, String soarsFilename, String id, String index, String title, String simulatorTitle, String visualShellTitle, String anmFilename, String agdFilename) {
		if ( Constant._enableSplashWindow
			&& !SplashWindow.execute( Constant._resourceDirectory + "/image/splash/splash.gif", true))
			return false;

		if ( !SoarsCommonTool.setup_look_and_feel()) {
			SplashWindow.terminate();
			return false;
		}

		MainFrame mainFrame = MainFrame.get_instance();

		if ( !mainFrame.create()) {
			SplashWindow.terminate();
			return false;
		}

		SplashWindow.terminate();

		Image image = Resource.load_image_from_resource( Constant._resourceDirectory + "/image/icon/icon.png", getClass());
		if ( null != image)
			mainFrame.setIconImage( image);

		if ( null != parentDirectory && null != rootDirectory) {
			if ( !mainFrame.load( parentDirectory, rootDirectory, soarsFilename, id, simulatorTitle, visualShellTitle))
				return true;

//			if ( !import_graphic_data( agdFilename))
//				return false;

		} else {
			if ( null != soarsFilename && null != id && null != index) {
				File file = new File( soarsFilename);
				if ( !file.exists() || !file.isFile() || !file.canRead())
					return false;

				if ( !mainFrame.open( file, id, index, title, simulatorTitle, visualShellTitle))
					return true;
//			} else {
//				if ( null == anmFilename)
//					return false;
//
//				File file = new File( anmFilename);
//				if ( !file.exists() || !file.isFile() || !file.canRead())
//					return false;
//
//				if ( !mainFrame.open( file))
//					return true;
//
//				if ( !import_graphic_data( agdFilename))
//					return false;
			}
		}

		return true;
	}

//	/**
//	 * @param agdFilename
//	 * @return
//	 */
//	private boolean import_graphic_data(String agdFilename) {
//		if ( null == agdFilename)
//			return true;
//
//		File file = new File( agdFilename);
//		if ( !file.exists() || !file.isFile() || !file.canRead())
//			return false;
//
//		return ( 0 <= Administrator.get_instance().import_graphic_data( file));
//	}

	/**
	 * The entry point of this application.
	 * @param args the command line arguments
	 */
	public static void main(String[] args) {
		try {
			String homeDirectoryName = System.getProperty( Constant._soarsHome);
			if ( null == homeDirectoryName || homeDirectoryName.equals( "")) {
				File homeDirectory = new File( "");
				if ( null == homeDirectory)
					System.exit( 1);

				System.setProperty( Constant._soarsHome, homeDirectory.getAbsolutePath());
			}

			String propertyDirectoryName = System.getProperty( Constant._soarsProperty);
			if ( null == propertyDirectoryName || propertyDirectoryName.equals( "")) {
				File propertyDirectory = SoarsCommonTool.get_default_property_directory();
				if ( null == propertyDirectory)
					System.exit( 1);

				System.setProperty( Constant._soarsProperty, propertyDirectory.getAbsolutePath());
			}

			boolean locale = false;
			String parentDirectory = null;
			String rootDirectory = null;
			String soarsFilename = null;
			String id = null;
			String index = null;
			String title = "";
			String simulatorTitle = "";
			String visualShellTitle = "";
			String anmFilename = null;
			String agdFilename = null;
			for ( int i = 0; i < args.length; ++i) {
				if ( args[ i].equals( "-language") && ( i + 1 < args.length) && !args[ i + 1].equals( "")) {
					JFrame.setDefaultLookAndFeelDecorated( false);
					//JFrame.setDefaultLookAndFeelDecorated( true);
					Locale.setDefault( new Locale( args[ i + 1]));
					CommonEnvironment.get_instance().set( CommonEnvironment._localeKey, args[ i + 1]);
					locale = true;
					//break;
				} else if ( args[ i].equals( "-parent_directory") && ( i + 1 < args.length) && !args[ i + 1].equals( "")) {
					parentDirectory = args[ i + 1];
				} else if ( args[ i].equals( "-root_directory") && ( i + 1 < args.length) && !args[ i + 1].equals( "")) {
					rootDirectory = args[ i + 1];
				} else if ( args[ i].equals( "-soars") && ( i + 1 < args.length) && !args[ i + 1].equals( "")) {
					soarsFilename = args[ i + 1];
				} else if ( args[ i].equals( "-id") && ( i + 1 < args.length) && !args[ i + 1].equals( "")) {
					id = args[ i + 1];
				} else if ( args[ i].equals( "-index") && ( i + 1 < args.length) && !args[ i + 1].equals( "")) {
					index = args[ i + 1];
				} else if ( args[ i].equals( "-anm") && ( i + 1 < args.length) && !args[ i + 1].equals( "")) {
					anmFilename = args[ i + 1];
				} else if ( args[ i].equals( "-agd") && ( i + 1 < args.length) && !args[ i + 1].equals( "")) {
					agdFilename = args[ i + 1];
				} else if ( args[ i].equals( "-title") && ( i + 1 < args.length) && !args[ i + 1].equals( "")) {
					title = args[ i + 1];
				} else if ( args[ i].equals( "-simulator_title") && ( i + 1 < args.length) && !args[ i + 1].equals( "")) {
					simulatorTitle = args[ i + 1];
				} else if ( args[ i].equals( "-visualshell_title") && ( i + 1 < args.length) && !args[ i + 1].equals( "")) {
					visualShellTitle = args[ i + 1];
				} else if ( args[ i].equals( "-soars_version") && ( i + 1 < args.length) && !args[ i + 1].equals( "")) {
					Constant._soarsVersion = args[ i + 1];
				} else if ( args[ i].equals( "-copyright") && ( i + 1 < args.length) && !args[ i + 1].equals( "")) {
					Constant._copyright = args[ i + 1];
				} else if ( args[ i].equals( "-demo")) {
					_demo = true;
				}
			}

			if ( !locale) {
				JFrame.setDefaultLookAndFeelDecorated( false);
				//JFrame.setDefaultLookAndFeelDecorated( true);
				Locale.setDefault(
					new Locale( CommonEnvironment.get_instance().get(
						CommonEnvironment._localeKey, Locale.getDefault().getLanguage())));
			}

			Application application = new Application();
			if ( !application.init_instance( parentDirectory, rootDirectory, soarsFilename, id, index, title, simulatorTitle, visualShellTitle, anmFilename, agdFilename))
				System.exit( 1);

		} catch (Throwable ex) {
			StringWriter stringWriter = new StringWriter();
			PrintWriter printWriter = new PrintWriter( stringWriter);
			ex.printStackTrace( printWriter);
			JOptionPane.showMessageDialog( null,
				stringWriter.toString(),
				ResourceManager.get_instance().get( "application.title"),
				JOptionPane.ERROR_MESSAGE);
		}
	}
}
