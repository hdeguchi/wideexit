/**
 * 
 */
package soars.common.utility.swing.file.manager;

import java.awt.Component;
import java.awt.Frame;
import java.io.File;

import javax.swing.JScrollPane;
import javax.swing.JSplitPane;

import soars.common.utility.swing.file.manager.table.FileTableBase;
import soars.common.utility.swing.file.manager.tree.DirectoryTree1;

/**
 * @author kurata
 *
 */
public class FileManager extends JSplitPane implements IFileManager {

	/**
	 * 
	 */
	private DirectoryTree1 _directoryTree1 = null;

	/**
	 * 
	 */
	private FileTableBase _fileTableBase = null;

	/**
	 * 
	 */
	private IFileManagerCallBack _fileManagerCallBack = null;

	/**
	 * 
	 */
	protected Frame _owner = null;

	/**
	 * 
	 */
	protected Component _parent = null;

	/**
	 * @param fileManagerCallBack
	 * @param owner
	 * @param parent
	 */
	public FileManager(IFileManagerCallBack fileManagerCallBack, Frame owner, Component parent) {
		super(JSplitPane.HORIZONTAL_SPLIT);
		_fileManagerCallBack = fileManagerCallBack;
		_owner = owner;
		_parent = parent;
	}

	/**
	 * @return
	 */
	public String get_current_directory() {
		return _directoryTree1.get_current_directory();
	}

	/**
	 * @param path
	 */
	public void set_current_directory(String path) {
		_directoryTree1.set_current_directory( path);
	}

	/**
	 * @param path
	 */
	public void select(String path) {
		_directoryTree1.select( path);
	}

	/**
	 * @param popupMenu
	 * @param root_directory
	 * @return
	 */
	public boolean setup(boolean popupMenu, File root_directory) {
		_directoryTree1 = new DirectoryTree1( root_directory, _fileManagerCallBack, this, this, _owner, _parent);
		_fileTableBase = new FileTableBase( _fileManagerCallBack, this, this, _owner, _parent);

		if ( !setup_fileTable())
			return false;

		if ( !setup_directoryTree( popupMenu))
			return false;

		return true;
	}

	/**
	 * @param popupMenu
	 * @return
	 */
	private boolean setup_directoryTree(boolean popupMenu) {
		if ( !_directoryTree1.setup( popupMenu, _fileTableBase))
			return false;

		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setHorizontalScrollBarPolicy( JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		scrollPane.setVerticalScrollBarPolicy( JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
		scrollPane.getViewport().setView( _directoryTree1);

		setLeftComponent( scrollPane);

		return true;
	}

	/**
	 * @return
	 */
	private boolean setup_fileTable() {
		if ( !_fileTableBase.setup( _directoryTree1))
			return false;

		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setHorizontalScrollBarPolicy( JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		scrollPane.setVerticalScrollBarPolicy( JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
		scrollPane.getViewport().setView( _fileTableBase);

		setRightComponent( scrollPane);

		return true;
	}

	/**
	 * 
	 */
	public void on_setup_completed() {
		_directoryTree1.on_setup_completed();
		_fileTableBase.on_setup_completed();
	}

	/**
	 * @param root_directory
	 */
	public void update(File root_directory) {
		_directoryTree1.update( root_directory);
		setEnabled( true);
	}

	/**
	 * 
	 */
	public void cleanup() {
		_directoryTree1.update( ( File)null);
		_fileTableBase.cleanup();
		setEnabled( false);
	}

	/* (non-Javadoc)
	 * @see javax.swing.JComponent#setEnabled(boolean)
	 */
	public void setEnabled(boolean enabled) {
		super.setEnabled(enabled);
		_directoryTree1.setEnabled( enabled);
		_fileTableBase.setEnabled( enabled);
	}

	/**
	 * 
	 */
	public void refresh() {
		_directoryTree1.refresh();
	}
}
