/**
 * 
 */
package soars.application.visualshell.object.entity.base.edit.panel.variable.panel.exchange_algebra.table;

import java.awt.Color;
import java.awt.Component;

import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;

import soars.application.visualshell.object.entity.base.object.exchange_algebra.ExchangeAlgebraInitialValue;

/**
 * @author kurata
 *
 */
public class ExchangeAlgebraInitialValueTableRowRenderer extends JLabel implements TableCellRenderer {

	/**
	 * 
	 */
	protected Color _color = null;

	/**
	 * @param color 
	 * 
	 */
	public ExchangeAlgebraInitialValueTableRowRenderer(Color color) {
		super();
		_color = color;
	}

	/* (non-Javadoc)
	 * @see javax.swing.table.TableCellRenderer#getTableCellRendererComponent(javax.swing.JTable, java.lang.Object, boolean, boolean, int, int)
	 */
	@Override
	public Component getTableCellRendererComponent(JTable arg0, Object arg1, boolean arg2, boolean arg3, int arg4, int arg5) {
		String text = "";
		if ( null != arg1) {
			ExchangeAlgebraInitialValue exchangeAlgebraInitialValue = ( ExchangeAlgebraInitialValue)arg1;
			switch ( arg0.convertColumnIndexToModel( arg5)) {
				case 0:
					text = exchangeAlgebraInitialValue.get_initial_value();
					break;
			}
		}

		setOpaque( true);

		ExchangeAlgebraInitialValueTable exchangeAlgebraInitialValueTable = ( ExchangeAlgebraInitialValueTable)arg0;
		if ( arg2) {
			setForeground( Color.white);
			setBackground( _color/*exchangeAlgebraInitialValueTable.getSelectionBackground()*/);
		} else {
			setForeground( _color/*exchangeAlgebraInitialValueTable.getForeground()*/);
			setBackground( exchangeAlgebraInitialValueTable.getBackground());
		}

		setText( text);

		return this;
	}
}
