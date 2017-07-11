/*
 * 2005/03/09
 */
package soars.application.animator.common.font;

import java.awt.Component;
import java.awt.SystemColor;

import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.ListCellRenderer;

/**
 * The renderer for font sizes combo box.
 * @author kurata / SOARS project
 */
public class FontSizeListCellRenderer extends JLabel implements ListCellRenderer {

	/**
	 * Creates a new FontSizeListCellRenderer.
	 */
	public FontSizeListCellRenderer() {
		super();
	}

	/* (Non Javadoc)
	 * @see javax.swing.ListCellRenderer#getListCellRendererComponent(javax.swing.JList, java.lang.Object, int, boolean, boolean)
	 */
	public Component getListCellRendererComponent(JList arg0, Object arg1, int arg2, boolean arg3, boolean arg4) {
		setOpaque( true);
		setForeground( arg3 ? SystemColor.textHighlightText : SystemColor.textText);
		setBackground( arg3 ? SystemColor.textHighlight : SystemColor.text);
		setHorizontalAlignment( JLabel.RIGHT);
		setText( arg1.toString());
		validate();
		return this;
	}
}
