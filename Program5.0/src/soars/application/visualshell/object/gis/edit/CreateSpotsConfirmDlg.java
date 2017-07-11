/**
 * 
 */
package soars.application.visualshell.object.gis.edit;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import soars.application.visualshell.main.ResourceManager;
import soars.common.utility.swing.window.Dialog;

/**
 * @author kurata
 *
 */
public class CreateSpotsConfirmDlg extends Dialog {

	/**
	 * 
	 */
	private double[] _range = null;

	/**
	 * 
	 */
	private List<JLabel> _labels = new ArrayList<JLabel>();

	/**
	 * 
	 */
	private List<JTextField> _textFields = new ArrayList<JTextField>();

	/**
	 * @param arg0
	 * @param arg1
	 * @param arg2
	 * @param range
	 */
	public CreateSpotsConfirmDlg(Frame arg0, String arg1, boolean arg2, double[] range) {
		super(arg0, arg1, arg2);
		_range = range;
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

		adjust();

		return true;
	}

	/**
	 * 
	 */
	private void setup_northPanel() {
		JPanel northPanel = new JPanel();
		northPanel.setLayout( new BoxLayout( northPanel, BoxLayout.Y_AXIS));

		insert_horizontal_glue( northPanel);

		setup_textFields( ResourceManager.get_instance().get( "create.spots.confirm.dialog.width"), _range[ 0], _range[ 2], northPanel);

		insert_horizontal_glue( northPanel);

		setup_textFields( ResourceManager.get_instance().get( "create.spots.confirm.dialog.height"), _range[ 1], _range[ 3], northPanel);

		insert_horizontal_glue( northPanel);

		setup_message( northPanel);

		add( northPanel, "North");
	}

	/**
	 * @param word
	 * @param mix
	 * @param max
	 * @param northPanel
	 */
	private void setup_textFields(String word, double min, double max, JPanel parent) {
		JPanel panel = new JPanel();
		panel.setLayout( new BoxLayout( panel, BoxLayout.X_AXIS));

		panel.add( Box.createHorizontalStrut( 5));

		JLabel label = new JLabel( word);
		_labels.add( label);
		panel.add( label);

		panel.add( Box.createHorizontalStrut( 5));

		JTextField textField = new JTextField( String.valueOf( min));
		textField.setHorizontalAlignment( SwingConstants.RIGHT);
		textField.setEditable( false);
		_textFields.add( textField);
		panel.add( textField);

		panel.add( Box.createHorizontalStrut( 5));

		label = new JLabel( " - ");
		panel.add( label);

		panel.add( Box.createHorizontalStrut( 5));

		textField = new JTextField( String.valueOf( max));
		textField.setHorizontalAlignment( SwingConstants.RIGHT);
		textField.setEditable( false);
		_textFields.add( textField);
		panel.add( textField);

		panel.add( Box.createHorizontalStrut( 5));

		parent.add( panel);
	}

	/**
	 * @param parent
	 */
	private void setup_message(JPanel parent) {
		JPanel panel = new JPanel();
		panel.setLayout( new FlowLayout( FlowLayout.RIGHT, 5, 0));

		JLabel label = new JLabel( ResourceManager.get_instance().get( "create.spots.confirm.dialog.message"));
		panel.add( label);

		parent.add( panel);
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

	/**
	 * 
	 */
	private void adjust() {
		int width = 0;
		for ( JComponent component:_labels)
			width = Math.max( width, component.getPreferredSize().width);

		for ( JComponent component:_labels)
			component.setPreferredSize( new Dimension( width, component.getPreferredSize().height));

		width = 0;
		for ( JComponent component:_textFields)
			width = Math.max( width, component.getPreferredSize().width);

		for ( JComponent component:_textFields)
			component.setPreferredSize( new Dimension( width, component.getPreferredSize().height));
	}

	/* (non-Javadoc)
	 * @see soars.common.utility.swing.window.Dialog#on_ok(java.awt.event.ActionEvent)
	 */
	protected void on_ok(ActionEvent actionEvent) {
		super.on_ok(actionEvent);
	}
}
