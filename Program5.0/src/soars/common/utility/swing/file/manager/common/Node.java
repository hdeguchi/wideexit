/**
 * 
 */
package soars.common.utility.swing.file.manager.common;

import java.awt.Component;
import java.awt.dnd.DnDConstants;
import java.io.File;
import java.io.IOException;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JOptionPane;

import soars.common.utility.swing.file.manager.IFileManager;
import soars.common.utility.swing.file.manager.IFileManagerCallBack;
import soars.common.utility.swing.file.manager.ResourceManager;
import soars.common.utility.swing.progress.IntProgressDlg;
import soars.common.utility.tool.file.FileUtility;

/**
 * @author kurata
 *
 */
public class Node extends File {

	/**
	 * 
	 */
	public Node[] _childs = null;

	/**
	 * 
	 */
	public File[] _files = null;

	/**
	 * 
	 */
	static private int _kindOfPaste = 0;

	/**
	 * 
	 */
	static private int _counter = 0;

	/**
	 * @param parent
	 * @param child
	 */
	public Node(File directory) {
		super(directory.getAbsolutePath());
	}

	/**
	 * @param counter
	 * @return
	 */
	public boolean create(IntBuffer counter) {
		return create( listFiles(), counter);
	}

	/**
	 * @param files
	 * @param counter
	 * @return
	 */
	public boolean create(File[] files, IntBuffer counter) {
		if ( null == files)
			return false;

		List nodeList = new ArrayList();
		List fileList = new ArrayList();
		for ( int i = 0; i < files.length; ++i) {
			if ( null == files[ i] || !files[ i].exists())
				continue;

			if ( files[ i].isDirectory()) {
				Node node = new Node( files[ i]);
				if ( !node.create( node.listFiles(), counter))
					return false;

				nodeList.add ( node);
				counter.put( 0, counter.get( 0) + 1);
			} else {
				fileList.add( files[ i]);
				counter.put( 0, counter.get( 0) + 1);
			}
		}

		_childs = ( Node[])nodeList.toArray( new Node[ 0]);
		_files = ( File[])fileList.toArray( new File[ 0]);

		return true;
	}

	/**
	 * @param parent
	 * @param action
	 * @param auto
	 * @param checkModified
	 * @param fileManager
	 * @param fileManagerCallBack
	 * @param component
	 * @param intProgressDlg
	 * @param size
	 * @return
	 */
	public int paste(File parent, int action, boolean auto, boolean checkModified, IFileManager fileManager, IFileManagerCallBack fileManagerCallBack, Component component, IntProgressDlg intProgressDlg, int size) {
		_kindOfPaste = 0;
		_counter = 0;
		return internal_paste( parent, action, auto, checkModified, fileManager, fileManagerCallBack, component, intProgressDlg, size);
	}

	/**
	 * @param parent
	 * @param action
	 * @param auto
	 * @param checkModified
	 * @param fileManager
	 * @param fileManagerCallBack
	 * @param component
	 * @param intProgressDlg
	 * @param size
	 * @return
	 */
	private int internal_paste(File parent, int action, boolean auto, boolean checkModified, IFileManager fileManager, IFileManagerCallBack fileManagerCallBack, Component component, IntProgressDlg intProgressDlg, int size) {
		if ( 4 == _kindOfPaste)
			return 0;

		if ( null != intProgressDlg && intProgressDlg._canceled) {
			_kindOfPaste = 4;
			return 0;
		}

		for ( int i = 0; i < _childs.length; ++i) {
			if ( 4 == _kindOfPaste)
				return 0;

			if ( null != intProgressDlg && intProgressDlg._canceled) {
				_kindOfPaste = 4;
				return 0;
			}

			File destPath = auto ? get_unique_path( parent, _childs[ i]) : new File( parent, _childs[ i].getName());
			if ( destPath.exists()) {
				if ( 3 == _kindOfPaste) {
					update_progress_bar( intProgressDlg, size);
					continue;
				} else if ( 2 != _kindOfPaste) {
					// "Yes", "No", "Yes for all remained", "No for all remained", "Cancel"
					int result = showOptionDialog( _childs[ i], ( null != intProgressDlg) ? intProgressDlg : component);
					if ( 1 == result) {	// No
						update_progress_bar( intProgressDlg, size);
						continue;
					} else if ( 4 == result || JOptionPane.CLOSED_OPTION == result) {		// Cancel or Close
						// 既に行った操作はキャンセル出来ない
						_kindOfPaste = 4;
						return 0;
					} else if ( 2 == result) {	// Yes for all remained
						_kindOfPaste = 2;
					} else if ( 3 == result) {	// No for all remained
						_kindOfPaste = 3;
						update_progress_bar( intProgressDlg, size);
						continue;
					}
					update_progress_bar( intProgressDlg, size);
				}
			} else {
				if ( !fileManagerCallBack.can_copy( _childs[ i])) {
					update_progress_bar( intProgressDlg, size);
					continue;
				}

				if ( !fileManagerCallBack.can_paste( destPath)) {
					update_progress_bar( intProgressDlg, size);
					continue;
				}

				if ( !destPath.mkdirs())
					return -1;

				if ( null != fileManagerCallBack) {
					if ( checkModified)
						fileManagerCallBack.modified( fileManager);
					if ( DnDConstants.ACTION_MOVE == action)
						fileManagerCallBack.on_move( _childs[ i], destPath);
				}

				update_progress_bar( intProgressDlg, size);
			}

			int result = _childs[ i].internal_paste( destPath, action, auto, checkModified, fileManager, fileManagerCallBack, component, intProgressDlg, size);
			if ( 0 >= result)
				return result;
		}

		for ( int i = 0; i < _files.length; ++i) {
			if ( 4 == _kindOfPaste)
				return 0;

			if ( null != intProgressDlg && intProgressDlg._canceled) {
				_kindOfPaste = 4;
				return 0;
			}

			File destPath = auto ? get_unique_path( parent, _files[ i]) : new File( parent, _files[ i].getName());
			if ( destPath.exists()) {
				if ( 3 == _kindOfPaste) {
					update_progress_bar( intProgressDlg, size);
					continue;
				} else if ( 2 != _kindOfPaste) {
					// "Yes", "No", "Yes for all remained", "No for all remained", "Cancel"
					int result = showOptionDialog( _files[ i], ( null != intProgressDlg) ? intProgressDlg : component);
					if ( 1 == result) {	// No
						update_progress_bar( intProgressDlg, size);
						continue;
					} else if ( 4 == result || JOptionPane.CLOSED_OPTION == result) {		// Cancel or Close
						// 既に行った操作はキャンセル出来ない
						_kindOfPaste = 4;
						return 0;
					} else if ( 2 == result) {	// Yes for all remained
						_kindOfPaste = 2;
					} else if ( 3 == result) {	// No for all remained
						_kindOfPaste = 3;
						update_progress_bar( intProgressDlg, size);
						continue;
					}
					update_progress_bar( intProgressDlg, size);
				}
			} else {
				if ( !fileManagerCallBack.can_copy( _files[ i])) {
					update_progress_bar( intProgressDlg, size);
					continue;
				}

				if ( !fileManagerCallBack.can_paste( destPath)) {
					update_progress_bar( intProgressDlg, size);
					continue;
				}

				try {
					destPath.createNewFile();
				} catch (IOException e) {
					e.printStackTrace();
					return -1;
				}
			}

			if ( !FileUtility.copy( _files[ i], destPath))
				return -1;

			if ( null != fileManagerCallBack) {
				if ( checkModified)
					fileManagerCallBack.modified( fileManager);
				if ( DnDConstants.ACTION_MOVE == action)
					fileManagerCallBack.on_move( _files[ i], destPath);
			}

			update_progress_bar( intProgressDlg, size);
		}

		return 1;
	}

	/**
	 * @param parent
	 * @param file
	 * @return
	 */
	private File get_unique_path(File parent, File file) {
		String name = file.getName();
		File destPath = new File( parent, name);
		if ( !destPath.exists())
			return destPath;

		String new_name = "Copy of " + name;
		destPath = new File( parent, new_name);
		int index = 1;
		while ( destPath.exists()) {
			new_name = "Copy (" + String.valueOf( index) + ") of " + name;
			destPath = new File( parent, new_name);
			if ( !destPath.exists())
				break;

			++index;
		}

		return destPath;
	}

	/**
	 * @param intProgressDlg
	 * @param size
	 */
	private void update_progress_bar(IntProgressDlg intProgressDlg, int size) {
		if ( null == intProgressDlg || 0 >= size || _counter >= size)
			return;

		intProgressDlg.set( ( int)( 100.0 * ( double)++_counter / ( double)size));
		//System.out.println( _counter + ", " + size);
	}

	/**
	 * @param file
	 * @param component
	 * @return
	 */
	private int showOptionDialog(File file, Component component) {
		String[] overwrite_options = new String[] {
			ResourceManager.get_instance().get( "dialog.yes"),
			ResourceManager.get_instance().get( "dialog.no"),
			ResourceManager.get_instance().get( "dialog.yes.for.all.remained"),
			ResourceManager.get_instance().get( "dialog.no.for.all.remained"),
			ResourceManager.get_instance().get( "dialog.cancel")};
		return JOptionPane.showOptionDialog(
			component,
			file.getName() + " - " + ResourceManager.get_instance().get( "file.manager.confirm.overwrite.message"),
			ResourceManager.get_instance().get( "application.title"),
			JOptionPane.DEFAULT_OPTION,
			JOptionPane.INFORMATION_MESSAGE,
			null,
			overwrite_options,
			overwrite_options[ 0]);
	}
}
