/**
 * 
 */
package soars.application.visualshell.object.entity.base.edit.panel.extransfer;

import java.awt.Component;
import java.awt.SystemColor;

import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;

import soars.application.visualshell.object.entity.base.object.extransfer.ExTransferObject;

/**
 * @author kurata
 *
 */
public class ExTransferTableRowRenderer extends JLabel implements TableCellRenderer {

	/**
	 * 
	 */
	public ExTransferTableRowRenderer() {
		super();
	}

	/* (non-Javadoc)
	 * @see javax.swing.table.TableCellRenderer#getTableCellRendererComponent(javax.swing.JTable, java.lang.Object, boolean, boolean, int, int)
	 */
	@Override
	public Component getTableCellRendererComponent(JTable arg0, Object arg1, boolean arg2, boolean arg3, int arg4, int arg5) {
		String text = "";
		if ( null != arg1) {
			ExTransferObject exTransferObject = ( ExTransferObject)arg1;

			switch ( arg0.convertColumnIndexToModel( arg5)) {
				case 0:
					text = exTransferObject._name;
					break;
				case 1:
					text = exTransferObject._initialValue;
					break;
				case 2:
					text = exTransferObject._comment;
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
