/**
 * 
 */
package soars.application.visualshell.object.experiment.edit;

import java.awt.FlowLayout;
import java.awt.Frame;

import javax.swing.JPanel;

import soars.application.visualshell.main.Environment;
import soars.application.visualshell.object.experiment.ExperimentManager;
import soars.application.visualshell.object.experiment.edit.base.EditExperimentDlgBase1;

/**
 * The dialog box to edit the experiment data for Simulator.
 * @author kurata / SOARS project
 */
public class EditExperimentForSimulatorDlg extends EditExperimentDlgBase1 {

	/**
	 * Creates a non-modal dialog with the specified title and with the specified owner frame. If owner is null, a shared, hidden frame will be set as the owner of the dialog. 
	 * @param arg0 the Frame from which the dialog is displayed
	 * @param arg1 the String to display in the dialog's title bar
	 * @param arg2 true for a modal dialog, false for one that allows other windows to be active at the same time
	 * @param experimentManager the experiment support manager
	 */
	public EditExperimentForSimulatorDlg(Frame arg0, String arg1, boolean arg2, ExperimentManager experimentManager) {
		super(arg0, arg1, arg2, experimentManager);
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.experiment.edit.base.EditExperimentDlgBase#on_setup_south_panel(javax.swing.JPanel)
	 */
	protected void on_setup_south_panel(JPanel parent) {
		super.on_setup_south_panel( parent);

		JPanel panel = new JPanel();
		panel.setLayout( new FlowLayout( FlowLayout.LEFT, 5, 0));

		setup_number_of_times_numberSpinner( panel);

		setup_to_log_file_checkBox( Environment.get_instance().get( Environment._editExportSettingDialogToFileKey, "false").equals( "true"), panel);

		parent.add( panel);

		insert_horizontal_glue( parent);

		setup_buttons_for_run( parent);
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.experiment.edit.base.EditExperimentDlgBase#get()
	 */
	protected boolean get() {
		if ( !super.get())
			return false;

		_experimentManager._numberOfTimes = _numberOfTimesNumberSpinner.get_value();

		Environment.get_instance().set( Environment._editExportSettingDialogToFileKey, String.valueOf( _toLogFileCheckBox.isSelected()));

		return true;
	}
}
