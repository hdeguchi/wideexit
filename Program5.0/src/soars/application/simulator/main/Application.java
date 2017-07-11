/**
 * 
 */
package soars.application.simulator.main;

import java.awt.Image;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.io.Reader;
import java.io.StringWriter;
import java.util.Locale;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import soars.common.soars.environment.CommonEnvironment;
import soars.common.soars.tool.SoarsCommonTool;
import soars.common.utility.swing.window.SplashWindow;
import soars.common.utility.tool.clipboard.Clipboard;
import soars.common.utility.tool.file.FileUtility;
import soars.common.utility.tool.file.ZipUtility;
import soars.common.utility.tool.resource.Resource;

/**
 * @author kurata
 *
 */
public class Application {

	/**
	 * 
	 */
	static private Object _lock = new Object();

	/**
	 * 
	 */
	static private Application _application = null;

	/**
	 * 
	 */
	static public boolean _demo = false;

	/**
	 * @return
	 */
	public static Application get_instance() {
		synchronized( _lock) {
			if ( null == _application) {
				_application = new Application();
			}
		}
		return _application;
	}

	/**
	 * 
	 */
	public Application() {
		super();
	}

	/**
	 * @param soarsFilename
	 * @param id
	 * @param title
	 * @param visualShellTitle
	 * @return
	 */
	private boolean init_instance(String soarsFilename, String id, String title, String visualShellTitle) {
		if ( null == soarsFilename || soarsFilename.equals( ""))
			return false;

		if ( null == id || id.equals( ""))
			return false;

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

		return mainFrame.open( new File( soarsFilename), id, title, visualShellTitle);
	}

	/**
	 * @param reader
	 * @param parentDirectory
	 * @param experiment
	 * @param logFolderPath
	 * @param visualShellTitle
	 * @return
	 */
	public boolean init_instance(Reader reader, File parentDirectory, String soarsFilename, String experiment, String logFolderPath, String visualShellTitle) {
		if ( Constant._enableSplashWindow
			&& !SplashWindow.execute( Constant._resourceDirectory + "/image/splash/splash.gif", true))
			return false;

		if ( !SoarsCommonTool.setup_look_and_feel()) {
			SplashWindow.terminate();
			return false;
		}

		MainFrame mainFrame = MainFrame.get_instance();
		if ( !mainFrame.create( parentDirectory, logFolderPath)) {
			SplashWindow.terminate();
			return false;
		}

		SplashWindow.terminate();

		Image image = Resource.load_image_from_resource( Constant._resourceDirectory + "/image/icon/icon.png", getClass());
		if ( null != image)
			mainFrame.setIconImage( image);

		return mainFrame.start( reader, experiment, visualShellTitle, soarsFilename);
	}

	/**
	 * 
	 */
	public void exit_instance() {
		System.exit( 0);
	}

	/**
	 * @param args
	 * @param inputStream
	 */
	public static boolean start(String[] args, InputStream inputStream) {
		// TODO これはどこから呼ばれるのか？
		if ( null == args)
			return false;

		if ( null == inputStream)
			return false;

		String homeDirectoryName = System.getProperty( Constant._soarsHome);
		if ( null == homeDirectoryName || homeDirectoryName.equals( "")) {
			File homeDirectory = new File( "");
			if ( null == homeDirectory)
				return false;

			System.setProperty( Constant._soarsHome, homeDirectory.getAbsolutePath());
		}

		String propertyDirectoryName = System.getProperty( Constant._soarsProperty);
		if ( null == propertyDirectoryName || propertyDirectoryName.equals( "")) {
			File propertyDirectory = SoarsCommonTool.get_system_property_directory();
			if ( null == propertyDirectory)
				return false;

			System.setProperty( Constant._soarsProperty, propertyDirectory.getAbsolutePath());
		}

		boolean locale = false;
		String parentDirectory = null;
		String zip = null;
		String soarsFile = "";
		String experiment = "";
		String logFolderPath = "";
		String visualShellTitle = "";
		boolean remove = false;
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
			} else if ( args[ i].equals( "-zip") && ( i + 1 < args.length) && !args[ i + 1].equals( "")) {
				zip = args[ i + 1];
			} else if ( args[ i].equals( "-vml") && ( i + 1 < args.length) && !args[ i + 1].equals( "")) {
				soarsFile = args[ i + 1];
			} else if ( args[ i].equals( "-experiment") && ( i + 1 < args.length) && !args[ i + 1].equals( "")) {
				experiment = args[ i + 1];
			} else if ( args[ i].equals( "-log") && ( i + 1 < args.length) && !args[ i + 1].equals( "")) {
				logFolderPath = args[ i + 1];
			} else if ( args[ i].equals( "-visualshell_title") && ( i + 1 < args.length) && !args[ i + 1].equals( "")) {
				visualShellTitle = args[ i + 1];
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

		File directory = get_parent_directory( parentDirectory, zip);
		if ( null == directory)
			return false;

		Reader reader = get_reader( inputStream, directory);
		if ( null == reader)
			return false;

		//String memorySize = System.getProperty( Constant._soarsMemorySize);
		//JOptionPane.showMessageDialog( null, ( null == memorySize ? "No property" : memorySize), "Simulator", JOptionPane.INFORMATION_MESSAGE);

		if ( !Application.get_instance().init_instance( reader, directory, soarsFile, experiment, logFolderPath, visualShellTitle))
			return false;

		return true;
	}

	/**
	 * @param inputStream
	 * @param parentDirectory
	 * @return
	 */
	private static Reader get_reader(InputStream inputStream, File parentDirectory) {
		String script = FileUtility.read_text_from_file( inputStream);
		if ( null == script)
			return null;

		String userDataDirectory = ( parentDirectory.getAbsolutePath().replaceAll( "\\\\", "/") + "/simulator/" + Constant._userDataDirectory);
		System.setProperty( Constant._soarsUserDataDirectory, userDataDirectory);

		userDataDirectory += "/"; 

		//JOptionPane.showMessageDialog( null, userDataDirectory, "Simulator", JOptionPane.INFORMATION_MESSAGE);

		while ( 0 <= script.indexOf( Constant._reservedUserDataDirectory))
			script = script.replace( Constant._reservedUserDataDirectory, userDataDirectory);

		try {
			File newFile = File.createTempFile( "soars_", ".sor");
			newFile.deleteOnExit();
			if ( !FileUtility.write_text_to_file( newFile, script))
				return null;

			return new FileReader( newFile);
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			//String memorySize = System.getProperty( Constant._soarsMemorySize);
			//JOptionPane.showMessageDialog( null, ( null == memorySize ? "No property" : memorySize), "Simulator", JOptionPane.INFORMATION_MESSAGE);

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
			String script = null;
			String parentDirectory = null;
			String zip = null;
			String soarsFilename = "";
			String id = "";
			String experiment = "";
			String logFolderPath = "";
			String title = "";
			String visualShellTitle = "";
			boolean remove = false;
			for ( int i = 0; i < args.length; ++i) {
				if ( args[ i].equals( "-language") && ( i + 1 < args.length) && !args[ i + 1].equals( "")) {
					JFrame.setDefaultLookAndFeelDecorated( false);
					//JFrame.setDefaultLookAndFeelDecorated( true);
					Locale.setDefault( new Locale( args[ i + 1]));
					CommonEnvironment.get_instance().set( CommonEnvironment._localeKey, args[ i + 1]);
					locale = true;
					//break;
				} else if ( args[ i].equals( "-script") && ( i + 1 < args.length) && !args[ i + 1].equals( "")) {
					script = args[ i + 1];
				} else if ( args[ i].equals( "-parent_directory") && ( i + 1 < args.length) && !args[ i + 1].equals( "")) {
					parentDirectory = args[ i + 1];
				} else if ( args[ i].equals( "-zip") && ( i + 1 < args.length) && !args[ i + 1].equals( "")) {
					zip = args[ i + 1];
				} else if ( args[ i].equals( "-soars") && ( i + 1 < args.length) && !args[ i + 1].equals( "")) {
					soarsFilename = args[ i + 1];
				} else if ( args[ i].equals( "-id") && ( i + 1 < args.length) && !args[ i + 1].equals( "")) {
					id = args[ i + 1];
				} else if ( args[ i].equals( "-experiment") && ( i + 1 < args.length) && !args[ i + 1].equals( "")) {
					experiment = args[ i + 1];
				} else if ( args[ i].equals( "-log") && ( i + 1 < args.length) && !args[ i + 1].equals( "")) {
					logFolderPath = args[ i + 1];
				} else if ( args[ i].equals( "-title") && ( i + 1 < args.length) && !args[ i + 1].equals( "")) {
					title = args[ i + 1];
				} else if ( args[ i].equals( "-visualshell_title") && ( i + 1 < args.length) && !args[ i + 1].equals( "")) {
					visualShellTitle = args[ i + 1];
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

			if ( null == script) {
				if ( !Application.get_instance().init_instance( soarsFilename, id, title, visualShellTitle))
					System.exit( 1);
			} else {
				File file = new File( script);
				if ( !file.exists() || !file.isFile() || !file.canRead())
					System.exit( 1);

				File directory = get_parent_directory( parentDirectory, zip);
				if ( null == directory || !directory.exists() || !directory.isDirectory())
					System.exit( 1);

				Reader reader = null;
				if ( null != parentDirectory) {
					try {
						reader = new FileReader( file);
					} catch (FileNotFoundException e) {
						e.printStackTrace();
						System.exit( 1);
					}
				} else if ( null != zip) {
					reader = get_reader( file, directory);
					if ( null == reader)
						System.exit( 1);
				} else
					System.exit( 1);

				if ( _demo && soarsFilename.equals( "")) {
					soarsFilename = System.getProperty( "VML_FILE");
					visualShellTitle = VisualShellSaxLoader.get_title( soarsFilename);
					debug( args);
				}

				if ( !Application.get_instance().init_instance( reader, directory, soarsFilename, experiment, logFolderPath, visualShellTitle))
					System.exit( 1);
			}
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

	/**
	 * @param parentDirectory
	 * @param zip
	 * @return
	 */
	private static File get_parent_directory(String parentDirectory, String zip) {
		if ( null != parentDirectory)
			return new File( parentDirectory);

		if ( null != zip) {
			File directory = SoarsCommonTool.make_parent_directory();
			if ( null == directory)
				return null;

			if ( !ZipUtility.decompress( new File( zip), directory)) {
				FileUtility.delete( directory, true);
				return null;
			}

			return directory;
		}

		return null;
	}

	/**
	 * @param file
	 * @param parentDirectory
	 * @return
	 */
	private static Reader get_reader(File file, File parentDirectory) {
		String script = FileUtility.read_text_from_file( file);
		if ( null == script)
			System.exit( 1);

		String userDataDirectory = ( parentDirectory.getAbsolutePath().replaceAll( "\\\\", "/") + "/simulator/" + Constant._userDataDirectory);
		System.setProperty( Constant._soarsUserDataDirectory, userDataDirectory);

		userDataDirectory += "/"; 

		//JOptionPane.showMessageDialog( null, userDataDirectory, "Simulator", JOptionPane.INFORMATION_MESSAGE);

		while ( 0 <= script.indexOf( Constant._reservedUserDataDirectory))
			script = script.replace( Constant._reservedUserDataDirectory, userDataDirectory);

		if ( !FileUtility.write_text_to_file( file, script))
			return null;

		Reader reader = null;
		try {
			reader = new FileReader( file);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return null;
		}

		return reader;
	}

	/**
	 * @param args
	 */
	private static void debug(String[] args) {
		String homeDirectoryName = System.getProperty( Constant._soarsHome);
		String propertyDirectoryName = System.getProperty( Constant._soarsProperty);

		String memorySize = CommonEnvironment.get_instance().get_memory_size();
		String osname = System.getProperty( "os.name");

		String text = ""; 
		text += ( "Type : Simulator demo" + Constant._lineSeparator);
		text += ( "OS : " + osname + " [" + System.getProperty( "os.version") + "]" + Constant._lineSeparator);

		text += ( "Parameter : -D" + Constant._soarsHome + "=" + homeDirectoryName + Constant._lineSeparator);
		text += ( "Parameter : -D" + Constant._soarsProperty + "=" + propertyDirectoryName + Constant._lineSeparator);
		text += ( "Parameter : -D" + Constant._soarsMemorySize + "=" + memorySize + Constant._lineSeparator);

		for ( int i = 0; i < args.length; ++i)
			text += ( "Parameter : " + args[ i] + Constant._lineSeparator);

		Clipboard.set( text);
	}
}
