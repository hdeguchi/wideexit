/**
 * 
 */
package soars.application.visualshell.object.gis.edit.panel.simple;

import java.awt.Component;
import java.awt.Frame;
import java.util.Map;

import javax.swing.table.DefaultTableColumnModel;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableColumn;

import soars.application.visualshell.main.ResourceManager;
import soars.application.visualshell.object.gis.edit.panel.base.GisPropertyPanelBase;
import soars.application.visualshell.object.gis.edit.panel.base.GisVariableTableBase;
import soars.application.visualshell.object.gis.object.base.GisObjectBase;
import soars.application.visualshell.object.gis.object.keyword.GisKeywordObject;
import soars.application.visualshell.object.gis.object.number.GisNumberObject;
import soars.application.visualshell.object.gis.object.spot.GisSpotVariableObject;
import soars.application.visualshell.object.gis.object.time.GisTimeVariableObject;
import soars.common.utility.swing.table.base.StandardTableHeaderRenderer;
import soars.common.utility.tool.sort.StringNumberComparator;

/**
 * @author kurata
 *
 */
public class GisSimpleVariableTable extends GisVariableTableBase {

	/**
	 * @param gisPropertyPanelBaseMap
	 * @param gisPropertyPanelBase
	 * @param owner
	 * @param parent
	 */
	public GisSimpleVariableTable(Map<String, GisPropertyPanelBase> gisPropertyPanelBaseMap, GisPropertyPanelBase gisPropertyPanelBase, Frame owner, Component parent) {
		super("simple variable", gisPropertyPanelBaseMap, gisPropertyPanelBase, 5, owner, parent);
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.gis.edit.panel.base.GisVariableTableBase#setup()
	 */
	@Override
	public boolean setup() {
		if ( !super.setup())
			return false;


		setAutoResizeMode( AUTO_RESIZE_OFF);



		JTableHeader tableHeader = getTableHeader();
		StandardTableHeaderRenderer standardTableHeaderRenderer = new StandardTableHeaderRenderer();

		tableHeader.setDefaultRenderer( standardTableHeaderRenderer);

		DefaultTableModel defaultTableModel = ( DefaultTableModel)getModel();
		defaultTableModel.setColumnCount( 5);

		DefaultTableColumnModel defaultTableColumnModel = ( DefaultTableColumnModel)getColumnModel();
		defaultTableColumnModel.getColumn( 0).setHeaderValue(
			ResourceManager.get_instance().get( "edit.object.dialog.simple.variable.table.header.kind"));
		defaultTableColumnModel.getColumn( 1).setHeaderValue(
			ResourceManager.get_instance().get( "edit.object.dialog.simple.variable.table.header.name"));
		defaultTableColumnModel.getColumn( 2).setHeaderValue(
			ResourceManager.get_instance().get( "edit.object.dialog.simple.variable.table.header.initial.value"));
		defaultTableColumnModel.getColumn( 3).setHeaderValue(
			ResourceManager.get_instance().get( "edit.object.dialog.simple.variable.table.header.type"));
		defaultTableColumnModel.getColumn( 4).setHeaderValue(
			ResourceManager.get_instance().get( "edit.object.dialog.simple.variable.table.header.comment"));

		defaultTableColumnModel.getColumn( 0).setPreferredWidth( 200);
		defaultTableColumnModel.getColumn( 1).setPreferredWidth( 200);
		defaultTableColumnModel.getColumn( 2).setPreferredWidth( 200);
		defaultTableColumnModel.getColumn( 3).setPreferredWidth( 100);
		defaultTableColumnModel.getColumn( 4).setPreferredWidth( 2000);

		for ( int i = 0; i < 5; ++i) {
			TableColumn tableColumn = defaultTableColumnModel.getColumn( i);
			tableColumn.setCellRenderer( new GisSimpleVariableTableRowRenderer());
		}

		return true;
	}

	/**
	 * @param gisObjectBase0
	 * @param gisObjectBase1
	 * @return
	 */
	protected int compare(GisObjectBase gisObjectBase0, GisObjectBase gisObjectBase1) {
		if ( ( gisObjectBase0 instanceof GisKeywordObject && gisObjectBase1 instanceof GisNumberObject)
			|| ( gisObjectBase0 instanceof GisKeywordObject && gisObjectBase1 instanceof GisTimeVariableObject)
			|| ( gisObjectBase0 instanceof GisKeywordObject && gisObjectBase1 instanceof GisSpotVariableObject)
			|| ( gisObjectBase0 instanceof GisNumberObject && gisObjectBase1 instanceof GisTimeVariableObject)
			|| ( gisObjectBase0 instanceof GisNumberObject && gisObjectBase1 instanceof GisSpotVariableObject)
			|| ( gisObjectBase0 instanceof GisTimeVariableObject && gisObjectBase1 instanceof GisSpotVariableObject))
			return -1;
		else if ( ( gisObjectBase0 instanceof GisNumberObject && gisObjectBase1 instanceof GisKeywordObject)
			|| ( gisObjectBase0 instanceof GisTimeVariableObject && gisObjectBase1 instanceof GisKeywordObject)
			|| ( gisObjectBase0 instanceof GisTimeVariableObject && gisObjectBase1 instanceof GisNumberObject)
			|| ( gisObjectBase0 instanceof GisSpotVariableObject && gisObjectBase1 instanceof GisKeywordObject)
			|| ( gisObjectBase0 instanceof GisSpotVariableObject && gisObjectBase1 instanceof GisNumberObject)
			|| ( gisObjectBase0 instanceof GisSpotVariableObject && gisObjectBase1 instanceof GisTimeVariableObject))
			return 1;
		else
			return StringNumberComparator.compareTo( gisObjectBase0._name, gisObjectBase1._name);
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.gis.edit.panel.base.GisVariableTableBase#can_remove_selected_objects(soars.application.visualshell.object.gis.object.base.GisObjectBase)
	 */
	@Override
	protected boolean can_remove_selected_objects(GisObjectBase gisObjectBase) {
		if ( _gisPropertyPanelBaseMap.get( "variable").contains( gisObjectBase))
			return false;

		return super.can_remove_selected_objects(gisObjectBase);
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.gis.edit.panel.base.GisVariableTableBase#on_remove()
	 */
	@Override
	protected void on_remove() {
		_gisPropertyPanelBaseMap.get( "variable").update();
	}
}
