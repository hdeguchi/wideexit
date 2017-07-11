/**
 * 
 */
package soars.application.visualshell.object.entity.base.edit.panel.variable.panel.set_and_list.base.panel.table;

import java.awt.Color;
import java.awt.Component;

import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;

import soars.application.visualshell.object.entity.base.edit.panel.variable.panel.base.panel.table.InitialValueTableBase;
import soars.application.visualshell.object.entity.base.object.variable.VariableInitialValue;

/**
 * @author kurata
 *
 */
public class VariableInitialValueTableRowRenderer extends JLabel implements TableCellRenderer {

	/**
	 * 
	 */
	protected Color _color = null;

	/**
	 * @param color 
	 * 
	 */
	public VariableInitialValueTableRowRenderer(Color color) {
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
			VariableInitialValue variableInitialValue = ( VariableInitialValue)arg1;
			switch ( arg0.convertColumnIndexToModel( arg5)) {
				case 0:
					String name = InitialValueTableBase.__nameMap.get( variableInitialValue._type);
					if ( null != name)
						text = name;
					break;
				case 1:
					text = variableInitialValue._value;
					break;
			}
		}

		setOpaque( true);

		VariableInitialValueTable variableInitialValueTable = ( VariableInitialValueTable)arg0;
		if ( arg2) {
			setForeground( Color.white);
			setBackground( _color/*variableInitialValueTable.getSelectionBackground()*/);
		} else {
			setForeground( _color/*variableInitialValueTable.getForeground()*/);
			setBackground( variableInitialValueTable.getBackground());
		}

		setText( text);

		return this;
	}
}
