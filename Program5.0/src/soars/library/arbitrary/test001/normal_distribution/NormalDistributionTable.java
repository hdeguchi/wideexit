/**
 * 
 */
package soars.library.arbitrary.test001.normal_distribution;

import javax.swing.JTable;
import javax.swing.table.DefaultTableColumnModel;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableColumn;

/**
 * @author kurata
 *
 */
public class NormalDistributionTable extends JTable {

	/**
	 * 
	 */
	public NormalDistributionTable() {
		super();
	}

	/**
	 * @return
	 */
	public boolean setup() {
		getTableHeader().setReorderingAllowed( false);
		setDefaultEditor( Object.class, null);


		JTableHeader tableHeader = getTableHeader();
		tableHeader.setDefaultRenderer( new NormalDistributionTableHeaderRenderer());


		DefaultTableModel defaultTableModel = ( DefaultTableModel)getModel();
		defaultTableModel.setColumnCount( 11);


		DefaultTableColumnModel defaultTableColumnModel = ( DefaultTableColumnModel)getColumnModel();
		for ( int i = 0; i < defaultTableColumnModel.getColumnCount(); ++i) {
			TableColumn tableColumn = defaultTableColumnModel.getColumn( i);
			defaultTableColumnModel.getColumn( i).setCellRenderer( new NormalDistributionTableCellRenderer());
		}

		String[] data = new String[ 11];
		for ( int i = 0; i < 11; ++i)
			data[ i] = "";

		for ( int i = 0; i < 31; ++i) {
			data[ 0] = String.valueOf( 0.1 * i);
			if ( 3 < data[ 0].length())
				data[ 0] = data[ 0].substring( 0, 3);

			defaultTableModel.addRow( data);
		}

		return true;
	}
}
