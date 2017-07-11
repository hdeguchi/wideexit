/**
 * 
 */
package soars.application.visualshell.object.entity.base.edit.panel.variable.panel.base.panel.table;

import java.awt.Color;
import java.awt.Component;

import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;

/**
 * @author kurata
 *
 */
public class RowHeaderTableCellRenderer extends JLabel implements TableCellRenderer {

	/**
	 * 
	 */
	protected Color _color = null;

	/**
	 * @param color 
	 * 
	 */
	public RowHeaderTableCellRenderer(Color color) {
		super();
		_color = color;
	}

	/* (non-Javadoc)
	 * @see javax.swing.table.TableCellRenderer#getTableCellRendererComponent(javax.swing.JTable, java.lang.Object, boolean, boolean, int, int)
	 */
	@Override
	public Component getTableCellRendererComponent(JTable arg0, Object arg1, boolean arg2, boolean arg3, int arg4, int arg5) {
		if ( 0 == arg0.convertColumnIndexToModel( arg5))
			setHorizontalAlignment( JLabel.CENTER);
		else
			setHorizontalAlignment( JLabel.LEFT);

		setOpaque( true);

		RowHeaderTable rowHeaderTable = ( RowHeaderTable)arg0;

		if ( arg2) {
			setForeground( Color.white);
			//setBackground( Color.blue);
			setBackground( _color/*rowHeaderTable.getSelectionBackground()*/);
		} else {
			setForeground( _color/*rowHeaderTable.getForeground()*/);
			setBackground( ( 0 == arg0.convertColumnIndexToModel( arg5))
				? Color.lightGray
				: rowHeaderTable.getBackground());
		}

		setText( String.valueOf( arg4 + 1));

		return this;
	}
}
