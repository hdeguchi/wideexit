/*
 * 2005/03/10
 */
package soars.application.animator.object.property.base.edit.select;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Vector;

import javax.swing.BoxLayout;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import soars.application.animator.main.ResourceManager;
import soars.common.utility.swing.window.Dialog;

/**
 * The dialog box to edit the order of properties.
 * @author kurata / SOARS project
 */
public class EditPropertyOrderDlg extends Dialog {

	/**
	 * 
	 */
	private Vector _selected_properties = null;

	/**
	 * 
	 */
	private SelectPropertyDlg _selectPropertyDlg = null;

	/**
	 * 
	 */
	private DefaultListModel _model = null;

	/**
	 * 
	 */
	private JList _selected_properties_list = null;

	/**
	 * 
	 */
	private JLabel _dummy = null;

	/**
	 * 
	 */
	private JButton _down_button = null;

	/**
	 * 
	 */
	private JButton _up_button = null;

	/**
	 * 
	 */
	private final int _selected_properties_list_width = 400;

	/**
	 * 
	 */
	private final int _selected_properties_list_height = 80;

	/**
	 * Creates a non-modal dialog with the specified title and with the specified owner frame. If owner is null, a shared, hidden frame will be set as the owner of the dialog. 
	 * @param arg0 the Frame from which the dialog is displayed
	 * @param arg1 the String to display in the dialog's title bar
	 * @param arg2 true for a modal dialog, false for one that allows other windows to be active at the same time
	 * @param selected_properties the array of the visible properties
	 * @param selectPropertyDlg the dialog box to select the visible properties
	 */
	public EditPropertyOrderDlg(Frame arg0, String arg1, boolean arg2, Vector selected_properties, SelectPropertyDlg selectPropertyDlg) {
		super(arg0, arg1, arg2);
		_selected_properties = selected_properties;
		_selectPropertyDlg = selectPropertyDlg;
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

		JPanel parentPanel = setup_selected_properties_list();

		JPanel buttonPanel = setup_up_button();

		setup_down_button( buttonPanel);

		parentPanel.add( buttonPanel);
		getContentPane().add( parentPanel);


		insert_horizontal_glue();

		setup_ok_and_cancel_button(
			ResourceManager.get_instance().get( "dialog.ok"),
			ResourceManager.get_instance().get( "dialog.cancel"),
			false, false);

		insert_horizontal_glue();


		setDefaultCloseOperation( DISPOSE_ON_CLOSE);


		adjust();


		return true;
	}

	/**
	 * @return
	 */
	private JPanel setup_selected_properties_list() {
		JPanel panel = new JPanel();
		panel.setLayout( new FlowLayout( FlowLayout.LEFT, 5, 0));

		JLabel label = new JLabel(
			ResourceManager.get_instance().get( "edit.property.order.dialog.list"));
		panel.add( label);
		getContentPane().add( panel);



		JPanel parentPanel = new JPanel();
		parentPanel.setLayout( new FlowLayout( FlowLayout.LEFT, 5, 0));


		panel = new JPanel();
		panel.setLayout( new FlowLayout( FlowLayout.LEFT, 0, 0));

		_model = new DefaultListModel();
		for ( int i = 0; i < _selected_properties.size(); ++i) {
			String name = ( String)_selected_properties.get( i);
			_model.addElement( name);
		}

		_selected_properties_list = new JList( _model);
		_selected_properties_list.setSelectionMode( ListSelectionModel.SINGLE_SELECTION);
		_selected_properties_list.addListSelectionListener( new ListSelectionListener() {
			public void valueChanged(ListSelectionEvent arg0) {
				on_selected( arg0);
			}
		});



		JScrollPane scrollPane = new JScrollPane();
		scrollPane.getViewport().setView( _selected_properties_list);
		scrollPane.setPreferredSize( new Dimension(
			_selected_properties_list_width,
			_selected_properties_list_height));
		panel.add( scrollPane);

		parentPanel.add( panel);

		return parentPanel;
	}

	/**
	 * @param listSelectionEvent
	 */
	protected void on_selected(ListSelectionEvent listSelectionEvent) {
		int index = _selected_properties_list.getSelectedIndex();
		if ( 0 == index) {
			_up_button.setEnabled( false);
			_down_button.setEnabled( true);
		} else if ( _model.getSize() - 1 == index) {
			_up_button.setEnabled( true);
			_down_button.setEnabled( false);
		} else {
			_up_button.setEnabled( true);
			_down_button.setEnabled( true);
		}
	}

	/**
	 * @return
	 */
	private JPanel setup_up_button() {
		JPanel buttonPanel = new JPanel();
		buttonPanel.setLayout( new BoxLayout( buttonPanel, BoxLayout.Y_AXIS));

		JPanel panel = new JPanel();
		panel.setLayout( new FlowLayout( FlowLayout.LEFT, 0, 0));
		_dummy = new JLabel( "");
		panel.add( _dummy);
		buttonPanel.add( panel);

		panel = new JPanel();
		panel.setLayout( new FlowLayout( FlowLayout.LEFT, 0, 0));
		_up_button = new JButton(
			ResourceManager.get_instance().get( "edit.property.order.dialog.up.button"));
		_up_button.setEnabled( false);
		_up_button.addActionListener( new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				on_up( arg0);
			}
		});

		panel.add( _up_button);
		buttonPanel.add( panel);
		return buttonPanel;
	}

	/**
	 * @param actionEvent
	 */
	protected void on_up(ActionEvent actionEvent) {
		int index = _selected_properties_list.getSelectedIndex();
		if ( 1 > index)
			return;

		Object object = _model.remove( index);
		_model.insertElementAt( object, index - 1);
		_selected_properties_list.setSelectedIndex( index - 1);
	}

	/**
	 * @param button_panel
	 */
	private void setup_down_button(JPanel button_panel) {
		JPanel panel = new JPanel();
		panel.setLayout( new FlowLayout( FlowLayout.LEFT, 0, 0));
		_down_button = new JButton(
			ResourceManager.get_instance().get( "edit.property.order.dialog.down.button"));
		_down_button.setEnabled( false);
		_down_button.addActionListener( new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				on_down( arg0);
			}
		});

		panel.add( _down_button);
		button_panel.add( panel);
	}

	/**
	 * @param actionEvent
	 */
	protected void on_down(ActionEvent actionEvent) {
		int index = _selected_properties_list.getSelectedIndex();
		if ( _model.getSize() - 1 <= index)
			return;

		Object object = _model.remove( index);
		_model.insertElementAt( object, index + 1);
		_selected_properties_list.setSelectedIndex( index + 1);
	}

	/**
	 * 
	 */
	private void adjust() {
		_dummy.setPreferredSize( new Dimension(
			_dummy.getPreferredSize().width,
			_selected_properties_list_height
				- _up_button.getPreferredSize().height
				- _down_button.getPreferredSize().height));

		int width = _up_button.getPreferredSize().width;
		width = Math.max( width, _down_button.getPreferredSize().width);

		_up_button.setPreferredSize( new Dimension( width,
			_up_button.getPreferredSize().height));
		_down_button.setPreferredSize( new Dimension( width,
			_down_button.getPreferredSize().height));
	}

	/* (Non Javadoc)
	 * @see soars.common.utility.swing.window.Dialog#on_ok(java.awt.event.ActionEvent)
	 */
	protected void on_ok(ActionEvent actionEvent) {
		_selected_properties.clear();

		for ( int i = 0; i < _model.getSize(); ++i)
			_selected_properties.add( _model.getElementAt( i));

		_selectPropertyDlg.update();

		super.on_ok(actionEvent);
	}
}
