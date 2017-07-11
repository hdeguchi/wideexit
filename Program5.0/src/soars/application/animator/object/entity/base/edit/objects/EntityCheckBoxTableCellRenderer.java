/*
 * 2005/03/15
 */
package soars.application.animator.object.entity.base.edit.objects;

import java.awt.Component;
import java.awt.SystemColor;

import javax.swing.JCheckBox;
import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;

/**
 * The renderer for CheckBox in EntityTable class.
 * @author kurata / SOARS project
 */
public class EntityCheckBoxTableCellRenderer extends JCheckBox implements TableCellRenderer {

	/**
	 * Creates a new ObjectCheckBoxTableCellRenderer.
	 */
	public EntityCheckBoxTableCellRenderer() {
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

		setOpaque( true);

		setForeground( arg2 ? SystemColor.textHighlightText : SystemColor.textText);
		setBackground( arg2 ? SystemColor.textHighlight : SystemColor.text);

		JCheckBox checkBox = ( JCheckBox)arg1;
		setText( checkBox.getText());
		setSelected( checkBox.isSelected());

		return this;
	}
}
