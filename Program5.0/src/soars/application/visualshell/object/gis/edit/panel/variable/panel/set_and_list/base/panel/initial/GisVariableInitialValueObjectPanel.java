/**
 * 
 */
package soars.application.visualshell.object.gis.edit.panel.variable.panel.set_and_list.base.panel.initial;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.util.Map;

import javax.swing.JPanel;

import soars.application.visualshell.common.tool.CommonTool;
import soars.application.visualshell.object.gis.GisDataManager;
import soars.application.visualshell.object.gis.edit.panel.base.GisPropertyPanelBase;
import soars.application.visualshell.object.gis.edit.panel.variable.panel.base.panel.table.GisInitialValueTableBase;
import soars.application.visualshell.object.gis.edit.panel.variable.panel.set_and_list.base.panel.initial.base.GisVariableInitialValuePanel;
import soars.application.visualshell.object.gis.edit.panel.variable.panel.set_and_list.base.panel.table.GisVariableInitialValueTable;
import soars.application.visualshell.object.gis.object.variable.GisVariableInitialValue;
import soars.common.utility.swing.combo.ComboBox;
import soars.common.utility.swing.combo.CommonComboBoxRenderer;

/**
 * @author kurata
 *
 */
public class GisVariableInitialValueObjectPanel extends GisVariableInitialValuePanel {

	/**
	 * 
	 */
	private ComboBox _comboBox = null;

	/**
	 * @param color
	 * @param gisDataManager
	 * @param gisPropertyPanelBaseMap
	 * @param typeComboBox
	 * @param gisVariableInitialValueTable
	 */
	public GisVariableInitialValueObjectPanel(Color color, GisDataManager gisDataManager, Map<String, GisPropertyPanelBase> gisPropertyPanelBaseMap, ComboBox typeComboBox, GisVariableInitialValueTable gisVariableInitialValueTable) {
		super(color, gisDataManager, gisPropertyPanelBaseMap, typeComboBox, gisVariableInitialValueTable);
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.gis.edit.panel.variable.panel.set_and_list.base.panel.initial.base.GisVariableInitialValuePanel#setEnabled(boolean)
	 */
	@Override
	public void setEnabled(boolean enabled) {
		_comboBox.setEnabled( enabled);
		super.setEnabled(enabled);
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.gis.edit.panel.variable.panel.set_and_list.base.panel.initial.base.GisVariableInitialValuePanel#create(javax.swing.JPanel)
	 */
	@Override
	protected void create(JPanel parent) {
		_comboBox = new ComboBox( _color, false, new CommonComboBoxRenderer( _color, false));
		parent.add( _comboBox);
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.gis.edit.panel.variable.panel.set_and_list.base.panel.initial.base.GisVariableInitialValuePanel#update(java.lang.String)
	 */
	@Override
	public void update(String item) {
		String type = GisInitialValueTableBase.get_typeMap().get( item);
		if ( null == type)
			return;

		if ( type.equals( "collection") || type.equals( "list") || type.equals( "map"))
			CommonTool.update( _comboBox, ( String[])_gisPropertyPanelBaseMap.get( "variable").get( type));
		else
			CommonTool.update( _comboBox, _gisPropertyPanelBaseMap.get( "simple variable").get( type));
//		if ( type.equals( "collection") || type.equals( "list") || type.equals( "map")
//			|| ( type.equals( "exchange algebra") && Environment.get_instance().is_exchange_algebra_enable()))
//			CommonTool.update( _comboBox, ( String[])_gisPropertyPanelBaseMap.get( "variable").get( type));
//		else if ( type.equals( "class variable"))
//			CommonTool.update( _comboBox, _gisPropertyPanelBaseMap.get( "class variable").get());
//		else if ( type.equals( "file"))
//			CommonTool.update( _comboBox, _gisPropertyPanelBaseMap.get( "file").get());
//		else {
//			if ( type.equals( "role variable") && !( _entityBase instanceof AgentObject))
//				return;
//
//			CommonTool.update( _comboBox, _gisPropertyPanelBaseMap.get( "simple variable").get( type));
//		}
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.gis.edit.panel.variable.panel.set_and_list.base.panel.initial.base.GisVariableInitialValuePanel#set(soars.application.visualshell.object.gis.object.variable.GisVariableInitialValue)
	 */
	@Override
	public void set(GisVariableInitialValue gisVariableInitialValue) {
		_comboBox.setSelectedItem( gisVariableInitialValue._value);
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.gis.edit.panel.variable.panel.set_and_list.base.panel.initial.base.GisVariableInitialValuePanel#on_append(java.awt.event.ActionEvent)
	 */
	@Override
	protected void on_append(ActionEvent actionEvent) {
		String item = ( String)_typeComboBox.getSelectedItem();
		if ( null == item)
			return;

		String type = GisVariableInitialValueTable.get_typeMap().get( item);
		if ( null == type)
			return;

		String value = ( String)_comboBox.getSelectedItem();
		if ( null == value)
			return;

		GisVariableInitialValue gisVariableInitialValue = new GisVariableInitialValue( type, "", value);
		_gisInitialValueTableBase.append( new GisVariableInitialValue[] { gisVariableInitialValue, gisVariableInitialValue});
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.gis.edit.panel.variable.panel.set_and_list.base.panel.initial.base.GisVariableInitialValuePanel#on_insert(java.awt.event.ActionEvent)
	 */
	@Override
	protected void on_insert(ActionEvent actionEvent) {
		if ( 1 != _gisInitialValueTableBase.getSelectedRowCount())
			return;

		String item = ( String)_typeComboBox.getSelectedItem();
		if ( null == item)
			return;

		String type = GisVariableInitialValueTable.get_typeMap().get( item);
		if ( null == type)
			return;

		String value = ( String)_comboBox.getSelectedItem();
		if ( null == value)
			return;

		GisVariableInitialValue gisVariableInitialValue = new GisVariableInitialValue( type, "", value);
		_gisInitialValueTableBase.insert( new GisVariableInitialValue[] { gisVariableInitialValue, gisVariableInitialValue});
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.gis.edit.panel.variable.panel.set_and_list.base.panel.initial.base.GisVariableInitialValuePanel#on_update(java.awt.event.ActionEvent)
	 */
	@Override
	protected void on_update(ActionEvent actionEvent) {
		if ( 1 != _gisInitialValueTableBase.getSelectedRowCount())
			return;

		String item = ( String)_typeComboBox.getSelectedItem();
		if ( null == item)
			return;

		String type = GisVariableInitialValueTable.get_typeMap().get( item);
		if ( null == type)
			return;

		String value = ( String)_comboBox.getSelectedItem();
		if ( null == value)
			return;

		GisVariableInitialValue gisVariableInitialValue = new GisVariableInitialValue( type, "", value);
		_gisInitialValueTableBase.update( new GisVariableInitialValue[] { gisVariableInitialValue, gisVariableInitialValue});
	}
}
