/*
 * 2005/05/11
 */
package soars.application.visualshell.layer;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.util.Vector;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;

import soars.common.utility.swing.window.Dialog;
import soars.application.visualshell.main.ResourceManager;

/**
 * The dialog box to select the layer.
 * @author kurata / SOARS project
 */
public class SelectLayerDlg extends Dialog {

	/**
	 * 
	 */
	private JComboBox _layerComboBox = null;

	/**
	 * Current layer.
	 */
	public int _sourceLayer;

	/**
	 * New layer.
	 */
	public int _targetLayer;

	/**
	 * The number of the layers.
	 */
	public int _number;

	/**
	 * Creates a non-modal dialog with the specified title and with the specified owner frame. If owner is null, a shared, hidden frame will be set as the owner of the dialog. 
	 * @param arg0 the Frame from which the dialog is displayed
	 * @param arg1 the String to display in the dialog's title bar
	 * @param arg2 true for a modal dialog, false for one that allows other windows to be active at the same time
	 * @param source_layer the current layer
	 * @param number the number of the layers
	 */
	public SelectLayerDlg(Frame arg0, String arg1, boolean arg2, int source_layer, int number) {
		super(arg0, arg1, arg2);
		_sourceLayer = source_layer;
		_number = number;
	}

	/* (Non Javadoc)
	 * @see soars.common.utility.swing.window.Dialog#on_init_dialog()
	 */
	protected boolean on_init_dialog() {
		if ( !super.on_init_dialog())
			return false;


		setResizable( false);


		link_to_cancel( getRootPane());


		getContentPane().setLayout( new BoxLayout( getContentPane(), BoxLayout.Y_AXIS));

		JPanel panel = setup_layer_combo_box();


		setup_ok_and_cancel_button(
			panel,
			ResourceManager.get_instance().get( "dialog.ok"),
			ResourceManager.get_instance().get( "dialog.cancel"),
			true, true);

		getContentPane().add( panel);


		setDefaultCloseOperation( DISPOSE_ON_CLOSE);


		return true;
	}

	/**
	 * @return
	 */
	private JPanel setup_layer_combo_box() {
		JPanel panel = new JPanel();
		panel.setLayout( new FlowLayout( FlowLayout.LEFT, 5, 5));

		JLabel label = new JLabel(
			ResourceManager.get_instance().get( "select.layer.dialog.name"));
		panel.add( label);

		panel.add( Box.createHorizontalStrut( 5));

		Vector<String> layers = new Vector<String>();
		for ( int i = 0; i < _number; ++i) {
			if ( _sourceLayer == i)
				continue;

			String name = ResourceManager.get_instance().get( "layer.combobox.name") + ( i + 1);
			layers.add( name);
		}
		_layerComboBox = new JComboBox( layers);

		_layerComboBox.setSelectedIndex( 0);

		_layerComboBox.setPreferredSize( new Dimension( 200,
			_layerComboBox.getPreferredSize().height));

		link_to_cancel( _layerComboBox);

		panel.add( _layerComboBox);
		panel.add( Box.createHorizontalStrut( 5));
		return panel;
	}

	/* (Non Javadoc)
	 * @see soars.common.utility.swing.window.Dialog#on_setup_completed()
	 */
	protected void on_setup_completed() {
		_layerComboBox.requestFocusInWindow();
	}

	/* (Non Javadoc)
	 * @see soars.common.utility.swing.window.Dialog#on_ok(java.awt.event.ActionEvent)
	 */
	protected void on_ok(ActionEvent actionEvent) {
		String item = ( String)_layerComboBox.getSelectedItem();
		_targetLayer = Integer.parseInt(
			item.substring( ResourceManager.get_instance().get( "layer.combobox.name").length())) - 1;

		super.on_ok(actionEvent);
	}
}
