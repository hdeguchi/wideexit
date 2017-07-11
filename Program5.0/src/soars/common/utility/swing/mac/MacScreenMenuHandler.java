/**
 * 
 */
package soars.common.utility.swing.mac;

import com.apple.eawt.Application;
import com.apple.eawt.ApplicationAdapter;
import com.apple.eawt.ApplicationEvent;


/**
 * @author kurata
 *
 */
@SuppressWarnings("deprecation")
public class MacScreenMenuHandler {

	/**
	 * @param macScreenMenuHandler
	 * @return
	 */
	public static boolean setup(final IMacScreenMenuHandler macScreenMenuHandler) {
		Application application = Application.getApplication();
		if ( null == application)
			return false;

		application.addApplicationListener(new ApplicationAdapter() {
			public void handleAbout(ApplicationEvent arg0) {
				macScreenMenuHandler.on_mac_about();
				arg0.setHandled( true);		// OSXデフォルトのダイアログを表示させない
			}
			public void handleQuit(ApplicationEvent arg0) {
				macScreenMenuHandler.on_mac_quit();
				arg0.setHandled( false);	// 自動的に終了させない
			}
		});
		return true;
	}
}
