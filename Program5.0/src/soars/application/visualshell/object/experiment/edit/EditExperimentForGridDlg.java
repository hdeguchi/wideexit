/**
 * 
 */
package soars.application.visualshell.object.experiment.edit;

import java.awt.Component;
import java.awt.Frame;

import javax.swing.JPanel;

import soars.application.visualshell.object.experiment.ExperimentManager;
import soars.application.visualshell.object.experiment.edit.base.EditExperimentDlgBase1;

/**
 * The dialog box to edit the experiment data for Grid.
 * @author kurata / SOARS project
 */
public class EditExperimentForGridDlg extends EditExperimentDlgBase1 {

	/**
	 * Creates a non-modal dialog with the specified title and with the specified owner frame. If owner is null, a shared, hidden frame will be set as the owner of the dialog. 
	 * @param arg0 the Frame from which the dialog is displayed
	 * @param arg1 the String to display in the dialog's title bar
	 * @param arg2 true for a modal dialog, false for one that allows other windows to be active at the same time
	 * @param parent the parent container of this component
	 * @param experimentManager the experiment support manager
	 */
	public EditExperimentForGridDlg(Frame arg0, String arg1, boolean arg2, Component parent, ExperimentManager experimentManager) {
		super(arg0, arg1, arg2, parent, experimentManager);
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.experiment.edit.base.EditExperimentDlgBase#on_setup_south_panel(javax.swing.JPanel)
	 */
	protected void on_setup_south_panel(JPanel parent) {
		super.on_setup_south_panel( parent);
		setup_buttons_for_run( parent);
	}
}
