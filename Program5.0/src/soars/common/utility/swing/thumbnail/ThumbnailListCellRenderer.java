/**
 * 
 */
package soars.common.utility.swing.thumbnail;

import java.awt.Component;

import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.ListCellRenderer;

/**
 * The cell renderer for the thumbnail list.
 * @author kurata / SOARS project
 */
public class ThumbnailListCellRenderer extends JPanel implements 	ListCellRenderer {

	/**
	 * Creates the cell renderer for the thumbnail list.
	 */
	public ThumbnailListCellRenderer() {
		super();
	}

	/* (non-Javadoc)
	 * @see javax.swing.ListCellRenderer#getListCellRendererComponent(javax.swing.JList, java.lang.Object, int, boolean, boolean)
	 */
	public Component getListCellRendererComponent(JList arg0, Object arg1, int arg2, boolean arg3, boolean arg4) {
		ThumbnailItem thumbnailItem = ( ThumbnailItem)arg1;
		thumbnailItem.set_border( arg0, arg2, arg3, arg4);
		return thumbnailItem;
	}
}
