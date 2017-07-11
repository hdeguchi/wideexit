/**
 * 
 */
package soars.application.visualshell.object.entity.base.edit.panel.variable.panel.map.table;

import java.awt.Color;
import java.awt.Component;

import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;

import soars.application.visualshell.object.entity.base.edit.panel.variable.panel.base.panel.table.InitialValueTableBase;
import soars.application.visualshell.object.entity.base.object.map.MapInitialValue;

/**
 * @author kurata
 *
 */
public class MapInitialValueTableRowRenderer extends JLabel implements TableCellRenderer {

	/**
	 * 
	 */
	protected Color _color = null;

	/**
	 * @param color 
	 * 
	 */
	public MapInitialValueTableRowRenderer(Color color) {
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
			MapInitialValue mapInitialValue = ( MapInitialValue)arg1;
			switch ( arg0.convertColumnIndexToModel( arg5)) {
				case 0:
					text = ( ( mapInitialValue._key[ 0].equals( "immediate") ? "\"" : "") + mapInitialValue._key[ 1] + ( mapInitialValue._key[ 0].equals( "immediate") ? "\"" : ""));
					break;
				case 1:
					String name = InitialValueTableBase.__nameMap.get( mapInitialValue._value[ 0]);
					if ( null != name)
						text = name;
					break;
				case 2:
					text = mapInitialValue._value[ 1];
					break;
			}
		}

		setOpaque( true);

		MapInitialValueTable mapInitialValueTable = ( MapInitialValueTable)arg0;
		if ( arg2) {
			setForeground( Color.white);
			setBackground( _color/*mapInitialValueTable.getSelectionBackground()*/);
		} else {
			setForeground( _color/*mapInitialValueTable.getForeground()*/);
			setBackground( mapInitialValueTable.getBackground());
		}

		setText( text);

		return this;
	}
}
