/**
 * 
 */
package soars.application.visualshell.file.common;

import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.event.ActionEvent;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JPanel;
import javax.swing.JRadioButton;

import soars.application.visualshell.main.Environment;
import soars.application.visualshell.main.ResourceManager;
import soars.common.utility.swing.window.Dialog;

/**
 * The dialog box to select the type of the initial data imported or exported.
 * @author kurata / SOARS project
 */
public class EditInitialDataSelectionDlg extends Dialog {

	/**
	 * 
	 */
	private String _key = "";

	/**
	 * 
	 */
	private JRadioButton[] _radioButtons1 = new JRadioButton[] {
		null, null
	};

	/**
	 * Creates a non-modal dialog with the specified title and with the specified owner frame. If owner is null, a shared, hidden frame will be set as the owner of the dialog. 
	 * @param arg0 the Frame from which the dialog is displayed
	 * @param arg1 the String to display in the dialog's title bar
	 * @param arg2 true for a modal dialog, false for one that allows other windows to be active at the same time
	 */
	public EditInitialDataSelectionDlg(Frame arg0, String arg1, boolean arg2, String key) {
		super(arg0, arg1, arg2);
		_key = key;
	}

	/* (non-Javadoc)
	 * @see soars.common.utility.swing.window.Dialog#on_init_dialog()
	 */
	protected boolean on_init_dialog() {
		if ( !super.on_init_dialog())
			return false;


		setResizable( false);


		link_to_cancel( getRootPane());


		getContentPane().setLayout( new BoxLayout( getContentPane(), BoxLayout.Y_AXIS));

		insert_horizontal_glue();

		ButtonGroup buttonGroup1 = new ButtonGroup();

		setup_agents_and_spots( buttonGroup1);

		insert_horizontal_glue();

		setup_all( buttonGroup1);

		insert_horizontal_glue();

		setup_ok_and_cancel_button(
			ResourceManager.get_instance().get( "dialog.ok"),
			ResourceManager.get_instance().get( "dialog.cancel"),
			true, true);

		insert_horizontal_glue();


		setDefaultCloseOperation( DISPOSE_ON_CLOSE);


		return true;
	}

	/**
	 * @param buttonGroup1
	 */
	private void setup_agents_and_spots(ButtonGroup buttonGroup1) {
		JPanel panel = new JPanel();
		panel.setLayout( new FlowLayout( FlowLayout.LEFT, 5, 0));

		panel.add( Box.createHorizontalStrut( 5));

		_radioButtons1[ 0] = new JRadioButton(
			ResourceManager.get_instance().get( "edit.initial.data.selection.dialog.agents.and.spots.data"));
		buttonGroup1.add( _radioButtons1[ 0]);
		link_to_cancel( _radioButtons1[ 0]);
		panel.add( _radioButtons1[ 0]);

		panel.add( Box.createHorizontalStrut( 5));

		getContentPane().add( panel);
	}

	/**
	 * @param buttonGroup1
	 */
	private void setup_all(ButtonGroup buttonGroup1) {
		JPanel panel = new JPanel();
		panel.setLayout( new FlowLayout( FlowLayout.LEFT, 5, 0));

		panel.add( Box.createHorizontalStrut( 5));

		_radioButtons1[ 1] = new JRadioButton(
			ResourceManager.get_instance().get( "edit.initial.data.selection.dialog.all.data"));
		buttonGroup1.add( _radioButtons1[ 1]);
		link_to_cancel( _radioButtons1[ 1]);
		panel.add( _radioButtons1[ 1]);

		panel.add( Box.createHorizontalStrut( 5));

		getContentPane().add( panel);
	}

	/* (non-Javadoc)
	 * @see soars.common.utility.swing.window.Dialog#on_setup_completed()
	 */
	protected void on_setup_completed() {
		_radioButtons1[ Environment.get_instance().get( _key, "false").equals( "true") ? 1 : 0].setSelected( true);
		super.on_setup_completed();
	}

	/* (non-Javadoc)
	 * @see soars.common.utility.swing.window.Dialog#on_ok(java.awt.event.ActionEvent)
	 */
	protected void on_ok(ActionEvent actionEvent) {
		Environment.get_instance().set( _key, String.valueOf( _radioButtons1[ 1].isSelected()));
		super.on_ok(actionEvent);
	}
}
