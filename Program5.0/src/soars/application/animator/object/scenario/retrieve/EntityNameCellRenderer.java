/*
 * 2005/03/29
 */
package soars.application.animator.object.scenario.retrieve;

import java.awt.Component;
import java.awt.SystemColor;

import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.ListCellRenderer;

import soars.application.animator.object.entity.base.EntityBase;

/**
 * The renderer for JComboBox ont the RetrievePropertyDlg class.
 * @author kurata / SOARS project
 */
public class EntityNameCellRenderer extends JLabel implements ListCellRenderer {

	/**
	 * Creates a new ObjectNameCellRenderer.
	 */
	public EntityNameCellRenderer() {
		super();
	}

	/* (Non Javadoc)
	 * @see javax.swing.ListCellRenderer#getListCellRendererComponent(javax.swing.JList, java.lang.Object, int, boolean, boolean)
	 */
	public Component getListCellRendererComponent(JList arg0, Object arg1, int arg2, boolean arg3, boolean arg4) {
		setOpaque( true);
		setForeground( arg3 ? SystemColor.textHighlightText : SystemColor.textText);
		setBackground( arg3 ? SystemColor.textHighlight : SystemColor.text);
		EntityBase entityBase = ( EntityBase)arg1;
		setText( entityBase._name);
		validate();
		return this;
	}
}
