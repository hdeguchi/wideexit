/**
 * 
 */
package soars.common.utility.swing.file.manager.tree;

import java.awt.Component;

import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.TreePath;

/**
 * @author kurata
 *
 */
public class DirectoryTreeCellRendererBase extends DefaultTreeCellRenderer {

	/**
	 * 
	 */
	public DirectoryTreeCellRendererBase() {
		super();
	}

	/* (non-Javadoc)
	 * @see javax.swing.tree.DefaultTreeCellRenderer#getTreeCellRendererComponent(javax.swing.JTree, java.lang.Object, boolean, boolean, boolean, int, boolean)
	 */
	public Component getTreeCellRendererComponent(JTree arg0, Object arg1, boolean arg2, boolean arg3, boolean arg4, int arg5, boolean arg6) {

		DirectoryTreeBase directoryTreeBase = ( DirectoryTreeBase)arg0;
		super.getTreeCellRendererComponent(arg0, arg1, arg2 || arg1 == directoryTreeBase._dropTargetTreeNode, arg3, arg4, arg5, arg6);

		DefaultMutableTreeNode defaultMutableTreeNode = ( DefaultMutableTreeNode)arg1;
		Object object = defaultMutableTreeNode.getUserObject();
		set( arg0, defaultMutableTreeNode, object);

		return this;
	}

	/**
	 * @param tree
	 * @param defaultMutableTreeNode
	 * @param object
	 */
	protected void set(JTree tree, DefaultMutableTreeNode defaultMutableTreeNode, Object object) {
	}

	/**
	 * @param tree
	 * @param defaultMutableTreeNode
	 * @param object
	 */
	protected void on_setIcon(JTree tree, DefaultMutableTreeNode defaultMutableTreeNode, Object object) {
		if ( defaultMutableTreeNode.isLeaf()) {
			TreePath treePath = tree.getSelectionPath();
			if ( null != treePath)
				setIcon( defaultMutableTreeNode.equals( treePath.getLastPathComponent()) ? getOpenIcon() : getClosedIcon());
			else
				setIcon( getClosedIcon());
		}
	}
}
