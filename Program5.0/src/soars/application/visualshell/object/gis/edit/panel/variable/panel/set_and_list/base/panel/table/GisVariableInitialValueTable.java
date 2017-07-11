/**
 * 
 */
package soars.application.visualshell.object.gis.edit.panel.variable.panel.set_and_list.base.panel.table;

import java.awt.Color;
import java.awt.Component;
import java.awt.Frame;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.swing.table.DefaultTableColumnModel;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;

import soars.application.visualshell.main.ResourceManager;
import soars.application.visualshell.object.gis.GisDataManager;
import soars.application.visualshell.object.gis.edit.panel.base.GisPropertyPanelBase;
import soars.application.visualshell.object.gis.edit.panel.variable.panel.base.panel.GisVariablePanelBase;
import soars.application.visualshell.object.gis.edit.panel.variable.panel.base.panel.table.GisInitialValueTable;
import soars.application.visualshell.object.gis.edit.panel.variable.panel.base.panel.table.GisInitialValueTableBase;
import soars.application.visualshell.object.gis.object.base.GisInitialValueBase;
import soars.application.visualshell.object.gis.object.base.GisObjectBase;
import soars.application.visualshell.object.gis.object.variable.GisVariableInitialValue;
import soars.application.visualshell.object.gis.object.variable.GisVariableObject;
import soars.application.visualshell.observer.WarningDlg1;
import soars.common.soars.warning.WarningManager;
import soars.common.utility.tool.common.Tool;

/**
 * @author kurata
 *
 */
public class GisVariableInitialValueTable extends GisInitialValueTable {

	/**
	 * @param color
	 * @param gisDataManager
	 * @param gisPropertyPanelBaseMap
	 * @param gisPropertyPanelBase
	 * @param owner
	 * @param parent
	 */
	public GisVariableInitialValueTable(Color color, GisDataManager gisDataManager, Map<String, GisPropertyPanelBase> gisPropertyPanelBaseMap, GisVariablePanelBase gisPropertyPanelBase, Frame owner, Component parent) {
		super("variable", color, gisDataManager, gisPropertyPanelBaseMap, gisPropertyPanelBase, owner, parent);
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.gis.edit.panel.variable.panel.base.panel.table.GisInitialValueTableBase#setup(soars.application.visualshell.object.gis.edit.panel.variable.panel.base.panel.table.GisInitialValueTableBase)
	 */
	@Override
	public boolean setup(GisInitialValueTableBase gisInitialValueTableBase) {
		if ( !super.setup(gisInitialValueTableBase))
			return false;

		DefaultTableModel defaultTableModel = ( DefaultTableModel)getModel();
		defaultTableModel.setColumnCount( 2);

		DefaultTableColumnModel defaultTableColumnModel = ( DefaultTableColumnModel)getColumnModel();
		defaultTableColumnModel.getColumn( 0).setHeaderValue( ResourceManager.get_instance().get( "edit.object.dialog.variable.initial.value.table.header.kind"));
		defaultTableColumnModel.getColumn( 1).setHeaderValue( ResourceManager.get_instance().get( "edit.object.dialog.variable.initial.value.table.header.initial.value"));

		defaultTableColumnModel.getColumn( 0).setPreferredWidth( 130);
		defaultTableColumnModel.getColumn( 1).setPreferredWidth( 2000);

		for ( int i = 0; i < 2; ++i) {
			TableColumn tableColumn = defaultTableColumnModel.getColumn( i);
			tableColumn.setCellRenderer( new GisVariableInitialValueTableRowRenderer( _color));
		}

		return true;
	}

	/**
	 * @param gisVariableObject
	 */
	public void update(GisVariableObject gisVariableObject) {
		cleanup();
		_gisInitialValueTableBase.cleanup();
		if ( null == gisVariableObject)
			return;

		for ( GisVariableInitialValue gviv:gisVariableObject._gisVariableInitialValues) {
			GisVariableInitialValue gisVariableInitialValue = new GisVariableInitialValue( gviv);
			append( new GisVariableInitialValue[] { gisVariableInitialValue, gisVariableInitialValue});
		}

		if ( 0 < getRowCount()) {
			select( 0);
			_gisVariablePanelBase.changeSelection( getValueAt( 0, 0));
			scroll( 0);
		}
	}

	/**
	 * @param gisVariableObject
	 */
	public void get(GisVariableObject gisVariableObject) {
		gisVariableObject._gisVariableInitialValues.clear();
		for ( int i = 0; i < getRowCount(); ++i)
			gisVariableObject._gisVariableInitialValues.add( ( GisVariableInitialValue)getValueAt( i, 0));
	}

	/**
	 * @param gisObjectBase
	 * @return
	 */
	public boolean contains(GisObjectBase gisObjectBase) {
		for ( int i = 0; i < getRowCount(); ++i) {
			GisVariableInitialValue gisVariableInitialValue = ( GisVariableInitialValue)getValueAt( i, 0);
			if ( gisVariableInitialValue.contains( gisObjectBase))
				return true;
		}
		return false;
	}

	/**
	 * @param type
	 * @param name
	 * @param newName
	 * @return
	 */
	public boolean update_object_name(String type, String name, String newName) {
		boolean result = false;
		for ( int i = 0; i < getRowCount(); ++i) {
			GisVariableInitialValue gisVariableInitialValue = ( GisVariableInitialValue)getValueAt( i, 0);
			if ( gisVariableInitialValue.update_object_name( type, name, newName))
				result = true;
		}
		return result;
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.gis.edit.panel.variable.panel.base.panel.table.GisInitialValueTableBase#paste(int[])
	 */
	@Override
	public void paste(int[] rows) {
		if ( __gisInitialValueBasesMap.get( _kind).isEmpty())
			return;

		if ( 0 < getRowCount() && 1 != rows.length)
			return;

		WarningManager.get_instance().cleanup();

		List<GisVariableInitialValue> gisVariableInitialValues = new ArrayList<GisVariableInitialValue>();
		if ( 0 == getRowCount()) {
			for ( int i = 0; i < __gisInitialValueBasesMap.get( _kind).size(); ++i) {
				GisVariableInitialValue gisVariableInitialValue = ( GisVariableInitialValue)GisInitialValueBase.create( __gisInitialValueBasesMap.get( _kind).get( i));
				if ( !gisVariableInitialValue.can_paste( _gisPropertyPanelBaseMap, null, null/*, _entityBase*/)) {
					String[] message = new String[] {
						GisPropertyPanelBase.get_nameMap().get( gisVariableInitialValue._type),
						"name = " + gisVariableInitialValue._value
					};

					WarningManager.get_instance().add( message);
					continue;
				}

				append( new GisVariableInitialValue[] { gisVariableInitialValue, gisVariableInitialValue});
				gisVariableInitialValues.add( gisVariableInitialValue);
			}
		} else {
			// 自分自身のを含む場合はとりあえず放置しておく
			for ( int i = 0; i < __gisInitialValueBasesMap.get( _kind).size(); ++i) {
				GisVariableInitialValue variableInitialValue = ( GisVariableInitialValue)GisInitialValueBase.create( __gisInitialValueBasesMap.get( _kind).get( i));
				if ( !variableInitialValue.can_paste( _gisPropertyPanelBaseMap, null, null/*, _entityBase*/)) {
					String[] message = new String[] {
							GisPropertyPanelBase.get_nameMap().get( variableInitialValue._type),
						"name = " + variableInitialValue._value
					};

					WarningManager.get_instance().add( message);
					continue;
				}

				int index = ( rows[ 0] + __gisInitialValueBasesMap.get( _kind).get( i)._row);
				if ( getRowCount() > index)
					insert( new GisVariableInitialValue[] { variableInitialValue, variableInitialValue}, index);
				else
					append( new GisVariableInitialValue[] { variableInitialValue, variableInitialValue});
				gisVariableInitialValues.add( variableInitialValue);
			}
		}

		List<Integer> list = new ArrayList<Integer>();
		for ( GisVariableInitialValue variableInitialValue:gisVariableInitialValues) {
			for ( int i = 0; i < getRowCount(); ++i) {
				if ( variableInitialValue == getValueAt( i, 0)) {
					list.add( i);
					break;
				}
			}
		}
		_gisInitialValueTableBase.select( Tool.get_array( list));
		changeSelection();

		if ( !WarningManager.get_instance().isEmpty()) {
			WarningDlg1 warningDlg1 = new WarningDlg1(
				_owner,
				ResourceManager.get_instance().get( "warning.dialog1.title"),
				ResourceManager.get_instance().get( "warning.dialog1.message6"),
				getParent());
			warningDlg1.do_modal();
		}
	}
}
