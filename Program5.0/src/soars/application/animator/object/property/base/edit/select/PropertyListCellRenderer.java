/*
 * 2005/03/10
 */
package soars.application.animator.object.property.base.edit.select;

import java.awt.Component;

import javax.swing.JCheckBox;
import javax.swing.JList;
import javax.swing.ListCellRenderer;

/**
 * The renderer for properties list class.
 * @author kurata / SOARS project
 */
public class PropertyListCellRenderer extends JCheckBox implements ListCellRenderer {

	/**
	 * Creates a new PropertyListCellRenderer.
	 */
	public PropertyListCellRenderer() {
		super();
	}

	/* (Non Javadoc)
	 * @see javax.swing.ListCellRenderer#getListCellRendererComponent(javax.swing.JList, java.lang.Object, int, boolean, boolean)
	 */
	public Component getListCellRendererComponent(JList arg0, Object arg1, int arg2, boolean arg3, 	boolean arg4) {
		setOpaque( false);
		JCheckBox checkBox = ( JCheckBox)arg1;
		setText( checkBox.getText());
		setSelected( checkBox.isSelected());
		return this;
	}
}
