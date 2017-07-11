/*
 * Created on 2005/10/31
 */
package soars.application.visualshell.object.role.base.edit.tab.legacy.common.number;

import java.awt.Color;
import java.awt.Component;

import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;

/**
 * @author kurata
 */
public class VariableValueTableCellRenderer extends JLabel implements TableCellRenderer {

	/**
	 * 
	 */
	private Color _color = null;

	/**
	 * @param color
	 * 
	 */
	public VariableValueTableCellRenderer(Color color) {
		super();
		_color = color;
	}

	/* (Non Javadoc)
	 * @see javax.swing.table.TableCellRenderer#getTableCellRendererComponent(javax.swing.JTable, java.lang.Object, boolean, boolean, int, int)
	 */
	@Override
	public Component getTableCellRendererComponent(JTable arg0, Object arg1, boolean arg2, boolean arg3, int arg4, int arg5) {

		setHorizontalAlignment( JLabel.RIGHT);

		String text = ( null == arg1) ? "" : ( String)arg1;

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
