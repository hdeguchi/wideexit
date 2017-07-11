/*
 * 2005/02/23
 */
package soars.application.animator.object.property.base.edit;

import java.awt.Component;

import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.UIManager;
import javax.swing.border.Border;
import javax.swing.table.DefaultTableCellRenderer;

/**
 * The renderer for the header of PropertyTable class.
 * @author kurata / SOARS project
 */
//public class PropertyTableHeaderRenderer extends JLabel implements TableCellRenderer {
public class PropertyTableHeaderRenderer extends DefaultTableCellRenderer {

	/**
	 * Creates a new PropertyTableHeaderRenderer.
	 */
	public PropertyTableHeaderRenderer() {
		super();
	}

	/* (non-Javadoc)
	 * @see javax.swing.table.DefaultTableCellRenderer#getTableCellRendererComponent(javax.swing.JTable, java.lang.Object, boolean, boolean, int, int)
	 */
	public Component getTableCellRendererComponent(JTable arg0, Object arg1, boolean arg2, boolean arg3, int arg4, int arg5) {
		setOpaque( true);
		setBorder( ( Border)UIManager.getDefaults().get( "TableHeader.cellBorder"));

		if ( 5 == arg0.convertColumnIndexToModel( arg5))
			setHorizontalAlignment( JLabel.RIGHT);
		else
			setHorizontalAlignment( JLabel.LEFT);

		setText( arg1.toString());
		return this;
	}

//	/* (Non Javadoc)
//	 * @see javax.swing.table.TableCellRenderer#getTableCellRendererComponent(javax.swing.JTable, java.lang.Object, boolean, boolean, int, int)
//	 */
//	public Component getTableCellRendererComponent(JTable arg0, Object arg1, boolean arg2, boolean arg3, int arg4, int arg5) {
//		setOpaque( true);
//		setBorder( ( Border)UIManager.getDefaults().get( "TableHeader.cellBorder"));
//
//		if ( 5 == arg0.convertColumnIndexToModel( arg5))
//			setHorizontalAlignment( JLabel.RIGHT);
//		else
//			setHorizontalAlignment( JLabel.LEFT);
//
//		setText( arg1.toString());
//		return this;
//	}
}
