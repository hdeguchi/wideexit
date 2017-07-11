/*
 * 2005/03/09
 */
package soars.application.animator.object.property.base.edit.select;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Vector;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;

import soars.common.utility.swing.window.Dialog;
import soars.application.animator.main.ResourceManager;

/**
 * The dialog box to select the visible properties.
 * @author kurata / SOARS project
 */
public class SelectPropertyDlg extends Dialog {

	/**
	 * 
	 */
	private Vector _order = null;

	/**
	 * 
	 */
	private Vector _original_selected_properties = null;

	/**
	 * 
	 */
	private Vector _selected_properties = new Vector();

	/**
	 * 
	 */
	private DefaultListModel _model = null;

	/**
	 * 
	 */
	private JList _order_list = null;

	/**
	 * 
	 */
	private JButton _edit_order_button = null;

	/**
	 * 
	 */
	private JTextField _selected_properties_text_field = null;

	/**
	 * Creates a non-modal dialog with the specified title and with the specified owner frame. If owner is null, a shared, hidden frame will be set as the owner of the dialog. 
	 * @param arg0 the Frame from which the dialog is displayed
	 * @param arg1 the String to display in the dialog's title bar
	 * @param arg2 true for a modal dialog, false for one that allows other windows to be active at the same time
	 * @param order the array of the properties order
	 * @param selected_properties the array of the visible properties
	 */
	public SelectPropertyDlg(Frame arg0, String arg1, boolean arg2, Vector order, Vector selected_properties) {
		super(arg0, arg1, arg2);
		_order = order;
		_original_selected_properties = selected_properties;
		_selected_properties.addAll( selected_properties);
	}

	/* (Non Javadoc)
	 * @see soars.common.utility.swing.window.Dialog#on_init_dialog()
	 */
	protected boolean on_init_dialog() {
		if ( !super.on_init_dialog())
			return false;


		setResizable( false);


		getContentPane().setLayout( new BoxLayout( getContentPane(), BoxLayout.Y_AXIS));

		insert_horizontal_glue();

		setup_order_list();

		insert_horizontal_glue();

		setup_selected_properties_text_field();

		insert_horizontal_glue();

		setup_edit_order_button();

		insert_horizontal_glue();

		setup_ok_and_cancel_button(
			ResourceManager.get_instance().get( "dialog.ok"),
			ResourceManager.get_instance().get( "dialog.cancel"),
			false, false);

		insert_horizontal_glue();


		setDefaultCloseOperation( DISPOSE_ON_CLOSE);

		return true;
	}

	/**
	 * 
	 */
	private void setup_order_list() {
		JPanel panel = new JPanel();
		panel.setLayout( new FlowLayout( FlowLayout.LEFT, 5, 0));

		JLabel label = new JLabel(
			ResourceManager.get_instance().get( "select.property.dialog.list"));
		panel.add( label);
		getContentPane().add( panel);


		panel = new JPanel();
		panel.setLayout( new BoxLayout( panel, BoxLayout.X_AXIS));

		panel.add( Box.createHorizontalStrut( 5));

		_model = new DefaultListModel();
		for ( int i = 0; i < _order.size(); ++i) {
			String name = ( String)_order.get( i);
			_model.addElement( new JCheckBox( name,
				_selected_properties.contains( name)));
		}

		_order_list = new JList( _model);
		PropertyListCellRenderer PropertyListCellRenderer = new PropertyListCellRenderer();
		_order_list.setCellRenderer( PropertyListCellRenderer);
		_order_list.addMouseListener( new MouseAdapter() {
			public void mouseClicked(MouseEvent arg0) {
				on_select_changed( arg0);
			}
		});

		JScrollPane scrollPane = new JScrollPane();
		scrollPane.getViewport().setView( _order_list);
		scrollPane.setPreferredSize( new Dimension( 400, 200));

		panel.add( scrollPane);
		panel.add( Box.createHorizontalStrut( 5));
		getContentPane().add( panel);
	}

	/**
	 * 
	 */
	private void setup_selected_properties_text_field() {
		JPanel panel = new JPanel();
		panel.setLayout( new BoxLayout( panel, BoxLayout.X_AXIS));

		panel.add( Box.createHorizontalStrut( 5));

		JLabel label = new JLabel(
			ResourceManager.get_instance().get( "select.property.dialog.order"));
		panel.add( label);

		panel.add( Box.createHorizontalStrut( 5));

		_selected_properties_text_field = new JTextField( get_selected_properties_text());
		_selected_properties_text_field.setEditable( false);
		panel.add( _selected_properties_text_field);

		panel.add( Box.createHorizontalStrut( 5));

		getContentPane().add( panel);
	}

	/**
	 * 
	 */
	private void setup_edit_order_button() {
		JPanel panel = new JPanel();
		panel.setLayout( new BoxLayout( panel, BoxLayout.X_AXIS));

		panel.add( Box.createHorizontalStrut( 5));

		JPanel buttonPanel = new JPanel();
		buttonPanel.setLayout( new GridLayout( 1, 1));

		_edit_order_button = new JButton(
			ResourceManager.get_instance().get( "select.property.dialog.order.change.button"));
		_edit_order_button.setEnabled( 1 < _selected_properties.size());
		_edit_order_button.addActionListener( new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				on_edit_order( arg0);
			}
		});

		buttonPanel.add( _edit_order_button);
		panel.add( buttonPanel);
		panel.add( Box.createHorizontalStrut( 5));
		getContentPane().add( panel);
	}

	/**
	 * @return
	 */
	private String get_selected_properties_text() {
		String text = "";
		for ( int i = 0; i < _selected_properties.size(); ++i) {
			text += _selected_properties.get( i);
			if ( _selected_properties.size() - 1 > i)
				text += ", ";
		}
		return text;
	}

	/**
	 * @param mouseEvent
	 */
	protected void on_select_changed(MouseEvent mouseEvent) {
		int index = _order_list.locationToIndex( mouseEvent.getPoint());
		JCheckBox checkBox = ( JCheckBox)_model.getElementAt( index);
		checkBox.setSelected( !checkBox.isSelected());

		_order_list.repaint();

		if ( checkBox.isSelected()) {
			if ( !_selected_properties.contains( checkBox.getText()))
				_selected_properties.add( checkBox.getText());
		} else {
			if ( _selected_properties.contains( checkBox.getText()))
				_selected_properties.remove( checkBox.getText());
		}

		_selected_properties_text_field.setText( get_selected_properties_text());

		_edit_order_button.setEnabled( 1 < _selected_properties.size());
	}

	/**
	 * @param actionEvent
	 */
	protected void on_edit_order(ActionEvent actionEvent) {
		if ( 2 > _selected_properties.size())
			return;

		EditPropertyOrderDlg editPropertyOrderDlg
			= new EditPropertyOrderDlg( ( Frame)getOwner(),
					ResourceManager.get_instance().get( "edit.property.order.dialog.title"),
					true, _selected_properties, this);
		editPropertyOrderDlg.do_modal( ( Component)this);
	}

	/**
	 * Updates the TextField for the order of the visible properties.
	 */
	public void update() {
		_selected_properties_text_field.setText( get_selected_properties_text());
	}

	/* (Non Javadoc)
	 * @see soars.common.utility.swing.window.Dialog#on_ok(java.awt.event.ActionEvent)
	 */
	protected void on_ok(ActionEvent actionEvent) {
		_original_selected_properties.clear();
		_original_selected_properties.addAll( _selected_properties);
		super.on_ok(actionEvent);
	}
}
