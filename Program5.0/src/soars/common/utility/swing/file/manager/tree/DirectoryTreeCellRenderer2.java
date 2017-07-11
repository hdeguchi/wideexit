/**
 * 
 */
package soars.common.utility.swing.file.manager.tree;

import java.io.File;

import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;

import soars.common.utility.swing.file.manager.ResourceManager;

/**
 * @author kurata
 *
 */
public class DirectoryTreeCellRenderer2 extends DirectoryTreeCellRendererBase {

	/**
	 * 
	 */
	public DirectoryTreeCellRenderer2() {
		super();
	}

	/* (non-Javadoc)
	 * @see soars.common.utility.swing.file.manager.tree.DirectoryTreeCellRendererBase#set(javax.swing.JTree, javax.swing.tree.DefaultMutableTreeNode, java.lang.Object)
	 */
	protected void set(JTree tree, DefaultMutableTreeNode defaultMutableTreeNode, Object object) {
		if ( null == object)
			setText( "unknown");
		else {
			if ( !( object instanceof File)) {
				if ( defaultMutableTreeNode.isRoot())
					setText( ResourceManager.get_instance().get( "directory.tree.root.label"));
				else
					setText( "unknown");
			} else {
				File directory = ( File)object;
				setText( directory.getName());
			}

			on_setIcon( tree, defaultMutableTreeNode, object);
		}
	}
}
