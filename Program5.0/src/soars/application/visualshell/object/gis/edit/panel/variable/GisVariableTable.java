/*
 * 2005/05/27
 */
package soars.application.visualshell.object.gis.edit.panel.variable;

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
import soars.application.visualshell.object.gis.object.map.GisMapObject;
import soars.application.visualshell.object.gis.object.variable.GisVariableObject;
import soars.common.utility.swing.table.base.StandardTableHeaderRenderer;
import soars.common.utility.tool.sort.StringNumberComparator;

/**
 * @author kurata
 */
public class GisVariableTable extends GisVariableTableBase {

	/**
	 * @param gisPropertyPanelBaseMap
	 * @param gisPropertyPanelBase
	 * @param owner
	 * @param parent
	 */
	public GisVariableTable(Map<String, GisPropertyPanelBase> gisPropertyPanelBaseMap, GisPropertyPanelBase gisPropertyPanelBase, Frame owner, Component parent) {
		super("variable", gisPropertyPanelBaseMap, gisPropertyPanelBase, 4, owner, parent);
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
		defaultTableModel.setColumnCount( _columns);

		DefaultTableColumnModel defaultTableColumnModel = ( DefaultTableColumnModel)getColumnModel();
		defaultTableColumnModel.getColumn( 0).setHeaderValue(
			ResourceManager.get_instance().get( "edit.object.dialog.variable.table.header.kind"));
		defaultTableColumnModel.getColumn( 1).setHeaderValue(
			ResourceManager.get_instance().get( "edit.object.dialog.variable.table.header.name"));
		defaultTableColumnModel.getColumn( 2).setHeaderValue(
			ResourceManager.get_instance().get( "edit.object.dialog.variable.table.header.initial.value"));
		defaultTableColumnModel.getColumn( 3).setHeaderValue(
			ResourceManager.get_instance().get( "edit.object.dialog.variable.table.header.comment"));

		defaultTableColumnModel.getColumn( 0).setPreferredWidth( 200);
		defaultTableColumnModel.getColumn( 1).setPreferredWidth( 200);
		defaultTableColumnModel.getColumn( 2).setPreferredWidth( 200);
		defaultTableColumnModel.getColumn( 3).setPreferredWidth( 2000);

		for ( int i = 0; i < _columns; ++i) {
			TableColumn tableColumn = defaultTableColumnModel.getColumn( i);
			tableColumn.setCellRenderer( new GisVariableTableRowRenderer());
		}

		setup_popup_menu();

		return true;
	}

	/**
	 * @param gisObjectBase0
	 * @param gisObjectBase1
	 * @return
	 */
	protected int compare(GisObjectBase gisObjectBase0, GisObjectBase gisObjectBase1) {
		if ( ( gisObjectBase0 instanceof GisVariableObject && ( ( GisVariableObject)gisObjectBase0)._kind.equals( "collection")
				&& gisObjectBase1 instanceof GisVariableObject && ( ( GisVariableObject)gisObjectBase1)._kind.equals( "list"))
			|| ( gisObjectBase0 instanceof GisVariableObject && gisObjectBase1 instanceof GisMapObject))
//			|| ( gisObjectBase0 instanceof GisVariableObject && gisObjectBase1 instanceof GisExchangeAlgebraObject)
//			|| ( gisObjectBase0 instanceof GisMapObject && gisObjectBase1 instanceof GisExchangeAlgebraObject))
			return -1;
		else if ( ( gisObjectBase0 instanceof GisVariableObject && ( ( GisVariableObject)gisObjectBase0)._kind.equals( "list")
				&& gisObjectBase1 instanceof GisVariableObject && ( ( GisVariableObject)gisObjectBase1)._kind.equals( "collection"))
			|| ( gisObjectBase0 instanceof GisMapObject && gisObjectBase1 instanceof GisVariableObject))
//			|| ( gisObjectBase0 instanceof GisExchangeAlgebraObject && gisObjectBase1 instanceof GisVariableObject)
//			|| ( gisObjectBase0 instanceof GisExchangeAlgebraObject && gisObjectBase1 instanceof GisMapObject))
			return 1;
		else
			return StringNumberComparator.compareTo( gisObjectBase0._name, gisObjectBase1._name);
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.gis.edit.panel.base.GisVariableTableBase#can_remove_selected_objects(soars.application.visualshell.object.gis.object.base.GisObjectBase)
	 */
	@Override
	protected boolean can_remove_selected_objects(GisObjectBase gisObjectBase) {
		if ( contains( gisObjectBase))
			return false;

		return super.can_remove_selected_objects( gisObjectBase);
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.gis.edit.panel.base.GisVariableTableBase#on_remove()
	 */
	@Override
	protected void on_remove() {
		_gisPropertyPanelBase.update();
	}

//	/* (non-Javadoc)
//	 * @see soars.application.visualshell.object.gis.edit.panel.base.GisVariableTableBase#on_ok()
//	 */
//	@Override
//	public void on_ok() {
//		_entityBase._objectMapMap.get( "collection").clear();
//		_entityBase._objectMapMap.get( "list").clear();
//		_entityBase._objectMapMap.get( "map").clear();
//		_entityBase._objectMapMap.get( "exchange algebra").clear();
//
//		DefaultTableModel defaultTableModel = ( DefaultTableModel)getModel();
//		for ( int i = 0; i < defaultTableModel.getRowCount(); ++i) {
//			GisObjectBase objectBase = ( GisObjectBase)defaultTableModel.getValueAt( i, 0);
//			if ( null == objectBase)
//				continue;
//
//			if ( objectBase instanceof GisVariableObject && objectBase._kind.equals( "collection"))
//				_entityBase._objectMapMap.get( "collection").put( objectBase._name, objectBase);
//			else if ( objectBase instanceof GisVariableObject && objectBase._kind.equals( "list"))
//				_entityBase._objectMapMap.get( "list").put( objectBase._name, objectBase);
//			else if ( objectBase instanceof GisMapObject)
//				_entityBase._objectMapMap.get( "map").put( objectBase._name, objectBase);
////			else if ( objectBase instanceof GisExchangeAlgebraObject)
////				_entityBase._objectMapMap.get( "exchange algebra").put( objectBase._name, objectBase);
//		}
//	}
}
