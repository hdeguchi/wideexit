/**
 * 
 */
package soars.application.visualshell.object.gis.file.importer;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.event.ActionEvent;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;

import soars.application.visualshell.main.Environment;
import soars.application.visualshell.main.ResourceManager;
import soars.common.utility.swing.window.Dialog;

/**
 * @author kurata
 *
 */
public class SelectCharacterCodeDlg extends Dialog {

	/**
	 * 
	 */
	private final String[] _encodings = new String[] {
		"UTF-8",
		"UTF-16",
		"MS932",
		"EUC_JP",
		"SJIS"
	};

	/**
	 * 
	 */
	private JComboBox _comboBox = null;

	/**
	 * @param arg0
	 * @param arg1
	 * @param arg2
	 */
	public SelectCharacterCodeDlg(Frame arg0, String arg1, boolean arg2) {
		super(arg0, arg1, arg2);
	}

	/* (non-Javadoc)
	 * @see soars.common.utility.swing.window.Dialog#on_init_dialog()
	 */
	protected boolean on_init_dialog() {
		if (!super.on_init_dialog())
			return false;

		setLayout( new BorderLayout());

		setup_northPanel();

		setup_southPanel();

		setDefaultCloseOperation( DISPOSE_ON_CLOSE);

		//adjust();

		return true;
	}

	/**
	 * 
	 */
	private void setup_northPanel() {
		JPanel northPanel = new JPanel();
		northPanel.setLayout( new BoxLayout( northPanel, BoxLayout.Y_AXIS));

		insert_horizontal_glue( northPanel);

		setup_comboBox( northPanel);

		add( northPanel, "North");
	}

	/**
	 * @param parent
	 */
	private void setup_comboBox(JPanel parent) {
		JPanel panel = new JPanel();
		panel.setLayout( new BoxLayout( panel, BoxLayout.X_AXIS));

		panel.add( Box.createHorizontalStrut( 5));

		JLabel label = new JLabel( ResourceManager.get_instance().get( "import.gis.data.select.character.code.dialog.label.character.code"));
		panel.add( label);

		panel.add( Box.createHorizontalStrut( 5));

		_comboBox = new JComboBox( _encodings);
		_comboBox.setPreferredSize( new Dimension( 100, _comboBox.getPreferredSize().height));
		select();
		panel.add( _comboBox);

		link_to_cancel( _comboBox);

		panel.add( Box.createHorizontalStrut( 5));

		parent.add( panel);
	}

	/**
	 * 
	 */
	private void select() {
		for ( String encoding:_encodings) {
			if ( Environment.get_instance().get( Environment._gisShapefileAnalyzerCharacterCode, "MS932").equals( encoding)) {
				_comboBox.setSelectedItem( encoding);
				break;
			}
		}
	}

	/**
	 * 
	 */
	private void setup_southPanel() {
		JPanel southPanel = new JPanel();
		southPanel.setLayout( new BoxLayout( southPanel, BoxLayout.Y_AXIS));

		JPanel panel = new JPanel();
		panel.setLayout( new FlowLayout( FlowLayout.RIGHT, 5, 0));

		insert_horizontal_glue( southPanel);

		setup_ok_and_cancel_button(
			panel,
			ResourceManager.get_instance().get( "dialog.ok"),
			ResourceManager.get_instance().get( "dialog.cancel"),
			false, false);
		southPanel.add( panel);

		insert_horizontal_glue( southPanel);

		add( southPanel, "South");
	}

	/* (non-Javadoc)
	 * @see soars.common.utility.swing.window.Dialog#on_ok(java.awt.event.ActionEvent)
	 */
	protected void on_ok(ActionEvent actionEvent) {
		Environment.get_instance().set( Environment._gisShapefileAnalyzerCharacterCode, ( String)_comboBox.getSelectedItem());
		super.on_ok(actionEvent);
	}
}
