/**
 * 
 */
package soars.library.arbitrary.test001.normal_distribution;

import java.awt.Component;
import java.awt.Font;

import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;

/**
 * @author kurata
 *
 */
public class NormalDistributionTableCellRenderer extends JLabel implements TableCellRenderer {


	/**
	 * 
	 */
	static private Font _plain_font = null;

	/**
	 * 
	 */
	static private Font _bold_font = null;

	/**
	 * 
	 */
	public NormalDistributionTableCellRenderer() {
		super();
	}

	/* (non-Javadoc)
	 * @see javax.swing.table.TableCellRenderer#getTableCellRendererComponent(javax.swing.JTable, java.lang.Object, boolean, boolean, int, int)
	 */
	public Component getTableCellRendererComponent(JTable table, Object value,
		boolean isSelected, boolean hasFocus, int row, int column) {

		if ( null == _plain_font || null == _bold_font) {
			_plain_font = new Font( getFont().getName(), Font.PLAIN, getFont().getSize());
			_bold_font = new Font( getFont().getName(), Font.BOLD, getFont().getSize());
		}

		setOpaque( true);

		setFont( ( 0 == column) ? _bold_font : _plain_font);
		setHorizontalAlignment( ( 0 == column) ? JLabel.LEFT : JLabel.RIGHT);

		setText( ( String)value);

		return this;
	}
}
