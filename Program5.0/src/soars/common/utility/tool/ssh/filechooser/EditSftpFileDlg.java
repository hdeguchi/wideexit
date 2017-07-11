/**
 * 
 */
package soars.common.utility.tool.ssh.filechooser;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.event.ActionEvent;

import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import soars.common.utility.swing.window.Dialog;

/**
 * @author kurata
 *
 */
public class EditSftpFileDlg extends Dialog {

	/**
	 * 
	 */
	public String _name = "";

	/**
	 * 
	 */
	private String _originalName = "";

	/**
	 * 
	 */
	private String _label = "";

	/**
	 * 
	 */
	private JTextField _nameTextField = null;

	/**
	 * @param arg0
	 * @param arg1
	 * @param arg2
	 * @param label
	 */
	public EditSftpFileDlg(Frame arg0, String arg1, boolean arg2, String label) {
		super(arg0, arg1, arg2);
		_label = label;
	}

	/**
	 * @param arg0
	 * @param arg1
	 * @param arg2
	 * @param originalName
	 * @param label
	 */
	public EditSftpFileDlg(Frame arg0, String arg1, boolean arg2, String originalName, String label) {
		super(arg0, arg1, arg2);
		_originalName = originalName;
		_label = label;
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

		setup_name_textField();

		insert_horizontal_glue();

		setup_ok_and_cancel_button(
			ResourceManager.get_instance().get( "dialog.ok"),
			ResourceManager.get_instance().get( "dialog.cancel"),
			true, true);

		insert_horizontal_glue();

		return true;
	}

	/**
	 * 
	 */
	private void setup_name_textField() {
		int pad = 5;

		JPanel panel = new JPanel();
		panel.setLayout( new FlowLayout( FlowLayout.LEFT, pad, 0));

		JLabel label = new JLabel( _label);
		panel.add( label);

		_nameTextField = new JTextField( _originalName);
		_nameTextField.setPreferredSize( new Dimension( 250, _nameTextField.getPreferredSize().height));
		panel.add( _nameTextField);

		link_to_cancel( _nameTextField);

		getContentPane().add( panel);
	}

	/* (non-Javadoc)
	 * @see soars.common.utility.swing.window.Dialog#on_ok(java.awt.event.ActionEvent)
	 */
	protected void on_ok(ActionEvent actionEvent) {
		_name = _nameTextField.getText();
		if ( _name.equals( "") || _name.equals( _originalName) || _name.startsWith( "."))
			return;

		super.on_ok(actionEvent);
	}
}
