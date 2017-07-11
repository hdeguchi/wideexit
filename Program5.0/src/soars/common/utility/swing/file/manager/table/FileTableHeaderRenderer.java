/*
 * 2005/05/21
 */
package soars.common.utility.swing.file.manager.table;

import java.awt.Component;

import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.UIManager;
import javax.swing.border.Border;
import javax.swing.table.DefaultTableCellRenderer;

/**
 * @author kurata
 */
public class FileTableHeaderRenderer extends DefaultTableCellRenderer {

	/**
	 * 
	 */
	public FileTableHeaderRenderer() {
		super();
	}

	/* (non-Javadoc)
	 * @see javax.swing.table.DefaultTableCellRenderer#getTableCellRendererComponent(javax.swing.JTable, java.lang.Object, boolean, boolean, int, int)
	 */
	public Component getTableCellRendererComponent(JTable arg0, Object arg1, boolean arg2, boolean arg3, int arg4, int arg5) {

		if ( 1 == arg0.convertColumnIndexToModel( arg5))
			setHorizontalAlignment( JLabel.RIGHT);
		else
			setHorizontalAlignment( JLabel.LEFT);

		String text = ( null == arg1) ? "" : arg1.toString();

		setOpaque( true);
		setBorder( ( Border)UIManager.getDefaults().get( "TableHeader.cellBorder"));
		setText( text);

		return this;
	}
}
