/**
 * 
 */
package soars.application.visualshell.object.docker.edit;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.HeadlessException;
import java.awt.event.ActionEvent;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.io.File;

import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;

import soars.application.visualshell.file.docker.DockerFilesetProperty;
import soars.application.visualshell.main.Constant;
import soars.application.visualshell.main.Environment;
import soars.application.visualshell.main.ResourceManager;
import soars.application.visualshell.main.VisualShellView;
import soars.application.visualshell.object.docker.edit.comon.DockerPropertyPanel;
import soars.common.soars.environment.CommonEnvironment;
import soars.common.utility.swing.combo.ComboBox;
import soars.common.utility.swing.combo.CommonComboBoxRenderer;
import soars.common.utility.swing.window.Dialog;

/**
 * @author kurata
 *
 */
public class EditDockerPropertyDlg extends Dialog {

	/**
	 * 
	 */
	private int _minimumWidth = 400;

	/**
	 * 
	 */
	private int _minimumHeight = -1;

	/**
	 * 
	 */
	private DockerPropertyPanel _dockerPropertyPanel = null;

	/**
	 * 
	 */
	private ComboBox _memorySizeComboBox = null;

	/**
	 * 
	 */
	private JCheckBox _toLogFileCheckBox = null;

	/**
	 * @param arg0
	 * @param arg1
	 * @param arg2
	 * @throws HeadlessException
	 */
	public EditDockerPropertyDlg(Frame arg0, String arg1, boolean arg2) throws HeadlessException {
		super(arg0, arg1, arg2);
	}

	/* (non-Javadoc)
	 * @see soars.common.utility.swing.window.Dialog#on_init_dialog()
	 */
	@Override
	protected boolean on_init_dialog() {
		if (!super.on_init_dialog())
			return false;

		getContentPane().setLayout( new BorderLayout());

		_dockerPropertyPanel = new DockerPropertyPanel();
		_dockerPropertyPanel.setup();
		setup_other_panel( _dockerPropertyPanel);
		setup_generate_and_close_button( _dockerPropertyPanel);
		insert_horizontal_glue( _dockerPropertyPanel);
		getContentPane().add( _dockerPropertyPanel, "North");

		return true;
	}

	/**
	 * @param parent
	 */
	private void setup_other_panel(DockerPropertyPanel parent) {
		JPanel panel = new JPanel();
		panel.setLayout( new FlowLayout( FlowLayout.LEFT, 5, 0));
		setup_memorySizeComboBox( panel);
		setup_toLogFileCheckBox( panel);
		parent.add( panel);
	}

	/**
	 * @param parent
	 */
	private void setup_memorySizeComboBox(JPanel parent) {
		JLabel label = new JLabel( ResourceManager.get_instance().get( "create.docker.fileset.dialog.memory.size.label"));
		parent.add( label);

		String[] memorySizes = new String[ 1 + Constant._memorySizes.length];
		memorySizes[ 0] = ResourceManager.get_instance().get( "create.docker.fileset.dialog.memory.non.use");
		System.arraycopy( Constant._memorySizes, 0, memorySizes, 1, Constant._memorySizes.length);
		_memorySizeComboBox = ComboBox.create( memorySizes, 100, true, new CommonComboBoxRenderer( null, true));
		String memorySize = Environment.get_instance().get( Environment._dockerMemorySizeKey, CommonEnvironment.get_instance().get_memory_size());
		_memorySizeComboBox.setSelectedItem( memorySize.equals( "0") ? ResourceManager.get_instance().get( "create.docker.fileset.dialog.memory.non.use") : memorySize);
		parent.add( _memorySizeComboBox);
	}

	/**
	 * @param parent
	 */
	private void setup_toLogFileCheckBox(JPanel parent) {
		_toLogFileCheckBox = new JCheckBox(
			ResourceManager.get_instance().get( "edit.export.setting.dialog.check.box.to.file.name"));
			_toLogFileCheckBox.setSelected( Environment.get_instance().get( Environment._dockerLogToFileKey, "false").equals( "true"));
			parent.add( _toLogFileCheckBox);
	}

	/**
	 * @param parent
	 */
	protected void setup_generate_and_close_button(JPanel parent) {
		JPanel panel = new JPanel();
		panel.setLayout( new FlowLayout( FlowLayout.RIGHT, 5, 0));
		setup_ok_and_cancel_button(
			panel,
			_dockerPropertyPanel.get_generate_button_label(),
			_dockerPropertyPanel.get_close_button_label(),
			false, false);
		parent.add( panel);
	}

	/* (non-Javadoc)
	 * @see soars.common.utility.swing.window.Dialog#on_setup_completed()
	 */
	@Override
	protected void on_setup_completed() {
		super.on_setup_completed();

		//_minimumWidth = getPreferredSize().width;
		_minimumHeight = getPreferredSize().height;

		addComponentListener( new ComponentAdapter() {
			public void componentResized(ComponentEvent e){
				if ( 0 > _minimumWidth || 0 > _minimumHeight)
					return;

				int width = getSize().width;
				setSize( ( _minimumWidth > width) ? _minimumWidth : width, _minimumHeight);
			}
		});

		_dockerPropertyPanel.on_setup_completed();
	}

	/**
	 * 
	 */
	private void store() {
		_dockerPropertyPanel.store();

		String memorySize = ( String)_memorySizeComboBox.getSelectedItem();
		memorySize = ( memorySize.equals( ResourceManager.get_instance().get( "create.docker.fileset.dialog.memory.non.use")) ? "0" : memorySize);
		Environment.get_instance().set( Environment._dockerMemorySizeKey, memorySize);

		Environment.get_instance().set( Environment._dockerLogToFileKey, String.valueOf( _toLogFileCheckBox.isSelected()));
	}

	/* (non-Javadoc)
	 * @see soars.common.utility.swing.window.Dialog#on_ok(java.awt.event.ActionEvent)
	 */
	@Override
	protected void on_ok(ActionEvent actionEvent) {
		store();

		DockerFilesetProperty dockerFilesetProperty = new DockerFilesetProperty(
			Environment.get_instance().get( Environment._dockerFilesetCreatorSpecificDockerImageNameKey, "false").equals( "true"),
			Environment.get_instance().get( Environment._dockerFilesetCreatorDockerImageNameKey, ""),
			Environment.get_instance().get( Environment._dockerFilesetCreatorSpecificUserKey, "false").equals( "true"),
			Environment.get_instance().get( Environment._dockerFilesetCreatorUserIdKey, "1000"),
			Environment.get_instance().get( Environment._dockerFilesetCreatorUsernameKey, ""),
			Environment.get_instance().get( Environment._dockerFilesetCreatorPasswordKey, ""));

		if ( !dockerFilesetProperty.check( this))
			return;

		File dockerFilesetFile = VisualShellView.get_instance().get_docker_fileset_file();
		if ( null == dockerFilesetFile)
			return;

		boolean result = VisualShellView.get_instance().on_file_create_docker_fileset( dockerFilesetProperty, dockerFilesetFile, this);

		_dockerPropertyPanel.show_message( result, this);

		//super.on_ok(actionEvent);
	}

	/* (non-Javadoc)
	 * @see soars.common.utility.swing.window.Dialog#on_cancel(java.awt.event.ActionEvent)
	 */
	@Override
	protected void on_cancel(ActionEvent actionEvent) {
		store();
		super.on_cancel(actionEvent);
	}
}
