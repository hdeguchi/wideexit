/**
 * 
 */
package soars.application.manager.library.main.tab.directory.table;

import java.awt.Component;
import java.awt.Frame;

import soars.application.manager.library.main.tab.common.table.FileTable;
import soars.application.manager.library.main.tab.directory.tree.NavigatorDirectoryTree;
import soars.application.manager.library.main.tab.tab.InternalTabbedPane;
import soars.common.utility.swing.file.manager.IFileManager;
import soars.common.utility.swing.file.manager.IFileManagerCallBack;

/**
 * @author kurata
 *
 */
public class NavigatorFileTable extends FileTable {

	/**
	 * @param fileManagerCallBack
	 * @param fileManager 
	 * @param component
	 * @param owner
	 * @param parent
	 */
	public NavigatorFileTable(IFileManagerCallBack fileManagerCallBack, IFileManager fileManager, Component component, Frame owner, Component parent) {
		super(fileManagerCallBack, fileManager, component, owner, parent);
	}

	/**
	 * @param navigatorDirectoryTree
	 * @param internalTabbedPane
	 * @return
	 */
	public boolean setup(NavigatorDirectoryTree navigatorDirectoryTree, InternalTabbedPane internalTabbedPane) {
		return super.setup( new NavigatorFileTableSpecificRowRenderer( this, navigatorDirectoryTree), navigatorDirectoryTree, internalTabbedPane);
	}
}
