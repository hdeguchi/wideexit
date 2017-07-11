/**
 * 
 */
package soars.application.visualshell.object.gis.edit.table;

import java.awt.Color;
import java.awt.Component;

import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;

import soars.application.visualshell.object.gis.GisData;

/**
 * @author kurata
 *
 */
public class GisDataTableCellRenderer extends JLabel implements TableCellRenderer {

	/**
	 * 
	 */
	static private Color _coordinatesBackgroundColor = new Color( 255, 240, 240);

	/**
	 * 
	 */
	static private Color _integerBackgroundColor = new Color( 255, 255, 108);

	/**
	 * 
	 */
	static private Color _realNumberBackgroundColor = new Color( 108, 255, 255);

	/**
	 * 
	 */
	private GisData _gisData = null;

	/**
	 * @param gisData
	 */
	public GisDataTableCellRenderer(GisData gisData) {
		super();
		_gisData = gisData;
	}

	/* (non-Javadoc)
	 * @see javax.swing.table.TableCellRenderer#getTableCellRendererComponent(javax.swing.JTable, java.lang.Object, boolean, boolean, int, int)
	 */
	public Component getTableCellRendererComponent(JTable arg0, Object arg1, boolean arg2, boolean arg3, int arg4, int arg5) {
		if ( arg5 < _gisData._types.size())
			setHorizontalAlignment( _gisData._types.get( arg5).equals( "C") ? JLabel.LEFT : JLabel.RIGHT);
		else
			setHorizontalAlignment( JLabel.RIGHT);

		setOpaque( true);

		if ( arg0.hasFocus()) {
			if ( arg2) {
				setForeground( Color.white);
				setBackground( arg0.getSelectionBackground());
			} else {
				setForeground( arg0.getForeground());
				setBackground( get_back_ground_color( arg0, arg5));
			}
		} else {
			if ( arg2) {
				setForeground( Color.white);
				setBackground( arg0.getSelectionBackground());
			} else {
				setForeground( arg0.getForeground());
				setBackground( get_back_ground_color( arg0, arg5));
			}
		}

		setText( ( String)arg1);

		return this;
	}

	/**
	 * @param table
	 * @param column
	 * @return
	 */
	private Color get_back_ground_color(JTable table, int column) {
		if ( table.getColumnCount() - 2 <= column)
			return _coordinatesBackgroundColor;

		if ( _gisData._classes.get( column).equals( Integer.class)
			|| _gisData._classes.get( column).equals( Long.class)
			|| _gisData._classes.get( column).equals( Short.class)
			|| _gisData._classes.get( column).equals( Byte.class))
			return _integerBackgroundColor;
		else if ( _gisData._classes.get( column).equals( Double.class)
			|| _gisData._classes.get( column).equals( Float.class))
			return _realNumberBackgroundColor;

		return Color.white;
	}
}
