/*
 * 2005/03/07
 */
package soars.application.animator.object.entity.base.edit.objects;

import java.awt.Color;
import java.awt.Component;
import java.awt.SystemColor;

import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;

import soars.application.animator.object.entity.base.EntityProperty;

/**
 * The renderer for EntityTable class.
 * @author kurata / SOARS project
 */
public class EntityTableCellRenderer extends JLabel implements TableCellRenderer {

	/**
	 * Creates a new EntityTableCellRenderer.
	 */
	public EntityTableCellRenderer() {
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

		if ( 7 == arg0.convertColumnIndexToModel( arg5))
			setHorizontalAlignment( JLabel.RIGHT);
		else
			setHorizontalAlignment( JLabel.LEFT);

		switch ( arg0.convertColumnIndexToModel( arg5)) {
			case 0:
				setOpaque( true);
				setForeground( arg2 ? SystemColor.textHighlightText : SystemColor.textText);
				setBackground( arg2 ? SystemColor.textHighlight : SystemColor.text);
				EntityProperty entityProperty = ( EntityProperty)arg1;
				setText( entityProperty._name);
			case 1:
			case 2:
				break;
			case 3:
			case 4:
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
