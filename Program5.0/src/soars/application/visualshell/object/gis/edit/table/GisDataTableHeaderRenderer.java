/**
 * 
 */
package soars.application.visualshell.object.gis.edit.table;

import java.awt.Color;
import java.awt.Component;
import java.util.Arrays;

import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.UIManager;
import javax.swing.border.Border;
import javax.swing.table.DefaultTableCellRenderer;

import soars.application.visualshell.object.gis.GisData;

/**
 * @author kurata
 *
 */
public class GisDataTableHeaderRenderer extends DefaultTableCellRenderer {

	/**
	 * 
	 */
	private GisData _gisData = null;

	/**
	 * @param gisData
	 */
	public GisDataTableHeaderRenderer(GisData gisData) {
		super();
		_gisData = gisData;
	}

	/* (non-Javadoc)
	 * @see javax.swing.table.DefaultTableCellRenderer#getTableCellRendererComponent(javax.swing.JTable, java.lang.Object, boolean, boolean, int, int)
	 */
	public Component getTableCellRendererComponent(JTable arg0, Object arg1, boolean arg2, boolean arg3, int arg4, int arg5) {
		if ( arg5 < _gisData._types.size())
			setHorizontalAlignment( _gisData._types.get( arg5).equals( "C") ? JLabel.LEFT : JLabel.RIGHT);
		else
			setHorizontalAlignment( JLabel.RIGHT);

		setOpaque( true);

		setBorder( ( Border)UIManager.getDefaults().get( "TableHeader.cellBorder"));

		int[] columns = arg0.getSelectedColumns();
		Arrays.sort( columns);
		if ( 0 <= Arrays.binarySearch( columns, arg5)) {
			setForeground( Color.white/*Color.lightGray*/);
			setBackground( Color.blue/*ruleTable.getSelectionBackground()*/);
		} else {
			setForeground( arg0.getTableHeader().getForeground());
			setBackground( arg0.getTableHeader().getBackground());
		}

		setText( ( String)arg1);

		return this;
	}
}
