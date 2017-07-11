/*
 * 2005/07/13
 */
package soars.application.visualshell.object.experiment.edit.table;

import java.awt.Color;
import java.awt.Component;

import javax.swing.JCheckBox;
import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;

/**
 * The renderer for ExperimentTable class.
 * @author kurata / SOARS project
 */
public class CheckBoxTableCellRenderer extends JCheckBox implements TableCellRenderer {

	/**
	 * Background color for the selected cell.
	 */
	static public Color _selectedBackgroundColor = new Color( 255, 255, 108);

	/**
	 * Creates a new CheckBoxTableCellRenderer.
	 */
	public CheckBoxTableCellRenderer() {
		super();
	}

	/* (Non Javadoc)
	 * @see javax.swing.table.TableCellRenderer#getTableCellRendererComponent(javax.swing.JTable, java.lang.Object, boolean, boolean, int, int)
	 */
	public Component getTableCellRendererComponent(JTable arg0, Object arg1, boolean arg2, boolean arg3, int arg4, int arg5) {

		setOpaque( true);

		JCheckBox checkBox = ( JCheckBox)arg1;

		if ( arg2 && arg0.getSelectedColumn() == arg5) {
			setForeground( Color.white);
			setBackground( arg0.getSelectionBackground());
		} else {
			setForeground( arg0.getForeground());
			if ( 0 == arg4)
				setBackground( Color.lightGray);
			else
				setBackground( checkBox.isSelected() ? _selectedBackgroundColor : arg0.getBackground());
				//setBackground( experimentTable.getBackground());
		}

		setText( checkBox.getText());
		setSelected( checkBox.isSelected());
		//setEnabled( 0 != arg4);
		//setVisible( 0 != arg4);
		return this;
	}
}
