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
import javax.swing.ImageIcon;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;

import soars.common.utility.swing.file.manager.Constant;
import soars.common.utility.swing.file.manager.ResourceManager;
import soars.common.utility.swing.window.Dialog;

/**
 * @author kurata
 *
 */
public class ConvertEncodingDlg extends Dialog {

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
	private JComboBox[] _encodingComboBoxes = new JComboBox[ 2];

	/**
	 * 
	 */
	static public String _defaultEncodings[] = new String[] { "", ""};

	/**
	 * @param arg0
	 * @param arg1
	 * @param arg2
	 * @throws HeadlessException
	 */
	public ConvertEncodingDlg(Frame arg0, String arg1, boolean arg2) throws HeadlessException {
		super(arg0, arg1, arg2);
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

		setup_encodingComboBoxes();

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
	private void setup_encodingComboBoxes() {
		for ( int i = 0; i < _encodingComboBoxes.length; ++i) {
			_encodingComboBoxes[ i] = new JComboBox( _encodings);
			select( i);
			_encodingComboBoxes[ i].setPreferredSize( new Dimension( 100, _encodingComboBoxes[ i].getPreferredSize().height));
			link_to_cancel( _encodingComboBoxes[ i]);
		}

		JLabel label = new JLabel();
		label.setIcon( new ImageIcon( getClass().getResource( Constant._resourceDirectory + "/image/convert.png")));

		JPanel panel = new JPanel();
		panel.setLayout( new BoxLayout( panel, BoxLayout.X_AXIS));

		panel.add( Box.createHorizontalStrut( 5));

		panel.add( _encodingComboBoxes[ 0]);

		panel.add( Box.createHorizontalStrut( 5));

		panel.add( label);

		panel.add( Box.createHorizontalStrut( 5));

		panel.add( _encodingComboBoxes[ 1]);

		panel.add( Box.createHorizontalStrut( 5));

		getContentPane().add( panel);
	}

	/**
	 * @param index
	 */
	private void select(int index) {
		for ( String encoding:_encodings) {
			if ( _defaultEncodings[ index].equals( encoding)) {
				_encodingComboBoxes[ index].setSelectedItem( encoding);
				break;
			}
		}
	}

	/* (non-Javadoc)
	 * @see soars.common.utility.swing.window.Dialog#on_ok(java.awt.event.ActionEvent)
	 */
	protected void on_ok(ActionEvent actionEvent) {
		if ( ( ( String)_encodingComboBoxes[ 0].getSelectedItem()).equals( ( String)_encodingComboBoxes[ 1].getSelectedItem()))
			return;

		for ( int i = 0; i < _encodingComboBoxes.length; ++i)
			_defaultEncodings[ i] = ( String)_encodingComboBoxes[ i].getSelectedItem();
		super.on_ok(actionEvent);
	}
}
