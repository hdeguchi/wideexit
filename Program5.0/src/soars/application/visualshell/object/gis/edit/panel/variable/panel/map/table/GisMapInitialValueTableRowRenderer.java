/**
 * 
 */
package soars.application.visualshell.object.gis.edit.panel.variable.panel.map.table;

import java.awt.Color;
import java.awt.Component;

import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;

import soars.application.visualshell.main.ResourceManager;
import soars.application.visualshell.object.gis.edit.panel.variable.panel.base.panel.table.GisInitialValueTableBase;
import soars.application.visualshell.object.gis.object.base.GisInitialValueBase;
import soars.application.visualshell.object.gis.object.map.GisMapInitialValue;

/**
 * @author kurata
 *
 */
public class GisMapInitialValueTableRowRenderer extends JLabel implements TableCellRenderer {

	/**
	 * 
	 */
	protected Color _color = null;

	/**
	 * @param color 
	 * 
	 */
	public GisMapInitialValueTableRowRenderer(Color color) {
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
			GisMapInitialValue gisMapInitialValue = ( GisMapInitialValue)arg1;
			switch ( arg0.convertColumnIndexToModel( arg5)) {
				case 0:
					if ( gisMapInitialValue._key[ 0].equals( "immediate"))
						text = ( "\"" + gisMapInitialValue._key[ 1] + "\"");
					else if ( gisMapInitialValue._key[ 0].equals( "keyword"))
						text = gisMapInitialValue._key[ 1];
					else if ( gisMapInitialValue._key[ 0].equals( "field"))
						text = ( gisMapInitialValue._key[ 1] + "[" + ResourceManager.get_instance().get( "edit.gis.data.initial.value.field") + "-" + GisInitialValueBase.__nameMap.get( gisMapInitialValue._key[ 2]) + "]");
					break;
				case 1:
					String name = null;
					if ( gisMapInitialValue._value[ 0].equals( "field"))
						name = ( ResourceManager.get_instance().get( "edit.gis.data.initial.value.field") + "(" + GisInitialValueBase.__nameMap.get( gisMapInitialValue._value[ 2]) + ")");
					else
						name = GisInitialValueTableBase.get_nameMap().get( gisMapInitialValue._value[ 0]);
					if ( null != name)
						text = name;
					break;
				case 2:
					text = gisMapInitialValue._value[ 1];
					break;
			}
		}

		setOpaque( true);

		GisMapInitialValueTable gisMapInitialValueTable = ( GisMapInitialValueTable)arg0;
		if ( arg2) {
			setForeground( Color.white);
			setBackground( _color/*gisMapInitialValueTable.getSelectionBackground()*/);
		} else {
			setForeground( _color/*gisMapInitialValueTable.getForeground()*/);
			setBackground( gisMapInitialValueTable.getBackground());
		}

		setText( text);

		return this;
	}
}
