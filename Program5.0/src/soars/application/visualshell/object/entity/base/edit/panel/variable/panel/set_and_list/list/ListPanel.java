/**
 * 
 */
package soars.application.visualshell.object.entity.base.edit.panel.variable.panel.set_and_list.list;

import java.awt.Color;
import java.awt.Component;
import java.awt.Frame;
import java.util.Map;

import soars.application.visualshell.main.ResourceManager;
import soars.application.visualshell.object.entity.base.EntityBase;
import soars.application.visualshell.object.entity.base.edit.panel.base.PropertyPanelBase;
import soars.application.visualshell.object.entity.base.edit.panel.variable.VariableTable;
import soars.application.visualshell.object.entity.base.edit.panel.variable.panel.set_and_list.base.panel.VariablePanel;

/**
 * @author kurata
 *
 */
public class ListPanel extends VariablePanel {

	/**
	 * @param entityBase
	 * @param propertyPanelBaseMap
	 * @param variableTable
	 * @param color
	 * @param owner
	 * @param parent
	 */
	public ListPanel(EntityBase entityBase, Map<String, PropertyPanelBase> propertyPanelBaseMap, VariableTable variableTable, Color color, Frame owner, Component parent) {
		super("list", entityBase, propertyPanelBaseMap, variableTable, color, owner, parent);
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.entity.base.edit.panel.variable.panel.base.panel.VariablePanelBase#get_name_label_text()
	 */
	@Override
	protected String get_name_label_text() {
		return ResourceManager.get_instance().get( "edit.object.dialog.name");
	}
}
