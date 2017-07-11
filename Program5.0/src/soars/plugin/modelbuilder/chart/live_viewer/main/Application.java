/*
 * Created on 2004/10/07
 */
package soars.plugin.modelbuilder.chart.live_viewer.main;

import java.util.Locale;

import javax.swing.JFrame;

import soars.common.soars.tool.SoarsCommonTool;

/**
 * @author kurata
 */
public class Application {


	/**
	 * 
	 */
	static public final String _title = "Chart LiveViewer";

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

		MainFrame mainFrame = new MainFrame( _title);

		if ( !mainFrame.create()) {
			return false;
		}

		return true;
	}

    public static void main(String[] args) {
		JFrame.setDefaultLookAndFeelDecorated( true);
		Locale.setDefault( new Locale( "en"));

		Application application = new Application();
		if ( !application.init_instance())
			System.exit( 1);
	}
}
