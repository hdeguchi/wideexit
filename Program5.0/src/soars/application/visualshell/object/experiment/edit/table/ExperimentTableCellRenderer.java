/*
 * 2005/07/12
 */
package soars.application.visualshell.object.experiment.edit.table;

import java.awt.Color;
import java.awt.Component;

import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;

/**
 * The renderer for ExperimentTable class.
 * @author kurata / SOARS project
 */
public class ExperimentTableCellRenderer extends JLabel implements TableCellRenderer {

	/**
	 * Cerates a new ExperimentTableCellRenderer.
	 */
	public ExperimentTableCellRenderer() {
		super();
	}

	/* (Non Javadoc)
	 * @see javax.swing.table.TableCellRenderer#getTableCellRendererComponent(javax.swing.JTable, java.lang.Object, boolean, boolean, int, int)
	 */
	public Component getTableCellRendererComponent(JTable arg0, Object arg1, boolean arg2, boolean arg3, int arg4, int arg5) {
		String text = ( String)arg1;

		setOpaque( true);

		ExperimentTable experimentTable = ( ExperimentTable)arg0;

		JCheckBox checkBox = ( JCheckBox)experimentTable.getValueAt( arg4, 0);

		if ( arg2) {
			setForeground( Color.white);
			setBackground( arg0.getSelectionBackground());
		} else {
			setForeground( arg0.getForeground());
			if ( 0 == arg4 && 3 > arg5)
				setBackground( Color.lightGray);
			else {
				setBackground( checkBox.isSelected() ? CheckBoxTableCellRenderer._selectedBackgroundColor : experimentTable.getBackground());
			}
		}

		setText( text);

		return this;
	}
}
