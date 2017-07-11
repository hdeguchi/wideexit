/**
 * 
 */
package soars.application.manager.library.main.tab.module.table;

import java.awt.Component;
import java.awt.Frame;

import soars.application.manager.library.main.tab.common.table.FileTable;
import soars.application.manager.library.main.tab.tab.InternalTabbedPane;
import soars.common.utility.swing.file.manager.IFileManager;
import soars.common.utility.swing.file.manager.IFileManagerCallBack;
import soars.common.utility.swing.file.manager.tree.DirectoryTreeBase;

/**
 * @author kurata
 *
 */
public class ModuleFileTable extends FileTable {

	/**
	 * @param fileManagerCallBack
	 * @param fileManager
	 * @param component
	 * @param owner
	 * @param parent
	 */
	public ModuleFileTable(IFileManagerCallBack fileManagerCallBack, IFileManager fileManager, Component component, Frame owner, Component parent) {
		super(fileManagerCallBack, fileManager, component, owner, parent);
	}

	/**
	 * @param directoryTreeBase
	 * @param internalTabbedPane
	 * @return
	 */
	public boolean setup(DirectoryTreeBase directoryTreeBase, InternalTabbedPane internalTabbedPane) {
		return super.setup(new ModuleFileTableSpecificRowRenderer( this), directoryTreeBase, internalTabbedPane);
	}
}
