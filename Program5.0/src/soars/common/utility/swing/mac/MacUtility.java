/**
 * 
 */
package soars.common.utility.swing.mac;

import java.awt.Frame;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JOptionPane;

import soars.common.utility.tool.reflection.Reflection;


/**
 * @author kurata
 *
 */
public class MacUtility {

	/**
	 * @param macScreenMenuHandler
	 * @param frame
	 * @param title
	 * @return
	 */
	public static boolean setup_screen_menu_handler(IMacScreenMenuHandler macScreenMenuHandler, Frame frame, String title) {
		if ( 0 > System.getProperty( "os.name").indexOf( "Mac"))
			return true;

		List resultList = new ArrayList();
		if ( !Reflection.execute_static_method( "soars.common.utility.swing.mac.MacScreenMenuHandler", "setup", new Class[] { IMacScreenMenuHandler.class}, new Object[] { macScreenMenuHandler}, resultList)
			|| resultList.isEmpty() || null == resultList.get( 0) || !( resultList.get( 0) instanceof Boolean) || !(( Boolean)resultList.get( 0)).booleanValue()) {
			JOptionPane.showMessageDialog( frame,
				"Mac error : Reflection.execute_static_method( ... )\n"
				+ " Class name : soars.common.utility.swing.mac.MacScreenMenuHandler\n"
				+ " Method name : setup\n",
				title,
			JOptionPane.ERROR_MESSAGE);
			return false;
		}

		return true;
	}
}
