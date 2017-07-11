/**
 * 
 */
package soars.plugin.visualshell.grid3.main;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.HeadlessException;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import com.sshtools.j2ssh.SshClient;

import soars.common.utility.swing.text.TextLimiter;
import soars.common.utility.swing.window.Dialog;
import soars.common.utility.tool.ssh.SshTool;

/**
 * @author kurata
 *
 */
public class GridLoginDlg extends Dialog {


	/**
	 * 
	 */
	private JTextField _gridPortalIpAddressTextField = null;


	/**
	 * 
	 */
	private JTextField _usernameTextField = null;


	/**
	 * 
	 */
	private List<JLabel> _labels = new ArrayList<JLabel>();


	/**
	 * 
	 */
	private String _rootDirectory = "";


	/**
	 * 
	 */
	private Parameters _parameters = null;

	/**
	 * @param arg0
	 * @param arg1
	 * @param arg2
	 * @param rootDirectory
	 * @param parameters
	 * @throws HeadlessException
	 */
	public GridLoginDlg(Frame arg0, String arg1, boolean arg2, String rootDirectory, Parameters parameters) throws HeadlessException {
		super(arg0, arg1, arg2);
		_rootDirectory = rootDirectory;
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

		setup_gridPortalIpAddressTextField();

		insert_horizontal_glue();

		setup_usernameTextField();

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
	private void setup_gridPortalIpAddressTextField() {
		int pad = 5;

		JPanel panel = new JPanel();
		panel.setLayout( new FlowLayout( FlowLayout.LEFT, pad, 0));

		JLabel label = new JLabel( ResourceManager.get_instance().get( "plugin.grid3.login.dialog.gird.portal.ip.address"), SwingConstants.RIGHT);
		panel.add( label);
		_labels.add( label);

		_gridPortalIpAddressTextField = new JTextField();
		_gridPortalIpAddressTextField.setText( Environment.get_instance().get( Environment._gridPortalIpAddressKey, ""));
		_gridPortalIpAddressTextField.setPreferredSize( new Dimension( 200,
			_gridPortalIpAddressTextField.getPreferredSize().height));
		panel.add( _gridPortalIpAddressTextField);

		getContentPane().add( panel);
	}

	/**
	 * 
	 */
	private void setup_usernameTextField() {
		int pad = 5;

		JPanel panel = new JPanel();
		panel.setLayout( new FlowLayout( FlowLayout.LEFT, pad, 0));

		JLabel label = new JLabel( ResourceManager.get_instance().get( "plugin.grid3.login.dialog.username"));
		panel.add( label);
		_labels.add( label);

		_usernameTextField = new JTextField();
		_usernameTextField.setDocument( new TextLimiter(
			"ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789_"));
		_usernameTextField.setText( Environment.get_instance().get( Environment._localUsernameKey, ""));
		_usernameTextField.setPreferredSize( new Dimension( 200,
			_usernameTextField.getPreferredSize().height));
		panel.add( _usernameTextField);

		link_to_cancel( _usernameTextField);

		getContentPane().add( panel);
	}

	/**
	 * 
	 */
	private void adjust() {
		int width = 0;
		for ( JLabel label:_labels)
			width = Math.max( width, label.getPreferredSize().width);

		for ( JLabel label:_labels)
			label.setPreferredSize( new Dimension( width, label.getPreferredSize().height));
	}

	/* (non-Javadoc)
	 * @see soars.common.utility.swing.window.Dialog#on_ok(java.awt.event.ActionEvent)
	 */
	protected void on_ok(ActionEvent actionEvent) {
		String gridPortalIpAddress = _gridPortalIpAddressTextField.getText();
		String localUsername = _usernameTextField.getText();

		if ( gridPortalIpAddress.equals( "") || localUsername.equals( ""))
			return;

		SshClient sshClient = SshTool.getSshClient( gridPortalIpAddress, _parameters._username, _parameters._password);
		if ( null == sshClient) {
			JOptionPane.showMessageDialog(
				this,
				ResourceManager.get_instance().get( "plugin.grid3.login.dialog.could.not.login.message"),
				ResourceManager.get_instance().get( "plugin.grid3.login.dialog.title"),
				JOptionPane.ERROR_MESSAGE);
			store( gridPortalIpAddress, localUsername);
			return;
		}

		sshClient.disconnect();

		if ( !SshTool.directory_exists( gridPortalIpAddress, _parameters._username, _parameters._password,
			_rootDirectory + "/" + localUsername)) {
			if ( JOptionPane.YES_OPTION != JOptionPane.showConfirmDialog(
				this,
				ResourceManager.get_instance().get( "plugin.grid3.login.dialog.confirm.create.new.user.message"),
				ResourceManager.get_instance().get( "plugin.grid3.login.dialog.title"),
				JOptionPane.YES_NO_OPTION)) {
				store( gridPortalIpAddress, localUsername);
				return;
			} else {
				if ( !SshTool.mkdirs( gridPortalIpAddress, _parameters._username, _parameters._password,
					_rootDirectory + "/" + localUsername)) {
					JOptionPane.showMessageDialog(
						this,
						ResourceManager.get_instance().get( "plugin.grid3.login.dialog.could.not.create.new.user.message"),
						ResourceManager.get_instance().get( "plugin.grid3.login.dialog.title"),
						JOptionPane.ERROR_MESSAGE);
					store( gridPortalIpAddress, localUsername);
					return;
				}
			}
		}

		store( gridPortalIpAddress, localUsername);

		_parameters._gridPortalIpAddress = gridPortalIpAddress;
		_parameters._localUsername = localUsername;

		super.on_ok(actionEvent);
	}

	/**
	 * @param gridPortalIpAddress
	 * @param localUsername
	 */
	private void store(String gridPortalIpAddress, String localUsername) {
		Environment.get_instance().set( Environment._gridPortalIpAddressKey, gridPortalIpAddress);
		Environment.get_instance().set( Environment._localUsernameKey, localUsername);
		Environment.get_instance().store();
	}
}
