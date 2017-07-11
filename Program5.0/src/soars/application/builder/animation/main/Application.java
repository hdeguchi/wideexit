/**
 * 
 */
package soars.application.builder.animation.main;

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
	private boolean init_instance() {
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

		return true;
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		try {
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
