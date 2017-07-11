/**
 * 
 */
package soars.application.manager.model.main.panel.tab.contents;

import java.awt.Color;
import java.awt.Component;

import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;

/**
 * @author kurata
 *
 */
public class SoarsContentsRowHeaderTableCellRenderer extends JLabel implements TableCellRenderer {

	/**
	 * 
	 */
	public SoarsContentsRowHeaderTableCellRenderer() {
		super();
	}

	/* (non-Javadoc)
	 * @see javax.swing.table.TableCellRenderer#getTableCellRendererComponent(javax.swing.JTable, java.lang.Object, boolean, boolean, int, int)
	 */
	public Component getTableCellRendererComponent(JTable arg0, Object arg1, boolean arg2, boolean arg3, int arg4, int arg5) {
		if ( 0 == arg0.convertColumnIndexToModel( arg5))
			setHorizontalAlignment( JLabel.CENTER);
		else
			setHorizontalAlignment( JLabel.LEFT);

		setOpaque( true);

		if ( arg2) {
			setForeground( Color.white);
			setBackground( Color.blue);
		} else {
			setForeground( arg0.getForeground());
			setBackground( ( 0 == arg0.convertColumnIndexToModel( arg5)) ? Color.lightGray : arg0.getBackground());
		}

		setText( String.valueOf( arg4 + 1));

		return this;
	}
}
