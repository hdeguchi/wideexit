/**
 * 
 */
package soars.application.simulator.main.filemanager;

import java.awt.Image;
import java.awt.Point;
import java.awt.Rectangle;
import java.beans.PropertyVetoException;
import java.io.File;

import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.event.InternalFrameEvent;

import org.xml.sax.SAXException;
import org.xml.sax.helpers.AttributesImpl;

import soars.application.simulator.data.FileManagerData;
import soars.application.simulator.main.Constant;
import soars.application.simulator.main.Environment;
import soars.application.simulator.main.MainFrame;
import soars.application.simulator.main.ResourceManager;
import soars.application.simulator.warning.WarningDlg1;
import soars.common.soars.warning.WarningManager;
import soars.common.utility.swing.file.manager.FileManager;
import soars.common.utility.swing.file.manager.IFileManager;
import soars.common.utility.swing.file.manager.IFileManagerCallBack;
import soars.common.utility.swing.file.manager.edit.InternalFileEditorFrame;
import soars.common.utility.swing.tool.SwingTool;
import soars.common.utility.swing.window.MDIChildFrame;
import soars.common.utility.tool.resource.Resource;
import soars.common.utility.xml.sax.Writer;

/**
 * @author kurata
 *
 */
public class FileManagerFrame extends MDIChildFrame implements IFileManagerCallBack {

	/**
	 * Default width.
	 */
	static public final int _defaultWidth = 600;

	/**
	 * Default height.
	 */
	static public final int _defaultHeight = 400;

	/**
	 * 
	 */
	private FileManager _fileManager = null;

	/**
	 * 
	 */
	private boolean _setupCompleted = false;

	/**
	 * @param arg0
	 * @param arg1
	 * @param arg2
	 * @param arg3
	 * @param arg4
	 */
	public FileManagerFrame(String arg0, boolean arg1, boolean arg2, boolean arg3, boolean arg4) {
		super(arg0, arg1, arg2, arg3, arg4);
	}

	/* (non-Javadoc)
	 * @see soars.common.utility.swing.window.MDIChildFrame#create()
	 */
	public boolean create() {
		return create( null);
	}

	/**
	 * @param fileManagerData
	 * @return
	 */
	public boolean create(FileManagerData fileManagerData) {
		if ( !super.create())
			return false;


		if ( !setup_fileManager( fileManagerData))
			return false;


		Image image = Resource.load_image_from_resource(
			Constant._resourceDirectory + "/image/icon/icon.png", getClass());
		if ( null != image)
			setFrameIcon( new ImageIcon( image));


		pack();


		if ( null == fileManagerData)
			setSize( _defaultWidth, _defaultHeight);
		else {
			setLocation( fileManagerData._windowRectangle.x, fileManagerData._windowRectangle.y);
			setSize( fileManagerData._windowRectangle.width, fileManagerData._windowRectangle.height);
		}


		setVisible( ( null == fileManagerData) ? true : fileManagerData._visible);


		try {
			setSelected( true);
		} catch (PropertyVetoException e) {
			e.printStackTrace();
		}


		setDefaultCloseOperation( DO_NOTHING_ON_CLOSE);


		_setupCompleted = true;


		return true;
	}

	/**
	 * @param fileManagerData
	 * @return
	 */
	private boolean setup_fileManager(FileManagerData fileManagerData) {
		_fileManager = new FileManager( this, MainFrame.get_instance(), this);
		if ( !_fileManager.setup( false, MainFrame.get_instance().get_user_data_directory()))
			return false;

		_fileManager.set_current_directory( ( null == fileManagerData) ? "" : fileManagerData._currentDirectory);

		_fileManager.setDividerLocation( ( null == fileManagerData) ? 100 : fileManagerData._dividerLocation);

		getContentPane().add( "Center", _fileManager);

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

	/* (non-Javadoc)
	 * @see soars.common.utility.swing.window.MDIChildFrame#on_internal_frame_closing(javax.swing.event.InternalFrameEvent)
	 */
	protected void on_internal_frame_closing(InternalFrameEvent internalFrameEvent) {
		setVisible( false);
		MainFrame.get_instance().modified( true);
	}

	/**
	 * @param writer
	 * @return
	 * @throws SAXException
	 */
	public boolean write(Writer writer) throws SAXException {
		Rectangle rectangle = getBounds();

		AttributesImpl attributesImpl = new AttributesImpl();
		attributesImpl.addAttribute( null, null, "x", "", String.valueOf( rectangle.x));
		attributesImpl.addAttribute( null, null, "y", "", String.valueOf( rectangle.y));
		attributesImpl.addAttribute( null, null, "width", "", String.valueOf( rectangle.width));
		attributesImpl.addAttribute( null, null, "height", "", String.valueOf( rectangle.height));
		attributesImpl.addAttribute( null, null, "divider_location", "", String.valueOf( _fileManager.getDividerLocation()));
		attributesImpl.addAttribute( null, null, "current_directory", "", Writer.escapeAttributeCharData( _fileManager.get_current_directory()));
		attributesImpl.addAttribute( null, null, "visible", "", String.valueOf( isVisible()));
		writer.writeElement( null, null, "file_manager_data", attributesImpl);

		return true;
	}

	/* (non-Javadoc)
	 * @see soars.common.utility.swing.file.manager.IFileManagerCallBack#on_start_paste()
	 */
	public void on_start_paste() {
		WarningManager.get_instance().cleanup();
	}

	/* (non-Javadoc)
	 * @see soars.common.utility.swing.file.manager.IFileManagerCallBack#on_start_paste_and_remove()
	 */
	public void on_start_paste_and_remove() {
		WarningManager.get_instance().cleanup();
	}

	/* (non-Javadoc)
	 * @see soars.common.utility.swing.file.manager.IFileManagerCallBack#can_copy(java.io.File)
	 */
	public boolean can_copy(File file) {
		return true;
	}

	/* (non-Javadoc)
	 * @see soars.common.utility.swing.file.manager.IFileManagerCallBack#can_paste(java.io.File)
	 */
	public boolean can_paste(File file) {
		if ( 0 > file.getAbsolutePath().indexOf( "$"))
			return true;

		if ( WarningManager.get_instance().size() < 100)
			WarningManager.get_instance().add( new String[] { file.getAbsolutePath()});

		return false;
	}

	/* (non-Javadoc)
	 * @see soars.common.utility.swing.file.manager.IFileManagerCallBack#on_paste_completed()
	 */
	public void on_paste_completed() {
		warn( ResourceManager.get_instance().get( "warning.dialog1.message4"));
	}

	/* (non-Javadoc)
	 * @see soars.common.utility.swing.file.manager.IFileManagerCallBack#on_paste_and_remove_completed()
	 */
	public void on_paste_and_remove_completed() {
		warn( ResourceManager.get_instance().get( "warning.dialog1.message5"));
	}

	/**
	 * @param message
	 */
	private void warn(String message) {
		if ( WarningManager.get_instance().isEmpty())
			return;

		WarningDlg1 warningDlg1 = new WarningDlg1(
			MainFrame.get_instance(),
			ResourceManager.get_instance().get( "warning.dialog1.title"),
			message,
			this);
		warningDlg1.do_modal();
	}

	/* (non-Javadoc)
	 * @see soars.common.utility.swing.file.manager.IFileManagerCallBack#modified(soars.common.utility.swing.file.manager.IFileManager)
	 */
	public void modified(IFileManager fileManager) {
		MainFrame.get_instance().modified( true);
	}

	/* (non-Javadoc)
	 * @see soars.common.utility.swing.file.manager.IFileManagerCallBack#select_changed(soars.common.utility.swing.file.manager.IFileManager)
	 */
	public void select_changed(IFileManager fileManager) {
		if ( !_setupCompleted)
			return;

		//MainFrame.get_instance().modified( true);
	}

	/* (non-Javadoc)
	 * @see soars.common.utility.swing.file.manager.IFileManagerCallBack#can_remove(java.io.File)
	 */
	public boolean can_remove(File file) {
		return true;
	}

	/* (non-Javadoc)
	 * @see soars.common.utility.swing.file.manager.IFileManagerCallBack#on_move(java.io.File, java.io.File)
	 */
	public void on_move(File srcPath, File destPath) {
	}

	/* (non-Javadoc)
	 * @see soars.common.utility.swing.file.manager.IFileManagerCallBack#on_select(java.io.File, java.lang.String)
	 */
	public void on_select(File file, String encoding) {
		// TODO Auto-generated method stub
		if ( !file.exists() || !file.isFile() || !file.canRead() || !file.canWrite())
			return;

		InternalFileEditorFrame iInternalFileEditorFrame = new InternalFileEditorFrame( file, true, true, true, true, MainFrame.get_instance(), Environment.get_instance(), Environment._fileEditorWindowRectangleKey, encoding, _fileManager, this);
		if ( !iInternalFileEditorFrame.create())
			return;

		Image image = Resource.load_image_from_resource( Constant._resourceDirectory + "/image/icon/icon.png", getClass());
		if ( null != image)
			iInternalFileEditorFrame.setFrameIcon( new ImageIcon( image));

//		if ( !MainFrame.append( iInternalFileEditorFrame))
//			return;

		iInternalFileEditorFrame.optimize_window_rectangle();

		if ( !MainFrame.get_instance().appendChildFrame( iInternalFileEditorFrame))
			return;

//		FileEditorFrame fileEditorFrame = new FileEditorFrame( file, Environment.get_instance(), Environment._file_editor_window_rectangle_key, _fileManager, this);
//		if ( !fileEditorFrame.create())
//			return;
//
//		Image image = Resource.load_image_from_resource( Constant._resource_directory + "/image/icon/icon.png", getClass());
//		if ( null != image)
//			fileEditorFrame.setIconImage( image);
	}

	/* (non-Javadoc)
	 * @see soars.common.utility.swing.file.manager.IFileManagerCallBack#on_rename(java.io.File, java.io.File)
	 */
	public void on_rename(File originalFile, File newFile) {
	}

	/* (non-Javadoc)
	 * @see soars.common.utility.swing.file.manager.IFileManagerCallBack#get_export_directory()
	 */
	public File get_export_directory() {
		// TODO Auto-generated method stub
		String exportDirectory = "";
		File directory = null;
		
		String value = Environment.get_instance().get( Environment._exportFilesDirectoryKey, "");
		if ( null != value && !value.equals( "")) {
			directory = new File( value);
			if ( directory.exists())
				exportDirectory = value;
		}
	
		JFileChooser fileChooser = new JFileChooser();
		fileChooser.setDialogTitle( ResourceManager.get_instance().get( "file.manager.export.files.title"));
		fileChooser.setFileSelectionMode( JFileChooser.DIRECTORIES_ONLY);

		if ( !exportDirectory.equals( "")) {
			fileChooser.setCurrentDirectory( new File( exportDirectory + "/../"));
			fileChooser.setSelectedFile( directory);
		}

		if ( JFileChooser.APPROVE_OPTION == fileChooser.showOpenDialog( _fileManager)) {
			directory = fileChooser.getSelectedFile();
			exportDirectory = directory.getAbsolutePath();
			Environment.get_instance().set( Environment._exportFilesDirectoryKey, exportDirectory);
			return directory;
		}

		return null;
	}

	/* (non-Javadoc)
	 * @see soars.common.utility.swing.file.manager.IFileManagerCallBack#visible(java.io.File)
	 */
	public boolean visible(File file) {
		return true;
	}
}
