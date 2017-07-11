/*
 * 2005/06/24
 */
package soars.application.visualshell.object.role.base.edit.table;

import java.awt.Color;
import java.awt.Component;

import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;

/**
 * @author kurata
 */
public class RuleRowHeaderTableCellRenderer extends JLabel implements TableCellRenderer {

	/**
	 * 
	 */
	public RuleRowHeaderTableCellRenderer() {
		super();
	}

	/* (Non Javadoc)
	 * @see javax.swing.table.TableCellRenderer#getTableCellRendererComponent(javax.swing.JTable, java.lang.Object, boolean, boolean, int, int)
	 */
	@Override
	public Component getTableCellRendererComponent(JTable arg0, Object arg1, boolean arg2, boolean arg3, int arg4, int arg5) {

		if ( 0 == arg0.convertColumnIndexToModel( arg5))
			setHorizontalAlignment( JLabel.CENTER);
		else
			setHorizontalAlignment( JLabel.LEFT);

		setOpaque( true);

		RuleRowHeaderTable ruleRowHeaderTable = ( RuleRowHeaderTable)arg0;

		if ( ruleRowHeaderTable._spreadSheetTableBase.copied( arg4)) {
			setForeground( Color.white);
			setBackground( Color.red);
		} else {
//			if ( ruleRowHeaderTable._focus) {
				//if ( arg3 || ( arg2 && arg0.getSelectedColumn() == arg5)) {
				if ( arg2) {
					setForeground( Color.white);
					setBackground( Color.blue);
					//setBackground( ruleRowHeaderTable.getSelectionBackground());
				} else {
					setForeground( ruleRowHeaderTable.getForeground());
					setBackground( ( 0 == arg0.convertColumnIndexToModel( arg5))
						? Color.lightGray
						: ruleRowHeaderTable.getBackground());
				}
//			} else {
//				//if ( arg3 || arg2) {
//				if ( arg2 || ruleRowHeaderTable._roleTableBases[ 2].isRowSelected( arg4)) {
//					setForeground( Color.white);
//					setBackground( arg0.getSelectionBackground());
//				} else {
//					setForeground( arg0.getForeground());
//					setBackground( ( 0 == arg0.convertColumnIndexToModel( arg5))
//						? Color.lightGray
//						: arg0.getBackground());
//				}
//			}
		}

		setText( String.valueOf( arg4 + 1));

		return this;
	}
}
