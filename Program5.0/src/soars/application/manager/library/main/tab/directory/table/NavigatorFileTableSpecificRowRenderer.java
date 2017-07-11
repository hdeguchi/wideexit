/**
 * 
 */
package soars.application.manager.library.main.tab.directory.table;

import java.io.File;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JTable;

import soars.application.manager.library.main.Constant;
import soars.application.manager.library.main.tab.directory.tree.NavigatorDirectoryTree;
import soars.common.utility.swing.file.manager.table.FileTableSpecificRowRendererBase;

/**
 * @author kurata
 *
 */
public class NavigatorFileTableSpecificRowRenderer extends FileTableSpecificRowRendererBase {

	/**
	 * 
	 */
	protected Icon _directoryCloseIcon = null;

	/**
	 * 
	 */
	protected Icon _moduleDirectoryCloseIcon = null;

	/**
	 * 
	 */
	protected Icon _htmlFileIcon = null;

	/**
	 * 
	 */
	private NavigatorDirectoryTree _navigatorDirectoryTree = null;

	/**
	 * @param table
	 * @param navigatorDirectoryTree
	 */
	public NavigatorFileTableSpecificRowRenderer(JTable table, NavigatorDirectoryTree navigatorDirectoryTree) {
		super(table);
		_directoryCloseIcon = new ImageIcon( getClass().getResource( Constant._resourceDirectory + "/image/directory_close.png"));
		_moduleDirectoryCloseIcon = new ImageIcon( getClass().getResource( Constant._resourceDirectory + "/image/module_directory_close.png"));
		_htmlFileIcon = new ImageIcon( getClass().getResource( Constant._resourceDirectory + "/image/html_file.png"));
		_navigatorDirectoryTree = navigatorDirectoryTree;
	}

	/* (non-Javadoc)
	 * @see soars.common.utility.swing.file.manager.table.FileTableSpecificRowRendererBase#on_setIcon(javax.swing.JLabel, java.io.File)
	 */
	protected void on_setIcon(JLabel iconLabel, File file) {
		if ( file.isDirectory())
			iconLabel.setIcon( is_module( file) ? _moduleDirectoryCloseIcon : _directoryCloseIcon);
		else
			iconLabel.setIcon( ( file.getName().endsWith( "html") || file.getName().endsWith( "htm")) ? _htmlFileIcon : _leafIcon);
	}

	/**
	 * @param directory
	 * @return
	 */
	private boolean is_module(File directory) {
		if ( _navigatorDirectoryTree.is_root_directory( directory))
			return false;

		return ( null != _navigatorDirectoryTree.get_module( directory, true));
	}
}
