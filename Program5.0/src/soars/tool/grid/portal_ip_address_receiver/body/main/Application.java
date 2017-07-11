/**
 * 
 */
package soars.tool.grid.portal_ip_address_receiver.body.main;

import java.awt.Image;
import java.io.File;

import javax.swing.JFrame;

import soars.common.soars.tool.SoarsCommonTool;
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
	public boolean init_instance() {
		if ( !SoarsCommonTool.setup_look_and_feel())
			return false;

		MainFrame mainFrame = MainFrame.get_instance();

		if ( !mainFrame.create())
			return false;

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
			File home_directory = new File( "../../../program");
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

		JFrame.setDefaultLookAndFeelDecorated( false);
		//JFrame.setDefaultLookAndFeelDecorated( true);

		Application application = new Application();
		if ( !application.init_instance())
			System.exit( 1);
	}
}
