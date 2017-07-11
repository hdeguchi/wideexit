/*
 * Created on 2006/06/26
 */
package soars.application.visualshell.plugin.edit;

import java.awt.Component;
import java.awt.SystemColor;

import javax.swing.JCheckBox;
import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;

/**
 * @author kurata
 */
public class PluginTableChekBoxCellRenderer extends JCheckBox implements TableCellRenderer {

	/**
	 * 
	 */
	public PluginTableChekBoxCellRenderer() {
		super();
	}

	/* (Non Javadoc)
	 * @see javax.swing.table.TableCellRenderer#getTableCellRendererComponent(javax.swing.JTable, java.lang.Object, boolean, boolean, int, int)
	 */
	public Component getTableCellRendererComponent(JTable arg0, Object arg1, boolean arg2, boolean arg3, int arg4, int arg5) {

		setOpaque( true);

		setForeground( arg2 ? SystemColor.textHighlightText : SystemColor.textText);
		setBackground( arg2 ? SystemColor.textHighlight : SystemColor.text);

		JCheckBox checkBox = ( JCheckBox)arg1;
		setText( checkBox.getText());
		setSelected( checkBox.isSelected());

		return this;
	}
}
