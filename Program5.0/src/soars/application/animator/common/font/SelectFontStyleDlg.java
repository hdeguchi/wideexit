/*
 * 2005/03/10
 */
package soars.application.animator.common.font;

import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.event.ActionEvent;

import javax.swing.BoxLayout;
import javax.swing.JComboBox;
import javax.swing.JPanel;

import soars.common.utility.swing.window.Dialog;
import soars.application.animator.common.tool.CommonTool;
import soars.application.animator.main.ResourceManager;

/**
 * The dialog box to select Font style name.
 * @author kurata / SOARS project
 */
public class SelectFontStyleDlg extends Dialog {

	/**
	 * Font style name.
	 */
	public String _style = "";

	/**
	 * Font style names combo box.
	 */
	private JComboBox _comboBox = null;

	/**
	 * Creates a non-modal dialog with the specified title and with the specified owner frame. If owner is null, a shared, hidden frame will be set as the owner of the dialog. 
	 * @param arg0 the Frame from which the dialog is displayed
	 * @param arg1 true for a modal dialog, false for one that allows other windows to be active at the same time
	 */
	public SelectFontStyleDlg(Frame arg0, boolean arg1) {
		super(arg0,
			ResourceManager.get_instance().get( "select.font.style.dialog.title"),
			arg1);
	}

	/**
	 * Creates a non-modal dialog with the specified title and with the specified owner frame. If owner is null, a shared, hidden frame will be set as the owner of the dialog. 
	 * @param arg0 the Frame from which the dialog is displayed
	 * @param arg1 true for a modal dialog, false for one that allows other windows to be active at the same time
	 * @param the Font style name
	 */
	public SelectFontStyleDlg(Frame arg0, boolean arg1, String style) {
		super(arg0,
			ResourceManager.get_instance().get( "select.font.style.dialog.title"),
			arg1);
		_style = style;
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

		JPanel panel = setup_font_style_combo_box();

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
	 * Returns the JPanel for Font style names combo box.
	 * @return the JPanel for Font style names combo box
	 */
	private JPanel setup_font_style_combo_box() {
		JPanel panel = new JPanel();
		panel.setLayout( new FlowLayout( FlowLayout.LEFT, 5, 5));

		_comboBox = CommonTool.get_font_style_combo_box();

		if ( !_style.equals( ""))
			_comboBox.setSelectedItem( _style);

		link_to_cancel( _comboBox);

		panel.add( _comboBox);
		return panel;
	}

	/* (Non Javadoc)
	 * @see soars.common.utility.swing.window.Dialog#on_ok(java.awt.event.ActionEvent)
	 */
	protected void on_ok(ActionEvent actionEvent) {
		_style = ( String)_comboBox.getSelectedItem();
		super.on_ok(actionEvent);
	}
}
