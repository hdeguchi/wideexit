/**
 * 
 */
package soars.library.arbitrary.test001.normal_distribution;

import java.awt.Component;

import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.UIManager;
import javax.swing.border.Border;
import javax.swing.table.DefaultTableCellRenderer;

/**
 * @author kurata
 *
 */
//public class NormalDistributionTableHeaderRenderer extends JLabel implements TableCellRenderer {
public class NormalDistributionTableHeaderRenderer extends DefaultTableCellRenderer {

	/**
	 * 
	 */
	public NormalDistributionTableHeaderRenderer() {
		super();
	}

	/* (non-Javadoc)
	 * @see javax.swing.table.DefaultTableCellRenderer#getTableCellRendererComponent(javax.swing.JTable, java.lang.Object, boolean, boolean, int, int)
	 */
	public Component getTableCellRendererComponent(JTable arg0, Object arg1, boolean arg2, boolean arg3, int arg4, int arg5) {

		setOpaque( true);

		setBorder( ( Border)UIManager.getDefaults().get( "TableHeader.cellBorder"));

		setHorizontalAlignment( JLabel.RIGHT);

		if ( 0 == arg5)
			setText( "");
		else {
			String data = String.valueOf( 0.01 * ( arg5 - 1));
			if ( 4 < data.length())
				data = data.substring( 0, 4);

			if ( data.equals( "0.0"))
				data = "0.00";

			setText( data);
		}

		return this;
	}

//	/* (non-Javadoc)
//	 * @see javax.swing.table.TableCellRenderer#getTableCellRendererComponent(javax.swing.JTable, java.lang.Object, boolean, boolean, int, int)
//	 */
//	public Component getTableCellRendererComponent(JTable arg0, Object arg1,
//		boolean arg2, boolean arg3, int arg4, int arg5) {
//
//		setOpaque( true);
//
//		setBorder( ( Border)UIManager.getDefaults().get( "TableHeader.cellBorder"));
//
//		setHorizontalAlignment( JLabel.RIGHT);
//
//		if ( 0 == arg5)
//			setText( "");
//		else {
//			String data = String.valueOf( 0.01 * ( arg5 - 1));
//			if ( 4 < data.length())
//				data = data.substring( 0, 4);
//
//			if ( data.equals( "0.0"))
//				data = "0.00";
//
//			setText( data);
//		}
//
//		return this;
//	}
}
