/**
 * 
 */
package soars.common.utility.swing.window;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;

import javax.swing.JWindow;

import soars.common.utility.tool.resource.Resource;

/**
 * @author kurata
 *
 */
public class SplashWindow extends JWindow {

	/**
	 * 
	 */
	static private SplashWindow _splashWindow = null;

	/**
	 * 
	 */
	private BufferedImage _bufferedImage = null;

	/**
	 * @param filename
	 * @param resource
	 * @return
	 */
	public static boolean execute(String filename, boolean resource) {
		return execute( new SplashWindow( new Frame()), filename, resource);
	}

	/**
	 * @param splashWindow
	 * @param filename
	 * @param resource
	 * @return
	 */
	public static boolean execute(SplashWindow splashWindow, String filename, boolean resource) {
		if ( null != _splashWindow)
			return false;

		_splashWindow = splashWindow;
		return _splashWindow.create( filename, resource);
	}

	/**
	 * 
	 */
	public static void terminate() {
		if ( null == _splashWindow)
			return;

		_splashWindow.destroy();
		_splashWindow.getOwner().dispose();
		_splashWindow = null;
	}

	/**
	 * @param arg0
	 */
	public SplashWindow(Frame arg0) {
		super(arg0);
	}

	/**
	 * @param filename
	 * @param resource 
	 * @return
	 */
	private boolean create(String filename, boolean resource) {
		_bufferedImage = resource ?	Resource.load_image_from_resource( filename, getClass()) : Resource.load_image( filename);
		if ( null == _bufferedImage)
			return false;

		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		setBounds(
			( screenSize.width - _bufferedImage.getWidth()) / 2,
			( screenSize.height - _bufferedImage.getHeight()) / 2,
			_bufferedImage.getWidth(),
			_bufferedImage.getHeight());

		if ( !on_create( filename, resource))
			return false;

		setVisible( true);

		return true;
	}

	/**
	 * @param filename
	 * @param resource
	 * @return
	 */
	protected boolean on_create(String filename, boolean resource) {
		return true;
	}

	/**
	 * 
	 */
	private void destroy() {
		setVisible( false);
	}

	/* (non-Javadoc)
	 * @see java.awt.Container#paint(java.awt.Graphics)
	 */
	public void paint(Graphics g) {
		g.drawImage( _bufferedImage, 0, 0, _bufferedImage.getWidth(), _bufferedImage.getHeight(), this);
	}

	/**
	 * @return
	 */
	public static Component get_instance() {
		// TODO Auto-generated method stub
		return _splashWindow;
	}
}
