/**
 * 
 */
package soars.application.manager.library.main.tab.directory.tree;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreePath;

import soars.application.manager.library.main.Constant;
import soars.application.manager.library.main.ResourceManager;
import soars.common.utility.swing.file.manager.tree.DirectoryTreeCellRenderer2;

/**
 * @author kurata
 *
 */
public class NavigatorDirectoryTreeCellRenderer extends DirectoryTreeCellRenderer2 {

	/**
	 * 
	 */
	protected Icon _directoryOpenIcon = null;

	/**
	 * 
	 */
	protected Icon _directoryCloseIcon = null;

	/**
	 * 
	 */
	protected Icon _moduleDirectoryOpenIcon = null;

	/**
	 * 
	 */
	protected Icon _moduleDirectoryCloseIcon = null;

	/**
	 * 
	 */
	protected Map<String, String> _nameMap = new HashMap<String, String>();

	/**
	 * 
	 */
	public NavigatorDirectoryTreeCellRenderer() {
		super();
		_directoryOpenIcon = new ImageIcon( getClass().getResource( Constant._resourceDirectory + "/image/directory_open.png"));
		_directoryCloseIcon = new ImageIcon( getClass().getResource( Constant._resourceDirectory + "/image/directory_close.png"));
		_moduleDirectoryOpenIcon = new ImageIcon( getClass().getResource( Constant._resourceDirectory + "/image/module_directory_open.png"));
		_moduleDirectoryCloseIcon = new ImageIcon( getClass().getResource( Constant._resourceDirectory + "/image/module_directory_close.png"));
		_nameMap.put( Constant._systemModuleDirectoryName, ResourceManager.get_instance().get( "directory.tree.system.root"));
		_nameMap.put( Constant._userModuleDirectoryName, ResourceManager.get_instance().get( "directory.tree.user.root"));
	}

	/* (non-Javadoc)
	 * @see soars.common.utility.swing.file.manager.tree.DirectoryTreeCellRenderer2#set(javax.swing.JTree, javax.swing.tree.DefaultMutableTreeNode, java.lang.Object)
	 */
	protected void set(JTree tree, DefaultMutableTreeNode defaultMutableTreeNode, Object object) {
		if ( null == object)
			setText( "unknown");
		else {
			if ( !( object instanceof File)) {
				if ( defaultMutableTreeNode.isRoot())
					setText( "");
				else
					setText( "unknown");
			} else {
				File directory = ( File)object;
				DefaultMutableTreeNode parent = ( DefaultMutableTreeNode)defaultMutableTreeNode.getParent();
				if ( !parent.isRoot())
					setText( directory.getName());
				else {
					String name = _nameMap.get( directory.getName());
					setText( ( null != name) ? name : "unknown");
				}
			}

			on_setIcon( tree, defaultMutableTreeNode, object);
		}
	}

	/* (non-Javadoc)
	 * @see soars.common.utility.swing.file.manager.tree.DirectoryTreeCellRendererBase#on_setIcon(javax.swing.JTree, javax.swing.tree.DefaultMutableTreeNode, java.lang.Object)
	 */
	protected void on_setIcon(JTree tree, DefaultMutableTreeNode defaultMutableTreeNode, Object object) {
		TreePath treePath = tree.getSelectionPath();
		if ( null != treePath)
			setIcon( getIcon( tree, object,
				defaultMutableTreeNode.equals( treePath.getLastPathComponent())
					? new Icon[] { _directoryOpenIcon, _moduleDirectoryOpenIcon} : new Icon[] { _directoryCloseIcon, _moduleDirectoryCloseIcon}));
		else
			setIcon( getIcon( tree, object, new Icon[] { _directoryCloseIcon, _moduleDirectoryCloseIcon}));
	}

	/**
	 * @param tree
	 * @param object
	 * @param icons
	 * @return
	 */
	private Icon getIcon(JTree tree, Object object, Icon[] icons) {
		if ( null == object || !( object instanceof File))
			return icons[ 0];

		File directory = ( File)object;
		if ( !directory.isDirectory())
			return icons[ 0];

		return icons[ is_module( tree, directory) ? 1 : 0];
	}

	/**
	 * @param tree
	 * @param directory
	 * @return
	 */
	private boolean is_module(JTree tree, File directory) {
		NavigatorDirectoryTree navigatorDirectoryTree = ( NavigatorDirectoryTree)tree;
		if ( navigatorDirectoryTree.is_root_directory( directory))
			return false;

		return ( null != navigatorDirectoryTree.get_module( directory, true));
	}
}
