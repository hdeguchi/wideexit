/**
 * 
 */
package soars.plugin.visualshell.grid2.main;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.HeadlessException;
import java.awt.event.ActionEvent;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;

import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import soars.common.soars.tool.Receiver;
import soars.common.utility.swing.text.TextLimiter;
import soars.common.utility.swing.window.Dialog;
import soars.common.utility.tool.ssh.SshTool;
import soars.common.utility.tool.timer.ITimerTaskImplementCallback;
import soars.common.utility.tool.timer.TimerTaskImplement;

/**
 * @author kurata
 *
 */
public class GridLoginDlg extends Dialog implements ITimerTaskImplementCallback {


	/**
	 * 
	 */
	private Receiver _receiver = null;


	/**
	 * 
	 */
	private JTextField _grid_portal_ip_address_textField = null;


	/**
	 * 
	 */
	private JTextField _username_textField = null;


	/**
	 * 
	 */
	private List _labels = new ArrayList();


	/**
	 * 
	 */
	private String _root_directory = "";


	/**
	 * 
	 */
	private Parameters _parameters = null;


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
	private final int _id = 1;

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
	 * @param arg0
	 * @param arg1
	 * @param arg2
	 * @param root_directory
	 * @param parameters
	 * @param receiver
	 * @throws HeadlessException
	 */
	public GridLoginDlg(Frame arg0, String arg1, boolean arg2, String root_directory, Parameters parameters, Receiver receiver) throws HeadlessException {
		super(arg0, arg1, arg2);
		_root_directory = root_directory;
		_parameters = parameters;
		_receiver = receiver;
	}

	/* (non-Javadoc)
	 * @see soars.common.utility.swing.window.Dialog#on_init_dialog()
	 */
	protected boolean on_init_dialog() {
		if ( !super.on_init_dialog())
			return false;


		setResizable( false);


		getContentPane().setLayout( new BoxLayout( getContentPane(), BoxLayout.Y_AXIS));


		insert_horizontal_glue();

		setup_grid_portal_ip_address_textField();

		insert_horizontal_glue();

		setup_username_textField();

		insert_horizontal_glue();

		setup_ok_and_cancel_button(
			ResourceManager.get_instance().get( "dialog.ok"),
			ResourceManager.get_instance().get( "dialog.cancel"),
			true, true);

		insert_horizontal_glue();


		setDefaultCloseOperation( DISPOSE_ON_CLOSE);


		adjust();


		addWindowListener( new WindowListener() {
			public void windowActivated(WindowEvent e) {
			}
			public void windowClosed(WindowEvent e) {
			}
			public void windowClosing(WindowEvent e) {
				stop_timer();
			}
			public void windowDeactivated(WindowEvent e) {
			}
			public void windowDeiconified(WindowEvent e) {
			}
			public void windowIconified(WindowEvent e) {
			}
			public void windowOpened(WindowEvent e) {
			}
		});


		start_timer();


		return true;
	}

	/**
	 * 
	 */
	private void setup_grid_portal_ip_address_textField() {
		int pad = 5;

		JPanel panel = new JPanel();
		panel.setLayout( new FlowLayout( FlowLayout.LEFT, pad, 0));

		JLabel label = new JLabel( ResourceManager.get_instance().get( "plugin.grid2.login.dialog.gird.portal.ip.address"), SwingConstants.RIGHT);
		panel.add( label);
		_labels.add( label);

		_grid_portal_ip_address_textField = new JTextField();
		_grid_portal_ip_address_textField.setEditable( false);
		_grid_portal_ip_address_textField.setPreferredSize( new Dimension( 200,
			_grid_portal_ip_address_textField.getPreferredSize().height));
		panel.add( _grid_portal_ip_address_textField);

		getContentPane().add( panel);
	}

	/**
	 * 
	 */
	private void setup_username_textField() {
		int pad = 5;

		JPanel panel = new JPanel();
		panel.setLayout( new FlowLayout( FlowLayout.LEFT, pad, 0));

		JLabel label = new JLabel( ResourceManager.get_instance().get( "plugin.grid2.login.dialog.username"));
		panel.add( label);
		_labels.add( label);

		_username_textField = new JTextField();
		_username_textField.setDocument( new TextLimiter(
			"ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789_"));
		_username_textField.setText( Environment.get_instance().get( Environment._local_username_key, ""));
		_username_textField.setPreferredSize( new Dimension( 200,
			_username_textField.getPreferredSize().height));
		panel.add( _username_textField);

		link_to_cancel( _username_textField);

		getContentPane().add( panel);
	}

	/**
	 * 
	 */
	private void adjust() {
		int width = 0;
		for ( int i = 0; i < _labels.size(); ++i) {
			JLabel label = ( JLabel)_labels.get( i);
			width = Math.max( width, label.getPreferredSize().width);
		}

		for ( int i = 0; i < _labels.size(); ++i) {
			JLabel label = ( JLabel)_labels.get( i);
			label.setPreferredSize( new Dimension( width,
				label.getPreferredSize().height));
		}
	}

	/* (non-Javadoc)
	 * @see soars.common.utility.swing.window.Dialog#on_ok(java.awt.event.ActionEvent)
	 */
	protected void on_ok(ActionEvent actionEvent) {
		if ( _username_textField.getText().equals( ""))
			return;

		String local_username = _username_textField.getText();

		if ( _receiver.get().equals( Receiver._noPortal)) {
			store( local_username);
			return;
		}

		if ( !SshTool.directory_exists( _receiver.get(), _parameters._username, _parameters._password,
			_root_directory + "/" + local_username)) {
			if ( JOptionPane.YES_OPTION != JOptionPane.showConfirmDialog(
				this,
				ResourceManager.get_instance().get( "plugin.grid2.login.dialog.confirm.create.new.user.message"),
				ResourceManager.get_instance().get( "plugin.grid2.login.dialog.title"),
				JOptionPane.YES_NO_OPTION)) {
				store( local_username);
				return;
			} else {
				if ( !SshTool.mkdirs( _receiver.get(), _parameters._username, _parameters._password,
					_root_directory + "/" + local_username)) {
					JOptionPane.showMessageDialog(
						this,
						ResourceManager.get_instance().get( "plugin.grid2.login.dialog.could.not.create.new.user.message"),
						ResourceManager.get_instance().get( "plugin.grid2.login.dialog.title"),
						JOptionPane.ERROR_MESSAGE);
					store( local_username);
					return;
				}
			}
		}

		store( local_username);

		_parameters._local_username = local_username;

		stop_timer();

		super.on_ok(actionEvent);
	}

	/**
	 * @param local_username
	 */
	private void store(String local_username) {
		Environment.get_instance().set( Environment._local_username_key, local_username);
		Environment.get_instance().store();
	}

	/* (non-Javadoc)
	 * @see soars.common.utility.swing.window.Dialog#on_cancel(java.awt.event.ActionEvent)
	 */
	protected void on_cancel(ActionEvent actionEvent) {
		stop_timer();
		super.on_cancel(actionEvent);
	}

	/**
	 * 
	 */
	private void start_timer() {
		if ( null == _timer) {
			_running = false;
			_timer = new Timer();
			_timerTaskImplement = new TimerTaskImplement( _id, this);
			_timer.schedule( _timerTaskImplement, _delay, _period);
		}
	}

	/**
	 * 
	 */
	protected void stop_timer() {
		if ( null != _timer) {
			_timer.cancel();
			_timer = null;
		}
	}

	/* (non-Javadoc)
	 * @see soars.common.utility.tool.timer.ITimerTaskImplementCallback#execute_timer_task(int)
	 */
	public void execute_timer_task(int id) {
		if ( id != _id || _running)
			return;

		_running = true;

		_grid_portal_ip_address_textField.setText( _receiver.get());
		_grid_portal_ip_address_textField.update( _grid_portal_ip_address_textField.getGraphics());

		_running = false;
	}
}
