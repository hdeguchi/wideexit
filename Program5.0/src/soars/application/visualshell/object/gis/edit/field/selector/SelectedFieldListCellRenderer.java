/**
 * 
 */
package soars.application.visualshell.object.gis.edit.field.selector;

import java.awt.Component;
import java.awt.SystemColor;

import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.ListCellRenderer;

/**
 * @author kurata
 *
 */
public class SelectedFieldListCellRenderer extends JLabel implements ListCellRenderer {

	/**
	 * 
	 */
	public SelectedFieldListCellRenderer() {
		super();
	}

	/* (non-Javadoc)
	 * @see javax.swing.ListCellRenderer#getListCellRendererComponent(javax.swing.JList, java.lang.Object, int, boolean, boolean)
	 */
	@Override
	public Component getListCellRendererComponent(JList arg0, Object arg1, int arg2, boolean arg3, boolean arg4) {
		setOpaque( true);
		setForeground( arg3 ? SystemColor.textHighlightText : SystemColor.textText);
		setBackground( arg3 ? SystemColor.textHighlight : SystemColor.text);
		if ( null != arg1 && arg1 instanceof Field) {
			Field field = ( Field)arg1;
			setText( ( field._flag ? "" : "\"") + field._value + ( field._flag ? "" : "\""));
		}
		return this;
	}
}
