/**
 * 
 */
package soars.tool.grid.portal_ip_address_receiver.body.main;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.HeadlessException;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.net.SocketException;
import java.util.Timer;

import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import soars.common.soars.tool.Receiver;
import soars.common.utility.swing.mac.IMacScreenMenuHandler;
import soars.common.utility.swing.mac.MacUtility;
import soars.common.utility.swing.tool.SwingTool;
import soars.common.utility.swing.window.Frame;
import soars.common.utility.tool.timer.ITimerTaskImplementCallback;
import soars.common.utility.tool.timer.TimerTaskImplement;

/**
 * @author kurata
 *
 */
public class MainFrame extends Frame implements IMacScreenMenuHandler, ITimerTaskImplementCallback {


	/**
	 * 
	 */
	static public final int _minimum_width = 310;

	/**
	 * 
	 */
	static public final int _minimum_height = 100;


	/**
	 * 
	 */
	static private Object _lock = new Object();


	/**
	 * 
	 */
	static private MainFrame _mainFrame = null;


	/**
	 * 
	 */
	private Rectangle _window_rectangle = new Rectangle();


	/**
	 * 
	 */
	private JLabel _portal_ip_address_label = null;


	/**
	 * 
	 */
	private Timer _timer = null;

	/**
	 * 
	 */
	private TimerTaskImplement _timerTaskImplement = null;

	/**
	 * 
	 */
	private final int _id = 0;

	/**
	 * 
	 */
	private final long _delay = 0;

	/**
	 * 
	 */
	private final long _period = 1000;

	/**
	 * 
	 */
	private boolean _running = false;


	/**
	 * 
	 */
	private Receiver _receiver = null;

	/**
	 * @return
	 */
	public static MainFrame get_instance() {
		synchronized( _lock) {
			if ( null == _mainFrame) {
				_mainFrame = new MainFrame( "Grid portal IP address receiver");
			}
		}
		return _mainFrame;
	}

	/**
	 * @param arg0
	 * @throws HeadlessException
	 */
	public MainFrame(String arg0) throws HeadlessException {
		super(arg0);
	}

	/**
	 * 
	 */
	private void get_property_from_environment_file() {
		String value = Environment.get_instance().get(
			Environment._window_rectangle_key + "x",
			String.valueOf( SwingTool.get_default_window_position( _minimum_width, _minimum_height).x));
		_window_rectangle.x = Integer.parseInt( value);

		value = Environment.get_instance().get(
			Environment._window_rectangle_key + "y",
			String.valueOf( SwingTool.get_default_window_position( _minimum_width, _minimum_height).y));
		_window_rectangle.y = Integer.parseInt( value);

		value = Environment.get_instance().get(
			Environment._window_rectangle_key + "width",
			String.valueOf( _minimum_width));
		_window_rectangle.width = Integer.parseInt( value);

		value = Environment.get_instance().get(
			Environment._window_rectangle_key + "height",
			String.valueOf( _minimum_height));
		_window_rectangle.height = Integer.parseInt( value);
	}

	/**
	 * 
	 */
	private void set_property_to_environment_file() throws IOException {
		Environment.get_instance().set(
			Environment._window_rectangle_key + "x", String.valueOf( _window_rectangle.x));
		Environment.get_instance().set(
			Environment._window_rectangle_key + "y", String.valueOf( _window_rectangle.y));
		Environment.get_instance().set(
			Environment._window_rectangle_key + "width", String.valueOf( _window_rectangle.width));
		Environment.get_instance().set(
			Environment._window_rectangle_key + "height", String.valueOf( _window_rectangle.height));

		Environment.get_instance().store();
	}

	/* (non-Javadoc)
	 * @see soars.common.utility.swing.window.Frame#on_create()
	 */
	protected boolean on_create() {
		if ( !super.on_create())
			return false;

		if ( !MacUtility.setup_screen_menu_handler( this, this, "SOARS"))
			return false;

		get_property_from_environment_file();


		getContentPane().setLayout( new BoxLayout( getContentPane(), BoxLayout.Y_AXIS));

		insert_horizontal_glue();

		setup();

		insert_horizontal_glue();


		setDefaultCloseOperation( DO_NOTHING_ON_CLOSE);

		pack();

		setBounds( _window_rectangle);

		Toolkit.getDefaultToolkit().setDynamicLayout( true);

		setVisible( true);


		if ( !start())
			return false;


		return true;
	}

	/* (non-Javadoc)
	 * @see soars.common.utility.swing.mac.IMacScreenMenuHandler#on_mac_about()
	 */
	public void on_mac_about() {
		JOptionPane.showMessageDialog( this,
			"Grid portal IP address receiver",
			"SOARS",
			JOptionPane.INFORMATION_MESSAGE);

		requestFocus();
	}

	/* (non-Javadoc)
	 * @see soars.common.utility.swing.mac.IMacScreenMenuHandler#on_mac_quit()
	 */
	public void on_mac_quit() {
		on_window_closing( null);
	}

	/* (non-Javadoc)
	 * @see soars.common.utility.swing.window.Frame#on_window_closing(java.awt.event.WindowEvent)
	 */
	protected void on_window_closing(WindowEvent windowEvent) {
		stop();

		_window_rectangle = getBounds();
		try {
			set_property_to_environment_file();
		}
		catch (IOException e) {
			throw new RuntimeException( e);
		}

		System.exit( 0);
	}

	/**
	 * 
	 */
	private void setup() {
		JPanel panel = new JPanel();
		panel.setLayout( new FlowLayout( FlowLayout.LEFT, 5, 0));

		_portal_ip_address_label = new JLabel( "               ");
		Font font = _portal_ip_address_label.getFont();
		_portal_ip_address_label.setFont( new Font( font.getFamily(), font.getStyle(), 32));
		_portal_ip_address_label.setPreferredSize( new Dimension( 300, _portal_ip_address_label.getPreferredSize().height));
		panel.add( _portal_ip_address_label);

		getContentPane().add( panel);
	}

	/**
	 * @return
	 */
	private boolean start() {
		if ( null == _receiver) {
			try {
				_receiver = new Receiver( Constant._portal_port);
			} catch (SocketException e) {
				//e.printStackTrace();
				return false;
			}

			_receiver.start();
		}

		if ( null == _timer) {
			_running = false;
			_timer = new Timer();
			_timerTaskImplement = new TimerTaskImplement( _id, this);
			_timer.schedule( _timerTaskImplement, _delay, _period);
		}

		return true;
	}

	/**
	 * 
	 */
	private void stop() {
		if ( null != _receiver)
			_receiver.stop();

		if ( null != _timer) {
			_timer.cancel();
			_timer = null;
		}
	}

	/* (non-Javadoc)
	 * @see soars.common.utility.tool.timer.ITimerTaskImplementCallback#execute_timer_task(int)
	 */
	public void execute_timer_task(int id) {
		if ( id != _id || _running || null == _receiver)
			return;

		_running = true;

		_portal_ip_address_label.setText( _receiver.get());

		_running = false;
	}
}
