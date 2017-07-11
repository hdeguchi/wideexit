/*
 * 2005/03/07
 */
package soars.application.animator.object.property.base.edit;

import java.awt.Color;
import java.awt.Component;
import java.awt.SystemColor;

import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;

/**
 * The renderer for PropertyTable class.
 * @author kurata / SOARS project
 */
public class PropertyTableCellRenderer extends JLabel implements TableCellRenderer {

	/**
	 * Creates a new PropertyTableCellRenderer.
	 */
	public PropertyTableCellRenderer() {
		super();
	}

	/* (Non Javadoc)
	 * @see javax.swing.table.TableCellRenderer#getTableCellRendererComponent(javax.swing.JTable, java.lang.Object, boolean, boolean, int, int)
	 */
	public Component getTableCellRendererComponent(JTable arg0, Object arg1, boolean arg2, boolean arg3, int arg4, int arg5) {
		if ( 6 == arg0.convertColumnIndexToModel( arg5))
			setHorizontalAlignment( JLabel.RIGHT);
		else
			setHorizontalAlignment( JLabel.LEFT);

		switch ( arg0.convertColumnIndexToModel( arg5)) {
			case 1:
				break;
			case 2:
			case 3:
				Color color = ( Color)arg1;
				setOpaque( true);
				setBackground( color);
				break;
			default:
				setOpaque( true);
				setForeground( arg2 ? SystemColor.textHighlightText : SystemColor.textText);
				setBackground( arg2 ? SystemColor.textHighlight : SystemColor.text);
				setText( arg1.toString());
				break;
		}
		return this;
	}
}
