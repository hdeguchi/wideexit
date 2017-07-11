/**
 * 
 */
package soars.application.visualshell.object.entity.base.edit.panel.variable.panel.set_and_list.base.panel.initial;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.util.Map;

import javax.swing.JPanel;

import soars.application.visualshell.common.selector.ObjectSelector;
import soars.application.visualshell.object.entity.base.EntityBase;
import soars.application.visualshell.object.entity.base.edit.panel.base.PropertyPanelBase;
import soars.application.visualshell.object.entity.base.edit.panel.variable.panel.set_and_list.base.panel.initial.base.VariableInitialValuePanel;
import soars.application.visualshell.object.entity.base.edit.panel.variable.panel.set_and_list.base.panel.table.VariableInitialValueTable;
import soars.application.visualshell.object.entity.base.object.variable.VariableInitialValue;
import soars.common.utility.swing.combo.ComboBox;

/**
 * @author kurata
 *
 */
public class VariableInitialValueSpotPanel extends VariableInitialValuePanel {

	/**
	 * 
	 */
	private ObjectSelector _spotSelector = null;

	/**
	 * @param color
	 * @param entityBase
	 * @param propertyPanelBaseMap
	 * @param typeComboBox
	 * @param variableInitialValueTable
	 */
	public VariableInitialValueSpotPanel(Color color, EntityBase entityBase, Map<String, PropertyPanelBase> propertyPanelBaseMap, ComboBox typeComboBox, VariableInitialValueTable variableInitialValueTable) {
		super(color, entityBase, propertyPanelBaseMap, typeComboBox, variableInitialValueTable);
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.entity.base.edit.panel.variable.panel.base.panel.initial.base.VariableInitialValuePanel#setEnabled(boolean)
	 */
	@Override
	public void setEnabled(boolean enabled) {
		_spotSelector.setEnabled( enabled);
		super.setEnabled(enabled);
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.entity.base.edit.panel.variable.panel.base.panel.initial.base.VariableInitialValuePanel#create(javax.swing.JPanel)
	 */
	@Override
	protected void create(JPanel parent) {
		_spotSelector = new ObjectSelector( "spot", false, 160, 40, _color, true, null);
		_spotSelector.selectFirstItem();
		parent.add( _spotSelector);
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.entity.base.edit.panel.variable.panel.base.panel.initial.base.VariableInitialValuePanel#set(java.lang.String)
	 */
	@Override
	public void set(String value) {
		_spotSelector.set( value);
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.entity.base.edit.panel.variable.panel.base.panel.initial.base.VariableInitialValuePanel#on_append(java.awt.event.ActionEvent)
	 */
	@Override
	protected void on_append(ActionEvent actionEvent) {
		String value = _spotSelector.get();
		if ( null == value || value.equals( ""))
			return;

		VariableInitialValue variableInitialValue = new VariableInitialValue( "spot", value);
		_initialValueTableBase.append( new VariableInitialValue[] { variableInitialValue, variableInitialValue});
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.entity.base.edit.panel.variable.panel.base.panel.initial.base.VariableInitialValuePanel#on_insert(java.awt.event.ActionEvent)
	 */
	@Override
	protected void on_insert(ActionEvent actionEvent) {
		if ( 1 != _initialValueTableBase.getSelectedRowCount())
			return;

		String value = _spotSelector.get();
		if ( null == value || value.equals( ""))
			return;

		VariableInitialValue variableInitialValue = new VariableInitialValue( "spot", value);
		_initialValueTableBase.insert( new VariableInitialValue[] { variableInitialValue, variableInitialValue});
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.entity.base.edit.panel.variable.panel.base.panel.initial.base.VariableInitialValuePanel#on_update(java.awt.event.ActionEvent)
	 */
	@Override
	protected void on_update(ActionEvent actionEvent) {
		if ( 1 != _initialValueTableBase.getSelectedRowCount())
			return;

		String value = _spotSelector.get();
		if ( null == value || value.equals( ""))
			return;

		VariableInitialValue variableInitialValue = new VariableInitialValue( "spot", value);
		_initialValueTableBase.update( new VariableInitialValue[] { variableInitialValue, variableInitialValue});
	}
}
