/**
 * 
 */
package soars.plugin.visualshell.tsubame2.main;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.HeadlessException;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BoxLayout;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import soars.common.utility.swing.text.TextLimiter;
import soars.common.utility.swing.window.Dialog;
import soars.common.utility.tool.encryption.Encryption;
import soars.common.utility.tool.ssh.SshTool;

import com.sshtools.j2ssh.SshClient;

/**
 * @author kurata
 *
 */
public class GridLoginDlg extends Dialog {


	/**
	 * 
	 */
	private JTextField _host_textField = null;

	/**
	 * 
	 */
	private JTextField _username_textField = null;

	/**
	 * 
	 */
	private JPasswordField _passwordField = null;


	/**
	 * 
	 */
	private JTextField _local_username_textField = null;


	/**
	 * 
	 */
	private JCheckBox _store_information_checkBox = null;


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
	private final String _key = "TSUBAME plugin";

	/**
	 * 
	 */
	private final String _algorithm = "Blowfish";

	/**
	 * @param arg0
	 * @param arg1
	 * @param arg2
	 * @param root_directory
	 * @param parameters
	 * @throws HeadlessException
	 */
	public GridLoginDlg(Frame arg0, String arg1, boolean arg2, String root_directory, Parameters parameters) throws HeadlessException {
		super(arg0, arg1, arg2);
		_root_directory = root_directory;
		_parameters = parameters;
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

		setup_host_textField();

		insert_horizontal_glue();

		setup_username_textField();

		insert_horizontal_glue();

		setup_password_textField();

		insert_horizontal_glue();

		setup_local_username_textField();

		insert_horizontal_glue();

		setup_store_information_checkBox();
		insert_horizontal_glue();

		setup_ok_and_cancel_button(
			ResourceManager.get_instance().get( "dialog.ok"),
			ResourceManager.get_instance().get( "dialog.cancel"),
			true, true);

		insert_horizontal_glue();


		setDefaultCloseOperation( DISPOSE_ON_CLOSE);


		adjust();


		return true;
	}

	/**
	 * 
	 */
	private void setup_host_textField() {
		int pad = 5;

		JPanel panel = new JPanel();
		panel.setLayout( new FlowLayout( FlowLayout.LEFT, pad, 0));

		JLabel label = new JLabel( ResourceManager.get_instance().get( "plugin.tsubame2.login.dialog.gird.portal.ip.address"));
		panel.add( label);
		_labels.add( label);

		_host_textField = new JTextField();
		_host_textField.setDocument( new TextLimiter(
			"ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789_."));
		_host_textField.setText( Environment.get_instance().get( Environment._host_key, ""));
		_host_textField.setPreferredSize( new Dimension( 200,
			_host_textField.getPreferredSize().height));
		panel.add( _host_textField);

		link_to_cancel( _host_textField);

		getContentPane().add( panel);
	}

	/**
	 * 
	 */
	private void setup_username_textField() {
		int pad = 5;

		JPanel panel = new JPanel();
		panel.setLayout( new FlowLayout( FlowLayout.LEFT, pad, 0));

		JLabel label = new JLabel( ResourceManager.get_instance().get( "plugin.tsubame2.login.dialog.gird.portal.username"));
		panel.add( label);
		_labels.add( label);

		_username_textField = new JTextField();
		_username_textField.setDocument( new TextLimiter(
			"ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789_"));
		_username_textField.setText( Environment.get_instance().get( Environment._username_key, ""));
		_username_textField.setPreferredSize( new Dimension( 200,
			_username_textField.getPreferredSize().height));
		panel.add( _username_textField);

		link_to_cancel( _username_textField);

		getContentPane().add( panel);
	}

	/**
	 * 
	 */
	private void setup_password_textField() {
		int pad = 5;

		JPanel panel = new JPanel();
		panel.setLayout( new FlowLayout( FlowLayout.LEFT, pad, 0));

		JLabel label = new JLabel( ResourceManager.get_instance().get( "plugin.tsubame2.login.dialog.gird.portal.password"));
		panel.add( label);
		_labels.add( label);

		String password = Environment.get_instance().get( Environment._password_key, "");
		if ( !password.equals( ""))
			password = Encryption.decrypt( _key, password, _algorithm);

		_passwordField = new JPasswordField();
		_passwordField.setDocument( new TextLimiter(
			"ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789_"));
		_passwordField.setText( ( null != password) ? password : "");
		_passwordField.setPreferredSize( new Dimension( 200,
			_passwordField.getPreferredSize().height));
		panel.add( _passwordField);

		link_to_cancel( _passwordField);

		getContentPane().add( panel);
	}

	/**
	 * 
	 */
	private void setup_local_username_textField() {
		int pad = 5;

		JPanel panel = new JPanel();
		panel.setLayout( new FlowLayout( FlowLayout.LEFT, pad, 0));

		JLabel label = new JLabel( ResourceManager.get_instance().get( "plugin.tsubame2.login.dialog.username"));
		panel.add( label);
		_labels.add( label);

		_local_username_textField = new JTextField();
		_local_username_textField.setDocument( new TextLimiter(
			"ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789_"));
		_local_username_textField.setText( Environment.get_instance().get( Environment._local_username_key, ""));
		_local_username_textField.setPreferredSize( new Dimension( 200,
			_local_username_textField.getPreferredSize().height));
		panel.add( _local_username_textField);

		link_to_cancel( _local_username_textField);

		getContentPane().add( panel);
	}

	/**
	 * 
	 */
	private void setup_store_information_checkBox() {
		int pad = 5;

		JPanel panel = new JPanel();
		panel.setLayout( new FlowLayout( FlowLayout.LEFT, pad, 0));

		_store_information_checkBox = new JCheckBox( ResourceManager.get_instance().get( "plugin.tsubame2.login.dialog.store.information"),
			Environment.get_instance().get( Environment._store_information, "false").equals( "true"));
		panel.add( _store_information_checkBox);

		link_to_cancel( _store_information_checkBox);

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
		if ( _host_textField.getText().equals( "")
			|| _username_textField.getText().equals( "")
			|| ( new String( _passwordField.getPassword())).equals( "")
			|| _local_username_textField.getText().equals( ""))
			return;

		String host = _host_textField.getText();
		String username = _username_textField.getText();
		String password = new String( _passwordField.getPassword());
		String local_username = _local_username_textField.getText();

		SshClient sshClient = SshTool.getSshClient( host, username, password);
		if ( null == sshClient) {
			JOptionPane.showMessageDialog(
				this,
				ResourceManager.get_instance().get( "plugin.tsubame2.login.dialog.could.not.login.message"),
				ResourceManager.get_instance().get( "plugin.tsubame2.login.dialog.title"),
				JOptionPane.ERROR_MESSAGE);
			store( host, username, password, local_username);
			return;
		}

		sshClient.disconnect();

		if ( !SshTool.directory_exists( host, username, password, _root_directory + "/" + local_username)) {
			if ( JOptionPane.YES_OPTION != JOptionPane.showConfirmDialog(
				this,
				ResourceManager.get_instance().get( "plugin.tsubame2.login.dialog.confirm.create.new.user.message"),
				ResourceManager.get_instance().get( "plugin.tsubame2.login.dialog.title"),
				JOptionPane.YES_NO_OPTION)) {
				store( host, username, password, local_username);
				return;
			} else {
				if ( !SshTool.mkdirs( host, username, password, _root_directory + "/" + local_username)) {
					JOptionPane.showMessageDialog(
						this,
						ResourceManager.get_instance().get( "plugin.tsubame2.login.dialog.could.not.create.new.user.message"),
						ResourceManager.get_instance().get( "plugin.tsubame2.login.dialog.title"),
						JOptionPane.ERROR_MESSAGE);
					store( host, username, password, local_username);
					return;
				}
			}
		}

		store( host, username, password, local_username);

		_parameters._host = host;
		_parameters._username = username;
		_parameters._password = password;
		_parameters._local_username = local_username;

		super.on_ok(actionEvent);
	}

	/**
	 * @param host
	 * @param username
	 * @param password
	 * @param local_username
	 */
	private void store(String host, String username, String password, String local_username) {
		if ( !_store_information_checkBox.isSelected()) {
			Environment.get_instance().set( Environment._host_key, "");
			Environment.get_instance().set( Environment._username_key, "");
			Environment.get_instance().set( Environment._password_key, "");
			Environment.get_instance().set( Environment._local_username_key, "");
		} else {
			String value = Encryption.encrypt( _key, password, _algorithm);
			Environment.get_instance().set( Environment._host_key, host);
			Environment.get_instance().set( Environment._username_key, username);
			Environment.get_instance().set( Environment._password_key, ( ( null != value) ? value : ""));
			Environment.get_instance().set( Environment._local_username_key, local_username);
		}
		Environment.get_instance().set( Environment._store_information,
			String.valueOf( _store_information_checkBox.isSelected()));
		Environment.get_instance().store();
	}

	/* (non-Javadoc)
	 * @see soars.common.utility.swing.window.Dialog#on_cancel(java.awt.event.ActionEvent)
	 */
	protected void on_cancel(ActionEvent actionEvent) {
		super.on_cancel(actionEvent);
	}
}
