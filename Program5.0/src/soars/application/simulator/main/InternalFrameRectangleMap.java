/**
 * 
 */
package soars.application.simulator.main;

import java.util.HashMap;

import javax.swing.JDesktopPane;
import javax.swing.JInternalFrame;

/**
 * @author kurata
 *
 */
public class InternalFrameRectangleMap extends HashMap {

	/**
	 *  
	 */
	public InternalFrameRectangleMap() {
		super();
	}

	/**
	 * @param desktopPane
	 */
	public void update(JDesktopPane desktopPane) {
		JInternalFrame[] internalFrames = desktopPane.getAllFrames();
		for ( int i = 0; i < internalFrames.length; ++i)
			put( internalFrames[ i], internalFrames[ i].getBounds());
	}
}
