/**
 * 
 */
package soars.application.visualshell.object.experiment.edit;

import java.awt.Component;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.util.List;

import soars.application.visualshell.object.experiment.ExperimentManager;

/**
 * @author kurata
 *
 */
public class EditExperimentForGeneticAlgorithmDlg extends EditExperimentForGridDlg {

	/**
	 * 
	 */
	private List<String> _algorithm = null;

	/**
	 * Creates a non-modal dialog with the specified title and with the specified owner frame. If owner is null, a shared, hidden frame will be set as the owner of the dialog. 
	 * @param arg0 the Frame from which the dialog is displayed
	 * @param arg1 the String to display in the dialog's title bar
	 * @param arg2 true for a modal dialog, false for one that allows other windows to be active at the same time
	 * @param parent the parent container of this component
	 * @param experimentManager the experiment support manager
	 * @param algorithm the algorithm
	 */
	public EditExperimentForGeneticAlgorithmDlg(Frame arg0, String arg1, boolean arg2, Component parent, ExperimentManager experimentManager, List<String> algorithm) {
		super(arg0, arg1, arg2, parent, experimentManager);
		_algorithm = algorithm;
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.experiment.edit.base.EditExperimentDlgBase#on_save_and_run(java.awt.event.ActionEvent)
	 */
	protected void on_save_and_run(ActionEvent actionEvent) {
		if ( !_experimentTable.can_export_for_genetic_algorithm( _algorithm)) {
			return;
		}

		super.on_save_and_run(actionEvent);
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.experiment.edit.base.EditExperimentDlgBase#on_run_without_saving(java.awt.event.ActionEvent)
	 */
	protected void on_run_without_saving(ActionEvent actionEvent) {
		if ( !_experimentTable.can_export_for_genetic_algorithm( _algorithm)) {
			return;
		}

		super.on_run_without_saving(actionEvent);
	}
}
