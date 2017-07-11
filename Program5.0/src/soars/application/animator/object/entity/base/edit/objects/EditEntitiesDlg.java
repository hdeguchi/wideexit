/*
 * 2005/03/15
 */
package soars.application.animator.object.entity.base.edit.objects;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.GraphicsEnvironment;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Vector;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import soars.application.animator.common.image.AnimatorImageManager;
import soars.application.animator.main.Environment;
import soars.application.animator.main.ResourceManager;
import soars.application.animator.main.internal.ObjectManager;
import soars.application.animator.observer.Observer;
import soars.common.utility.swing.tool.SwingTool;
import soars.common.utility.swing.window.Dialog;

/**
 * The dialog box edit all objects.
 * @author kurata / SOARS project
 */
public class EditEntitiesDlg extends Dialog {

	/**
	 * Degault width.
	 */
	static public final int _minimumWidth = 800;

	/**
	 * Default height.
	 */
	static public final int _minimumHeight = 400;

	/**
	 * 
	 */
	private Vector _order = null;

	/**
	 * 
	 */
	private String _openDirectoryKey = "";

	/**
	 * 
	 */
	private boolean _spot = false;

	/**
	 * 
	 */
	private ObjectManager _objectManager = null;

	/**
	 * 
	 */
	private JScrollPane _scrollPane = null;

	/**
	 * 
	 */
	private EntityTable _entityTable = null;

	/**
	 * Creates a non-modal dialog with the specified title and with the specified owner frame. If owner is null, a shared, hidden frame will be set as the owner of the dialog.
	 * @param arg0 the Frame from which the dialog is displayed
	 * @param arg1 the String to display in the dialog's title bar
	 * @param arg2 true for a modal dialog, false for one that allows other windows to be active at the same time
	 * @param spot 
	 * @param openDirectoryKey the key mapped to the default directory for the file chooser dialog
	 * @param order the array of all objects
	 * @param objectManager 
	 */
	public EditEntitiesDlg(Frame arg0, String arg1, boolean arg2, boolean spot, String openDirectoryKey, Vector order, ObjectManager objectManager) {
		super(arg0, arg1, arg2);
		_order = order;
		_spot = spot;
		_openDirectoryKey = openDirectoryKey;
		_objectManager = objectManager;
	}

	/**
	 * @return
	 */
	private Rectangle get_rectangle_from_environment_file() {
		String value = Environment.get_instance().get(
			Environment._editObjectsDialogRectangleKey + "x",
			String.valueOf( SwingTool.get_default_window_position( getOwner(), _minimumWidth, _minimumHeight).x));
		if ( null == value)
			return null;

		int x = Integer.parseInt( value);

		value = Environment.get_instance().get(
			Environment._editObjectsDialogRectangleKey + "y",
			String.valueOf( SwingTool.get_default_window_position( getOwner(), _minimumWidth, _minimumHeight).y));
		if ( null == value)
			return null;

		int y = Integer.parseInt( value);

		value = Environment.get_instance().get(
			Environment._editObjectsDialogRectangleKey + "width",
			String.valueOf( _minimumWidth));
		if ( null == value)
			return null;

		int width = Integer.parseInt( value);

		value = Environment.get_instance().get(
			Environment._editObjectsDialogRectangleKey + "height",
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
			Environment._editObjectsDialogRectangleKey + "x", String.valueOf( rectangle.x));
		Environment.get_instance().set(
			Environment._editObjectsDialogRectangleKey + "y", String.valueOf( rectangle.y));
		Environment.get_instance().set(
			Environment._editObjectsDialogRectangleKey + "width", String.valueOf( rectangle.width));
		Environment.get_instance().set(
			Environment._editObjectsDialogRectangleKey + "height", String.valueOf( rectangle.height));
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

		if ( !setup_object_table( centerPanel))
			return false;

		getContentPane().add( centerPanel);



		JPanel southPanel = new JPanel();
		southPanel.setLayout( new BoxLayout( southPanel, BoxLayout.Y_AXIS));

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
	private boolean setup_object_table(JPanel parent) {
		JPanel panel = new JPanel();
		panel.setLayout( new BoxLayout( panel, BoxLayout.X_AXIS));

		panel.add( Box.createHorizontalStrut( 5));

		_entityTable = new EntityTable( _openDirectoryKey, _spot, _objectManager, ( Frame)getOwner(), this);
		if ( !_entityTable.setup( _order))
			return false;

		_scrollPane = new JScrollPane();
		_scrollPane.getViewport().setView( _entityTable);

		panel.add( _scrollPane);
		panel.add( Box.createHorizontalStrut( 5));
		parent.add( panel);

		return true;
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
	}

	/* (Non Javadoc)
	 * @see soars.common.utility.swing.window.Dialog#on_ok(java.awt.event.ActionEvent)
	 */
	protected void on_ok(ActionEvent actionEvent) {
		_entityTable.on_ok();

		Observer.get_instance().modified();

		set_property_to_environment_file();

		AnimatorImageManager.get_instance().update();

		super.on_ok(actionEvent);
	}

	/* (Non Javadoc)
	 * @see soars.common.utility.swing.window.Dialog#on_cancel(java.awt.event.ActionEvent)
	 */
	protected void on_cancel(ActionEvent actionEvent) {
		_entityTable.on_cancel();

		set_property_to_environment_file();

		super.on_cancel(actionEvent);
	}
}
