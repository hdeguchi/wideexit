/**
 * 
 */
package soars.application.manager.library.main.tab.module;

import java.awt.Component;
import java.awt.Frame;
import java.awt.Image;
import java.io.File;
import java.net.MalformedURLException;

import javax.swing.JFileChooser;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;

import soars.application.manager.library.main.Constant;
import soars.application.manager.library.main.Environment;
import soars.application.manager.library.main.ResourceManager;
import soars.application.manager.library.main.tab.module.table.ModuleFileTable;
import soars.application.manager.library.main.tab.module.tree.ModuleDirectoryTree;
import soars.application.manager.library.main.tab.module.tree.ModuleDirectoryTreeCellRenderer;
import soars.application.manager.library.main.tab.tab.InternalTabbedPane;
import soars.common.utility.swing.file.manager.IFileManager;
import soars.common.utility.swing.file.manager.IFileManagerCallBack;
import soars.common.utility.swing.file.manager.edit.FileEditorFrame;
import soars.common.utility.tool.file.FileUtility;
import soars.common.utility.tool.network.BrowserLauncher;
import soars.common.utility.tool.resource.Resource;

/**
 * @author kurata
 *
 */
public class ModuleFileManager extends JSplitPane implements IFileManagerCallBack, IFileManager {

	/**
	 * 
	 */
	private ModuleDirectoryTree _moduleDirectoryTree = null;

	/**
	 * 
	 */
	private JSplitPane _splitPane = null;

	/**
	 * 
	 */
	private ModuleFileTable _moduleFileTable = null;

	/**
	 * 
	 */
	private InternalTabbedPane _internalTabbedPane = null;

	/**
	 * 
	 */
	private Frame _owner = null;

	/**
	 * 
	 */
	private Component _parent = null;

	/**
	 * @param owner
	 * @param parent
	 */
	public ModuleFileManager(Frame owner, Component parent) {
		super(JSplitPane.HORIZONTAL_SPLIT);
		_owner = owner;
		_parent = parent;
	}

	/**
	 * @param root_directories
	 * @return
	 */
	public boolean setup(String[] root_directories) {
		_splitPane = new JSplitPane( JSplitPane.VERTICAL_SPLIT);

		_internalTabbedPane = new InternalTabbedPane( JTabbedPane.BOTTOM, _owner, _parent);

		_moduleDirectoryTree = new ModuleDirectoryTree( root_directories, this, this, this, _owner, _parent);

		_moduleFileTable = new ModuleFileTable( this, this, this, _owner, _parent);

		if ( !_internalTabbedPane.setup( Environment._annotation_pane_divider_location1_key, _moduleFileTable))
			return false;

		if ( !_moduleFileTable.setup( _moduleDirectoryTree, _internalTabbedPane))
			return false;

		if ( !_moduleDirectoryTree.setup( true, new ModuleDirectoryTreeCellRenderer(), _moduleFileTable, _internalTabbedPane, _internalTabbedPane._propertyEditorPane))
			return false;

		setup_moduleDirectoryTree();
		setup_moduleFileTable();
		_splitPane.setBottomComponent( _internalTabbedPane);

		setRightComponent( _splitPane);

		setDividerLocation( Integer.parseInt( Environment.get_instance().get( Environment._module_file_manager_divider_location1_key, "100")));

		_splitPane.setDividerLocation( Integer.parseInt( Environment.get_instance().get( Environment._module_file_manager_divider_location2_key, "100")));

		return true;
	}

	/**
	 * 
	 */
	private void setup_moduleDirectoryTree() {
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setHorizontalScrollBarPolicy( JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		scrollPane.setVerticalScrollBarPolicy( JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
		scrollPane.getViewport().setView( _moduleDirectoryTree);

		setLeftComponent( scrollPane);
	}

	/**
	 * 
	 */
	private void setup_moduleFileTable() {
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setHorizontalScrollBarPolicy( JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		scrollPane.setVerticalScrollBarPolicy( JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
		scrollPane.getViewport().setView( _moduleFileTable);

		_splitPane.setTopComponent( scrollPane);
	}

	/**
	 * 
	 */
	public void optimize_divider_location() {
		setDividerLocation( 100);
		_splitPane.setDividerLocation( 100);
		_internalTabbedPane.optimize_divider_location();
	}

	/**
	 * 
	 */
	public void on_setup_completed() {
		_moduleDirectoryTree.on_setup_completed();
		_moduleFileTable.on_setup_completed();
		_internalTabbedPane.on_setup_completed();
	}

	/**
	 * @param save
	 */
	public void refresh(boolean save) {
		// 苦肉の策、美しくない
		boolean flag = _moduleDirectoryTree._flag;
		_moduleDirectoryTree._flag = save;
		_moduleDirectoryTree.refresh();
		_moduleDirectoryTree._flag = flag;

		_internalTabbedPane.refresh( save);
	}

	/**
	 * 
	 */
	public void store() {
		_internalTabbedPane.store();
	}

	/**
	 * 
	 */
	public void set_property_to_environment_file() {
		Environment.get_instance().set(
			Environment._module_file_manager_divider_location1_key, String.valueOf( getDividerLocation()));
		Environment.get_instance().set(
			Environment._module_file_manager_divider_location2_key, String.valueOf( _splitPane.getDividerLocation()));

		_internalTabbedPane.set_property_to_environment_file();
	}

	/* (non-Javadoc)
	 * @see soars.common.utility.swing.file.manager.IFileManagerCallBack#on_start_paste()
	 */
	public void on_start_paste() {
		refresh( true);
	}

	/* (non-Javadoc)
	 * @see soars.common.utility.swing.file.manager.IFileManagerCallBack#on_start_paste_and_remove()
	 */
	public void on_start_paste_and_remove() {
		refresh( true);
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
		return ( !file.getName().startsWith( ".") && !file.getName().toLowerCase().equals( Constant._moduleSpringFilename)/* && !file.getName().toLowerCase().endsWith( Constant._moduleAnnotationFileExtension)*/);
	}

	/* (non-Javadoc)
	 * @see soars.common.utility.swing.file.manager.IFileManagerCallBack#on_paste_completed()
	 */
	public void on_paste_completed() {
		refresh( false);
	}

	/* (non-Javadoc)
	 * @see soars.common.utility.swing.file.manager.IFileManagerCallBack#on_paste_and_remove_completed()
	 */
	public void on_paste_and_remove_completed() {
		refresh( false);
	}

	/* (non-Javadoc)
	 * @see soars.common.utility.swing.file.manager.IFileManagerCallBack#modified(soars.common.utility.swing.file.manager.IFileManager)
	 */
	public void modified(IFileManager fileManager) {
	}

	/* (non-Javadoc)
	 * @see soars.common.utility.swing.file.manager.IFileManagerCallBack#select_changed(soars.common.utility.swing.file.manager.IFileManager)
	 */
	public void select_changed(IFileManager fileManager) {
		_moduleDirectoryTree.update();
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
		if ( !file.exists() || !file.isFile() || !file.canRead() || !file.canWrite())
			return;

		if ( null == encoding && ( file.getName().endsWith( ".html") || file.getName().endsWith( ".htm"))) {
			try {
				BrowserLauncher.openURL( file.toURI().toURL().toString().replaceAll( "\\\\", "/"));
			} catch (MalformedURLException e) {
				e.printStackTrace();
			}
			return;
		}

		FileEditorFrame fileEditorFrame = new FileEditorFrame( file, Environment.get_instance(), Environment._file_editor_window_rectangle_key, encoding, this, this);
		if ( !fileEditorFrame.create())
			return;

		Image image = Resource.load_image_from_resource( Constant._resourceDirectory + "/image/icon/icon.png", getClass());
		if ( null != image)
			fileEditorFrame.setIconImage( image);
	}

	/* (non-Javadoc)
	 * @see soars.common.utility.swing.file.manager.IFileManagerCallBack#on_rename(java.io.File, java.io.File)
	 */
	public void on_rename(File originalFile, File newFile) {
		if ( originalFile.getName().toLowerCase().endsWith( ".jar")) {
			File annotationFile = new File( originalFile.getParent(), originalFile.getName().substring( 0, originalFile.getName().length() - ".jar".length()) + Constant._moduleAnnotationFileExtension);
			if ( annotationFile.exists() && annotationFile.isFile()) {
				if ( newFile.isFile() && newFile.getName().toLowerCase().endsWith( ".jar")) {
					File newAnnotationFile = new File( originalFile.getParent(), newFile.getName().substring( 0, newFile.getName().length() - ".jar".length()) + Constant._moduleAnnotationFileExtension);
					FileUtility.copy( annotationFile, newAnnotationFile);
				}
				annotationFile.delete();
				refresh( false);
			}
		}
	}

	/* (non-Javadoc)
	 * @see soars.common.utility.swing.file.manager.IFileManagerCallBack#get_export_directory()
	 */
	public File get_export_directory() {
		String export_directory = "";
		File directory = null;
		
		String value = Environment.get_instance().get( Environment._export_files_directory_key, "");
		if ( null != value && !value.equals( "")) {
			directory = new File( value);
			if ( directory.exists())
				export_directory = value;
		}
	
		JFileChooser fileChooser = new JFileChooser();
		fileChooser.setDialogTitle( ResourceManager.get_instance().get( "export.files.title"));
		fileChooser.setFileSelectionMode( JFileChooser.DIRECTORIES_ONLY);

		if ( !export_directory.equals( "")) {
			fileChooser.setCurrentDirectory( new File( export_directory + "/../"));
			fileChooser.setSelectedFile( directory);
		}

		if ( JFileChooser.APPROVE_OPTION == fileChooser.showOpenDialog( _owner)) {
			directory = fileChooser.getSelectedFile();
			export_directory = directory.getAbsolutePath();
			Environment.get_instance().set( Environment._export_files_directory_key, export_directory);
			return directory;
		}

		return null;
	}

	/* (non-Javadoc)
	 * @see soars.common.utility.swing.file.manager.IFileManagerCallBack#visible(java.io.File)
	 */
	public boolean visible(File file) {
		return ( !file.getName().startsWith( ".") && !file.getName().equals( Constant._moduleSpringFilename) && !file.getName().endsWith( Constant._moduleAnnotationFileExtension));
	}
}
