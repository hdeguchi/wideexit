/**
 * 
 */
package soars.application.visualshell.file.exporter.script.setting;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.event.ActionEvent;

import javax.swing.BoxLayout;
import javax.swing.JCheckBox;
import javax.swing.JPanel;

import soars.application.visualshell.main.Environment;
import soars.application.visualshell.main.ResourceManager;
import soars.common.utility.swing.window.Dialog;

/**
 * The dialog box to select the option on exporting the ModelBuilder script.
 * @author kurata / SOARS project
 */
public class EditExportSettingDlg extends Dialog {

	/**
	 * 
	 */
	private JCheckBox _toLogFileCheckBox = null;

	/**
	 * 
	 */
	private JCheckBox _keepUserDataFileCheckBox = null;

	/**
	 * 
	 */
	private boolean _keepUserDataFile = false;

	/**
	 * Creates a non-modal dialog with the specified title and with the specified owner frame. If owner is null, a shared, hidden frame will be set as the owner of the dialog. 
	 * @param arg0 the Frame from which the dialog is displayed
	 * @param arg1 the String to display in the dialog's title bar
	 * @param arg2 true for a modal dialog, false for one that allows other windows to be active at the same time
	 * @param keepUserDataFile 
	 */
	public EditExportSettingDlg(Frame arg0, String arg1, boolean arg2, boolean keepUserDataFile) {
		super(arg0, arg1, arg2);
		_keepUserDataFile = keepUserDataFile;
	}

	/* (non-Javadoc)
	 * @see soars.common.utility.swing.window.Dialog#on_init_dialog()
	 */
	protected boolean on_init_dialog() {
		if ( !super.on_init_dialog())
			return false;



		getContentPane().setLayout( new BorderLayout());



		JPanel centerPanel = new JPanel();
		centerPanel.setLayout( new BoxLayout( centerPanel, BoxLayout.Y_AXIS));

		insert_horizontal_glue( centerPanel);

		if ( !setup_to_log_file_checkBox( centerPanel))
			return false;

		if ( !setup_keep_user_data_file_checkBox( centerPanel))
			return false;

		getContentPane().add( centerPanel);



		JPanel southPanel = new JPanel();
		southPanel.setLayout( new BoxLayout( southPanel, BoxLayout.Y_AXIS));

		JPanel panel = new JPanel();
		panel.setLayout( new FlowLayout( FlowLayout.RIGHT, 5, 0));

		insert_horizontal_glue( southPanel);

		setup_ok_and_cancel_button(
			panel,
			ResourceManager.get_instance().get( "dialog.ok"),
			ResourceManager.get_instance().get( "dialog.cancel"),
			false, false);
		southPanel.add( panel);

		insert_horizontal_glue( southPanel);

		getContentPane().add( southPanel, "South");



		setDefaultCloseOperation( DISPOSE_ON_CLOSE);

		return true;
	}

	/**
	 * @param parent
	 * @return
	 */
	private boolean setup_to_log_file_checkBox(JPanel parent) {
		JPanel panel = new JPanel();
		panel.setLayout( new FlowLayout( FlowLayout.LEFT, 5, 0));

		_toLogFileCheckBox = new JCheckBox(
			ResourceManager.get_instance().get( "edit.export.setting.dialog.check.box.to.file.name"));
		_toLogFileCheckBox.setSelected( Environment.get_instance().get( Environment._editExportSettingDialogToFileKey, "false").equals( "true"));
		panel.add( _toLogFileCheckBox);
		parent.add( panel);

		return true;
	}

	/**
	 * @param parent
	 * @return
	 */
	private boolean setup_keep_user_data_file_checkBox(JPanel parent) {
		if ( !_keepUserDataFile)
			return true;

		JPanel panel = new JPanel();
		panel.setLayout( new FlowLayout( FlowLayout.LEFT, 5, 0));

		_keepUserDataFileCheckBox = new JCheckBox(
			ResourceManager.get_instance().get( "edit.export.setting.dialog.check.box.keep.user.data.file.name"));
		_keepUserDataFileCheckBox.setSelected( Environment.get_instance().get( Environment._editExportSettingDialogKeepUserDataFileKey, "false").equals( "true"));
		panel.add( _keepUserDataFileCheckBox);
		parent.add( panel);

		return true;
	}

	/* (non-Javadoc)
	 * @see soars.common.utility.swing.window.Dialog#on_setup_completed()
	 */
	protected void on_setup_completed() {
		_toLogFileCheckBox.requestFocusInWindow();
	}

	/* (non-Javadoc)
	 * @see soars.common.utility.swing.window.Dialog#on_ok(java.awt.event.ActionEvent)
	 */
	protected void on_ok(ActionEvent actionEvent) {
		Environment.get_instance().set( Environment._editExportSettingDialogToFileKey, String.valueOf( _toLogFileCheckBox.isSelected()));

		if ( _keepUserDataFile)
			Environment.get_instance().set( Environment._editExportSettingDialogKeepUserDataFileKey, String.valueOf( _keepUserDataFileCheckBox.isSelected()));

		super.on_ok(actionEvent);
	}
}
