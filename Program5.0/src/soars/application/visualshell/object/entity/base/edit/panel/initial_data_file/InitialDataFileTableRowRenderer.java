/**
 * 
 */
package soars.application.visualshell.object.entity.base.edit.panel.initial_data_file;

import java.awt.Component;
import java.awt.SystemColor;

import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;

import soars.application.visualshell.object.entity.base.object.initial_data_file.InitialDataFileObject;

/**
 * @author kurata
 *
 */
public class InitialDataFileTableRowRenderer extends JLabel implements TableCellRenderer {

	/**
	 * 
	 */
	public InitialDataFileTableRowRenderer() {
		super();
	}

	/* (non-Javadoc)
	 * @see javax.swing.table.TableCellRenderer#getTableCellRendererComponent(javax.swing.JTable, java.lang.Object, boolean, boolean, int, int)
	 */
	@Override
	public Component getTableCellRendererComponent(JTable arg0, Object arg1, boolean arg2, boolean arg3, int arg4, int arg5) {
		String text = "";
		if ( null != arg1) {
			InitialDataFileObject initialDataFileObject = ( InitialDataFileObject)arg1;

			switch ( arg0.convertColumnIndexToModel( arg5)) {
				case 0:
					text = initialDataFileObject._name;
					break;
				case 1:
					text = initialDataFileObject._comment;
					break;
			}
		}

		setOpaque( true);
		setForeground( arg2 ? SystemColor.textHighlightText : SystemColor.textText);
		setBackground( arg2 ? SystemColor.textHighlight : SystemColor.text);
		setText( text);

		return this;
	}
}
