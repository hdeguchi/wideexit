/**
 * 
 */
package soars.application.visualshell.object.gis.edit.panel.variable.panel.set_and_list.base.panel.initial;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.util.Map;

import javax.swing.JPanel;

import soars.application.visualshell.common.selector.ObjectSelector;
import soars.application.visualshell.object.gis.GisDataManager;
import soars.application.visualshell.object.gis.edit.panel.base.GisPropertyPanelBase;
import soars.application.visualshell.object.gis.edit.panel.variable.panel.set_and_list.base.panel.initial.base.GisVariableInitialValuePanel;
import soars.application.visualshell.object.gis.edit.panel.variable.panel.set_and_list.base.panel.table.GisVariableInitialValueTable;
import soars.application.visualshell.object.gis.object.variable.GisVariableInitialValue;
import soars.common.utility.swing.combo.ComboBox;

/**
 * @author kurata
 *
 */
public class GisVariableInitialValueSpotPanel extends GisVariableInitialValuePanel {

	/**
	 * 
	 */
	private ObjectSelector _spotSelector = null;

	/**
	 * @param color
	 * @param gisDataManager
	 * @param gisPropertyPanelBaseMap
	 * @param typeComboBox
	 * @param gisVariableInitialValueTable
	 */
	public GisVariableInitialValueSpotPanel(Color color, GisDataManager gisDataManager, Map<String, GisPropertyPanelBase> gisPropertyPanelBaseMap, ComboBox typeComboBox, GisVariableInitialValueTable gisVariableInitialValueTable) {
		super(color, gisDataManager, gisPropertyPanelBaseMap, typeComboBox, gisVariableInitialValueTable);
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.gis.edit.panel.variable.panel.set_and_list.base.panel.initial.base.GisVariableInitialValuePanel#setEnabled(boolean)
	 */
	@Override
	public void setEnabled(boolean enabled) {
		_spotSelector.setEnabled( enabled);
		super.setEnabled(enabled);
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.gis.edit.panel.variable.panel.set_and_list.base.panel.initial.base.GisVariableInitialValuePanel#create(javax.swing.JPanel)
	 */
	@Override
	protected void create(JPanel parent) {
		_spotSelector = new ObjectSelector( "spot", false, 160, 40, _color, true, null);
		_spotSelector.selectFirstItem();
		parent.add( _spotSelector);
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.gis.edit.panel.variable.panel.set_and_list.base.panel.initial.base.GisVariableInitialValuePanel#set(soars.application.visualshell.object.gis.object.variable.GisVariableInitialValue)
	 */
	@Override
	public void set(GisVariableInitialValue gisVariableInitialValue) {
		_spotSelector.set( gisVariableInitialValue._value);
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.gis.edit.panel.variable.panel.set_and_list.base.panel.initial.base.GisVariableInitialValuePanel#on_append(java.awt.event.ActionEvent)
	 */
	@Override
	protected void on_append(ActionEvent actionEvent) {
		String value = _spotSelector.get();
		if ( null == value || value.equals( ""))
			return;

		GisVariableInitialValue gisVariableInitialValue = new GisVariableInitialValue( "spot", "", value);
		_gisInitialValueTableBase.append( new GisVariableInitialValue[] { gisVariableInitialValue, gisVariableInitialValue});
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.gis.edit.panel.variable.panel.set_and_list.base.panel.initial.base.GisVariableInitialValuePanel#on_insert(java.awt.event.ActionEvent)
	 */
	@Override
	protected void on_insert(ActionEvent actionEvent) {
		if ( 1 != _gisInitialValueTableBase.getSelectedRowCount())
			return;

		String value = _spotSelector.get();
		if ( null == value || value.equals( ""))
			return;

		GisVariableInitialValue gisVariableInitialValue = new GisVariableInitialValue( "spot", "", value);
		_gisInitialValueTableBase.insert( new GisVariableInitialValue[] { gisVariableInitialValue, gisVariableInitialValue});
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.gis.edit.panel.variable.panel.set_and_list.base.panel.initial.base.GisVariableInitialValuePanel#on_update(java.awt.event.ActionEvent)
	 */
	@Override
	protected void on_update(ActionEvent actionEvent) {
		if ( 1 != _gisInitialValueTableBase.getSelectedRowCount())
			return;

		String value = _spotSelector.get();
		if ( null == value || value.equals( ""))
			return;

		GisVariableInitialValue gisVariableInitialValue = new GisVariableInitialValue( "spot", "", value);
		_gisInitialValueTableBase.update( new GisVariableInitialValue[] { gisVariableInitialValue, gisVariableInitialValue});
	}
}
