/**
 * 
 */
package soars.tool.animator.launcher.main;

import java.awt.Image;
import java.io.File;
import java.util.Locale;

import javax.swing.JFrame;

import soars.common.soars.environment.CommonEnvironment;
import soars.common.soars.tool.SoarsCommonTool;
import soars.common.utility.swing.window.SplashWindow;
import soars.common.utility.tool.resource.Resource;

/**
 * @author kurata
 *
 */
public class Application {


	/**
	 * 
	 */
	static public File _anm_file = null;

	/**
	 * 
	 */
	static public File _parameter_file = null;

	/**
	 * 
	 */
	public Application() {
		super();
	}

	/**
	 * @return
	 */
	private boolean init_instance() {
		if ( Constant._enableSplashWindow
			&& !SplashWindow.execute( Constant._resource_directory + "/image/splash/splash.gif", true))
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

		Image image = Resource.load_image_from_resource( Constant._resource_directory + "/image/icon/icon.png", getClass());
		if ( null != image)
			mainFrame.setIconImage( image);

		return true;
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		String home_directory_name = System.getProperty( Constant._soarsHome);
		if ( null == home_directory_name || home_directory_name.equals( "")) {
			File home_directory = new File( "");
			if ( null == home_directory)
				System.exit( 1);

			System.setProperty( Constant._soarsHome, home_directory.getAbsolutePath());
		}

		String property_directory_name = System.getProperty( Constant._soarsProperty);
		if ( null == property_directory_name || property_directory_name.equals( "")) {
			File property_directory = SoarsCommonTool.get_default_property_directory();
			if ( null == property_directory)
				System.exit( 1);

			System.setProperty( Constant._soarsProperty, property_directory.getAbsolutePath());
		}

		boolean locale = false;
		for ( int i = 0; i < args.length; ++i) {
			if ( args[ i].equals( "-language") && ( i + 1 < args.length) && !args[ i + 1].equals( "")) {
				JFrame.setDefaultLookAndFeelDecorated( false);
				//JFrame.setDefaultLookAndFeelDecorated( true);
				Locale.setDefault( new Locale( args[ i + 1]));
				CommonEnvironment.get_instance().set( CommonEnvironment._localeKey, args[ i + 1]);
				locale = true;
			} else if ( args[ i].equals( "-anm") && ( i + 1 < args.length) && !args[ i + 1].equals( "")) {
				_anm_file = new File( args[ i + 1]);
			} else if ( args[ i].equals( "-parameter") && ( i + 1 < args.length) && !args[ i + 1].equals( "")) {
				_parameter_file = new File( args[ i + 1]);
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
		if ( !application.init_instance())
			System.exit( 1);
	}
}
