/**
 * 
 */
package soars.application.visualshell.object.gis.edit.tree;

import java.awt.Component;

import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;

import soars.application.visualshell.object.gis.edit.panel.base.GisPropertyPanelBase;

/**
 * @author kurata
 *
 */
public class GisObjectTreeCellRenderer extends DefaultTreeCellRenderer {

	/**
	 * 
	 */
	public GisObjectTreeCellRenderer() {
		super();
	}

	/* (non-Javadoc)
	 * @see javax.swing.tree.DefaultTreeCellRenderer#getTreeCellRendererComponent(javax.swing.JTree, java.lang.Object, boolean, boolean, boolean, int, boolean)
	 */
	@Override
	public Component getTreeCellRendererComponent(JTree arg0, Object arg1, boolean arg2, boolean arg3, boolean arg4, int arg5, boolean arg6) {
		super.getTreeCellRendererComponent(arg0, arg1, arg2, arg3, arg4, arg5, arg6);

		DefaultMutableTreeNode defaultMutableTreeNode = ( DefaultMutableTreeNode)arg1;
		Object object = defaultMutableTreeNode.getUserObject();
		if ( null == object)
			setText( "unknown");
		else {
			if ( object instanceof GisPropertyPanelBase) {
				GisPropertyPanelBase propertyPanelBase = ( GisPropertyPanelBase)object;
				setText( String.valueOf( propertyPanelBase._index + 1) + ". " + propertyPanelBase._title);
			}
		}

		return this;
	}
}
