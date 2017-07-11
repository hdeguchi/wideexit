/**
 * 
 */
package soars.application.visualshell.object.experiment.edit.base;

import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.GridLayout;

import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;

import soars.application.visualshell.main.Constant;
import soars.application.visualshell.main.Environment;
import soars.application.visualshell.main.ResourceManager;
import soars.application.visualshell.object.docker.edit.comon.DockerPropertyPanel;
import soars.application.visualshell.object.experiment.ExperimentManager;
import soars.common.soars.environment.CommonEnvironment;
import soars.common.utility.swing.combo.ComboBox;
import soars.common.utility.swing.combo.CommonComboBoxRenderer;

/**
 * @author kurata
 *
 */
public class EditExperimentDlgBase2 extends EditExperimentDlgBase {

	/**
	 * 
	 */
	private ComboBox _memorySizeComboBox = null;

	/**
	 * 
	 */
	private DockerPropertyPanel _dockerPropertyPanel = null;

	/**
	 * @param arg0
	 * @param arg1
	 * @param arg2
	 * @param experimentManager
	 */
	public EditExperimentDlgBase2(Frame arg0, String arg1, boolean arg2, ExperimentManager experimentManager) {
		super(arg0, arg1, arg2, experimentManager);
	}

	/**
	 * @param arg0
	 * @param arg1
	 * @param arg2
	 * @param parent
	 * @param experimentManager
	 */
	public EditExperimentDlgBase2(Frame arg0, String arg1, boolean arg2, Component parent, ExperimentManager experimentManager) {
		super(arg0, arg1, arg2, parent, experimentManager);
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.experiment.edit.base.EditExperimentDlgBase#on_setup_south_panel(javax.swing.JPanel)
	 */
	@Override
	protected void on_setup_south_panel(JPanel parent) {
		super.on_setup_south_panel( parent);

		JPanel panel = new JPanel();
		panel.setLayout( new GridLayout( 1, 2));

		setup_leftPanel( panel);

		setup_rightPanel( panel);

		parent.add( panel);
	}

	/**
	 * @param parent
	 */
	private void setup_leftPanel(JPanel parent) {
		JPanel leftPanel = new JPanel();
		leftPanel.setLayout( new BoxLayout( leftPanel, BoxLayout.Y_AXIS));

		setup_append_experiment_button( leftPanel);

		insert_horizontal_glue( leftPanel);

		setup_remove_all_button( leftPanel);

		insert_horizontal_glue( leftPanel);

		setup_export_button( leftPanel);

		insert_horizontal_glue( leftPanel);

		setup_import_table_button( leftPanel);

		insert_horizontal_glue( leftPanel);

		setup_export_table_button( leftPanel);

		insert_horizontal_glue( leftPanel);

		setup_other_panel( leftPanel);

		parent.add( leftPanel);
	}

	/**
	 * @param parent
	 */
	private void setup_other_panel(JPanel parent) {
		JPanel panel = new JPanel();
		panel.setLayout( new FlowLayout( FlowLayout.LEFT, 5, 0));

		setup_number_of_times_numberSpinner( panel);

		setup_memorySizeComboBox( panel);

		setup_to_log_file_checkBox( Environment.get_instance().get( Environment._dockerLogToFileKey, "false").equals( "true"), panel);

		parent.add( panel);
	}

	/**
	 * @param parent
	 */
	private void setup_memorySizeComboBox(JPanel parent) {
		JLabel label = new JLabel( ResourceManager.get_instance().get( "create.docker.fileset.dialog.memory.size.label"));
		parent.add( label);

		String[] memorySizes = new String[ 1 + Constant._memorySizes.length];
		memorySizes[ 0] = ResourceManager.get_instance().get( "docker.memory.non.use");
		System.arraycopy( Constant._memorySizes, 0, memorySizes, 1, Constant._memorySizes.length);
		_memorySizeComboBox = ComboBox.create( memorySizes, 100, true, new CommonComboBoxRenderer( null, true));
		String memorySize = Environment.get_instance().get( Environment._dockerMemorySizeKey, CommonEnvironment.get_instance().get_memory_size());
		_memorySizeComboBox.setSelectedItem( memorySize.equals( "0") ? ResourceManager.get_instance().get( "create.docker.fileset.dialog.memory.non.use") : memorySize);
		parent.add( _memorySizeComboBox);
	}

	/**
	 * @param parent
	 */
	private void setup_rightPanel(JPanel parent) {
		_dockerPropertyPanel = new DockerPropertyPanel();
		_dockerPropertyPanel.setup();
		parent.add( _dockerPropertyPanel);
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
	 * @see soars.application.visualshell.object.experiment.edit.base.EditExperimentDlgBase#on_setup_completed()
	 */
	@Override
	protected void on_setup_completed() {
		super.on_setup_completed();
		_dockerPropertyPanel.on_setup_completed();
	}

	/**
	 * 
	 */
	protected void store() {
		_dockerPropertyPanel.store();
	}

	/**
	 * @param result
	 */
	protected void show_message(boolean result) {
		_dockerPropertyPanel.show_message( result, this);
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.experiment.edit.base.EditExperimentDlgBase#get()
	 */
	@Override
	protected boolean get() {
		if (!super.get())
			return false;

		_experimentManager._numberOfTimes = _numberOfTimesNumberSpinner.get_value();

		String memorySize = ( String)_memorySizeComboBox.getSelectedItem();
		memorySize = ( memorySize.equals( ResourceManager.get_instance().get( "create.docker.fileset.dialog.memory.non.use")) ? "0" : memorySize);
		Environment.get_instance().set( Environment._dockerMemorySizeKey, memorySize);

		Environment.get_instance().set( Environment._dockerLogToFileKey, String.valueOf( _toLogFileCheckBox.isSelected()));

		return true;
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.experiment.edit.base.EditExperimentDlgBase#save()
	 */
	@Override
	protected void save() {
		super.save();

		_experimentManager._numberOfTimes = _numberOfTimesNumberSpinner.get_value();

		String memorySize = ( String)_memorySizeComboBox.getSelectedItem();
		memorySize = ( memorySize.equals( ResourceManager.get_instance().get( "create.docker.fileset.dialog.memory.non.use")) ? "0" : memorySize);
		Environment.get_instance().set( Environment._dockerMemorySizeKey, memorySize);

		Environment.get_instance().set( Environment._dockerLogToFileKey, String.valueOf( _toLogFileCheckBox.isSelected()));
	}
}
