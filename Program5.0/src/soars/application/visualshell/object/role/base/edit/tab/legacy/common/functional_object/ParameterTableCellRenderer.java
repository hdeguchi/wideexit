/**
 * 
 */
package soars.application.visualshell.object.role.base.edit.tab.legacy.common.functional_object;

import java.awt.Color;
import java.awt.Component;

import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;

/**
 * @author kurata
 *
 */
public class ParameterTableCellRenderer extends JLabel implements TableCellRenderer {

	/**
	 * 
	 */
	private Color _color = null;

	/**
	 * @param color
	 */
	public ParameterTableCellRenderer(Color color) {
		super();
		_color = color;
	}

	/* (non-Javadoc)
	 * @see javax.swing.table.TableCellRenderer#getTableCellRendererComponent(javax.swing.JTable, java.lang.Object, boolean, boolean, int, int)
	 */
	@Override
	public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
		setHorizontalAlignment( JLabel.RIGHT);

		String text = ( null == value) ? "" : ( String)value;
		switch ( column) {
			case 0:
				String[] words = text.split( "\\.");
				if ( null != words && 0 < words.length)
					text = words[ words.length - 1];

				break;
		}

		setOpaque( true);

		if ( !table.isEnabled())
			setEnabled( false);
		else {
			setForeground( isSelected ? Color.white : _color);
			setEnabled( true);
		}

		//setForeground( isSelected ? Color.white : _color);
		setBackground( isSelected ? _color : table.getBackground());

		setText( text);
		return this;
	}
}
