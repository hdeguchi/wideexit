/**
 * 
 */
package soars.application.visualshell.object.gis.edit.panel.variable;

import java.awt.Color;
import java.awt.Component;

import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;

import soars.application.visualshell.main.ResourceManager;
import soars.application.visualshell.object.gis.object.base.GisObjectBase;
import soars.application.visualshell.object.gis.object.map.GisMapInitialValue;
import soars.application.visualshell.object.gis.object.map.GisMapObject;
import soars.application.visualshell.object.gis.object.variable.GisVariableInitialValue;
import soars.application.visualshell.object.gis.object.variable.GisVariableObject;

/**
 * @author kurata
 *
 */
public class GisVariableTableRowRenderer extends JLabel implements TableCellRenderer {

	/**
	 * 
	 */
	public GisVariableTableRowRenderer() {
		super();
	}

	/* (non-Javadoc)
	 * @see javax.swing.table.TableCellRenderer#getTableCellRendererComponent(javax.swing.JTable, java.lang.Object, boolean, boolean, int, int)
	 */
	@Override
	public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {

		setOpaque( true);

		String text = "";
		if ( null != value && value instanceof GisObjectBase) {
			GisObjectBase gisObjectBase = ( GisObjectBase)value;
			switch ( table.convertColumnIndexToModel( column)) {
				case 0:
					if ( value instanceof GisVariableObject && gisObjectBase._kind.equals( "collection"))
						text = ResourceManager.get_instance().get( "edit.object.dialog.tree.collection");
					else if ( value instanceof GisVariableObject && gisObjectBase._kind.equals( "list"))
						text = ResourceManager.get_instance().get( "edit.object.dialog.tree.list");
					else if ( value instanceof GisMapObject)
						text = ResourceManager.get_instance().get( "edit.object.dialog.tree.map");
//					else if ( value instanceof GisExchangeAlgebraObject)
//						text = ResourceManager.get_instance().get( "edit.object.dialog.tree.exchange.algebra");
					break;
				case 1:
					text = gisObjectBase._name;
					break;
				case 2:
					if ( value instanceof GisVariableObject) {
						for ( GisVariableInitialValue gisVariableInitialValue:( ( GisVariableObject)gisObjectBase)._gisVariableInitialValues)
							text += ( ( text.equals( "") ? "" : " ") + "[" + gisVariableInitialValue._value + "]");
					} else if ( value instanceof GisMapObject) {
						for ( GisMapInitialValue gisMapInitialValue:( ( GisMapObject)gisObjectBase)._gisMapInitialValues)
							text += ( ( text.equals( "") ? "" : " ") + "["
								+ ( gisMapInitialValue._key[ 0].equals( "immediate") ? "\"" : "") + gisMapInitialValue._key[ 1] + ( gisMapInitialValue._key[ 0].equals( "immediate") ? "\"" : "")
								+ " - "
								+ gisMapInitialValue._value[ 1] + "]");
//					} else if ( value instanceof GisExchangeAlgebraObject) {
//						text = ( ( GisExchangeAlgebraObject)gisObjectBase).get_initial_values();
					}
					break;
				case 3:
					text = gisObjectBase._comment;
			}

			if ( value instanceof GisVariableObject && gisObjectBase._kind.equals( "collection")) {
				setForeground( isSelected ? Color.white : GisVariablePropertyPanel.get_colorMap().get( ResourceManager.get_instance().get( "edit.object.dialog.tree.collection")));
				setBackground( isSelected ? GisVariablePropertyPanel.get_colorMap().get( ResourceManager.get_instance().get( "edit.object.dialog.tree.collection")) : table.getBackground());
			} else if ( value instanceof GisVariableObject && gisObjectBase._kind.equals( "list")) {
				setForeground( isSelected ? Color.white : GisVariablePropertyPanel.get_colorMap().get( ResourceManager.get_instance().get( "edit.object.dialog.tree.list")));
				setBackground( isSelected ? GisVariablePropertyPanel.get_colorMap().get( ResourceManager.get_instance().get( "edit.object.dialog.tree.list")) : table.getBackground());
			} else if ( value instanceof GisMapObject) {
				setForeground( isSelected ? Color.white : GisVariablePropertyPanel.get_colorMap().get( ResourceManager.get_instance().get( "edit.object.dialog.tree.map")));
				setBackground( isSelected ? GisVariablePropertyPanel.get_colorMap().get( ResourceManager.get_instance().get( "edit.object.dialog.tree.map")) : table.getBackground());
//			} else if ( value instanceof GisExchangeAlgebraObject) {
//				setForeground( isSelected ? Color.white : GisVariablePropertyPanel.get_colorMap().get( ResourceManager.get_instance().get( "edit.object.dialog.tree.exchange.algebra")));
//				setBackground( isSelected ? GisVariablePropertyPanel.get_colorMap().get( ResourceManager.get_instance().get( "edit.object.dialog.tree.exchange.algebra")) : table.getBackground());
			}
		}

		setText( text);

		return this;
	}
}
