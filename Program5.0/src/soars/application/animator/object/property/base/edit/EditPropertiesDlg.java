/*
 * 2005/03/03
 */
package soars.application.animator.object.property.base.edit;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.Graphics2D;
import java.awt.GraphicsEnvironment;
import java.awt.GridLayout;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Vector;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import soars.application.animator.common.image.AnimatorImageManager;
import soars.application.animator.main.Environment;
import soars.application.animator.main.ResourceManager;
import soars.application.animator.object.property.base.PropertyManager;
import soars.application.animator.object.property.base.edit.select.SelectPropertyDlg;
import soars.application.animator.observer.Observer;
import soars.common.utility.swing.tool.SwingTool;
import soars.common.utility.swing.window.Dialog;

/**
 * The dialog box to edit the all properties.
 * @author kurata / SOARS project
 */
public class EditPropertiesDlg extends Dialog {

	/**
	 * Default width.
	 */
	static public final int _minimumWidth = 800;

	/**
	 * DEfaylt height.
	 */
	static public final int _minimumHeight = 400;

	/**
	 * 
	 */
	private PropertyManager _propertyManager = null;

	/**
	 * 
	 */
	private String _openDirectoryKey = "";

	/**
	 * 
	 */
	protected Vector _selectedProperties = new Vector();

	/**
	 * 
	 */
	private JTabbedPane _propertyPages = null;

	/**
	 * 
	 */
	private JTextField _textField = null;

	/**
	 * Creates a non-modal dialog with the specified title and with the specified owner frame. If owner is null, a shared, hidden frame will be set as the owner of the dialog. 
	 * @param arg0 the Frame from which the dialog is displayed
	 * @param arg1 the String to display in the dialog's title bar
	 * @param arg2 true for a modal dialog, false for one that allows other windows to be active at the same time
	 * @param openDirectoryKey the key mapped to the default directory for JFileChooser
	 * @param propertyManager the Property hashtable(name(String) - value(String) - PropertyBase)
	 */
	public EditPropertiesDlg(Frame arg0, String arg1, boolean arg2, String openDirectoryKey, PropertyManager propertyManager) {
		super(arg0, arg1, arg2);
		_openDirectoryKey = openDirectoryKey;
		_propertyManager = propertyManager;
		_selectedProperties.addAll( propertyManager.get_selected_properties());
	}

	/**
	 * @return
	 */
	private Rectangle get_rectangle_from_environment_file() {
		String value = Environment.get_instance().get(
			Environment._editPropertiesDialogRectangleKey + "x",
			String.valueOf( SwingTool.get_default_window_position( getOwner(), _minimumWidth, _minimumHeight).x));
		if ( null == value)
			return null;

		int x = Integer.parseInt( value);

		value = Environment.get_instance().get(
			Environment._editPropertiesDialogRectangleKey + "y",
			String.valueOf( SwingTool.get_default_window_position( getOwner(), _minimumWidth, _minimumHeight).y));
		if ( null == value)
			return null;

		int y = Integer.parseInt( value);

		value = Environment.get_instance().get(
			Environment._editPropertiesDialogRectangleKey + "width",
			String.valueOf( _minimumWidth));
		if ( null == value)
			return null;

		int width = Integer.parseInt( value);

		value = Environment.get_instance().get(
			Environment._editPropertiesDialogRectangleKey + "height",
			String.valueOf( _minimumHeight));
		if ( null == value)
			return null;

		int height = Integer.parseInt( value);

		return new Rectangle( x, y, width, height);
	}

	/**
	 * 
	 */
	private void optimize_window_rectangle() {
		Rectangle rectangle = getBounds();
		if ( !GraphicsEnvironment.getLocalGraphicsEnvironment().getMaximumWindowBounds().intersects( rectangle)
			|| GraphicsEnvironment.getLocalGraphicsEnvironment().getMaximumWindowBounds().intersection( rectangle).width <= 10
			|| rectangle.y <= -getInsets().top
			|| GraphicsEnvironment.getLocalGraphicsEnvironment().getMaximumWindowBounds().intersection( rectangle).height <= getInsets().top) {
			setSize( _minimumWidth, _minimumHeight);
			setLocationRelativeTo( getOwner());
		}
	}

	/**
	 * 
	 */
	protected void set_property_to_environment_file() {
		Rectangle rectangle = getBounds();

		Environment.get_instance().set(
			Environment._editPropertiesDialogRectangleKey + "x", String.valueOf( rectangle.x));
		Environment.get_instance().set(
			Environment._editPropertiesDialogRectangleKey + "y", String.valueOf( rectangle.y));
		Environment.get_instance().set(
			Environment._editPropertiesDialogRectangleKey + "width", String.valueOf( rectangle.width));
		Environment.get_instance().set(
			Environment._editPropertiesDialogRectangleKey + "height", String.valueOf( rectangle.height));
	}

	/**
	 * Returns true if this component is created successfully.
	 * @return true if this component is created successfully
	 */
	public boolean do_modal() {
		Rectangle rectangle = get_rectangle_from_environment_file();
		if ( null == rectangle)
			return do_modal( getOwner(), _minimumWidth, _minimumHeight);
		else
			return do_modal( rectangle);
	}

	/* (Non Javadoc)
	 * @see soars.common.utility.swing.window.Dialog#on_init_dialog()
	 */
	protected boolean on_init_dialog() {
		if ( !super.on_init_dialog())
			return false;



		getContentPane().setLayout( new BorderLayout());



		JPanel northPanel = new JPanel();
		northPanel.setLayout( new BoxLayout( northPanel, BoxLayout.Y_AXIS));

		insert_horizontal_glue( northPanel);

		getContentPane().add( northPanel, "North");



		JPanel centerPanel = new JPanel();
		centerPanel.setLayout( new BoxLayout( centerPanel, BoxLayout.Y_AXIS));

		if ( !setup_tabbed_pane( centerPanel))
			return false;

		getContentPane().add( centerPanel);



		JPanel southPanel = new JPanel();
		southPanel.setLayout( new BoxLayout( southPanel, BoxLayout.Y_AXIS));

		insert_horizontal_glue( southPanel);

		setup_selected_properties_order_text_field( southPanel);

		insert_horizontal_glue( southPanel);

		setup_select_properties_button( southPanel);

		insert_horizontal_glue( southPanel);

		JPanel panel = new JPanel();
		panel.setLayout( new FlowLayout( FlowLayout.RIGHT, 5, 0));

		setup_ok_and_cancel_button(
			panel,
			ResourceManager.get_instance().get( "dialog.ok"),
			ResourceManager.get_instance().get( "dialog.cancel"),
			false, false);
		southPanel.add( panel);

		insert_horizontal_glue( southPanel);

		getContentPane().add( southPanel, "South");



		setDefaultCloseOperation( DISPOSE_ON_CLOSE);



		return true;
	}

	/**
	 * @param parent
	 * @return
	 */
	private boolean setup_tabbed_pane(JPanel parent) {
		JPanel panel = new JPanel();
		panel.setLayout( new BoxLayout( panel, BoxLayout.X_AXIS));

		panel.add( Box.createHorizontalStrut( 5));

		_propertyPages = new JTabbedPane( SwingConstants.TOP, JTabbedPane.SCROLL_TAB_LAYOUT);

		for( int i = 0 ; i < _propertyManager.get_order().size(); ++i) {
			String name = ( String)_propertyManager.get_order().get( i);
			PropertyPage propertyPage = new PropertyPage( name, _propertyManager, _openDirectoryKey, ( Frame)getOwner(), this);
			if ( !propertyPage.create())
				return false;

			_propertyPages.add( propertyPage, name);
		}

		panel.add( _propertyPages);
		panel.add( Box.createHorizontalStrut( 5));
		parent.add( panel);

		return true;
	}

	/**
	 * @param parent
	 * 
	 */
	private void setup_selected_properties_order_text_field(JPanel parent) {
		JPanel panel = new JPanel();
		panel.setLayout( new BoxLayout( panel, BoxLayout.X_AXIS));

		panel.add( Box.createHorizontalStrut( 5));

		JLabel label = new JLabel(
			ResourceManager.get_instance().get( "edit.properties.dialog.order"));
		panel.add( label);

		panel.add( Box.createHorizontalStrut( 5));

		_textField = new JTextField( get_selected_properties_text());
		_textField.setEditable( false);
		panel.add( _textField);

		panel.add( Box.createHorizontalStrut( 5));

		parent.add( panel);
	}

	/**
	 * @param parent
	 * 
	 */
	private void setup_select_properties_button(JPanel parent) {
		JPanel panel = new JPanel();
		panel.setLayout( new BoxLayout( panel, BoxLayout.X_AXIS));

		panel.add( Box.createHorizontalStrut( 5));

		JPanel buttonPanel = new JPanel();
		buttonPanel.setLayout( new GridLayout( 1, 1));

		JButton button = new JButton(
			ResourceManager.get_instance().get( "edit.properties.dialog.select.button"));
		button.addActionListener( new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				on_select_properties( arg0);
			}
		});

		buttonPanel.add( button);
		panel.add( buttonPanel);
		panel.add( Box.createHorizontalStrut( 5));
		parent.add( panel);
	}

	/**
	 * @param actionEvent
	 */
	protected void on_select_properties(ActionEvent actionEvent) {
		SelectPropertyDlg selectPropertyDlg
			= new SelectPropertyDlg( ( Frame)getOwner(),
				ResourceManager.get_instance().get( "select.property.dialog.title"),
				true,
				_propertyManager.get_order(),
				_selectedProperties);
		if ( !selectPropertyDlg.do_modal( this))
			return;

		_textField.setText( get_selected_properties_text());
	}

	/**
	 * @return
	 */
	private String get_selected_properties_text() {
		String text = "";
		for ( int i = 0; i < _selectedProperties.size(); ++i) {
			text += _selectedProperties.get( i);
			if ( _selectedProperties.size() - 1 > i)
				text += ", ";
		}
		return text;
	}

	/* (non-Javadoc)
	 * @see soars.common.utility.swing.window.Dialog#on_setup_completed()
	 */
	protected void on_setup_completed() {
		optimize_window_rectangle();

		addComponentListener( new ComponentAdapter() {
			public void componentResized(ComponentEvent e){
				int width = getSize().width;
				int height = getSize().height;
				setSize( ( _minimumWidth > width) ? _minimumWidth : width,
					( _minimumHeight > height) ? _minimumHeight : height);
			}
		});

		addWindowListener( new WindowAdapter() {
			public void windowClosing(WindowEvent arg0) {
				set_property_to_environment_file();
			}
		});

		for ( int i = 0; i < _propertyPages.getTabCount(); ++i) {
			PropertyPage propertyPage = ( PropertyPage)_propertyPages.getComponentAt( i);
			propertyPage.on_setup_completed();
		}
	}

	/* (Non Javadoc)
	 * @see soars.common.utility.swing.window.Dialog#on_ok(java.awt.event.ActionEvent)
	 */
	protected void on_ok(ActionEvent actionEvent) {
		for ( int i = 0; i < _propertyPages.getTabCount(); ++i) {
			PropertyPage propertyPage = ( PropertyPage)_propertyPages.getComponentAt( i);
			propertyPage.on_ok( ( Graphics2D)getGraphics());
		}

		_propertyManager.update( _selectedProperties);

		Observer.get_instance().modified();

		set_property_to_environment_file();

		AnimatorImageManager.get_instance().update();

		super.on_ok(actionEvent);
	}

	/* (non-Javadoc)
	 * @see soars.common.utility.swing.window.Dialog#on_cancel(java.awt.event.ActionEvent)
	 */
	protected void on_cancel(ActionEvent actionEvent) {
		set_property_to_environment_file();
		super.on_cancel(actionEvent);
	}
}
