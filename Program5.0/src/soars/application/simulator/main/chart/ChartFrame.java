/*
 * 2004/12/17
 */
package soars.application.simulator.main.chart;

import java.awt.Image;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.image.BufferedImage;
import java.beans.PropertyVetoException;
import java.io.EOFException;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.Arrays;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.event.InternalFrameEvent;

import org.xml.sax.SAXException;

import soars.application.simulator.common.tool.CommonTool;
import soars.application.simulator.data.ChartData;
import soars.application.simulator.data.Dataset;
import soars.application.simulator.main.Application;
import soars.application.simulator.main.Constant;
import soars.application.simulator.main.MainFrame;
import soars.application.simulator.main.ResourceManager;
import soars.application.simulator.menu.file.IFileMenuHandler;
import soars.application.simulator.menu.file.SaveAsAction;
import soars.application.simulator.menu.file.SaveImageAsAction;
import soars.common.utility.swing.gui.UserInterface;
import soars.common.utility.swing.message.IMessageCallback;
import soars.common.utility.swing.message.MessageDlg;
import soars.common.utility.swing.tool.SwingTool;
import soars.common.utility.swing.window.MDIChildFrame;
import soars.common.utility.tool.resource.Resource;
import soars.common.utility.xml.sax.Writer;
import soars.plugin.modelbuilder.chart.chart.main.LogViewer;

/**
 * @author kurata
 */
public class ChartFrame extends MDIChildFrame implements IFileMenuHandler, IMessageCallback {

	/**
	 * Default width.
	 */
	static public final int _defaultWidth = 510;

	/**
	 * Default height.
	 */
	static public final int _defaultHeight = 360;

	/**
	 * 
	 */
	public String _name = "";

	/**
	 * 
	 */
	private LogViewer _logViewer = null;

	/**
	 * 
	 */
	private UserInterface _userInterface = null;

	/**
	 * 
	 */
	private JMenuItem _fileSaveAsMenuItem = null;

	/**
	 * 
	 */
	private JMenuItem _fileSaveImageAsMenuItem = null;

	/**
	 * @param name
	 * @param arg0 
	 * @param arg1
	 * @param arg2
	 * @param arg3
	 * @param arg4
	 */
	public ChartFrame(
		String name,
		String arg0,
		boolean arg1,
		boolean arg2,
		boolean arg3,
		boolean arg4) {
		super(arg0, arg1, arg2, arg3, arg4);
		_name = name;
	}

	/**
	 * 
	 */
	private void setup_menu() {
		if ( Application._demo)
			return;

		JMenuBar menuBar = new JMenuBar();

		JMenu menu = _userInterface.append_menu(
			menuBar,
			ResourceManager.get_instance().get( "file.menu"),
			true,
			ResourceManager.get_instance().get( "file.mnemonic"));

		_fileSaveAsMenuItem = _userInterface.append_menuitem(
			menu,
			ResourceManager.get_instance().get( "file.save.as.menu"),
			new SaveAsAction( ResourceManager.get_instance().get( "file.save.as.menu"), this),
			ResourceManager.get_instance().get( "file.save.as.mnemonic"),
			ResourceManager.get_instance().get( "file.save.as.stroke"));

		_fileSaveImageAsMenuItem = _userInterface.append_menuitem(
			menu,
			ResourceManager.get_instance().get( "file.save.image.as.menu"),
			new SaveImageAsAction( ResourceManager.get_instance().get( "file.save.image.as.menu"), this),
			ResourceManager.get_instance().get( "file.save.image.as.mnemonic"),
			ResourceManager.get_instance().get( "file.save.image.as.stroke"));

		setJMenuBar( menuBar);
	}

	/**
	 * @param chartData
	 * @param directory
	 * @return
	 */
	public boolean create(ChartData chartData, File directory) {
		if ( !super.create())
			return false;

		_userInterface = new UserInterface();

		_logViewer = new LogViewer();
		if ( !_logViewer.setup())
			return false;

		_logViewer.setTitle( chartData._title);
		_logViewer.setXLabel( chartData._XLabel);
		_logViewer.setYLabel( chartData._YLabel);

		if ( chartData._XRange)
			_logViewer.setXRange( chartData._XRangeMin, chartData._XRangeMax);

		if ( chartData._YRange)
			_logViewer.setYRange( chartData._YRangeMin, chartData._YRangeMax);

		for ( int i = 0;i < chartData._datasets.size(); ++i) {
			if ( !setup( chartData._name, ( Dataset)chartData._datasets.get( i), directory))
				return false;
		}

		getContentPane().add( "Center", _logViewer);
		//_logViewer.setButtons( true);
		_logViewer.setButtons( false);


		setup_menu();


		Image image = Resource.load_image_from_resource(
			Constant._resourceDirectory + "/image/icon/icon.png", getClass());
		if ( null != image)
			setFrameIcon( new ImageIcon( image));


		pack();


		setLocation( chartData._windowRectangle.x, chartData._windowRectangle.y);
		setSize( chartData._windowRectangle.width, chartData._windowRectangle.height);


		setVisible( true);


		try {
			setSelected( true);
		} catch (PropertyVetoException e) {
			e.printStackTrace();
		}


		return true;
	}

	/**
	 * @param name
	 * @param dataset
	 * @param directory
	 * @return
	 */
	private boolean setup(String name, Dataset dataset, File directory) {
		_logViewer.addLegend( dataset._id, dataset._legend);

		File file = new File( directory, name + "_" + String.valueOf( dataset._id) + ".log");
		if ( !file.exists() || !file.canRead())
			return false;

		try {
			RandomAccessFile randomAccessFile = new RandomAccessFile( file, "r");
			while ( true) {
				double x, y;
				try {
					x = randomAccessFile.readDouble();
				} catch (EOFException e) {
					//e.printStackTrace();
					break;
				}
				try {
					y = randomAccessFile.readDouble();
				} catch (EOFException e) {
					e.printStackTrace();
					randomAccessFile.close();
					return false;
				}

				if ( !dataset._connect && !_logViewer.getMarksStyle().equals( "dots"))
					_logViewer.setMarksStyle( "dots", dataset._id);

				_logViewer.addPoint( dataset._id, x, y, dataset._connect);
			}
			randomAccessFile.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return false;
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}

		return true;
	}

	/**
	 * @param chartData
	 * @return
	 */
	public boolean create(ChartData chartData) {
		if ( !super.create())
			return false;

		_userInterface = new UserInterface();

		_logViewer = new LogViewer();
		if ( !_logViewer.setup())
			return false;

		_logViewer.setTitle( chartData._title);
		_logViewer.setXLabel( chartData._XLabel);
		_logViewer.setYLabel( chartData._YLabel);

		if ( chartData._XRange)
			_logViewer.setXRange( chartData._XRangeMin, chartData._XRangeMax);

		if ( chartData._YRange)
			_logViewer.setYRange( chartData._YRangeMin, chartData._YRangeMax);

		for ( int i = 0;i < chartData._datasets.size(); ++i) {
			if ( !setup( ( Dataset)chartData._datasets.get( i)))
				return false;
		}

		getContentPane().add( "Center", _logViewer);
		//_logViewer.setButtons( true);
		_logViewer.setButtons( false);


		setup_menu();


		Image image = Resource.load_image_from_resource(
			Constant._resourceDirectory + "/image/icon/icon.png", getClass());
		if ( null != image)
			setFrameIcon( new ImageIcon( image));


		pack();


		setLocation( chartData._windowRectangle.x, chartData._windowRectangle.y);
		setSize( chartData._windowRectangle.width, chartData._windowRectangle.height);


		setVisible( true);


		try {
			setSelected( true);
		} catch (PropertyVetoException e) {
			e.printStackTrace();
		}


		return true;
	}

	/**
	 * @param dataset
	 * @return
	 */
	private boolean setup(Dataset dataset) {
		_logViewer.addLegend( dataset._id, dataset._legend);

		if ( !dataset._value.equals( "")) {
			String[] values = dataset._value.split( " ");
			if ( null == values || ( 0 != values.length % 3))
				return false;

			if ( 3 == values.length || 3 < values.length && values[ 3].equals( "m"))
				_logViewer.setMarksStyle( "dots", dataset._id);

			for ( int i = 0; i < values.length; i += 3) {
				if ( null == values[ i] || null == values[ i + 1] || null == values[ i + 2]
					|| ( !values[ i].equals( "m") && !values[ i].equals( "p")))
					return false;

				try {
					double x = Double.parseDouble( values[ i + 1]);
					double y = new Double( values[ i + 2]).doubleValue();
					_logViewer.addPoint( dataset._id, x, y, values[ i].equals( "p"));
				} catch (NumberFormatException e) {
					e.printStackTrace();
					return false;
				}
			}
		}
		return true;
	}

	/**
	 * 
	 */
	public void set_title() {
		setTitle( _logViewer.getTitle());
	}

	/* (Non Javadoc)
	 * @see soars.common.utility.swing.window.MDIChildFrame#on_internal_frame_closing(javax.swing.event.InternalFrameEvent)
	 */
	protected void on_internal_frame_closing(InternalFrameEvent internalFrameEvent) {
		//MainFrame.get_instance().update_user_interface();
		super.on_internal_frame_closing(internalFrameEvent);
	}

	/* (Non Javadoc)
	 * @see soars.common.utility.swing.window.MDIChildFrame#on_internal_frame_activated(javax.swing.event.InternalFrameEvent)
	 */
	protected void on_internal_frame_activated(InternalFrameEvent internalFrameEvent) {
		//MainFrame.get_instance().update_user_interface();
		super.on_internal_frame_activated(internalFrameEvent);
	}

	/* (Non Javadoc)
	 * @see soars.common.utility.swing.window.MDIChildFrame#on_internal_frame_deactivated(javax.swing.event.InternalFrameEvent)
	 */
	protected void on_internal_frame_deactivated(InternalFrameEvent internalFrameEvent) {
		//MainFrame.get_instance().update_user_interface();
		super.on_internal_frame_deactivated(internalFrameEvent);
	}

	/* (non-Javadoc)
	 * @see soars.application.simulator.menu.file.IFileMenuHandler#on_file_exit(java.awt.event.ActionEvent)
	 */
	public void on_file_exit(ActionEvent actionEvent) {
	}

	/* (non-Javadoc)
	 * @see soars.application.simulator.menu.file.IFileMenuHandler#on_file_new(java.awt.event.ActionEvent)
	 */
	public void on_file_new(ActionEvent actionEvent) {
	}

	/* (non-Javadoc)
	 * @see soars.application.simulator.menu.file.IFileMenuHandler#on_file_open(java.awt.event.ActionEvent)
	 */
	public void on_file_open(ActionEvent actionEvent) {
	}

	/* (non-Javadoc)
	 * @see soars.application.simulator.menu.file.IFileMenuHandler#on_file_close(java.awt.event.ActionEvent)
	 */
	public void on_file_close(ActionEvent actionEvent) {
	}

	/* (non-Javadoc)
	 * @see soars.application.simulator.menu.file.IFileMenuHandler#on_file_save(java.awt.event.ActionEvent)
	 */
	public void on_file_save(ActionEvent actionEvent) {
	}

	/* (non-Javadoc)
	 * @see soars.application.simulator.menu.file.IFileMenuHandler#on_file_save_as(java.awt.event.ActionEvent)
	 */
	public void on_file_save_as(ActionEvent actionEvent) {
		File file = CommonTool.get_save_file(
			soars.plugin.modelbuilder.chart.chart.main.Environment._save_as_directory_key,
			ResourceManager.get_instance().get( "file.save.as.dialog"),
			new String[] { "pml"},
			"soars chart data",
			this);

		if ( null == file)
			return;

		String absoluteName = file.getAbsolutePath();
		String name = file.getName();
		int index = name.lastIndexOf( '.');
		if ( -1 == index)
			file = new File( absoluteName + ".pml");
		else if ( name.length() - 1 == index)
			file = new File( absoluteName + "pml");

		if ( !MessageDlg.execute( MainFrame.get_instance(), getTitle(), true,
			"on_file_save_as", ResourceManager.get_instance().get( "file.save.as.show.message"),
			new Object[] { file.getAbsolutePath()}, this, this)) {
			JOptionPane.showMessageDialog( this, ResourceManager.get_instance().get( "file.save.as.error.message"), ResourceManager.get_instance().get( "application.title"), JOptionPane.ERROR_MESSAGE);
			return;
		}
	}

	/* (non-Javadoc)
	 * @see soars.application.simulator.menu.file.IFileMenuHandler#on_file_save_image_as(java.awt.event.ActionEvent)
	 */
	public void on_file_save_image_as(ActionEvent actionEvent) {
		String[] formatNames = ImageIO.getWriterFormatNames();
		if ( null == formatNames || 0 == formatNames.length)
			return;

		Arrays.sort( formatNames);
		if ( 0 > Arrays.binarySearch( formatNames, "png"))
			return;

		BufferedImage bufferedImage = _logViewer.exportImage();
		//BufferedImage bufferedImage = _logViewer.exportImage( _logViewer.getBounds());
		if ( null == bufferedImage)
			return;

		File file = CommonTool.get_save_file(
			soars.plugin.modelbuilder.chart.chart.main.Environment._save_image_as_directory_key,
			ResourceManager.get_instance().get( "file.save.image.as.dialog"),
			new String[] { "png"},
			"soars chart image data",
			this);

		if ( null == file)
			return;

		String absoluteName = file.getAbsolutePath();
		String name = file.getName();
		int index = name.lastIndexOf( '.');
		if ( -1 == index)
			file = new File( absoluteName + ".png");
		else if ( name.length() - 1 == index)
			file = new File( absoluteName + "png");

		if ( !MessageDlg.execute( MainFrame.get_instance(), getTitle(), true,
			"on_file_save_image_as", ResourceManager.get_instance().get( "file.save.image.as.show.message"),
			new Object[] { bufferedImage, "png", file}, this, this)) {
			JOptionPane.showMessageDialog( this, ResourceManager.get_instance().get( "file.save.image.as.error.message"), ResourceManager.get_instance().get( "application.title"), JOptionPane.ERROR_MESSAGE);
			return;
		}
	}

	/* (non-Javadoc)
	 * @see soars.common.utility.swing.message.IMessageCallback#message_callback(java.lang.String, java.lang.Object[], soars.common.utility.swing.message.MessageDlg)
	 */
	public boolean message_callback(String id, Object[] objects, MessageDlg messageDlg) {
		if ( id.equals( "on_file_save_as")) {
			try {
				return _logViewer.write_data( ( String)objects[ 0]);
			} catch (IOException e) {
				e.printStackTrace();
				return false;
			}
		} else if ( id.equals( "on_file_save_image_as")) {
			try {
				return ImageIO.write( ( BufferedImage)objects[ 0],
					( String)objects[ 1],
					( File)objects[ 2]);
			} catch (IOException e) {
				e.printStackTrace();
				return false;
			}
		}
		return true;
	}

	/**
	 * @return
	 */
	public boolean optimize_window_rectangle() {
		Rectangle rectangle = getBounds();
		if ( !MainFrame.get_instance().get_client_rectangle().intersects( rectangle)
			|| MainFrame.get_instance().get_client_rectangle().intersection( rectangle).width <= 10
			|| rectangle.y <= -MainFrame.get_instance().getInsets().top
			|| MainFrame.get_instance().get_client_rectangle().intersection( rectangle).height <= MainFrame.get_instance().getInsets().top) {
			Point position = SwingTool.get_default_window_position(
				MainFrame.get_instance().get_client_rectangle().getBounds().width,
				MainFrame.get_instance().get_client_rectangle().getBounds().height,
				_defaultWidth, _defaultHeight);
			setLocation( position);
			setSize( _defaultWidth, _defaultHeight);
			return true;
		}
		return false;
	}

	/**
	 * @param writer
	 * @return
	 * @throws SAXException
	 */
	public boolean write(Writer writer) throws SAXException {
		return _logViewer.write( writer, _name, getBounds());
	}

	/**
	 * @param dataset
	 * @param file
	 * @param connect
	 * @param writer
	 * @return
	 * @throws SAXException
	 */
	public boolean write(int dataset, File file, boolean connect, Writer writer) throws SAXException {
		return _logViewer.write( dataset, file, connect, writer, _name, getBounds());
	}

	/**
	 * @param dataset
	 * @param file
	 * @return
	 */
	public boolean write(int dataset, File file) {
		return _logViewer.write( dataset, file);
	}
}
