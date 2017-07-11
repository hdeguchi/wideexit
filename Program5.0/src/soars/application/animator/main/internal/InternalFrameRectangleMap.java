/**
 * 
 */
package soars.application.animator.main.internal;

import java.awt.Rectangle;
import java.util.HashMap;

import javax.swing.JDesktopPane;
import javax.swing.JInternalFrame;

/**
 * @author kurata
 *
 */
public class InternalFrameRectangleMap extends HashMap<JInternalFrame, Rectangle> {

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
