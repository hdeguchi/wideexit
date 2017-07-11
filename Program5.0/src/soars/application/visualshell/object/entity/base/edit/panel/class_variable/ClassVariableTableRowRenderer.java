/**
 * 
 */
package soars.application.visualshell.object.entity.base.edit.panel.class_variable;

import java.awt.Component;
import java.awt.SystemColor;

import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;

import soars.application.visualshell.object.entity.base.object.class_variable.ClassVariableObject;

/**
 * @author kurata
 *
 */
public class ClassVariableTableRowRenderer extends JLabel implements TableCellRenderer {

	/**
	 * 
	 */
	public ClassVariableTableRowRenderer() {
		super();
	}

	/* (non-Javadoc)
	 * @see javax.swing.table.TableCellRenderer#getTableCellRendererComponent(javax.swing.JTable, java.lang.Object, boolean, boolean, int, int)
	 */
	@Override
	public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {

		String text = "";
		if ( null != value && value instanceof ClassVariableObject) {
			ClassVariableObject classVariableObject = ( ClassVariableObject)value;
			String[] words;
			switch ( column) {
				case 0:
					text = classVariableObject._name;
					break;
				case 1:
					words = classVariableObject._jarFilename.split( "/");
					if ( null != words && 0 < words.length)
						text = words[ words.length - 1];

					break;
				case 2:
					words = classVariableObject._classname.split( "\\.");
					if ( null != words && 0 < words.length)
						text = words[ words.length - 1];

					break;
				case 3:
					text = classVariableObject._comment;
					break;
				default:
					text = "";
				break;
			}
		}

		setOpaque( true);
		setForeground( isSelected ? SystemColor.textHighlightText : SystemColor.textText);
		setBackground( isSelected ? SystemColor.textHighlight : SystemColor.text);
		setText( text);

		return this;
	}
}
