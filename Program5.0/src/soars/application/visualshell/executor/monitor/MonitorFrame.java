/*
 * Created on 2006/02/27
 */
package soars.application.visualshell.executor.monitor;

import java.awt.Component;
import java.awt.GraphicsEnvironment;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.event.WindowEvent;

import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.JToolBar;
import javax.swing.SwingConstants;

import soars.application.visualshell.main.Constant;
import soars.application.visualshell.main.Environment;
import soars.application.visualshell.main.MainFrame;
import soars.application.visualshell.main.ResourceManager;
import soars.common.utility.swing.tool.SwingTool;
import soars.common.utility.swing.window.Frame;
import soars.common.utility.tool.resource.Resource;

/**
 * The frame which contains the container of the monitor components to display the log output of the ModelBuilder.
 * @author kurata / SOARS project
 */
public class MonitorFrame extends Frame {

	/**
	 * Default width of this frame.
	 */
	static public final int _minimumWidth = 640;

	/**
	 * Default height of this frame.
	 */
	static public final int _minimumHeight = 480;

	/**
	 * 
	 */
	static private MonitorFrame _monitorFrame = null;

	/**
	 * 
	 */
	private Rectangle _windowRectangle = new Rectangle();

	/**
	 * 
	 */
	private MonitorTabbedPane _monitorTabbedPane = null;

	/**
	 * 
	 */
	static protected Object _lock = new Object();

	/**
	 * 
	 */
	static private boolean _atFirst = true;

	/**
	 * Returns the instance of this frame.
	 * @return the instance of this frame
	 */
	public static MonitorFrame get_instance() {
		if ( null == _monitorFrame) {
			_monitorFrame = new MonitorFrame(
				ResourceManager.get_instance().get( "run.monitor.frame.title"));
			if ( !_monitorFrame.create())
				return null;

			Image image = Resource.load_image_from_resource( Constant._resourceDirectory + "/image/icon/icon.png", _monitorFrame.getClass());
			if ( null != image)
				_monitorFrame.setIconImage( image);
		}

		return _monitorFrame;
	}

	/**
	 * Invoked when the appliction terminates.
	 */
	public static void on_closing() {
		if ( null == _monitorFrame)
			return;

		_monitorFrame.set_property_to_environment_file();
	}

	/**
	 * @param arg0
	 */
	protected MonitorFrame(String arg0) {
		super(arg0);
	}

	/**
	 * 
	 */
	private void get_property_from_environment_file() {
		String value = Environment.get_instance().get(
			Environment._monitorWindowRectangleKey + "x",
			String.valueOf( SwingTool.get_default_window_position( MainFrame.get_instance(), _minimumWidth, _minimumHeight).x));
		_windowRectangle.x = Integer.parseInt( value);

		value = Environment.get_instance().get(
			Environment._monitorWindowRectangleKey + "y",
			String.valueOf( SwingTool.get_default_window_position( MainFrame.get_instance(), _minimumWidth, _minimumHeight).y));
		_windowRectangle.y = Integer.parseInt( value);

		value = Environment.get_instance().get(
			Environment._monitorWindowRectangleKey + "width",
			String.valueOf( _minimumWidth));
		_windowRectangle.width = Integer.parseInt( value);

		value = Environment.get_instance().get(
			Environment._monitorWindowRectangleKey + "height",
			String.valueOf( _minimumHeight));
		_windowRectangle.height = Integer.parseInt( value);
	}

	/**
	 * @return
	 */
	private void optimize_window_rectangle() {
		if ( !GraphicsEnvironment.getLocalGraphicsEnvironment().getMaximumWindowBounds().intersects( _windowRectangle)
			|| GraphicsEnvironment.getLocalGraphicsEnvironment().getMaximumWindowBounds().intersection( _windowRectangle).width <= 10
			|| _windowRectangle.y <= -getInsets().top
			|| GraphicsEnvironment.getLocalGraphicsEnvironment().getMaximumWindowBounds().intersection( _windowRectangle).height <= getInsets().top)
			_windowRectangle.setBounds(
				SwingTool.get_default_window_position( MainFrame.get_instance(), _minimumWidth, _minimumHeight).x,
				SwingTool.get_default_window_position( MainFrame.get_instance(), _minimumWidth, _minimumHeight).y,
				_minimumWidth, _minimumHeight);
	}

	/**
	 * 
	 */
	private void set_property_to_environment_file() {
		_windowRectangle = getBounds();
		Environment.get_instance().set(
			Environment._monitorWindowRectangleKey + "x", String.valueOf( _windowRectangle.x));
		Environment.get_instance().set(
			Environment._monitorWindowRectangleKey + "y", String.valueOf( _windowRectangle.y));
		Environment.get_instance().set(
			Environment._monitorWindowRectangleKey + "width", String.valueOf( _windowRectangle.width));
		Environment.get_instance().set(
			Environment._monitorWindowRectangleKey + "height", String.valueOf( _windowRectangle.height));
	}

	/**
	 * Creates a new monitor component, appends it to the container and returns it.
	 * @param title the title of a new monitor component
	 * @param log_folder_path the directory of the log files
	 * @return a new monitor component
	 */
	public MonitorPropertyPage append(String title, String log_folder_path) {
		synchronized( _lock) {
			MonitorPropertyPage monitorPropertyPage = new MonitorPropertyPage( title, log_folder_path, ( Frame)getOwner(), this);
			if ( !monitorPropertyPage.create())
				return null;

			_monitorTabbedPane.add( monitorPropertyPage, monitorPropertyPage._title);

			if ( _atFirst) {
				setVisible( true);
				MainFrame.get_instance().enable_view_show_monitor_menuItem( false);
				_atFirst = false;
			}

			return monitorPropertyPage;
		}
	}

	/**
	 * @param component
	 */
	protected void select(Component component) {
		_monitorTabbedPane.setSelectedComponent( component);
	}

	/* (Non Javadoc)
	 * @see soars.common.utility.swing.window.Frame#on_create()
	 */
	protected boolean on_create() {
		if ( !super.on_create())
			return false;


		get_property_from_environment_file();


		getContentPane().setLayout( new BoxLayout( getContentPane(), BoxLayout.Y_AXIS));

		setup_tabbed_pane();

		setup_status_bar();


		setDefaultCloseOperation( DO_NOTHING_ON_CLOSE);

		pack();

		optimize_window_rectangle();
		setLocation( _windowRectangle.x, _windowRectangle.y);
		setSize( _windowRectangle.width, _windowRectangle.height);

		Toolkit.getDefaultToolkit().setDynamicLayout( true);

		//setVisible( true);

		return true;
	}

	/**
	 * 
	 */
	private void setup_tabbed_pane() {
		JPanel panel = new JPanel();
		panel.setLayout( new BoxLayout( panel, BoxLayout.X_AXIS));

		//panel.add( Box.createHorizontalStrut( 5));

		_monitorTabbedPane = new MonitorTabbedPane( SwingConstants.TOP, JTabbedPane.WRAP_TAB_LAYOUT);
		_monitorTabbedPane.setup();

//		JScrollPane scrollPane = new JScrollPane();
//		scrollPane.setHorizontalScrollBarPolicy( JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
//		scrollPane.setVerticalScrollBarPolicy( JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
//		scrollPane.getViewport().setView( _monitorTabbedPane);
//
//		//panel.add( Box.createHorizontalStrut( 5));
//
//		panel.add( scrollPane);
		panel.add( _monitorTabbedPane);

		getContentPane().add( panel);
	}

	/**
	 * 
	 */
	private void setup_status_bar() {
		JToolBar statusBar = new JToolBar();
		JPanel panel = new JPanel();
		panel.setLayout( new BoxLayout( panel, BoxLayout.X_AXIS));
		JLabel label = new JLabel( " ");
		panel.add( label);
		statusBar.add( panel);
		statusBar.setFloatable( false);
		getContentPane().add( statusBar);
	}

	/* (Non Javadoc)
	 * @see soars.common.utility.swing.window.Frame#on_window_closing(java.awt.event.WindowEvent)
	 */
	protected void on_window_closing(WindowEvent windowEvent) {
		setVisible( false);
		MainFrame.get_instance().enable_view_show_monitor_menuItem( true);
		//if ( Frame.ICONIFIED != getExtendedState())
		//	setExtendedState( Frame.ICONIFIED);
	}

	/**
	 * Makes this component visible.
	 */
	public void set_visible() {
		if ( 0 == _monitorTabbedPane.getComponentCount())
			return;

		if ( Frame.NORMAL != getExtendedState())
			setExtendedState( Frame.NORMAL);

		setVisible( true);
	}
}
