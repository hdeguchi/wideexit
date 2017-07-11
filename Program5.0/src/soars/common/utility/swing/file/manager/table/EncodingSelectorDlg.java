/**
 * 
 */
package soars.common.utility.swing.file.manager.table;

import java.awt.Dimension;
import java.awt.Frame;
import java.awt.HeadlessException;
import java.awt.event.ActionEvent;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JComboBox;
import javax.swing.JPanel;

import soars.common.utility.swing.file.manager.ResourceManager;
import soars.common.utility.swing.window.Dialog;

/**
 * @author kurata
 *
 */
public class EncodingSelectorDlg extends Dialog {

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
	private JComboBox _encodingComboBox = null;

	/**
	 * 
	 */
	public String _encoding = "";

	/**
	 * @param arg0
	 * @param arg1
	 * @param arg2
	 * @throws HeadlessException
	 */
	public EncodingSelectorDlg(Frame arg0, String arg1, boolean arg2) throws HeadlessException {
		super(arg0, arg1, arg2);
	}

	/**
	 * @param arg0
	 * @param arg1
	 * @param arg2
	 * @param encoding
	 * @throws HeadlessException
	 */
	public EncodingSelectorDlg(Frame arg0, String arg1, boolean arg2, String encoding) throws HeadlessException {
		super(arg0, arg1, arg2);
		_encoding = encoding;
	}

	/* (non-Javadoc)
	 * @see soars.common.utility.swing.window.Dialog#on_init_dialog()
	 */
	protected boolean on_init_dialog() {
		if ( !super.on_init_dialog())
			return false;


		link_to_cancel( getRootPane());


		getContentPane().setLayout( new BoxLayout( getContentPane(), BoxLayout.Y_AXIS));

		insert_horizontal_glue();

		setup_encode_name_comboBox();

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
	 * 
	 */
	private void setup_encode_name_comboBox() {
		JPanel panel = new JPanel();
		panel.setLayout( new BoxLayout( panel, BoxLayout.X_AXIS));

		panel.add( Box.createHorizontalStrut( 5));

		_encodingComboBox = new JComboBox( _encodings);
		select();
		_encodingComboBox.setPreferredSize( new Dimension( 100, _encodingComboBox.getPreferredSize().height));

		link_to_cancel( _encodingComboBox);

		panel.add( _encodingComboBox);
		panel.add( Box.createHorizontalStrut( 5));
		getContentPane().add( panel);
	}

	/**
	 * 
	 */
	private void select() {
		for ( int i = 0; i < _encodings.length; ++i) {
			if ( _encodings[ i].equals( _encoding))
				_encodingComboBox.setSelectedItem( _encoding);
		}
	}

	/* (non-Javadoc)
	 * @see soars.common.utility.swing.window.Dialog#on_ok(java.awt.event.ActionEvent)
	 */
	protected void on_ok(ActionEvent actionEvent) {
		_encoding = ( String)_encodingComboBox.getSelectedItem();
		super.on_ok(actionEvent);
	}
}
