/*
 * 2004/10/07
 */
package soars.common.utility.swing.window;

import java.awt.Container;
import java.awt.GraphicsConfiguration;
import java.awt.HeadlessException;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import javax.swing.JFrame;

import soars.common.utility.swing.tool.SwingTool;

/**
 * @author kurata
 */
public class Frame extends JFrame {

	/**
	 * @throws java.awt.HeadlessException
	 */
	public Frame() throws HeadlessException {
		super();
	}

	/**
	 * @param arg0
	 */
	public Frame(GraphicsConfiguration arg0) {
		super(arg0);
	}

	/**
	 * @param arg0
	 * @throws java.awt.HeadlessException
	 */
	public Frame(String arg0) throws HeadlessException {
		super(arg0);
	}

	/**
	 * @param arg0
	 * @param arg1
	 */
	public Frame(String arg0, GraphicsConfiguration arg1) {
		super(arg0, arg1);
	}

	/**
	 * @return
	 */
	public boolean create() {
		addWindowListener( new WindowListener() {
			public void windowActivated(WindowEvent arg0) {
				on_window_activated( arg0);
			}
			public void windowClosed(WindowEvent arg0) {
				on_window_closed( arg0);
			}
			public void windowClosing(WindowEvent arg0) {
				on_window_closing( arg0);
			}
			public void windowDeactivated(WindowEvent arg0) {
				on_window_deactivated( arg0);
			}
			public void windowDeiconified(WindowEvent arg0) {
				on_window_deiconified( arg0);
			}
			public void windowIconified(WindowEvent arg0) {
				on_window_iconified( arg0);
			}
			public void windowOpened(WindowEvent arg0) {
				on_window_opened( arg0);
			}
		});

		setFocusable( true);

		addKeyListener( new KeyListener() {
			public void keyPressed(KeyEvent arg0) {
				on_key_pressed( arg0);
			}
			public void keyReleased(KeyEvent arg0) {
				on_key_released( arg0);
			}
			public void keyTyped(KeyEvent arg0) {
				on_key_typeded( arg0);
			}
		});

		if ( !on_create())
			return false;

		return true;
	}

	/**
	 * @return
	 */
	protected boolean on_create() {
		return true;
	}

	/**
	 * @param windowEvent
	 */
	protected void on_window_activated(WindowEvent windowEvent) {
	}

	/**
	 * @param windowEvent
	 */
	protected void on_window_closed(WindowEvent windowEvent) {
	}

	/**
	 * @param windowEvent
	 */
	protected void on_window_closing(WindowEvent windowEvent) {
	}

	/**
	 * @param windowEvent
	 */
	protected void on_window_deactivated(WindowEvent windowEvent) {
	}

	/**
	 * @param windowEvent
	 */
	protected void on_window_deiconified(WindowEvent windowEvent) {
	}

	/**
	 * @param windowEvent
	 */
	protected void on_window_iconified(WindowEvent windowEvent) {
	}

	/**
	 * @param windowEvent
	 */
	protected void on_window_opened(WindowEvent windowEvent) {
	}

	/**
	 * @param keyEvent
	 */
	protected void on_key_pressed(KeyEvent keyEvent) {
	}

	/**
	 * @param keyEvent
	 */
	protected void on_key_released(KeyEvent keyEvent) {
	}

	/**
	 * @param keyEvent
	 */
	protected void on_key_typeded(KeyEvent keyEvent) {
	}

	/**
	 * 
	 */
	protected void insert_horizontal_glue() {
		SwingTool.insert_horizontal_glue( getContentPane());
	}

	/**
	 * @param container
	 */
	protected void insert_horizontal_glue(Container container) {
		SwingTool.insert_horizontal_glue( container);
	}
}
