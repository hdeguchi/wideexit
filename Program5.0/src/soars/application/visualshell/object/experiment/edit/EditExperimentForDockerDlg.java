/**
 * 
 */
package soars.application.visualshell.object.experiment.edit;

import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.io.File;

import javax.swing.JOptionPane;
import javax.swing.JPanel;

import soars.application.visualshell.file.docker.DockerFilesetProperty;
import soars.application.visualshell.main.Environment;
import soars.application.visualshell.main.ResourceManager;
import soars.application.visualshell.main.VisualShellView;
import soars.application.visualshell.object.experiment.ExperimentManager;
import soars.application.visualshell.object.experiment.edit.base.EditExperimentDlgBase2;
import soars.application.visualshell.observer.Observer;

/**
 * @author kurata
 *
 */
public class EditExperimentForDockerDlg extends EditExperimentDlgBase2 {

	/**
	 * @param arg0
	 * @param arg1
	 * @param arg2
	 * @param experimentManager
	 */
	public EditExperimentForDockerDlg(Frame arg0, String arg1, boolean arg2, ExperimentManager experimentManager) {
		super(arg0, arg1, arg2, experimentManager);
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.experiment.edit.base.EditExperimentDlgBase#on_setup_south_panel(javax.swing.JPanel)
	 */
	@Override
	protected void on_setup_south_panel(JPanel parent) {
		super.on_setup_south_panel( parent);

		setup_generate_and_close_button( parent);
	}

	/* (non-Javadoc)
	 * @see soars.common.utility.swing.window.Dialog#on_ok(java.awt.event.ActionEvent)
	 */
	@Override
	protected void on_ok(ActionEvent actionEvent) {
		if ( !get())
			return;

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

		boolean result = VisualShellView.get_instance().on_file_create_docker_fileset( dockerFilesetProperty, dockerFilesetFile, _experimentManager, this);

		show_message( result);

		//super.on_ok(actionEvent);
	}

	/* (non-Javadoc)
	 * @see soars.common.utility.swing.window.Dialog#on_cancel(java.awt.event.ActionEvent)
	 */
	@Override
	protected void on_cancel(ActionEvent actionEvent) {
		save();

		store();

		if ( !_experimentManager.sama_as( ExperimentManager.get_instance())) {
			int result = JOptionPane.showConfirmDialog(
				this,
				ResourceManager.get_instance().get( "create.docker.fileset.dialog.confirm.message"),
				ResourceManager.get_instance().get( "application.title"),
				JOptionPane.YES_NO_CANCEL_OPTION);
			switch ( result) {
				case JOptionPane.YES_OPTION:
					ExperimentManager.get_instance().copy( _experimentManager);
					Observer.get_instance().modified();
					break;
				case JOptionPane.NO_OPTION:
					break;
				case JOptionPane.CANCEL_OPTION:
					return;
			}
		}

		set_property_to_environment_file();

		super.on_cancel(actionEvent);
	}
}
