/*
 * Created on 2006/06/26
 */
package soars.application.visualshell.plugin.edit;

import java.awt.Component;

import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;

import soars.application.visualshell.plugin.Plugin;

/**
 * @author kurata
 */
public class PluginTableCellRenderer extends JLabel implements TableCellRenderer {

	/**
	 * 
	 */
	public PluginTableCellRenderer() {
		super();
	}

	/* (Non Javadoc)
	 * @see javax.swing.table.TableCellRenderer#getTableCellRendererComponent(javax.swing.JTable, java.lang.Object, boolean, boolean, int, int)
	 */
	public Component getTableCellRendererComponent(JTable arg0, Object arg1, boolean arg2, boolean arg3, int arg4, int arg5) {
		String text = "";
		if ( null != arg1 && arg1 instanceof Plugin) {
			Plugin plugin = ( Plugin)arg1;
			switch ( arg0.convertColumnIndexToModel( arg5)) {
				case 1:
					text = plugin.getVersion();
					break;
				case 2:
					text = plugin.get_comment();
					break;
			}
		}
		setOpaque( true);
		setForeground( arg2 ? arg0.getSelectionForeground() : arg0.getForeground());
		setBackground( arg2 ? arg0.getSelectionBackground() : arg0.getBackground());
		setText( text);
		return this;
	}
}
