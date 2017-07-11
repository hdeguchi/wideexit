/*
 * 2004/12/17
 */
package soars.plugin.modelbuilder.chart.log_viewer.body.main;

import java.awt.Frame;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.image.BufferedImage;
import java.beans.PropertyVetoException;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.event.InternalFrameEvent;

import soars.common.utility.swing.gui.UserInterface;
import soars.common.utility.swing.message.IMessageCallback;
import soars.common.utility.swing.message.MessageDlg;
import soars.common.utility.swing.window.MDIChildFrame;
import soars.common.utility.tool.resource.Resource;
import soars.plugin.modelbuilder.chart.log_viewer.body.common.tool.CommonTool;
import soars.plugin.modelbuilder.chart.log_viewer.body.menu.file.IFileMenuHandler;
import soars.plugin.modelbuilder.chart.log_viewer.body.menu.file.SaveImageAsAction;

/**
 * @author kurata
 */
public class ChartFrame extends MDIChildFrame implements IFileMenuHandler, IMessageCallback {

	/**
	 * 
	 */
	private Frame _owner = null;

	/**
	 * 
	 */
	private CustomLogViewer _customLogViewer = null;

	/**
	 * 
	 */
	private UserInterface _userInterface = null;

	/**
	 * 
	 */
	private JMenuItem _file_save_image_as_menuItem = null;

	/**
	 * @param arg0
	 * @param arg1
	 * @param arg2
	 * @param arg3
	 * @param arg4
	 * @param owner
	 */
	public ChartFrame(
		String arg0,
		boolean arg1,
		boolean arg2,
		boolean arg3,
		boolean arg4,
		Frame owner) {
		super(arg0, arg1, arg2, arg3, arg4);
		_owner = owner;
	}

	/**
	 * 
	 */
	private void setup_menu() {
		JMenuBar menuBar = new JMenuBar();

		JMenu menu = _userInterface.append_menu(
			menuBar,
			ResourceManager.get_instance().get( "file.menu"),
			true,
			ResourceManager.get_instance().get( "file.mnemonic"));

		_file_save_image_as_menuItem = _userInterface.append_menuitem(
			menu,
			ResourceManager.get_instance().get( "file.save.image.as.menu"),
			new SaveImageAsAction( ResourceManager.get_instance().get( "file.save.image.as.menu"), this),
			ResourceManager.get_instance().get( "file.save.image.as.mnemonic"),
			ResourceManager.get_instance().get( "file.save.image.as.stroke"));

		setJMenuBar( menuBar);
	}

	/* (Non Javadoc)
	 * @see soars.common.utility.swing.window.MDIChildFrame#on_create()
	 */
	protected boolean on_create() {
		if ( !super.on_create())
			return false;

		_userInterface = new UserInterface();

		_customLogViewer = new CustomLogViewer( this, _owner);
		if ( !_customLogViewer.setup())
			return false;

		getContentPane().add( "Center", _customLogViewer);
		//_logViewer.setButtons( true);
		_customLogViewer.setButtons( false);


		setup_menu();


		Image image = Resource.load_image_from_resource(
			Constant._resource_directory + "/image/icon/icon.png", getClass());
		if ( null != image)
			setFrameIcon( new ImageIcon( image));


		pack();


		setVisible( true);


		try {
			setSelected( true);
		} catch (PropertyVetoException e) {
			e.printStackTrace();
		}


		return true;
	}

	/**
	 * @param file
	 * @return
	 */
	public boolean read(File file) {
		return _customLogViewer.read( file);
	}

	/**
	 * 
	 */
	public void set_title() {
		setTitle( _customLogViewer.getTitle());
	}

	/* (Non Javadoc)
	 * @see soars.common.utility.swing.window.MDIChildFrame#on_internal_frame_closing(javax.swing.event.InternalFrameEvent)
	 */
	protected void on_internal_frame_closing(InternalFrameEvent internalFrameEvent) {
		MainFrame mainFrame = ( MainFrame)_owner;
		//mainFrame.update_user_interface();
		super.on_internal_frame_closing(internalFrameEvent);
	}

	/* (Non Javadoc)
	 * @see soars.common.utility.swing.window.MDIChildFrame#on_internal_frame_activated(javax.swing.event.InternalFrameEvent)
	 */
	protected void on_internal_frame_activated(InternalFrameEvent internalFrameEvent) {
		MainFrame mainFrame = ( MainFrame)_owner;
		//mainFrame.update_user_interface();
		super.on_internal_frame_activated(internalFrameEvent);
	}

	/* (Non Javadoc)
	 * @see soars.common.utility.swing.window.MDIChildFrame#on_internal_frame_deactivated(javax.swing.event.InternalFrameEvent)
	 */
	protected void on_internal_frame_deactivated(InternalFrameEvent internalFrameEvent) {
		MainFrame mainFrame = ( MainFrame)_owner;
		//mainFrame.update_user_interface();
		super.on_internal_frame_deactivated(internalFrameEvent);
	}

	/* (non-Javadoc)
	 * @see soars.plugin.modelbuilder.chart.log_viewer.menu.file.IFileMenuHandler#on_file_exit(java.awt.event.ActionEvent)
	 */
	public void on_file_exit(ActionEvent actionEvent) {
	}

	/* (non-Javadoc)
	 * @see soars.plugin.modelbuilder.chart.log_viewer.menu.file.IFileMenuHandler#on_file_open(java.awt.event.ActionEvent)
	 */
	public void on_file_open(ActionEvent actionEvent) {
	}

	/* (non-Javadoc)
	 * @see soars.plugin.modelbuilder.chart.log_viewer.menu.file.IFileMenuHandler#on_file_save_image_as(java.awt.event.ActionEvent)
	 */
	public void on_file_save_image_as(ActionEvent actionEvent) {
		String[] formatNames = ImageIO.getWriterFormatNames();
		if ( null == formatNames || 0 == formatNames.length)
			return;

		Arrays.sort( formatNames);
		if ( 0 > Arrays.binarySearch( formatNames, "png"))
			return;

		BufferedImage bufferedImage = _customLogViewer.exportImage();
		//BufferedImage bufferedImage = _logViewer.exportImage( _logViewer.getBounds());
		if ( null == bufferedImage)
			return;

		File file = CommonTool.get_save_file(
			Environment._save_image_directory_key,
			ResourceManager.get_instance().get( "file.save.image.as.dialog"),
			new String[] { "png"},
			"soars chart image data",
			this);

		if ( null == file)
			return;

		String absolute_name = file.getAbsolutePath();
		String name = file.getName();
		int index = name.lastIndexOf( '.');
		if ( -1 == index)
			file = new File( absolute_name + ".png");
		else if ( name.length() - 1 == index)
			file = new File( absolute_name + "png");

		if ( !MessageDlg.execute( _owner, getTitle(), true,
			"on_file_save_image_as", ResourceManager.get_instance().get( "file.save.image.as.show.message"),
			new Object[] { bufferedImage, "png", file}, this, this))
			return;
	}

	/* (non-Javadoc)
	 * @see soars.common.utility.swing.message.IMessageCallback#message_callback(java.lang.String, java.lang.Object[], soars.common.utility.swing.message.MessageDlg)
	 */
	public boolean message_callback(String id, Object[] objects, MessageDlg messageDlg) {
		if ( id.equals( "on_file_save_image_as")) {
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
}
