/*
 * Created on 2005/10/28
 */
package soars.application.visualshell.object.expression.edit;

import java.awt.Component;
import java.awt.SystemColor;

import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;

import soars.common.utility.tool.expression.Expression;

/**
 * The renderer for ExpressionTable class.
 * @author kurata / SOARS project
 */
public class ExpressionTableCellRenderer extends JLabel implements TableCellRenderer {

	/**
	 * Cerates a new ExpressionTableCellRenderer.
	 */
	public ExpressionTableCellRenderer() {
		super();
	}

	/* (Non Javadoc)
	 * @see javax.swing.table.TableCellRenderer#getTableCellRendererComponent(javax.swing.JTable, java.lang.Object, boolean, boolean, int, int)
	 */
	public Component getTableCellRendererComponent(JTable arg0, Object arg1, boolean arg2, boolean arg3, int arg4, int arg5) {

		setOpaque( true);
		setForeground( arg2 ? SystemColor.textHighlightText : SystemColor.textText);
		setBackground( arg2 ? SystemColor.textHighlight : SystemColor.text);

		if ( null == arg1) {
			setText( "");
			return this;
		}

		Expression expression = ( Expression)arg1;

		if ( 0 == arg0.convertColumnIndexToModel( arg5))
			setText( expression._value[ 0] + "(" + expression._value[ 1] + ")");
		else
			setText( expression._value[ 2]);

		return this;
	}
}
