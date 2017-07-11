/**
 * 
 */
package soars.application.manager.library.main.tab.module.tree;

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
import soars.common.soars.environment.BasicEnvironment;
import soars.common.soars.module.Module;
import soars.common.utility.swing.file.manager.tree.DirectoryTreeCellRendererBase;

/**
 * @author kurata
 *
 */
public class ModuleDirectoryTreeCellRenderer extends DirectoryTreeCellRendererBase {

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
	public ModuleDirectoryTreeCellRenderer() {
		super();
		_directoryOpenIcon = new ImageIcon( getClass().getResource( Constant._resourceDirectory + "/image/directory_open.png"));
		_directoryCloseIcon = new ImageIcon( getClass().getResource( Constant._resourceDirectory + "/image/directory_close.png"));
		_moduleDirectoryOpenIcon = new ImageIcon( getClass().getResource( Constant._resourceDirectory + "/image/module_directory_open.png"));
		_moduleDirectoryCloseIcon = new ImageIcon( getClass().getResource( Constant._resourceDirectory + "/image/module_directory_close.png"));
		_nameMap.put( Constant._systemModuleDirectory, ResourceManager.get_instance().get( "directory.tree.system.root"));

		File userModuleDirectory = BasicEnvironment.get_instance().get_projectSubFoler( Constant._userModuleDirectoryName);
		if ( null != userModuleDirectory)
			_nameMap.put( userModuleDirectory.getAbsolutePath(), ResourceManager.get_instance().get( "directory.tree.user.root"));
	}

	/* (non-Javadoc)
	 * @see soars.common.utility.swing.file.manager.tree.DirectoryTreeCellRendererBase#set(javax.swing.JTree, javax.swing.tree.DefaultMutableTreeNode, java.lang.Object)
	 */
	protected void set(JTree tree, DefaultMutableTreeNode defaultMutableTreeNode, Object object) {
		Icon openIcon = _directoryOpenIcon;
		Icon closeIcon = _directoryCloseIcon;
		if ( null == object)
			setText( "unknown");
		else {
			if ( !( object instanceof File)) {
				if ( defaultMutableTreeNode.isRoot())
					setText( "");
				else {
					DefaultMutableTreeNode parent = ( DefaultMutableTreeNode)defaultMutableTreeNode.getParent();
					if ( parent.isRoot() && object instanceof String)
						setText( _nameMap.get( ( String)object));
					else
						setText( "unknown");
				}
			} else {
				File directory = ( File)object;
				DefaultMutableTreeNode parent = ( DefaultMutableTreeNode)defaultMutableTreeNode.getParent();
				if ( parent.getUserObject() instanceof File)
					setText( directory.getName());
				else {
					ModuleDirectoryTree moduleDirectoryTree = ( ModuleDirectoryTree)tree;
					Module module = moduleDirectoryTree.get_module( directory);
					setText( ( null != module) ? module._name : directory.getName());
					openIcon = _moduleDirectoryOpenIcon;
					closeIcon = _moduleDirectoryCloseIcon;
				}
			}

			on_setIcon( tree, defaultMutableTreeNode, object, openIcon, closeIcon);
		}
	}

	/**
	 * @param tree
	 * @param defaultMutableTreeNode
	 * @param object
	 * @param openIcon
	 * @param closeIcon
	 */
	private void on_setIcon(JTree tree, DefaultMutableTreeNode defaultMutableTreeNode, Object object, Icon openIcon, Icon closeIcon) {
		TreePath treePath = tree.getSelectionPath();
		if ( null != treePath)
			setIcon( defaultMutableTreeNode.equals( treePath.getLastPathComponent()) ? openIcon : closeIcon);
		else
			setIcon( closeIcon);
	}
}
