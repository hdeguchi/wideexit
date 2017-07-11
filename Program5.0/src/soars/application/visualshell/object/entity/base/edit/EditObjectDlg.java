/*
 * 2005/05/06
 */
package soars.application.visualshell.object.entity.base.edit;

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
import java.util.ArrayList;
import java.util.List;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;

import soars.application.visualshell.layer.LayerManager;
import soars.application.visualshell.main.Constant;
import soars.application.visualshell.main.Environment;
import soars.application.visualshell.main.ResourceManager;
import soars.application.visualshell.object.entity.base.EntityBase;
import soars.application.visualshell.object.entity.base.edit.panel.ObjectPanel;
import soars.application.visualshell.object.entity.base.edit.panel.base.VariableTableBase;
import soars.application.visualshell.object.entity.base.edit.panel.variable.panel.base.panel.table.InitialValueTable;
import soars.application.visualshell.object.entity.base.edit.tree.ObjectTree;
import soars.application.visualshell.observer.Observer;
import soars.common.utility.swing.tool.SwingTool;
import soars.common.utility.swing.window.Dialog;

/**
 * @author kurata
 */
public class EditObjectDlg extends Dialog {

	/**
	 * 
	 */
	static public final int _minimumWidth = 640;

	/**
	 * 
	 */
	static public final int _minimumHeight = 480;

	/**
	 * 
	 */
	private EntityBase _entityBase = null;

	/**
	 * 
	 */
	private String _title = "";

	/**
	 * 
	 */
	private JSplitPane _splitPane = null;

	/**
	 * 
	 */
	private ObjectTree _objectTree = null;

	/**
	 * 
	 */
	private ObjectPanel _objectPanel = null;

	/**
	 * @param arg0
	 * @param arg1
	 * @param arg2
	 * @param entityBase
	 */
	public EditObjectDlg(Frame arg0, String arg1, boolean arg2, EntityBase entityBase) {
		super(arg0, arg1, arg2);
		_title = arg1;
		_entityBase = entityBase;
	}

	/**
	 * @return
	 */
	private Rectangle get_rectangle_from_environment_file() {
		String value = Environment.get_instance().get(
			Environment._editObjectDialogRectangleKey + "x",
			String.valueOf( SwingTool.get_default_window_position( getOwner(), _minimumWidth, _minimumHeight).x));
		if ( null == value)
			return null;

		int x = Integer.parseInt( value);

		value = Environment.get_instance().get(
			Environment._editObjectDialogRectangleKey + "y",
			String.valueOf( SwingTool.get_default_window_position( getOwner(), _minimumWidth, _minimumHeight).y));
		if ( null == value)
			return null;

		int y = Integer.parseInt( value);

		value = Environment.get_instance().get(
			Environment._editObjectDialogRectangleKey + "width",
			String.valueOf( _minimumWidth));
		if ( null == value)
			return null;

		int width = Integer.parseInt( value);

		value = Environment.get_instance().get(
			Environment._editObjectDialogRectangleKey + "height",
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
			if ( _splitPane.getDividerLocation() >= ( _minimumWidth - getInsets().left - getInsets().right))
				_splitPane.setDividerLocation( 100);

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
			Environment._editObjectDialogRectangleKey + "x", String.valueOf( rectangle.x));
		Environment.get_instance().set(
			Environment._editObjectDialogRectangleKey + "y", String.valueOf( rectangle.y));
		Environment.get_instance().set(
			Environment._editObjectDialogRectangleKey + "width", String.valueOf( rectangle.width));
		Environment.get_instance().set(
			Environment._editObjectDialogRectangleKey + "height", String.valueOf( rectangle.height));

		Environment.get_instance().set(
			Environment._editObjectDialogDividerLocationKey, String.valueOf( _splitPane.getDividerLocation()));
	}

	/**
	 * 
	 */
	public void do_modal() {
		Rectangle rectangle = get_rectangle_from_environment_file();
		if ( null == rectangle)
			do_modal( getOwner(), _minimumWidth, _minimumHeight);
		else
			do_modal( rectangle);
	}

	/* (Non Javadoc)
	 * @see soars.common.utility.swing.window.Dialog#on_init_dialog()
	 */
	protected boolean on_init_dialog() {
		if ( !super.on_init_dialog())
			return false;



		getContentPane().setLayout( new BorderLayout());



		JPanel centerPanel = new JPanel();
		centerPanel.setLayout( new BoxLayout( centerPanel, BoxLayout.Y_AXIS));

		insert_horizontal_glue( centerPanel);

		if ( !setup( centerPanel))
			return false;

		getContentPane().add( centerPanel);

		_splitPane.setDividerLocation( Integer.parseInt( Environment.get_instance().get( Environment._editObjectDialogDividerLocationKey, "100")));



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

		getContentPane().add( southPanel, "South");



		setDefaultCloseOperation( DISPOSE_ON_CLOSE);

//		// Test
//		Dialog dialog = new Dialog( this);
//		dialog.create();
//		dialog.setVisible( true);

		return true;
	}

	/**
	 * @param parent
	 * @return
	 */
	private boolean setup(JPanel parent) {
		JPanel basePanel = new JPanel();
		basePanel.setLayout( new BoxLayout( basePanel, BoxLayout.X_AXIS));

		basePanel.add( Box.createHorizontalStrut( 5));



		_splitPane = new JSplitPane();



		_objectTree = new ObjectTree( ( Frame)getOwner(), this);
		_objectPanel = new ObjectPanel( _entityBase);



		JPanel panel = new JPanel();
		panel.setLayout( new BoxLayout( panel, BoxLayout.X_AXIS));

//		panel.add( Box.createHorizontalStrut( 5));

		if ( !_objectTree.setup( _objectPanel, _entityBase._name/*_title*/))
			return false;

		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setHorizontalScrollBarPolicy( JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		scrollPane.setVerticalScrollBarPolicy( JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
		scrollPane.getViewport().setView( _objectTree);
		panel.add( scrollPane);

//		panel.add( Box.createHorizontalStrut( 5));

		_splitPane.setLeftComponent( panel);



		panel = new JPanel();
		panel.setLayout( new BoxLayout( panel, BoxLayout.X_AXIS));

//		panel.add( Box.createHorizontalStrut( 5));

		if ( !_objectPanel.setup( _objectTree, ( Frame)getOwner(), this))
			return false;


		panel.add( _objectPanel);

		panel.add( Box.createHorizontalStrut( 5));

		_splitPane.setRightComponent( panel);



		basePanel.add( _splitPane);



		parent.add( basePanel);



		return true;
	}

	/* (Non Javadoc)
	 * @see soars.common.utility.swing.window.Dialog#on_setup_completed()
	 */
	protected void on_setup_completed() {
		optimize_window_rectangle();

		_objectPanel.on_setup_completed();

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
				_objectPanel.windowClosing();
			}
		});
	}

	/* (Non Javadoc)
	 * @see soars.common.utility.swing.window.Dialog#on_ok(java.awt.event.ActionEvent)
	 */
	protected void on_ok(ActionEvent actionEvent) {
		String name = _entityBase._name;
		String number = _entityBase._number;

		if ( !_objectPanel.on_ok( _entityBase))
			return;

		update_gis_spots();

		for ( String kind:Constant._kinds)
			Observer.get_instance().on_update_object( kind);

		Observer.get_instance().on_update_entityBase( !_entityBase._name.equals( name) || !_entityBase._number.equals( number));

		Observer.get_instance().modified();

		set_property_to_environment_file();

		super.on_ok(actionEvent);
	}

	/**
	 * 
	 */
	private void update_gis_spots() {
		if ( _entityBase._gis.equals( ""))
			return;

		List<EntityBase> entityBases = new ArrayList<EntityBase>();
		LayerManager.get_instance().get_gis_spots( entityBases, _entityBase._gis);
		for ( EntityBase entityBase:entityBases)
			entityBase.update_on_gis( _entityBase);
	}

	/* (Non Javadoc)
	 * @see soars.common.utility.swing.window.Dialog#on_cancel(java.awt.event.ActionEvent)
	 */
	@Override
	protected void on_cancel(ActionEvent actionEvent) {
		_objectPanel.on_cancel();
		set_property_to_environment_file();
		super.on_cancel(actionEvent);
	}

	/**
	 * Clear buffers for copy and paste.
	 */
	public static void clear() {
		VariableTableBase.clear();
		InitialValueTable.clear();
	}
}
