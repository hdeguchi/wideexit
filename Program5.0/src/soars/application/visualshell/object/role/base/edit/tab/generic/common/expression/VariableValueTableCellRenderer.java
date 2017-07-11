/*
 * Created on 2005/10/31
 */
package soars.application.visualshell.object.role.base.edit.tab.generic.common.expression;

import java.awt.Color;
import java.awt.Component;

import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;

import soars.application.visualshell.object.role.base.Role;

/**
 * @author kurata
 */
public class VariableValueTableCellRenderer extends JLabel implements TableCellRenderer {

	/**
	 * 
	 */
	private Color _color = null;

	/**
	 * 
	 */
	private Role _role = null;

	/**
	 * @param color
	 * @param role
	 * 
	 */
	public VariableValueTableCellRenderer(Color color, Role role) {
		super();
		_color = color;
		_role = role;
	}

	/* (Non Javadoc)
	 * @see javax.swing.table.TableCellRenderer#getTableCellRendererComponent(javax.swing.JTable, java.lang.Object, boolean, boolean, int, int)
	 */
	@Override
	public Component getTableCellRendererComponent(JTable arg0, Object arg1, boolean arg2, boolean arg3, int arg4, int arg5) {

		setHorizontalAlignment( JLabel.RIGHT);

		String text = "";
		if ( null != arg1 && arg1 instanceof Variable) {
			Variable variable = ( Variable)arg1;
			switch ( arg5) {
				case 0:
					text = variable._name;
					break;
				case 1:
					text = variable._entityVariableRule.get_cell_text( _role, false);
					break;
			}
		}

		setOpaque( true);

		if ( !arg0.isEnabled())
			setEnabled( false);
		else {
			setForeground( arg2 ? Color.white : _color);
			setEnabled( true);
		}

		//setForeground( arg2 ? Color.white : _color);
		setBackground( arg2 ? _color : arg0.getBackground());

//		if ( arg3 || arg2) {
//			setForeground( Color.white);
//			setBackground( _color);
//		} else {
//			setForeground( _color);
//			setBackground( arg0.getBackground());
//		}

		setText( text);
		return this;
	}
}
