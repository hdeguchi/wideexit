/*
 * Created on 2005/04/20
 */
package soars.application.visualshell.main;

import java.awt.Image;
import java.io.File;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import soars.application.visualshell.object.arbitrary.JarFileProperties;
import soars.common.soars.environment.CommonEnvironment;
import soars.common.soars.tool.SoarsCommonTool;
import soars.common.utility.swing.window.SplashWindow;
import soars.common.utility.tool.resource.Resource;

import com.sshtools.j2ssh.configuration.ConfigurationException;
import com.sshtools.j2ssh.configuration.ConfigurationLoader;

/**
 * The Visual Shell main class.
 * @author kurata / SOARS project
 */
public class Application {

	/**
	 * Returns true if this application is initialized successfully.
	 * @param initialData the name of the file which contains the initial data
	 * @param removeInitialData whether to remove the file which contains the initial data
	 * @param executePlugins the list for the names of the plugins which are enabled
	 * @param soarsFilename the name of the file which contains the SOARS data
	 * @param direct
	 * @return true if this application is initialized successfully
	 */
	public boolean init_instance(String initialData, boolean removeInitialData, List<String> executePlugins, String soarsFilename, boolean direct) {
//		if ( 0 <= System.getProperty( "os.name").indexOf( "Mac")) {
//			Border border = UIManager.getBorder( "ComboBox.border");
//			if ( null != border)
//				UIManager.put( "ComboBox.border", new CompoundBorder( new EmptyBorder( 2, 0, 0, 0), border));
//			else
//				UIManager.put( "ComboBox.border", new EmptyBorder( 2, 0, 0, 0));
//		}

		try {
			ConfigurationLoader.initialize( false);
		} catch (ConfigurationException e) {
			e.printStackTrace();
			return false;
		}

		if ( Constant._enableSplashWindow
			&& !SplashWindow.execute( Constant._resourceDirectory + "/image/splash/splash.gif", true))
			return false;

		if ( !Constant.initialize_functionalObjectDirectories())
			return false;

		if ( Environment.get_instance().is_functional_object_enable()) {
			if ( !SplashWindow.execute( Constant._resourceDirectory + "/image/splash/splash.gif", true))
				return false;

//			if ( !JarFileProperties.get_instance().serialize()) {
//				SplashWindow.terminate();
//				return false;
//			}

			if ( !JarFileProperties.get_instance().merge()) {
				SplashWindow.terminate();
				return false;
			}
		}

		if ( !SoarsCommonTool.setup_look_and_feel()) {
			SplashWindow.terminate();
			return false;
		}

		MainFrame mainFrame = MainFrame.get_instance();

		if ( !mainFrame.create( direct)) {
			SplashWindow.terminate();
			return false;
		}

		SplashWindow.terminate();

		Image image = Resource.load_image_from_resource( Constant._resourceDirectory + "/image/icon/icon.png", getClass());
		if ( null != image)
			mainFrame.setIconImage( image);

		if ( !initialData.equals( "")) {
			File file = new File( initialData);
			if ( !file.exists() || !file.canRead())
				JOptionPane.showMessageDialog(
					mainFrame,
					"Could not read initial data!",
					ResourceManager.get_instance().get( "application.title"),
					JOptionPane.ERROR_MESSAGE );
			else
				mainFrame.import_initial_data( file, true);

			if ( removeInitialData)
				file.delete();
		} else if ( !soarsFilename.equals( "")) {
			File file = new File( soarsFilename);
			if ( !file.exists() || !file.canRead())
				JOptionPane.showMessageDialog(
					mainFrame,
					"Could not read soars data!",
					ResourceManager.get_instance().get( "application.title"),
					JOptionPane.ERROR_MESSAGE );
			else {
				if ( !mainFrame.open( file))
					JOptionPane.showMessageDialog(
						mainFrame,
						"Could not read soars data!",
						ResourceManager.get_instance().get( "application.title"),
						JOptionPane.ERROR_MESSAGE );
				else {
					if ( direct) {
						mainFrame.on_run_simulator( null);
						try {
							Thread.sleep( 1000);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
						mainFrame.terminate();
					}
				}
			}
		}

		mainFrame.execute_plugin( executePlugins);

		return true;
	}

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
			String initialData = "";
			boolean removeInitialData = false;
			List<String> executePlugins = new ArrayList<String>();
			String soarsFilename = "";
			boolean direct = false;
			for ( int i = 0; i < args.length; ++i) {
				if ( args[ i].equals( "-language") && ( i + 1 < args.length) && !args[ i + 1].equals( "")) {
					JFrame.setDefaultLookAndFeelDecorated( false);
					//JFrame.setDefaultLookAndFeelDecorated( true);
					Locale.setDefault( new Locale( args[ i + 1]));
					CommonEnvironment.get_instance().set( CommonEnvironment._localeKey, args[ i + 1]);
					locale = true;
				} else if ( args[ i].equals( "-initial_data") && ( i + 1 < args.length) && !args[ i + 1].equals( "")) {
					initialData = args[ i + 1];
				} else if ( args[ i].equals( "-remove_initial_data")) {
					removeInitialData = true;
				} else if ( args[ i].equals( "-enable_functional_object")) {
					Environment.get_instance().enable_functional_object();
				} else if ( args[ i].equals( "-enable_plugin") && ( i + 1 < args.length) && !args[ i + 1].equals( "")) {
					Environment.get_instance().append_enable_plugin( args[ i + 1]);
				} else if ( args[ i].equals( "-execute_plugin") && ( i + 1 < args.length) && !args[ i + 1].equals( "")) {
					executePlugins.add( args[ i + 1]);
				} else if ( args[ i].equals( "-soars") && ( i + 1 < args.length) && !args[ i + 1].equals( "")) {
					soarsFilename = args[ i + 1];
				} else if ( args[ i].equals( "-direct") && ( i + 1 < args.length) && !args[ i + 1].equals( "")) {
					direct = args[ i + 1].equals( "true");
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
			if ( !application.init_instance( initialData, removeInitialData, executePlugins, soarsFilename, direct))
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
