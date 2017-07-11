/**
 * 
 */
package soars.application.visualshell.object.experiment.edit.base;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JPanel;

import soars.application.visualshell.main.Environment;
import soars.application.visualshell.main.ResourceManager;
import soars.application.visualshell.object.experiment.ExperimentManager;
import soars.application.visualshell.observer.Observer;

/**
 * @author kurata
 *
 */
public class EditExperimentDlgBase1 extends EditExperimentDlgBase {

	/**
	 * 
	 */
	protected JCheckBox _parallelCheckBox = null;

	/**
	 * 
	 */
	protected JCheckBox _keepUserDataFileCheckBox = null;

	/**
	 * @param arg0
	 * @param arg1
	 * @param arg2
	 * @param experimentManager
	 */
	public EditExperimentDlgBase1(Frame arg0, String arg1, boolean arg2, ExperimentManager experimentManager) {
		super(arg0, arg1, arg2, experimentManager);
	}

	/**
	 * @param arg0
	 * @param arg1
	 * @param arg2
	 * @param parent
	 * @param experimentManager
	 */
	public EditExperimentDlgBase1(Frame arg0, String arg1, boolean arg2, Component parent, ExperimentManager experimentManager) {
		super(arg0, arg1, arg2, parent, experimentManager);
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.experiment.edit.base.EditExperimentDlgBase#on_setup_south_panel(javax.swing.JPanel)
	 */
	@Override
	protected void on_setup_south_panel(JPanel parent) {
		super.on_setup_south_panel( parent);

		setup_append_experiment_button( parent);

		insert_horizontal_glue( parent);

		setup_remove_all_button( parent);

		insert_horizontal_glue( parent);

		setup_export_button( parent);

		insert_horizontal_glue( parent);

		setup_import_table_button( parent);

		insert_horizontal_glue( parent);

		setup_export_table_button( parent);

		insert_horizontal_glue( parent);
	}

	/**
	 * @param parent
	 */
	protected void setup_parallel_checkBox(JPanel parent) {
		JPanel checkBoxPanel = new JPanel();
		checkBoxPanel.setLayout( new BoxLayout( checkBoxPanel, BoxLayout.X_AXIS));

		_parallelCheckBox = new JCheckBox(
			ResourceManager.get_instance().get( "edit.experiment.dialog.parallel.check.box.name"));
		_parallelCheckBox.setSelected( _experimentManager._parallel);
		checkBoxPanel.add( _parallelCheckBox);
		parent.add( checkBoxPanel);

		parent.add( Box.createHorizontalStrut( 5));
	}

	/**
	 * @param parent
	 */
	protected void setup_keep_user_data_file_checkBox(JPanel parent) {
		_keepUserDataFileCheckBox = new JCheckBox(
			ResourceManager.get_instance().get( "edit.export.setting.dialog.check.box.keep.user.data.file.name"));
		_keepUserDataFileCheckBox.setSelected( Environment.get_instance().get( Environment._editExportSettingDialogKeepUserDataFileKey, "false").equals( "true"));
		parent.add( _keepUserDataFileCheckBox);

		parent.add( Box.createHorizontalStrut( 5));
	}

	/**
	 * @param parent
	 */
	protected void setup_ok_and_cancel_button(JPanel parent) {
		JPanel panel = new JPanel();
		panel.setLayout( new FlowLayout( FlowLayout.RIGHT, 5, 0));
		setup_ok_and_cancel_button(
			panel,
			ResourceManager.get_instance().get( "dialog.ok"),
			ResourceManager.get_instance().get( "dialog.cancel"),
			false, false);
		parent.add( panel);
	}

	/**
	 * @param parent
	 */
	protected void setup_buttons_for_run(JPanel parent) {
		JPanel panel = new JPanel();
		panel.setLayout( new FlowLayout( FlowLayout.RIGHT, 5, 0));

		JButton saveAndRunButton = new JButton( ResourceManager.get_instance().get( "edit.experiment.dialog.save.and.run"));

		saveAndRunButton.addActionListener( new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				on_save_and_run( arg0);
			}
		});

		panel.add( saveAndRunButton);


		JButton runWithoutSavingButton = new JButton( ResourceManager.get_instance().get( "edit.experiment.dialog.run.without.saving"));

		runWithoutSavingButton.addActionListener( new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				on_run_without_saving( arg0);
			}
		});

		panel.add( runWithoutSavingButton);


		JButton saveButton = new JButton( ResourceManager.get_instance().get( "edit.experiment.dialog.save"));

		saveButton.addActionListener( new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				on_save( arg0);
			}
		});

		panel.add( saveButton);


		JButton cancelButton = new JButton( ResourceManager.get_instance().get( "dialog.cancel"));
		cancelButton.addActionListener( new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				on_cancel( arg0);
			}
		});

		panel.add( cancelButton);
		parent.add( panel);


		Dimension saveAndRunButtonDimension = saveAndRunButton.getPreferredSize();
		Dimension runWithoutSavingButtonDimension = runWithoutSavingButton.getPreferredSize();
		Dimension saveButtonDimension = saveButton.getPreferredSize();

		int width = Math.max( saveAndRunButtonDimension.width, Math.max( runWithoutSavingButtonDimension.width, saveButtonDimension.width));
		saveAndRunButton.setPreferredSize( new Dimension( width, saveAndRunButtonDimension.height));
		runWithoutSavingButton.setPreferredSize( new Dimension( width, runWithoutSavingButtonDimension.height));
		saveButton.setPreferredSize( new Dimension( width, saveButtonDimension.height));
	}

	/**
	 * @param actionEvent
	 */
	protected void on_save_and_run(ActionEvent actionEvent) {
		if ( !get())
			return;

		ExperimentManager.get_instance().copy( _experimentManager);

		Observer.get_instance().on_update_experiment();
		Observer.get_instance().modified();

		set_property_to_environment_file();

		if ( !_experimentTable.can_export())
			return;

		super.on_ok( null);
	}

	/**
	 * @param actionEvent
	 */
	protected void on_run_without_saving(ActionEvent actionEvent) {
		if ( !get())
			return;

		set_property_to_environment_file();

		if ( !_experimentTable.can_export())
			return;

		super.on_ok( null);
	}

	/**
	 * @param actionEvent
	 */
	protected void on_save(ActionEvent actionEvent) {
		save();

		ExperimentManager.get_instance().copy( _experimentManager);

		Observer.get_instance().on_update_experiment();
		Observer.get_instance().modified();
	}

	/* (Non Javadoc)
	 * @see soars.common.utility.swing.window.Dialog#on_cancel(java.awt.event.ActionEvent)
	 */
	protected void on_cancel(ActionEvent actionEvent) {
		set_property_to_environment_file();
		super.on_cancel(actionEvent);
	}
}
