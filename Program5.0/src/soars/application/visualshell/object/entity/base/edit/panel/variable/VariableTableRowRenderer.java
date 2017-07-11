/**
 * 
 */
package soars.application.visualshell.object.entity.base.edit.panel.variable;

import java.awt.Color;
import java.awt.Component;

import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;

import soars.application.visualshell.main.ResourceManager;
import soars.application.visualshell.object.entity.base.object.base.ObjectBase;
import soars.application.visualshell.object.entity.base.object.exchange_algebra.ExchangeAlgebraObject;
import soars.application.visualshell.object.entity.base.object.map.MapInitialValue;
import soars.application.visualshell.object.entity.base.object.map.MapObject;
import soars.application.visualshell.object.entity.base.object.variable.VariableInitialValue;
import soars.application.visualshell.object.entity.base.object.variable.VariableObject;

/**
 * @author kurata
 *
 */
public class VariableTableRowRenderer extends JLabel implements TableCellRenderer {

	/**
	 * 
	 */
	public VariableTableRowRenderer() {
		super();
	}

	/* (non-Javadoc)
	 * @see javax.swing.table.TableCellRenderer#getTableCellRendererComponent(javax.swing.JTable, java.lang.Object, boolean, boolean, int, int)
	 */
	@Override
	public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {

		setOpaque( true);

		String text = "";
		if ( null != value && value instanceof ObjectBase) {
			ObjectBase objectBase = ( ObjectBase)value;
			switch ( table.convertColumnIndexToModel( column)) {
				case 0:
					if ( value instanceof VariableObject && objectBase._kind.equals( "collection"))
						text = ResourceManager.get_instance().get( "edit.object.dialog.tree.collection");
					else if ( value instanceof VariableObject && objectBase._kind.equals( "list"))
						text = ResourceManager.get_instance().get( "edit.object.dialog.tree.list");
					else if ( value instanceof MapObject)
						text = ResourceManager.get_instance().get( "edit.object.dialog.tree.map");
					else if ( value instanceof ExchangeAlgebraObject)
						text = ResourceManager.get_instance().get( "edit.object.dialog.tree.exchange.algebra");
					break;
				case 1:
					text = objectBase._name;
					break;
				case 2:
					if ( value instanceof VariableObject) {
						for ( VariableInitialValue variableInitialValue:( ( VariableObject)objectBase)._variableInitialValues)
							text += ( ( text.equals( "") ? "" : " ") + "[" + variableInitialValue._value + "]");
					} else if ( value instanceof MapObject) {
						for ( MapInitialValue mapInitialValue:( ( MapObject)objectBase)._mapInitialValues)
							text += ( ( text.equals( "") ? "" : " ") + "["
								+ ( mapInitialValue._key[ 0].equals( "immediate") ? "\"" : "") + mapInitialValue._key[ 1] + ( mapInitialValue._key[ 0].equals( "immediate") ? "\"" : "")
								+ " - "
								+ mapInitialValue._value[ 1] + "]");
					} else if ( value instanceof ExchangeAlgebraObject) {
						text = ( ( ExchangeAlgebraObject)objectBase).get_initial_values();
					}
					break;
				case 3:
					text = objectBase._comment;
			}

			if ( value instanceof VariableObject && objectBase._kind.equals( "collection")) {
				setForeground( isSelected ? Color.white : VariablePropertyPanel._colorMap.get( ResourceManager.get_instance().get( "edit.object.dialog.tree.collection")));
				setBackground( isSelected ? VariablePropertyPanel._colorMap.get( ResourceManager.get_instance().get( "edit.object.dialog.tree.collection")) : table.getBackground());
			} else if ( value instanceof VariableObject && objectBase._kind.equals( "list")) {
				setForeground( isSelected ? Color.white : VariablePropertyPanel._colorMap.get( ResourceManager.get_instance().get( "edit.object.dialog.tree.list")));
				setBackground( isSelected ? VariablePropertyPanel._colorMap.get( ResourceManager.get_instance().get( "edit.object.dialog.tree.list")) : table.getBackground());
			} else if ( value instanceof MapObject) {
				setForeground( isSelected ? Color.white : VariablePropertyPanel._colorMap.get( ResourceManager.get_instance().get( "edit.object.dialog.tree.map")));
				setBackground( isSelected ? VariablePropertyPanel._colorMap.get( ResourceManager.get_instance().get( "edit.object.dialog.tree.map")) : table.getBackground());
			} else if ( value instanceof ExchangeAlgebraObject) {
				setForeground( isSelected ? Color.white : VariablePropertyPanel._colorMap.get( ResourceManager.get_instance().get( "edit.object.dialog.tree.exchange.algebra")));
				setBackground( isSelected ? VariablePropertyPanel._colorMap.get( ResourceManager.get_instance().get( "edit.object.dialog.tree.exchange.algebra")) : table.getBackground());
			}
		}

		setText( text);

		return this;
	}
}
