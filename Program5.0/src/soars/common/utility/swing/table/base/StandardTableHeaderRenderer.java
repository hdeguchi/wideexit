/*
 * 2005/02/23
 */
package soars.common.utility.swing.table.base;

import java.awt.Component;

import javax.swing.JTable;
import javax.swing.UIManager;
import javax.swing.border.Border;
import javax.swing.table.DefaultTableCellRenderer;

/**
 * @author kurata
 */
//public class StandardTableHeaderRenderer extends JLabel implements TableCellRenderer {
public class StandardTableHeaderRenderer extends DefaultTableCellRenderer {

	/**
	 * 
	 */
	public StandardTableHeaderRenderer() {
		super();
	}

	/* (non-Javadoc)
	 * @see javax.swing.table.DefaultTableCellRenderer#getTableCellRendererComponent(javax.swing.JTable, java.lang.Object, boolean, boolean, int, int)
	 */
	public Component getTableCellRendererComponent(JTable arg0, Object arg1, boolean arg2, boolean arg3, int arg4, int arg5) {
		String text = ( null == arg1) ? "" : arg1.toString();
		setOpaque( true);
		setBorder( ( Border)UIManager.getDefaults().get( "TableHeader.cellBorder"));
		setText( text);
		return this;
	}
}
