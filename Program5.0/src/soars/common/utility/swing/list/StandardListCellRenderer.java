/*
 * 2005/05/01
 */
package soars.common.utility.swing.list;

import java.awt.Component;

import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.ListCellRenderer;

/**
 * @author kurata
 */
public class StandardListCellRenderer extends JLabel implements ListCellRenderer {

	/**
	 * 
	 */
	public StandardListCellRenderer() {
		super();
	}

	/* (Non Javadoc)
	 * @see javax.swing.ListCellRenderer#getListCellRendererComponent(javax.swing.JList, java.lang.Object, int, boolean, boolean)
	 */
	public Component getListCellRendererComponent(
		JList arg0,
		Object arg1,
		int arg2,
		boolean arg3,
		boolean arg4) {
		String text = ( null == arg1) ? "" : arg1.toString();
		setOpaque( true);
		setForeground( arg3 ? arg0.getSelectionForeground() : arg0.getForeground());
		setBackground( arg3 ? arg0.getSelectionBackground() : arg0.getBackground());
		setText( text);
		return this;
	}
}
