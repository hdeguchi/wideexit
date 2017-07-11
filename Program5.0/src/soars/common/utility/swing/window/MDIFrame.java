/*
 * 2004/12/17
 */
package soars.common.utility.swing.window;

import java.awt.GraphicsConfiguration;
import java.awt.HeadlessException;
import java.awt.Rectangle;

import javax.swing.JDesktopPane;

/**
 * @author kurata
 */
public class MDIFrame extends Frame {

	/**
	 * 
	 */
	protected JDesktopPane _desktopPane = null;

	/**
	 * @throws java.awt.HeadlessException
	 */
	public MDIFrame() throws HeadlessException {
		super();
	}

	/**
	 * @param arg0
	 */
	public MDIFrame(GraphicsConfiguration arg0) {
		super(arg0);
	}

	/**
	 * @param arg0
	 * @throws java.awt.HeadlessException
	 */
	public MDIFrame(String arg0) throws HeadlessException {
		super(arg0);
	}

	/**
	 * @param arg0
	 * @param arg1
	 */
	public MDIFrame(String arg0, GraphicsConfiguration arg1) {
		super(arg0, arg1);
	}

	/**
	 * @return
	 */
	public boolean create() {
		if ( !super.create())
			return false;

		return true;
	}

	/**
	 * @return
	 */
	protected boolean on_create() {
		if ( !super.on_create())
			return false;

		_desktopPane = new JDesktopPane();
		getContentPane().add( _desktopPane);

		return true;
	}

	/**
	 * @return
	 */
	public Rectangle get_client_rectangle() {
		if ( null == _desktopPane)
			return null;

		return new Rectangle( 0, 0, _desktopPane.getBounds().width, _desktopPane.getBounds().height);
	}
}
