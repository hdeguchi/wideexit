/*
 * 2005/06/01
 */
package soars.application.visualshell.object.log.edit.option;

import java.awt.Component;
import java.awt.SystemColor;

import javax.swing.JCheckBox;
import javax.swing.JList;
import javax.swing.ListCellRenderer;

/**
 * The renderer for LogOptionList class.
 * @author kurata / SOARS project
 */
public class LogOptionListCellRenderer extends JCheckBox implements ListCellRenderer {

	/**
	 * Cerates a new LogOptionListCellRenderer.
	 */
	public LogOptionListCellRenderer() {
		super();
	}

	/* (Non Javadoc)
	 * @see javax.swing.ListCellRenderer#getListCellRendererComponent(javax.swing.JList, java.lang.Object, int, boolean, boolean)
	 */
	public Component getListCellRendererComponent(JList arg0, Object arg1, int arg2, boolean arg3, boolean arg4) {
		setOpaque( true);
		setForeground( SystemColor.textText);
		setBackground( SystemColor.text);
		if ( null != arg1 && arg1 instanceof JCheckBox) {
			JCheckBox checkBox = ( JCheckBox)arg1;
			setText( checkBox.getText());
			setSelected( checkBox.isSelected());
		}
		return this;
	}
}
