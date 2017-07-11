/**
 * 
 */
package soars.application.visualshell.object.chart.edit;

import java.awt.Component;
import java.awt.SystemColor;

import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;

import soars.application.visualshell.main.ResourceManager;
import soars.application.visualshell.object.chart.NumberObjectData;

/**
 * The renderer for NumberObjectDataPairTable class.
 * @author kurata / SOARS project
 */
public class NumberObjectDataPairTableRowRenderer extends JLabel implements TableCellRenderer {

	/**
	 * Creates a new NumberObjectDataPairTable.
	 */
	public NumberObjectDataPairTableRowRenderer() {
		super();
	}

	/* (non-Javadoc)
	 * @see javax.swing.table.TableCellRenderer#getTableCellRendererComponent(javax.swing.JTable, java.lang.Object, boolean, boolean, int, int)
	 */
	public Component getTableCellRendererComponent(JTable arg0, Object arg1, boolean arg2, boolean arg3, int arg4, int arg5) {
		String text = "";
		if ( null != arg1) {
			NumberObjectData numberObjectData = ( NumberObjectData)arg1;
			if ( numberObjectData._type.equals( "step"))
				text = ( "[" + ResourceManager.get_instance().get( "edit.chart.dialog.number.object.pairs.table.type.step") + "]");
			else {
				text = ( numberObjectData._type.equals( "agent")
					?	( "[" + ResourceManager.get_instance().get( "edit.chart.dialog.number.object.pairs.table.type.agent") + "]")
					: ( "[" + ResourceManager.get_instance().get( "edit.chart.dialog.number.object.pairs.table.type.spot") + "]"));
				text += ( " " + numberObjectData._objectName + " - " + numberObjectData._numberVariable);
			}
		}

		setOpaque( true);
		setForeground( arg2 ? SystemColor.textHighlightText : SystemColor.textText);
		setBackground( arg2 ? SystemColor.textHighlight : SystemColor.text);
		setText( text);

		return this;
	}
}
