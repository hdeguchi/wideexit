/**
 * 
 */
package soars.application.visualshell.object.experiment.edit.table;

import java.awt.Color;
import java.awt.Component;

import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;

/**
 * @author kurata
 *
 */
public class ExperimentRowHeaderTableCellRenderer extends JLabel implements TableCellRenderer {

	/**
	 * 
	 */
	public ExperimentRowHeaderTableCellRenderer() {
	}

	/* (non-Javadoc)
	 * @see javax.swing.table.TableCellRenderer#getTableCellRendererComponent(javax.swing.JTable, java.lang.Object, boolean, boolean, int, int)
	 */
	@Override
	public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
		if ( 0 == table.convertColumnIndexToModel( column))
			setHorizontalAlignment( JLabel.CENTER);
		else
			setHorizontalAlignment( JLabel.LEFT);

		setOpaque( true);

		ExperimentRowHeaderTable experimentRowHeaderTable = ( ExperimentRowHeaderTable)table;

		if ( experimentRowHeaderTable._spreadSheetTableBase.copied( row)) {
			setForeground( Color.white);
			setBackground( Color.red);
		} else {
				if ( isSelected) {
					setForeground( Color.white);
					setBackground( Color.blue);
				} else {
					setForeground( experimentRowHeaderTable.getForeground());
					setBackground( ( 0 == table.convertColumnIndexToModel( column))
						? Color.lightGray
						: experimentRowHeaderTable.getBackground());
				}
		}

		setText( ( 0 >= row) ? "" : String.valueOf( row));

		return this;
	}
}
