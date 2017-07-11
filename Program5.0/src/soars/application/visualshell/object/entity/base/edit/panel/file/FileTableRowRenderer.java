/**
 * 
 */
package soars.application.visualshell.object.entity.base.edit.panel.file;

import java.awt.Component;
import java.awt.SystemColor;

import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;

import soars.application.visualshell.object.entity.base.object.file.FileObject;

/**
 * @author kurata
 *
 */
public class FileTableRowRenderer extends JLabel implements TableCellRenderer {

	/**
	 * 
	 */
	public FileTableRowRenderer() {
		super();
	}

	/* (non-Javadoc)
	 * @see javax.swing.table.TableCellRenderer#getTableCellRendererComponent(javax.swing.JTable, java.lang.Object, boolean, boolean, int, int)
	 */
	@Override
	public Component getTableCellRendererComponent(JTable arg0, Object arg1, boolean arg2, boolean arg3, int arg4, int arg5) {
		String text = "";
		if ( null != arg1) {
			FileObject fileObject = ( FileObject)arg1;

			switch ( arg0.convertColumnIndexToModel( arg5)) {
				case 0:
					text = fileObject._name;
					break;
				case 1:
					text = fileObject._initialValue;
					break;
				case 2:
					text = fileObject._comment;
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
