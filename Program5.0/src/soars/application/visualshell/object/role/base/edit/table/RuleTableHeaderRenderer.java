/*
 * 2005/06/24
 */
package soars.application.visualshell.object.role.base.edit.table;

import java.awt.Color;
import java.awt.Component;
import java.util.Arrays;

import javax.swing.JTable;
import javax.swing.UIManager;
import javax.swing.border.Border;
import javax.swing.table.DefaultTableCellRenderer;

/**
 * @author kurata
 */
//public class RuleTableHeaderRenderer extends JLabel implements TableCellRenderer {
public class RuleTableHeaderRenderer extends DefaultTableCellRenderer {

	/**
	 * 
	 */
	public RuleTableHeaderRenderer() {
		super();
	}

	/* (non-Javadoc)
	 * @see javax.swing.table.DefaultTableCellRenderer#getTableCellRendererComponent(javax.swing.JTable, java.lang.Object, boolean, boolean, int, int)
	 */
	@Override
	public Component getTableCellRendererComponent(JTable arg0, Object arg1, boolean arg2, boolean arg3, int arg4, int arg5) {

		setOpaque( true);

		setBorder( ( Border)UIManager.getDefaults().get( "TableHeader.cellBorder"));

		RuleTable ruleTable = ( RuleTable)arg0;

//		if ( ruleTable.hasFocus()) {
			int[] columns = arg0.getSelectedColumns();
			Arrays.sort( columns);
			//System.out.println( arg0.getSelectedColumn());
			//for ( int column:columns)
			//	System.out.println( column);
			if ( 0 <= Arrays.binarySearch( columns, arg5)) {
//			if ( arg0.getSelectedColumn() == arg5) {
				setForeground( Color.white/*Color.lightGray*/);
				setBackground( Color.blue/*ruleTable.getSelectionBackground()*/);
				//Color color = getBackground();
				//System.out.println( color.getRed() + ", " + color.getGreen() + ", " + color.getBlue());
//				setForeground( Color.cyan);
//				setBackground( ruleTableBase.getSelectionBackground());
//				Color c = ruleTableBase.getSelectionBackground();
//				Color color = new Color( c.getRed(), c.getGreen(), c.getBlue());
//				setBackground( color);
//				System.out.println( c.getRed() + ", " + c.getGreen() + ", " + c.getBlue());
			} else {
				setForeground( ruleTable.getTableHeader().getForeground());
				setBackground( ruleTable.getTableHeader().getBackground());
			}
//		} else {
//			setForeground( ruleTable.getTableHeader().getForeground());
//			setBackground( ruleTable.getTableHeader().getBackground());
//		}

		String text = ( null == arg1) ? "" : arg1.toString();
		setText( text);

		return this;
	}

//	/* (Non Javadoc)
//	 * @see javax.swing.table.TableCellRenderer#getTableCellRendererComponent(javax.swing.JTable, java.lang.Object, boolean, boolean, int, int)
//	 */
//	@Override
//	public Component getTableCellRendererComponent(JTable arg0, Object arg1,
//		boolean arg2, boolean arg3, int arg4, int arg5) {
//
//		setOpaque( true);
//
//		setBorder( ( Border)UIManager.getDefaults().get( "TableHeader.cellBorder"));
//
//		RuleTableBase ruleTableBase = ( RuleTableBase)arg0;
//
//		if ( ruleTableBase._focus) {
//			if ( arg0.getSelectedColumn() == arg5) {
//				setForeground( Color.lightGray);
//				setBackground( ruleTableBase.getSelectionBackground());
//				//Color color = getBackground();
//				//System.out.println( color.getRed() + ", " + color.getGreen() + ", " + color.getBlue());
////				setForeground( Color.cyan);
////				setBackground( ruleTableBase.getSelectionBackground());
////				Color c = ruleTableBase.getSelectionBackground();
////				Color color = new Color( c.getRed(), c.getGreen(), c.getBlue());
////				setBackground( color);
////				System.out.println( c.getRed() + ", " + c.getGreen() + ", " + c.getBlue());
//			} else {
//				setForeground( ruleTableBase.getTableHeader().getForeground());
//				setBackground( ruleTableBase.getTableHeader().getBackground());
//			}
//		} else {
//			setForeground( ruleTableBase.getTableHeader().getForeground());
//			setBackground( ruleTableBase.getTableHeader().getBackground());
//		}
//
//		String text = ( null == arg1) ? "" : arg1.toString();
//		setText( text);
//
//		return this;
//	}
}
