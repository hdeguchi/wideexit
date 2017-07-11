/*
 * 2005/07/12
 */
package soars.application.visualshell.object.experiment.edit;

import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.event.ActionEvent;

import javax.swing.JPanel;

import soars.application.visualshell.object.experiment.ExperimentManager;
import soars.application.visualshell.object.experiment.edit.base.EditExperimentDlgBase1;
import soars.application.visualshell.observer.Observer;

/**
 * The dialog box to edit the experiment data.
 * @author kurata / SOARS project
 */
public class EditExperimentDlg extends EditExperimentDlgBase1 {

	/**
	 * Creates a non-modal dialog with the specified title and with the specified owner frame. If owner is null, a shared, hidden frame will be set as the owner of the dialog. 
	 * @param arg0 the Frame from which the dialog is displayed
	 * @param arg1 the String to display in the dialog's title bar
	 * @param arg2 true for a modal dialog, false for one that allows other windows to be active at the same time
	 * @param experimentManager the experiment support manager
	 */
	public EditExperimentDlg(Frame arg0, String arg1, boolean arg2, ExperimentManager experimentManager) {
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

		setup_parallel_checkBox( panel);

		parent.add( panel);

		insert_horizontal_glue( parent);

		setup_ok_and_cancel_button( parent);
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.experiment.edit.base.EditExperimentDlgBase#save()
	 */
	@Override
	protected void save() {
		// TODO Auto-generated method stub
		super.save();
		_experimentManager._numberOfTimes = _numberOfTimesNumberSpinner.get_value();
		_experimentManager._parallel = _parallelCheckBox.isSelected();
	}

	/* (Non Javadoc)
	 * @see soars.common.utility.swing.window.Dialog#on_ok(java.awt.event.ActionEvent)
	 */
	protected void on_ok(ActionEvent actionEvent) {
		save();

		set_property_to_environment_file();

		Observer.get_instance().on_update_experiment();
		Observer.get_instance().modified();

		super.on_ok(actionEvent);
	}
}
