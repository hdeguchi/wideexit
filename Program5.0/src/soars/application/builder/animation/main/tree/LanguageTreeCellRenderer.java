/**
 * 
 */
package soars.application.builder.animation.main.tree;

import java.awt.Component;

import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;

import soars.application.builder.animation.document.Document;

/**
 * @author kurata
 *
 */
public class LanguageTreeCellRenderer extends DefaultTreeCellRenderer {

	/**
	 * 
	 */
	public LanguageTreeCellRenderer() {
		super();
	}

	/* (non-Javadoc)
	 * @see javax.swing.tree.DefaultTreeCellRenderer#getTreeCellRendererComponent(javax.swing.JTree, java.lang.Object, boolean, boolean, boolean, int, boolean)
	 */
	public Component getTreeCellRendererComponent(JTree arg0, Object arg1,
		boolean arg2, boolean arg3, boolean arg4, int arg5, boolean arg6) {

		super.getTreeCellRendererComponent(arg0, arg1, arg2, arg3, arg4, arg5, 	arg6);

		DefaultMutableTreeNode defaultMutableTreeNode = ( DefaultMutableTreeNode)arg1;
		Object object = defaultMutableTreeNode.getUserObject();
		if ( null == object)
			setText( "unknown");
		else {
			if ( object instanceof String)
				setText( ( String)object);
			else if ( object instanceof Document) {
				Document document = ( Document)object;
				setText( document._language_name);
			} else
				setText( "unknown");
		}

		return this;
	}
}
