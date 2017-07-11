/**
 * 
 */
package soars.plugin.visualshell.xgrid1.main;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.HeadlessException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import soars.common.utility.swing.text.TextLimiter;
import soars.common.utility.swing.window.Dialog;
import soars.common.utility.tool.ssh2.SshTool2;

import com.sshtools.j2ssh.SshClient;

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
	private JTextField _privateKeyFilenameTextField = null;

	/**
	 * 
	 */
	private String _privateKeyFilename = "";

	/**
	 * 
	 */
	private List<JComponent> _components = new ArrayList<JComponent>();

	/**
	 * 
	 */
	private String _usersDirectory = "";

	/**
	 * 
	 */
	private Parameters _parameters = null;

	/**
	 * @param arg0
	 * @param arg1
	 * @param arg2
	 * @param usersDirectory
	 * @param parameters
	 * @throws HeadlessException
	 */
	public GridLoginDlg(Frame arg0, String arg1, boolean arg2, String usersDirectory, Parameters parameters) throws HeadlessException {
		super(arg0, arg1, arg2);
		_usersDirectory = usersDirectory;
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

		setup_privateKeyFilenameTextField();

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

		JLabel label = new JLabel( ResourceManager.get_instance().get( "plugin.xgrid1.login.dialog.gird.portal.ip.address"), SwingConstants.RIGHT);
		panel.add( label);
		_components.add( label);

		_gridPortalIpAddressTextField = new JTextField();
		_gridPortalIpAddressTextField.setText( Environment.get_instance().get( Environment._gridPortalIpAddressKey, ""));
		_gridPortalIpAddressTextField.setPreferredSize( new Dimension( 200, _gridPortalIpAddressTextField.getPreferredSize().height));
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

		JLabel label = new JLabel( ResourceManager.get_instance().get( "plugin.xgrid1.login.dialog.username"), SwingConstants.RIGHT);
		panel.add( label);
		_components.add( label);

		_usernameTextField = new JTextField();
		_usernameTextField.setDocument( new TextLimiter(
			"ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789_."));
		_usernameTextField.setText( Environment.get_instance().get( Environment._usernameKey, ""));
		_usernameTextField.setPreferredSize( new Dimension( 200, _usernameTextField.getPreferredSize().height));
		panel.add( _usernameTextField);

		link_to_cancel( _usernameTextField);

		getContentPane().add( panel);
	}

	/**
	 * 
	 */
	private void setup_privateKeyFilenameTextField() {
		int pad = 5;

		JPanel panel = new JPanel();
		panel.setLayout( new FlowLayout( FlowLayout.LEFT, pad, 0));

		JButton button = new JButton( ResourceManager.get_instance().get( "plugin.xgrid1.login.dialog.private.key.filename"));
		button.addActionListener( new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				on_privateKeyFileSelectorButton_actionPerformed( arg0);
			}
		});
		panel.add( button);
		_components.add( button);

		_privateKeyFilename = Environment.get_instance().get( Environment._privateKeyFilenameKey, "");
		File file = new File( _privateKeyFilename);

		_privateKeyFilenameTextField = new JTextField();
		_privateKeyFilenameTextField.setText( ( null != file && file.exists() && file.isFile() && file.canRead()) ? file.getName() : "");
		_privateKeyFilenameTextField.setPreferredSize( new Dimension( 200, _usernameTextField.getPreferredSize().height));
		_privateKeyFilenameTextField.setEditable( false);
		_privateKeyFilenameTextField.setToolTipText( ( null != file && file.exists() && file.isFile() && file.canRead()) ? _privateKeyFilename : null);
		panel.add( _privateKeyFilenameTextField);

		link_to_cancel( _usernameTextField);

		getContentPane().add( panel);
	}

	/**
	 * @param actionEvent
	 */
	protected void on_privateKeyFileSelectorButton_actionPerformed(ActionEvent actionEvent) {
		File file = Tool.get_file( _privateKeyFilename,
			ResourceManager.get_instance().get( "plugin.xgrid1.private.key.file.selector.dialog.title"), new String[] { "ppk"}, "Private key file", this);
		if ( null == file)
			return;

		_privateKeyFilenameTextField.setText( file.getName());
		_privateKeyFilename = file.getAbsolutePath();
		_privateKeyFilenameTextField.setToolTipText( _privateKeyFilename);
	}

	/**
	 * 
	 */
	private void adjust() {
		int width = 0;
		for ( JComponent component:_components)
			width = Math.max( width, component.getPreferredSize().width);

		for ( JComponent component:_components)
			component.setPreferredSize( new Dimension( width, component.getPreferredSize().height));
	}

	/* (non-Javadoc)
	 * @see soars.common.utility.swing.window.Dialog#on_ok(java.awt.event.ActionEvent)
	 */
	protected void on_ok(ActionEvent actionEvent) {
		String gridPortalIpAddress = _gridPortalIpAddressTextField.getText();
		String username = _usernameTextField.getText();

		if ( gridPortalIpAddress.equals( "") || username.equals( "") || _privateKeyFilename.equals( ""))
			return;

		SshClient sshClient = SshTool2.getSshClient( gridPortalIpAddress, username, new File( _privateKeyFilename));
		if ( null == sshClient) {
			JOptionPane.showMessageDialog(
				this,
				ResourceManager.get_instance().get( "plugin.xgrid1.login.dialog.could.not.login.message"),
				ResourceManager.get_instance().get( "plugin.xgrid1.login.dialog.title"),
				JOptionPane.ERROR_MESSAGE);
			store( gridPortalIpAddress, username);
			return;
		}

		sshClient.disconnect();

		String dataDirectoryName = ( _usersDirectory + "/" + username + Constant._baseDataDirectoryName);
		if ( !SshTool2.directory_exists( gridPortalIpAddress, username, new File( _privateKeyFilename), dataDirectoryName)) {
			if ( !SshTool2.mkdirs( gridPortalIpAddress, username, new File( _privateKeyFilename), dataDirectoryName)) {
				JOptionPane.showMessageDialog(
					this,
					ResourceManager.get_instance().get( "plugin.xgrid1.login.dialog.could.not.create.user.folder.message"),
					ResourceManager.get_instance().get( "plugin.xgrid1.login.dialog.title"),
					JOptionPane.ERROR_MESSAGE);
				store( gridPortalIpAddress, username);
				return;
			}
		}

		String outputDirectoryName = ( _usersDirectory + "/" + username + Constant._baseOutputDirectoryName);
		if ( !SshTool2.directory_exists( gridPortalIpAddress, username, new File( _privateKeyFilename), outputDirectoryName)) {
			if ( !SshTool2.mkdirs( gridPortalIpAddress, username, new File( _privateKeyFilename), outputDirectoryName)) {
				JOptionPane.showMessageDialog(
					this,
					ResourceManager.get_instance().get( "plugin.xgrid1.login.dialog.could.not.create.user.folder.message"),
					ResourceManager.get_instance().get( "plugin.xgrid1.login.dialog.title"),
					JOptionPane.ERROR_MESSAGE);
				store( gridPortalIpAddress, username);
				return;
			}
		}

		store( gridPortalIpAddress, username);

		_parameters._gridPortalIpAddress = gridPortalIpAddress;
		_parameters._username = username;
		_parameters._privateKeyFilename = _privateKeyFilename;
		_parameters._dataDirectoryName = dataDirectoryName;
		_parameters._outputDirectoryName = ( _parameters._directoryPrefix + outputDirectoryName);

		super.on_ok(actionEvent);
	}

	/**
	 * @param gridPortalIpAddress
	 * @param username
	 */
	private void store(String gridPortalIpAddress, String username) {
		Environment.get_instance().set( Environment._gridPortalIpAddressKey, gridPortalIpAddress);
		Environment.get_instance().set( Environment._usernameKey, username);
		Environment.get_instance().set( Environment._privateKeyFilenameKey, _privateKeyFilename);
		Environment.get_instance().store();
	}
}
