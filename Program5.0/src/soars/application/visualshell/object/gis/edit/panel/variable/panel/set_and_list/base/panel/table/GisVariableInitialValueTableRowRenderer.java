/**
 * 
 */
package soars.application.visualshell.object.gis.edit.panel.variable.panel.set_and_list.base.panel.table;

import java.awt.Color;
import java.awt.Component;

import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;

import soars.application.visualshell.main.ResourceManager;
import soars.application.visualshell.object.gis.edit.panel.variable.panel.base.panel.table.GisInitialValueTableBase;
import soars.application.visualshell.object.gis.object.base.GisInitialValueBase;
import soars.application.visualshell.object.gis.object.variable.GisVariableInitialValue;

/**
 * @author kurata
 *
 */
public class GisVariableInitialValueTableRowRenderer extends JLabel implements TableCellRenderer {

	/**
	 * 
	 */
	protected Color _color = null;

	/**
	 * @param color 
	 * 
	 */
	public GisVariableInitialValueTableRowRenderer(Color color) {
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
			GisVariableInitialValue gisVariableInitialValue = ( GisVariableInitialValue)arg1;
			switch ( arg0.convertColumnIndexToModel( arg5)) {
				case 0:
					if ( gisVariableInitialValue._type.equals( "field")) {
						String fieldType = GisInitialValueBase.__nameMap.get( gisVariableInitialValue._fieldType);
						if ( null != fieldType)
							text = ResourceManager.get_instance().get( "edit.gis.data.initial.value.field") + "(" + fieldType + ")";
					} else {
						String name = GisInitialValueTableBase.get_nameMap().get( gisVariableInitialValue._type);
						if ( null != name)
							text = name;
					}
					break;
				case 1:
					text = gisVariableInitialValue._value;
					break;
			}
		}

		setOpaque( true);

		GisVariableInitialValueTable gisVariableInitialValueTable = ( GisVariableInitialValueTable)arg0;
		if ( arg2) {
			setForeground( Color.white);
			setBackground( _color/*gisVariableInitialValueTable.getSelectionBackground()*/);
		} else {
			setForeground( _color/*gisVariableInitialValueTable.getForeground()*/);
			setBackground( gisVariableInitialValueTable.getBackground());
		}

		setText( text);

		return this;
	}
}
