/*
 * 2005/03/07
 */
package soars.common.utility.swing.table.base;

import java.awt.Component;

import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;

/**
 * @author kurata
 */
public class StandardTableRowRenderer extends JLabel implements TableCellRenderer {

	/**
	 * 
	 */
	public StandardTableRowRenderer() {
		super();
	}

	/* (Non Javadoc)
	 * @see javax.swing.table.TableCellRenderer#getTableCellRendererComponent(javax.swing.JTable, java.lang.Object, boolean, boolean, int, int)
	 */
	public Component getTableCellRendererComponent(
		JTable arg0,
		Object arg1,
		boolean arg2,
		boolean arg3,
		int arg4,
		int arg5) {
		String text = ( null == arg1) ? "" : arg1.toString();
		setOpaque( true);
		setForeground( arg2 ? arg0.getSelectionForeground() : arg0.getForeground());
		setBackground( arg2 ? arg0.getSelectionBackground() : arg0.getBackground());
		setText( text);
		return this;
	}
}
