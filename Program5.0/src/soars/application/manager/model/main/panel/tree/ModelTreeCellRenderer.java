/**
 * 
 */
package soars.application.manager.model.main.panel.tree;

import java.awt.Component;
import java.io.File;
import java.util.HashMap;
import java.util.Map;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.TreePath;

import soars.application.manager.model.main.Constant;
import soars.application.manager.model.main.ResourceManager;
import soars.application.manager.model.main.panel.tree.property.NodeProperty;

/**
 * @author kurata
 *
 */
public class ModelTreeCellRenderer extends DefaultTreeCellRenderer {

	/**
	 * 
	 */
	private Map<File, NodeProperty> _nodePropertyMap = new HashMap<File, NodeProperty>();

	/**
	 * 
	 */
	private Icon _directoryOpenIcon = null;

	/**
	 * 
	 */
	private Icon _directoryCloseIcon = null;

	/**
	 * 
	 */
	private Icon _fileIcon = null;

	/**
	 * 
	 */
	private Map<File, String> _rootDirectoryNameMap = new HashMap<File, String>();

	/**
	 * @param nodePropertyMap 
	 * @param rootDirectoryNameMap 
	 */
	public ModelTreeCellRenderer(Map<File, NodeProperty> nodePropertyMap, Map<File, String> rootDirectoryNameMap) {
		super();
		_nodePropertyMap = nodePropertyMap;
		_directoryOpenIcon = new ImageIcon( getClass().getResource( Constant._resourceDirectory + "/image/directory_open.png"));
		_directoryCloseIcon = new ImageIcon( getClass().getResource( Constant._resourceDirectory + "/image/directory_close.png"));
		_fileIcon = new ImageIcon( getClass().getResource( Constant._resourceDirectory + "/image/file.png"));
		_rootDirectoryNameMap = rootDirectoryNameMap;
	}

	/* (non-Javadoc)
	 * @see javax.swing.tree.DefaultTreeCellRenderer#getTreeCellRendererComponent(javax.swing.JTree, java.lang.Object, boolean, boolean, boolean, int, boolean)
	 */
	public Component getTreeCellRendererComponent(JTree arg0, Object arg1, boolean arg2, boolean arg3, boolean arg4, int arg5, boolean arg6) {

		ModelTree modelTree = ( ModelTree)arg0;
		super.getTreeCellRendererComponent(arg0, arg1, arg2 || arg1 == modelTree._dropTargetTreeNode, arg3, arg4, arg5, arg6);
		//super.getTreeCellRendererComponent(arg0, arg1, arg2, arg3, arg4, arg5, arg6);

		DefaultMutableTreeNode defaultMutableTreeNode = ( DefaultMutableTreeNode)arg1;
		Object object = defaultMutableTreeNode.getUserObject();

		if ( null == object)
			setText( "unknown");
		else {
			if ( !( object instanceof File)) {
				if ( defaultMutableTreeNode.isRoot())
					setText( "");
				else
					setText( "unknown");
			} else {
				File file = ( File)object;
				DefaultMutableTreeNode parent = ( DefaultMutableTreeNode)defaultMutableTreeNode.getParent();
				if ( !parent.isRoot()) {
					if ( file.isDirectory())
						setText( file.getName());
					else {
						NodeProperty nodeProperty = _nodePropertyMap.get( file);
						if ( null == nodeProperty || null == nodeProperty._title)
							setText( ResourceManager.get_instance().get( "model.tree.no.title"));
							//setText( "unknown");
						else
							setText( nodeProperty._title);
					}
				} else {
					String name = _rootDirectoryNameMap.get( file);
					setText( ( null != name) ? name : "unknown");
				}
			}
		}

		if ( !defaultMutableTreeNode.isRoot()) {
			File file = ( File)object;
			if ( file.isFile())
				setIcon( _fileIcon);
			else {
				TreePath treePath = arg0.getSelectionPath();
				if ( null != treePath)
					setIcon( defaultMutableTreeNode.equals( treePath.getLastPathComponent()) ? _directoryOpenIcon : _directoryCloseIcon);
				else
					setIcon( _directoryCloseIcon);
			}
		}

		return this;
	}
}
