/**
 * 
 */
package soars.plugin.visualshell.ga1.main;

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
	private JTextField _grid_portal_ip_address_textField = null;

	/**
	 * 
	 */
	private JTextField _local_username_textField = null;

	/**
	 * 
	 */
	private List<JLabel> _labels = new ArrayList<JLabel>();

	/**
	 * 
	 */
	private String _root_directory = "";

	/**
	 * 
	 */
	private Parameters _parameters = null;

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


		return true;
	}

	/**
	 * 
	 */
	private void setup_grid_portal_ip_address_textField() {
		int pad = 5;

		JPanel panel = new JPanel();
		panel.setLayout( new FlowLayout( FlowLayout.LEFT, pad, 0));

		JLabel label = new JLabel( ResourceManager.get_instance().get( "plugin.ga1.login.dialog.gird.portal.ip.address"), SwingConstants.RIGHT);
		panel.add( label);
		_labels.add( label);

		_grid_portal_ip_address_textField = new JTextField();
		_grid_portal_ip_address_textField.setText( _parameters._grid_portal_ip_address);
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

		JLabel label = new JLabel( ResourceManager.get_instance().get( "plugin.ga1.login.dialog.username"));
		panel.add( label);
		_labels.add( label);

		_local_username_textField = new JTextField();
		_local_username_textField.setDocument( new TextLimiter(
			"ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789_"));
		_local_username_textField.setText( _parameters._local_username);
		_local_username_textField.setPreferredSize( new Dimension( 200,
			_local_username_textField.getPreferredSize().height));
		panel.add( _local_username_textField);

		link_to_cancel( _local_username_textField);

		getContentPane().add( panel);
	}

	/**
	 * 
	 */
	private void adjust() {
		int width = 0;
		for ( JLabel label:_labels)
			width = Math.max( width, label.getPreferredSize().width);

		for ( JLabel label:_labels) {
			label.setPreferredSize( new Dimension( width, label.getPreferredSize().height));
		}
	}

	/* (non-Javadoc)
	 * @see soars.common.utility.swing.window.Dialog#on_ok(java.awt.event.ActionEvent)
	 */
	protected void on_ok(ActionEvent actionEvent) {
		String grid_portal_ip_address = _grid_portal_ip_address_textField.getText();
		String local_username = _local_username_textField.getText();

		if ( grid_portal_ip_address.equals( "") || local_username.equals( ""))
			return;

		SshClient sshClient = SshTool.getSshClient( grid_portal_ip_address, _parameters._username, _parameters._password);
		if ( null == sshClient) {
			JOptionPane.showMessageDialog(
				this,
				ResourceManager.get_instance().get( "plugin.ga1.login.dialog.could.not.login.message"),
				ResourceManager.get_instance().get( "plugin.ga1.login.dialog.title"),
				JOptionPane.ERROR_MESSAGE);
//			store( grid_portal_ip_address, local_username);
			return;
		}

		sshClient.disconnect();

		if ( !SshTool.directory_exists( grid_portal_ip_address, _parameters._username, _parameters._password,
			_root_directory + "/" + local_username)) {
			if ( JOptionPane.YES_OPTION != JOptionPane.showConfirmDialog(
				this,
				ResourceManager.get_instance().get( "plugin.ga1.login.dialog.confirm.create.new.user.message"),
				ResourceManager.get_instance().get( "plugin.ga1.login.dialog.title"),
				JOptionPane.YES_NO_OPTION)) {
//				store( grid_portal_ip_address, local_username);
				return;
			} else {
				if ( !SshTool.mkdirs( grid_portal_ip_address, _parameters._username, _parameters._password,
					_root_directory + "/" + local_username)) {
					JOptionPane.showMessageDialog(
						this,
						ResourceManager.get_instance().get( "plugin.ga1.login.dialog.could.not.create.new.user.message"),
						ResourceManager.get_instance().get( "plugin.ga1.login.dialog.title"),
						JOptionPane.ERROR_MESSAGE);
//					store( grid_portal_ip_address, local_username);
					return;
				}
			}
		}

//		store( grid_portal_ip_address, local_username);

		_parameters._grid_portal_ip_address = grid_portal_ip_address;
		_parameters._local_username = local_username;

		super.on_ok(actionEvent);
	}

//	/**
//	 * @param grid_portal_ip_address
//	 * @param local_username
//	 */
//	private void store(String grid_portal_ip_address, String local_username) {
//		Environment.get_instance().set( Environment._grid_portal_ip_address_key, grid_portal_ip_address);
//		Environment.get_instance().set( Environment._local_username_key, local_username);
//		Environment.get_instance().store();
//	}
}
