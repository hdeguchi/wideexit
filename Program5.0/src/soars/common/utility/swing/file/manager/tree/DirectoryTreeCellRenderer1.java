/**
 * 
 */
package soars.common.utility.swing.file.manager.tree;

import java.io.File;

import javax.swing.ImageIcon;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;

import soars.common.utility.swing.file.manager.Constant;
import soars.common.utility.swing.file.manager.ResourceManager;

/**
 * @author kurata
 *
 */
public class DirectoryTreeCellRenderer1 extends DirectoryTreeCellRendererBase {

	/**
	 * 
	 */
	public DirectoryTreeCellRenderer1() {
		super();
		setOpenIcon( new ImageIcon( getClass().getResource( Constant._resourceDirectory + "/image/directory_open.png")));
		setClosedIcon( new ImageIcon( getClass().getResource( Constant._resourceDirectory + "/image/directory_close.png")));
	}

	/* (non-Javadoc)
	 * @see soars.common.utility.swing.file.manager.tree.DirectoryTreeCellRendererBase#set(javax.swing.JTree, javax.swing.tree.DefaultMutableTreeNode, java.lang.Object)
	 */
	protected void set(JTree tree, DefaultMutableTreeNode defaultMutableTreeNode, Object object) {
		if ( null == object || !( object instanceof File))
			setText( "unknown");
		else {
			if ( defaultMutableTreeNode.isRoot())
				setText( ResourceManager.get_instance().get( "directory.tree.root.label"));
			else {
				File directory = ( File)object;
				setText( directory.getName());
			}

			on_setIcon( tree, defaultMutableTreeNode, object);
		}
	}
}
