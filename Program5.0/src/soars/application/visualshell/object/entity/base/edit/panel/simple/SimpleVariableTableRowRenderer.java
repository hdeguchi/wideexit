/**
 * 
 */
package soars.application.visualshell.object.entity.base.edit.panel.simple;

import java.awt.Color;
import java.awt.Component;

import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;

import soars.application.visualshell.main.ResourceManager;
import soars.application.visualshell.object.entity.base.object.base.SimpleVariableObject;
import soars.application.visualshell.object.entity.base.object.keyword.KeywordObject;
import soars.application.visualshell.object.entity.base.object.number.NumberObject;
import soars.application.visualshell.object.entity.base.object.probability.ProbabilityObject;
import soars.application.visualshell.object.entity.base.object.role.RoleVariableObject;
import soars.application.visualshell.object.entity.base.object.spot.SpotVariableObject;
import soars.application.visualshell.object.entity.base.object.time.TimeVariableObject;

/**
 * @author kurata
 *
 */
public class SimpleVariableTableRowRenderer extends JLabel implements TableCellRenderer {

	/**
	 * 
	 */
	public SimpleVariableTableRowRenderer() {
		super();
	}

	/* (non-Javadoc)
	 * @see javax.swing.table.TableCellRenderer#getTableCellRendererComponent(javax.swing.JTable, java.lang.Object, boolean, boolean, int, int)
	 */
	@Override
	public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {

		setOpaque( true);

		String text = "";
		if ( null != value && value instanceof SimpleVariableObject) {
			SimpleVariableObject simpleVariableObject = ( SimpleVariableObject)value;
			switch ( table.convertColumnIndexToModel( column)) {
				case 0:
					if ( value instanceof ProbabilityObject)
						text = ResourceManager.get_instance().get( "edit.object.dialog.tree.probability");
					else if ( value instanceof KeywordObject)
						text = ResourceManager.get_instance().get( "edit.object.dialog.tree.keyword");
					else if ( value instanceof NumberObject)
						text = ResourceManager.get_instance().get( "edit.object.dialog.tree.number.object");
					else if ( value instanceof RoleVariableObject)
						text = ResourceManager.get_instance().get( "edit.object.dialog.tree.role.variable");
					else if ( value instanceof TimeVariableObject)
						text = ResourceManager.get_instance().get( "edit.object.dialog.tree.time.variable");
					else if ( value instanceof SpotVariableObject)
						text = ResourceManager.get_instance().get( "edit.object.dialog.tree.spot.variable");
					break;
				case 1:
					text = simpleVariableObject._name;
					break;
				case 2:
					text = simpleVariableObject._initialValue;
					break;
				case 3:
					if ( simpleVariableObject instanceof NumberObject) {
						NumberObject numberObject = ( NumberObject)simpleVariableObject;
						text = NumberObject.get_type_name( numberObject._type);
					}
					break;
				case 4:
					text = simpleVariableObject._comment;
			}

			if ( value instanceof ProbabilityObject) {
				setForeground( isSelected ? Color.white : SimpleVariablePropertyPanel._colorMap.get( ResourceManager.get_instance().get( "edit.object.dialog.tree.probability")));
				setBackground( isSelected ? SimpleVariablePropertyPanel._colorMap.get( ResourceManager.get_instance().get( "edit.object.dialog.tree.probability")) : table.getBackground());
			} else if ( value instanceof KeywordObject) {
				setForeground( isSelected ? Color.white : SimpleVariablePropertyPanel._colorMap.get( ResourceManager.get_instance().get( "edit.object.dialog.tree.keyword")));
				setBackground( isSelected ? SimpleVariablePropertyPanel._colorMap.get( ResourceManager.get_instance().get( "edit.object.dialog.tree.keyword")) : table.getBackground());
			} else if ( value instanceof NumberObject) {
				setForeground( isSelected ? Color.white : SimpleVariablePropertyPanel._colorMap.get( ResourceManager.get_instance().get( "edit.object.dialog.tree.number.object")));
				setBackground( isSelected ? SimpleVariablePropertyPanel._colorMap.get( ResourceManager.get_instance().get( "edit.object.dialog.tree.number.object")) : table.getBackground());
			} else if ( value instanceof RoleVariableObject) {
				setForeground( isSelected ? Color.white : SimpleVariablePropertyPanel._colorMap.get( ResourceManager.get_instance().get( "edit.object.dialog.tree.role.variable")));
				setBackground( isSelected ? SimpleVariablePropertyPanel._colorMap.get( ResourceManager.get_instance().get( "edit.object.dialog.tree.role.variable")) : table.getBackground());
			} else if ( value instanceof TimeVariableObject) {
				setForeground( isSelected ? Color.white : SimpleVariablePropertyPanel._colorMap.get( ResourceManager.get_instance().get( "edit.object.dialog.tree.time.variable")));
				setBackground( isSelected ? SimpleVariablePropertyPanel._colorMap.get( ResourceManager.get_instance().get( "edit.object.dialog.tree.time.variable")) : table.getBackground());
			} else if ( value instanceof SpotVariableObject) {
				setForeground( isSelected ? Color.white : SimpleVariablePropertyPanel._colorMap.get( ResourceManager.get_instance().get( "edit.object.dialog.tree.spot.variable")));
				setBackground( isSelected ? SimpleVariablePropertyPanel._colorMap.get( ResourceManager.get_instance().get( "edit.object.dialog.tree.spot.variable")) : table.getBackground());
			}
//			setForeground( isSelected ? SystemColor.textHighlightText : SystemColor.textText);
//			setBackground( isSelected ? SystemColor.textHighlight : SystemColor.text);
		}

		setText( text);

		return this;
	}
}
