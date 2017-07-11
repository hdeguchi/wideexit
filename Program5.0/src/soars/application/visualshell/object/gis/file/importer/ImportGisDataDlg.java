/**
 * 
 */
package soars.application.visualshell.object.gis.file.importer;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.GraphicsEnvironment;
import java.awt.GridLayout;
import java.awt.HeadlessException;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import soars.application.visualshell.layer.LayerManager;
import soars.application.visualshell.main.Environment;
import soars.application.visualshell.main.MainFrame;
import soars.application.visualshell.main.ResourceManager;
import soars.application.visualshell.object.gis.GisDataManager;
import soars.common.utility.swing.message.IObjectsMessageCallback;
import soars.common.utility.swing.message.ObjectsMessageDlg;
import soars.common.utility.swing.tool.SwingTool;
import soars.common.utility.swing.window.Dialog;

/**
 * @author kurata
 *
 */
public class ImportGisDataDlg extends Dialog implements IObjectsMessageCallback {

	/**
	 * 
	 */
	static public final int _minimumWidth = 780;

	/**
	 * 
	 */
	static public final int _minimumHeight = 320;

	/**
	 * 
	 */
	public GisDataManager _gisDataManager = null;

	/**
	 * 
	 */
	private DirectoryList _directoryList = null; 

	/**
	 * @param arg0
	 * @param arg1
	 * @param arg2
	 * @throws HeadlessException
	 */
	public ImportGisDataDlg(Frame arg0, String arg1, boolean arg2) throws HeadlessException {
		super(arg0, arg1, arg2);
	}

	/**
	 * @return
	 */
	private Rectangle get_rectangle_from_environment_file() {
		String value = Environment.get_instance().get(
			Environment._importGisDataDialogRectangleKey + "x",
			String.valueOf( SwingTool.get_default_window_position( getOwner(), _minimumWidth, _minimumHeight).x));
		if ( null == value)
			return null;

		int x = Integer.parseInt( value);

		value = Environment.get_instance().get(
			Environment._importGisDataDialogRectangleKey + "y",
			String.valueOf( SwingTool.get_default_window_position( getOwner(), _minimumWidth, _minimumHeight).y));
		if ( null == value)
			return null;

		int y = Integer.parseInt( value);

		value = Environment.get_instance().get(
			Environment._importGisDataDialogRectangleKey + "width",
			String.valueOf( _minimumWidth));
		if ( null == value)
			return null;

		int width = Integer.parseInt( value);

		value = Environment.get_instance().get(
			Environment._importGisDataDialogRectangleKey + "height",
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
			Environment._importGisDataDialogRectangleKey + "x", String.valueOf( rectangle.x));
		Environment.get_instance().set(
			Environment._importGisDataDialogRectangleKey + "y", String.valueOf( rectangle.y));
		Environment.get_instance().set(
			Environment._importGisDataDialogRectangleKey + "width", String.valueOf( rectangle.width));
		Environment.get_instance().set(
			Environment._importGisDataDialogRectangleKey + "height", String.valueOf( rectangle.height));
	}

	/**
	 * @return
	 */
	public boolean do_modal() {
		Rectangle rectangle = get_rectangle_from_environment_file();
		if ( null == rectangle)
			return do_modal( getOwner(), _minimumWidth, _minimumHeight);
		else
			return do_modal( rectangle);
	}

	/* (non-Javadoc)
	 * @see soars.common.utility.swing.window.Dialog#on_init_dialog()
	 */
	protected boolean on_init_dialog() {
		if (!super.on_init_dialog())
			return false;

		//link_to_cancel( getRootPane());

		getContentPane().setLayout( new BorderLayout());

		setup_north_panel();

		if ( !setup_center_panel())
			return false;

		setup_south_panel();

		//adjust();

		setDefaultCloseOperation( DISPOSE_ON_CLOSE);

		return true;
	}

	/**
	 * 
	 */
	private void setup_north_panel() {
		JPanel northPanel = new JPanel();
		northPanel.setLayout( new BoxLayout( northPanel, BoxLayout.Y_AXIS));
		insert_horizontal_glue( northPanel);
		getContentPane().add( northPanel, "North");
	}

	/**
	 * @return
	 */
	private boolean setup_center_panel() {
		JPanel centerPanel = new JPanel();
		centerPanel.setLayout( new BoxLayout( centerPanel, BoxLayout.Y_AXIS));

		JPanel panel = new JPanel();
		panel.setLayout( new BoxLayout( panel, BoxLayout.X_AXIS));

		panel.add( Box.createHorizontalStrut( 5));

		_directoryList = new DirectoryList( ( Frame)getOwner(), this);
		if ( !_directoryList.setup( true, false))
			return false;

		//link_to_cancel( _directoryList);

		JScrollPane scrollPane = new JScrollPane();
		scrollPane.getViewport().setView( _directoryList);

		//link_to_cancel( scrollPane);

		panel.add( scrollPane);
		panel.add( Box.createHorizontalStrut( 5));
		centerPanel.add( panel);

		getContentPane().add( centerPanel);

		return true;
	}

	/**
	 * 
	 */
	private void setup_south_panel() {
		JPanel southPanel = new JPanel();
		southPanel.setLayout( new BoxLayout( southPanel, BoxLayout.Y_AXIS));

		setup_appen_directory_button( southPanel);

		insert_horizontal_glue( southPanel);

		setup_ok_and_cancel_button( southPanel);

		insert_horizontal_glue( southPanel);

		getContentPane().add( southPanel, "South");
	}

	/**
	 * @param parent
	 */
	private void setup_appen_directory_button(JPanel parent) {
		JPanel panel = new JPanel();
		panel.setLayout( new BoxLayout( panel, BoxLayout.X_AXIS));

		panel.add( Box.createHorizontalStrut( 5));

		JPanel buttonPanel = new JPanel();
		buttonPanel.setLayout( new GridLayout( 1, 1));

		JButton button = new JButton( ResourceManager.get_instance().get( "import.gis.data.dialog.append.new.directory.button.name"));
		button.addActionListener( new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				_directoryList.on_append( null);
			}
		});

		//link_to_cancel( button);

		buttonPanel.add( button);
		panel.add( buttonPanel);
		panel.add( Box.createHorizontalStrut( 5));

		parent.add( panel);
	}

	/**
	 * @param parent
	 */
	private void setup_ok_and_cancel_button(JPanel parent) {
		JPanel panel = new JPanel();
		panel.setLayout( new FlowLayout( FlowLayout.RIGHT, 5, 0));

		setup_ok_and_cancel_button(
			panel,
			ResourceManager.get_instance().get( "import.gis.data.dialog.load"),
			ResourceManager.get_instance().get( "dialog.cancel"),
			true, true);
		parent.add( panel);
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

	/* (non-Javadoc)
	 * @see soars.common.utility.swing.window.Dialog#on_ok(java.awt.event.ActionEvent)
	 */
	protected void on_ok(ActionEvent actionEvent) {
		File[] directories = _directoryList.get();
		if ( null == directories || 0 == directories.length)
			return;

		SelectCharacterCodeDlg selectCharacterCodeDlg = new SelectCharacterCodeDlg( MainFrame.get_instance(), ResourceManager.get_instance().get( "import.gis.data.dialog.title"), true);
		if ( !selectCharacterCodeDlg.do_modal( this))
			return;

		GisDataManager[] gisDataManagers = ( GisDataManager[])ObjectsMessageDlg.execute( MainFrame.get_instance(), ResourceManager.get_instance().get( "import.gis.data.dialog.title"), true,
			"on_file_import_gis_data", ResourceManager.get_instance().get( "file.import.gis.data.show.message"),
			new Object[] { directories}, this, this);
		if ( null == gisDataManagers || null == gisDataManagers[ 0]) {
			JOptionPane.showMessageDialog( this,
				ResourceManager.get_instance().get( "import.gis.data.dialog.load.error.message"),
				ResourceManager.get_instance().get( "import.gis.data.dialog.title"),
				JOptionPane.ERROR_MESSAGE);
			return;
		}

		_gisDataManager = gisDataManagers[ 0];

		set_property_to_environment_file();

		super.on_ok(actionEvent);
	}

	/* (non-Javadoc)
	 * @see soars.common.utility.swing.message.IObjectsMessageCallback#objects_message_callback(java.lang.String, java.lang.Object[], soars.common.utility.swing.message.ObjectsMessageDlg)
	 */
	public Object[] objects_message_callback(String id, Object[] objects, ObjectsMessageDlg objectsMessageDlg) {
		if ( id.equals( "on_file_import_gis_data")) {
			GisDataManager gisDataManager = LayerManager.get_instance().import_gis_data( ( File[])objects[ 0]);
			return ( new GisDataManager[] { gisDataManager});
		}
		return null;
	}

	/* (non-Javadoc)
	 * @see soars.common.utility.swing.window.Dialog#on_cancel(java.awt.event.ActionEvent)
	 */
	protected void on_cancel(ActionEvent actionEvent) {
		set_property_to_environment_file();
		super.on_cancel(actionEvent);
	}
}
