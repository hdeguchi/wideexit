/**
 * 
 */
package soars.application.visualshell.object.gis.edit.panel.variable.panel.set_and_list.list;

import java.awt.Color;
import java.awt.Component;
import java.awt.Frame;
import java.util.Map;

import soars.application.visualshell.main.ResourceManager;
import soars.application.visualshell.object.gis.GisDataManager;
import soars.application.visualshell.object.gis.edit.panel.base.GisPropertyPanelBase;
import soars.application.visualshell.object.gis.edit.panel.base.GisVariableTableBase;
import soars.application.visualshell.object.gis.edit.panel.variable.panel.set_and_list.base.panel.GisVariablePanel;

/**
 * @author kurata
 *
 */
public class GisListPanel extends GisVariablePanel {

	/**
	 * @param gisDataManager
	 * @param gisPropertyPanelBaseMap
	 * @param gisVariableTableBase
	 * @param color
	 * @param owner
	 * @param parent
	 */
	public GisListPanel(GisDataManager gisDataManager, Map<String, GisPropertyPanelBase> gisPropertyPanelBaseMap, GisVariableTableBase gisVariableTableBase, Color color, Frame owner, Component parent) {
		super("list", gisDataManager, gisPropertyPanelBaseMap, gisVariableTableBase, color, owner, parent);
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.gis.edit.panel.base.GisPanelBase#get_name_label_text()
	 */
	@Override
	protected String get_name_label_text() {
		return ResourceManager.get_instance().get( "edit.object.dialog.name");
	}
}
